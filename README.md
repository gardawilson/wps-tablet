# WPS Tablet

Aplikasi Android tablet (Java) untuk **manajemen produksi kayu** di lingkungan Utama Corp — mencakup alur Sawmill, Sawn Timber, S4S, Moulding, Finger Joint (FJ), Laminating, CrossCut, Sanding, dan Packing, ditambah modul Stock Opname, Penjualan, Planning Mesin, Mapping, SPK, Audit Trail, dan pelaporan.

Aplikasi terhubung **langsung ke SQL Server** melalui JDBC (driver jTDS) dan ke beberapa microservice HTTP internal (API utama, report service, device service).

---

## Ringkasan Teknis

| | |
|---|---|
| Bahasa | Java (Android) |
| Package | `com.example.myapplication` |
| minSdk / targetSdk / compileSdk | 29 / 34 / 34 |
| Orientasi | Tablet landscape |
| Build system | Gradle (Kotlin DSL) + version catalog `gradle/libs.versions.toml` |
| AGP | 8.5.2 |
| Database | SQL Server via JDBC langsung (`jdbc:jtds:sqlserver://...`) |
| Versi saat ini | `versionName 1.1.84` / `versionCode 12` |

### Library utama
- **jTDS** — koneksi JDBC ke SQL Server
- **iText 7** — generate PDF label & laporan
- **jBCrypt** — hashing password
- **Retrofit + Gson** — klien HTTP microservice
- **CameraX + ZXing** — scan barcode/QR
- **RxJava 3 / RxAndroid** — async
- **Room + WorkManager** — antrian persisten status print & retry background job
- **smbj / BouncyCastle** — akses share SMB (legacy)
- **Java-WebSocket** — koneksi realtime (`config/WebSocketConnection.java`)
- **Paging 3, RecyclerView, ViewPager2, SwipeRefresh, DrawerLayout, CardView**

---

## Struktur Proyek

```
app/src/main/java/com/example/myapplication/
├── api/         # Lapisan akses DB & HTTP per-domain
│                #   SawnTimberApi, S4sApi, MldApi, FjApi, LmtApi, CcApi, SndApi, BjApi,
│                #   ProsesProduksiApi, MasterApi, GradeApi, MappingApi, SpkApi,
│                #   SawmillApi, StockOpnameApi, PlanningMesinApi, PenjualanApi,
│                #   NyangkutApi, AuditApi, AuthApi, DeviceServiceApi
├── model/       # ~74 data model (StData, S4sData, LabelDetailData, GradeDetailData, dll)
├── utils/       # Helper: DateTimeUtils, AuditSessionContextHelper, SharedPrefUtils,
│                #   TokenManager, PermissionUtils, PdfUtils/PdfMicroserviceUtils,
│                #   BluetoothEscPosPrinter, QRCodeUtils, CameraUtils, Table*Utils,
│                #   PrintStatusQueue / PrintStatusSyncWorker (Room + WorkManager), dll
├── config/      # DatabaseConfig, ApiEndpoints, WebSocketConnection
├── sidebar/     # Komponen menu sidebar (adapter + model nav)
├── widget/      # Custom view (BarChartView)
└── *.java       # Activities & Fragments (lihat di bawah)
```

### Activities / layar utama
- **Entry**: `SplashActivity` (launcher) → `MainActivity` → `HostActivity` / `MenuUtama`
- **Auth**: `Registrasi`, `UpdateManager` (cek & unduh APK dari backend)
- **Input label**: `SawnTimber`, `S4S`, `Moulding`, `FingerJoint`, `Laminating`, `CrossCut`, `Sanding`, `Packing`, `BeforeLabel`, `InputLabel`
- **Proses produksi**: `ProsesProduksi`, `ProsesProduksiS4S`, `ProsesProduksiFJ`, `ProsesProduksiMoulding`, `ProsesProduksiLaminating`, `ProsesProduksiCrossCut`, `ProsesProduksiSanding`, `ProsesProduksiPacking`
- **Sawmill**: `Sawmill`, `ProsesSawmill`, `QcSawmill`, `PenerimaanStDariSawmill`
- **Sawn Timber pendukung**: `SawnTimberCategory`, `SawnTimberPembelian`, `SawnTimberUpah`
- **Stock Opname**: `StockOpnameMenu`, `StockOpname`, `StockOpnameAscend`, `BongkarSusun`
- **Penjualan**: `Penjualan`, `PenjualanBJ`, `PenjualanStSnd`
- **Lainnya**: `PlanningMesin`, `SPK`, `GradeABC`, `Nyangkut`, `AuditActivity`, `PdfPreviewActivity`
- **Fragment laporan**: `Laporan{ST,S4S,FJ,MLD,LMT,CCA,SND,BJ,KB,KbRambung,Manajemen,Verifikasi}Fragment`, `DashboardFragment`, `MappingFragment`

---

## Konfigurasi & Rahasia

Konfigurasi dipisah menjadi **environment** (di-commit) dan **rahasia lokal** (gitignored).

### 1. `local.properties` (per-mesin, JANGAN di-commit)
Berisi lokasi SDK + kredensial DB:
```properties
sdk.dir=C:\\Users\\<you>\\AppData\\Local\\Android\\Sdk
DB_PORT=1433
DB_USER=<user_sql_server>
DB_PASS=<password_sql_server>
```

