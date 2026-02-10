package com.example.myapplication.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.example.myapplication.model.LabelDetailData;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.barcodes.BarcodeQRCode;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class EpsonPrinterHelper {

    private static final String TAG = "EpsonPrinterHelper";

    private Context context;
    private Printer printer;
    private PrintCallback callback;

    public interface PrintCallback {
        void onPrintSuccess();
        void onPrintError(String error);
        void onPrintProgress(String message);
    }

    public EpsonPrinterHelper(Context context) {
        this.context = context;
    }

    public void printPdf(Uri pdfUri, String printerTarget, PrintCallback callback) {
        this.callback = callback;

        new Thread(() -> {
            try {
                Log.d(TAG, "Starting print job to: " + printerTarget);
                initializePrinter();
                connectPrinter(printerTarget);
                printPdfPages(pdfUri);
            } catch (Epos2Exception e) {
                e.printStackTrace();
                String error = "Epson SDK Error: " + getErrorMessage(e.getErrorStatus());
                Log.e(TAG, error);
                notifyError(error);
                disconnectPrinter();
            } catch (Exception e) {
                e.printStackTrace();
                String error = "Error: " + e.getMessage();
                Log.e(TAG, error);
                notifyError(error);
                disconnectPrinter();
            }
        }).start();
    }

    // ========== PRINT DIRECT TEXT (NEW METHOD) ==========
    public void printDirectText(
            String noST, String jenisKayu, String tglStickBundle,
            String tellyBy, String noSPK, String stickBy, String platTruk,
            List<LabelDetailData> detailData, String noKayuBulat, String namaSupplier,
            String noTruk, String jumlahPcs, String m3, String ton,
            int printCount, String remark, int isSLP, String idUOMTblLebar,
            String idUOMPanjang, String noPenST, int labelVersion, String customer,
            String printerTarget, PrintCallback callback
    ) {
        this.callback = callback;

        new Thread(() -> {
            try {
                Log.d(TAG, "Starting direct text print");
                initializePrinter();
                connectPrinter(printerTarget);

                // === HEADER ===
                String headerLabel = "";
                if (labelVersion == 1 || noPenST.startsWith("BA")) {
                    headerLabel = "LABEL ST (Pbl)";
                } else if (labelVersion == 2 || noPenST.startsWith("O")) {
                    headerLabel = "LABEL ST (Upah)";
                } else {
                    headerLabel = "LABEL ST";
                }

                printer.addTextAlign(Printer.ALIGN_CENTER);
                printer.addTextStyle(Printer.FALSE, Printer.TRUE, Printer.FALSE, Printer.COLOR_1); // Bold
                printer.addTextSize(2, 2);
                printer.addText(headerLabel + "\n");
                printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.COLOR_1); // Normal
                printer.addTextSize(1, 1);
                printer.addFeedLine(1);

                // === NO ST (BOLD, LARGE) ===
                printer.addTextAlign(Printer.ALIGN_LEFT);
                printer.addTextStyle(Printer.FALSE, Printer.TRUE, Printer.FALSE, Printer.COLOR_1);
                printer.addTextSize(2, 2);
                printer.addText("NO: " + noST + "\n");
                printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.COLOR_1);
                printer.addTextSize(1, 1);
                printer.addFeedLine(1);

                // === WATERMARK "COPY" (jika printCount > 0) ===
                if (printCount > 0) {
                    printer.addTextAlign(Printer.ALIGN_CENTER);
                    printer.addTextSize(3, 3);
                    printer.addTextStyle(Printer.FALSE, Printer.TRUE, Printer.FALSE, Printer.COLOR_1);
                    printer.addText("*** COPY ***\n");
                    printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.COLOR_1);
                    printer.addTextSize(1, 1);
                    printer.addFeedLine(1);
                }

                // === INFO SECTION (2 KOLOM) ===
                printer.addTextAlign(Printer.ALIGN_LEFT);
                printer.addText(String.format("%-18s %-20s\n", "Jenis: " + jenisKayu, "Tgl: " + tglStickBundle));
                printer.addText(String.format("%-18s %-20s\n", "Plat: " + platTruk, "Telly: " + tellyBy));
                printer.addText(String.format("%-18s %-20s\n", "SPK: " + noSPK, "Stick: " + stickBy));
                printer.addFeedLine(1);

                // === INFO TAMBAHAN (Kayu Bulat / Pembelian / Upah) ===
                if (labelVersion == 1 || noPenST.startsWith("BA")) {
                    printer.addText("No. Pbl: " + noPenST + "\n");
                    printer.addText("Supplier: " + namaSupplier + "\n");
                    printer.addText("No. Truk: " + noTruk + "\n");
                } else if (labelVersion == 2 || noPenST.startsWith("O")) {
                    printer.addText("No. Upah: " + noPenST + "\n");
                    printer.addText("Customer: " + customer + "\n");
                    printer.addText("No. Truk: " + noTruk + "\n");
                } else {
                    printer.addText("No. KB: " + noKayuBulat + "\n");
                    printer.addText("Supplier: " + namaSupplier + "\n");
                    printer.addText("No. Truk: " + noTruk + "\n");
                }
                printer.addFeedLine(1);

                // === SEPARATOR ===
                printer.addText("========================================\n");

                // === TABLE HEADER ===
                printer.addTextAlign(Printer.ALIGN_CENTER);
                printer.addTextStyle(Printer.FALSE, Printer.TRUE, Printer.FALSE, Printer.COLOR_1);
                printer.addText(String.format("%-10s %-10s %-10s %-8s\n", "Tebal", "Lebar", "Panjang", "Pcs"));
                printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.COLOR_1);
                printer.addText("----------------------------------------\n");

                // === TABLE DATA ===
                DecimalFormat df = new DecimalFormat("#,###.##");
                for (LabelDetailData row : detailData) {
                    String tebal = row.getTebal() != null ? df.format(Float.parseFloat(row.getTebal())) : "-";
                    String lebar = row.getLebar() != null ? df.format(Float.parseFloat(row.getLebar())) : "-";
                    String panjang = row.getPanjang() != null ? df.format(Float.parseFloat(row.getPanjang())) : "-";
                    String pcs = row.getPcs() != null ? df.format(Integer.parseInt(row.getPcs())) : "-";

                    String tebalStr = tebal + " " + idUOMTblLebar;
                    String lebarStr = lebar + " " + idUOMTblLebar;
                    String panjangStr = panjang + " " + idUOMPanjang;

                    printer.addText(String.format("%-10s %-10s %-10s %-8s\n", tebalStr, lebarStr, panjangStr, pcs));
                }

                // === SEPARATOR ===
                printer.addText("========================================\n");

                // === SUMMARY ===
                printer.addTextAlign(Printer.ALIGN_RIGHT);
                printer.addText("Jumlah : " + jumlahPcs + "\n");
                printer.addText("Ton    : " + ton + "\n");
                printer.addText("m3     : " + m3 + "\n");
                printer.addFeedLine(2);

                // === REMARK (jika ada) ===
                if (!remark.isEmpty() && !remark.equals("-")) {
                    printer.addTextAlign(Printer.ALIGN_CENTER);
                    printer.addText("Remark: " + remark + "\n");
                    printer.addFeedLine(1);
                }

                // === QR CODE ===
                Bitmap qrBitmap = generateQRCode(noST, 200);
                if (qrBitmap != null) {
                    printer.addTextAlign(Printer.ALIGN_CENTER);
                    printer.addImage(
                            qrBitmap, 0, 0,
                            qrBitmap.getWidth(), qrBitmap.getHeight(),
                            Printer.COLOR_1, Printer.MODE_MONO,
                            Printer.HALFTONE_DITHER,
                            Printer.PARAM_DEFAULT,
                            Printer.COMPRESS_NONE
                    );
                    qrBitmap.recycle();
                }

                printer.addFeedLine(1);
                printer.addTextAlign(Printer.ALIGN_CENTER);
                printer.addTextSize(2, 2);
                printer.addText(noST + "\n");
                printer.addTextSize(1, 1);

                // === LABEL SLP (jika isSLP == 1) ===
                if (isSLP == 1) {
                    printer.addFeedLine(1);
                    printer.addTextAlign(Printer.ALIGN_RIGHT);
                    printer.addTextSize(3, 3);
                    printer.addTextStyle(Printer.FALSE, Printer.TRUE, Printer.FALSE, Printer.COLOR_1);
                    printer.addText("SLP\n");
                    printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.COLOR_1);
                    printer.addTextSize(1, 1);
                }

                // === SEPARATOR AKHIR (Dotted Line) ===
                printer.addFeedLine(2);
                printer.addTextAlign(Printer.ALIGN_CENTER);
                printer.addText("- - - - - - - - - - - - - - - - - - - -\n");
                printer.addFeedLine(5);

                // === SEND PRINT DATA ===
                Log.d(TAG, "Sending print data...");
                printer.sendData(Printer.PARAM_DEFAULT);

            } catch (Exception e) {
                e.printStackTrace();
                notifyError("Print failed: " + e.getMessage());
                disconnectPrinter();
            }
        }).start();
    }

    // === HELPER: Generate QR Code Bitmap ===
    private Bitmap generateQRCode(String text, int size) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size);

            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;

        } catch (WriterException e) {
            Log.e(TAG, "Failed to generate QR code: " + e.getMessage());
            return null;
        }
    }

    public void testPrintText(String printerTarget, PrintCallback callback) {
        this.callback = callback;

        new Thread(() -> {
            try {
                Log.d(TAG, "Starting test text print");
                initializePrinter();
                connectPrinter(printerTarget);

                printer.addTextAlign(Printer.ALIGN_CENTER);
                printer.addTextSize(2, 2);
                printer.addText("=== TEST PRINT ===\n");
                printer.addTextSize(1, 1);
                printer.addText("TM-U220B Test\n");
                printer.addText("Printer Working!\n");
                printer.addText("Date: " + new java.util.Date().toString() + "\n");
                printer.addFeedLine(5);

                Log.d(TAG, "Sending test text...");
                printer.sendData(Printer.PARAM_DEFAULT);

            } catch (Exception e) {
                e.printStackTrace();
                notifyError("Test print failed: " + e.getMessage());
                disconnectPrinter();
            }
        }).start();
    }


    private void initializePrinter() throws Epos2Exception {
        if (printer != null) {
            Log.d(TAG, "Disconnecting existing printer instance");
            disconnectPrinter();
        }

        notifyProgress("Menginisialisasi printer...");
        Log.d(TAG, "Initializing printer: TM_U220");

        printer = new Printer(
                Printer.TM_U220,
                Printer.MODEL_ANK,
                context
        );

        printer.setReceiveEventListener(new ReceiveListener() {
            @Override
            public void onPtrReceive(Printer printer, int code, PrinterStatusInfo status, String printJobId) {
                Log.d(TAG, "Print response code: " + code);
                logPrinterStatus(status);

                new Handler(Looper.getMainLooper()).post(() -> {
                    if (code == 0) {
                        Log.d(TAG, "✅ Print success!");
                        if (callback != null) {
                            callback.onPrintSuccess();
                        }
                    } else {
                        String errorMsg = getErrorMessage(code);
                        Log.e(TAG, "❌ Print failed: " + errorMsg);
                        if (callback != null) {
                            callback.onPrintError("Print failed: " + errorMsg);
                        }
                    }
                    disconnectPrinter();
                });
            }
        });

        Log.d(TAG, "Printer initialized successfully");
    }

    private void connectPrinter(String target) throws Epos2Exception {
        notifyProgress("Menghubungkan ke printer...");
        Log.d(TAG, "Connecting to: " + target);

        try {
            if (printer != null) {
                try {
                    PrinterStatusInfo status = printer.getStatus();
                    if (status != null && status.getConnection() == Printer.TRUE) {
                        Log.d(TAG, "Previous connection found, disconnecting...");
                        printer.disconnect();
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    Log.w(TAG, "Could not check previous connection: " + e.getMessage());
                }
            }

            Log.d(TAG, "Attempting connection...");
            printer.connect(target, Printer.PARAM_DEFAULT);
            Log.d(TAG, "Connected successfully");
            notifyProgress("Terhubung. Mempersiapkan print...");

            Thread.sleep(1000);

            PrinterStatusInfo status = printer.getStatus();
            logPrinterStatus(status);

            if (status == null || status.getConnection() == Printer.FALSE) {
                throw new Epos2Exception(Epos2Exception.ERR_DISCONNECT);
            }

            if (status.getOnline() == Printer.FALSE) {
                Log.w(TAG, "Warning: Printer reports offline, attempting to continue...");
            }

            Log.d(TAG, "Printer connection verified");

        } catch (InterruptedException e) {
            Log.e(TAG, "Connection interrupted", e);
            throw new Epos2Exception(Epos2Exception.ERR_PROCESSING);
        } catch (Epos2Exception e) {
            Log.e(TAG, "Epos2Exception during connect: " + e.getErrorStatus(), e);
            throw e;
        }
    }

    // ✅ UPDATED: Direct 1:1 rendering (NO SCALING)
    private void printPdfPages(Uri pdfUri) throws IOException, Epos2Exception {
        ParcelFileDescriptor fileDescriptor = null;
        PdfRenderer pdfRenderer = null;

        try {
            fileDescriptor = context.getContentResolver().openFileDescriptor(pdfUri, "r");
            if (fileDescriptor == null) {
                throw new IOException("Failed to open PDF");
            }

            pdfRenderer = new PdfRenderer(fileDescriptor);
            int pageCount = pdfRenderer.getPageCount();

            notifyProgress("Memproses " + pageCount + " halaman...");

            for (int i = 0; i < pageCount; i++) {
                PdfRenderer.Page page = null;
                Bitmap renderedBitmap = null;
                Bitmap monoBitmap = null;

                try {
                    page = pdfRenderer.openPage(i);

                    // ✅ PDF sekarang 420 points, render DIRECT 1:1
                    int pdfWidth = page.getWidth();   // 420 from PDF
                    int pdfHeight = page.getHeight();

                    Log.d(TAG, "=== Page " + (i + 1) + " ===");
                    Log.d(TAG, "PDF Size: " + pdfWidth + "x" + pdfHeight);
                    Log.d(TAG, "Rendering: DIRECT 1:1 (NO SCALING)");

                    // ✅ Render dengan ukuran ASLI PDF (420 points)
                    renderedBitmap = Bitmap.createBitmap(pdfWidth, pdfHeight, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(renderedBitmap);
                    canvas.drawColor(Color.WHITE);

                    // ✅ RENDER_MODE_FOR_PRINT
                    page.render(renderedBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);

                    // ✅ Convert langsung ke B&W (NO SCALING!)
                    monoBitmap = convertToMonochromeWithDithering(renderedBitmap);

                    int finalWidth = monoBitmap.getWidth();
                    int finalHeight = monoBitmap.getHeight();

                    Log.d(TAG, "Final bitmap: " + finalWidth + "x" + finalHeight);

                    notifyProgress("Mencetak halaman " + (i + 1) + "/" + pageCount);

                    // ✅ Print EXACT SIZE (420 dots width)
                    printer.addImage(
                            monoBitmap,
                            0, 0,
                            finalWidth,
                            finalHeight,
                            Printer.COLOR_1,
                            Printer.MODE_MONO,
                            Printer.HALFTONE_DITHER,
                            Printer.PARAM_DEFAULT,
                            Printer.COMPRESS_NONE
                    );

                    Log.d(TAG, "Page " + (i + 1) + " sent to printer successfully");

                    if (i < pageCount - 1) {
                        printer.addFeedLine(3);
                    }

                } catch (Epos2Exception e) {
                    Log.e(TAG, "Epos2Exception on page " + (i + 1) + ": " + getErrorMessage(e.getErrorStatus()), e);
                    throw e;

                } catch (Exception e) {
                    Log.e(TAG, "Error processing page " + (i + 1) + ": " + e.getMessage(), e);
                    throw e;

                } finally {
                    // ✅ CLEANUP BITMAPS
                    if (monoBitmap != null && !monoBitmap.isRecycled()) {
                        monoBitmap.recycle();
                        monoBitmap = null;
                    }
                    if (renderedBitmap != null && !renderedBitmap.isRecycled()) {
                        renderedBitmap.recycle();
                        renderedBitmap = null;
                    }

                    // ✅ CLOSE PAGE
                    if (page != null) {
                        try {
                            page.close();
                            Log.d(TAG, "Page " + (i + 1) + " closed successfully");
                        } catch (Exception e) {
                            Log.e(TAG, "Error closing page " + (i + 1) + ": " + e.getMessage());
                        }
                        page = null;
                    }
                }
            }

            // ✅ Send print data
            printer.addFeedLine(5);

            Log.d(TAG, "Sending print data to printer...");
            printer.sendData(Printer.PARAM_DEFAULT);
            Log.d(TAG, "✅ Print data sent successfully");

        } finally {
            // ✅ CLEANUP RENDERER & FILE
            if (pdfRenderer != null) {
                try {
                    pdfRenderer.close();
                    Log.d(TAG, "PDF Renderer closed");
                } catch (Exception e) {
                    Log.e(TAG, "Error closing PDF renderer: " + e.getMessage());
                }
            }
            if (fileDescriptor != null) {
                try {
                    fileDescriptor.close();
                    Log.d(TAG, "File descriptor closed");
                } catch (IOException e) {
                    Log.e(TAG, "Error closing file descriptor: " + e.getMessage());
                }
            }
        }
    }

    // ✅ Floyd-Steinberg Dithering (SAME AS BEFORE)
    private Bitmap convertToMonochromeWithDithering(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();

        int[][] grayPixels = new int[height][width];

        // ✅ Weighted grayscale (ITU-R BT.601 standard)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = source.getPixel(x, y);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);
                // Luminance formula
                grayPixels[y][x] = (int)(0.299 * r + 0.587 * g + 0.114 * b);
            }
        }

        // ✅ Floyd-Steinberg Dithering
        int threshold = 128;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int oldPixel = grayPixels[y][x];
                int newPixel = (oldPixel < threshold) ? 0 : 255;
                grayPixels[y][x] = newPixel;

                int error = oldPixel - newPixel;

                // Distribute error ke pixel sekitar
                if (x + 1 < width) {
                    grayPixels[y][x + 1] = clamp(grayPixels[y][x + 1] + error * 7 / 16);
                }
                if (y + 1 < height) {
                    if (x > 0) {
                        grayPixels[y + 1][x - 1] = clamp(grayPixels[y + 1][x - 1] + error * 3 / 16);
                    }
                    grayPixels[y + 1][x] = clamp(grayPixels[y + 1][x] + error * 5 / 16);
                    if (x + 1 < width) {
                        grayPixels[y + 1][x + 1] = clamp(grayPixels[y + 1][x + 1] + error * 1 / 16);
                    }
                }
            }
        }

        // Convert ke Bitmap
        Bitmap monoBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = (grayPixels[y][x] == 0) ? Color.BLACK : Color.WHITE;
                monoBitmap.setPixel(x, y, color);
            }
        }

        return monoBitmap;
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    private void disconnectPrinter() {
        if (printer != null) {
            try {
                Log.d(TAG, "Checking printer status before disconnect...");
                PrinterStatusInfo status = printer.getStatus();
                if (status != null && status.getConnection() == Printer.TRUE) {
                    Log.d(TAG, "Disconnecting printer...");
                    printer.disconnect();
                    Log.d(TAG, "Disconnected successfully");
                } else {
                    Log.d(TAG, "Printer already disconnected");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error during disconnect (ignored): " + e.getMessage());
            }

            try {
                Log.d(TAG, "Clearing command buffer...");
                printer.clearCommandBuffer();
            } catch (Exception e) {
                Log.e(TAG, "Error clearing buffer (ignored): " + e.getMessage());
            }

            try {
                printer.setReceiveEventListener(null);
            } catch (Exception e) {
                Log.e(TAG, "Error removing listener (ignored): " + e.getMessage());
            }

            printer = null;
            Log.d(TAG, "Printer instance cleared");
        }
    }

    private String getErrorMessage(int code) {
        switch (code) {
            case 0: return "Success";
            case Epos2Exception.ERR_PARAM: return "Parameter error";
            case Epos2Exception.ERR_CONNECT: return "Connection error - Cek IP & network";
            case Epos2Exception.ERR_TIMEOUT: return "Timeout - Printer tidak merespon";
            case Epos2Exception.ERR_MEMORY: return "Memory full";
            case Epos2Exception.ERR_ILLEGAL: return "Illegal operation";
            case Epos2Exception.ERR_PROCESSING: return "Processing error";
            case Epos2Exception.ERR_NOT_FOUND: return "Printer not found - Cek IP printer";
            case Epos2Exception.ERR_IN_USE: return "Printer sedang digunakan";
            case Epos2Exception.ERR_TYPE_INVALID: return "Invalid printer type";
            case Epos2Exception.ERR_DISCONNECT: return "Disconnected - Koneksi terputus";
            case Epos2Exception.ERR_ALREADY_OPENED: return "Already opened";
            case Epos2Exception.ERR_ALREADY_USED: return "Already used";
            case Epos2Exception.ERR_BOX_COUNT_OVER: return "Box count over";
            case Epos2Exception.ERR_BOX_CLIENT_OVER: return "Box client over";
            case Epos2Exception.ERR_UNSUPPORTED: return "Unsupported";
            case Epos2Exception.ERR_FAILURE: return "General failure";
            case Epos2Exception.ERR_RECOVERY_FAILURE: return "Recovery failure";
            default: return "Unknown error (code: " + code + ")";
        }
    }

    private void logPrinterStatus(PrinterStatusInfo status) {
        if (status == null) {
            Log.w(TAG, "Printer status is null");
            return;
        }

        Log.d(TAG, "=== Printer Status ===");
        Log.d(TAG, "Connection: " + (status.getConnection() == Printer.TRUE ? "Connected" : "Disconnected"));
        Log.d(TAG, "Online: " + (status.getOnline() == Printer.TRUE ? "Yes" : "No"));
        Log.d(TAG, "Cover Open: " + (status.getCoverOpen() == Printer.TRUE ? "Yes" : "No"));
        Log.d(TAG, "Paper: " + (status.getPaper() == Printer.PAPER_OK ? "OK" : "Out/Near End"));
        Log.d(TAG, "PaperFeed: " + (status.getPaperFeed() == Printer.TRUE ? "Yes" : "No"));
        Log.d(TAG, "ErrorStatus: " + status.getErrorStatus());
        Log.d(TAG, "=====================");
    }

    private void notifyProgress(String message) {
        if (callback != null) {
            new Handler(Looper.getMainLooper()).post(() -> {
                callback.onPrintProgress(message);
            });
        }
    }

    private void notifyError(String error) {
        if (callback != null) {
            new Handler(Looper.getMainLooper()).post(() -> {
                callback.onPrintError(error);
            });
        }
    }

    public void cancelPrint() {
        disconnectPrinter();
    }
}