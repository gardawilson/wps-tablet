package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.api.ProductionApi;
import com.example.myapplication.api.LabelApi;
import com.example.myapplication.model.ProductionData;
import com.example.myapplication.model.STPembelianData;
import com.example.myapplication.model.STPembelianDataReject;
import com.example.myapplication.model.SupplierData;
import com.example.myapplication.utils.DateTimeUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
    private Button btnDataBaru;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sawn_timber_pembelian);

        mainTable = findViewById(R.id.mainTable);
        nonRejectTableLayout = findViewById(R.id.nonRejectTableLayout);
        rejectTableLayout = findViewById(R.id.rejectTableLayout);
        btnAddLabelSTBeli = findViewById(R.id.btnAddLabelSTBeli);
        btnDataBaru = findViewById(R.id.btnDataBaru);

        loadDataAndDisplayTable();

        // Menambahkan OnClickListener
        btnDataBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewDataDialog();

            }
        });

    }

    private void loadDataAndDisplayTable() {
        // Menjalankan operasi di background thread
        executorService.execute(() -> {
            dataList = LabelApi.getSTPembelianData();

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
        AlertDialog.Builder builder = new AlertDialog.Builder(SawnTimberPembelian.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_insert_data_penerimaan_st, null);
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
                            LabelApi.insertDataToDatabase(newNoPenerimaanST, tglLaporanVal, tglMasukVal, idSupplierVal, noTrukVal, noPlatVal, suketVal, tonSJVal, noteVal);

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

    private void populateNonRejectList(List<String> noS4SList) {
        nonRejectTableLayout.removeAllViews();

        int rowIndex = 0;

        if (noS4SList == null || noS4SList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Tidak ada Data");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            nonRejectTableLayout.addView(noDataView);
            return;
        }

        // Isi tabel
        for (String noS4S : noS4SList) {
            TableRow row = new TableRow(this);

            row.setTag(rowIndex);

            // Tambahkan TextView ke baris tabel
            TextView textView = createTextView(noS4S, 1.0f);
            row.addView(textView);

            // Tambahkan OnClickListener untuk menampilkan tooltip
//            row.setOnClickListener(view -> fetchDataAndShowTooltip(view, noS4S, "S4S_h", "S4S_d", "NoS4S"));

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

            // Menambahkan setiap kolom data dalam baris
//            row.addView(createTextView(data.getNoUrut(), 1.0f)); // NoUrut
            row.addView(createTextView(String.valueOf(data.getTebal()), 1.0f)); // Tebal
            row.addView(createDivider());

            row.addView(createTextView(String.valueOf(data.getLebar()), 1.0f)); // Lebar
            row.addView(createDivider());

            row.addView(createTextView(String.valueOf(data.getPanjang()), 1.0f)); // Panjang
//            row.addView(createTextView(String.valueOf(data.getJmlhBatang()), 1.0f)); // JmlhBatang
//            row.addView(createTextView(String.valueOf(data.getIdUOMTblLebar()), 1.0f)); // IdUOMTblLebar
//            row.addView(createTextView(String.valueOf(data.getIdUOMPanjang()), 1.0f)); // IdUOMPanjang
//            row.addView(createTextView(String.valueOf(data.getTon()), 1.0f)); // Ton

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
            List<STPembelianDataReject> rejectDataList = LabelApi.fetchRejectDataByNoPenerimaanST(noSTPembelian);


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