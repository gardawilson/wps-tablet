package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ProsesProduksi extends AppCompatActivity {

    private CardView S4SProduksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_proses_produksi);

        S4SProduksi = findViewById(R.id.S4SProduksi);

        S4SProduksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProsesProduksi.this, ProsesProduksiS4S.class);
                startActivity(intent);
            }
        });
    }
}