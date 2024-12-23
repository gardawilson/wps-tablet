package com.example.myapplication.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import java.nio.ByteBuffer;

public class CameraXAnalyzer implements ImageAnalysis.Analyzer {

    private final OnQRCodeScannedListener listener;

    // Interface untuk mengirim hasil scan ke halaman yang berbeda
    public interface OnQRCodeScannedListener {
        void onQRCodeScanned(String result);
    }

    public CameraXAnalyzer(OnQRCodeScannedListener listener) {
        this.listener = listener;
    }

    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        try {
            ByteBuffer buffer = imageProxy.getPlanes()[0].getBuffer();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);

            int width = imageProxy.getWidth();
            int height = imageProxy.getHeight();

            String result = QRCodeUtils.decodeQRCode(data, width, height);
            if (result != null) {
                listener.onQRCodeScanned(result); // Kirim hasil ke halaman
            }
        } catch (Exception e) {
            Log.e("CameraXAnalyzer", "Error analyzing image: " + e.getMessage());
        } finally {
            imageProxy.close();
        }
    }
}
