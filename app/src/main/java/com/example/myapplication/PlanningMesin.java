package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.model.GradeABCData;
import com.google.android.material.tabs.TabLayout;

import java.util.List;


public class PlanningMesin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_mesin);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("S4S"));
        tabs.addTab(tabs.newTab().setText("FJ"));
        tabs.addTab(tabs.newTab().setText("MLD"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                // TODO: ambil data sesuai tab
//                List<GradeABCData> data;
//                if (pos == 1) data = Repo.getInstance().getFJ();
//                else if (pos == 2) data = Repo.getInstance().getMLD();
//                else data = Repo.getInstance().getS4S();
//
//                populateMainTable(data); // method kamu
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });



    }
}