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

import java.io.IOException;

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

    // METHOD BARU: Test print text untuk verify printer
    public void testPrintText(String printerTarget, PrintCallback callback) {
        this.callback = callback;

        new Thread(() -> {
            try {
                Log.d(TAG, "Starting test text print");
                initializePrinter();
                connectPrinter(printerTarget);

                // Simple text print untuk test printer
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
                        Log.d(TAG, "Print success!");
                        if (callback != null) {
                            callback.onPrintSuccess();
                        }
                    } else {
                        String errorMsg = getErrorMessage(code);
                        Log.e(TAG, "Print failed: " + errorMsg);
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
            // Clear any previous connection state
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

            // Delay untuk stabilitas
            Thread.sleep(1000);

            // Check printer status
            PrinterStatusInfo status = printer.getStatus();
            logPrinterStatus(status);

            if (status == null || status.getConnection() == Printer.FALSE) {
                throw new Epos2Exception(Epos2Exception.ERR_DISCONNECT);
            }

            // Warning jika offline tapi tetap lanjut (beberapa printer report offline padahal ready)
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

    private void printPdfPages(Uri pdfUri) throws IOException, Epos2Exception {
        ParcelFileDescriptor fileDescriptor = null;
        PdfRenderer pdfRenderer = null;

        try {
            Log.d(TAG, "Opening PDF: " + pdfUri.toString());
            fileDescriptor = context.getContentResolver().openFileDescriptor(pdfUri, "r");
            if (fileDescriptor == null) {
                throw new IOException("Failed to open PDF");
            }

            pdfRenderer = new PdfRenderer(fileDescriptor);
            int pageCount = pdfRenderer.getPageCount();
            Log.d(TAG, "PDF has " + pageCount + " page(s)");

            notifyProgress("Memproses " + pageCount + " halaman...");

            for (int i = 0; i < pageCount; i++) {
                Log.d(TAG, "Processing page " + (i + 1) + "/" + pageCount);
                PdfRenderer.Page page = pdfRenderer.openPage(i);

                int originalWidth = page.getWidth();
                int originalHeight = page.getHeight();
                Log.d(TAG, "Original page size: " + originalWidth + "x" + originalHeight);

                // TM-U220B: 384 dots width (safer untuk dot matrix)
                int printWidth = 384;
                int printHeight = (int) ((float) originalHeight / originalWidth * printWidth);

                // Limit height max 800 dots
                if (printHeight > 800) {
                    printHeight = 800;
                    printWidth = (int) ((float) originalWidth / originalHeight * printHeight);
                }

                Log.d(TAG, "Scaled size for printing: " + printWidth + "x" + printHeight);

                // Create bitmap dengan white background
                Bitmap bitmap = Bitmap.createBitmap(printWidth, printHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawColor(Color.WHITE);

                // Render PDF
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);

                // Convert ke monochrome
                Bitmap monoBitmap = convertToMonochrome(bitmap);
                bitmap.recycle();

                notifyProgress("Mencetak halaman " + (i + 1) + "/" + pageCount + "...");
                Log.d(TAG, "Adding image to print buffer...");

                // Add ke printer dengan setting optimal untuk dot matrix
                printer.addImage(
                        monoBitmap,
                        0,
                        0,
                        printWidth,
                        printHeight,
                        Printer.COLOR_1,
                        Printer.MODE_MONO,
                        Printer.HALFTONE_THRESHOLD,  // Threshold lebih baik untuk dot matrix
                        Printer.PARAM_DEFAULT,
                        Printer.COMPRESS_NONE        // No compression untuk dot matrix
                );

                page.close();
                monoBitmap.recycle();
                Log.d(TAG, "Page " + (i + 1) + " added to buffer");

                if (i < pageCount - 1) {
                    printer.addFeedLine(3);
                }
            }

            Log.d(TAG, "Adding feed lines");
            printer.addFeedLine(5);

            notifyProgress("Mengirim ke printer...");
            Log.d(TAG, "Sending data to printer...");
            printer.sendData(Printer.PARAM_DEFAULT);
            Log.d(TAG, "Data sent successfully, waiting for callback...");

        } finally {
            if (pdfRenderer != null) {
                pdfRenderer.close();
            }
            if (fileDescriptor != null) {
                fileDescriptor.close();
            }
        }
    }

    // METHOD BARU: Convert ke pure black & white
    private Bitmap convertToMonochrome(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap monoBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Threshold: 128 = medium (adjust kalau terlalu terang/gelap)
        int threshold = 128;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = source.getPixel(x, y);

                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);
                int gray = (r + g + b) / 3;

                // Pure black or white
                int newPixel = (gray < threshold) ? Color.BLACK : Color.WHITE;
                monoBitmap.setPixel(x, y, newPixel);
            }
        }

        return monoBitmap;
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