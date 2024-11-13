package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.sql.DriverManager;

public class InputProduksi extends AppCompatActivity {

private TextView S4S;
private TextView FingerJoin;
private TextView Moulding;
private TextView Laminating;
private TextView CrossCut;
private TextView Sanding;
private TextView Packing;
private TextView SawnTimber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_input_produksi);

        S4S = findViewById(R.id.S4S);
        FingerJoin = findViewById(R.id.FingerJoin);
        Moulding = findViewById(R.id.Moulding);
        Laminating = findViewById(R.id.Laminating);
        CrossCut = findViewById(R.id.CrossCut);
        Sanding = findViewById(R.id.Sanding);
        Packing = findViewById(R.id.Packing);
        SawnTimber = findViewById(R.id.SawnTimber);

        S4S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( InputProduksi.this, S4S.class);
                startActivity(intent);
            }
        });

        FingerJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( InputProduksi.this, FingerJoint.class);
                startActivity(intent);
            }
        });

        Moulding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(InputProduksi.this,Moulding.class);
                startActivity(intent);
            }
        });

        Laminating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InputProduksi.this,Laminating.class);
                startActivity(intent);
            }
        });

        CrossCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InputProduksi.this,CrossCut.class);
                startActivity(intent);
            }
        });

        Sanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InputProduksi.this, Sanding.class);
                startActivity(intent);
            }
        });

        Packing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InputProduksi.this,Packing.class);
                startActivity(intent);
            }
        });

        SawnTimber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InputProduksi.this,SawnTimber.class);
                startActivity(intent);
            }
        });

    }
}