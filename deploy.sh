#!/bin/bash
set -e

# ============================================================
# deploy.sh — Build APK release WPS Tablet & publish via HTTP API
#
# Update di app dibaca dari backend (UpdateManager.java -> /api/update/tablet):
#   GET  /api/update/tablet/version           -> info versi
#   GET  /api/update/tablet/download/:file    -> unduh APK
#   POST /api/update/tablet/publish           -> dipakai skrip ini
#
# Usage:
#   ./deploy.sh "<changelog>" [options]
#
# Options:
#   --dev        Publish ke server dev/lokal (tes dulu)
#   --force      Paksa semua user update (forceUpdate: true)
#   --min <ver>  Set minVersion (default: versi sekarang, sebelum bump)
#   --minor      Naikkan MINOR (x.Y.0); default naikkan PATCH (x.y.Z)
#   --clean      ./gradlew clean sebelum build
#   --no-git     Jangan commit bump versi
#   --dry-run    Build + sign saja, tidak publish
#
# Alur yang disarankan:
#   1. ./deploy.sh "Fix X" --dev     <- tes di lokal
#   2. ./deploy.sh "Fix X"           <- production
# ============================================================

cd "$(dirname "$0")"

CHANGELOG="$1"
MODE="production"
FORCE_UPDATE="false"
MIN_VERSION_OVERRIDE=""
BUMP="patch"
DO_CLEAN=false
DO_GIT=true
DRY_RUN=false
ASSUME_YES=false

shift 1 2>/dev/null || true
while [[ $# -gt 0 ]]; do
  case "$1" in
    --dev)     MODE="dev" ;;
    --force)   FORCE_UPDATE="true" ;;
    --min)     MIN_VERSION_OVERRIDE="$2"; shift ;;
    --minor)   BUMP="minor" ;;
    --clean)   DO_CLEAN=true ;;
    --no-git)  DO_GIT=false ;;
    --dry-run) DRY_RUN=true ;;
    --yes|-y)  ASSUME_YES=true ;;
    *) echo "Unknown flag: $1"; exit 1 ;;
  esac
  shift
done

# --- Config (override via env) ---
BUILD_FILE="app/build.gradle.kts"
APK_SIGNED="app/build/outputs/apk/release/app-release.apk"
APK_UNSIGNED="app/build/outputs/apk/release/app-release-unsigned.apk"

APP_ID="tablet"
UPDATE_TOKEN="${UPDATE_TOKEN:-UTAMA-UPDATE-SECRET-123}"
PORT="${UPDATE_PORT:-5002}"
DEV_IP="${UPDATE_DEV_IP:-192.168.11.153}"
PROD_IP="${UPDATE_PROD_IP:-192.168.11.79}"

ANDROID_SDK="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-$HOME/AppData/Local/Android/Sdk}}"

if [ "$MODE" = "dev" ]; then
  API_BASE_URL="http://$DEV_IP:$PORT"
else
  API_BASE_URL="http://$PROD_IP:$PORT"
fi
PUBLISH_URL="$API_BASE_URL/api/update/$APP_ID/publish"

# --- Validasi ---
if [ -z "$CHANGELOG" ]; then
  cat <<'EOF'

Usage: ./deploy.sh "<changelog>" [--dev] [--force] [--min <ver>] [--minor] [--clean] [--no-git] [--dry-run]

Contoh:
  ./deploy.sh "Perbaikan menu Mapping"
  ./deploy.sh "Perbaikan menu Mapping" --dev
  ./deploy.sh "Rilis wajib" --force --min 1.1.70
  ./deploy.sh "Uji build" --dry-run

EOF
  exit 1
fi

if [ ! -f "keystore.properties" ]; then
  echo "ERROR: keystore.properties tidak ditemukan."
  echo "       Salin dari keystore.properties.example lalu isi kredensial keystore rilis."
  exit 1
fi

