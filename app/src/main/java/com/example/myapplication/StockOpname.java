package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.api.StockOpnameApi;
import com.example.myapplication.model.StockOpnameData;
import com.example.myapplication.model.StockOpnameDataByNoSO;
import com.example.myapplication.model.StockOpnameDataInputByNoSO;

import java.util.ArrayList;
import java.util.List;
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

    private void initializeViews() {
        loadingIndicator = findViewById(R.id.loadingIndicator);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewBefore = findViewById(R.id.recyclerViewBefore);
        loadingIndicatorBefore = findViewById(R.id.loadingIndicatorBefore);
        recyclerViewAfter = findViewById(R.id.recyclerViewAfter);
        loadingIndicatorAfter = findViewById(R.id.loadingIndicatorAfter);
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
    }

    private void setListeners() {
        adapter.setOnItemClickListener(position -> {
            StockOpnameData stockOpname = stockOpnames.get(position);
            selectedNoSO = stockOpname.getNoSO();
            selectedTglSO = stockOpname.getTgl();

            // Reset paging
            hasMoreDataToFetchBefore = true;
            hasMoreDataToFetchAfter = true;
            currentPageForNoSO = 0;
            currentPageForNoSOInput = 0;

            // Mulai fetch dari awal
            fetchDataByNoSO(selectedNoSO, selectedTglSO, 0);
            fetchDataInputByNoSO(selectedNoSO, selectedTglSO, 0);
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
                    }
                    if (!isLoadingAfter && isLastItemVisible(recyclerViewAfter)) {
                        fetchDataInputByNoSO(selectedNoSO, selectedTglSO, currentPageForNoSOInput);
                    }
                }
            }
        };
    }


    private void fetchDataByNoSO(String noSO, String tglSO, int page) {
        if (isLoadingBefore) return;  // Prevent fetching if already loading for RecyclerView "Before"

        isLoadingBefore = true;
        showLoadingIndicatorBefore(true);
        int limit = (page == 0) ? 100 : 50;
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


    private void fetchDataInputByNoSO(String noSO, String tglSO, int page) {
        if (isLoadingAfter) return;  // Prevent fetching if already loading for RecyclerView "After"

        isLoadingAfter = true;
        showLoadingIndicatorAfter(true);
        int limit = (page == 0) ? 100 : 50;
        int offset = page * limit;

        executorService.execute(() -> {
            List<StockOpnameDataInputByNoSO> data = StockOpnameApi.getStockOpnameDataInputByNoSO(noSO, offset, limit);
            runOnUiThread(() -> {
                if (data != null && !data.isEmpty()) {
                    if (page == 0) stockOpnameDataInputByNoSOList.clear();
                    for (StockOpnameDataInputByNoSO item : data) {
                        if (!stockOpnameDataInputByNoSOList.contains(item)) {
                            stockOpnameDataInputByNoSOList.add(item);
                        }
                    }
                    currentPageForNoSOInput++;

                    if (data.size() < limit) {
                        hasMoreDataToFetchAfter = false;  // No more data to fetch
                    }
                } else {
                    hasMoreDataToFetchAfter = false;
                    stockOpnameDataInputByNoSOList.clear();
//                    showToast("Tidak ada data lagi untuk NoSO: " + noSO);
                }

                stockOpnameDataInputAdapter.notifyDataSetChanged();
                showLoadingIndicatorAfter(false);
                isLoadingAfter = false;
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
            return lastVisibleItemPosition == totalItemCount - 1 && !isLoadingBefore && hasMoreDataToFetchBefore;
        } else if (recyclerView == recyclerViewAfter) {
            // Mengontrol fetch untuk RecyclerView After
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
                .setMessage("Apakah Anda yakin ingin menghapus item ini? " + item.getNoLabelInput())
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Memanggil method onItemDelete untuk menghapus item
                    onItemDelete(item, position);
                })
                .setNegativeButton("No", null)  // Jika pengguna memilih "No", dialog akan ditutup
                .show();
    }

//    public void onItemDelete(StockOpnameDataInputByNoSO item, int position) {
//        Log.d("StockOpname", "Deleting item at position: " + position);
//
//        // Cari item berdasarkan ID
//        for (int i = 0; i < stockOpnameDataInputByNoSOList.size(); i++) {
//            if (stockOpnameDataInputByNoSOList.get(i).getNoLabelInput().equals(item.getNoLabelInput())) {
//                stockOpnameDataInputByNoSOList.remove(i);
//                stockOpnameDataInputAdapter.notifyItemRemoved(i); // Hapus item di posisi yang benar
//
//                Log.d("StockOpname", "Item deleted, new list size: " + stockOpnameDataInputByNoSOList.size());
//                Toast.makeText(this, "Item deleted: " + item.getNoLabelInput(), Toast.LENGTH_SHORT).show();
//                break;
//            }
//        }
//    }

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
