package com.example.myapplication.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.example.myapplication.R;
import com.example.myapplication.api.ProsesProduksiApi;
import com.example.myapplication.model.TooltipData;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutorService;


public class TooltipUtils {

    /**
     * Mengambil data tooltip dan menampilkan tooltip dengan loading
     */
    public static void fetchDataAndShowTooltip(Activity activity,
                                               ExecutorService executorService,
                                               View anchorView,
                                               String noLabel,
                                               String tableH,
                                               String tableD,
                                               String mainColumn,
                                               TooltipDismissListener dismissListener) {
        // 1. Langsung tampilkan tooltip dengan loading state
        PopupWindow popupWindow = showLoadingTooltip(activity, anchorView, noLabel, executorService);

        // 2. Tambahkan dismiss listener
        popupWindow.setOnDismissListener(() -> {
            if (dismissListener != null) {
                dismissListener.onTooltipDismissed();
            }
        });

        // 3. Fetch data di background
        executorService.execute(() -> {
            TooltipData tooltipData = ProsesProduksiApi.getTooltipData(noLabel, tableH, tableD, mainColumn);

            activity.runOnUiThread(() -> {
                if (tooltipData != null && tooltipData.getNoLabel() != null && tooltipData.getTableData() != null) {
                    // 4. Update tooltip dengan data yang sudah di-fetch
                    updateTooltipWithData(activity, popupWindow, tooltipData, tableH, executorService);
                } else {
                    // 5. Tampilkan error state
                    showErrorTooltip(activity, popupWindow, noLabel);
                }
            });
        });
    }


    public interface TooltipDismissListener {
        void onTooltipDismissed();
    }

    /**
     * Menampilkan tooltip dengan state loading menggunakan layout existing
     */
    private static PopupWindow showLoadingTooltip(Activity activity,
                                                  View anchorView,
                                                  String noLabel,
                                                  ExecutorService executorService) {

        // Inflate layout tooltip existing
        View tooltipView = LayoutInflater.from(activity).inflate(R.layout.tooltip_layout_right, null);

        // Set semua text field ke loading state
        setLoadingState(tooltipView);

        // Sembunyikan tabel dan ganti dengan loading indicator
        showLoadingIndicator(activity, tooltipView);

        // Buat dan tampilkan popup
        return createAndShowPopupWindow(activity, anchorView, tooltipView);
    }


    /**
     * Set semua field ke loading state
     */
    private static void setLoadingState(View tooltipView) {
        // Set semua TextView ke "Loading..."
        ((TextView) tooltipView.findViewById(R.id.tvNoLabel)).setText("Loading...");
        ((TextView) tooltipView.findViewById(R.id.tvDateTime)).setText("Loading...");
        ((TextView) tooltipView.findViewById(R.id.tvJenis)).setText("Loading...");
        ((TextView) tooltipView.findViewById(R.id.tvNoSPK)).setText("Loading...");
        ((TextView) tooltipView.findViewById(R.id.tvNoSPKAsal)).setText("Loading...");
        ((TextView) tooltipView.findViewById(R.id.tvNamaGrade)).setText("Loading...");
        ((TextView) tooltipView.findViewById(R.id.tvIsLembur)).setText("Loading...");
        ((TextView) tooltipView.findViewById(R.id.tvNoPlat)).setText("Loading...");
        ((TextView) tooltipView.findViewById(R.id.tvNoKBSuket)).setText("Loading...");

        // Set text color ke gray untuk loading state
        int loadingColor = Color.GRAY;
        ((TextView) tooltipView.findViewById(R.id.tvNoLabel)).setTextColor(loadingColor);
        ((TextView) tooltipView.findViewById(R.id.tvDateTime)).setTextColor(loadingColor);
        ((TextView) tooltipView.findViewById(R.id.tvJenis)).setTextColor(loadingColor);
        ((TextView) tooltipView.findViewById(R.id.tvNoSPK)).setTextColor(loadingColor);
        ((TextView) tooltipView.findViewById(R.id.tvNoSPKAsal)).setTextColor(loadingColor);
        ((TextView) tooltipView.findViewById(R.id.tvNamaGrade)).setTextColor(loadingColor);
        ((TextView) tooltipView.findViewById(R.id.tvIsLembur)).setTextColor(loadingColor);
        ((TextView) tooltipView.findViewById(R.id.tvNoPlat)).setTextColor(loadingColor);
        ((TextView) tooltipView.findViewById(R.id.tvNoKBSuket)).setTextColor(loadingColor);
    }

