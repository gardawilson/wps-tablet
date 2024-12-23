package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.example.myapplication.utils.CameraXAnalyzer;
import com.google.common.util.concurrent.ListenableFuture;

public class QRCodeScanActivity extends AppCompatActivity {

    private PreviewView previewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_qr_code_scan);
//
//        previewView = findViewById(R.id.previewView);

        startCamera();
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll();

                CameraXAnalyzer analyzer = new CameraXAnalyzer(result -> {
                    Log.d("QRCodeScanActivity1", "QR Code Detected: " + result);
                    // Logika khusus untuk Halaman 1
                    handleQRCodeForPage1(result);
                });

                CameraUtils.setupCamera(this, previewView, cameraProvider, analyzer);
            } catch (Exception e) {
                Log.e("QRCodeScanActivity1", "Error starting camera: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void handleQRCodeForPage1(String result) {
        // Implementasi logika untuk QR Code di Halaman 1
        Log.d("QRCodeScanActivity1", "Handling result in Page 1: " + result);
    }
}
