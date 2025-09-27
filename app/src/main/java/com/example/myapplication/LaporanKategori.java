package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class LaporanKategori extends AppCompatActivity {

    private CardView kbReport;
    private CardView kbRambungReport;
    private CardView stReport;
    private CardView s4sReport;
    private CardView fjReport;
    private CardView mldReport;
    private CardView lmtReport;
    private CardView ccReport;
    private CardView sndReport;
    private CardView bjReport;
    private CardView managementReport;
    private CardView verifReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_kategori);

        kbReport = findViewById(R.id.kbReport);
        kbRambungReport = findViewById(R.id.kbRambungReport);
        stReport = findViewById(R.id.stReport);
        s4sReport = findViewById(R.id.s4sReport);
        fjReport = findViewById(R.id.fjReport);
        mldReport = findViewById(R.id.mldReport);
        lmtReport = findViewById(R.id.lmtReport);
        ccReport = findViewById(R.id.ccReport);
        sndReport = findViewById(R.id.sndReport);
        bjReport = findViewById(R.id.bjReport);
        managementReport = findViewById(R.id.managementReport);
        verifReport = findViewById(R.id.verifReport);


        kbReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LaporanKategori.this, LaporanKB.class);
                startActivity(intent);
            }
        });

        kbRambungReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LaporanKategori.this, LaporanKbRambung.class);
                startActivity(intent);
            }
        });

        stReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LaporanKategori.this, LaporanST.class);
                startActivity(intent);
            }
        });

        s4sReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LaporanKategori.this, LaporanS4S.class);
                startActivity(intent);
            }
        });

        fjReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LaporanKategori.this, LaporanFJ.class);
                startActivity(intent);
            }
        });

        mldReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LaporanKategori.this, LaporanMLD.class);
                startActivity(intent);
            }
        });

        lmtReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LaporanKategori.this, LaporanLMT.class);
                startActivity(intent);
            }
        });

        ccReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LaporanKategori.this, LaporanCCA.class);
                startActivity(intent);
            }
        });

        sndReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LaporanKategori.this, LaporanSND.class);
                startActivity(intent);
            }
        });

        bjReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LaporanKategori.this, LaporanBJ.class);
                startActivity(intent);
            }
        });

        managementReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LaporanKategori.this, LaporanManajemen.class);
                startActivity(intent);
            }
        });

        verifReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LaporanKategori.this, LaporanVerifikasi.class);
                startActivity(intent);
            }
        });

    }
}