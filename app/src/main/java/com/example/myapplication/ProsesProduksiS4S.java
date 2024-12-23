package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.example.myapplication.api.ProductionApi;
import com.example.myapplication.model.ProductionData;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProsesProduksiS4S extends AppCompatActivity {

    private TableLayout tableLayout;
    private PreviewView cameraPreview; // Komponen Preview Kamera
    private ProcessCameraProvider cameraProvider;
    private Camera camera;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private TableLayout scanResultsTable;
    private int scanResultCount = 0; // Counter untuk hasil scan
    private List<String> scannedResults = new ArrayList<>(); // Untuk menyimpan hasil scan unik


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_produksi_s4_s);

        tableLayout = findViewById(R.id.tableLayout);
        cameraPreview = findViewById(R.id.cameraPreview); // Hubungkan dengan layout XML Anda
        scanResultsTable = findViewById(R.id.scanResultsTable); // Tabel hasil scan


        executorService.execute(() -> {
            List<ProductionData> productionDataList = ProductionApi.getProductionData();

            runOnUiThread(() -> {
                populateTable(productionDataList);
            });
        });

        // Memulai kamera dengan ZXing
        startCamera();
    }

    private void addScanResultToTable(String result) {
        runOnUiThread(() -> {
            if (!scannedResults.contains(result)) {
                // Tambahkan hasil ke daftar
                scannedResults.add(result);

                // Tambahkan baris baru ke tabel
                TableRow row = new TableRow(this);

                // Kolom No.
                TextView numberView = new TextView(this);
                numberView.setText(String.valueOf(scannedResults.size())); // Nomor berdasarkan ukuran daftar
                numberView.setPadding(8, 8, 8, 8);
                numberView.setGravity(Gravity.CENTER);

                // Kolom Hasil Scan
                TextView resultView = new TextView(this);
                resultView.setText(result);
                resultView.setPadding(8, 8, 8, 8);
                resultView.setGravity(Gravity.START);

                // Tambahkan kolom ke baris
                row.addView(numberView);
                row.addView(resultView);

                // Tambahkan baris ke tabel dengan ID scanResultsTable
                scanResultsTable.addView(row);
            } else {
                Log.d("DuplicateScan", "Hasil scan sudah ada: " + result);
            }
        });
    }



    private void populateTable(List<ProductionData> dataList) {
        Log.d("PopulateTable", "Start");

        if (dataList == null || dataList.isEmpty()) {
            Log.w("PopulateTable", "Data list is empty or null");
            TextView noDataView = new TextView(this);
            noDataView.setText("Data tidak ditemukan");
            noDataView.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            tableLayout.addView(noDataView);
            return;
        }

        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(ContextCompat.getColor(this, R.color.hijau));

        TextView headerNoProd = createTextView("No. Produksi", true);
        TextView headerShift = createTextView("Shift", true);
        TextView headerTanggal = createTextView("Tanggal", true);
        TextView headerMesin = createTextView("Mesin", true);
        TextView headerOperator = createTextView("Operator", true);

        headerRow.addView(headerNoProd);
        headerRow.addView(headerShift);
        headerRow.addView(headerTanggal);
        headerRow.addView(headerMesin);
        headerRow.addView(headerOperator);

        tableLayout.addView(headerRow);

        for (int i = 0; i < dataList.size(); i++) {
            ProductionData data = dataList.get(i);
            Log.d("PopulateTable", "Adding row for NoProduksi: " + data.getNoProduksi());

            CardView cardView = new CardView(this);
            cardView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            int margin = getResources().getDimensionPixelSize(R.dimen.margin_small);
            cardView.setContentPadding(margin, margin, margin, margin);
            cardView.setCardElevation(4);
            if (i % 2 == 0) {
                cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
            } else {
                cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            }

            TableRow row = new TableRow(this);

            TextView noProdView = createTextView(data.getNoProduksi(), false);
            TextView shiftView = createTextView(data.getShift(), false);
            TextView tanggalView = createTextView(data.getTanggal(), false);
            TextView mesinView = createTextView(data.getMesin(), false);
            TextView operatorView = createTextView(data.getOperator(), false);

            noProdView.setGravity(Gravity.CENTER);
            shiftView.setGravity(Gravity.CENTER);
            tanggalView.setGravity(Gravity.CENTER);
            mesinView.setGravity(Gravity.CENTER);
            operatorView.setGravity(Gravity.CENTER);

            row.addView(noProdView);
            row.addView(shiftView);
            row.addView(tanggalView);
            row.addView(mesinView);
            row.addView(operatorView);

            cardView.addView(row);

            tableLayout.addView(cardView);
        }

        Log.d("PopulateTable", "End");
    }

    // Konfigurasi dan memulai CameraX dengan ZXing Analyzer
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                        .build();

                androidx.camera.core.Preview preview = new androidx.camera.core.Preview.Builder()
                        .build();
                preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(getExecutor(), new ZXingAnalyzer());

                camera = cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageAnalysis);

                Log.d("Camera", "Camera started successfully");

            } catch (Exception e) {
                Log.e("CameraError", "Error starting camera: " + e.getMessage(), e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    // ZXingAnalyzer untuk membaca QR Code
    private class ZXingAnalyzer implements ImageAnalysis.Analyzer {
        @Override
        public void analyze(@NonNull ImageProxy imageProxy) {
            try {
                ImageProxy.PlaneProxy[] planes = imageProxy.getPlanes();
                ByteBuffer buffer = planes[0].getBuffer();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);

                int width = imageProxy.getWidth();
                int height = imageProxy.getHeight();
                LuminanceSource source = new PlanarYUVLuminanceSource(
                        data, width, height,
                        0, 0, width, height,
                        false);

                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                Reader reader = new QRCodeReader();

                try {
                    Result result = reader.decode(bitmap);
                    Log.d("ZXingAnalyzer", "QR Code detected: " + result.getText());
                    addScanResultToTable(result.getText()); // Tambahkan hasil ke tabel jika unik
                } catch (Exception e) {
                    Log.d("ZXingAnalyzer", "No QR Code found in this frame.");
                }
            } finally {
                imageProxy.close();
            }
        }
    }



    private TextView createTextView(String text, boolean isHeader) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        textView.setPadding(16, 16, 16, 16);
        if (isHeader) {
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
        }
        return textView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
        executorService.shutdown();
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }
}
