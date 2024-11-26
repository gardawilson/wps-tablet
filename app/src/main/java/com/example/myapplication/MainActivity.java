package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    // UI Components
    private EditText Username;
    private EditText Password;
    private Button BtnLogin;
    private Button BtnRegistrasi;
    private ProgressBar progressBar;

    // Update related
    private UpdateManager updateManager;
    private boolean isChecking = false;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize all views
        initializeViews();

        // Set click listeners
        setupClickListeners();

        // Start update check
        initializeUpdateManagerAndCheck();
    }

    private void initializeViews() {
        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.Password);
        BtnLogin = findViewById(R.id.BtnLogin);
        BtnRegistrasi = findViewById(R.id.BtnRegistrasi);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    private void setupClickListeners() {
        BtnLogin.setOnClickListener(v -> {
            String username = Username.getText().toString().trim();
            String password = Password.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                boolean isLoginSuccessful = validateLogin(username, password);

                runOnUiThread(() -> {
                    if (isLoginSuccessful) {
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, MenuUtama.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        BtnRegistrasi.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Registrasi.class);
            startActivity(intent);
        });
    }

    private void initializeUpdateManagerAndCheck() {
        if (isChecking) return; // Prevent multiple simultaneous checks

        isChecking = true;
//        enableLoginControls(false); // Disable controls during update check

        updateManager = new UpdateManager(this);
        updateManager.initialize(new UpdateManager.InitCallback() {
            @Override
            public void onInitComplete() {
                checkForUpdates();
            }

            @Override
            public void onInitFailed(String error) {
                runOnUiThread(() -> {
                    Log.e(TAG, "Failed to initialize UpdateManager: " + error);
                    Toast.makeText(MainActivity.this,
                            "Gagal memeriksa pembaruan: " + error,
                            Toast.LENGTH_LONG).show();
                    isChecking = false;
//                    enableLoginControls(true);
                    cleanupUpdateManager();
                });
            }
        });
    }

    private void checkForUpdates() {
        Log.d(TAG, "Starting update check from MainActivity");

        updateManager.checkForUpdates(new UpdateManager.UpdateCallback() {
            @Override
            public void onUpdateAvailable(UpdateManager.UpdateInfo updateInfo) {
                Log.d(TAG, "Update available: " + updateInfo.version);
                runOnUiThread(() -> {
                    isChecking = false;
                    showUpdateDialog(updateInfo);
                });
            }

            @Override
            public void onUpdateNotAvailable() {
                Log.d(TAG, "No update available");
                runOnUiThread(() -> {
                    isChecking = false;
//                    enableLoginControls(true);
                    cleanupUpdateManager();
                });
            }

            @Override
            public void onUpdateCheckFailed(String errorMessage) {
                Log.e(TAG, "Update check failed: " + errorMessage);
                runOnUiThread(() -> {
                    isChecking = false;
//                    enableLoginControls(true);
                    Toast.makeText(MainActivity.this,
                            "Gagal memeriksa pembaruan: " + errorMessage,
                            Toast.LENGTH_LONG).show();
                    cleanupUpdateManager();
                });
            }
        });
    }

    private void showUpdateDialog(UpdateManager.UpdateInfo updateInfo) {
        new AlertDialog.Builder(this)
                .setTitle("Pembaruan Tersedia")
                .setMessage("Versi " + updateInfo.version + " tersedia.\n\n" + "Changelog:\n" + updateInfo.changelog)
                .setPositiveButton("Update Sekarang", (dialog, which) -> {
                    dialog.dismiss();
                    startDownload(updateInfo.fileName);
                })
                .setNegativeButton("Nanti", (dialog, which) -> {
                    dialog.dismiss();
//                    enableLoginControls(true);
                    cleanupUpdateManager();
                })
                .setCancelable(false)
                .show();
    }

    private void startDownload(String fileName) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
//        enableLoginControls(false);

        updateManager.downloadUpdate(fileName, new UpdateManager.DownloadCallback() {
            @Override
            public void onDownloadProgress(int percentage) {
                runOnUiThread(() -> progressBar.setProgress(percentage));
            }

            @Override
            public void onDownloadComplete(File updateFile) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    installUpdate(updateFile);
                    cleanupUpdateManager();
                });
            }

            @Override
            public void onDownloadFailed(String errorMessage) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
//                    enableLoginControls(true);
                    Toast.makeText(MainActivity.this,
                            "Gagal mengunduh pembaruan: " + errorMessage,
                            Toast.LENGTH_LONG).show();
                    cleanupUpdateManager();
                });
            }
        });
    }

    private void installUpdate(File updateFile) {
        try {
            Log.d(TAG, "Installing update from: " + updateFile.getAbsolutePath());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".provider",
                    updateFile);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                throw new Exception("No activity found to handle APK installation");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error installing update: " + e.getMessage(), e);
            Toast.makeText(this, "Gagal menginstal pembaruan: " + e.getMessage(), Toast.LENGTH_LONG).show();
//            enableLoginControls(true);

            if (updateFile.exists()) {
                updateFile.delete();
            }
        }
    }

//    private void enableLoginControls(boolean enable) {
//        runOnUiThread(() -> {
//            Username.setEnabled(enable);
//            Password.setEnabled(enable);
//            BtnLogin.setEnabled(enable);
//            BtnRegistrasi.setEnabled(enable);
//        });
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanupUpdateManager();
    }

    private void cleanupUpdateManager() {
        if (updateManager != null) {
            executorService.execute(() -> {
                try {
                    updateManager.cleanup();
                    Log.d(TAG, "UpdateManager cleaned up successfully");
                } catch (Exception e) {
                    Log.e(TAG, "Error during UpdateManager cleanup", e);
                }
                updateManager = null;
            });
        }
    }

    @SuppressLint("NewApi")
    private Connection ConnectionClass() {
        Connection con = null;
        String ip = "192.168.10.100";
        String port = "1433";
        String username = "sa";
        String password = "Utama1234";
        String databasename = "WPS_Test";

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + databasename + ";User=" + username + ";password=" + password + ";";
            con = DriverManager.getConnection(connectionUrl);
        } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        }

        return con;
    }

    private boolean validateLogin(String username, String password) {
        boolean isValid = false;
        Connection con = ConnectionClass();
        if (con != null) {
            String query = "SELECT COUNT(*) FROM dbo.MstUsername WHERE Username = ? AND PassMobile = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, username);
                ps.setString(2, hashPassword(password));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        isValid = count > 0;
                    }
                }
            } catch (SQLException e) {
                Log.e("SQL Error", e.getMessage());
            } finally {
                try {
                    if (con != null) con.close();
                } catch (SQLException e) {
                    Log.e("Connection Error", "Error closing connection", e);
                }
            }
        } else {
            Log.e("Connection Error", "Failed to connect to the database.");
        }
        return isValid;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("Hashing Error", "Hash algorithm not found", e);
        }
        return null;
    }
}