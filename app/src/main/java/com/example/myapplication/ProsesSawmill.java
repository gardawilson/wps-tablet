package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ProsesSawmill extends AppCompatActivity {

    private CardView card_lembar_telly;
    private CardView card_quality_control_sawmill;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_proses_sawmill);

        card_lembar_telly = findViewById(R.id.card_lembar_telly);
        card_quality_control_sawmill = findViewById(R.id.card_quality_control_sawmill);


        card_lembar_telly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProsesSawmill.this, Sawmill.class);
                startActivity(intent);
            }
        });

        card_quality_control_sawmill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProsesSawmill.this, QcSawmill.class);
                startActivity(intent);
            }
        });

    }
}