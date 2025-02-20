package com.example.myapplication;

import static com.example.myapplication.api.StockOpnameApi.getLokasiAndBlok;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private String selectedIdLokasi;



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

        // Tombol Apply
        Button btnApply = dialogView.findViewById(R.id.btn_apply);
        btnApply.setOnClickListener(v -> {
            // Reset paging
            hasMoreDataToFetchBefore = true;
            hasMoreDataToFetchAfter = true;
            currentPageForNoSO = 0;
            currentPageForNoSOInput = 0;

            // Ambil nilai yang dipilih dari Spinner
            Spinner blokSpinner = dialogView.findViewById(R.id.blok);
            Spinner idLokasiSpinner = dialogView.findViewById(R.id.idLokasi);

            String selectedBlok = (String) blokSpinner.getSelectedItem();
            selectedIdLokasi = (String) idLokasiSpinner.getSelectedItem();

            // Ambil nilai yang dipilih dari RadioGroup
            RadioGroup filterGroup = dialogView.findViewById(R.id.filterGroup);
            int selectedRadioButtonId = filterGroup.getCheckedRadioButtonId();

            if (selectedRadioButtonId != -1) {  // Memastikan ada RadioButton yang dipilih
                RadioButton selectedRadioButton = dialogView.findViewById(selectedRadioButtonId);
                selectedLabel = selectedRadioButton.getText().toString();
            }

            // Tampilkan nilai yang dipilih dari Spinner dan RadioButton (atau default "All")
            Toast.makeText(StockOpname.this, selectedBlok + " " + selectedIdLokasi + " " + selectedLabel, Toast.LENGTH_SHORT).show();
            filterDataInputByNoSO(selectedIdLokasi, selectedLabel, currentPageForNoSOInput);

            // Menutup dialog setelah tombol Apply ditekan
            dialog.dismiss();
        });

        // Tombol Cancel
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {
            // Menutup dialog tanpa aksi
            dialog.dismiss();
        });

        // Tampilkan dialog
        dialog.show();
    }



    private void setupSpinnersInDialog(View dialogView) {
        executorService.execute(() -> {
            // Ambil data dari API di background thread
            List<LokasiBlok> lokasiBlokList = StockOpnameApi.getLokasiAndBlok();

            // Update UI di main thread setelah data diambil
            runOnUiThread(() -> {
                if (lokasiBlokList != null && !lokasiBlokList.isEmpty()) {
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
                    blokAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    blokSpinner.setAdapter(blokAdapter);

                    // Setup adapter untuk spinner idLokasi (kosongkan dulu)
                    Spinner idLokasiSpinner = dialogView.findViewById(R.id.idLokasi);
                    ArrayAdapter<String> idLokasiAdapter = new ArrayAdapter<>(StockOpname.this, android.R.layout.simple_spinner_item, new ArrayList<>());
                    idLokasiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    idLokasiSpinner.setAdapter(idLokasiAdapter);

                    // Tambahkan listener untuk blokSpinner
                    blokSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            // Ambil blok yang dipilih
                            String selectedBlok = blokList.get(position);

                            // Jika "Semua" dipilih, set idLokasi juga menjadi "Semua"
                            if ("Semua".equals(selectedBlok)) {
                                List<String> allLokasiList = new ArrayList<>();
                                allLokasiList.add("Semua"); // Menambahkan "Semua" ke daftar idLokasi

                                // Update spinner idLokasi dengan "Semua"
                                ArrayAdapter<String> idLokasiAdapter = new ArrayAdapter<>(StockOpname.this, android.R.layout.simple_spinner_item, allLokasiList);
                                idLokasiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                idLokasiSpinner.setAdapter(idLokasiAdapter);
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
                                idLokasiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                idLokasiSpinner.setAdapter(idLokasiAdapter);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // Kosongkan spinner idLokasi jika tidak ada pilihan blok
                            ArrayAdapter<String> idLokasiAdapter = new ArrayAdapter<>(StockOpname.this, android.R.layout.simple_spinner_item, new ArrayList<>());
                            idLokasiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            idLokasiSpinner.setAdapter(idLokasiAdapter);
                        }
                    });
                } else {
                    Toast.makeText(StockOpname.this, "Tidak ada data lokasi dan blok", Toast.LENGTH_SHORT).show();
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
                    filterDataInputByNoSO(selectedIdLokasi, selectedLabel, 0);
                } else {
                    Log.d("SearchView", "Teks tidak kosong, memanggil searchDataInputByNoSO");
                    searchDataInputByNoSO(newText);
                }
                return true;
            }
        });

        filterButton.setOnClickListener(v -> {
            // Membuka dialog untuk memilih filter
            setupSpinners();
            openFilterDialog();
        });
    }


    private void setListeners() {
        adapter.setOnItemClickListener(position -> {
            StockOpnameData stockOpname = stockOpnames.get(position);
            selectedNoSO = stockOpname.getNoSO();
            selectedTglSO = stockOpname.getTgl();
            selectedLabel = "all";
            selectedIdLokasi = "Semua";

            // Reset paging
            hasMoreDataToFetchBefore = true;
            hasMoreDataToFetchAfter = true;
            currentPageForNoSO = 0;
            currentPageForNoSOInput = 0;

            // Mulai fetch dari awal
            fetchDataByNoSO(selectedNoSO, selectedTglSO, currentPageForNoSO);
            filterDataInputByNoSO(selectedIdLokasi, selectedLabel, currentPageForNoSOInput);
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
                        fetchDataByNoSO(selectedNoSO, selectedTglSO, currentPageForNoSO);
                        Log.d("valuefilter", "scroll with offset: " + currentPageForNoSO + " and limit: " + selectedLabel);

                    }
                    if (!isLoadingAfter && isLastItemVisible(recyclerViewAfter)) {
                        filterDataInputByNoSO(selectedIdLokasi, selectedLabel, currentPageForNoSOInput);
//                        Log.d("valuefilter", "scroll with offset: " + selectedIdLokasi + " and limit: " + selectedLabel);

                    }
                }
            }
        };
    }

    private void filterDataInputByNoSO(String selectedIdLokasi, String selectedLabel, int page) {

        // Show loading indicator while searching
        isLoadingAfter = true;
        showLoadingIndicatorAfter(true);

        int limit = 100;
        int offset = page * limit;

        // Run the network call in a background thread
        executorService.execute(() -> {
            // Perform the network or database call in the background
            List<StockOpnameDataInputByNoSO> data = StockOpnameApi.getStockOpnameDataInputByFilter(selectedNoSO, selectedIdLokasi, selectedLabel, offset, limit);

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

    private void searchDataInputByNoSO(String searchTerm) {
        // Show loading indicator while searching
        showLoadingIndicatorAfter(true);
        isLoadingAfter = true;

        // Run the network call in a background thread
        executorService.execute(() -> {
            // Perform the network or database call in the background
            List<StockOpnameDataInputByNoSO> data = StockOpnameApi.getStockOpnameDataInputBySearch(selectedNoSO, searchTerm, 0, 10);

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

    private void fetchDataByNoSO(String noSO, String tglSO, int page) {

        isLoadingBefore = true;
        showLoadingIndicatorBefore(true);
        int limit = 100;
        int offset = page * limit;

        executorService.execute(() -> {
            List<StockOpnameDataByNoSO> data = StockOpnameApi.getStockOpnameDataByNoSO(noSO, tglSO, offset, limit);
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