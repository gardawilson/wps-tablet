package com.example.myapplication;

import static com.example.myapplication.api.StockOpnameApi.getLokasiAndBlok;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.api.StockOpnameApi;
import com.example.myapplication.model.LokasiBlok;
import com.example.myapplication.model.StockOpnameData;
import com.example.myapplication.model.StockOpnameDataByNoSO;
import com.example.myapplication.model.StockOpnameDataInputByNoSO;
import com.example.myapplication.model.UserIDSO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StockOpname extends AppCompatActivity implements StockOpnameDataInputAdapter.OnDeleteConfirmationListener {

    private ProgressBar loadingIndicator;
    private RecyclerView recyclerView;
    private StockOpnameAdapter adapter;
    private ExecutorService executorService;
    private List<StockOpnameData> stockOpnames = new ArrayList<>();
    private boolean isLoading = false;
    private boolean isLoadingBefore = false;
    private boolean isLoadingAfter = false;
    private int currentPage = 0;  // Halaman pertama
    private final int LIMIT = 100;  // Data per halaman

    // Data untuk RecyclerView kedua
    private RecyclerView recyclerViewBefore;
    private RecyclerView recyclerViewAfter;
    private ProgressBar loadingIndicatorBefore;
    private ProgressBar loadingIndicatorAfter;
    private StockOpnameDataAdapter stockOpnameDataAdapter;
    private StockOpnameDataInputAdapter stockOpnameDataInputAdapter;
    private List<StockOpnameDataByNoSO> stockOpnameDataByNoSOList = new ArrayList<>();
    private List<StockOpnameDataInputByNoSO> stockOpnameDataInputByNoSOList = new ArrayList<>();
    private String selectedNoSO;
    private String selectedTglSO;
    private int currentPageForNoSO = 0;
    private int currentPageForNoSOInput = 0;
    private SearchView searchView;
    private Button filterButton;
    private Spinner blokSpinner;
    private Spinner idLokasiSpinner;
    private String selectedLabel;
    private String selectedUserID;
    private String selectedIdLokasi;
    private String selectedBlok;
    private Set<String> selectedLabels = new HashSet<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_opname);

        initializeViews();
        initializeRecyclerView();
        executorService = Executors.newCachedThreadPool();  // More efficient threading

        loadMoreData();

        setListeners();

    }


    private void setupSpinners() {
        // Jalankan operasi pengambilan data di background thread menggunakan ExecutorService
        executorService.execute(() -> {
            // Ambil data dari API atau database di background thread
            List<LokasiBlok> lokasiBlokList = StockOpnameApi.getLokasiAndBlok();

            // Update UI di thread utama setelah data diambil
            runOnUiThread(() -> {
                if (lokasiBlokList != null && !lokasiBlokList.isEmpty()) {
                    // Pastikan spinner ditemukan
                    Spinner blokSpinner = findViewById(R.id.blok);
                    Spinner idLokasiSpinner = findViewById(R.id.idLokasi);

                    if (blokSpinner == null || idLokasiSpinner == null) {
                        Log.e("SetupSpinners", "Spinner tidak ditemukan!");
                        return;
                    }

                    // Membuat adapter dan menghubungkannya ke spinner
                    ArrayAdapter<LokasiBlok> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lokasiBlokList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    blokSpinner.setAdapter(adapter);
                    idLokasiSpinner.setAdapter(adapter);
                } else {
                    Toast.makeText(StockOpname.this, "Tidak ada data lokasi dan blok", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setupUserSpinner() {
        // Jalankan operasi pengambilan data di background thread menggunakan ExecutorService
        executorService.execute(() -> {
            // Ambil data UserIDSO dari database
            List<UserIDSO> userList = StockOpnameApi.getUserIdsForNoSO("Q.000012");

            // Update UI di thread utama setelah data diambil
            runOnUiThread(() -> {
                if (userList != null && !userList.isEmpty()) {
                    // Pastikan spinner ditemukan
                    Spinner userSpinner = findViewById(R.id.userIDSO);

                    if (userSpinner == null) {
                        Log.e("SetupUserSpinner", "Spinner tidak ditemukan!");
                        return;
                    }

                    // Membuat adapter dan menghubungkannya ke spinner
                    ArrayAdapter<UserIDSO> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    userSpinner.setAdapter(adapter);
                } else {
                    Toast.makeText(StockOpname.this, "Tidak ada data UserIDSO", Toast.LENGTH_SHORT).show();
                }
            });
        });
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
        filterMoulding.setChecked(selectedLabels.contains("Moulding"));
        filterLaminating.setChecked(selectedLabels.contains("Laminating"));
        filterCCAkhir.setChecked(selectedLabels.contains("CCAkhir"));
        filterSanding.setChecked(selectedLabels.contains("Sanding"));
        filterBJ.setChecked(selectedLabels.contains("BJ"));

        // Ambil nilai yang dipilih dari Spinner
        Spinner blokSpinner = dialogView.findViewById(R.id.blok);
        Spinner idLokasiSpinner = dialogView.findViewById(R.id.idLokasi);
        Spinner userSpinner = dialogView.findViewById(R.id.userIDSO);

        // Cek jika semua checkbox tercentang, jika ya, sembunyikan status ceklis
        boolean allChecked = selectedLabels.contains("ST") && selectedLabels.contains("S4S") &&
                selectedLabels.contains("FJ") && selectedLabels.contains("Moulding") &&
                selectedLabels.contains("Laminating") && selectedLabels.contains("CCAkhir") &&
                selectedLabels.contains("Sanding") && selectedLabels.contains("BJ");

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
            if (filterMoulding.isChecked() && !selectedLabels.contains("Moulding")) {
                selectedLabels.add("Moulding");
            }
            if (filterLaminating.isChecked() && !selectedLabels.contains("Laminating")) {
                selectedLabels.add("Laminating");
            }
            if (filterCCAkhir.isChecked() && !selectedLabels.contains("CCAkhir")) {
                selectedLabels.add("CCAkhir");
            }
            if (filterSanding.isChecked() && !selectedLabels.contains("Sanding")) {
                selectedLabels.add("Sanding");
            }
            if (filterBJ.isChecked() && !selectedLabels.contains("BJ")) {
                selectedLabels.add("BJ");
            }


            // Cek apakah ada filter yang dipilih
            if (selectedLabels.isEmpty()) {
                selectedLabels.add("ST");
                selectedLabels.add("S4S");
                selectedLabels.add("FJ");
                selectedLabels.add("Moulding");
                selectedLabels.add("Laminating");
                selectedLabels.add("CCAkhir");
                selectedLabels.add("Sanding");
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
                if (newText.isEmpty()) {
                    Log.d("SearchView", "Teks kosong, memanggil fetchDataInputByNoSO");
                    filterDataInputByNoSO(selectedIdLokasi, selectedLabels , selectedUserID,0);
                    fetchDataByNoSO(selectedIdLokasi, selectedLabels, 0);
                } else {
                    Log.d("SearchView", "Teks tidak kosong, memanggil searchDataInputByNoSO");
                    searchDataInputByNoSO(newText);
                    searchDataByNoSO(newText);
                }
                return true;
            }
        });

        filterButton.setOnClickListener(v -> {
            // Membuka dialog untuk memilih filter
            setupSpinners();
            setupUserSpinner();
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
            selectedLabels.add("Moulding");
            selectedLabels.add("Laminating");
            selectedLabels.add("CCAkhir");
            selectedLabels.add("Sanding");
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
        showLoadingIndicatorAfter(true);

        int limit = 100;
        int offset = page * limit;

        // Pastikan parameter tidak null, beri nilai default jika perlu
        String safeSelectedNoSO = (selectedNoSO == null) ? "" : selectedNoSO;
        String safeSelectedIdLokasi = (selectedIdLokasi == null) ? "" : selectedIdLokasi;
        Set<String> safeSelectedLabels = (selectedLabels == null) ? new HashSet<>() : selectedLabels;

        // Run the network call in a background thread
        executorService.execute(() -> {
            // Perform the network or database call in the background
            List<StockOpnameDataInputByNoSO> data = StockOpnameApi.getStockOpnameDataInputByFilter(safeSelectedNoSO, safeSelectedIdLokasi, safeSelectedLabels, selectedUserID, offset, limit);

            // Now update the UI on the main thread after the data is fetched
            runOnUiThread(() -> {
                if (data != null && !data.isEmpty()) {
                    // Iterate over the fetched data and add unique items to the list
                    if (page == 0) stockOpnameDataInputByNoSOList.clear();
                    for (StockOpnameDataInputByNoSO item : data) {
                        if (!stockOpnameDataInputByNoSOList.contains(item)) {
                            stockOpnameDataInputByNoSOList.add(item);  // Add the item if it's not already in the list
                        }
                    }
                    currentPageForNoSOInput++;

                    if (data.size() < limit) {
                        hasMoreDataToFetchAfter = false;  // No more data to fetch
                    }

                } else {
                    hasMoreDataToFetchAfter = false;
                    stockOpnameDataInputByNoSOList.clear();  // Clear the list if no data is found
                    // Optionally, you can show a message if no data is found
                    // showToast("Tidak ada data lagi untuk NoSO: " + selectedNoSO);
                }

                // Notify the adapter that data has been updated
                stockOpnameDataInputAdapter.notifyDataSetChanged();

                // Hide the loading indicator
                showLoadingIndicatorAfter(false);
                isLoadingAfter = false;
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
            List<StockOpnameDataByNoSO> data = StockOpnameApi.searchStockOpnameDataByNoSO(selectedNoSO, selectedTglSO, searchTerm, selectedLabels,0, 10);

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
        showLoadingIndicatorAfter(true);
        isLoadingAfter = true;

        // Run the network call in a background thread
        executorService.execute(() -> {
            // Perform the network or database call in the background
            List<StockOpnameDataInputByNoSO> data = StockOpnameApi.getStockOpnameDataInputBySearch(selectedNoSO, searchTerm, selectedLabels,0, 10);

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
            List<StockOpnameDataByNoSO> data = StockOpnameApi.getStockOpnameDataByNoSO(safeSelectedNoSO, safeSelectedTglSO, safeSelectedIdLokasi, safeSelectedLabels, offset, limit);
            runOnUiThread(() -> {
                if (data != null && !data.isEmpty()) {
                    findViewById(R.id.dataLabelEmpty).setVisibility(View.GONE);
                    if (page == 0) stockOpnameDataByNoSOList.clear();
                    for (StockOpnameDataByNoSO item : data) {
                        if (!stockOpnameDataByNoSOList.contains(item)) {
                            stockOpnameDataByNoSOList.add(item);
                            Log.d("before", ": " + item.getNoLabel());

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
            List<StockOpnameData> newStockOpnames = StockOpnameApi.getStockOpnameData(currentPage * LIMIT, LIMIT);
            runOnUiThread(() -> {
                if (newStockOpnames != null && !newStockOpnames.isEmpty()) {
                    stockOpnames.addAll(newStockOpnames);
                    adapter.notifyDataSetChanged();
                    currentPage++;
                } else {
                    showToast("Tidak ada data lagi");
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
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();  // Graceful shutdown
        }
    }
}