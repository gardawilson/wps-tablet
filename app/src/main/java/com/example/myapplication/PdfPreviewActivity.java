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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    private Button btnSelectPrinter;
    private Button btnPrint;
    private ParcelFileDescriptor parcelFileDescriptor;
    private PdfRenderer pdfRenderer;
    private final List<Bitmap> renderedBitmaps = new ArrayList<>();
    private BluetoothDevice selectedPrinter;

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

    private void showPrinterPicker() {
        if (!ensureBluetoothPermission()) {
            return;
        }

        List<BluetoothDevice> devices = BluetoothEscPosPrinter.getBondedDevices();
        if (devices.isEmpty()) {
            Toast.makeText(this, "Tidak ada printer Bluetooth yang sudah dipairing.", Toast.LENGTH_SHORT).show();
            return;
        }

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_printer_picker, null);
        ListView listPrinter = dialogView.findViewById(R.id.listPrinter);
        Button btnCloseDialog = dialogView.findViewById(R.id.btnClosePrinterDialog);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        PrinterPickerAdapter adapter = new PrinterPickerAdapter(devices);
        listPrinter.setAdapter(adapter);
        listPrinter.setOnItemClickListener((parent, view, position, id) -> {
            selectedPrinter = devices.get(position);
            saveSelectedPrinter(selectedPrinter);
            refreshPrinterUi();
            dialog.dismiss();
        });

        btnCloseDialog.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
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

        btnPrint.setEnabled(false);
        btnSelectPrinter.setEnabled(false);
        Toast.makeText(this, "Mengirim data ke printer...", Toast.LENGTH_SHORT).show();

        new Thread(() -> {
            boolean success = false;
            String errorMsg = null;
            try {
                success = BluetoothEscPosPrinter.printBitmaps(selectedPrinter, renderedBitmaps);
            } catch (Exception e) {
                errorMsg = e.getMessage();
            }

            final boolean finalSuccess = success;
            final String finalErrorMsg = errorMsg;
            runOnUiThread(() -> {
                btnPrint.setEnabled(true);
                btnSelectPrinter.setEnabled(true);
                if (finalSuccess) {
                    Toast.makeText(PdfPreviewActivity.this, "Cetak selesai.", Toast.LENGTH_SHORT).show();
                    Intent result = new Intent();
                    result.putExtra(EXTRA_LABEL_NO, labelNo);
                    setResult(RESULT_OK, result);
                    finish();
                } else {
                    Toast.makeText(
                            PdfPreviewActivity.this,
                            "Cetak gagal: " + (finalErrorMsg != null ? finalErrorMsg : "unknown error"),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        }).start();
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
        if (selectedPrinter == null) {
            tvPrinter.setText("Belum ada printer dipilih");
        } else {
            String name = selectedPrinter.getName() != null ? selectedPrinter.getName() : "Unknown";
            tvPrinter.setText(name + "\n" + selectedPrinter.getAddress());
        }
    }

    private class PrinterPickerAdapter extends ArrayAdapter<BluetoothDevice> {
        PrinterPickerAdapter(List<BluetoothDevice> devices) {
            super(PdfPreviewActivity.this, 0, devices);
        }

        @Override
        public View getView(int position, @Nullable View convertView, @androidx.annotation.NonNull ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_printer_picker, parent, false);
            }

            BluetoothDevice device = getItem(position);
            TextView tvPrinterName = view.findViewById(R.id.tvPrinterName);
            TextView tvPrinterAddress = view.findViewById(R.id.tvPrinterAddress);
            TextView tvPrinterBadge = view.findViewById(R.id.tvPrinterBadge);

            String name = (device != null && device.getName() != null && !device.getName().trim().isEmpty())
                    ? device.getName()
                    : "Printer Bluetooth";
            String address = device != null ? device.getAddress() : "-";

            tvPrinterName.setText(name);
            tvPrinterAddress.setText(address);

            boolean isCurrent = selectedPrinter != null
                    && device != null
                    && selectedPrinter.getAddress().equalsIgnoreCase(device.getAddress());
            tvPrinterBadge.setVisibility(isCurrent ? View.VISIBLE : View.GONE);

            return view;
        }
    }

    private void saveSelectedPrinter(BluetoothDevice device) {
        SharedPreferences sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_PRINTER_ADDR, device.getAddress()).apply();
    }

    private void restoreSelectedPrinter() {
        if (!ensureBluetoothPermission()) return;
        SharedPreferences sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String savedAddress = sp.getString(KEY_PRINTER_ADDR, null);
        if (savedAddress == null || savedAddress.trim().isEmpty()) return;

        List<BluetoothDevice> devices = BluetoothEscPosPrinter.getBondedDevices();
        for (BluetoothDevice d : devices) {
            if (savedAddress.equalsIgnoreCase(d.getAddress())) {
                selectedPrinter = d;
                return;
            }
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
