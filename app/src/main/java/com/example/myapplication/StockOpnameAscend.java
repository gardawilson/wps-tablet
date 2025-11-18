    package com.example.myapplication;

    import android.app.AlertDialog;
    import android.graphics.Color;
    import android.graphics.drawable.ColorDrawable;
    import android.os.Bundle;
    import android.text.Editable;
    import android.text.InputType;
    import android.text.TextWatcher;
    import android.util.DisplayMetrics;
    import android.view.Gravity;
    import android.view.HapticFeedbackConstants;
    import android.view.LayoutInflater;
    import android.view.MotionEvent;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.LinearLayout;
    import android.widget.PopupWindow;
    import android.widget.ProgressBar;
    import android.widget.Spinner;
    import android.widget.TableLayout;
    import android.widget.TableRow;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.SearchView;
    import androidx.core.content.ContextCompat;

    import com.example.myapplication.api.GradeApi;
    import com.example.myapplication.api.MasterApi;
    import com.example.myapplication.api.StockOpnameApi;
    import com.example.myapplication.model.StockOpnameAscendData;
    import com.example.myapplication.model.GradeABCDetailData;
    import com.example.myapplication.model.MstGradeABCData;
    import com.example.myapplication.model.StockOpnameAscendData;
    import com.example.myapplication.model.StockOpnameAscendFamilyData;
    import com.example.myapplication.model.StockOpnameData;
    import com.example.myapplication.utils.DateTimeUtils;
    import com.example.myapplication.utils.LoadingDialogHelper;
    import com.example.myapplication.utils.PermissionUtils;
    import com.example.myapplication.utils.SharedPrefUtils;
    import com.example.myapplication.utils.TableUtils;
    import com.google.android.material.floatingactionbutton.FloatingActionButton;

    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.List;
    import java.util.concurrent.ExecutorService;
    import java.util.concurrent.Executors;


    public class StockOpnameAscend extends AppCompatActivity {

        private TableLayout mainTable;
        private TableLayout familyTable;
        private TableRow selectedRow;
        private TableRow selectedFamilyRow;
        private String selectedFamilyId;
        private StockOpnameAscendData selectedGradeABCData;
        private StockOpnameAscendFamilyData selectedFamilyData;
        private final ExecutorService executorService = Executors.newSingleThreadExecutor();
        private String noSO;
        private String tglSO;
        private String familyID;
        private List<StockOpnameAscendFamilyData> familyDataList; // Data asli yang tidak difilter
        private ProgressBar mainLoadingIndicator;
        private ProgressBar familyLoadingIndicator;
        private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();
        private List<String> userPermissions;
        private Spinner spinNoSO;
        private Button btnSave;
        private SearchView searchData;
        private TextView tvFamilyTable;
        private TextView tvDataTable;


        // Pagination
        private static final int PAGE_SIZE = 20;
        private static final int MAX_VISIBLE_PAGES = 10;

        private int currentPage = 0;
        private int totalPages = 0;

        private boolean isFetchingData = false;   // ⬅️ tambahin ini

        private List<StockOpnameAscendData> allDataList = new ArrayList<>();

        private LinearLayout paginationBar;
        private LinearLayout pageNumbersContainer;
        private ImageButton btnPrevPage, btnNextPage;

        // Debounce untuk search
        private static final long SEARCH_DEBOUNCE_MS = 400L;
        private android.os.Handler searchHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        private Runnable searchRunnable;
        private String lastSearchQuery = "";




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_stock_opname_ascend);

            mainTable = findViewById(R.id.mainTable);
            mainLoadingIndicator = findViewById(R.id.mainLoadingIndicator);
            spinNoSO = findViewById(R.id.spinNoSO);
            btnSave = findViewById(R.id.btnSave);
            searchData = findViewById(R.id.searchData);
            familyTable = findViewById(R.id.familyTable);
            familyLoadingIndicator = findViewById(R.id.familyLoadingIndicator);
            tvFamilyTable = findViewById(R.id.tvFamilyTable);
            tvDataTable = findViewById(R.id.tvDataTable);

            paginationBar        = findViewById(R.id.paginationBar);
            pageNumbersContainer = findViewById(R.id.pageNumbersContainer);
            btnPrevPage          = findViewById(R.id.btnPrevPage);
            btnNextPage          = findViewById(R.id.btnNextPage);

            // Listener Prev
            btnPrevPage.setOnClickListener(v -> {
                goToPage(currentPage - 1);
            });

            // Listener Next
            btnNextPage.setOnClickListener(v -> {
                goToPage(currentPage + 1);
            });


            //PERMISSION CHECK
            userPermissions = SharedPrefUtils.getPermissions(this);

            loadStockOpnameSpinner("");

            spinNoSO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    StockOpnameData selected = (StockOpnameData) parent.getItemAtPosition(position);
                    tvDataTable.setVisibility(View.VISIBLE);

                    if (selected != null && !selected.getNoSO().isEmpty()) {
                        noSO = selected.getNoSO();
                        tglSO = selected.getTgl();
                        familyID = null;
                        mainTable.removeAllViews();

                        paginationBar.setVisibility(View.GONE);
                        loadFamilyDataAndDisplayTable(selected.getNoSO());
                    } else {
                        mainTable.removeAllViews(); // kosongkan tabel kalau pilih "PILIH"
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            searchData.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // kalau user tekan enter / search di keyboard → langsung jalanin
                    lastSearchQuery = query != null ? query.trim() : "";

                    // cancel debounce yang lagi jalan
                    if (searchRunnable != null) {
                        searchHandler.removeCallbacks(searchRunnable);
                    }

                    if (noSO != null && !noSO.isEmpty()) {
                        loadDataAndDisplayTable(noSO, familyID, lastSearchQuery);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    lastSearchQuery = newText != null ? newText.trim() : "";

                    // setiap ketik baru → cancel timer sebelumnya
                    if (searchRunnable != null) {
                        searchHandler.removeCallbacks(searchRunnable);
                    }

                    // set runnable baru
                    searchRunnable = () -> {
                        if (noSO != null && !noSO.isEmpty()) {
                            loadDataAndDisplayTable(noSO, familyID, lastSearchQuery);
                        }
                    };

                    // jalankan setelah delay (debounce)
                    searchHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_MS);

                    return true;
                }
            });


            btnSave.setOnClickListener(v -> {
                StockOpnameData selectedSO = (StockOpnameData) spinNoSO.getSelectedItem();

                if (selectedSO == null || selectedSO.getNoSO().isEmpty()) {
                    Toast.makeText(this, "Pilih NoSO dulu", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 🔑 Loop semua row & ambil nilai terbaru dari EditText
                for (int i = 0; i < mainTable.getChildCount(); i++) {
                    View rowView = mainTable.getChildAt(i);
                    if (rowView instanceof TableRow) {
                        TableRow row = (TableRow) rowView;

                        // Ambil data langsung dari tag
                        StockOpnameAscendData data = (StockOpnameAscendData) row.getTag();

                        // Cari EditText dari row (bukan index)
                        EditText colQty = row.findViewWithTag("qtyFound");
                        EditText colRemark = row.findViewWithTag("remark");

                        if (colQty != null) {
                            String qtyText = colQty.getText().toString().trim();
                            if (qtyText.isEmpty()) {
                                data.setQtyFound(null);
                            } else {
                                try {
                                    double qtyFound = Double.parseDouble(qtyText);
                                    data.setQtyFound(qtyFound);
                                } catch (NumberFormatException e) {
                                    data.setQtyFound(null);
                                }
                            }
                        }

                        if (colRemark != null) {
                            data.setUsageRemark(colRemark.getText().toString());
                        }
                    }
                }

                loadingDialogHelper.show(this);

                executorService.execute(() -> {
                    boolean success = StockOpnameApi.saveStockOpnameAscendHasil(allDataList, selectedSO.getNoSO());

                    runOnUiThread(() -> {
                        loadingDialogHelper.hide();
                        if (success) {
                            loadFamilyDataAndDisplayTable(noSO);

                            // ambil keyword yang sedang aktif di SearchView (kalau mau filter tetap kepakai)
                            String currentKeyword = "";
                            if (searchData != null && searchData.getQuery() != null) {
                                currentKeyword = searchData.getQuery().toString();
                            }

                            // reload data tapi TETAP di page yang sama
                            loadDataAndDisplayTable(noSO, familyID, currentKeyword, true);

                            Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                        }

                    });
                });
            });

        }


        private void addPageButton(int pageIndex) {
            TextView tv = new TextView(this);
            tv.setText(String.valueOf(pageIndex + 1));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            lp.setMargins(8, 0, 8, 0);
            tv.setLayoutParams(lp);

            tv.setTextSize(14);
            tv.setGravity(Gravity.CENTER);
            tv.setMinWidth(dpToPx(32));
            tv.setMinHeight(dpToPx(32));

            boolean isCurrent  = (pageIndex == currentPage);
            boolean isComplete = isPageComplete(pageIndex);

            if (isCurrent) {
                tv.setBackgroundResource(R.drawable.bg_page_number_active);
                tv.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            } else if (isComplete) {
                tv.setBackgroundResource(R.drawable.bg_page_number_complete);
                tv.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            } else {
                tv.setBackgroundResource(R.drawable.bg_page_number);
                tv.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            }

            // SELALU pasang listener, tapi cek isFetchingData DI DALAM listener
            tv.setOnClickListener(v -> {
                if (isFetchingData) return;   // lagi fetch, abaikan klik
                if (pageIndex == currentPage) return;
                goToPage(pageIndex);
            });

            tv.setClickable(true);
            tv.setFocusable(true);

            pageNumbersContainer.addView(tv);
        }


        private boolean isPageComplete(int pageIndex) {
            if (allDataList == null || allDataList.isEmpty()) return false;

            int fromIndex = pageIndex * PAGE_SIZE;
            int toIndex   = Math.min(fromIndex + PAGE_SIZE, allDataList.size());

            if (fromIndex >= toIndex) return false; // tidak ada data di page itu

            for (int i = fromIndex; i < toIndex; i++) {
                StockOpnameAscendData d = allDataList.get(i);
                if (d == null) return false;

                Double q = d.getQtyFound();
                // aturan lengkap: wajib terisi dan > 0 (atau terserah kamu)
                if (q == null) {
                    return false;
                }
            }
            return true;
        }


        private int dpToPx(int dp) {
            float density = getResources().getDisplayMetrics().density;
            return (int) (dp * density + 0.5f);
        }


        private void addEllipsis() {
            TextView dot = new TextView(this);
            dot.setText("...");
            dot.setPadding(8, 12, 8, 12);
            dot.setTextSize(14);
            dot.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));

            pageNumbersContainer.addView(dot);
        }


        private void goToPage(int pageIndex) {
            if (isFetchingData) return;               // lagi fetch API, abaikan klik
            if (totalPages == 0) return;
            if (pageIndex < 0 || pageIndex >= totalPages) return;
            if (pageIndex == currentPage) return;

            // anggap ini juga semacam “fetching lokal”
            isFetchingData = true;
            setPaginationEnabled(false);

            currentPage = pageIndex;
            renderPageNumbers();

            mainTable.removeAllViews();
            mainTable.setVisibility(View.INVISIBLE);
            tvDataTable.setVisibility(View.GONE);
            mainLoadingIndicator.setVisibility(View.VISIBLE);

            mainTable.post(() -> {
                showCurrentPage();                       // pakai data di allDataList
                mainLoadingIndicator.setVisibility(View.GONE);
                mainTable.setVisibility(View.VISIBLE);

                // selesai “loading” lokal
                isFetchingData = false;
                setPaginationEnabled(true);
                renderPageNumbers(); // refresh state warna/enable
            });
        }




        private void loadStockOpnameSpinner(@Nullable String selectedNoSO, @Nullable Runnable onDone) {
            executorService.execute(() -> {
                List<StockOpnameData> soList = StockOpnameApi.getStockOpnameData(true); // method access kamu

                // Tambahkan item default "PILIH"
                soList.add(0, new StockOpnameData("", "PILIH"));

                runOnUiThread(() -> {
                    ArrayAdapter<StockOpnameData> adapter = new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            soList
                    );
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinNoSO.setAdapter(adapter);

                    // Set default selection
                    if (selectedNoSO == null || selectedNoSO.equals("0")) {
                        spinNoSO.setSelection(0);
                    } else {
                        for (int i = 0; i < soList.size(); i++) {
                            if (soList.get(i).getNoSO().equals(selectedNoSO)) {
                                spinNoSO.setSelection(i);
                                break;
                            }
                        }
                    }

                    // 🔑 Callback setelah selesai
                    if (onDone != null) onDone.run();
                });
            });
        }
        // Overload tanpa callback
        private void loadStockOpnameSpinner(@Nullable String selectedNoSO) {
            loadStockOpnameSpinner(selectedNoSO, null);
        }

        private void loadFamilyDataAndDisplayTable(String noSO) {
            familyLoadingIndicator.setVisibility(View.VISIBLE);

            executorService.execute(() -> {
                familyDataList = StockOpnameApi.getStockOpnameAscendFamilyData(noSO);

                runOnUiThread(() -> {
                    populateFamilyTable(familyDataList);
                    familyLoadingIndicator.setVisibility(View.GONE);
                });
            });
        }

        //POPULATE DI TABEL FAMILY
        private void populateFamilyTable(List<StockOpnameAscendFamilyData> familyDataList) {

            familyTable.removeAllViews();
            tvFamilyTable.setVisibility(View.GONE);

            if (familyDataList == null || familyDataList.isEmpty()) {
                TextView noDataView = new TextView(this);
                noDataView.setText("Data tidak ditemukan");
                noDataView.setGravity(Gravity.CENTER);
                noDataView.setPadding(16, 16, 16, 16);
                familyTable.addView(noDataView);
                return;
            }

            int rowIndex = 0;

            for (StockOpnameAscendFamilyData data : familyDataList) {
                TableRow row = new TableRow(this);
                row.setTag(rowIndex);

                // Kolom FamilyName
                TextView col2 = TableUtils.createTextView(
                        this,
                        data.getFamilyName() + " (" + data.getCompleteItem() + "/" + data.getTotalItem() + ")",
                        1.0f
                );
                col2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

                // Tambahkan ke row
                row.addView(col2);

                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

                // 🔑 Klik listener
                row.setOnClickListener(v -> {
                    // reset warna row sebelumnya
                    if (selectedFamilyRow != null) {
                        selectedFamilyRow.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                        TableUtils.resetTextColor(this, selectedFamilyRow);
                    }

                    // tandai row baru
                    row.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
                    TableUtils.setTextColor(this, row, R.color.white);

                    selectedFamilyRow = row;
                    selectedFamilyData = data;
                    familyID = data.getFamilyID(); // simpan FamilyID

                    loadDataAndDisplayTable(noSO, familyID);
                });

                familyTable.addView(row);
                rowIndex++;
            }

            // ✅ Restore highlight kalau ada familyID yang sudah dipilih sebelumnya
            if (familyID != null) {
                for (int i = 0; i < familyTable.getChildCount(); i++) {
                    View rowView = familyTable.getChildAt(i);
                    if (rowView instanceof TableRow) {
                        TableRow row = (TableRow) rowView;
                        StockOpnameAscendFamilyData rowData = familyDataList.get(i);

                        if (rowData.getFamilyID().equals(familyID)) {
                            row.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
                            TableUtils.setTextColor(this, row, R.color.white);

                            selectedFamilyRow = row;
                            selectedFamilyData = rowData;
                            break;
                        }
                    }
                }
            }
        }



        // CORE method (punya flag keepCurrentPage)
        // CORE method (punya flag keepCurrentPage)
        private void loadDataAndDisplayTable(String noSO,
                                             String familyID,
                                             String keyword,
                                             boolean keepCurrentPage) {

            isFetchingData = true;                          // ⬅️ lagi fetch
            mainLoadingIndicator.setVisibility(View.VISIBLE);
            setPaginationEnabled(false);                   // ⬅️ matikan pagination sementara

            final int pageBefore = currentPage;

            executorService.execute(() -> {
                List<StockOpnameAscendData> result =
                        StockOpnameApi.getStockOpnameAscendData(noSO, tglSO, familyID, keyword);

                runOnUiThread(() -> {
                    try {
                        allDataList = result != null ? result : new ArrayList<>();

                        if (allDataList.isEmpty()) {
                            currentPage = 0;
                            totalPages = 0;

                            // kosongkan tabel & pagination
                            populateTable(Collections.emptyList(), 0);
                            paginationBar.setVisibility(View.GONE);
                            pageNumbersContainer.removeAllViews();

                        } else {
                            totalPages = (int) Math.ceil(allDataList.size() / (double) PAGE_SIZE);

                            if (keepCurrentPage) {
                                int page = pageBefore;
                                if (page < 0) page = 0;
                                if (page > totalPages - 1) page = totalPages - 1;
                                currentPage = page;
                            } else {
                                currentPage = 0;
                            }

                            // render tabel + angka pagination
                            showCurrentPage();
                            renderPageNumbers();
                        }
                    } finally {
                        // ✅ apapun hasilnya, pastikan flag & UI balik normal
                        isFetchingData = false;
                        mainLoadingIndicator.setVisibility(View.GONE);
                        setPaginationEnabled(true);
                    }
                });
            });
        }

        // dipakai oleh search & klik family (mulai dari page 1)
        private void loadDataAndDisplayTable(String noSO, String familyID, String keyword) {
            loadDataAndDisplayTable(noSO, familyID, keyword, false);
        }

        private void loadDataAndDisplayTable(String noSO, String familyID) {
            loadDataAndDisplayTable(noSO, familyID, "", false);
        }


        private void setPaginationEnabled(boolean enabled) {
            // prev/next
            btnPrevPage.setEnabled(enabled && currentPage > 0);
            btnNextPage.setEnabled(enabled && currentPage < totalPages - 1);

            float alpha = enabled ? 1.0f : 0.4f;
            btnPrevPage.setAlpha(alpha);
            btnNextPage.setAlpha(alpha);

            // nomor halaman yang sudah ter-render saat ini
            for (int i = 0; i < pageNumbersContainer.getChildCount(); i++) {
                View child = pageNumbersContainer.getChildAt(i);
                child.setEnabled(enabled);
                child.setAlpha(alpha);
            }
        }


        private void showCurrentPage() {
            if (allDataList == null || allDataList.isEmpty()) {
                populateTable(Collections.emptyList(), 0);
                paginationBar.setVisibility(View.GONE);
                return;
            }

            int fromIndex = currentPage * PAGE_SIZE;
            int toIndex   = Math.min(fromIndex + PAGE_SIZE, allDataList.size());

            List<StockOpnameAscendData> pageList = allDataList.subList(fromIndex, toIndex);

            // render isi tabel
            populateTable(pageList, fromIndex);

            paginationBar.setVisibility(totalPages > 1 ? View.VISIBLE : View.GONE);
            // ❌ JANGAN panggil renderPageNumbers() di sini lagi
        }




        private void renderPageNumbers() {
            pageNumbersContainer.removeAllViews();

            if (totalPages <= 1) {
                btnPrevPage.setEnabled(false);
                btnNextPage.setEnabled(false);
                setPaginationEnabled(!isFetchingData);  // sync prev/next juga
                return;
            }

            // Prev / Next state dasar
            btnPrevPage.setEnabled(currentPage > 0);
            btnNextPage.setEnabled(currentPage < totalPages - 1);

            int firstPage = 0;
            int lastPage  = totalPages - 1;

            if (totalPages <= MAX_VISIBLE_PAGES) {
                // Kasus sedikit halaman → tampilkan semua
                for (int i = 0; i < totalPages; i++) {
                    addPageButton(i);
                }
            } else {
                // Kasus halaman banyak → window + ellipsis
                int windowSize = MAX_VISIBLE_PAGES - 2;  // sisakan slot untuk first & last
                int windowStart = currentPage - windowSize / 2;
                int windowEnd   = currentPage + windowSize / 2;

                if (windowStart < 1) {
                    windowStart = 1;
                    windowEnd = windowStart + windowSize - 1;
                }
                if (windowEnd > lastPage - 1) {
                    windowEnd = lastPage - 1;
                    windowStart = windowEnd - windowSize + 1;
                }

                // pertama
                addPageButton(firstPage);

                if (windowStart > 1) {
                    addEllipsis();
                }

                for (int i = windowStart; i <= windowEnd; i++) {
                    addPageButton(i);
                }

                if (windowEnd < lastPage - 1) {
                    addEllipsis();
                }

                // terakhir
                addPageButton(lastPage);
            }

            // ⬅️ SELALU sync enable/alpha pagination di akhir
            setPaginationEnabled(!isFetchingData);
        }





        private void populateTable(List<StockOpnameAscendData> dataList, int startOffset) {
            mainTable.removeAllViews();
            tvDataTable.setVisibility(View.GONE);

            if (dataList == null || dataList.isEmpty()) {
                TextView noDataView = new TextView(this);
                noDataView.setText("Data tidak ditemukan");
                noDataView.setGravity(Gravity.CENTER);
                noDataView.setPadding(16, 16, 16, 16);
                mainTable.addView(noDataView);
                return;
            }

            int rowIndex = 0;

            for (StockOpnameAscendData data : dataList) {
                TableRow row = new TableRow(this);
                row.setTag(data); // simpan object, bukan index

                // 🔹 Kolom Nomor Urut (pakai offset)
                int rowNumber = startOffset + rowIndex + 1;
                TextView col1 = TableUtils.createTextView(this, String.valueOf(rowNumber), 0.2f);

                TextView col2 = TableUtils.createTextView(this, data.getItemCode(), 0.5f);

                // Kolom ShelfCode
                TextView colShelfCode = TableUtils.createTextView(this, data.getShelfCode(), 0.5f);

                // Kolom ItemName
                TextView col3 = TableUtils.createTextView(this, data.getItemName(), 1.0f);
                col3.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

                // Kolom PCS
                TextView col4 = TableUtils.createTextView(this, String.valueOf(data.getPcs()), 0.3f);

                // Kolom UOM
                TextView colUOM = TableUtils.createTextView(this, data.getUomID(), 0.3f);

                String usageText = !data.isUpdateUsage() ? "?" : String.valueOf(data.getQtyUsage());
                TextView col5 = TableUtils.createTextView(this, usageText, 0.3f);

                // Kolom Qty Found
                EditText col6 = TableUtils.createEditTextNumber(
                        this,
                        data.getQtyFound() != null ? String.valueOf(data.getQtyFound()) : "",
                        0.3f
                );
                col6.setTag("qtyFound");

                final boolean[] alreadyFetched = {false};

                // long press → popup
                col5.setOnLongClickListener(v -> {
                    v.post(() -> {
                        int[] location = new int[2];
                        v.getLocationOnScreen(location);

                        float rawX = location[0] + v.getWidth() / 2f;
                        float rawY = location[1];

                        showQtyUsagePopup(v, data, noSO, rawX, rawY, col6);
                    });
                    return true;
                });

                col6.addTextChangedListener(new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!alreadyFetched[0]) {
                            if (s.length() > 0) {
                                alreadyFetched[0] = true;

                                new Thread(() -> {
                                    double newUsage = StockOpnameApi.fetchQtyUsage(data.getItemID(), tglSO);
                                    data.setQtyUsage(newUsage);

                                    runOnUiThread(() -> col5.setText(String.valueOf(newUsage)));
                                }).start();
                            }
                        }
                    }
                    @Override public void afterTextChanged(Editable s) {
                        if (s.length() == 0 && !data.isUpdateUsage()) {
                            data.setQtyUsage(-1);
                            col5.setText("?");
                            alreadyFetched[0] = false;
                        }
                    }
                });

                col6.setOnFocusChangeListener((v, hasFocus) -> {
                    if (!hasFocus) {
                        String text = col6.getText().toString().trim();
                        if (text.isEmpty()) {
                            data.setQtyFound(null);
                        } else {
                            try {
                                data.setQtyFound(Double.parseDouble(text));
                            } catch (NumberFormatException e) {
                                data.setQtyFound(null);
                            }
                        }
                    }
                });

                // Kolom Remark
                EditText col7 = TableUtils.createEditTextText(
                        this,
                        data.getUsageRemark() != null ? data.getUsageRemark() : "",
                        1.0f
                );
                col7.setTag("remark");
                col7.setOnFocusChangeListener((v, hasFocus) -> {
                    if (!hasFocus) {
                        data.setUsageRemark(col7.getText().toString());
                    }
                });

                // Tambahkan ke row
                row.addView(col1);
                row.addView(TableUtils.createDivider(this));

                row.addView(col2);
                row.addView(TableUtils.createDivider(this));

                row.addView(colShelfCode);
                row.addView(TableUtils.createDivider(this));

                row.addView(col3);
                row.addView(TableUtils.createDivider(this));

                row.addView(colUOM);
                row.addView(TableUtils.createDivider(this));

                row.addView(col4);
                row.addView(TableUtils.createDivider(this));

                row.addView(col5);
                row.addView(TableUtils.createDivider(this));

                row.addView(col6);
                row.addView(TableUtils.createDivider(this));

                row.addView(col7);

                // Warna baris selang-seling (pakai index di halaman saja, boleh)
                if (rowIndex % 2 == 0) {
                    row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
                } else {
                    row.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                }

                mainTable.addView(row);
                rowIndex++;
            }
        }



        private void showQtyUsagePopup(View anchorView,
                                       StockOpnameAscendData data,
                                       String noSO,    // ✅ tambahkan noso agar bisa dipakai delete
                                       float rawX, float rawY,
                                       EditText colQtyFound) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View popupView = inflater.inflate(R.layout.popup_menu_info_clear, null);

            popupView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );
            int pw = popupView.getMeasuredWidth();
            int ph = popupView.getMeasuredHeight();

            final PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
            );
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchable(true);

            // Posisi
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int screenW = dm.widthPixels;
            int screenH = dm.heightPixels;

            int desiredX = (int) rawX - (int) (pw * 0.85f);
            int desiredY = (int) rawY - ph - Math.round(8 * dm.density);

            desiredX = Math.max(0, Math.min(desiredX, screenW - pw));
            desiredY = Math.max(0, Math.min(desiredY, screenH - ph));

            View root = getWindow().getDecorView();
            popupWindow.showAtLocation(root, Gravity.TOP | Gravity.START, desiredX, desiredY);

            // Wiring tombol
            Button btnInfo = popupView.findViewById(R.id.btnInfo);
            btnInfo.setOnClickListener(v -> {
                popupWindow.dismiss();
                String usageText = data.getQtyUsage() < 0 ? "belum dihitung" : String.valueOf(data.getQtyUsage());
                Toast.makeText(this, "Qty Usage: " + usageText, Toast.LENGTH_SHORT).show();
            });

            Button btnClear = popupView.findViewById(R.id.btnDelete);
            btnClear.setOnClickListener(v -> {
                popupWindow.dismiss();

                // 🔹 Jalankan di background thread
                executorService.execute(() -> {
                    boolean deleted = StockOpnameApi.deleteAscendHasil(noSO, data.getItemID());

                    runOnUiThread(() -> {
                        if (deleted) {
                            // reset model
                            data.setQtyUsage(-1);
                            data.setQtyFound(null);
                            colQtyFound.setText("");

                            // update UI
                            ((TextView) anchorView).setText("?");
                            Toast.makeText(this, "Qty Usage berhasil direset", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Gagal reset Qty Usage", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            });
        }



        @Override
        protected void onDestroy() {
            super.onDestroy();
            executorService.shutdown();

            if (searchRunnable != null) {
                searchHandler.removeCallbacks(searchRunnable);
            }
        }

    }