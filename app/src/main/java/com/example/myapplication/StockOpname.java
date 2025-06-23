package com.example.myapplication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.api.ProductionApi;
import com.example.myapplication.api.StockOpnameApi;
import com.example.myapplication.model.LokasiBlok;
import com.example.myapplication.model.StockOpnameData;
import com.example.myapplication.model.StockOpnameDataByNoSO;
import com.example.myapplication.model.StockOpnameDataInputByNoSO;
import com.example.myapplication.model.TooltipData;
import com.example.myapplication.model.UserIDSO;
import com.example.myapplication.utils.WebSocketConnection;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StockOpname extends AppCompatActivity implements StockOpnameDataInputAdapter.OnDeleteConfirmationListener, WebSocketConnection.WebSocketListener {

    private WebSocketConnection webSocketConnection;

    private String currentSearchText = "";



    // UI Elements
    private ProgressBar loadingIndicator;
    private ProgressBar loadingIndicatorBefore;
    private ProgressBar loadingIndicatorAfter;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewBefore;
    private RecyclerView recyclerViewAfter;
    private SearchView searchView;
    private Button filterButton;
    private Spinner blokSpinner;
    private Spinner idLokasiSpinner;
    private Spinner spinnerNoSO;

    // Adapters
    private StockOpnameAdapter adapter;
    private StockOpnameDataAdapter stockOpnameDataAdapter;
    private StockOpnameDataInputAdapter stockOpnameDataInputAdapter;

    // Data Lists
    private List<StockOpnameData> stockOpnames = new ArrayList<>();
    private List<StockOpnameDataByNoSO> stockOpnameDataByNoSOList = new ArrayList<>();
    private List<StockOpnameDataInputByNoSO> stockOpnameDataInputByNoSOList = new ArrayList<>();

    // State Flags
    private boolean isLoading = false;
    private boolean isLoadingBefore = false;
    private boolean isLoadingAfter = false;

    // Pagination
    private int currentPage = 0;  // Halaman pertama
    private final int LIMIT = 100;  // Data per halaman
    private int currentPageForNoSO = 0;
    private int currentPageForNoSOInput = 0;
    private int countNewDataNotification = 1;

    // Selected Values
    private String selectedNoSO;
    private String selectedTglSO;
    private String selectedLabel;
    private String selectedUserID;
    private String selectedIdLokasi;
    private String selectedBlok;
    private Set<String> selectedLabels = new HashSet<>();

    // Executor for background tasks
    private ExecutorService executorService;


    private final Handler handler = new Handler();

    private final Runnable fetchDataRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("Polling", "Fetching data...");
            filterDataInputByNoSO(selectedIdLokasi, selectedLabels, selectedUserID, 0);
            fetchDataByNoSO(selectedIdLokasi, selectedLabels, 0);
            handler.postDelayed(this, 5000); // Jalankan lagi setelah 5 detik
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_opname);

        initializeViews();
        initializeRecyclerView();
        executorService = Executors.newCachedThreadPool();  // More efficient threading

        loadMoreData();

        setupNoSOSpinner();

        // Inisialisasi WebSocketConnection
        webSocketConnection = WebSocketConnection.getInstance();
        webSocketConnection.setListener(this); // Set listener ke Activity ini
        webSocketConnection.connect(); // Mulai koneksi WebSocket

        setListeners();

    }

    // Mengambil data tooltip dan menampilkan tooltip
    private void fetchDataAndShowTooltip(View anchorView, String noLabel, String tableH, String tableD, String mainColumn, boolean isLeft) {
        executorService.execute(() -> {
            // Ambil data tooltip menggunakan ProductionApi
            Log.d("TooltipDebug", "Tooltip dijalankan");

            TooltipData tooltipData = ProductionApi.getTooltipData(noLabel, tableH, tableD, mainColumn);

            runOnUiThread(() -> {
                if (tooltipData != null) {
                    // Pengecekan lebih lanjut jika data dalam tooltipData null
                    if (tooltipData.getNoLabel() != null && tooltipData.getTableData() != null) {

                        // Tampilkan tooltip dengan data yang diperoleh
                        showTooltip(
                                anchorView,
                                tooltipData.getNoLabel(),
                                tooltipData.getFormattedDateTime(),
                                tooltipData.getJenis(),
                                tooltipData.getSpkDetail(),
                                tooltipData.getSpkAsalDetail(),
                                tooltipData.getNamaGrade(),
                                tooltipData.isLembur(),
                                tooltipData.getTableData(),
                                tooltipData.getTotalPcs(),
                                tooltipData.getTotalM3(),
                                tooltipData.getTotalTon(),
                                tooltipData.getNoPlat(),
                                tooltipData.getNoKBSuket(),
                                tableH,
                                isLeft
                        );



                    } else {
                        // Tampilkan pesan error jika data utama tidak ada
                        Toast.makeText(this, "Data tooltip tidak lengkap", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Tampilkan pesan error jika tooltipData null
                    Toast.makeText(this, "Error fetching tooltip data", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

    private void showTooltip(View anchorView, String noLabel, String formattedDateTime, String jenis, String spkDetail, String spkAsalDetail, String namaGrade, boolean isLembur, List<String[]> tableData, int totalPcs, double totalM3, double totalTon, String noPlat, String noKBSuket, String tableH, boolean isLeft) {
        // Inflate layout tooltip

        View tooltipView;

        if (isLeft) {
            tooltipView = LayoutInflater.from(this).inflate(R.layout.tooltip_layout_left, null);
        } else {
            tooltipView = LayoutInflater.from(this).inflate(R.layout.tooltip_layout_right, null);

        }

        // Set data pada TextView
        ((TextView) tooltipView.findViewById(R.id.tvNoLabel)).setText(noLabel);
        ((TextView) tooltipView.findViewById(R.id.tvDateTime)).setText(formattedDateTime);
        ((TextView) tooltipView.findViewById(R.id.tvJenis)).setText(jenis);
        ((TextView) tooltipView.findViewById(R.id.tvNoSPK)).setText(spkDetail);
        ((TextView) tooltipView.findViewById(R.id.tvNoSPKAsal)).setText(spkAsalDetail);
        ((TextView) tooltipView.findViewById(R.id.tvNamaGrade)).setText(namaGrade);
        ((TextView) tooltipView.findViewById(R.id.tvIsLembur)).setText(isLembur ? "Yes" : "No");
        ((TextView) tooltipView.findViewById(R.id.tvNoPlat)).setText(noPlat);
        ((TextView) tooltipView.findViewById(R.id.tvNoKBSuket)).setText(noKBSuket);

        // Referensi TableLayout
        TableLayout tableLayout = tooltipView.findViewById(R.id.tabelDetailTooltip);

        // Membuat Header Tabel Secara Dinamis
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(getResources().getColor(R.color.hijau));

        String[] headerTexts = {"Tebal", "Lebar", "Panjang", "Pcs"};
        for (String headerText : headerTexts) {
            TextView headerTextView = new TextView(this);
            headerTextView.setText(headerText);
            headerTextView.setGravity(Gravity.CENTER);
            headerTextView.setPadding(8, 8, 8, 8);
            headerTextView.setTextColor(Color.WHITE);
            headerTextView.setTypeface(Typeface.DEFAULT_BOLD);
            headerRow.addView(headerTextView);
        }

        // Tambahkan Header ke TableLayout
        tableLayout.addView(headerRow);

        // Tambahkan Data ke TableLayout
        for (String[] row : tableData) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            tableRow.setBackgroundColor(getResources().getColor(R.color.background_cream));

            for (String cell : row) {
                TextView textView = new TextView(this);
                textView.setText(cell);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(8, 8, 8, 8);
                textView.setTextColor(Color.BLACK);
                tableRow.addView(textView);
            }
            tableLayout.addView(tableRow);
        }

        // Tambahkan Baris untuk Total Pcs
        TableRow totalRow = new TableRow(this);
        totalRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        totalRow.setBackgroundColor(Color.WHITE);

        // Cell kosong untuk memisahkan total dengan tabel
        for (int i = 0; i < 2; i++) {
            TextView emptyCell = new TextView(this);
            emptyCell.setText(""); // Cell kosong
            totalRow.addView(emptyCell);
        }

        TextView totalLabel = new TextView(this);
        totalLabel.setText("Total :");
        totalLabel.setGravity(Gravity.END);
        totalLabel.setPadding(8, 8, 8, 8);
        totalLabel.setTypeface(Typeface.DEFAULT_BOLD);
        totalRow.addView(totalLabel);

        // Cell untuk Total Pcs
        TextView totalValue = new TextView(this);
        totalValue.setText(String.valueOf(totalPcs));
        totalValue.setGravity(Gravity.CENTER);
        totalValue.setPadding(8, 8, 8, 8);
        totalValue.setTypeface(Typeface.DEFAULT_BOLD);
        totalRow.addView(totalValue);

        // Tambahkan totalRow ke TableLayout
        tableLayout.addView(totalRow);

        // Tambahkan Baris untuk Total M3
        TableRow m3Row = new TableRow(this);
        m3Row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        m3Row.setBackgroundColor(Color.WHITE);

        // Cell kosong untuk memisahkan m3 dengan tabel
        for (int i = 0; i < 2; i++) {
            TextView emptyCell = new TextView(this);
            emptyCell.setText(""); // Cell kosong
            m3Row.addView(emptyCell);
        }

        TextView m3Label = new TextView(this);
        m3Label.setText("M3 :");
        m3Label.setGravity(Gravity.END);
        m3Label.setPadding(8, 8, 8, 8);
        m3Label.setTypeface(Typeface.DEFAULT_BOLD);
        m3Row.addView(m3Label);

        // Cell untuk Total M3
        DecimalFormat df = new DecimalFormat("0.0000");
        TextView m3Value = new TextView(this);
        m3Value.setText(df.format(totalM3));
        m3Value.setGravity(Gravity.CENTER);
        m3Value.setPadding(8, 8, 8, 8);
        m3Value.setTypeface(Typeface.DEFAULT_BOLD);
        m3Row.addView(m3Value);

        // Tambahkan m3Row ke TableLayout
        tableLayout.addView(m3Row);

        //TOOLTIP VIEW PRECONDITION
        if (tableH.equals("ST_h")) {
            tooltipView.findViewById(R.id.fieldNoSPKAsal).setVisibility(View.GONE);
            tooltipView.findViewById(R.id.fieldGrade).setVisibility(View.GONE);

            // Tambahkan Baris untuk Total Ton
            TableRow tonRow = new TableRow(this);
            tonRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            tonRow.setBackgroundColor(Color.WHITE);

            // Cell kosong untuk memisahkan m3 dengan tabel
            for (int i = 0; i < 2; i++) {
                TextView emptyCell = new TextView(this);
                emptyCell.setText(""); // Cell kosong
                tonRow.addView(emptyCell);
            }

            TextView tonLabel = new TextView(this);
            tonLabel.setText("Ton :");
            tonLabel.setGravity(Gravity.END);
            tonLabel.setPadding(8, 8, 8, 8);
            tonLabel.setTypeface(Typeface.DEFAULT_BOLD);
            tonRow.addView(tonLabel);

            // Cell untuk Total Ton
            TextView tonValue = new TextView(this);
            tonValue.setText(df.format(totalTon));
            tonValue.setGravity(Gravity.CENTER);
            tonValue.setPadding(8, 8, 8, 8);
            tonValue.setTypeface(Typeface.DEFAULT_BOLD);
            tonRow.addView(tonValue);

            // Tambahkan m3Row ke TableLayout
            tableLayout.addView(tonRow);
        } else {
            tooltipView.findViewById(R.id.tvNoKBSuket).setVisibility(View.GONE);
            tooltipView.findViewById(R.id.fieldPlatTruk).setVisibility(View.GONE);
        }

        // Buat PopupWindow
        PopupWindow popupWindow = new PopupWindow(
                tooltipView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // Ukur ukuran tooltip sebelum menampilkannya
        tooltipView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int tooltipWidth = tooltipView.getMeasuredWidth();
        int tooltipHeight = tooltipView.getMeasuredHeight();

        // Dapatkan posisi anchorView
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);

        // Hitung posisi tooltip
        int x = isLeft ? location[0] + anchorView.getWidth() : location[0] - tooltipWidth;

        // Setelah itu, gunakan nilai x untuk menampilkan tooltip atau menempatkan elemen di posisi yang diinginkan
        int y = location[1] + (anchorView.getHeight() / 2) - (tooltipHeight / 2);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;
        ImageView trianglePointer = tooltipView.findViewById(R.id.trianglePointer);

        // Menaikkan pointer ketika popup melebihi batas layout
        Log.d("TooltipDebug", "TrianglePointer Y: " + y);
        Log.d("TooltipDebug", "TrianglePointer tooltip : " + (screenHeight - tooltipHeight) );

        if (y < 60) {
            trianglePointer.setY(y - 60);
        }
        else if(y > (screenHeight - tooltipHeight)){
            trianglePointer.setY(y - (screenHeight - tooltipHeight));
        }



        // Tampilkan tooltip
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);
    }


    private void openFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Layout untuk dialog filter
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter_so, null);
        builder.setView(dialogView);

        // Buat AlertDialog dari builder
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_background);

        // Setup spinners di dalam dialog
        setupSpinnersInDialog(dialogView);

        CheckBox filterST = dialogView.findViewById(R.id.filter_ST);
        CheckBox filterS4S = dialogView.findViewById(R.id.filter_S4S);
        CheckBox filterFJ = dialogView.findViewById(R.id.filter_FJ);
        CheckBox filterMoulding = dialogView.findViewById(R.id.filter_Moulding);
        CheckBox filterLaminating = dialogView.findViewById(R.id.filter_Laminating);
        CheckBox filterCCAkhir = dialogView.findViewById(R.id.filter_ccakhir);
        CheckBox filterSanding = dialogView.findViewById(R.id.filter_sanding);
        CheckBox filterBJ = dialogView.findViewById(R.id.filter_bj);

        // Setel checkbox berdasarkan nilai dalam selectedLabels
        filterST.setChecked(selectedLabels.contains("ST"));
        filterS4S.setChecked(selectedLabels.contains("S4S"));
        filterFJ.setChecked(selectedLabels.contains("FJ"));
        filterMoulding.setChecked(selectedLabels.contains("MLD"));
        filterLaminating.setChecked(selectedLabels.contains("LMT"));
        filterCCAkhir.setChecked(selectedLabels.contains("CC"));
        filterSanding.setChecked(selectedLabels.contains("SND"));
        filterBJ.setChecked(selectedLabels.contains("BJ"));

        // Ambil nilai yang dipilih dari Spinner
        Spinner blokSpinner = dialogView.findViewById(R.id.blok);
        Spinner idLokasiSpinner = dialogView.findViewById(R.id.idLokasi);
        Spinner userSpinner = dialogView.findViewById(R.id.userIDSO);

        // Cek jika semua checkbox tercentang, jika ya, sembunyikan status ceklis
        boolean allChecked = selectedLabels.contains("ST") && selectedLabels.contains("S4S") &&
                selectedLabels.contains("FJ") && selectedLabels.contains("MLD") &&
                selectedLabels.contains("LMT") && selectedLabels.contains("CC") &&
                selectedLabels.contains("SND") && selectedLabels.contains("BJ");

        // Jika semua sudah dicentang, tidak perlu tampilkan tanda centang di checkbox
        if (allChecked) {
            filterST.setChecked(false);
            filterS4S.setChecked(false);
            filterFJ.setChecked(false);
            filterMoulding.setChecked(false);
            filterLaminating.setChecked(false);
            filterCCAkhir.setChecked(false);
            filterSanding.setChecked(false);
            filterBJ.setChecked(false);
        }

        // Tombol Apply
        Button btnApply = dialogView.findViewById(R.id.btn_apply);
        btnApply.setOnClickListener(v -> {

            //Tutup Notifikasi NewData
            LinearLayout notificationLayout = findViewById(R.id.notificationLayout);
            notificationLayout.setVisibility(View.GONE);
            countNewDataNotification = 1;

            // Reset paging
            hasMoreDataToFetchBefore = true;
            hasMoreDataToFetchAfter = true;
            currentPageForNoSO = 0;
            currentPageForNoSOInput = 0;
            selectedLabels.clear();

            selectedBlok = (String) blokSpinner.getSelectedItem();
            selectedIdLokasi = (String) idLokasiSpinner.getSelectedItem();
            // Ambil UserIDSO yang dipilih
            UserIDSO selectedUser = (UserIDSO) userSpinner.getSelectedItem();
            selectedUserID = selectedUser.getUserIDSO(); // Mengambil nilai String dari objek UserIDSO

            // Setelah pengguna mengklik checkbox, ambil nilai yang dipilih
            if (filterST.isChecked() && !selectedLabels.contains("ST")) {
                selectedLabels.add("ST");
            }
            if (filterS4S.isChecked() && !selectedLabels.contains("S4S")) {
                selectedLabels.add("S4S");
            }
            if (filterFJ.isChecked() && !selectedLabels.contains("FJ")) {
                selectedLabels.add("FJ");
            }
            if (filterMoulding.isChecked() && !selectedLabels.contains("MLD")) {
                selectedLabels.add("MLD");
            }
            if (filterLaminating.isChecked() && !selectedLabels.contains("LMT")) {
                selectedLabels.add("LMT");
            }
            if (filterCCAkhir.isChecked() && !selectedLabels.contains("CC")) {
                selectedLabels.add("CC");
            }
            if (filterSanding.isChecked() && !selectedLabels.contains("SND")) {
                selectedLabels.add("SND");
            }
            if (filterBJ.isChecked() && !selectedLabels.contains("BJ")) {
                selectedLabels.add("BJ");
            }


            // Cek apakah ada filter yang dipilih
            if (selectedLabels.isEmpty()) {
                selectedLabels.add("ST");
                selectedLabels.add("S4S");
                selectedLabels.add("FJ");
                selectedLabels.add("MLD");
                selectedLabels.add("LMT");
                selectedLabels.add("CC");
                selectedLabels.add("SND");
                selectedLabels.add("BJ");
            }

            // Tampilkan nilai yang dipilih dari Spinner dan CheckBox dalam Toast
            Toast.makeText(StockOpname.this, selectedBlok + " " + selectedIdLokasi + " " + selectedLabels + " " + selectedUserID, Toast.LENGTH_SHORT).show();

            // Lakukan aksi dengan data yang dipilih (filter data)
            filterDataInputByNoSO(selectedIdLokasi, selectedLabels, selectedUserID, currentPageForNoSOInput);
            fetchDataByNoSO(selectedIdLokasi, selectedLabels, currentPageForNoSO);

            // Menutup dialog setelah tombol Apply ditekan
            dialog.dismiss();
        });


        // Tombol Cancel
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {

            // Menyetel checkbox ke false (tidak tercentang)
            filterST.setChecked(false);
            filterS4S.setChecked(false);
            filterFJ.setChecked(false);
            filterMoulding.setChecked(false);
            filterLaminating.setChecked(false);
            filterCCAkhir.setChecked(false);
            filterSanding.setChecked(false);
            filterBJ.setChecked(false);


            selectedBlok = "Semua";
            blokSpinner.setSelection(0);

            selectedIdLokasi = "Semua";
            idLokasiSpinner.setSelection(0);

            selectedUserID = "Semua";
            userSpinner.setSelection(0);

            // Kosongkan selectedLabels
            selectedLabels.clear();
            // Menutup dialog tanpa aksi
//            dialog.dismiss();
        });

        // Tampilkan dialog
        dialog.show();
    }

    private void setupNoSOSpinner() {
        // Jalankan operasi pengambilan data di background thread menggunakan ExecutorService
        executorService.execute(() -> {
            List<StockOpnameData> noSOList = StockOpnameApi.getStockOpnameData();

            // Update UI di thread utama setelah data diambil
            runOnUiThread(() -> {
                if (noSOList != null && !noSOList.isEmpty()) {
                    // Pastikan spinner ditemukan
                    if (spinnerNoSO == null) {
                        Log.e("SetupUserSpinner", "Spinner tidak ditemukan!");
                        return;
                    }

                    // Membuat adapter dan menghubungkannya ke spinner
                    ArrayAdapter<StockOpnameData> adapter = new ArrayAdapter<>(this, R.layout.spinner_display_item, noSOList);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinnerNoSO.setAdapter(adapter);

                    // Set listener untuk item click
                    spinnerNoSO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // Ambil item yang dipilih
                            StockOpnameData selectedItem = (StockOpnameData) parent.getItemAtPosition(position);

                            // Jalankan logika yang sama seperti di setListeners()
                            onSpinnerItemSelected(selectedItem);

                            //Tutup Notifikasi NewData
                            LinearLayout notificationLayout = findViewById(R.id.notificationLayout);
                            notificationLayout.setVisibility(View.GONE);
                            countNewDataNotification = 1;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Handle ketika tidak ada item yang dipilih
                        }
                    });
                } else {
                    Toast.makeText(StockOpname.this, "Tidak ada Nomor Stock Opname yang aktif", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Method yang akan dijalankan ketika item dipilih
    private void onSpinnerItemSelected(StockOpnameData selectedItem) {
        // Set nilai yang dipilih
        selectedNoSO = selectedItem.getNoSO();
        selectedTglSO = selectedItem.getTgl();

        // Set nilai default untuk filter lainnya
        selectedBlok = "Semua";
        selectedIdLokasi = "Semua";
        selectedUserID = "Semua";

        // Set label yang dipilih
        selectedLabels.clear();
        selectedLabels.add("ST");
        selectedLabels.add("S4S");
        selectedLabels.add("FJ");
        selectedLabels.add("MLD");
        selectedLabels.add("LMT");
        selectedLabels.add("CC");
        selectedLabels.add("SND");
        selectedLabels.add("BJ");

        // Reset paging
        hasMoreDataToFetchBefore = true;
        hasMoreDataToFetchAfter = true;
        currentPageForNoSO = 0;
        currentPageForNoSOInput = 0;

        // Mulai fetch dari awal
        fetchDataByNoSO(selectedIdLokasi, selectedLabels, currentPageForNoSO);
        filterDataInputByNoSO(selectedIdLokasi, selectedLabels, selectedUserID, currentPageForNoSOInput);
    }


    private void setupSpinnersInDialog(View dialogView) {
        executorService.execute(() -> {
            // Ambil data lokasi dan blok dari API di background thread
            List<LokasiBlok> lokasiBlokList = StockOpnameApi.getLokasiAndBlok();

            // Ambil data UserIDSO
            List<UserIDSO> userIDSOList = StockOpnameApi.getUserIdsForNoSO(selectedNoSO);

            // Update UI di main thread setelah data diambil
            runOnUiThread(() -> {
                if (lokasiBlokList != null && !lokasiBlokList.isEmpty() && userIDSOList != null) {
                    // Pisahkan data blok dan idLokasi
                    Set<String> blokSet = new HashSet<>(); // Menggunakan Set untuk menghilangkan duplikasi
                    List<LokasiBlok> idLokasiList = new ArrayList<>();

                    // Mengisi blokSet dengan data unik blok
                    for (LokasiBlok lokasiBlok : lokasiBlokList) {
                        blokSet.add(lokasiBlok.getBlok());  // Menambahkan blok ke Set (menghilangkan duplikasi)
                        idLokasiList.add(lokasiBlok); // Menambahkan objek LokasiBlok ke list
                    }

                    // Mengonversi Set menjadi List untuk spinner
                    List<String> blokList = new ArrayList<>(blokSet);

                    // Tambahkan pilihan "Semua" di awal blokList
                    blokList.add(0, "Semua"); // "Semua" ada di index pertama

                    // Setup adapter untuk spinner blok
                    Spinner blokSpinner = dialogView.findViewById(R.id.blok);
                    ArrayAdapter<String> blokAdapter = new ArrayAdapter<>(StockOpname.this, android.R.layout.simple_spinner_item, blokList);
                    blokAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    blokSpinner.setAdapter(blokAdapter);

                    // Setup adapter untuk spinner idLokasi (kosongkan dulu)
                    Spinner idLokasiSpinner = dialogView.findViewById(R.id.idLokasi);
                    ArrayAdapter<String> idLokasiAdapter = new ArrayAdapter<>(StockOpname.this, android.R.layout.simple_spinner_item, new ArrayList<>());
                    idLokasiAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    idLokasiSpinner.setAdapter(idLokasiAdapter);

                    // Setup adapter untuk spinner UserIDSO
                    userIDSOList.add(0, new UserIDSO("Semua")); // Menambahkan "Semua" ke dalam daftar
                    Spinner userSpinner = dialogView.findViewById(R.id.userIDSO);
                    ArrayAdapter<UserIDSO> userAdapter = new ArrayAdapter<>(StockOpname.this, android.R.layout.simple_spinner_item, userIDSOList);
                    userAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    userSpinner.setAdapter(userAdapter);

                    // Mengatur nilai spinner userIDSO yang dipilih sebelumnya
                    if (selectedUserID != null) {
                        int userIDIndex = -1;
                        for (int i = 0; i < userIDSOList.size(); i++) {
                            if (userIDSOList.get(i).getUserIDSO().equals(selectedUserID)) {
                                userIDIndex = i;
                                break;
                            }
                        }
                        if (userIDIndex != -1) {
                            userSpinner.setSelection(userIDIndex); // Set spinner ke nilai yang dipilih sebelumnya
                        }
                    }


                    // Set nilai yang dipilih sebelumnya (selectedBlok dan selectedIdLokasi)
                    if (selectedBlok == null || !blokList.contains(selectedBlok)) {
                        selectedBlok = "Semua"; // Reset to "Semua" if no valid selection
                    }

                    // Setel nilai yang dipilih sebelumnya
                    int blokIndex = blokList.indexOf(selectedBlok);
                    blokSpinner.setSelection(blokIndex);  // Set spinner blok ke nilai yang dipilih sebelumnya

                    // Set adapter untuk spinner idLokasi
                    if (selectedBlok != null && !"Semua".equals(selectedBlok)) {
                        // Filter lokasi berdasarkan blok yang dipilih
                        List<String> filteredLokasiList = new ArrayList<>();
                        for (LokasiBlok lokasiBlok : lokasiBlokList) {
                            if (lokasiBlok.getBlok().equals(selectedBlok)) {
                                filteredLokasiList.add(lokasiBlok.getIdLokasi());
                            }
                        }

                        // Set adapter dan pilih idLokasi yang sesuai
                        idLokasiAdapter = new ArrayAdapter<>(StockOpname.this, android.R.layout.simple_spinner_item, filteredLokasiList);
                        idLokasiAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        idLokasiSpinner.setAdapter(idLokasiAdapter);

                        if (filteredLokasiList.contains(selectedIdLokasi)) {
                            int lokasiIndex = filteredLokasiList.indexOf(selectedIdLokasi);
                            idLokasiSpinner.setSelection(lokasiIndex); // Set spinner idLokasi ke nilai yang dipilih sebelumnya
                        }
                    } else {
                        // Jika "Semua" dipilih untuk blok, set idLokasi juga menjadi "Semua"
                        List<String> allLokasiList = new ArrayList<>();
                        allLokasiList.add("Semua"); // Menambahkan "Semua" ke daftar idLokasi
                        idLokasiAdapter = new ArrayAdapter<>(StockOpname.this, android.R.layout.simple_spinner_item, allLokasiList);
                        idLokasiAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        idLokasiSpinner.setAdapter(idLokasiAdapter);
                        selectedIdLokasi = "Semua"; // Set selectedIdLokasi ke "Semua"
                    }

                    // Tambahkan listener untuk blokSpinner
                    blokSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            // Ambil blok yang dipilih
                            selectedBlok = blokList.get(position);

                            // Jika "Semua" dipilih, set idLokasi juga menjadi "Semua"
                            if ("Semua".equals(selectedBlok)) {
                                List<String> allLokasiList = new ArrayList<>();
                                allLokasiList.add("Semua"); // Menambahkan "Semua" ke daftar idLokasi

                                // Update spinner idLokasi dengan "Semua"
                                ArrayAdapter<String> idLokasiAdapter = new ArrayAdapter<>(StockOpname.this, android.R.layout.simple_spinner_item, allLokasiList);
                                idLokasiAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                idLokasiSpinner.setAdapter(idLokasiAdapter);

                                selectedIdLokasi = "Semua"; // Set selectedIdLokasi ke "Semua"
                            } else {
                                // Filter lokasi berdasarkan blok yang dipilih
                                List<String> filteredLokasiList = new ArrayList<>();
                                filteredLokasiList.add("Semua"); // Tambahkan "Semua" di awal daftar

                                for (LokasiBlok lokasiBlok : lokasiBlokList) {
                                    if (lokasiBlok.getBlok().equals(selectedBlok)) {
                                        filteredLokasiList.add(lokasiBlok.getIdLokasi());
                                    }
                                }

                                // Update spinner idLokasi dengan pilihan yang sudah difilter
                                ArrayAdapter<String> idLokasiAdapter = new ArrayAdapter<>(StockOpname.this, android.R.layout.simple_spinner_item, filteredLokasiList);
                                idLokasiAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                idLokasiSpinner.setAdapter(idLokasiAdapter);

                                // If the selectedIdLokasi exists in filtered list, set it
                                if (filteredLokasiList.contains(selectedIdLokasi)) {
                                    int lokasiIndex = filteredLokasiList.indexOf(selectedIdLokasi);
                                    idLokasiSpinner.setSelection(lokasiIndex);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // Kosongkan spinner idLokasi jika tidak ada pilihan blok
                            ArrayAdapter<String> idLokasiAdapter = new ArrayAdapter<>(StockOpname.this, android.R.layout.simple_spinner_item, new ArrayList<>());
                            idLokasiAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            idLokasiSpinner.setAdapter(idLokasiAdapter);
                        }
                    });
                } else {
                    Toast.makeText(StockOpname.this, "Tidak ada data lokasi dan blok atau UserIDSO", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }







    private void initializeViews() {
        loadingIndicator = findViewById(R.id.loadingIndicator);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewBefore = findViewById(R.id.recyclerViewBefore);
        loadingIndicatorBefore = findViewById(R.id.loadingIndicatorBefore);
        recyclerViewAfter = findViewById(R.id.recyclerViewAfter);
        loadingIndicatorAfter = findViewById(R.id.loadingIndicatorAfter);
        searchView = findViewById(R.id.searchData);  // Using the SearchView from XML
        filterButton = findViewById(R.id.filterButton);
        blokSpinner = findViewById(R.id.blok);
        idLokasiSpinner = findViewById(R.id.idLokasi);
        spinnerNoSO = findViewById(R.id.spinnerNoSO);



    }

    private void initializeRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StockOpnameAdapter(stockOpnames);
        recyclerView.setAdapter(adapter);

        recyclerViewBefore.setLayoutManager(new LinearLayoutManager(this));
        stockOpnameDataAdapter = new StockOpnameDataAdapter(stockOpnameDataByNoSOList);
        recyclerViewBefore.setAdapter(stockOpnameDataAdapter);

        recyclerViewAfter.setLayoutManager(new LinearLayoutManager(this));
        stockOpnameDataInputAdapter = new StockOpnameDataInputAdapter(stockOpnameDataInputByNoSOList, this); // Menghubungkan activity dengan adapter
        recyclerViewAfter.setAdapter(stockOpnameDataInputAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDataInputByNoSO(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchText = newText; // Simpan teks pencarian dalam variabel global

                if (newText.isEmpty()) {
                    filterDataInputByNoSO(selectedIdLokasi, selectedLabels , selectedUserID,0);
                    fetchDataByNoSO(selectedIdLokasi, selectedLabels, 0);

                } else {
                    // Jika ada teks, hentikan sementara WebSocket
                    Log.d("SearchView", "Teks tidak kosong, memanggil searchDataInputByNoSO");
                    searchDataInputByNoSO(newText);
                    searchDataByNoSO(newText);
                }
                return true;
            }
        });

        filterButton.setOnClickListener(v -> {
            // Membuka dialog untuk memilih filter
            openFilterDialog();
        });
    }


    private void setListeners() {
        adapter.setOnItemClickListener(position -> {
            StockOpnameData stockOpname = stockOpnames.get(position);
            selectedNoSO = stockOpname.getNoSO();
            selectedTglSO = stockOpname.getTgl();

            selectedBlok = "Semua";
            selectedIdLokasi = "Semua";
            selectedUserID = "Semua";

            selectedLabels.add("ST");
            selectedLabels.add("S4S");
            selectedLabels.add("FJ");
            selectedLabels.add("MLD");
            selectedLabels.add("LMT");
            selectedLabels.add("CC");
            selectedLabels.add("SND");
            selectedLabels.add("BJ");



            // Reset paging
            hasMoreDataToFetchBefore = true;
            hasMoreDataToFetchAfter = true;
            currentPageForNoSO = 0;
            currentPageForNoSOInput = 0;

            // Mulai fetch dari awal
            fetchDataByNoSO(selectedIdLokasi, selectedLabels, currentPageForNoSO);
            filterDataInputByNoSO(selectedIdLokasi, selectedLabels , selectedUserID, currentPageForNoSOInput);

        });

        stockOpnameDataAdapter.setOnItemClickListener(new StockOpnameDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Ambil data item yang diklik
                StockOpnameDataByNoSO clickedItem = stockOpnameDataByNoSOList.get(position);

                // Gunakan post() untuk memastikan eksekusi setelah RecyclerView selesai memperbarui tampilan
                recyclerViewBefore.post(() -> {
                    RecyclerView.ViewHolder holder = recyclerViewBefore.findViewHolderForAdapterPosition(position);
                    if (holder != null) {
                        // Dapatkan view item dari ViewHolder
                        View itemView = holder.itemView;

                        if (clickedItem.getNoLabel() != null && clickedItem.getNoLabel().startsWith("E")) {
                            // Panggil fungsi fetchDataAndShowTooltip dengan itemView
                            fetchDataAndShowTooltip(itemView, clickedItem.getNoLabel(), "ST_h", "ST_d", "NoST", true);
                        } else if (clickedItem.getNoLabel() != null && clickedItem.getNoLabel().startsWith("R")) {
                            fetchDataAndShowTooltip(itemView, clickedItem.getNoLabel(), "S4S_h", "S4S_d", "NoS4S", true);
                        } else if (clickedItem.getNoLabel() != null && clickedItem.getNoLabel().startsWith("S")) {
                            fetchDataAndShowTooltip(itemView, clickedItem.getNoLabel(), "FJ_h", "FJ_d", "NoFJ", true);
                        } else if (clickedItem.getNoLabel() != null && clickedItem.getNoLabel().startsWith("T")) {
                            fetchDataAndShowTooltip(itemView, clickedItem.getNoLabel(), "Moulding_h", "Moulding_d", "NoMoulding", true);
                        } else if (clickedItem.getNoLabel() != null && clickedItem.getNoLabel().startsWith("U")) {
                            fetchDataAndShowTooltip(itemView, clickedItem.getNoLabel(), "Laminating_h", "Laminating_d", "NoLaminating", true);
                        } else if (clickedItem.getNoLabel() != null && clickedItem.getNoLabel().startsWith("V")) {
                            fetchDataAndShowTooltip(itemView, clickedItem.getNoLabel(), "CCAkhir_h", "CCAkhir_d", "NoCCAkhir", true);
                        } else if (clickedItem.getNoLabel() != null && clickedItem.getNoLabel().startsWith("W")) {
                            fetchDataAndShowTooltip(itemView, clickedItem.getNoLabel(), "Sanding_h", "Sanding_d", "NoSanding", true);
                        } else if (clickedItem.getNoLabel() != null && clickedItem.getNoLabel().startsWith("I")) {
                            fetchDataAndShowTooltip(itemView, clickedItem.getNoLabel(), "BarangJadi_h", "BarangJadi_d", "NoBJ", true);
                        } else {
                            Toast.makeText(StockOpname.this, "Tidak ada data " + clickedItem.getNoLabel(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Menampilkan toast sebagai feedback
//                Toast.makeText(StockOpname.this, "Item clicked: " + clickedItem.getNoLabel(), Toast.LENGTH_SHORT).show();
            }
        });


        stockOpnameDataInputAdapter.setOnItemClickListener(new StockOpnameDataInputAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Ambil data item yang diklik
                StockOpnameDataInputByNoSO clickedItem = stockOpnameDataInputByNoSOList.get(position);

                // Gunakan post() untuk memastikan eksekusi setelah RecyclerView selesai memperbarui tampilan
                recyclerViewAfter.post(() -> {
                    RecyclerView.ViewHolder holder = recyclerViewAfter.findViewHolderForAdapterPosition(position);
                    if (holder != null) {
                        // Dapatkan view item dari ViewHolder
                        View itemView = holder.itemView;

                        if (clickedItem.getNoLabelInput() != null && clickedItem.getNoLabelInput().startsWith("E")) {
                            // Panggil fungsi fetchDataAndShowTooltip dengan itemView
                            fetchDataAndShowTooltip(itemView, clickedItem.getNoLabelInput(), "ST_h", "ST_d", "NoST", false);
                        } else if (clickedItem.getNoLabelInput() != null && clickedItem.getNoLabelInput().startsWith("R")) {
                            fetchDataAndShowTooltip(itemView, clickedItem.getNoLabelInput(), "S4S_h", "S4S_d", "NoS4S", false);
                        } else if (clickedItem.getNoLabelInput() != null && clickedItem.getNoLabelInput().startsWith("S")) {
                            fetchDataAndShowTooltip(itemView, clickedItem.getNoLabelInput(), "FJ_h", "FJ_d", "NoFJ", false);
                        } else if (clickedItem.getNoLabelInput() != null && clickedItem.getNoLabelInput().startsWith("T")) {
                            fetchDataAndShowTooltip(itemView, clickedItem.getNoLabelInput(), "Moulding_h", "Moulding_d", "NoMoulding", false);
                        } else if (clickedItem.getNoLabelInput() != null && clickedItem.getNoLabelInput().startsWith("U")) {
                            fetchDataAndShowTooltip(itemView, clickedItem.getNoLabelInput(), "Laminating_h", "Laminating_d", "NoLaminating", false);
                        } else if (clickedItem.getNoLabelInput() != null && clickedItem.getNoLabelInput().startsWith("V")) {
                            fetchDataAndShowTooltip(itemView, clickedItem.getNoLabelInput(), "CCAkhir_h", "CCAkhir_d", "NoCCAkhir", false);
                        } else if (clickedItem.getNoLabelInput() != null && clickedItem.getNoLabelInput().startsWith("W")) {
                            fetchDataAndShowTooltip(itemView, clickedItem.getNoLabelInput(), "Sanding_h", "Sanding_d", "NoSanding", false);
                        } else if (clickedItem.getNoLabelInput() != null && clickedItem.getNoLabelInput().startsWith("I")) {
                            fetchDataAndShowTooltip(itemView, clickedItem.getNoLabelInput(), "BarangJadi_h", "BarangJadi_d", "NoBJ", false);
                        } else {
                            Toast.makeText(StockOpname.this, "Tidak ada data " + clickedItem.getNoLabelInput(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Menampilkan toast sebagai feedback
//                Toast.makeText(StockOpname.this, "Item clicked: " + clickedItem.getNoLabel(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.addOnScrollListener(createScrollListener(true));
        recyclerViewBefore.addOnScrollListener(createScrollListener(false));
        recyclerViewAfter.addOnScrollListener(createScrollListener(false));
    }


    private RecyclerView.OnScrollListener createScrollListener(boolean isMainRecyclerView) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isMainRecyclerView) {
                    if (!isLoading && isLastItemVisible(recyclerView)) {
                        loadMoreData();
                    }
                } else {
                    if (!isLoadingBefore && isLastItemVisible(recyclerViewBefore)) {
                        fetchDataByNoSO(selectedIdLokasi, selectedLabels, currentPageForNoSO);
                        Log.d("valuefilter", "scroll with offset: " + currentPageForNoSO + " and limit: " + selectedLabel);

                    }
                    if (!isLoadingAfter && isLastItemVisible(recyclerViewAfter)) {
                        filterDataInputByNoSO(selectedIdLokasi, selectedLabels , selectedUserID, currentPageForNoSOInput);
//                        Log.d("valuefilter", "scroll with offset: " + selectedIdLokasi + " and limit: " + selectedLabel);

                    }
                }
            }
        };
    }

    private void filterDataInputByNoSO(String selectedIdLokasi, Set<String> selectedLabels, String selectedUserID, int page) {

        // Show loading indicator while searching
        isLoadingAfter = true;
        showLoadingIndicatorAfter(false);

        int limit = 100;
        int offset = page * limit;

        // Pastikan parameter tidak null, beri nilai default jika perlu
        String safeSelectedNoSO = (selectedNoSO == null) ? "" : selectedNoSO;
        String safeSelectedBlok = (selectedBlok == null) ? "" : selectedBlok;
        String safeSelectedIdLokasi = (selectedIdLokasi == null) ? "" : selectedIdLokasi;
        Set<String> safeSelectedLabels = (selectedLabels == null) ? new HashSet<>() : selectedLabels;
        String safeSelectedUserID = (selectedUserID == null) ? "" : selectedUserID;

        // Run the network call in a background thread
        executorService.execute(() -> {
            // Fetching the data with the filter applied
            List<StockOpnameDataInputByNoSO> data = StockOpnameApi.getStockOpnameDataInputByFilter(
                    safeSelectedNoSO, safeSelectedBlok, safeSelectedIdLokasi, safeSelectedLabels, safeSelectedUserID, offset, limit);

            // Now update the UI on the main thread after the data is fetched
            runOnUiThread(() -> {

                updateLabelCount( safeSelectedNoSO, selectedTglSO, safeSelectedBlok, safeSelectedIdLokasi, safeSelectedLabels, safeSelectedUserID);

                if (data != null && !data.isEmpty()) {
                    // Iterate over the fetched data and add unique items to the list
                    if (page == 0) stockOpnameDataInputByNoSOList.clear();
                    for (StockOpnameDataInputByNoSO item : data) {
                        if (!stockOpnameDataInputByNoSOList.contains(item)) {
                            stockOpnameDataInputByNoSOList.add(item);  // Add the item if it's not already in the list
                        }
                    }
                    currentPageForNoSOInput++;

                    // Check if there are no more data to fetch
                    if (data.size() < limit) {
                        hasMoreDataToFetchAfter = false;  // No more data to fetch
                    }

                } else {
                    hasMoreDataToFetchAfter = false;
                    stockOpnameDataInputByNoSOList.clear();  // Clear the list if no data is found
                }

                // Update UI with total count of data

                // Notify the adapter that data has been updated
                stockOpnameDataInputAdapter.notifyDataSetChanged();

                // Hide the loading indicator
                showLoadingIndicatorAfter(false);
                isLoadingAfter = false;
            });
        });
    }

    private void updateLabelCount(String safeSelectedNoSO, String safeSelectedTglSO, String safeSelectedBlok, String safeSelectedIdLokasi, Set<String> safeSelectedLabels, String safeSelectedUserID) {

        executorService.execute(() -> {
            int totalCountStock = StockOpnameApi.getTotalDataStockCount(safeSelectedNoSO, selectedTglSO);
            int totalCountBefore = StockOpnameApi.getTotalStockOpnameDataCount(safeSelectedNoSO, selectedTglSO, safeSelectedBlok, safeSelectedIdLokasi, safeSelectedLabels);
            int totalCountAfter = StockOpnameApi.getStockOpnameDataInputCountByFilter(safeSelectedNoSO, safeSelectedBlok, safeSelectedIdLokasi, safeSelectedLabels, safeSelectedUserID);

            runOnUiThread(() -> {
                TextView totalCountDataStock = findViewById(R.id.countDataStock);
                TextView totalCountLabelBefore = findViewById(R.id.countLabelBefore);
                TextView totalCountLabelAfter = findViewById(R.id.countLabelAfter);
                if (totalCountDataStock != null && totalCountLabelBefore != null && totalCountLabelAfter != null) {
                    totalCountDataStock.setText("Total Label : " + totalCountStock); // Menampilkan total count
                    totalCountLabelBefore.setText("Sisa Label : " + totalCountBefore); // Menampilkan total count
                    totalCountLabelAfter.setText("Total Scan : " + totalCountAfter); // Menampilkan total count
                }
            });
        });
    }


    private void searchDataByNoSO(String searchTerm) {
        // Show loading indicator while searching
        showLoadingIndicatorBefore(true);
        isLoadingBefore = true;

        // Run the network call in a background thread
        executorService.execute(() -> {
            // Perform the network or database call in the background
            List<StockOpnameDataByNoSO> data = StockOpnameApi.searchStockOpnameDataByNoSO(selectedNoSO, selectedTglSO, searchTerm, selectedBlok, selectedIdLokasi, selectedLabels,0, 25);

            // Now update the UI on the main thread after the data is fetched
            runOnUiThread(() -> {
                if (data != null && !data.isEmpty()) {
                    // Iterate over the fetched data and add unique items to the list
                    stockOpnameDataByNoSOList.clear();
                    for (StockOpnameDataByNoSO item : data) {
                        if (!stockOpnameDataByNoSOList.contains(item)) {
                            stockOpnameDataByNoSOList.add(item);  // Add the item if it's not already in the list
                        }
                    }
                    currentPageForNoSO++;

                } else {
                    hasMoreDataToFetchBefore = false;
                    stockOpnameDataByNoSOList.clear();  // Clear the list if no data is found
                    // You can display a toast message or any other indicator if no data is found.
                    // showToast("Tidak ada data lagi untuk NoSO: " + noSO);
                }

                // Notify the adapter that data has been updated
                stockOpnameDataAdapter.notifyDataSetChanged();

                // Hide the loading indicator
                showLoadingIndicatorBefore(false);
                isLoadingBefore = false;
            });
        });
    }

    private void searchDataInputByNoSO(String searchTerm) {
        // Show loading indicator while searching
        showLoadingIndicatorAfter(false);
        isLoadingAfter = true;

        // Run the network call in a background thread
        executorService.execute(() -> {
            // Perform the network or database call in the background
            List<StockOpnameDataInputByNoSO> data = StockOpnameApi.getStockOpnameDataInputBySearch(selectedNoSO, searchTerm, selectedBlok, selectedIdLokasi, selectedLabels,0, 25);

            // Now update the UI on the main thread after the data is fetched
            runOnUiThread(() -> {
                if (data != null && !data.isEmpty()) {
                    // Iterate over the fetched data and add unique items to the list
                    stockOpnameDataInputByNoSOList.clear();
                    for (StockOpnameDataInputByNoSO item : data) {
                        if (!stockOpnameDataInputByNoSOList.contains(item)) {
                            stockOpnameDataInputByNoSOList.add(item);  // Add the item if it's not already in the list
                        }
                    }
                    currentPageForNoSOInput++;

                } else {
                    hasMoreDataToFetchAfter = false;
                    stockOpnameDataInputByNoSOList.clear();  // Clear the list if no data is found
                    // You can display a toast message or any other indicator if no data is found.
                    // showToast("Tidak ada data lagi untuk NoSO: " + noSO);
                }

                // Notify the adapter that data has been updated
                stockOpnameDataInputAdapter.notifyDataSetChanged();

                // Hide the loading indicator
                showLoadingIndicatorAfter(false);
                isLoadingAfter = false;
            });
        });
    }

    private void fetchDataByNoSO(String selectedIdLokasi, Set<String> selectedLabels, int page) {

        isLoadingBefore = true;
        showLoadingIndicatorBefore(true);
        int limit = 100;
        int offset = page * limit;

        String safeSelectedNoSO = (selectedNoSO == null) ? "" : selectedNoSO;
        String safeSelectedTglSO = (selectedTglSO == null) ? "" : selectedTglSO;
        String safeSelectedIdLokasi = (selectedIdLokasi == null) ? "" : selectedIdLokasi;
        Set<String> safeSelectedLabels = (selectedLabels == null) ? new HashSet<>() : selectedLabels;

        executorService.execute(() -> {
            List<StockOpnameDataByNoSO> data = StockOpnameApi.getStockOpnameDataByNoSO(safeSelectedNoSO, safeSelectedTglSO, selectedBlok, safeSelectedIdLokasi, safeSelectedLabels, offset, limit);
            runOnUiThread(() -> {

                updateLabelCount( safeSelectedNoSO, selectedTglSO, selectedBlok, safeSelectedIdLokasi, safeSelectedLabels, selectedUserID);

                if (data != null && !data.isEmpty()) {
                    findViewById(R.id.dataLabelEmpty).setVisibility(View.GONE);
                    if (page == 0) stockOpnameDataByNoSOList.clear();
                    for (StockOpnameDataByNoSO item : data) {
                        if (!stockOpnameDataByNoSOList.contains(item)) {
                            stockOpnameDataByNoSOList.add(item);

                        }
                    }
                    currentPageForNoSO++;

                    if (data.size() < limit) {
                        hasMoreDataToFetchBefore = false;  // No more data to fetch
                    }

                } else {
                    hasMoreDataToFetchBefore = false;
//                    showToast("Tidak ada data lagi untuk NoSO: " + noSO);
                    stockOpnameDataByNoSOList.clear();
                    findViewById(R.id.dataLabelEmpty).setVisibility(View.VISIBLE); // Tampilkan pesan
                }

                stockOpnameDataAdapter.notifyDataSetChanged();
                showLoadingIndicatorBefore(false);
                isLoadingBefore = false;
            });
        });
    }



    private void loadMoreData() {
        isLoading = true;
        showLoadingIndicator(true);

        executorService.execute(() -> {
            List<StockOpnameData> newStockOpnames = StockOpnameApi.getStockOpnameData();
            runOnUiThread(() -> {
                if (newStockOpnames != null && !newStockOpnames.isEmpty()) {
                    stockOpnames.addAll(newStockOpnames);
                    adapter.notifyDataSetChanged();
                    currentPage++;
                } else {
                    showToast("Tidak ada data");
                }
                showLoadingIndicator(false);
                isLoading = false;
            });
        });
    }

    private boolean hasMoreDataToFetchBefore = true;  // Flag untuk RecyclerView "Before"
    private boolean hasMoreDataToFetchAfter = true;   // Flag untuk RecyclerView "After"


    private boolean isLastItemVisible(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

        // Periksa apakah ada data yang lebih untuk di-fetch, tergantung pada recyclerView yang sedang digunakan
        if (recyclerView == recyclerViewBefore) {
            // Mengontrol fetch untuk RecyclerView Before
//            Log.d("ScrollCheck", "recyclerViewBefore last position: " + lastVisibleItemPosition + " total item count: " + totalItemCount);

            return lastVisibleItemPosition == totalItemCount - 1 && !isLoadingBefore && hasMoreDataToFetchBefore;
        } else if (recyclerView == recyclerViewAfter) {
            // Mengontrol fetch untuk RecyclerView After
//            Log.d("ScrollCheck", "recyclerViewAfter last position: " + lastVisibleItemPosition + " total item count: " + totalItemCount);

            return lastVisibleItemPosition == totalItemCount - 1 && !isLoadingAfter && hasMoreDataToFetchAfter;
        }

        return false;
    }

    private boolean isFirstItemVisible(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        // Cek apakah item pertama (index 0) terlihat
        return firstVisibleItemPosition == 0;
    }




    private void showLoadingIndicator(boolean isVisible) {
        loadingIndicator.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void showLoadingIndicatorBefore(boolean isVisible) {
        loadingIndicatorBefore.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void showLoadingIndicatorAfter(boolean isVisible) {
        loadingIndicatorAfter.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void showToast(String message) {
        Toast.makeText(StockOpname.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteConfirmation(StockOpnameDataInputByNoSO item, int position) {
        showDeleteConfirmationDialog(item, position); // Menampilkan dialog konfirmasi penghapusan

    }

    private void showDeleteConfirmationDialog(StockOpnameDataInputByNoSO item, int position) {
        new AlertDialog.Builder(this)
                .setMessage("Apakah Anda yakin ingin menghapus label " + item.getNoLabelInput() + "? " )
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Memanggil method onItemDelete untuk menghapus item
                    onItemDelete(item, position);
                })
                .setNegativeButton("No", null)  // Jika pengguna memilih "No", dialog akan ditutup
                .show();
    }

    public void onItemDelete(StockOpnameDataInputByNoSO item, int position) {
        Log.d("StockOpname", "Deleting item at position: " + position);

        // Jalankan operasi penghapusan di background thread menggunakan ExecutorService
        executorService.execute(() -> {
            // Panggil API untuk menghapus data dari database
            boolean isDeleted = StockOpnameApi.deleteStockOpnameDataInputByNoLabel(item.getNoLabelInput());

            // Setelah operasi selesai, perbarui UI di main thread
            runOnUiThread(() -> {
                if (isDeleted) {
                    // Cari item berdasarkan ID
                    for (int i = 0; i < stockOpnameDataInputByNoSOList.size(); i++) {
                        if (stockOpnameDataInputByNoSOList.get(i).getNoLabelInput().equals(item.getNoLabelInput())) {
                            stockOpnameDataInputByNoSOList.remove(i);
                            stockOpnameDataInputAdapter.notifyItemRemoved(i); // Hapus item di posisi yang benar
                            updateLabelCount(selectedNoSO, selectedTglSO, selectedBlok, selectedIdLokasi, selectedLabels, selectedUserID);

                            if (currentSearchText.isEmpty()) {
                                fetchDataByNoSO(selectedIdLokasi, selectedLabels, 0);
                            } else {
                                searchDataByNoSO(currentSearchText);
                            }


                            Log.d("StockOpname", "Item deleted, new list size: " + stockOpnameDataInputByNoSOList.size());
                            Toast.makeText(this, "Item deleted: " + item.getNoLabelInput(), Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                } else {
                    Log.e("StockOpname", "Failed to delete item: " + item.getNoLabelInput());
                    Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Mulai polling saat Activity aktif
//        handler.post(fetchDataRunnable);
        // Membuat koneksi WebSocket saat Activity aktif
    }

    @Override
    public void onMessageReceived(String noso) {
        Log.d("WebSocket", "Triggering data fetch after receiving message.");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (noso.equals(selectedNoSO)) {

                    LinearLayout notificationLayout = findViewById(R.id.notificationLayout);
                    TextView notificationText = findViewById(R.id.tvNewDataNotification);

                    hasMoreDataToFetchBefore = true;
                    currentPageForNoSO = 0;


                    fetchDataByNoSO(selectedIdLokasi, selectedLabels, currentPageForNoSO);

                    // Periksa apakah item pertama terlihat
                    if (isFirstItemVisible(recyclerViewAfter)) {
                        // Jika item pertama terlihat, langsung jalankan fungsi untuk mengambil data
                        triggerDataFetch();
                        // Sembunyikan notifikasi setelah data di-fetch
                        notificationLayout.setVisibility(View.GONE);
                        countNewDataNotification = 1;
                    } else {
                        // Jika item pertama tidak terlihat, tampilkan notifikasi
                        notificationText.setText("Terdapat " + countNewDataNotification + " label baru!");
                        notificationLayout.setVisibility(View.VISIBLE); // Menampilkan notifikasi

                        // Set OnClickListener untuk tombol notifikasi
                        notificationText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Panggil fungsi untuk mengambil data
                                triggerDataFetch();
                                // Scroll ke index 0 (item pertama)
                                scrollToTop(recyclerViewAfter);
                                // Sembunyikan notifikasi setelah klik
                                notificationLayout.setVisibility(View.GONE);
                                countNewDataNotification = 1;

                            }
                        });
                        countNewDataNotification++;
                    }
                }
            }
        });
    }



    private void scrollToTop(RecyclerView recyclerView) {
        // Ambil LayoutManager dari RecyclerView
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        if (layoutManager != null) {
            // Membuat LinearSmoothScroller untuk animasi halus
            LinearSmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                @Override
                public int getVerticalSnapPreference() {
                    return LinearSmoothScroller.SNAP_TO_START;  // Menempatkan item di posisi awal layar
                }
            };

            // Set target posisi ke 0 (index pertama)
            smoothScroller.setTargetPosition(0);

            // Mulai scroll dengan animasi halus
            layoutManager.startSmoothScroll(smoothScroller);
        }
    }


    private void triggerDataFetch() {
        if (currentSearchText.isEmpty()) {
            // Lakukan fetch data
            hasMoreDataToFetchAfter = true;
            currentPageForNoSOInput = 0;
            filterDataInputByNoSO(selectedIdLokasi, selectedLabels, selectedUserID, currentPageForNoSOInput);
        } else {
            // Lakukan pencarian data
            searchDataInputByNoSO(currentSearchText);
            searchDataByNoSO(currentSearchText);
            updateLabelCount(selectedNoSO, selectedTglSO, selectedBlok, selectedIdLokasi, selectedLabels, selectedUserID);
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();  // Graceful shutdown
        }
        // Pastikan untuk menutup WebSocket saat activity dihentikan
        if (WebSocketConnection.getInstance().isConnected()) {
            WebSocketConnection.getInstance().closeConnection();
        }
    }
}