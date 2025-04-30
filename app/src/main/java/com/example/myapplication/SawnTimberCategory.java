package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SawnTimberCategory extends AppCompatActivity {

    private CardView STAll;
    private CardView STUpah;
    private CardView STBeli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sawn_timber_category);

        STAll = findViewById(R.id.STAll);
        STUpah = findViewById(R.id.STUpah);
        STBeli = findViewById(R.id.STBeli);

        STAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( SawnTimberCategory.this, SawnTimber.class);
                startActivity(intent);
            }
        });

        STBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( SawnTimberCategory.this, SawnTimberPembelian.class);
                startActivity(intent);
            }
        });

        STUpah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( SawnTimberCategory.this, SawnTimberUpah.class);
                startActivity(intent);
            }
        });

    }
}