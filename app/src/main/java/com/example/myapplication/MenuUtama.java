package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.widget.TextView;
import android.widget.Button;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.utils.SharedPrefUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Date;


public class MenuUtama extends AppCompatActivity {

    private CardView InputLabel;
    private CardView ProsesProduksi;
    private CardView StockOpname;
    private CardView Sawmill;
    private CardView Laporan;
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
        Laporan = findViewById(R.id.Laporan);

        String username = SharedPrefUtils.getUsername(this);

        // Ambil list role dari SharedPreferences
        List<String> userRoles = SharedPrefUtils.getRoles(MenuUtama.this);

        usernameView.setText(username + " !");

        BtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the task to insert into Riwayat
                String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                String activity = String.format("User %s Telah Logout", username);
                new SaveToRiwayatTask(username, currentDateTime, activity).execute();

                Intent intent = new Intent(MenuUtama.this, MainActivity.class);
                startActivity(intent);
            }
        });


        // Cek apakah user memiliki IdUGroup "30" = Tally
        if (userRoles.contains("20")) {
            // Hanya aktifkan jika termasuk group 30
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

        } else {
            // Disable klik dan tampilkan visual tidak aktif (opsional)
            InputLabel.setAlpha(0.5f);
            ProsesProduksi.setAlpha(0.5f);
        }


        // Cek apakah user memiliki IdUGroup "20" = Stock Opname
        if (userRoles.contains("20")) {
            StockOpname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuUtama.this, StockOpname.class);
                    startActivity(intent);
                }
            });
        } else {
            // Disable klik dan tampilkan visual tidak aktif (opsional)
            StockOpname.setAlpha(0.5f);
        }


        Sawmill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuUtama.this, ProsesSawmill.class);
                startActivity(intent);
            }
        });

        Laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuUtama.this, LaporanKategori.class);
                startActivity(intent);
            }
        });
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