    /**
     * Menampilkan loading indicator di area tabel
     */
    private static void showLoadingIndicator(Activity activity, View tooltipView) {
        TableLayout tableLayout = tooltipView.findViewById(R.id.tabelDetailTooltip);

        // Clear existing content
        tableLayout.removeAllViews();

        // Create loading row
        TableRow loadingRow = new TableRow(activity);
        loadingRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        loadingRow.setGravity(Gravity.CENTER);
        loadingRow.setPadding(0, 20, 0, 20);

        // Create container for progress bar and text
        LinearLayout loadingContainer = new LinearLayout(activity);
        loadingContainer.setOrientation(LinearLayout.HORIZONTAL);
        loadingContainer.setGravity(Gravity.CENTER);

        // Add ProgressBar
        ProgressBar progressBar = new ProgressBar(activity);
        LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(48, 48);
        progressParams.setMargins(0, 0, 16, 0);
        progressBar.setLayoutParams(progressParams);

        // Add loading text
        TextView loadingText = new TextView(activity);
        loadingText.setText("Loading table data...");
        loadingText.setTextColor(Color.GRAY);
        loadingText.setTextSize(12);

        loadingContainer.addView(progressBar);
        loadingContainer.addView(loadingText);

        // Create cell that spans all columns
        TableRow.LayoutParams cellParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        cellParams.span = 4; // Span across all 4 columns (Tebal, Lebar, Panjang, Pcs)
        loadingContainer.setLayoutParams(cellParams);

        loadingRow.addView(loadingContainer);
        tableLayout.addView(loadingRow);

        // Set tag untuk referensi nanti
        loadingRow.setTag("loading_row");
    }

    /**
     * Update tooltip dengan data yang sudah di-fetch
     */
    private static void updateTooltipWithData(Activity activity,
                                              PopupWindow popupWindow,
                                              TooltipData tooltipData,
                                              String tableH,
                                              ExecutorService executorService) {

        View tooltipView = popupWindow.getContentView();
        int normalColor = Color.BLACK;

        ((TextView) tooltipView.findViewById(R.id.tvNoLabel)).setText(tooltipData.getNoLabel());
        ((TextView) tooltipView.findViewById(R.id.tvNoLabel)).setTextColor(normalColor);

        ((TextView) tooltipView.findViewById(R.id.tvDateTime)).setText(tooltipData.getFormattedDateTime());
        ((TextView) tooltipView.findViewById(R.id.tvDateTime)).setTextColor(normalColor);

        ((TextView) tooltipView.findViewById(R.id.tvJenis)).setText(tooltipData.getJenis());
        ((TextView) tooltipView.findViewById(R.id.tvJenis)).setTextColor(normalColor);

        ((TextView) tooltipView.findViewById(R.id.tvNoSPK)).setText(tooltipData.getSpkDetail());
        ((TextView) tooltipView.findViewById(R.id.tvNoSPK)).setTextColor(normalColor);

        ((TextView) tooltipView.findViewById(R.id.tvNoSPKAsal)).setText(tooltipData.getSpkAsalDetail());
        ((TextView) tooltipView.findViewById(R.id.tvNoSPKAsal)).setTextColor(normalColor);

        ((TextView) tooltipView.findViewById(R.id.tvNamaGrade)).setText(tooltipData.getNamaGrade());
        ((TextView) tooltipView.findViewById(R.id.tvNamaGrade)).setTextColor(normalColor);

        ((TextView) tooltipView.findViewById(R.id.tvIsLembur)).setText(tooltipData.isLembur() ? "Yes" : "No");
        ((TextView) tooltipView.findViewById(R.id.tvIsLembur)).setTextColor(normalColor);

        ((TextView) tooltipView.findViewById(R.id.tvNoPlat)).setText(tooltipData.getNoPlat());
        ((TextView) tooltipView.findViewById(R.id.tvNoPlat)).setTextColor(normalColor);

        ((TextView) tooltipView.findViewById(R.id.tvNoKBSuket)).setText(tooltipData.getNoKBSuket());
        ((TextView) tooltipView.findViewById(R.id.tvNoKBSuket)).setTextColor(normalColor);

        // Setup data tabel
        setupTooltipTable(activity, tooltipView, tooltipData.getTableData(),
                tooltipData.getTotalPcs(), tooltipData.getTotalM3(),
                tooltipData.getTotalTon(), tableH);

        // Setup visibilitas elemen berdasarkan tableH
        setupVisibilityByTableType(tooltipView, tableH);

    }


