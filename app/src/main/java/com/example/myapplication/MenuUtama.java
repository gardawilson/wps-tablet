package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.utils.PermissionUtils;
import com.example.myapplication.utils.RiwayatUtils;
import com.example.myapplication.utils.SharedPrefUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class MenuUtama extends AppCompatActivity {

    private ViewPager2 menuPager;
    private TabLayout tabDots;
    private TextView usernameView;
    private MaterialButton BtnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);

        usernameView = findViewById(R.id.usernameView);
        BtnLogout    = findViewById(R.id.BtnLogout);
        menuPager    = findViewById(R.id.menuPager);
        tabDots      = findViewById(R.id.tabDots);

        String username = SharedPrefUtils.getUsername(this);
        usernameView.setText(username + " !");

        // ===== Definisikan semua menu di urutan yang kamu mau =====
        List<MenuItem> menus = new ArrayList<>();
        menus.add(new MenuItem("Input Label",       R.drawable.label,             null,                   v -> startActivity(new Intent(this, InputLabel.class))));
        menus.add(new MenuItem("Proses Produksi",   R.drawable.proses,            null,                   v -> startActivity(new Intent(this, ProsesProduksi.class))));
        menus.add(new MenuItem("Stock Opname",      R.drawable.ic_stock_opname,   "stock_opname:read",    v -> startActivity(new Intent(this, StockOpnameMenu.class))));
        menus.add(new MenuItem("Proses Sawmill",    R.drawable.sawmill,           "proses_sawmill:read",  v -> startActivity(new Intent(this, ProsesSawmill.class))));
        menus.add(new MenuItem("Laporan",           R.drawable.ic_report,         "laporan:read",         v -> startActivity(new Intent(this, LaporanKategori.class))));
        menus.add(new MenuItem("Penjualan",         R.drawable.ic_sales,          null,                   v -> startActivity(new Intent(this, Penjualan.class))));
        menus.add(new MenuItem("SPK",               R.drawable.ic_spk,         "spk:read",                   v -> startActivity(new Intent(this, SPK.class))));
        menus.add(new MenuItem("Grade ABC",         R.drawable.ic_grade,         "grade_abc:read",                   v -> startActivity(new Intent(this, GradeABC.class))));
        menus.add(new MenuItem("Planning Mesin",    R.drawable.ic_schedule,         "planning_mesin:read",                   v -> startActivity(new Intent(this, PlanningMesin.class))));

        // ===== Adapter 6 item per halaman (2 kolom × 3 baris) =====
        MenuPagerAdapter adapter = new MenuPagerAdapter(menus, this);
        menuPager.setAdapter(adapter);

        new TabLayoutMediator(tabDots, menuPager, (tab, pos) -> {}).attach();

        // >>> Tambahkan dua baris ini:
        setupDots();
        syncPagerToDots();

        // Logout
        BtnLogout.setOnClickListener(view -> {
            String activity = RiwayatUtils.formatLogoutActivity(username);
            RiwayatUtils.saveToRiwayat(MenuUtama.this, username, activity, new RiwayatUtils.RiwayatCallback() {
                @Override public void onSuccess() { Log.d("Logout", "saved"); }
                @Override public void onError(String e) { Log.e("Logout", "err: " + e); }
            });
            startActivity(new Intent(MenuUtama.this, MainActivity.class));
        });
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

    /** Set ikon bulat untuk setiap tab sebagai dot indicator */
    private void setupDots() {
        // Set semua tab: unselected + beri padding antar dot
        for (int i = 0; i < tabDots.getTabCount(); i++) {
            TabLayout.Tab tab = tabDots.getTabAt(i);
            if (tab != null) {
                tab.setIcon(R.drawable.tab_dot_unselected);
                // jarak antar dot
                View strip = tabDots.getChildAt(0); // TabLayout's sliding strip (LinearLayout)
                if (strip instanceof ViewGroup) {
                    View tabView = ((ViewGroup) strip).getChildAt(i);
                    if (tabView != null) {
                        int pad = (int) (8 * getResources().getDisplayMetrics().density);
                        tabView.setPadding(pad, pad, pad, pad);
                    }
                }
            }
        }
        // tandai tab pertama sebagai selected
        TabLayout.Tab first = tabDots.getTabAt(0);
        if (first != null) first.setIcon(R.drawable.tab_dot_selected);

        tabDots.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab)   { tab.setIcon(R.drawable.tab_dot_selected); }
            @Override public void onTabUnselected(TabLayout.Tab tab) { tab.setIcon(R.drawable.tab_dot_unselected); }
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    /** Sinkronkan ViewPager2 -> TabLayout (kalau user swipe) */
    private void syncPagerToDots() {
        menuPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override public void onPageSelected(int position) {
                TabLayout.Tab tab = tabDots.getTabAt(position);
                if (tab != null) tab.select();
            }
        });
    }


    // ===== Model item =====
    static class MenuItem {
        String title;
        int iconRes;
        String permissionKey; // bisa null
        View.OnClickListener onClick;

        MenuItem(String title, int iconRes, String permissionKey, View.OnClickListener onClick) {
            this.title = title;
            this.iconRes = iconRes;
            this.permissionKey = permissionKey;
            this.onClick = onClick;
        }
    }

    // ===== Adapter: 6 item/page, 2 kolom × 3 baris =====
    // Di MenuUtama.java, ganti adapter lamamu dengan ini:
    static class MenuPagerAdapter extends RecyclerView.Adapter<MenuPagerAdapter.PageVH> {
        private final List<List<MenuItem>> pages;
        private final MenuUtama activity;

        MenuPagerAdapter(List<MenuItem> allMenus, MenuUtama activity) {
            this.activity = activity;
            this.pages = new ArrayList<>();
            // chunk 6 item per halaman
            for (int i = 0; i < allMenus.size(); i += 6) {
                int end = Math.min(i + 6, allMenus.size());
                pages.add(new ArrayList<>(allMenus.subList(i, end)));
            }
        }

        @Override
        public PageVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.page_menu_grid_6, parent, false);
            return new PageVH(v);
        }

        @Override
        public void onBindViewHolder(PageVH h, int position) {
            List<MenuItem> items = pages.get(position);

            // siapkan array referensi 6 slot icon/title/card
            ImageView[] icons = new ImageView[]{h.icon1, h.icon2, h.icon3, h.icon4, h.icon5, h.icon6};
            TextView[]  titles = new TextView[]{h.title1, h.title2, h.title3, h.title4, h.title5, h.title6};
            View[]      cards = new View[]{h.card1, h.card2, h.card3, h.card4, h.card5, h.card6};

            // reset semua slot (hide dulu)
            for (int i = 0; i < 6; i++) {
                cards[i].setVisibility(View.INVISIBLE);
                cards[i].setOnClickListener(null);
            }

            // isi sesuai urutan
            for (int i = 0; i < items.size() && i < 6; i++) {
                MenuItem it = items.get(i);

                // set icon/title
                icons[i].setImageResource(it.iconRes);
                titles[i].setText(it.title);

                // permission per kartu (opsional)
                if (it.permissionKey != null) {
                    PermissionUtils.permissionCheck(activity, cards[i], it.permissionKey);
                }

                // klik
                cards[i].setOnClickListener(it.onClick);

                // tampilkan
                cards[i].setVisibility(View.VISIBLE);
            }
        }

        @Override public int getItemCount() { return pages.size(); }

        static class PageVH extends RecyclerView.ViewHolder {
            // card roots
            View card1, card2, card3, card4, card5, card6;
            // icons
            ImageView icon1, icon2, icon3, icon4, icon5, icon6;
            // titles
            TextView title1, title2, title3, title4, title5, title6;

            PageVH(View itemView) {
                super(itemView);
                card1 = itemView.findViewById(R.id.card1);
                card2 = itemView.findViewById(R.id.card2);
                card3 = itemView.findViewById(R.id.card3);
                card4 = itemView.findViewById(R.id.card4);
                card5 = itemView.findViewById(R.id.card5);
                card6 = itemView.findViewById(R.id.card6);

                icon1 = itemView.findViewById(R.id.icon1);
                icon2 = itemView.findViewById(R.id.icon2);
                icon3 = itemView.findViewById(R.id.icon3);
                icon4 = itemView.findViewById(R.id.icon4);
                icon5 = itemView.findViewById(R.id.icon5);
                icon6 = itemView.findViewById(R.id.icon6);

                title1 = itemView.findViewById(R.id.title1);
                title2 = itemView.findViewById(R.id.title2);
                title3 = itemView.findViewById(R.id.title3);
                title4 = itemView.findViewById(R.id.title4);
                title5 = itemView.findViewById(R.id.title5);
                title6 = itemView.findViewById(R.id.title6);
            }
        }
    }

}
