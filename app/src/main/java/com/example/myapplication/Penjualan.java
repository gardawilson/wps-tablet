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

import com.example.myapplication.utils.PermissionUtils;

public class Penjualan extends AppCompatActivity {

    private CardView PenjualanStSnd;
    private CardView PenjualanBj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan);

        PenjualanStSnd = findViewById(R.id.PenjualanStSnd);
        PenjualanBj = findViewById(R.id.PenjualanBj);

        PermissionUtils.permissionCheck(this, PenjualanStSnd, "penjualan_st_snd:read");
        PermissionUtils.permissionCheck(this, PenjualanBj, "penjualan_bj:read");


        PenjualanStSnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Penjualan.this, PenjualanStSnd.class);
                startActivity(intent);
            }
        });

        PenjualanBj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Penjualan.this, PenjualanBJ.class);
                startActivity(intent);
            }
        });

    }
}