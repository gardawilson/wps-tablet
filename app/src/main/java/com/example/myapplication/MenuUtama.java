package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.widget.TextView;
import android.widget.Button;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;


public class MenuUtama extends AppCompatActivity {

    private CardView InputLabel;
    private CardView ProsesProduksi;
    private CardView StockOpname;
    private CardView Sawmill;
    private TextView usernameView;
    private Button BtnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);

        InputLabel = findViewById(R.id.InputLabel);
        usernameView = findViewById(R.id.usernameView);
        BtnLogout = findViewById(R.id.BtnLogout);
        ProsesProduksi = findViewById(R.id.ProsesProduksi);
        StockOpname = findViewById(R.id.StockOpname);
        Sawmill = findViewById(R.id.Sawmill);

        SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", "");
        String capitalizedUsername = capitalizeFirstLetter(username);

//        if (!username.equals("x")) {
//            ProsesProduksi.setEnabled(false);
//            ProsesProduksi.setAlpha(0.5f);
//        }

        usernameView.setText(capitalizedUsername + " !");

        BtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the task to insert into Riwayat
                String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                String activity = String.format("User %s Telah Logout", capitalizedUsername);
                new SaveToRiwayatTask(capitalizedUsername, currentDateTime, activity).execute();

                Intent intent = new Intent(MenuUtama.this, MainActivity.class);
                startActivity(intent);
            }
        });

        InputLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuUtama.this, InputLabel.class);
                startActivity(intent);
            }
        });

        ProsesProduksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuUtama.this, ProsesProduksi.class);
                startActivity(intent);
            }
        });

        StockOpname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuUtama.this, StockOpname.class);
                startActivity(intent);
            }
        });

        Sawmill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuUtama.this, Sawmill.class);
                startActivity(intent);
            }
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