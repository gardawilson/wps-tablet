package com.example.myapplication.utils;

import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public static String getCurrentDate() {
        // Ambil tanggal saat ini
        LocalDate today = LocalDate.now();

        // Format tanggal sesuai kebutuhan (contoh: "yyyy-MM-dd")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

        // Kembalikan hasil format
        return today.format(formatter);
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
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");

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

    public static String formatDateToDdYY(String dateString) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
            LocalDate localDate = LocalDate.parse(dateString, inputFormatter);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMyy", Locale.ENGLISH);
            return localDate.format(outputFormatter);

        } catch (DateTimeParseException e) {
            Log.e("DateFormatError", "Gagal memformat tanggal (dd-MMM-yyyy ke ddyy): " + e.getMessage());
            return "ERROR"; // Atau nilai default lain yang sesuai
        }
    }

    public static String formatTimeToHHmm(String timeString) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH);
            LocalTime localTime = LocalTime.parse(timeString, inputFormatter);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
            return localTime.format(outputFormatter);

        } catch (DateTimeParseException e) {
            Log.e("TimeFormatError", "Gagal memformat waktu (HH:mm:ss ke HH:mm): " + e.getMessage());
            return "ERROR"; // Atau nilai default lain yang sesuai
        }
    }


    // Format dari "dd-MMM-yyyy" ke "yyyy-MM-dd"
    public static String formatToDatabaseDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);

        } catch (ParseException e) {
            Log.e("DateFormatError", "Gagal memformat tanggal (dd-MMM-yyyy ke yyyy-MM-dd): " + e.getMessage());
            return "ERROR";
        }
    }

    public static String formatTimeToHHmmss(String timeString) {
        if (timeString == null || timeString.trim().isEmpty()) {
            Log.e("TimeFormatError", "Input waktu null atau kosong.");
            return "ERROR";
        }

        try {
            // Format input seperti 14:30:00.0000000
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSSS", Locale.ENGLISH);
            LocalTime localTime = LocalTime.parse(timeString, inputFormatter);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
            return localTime.format(outputFormatter);

        } catch (DateTimeParseException e) {
            Log.e("TimeFormatError", "Gagal memformat waktu (HH:mm:ss.SSSSSSS ke HH:mm): " + e.getMessage());
            return "ERROR";
        }
    }



}
