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
import com.example.myapplication.utils.PermissionUtils;
import com.example.myapplication.utils.RiwayatUtils;
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

        usernameView.setText(username + " !");


        //PERMISSION CHECK
        PermissionUtils.permissionCheck(this, StockOpname, "stock_opname:read");
        PermissionUtils.permissionCheck(this, Sawmill, "proses_sawmill:read");
        PermissionUtils.permissionCheck(this, Laporan, "laporan:read");


        BtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Simpan aktivitas logout menggunakan RiwayatUtils dengan ExecutorService
                String activity = RiwayatUtils.formatLogoutActivity(username);
                RiwayatUtils.saveToRiwayat(MenuUtama.this, username, activity, new RiwayatUtils.RiwayatCallback() {
                    @Override
                    public void onSuccess() {
                        // Callback ini sudah dijalankan di UI thread
                        Log.d("Logout", "Logout activity saved successfully");
                        // Bisa tambahkan Toast atau update UI lainnya di sini
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Callback ini sudah dijalankan di UI thread
                        Log.e("Logout", "Failed to save logout activity: " + errorMessage);
                        // Bisa tambahkan error handling di sini
                    }
                });

                // Navigate to MainActivity
                Intent intent = new Intent(MenuUtama.this, MainActivity.class);
                startActivity(intent);
            }
        });


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