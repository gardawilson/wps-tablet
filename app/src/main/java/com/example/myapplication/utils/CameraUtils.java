package com.example.myapplication.utils;

import android.content.Context;
import androidx.core.content.ContextCompat;


import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;

public class CameraUtils {

    public static Camera setupCamera(
            Context context,
            PreviewView previewView,
            ProcessCameraProvider cameraProvider,
            ImageAnalysis.Analyzer analyzer) {

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        androidx.camera.core.Preview preview = new androidx.camera.core.Preview.Builder()
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), analyzer);

        return cameraProvider.bindToLifecycle(
                (androidx.lifecycle.LifecycleOwner) context,
                cameraSelector,
                preview,
                imageAnalysis);
    }
}
