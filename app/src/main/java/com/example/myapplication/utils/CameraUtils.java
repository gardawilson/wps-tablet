package com.example.myapplication.utils;

import androidx.core.content.ContextCompat;
import android.content.Context;
import android.util.Log;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;

import com.google.common.util.concurrent.ListenableFuture;

public class CameraUtils {

    private static final String TAG = "CameraUtils";

    public static Camera setupCamera(
            Context context,
            PreviewView previewView,
            ImageAnalysis.Analyzer analyzer,
            int lensFacing,
            ListenableFuture<ProcessCameraProvider> cameraProviderFuture) {

        // Gunakan array untuk menyimpan referensi kamera
        final Camera[] cameraHolder = new Camera[1];

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(lensFacing)
                        .build();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), analyzer);

                // Simpan referensi kamera ke array
                cameraHolder[0] = cameraProvider.bindToLifecycle(
                        (androidx.lifecycle.LifecycleOwner) context,
                        cameraSelector,
                        preview,
                        imageAnalysis);

                Camera camera = cameraHolder[0];
                if (camera != null) {
                    camera.getCameraControl().setZoomRatio(3.0f); // Zoom 3x
                    Log.d(TAG, "Zoom ratio set to 2x.");
                }

                Log.d(TAG, "Camera setup successfully.");

            } catch (Exception e) {
                Log.e(TAG, "Error setting up camera: " + e.getMessage(), e);
            }
        }, ContextCompat.getMainExecutor(context));

        // Kembalikan referensi kamera dari array
        return cameraHolder[0];
    }
}
