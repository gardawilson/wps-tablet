package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.utils.RiwayatUtils;
import com.example.myapplication.utils.SharedPrefUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

public class MenuUtama extends AppCompatActivity {
    private static final String TAG = "MenuUtama";

    private View sidebarContainer;
    private View sidebarHeader;
    private View sidebarLogo;
    private NavigationView navigationView;
    private MaterialButton sidebarToggle;
    private TextView sidebarTitle;
    private TextView sidebarSubtitle;
    private TextView sidebarUsername;
    private TextView dashboardTitle;
    private TextView dashboardSubtitle;
    private TextView metricModuleCount;
    private TextView metricMode;
    private TextView metricServer;
    private int selectedMenuItemId = R.id.nav_dashboard;
    private boolean syncingSidebarSelection = false;
    private boolean isSidebarCollapsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);

        bindViews();
        setupDashboard();
        setupSidebar();

        String username = safeUsername(SharedPrefUtils.getUsername(this));
        applyUserIdentity(username);
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
        sidebarContainer = findViewById(R.id.sidebarContainer);
        sidebarHeader = findViewById(R.id.sidebarHeader);
        sidebarLogo = findViewById(R.id.sidebarLogo);
        navigationView = findViewById(R.id.navigationView);
        sidebarToggle = findViewById(R.id.sidebarToggle);
        sidebarTitle = findViewById(R.id.sidebarTitle);
        sidebarSubtitle = findViewById(R.id.sidebarSubtitle);
        sidebarUsername = findViewById(R.id.sidebarUsername);
        dashboardTitle = findViewById(R.id.dashboardTitle);
        dashboardSubtitle = findViewById(R.id.dashboardSubtitle);
        metricModuleCount = findViewById(R.id.metricModuleCount);
        metricMode = findViewById(R.id.metricMode);
        metricServer = findViewById(R.id.metricServer);
    }

    private void setupDashboard() {
        metricModuleCount.setText("12.480");
        metricMode.setText("328");
        metricServer.setText("Online");
    }

    private void setupSidebar() {
        navigationView.setNavigationItemSelectedListener(item -> {
            if (syncingSidebarSelection) {
                return true;
            }
            handleMenuSelection(item.getItemId());
            return true;
        });

        sidebarToggle.setOnClickListener(v -> toggleSidebar());

        inflateSidebarMenu(false);
        syncSelectedSidebarItem(selectedMenuItemId);
        updateSidebarState(false);
    }

    private void applyUserIdentity(String username) {
        sidebarUsername.setText("User: " + username);
        dashboardTitle.setText("Halo, " + username);
        dashboardSubtitle.setText("Ringkasan stok, produksi hari ini, dan status server dalam satu layar.");
    }

    private void toggleSidebar() {
        updateSidebarState(!isSidebarCollapsed);
    }

    private void updateSidebarState(boolean collapsed) {
        isSidebarCollapsed = collapsed;

        ViewGroup.LayoutParams layoutParams = sidebarContainer.getLayoutParams();
        layoutParams.width = dpToPx(collapsed ? 72 : 300);
        sidebarContainer.setLayoutParams(layoutParams);

        ViewGroup.LayoutParams headerParams = sidebarHeader.getLayoutParams();
        headerParams.height = dpToPx(collapsed ? 96 : 180);
        sidebarHeader.setLayoutParams(headerParams);

        sidebarLogo.setVisibility(View.VISIBLE);
        sidebarTitle.setVisibility(collapsed ? View.GONE : View.VISIBLE);
        sidebarSubtitle.setVisibility(collapsed ? View.GONE : View.VISIBLE);
        sidebarUsername.setVisibility(collapsed ? View.GONE : View.VISIBLE);

        inflateSidebarMenu(collapsed);
        sidebarToggle.setIconResource(collapsed ? R.drawable.ic_sidebar_expand : R.drawable.ic_sidebar_collapse);
        navigationView.setItemTextColor(ColorStateList.valueOf(collapsed ? Color.TRANSPARENT : ContextCompat.getColor(this, R.color.white)));
        navigationView.setItemIconPadding(dpToPx(collapsed ? 0 : 12));
        navigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.white));

        syncSelectedSidebarItem(selectedMenuItemId);
    }

    private void inflateSidebarMenu(boolean collapsed) {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(collapsed ? R.menu.menu_menu_utama_collapsed : R.menu.menu_menu_utama);
    }

    private void syncSelectedSidebarItem(int itemId) {
        syncingSidebarSelection = true;
        try {
            navigationView.setCheckedItem(itemId);
        } finally {
            syncingSidebarSelection = false;
        }
    }

    private void handleMenuSelection(int itemId) {
        selectedMenuItemId = itemId;
        syncSelectedSidebarItem(itemId);

        if (itemId == R.id.nav_dashboard) {
            return;
        }
        if (itemId == R.id.nav_input_label) {
            startActivity(new Intent(this, InputLabel.class));
            return;
        }
        if (itemId == R.id.nav_proses_produksi) {
            startActivity(new Intent(this, ProsesProduksi.class));
            return;
        }
        if (itemId == R.id.nav_stock_opname) {
            startActivity(new Intent(this, StockOpnameMenu.class));
            return;
        }
        if (itemId == R.id.nav_sawmill) {
            startActivity(new Intent(this, ProsesSawmill.class));
            return;
        }
        if (itemId == R.id.nav_laporan) {
            startActivity(new Intent(this, LaporanKategori.class));
            return;
        }
        if (itemId == R.id.nav_penjualan) {
            startActivity(new Intent(this, Penjualan.class));
            return;
        }
        if (itemId == R.id.nav_audit) {
            startActivity(new Intent(this, AuditActivity.class));
            return;
        }
        if (itemId == R.id.nav_spk) {
            startActivity(new Intent(this, SPK.class));
            return;
        }
        if (itemId == R.id.nav_grade_abc) {
            startActivity(new Intent(this, GradeABC.class));
            return;
        }
        if (itemId == R.id.nav_planning_mesin) {
            startActivity(new Intent(this, PlanningMesin.class));
            return;
        }
        if (itemId == R.id.nav_nyangkut) {
            startActivity(new Intent(this, Nyangkut.class));
            return;
        }
        if (itemId == R.id.nav_logout) {
            performLogout(safeUsername(SharedPrefUtils.getUsername(this)));
        }
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

    private String safeUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "-";
        }
        return capitalizeFirstLetter(username.trim());
    }

    private String capitalizeFirstLetter(String inputUsername) {
        if (inputUsername == null || inputUsername.isEmpty()) {
            return inputUsername;
        }
        return inputUsername.substring(0, 1).toUpperCase() + inputUsername.substring(1).toLowerCase();
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