    /**
     * Menampilkan error state
     */
    private static void showErrorTooltip(Activity activity, PopupWindow popupWindow, String noLabel) {
        View tooltipView = popupWindow.getContentView();

        // Set error text dengan warna merah
        int errorColor = Color.RED;

        ((TextView) tooltipView.findViewById(R.id.tvNoLabel)).setText("Error loading data");
        ((TextView) tooltipView.findViewById(R.id.tvNoLabel)).setTextColor(errorColor);

        ((TextView) tooltipView.findViewById(R.id.tvDateTime)).setText("Failed to load");
        ((TextView) tooltipView.findViewById(R.id.tvDateTime)).setTextColor(errorColor);

        ((TextView) tooltipView.findViewById(R.id.tvJenis)).setText("-");
        ((TextView) tooltipView.findViewById(R.id.tvJenis)).setTextColor(errorColor);

        ((TextView) tooltipView.findViewById(R.id.tvNoSPK)).setText("-");
        ((TextView) tooltipView.findViewById(R.id.tvNoSPK)).setTextColor(errorColor);

        ((TextView) tooltipView.findViewById(R.id.tvNoSPKAsal)).setText("-");
        ((TextView) tooltipView.findViewById(R.id.tvNoSPKAsal)).setTextColor(errorColor);

        ((TextView) tooltipView.findViewById(R.id.tvNamaGrade)).setText("-");
        ((TextView) tooltipView.findViewById(R.id.tvNamaGrade)).setTextColor(errorColor);

        ((TextView) tooltipView.findViewById(R.id.tvIsLembur)).setText("-");
        ((TextView) tooltipView.findViewById(R.id.tvIsLembur)).setTextColor(errorColor);

        ((TextView) tooltipView.findViewById(R.id.tvNoPlat)).setText("-");
        ((TextView) tooltipView.findViewById(R.id.tvNoPlat)).setTextColor(errorColor);

        ((TextView) tooltipView.findViewById(R.id.tvNoKBSuket)).setText("-");
        ((TextView) tooltipView.findViewById(R.id.tvNoKBSuket)).setTextColor(errorColor);

        // Show error in table area
        showErrorInTable(activity, tooltipView);

    }

    /**
     * Menampilkan error di area tabel
     */
    private static void showErrorInTable(Activity activity, View tooltipView) {
        TableLayout tableLayout = tooltipView.findViewById(R.id.tabelDetailTooltip);

        // Clear existing content
        tableLayout.removeAllViews();

        // Create error row
        TableRow errorRow = new TableRow(activity);
        errorRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        errorRow.setGravity(Gravity.CENTER);
        errorRow.setPadding(0, 20, 0, 20);

        TextView errorText = new TextView(activity);
        errorText.setText("Failed to load table data");
        errorText.setTextColor(Color.RED);
        errorText.setTextSize(12);
        errorText.setGravity(Gravity.CENTER);

        // Create cell that spans all columns
        TableRow.LayoutParams cellParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        cellParams.span = 4;
        errorText.setLayoutParams(cellParams);

        errorRow.addView(errorText);
        tableLayout.addView(errorRow);
    }

