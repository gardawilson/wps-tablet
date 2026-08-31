package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.myapplication.sidebar.SidebarNavItem;
import com.example.myapplication.utils.RiwayatUtils;

/**
 * Activity tunggal (shell) untuk arsitektur Single-Activity. Sidebar di-inflate
 * sekali lewat {@link BaseSidebarActivity#setContent(int)}; tiap layar yang sudah
 * dimigrasi tampil sebagai Fragment di dalam {@code hostFragmentContainer}
 * sehingga sidebar tidak pernah dirender ulang saat berpindah menu.
 *
 * Layar yang belum dimigrasi tetap dibuka sebagai Activity terpisah (perilaku
 * lama) sampai gilirannya dikonversi.
 */
public class HostActivity extends BaseSidebarActivity {

    private static final String TAG = "HostActivity";
    private static final String STATE_MENU_ID = "host_current_menu_id";

    private int currentMenuId = ID_DASHBOARD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.host_content);

        if (savedInstanceState != null) {
            currentMenuId = savedInstanceState.getInt(STATE_MENU_ID, ID_DASHBOARD);
            // FragmentManager sudah memulihkan fragment; cukup sinkronkan sidebar.
            selectSidebarItem(currentMenuId);
            return;
        }

        int requestedMenuId = getIntent().getIntExtra(EXTRA_MENU_ID, ID_DASHBOARD);
        showMenu(requestedMenuId, false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int requestedMenuId = intent.getIntExtra(EXTRA_MENU_ID, currentMenuId);
        showMenu(requestedMenuId, false);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_MENU_ID, currentMenuId);
    }

    @Override
    protected boolean isSidebarHome() {
        return true;
    }

    @Override
    protected int getSelectedMenuId() {
        return currentMenuId;
    }

    /** Klik item sidebar berbasis Fragment -> tukar fragment tanpa pindah Activity. */
    @Override
    protected void openFragmentItem(SidebarNavItem item) {
        if (item.id == currentMenuId && getSupportFragmentManager().findFragmentById(R.id.hostFragmentContainer) != null) {
            return;
        }
        showMenu(item.id, true);
    }

    private void showMenu(int menuId, boolean animate) {
        SidebarNavItem item = findMenuItemById(menuId);
        if (item == null || !item.hasFragment()) {
            // Fallback: id tak dikenal / bukan fragment -> tampilkan Dashboard.
            item = findMenuItemById(ID_DASHBOARD);
            menuId = ID_DASHBOARD;
        }
        if (item == null) {
            return;
        }

        Fragment fragment = item.fragmentFactory.get();
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.hostFragmentContainer, fragment)
                .commit();

        currentMenuId = menuId;
        selectSidebarItem(menuId);
    }

    @Override
    protected void onSidebarLogout() {
        String username = currentUsername();
        String activity = RiwayatUtils.formatLogoutActivity(username);
        RiwayatUtils.saveToRiwayat(this, username, activity, new RiwayatUtils.RiwayatCallback() {
            @Override
            public void onSuccess() {
                Log.d("Logout", "saved");
            }

            @Override
            public void onError(String e) {
                Log.e("Logout", "err: " + e);
            }
        });

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
            return;
        }
        if (currentMenuId != ID_DASHBOARD) {
            showMenu(ID_DASHBOARD, false);
            return;
        }
        new AlertDialog.Builder(this)
                .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
                .setCancelable(false)
                .setPositiveButton("Ya", (d, w) -> {
                    finishAffinity();
                    System.exit(0);
                })
                .setNegativeButton("Tidak", null)
                .show();
    }
}
