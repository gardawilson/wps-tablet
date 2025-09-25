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

public class StockOpnameMenu extends AppCompatActivity {

    private CardView stock_opname;
    private CardView stock_opname_ascend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_opname_menu);

        stock_opname = findViewById(R.id.stock_opname);
        stock_opname_ascend = findViewById(R.id.stock_opname_ascend);


        stock_opname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( StockOpnameMenu.this, StockOpname.class);
                startActivity(intent);
            }
        });

        stock_opname_ascend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( StockOpnameMenu.this, StockOpnameAscend.class);
                startActivity(intent);
            }
        });



    }
}