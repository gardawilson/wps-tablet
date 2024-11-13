package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    private EditText Username;
    private EditText Password;
    private Button BtnLogin;
    private Button BtnRegistrasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.Password);
        BtnLogin = findViewById(R.id.BtnLogin);
        BtnRegistrasi = findViewById(R.id.BtnRegistrasi);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Username.getText().toString().trim();
                String password = Password.getText().toString().trim();

                Executors.newSingleThreadExecutor().execute(() -> {
                    boolean isLoginSuccessful = validateLogin(username, password);

                    runOnUiThread(() -> {
                        if (isLoginSuccessful) {
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,MenuUtama.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        });

        BtnRegistrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registrasi.class);
                startActivity(intent);
            }
        });
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
                ps.setString(2, hashPassword(password)); // Hash the password for comparison
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
