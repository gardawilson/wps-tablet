package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.myapplication.api.ProductionApi;
import com.example.myapplication.model.ProductionData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProsesProduksiS4S extends AppCompatActivity {

    private TableLayout tableLayout;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_produksi_s4_s);

        tableLayout = findViewById(R.id.tableLayout);

        executorService.execute(() -> {
            List<ProductionData> productionDataList = ProductionApi.getProductionData();

            runOnUiThread(() -> {
                populateTable(productionDataList);
            });
        });
    }

    private void populateTable(List<ProductionData> dataList) {
        Log.d("PopulateTable", "Start");

        if (dataList == null || dataList.isEmpty()) {
            Log.w("PopulateTable", "Data list is empty or null");
            // Tampilkan pesan "Data tidak ditemukan" jika data kosong
            TextView noDataView = new TextView(this);
            noDataView.setText("Data tidak ditemukan");
            noDataView.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            tableLayout.addView(noDataView);
            return;
        }

        // Tambahkan header
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(ContextCompat.getColor(this, R.color.hijau));

        TextView headerNoProd = createTextView("No. Produksi", true);
        TextView headerShift = createTextView("Shift", true);
        TextView headerTanggal = createTextView("Tanggal", true);
        TextView headerMesin = createTextView("Mesin", true);
        TextView headerOperator = createTextView("Operator", true);

        headerRow.addView(headerNoProd);
        headerRow.addView(headerShift);
        headerRow.addView(headerTanggal);
        headerRow.addView(headerMesin);
        headerRow.addView(headerOperator);

        tableLayout.addView(headerRow);

        for (int i = 0; i < dataList.size(); i++) {
            ProductionData data = dataList.get(i);
            Log.d("PopulateTable", "Adding row for NoProduksi: " + data.getNoProduksi());

            CardView cardView = new CardView(this);
            cardView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            int margin = getResources().getDimensionPixelSize(R.dimen.margin_small);
            cardView.setContentPadding(margin, margin, margin, margin);
            cardView.setCardElevation(4);
            if (i % 2 == 0) {
                cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
            } else {
                cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            }

            // Tambahkan OnClickListener ke CardView
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int rowIndex = tableLayout.indexOfChild(v);
                    ProductionData clickedData = dataList.get(rowIndex - 1); // Kurangi 1 karena ada header

                    Log.d("Row Click", "Clicked row: " + rowIndex +
                            ", NoProduksi: " + clickedData.getNoProduksi());
                }
            });

            TableRow row = new TableRow(this);

            TextView noProdView = createTextView(data.getNoProduksi(), false);
            TextView shiftView = createTextView(data.getShift(), false);
            TextView tanggalView = createTextView(data.getTanggal(), false);
            TextView mesinView = createTextView(data.getMesin(), false);
            TextView operatorView = createTextView(data.getOperator(), false);

            noProdView.setGravity(Gravity.CENTER);
            shiftView.setGravity(Gravity.CENTER);
            tanggalView.setGravity(Gravity.CENTER);
            mesinView.setGravity(Gravity.CENTER);
            operatorView.setGravity(Gravity.CENTER);


            row.addView(noProdView);
            row.addView(shiftView);
            row.addView(tanggalView);
            row.addView(mesinView);
            row.addView(operatorView);

            cardView.addView(row);

            tableLayout.addView(cardView);
        }

        Log.d("PopulateTable", "End");
    }

    // Helper method untuk membuat TextView dengan opsi header
    private TextView createTextView(String text, boolean isHeader) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        textView.setPadding(16, 16, 16, 16);
        if (isHeader) {
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
        }
        return textView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}