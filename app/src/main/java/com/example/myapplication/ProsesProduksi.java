package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ProsesProduksi extends AppCompatActivity {

    private CardView S4SProduksi;
    private CardView FJProduksi;
    private CardView MouldingProduksi;
    private CardView LaminatingProduksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_proses_produksi);

        S4SProduksi = findViewById(R.id.S4SProduksi);
        FJProduksi = findViewById(R.id.FJProduksi);
        MouldingProduksi = findViewById(R.id.MouldingProduksi);
        LaminatingProduksi = findViewById(R.id.LaminatingProduksi);


        S4SProduksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProsesProduksi.this, ProsesProduksiS4S.class);
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
    }
}