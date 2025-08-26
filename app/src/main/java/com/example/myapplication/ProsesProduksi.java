package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myapplication.utils.PermissionUtils;
import com.example.myapplication.utils.SharedPrefUtils;

import org.bouncycastle.util.Pack;

import java.util.List;

public class ProsesProduksi extends AppCompatActivity {

    private CardView S4SProduksi;
    private CardView FJProduksi;
    private CardView MouldingProduksi;
    private CardView LaminatingProduksi;
    private CardView CrossCutProduksi;
    private CardView SandingProduksi;
    private CardView PackingProduksi;
    private CardView BongkarSusun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_proses_produksi);

        S4SProduksi = findViewById(R.id.S4SProduksi);
        FJProduksi = findViewById(R.id.FJProduksi);
        MouldingProduksi = findViewById(R.id.MouldingProduksi);
        LaminatingProduksi = findViewById(R.id.LaminatingProduksi);
        CrossCutProduksi = findViewById(R.id.CrossCutProduksi);
        SandingProduksi = findViewById(R.id.SandingProduksi);
        PackingProduksi = findViewById(R.id.PackingProduksi);
        BongkarSusun = findViewById(R.id.BongkarSusun);


        //PERMISSION CHECK
        PermissionUtils.permissionCheck(this, S4SProduksi, "proses_s4s:read");
        PermissionUtils.permissionCheck(this, FJProduksi, "proses_fj:read");
        PermissionUtils.permissionCheck(this, MouldingProduksi, "proses_mld:read");
        PermissionUtils.permissionCheck(this, LaminatingProduksi, "proses_lmt:read");
        PermissionUtils.permissionCheck(this, CrossCutProduksi, "proses_cca:read");
        PermissionUtils.permissionCheck(this, SandingProduksi, "proses_snd:read");
        PermissionUtils.permissionCheck(this, PackingProduksi, "proses_bj:read");
        PermissionUtils.permissionCheck(this, BongkarSusun, "bongkar_susun:read");



        S4SProduksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProsesProduksi.this, ProsesProduksiS4S.class);
                startActivity(intent);
            }
        });


        FJProduksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProsesProduksi.this, ProsesProduksiFJ.class);
                startActivity(intent);
            }
        });

        MouldingProduksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProsesProduksi.this, ProsesProduksiMoulding.class);
                startActivity(intent);
            }
        });

        LaminatingProduksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProsesProduksi.this, ProsesProduksiLaminating.class);
                startActivity(intent);
            }
        });

        CrossCutProduksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProsesProduksi.this, ProsesProduksiCrossCut.class);
                startActivity(intent);
            }
        });

        SandingProduksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProsesProduksi.this, ProsesProduksiSanding.class);
                startActivity(intent);
            }
        });

        PackingProduksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProsesProduksi.this, ProsesProduksiPacking.class);
                startActivity(intent);
            }
        });

        BongkarSusun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProsesProduksi.this, BongkarSusun.class);
                startActivity(intent);
            }
        });
    }
}