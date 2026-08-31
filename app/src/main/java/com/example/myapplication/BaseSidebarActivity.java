package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.sidebar.SidebarMenuAdapter;
import com.example.myapplication.sidebar.SidebarNavItem;
import com.example.myapplication.sidebar.SidebarSection;
import com.example.myapplication.utils.SharedPrefUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Activity dasar yang menyediakan sidebar permanen untuk semua menu.
 *
 * Cara pakai pada Activity turunan:
 * <pre>
 *   public class Foo extends BaseSidebarActivity {
 *       protected void onCreate(Bundle b) {
 *           super.onCreate(b);
 *           setContent(R.layout.activity_foo);   // ganti setContentView
 *           ...
 *       }
 *   }
 * </pre>
 *
 * Layar tetap berupa Activity terpisah, namun setiap layar merender sidebar
 * yang sama sehingga terlihat menetap saat berpindah menu. Status collapse
 * sidebar disimpan di SharedPreferences agar konsisten antar layar.
 */
public abstract class BaseSidebarActivity extends AppCompatActivity {

    protected static final int ID_DASHBOARD = 1;
    protected static final int ID_LABEL = 2;
    protected static final int ID_LABEL_ST = 3;
    protected static final int ID_LABEL_S4S = 4;
    protected static final int ID_LABEL_FJ = 5;
    protected static final int ID_LABEL_MLD = 6;
    protected static final int ID_LABEL_LMT = 7;
    protected static final int ID_LABEL_CCA = 8;
    protected static final int ID_LABEL_SND = 9;
    protected static final int ID_LABEL_BJ = 10;
    protected static final int ID_PROSES = 11;
    protected static final int ID_PROSES_S4S = 12;
    protected static final int ID_PROSES_FJ = 13;
    protected static final int ID_PROSES_MLD = 14;
    protected static final int ID_PROSES_LMT = 15;
    protected static final int ID_PROSES_CCA = 16;
    protected static final int ID_PROSES_SND = 17;
    protected static final int ID_PROSES_BJ = 18;
    protected static final int ID_PROSES_BONGKAR = 19;
    protected static final int ID_SAWMILL = 20;
    protected static final int ID_SAWMILL_TELLY = 21;
    protected static final int ID_SAWMILL_QC = 22;
    protected static final int ID_SAWMILL_PENERIMAAN = 23;
    protected static final int ID_STOCK_OPNAME = 24;
    protected static final int ID_STOCK_OPNAME_NORMAL = 25;
    protected static final int ID_STOCK_OPNAME_ASCEND = 26;
    protected static final int ID_GRADE_ABC = 27;
    protected static final int ID_PLANNING_MESIN = 28;
    protected static final int ID_NYANGKUT = 29;
    protected static final int ID_LAPORAN = 30;
    protected static final int ID_LAPORAN_KB = 31;
    protected static final int ID_LAPORAN_KB_RAMBUNG = 32;
    protected static final int ID_LAPORAN_ST = 33;
    protected static final int ID_LAPORAN_S4S = 34;
    protected static final int ID_LAPORAN_FJ = 35;
    protected static final int ID_LAPORAN_MLD = 36;
    protected static final int ID_LAPORAN_LMT = 37;
    protected static final int ID_LAPORAN_CCA = 38;
    protected static final int ID_LAPORAN_SND = 39;
    protected static final int ID_LAPORAN_BJ = 40;
    protected static final int ID_LAPORAN_MANAJEMEN = 41;
    protected static final int ID_LAPORAN_VERIFIKASI = 42;
    protected static final int ID_PENJUALAN = 43;
    protected static final int ID_PENJUALAN_ST_SND = 44;
    protected static final int ID_PENJUALAN_BJ = 45;
    protected static final int ID_AUDIT = 46;
    protected static final int ID_SPK = 47;
    protected static final int ID_LOGOUT = 48;
    protected static final int ID_MAPPING = 49;

    private static final String SIDEBAR_PREFS = "sidebar_prefs";
    private static final String KEY_COLLAPSED = "collapsed";
    private static final String KEY_SCROLL_POS = "scroll_pos";
    private static final String KEY_SCROLL_OFFSET = "scroll_offset";

    /** Id menu (ID_*) yang harus dibuka HostActivity sebagai Fragment. */
    public static final String EXTRA_MENU_ID = "com.example.myapplication.EXTRA_MENU_ID";

    private View sidebarContainer;
    private View sidebarHeader;
    private View sidebarLogo;
    private RecyclerView sidebarMenuList;
    private LinearLayoutManager sidebarLayoutManager;
    private SidebarMenuAdapter sidebarMenuAdapter;
    private ImageView sidebarToggle;
    private TextView sidebarTitle;
    private TextView sidebarSubtitle;

