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

import com.example.myapplication.api.LabelApi;
import com.example.myapplication.api.ProductionApi;
import com.example.myapplication.model.CustomerData;
import com.example.myapplication.model.STUpahData;
import com.example.myapplication.model.TooltipData;
import com.example.myapplication.utils.DateTimeUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class SawnTimberUpah extends AppCompatActivity {

    private TableLayout mainTable;
    private TableLayout tableNoSTList;
    private TableRow selectedRow;
    private STUpahData selectedSTUpahData;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String noSTUpah;
    private String tglLaporan;
    private TextView noProduksiView;
    private List<STUpahData> dataList; // Data asli yang tidak difilter
    private List<String> noSTList = new ArrayList<>();
    private ImageButton btnAddLabelSTUpah;
    private Button btnDataBaru;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sawn_timber_upah);

        mainTable = findViewById(R.id.mainTable);
        tableNoSTList = findViewById(R.id.tableNoSTList);
        btnAddLabelSTUpah = findViewById(R.id.btnAddLabelSTUpah);
        btnDataBaru = findViewById(R.id.btnDataBaru);

        loadDataAndDisplayTable();

        // Menambahkan OnClickListener
        btnDataBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewDataDialog();

            }
        });

        btnAddLabelSTUpah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (noSTUpah == null || noSTUpah.isEmpty()) {
                    Toast.makeText(SawnTimberUpah.this, "Pilih No. Penerimaan ST!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent( SawnTimberUpah.this, SawnTimber.class);
                    intent.putExtra("noPenST", noSTUpah); // Mengirim integer
                    intent.putExtra("labelVersion", 2); // Mengirim string
                    startActivity(intent);
                }
            }
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
        View tooltipView = LayoutInflater.from(this).inflate(R.layout.tooltip_layout_right, null);

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

    private void loadDataAndDisplayTable() {
        // Menjalankan operasi di background thread
        executorService.execute(() -> {
            dataList = LabelApi.getSTUpahData();

            // Menampilkan data di UI thread setelah selesai mengambil data
            runOnUiThread(() -> {
                populateTable(dataList);  // Memperbarui tampilan tabel dengan data terbaru
                // Sembunyikan loading indicator jika ada
                // loadingIndicator.setVisibility(View.GONE);
            });
        });
    }


    // Di dalam Activity atau Fragment Anda
    private void showNewDataDialog() {
        // Membuat Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(SawnTimberUpah.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_insert_data_st_upah, null);
        builder.setView(view);

        // Menyiapkan EditText dan Spinner
        final EditText tglLaporan = view.findViewById(R.id.editTglLaporan);
        final Spinner spinIdCustomer = view.findViewById(R.id.spinIdCustomer);
        final EditText noTruk = view.findViewById(R.id.editNoTruk);
        final EditText noPlat = view.findViewById(R.id.editNoPlat);
        final EditText noSJ = view.findViewById(R.id.editNoSJ);
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

        // Deklarasikan objek dialog di luar listener
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        // Memanggil metode untuk memuat data ke Spinner
        loadCustomerDataToSpinner(spinIdCustomer);

        // Set OnClickListener untuk tglLaporan (datepicker)
        tglLaporan.setOnClickListener(v -> showDatePickerDialog(tglLaporan));

        Log.d("SpinnerValue", "Selected Supplier ID: " + spinIdCustomer);

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
            CustomerData selectedCustomer = (CustomerData) spinIdCustomer.getSelectedItem();
            // Pastikan selectedCustomer tidak null dan idSupplier valid
            final int idCustomerVal = (selectedCustomer != null && selectedCustomer.getIdCustomer() != null) ? selectedCustomer.getIdCustomer() : -1;
            String noTrukVal = noTruk.getText().toString();
            String noPlatVal = noPlat.getText().toString();
            String noSJVal = noSJ.getText().toString();
            String noteVal = note.getText().toString();

            // Validasi: Cek apakah ada field yang kosong
            boolean valid = true;

            // Validasi Spinner untuk memastikan item yang dipilih bukan "PILIH"
            if (idCustomerVal == -1) {  // Jika supplier yang dipilih adalah "PILIH"
                // Menambahkan error pada spinner dan memberi pesan kesalahan
                spinIdCustomer.setBackgroundResource(R.drawable.spinner_error);  // Background merah untuk error
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
            if (noSJVal.isEmpty()) {
                noSJ.setError("Suket harus diisi");
                valid = false;
            }

            // Jika semua field valid, lanjutkan proses
            if (valid) {
                // Menjalankan ExecutorService untuk mengambil nomor penerimaan baru dan menyimpan data
                executorService.execute(() -> {
                    // Ambil NoPenerimaanST baru dari database ketika tombol simpan diklik
                    String newNoPenerimaanST = LabelApi.getNextNoPenerimaanSTUpah();

                    // Periksa apakah NoPenerimaanST berhasil diambil
                    if (newNoPenerimaanST != null && !newNoPenerimaanST.isEmpty()) {
                        try {
                            LabelApi.insertDataToUpah(newNoPenerimaanST, tglLaporanVal, idCustomerVal, noPlatVal, noTrukVal, noSJVal, noteVal);

                            // Setelah operasi selesai, update UI di main thread
                            runOnUiThread(() -> {
                                // Menampilkan dialog sukses
                                AlertDialog.Builder builderAlert = new AlertDialog.Builder(this);
                                View dialogView = getLayoutInflater().inflate(R.layout.alert_success, null);

                                TextView tvMessage = dialogView.findViewById(R.id.tvMessage);
                                Button btnOk = dialogView.findViewById(R.id.btnOk);

                                tvMessage.setText("Berhasil disimpan dengan No. Upah " + newNoPenerimaanST);

                                builderAlert.setView(dialogView);
                                AlertDialog dialogSuccess = builderAlert.create();
                                dialogSuccess.setCancelable(false);

                                dialogSuccess.show(); // Tampilkan dulu agar window tersedia

                                // Buat sudut dialog kelihatan
                                dialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                // Atur ukuran dialog
                                dialogSuccess.getWindow().setLayout(
                                        1000,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                );

                                btnOk.setOnClickListener(a -> {
                                    loadDataAndDisplayTable();
                                    dialogSuccess.dismiss();
                                    dialog.dismiss();  // tutup dialog lama kalau ada
                                });
                            });

                        } catch (Exception e) {
                            // Jika terjadi error, beri tahu pengguna di main thread
                            runOnUiThread(() -> {
                                Toast.makeText(SawnTimberUpah.this, "Terjadi kesalahan saat menyimpan data", Toast.LENGTH_SHORT).show();
                            });
                        }

                    } else {
                        // Tangani kasus jika NoPenerimaanST gagal didapatkan
                        runOnUiThread(() -> {
                            Toast.makeText(SawnTimberUpah.this, "Gagal mengambil NoPenerimaanST", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();  // Menutup dialog jika terjadi error
                        });
                    }
                });
            } else {
                Toast.makeText(SawnTimberUpah.this, "Data belum lengkap!", Toast.LENGTH_SHORT).show();
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(SawnTimberUpah.this,
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


    private void loadCustomerDataToSpinner(final Spinner spinIdCustomer) {
        // List untuk menyimpan data Supplier
        final List<CustomerData> customerList = new ArrayList<>();

        // Menambahkan pilihan default ("Silakan Pilih") dengan id null
        customerList.add(new CustomerData(null, "PILIH"));  // Tambahkan pilihan default

        // Mengambil data supplier dari database menggunakan ExecutorService
        executorService.execute(() -> {
            // Ambil data supplier dari LabelApi
            List<CustomerData> customers = LabelApi.getCustomerList();
            customerList.addAll(customers);  // Menambahkan supplier yang diambil dari database

            // Perbarui Spinner di UI thread
            runOnUiThread(() -> {
                // Membuat ArrayAdapter untuk Spinner
                ArrayAdapter<CustomerData> adapter = new ArrayAdapter<>(SawnTimberUpah.this, android.R.layout.simple_spinner_item, customerList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinIdCustomer.setAdapter(adapter);
                spinIdCustomer.setSelection(0);  // Menampilkan pilihan default pertama ("PILIH")

            });
        });

        spinIdCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                CustomerData selectedCustomer = (CustomerData) spinIdCustomer.getSelectedItem();

                // Periksa apakah selectedCustomer tidak null dan memiliki idSupplier yang valid
                int idCustomerVal = -1;
                if (selectedCustomer != null && selectedCustomer.getIdCustomer() != null) {
                    idCustomerVal = selectedCustomer.getIdCustomer();
                }

                // Jika item yang dipilih adalah "PILIH", beri error
                if (idCustomerVal != -1) {
                    spinIdCustomer.setBackgroundResource(R.drawable.border_input); // Ganti dengan background normal

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle when nothing is selected (optional)
            }
        });
    }


    private void populateTable(List<STUpahData> dataList) {

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

        for (STUpahData data : dataList) {
            TableRow row = new TableRow(this);

            // Simpan indeks baris di tag
            row.setTag(rowIndex);

            // Tambahkan TextView untuk setiap kolom
            TextView col1 = createTextView(data.getNoSTUpah(), 1.0f);
            TextView col2 = createTextView(data.getTgl(), 1.0f);
            TextView col3 = createTextView(data.getCustomer(), 1.0f);
            TextView col4 = createTextView(data.getNoPlat(), 1.0f);
            TextView col5 = createTextView(data.getNoTruk(), 1.0f);
            TextView col6 = createTextView(data.getNoSJ(), 1.0f);
            TextView col7 = createTextView(data.getKeteranganUpah(), 2.0f);

            setDateToView(data.getTgl(), col2);

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
        for (String noST : noSTList) {
            TableRow row = new TableRow(this);

            row.setTag(rowIndex);

            // Tambahkan TextView ke baris tabel
            TextView textView = createTextView(noST, 1.0f);
            row.addView(textView);

            // Tambahkan OnClickListener untuk menampilkan tooltip
            row.setOnClickListener(view -> fetchDataAndShowTooltip(view, noST, "ST_h", "ST_d", "NoST"));

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


    private void onRowClick(STUpahData data) {

        executorService.execute(() -> {
            // Ambil data latar belakang
            noSTUpah = data.getNoSTUpah();

            noSTList = LabelApi.getSTListBySTUpah(noSTUpah);

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