package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.example.myapplication.api.SpkApi;
import com.example.myapplication.model.CustomerData;
import com.example.myapplication.model.MstSPKData;
import com.example.myapplication.model.MstSPKData;
import com.example.myapplication.model.MstSPKData;
import com.example.myapplication.model.SawmillData;
import com.example.myapplication.model.TooltipData;
import com.example.myapplication.utils.DateTimeUtils;
import com.example.myapplication.utils.TableUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class SPK extends AppCompatActivity {

    private TableLayout mainTable;
    private TableRow selectedRow;
    private MstSPKData selectedSPK;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String noSPK;
    private List<MstSPKData> dataList; // Data asli yang tidak difilter
    // di class Activity kamu
    private float touchX = 0f, touchY = 0f;
    private PopupWindow currentPopup; // simpan biar bisa dismiss saat pindah
    private List<String> userPermissions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spk);

        mainTable = findViewById(R.id.mainTable);

        loadDataAndDisplayTable();

    }

    private void loadDataAndDisplayTable() {
        // Menjalankan operasi di background thread
        executorService.execute(() -> {
            dataList = SpkApi.getMstSPKData();

            // Menampilkan data di UI thread setelah selesai mengambil data
            runOnUiThread(() -> {
                populateTable(dataList);  // Memperbarui tampilan tabel dengan data terbaru
                // Sembunyikan loading indicator jika ada
                // loadingIndicator.setVisibility(View.GONE);
            });
        });
    }


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

            // zebra background
            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }

            // Klik biasa (pakai milikmu)
            row.setOnClickListener(v -> {
                if (selectedRow != null && selectedRow != row) {
                    int prevIndex = mainTable.indexOfChild(selectedRow);
                    // kembalikan zebra row sebelumnya
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
                onRowClick(data);
            });

            // Tangkap koordinat sentuhan
            row.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchX = event.getRawX();
                    touchY = event.getRawY();
                }
                return false; // biarkan event lanjut ke click/longClick
            });

            // Long click → tampilkan popup di koordinat sentuhan
            row.setOnLongClickListener(v -> {
                // highlight row saat long-press
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

                // tampilkan popup
                showRowPopup(v, data, touchX, touchY);
                return true;
            });

            mainTable.addView(row);
            rowIndex++;
        }
    }



    private void showRowPopup(View anchorView, MstSPKData data, float x, float y) {
        // Inflate popup layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_menu_row_spk, null);

        // Buat popup window
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Atur background agar bisa dismiss ketika klik di luar
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setElevation(16f);

        // Tombol pada popup
        Button btnActivateDeactivate = popupView.findViewById(R.id.btnActivateDeactivate);

        // Set label awal sesuai status
        updateToggleButtonText(btnActivateDeactivate, data.isEnabled());

        // Klik toggle
        btnActivateDeactivate.setOnClickListener(v -> {
            popupWindow.dismiss();

            final int target = data.isEnabled() ? 0 : 1;
            final String actionText = (target == 1) ? "Aktifkan" : "Nonaktifkan";

            new AlertDialog.Builder(this)
                    .setTitle(actionText + " SPK")
                    .setMessage(actionText + " SPK " + data.getNoSPK() + "?")
                    .setPositiveButton("Ya", (d, w) -> {
                        executorService.execute(() -> {
                            boolean ok = SpkApi.setMstSPKEnable(data.getNoSPK(), target); // <-- hanya kolom Enable
                            runOnUiThread(() -> {
                                if (!ok) {
                                    Toast.makeText(this, "Gagal memperbarui status.", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                // Update model & UI baris
                                data.setEnable(target);
                                if (anchorView instanceof TableRow) {
                                    TableRow row = (TableRow) anchorView;
                                    // col6 ada di index 10 (6 kolom + 5 divider)
                                    TextView col6 = (TextView) row.getChildAt(10);
                                    col6.setText(data.getEnableLabel()); // "AKTIF"/"NONAKTIF"
                                }
                                Toast.makeText(this, "Status: " + data.getEnableLabel(), Toast.LENGTH_SHORT).show();
                            });
                        });
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });


        // Ukur ukuran popup
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth  = popupView.getMeasuredWidth();
        int popupHeight = popupView.getMeasuredHeight();

        // Dapatkan ukuran layar
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth  = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        // Hitung posisi popup (di atas titik sentuh)
        int popupX = (int) x - (popupWidth / 2);
        int popupY = (int) y - popupHeight - 50;

        // Clamp agar tidak keluar layar
        if (popupX < 10) popupX = 10;
        if (popupX + popupWidth > screenWidth - 10) popupX = screenWidth - popupWidth - 10;
        if (popupY < 10) popupY = (int) y + 50; // kalau tidak muat di atas, tampilkan di bawah

        // Tampilkan popup di koordinat yang dihitung
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, popupX, popupY);
    }

    private void updateToggleButtonText(Button btn, boolean enabled) {
        btn.setText(enabled ? "Nonaktifkan" : "Aktifkan");
        // opsional ubah ikon:
         btn.setCompoundDrawablesWithIntrinsicBounds(
             enabled ? R.drawable.ic_undone : R.drawable.ic_done_all, 0, 0, 0);
    }







    private void onRowClick(MstSPKData data) {

        executorService.execute(() -> {
            // Ambil data latar belakang

            // Perbarui UI di thread utama
            runOnUiThread(() -> {

            });
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}