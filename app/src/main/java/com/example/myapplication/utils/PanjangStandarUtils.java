package com.example.myapplication.utils;

import android.content.Context;
import android.text.Html;

import androidx.appcompat.app.AlertDialog;

import java.util.Locale;

public class PanjangStandarUtils {

    // Hardcoded standar panjang (meter)
    private static final double[] PANJANG_STANDAR = {1.0, 2.0, 2.5, 3.0, 3.5, 4.0};
    private static final double EPS = 0.0001;

    private PanjangStandarUtils() {
        // no instance
    }

    public static boolean isPanjangStandar(double value) {
        for (double s : PANJANG_STANDAR) {
            if (Math.abs(value - s) < EPS) return true;
        }
        return false;
    }

    /** String tampilan standar: "1, 2, 2.5, 3, 3.5, 4" */
    public static String standarText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < PANJANG_STANDAR.length; i++) {
            double v = PANJANG_STANDAR[i];

            String txt = (v == (long) v)
                    ? String.format(Locale.US, "%d", (long) v)
                    : String.format(Locale.US, "%s", v);

            if (i > 0) sb.append(", ");

            // Tambahkan tag bold di sini
            sb.append("<b>").append(txt).append("</b>");
        }
        return sb.toString();
    }

    /**
     * Jika panjang di luar standar, tampilkan dialog konfirmasi.
     * Return true jika dialog ditampilkan (artinya user harus pilih Ya/Tidak),
     * Return false jika panjang sudah standar (tidak perlu dialog).
     */
    public static boolean confirmIfNotStandard(Context ctx, double panjangValue, String panjangText, Runnable onYes) {
        if (isPanjangStandar(panjangValue)) return false;

        // Buat string HTML utuh
        String fullMessage = "Panjang " + panjangText + " tidak termasuk standar (" + standarText() + ").<br><br>" +
                "Apakah yakin tetap ingin input?";

        new AlertDialog.Builder(ctx)
                .setTitle("⚠️ Konfirmasi Ukuran")
                // Gunakan Html.fromHtml untuk me-render format bold
                .setMessage(Html.fromHtml(fullMessage, Html.FROM_HTML_MODE_LEGACY))
                .setPositiveButton("Ya, Lanjutkan", (dialog, which) -> onYes.run())
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();

        return true;
    }
}
