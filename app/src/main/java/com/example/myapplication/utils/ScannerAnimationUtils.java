package com.example.myapplication.utils;

import android.animation.ObjectAnimator;
import android.util.DisplayMetrics;
import android.view.View;

public class ScannerAnimationUtils {

    private static ObjectAnimator animator; // Animator global untuk mengontrol animasi

    /**
     * Memulai animasi scanning pada View scannerOverlay.
     *
     * @param scannerOverlay View yang akan dianimasikan
     * @param displayMetrics DisplayMetrics untuk mendapatkan tinggi layar
     */
    public static void startScanningAnimation(View scannerOverlay, DisplayMetrics displayMetrics) {
        // Hentikan animasi lama jika ada
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }

        // Dapatkan tinggi layar
        float screenHeight = displayMetrics.heightPixels;

        // Buat animator untuk memindahkan overlay naik turun
        animator = ObjectAnimator.ofFloat(
                scannerOverlay,
                "translationY",
                0f,
                screenHeight
        );

        // Konfigurasi animasi
        animator.setDuration(1500); // Durasi animasi (ms)
        animator.setRepeatMode(ObjectAnimator.REVERSE); // Animasi bolak-balik
        animator.setRepeatCount(ObjectAnimator.INFINITE); // Ulang terus

        // Mulai animasi
        animator.start();
    }

    /**
     * Menghentikan animasi scanning pada View scannerOverlay.
     */
    public static void stopScanningAnimation() {
        if (animator != null && animator.isRunning()) {
            animator.cancel(); // Hentikan animasi
            animator = null;   // Bebaskan referensi animator
        }
    }
}
