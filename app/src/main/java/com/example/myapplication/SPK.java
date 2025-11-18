package com.example.myapplication;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import com.example.myapplication.api.SpkApi;
import com.example.myapplication.model.MstSPKData;
import com.example.myapplication.utils.TableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SPK extends AppCompatActivity {

    private TableLayout mainTable;
    private TableRow selectedRow;
    private MstSPKData selectedSPK;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<MstSPKData> dataList = new ArrayList<>();
    private float touchX = 0f, touchY = 0f;

    private int currentPage = 1;
    private final int pageSize = 50;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private ScrollView scrollView;
    private ProgressBar mainLoadingIndicator;
    private SearchView searchData;   // 🔍 Search bar
    private String searchQuery = ""; // current search text

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spk);

        mainTable = findViewById(R.id.mainTable);
        scrollView = findViewById(R.id.scrollView);
        mainLoadingIndicator = findViewById(R.id.mainLoadingIndicator);
        searchData = findViewById(R.id.searchData);

        // 🧠 SearchView listener
        searchData.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // When user presses “search” on keyboard
                searchQuery = query.trim();
                currentPage = 1;
                isLastPage = false;
                loadDataAndDisplayTable(false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText.trim();

                // Cancel any pending search
                handler.removeCallbacks(searchRunnable);

                // Post new delayed search
                searchRunnable = () -> {
                    currentPage = 1;
                    isLastPage = false;
                    loadDataAndDisplayTable(false);
                };

                handler.postDelayed(searchRunnable, 600); // ⏱ wait 600ms after last keystroke
                return true;
            }
        });


        // Initial load
        loadDataAndDisplayTable(false);

        // Infinite scroll detection
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
            int diff = view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY());
            if (diff <= 150 && !isLoading && !isLastPage) {
                loadDataAndDisplayTable(true);
            }
        });
    }

    // -------------------------------------
    // 🔹 Load data (with pagination + search)
    // -------------------------------------
    private void loadDataAndDisplayTable(boolean append) {
        if (isLoading || isLastPage) return;

        isLoading = true;
        if (!append)
            mainLoadingIndicator.setVisibility(View.VISIBLE);
        else
            showLoadingFooter();

        executorService.execute(() -> {
            // ✅ now using searchQuery
            List<MstSPKData> newData = SpkApi.getMstSPKData(currentPage, pageSize, searchQuery);

            runOnUiThread(() -> {
                removeLoadingFooter();
                mainLoadingIndicator.setVisibility(View.GONE);

                if (newData == null || newData.isEmpty()) {
                    if (!append) {
                        dataList.clear();
                        populateTable(dataList);
                    }
                    isLastPage = true;
                } else {
                    if (append) dataList.addAll(newData);
                    else dataList = newData;

                    populateTable(dataList);
                    currentPage++;
                }

                isLoading = false;
            });
        });
    }

    // -------------------------------------
    // 🔹 Populate table
    // -------------------------------------
    private void populateTable(List<MstSPKData> dataList) {
        mainTable.removeAllViews();

        if (dataList == null || dataList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Data tidak ditemukan");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            mainTable.addView(noDataView);
            return;
        }

        int rowIndex = 0;

        for (MstSPKData data : dataList) {
            TableRow row = new TableRow(this);
            row.setTag(data);

            TextView col1 = TableUtils.createTextView(this, data.getNoSPK(), 1.0f);
            TextView col2 = TableUtils.createTextView(this, data.getTanggal(), 1.0f);
            TextView col3 = TableUtils.createTextView(this, data.getNoContract(), 1.0f);
            TextView col4 = TableUtils.createTextView(this, data.getBuyerName(), 1.0f);
            TextView col5 = TableUtils.createTextView(this, data.getTujuan(), 1.0f);
            TextView col6 = TableUtils.createTextView(this, data.getEnableLabel(), 1.0f);

            row.addView(col1);
            row.addView(TableUtils.createDivider(this));
            row.addView(col2);
            row.addView(TableUtils.createDivider(this));
            row.addView(col3);
            row.addView(TableUtils.createDivider(this));
            row.addView(col4);
            row.addView(TableUtils.createDivider(this));
            row.addView(col5);
            row.addView(TableUtils.createDivider(this));
            row.addView(col6);

            // zebra color
            if (rowIndex % 2 == 0)
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
            else
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

            // click handler
            row.setOnClickListener(v -> {
                if (selectedRow != null && selectedRow != row) {
                    int prevIndex = mainTable.indexOfChild(selectedRow);
                    int prevColor = (prevIndex % 2 == 0)
                            ? ContextCompat.getColor(this, R.color.background_cream)
                            : ContextCompat.getColor(this, R.color.white);
                    selectedRow.setBackgroundColor(prevColor);
                    TableUtils.resetTextColor(this, selectedRow);
                }

                row.setBackgroundResource(R.drawable.row_selector);
                TableUtils.setTextColor(this, row, R.color.white);
                selectedRow = row;
                selectedSPK = data;
            });

            // track touch
            row.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchX = event.getRawX();
                    touchY = event.getRawY();
                }
                return false;
            });

            // long click popup
            row.setOnLongClickListener(v -> {
                showRowPopup(v, data, touchX, touchY);
                return true;
            });

            mainTable.addView(row);
            rowIndex++;
        }
    }

    // -------------------------------------
    // 🔹 Footer: show / remove loading row
    // -------------------------------------
    private void showLoadingFooter() {
        if (findViewById(R.id.footer_loading_row) != null) return;

        TableRow footer = new TableRow(this);
        footer.setId(R.id.footer_loading_row);
        footer.setTag("loading_footer");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(16, 16, 16, 16);

        ProgressBar pb = new ProgressBar(this);
        pb.setIndeterminate(true);
        pb.setPadding(8, 0, 16, 0);

        TextView tv = new TextView(this);
        tv.setText("Loading more data...");
        tv.setTextColor(Color.DKGRAY);
        tv.setTextSize(14);

        layout.addView(pb);
        layout.addView(tv);
        footer.addView(layout);

        mainTable.addView(footer);
    }

    private void removeLoadingFooter() {
        View footer = findViewById(R.id.footer_loading_row);
        if (footer != null) mainTable.removeView(footer);
    }

    // -------------------------------------
    // 🔹 Popup (unchanged)
    // -------------------------------------
    private void showRowPopup(View anchorView, MstSPKData data, float x, float y) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_menu_row_spk, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setElevation(16f);

        Button btnToggle = popupView.findViewById(R.id.btnActivateDeactivate);
        updateToggleButtonText(btnToggle, data.isEnabled());

        btnToggle.setOnClickListener(v -> {
            popupWindow.dismiss();
            final int target = data.isEnabled() ? 0 : 1;
            final String actionText = (target == 1) ? "Aktifkan" : "Nonaktifkan";

            new AlertDialog.Builder(this)
                    .setTitle(actionText + " SPK")
                    .setMessage(actionText + " SPK " + data.getNoSPK() + "?")
                    .setPositiveButton("Ya", (d, w) -> {
                        executorService.execute(() -> {
                            boolean ok = SpkApi.setMstSPKEnable(data.getNoSPK(), target);
                            runOnUiThread(() -> {
                                if (!ok) {
                                    Toast.makeText(this, "Gagal memperbarui status.", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                data.setEnable(target);
                                if (anchorView instanceof TableRow) {
                                    TableRow row = (TableRow) anchorView;
                                    TextView col6 = (TextView) row.getChildAt(10);
                                    col6.setText(data.getEnableLabel());
                                }
                                Toast.makeText(this, "Status: " + data.getEnableLabel(), Toast.LENGTH_SHORT).show();
                            });
                        });
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = popupView.getMeasuredWidth();
        int popupHeight = popupView.getMeasuredHeight();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        int popupX = (int) x - (popupWidth / 2);
        int popupY = (int) y - popupHeight - 50;

        if (popupX < 10) popupX = 10;
        if (popupX + popupWidth > screenWidth - 10)
            popupX = screenWidth - popupWidth - 10;
        if (popupY < 10) popupY = (int) y + 50;

        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, popupX, popupY);
    }

    private void updateToggleButtonText(Button btn, boolean enabled) {
        btn.setText(enabled ? "Nonaktifkan" : "Aktifkan");
        btn.setCompoundDrawablesWithIntrinsicBounds(
                enabled ? R.drawable.ic_undone : R.drawable.ic_done_all, 0, 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        executorService.shutdown();
    }
}
