package com.example.myapplication.utils;

import android.app.Activity;
import android.util.Log;

import com.example.myapplication.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RiwayatUtils {

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public interface RiwayatCallback {
        void onSuccess();
        void onError(String errorMessage);
    }

    /**
     * Simpan aktivitas user ke tabel Riwayat
     * @param activity Activity context untuk runOnUiThread
     * @param username NIP user
     * @param activityDesc Deskripsi aktivitas
     * @param callback Callback untuk handling result
     */
    public static void saveToRiwayat(Activity activity, String username, String activityDesc, RiwayatCallback callback) {
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        saveToRiwayat(activity, username, currentDateTime, activityDesc, callback);
    }

    /**
     * Simpan aktivitas user ke tabel Riwayat dengan timestamp custom
     * @param activity Activity context untuk runOnUiThread
     * @param username NIP user
     * @param dateTime Custom timestamp
     * @param activityDesc Deskripsi aktivitas
     * @param callback Callback untuk handling result
     */
    public static void saveToRiwayat(Activity activity, String username, String dateTime, String activityDesc, RiwayatCallback callback) {
        executorService.execute(() -> {
            boolean success = false;
            String errorMessage = null;

            Connection con = getConnection();

            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.Riwayat (Nip, Tgl, Aktivitas) VALUES (?, ?, ?)";
                    Log.d("SQL Query", "Executing query: " + query);

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, username);
                    ps.setString(2, dateTime);
                    ps.setString(3, activityDesc);

                    int rowsAffected = ps.executeUpdate();
                    Log.d("Database", "Rows affected: " + rowsAffected);

                    ps.close();
                    con.close();

                    success = rowsAffected > 0;
                    Log.d("Riwayat", "Data successfully inserted into Riwayat.");

                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                    errorMessage = e.getMessage();
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
                errorMessage = "Failed to connect to the database.";
            }

            // Update UI di main thread
            final boolean finalSuccess = success;
            final String finalErrorMessage = errorMessage;

            if (activity != null && !activity.isDestroyed() && !activity.isFinishing()) {
                activity.runOnUiThread(() -> {
                    if (callback != null) {
                        if (finalSuccess) {
                            Log.d("Riwayat", "Data berhasil disimpan di Riwayat");
                            callback.onSuccess();
                        } else {
                            Log.e("Riwayat", "Gagal menyimpan data di Riwayat");
                            callback.onError(finalErrorMessage != null ? finalErrorMessage : "Unknown error");
                        }
                    }
                });
            }
        });
    }

    /**
     * Helper method untuk format aktivitas logout
     * @param username NIP user
     * @return Formatted logout activity string
     */
    public static String formatLogoutActivity(String username) {
        return String.format("User %s Telah Logout", username);
    }

    /**
     * Helper method untuk format aktivitas login
     * @param username NIP user
     * @return Formatted login activity string
     */
    public static String formatLoginActivity(String username) {
        return String.format("User %s Telah Login", username);
    }

    /**
     * Helper method untuk format aktivitas custom
     * @param username NIP user
     * @param action Aksi yang dilakukan
     * @return Formatted activity string
     */
    public static String formatActivity(String username, String action) {
        return String.format("User %s %s", username, action);
    }

    /**
     * Mendapatkan koneksi database
     * @return Connection object atau null jika gagal
     */
    private static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
        } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        }
        return con;
    }

    /**
     * Shutdown executor service ketika aplikasi ditutup
     * Panggil method ini di onDestroy() Application class
     */
    public static void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            Log.d("RiwayatUtils", "ExecutorService has been shutdown");
        }
    }
}