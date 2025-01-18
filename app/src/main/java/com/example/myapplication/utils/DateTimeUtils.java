package com.example.myapplication.utils;

import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    // 1. Mendapatkan waktu saat ini dengan format tertentu
    public static String getCurrentDateTime() {
        // Ambil waktu saat ini
        LocalDateTime now = LocalDateTime.now();

        // Format waktu sesuai kebutuhan (contoh: "yyyy-MM-dd HH:mm:ss")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Kembalikan hasil format
        return now.format(formatter);
    }

    // 2. Memformat ulang dateTime dari satu format ke format lain
    public static String formatDateTime(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return "N/A"; // Tampilkan "N/A" jika input null atau kosong
        }

        try {
            // Format input dan output sesuai kebutuhan
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.getDefault());

            // Parsing input date
            Date date = inputFormat.parse(dateTime);

            // Format ulang ke output
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "N/A"; // Tampilkan "N/A" jika parsing gagal
        }
    }

    // Format tanggal dari "yyyy-MM-dd" ke "dd MMM yyyy"
    public static String formatDate(String originalDate) {
        String formattedDate;

        try {
            // Format input dan output
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");

            // Parsing tanggal dan format ulang
            Date date = inputFormat.parse(originalDate);
            formattedDate = outputFormat.format(date);
        } catch (Exception e) {
            Log.e("DateFormatError", "Gagal memformat tanggal: " + e.getMessage());
            // Jika gagal, gunakan tanggal asli untuk tampilan
            formattedDate = originalDate;
        }

        return formattedDate;
    }
}
