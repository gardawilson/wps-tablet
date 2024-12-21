package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class InputLabel extends AppCompatActivity {

private CardView S4S;
private CardView FingerJoin;
private CardView Moulding;
private CardView Laminating;
private CardView CrossCut;
private CardView Sanding;
private CardView Packing;
private CardView SawnTimber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_input_label);

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
                Intent intent = new Intent( InputLabel.this, S4S.class);
                startActivity(intent);
            }
        });

        FingerJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( InputLabel.this, FingerJoint.class);
                startActivity(intent);
            }
        });

        Moulding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(InputLabel.this,Moulding.class);
                startActivity(intent);
            }
        });

        Laminating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InputLabel.this,Laminating.class);
                startActivity(intent);
            }
        });

        CrossCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InputLabel.this,CrossCut.class);
                startActivity(intent);
            }
        });

        Sanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InputLabel.this, Sanding.class);
                startActivity(intent);
            }
        });

        Packing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InputLabel.this,Packing.class);
                startActivity(intent);
            }
        });

//        SawnTimber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(InputLabel.this,SawnTimber.class);
//                startActivity(intent);
//            }
//        });

    }
}