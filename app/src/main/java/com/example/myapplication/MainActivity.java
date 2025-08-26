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

import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.utils.SharedPrefUtils;

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
//                        SharedPrefUtils.saveUsername(username);
                        // Start the task to insert into Riwayat
                        String capitalizedUsername = capitalizeFirstLetter(username);

                        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                        String activity = String.format("User %s Telah Login", capitalizedUsername);
                        new SaveToRiwayatTask(capitalizedUsername, currentDateTime, activity).execute();
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
        new AlertDialog.Builder(this)
                .setTitle("Pembaruan Tersedia!")
                .setMessage("WPS Tablet Versi " + updateInfo.version + "\n\n" + "Rincian :\n" + updateInfo.changelog)
                .setPositiveButton("Update", (dialog, which) -> {
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

    private boolean validateLogin(String username, String password) {
        boolean isValid = false;
        Connection con = ConnectionClass();

        if (con != null) {
            String query = "SELECT DISTINCT MUGM.IdUGroup, MUGP.NoPermission " +
                    "FROM dbo.MstUsername MU " +
                    "JOIN dbo.MstUserGroupMember MUGM ON MU.IdUsername = MUGM.IdUsername " +
                    "JOIN dbo.MstUserGroupPermission MUGP ON MUGM.IdUGroup = MUGP.IdUGroup " +
                    "WHERE MU.Username = ? AND MU.Password = ? " +
                    "AND MUGP.Allow = 1";

            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, username);
                ps.setString(2, hashPassword(password));

                try (ResultSet rs = ps.executeQuery()) {
                    Set<String> roleSet = new HashSet<>();
                    Set<String> permissionSet = new HashSet<>();

                    while (rs.next()) {
                        String groupId = rs.getString("IdUGroup");
                        String noPermission = rs.getString("NoPermission");

                        roleSet.add(groupId);
                        permissionSet.add(noPermission);

                        // Logcat output
                        Log.d("LoginPermission", "Group: " + groupId + " | Permission: " + noPermission);
                    }

                    if (!roleSet.isEmpty()) {
                        SharedPrefUtils.saveUsername(this, username);
                        SharedPrefUtils.saveRoles(this, new ArrayList<>(roleSet));
                        SharedPrefUtils.savePermissions(this, new ArrayList<>(permissionSet));
                        isValid = true;
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
            // Membuat objek MessageDigest dengan algoritma MD5
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            // Menghitung hash MD5 dari password
            byte[] keyBytes = md5.digest(password.getBytes());

            // Membuat objek SecretKeySpec dengan key MD5
            SecretKeySpec key = new SecretKeySpec(keyBytes, "DESede");

            // Membuat objek Cipher dengan algoritma TripleDES
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");

            // Menginisialisasi Cipher dengan mode enkripsi dan key
            cipher.init(Cipher.ENCRYPT_MODE, key);

            // Mengenkripsi password
            byte[] encryptedBytes = cipher.doFinal(password.getBytes());

            // Mengonversi hasil enkripsi menjadi Base64
            String encodedHash = Base64.getEncoder().encodeToString(encryptedBytes);

            // Mengembalikan hash yang telah dienkripsi dan di-encode
            return encodedHash;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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