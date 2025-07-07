package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.config.DatabaseConfig;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Registrasi extends AppCompatActivity {

    private Button BtnRegistrasi2;
    private Button BtnLogin2;
    private EditText UserRegis;
    private EditText PassRegis;
    private TextView IdRegis;
    private EditText InputNamaDepan;
    private EditText InputNamaBelakang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        // Initialize UI components
        BtnRegistrasi2 = findViewById(R.id.BtnRegistrasi2);
        BtnLogin2 = findViewById(R.id.BtnLogin2);
        UserRegis = findViewById(R.id.UserRegis);
        PassRegis = findViewById(R.id.PassRegis);
        IdRegis = findViewById(R.id.IdRegis);
        InputNamaDepan = findViewById(R.id.InputNamaDepan);
        InputNamaBelakang = findViewById(R.id.InputNamaBelakang);

        // Fetch new ID from the database in the background
        Executors.newSingleThreadExecutor().execute(() -> {
            int newId = getNewIdFromDatabase();
            runOnUiThread(() -> IdRegis.setText(String.valueOf(newId)));
        });

        // Set up click listener for Registration button
        BtnRegistrasi2.setOnClickListener(v -> {
            String username = UserRegis.getText().toString().trim();
            String password = PassRegis.getText().toString().trim();
            String idText = IdRegis.getText().toString().trim();
            String namadepan = InputNamaDepan.getText().toString().trim();
            String namabelakang = InputNamaBelakang.getText().toString().trim();

            if (validateInput(username, password, namadepan, namabelakang)) {
                try {
                    int id = Integer.parseInt(idText);
                    String hashedPassword = hashPassword(password);

                    if (hashedPassword != null) {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            boolean isSuccess = saveUserToDatabase(id, username, hashedPassword, namadepan, namabelakang);
                            runOnUiThread(() -> {
                                if (isSuccess) {
                                    Toast.makeText(Registrasi.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                                    // Return to login page
                                    Intent intent = new Intent(Registrasi.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        });
                    } else {
                        Toast.makeText(Registrasi.this, "Gagal mengenkripsi password", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(Registrasi.this, "ID harus berupa angka", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up click listener for Login button
        BtnLogin2.setOnClickListener(v -> {
            Intent intent = new Intent(Registrasi.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Input validation
    private boolean validateInput(String username, String password, String namadepan, String namabelakang) {
        if (username.isEmpty() || password.isEmpty() || namadepan.isEmpty() || namabelakang.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (username.length() < 1) {
            Toast.makeText(this, "Username minimal 4 karakter", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 1) {
            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Get a new user ID from the database
    private int getNewIdFromDatabase() {
        int newId = 1; // Default ID
        try (Connection con = ConnectionClass()) {
            if (con != null) {
                String queryMaxId = "SELECT MAX(IdUsername) AS max_id FROM dbo.MstUsername";
                try (PreparedStatement psMaxId = con.prepareStatement(queryMaxId);
                     ResultSet rs = psMaxId.executeQuery()) {
                    if (rs.next()) {
                        newId = rs.getInt("max_id") + 1;
                    }
                }
            }
        } catch (SQLException e) {
            Log.e("Database Error", "SQL Error: " + e.getMessage(), e);
        }
        return newId;
    }

    // Save the new user to the database
    private boolean saveUserToDatabase(int id, String username, String hashedPassword, String namadepan, String namabelakang) {
        boolean isSuccess = false;
        try (Connection con = ConnectionClass()) {
            if (con != null) {
                // Check if username already exists
                String checkQuery = "SELECT COUNT(*) FROM dbo.MstUsername WHERE Username = ?";
                try (PreparedStatement checkPs = con.prepareStatement(checkQuery)) {
                    checkPs.setString(1, username);
                    try (ResultSet rs = checkPs.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            runOnUiThread(() -> {
                                Toast.makeText(Registrasi.this, "Username sudah terdaftar", Toast.LENGTH_SHORT).show();
                            });
                            return false;
                        }
                    }
                }

                // If username doesn't exist, proceed with insertion
                String query = "INSERT INTO dbo.MstUsername (IdUsername, Username, Password, FName, LName) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(query)) {
                    ps.setInt(1, id);
                    ps.setString(2, username);
                    ps.setString(3, hashedPassword);
                    ps.setString(4, namadepan);
                    ps.setString(5, namabelakang);
                    int rowsAffected = ps.executeUpdate();
                    isSuccess = rowsAffected > 0;
                }
            }
        } catch (SQLException e) {
            Log.e("Registration Error", "SQL Error: " + e.getMessage(), e);
            runOnUiThread(() -> {
                Toast.makeText(Registrasi.this, "Terjadi kesalahan saat registrasi", Toast.LENGTH_SHORT).show();
            });
        }
        return isSuccess;
    }

    // Password hashing using MD5 + TripleDES + Base64
    private String hashPassword(String password) {
        try {
            // Create MD5 hash
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] keyBytes = md5.digest(password.getBytes());

            // Create TripleDES key
            SecretKeySpec key = new SecretKeySpec(keyBytes, "DESede");

            // Initialize cipher
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            // Encrypt password
            byte[] encryptedBytes = cipher.doFinal(password.getBytes());

            // Convert to Base64
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            Log.e("Hashing Error", "Failed to hash password", e);
            e.printStackTrace();
            return null;
        }
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