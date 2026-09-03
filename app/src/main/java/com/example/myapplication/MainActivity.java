package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.myapplication.api.AuthApi;
import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.model.LoginResponse;
import com.example.myapplication.utils.SharedPrefUtils;
import com.example.myapplication.utils.TokenManager;

import java.io.File;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;



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

        // Pemeriksaan update tidak diperlukan saat mengembangkan UI.
        if (!BuildConfig.DEBUG) {
            initializeUpdateManagerAndCheck();
        }
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

            progressBar.setVisibility(View.VISIBLE);
            BtnLogin.setEnabled(false);

            Executors.newSingleThreadExecutor().execute(() -> {
                // Autentikasi tunggal lewat backend API (menggantikan login JDBC).
                LoginResponse response = AuthApi.login(username, password);
                //Log.d("err", response.getMessage());

                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    BtnLogin.setEnabled(true);

                    boolean ok = response.isSuccess()
                            && response.getToken() != null && !response.getToken().isEmpty()
                            && response.getUser() != null;

                    if (!ok) {
                        String msg = (response.getMessage() == null || response.getMessage().isEmpty())
                                ? "Login gagal. Periksa username/password atau koneksi server."
                                : response.getMessage();
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                        return;
                    }

                    persistSession(response);

                    String capitalizedUsername = capitalizeFirstLetter(response.getUser().getUsername());
                    String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    String activity = String.format("User %s Telah Login", capitalizedUsername);
                    new SaveToRiwayatTask(capitalizedUsername, currentDateTime, activity).execute();

                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, HostActivity.class));
                    finish();
                });
            });
        });

        BtnRegistrasi.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Registrasi.class);
            startActivity(intent);
        });
    }

    //Fungsi untuk membuat huruf kapital
    public String capitalizeFirstLetter(String inputUsername) {
        if (inputUsername == null || inputUsername.isEmpty()) {
            return inputUsername; // Jika null atau kosong, kembalikan string asli
        }
        return inputUsername.substring(0, 1).toUpperCase() + inputUsername.substring(1).toLowerCase();
    }

    private class SaveToRiwayatTask extends AsyncTask<Void, Void, Boolean> {
        private String username;
        private String currentDate;
        private String activity;

        public SaveToRiwayatTask(String username, String currentDate, String activity) {
            this.username = username;
            this.currentDate = currentDate;
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            boolean success = false;

            if (con != null) {
                try {
                    // Query untuk insert ke tabel Riwayat
                    String query = "INSERT INTO dbo.Riwayat (Nip, Tgl, Aktivitas) VALUES (?, ?, ?)";
                    Log.d("SQL Query", "Executing query: " + query);
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, username);
                    ps.setString(2, currentDate);
                    ps.setString(3, activity);

                    int rowsAffected = ps.executeUpdate();
                    Log.d("Database", "Rows affected: " + rowsAffected);

                    ps.close();
                    con.close();

                    success = rowsAffected > 0;
                    Log.d("Riwayat", "Data successfully inserted into Riwayat.");

                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // Update UI atau beri feedback ke pengguna setelah data disimpan
            if (success) {
                Log.d("Riwayat", "Data berhasil disimpan di Riwayat");
            } else {
                Log.e("Riwayat", "Gagal menyimpan data di Riwayat");
            }
        }
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
        boolean mandatory = updateInfo.forceUpdate || isBelowMinVersion(updateInfo.minVersion);

        String message = "WPS Tablet Versi " + updateInfo.version + "\n\nRincian :\n" + updateInfo.changelog;
        if (mandatory) {
            message += "\n\nPembaruan ini wajib dipasang untuk melanjutkan.";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Pembaruan Tersedia!")
                .setMessage(message)
                .setPositiveButton("Update", (dialog, which) -> {
                    dialog.dismiss();
                    startDownload(updateInfo.fileName);
                })
                .setCancelable(false);

        if (!mandatory) {
            builder.setNegativeButton("Nanti", (dialog, which) -> {
                dialog.dismiss();
                cleanupUpdateManager();
            });
        }

        builder.show();
    }

    private boolean isBelowMinVersion(String minVersion) {
        if (minVersion == null || minVersion.trim().isEmpty()) {
            return false;
        }
        try {
            String current = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
            String[] a = current.split("\\.");
            String[] b = minVersion.trim().split("\\.");
            int len = Math.max(a.length, b.length);
            for (int i = 0; i < len; i++) {
                int x = i < a.length ? Integer.parseInt(a[i].replaceAll("[^0-9]", "")) : 0;
                int y = i < b.length ? Integer.parseInt(b[i].replaceAll("[^0-9]", "")) : 0;
                if (x != y) {
                    return x < y;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "isBelowMinVersion: " + e.getMessage());
        }
        return false;
    }

    private void startDownload(String fileName) {
        // Membuat custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.progress_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog progressDialog = builder.create();
        progressDialog.show();

        // Referensi komponen dialog
        ProgressBar progressBar = dialogView.findViewById(R.id.progress_bar);
        TextView progressText = dialogView.findViewById(R.id.progress_text);

        updateManager.downloadUpdate(fileName, new UpdateManager.DownloadCallback() {
            @Override
            public void onDownloadProgress(int percentage) {
                runOnUiThread(() -> {
                    progressBar.setProgress(percentage);
                    progressText.setText(percentage + "%");
                });
            }

            @Override
            public void onDownloadComplete(File updateFile) {
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    installUpdate(updateFile);
                    cleanupUpdateManager();
                });
            }

            @Override
            public void onDownloadFailed(String errorMessage) {
                runOnUiThread(() -> {
                    progressDialog.dismiss();
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



    /** Simpan sesi (token, user, permission) dari respons login API. */
    private void persistSession(LoginResponse response) {
        LoginResponse.UserData user = response.getUser();

        TokenManager.saveToken(MainActivity.this, response.getToken());
        TokenManager.saveUserData(
                MainActivity.this,
                user.getIdUsername(),
                user.getUsername(),
                user.getFullName()
        );

        SharedPrefUtils.saveUsername(MainActivity.this, user.getUsername());
        SharedPrefUtils.saveIdUsername(MainActivity.this, String.valueOf(user.getIdUsername()));

        List<String> permissions = user.getPermissions();
        SharedPrefUtils.savePermissions(
                MainActivity.this,
                permissions != null ? new ArrayList<>(new HashSet<>(permissions)) : new ArrayList<>()
        );

        Log.d("API_LOGIN", "Sesi tersimpan. Total permissions: "
                + (permissions != null ? permissions.size() : 0));
    }




    @Override
    public void onBackPressed() {
        // Membuat AlertDialog untuk konfirmasi keluar
        new AlertDialog.Builder(this)
                .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
                .setCancelable(false) // Agar dialog tidak bisa dibatalkan dengan menekan luar dialog
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity(); // Menutup semua aktivitas dalam stack
                        System.exit(0);   // Memastikan aplikasi tertutup
                    }
                })
                .setNegativeButton("Tidak", null) // Jika memilih "Tidak", dialog ditutup
                .show();
    }


    //Koneksi Database
    @SuppressLint("NewApi")
    private Connection ConnectionClass() {
        Connection con = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
        } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        }
        return con;
    }
}