    private List<SidebarSection> sidebarSections;
    private boolean isSidebarCollapsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Matikan animasi masuk Activity ini (pelengkap windowAnimationStyle).
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSidebarScroll();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    /**
     * Menggantikan {@link #setContentView(int)}. Memuat kerangka sidebar lalu
     * meng-inflate {@code contentLayoutRes} ke area konten.
     */
    protected void setContent(@LayoutRes int contentLayoutRes) {
        super.setContentView(R.layout.activity_base_sidebar);

        FrameLayout contentContainer = findViewById(R.id.baseContentContainer);
        getLayoutInflater().inflate(contentLayoutRes, contentContainer, true);

        bindSidebarViews();
        setupSidebar();
    }

    private void bindSidebarViews() {
        sidebarContainer = findViewById(R.id.sidebarContainer);
        sidebarHeader = findViewById(R.id.sidebarHeader);
        sidebarLogo = findViewById(R.id.sidebarLogo);
        sidebarMenuList = findViewById(R.id.sidebarMenuList);
        sidebarToggle = findViewById(R.id.sidebarToggle);
        sidebarTitle = findViewById(R.id.sidebarTitle);
        sidebarSubtitle = findViewById(R.id.sidebarSubtitle);
    }

    private void setupSidebar() {
        sidebarSections = buildSidebarSections();

        int selectedId = getSelectedMenuId();

        sidebarMenuAdapter = new SidebarMenuAdapter(this, sidebarSections);
        sidebarMenuAdapter.setOnLeafClickListener(this::handleLeafSelected);
        sidebarMenuAdapter.setSelectedLeafId(selectedId);
        sidebarMenuAdapter.expandParentOf(selectedId);
        sidebarLayoutManager = new LinearLayoutManager(this);
        sidebarMenuList.setLayoutManager(sidebarLayoutManager);
        sidebarMenuList.setAdapter(sidebarMenuAdapter);
        // Matikan animasi item RecyclerView supaya sidebar tidak "berkedip".
        sidebarMenuList.setItemAnimator(null);

        sidebarToggle.setOnClickListener(v -> toggleSidebar());

        updateSidebarState(readCollapsedPref());
        restoreSidebarScroll();
    }

    private void saveSidebarScroll() {
        if (sidebarLayoutManager == null) {
            return;
        }
        int pos = sidebarLayoutManager.findFirstVisibleItemPosition();
        if (pos == RecyclerView.NO_POSITION) {
            return;
        }
        View first = sidebarMenuList.getChildAt(0);
        int offset = (first == null) ? 0 : first.getTop() - sidebarMenuList.getPaddingTop();
        getSharedPreferences(SIDEBAR_PREFS, MODE_PRIVATE)
                .edit()
                .putInt(KEY_SCROLL_POS, pos)
                .putInt(KEY_SCROLL_OFFSET, offset)
                .apply();
    }

    private void restoreSidebarScroll() {
        SharedPreferences prefs = getSharedPreferences(SIDEBAR_PREFS, MODE_PRIVATE);
        int pos = prefs.getInt(KEY_SCROLL_POS, 0);
        int offset = prefs.getInt(KEY_SCROLL_OFFSET, 0);
        if ((pos > 0 || offset != 0) && pos < sidebarMenuAdapter.getItemCount()) {
            sidebarLayoutManager.scrollToPositionWithOffset(pos, offset);
        }
    }

    /**
     * ID menu yang sedang aktif. Default: dicocokkan dari kelas Activity ini
     * terhadap {@code targetActivity} di pohon menu. Activity tanpa entri menu
     * (mis. dashboard) bisa meng-override.
     */
    protected int getSelectedMenuId() {
        return findMenuIdForActivity(getClass());
    }

    private int findMenuIdForActivity(Class<?> activityClass) {
        if (sidebarSections == null) {
            return -1;
        }
        for (SidebarSection section : sidebarSections) {
            for (SidebarNavItem top : section.items) {
                if (!top.hasChildren() && top.targetActivity == activityClass) {
                    return top.id;
                }
                for (SidebarNavItem child : top.children) {
                    if (child.targetActivity == activityClass) {
                        return child.id;
                    }
                }
            }
        }
        return -1;
    }

    /** Cari item menu (leaf/flat) berdasarkan id. Null bila tidak ada. */
    protected SidebarNavItem findMenuItemById(int id) {
        if (sidebarSections == null) {
            return null;
        }
        for (SidebarSection section : sidebarSections) {
            for (SidebarNavItem top : section.items) {
                if (top.id == id) {
                    return top;
                }
                for (SidebarNavItem child : top.children) {
                    if (child.id == id) {
                        return child;
                    }
                }
            }
        }
        return null;
    }

