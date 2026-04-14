package com.example.myapplication;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.api.DeviceServiceApi;
import com.example.myapplication.utils.BluetoothEscPosPrinter;

import java.util.ArrayList;
import java.util.List;

public class PdfPreviewActivity extends AppCompatActivity {
    public static final String EXTRA_PDF_URI = "extra_pdf_uri";
    public static final String EXTRA_LABEL_NO = "extra_label_no";
    public static final String EXTRA_PREVIEW_TITLE = "extra_preview_title";

    private static final String TAG = "PdfPreviewActivity";
    private static final int PAPER_WIDTH_MM = 80;
    private static final int REQUEST_BT_CONNECT = 3301;
    private static final String PREF_NAME = "pdf_preview_pref";
    private static final String KEY_PRINTER_ADDR = "printer_addr";

    private Uri pdfUri;
    private String labelNo;
    private LinearLayout pagesContainer;
    private TextView tvPreviewTitle;
    private TextView tvPrinter;
    private View layoutSelectedPrinterStats;
    private TextView tvSelectedPrinterUsage;
    private TextView tvSelectedPrinterStatus;
    private Button btnSelectPrinter;
    private Button btnPrint;
    private ParcelFileDescriptor parcelFileDescriptor;
    private PdfRenderer pdfRenderer;
    private final List<Bitmap> renderedBitmaps = new ArrayList<>();
    private BluetoothDevice selectedPrinter;
    private DeviceServiceApi.PrinterData selectedPrinterData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_preview_80mm);

        pagesContainer = findViewById(R.id.pagesContainer);
        tvPreviewTitle = findViewById(R.id.tvPreviewTitle);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnPrint = findViewById(R.id.btnPrint);
        btnSelectPrinter = findViewById(R.id.btnSelectPrinter);
        tvPrinter = findViewById(R.id.tvPrinter);
        layoutSelectedPrinterStats = findViewById(R.id.layoutSelectedPrinterStats);
        tvSelectedPrinterUsage = findViewById(R.id.tvSelectedPrinterUsage);
        tvSelectedPrinterStatus = findViewById(R.id.tvSelectedPrinterStatus);

        String pdfUriString = getIntent().getStringExtra(EXTRA_PDF_URI);
        labelNo = getIntent().getStringExtra(EXTRA_LABEL_NO);
        String previewTitle = getIntent().getStringExtra(EXTRA_PREVIEW_TITLE);

        if (pdfUriString == null || pdfUriString.trim().isEmpty()) {
            Toast.makeText(this, "File PDF tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        pdfUri = Uri.parse(pdfUriString);
        tvPreviewTitle.setText(
                previewTitle != null && !previewTitle.trim().isEmpty()
                        ? previewTitle
                        : "Preview Label"
        );
        renderPdfAsContinuousPaper();
        restoreSelectedPrinter();
        refreshPrinterUi();

        btnBack.setOnClickListener(v -> finish());
        btnSelectPrinter.setOnClickListener(v -> showPrinterPicker());
        btnPrint.setOnClickListener(v -> printDirectBluetooth());
    }

    private void renderPdfAsContinuousPaper() {
        pagesContainer.removeAllViews();
        renderedBitmaps.clear();
        closeRenderer();

        try {
            parcelFileDescriptor = getContentResolver().openFileDescriptor(pdfUri, "r");
            if (parcelFileDescriptor == null) {
                Toast.makeText(this, "Gagal membuka file PDF.", Toast.LENGTH_SHORT).show();
                return;
            }

            pdfRenderer = new PdfRenderer(parcelFileDescriptor);
            int pageCount = pdfRenderer.getPageCount();
            int targetWidthPx = mmToPx(PAPER_WIDTH_MM);

            for (int i = 0; i < pageCount; i++) {
                PdfRenderer.Page page = pdfRenderer.openPage(i);
                try {
                    float scale = (float) targetWidthPx / (float) page.getWidth();
                    int bitmapWidth = targetWidthPx;
                    int bitmapHeight = Math.max(1, Math.round(page.getHeight() * scale));

                    Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
                    // Important: white background to avoid transparent pixels becoming black on thermal print.
                    Canvas canvas = new Canvas(bitmap);
                    canvas.drawColor(Color.WHITE);
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    renderedBitmaps.add(bitmap);

                    ImageView pageView = new ImageView(this);
                    pageView.setImageBitmap(bitmap);
                    pageView.setAdjustViewBounds(true);
                    pageView.setScaleType(ImageView.ScaleType.FIT_XY);

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    pageView.setLayoutParams(lp);
                    pagesContainer.addView(pageView);

                    // Garis tipis antar page agar terlihat tetap continuous.
                    if (i < pageCount - 1) {
                        View divider = new View(this);
                        divider.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                Math.max(1, mmToPx(1))
                        ));
                        divider.setBackgroundColor(0xFFE6E6E6);
                        pagesContainer.addView(divider);
                    }
                } finally {
                    page.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Gagal render PDF", e);
            Toast.makeText(this, "Gagal menampilkan PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Fetch ulang data printer dari server setelah print sukses,
     * simpan ke SharedPreferences, lalu finish.
     * Jika fetch gagal atau timeout, langsung finish saja.
     */
    private void refreshPrinterDataThenFinish() {
        if (selectedPrinterData == null || selectedPrinterData.id.isEmpty()) {
            finish();
            return;
        }
        DeviceServiceApi.fetchPrinterById(selectedPrinterData.id,
                new DeviceServiceApi.FetchPrinterByIdCallback() {
                    @Override
                    public void onResult(DeviceServiceApi.PrinterData data) {
                        Log.d(TAG, "refreshPrinterDataThenFinish: usage=" + data.printUsage
                                + " status=" + data.status);
                        runOnUiThread(() -> finish());
                    }

                    @Override
                    public void onNotFound() {
                        runOnUiThread(() -> finish());
                    }

                    @Override
                    public void onError(String message) {
                        Log.w(TAG, "refreshPrinterDataThenFinish: " + message);
                        runOnUiThread(() -> finish());
                    }
                });
    }

    private void showPrinterPicker() {
        if (!ensureBluetoothPermission()) {
            return;
        }

        List<BluetoothDevice> devices = BluetoothEscPosPrinter.getBondedDevices();
        if (devices.isEmpty()) {
            Toast.makeText(this, "Tidak ada printer Bluetooth yang sudah dipairing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch data dari device-service dulu, baru tampilkan dialog
        btnPrint.setEnabled(false);
        btnSelectPrinter.setEnabled(false);
        tvPrinter.setText("Memuat daftar printer...");

        DeviceServiceApi.fetchRegisteredPrinters(new DeviceServiceApi.FetchPrintersCallback() {
            @Override
            public void onResult(List<DeviceServiceApi.PrinterData> printers) {
                // Buat map MAC → PrinterData untuk lookup cepat
                java.util.Map<String, DeviceServiceApi.PrinterData> dataMap = new java.util.HashMap<>();
                for (DeviceServiceApi.PrinterData p : printers) {
                    dataMap.put(p.identifier, p); // identifier sudah uppercase
                }
                runOnUiThread(() -> {
                    btnPrint.setEnabled(true);
                    btnSelectPrinter.setEnabled(true);
                    refreshPrinterUi();
                    showPrinterPickerDialog(devices, dataMap);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    btnPrint.setEnabled(true);
                    btnSelectPrinter.setEnabled(true);
                    refreshPrinterUi();
                    // Tetap tampilkan dialog tanpa data API (degraded mode)
                    Log.w(TAG, "showPrinterPicker: gagal fetch data API, tampil tanpa info registrasi");
                    showPrinterPickerDialog(devices, new java.util.HashMap<>());
                });
            }
        });
    }

    private void showPrinterPickerDialog(List<BluetoothDevice> devices,
                                         java.util.Map<String, DeviceServiceApi.PrinterData> dataMap) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_printer_picker, null);
        ListView listPrinter = dialogView.findViewById(R.id.listPrinter);
        Button btnCloseDialog = dialogView.findViewById(R.id.btnClosePrinterDialog);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        PrinterPickerAdapter adapter = new PrinterPickerAdapter(devices, dataMap);
        listPrinter.setAdapter(adapter);
        listPrinter.setOnItemClickListener((parent, view, position, id) -> {
            BluetoothDevice picked = devices.get(position);
            dialog.dismiss();
            String mac = picked.getAddress().toUpperCase();
            DeviceServiceApi.PrinterData data = dataMap.get(mac);
            if (data != null) {
                selectedPrinter = picked;
                selectedPrinterData = data;
                saveSelectedPrinter(picked);
                refreshPrinterUi();
            } else {
                showRegisterPrinterDialog(picked);
            }
        });

        btnCloseDialog.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    /**
     * Cek apakah printer sudah terdaftar di device-service.
     * - Jika sudah → langsung set sebagai selectedPrinter.
     * - Jika belum → tampilkan dialog registrasi.
     * - Jika error jaringan → tampilkan pesan, printer tidak dipilih.
     */
    /**
     * Cek registrasi printer via GET /api/devices/printers (all) lalu filter by MAC.
     * Dipanggil saat user memilih printer dari picker.
     * - Terdaftar → set sebagai selectedPrinter
     * - Tidak terdaftar (404) → dialog registrasi
     */
    private void checkAndSelectPrinter(BluetoothDevice device) {
        btnPrint.setEnabled(false);
        btnSelectPrinter.setEnabled(false);
        tvPrinter.setText("Memeriksa registrasi printer...");

        String mac = device.getAddress().toUpperCase();
        DeviceServiceApi.fetchRegisteredPrinters(new DeviceServiceApi.FetchPrintersCallback() {
            @Override
            public void onResult(List<DeviceServiceApi.PrinterData> printers) {
                DeviceServiceApi.PrinterData found = null;
                for (DeviceServiceApi.PrinterData p : printers) {
                    if (mac.equalsIgnoreCase(p.identifier)) {
                        found = p;
                        break;
                    }
                }
                final DeviceServiceApi.PrinterData data = found;
                runOnUiThread(() -> {
                    btnPrint.setEnabled(true);
                    btnSelectPrinter.setEnabled(true);
                    if (data != null) {
                        selectedPrinter = device;
                        selectedPrinterData = data;
                        saveSelectedPrinter(device);
                        refreshPrinterUi();
                    } else {
                        refreshPrinterUi();
                        showRegisterPrinterDialog(device);
                    }
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    btnPrint.setEnabled(true);
                    btnSelectPrinter.setEnabled(true);
                    refreshPrinterUi();
                    Toast.makeText(PdfPreviewActivity.this,
                            "Gagal memeriksa registrasi printer: " + message,
                            Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    /**
     * Dialog untuk mendaftarkan printer yang belum terdaftar.
     * User memasukkan nama printer, lalu app memanggil POST /api/devices/printers.
     */
    private void showRegisterPrinterDialog(BluetoothDevice device) {
        String btName = (device.getName() != null && !device.getName().trim().isEmpty())
                ? device.getName() : "Printer Bluetooth";

        EditText etName = new EditText(this);
        etName.setHint("Nama printer (contoh: Printer Lantai 1)");
        etName.setText(btName);
        etName.setSingleLine(true);

        int paddingPx = (int) (16 * getResources().getDisplayMetrics().density);
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(paddingPx, paddingPx / 2, paddingPx, 0);
        container.addView(etName);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Printer Belum Terdaftar")
                .setMessage("Printer " + device.getAddress() + " belum terdaftar.\nMasukkan nama untuk mendaftarkannya:")
                .setView(container)
                .setCancelable(false)
                .setPositiveButton("Daftarkan", null) // set listener manually to control dismiss
                .setNegativeButton("Batal", (d, w) -> refreshPrinterUi())
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String name = etName.getText().toString().trim();
                if (name.isEmpty()) {
                    etName.setError("Nama tidak boleh kosong");
                    return;
                }
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);

                DeviceServiceApi.registerPrinter(device.getAddress(), name,
                        new DeviceServiceApi.RegisterPrinterCallback() {
                            @Override
                            public void onSuccess() {
                                runOnUiThread(() -> {
                                    dialog.dismiss();
                                    selectedPrinter = device;
                                    saveSelectedPrinter(device);
                                    refreshPrinterUi();
                                    Toast.makeText(PdfPreviewActivity.this,
                                            "Printer berhasil didaftarkan.",
                                            Toast.LENGTH_SHORT).show();
                                });
                            }

                            @Override
                            public void onError(String message) {
                                runOnUiThread(() -> {
                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);
                                    Toast.makeText(PdfPreviewActivity.this,
                                            "Gagal mendaftarkan printer: " + message,
                                            Toast.LENGTH_LONG).show();
                                });
                            }
                        });
            });
        });

        dialog.show();
    }

    /**
     * Notifikasi WARNING — printer mendekati batas maintenance.
     * User masih bisa melanjutkan cetak.
     */
    private void showPrinterWarningDialog() {
        String printerLabel = selectedPrinterData != null && selectedPrinterData.name != null
                ? selectedPrinterData.name
                : (selectedPrinter != null ? selectedPrinter.getName() : "Printer");
        String usage = selectedPrinterData != null ? selectedPrinterData.printUsage : "-";

        new AlertDialog.Builder(this)
                .setTitle("⚠ Peringatan Printer")
                .setMessage("Printer \"" + printerLabel + "\" mendekati batas maintenance.\n\n"
                        + "Penggunaan: " + usage + "\n\n"
                        + "Disarankan segera lakukan maintenance. Anda masih dapat mencetak, "
                        + "namun kualitas cetak mungkin menurun.")
                .setCancelable(false)
                .setPositiveButton("Lanjutkan Cetak", (d, w) -> executePrint())
                .setNegativeButton("Batal", null)
                .show();
    }

    /**
     * Blokir CRITICAL — printer sudah melewati batas maintenance, tidak bisa cetak.
     */
    private void showPrinterCriticalDialog() {
        String printerLabel = selectedPrinterData != null && selectedPrinterData.name != null
                ? selectedPrinterData.name
                : (selectedPrinter != null ? selectedPrinter.getName() : "Printer");
        String usage = selectedPrinterData != null ? selectedPrinterData.printUsage : "-";

        new AlertDialog.Builder(this)
                .setTitle("🚫 Printer Tidak Dapat Digunakan")
                .setMessage("Printer \"" + printerLabel + "\" telah melampaui batas maintenance dan tidak dapat digunakan untuk mencetak.\n\n"
                        + "Penggunaan: " + usage + "\n\n"
                        + "Hubungi teknisi untuk melakukan maintenance sebelum printer dapat digunakan kembali.")
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .show();
    }

    /** Jalankan print tanpa pengecekan status ulang (sudah lolos validasi). */
    private void executePrint() {
        btnPrint.setEnabled(false);
        btnSelectPrinter.setEnabled(false);
        Toast.makeText(this, "Mengirim data ke printer...", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "executePrint:start printer="
                + (selectedPrinter != null ? selectedPrinter.getName() : "null")
                + " addr=" + (selectedPrinter != null ? selectedPrinter.getAddress() : "-")
                + " pages=" + renderedBitmaps.size()
                + " labelNo=" + labelNo);

        new Thread(() -> {
            boolean success = false;
            String errorMsg = null;
            try {
                success = BluetoothEscPosPrinter.printBitmaps(selectedPrinter, renderedBitmaps);
            } catch (Exception e) {
                errorMsg = e.getMessage();
                Log.e(TAG, "executePrint:failed", e);
            }

            final boolean finalSuccess = success;
            final String finalErrorMsg = errorMsg;
            runOnUiThread(() -> {
                btnPrint.setEnabled(true);
                btnSelectPrinter.setEnabled(true);
                if (finalSuccess) {
                    Log.d(TAG, "executePrint:success");
                    DeviceServiceApi.logPrinterUsageAsync(PdfPreviewActivity.this, selectedPrinter.getAddress());
                    Toast.makeText(PdfPreviewActivity.this, "Cetak selesai.", Toast.LENGTH_SHORT).show();
                    Intent result = new Intent();
                    result.putExtra(EXTRA_LABEL_NO, labelNo);
                    setResult(RESULT_OK, result);
                    // Refresh data printer di SharedPreferences agar usage & status
                    // tercatat terkini untuk sesi print berikutnya, lalu finish.
                    refreshPrinterDataThenFinish();
                } else {
                    Log.e(TAG, "executePrint:error=" + finalErrorMsg);
                    Toast.makeText(
                            PdfPreviewActivity.this,
                            "Cetak gagal: " + (finalErrorMsg != null ? finalErrorMsg : "unknown error"),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        }).start();
    }

    private void printDirectBluetooth() {
        if (renderedBitmaps.isEmpty()) {
            Toast.makeText(this, "PDF belum siap untuk dicetak.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ensureBluetoothPermission()) {
            return;
        }

        if (selectedPrinter == null) {
            showPrinterPicker();
            return;
        }

        // Selalu fetch data terbaru sebelum print agar status & usage selalu akurat
        btnPrint.setEnabled(false);
        btnSelectPrinter.setEnabled(false);
        Toast.makeText(this, "Memeriksa status printer...", Toast.LENGTH_SHORT).show();

        fetchCurrentPrinterData(new DeviceServiceApi.FetchPrinterByIdCallback() {
            @Override
            public void onResult(DeviceServiceApi.PrinterData data) {
                runOnUiThread(() -> {
                    btnPrint.setEnabled(true);
                    btnSelectPrinter.setEnabled(true);
                    selectedPrinterData = data;
                    refreshPrinterUi();

                    if ("CRITICAL".equalsIgnoreCase(data.status)) {
                        showPrinterCriticalDialog();
                    } else if ("WARNING".equalsIgnoreCase(data.status)) {
                        showPrinterWarningDialog();
                    } else {
                        executePrint();
                    }
                });
            }

            @Override
            public void onNotFound() {
                runOnUiThread(() -> {
                    btnPrint.setEnabled(true);
                    btnSelectPrinter.setEnabled(true);
                    Toast.makeText(PdfPreviewActivity.this,
                            "Printer tidak terdaftar. Pilih printer terlebih dahulu.",
                            Toast.LENGTH_LONG).show();
                    showPrinterPicker();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    btnPrint.setEnabled(true);
                    btnSelectPrinter.setEnabled(true);
                    Toast.makeText(PdfPreviewActivity.this,
                            "Gagal memeriksa status printer: " + message,
                            Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private int mmToPx(float mm) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, mm, getResources().getDisplayMetrics());
    }

    private void closeRenderer() {
        if (pdfRenderer != null) {
            pdfRenderer.close();
            pdfRenderer = null;
        }
        if (parcelFileDescriptor != null) {
            try {
                parcelFileDescriptor.close();
            } catch (Exception ignored) {
            }
            parcelFileDescriptor = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeRenderer();
        for (Bitmap bitmap : renderedBitmaps) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        renderedBitmaps.clear();
    }

    private boolean ensureBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            boolean hasConnect = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                    == PackageManager.PERMISSION_GRANTED;
            boolean hasScan = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                    == PackageManager.PERMISSION_GRANTED;

            if (!hasConnect || !hasScan) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.BLUETOOTH_SCAN
                        },
                        REQUEST_BT_CONNECT
                );
                return false;
            }
        }
        return true;
    }

    private void refreshPrinterUi() {
        if (tvPrinter == null) return;
        if (selectedPrinter == null || selectedPrinterData == null) {
            tvPrinter.setText("Belum ada printer dipilih");
            if (layoutSelectedPrinterStats != null) {
                layoutSelectedPrinterStats.setVisibility(View.GONE);
            }
            return;
        }

        // Alias: pakai name dari API jika ada, fallback ke BT name
        String alias = selectedPrinterData.name != null
                ? selectedPrinterData.name
                : (selectedPrinter.getName() != null ? selectedPrinter.getName() : selectedPrinter.getAddress());
        tvPrinter.setText(alias);

        // Stats row
        tvSelectedPrinterUsage.setText("Cetak: " + selectedPrinterData.printUsage);

        tvSelectedPrinterStatus.setText(selectedPrinterData.status);
        if ("NORMAL".equalsIgnoreCase(selectedPrinterData.status)) {
            tvSelectedPrinterStatus.setTextColor(0xFF166534);
            tvSelectedPrinterStatus.setBackgroundResource(R.drawable.border_granted);
        } else if ("WARNING".equalsIgnoreCase(selectedPrinterData.status)) {
            tvSelectedPrinterStatus.setTextColor(0xFF92400E);
            tvSelectedPrinterStatus.setBackgroundResource(R.drawable.border_warning);
        } else {
            // CRITICAL
            tvSelectedPrinterStatus.setTextColor(0xFF991B1B);
            tvSelectedPrinterStatus.setBackgroundResource(R.drawable.border_rejected);
        }

        layoutSelectedPrinterStats.setVisibility(View.VISIBLE);
    }

    private class PrinterPickerAdapter extends ArrayAdapter<BluetoothDevice> {
        private final java.util.Map<String, DeviceServiceApi.PrinterData> dataMap;

        PrinterPickerAdapter(List<BluetoothDevice> devices,
                             java.util.Map<String, DeviceServiceApi.PrinterData> dataMap) {
            super(PdfPreviewActivity.this, 0, devices);
            this.dataMap = dataMap;
        }

        @Override
        public View getView(int position, @Nullable View convertView, @androidx.annotation.NonNull ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_printer_picker, parent, false);
            }

            BluetoothDevice device = getItem(position);
            TextView tvPrinterName    = view.findViewById(R.id.tvPrinterName);
            TextView tvPrinterAlias   = view.findViewById(R.id.tvPrinterAlias);
            TextView tvPrinterAddress = view.findViewById(R.id.tvPrinterAddress);
            TextView tvPrinterBadge   = view.findViewById(R.id.tvPrinterBadge);
            View     layoutStats      = view.findViewById(R.id.layoutPrinterStats);
            TextView tvPrinterUsage   = view.findViewById(R.id.tvPrinterUsage);
            TextView tvPrinterStatus  = view.findViewById(R.id.tvPrinterStatus);

            String btName = (device != null && device.getName() != null && !device.getName().trim().isEmpty())
                    ? device.getName() : "Printer Bluetooth";
            String address = device != null ? device.getAddress() : "-";

            tvPrinterName.setText(btName);
            tvPrinterAddress.setText(address);

            // Data dari device-service (jika terdaftar)
            DeviceServiceApi.PrinterData data = device != null
                    ? dataMap.get(device.getAddress().toUpperCase()) : null;

            if (data != null) {
                // Alias
                tvPrinterAlias.setText(data.name != null ? data.name : "—");
                tvPrinterAlias.setTextColor(data.name != null ? 0xFF374151 : 0xFF9CA3AF);

                // printUsage + status
                layoutStats.setVisibility(View.VISIBLE);
                tvPrinterUsage.setText("Cetak: " + data.printUsage);

                tvPrinterStatus.setText(data.status);
                if ("NORMAL".equalsIgnoreCase(data.status)) {
                    tvPrinterStatus.setTextColor(0xFF166534);
                    tvPrinterStatus.setBackgroundResource(R.drawable.border_granted);
                } else if ("WARNING".equalsIgnoreCase(data.status)) {
                    tvPrinterStatus.setTextColor(0xFF92400E);
                    tvPrinterStatus.setBackgroundResource(R.drawable.border_warning);
                } else {
                    // CRITICAL atau status lain
                    tvPrinterStatus.setTextColor(0xFF991B1B);
                    tvPrinterStatus.setBackgroundResource(R.drawable.border_rejected);
                }
            } else {
                // Printer belum terdaftar di device-service
                tvPrinterAlias.setText("Belum terdaftar");
                tvPrinterAlias.setTextColor(0xFF9CA3AF);
                layoutStats.setVisibility(View.GONE);
            }

            // Badge "Dipilih"
            boolean isCurrent = selectedPrinter != null
                    && device != null
                    && selectedPrinter.getAddress().equalsIgnoreCase(device.getAddress());
            tvPrinterBadge.setVisibility(isCurrent ? View.VISIBLE : View.GONE);

            return view;
        }
    }

    private void saveSelectedPrinter(BluetoothDevice device) {
        getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_PRINTER_ADDR, device.getAddress())
                .apply();
    }

    private void restoreSelectedPrinter() {
        if (!ensureBluetoothPermission()) return;
        SharedPreferences sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String savedAddress = sp.getString(KEY_PRINTER_ADDR, null);
        if (savedAddress == null || savedAddress.trim().isEmpty()) return;

        // Cocokkan dengan bonded BT devices
        List<BluetoothDevice> devices = BluetoothEscPosPrinter.getBondedDevices();
        for (BluetoothDevice d : devices) {
            if (savedAddress.equalsIgnoreCase(d.getAddress())) {
                selectedPrinter = d;
                break;
            }
        }

        // Fetch data terbaru dari server di background untuk update card
        if (selectedPrinter != null) {
            fetchAndUpdatePrinterCard(savedAddress);
        }
    }

    /**
     * Fetch data printer by MAC (via fetchRegisteredPrinters + filter) lalu update card.
     * Non-blocking, tidak memblokir print.
     */
    private void fetchAndUpdatePrinterCard(String mac) {
        DeviceServiceApi.fetchRegisteredPrinters(new DeviceServiceApi.FetchPrintersCallback() {
            @Override
            public void onResult(List<DeviceServiceApi.PrinterData> printers) {
                DeviceServiceApi.PrinterData found = null;
                for (DeviceServiceApi.PrinterData p : printers) {
                    if (mac.equalsIgnoreCase(p.identifier)) {
                        found = p;
                        break;
                    }
                }
                final DeviceServiceApi.PrinterData data = found;
                runOnUiThread(() -> {
                    selectedPrinterData = data;
                    refreshPrinterUi();
                });
            }

            @Override
            public void onError(String message) {
                Log.w(TAG, "fetchAndUpdatePrinterCard: " + message);
                // Biarkan card tetap seperti adanya
            }
        });
    }

    /**
     * Fetch data printer terkini:
     * - Jika sudah ada ID di selectedPrinterData → fetchPrinterById(id)
     * - Jika belum (restore dari SharedPrefs) → fetchRegisteredPrinters + filter MAC
     */
    private void fetchCurrentPrinterData(DeviceServiceApi.FetchPrinterByIdCallback callback) {
        if (selectedPrinterData != null && !selectedPrinterData.id.isEmpty()) {
            DeviceServiceApi.fetchPrinterById(selectedPrinterData.id, callback);
        } else if (selectedPrinter != null) {
            String mac = selectedPrinter.getAddress().toUpperCase();
            DeviceServiceApi.fetchRegisteredPrinters(new DeviceServiceApi.FetchPrintersCallback() {
                @Override
                public void onResult(List<DeviceServiceApi.PrinterData> printers) {
                    for (DeviceServiceApi.PrinterData p : printers) {
                        if (mac.equalsIgnoreCase(p.identifier)) {
                            callback.onResult(p);
                            return;
                        }
                    }
                    callback.onNotFound();
                }

                @Override
                public void onError(String message) {
                    callback.onError(message);
                }
            });
        } else {
            callback.onNotFound();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BT_CONNECT) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }
            if (granted) {
                restoreSelectedPrinter();
                refreshPrinterUi();
            } else {
                Toast.makeText(this, "Izin Bluetooth diperlukan untuk memilih dan mencetak.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
