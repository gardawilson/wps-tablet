package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MenuUtama extends AppCompatActivity {
    private static final String TAG = "MenuUtama";

    private ImageView inputProduksiButton;
    private Button checkUpdateButton;
    private ProgressBar progressBar;
    private UpdateManager updateManager;
    private boolean isChecking = false;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_utama);

        initializeViews();
    }

    private void initializeViews() {
        inputProduksiButton = findViewById(R.id.InputProduksi);
        checkUpdateButton = findViewById(R.id.btnCheckUpdate);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        inputProduksiButton.setOnClickListener(v -> {
            Intent intent = new Intent(MenuUtama.this, InputProduksi.class);
            startActivity(intent);
        });

        checkUpdateButton.setOnClickListener(v -> {
            if (!isChecking) {
                initializeUpdateManagerAndCheck();
            }
        });
    }

    private void initializeUpdateManagerAndCheck() {
        isChecking = true;
        checkUpdateButton.setEnabled(false);
        Toast.makeText(this, "Memeriksa pembaruan...", Toast.LENGTH_SHORT).show();

        // Initialize UpdateManager on-demand
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
                    Toast.makeText(MenuUtama.this,
                            "Gagal memeriksa pembaruan: " + error,
                            Toast.LENGTH_LONG).show();
                    isChecking = false;
                    checkUpdateButton.setEnabled(true);
                    cleanupUpdateManager();
                });
            }
        });
    }

    private void checkForUpdates() {
        Log.d(TAG, "Starting update check from MenuUtama");

        updateManager.checkForUpdates(new UpdateManager.UpdateCallback() {
            @Override
            public void onUpdateAvailable(UpdateManager.UpdateInfo updateInfo) {
                Log.d(TAG, "Update available: " + updateInfo.version);
                runOnUiThread(() -> {
                    isChecking = false;
                    checkUpdateButton.setEnabled(true);
                    showUpdateDialog(updateInfo);
                });
            }

            @Override
            public void onUpdateNotAvailable() {
                Log.d(TAG, "No update available");
                runOnUiThread(() -> {
                    isChecking = false;
                    checkUpdateButton.setEnabled(true);
                    Toast.makeText(MenuUtama.this, "Tidak ada pembaruan tersedia", Toast.LENGTH_SHORT).show();
                    cleanupUpdateManager();
                });
            }

            @Override
            public void onUpdateCheckFailed(String errorMessage) {
                Log.e(TAG, "Update check failed: " + errorMessage);
                runOnUiThread(() -> {
                    isChecking = false;
                    checkUpdateButton.setEnabled(true);
                    Toast.makeText(MenuUtama.this,
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
                    cleanupUpdateManager();
                })
                .show();
    }

    private void startDownload(String fileName) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        checkUpdateButton.setEnabled(false);

        updateManager.downloadUpdate(fileName, new UpdateManager.DownloadCallback() {
            @Override
            public void onDownloadProgress(int percentage) {
                runOnUiThread(() -> progressBar.setProgress(percentage));
            }

            @Override
            public void onDownloadComplete(File updateFile) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    checkUpdateButton.setEnabled(true);
                    installUpdate(updateFile);
                    cleanupUpdateManager();
                });
            }

            @Override
            public void onDownloadFailed(String errorMessage) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    checkUpdateButton.setEnabled(true);
                    Toast.makeText(MenuUtama.this,
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

            if (updateFile.exists()) {
                updateFile.delete();
            }
        }
    }

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
}