    /** Perbarui highlight & auto-expand parent pada sidebar. */
    protected void selectSidebarItem(int id) {
        if (sidebarMenuAdapter == null) {
            return;
        }
        sidebarMenuAdapter.setSelectedLeafId(id);
        sidebarMenuAdapter.expandParentOf(id);
    }

    protected void handleLeafSelected(SidebarNavItem item) {
        if (item.isLogout) {
            onSidebarLogout();
            return;
        }
        if (item.hasFragment()) {
            openFragmentItem(item);
            return;
        }
        if (item.targetActivity == null || item.targetActivity == getClass()) {
            // Sudah berada di layar ini - tidak perlu berpindah.
            return;
        }

        Intent intent = new Intent(this, item.targetActivity);
        // CLEAR_TOP + SINGLE_TOP: jika layar tujuan sudah ada di back stack
        // (mis. dashboard), bawa ke depan tanpa membuat instance baru.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);

        // Jaga agar back stack tetap dangkal: layar non-home ditutup supaya
        // tombol back selalu kembali ke dashboard, bukan menelusuri riwayat menu.
        if (!isSidebarHome()) {
            finish();
            overridePendingTransition(0, 0);
        }
    }

    /**
     * Membuka item yang sudah berbasis Fragment. Dari Activity lama (belum
     * dimigrasi) kita tidak bisa menampung Fragment, jadi routing ke
     * {@link HostActivity} sambil membawa id menu yang dipilih. HostActivity
     * meng-override method ini untuk menukar Fragment langsung tanpa pindah
     * Activity (sidebar tidak dirender ulang).
     */
    protected void openFragmentItem(SidebarNavItem item) {
        Intent intent = new Intent(this, HostActivity.class);
        intent.putExtra(EXTRA_MENU_ID, item.id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
        if (!isSidebarHome()) {
            finish();
            overridePendingTransition(0, 0);
        }
    }

    /**
     * Dipanggil saat item "Logout" ditekan. Default: tidak melakukan apa pun.
     * Activity home (MenuUtama) meng-override untuk menjalankan proses logout.
     */
    protected void onSidebarLogout() {
        // no-op
    }

    /**
     * True jika Activity ini adalah layar utama/dashboard. Saat true, navigasi
     * sidebar tidak menutup Activity ini sehingga tetap menjadi akar back stack.
     */
    protected boolean isSidebarHome() {
        return false;
    }

    private void toggleSidebar() {
        updateSidebarState(!isSidebarCollapsed);
        writeCollapsedPref(isSidebarCollapsed);
    }

    private void updateSidebarState(boolean collapsed) {
        isSidebarCollapsed = collapsed;

        ViewGroup.LayoutParams layoutParams = sidebarContainer.getLayoutParams();
        layoutParams.width = dpToPx(collapsed ? 76 : 240);
        sidebarContainer.setLayoutParams(layoutParams);

        ViewGroup.LayoutParams headerParams = sidebarHeader.getLayoutParams();
        headerParams.height = dpToPx(collapsed ? 76 : 116);
        sidebarHeader.setLayoutParams(headerParams);

        sidebarLogo.setVisibility(View.VISIBLE);
        sidebarTitle.setVisibility(collapsed ? View.GONE : View.VISIBLE);
        sidebarSubtitle.setVisibility(collapsed ? View.GONE : View.VISIBLE);

        sidebarToggle.setImageResource(collapsed ? R.drawable.ic_sidebar_expand : R.drawable.ic_sidebar_collapse);
        sidebarMenuAdapter.setCollapsedMode(collapsed);
    }

    private boolean readCollapsedPref() {
        SharedPreferences prefs = getSharedPreferences(SIDEBAR_PREFS, MODE_PRIVATE);
        return prefs.getBoolean(KEY_COLLAPSED, false);
    }

    private void writeCollapsedPref(boolean collapsed) {
        getSharedPreferences(SIDEBAR_PREFS, MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_COLLAPSED, collapsed)
                .apply();
    }

    protected String currentUsername() {
        String username = SharedPrefUtils.getUsername(this);
        if (username == null || username.trim().isEmpty()) {
            return "-";
        }
        username = username.trim();
        return username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private List<SidebarSection> buildSidebarSections() {
        return Arrays.asList(
                new SidebarSection(getString(R.string.sidebar_section_main), Arrays.asList(
                        // Dashboard sudah dimigrasi ke Fragment (HostActivity).
                        SidebarNavItem.flatFragment(ID_DASHBOARD, getString(R.string.nav_dashboard), R.drawable.ic_dashboard, DashboardFragment::new)
                )),
                new SidebarSection(getString(R.string.sidebar_section_production), Arrays.asList(
                        SidebarNavItem.parent(ID_LABEL, getString(R.string.nav_input_label), R.drawable.label, Arrays.asList(
                                SidebarNavItem.leafChild(ID_LABEL_ST, "Sawn Timber", R.drawable.ic_sb_sawn_timber, SawnTimberCategory.class, "label_st:read"),
                                SidebarNavItem.leafChild(ID_LABEL_S4S, "S4S", R.drawable.ic_sb_s4s, S4S.class, "label_s4s:read"),
                                SidebarNavItem.leafChild(ID_LABEL_FJ, "Finger Joint", R.drawable.ic_sb_finger_joint, FingerJoint.class, "label_fj:read"),
                                SidebarNavItem.leafChild(ID_LABEL_MLD, "Moulding", R.drawable.ic_sb_moulding, Moulding.class, "label_mld:read"),
                                SidebarNavItem.leafChild(ID_LABEL_LMT, "Laminating", R.drawable.ic_sb_laminating, Laminating.class, "label_lmt:read"),
                                SidebarNavItem.leafChild(ID_LABEL_CCA, "Cross Cut", R.drawable.ic_sb_cross_cut, CrossCut.class, "label_cca:read"),
                                SidebarNavItem.leafChild(ID_LABEL_SND, "Sanding", R.drawable.ic_sb_sanding, Sanding.class, "label_snd:read"),
                                SidebarNavItem.leafChild(ID_LABEL_BJ, "Barang Jadi (Packing)", R.drawable.ic_sb_packing, Packing.class, "label_bj:read")
                        )),
                        SidebarNavItem.parent(ID_PROSES, getString(R.string.nav_proses_produksi), R.drawable.proses, Arrays.asList(
                                SidebarNavItem.leafChild(ID_PROSES_S4S, "S4S", R.drawable.ic_sb_s4s, ProsesProduksiS4S.class, "proses_s4s:read"),
                                SidebarNavItem.leafChild(ID_PROSES_FJ, "Finger Joint", R.drawable.ic_sb_finger_joint, ProsesProduksiFJ.class, "proses_fj:read"),
                                SidebarNavItem.leafChild(ID_PROSES_MLD, "Moulding", R.drawable.ic_sb_moulding, ProsesProduksiMoulding.class, "proses_mld:read"),
                                SidebarNavItem.leafChild(ID_PROSES_LMT, "Laminating", R.drawable.ic_sb_laminating, ProsesProduksiLaminating.class, "proses_lmt:read"),
                                SidebarNavItem.leafChild(ID_PROSES_CCA, "Cross Cut", R.drawable.ic_sb_cross_cut, ProsesProduksiCrossCut.class, "proses_cca:read"),
                                SidebarNavItem.leafChild(ID_PROSES_SND, "Sanding", R.drawable.ic_sb_sanding, ProsesProduksiSanding.class, "proses_snd:read"),
                                SidebarNavItem.leafChild(ID_PROSES_BJ, "Barang Jadi (Packing)", R.drawable.ic_sb_packing, ProsesProduksiPacking.class, "proses_bj:read"),
                                SidebarNavItem.leafChild(ID_PROSES_BONGKAR, "Bongkar Susun", R.drawable.ic_sb_bongkar_susun, BongkarSusun.class, "bongkar_susun:read")
                        )),
                        SidebarNavItem.parent(ID_SAWMILL, getString(R.string.nav_sawmill), R.drawable.sawmill, Arrays.asList(
                                SidebarNavItem.leafChild(ID_SAWMILL_TELLY, "Lembar Telly", R.drawable.ic_sb_telly, Sawmill.class, null),
                                SidebarNavItem.leafChild(ID_SAWMILL_QC, "Quality Control", R.drawable.ic_sb_qc, QcSawmill.class, null),
                                SidebarNavItem.leafChild(ID_SAWMILL_PENERIMAAN, "Penerimaan ST", R.drawable.ic_sb_receive, PenerimaanStDariSawmill.class, null)
                        ))
                )),
                new SidebarSection(getString(R.string.sidebar_section_operations), Arrays.asList(
                        SidebarNavItem.parent(ID_STOCK_OPNAME, getString(R.string.nav_stock_opname), R.drawable.ic_stock_opname, Arrays.asList(
                                SidebarNavItem.leafChild(ID_STOCK_OPNAME_NORMAL, "Stock Opname", R.drawable.ic_sb_warehouse, StockOpname.class, null),
                                SidebarNavItem.leafChild(ID_STOCK_OPNAME_ASCEND, "Stock Opname Ascend", R.drawable.ic_sb_ascend, StockOpnameAscend.class, null)
                        )),
                        SidebarNavItem.flat(ID_GRADE_ABC, getString(R.string.nav_grade_abc), R.drawable.ic_grade, GradeABC.class),
                        SidebarNavItem.flat(ID_PLANNING_MESIN, getString(R.string.nav_planning_mesin), R.drawable.ic_schedule, PlanningMesin.class),
                        SidebarNavItem.flat(ID_NYANGKUT, getString(R.string.nav_nyangkut), R.drawable.ic_pending, Nyangkut.class),
                        SidebarNavItem.flatFragment(ID_MAPPING, "Mapping", R.drawable.ic_sb_mapping, MappingFragment::new)
                )),
                new SidebarSection(getString(R.string.sidebar_section_reports), Arrays.asList(
                        SidebarNavItem.parent(ID_LAPORAN, getString(R.string.nav_laporan), R.drawable.ic_report, Arrays.asList(
                                SidebarNavItem.leafChildFragment(ID_LAPORAN_KB, "Laporan KB", R.drawable.ic_sb_logs, LaporanKBFragment::new, null),
                                SidebarNavItem.leafChildFragment(ID_LAPORAN_KB_RAMBUNG, "Laporan KB Rambung", R.drawable.ic_sb_logs, LaporanKbRambungFragment::new, null),
                                SidebarNavItem.leafChildFragment(ID_LAPORAN_ST, "Laporan ST", R.drawable.ic_sb_sawn_timber, LaporanSTFragment::new, null),
                                SidebarNavItem.leafChildFragment(ID_LAPORAN_S4S, "Laporan S4S", R.drawable.ic_sb_s4s, LaporanS4SFragment::new, null),
                                SidebarNavItem.leafChildFragment(ID_LAPORAN_FJ, "Laporan FJ", R.drawable.ic_sb_finger_joint, LaporanFJFragment::new, null),
                                SidebarNavItem.leafChildFragment(ID_LAPORAN_MLD, "Laporan Moulding", R.drawable.ic_sb_moulding, LaporanMLDFragment::new, null),
                                SidebarNavItem.leafChildFragment(ID_LAPORAN_LMT, "Laporan Laminating", R.drawable.ic_sb_laminating, LaporanLMTFragment::new, null),
                                SidebarNavItem.leafChildFragment(ID_LAPORAN_CCA, "Laporan Cross Cut", R.drawable.ic_sb_cross_cut, LaporanCCAFragment::new, null),
                                SidebarNavItem.leafChildFragment(ID_LAPORAN_SND, "Laporan Sanding", R.drawable.ic_sb_sanding, LaporanSNDFragment::new, null),
                                SidebarNavItem.leafChildFragment(ID_LAPORAN_BJ, "Laporan Packing", R.drawable.ic_sb_packing, LaporanBJFragment::new, null),
                                SidebarNavItem.leafChildFragment(ID_LAPORAN_MANAJEMEN, "Laporan Manajemen", R.drawable.ic_sb_management, LaporanManajemenFragment::new, null),
                                SidebarNavItem.leafChildFragment(ID_LAPORAN_VERIFIKASI, "Laporan Verifikasi", R.drawable.ic_sb_qc, LaporanVerifikasiFragment::new, null)
                        )),
                        SidebarNavItem.parent(ID_PENJUALAN, getString(R.string.nav_penjualan), R.drawable.ic_sales, Arrays.asList(
                                SidebarNavItem.leafChild(ID_PENJUALAN_ST_SND, "Penjualan ST/Sanding", R.drawable.ic_sb_sell, PenjualanStSnd.class, "penjualan_st_snd:read"),
                                SidebarNavItem.leafChild(ID_PENJUALAN_BJ, "Penjualan Packing", R.drawable.ic_sb_sell, PenjualanBJ.class, "penjualan_bj:read")
                        )),
                        SidebarNavItem.flat(ID_AUDIT, getString(R.string.nav_audit), R.drawable.ic_history, AuditActivity.class),
                        SidebarNavItem.flat(ID_SPK, getString(R.string.nav_spk), R.drawable.ic_spk, SPK.class)
                )),
                new SidebarSection(getString(R.string.sidebar_section_system), Arrays.asList(
                        SidebarNavItem.logout(ID_LOGOUT, getString(R.string.nav_logout), R.drawable.ic_logout)
                ))
        );
    }
}