### 2. `env/dev.properties` & `env/prod.properties` (di-commit)
Menimpa `local.properties` untuk key yang sama. Pilih dengan `-PbuildEnv=dev|prod` (default `dev`):
```properties
DB_IP=192.168.10.100
DB_NAME=WPS_TEST3          # prod: WPS
BASE_URL_API=http://192.168.11.153:5002
BASE_REPORT_MICROSERVICE=http://192.168.10.100:5006/
DEVICE_SERVICE_BASE=http://192.168.11.153:3000/
```

Semua nilai ini di-inject ke `BuildConfig` dan dibaca lewat `DatabaseConfig` / `ApiEndpoints`.

### 3. `keystore.properties` (untuk build release, gitignored)
Salin dari `keystore.properties.example` lalu isi:
```properties
storeFile=my-release-key.jks
storePassword=...
keyAlias=...
keyPassword=...
```

---

## Build & Menjalankan

Gunakan Gradle wrapper dari root repo:

```bash
./gradlew assembleDebug                      # APK debug (env dev)
./gradlew assembleRelease -PbuildEnv=prod     # APK release, env production
./gradlew assembleStaging                     # release + debuggable, applicationId produksi
./gradlew test                                # unit test JVM
./gradlew connectedAndroidTest                # instrumented test (butuh device/emulator)
./gradlew lint                                # Android lint
```

### Build variants
| Variant | applicationId | Catatan |
|---|---|---|
| `debug` | `com.example.myapplication.debug` | Bisa dipasang berdampingan dengan release. Ada **UI Debug Launcher** (buka Activity langsung tanpa splash/login) — lihat `DEBUG_UI_WORKFLOW.md` |
| `release` | `com.example.myapplication` | Ditandatangani bila `keystore.properties` ada |
| `staging` | `com.example.myapplication` | Seperti release tapi `debuggable = true` |

---

## Deploy (rilis APK ke user)

Distribusi update dilakukan lewat backend (`/api/update/tablet/...`), dicek oleh `UpdateManager.java`. Gunakan `deploy.sh`:

```bash
./deploy.sh "Perbaikan menu Mapping" --dev    # publish ke server dev, tes dulu
./deploy.sh "Perbaikan menu Mapping"          # publish ke production
./deploy.sh "Rilis wajib" --force --min 1.1.70
./deploy.sh "Uji build" --dry-run             # build + sign saja, tidak publish
```

Skrip otomatis: bump `versionName`/`versionCode` (patch, atau `--minor`), build release ter-sign, commit bump (kecuali `--no-git`), lalu POST APK ke endpoint publish.

---

## Konvensi Kode

Lihat `CLAUDE.md` dan `AGENTS.md` untuk detail. Ringkas:

- **Java style**: indentasi 4 spasi, `PascalCase` kelas, `camelCase` method/field. Tidak ada formatter yang dipaksakan — ikuti kode sekitar.
- **Dialog list**: RecyclerView + popup menu long-press (bukan TableLayout + tombol header). Layout `dialog_list_item_*.xml` + `item_dialog_label_*.xml` + `popup_menu_label_dialog_row.xml`.
- **Proses produksi**: popup menu long-press pada baris tabel (`showProductionRowActionPopup()` + `printSelectedProduction()`).
- **Audit trail**: setiap DB write wajib `AuditSessionContextHelper.apply(con, actorId, actorName, requestId)` dengan `requestId = UUID.randomUUID().toString()`.
- **Smart update**: hindari naive DELETE+INSERT. Cek `isHeaderChanged()`, lalu `replaceDetail()` (diff existing vs incoming). Bandingkan float dengan `Math.abs(a - b) < 0.000001`.
- **Permission check**: `userPermissions.contains("label_st:update")`, `"label_s4s:update"`, `"proses_mld:update"`, dst.
- **Windows**: file `.java` bisa read-only — jalankan `attrib -R "path\file.java"` sebelum edit.

---

## Testing

- Unit test: `app/src/test/java/` — pola `*Test.java`
- Instrumented test: `app/src/androidTest/java/com/example/myapplication/` — pola `ProsesProduksi*InstrumentedTest.java`, `SawnTimberInstrumentedTest`, `StRepoInstrumentedTest`
- Log tags: `LMT_TEST`, `CCA_TEST`, `SND_TEST`, `PKG_TEST`

---

## Izin Android yang Dipakai
`INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE`, `CAMERA` (scan barcode), `BLUETOOTH*` / `BLUETOOTH_CONNECT` / `BLUETOOTH_SCAN` (printer ESC/POS), `REQUEST_INSTALL_PACKAGES` (self-update APK), `READ/WRITE_EXTERNAL_STORAGE` (maxSdk 28).

---

## Catatan
- `usesCleartextTraffic="true"` + `network_security_config.xml` — semua endpoint internal via HTTP di jaringan LAN.
- Koneksi JDBC langsung dari tablet ke SQL Server: pastikan tablet berada di jaringan yang benar dan kredensial `local.properties` valid untuk environment yang dipilih.
- File `hs_err_pid*.log`, `replay_pid*.log` di root adalah artefak crash JVM/Gradle dan bisa diabaikan/dihapus.
