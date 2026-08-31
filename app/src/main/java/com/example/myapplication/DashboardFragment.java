package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.utils.SharedPrefUtils;
import com.example.myapplication.widget.BarChartView;

import java.util.Arrays;
import java.util.List;

/**
 * Layar Dashboard sebagai Fragment (hasil migrasi dari {@code MenuUtama}).
 * Dihost oleh {@link HostActivity} yang menyediakan sidebar permanen.
 */
public class DashboardFragment extends Fragment {

    private TextView dashboardTitle;
    private TextView dashboardSubtitle;
    private TextView metricModuleCount;
    private TextView metricMode;
    private TextView metricServer;
    private TextView metricPesananAktif;
    private TextView topBarUsername;
    private BarChartView productionChart;
    private LinearLayout activityListContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_menu_utama, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dashboardTitle = view.findViewById(R.id.dashboardTitle);
        dashboardSubtitle = view.findViewById(R.id.dashboardSubtitle);
        metricModuleCount = view.findViewById(R.id.metricModuleCount);
        metricMode = view.findViewById(R.id.metricMode);
        metricServer = view.findViewById(R.id.metricServer);
        metricPesananAktif = view.findViewById(R.id.metricPesananAktif);
        topBarUsername = view.findViewById(R.id.topBarUsername);
        productionChart = view.findViewById(R.id.productionChart);
        activityListContainer = view.findViewById(R.id.activityListContainer);

        setupDashboard();
        applyUserIdentity(currentUsername());
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

        LayoutInflater inflater = LayoutInflater.from(requireContext());
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

    private String currentUsername() {
        String username = SharedPrefUtils.getUsername(requireContext());
        if (username == null || username.trim().isEmpty()) {
            return "-";
        }
        username = username.trim();
        return username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
    }
}
