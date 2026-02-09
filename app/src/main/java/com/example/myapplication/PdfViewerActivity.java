package com.example.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.helper.EpsonPrinterHelper;

import java.io.IOException;

public class PdfViewerActivity extends AppCompatActivity {

    // UI Components
    private ImageView pdfImageView;
    private TextView tvTitle;
    private TextView tvPageInfo;
    private Button btnClose;
    private Button btnPrint;
    private Button btnPrevPage;
    private Button btnNextPage;
    private LinearLayout layoutPrintProgress;
    private ProgressBar progressBar;
    private TextView tvPrintStatus;

    // PDF Components
    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private ParcelFileDescriptor fileDescriptor;
    private int currentPageIndex = 0;
    private Uri pdfUri;

    // Printer Helper
    private EpsonPrinterHelper printerHelper;

    // Printer Configuration
    private static final String PRINTER_IP_DEFAULT = "192.168.8.105";
    private static final String PRINTER_TARGET_USB = "USB:000000000000000000";

    // SharedPreferences
    private static final String PREFS_NAME = "PrinterSettings";
    private static final String KEY_PRINTER_IP = "printer_ip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        initViews();
        loadPdfFromIntent();
        setupListeners();

        printerHelper = new EpsonPrinterHelper(this);
    }

    private void initViews() {
        pdfImageView = findViewById(R.id.pdfImageView);
        tvTitle = findViewById(R.id.tvTitle);
        tvPageInfo = findViewById(R.id.tvPageInfo);
        btnClose = findViewById(R.id.btnClose);
        btnPrint = findViewById(R.id.btnPrint);
        btnPrevPage = findViewById(R.id.btnPrevPage);
        btnNextPage = findViewById(R.id.btnNextPage);
        layoutPrintProgress = findViewById(R.id.layoutPrintProgress);
        progressBar = findViewById(R.id.progressBar);
        tvPrintStatus = findViewById(R.id.tvPrintStatus);
    }

    private void loadPdfFromIntent() {
        String pdfUriString = getIntent().getStringExtra("PDF_URI");
        String title = getIntent().getStringExtra("TITLE");

        if (pdfUriString == null || pdfUriString.isEmpty()) {
            Toast.makeText(this, "PDF tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        pdfUri = Uri.parse(pdfUriString);

        if (title != null && !title.isEmpty()) {
            tvTitle.setText(title);
        }

        try {
            openPdfRenderer(pdfUri);
            showPage(currentPageIndex);
            updatePageInfo();
            updateNavigationButtons();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal membuka PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void openPdfRenderer(Uri uri) throws IOException {
        fileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        if (fileDescriptor == null) {
            throw new IOException("Failed to open file descriptor");
        }
        pdfRenderer = new PdfRenderer(fileDescriptor);
    }

    private void showPage(int index) {
        if (pdfRenderer == null) {
            return;
        }

        if (index < 0 || index >= pdfRenderer.getPageCount()) {
            return;
        }

        if (currentPage != null) {
            currentPage.close();
        }

        currentPage = pdfRenderer.openPage(index);

        int width = currentPage.getWidth();
        int height = currentPage.getHeight();

        int scale = 2;
        Bitmap bitmap = Bitmap.createBitmap(
                width * scale,
                height * scale,
                Bitmap.Config.ARGB_8888
        );

        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        pdfImageView.setImageBitmap(bitmap);

        currentPageIndex = index;
        updatePageInfo();
        updateNavigationButtons();
    }

    private void updatePageInfo() {
        if (pdfRenderer != null) {
            int totalPages = pdfRenderer.getPageCount();
            String pageInfo = "Halaman " + (currentPageIndex + 1) + " dari " + totalPages;
            tvPageInfo.setText(pageInfo);
        }
    }

    private void updateNavigationButtons() {
        if (pdfRenderer == null) {
            btnPrevPage.setVisibility(View.GONE);
            btnNextPage.setVisibility(View.GONE);
            return;
        }

        int totalPages = pdfRenderer.getPageCount();

        if (totalPages <= 1) {
            btnPrevPage.setVisibility(View.GONE);
            btnNextPage.setVisibility(View.GONE);
        } else {
            btnPrevPage.setVisibility(View.VISIBLE);
            btnNextPage.setVisibility(View.VISIBLE);

            btnPrevPage.setEnabled(currentPageIndex > 0);
            btnNextPage.setEnabled(currentPageIndex < totalPages - 1);

            btnPrevPage.setAlpha(currentPageIndex > 0 ? 1.0f : 0.5f);
            btnNextPage.setAlpha(currentPageIndex < totalPages - 1 ? 1.0f : 0.5f);
        }
    }

    private void setupListeners() {
        btnClose.setOnClickListener(v -> finish());

        btnPrint.setOnClickListener(v -> showPrintOptions());

        btnPrevPage.setOnClickListener(v -> {
            if (currentPageIndex > 0) {
                showPage(currentPageIndex - 1);
            }
        });

        btnNextPage.setOnClickListener(v -> {
            if (pdfRenderer != null && currentPageIndex < pdfRenderer.getPageCount() - 1) {
                showPage(currentPageIndex + 1);
            }
        });
    }

    private void showPrintOptions() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedIp = prefs.getString(KEY_PRINTER_IP, PRINTER_IP_DEFAULT);

        String[] options = {
                "Test Print Text (Verify Ribbon)",
                "Print via LAN (" + savedIp + ")",
                "Print via USB",
                "Ganti IP Printer",
                "Print via PrinterShare (Fallback)"
        };

        new AlertDialog.Builder(this)
                .setTitle("Pilih Metode Print")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: // Test Text
                            testPrintText(savedIp);
                            break;
                        case 1: // LAN
                            String lanTarget = "TCP:" + savedIp;
                            printWithEpsonSDK(lanTarget, "LAN (" + savedIp + ")");
                            break;
                        case 2: // USB
                            printWithEpsonSDK(PRINTER_TARGET_USB, "USB");
                            break;
                        case 3: // Ganti IP
                            showChangeIpDialog();
                            break;
                        case 4: // PrinterShare
                            printWithPrinterShare();
                            break;
                    }
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    // METHOD INI YANG KURANG - SEKARANG LENGKAP
    private void testPrintText(String ip) {
        layoutPrintProgress.setVisibility(View.VISIBLE);
        tvPrintStatus.setText("Test printing text...");
        btnPrint.setEnabled(false);

        printerHelper.testPrintText("TCP:" + ip, new EpsonPrinterHelper.PrintCallback() {
            @Override
            public void onPrintSuccess() {
                runOnUiThread(() -> {
                    layoutPrintProgress.setVisibility(View.GONE);
                    btnPrint.setEnabled(true);

                    new AlertDialog.Builder(PdfViewerActivity.this)
                            .setTitle("Test Print Berhasil!")
                            .setMessage("Cek printer sekarang:\n\n" +
                                    "✅ Kalau TEXT JELAS → Ribbon OK, printer siap!\n" +
                                    "❌ Kalau TEXT PUDAR/BLANK → Ribbon habis, perlu ganti!\n\n" +
                                    "Lanjut print PDF sekarang?")
                            .setPositiveButton("Ya, Print PDF", (d, w) -> {
                                String target = "TCP:" + ip;
                                printWithEpsonSDK(target, "LAN (" + ip + ")");
                            })
                            .setNegativeButton("Tidak", null)
                            .show();
                });
            }

            @Override
            public void onPrintError(String error) {
                runOnUiThread(() -> {
                    layoutPrintProgress.setVisibility(View.GONE);
                    btnPrint.setEnabled(true);

                    new AlertDialog.Builder(PdfViewerActivity.this)
                            .setTitle("Test Print Gagal")
                            .setMessage("Error: " + error + "\n\n" +
                                    "Troubleshooting:\n" +
                                    "• Pastikan printer nyala\n" +
                                    "• Cek IP: " + ip + "\n" +
                                    "• Cek koneksi network\n" +
                                    "• Restart printer\n\n" +
                                    "Ganti IP atau coba PrinterShare?")
                            .setPositiveButton("Ganti IP", (d, w) -> showChangeIpDialog())
                            .setNeutralButton("PrinterShare", (d, w) -> printWithPrinterShare())
                            .setNegativeButton("Tutup", null)
                            .show();
                });
            }

            @Override
            public void onPrintProgress(String message) {
                runOnUiThread(() -> {
                    tvPrintStatus.setText(message);
                });
            }
        });
    }

    private void showChangeIpDialog() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String currentIp = prefs.getString(KEY_PRINTER_IP, PRINTER_IP_DEFAULT);

        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(currentIp);
        input.setHint("192.168.8.105");

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 0, 50, 0);
        input.setLayoutParams(lp);

        new AlertDialog.Builder(this)
                .setTitle("Ganti IP Printer")
                .setMessage("Masukkan IP Address printer:")
                .setView(input)
                .setPositiveButton("Simpan", (dialog, which) -> {
                    String newIp = input.getText().toString().trim();
                    if (!newIp.isEmpty()) {
                        if (isValidIpAddress(newIp)) {
                            prefs.edit().putString(KEY_PRINTER_IP, newIp).apply();
                            Toast.makeText(this, "IP Printer disimpan: " + newIp, Toast.LENGTH_SHORT).show();

                            new AlertDialog.Builder(this)
                                    .setTitle("Test Print")
                                    .setMessage("IP berhasil disimpan. Test print text dulu?")
                                    .setPositiveButton("Ya, Test Print", (d, w) -> testPrintText(newIp))
                                    .setNeutralButton("Langsung Print PDF", (d, w) -> {
                                        String target = "TCP:" + newIp;
                                        printWithEpsonSDK(target, "LAN (" + newIp + ")");
                                    })
                                    .setNegativeButton("Nanti", null)
                                    .show();
                        } else {
                            Toast.makeText(this, "Format IP tidak valid!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    private boolean isValidIpAddress(String ip) {
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(ipPattern);
    }

    private void printWithEpsonSDK(String printerTarget, String connectionType) {
        layoutPrintProgress.setVisibility(View.VISIBLE);
        tvPrintStatus.setText("Menghubungkan ke printer via " + connectionType + "...");
        btnPrint.setEnabled(false);

        printerHelper.printPdf(pdfUri, printerTarget, new EpsonPrinterHelper.PrintCallback() {
            @Override
            public void onPrintSuccess() {
                runOnUiThread(() -> {
                    layoutPrintProgress.setVisibility(View.GONE);
                    btnPrint.setEnabled(true);
                    Toast.makeText(PdfViewerActivity.this, "Print berhasil! Cek hasil di printer.", Toast.LENGTH_LONG).show();
                    finish();
                });
            }

            @Override
            public void onPrintError(String error) {
                runOnUiThread(() -> {
                    layoutPrintProgress.setVisibility(View.GONE);
                    btnPrint.setEnabled(true);

                    new AlertDialog.Builder(PdfViewerActivity.this)
                            .setTitle("Print Error")
                            .setMessage("Gagal print via " + connectionType + ":\n" + error +
                                    "\n\nTroubleshooting:\n" +
                                    "• Pastikan printer nyala\n" +
                                    "• Cek koneksi network\n" +
                                    "• Cek IP printer sudah benar\n" +
                                    "• Test print text dulu\n\n" +
                                    "Pilihan:")
                            .setPositiveButton("Test Print Text", (d, w) -> {
                                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                                String ip = prefs.getString(KEY_PRINTER_IP, PRINTER_IP_DEFAULT);
                                testPrintText(ip);
                            })
                            .setNeutralButton("PrinterShare", (dialog, w) -> printWithPrinterShare())
                            .setNegativeButton("Ganti IP", (dialog, w) -> showChangeIpDialog())
                            .show();
                });
            }

            @Override
            public void onPrintProgress(String message) {
                runOnUiThread(() -> {
                    tvPrintStatus.setText(message);
                });
            }
        });
    }

    private void printWithPrinterShare() {
        if (pdfUri == null) {
            Toast.makeText(this, "PDF tidak tersedia", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setPackage("com.dynamixsoftware.printershare");

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "PrinterShare tidak terinstall.\n\nDownload dari Play Store terlebih dahulu.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        closeRenderer();
        if (printerHelper != null) {
            printerHelper.cancelPrint();
        }
        super.onDestroy();
    }

    private void closeRenderer() {
        try {
            if (currentPage != null) {
                currentPage.close();
                currentPage = null;
            }
            if (pdfRenderer != null) {
                pdfRenderer.close();
                pdfRenderer = null;
            }
            if (fileDescriptor != null) {
                fileDescriptor.close();
                fileDescriptor = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}