    /**
     * Setup table layout untuk tooltip
     */
    private static void setupTooltipTable(Activity activity, View tooltipView,
                                          List<String[]> tableData, int totalPcs,
                                          double totalM3, double totalTon, String tableH) {

        // Referensi TableLayout
        TableLayout tableLayout = tooltipView.findViewById(R.id.tabelDetailTooltip);

        // Clear existing content (termasuk loading indicator)
        tableLayout.removeAllViews();

        // Membuat Header Tabel Secara Dinamis
        createTableHeader(activity, tableLayout);

        // Tambahkan Data ke TableLayout
        addTableData(activity, tableLayout, tableData);

        // Tambahkan Total Rows
        addTotalPcsRow(activity, tableLayout, totalPcs);
        addTotalM3Row(activity, tableLayout, totalM3);

        // Tambahkan Total Ton hanya untuk ST_h
        if (tableH.equals("ST_h")) {
            addTotalTonRow(activity, tableLayout, totalTon);
        }
    }

    /**
     * Membuat header tabel
     */
    private static void createTableHeader(Activity activity, TableLayout tableLayout) {
        TableRow headerRow = new TableRow(activity);
        headerRow.setBackgroundColor(activity.getResources().getColor(R.color.hijau));

        String[] headerTexts = {"Tebal", "Lebar", "Panjang", "Pcs"};
        for (String headerText : headerTexts) {
            TextView headerTextView = new TextView(activity);
            headerTextView.setText(headerText);
            headerTextView.setGravity(Gravity.CENTER);
            headerTextView.setPadding(8, 8, 8, 8);
            headerTextView.setTextColor(Color.WHITE);
            headerTextView.setTypeface(Typeface.DEFAULT_BOLD);
            headerRow.addView(headerTextView);
        }

        tableLayout.addView(headerRow);
    }

    /**
     * Menambahkan data ke tabel
     */
    private static void addTableData(Activity activity, TableLayout tableLayout, List<String[]> tableData) {
        for (String[] row : tableData) {
            TableRow tableRow = new TableRow(activity);
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            tableRow.setBackgroundColor(activity.getResources().getColor(R.color.background_cream));

            for (String cell : row) {
                TextView textView = new TextView(activity);
                textView.setText(cell);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(8, 8, 8, 8);
                textView.setTextColor(Color.BLACK);
                tableRow.addView(textView);
            }
            tableLayout.addView(tableRow);
        }
    }

    /**
     * Menambahkan baris total Pcs
     */
    private static void addTotalPcsRow(Activity activity, TableLayout tableLayout, int totalPcs) {
        TableRow totalRow = new TableRow(activity);
        totalRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        totalRow.setBackgroundColor(Color.WHITE);

        // Cell kosong untuk memisahkan total dengan tabel
        for (int i = 0; i < 2; i++) {
            TextView emptyCell = new TextView(activity);
            emptyCell.setText("");
            totalRow.addView(emptyCell);
        }

        TextView totalLabel = new TextView(activity);
        totalLabel.setText("Total :");
        totalLabel.setGravity(Gravity.END);
        totalLabel.setPadding(8, 8, 8, 8);
        totalLabel.setTypeface(Typeface.DEFAULT_BOLD);
        totalRow.addView(totalLabel);

        TextView totalValue = new TextView(activity);
        totalValue.setText(String.valueOf(totalPcs));
        totalValue.setGravity(Gravity.CENTER);
        totalValue.setPadding(8, 8, 8, 8);
        totalValue.setTypeface(Typeface.DEFAULT_BOLD);
        totalRow.addView(totalValue);

        tableLayout.addView(totalRow);
    }

