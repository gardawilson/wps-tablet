# WPS Tablet - Android Project

## Overview
Android tablet app (Java) untuk manajemen produksi kayu (Sawn Timber, S4S, Moulding, FJ, Laminating, CrossCut, Sanding, Packing).
Target: tablet landscape, minSdk 26, MSSQL Server via JDBC.

## Tech Stack
- Language: Java (Android)
- Database: SQL Server (direct JDBC connection via `DatabaseConfig.getConnectionUrl()`)
- PDF: iTextPDF
- Bluetooth Print: custom BT printer library
- RecyclerView, PopupWindow, AlertDialog pattern

## Project Structure
```
app/src/main/java/com/example/myapplication/
├── api/               # DB access layer (SawnTimberApi, S4sApi, ProsesProduksiApi, MasterApi, dll)
├── model/             # Data models (StData, S4sData, LabelDetailData, GradeDetailData, dll)
├── utils/             # Helpers (DateTimeUtils, AuditSessionContextHelper, TooltipUtils, SharedPrefUtils, dll)
├── config/            # DatabaseConfig
└── *.java             # Activities (SawnTimber, S4S, ProsesProduksiFJ, ProsesProduksiMoulding, dll)

app/src/main/res/
├── layout/            # XML layouts
└── drawable/          # Icons, backgrounds
```

## Key Patterns

### Dialog List (RecyclerView format - S4S style)
Semua dialog list label menggunakan RecyclerView + popup menu long-press (bukan TableLayout + btnEdit/btnDelete header).
- Layout dialog: `dialog_list_item_*.xml` → RecyclerView + FrameLayout
- Layout row item: `item_dialog_label_*.xml`
- Popup menu: `popup_menu_label_dialog_row.xml` (Edit, Hapus, Print, History)
- Inner class `DialogLabelAdapter extends RecyclerView.Adapter`
- Long-press row → `showDialogRowActionPopup()`

### Proses Produksi Format (Moulding style)
Semua aktivitas proses produksi menggunakan popup menu long-press pada baris tabel (bukan header button btnEdit/btnPrint).
- Method: `showProductionRowActionPopup()` + `printSelectedProduction()`
- Lambda fix: gunakan `final TableRow currentRow = row; final ProductionData currentData = data;`
- Touch tracking: `setOnTouchListener` simpan koordinat ke `float[] touchPoint`

### Audit Trail
Setiap DB write wajib set context: `AuditSessionContextHelper.apply(con, actorId, actorName, requestId)`
- `actorId` = `idUsername` (String)
- `requestId` = `UUID.randomUUID().toString()`

### Smart Update Pattern (S4S / SawnTimber style)
Untuk update, JANGAN naive DELETE+INSERT. Gunakan smart comparison:
- Header: cek `isHeaderChanged()` dulu sebelum UPDATE
- Detail: `replaceDetail()` → Map<Integer, DetailRow> existing vs incoming, hanya INSERT/UPDATE/DELETE yang berubah
- Float comparison: `Math.abs(a - b) < 0.000001`

### Permission Check
```java
userPermissions.contains("label_st:update")   // SawnTimber
userPermissions.contains("label_s4s:update")  // S4S
userPermissions.contains("proses_mld:update") // Moulding
```

### Tooltip Popup (dalam dialog list)
Menggunakan `ProsesProduksiApi.getTooltipData(noKey, tableH, tableD, mainColumn)`
- ST: tableH="ST_h", tableD="ST_d", mainColumn="NoST"
- S4S: tableH="S4S_h", tableD="S4S_d", mainColumn="NoS4S"

## Aktivitas yang Sudah Direvisi (format terbaru)
- ProsesProduksiMoulding ✓ (template/referensi)
- ProsesProduksiLaminating ✓
- ProsesProduksiCrossCut ✓
- ProsesProduksiSanding ✓
- ProsesProduksiPacking ✓
- SawnTimber (dialog list) ✓ → RecyclerView + popup menu

## Instrumented Tests
File test ada di `app/src/androidTest/java/com/example/myapplication/`
Format: `ProsesProduksi*InstrumentedTest.java`
Log tags: LMT_TEST, CCA_TEST, SND_TEST, PKG_TEST

## Hal Penting / Jangan Dilupakan
- File di Windows mungkin read-only → jalankan `attrib -R "path\file.java"` sebelum edit
- `rawDateVacuum` di SawnTimber.java HARUS di-set saat load header data (bug lama sudah fix)
- SawnTimberApi: smart comparison sudah diimplementasi (replaceSTDetail, replaceSTGrade, isSTHeaderChanged)
- Popup position calculation: hitung dari `event.getRawX()/getRawY()`, adjust agar tidak keluar layar
