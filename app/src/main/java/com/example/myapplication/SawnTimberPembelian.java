package com.example.myapplication;

import static com.example.myapplication.api.LabelApi.insertDataRejectPembelian;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.myapplication.api.LabelApi;
import com.example.myapplication.api.ProductionApi;
import com.example.myapplication.model.STPembelianData;
import com.example.myapplication.model.STPembelianDataReject;
import com.example.myapplication.model.SupplierData;
import com.example.myapplication.model.TooltipData;
import com.example.myapplication.utils.DateTimeUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class SawnTimberPembelian extends AppCompatActivity {

    private TableLayout mainTable;
    private TableLayout nonRejectTableLayout;
    private TableLayout rejectTableLayout;
    private TableRow selectedRow;
    private STPembelianData selectedSTPembelianData;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String noSTPembelian;
    private String tglLaporan;
    private TextView noProduksiView;
    private List<STPembelianData> dataList; // Data asli yang tidak difilter
    private List<String> nonRejectList = new ArrayList<>();
    private ImageButton btnAddLabelSTBeli;
    private ImageButton btnAddReject;
    private Button btnDataBaru;
    private int countReject;
    private LinearLayout btnSwapToReject;
    private LinearLayout btnSwapToNonReject;
    private LinearLayout nonRejectLayoutView;
    private LinearLayout rejectLayoutView;
    private List<STPembelianDataReject> rejectDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sawn_timber_pembelian);

        mainTable = findViewById(R.id.mainTable);
        nonRejectTableLayout = findViewById(R.id.nonRejectTableLayout);
        rejectTableLayout = findViewById(R.id.rejectTableLayout);
        btnAddLabelSTBeli = findViewById(R.id.btnAddLabelSTBeli);
        btnAddReject = findViewById(R.id.btnAddReject);
        btnDataBaru = findViewById(R.id.btnDataBaru);
        btnSwapToReject = findViewById(R.id.btnSwapToReject);
        btnSwapToNonReject = findViewById(R.id.btnSwapToNonReject);
        nonRejectLayoutView = findViewById(R.id.nonRejectLayoutView);
        rejectLayoutView = findViewById(R.id.rejectLayoutView);

        loadDataAndDisplayTable();
        rejectLayoutView.setVisibility(View.GONE);

        // Menambahkan OnClickListener
        btnDataBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewDataDialog();

            }
        });

        btnAddLabelSTBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (noSTPembelian == null || noSTPembelian.isEmpty()) {
                    Toast.makeText(SawnTimberPembelian.this, "Pilih No. Penerimaan ST!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent( SawnTimberPembelian.this, SawnTimber.class);
                    intent.putExtra("noPenST", noSTPembelian); // Mengirim integer
                    intent.putExtra("labelVersion", 1); // Mengirim string
                    startActivity(intent);
                }
            }
        });

        btnAddReject.setOnClickListener(v -> {

            if (noSTPembelian == null || noSTPembelian.isEmpty()) {
                Toast.makeText(SawnTimberPembelian.this, "Pilih No. Penerimaan ST!", Toast.LENGTH_SHORT).show();
            } else {
                showAddRejectDialog(noSTPembelian);
            }
        });

        btnSwapToReject.setOnClickListener(v -> {
            rejectLayoutView.setVisibility(View.VISIBLE);
            nonRejectLayoutView.setVisibility(View.GONE);
        });

        btnSwapToNonReject.setOnClickListener(v -> {
            rejectLayoutView.setVisibility(View.GONE);
            nonRejectLayoutView.setVisibility(View.VISIBLE);
        });
    }

    // Mengambil data tooltip dan menampilkan tooltip
    private void fetchDataAndShowTooltip(View anchorView, String noLabel, String tableH, String tableD, String mainColumn) {
        executorService.execute(() -> {
            // Ambil data tooltip menggunakan ProductionApi
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
                                tableH
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


    private void showTooltip(View anchorView, String noLabel, String formattedDateTime, String jenis, String spkDetail, String spkAsalDetail, String namaGrade, boolean isLembur, List<String[]> tableData, int totalPcs, double totalM3, double totalTon, String noPlat, String noKBSuket, String tableH) {
        // Inflate layout tooltip
        View tooltipView = LayoutInflater.from(this).inflate(R.layout.tooltip_layout, null);

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
        int x = location[0] - tooltipWidth;
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

    private void showAddRejectDialog(String noSTPembelian) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SawnTimberPembelian.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_reject_pembelian_st, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText inputTebal = dialogView.findViewById(R.id.inputTebal);
        EditText inputLebar = dialogView.findViewById(R.id.inputLebar);
        EditText inputPanjang = dialogView.findViewById(R.id.inputPanjang);
        EditText inputPcs = dialogView.findViewById(R.id.inputPcs);
        Button btnSubmit = dialogView.findViewById(R.id.btnInputReject);
        RadioButton radioMillimeter = dialogView.findViewById(R.id.radioMillimeter);

        btnSubmit.setOnClickListener(view -> {
            String tebal = inputTebal.getText().toString().trim();
            String lebar = inputLebar.getText().toString().trim();
            String panjang = inputPanjang.getText().toString().trim();
            String pcs = inputPcs.getText().toString().trim();
            int count = countReject + 1;

            if (tebal.isEmpty() || lebar.isEmpty() || panjang.isEmpty() || pcs.isEmpty()) {
                Toast.makeText(SawnTimberPembelian.this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            int idUOMTblLebar = radioMillimeter.isChecked() ? 1 : 3;
            int idUOMPanjang = 4;

            double tebalVal = Double.parseDouble(tebal);
            double lebarVal = Double.parseDouble(lebar);
            double panjangVal = Double.parseDouble(panjang);
            int pcsVal = Integer.parseInt(pcs);

            double tonVal = hitungTon(tebalVal, lebarVal, panjangVal, pcsVal, idUOMTblLebar);

            executorService.execute(() -> {
                insertDataRejectPembelian(noSTPembelian, count, tebalVal, lebarVal, panjangVal, pcsVal, idUOMTblLebar, idUOMPanjang, tonVal);
                rejectDataList = LabelApi.fetchRejectDataByNoPenerimaanST(noSTPembelian);

                runOnUiThread(() -> {
                    populateRejectList(rejectDataList);
                    Toast.makeText(SawnTimberPembelian.this, "Reject berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            });
        });

        dialog.show();
    }


    private double hitungTon(double tebal, double lebar, double panjang, int pcs, int idUOMTblLebar) {
        double rowTON;

        if (idUOMTblLebar == 1) {
            rowTON = ((tebal * lebar * panjang * pcs * 304.8 / 1000000000 / 1.416 * 10000) / 10000);
            rowTON = Math.floor(rowTON * 10000) / 10000;
        } else {
            rowTON = ((tebal * lebar * panjang * pcs / 7200.8 * 10000) / 10000);
            rowTON = Math.floor(rowTON * 10000) / 10000;
        }

        return rowTON;
    }


    private void loadDataAndDisplayTable() {
        // Menjalankan operasi di background thread
        executorService.execute(() -> {
            dataList = LabelApi.getSTPembelianData();
            nonRejectList = LabelApi.getNonRejectListByNoPenST(noSTPembelian);

            // Menampilkan data di UI thread setelah selesai mengambil data
            runOnUiThread(() -> {
                populateTable(dataList);
                populateNonRejectList(nonRejectList);
                // Memperbarui tampilan tabel dengan data terbaru
                // Sembunyikan loading indicator jika ada
                // loadingIndicator.setVisibility(View.GONE);
            });
        });
    }


    // Di dalam Activity atau Fragment Anda
    private void showNewDataDialog() {
        // Membuat Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(SawnTimberPembelian.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_insert_data_st_pembelian, null);
        builder.setView(view);

        // Menyiapkan EditText dan Spinner
        final EditText tglLaporan = view.findViewById(R.id.editTglLaporan);
        final EditText tglMasuk = view.findViewById(R.id.editTglMasuk);
        final Spinner spinIdSupplier = view.findViewById(R.id.spinIdSupplier);
        final EditText noTruk = view.findViewById(R.id.editNoTruk);
        final EditText noPlat = view.findViewById(R.id.editNoPlat);
        final EditText suket = view.findViewById(R.id.editSuket);
        final EditText tonSJ = view.findViewById(R.id.editTonSJ);
        final EditText note = view.findViewById(R.id.editNote);

        // Mengambil tanggal hari ini
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Format tanggal
        String date = day + "/" + (month + 1) + "/" + year;  // Format: dd/MM/yyyy

        // Menetapkan tanggal hari ini ke tglLaporan dan tglMasuk
        tglLaporan.setText(date);
        tglMasuk.setText(date);

        // Deklarasikan objek dialog di luar listener
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        // Memanggil metode untuk memuat data ke Spinner
        loadSupplierDataToSpinner(spinIdSupplier);

        // Set OnClickListener untuk tglLaporan (datepicker)
        tglLaporan.setOnClickListener(v -> showDatePickerDialog(tglLaporan));

        // Set OnClickListener untuk tglMasuk (datepicker)
        tglMasuk.setOnClickListener(v -> showDatePickerDialog(tglMasuk));

        Log.d("SpinnerValue", "Selected Supplier ID: " + spinIdSupplier);

        // Menambahkan onClickListener untuk tombol tutup dialog
        ImageButton btnCloseDialogInputNoPST = view.findViewById(R.id.btnCloseDialogInputNoPST);
        btnCloseDialogInputNoPST.setOnClickListener(v -> {
            dialog.dismiss();  // Menutup DialogFragment
        });

        // Tombol Simpan
        Button btnSave = view.findViewById(R.id.btnSimpan);
        btnSave.setOnClickListener(v -> {
            // Ambil data dari EditText dan Spinner
            String tglLaporanVal = tglLaporan.getText().toString();
            String tglMasukVal = tglMasuk.getText().toString();
            SupplierData selectedSupplier = (SupplierData) spinIdSupplier.getSelectedItem();
            // Pastikan selectedSupplier tidak null dan idSupplier valid
            final int idSupplierVal = (selectedSupplier != null && selectedSupplier.getIdSupplier() != null) ? selectedSupplier.getIdSupplier() : -1;
            String noTrukVal = noTruk.getText().toString();
            String noPlatVal = noPlat.getText().toString();
            String suketVal = suket.getText().toString();
            String noteVal = note.getText().toString();
            String tonSJVal = tonSJ.getText().toString();

            // Validasi: Cek apakah ada field yang kosong
            boolean valid = true;

            // Validasi Spinner untuk memastikan item yang dipilih bukan "PILIH"
            if (idSupplierVal == -1) {  // Jika supplier yang dipilih adalah "PILIH"
                // Menambahkan error pada spinner dan memberi pesan kesalahan
                spinIdSupplier.setBackgroundResource(R.drawable.spinner_error);  // Background merah untuk error
                valid = false;
            }

            if (noTrukVal.isEmpty()) {
                noTruk.setError("Nomor Truk harus diisi");
                valid = false;
            }
            if (noPlatVal.isEmpty()) {
                noPlat.setError("Nomor Plat harus diisi");
                valid = false;
            }
            if (suketVal.isEmpty()) {
                suket.setError("Suket harus diisi");
                valid = false;
            }
            if (tonSJVal.isEmpty()) {
                tonSJ.setError("Ton SJ harus diisi");
                valid = false;
            }

            // Jika semua field valid, lanjutkan proses
            if (valid) {
                // Menjalankan ExecutorService untuk mengambil nomor penerimaan baru dan menyimpan data
                executorService.execute(() -> {
                    // Ambil NoPenerimaanST baru dari database ketika tombol simpan diklik
                    String newNoPenerimaanST = LabelApi.getNextNoPenerimaanST();

                    // Periksa apakah NoPenerimaanST berhasil diambil
                    if (newNoPenerimaanST != null && !newNoPenerimaanST.isEmpty()) {
                        try {
                            LabelApi.insertDataToPembelian(newNoPenerimaanST, tglLaporanVal, tglMasukVal, idSupplierVal, noTrukVal, noPlatVal, suketVal, tonSJVal, noteVal);

                            // Setelah operasi selesai, update UI di main thread
                            runOnUiThread(() -> {
                                // Menampilkan dialog sukses
                                new MaterialAlertDialogBuilder(this)
                                        .setTitle("Berhasil!")
                                        .setMessage("Data " + newNoPenerimaanST + " berhasil disimpan")
                                        .setPositiveButton("OK", (dialogSuccess, which) -> {
                                            // Ketika OK di-click, refresh tabel dan UI lainnya
                                            loadDataAndDisplayTable();
                                            dialog.dismiss();  // Menutup dialog setelah OK diklik

                                        })
                                        .setIcon(R.drawable.ic_done)
                                        .show();
                            });

                        } catch (Exception e) {
                            // Jika terjadi error, beri tahu pengguna di main thread
                            runOnUiThread(() -> {
                                Toast.makeText(SawnTimberPembelian.this, "Terjadi kesalahan saat menyimpan data", Toast.LENGTH_SHORT).show();
                            });
                        }

                    } else {
                        // Tangani kasus jika NoPenerimaanST gagal didapatkan
                        runOnUiThread(() -> {
                            Toast.makeText(SawnTimberPembelian.this, "Gagal mengambil NoPenerimaanST", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();  // Menutup dialog jika terjadi error
                        });
                    }
                });
            } else {
                Toast.makeText(SawnTimberPembelian.this, "Data belum lengkap!", Toast.LENGTH_SHORT).show();
            }
        });

        // Menampilkan Dialog
        dialog.show();
    }


    private void showDatePickerDialog(final EditText dateEditText) {
        // Mendapatkan tanggal hari ini
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Membuat DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(SawnTimberPembelian.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Menampilkan tanggal yang dipilih pada EditText
                        dateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);

        // Menampilkan DatePickerDialog
        datePickerDialog.show();
    }


    private void loadSupplierDataToSpinner(final Spinner spinIdSupplier) {
        // List untuk menyimpan data Supplier
        final List<SupplierData> supplierList = new ArrayList<>();

        // Menambahkan pilihan default ("Silakan Pilih") dengan id null
        supplierList.add(new SupplierData(null, "PILIH"));  // Tambahkan pilihan default

        // Mengambil data supplier dari database menggunakan ExecutorService
        executorService.execute(() -> {
            // Ambil data supplier dari LabelApi
            List<SupplierData> suppliers = LabelApi.getSupplierList();
            supplierList.addAll(suppliers);  // Menambahkan supplier yang diambil dari database

            // Perbarui Spinner di UI thread
            runOnUiThread(() -> {
                // Membuat ArrayAdapter untuk Spinner
                ArrayAdapter<SupplierData> adapter = new ArrayAdapter<>(SawnTimberPembelian.this, android.R.layout.simple_spinner_item, supplierList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinIdSupplier.setAdapter(adapter);
                spinIdSupplier.setSelection(0);  // Menampilkan pilihan default pertama ("PILIH")

            });
        });

        spinIdSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SupplierData selectedSupplier = (SupplierData) spinIdSupplier.getSelectedItem();

                // Periksa apakah selectedSupplier tidak null dan memiliki idSupplier yang valid
                int idSupplierVal = -1;
                if (selectedSupplier != null && selectedSupplier.getIdSupplier() != null) {
                    idSupplierVal = selectedSupplier.getIdSupplier();
                }

                // Jika item yang dipilih adalah "PILIH", beri error
                if (idSupplierVal != -1) {
                    spinIdSupplier.setBackgroundResource(R.drawable.border_input); // Ganti dengan background normal

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle when nothing is selected (optional)
            }
        });
    }


    private void populateTable(List<STPembelianData> dataList) {

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

        for (STPembelianData data : dataList) {
            TableRow row = new TableRow(this);

            // Simpan indeks baris di tag
            row.setTag(rowIndex);

            // Tambahkan TextView untuk setiap kolom
            TextView col1 = createTextView(data.getNoSTPembelian(), 1.0f);
            TextView col2 = createTextView(data.getTglLaporan(), 1.0f);
            TextView col3 = createTextView(data.getTglMasuk(), 1.0f);
            TextView col4 = createTextView(data.getSupplier(), 1.0f);
            TextView col5 = createTextView(data.getNoTruk(), 1.0f);
            TextView col6 = createTextView(data.getNoPlat(), 1.0f);
            TextView col7 = createTextView(data.getTonSJ(), 2.0f);
//            TextView col8 = createTextView(data.getKeteranganPembelian(), 1.0f);

            setDateToView(data.getTglLaporan(), col2);
            setDateToView(data.getTglMasuk(), col3);

            row.addView(col1);
            row.addView(createDivider());

            row.addView(col2);
            row.addView(createDivider());

            row.addView(col3);
            row.addView(createDivider());

            row.addView(col4);
            row.addView(createDivider());

            row.addView(col5);
            row.addView(createDivider());

            row.addView(col6);
            row.addView(createDivider());

            row.addView(col7);
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
                selectedSTPembelianData = data;

                // Tangani aksi tambahan
                onRowClick(data);
            });


            mainTable.addView(row);
            rowIndex++; // Tingkatkan indeks
        }

    }

    private void populateNonRejectList(List<String> nonRejectList) {
        nonRejectTableLayout.removeAllViews();

        int rowIndex = 0;

        if (nonRejectList == null || nonRejectList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Tidak ada Data");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            nonRejectTableLayout.addView(noDataView);
            return;
        }

        // Isi tabel
        for (String nonRejectST : nonRejectList) {
            TableRow row = new TableRow(this);

            row.setTag(rowIndex);

            // Tambahkan TextView ke baris tabel
            TextView textView = createTextView(nonRejectST, 1.0f);
            row.addView(textView);

            row.setOnClickListener(view -> fetchDataAndShowTooltip(view, nonRejectST, "ST_h", "ST_d", "NoST"));

            // Tetapkan warna latar belakang berdasarkan indeks baris
            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream)); // Warna untuk baris genap
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white)); // Warna untuk baris ganjil
            }

            // Tambahkan baris ke TableLayout
            nonRejectTableLayout.addView(row);
            rowIndex++;
        }
    }

    private void populateRejectList(List<STPembelianDataReject> rejectDataList) {
        rejectTableLayout.removeAllViews(); // Menghapus tampilan sebelumnya

        int rowIndex = 0;
        countReject = rejectDataList.size();

        DecimalFormat df = new DecimalFormat("0.####"); // Menampilkan max 2 desimal, tanpa trailing .0

        if (rejectDataList == null || rejectDataList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Tidak ada Data");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            rejectTableLayout.addView(noDataView);
            return;
        }

        // Isi tabel dengan data yang diambil dari query
        for (STPembelianDataReject data : rejectDataList) {
            TableRow row = new TableRow(this);
            row.setTag(rowIndex);

            String idUOMTblLebar = (data.getIdUOMTblLebar() == 1) ? "mm" : "\"";
            String idUOMPanjang = (data.getIdUOMPanjang() == 4) ? "ft" : "N/A";

            // Menambahkan setiap kolom data dalam baris
//            row.addView(createTextView(data.getNoUrut(), 1.0f)); // NoUrut


            row.addView(createTextView(df.format(data.getTebal()) + " " + idUOMTblLebar, 1.0f)); // Tebal
            row.addView(createDivider());

            row.addView(createTextView(df.format(data.getLebar()) + " " + idUOMTblLebar, 1.0f)); // Lebar
            row.addView(createDivider());

            row.addView(createTextView(df.format(data.getPanjang()) + " " + idUOMPanjang, 1.0f)); // Panjang
            row.addView(createDivider());

            row.addView(createTextView(String.valueOf(data.getJmlhBatang()), 1.0f)); // JmlhBatang
            row.addView(createDivider());

            row.addView(createTextView(df.format(data.getTon()), 1.0f)); // Ton

            // Menetapkan warna latar belakang berdasarkan indeks baris
            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream)); // Warna untuk baris genap
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white)); // Warna untuk baris ganjil
            }

            // Menambahkan baris ke TableLayout
            rejectTableLayout.addView(row);
            rowIndex++;
        }
    }




    private void onRowClick(STPembelianData data) {

        executorService.execute(() -> {
            // Ambil data latar belakang
            noSTPembelian = data.getNoSTPembelian();

            nonRejectList = LabelApi.getNonRejectListByNoPenST(noSTPembelian);
            rejectDataList = LabelApi.fetchRejectDataByNoPenerimaanST(noSTPembelian);


            // Perbarui UI di thread utama
            runOnUiThread(() -> {
                populateNonRejectList(nonRejectList);
                populateRejectList(rejectDataList);


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