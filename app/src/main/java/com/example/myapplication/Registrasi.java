package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;

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

        BtnRegistrasi2.setOnClickListener(v -> {
            String username = UserRegis.getText().toString().trim();
            String password = PassRegis.getText().toString().trim();
            String idText = IdRegis.getText().toString().trim();
            String namadepan = InputNamaDepan.getText().toString().trim();
            String namabelakang = InputNamaBelakang.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty() && !idText.isEmpty() && !namadepan.isEmpty() && !namabelakang.isEmpty()) {
                try {
                    int id = Integer.parseInt(idText);
                    String hashedPassword = hashPassword(password);

                    Executors.newSingleThreadExecutor().execute(() -> {
                        boolean isSuccess = saveUserToDatabase(id, username, hashedPassword, namadepan, namabelakang);
                        runOnUiThread(() -> {
                            if (isSuccess) {
                                Toast.makeText(Registrasi.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Registrasi.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                } catch (NumberFormatException e) {
                    Toast.makeText(Registrasi.this, "ID must be a number", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Registrasi.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            }
        });

        BtnLogin2.setOnClickListener(v -> {
            Intent intent = new Intent(Registrasi.this, MainActivity.class);
            startActivity(intent);
        });
    }

    // Get a new user ID from the database
    private int getNewIdFromDatabase() {
        int newId = 1; // Default ID
        try (Connection con = getDatabaseConnection()) {
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
        try (Connection con = getDatabaseConnection()) {
            if (con != null) {
                String query = "INSERT INTO dbo.MstUsername (IdUsername, Username, PassMobile, FName, LName) VALUES (?, ?, ?, ?, ?)";
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
        }
        return isSuccess;
    }

    // Create a reusable method for establishing a database connection
    private Connection getDatabaseConnection() {
        String ip = "192.168.10.100";
        String port = "1433";
        String db = "WPS";
        String user = "sa";
        String pass = "Utama1234";
        Connection con = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + db + ";User=" + user + ";password=" + pass + ";";
            con = DriverManager.getConnection(connectionUrl);
        } catch (ClassNotFoundException e) {
            Log.e("Database Error", "JDBC Driver not found", e);
        } catch (SQLException e) {
            Log.e("Database Error", "Connection failed: " + e.getMessage(), e);
        }
        return con;
    }

    // Hash password using SHA-256
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
