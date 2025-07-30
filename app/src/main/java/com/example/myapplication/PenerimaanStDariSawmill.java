package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.api.ProsesProduksiApi;
import com.example.myapplication.api.SawmillApi;
import com.example.myapplication.model.CustomerData;
import com.example.myapplication.model.PenerimaanSTSawmillData;
import com.example.myapplication.model.PenerimaanSTSawmillData;
import com.example.myapplication.model.TooltipData;
import com.example.myapplication.utils.DateTimeUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class PenerimaanStDariSawmill extends AppCompatActivity {

    private TableLayout mainTable;
    private TableLayout tableNoSTList;
    private TableRow selectedRow;
    private PenerimaanSTSawmillData selectedSTUpahData;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String noPenerimaanST;
    private String tglLaporan;
    private TextView noProduksiView;
    private List<PenerimaanSTSawmillData> dataList; // Data asli yang tidak difilter
    private List<String> noSTList = new ArrayList<>();
    private Button btnDataBaru;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penerimaan_st_dari_sawmill);

        mainTable = findViewById(R.id.mainTable);
        tableNoSTList = findViewById(R.id.tableNoSTList);
        btnDataBaru = findViewById(R.id.btnDataBaru);

        loadDataAndDisplayTable();

    }

    private void loadDataAndDisplayTable() {
        // Menjalankan operasi di background thread
        executorService.execute(() -> {
            dataList = SawmillApi.getPenerimaanSTSawmillData();

            // Menampilkan data di UI thread setelah selesai mengambil data
            runOnUiThread(() -> {
                populateTable(dataList);  // Memperbarui tampilan tabel dengan data terbaru
                // Sembunyikan loading indicator jika ada
                // loadingIndicator.setVisibility(View.GONE);
            });
        });
    }


    private void populateTable(List<PenerimaanSTSawmillData> dataList) {

        mainTable.removeAllViews();

        if (dataList == null || dataList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Data tidak ditemukan");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            mainTable.addView(noDataView);
            return;
        }

        int rowIndex = 0; // Untuk melacak indeks baris

        for (PenerimaanSTSawmillData data : dataList) {
            TableRow row = new TableRow(this);

            // Simpan indeks baris di tag
            row.setTag(rowIndex);

            // Tambahkan TextView untuk setiap kolom
            TextView col1 = createTextView(data.getNoPenerimaanST(), 1.0f);
            TextView col2 = createTextView(data.getTglLaporan(), 1.0f);
            TextView col3 = createTextView(data.getNoKayuBulat(), 1.0f);

            setDateToView(data.getTglLaporan(), col2);

            row.addView(col1);
            row.addView(createDivider());

            row.addView(col2);
            row.addView(createDivider());

            row.addView(col3);
            row.addView(createDivider());


            // Tetapkan warna latar belakang berdasarkan indeks baris
            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream)); // Warna untuk baris genap
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white)); // Warna untuk baris ganjil
            }

            row.setOnClickListener(v -> {
                // Reset warna baris sebelumnya (jika ada)
                if (selectedRow != null) {
                    int previousRowIndex = (int) selectedRow.getTag();
                    if (previousRowIndex % 2 == 0) {
                        selectedRow.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
                    } else {
                        selectedRow.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                    }
                    resetTextColor(selectedRow); // Kembalikan warna teks ke hitam
                }

                // Tandai baris yang baru dipilih
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.primary)); // Warna penandaan
                setTextColor(row, R.color.white); // Ubah warna teks menjadi putih
                selectedRow = row;

                // Simpan data yang dipilih
                selectedSTUpahData = data;

                // Tangani aksi tambahan
                onRowClick(data);
            });


            mainTable.addView(row);
            rowIndex++; // Tingkatkan indeks
        }
    }

    private void populateNoSTList(List<String> noSTList) {
        tableNoSTList.removeAllViews();

        int rowIndex = 0;

        if (noSTList == null || noSTList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Tidak ada Data");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            tableNoSTList.addView(noDataView);
            return;
        }

        // Isi tabel
        for (String noSTSawmill : noSTList) {
            TableRow row = new TableRow(this);

            row.setTag(rowIndex);

            // Tambahkan TextView ke baris tabel
            TextView textView = createTextView(noSTSawmill, 1.0f);
            row.addView(textView);

            // Tambahkan OnClickListener untuk menampilkan tooltip
//            row.setOnClickListener(view -> fetchDataAndShowTooltip(view, noSTSawmill, "ST_h", "ST_d", "NoST"));

            // Tetapkan warna latar belakang berdasarkan indeks baris
            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream)); // Warna untuk baris genap
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white)); // Warna untuk baris ganjil
            }

            // Tambahkan baris ke TableLayout
            tableNoSTList.addView(row);
            rowIndex++;
        }
    }


    private void onRowClick(PenerimaanSTSawmillData data) {

        executorService.execute(() -> {
            // Ambil data latar belakang
            noPenerimaanST = data.getNoPenerimaanST();

            noSTList = SawmillApi.getSTSawmillListByNoPenerimaanST(noPenerimaanST);

            // Perbarui UI di thread utama
            runOnUiThread(() -> {
                populateNoSTList(noSTList);

            });
        });
    }

    // Tambahkan metode untuk membuat garis pembatas
    private View createDivider() {
        View divider = new View(this);
        divider.setBackgroundColor(Color.GRAY); // Warna garis pemisah

        // Set parameter untuk garis tipis (0.5dp)
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                1,
                TableRow.LayoutParams.MATCH_PARENT // Tinggi penuh
        );
        divider.setLayoutParams(params);

        return divider;
    }

    private TextView createTextView(String text, float weight) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 15, 8, 15); // Padding untuk jarak
        textView.setGravity(Gravity.CENTER); // Pusatkan teks di tengah

        // Atur LayoutParams untuk mengatur lebar kolom berdasarkan weight
        textView.setLayoutParams(new TableRow.LayoutParams(
                0, // Lebar proporsional (diatur oleh weight)
                TableRow.LayoutParams.WRAP_CONTENT, // Tinggi mengikuti konten
                weight // Berat untuk membagi lebar
        ));

        return textView;
    }

    public void setDateToView(String tglProduksi, TextView tglProduksiView) {
        // Gunakan metode dari DateTimeUtils untuk memformat tanggal
        String formattedDate = DateTimeUtils.formatDate(tglProduksi);

        // Set tanggal terformat ke TextView
        tglProduksiView.setText(formattedDate);
    }

    private void setTextColor(TableRow row, int colorResId) {
        for (int i = 0; i < row.getChildCount(); i++) {
            View child = row.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(ContextCompat.getColor(this, colorResId));
            }
        }
    }

    private void resetTextColor(TableRow row) {
        for (int i = 0; i < row.getChildCount(); i++) {
            View child = row.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(ContextCompat.getColor(this, R.color.black)); // Kembalikan ke hitam
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}