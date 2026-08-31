package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.utils.RiwayatUtils;
import com.example.myapplication.widget.BarChartView;

import java.util.Arrays;
import java.util.List;

public class MenuUtama extends BaseSidebarActivity {
    private static final String TAG = "MenuUtama";

    private TextView dashboardTitle;
    private TextView dashboardSubtitle;
    private TextView metricModuleCount;
    private TextView metricMode;
    private TextView metricServer;
    private TextView metricPesananAktif;
    private TextView topBarUsername;
    private BarChartView productionChart;
    private LinearLayout activityListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_menu_utama);

        bindViews();
        setupDashboard();
        applyUserIdentity(currentUsername());
    }

    @Override
    protected boolean isSidebarHome() {
        return true;
    }

    @Override
    protected void onSidebarLogout() {
        performLogout(currentUsername());
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
                .setCancelable(false)
                .setPositiveButton("Ya", (DialogInterface dialog, int id) -> {
                    finishAffinity();
                    System.exit(0);
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void bindViews() {
        dashboardTitle = findViewById(R.id.dashboardTitle);
        dashboardSubtitle = findViewById(R.id.dashboardSubtitle);
        metricModuleCount = findViewById(R.id.metricModuleCount);
        metricMode = findViewById(R.id.metricMode);
        metricServer = findViewById(R.id.metricServer);
        metricPesananAktif = findViewById(R.id.metricPesananAktif);
        topBarUsername = findViewById(R.id.topBarUsername);
        productionChart = findViewById(R.id.productionChart);
        activityListContainer = findViewById(R.id.activityListContainer);
    }

    private void setupDashboard() {
        metricModuleCount.setText("12.480");
        metricMode.setText("328");
        metricServer.setText("Online");
        metricPesananAktif.setText("18");

        List<String> chartLabels = Arrays.asList("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min");
        List<Float> chartValues = Arrays.asList(320f, 410f, 380f, 560f, 430f, 620f, 385f);
        productionChart.setData(chartLabels, chartValues);

        populateActivityList();
    }

    private void populateActivityList() {
        String[][] activities = {
                {"Penerimaan Kayu Bulat - No. KB-2026-0168", "2 menit lalu"},
                {"Proses S4S - No. PRD-2026-0456", "15 menit lalu"},
                {"Penjualan ST - No. J-ST-2026-0214", "1 jam lalu"},
                {"Stock Opname - Warehouse A", "3 jam lalu"},
                {"Proses Kiln Dry - No. KD-2026-0098", "5 jam lalu"},
        };

        LayoutInflater inflater = LayoutInflater.from(this);
        for (String[] activity : activities) {
            View row = inflater.inflate(R.layout.item_dashboard_activity, activityListContainer, false);
            ((TextView) row.findViewById(R.id.activityText)).setText(activity[0]);
            ((TextView) row.findViewById(R.id.activityTime)).setText(activity[1]);
            activityListContainer.addView(row);
        }
    }

    private void applyUserIdentity(String username) {
        dashboardTitle.setText("Halo, " + username);
        dashboardSubtitle.setText("Ringkasan stok, produksi hari ini, dan status server dalam satu layar.");
        topBarUsername.setText(username);
    }

    private void performLogout(String username) {
        String activity = RiwayatUtils.formatLogoutActivity(username);
        RiwayatUtils.saveToRiwayat(MenuUtama.this, username, activity, new RiwayatUtils.RiwayatCallback() {
            @Override
            public void onSuccess() {
                Log.d("Logout", "saved");
            }

            @Override
            public void onError(String e) {
                Log.e("Logout", "err: " + e);
            }
        });

        Intent intent = new Intent(MenuUtama.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