    /**
     * Menambahkan baris total M3
     */
    private static void addTotalM3Row(Activity activity, TableLayout tableLayout, double totalM3) {
        TableRow m3Row = new TableRow(activity);
        m3Row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        m3Row.setBackgroundColor(Color.WHITE);

        // Cell kosong
        for (int i = 0; i < 2; i++) {
            TextView emptyCell = new TextView(activity);
            emptyCell.setText("");
            m3Row.addView(emptyCell);
        }

        TextView m3Label = new TextView(activity);
        m3Label.setText("M3 :");
        m3Label.setGravity(Gravity.END);
        m3Label.setPadding(8, 8, 8, 8);
        m3Label.setTypeface(Typeface.DEFAULT_BOLD);
        m3Row.addView(m3Label);

        DecimalFormat df = new DecimalFormat("0.0000");
        TextView m3Value = new TextView(activity);
        m3Value.setText(df.format(totalM3));
        m3Value.setGravity(Gravity.CENTER);
        m3Value.setPadding(8, 8, 8, 8);
        m3Value.setTypeface(Typeface.DEFAULT_BOLD);
        m3Row.addView(m3Value);

        tableLayout.addView(m3Row);
    }

    /**
     * Menambahkan baris total Ton
     */
    private static void addTotalTonRow(Activity activity, TableLayout tableLayout, double totalTon) {
        TableRow tonRow = new TableRow(activity);
        tonRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        tonRow.setBackgroundColor(Color.WHITE);

        // Cell kosong
        for (int i = 0; i < 2; i++) {
            TextView emptyCell = new TextView(activity);
            emptyCell.setText("");
            tonRow.addView(emptyCell);
        }

        TextView tonLabel = new TextView(activity);
        tonLabel.setText("Ton :");
        tonLabel.setGravity(Gravity.END);
        tonLabel.setPadding(8, 8, 8, 8);
        tonLabel.setTypeface(Typeface.DEFAULT_BOLD);
        tonRow.addView(tonLabel);

        DecimalFormat df = new DecimalFormat("0.0000");
        TextView tonValue = new TextView(activity);
        tonValue.setText(df.format(totalTon));
        tonValue.setGravity(Gravity.CENTER);
        tonValue.setPadding(8, 8, 8, 8);
        tonValue.setTypeface(Typeface.DEFAULT_BOLD);
        tonRow.addView(tonValue);

        tableLayout.addView(tonRow);
    }

    /**
     * Setup visibility berdasarkan tipe tabel
     */
    private static void setupVisibilityByTableType(View tooltipView, String tableH) {
        if (tableH.equals("ST_h")) {
            tooltipView.findViewById(R.id.fieldNoSPKAsal).setVisibility(View.GONE);
            tooltipView.findViewById(R.id.fieldGrade).setVisibility(View.GONE);
            // Show fields yang khusus untuk ST_h
            tooltipView.findViewById(R.id.fieldPlatTruk).setVisibility(View.VISIBLE);
        } else {
            tooltipView.findViewById(R.id.fieldNoSPKAsal).setVisibility(View.VISIBLE);
            tooltipView.findViewById(R.id.fieldGrade).setVisibility(View.VISIBLE);
            // Hide fields yang khusus untuk ST_h
            tooltipView.findViewById(R.id.fieldPlatTruk).setVisibility(View.GONE);
        }
    }

    /**
     * Membuat dan menampilkan PopupWindow
     */
    private static PopupWindow createAndShowPopupWindow(Activity activity, View anchorView, View tooltipView) {
        // Buat PopupWindow
        PopupWindow popupWindow = new PopupWindow(
                tooltipView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(false);

        // Ukur ukuran tooltip sebelum menampilkannya
        tooltipView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int tooltipWidth = tooltipView.getMeasuredWidth();
        int tooltipHeight = tooltipView.getMeasuredHeight();

        // Dapatkan posisi anchorView
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);

        // Hitung posisi tooltip
        int x = location[0] - tooltipWidth;
        int y = location[1] + (anchorView.getHeight() / 2) - (tooltipHeight / 2);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;
        ImageView trianglePointer = tooltipView.findViewById(R.id.trianglePointer);

        // Menaikkan pointer ketika popup melebihi batas layout
        if (trianglePointer != null) {
            if (y < 60) {
                trianglePointer.setY(y - 60);
            } else if (y > (screenHeight - tooltipHeight)) {
                trianglePointer.setY(y - (screenHeight - tooltipHeight));
            }
        }

        // Tampilkan tooltip
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);

        return popupWindow;
    }
}