APKSIGNER=$(ls "$ANDROID_SDK"/build-tools/*/apksigner* 2>/dev/null | sort -V | tail -1 || true)

# --- Hitung versi berikutnya ---
CUR_NAME=$(grep -E '^\s*versionName\s*=' "$BUILD_FILE" | head -1 | sed -E 's/.*"([^"]+)".*/\1/')
CUR_CODE=$(grep -E '^\s*versionCode\s*=' "$BUILD_FILE" | head -1 | sed -E 's/[^0-9]//g')

MAJOR=$(echo "$CUR_NAME" | cut -d. -f1)
MINOR=$(echo "$CUR_NAME" | cut -d. -f2)
PATCH=$(echo "$CUR_NAME" | cut -d. -f3)

if [ "$BUMP" = "minor" ]; then
  NEXT_NAME="$MAJOR.$((MINOR + 1)).0"
else
  NEXT_NAME="$MAJOR.$MINOR.$((PATCH + 1))"
fi
NEXT_CODE=$((CUR_CODE + 1))
APK_NAME="wps-tablet-$NEXT_NAME.apk"
MIN_VERSION="${MIN_VERSION_OVERRIDE:-$CUR_NAME}"

echo ""
echo "=================================================="
echo " WPS Tablet — Deploy (via API)"
echo "=================================================="
echo " Mode         : $MODE"
echo " Server       : $API_BASE_URL"
echo " Versi        : $CUR_NAME (code $CUR_CODE)  ->  $NEXT_NAME (code $NEXT_CODE)"
echo " APK          : $APK_NAME"
echo " Min Version  : $MIN_VERSION"
echo " Force Update : $FORCE_UPDATE"
echo " Changelog    : $CHANGELOG"
echo " clean=$DO_CLEAN  git-commit=$DO_GIT  dry-run=$DRY_RUN"
echo "=================================================="
[ "$MODE" = "production" ] && echo " WARNING: PRODUCTION — update diterima semua user!" && echo ""
if [ "$ASSUME_YES" != "true" ]; then
  read -p "Lanjutkan? (y/N): " CONFIRM
  [[ "$CONFIRM" = "y" || "$CONFIRM" = "Y" ]] || { echo "Dibatalkan."; exit 0; }
fi

# --- [1/5] Bump versi ---
echo ""
echo "[1/5] Update versi di $BUILD_FILE ..."
sed -i -E "s/(^[[:space:]]*versionCode[[:space:]]*=[[:space:]]*)[0-9]+/\1$NEXT_CODE/" "$BUILD_FILE"
sed -i -E "s/(^[[:space:]]*versionName[[:space:]]*=[[:space:]]*\")[^\"]+(\")/\1$NEXT_NAME\2/" "$BUILD_FILE"
echo "      $CUR_NAME/$CUR_CODE  ->  $NEXT_NAME/$NEXT_CODE"

# --- [2/5] Build ---
echo ""
echo "[2/5] Build APK release ..."
[ "$DO_CLEAN" = "true" ] && ./gradlew clean
./gradlew :app:assembleRelease

# --- [3/5] Verifikasi APK ---
echo ""
echo "[3/5] Verifikasi APK ..."
if [ -f "$APK_SIGNED" ]; then
  OUT_APK="$APK_SIGNED"
elif [ -f "$APK_UNSIGNED" ]; then
  echo "ERROR: hanya ada APK unsigned. Cek keystore.properties."
  exit 1
else
  echo "ERROR: APK release tidak ditemukan di app/build/outputs/apk/release/"
  exit 1
fi
if [ -n "$APKSIGNER" ]; then
  "$APKSIGNER" verify "$OUT_APK" >/dev/null && echo "      Tanda tangan OK"
fi
echo "      $OUT_APK ($(du -h "$OUT_APK" | cut -f1))"

# --- [4/5] Commit bump versi ---
echo ""
if [ "$DO_GIT" = "true" ]; then
  echo "[4/5] Commit bump versi ..."
  git add "$BUILD_FILE"
  git commit -m "chore: release $NEXT_NAME (versionCode $NEXT_CODE)" \
    || echo "      (tidak ada perubahan untuk di-commit)"
  echo "      Ingat: 'git push' sendiri bila perlu."
else
  echo "[4/5] Lewati git commit (--no-git)"
fi

# --- [5/5] Publish ke API ---
echo ""
if [ "$DRY_RUN" = "true" ]; then
  echo "[5/5] --dry-run: tidak publish. APK siap di $OUT_APK"
  echo ""
  echo "Selesai (dry-run). Versi $NEXT_NAME sudah di-build & di-sign."
  exit 0
fi

echo "[5/5] Upload & publish ke $PUBLISH_URL ($MODE) ..."
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$PUBLISH_URL" \
  -H "x-update-token: $UPDATE_TOKEN" \
  -F "apk=@$OUT_APK;filename=$APK_NAME" \
  -F "latestVersion=$NEXT_NAME" \
  -F "minVersion=$MIN_VERSION" \
  -F "forceUpdate=$FORCE_UPDATE" \
  -F "changelog=$CHANGELOG")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "200" ] || [ "$HTTP_CODE" = "201" ]; then
  echo "      OK (HTTP $HTTP_CODE)"
  echo "      $BODY"
else
  echo "      ERROR: server membalas HTTP $HTTP_CODE"
  echo "      $BODY"
  exit 1
fi

echo ""
echo "=================================================="
if [ "$MODE" = "dev" ]; then
  echo " Dev deploy selesai. Versi $NEXT_NAME ada di server lokal."
  echo " Tes di device, lalu: ./deploy.sh \"$CHANGELOG\" (tanpa --dev) untuk production."
else
  echo " Deploy selesai. Versi $NEXT_NAME publish ke production."
fi
echo "=================================================="
echo ""
