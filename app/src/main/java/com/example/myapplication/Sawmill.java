package com.example.myapplication;

import static com.example.myapplication.config.ApiEndpoints.CRYSTAL_REPORT_WPS_EXPORT_PDF;
import static com.example.myapplication.utils.DateTimeUtils.formatDate;
import static com.example.myapplication.utils.DateTimeUtils.formatTimeToHHmmss;
import static com.example.myapplication.utils.PanjangStandarUtils.confirmIfNotStandard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.api.SawmillApi;
import com.example.myapplication.model.GradeKBData;
import com.example.myapplication.model.MstMejaData;
import com.example.myapplication.model.MstOperatorData;
import com.example.myapplication.model.SawmillData;
import com.example.myapplication.model.SawmillDetailData;
import com.example.myapplication.model.SawmillDetailInputData;
import com.example.myapplication.model.SpecialConditionData;
import com.example.myapplication.model.KayuBulatData;
import com.example.myapplication.model.SpkProdukData;
import com.example.myapplication.utils.InputFilterMinDecimalNoLeadingZero;
import com.example.myapplication.utils.InputFilterMinIntNoLeadingZero;
import com.example.myapplication.utils.LoadingDialogHelper;
import com.example.myapplication.utils.PdfUtils;
import com.example.myapplication.utils.SharedPrefUtils;
import com.example.myapplication.utils.TokenManager;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.function.Supplier;

import android.text.TextWatcher;
import android.text.Editable;


public class Sawmill extends AppCompatActivity {

    private TableLayout mainTable;
    private TableRow selectedRowMain;
    private TableRow selectedRowDetail;
    private List<SawmillData> dataList = new ArrayList<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Button btnDataBaru;
    private Button btnEditData;
    private Button btnHapusData;
    private Button btnCetak;
    private int totalPcsAcuan = 0;
    private int sisaPcs = 0;
    private final List<SawmillDetailInputData> inputList = new ArrayList<>();
    private List<SawmillDetailData> detailDataList = new ArrayList<>();
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private ProgressBar mainLoadingIndicator;
    private TableLayout detailSawmillTableLayout;
    private TextView textJumlah;
    private TextView textTon;
    private SwipeRefreshLayout swipeRefreshLayout;
    private float touchX, touchY;     // Variabel untuk menyimpan koordinat klik
    private String tglTutupTransaksi;
    private List<String> allNoMeja = new ArrayList<>();
    private Boolean isValidNoKB = false;
    private SearchView searchNoKB;
    private ScrollView scrollView;
    private int currentPage = 1;
    private boolean isLoading = false;
    private final int PAGE_SIZE = 50;
    private TableRow loadingRow = null;
    private String currentSearchKeyword = ""; // simpan keyword global
    private boolean hasMoreData = true;
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private static final int SEARCH_DELAY = 1000;
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();
    private String username;

    // ✅ TAMBAHKAN INI untuk prevent race condition
    private volatile boolean isDetailDataLoaded = false;
    private final Object detailDataLock = new Object();

    final double[] originalSisaTon = {0.0};   // store value from API

    private double currentTempTon = 0.0; // Track ton dari input temp

    private double acuanTempTon = 0.0;   // ✅ TAMBAHKAN: Track ton dari data acuan




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sawmill);

        mainTable = findViewById(R.id.mainTable);
        btnDataBaru = findViewById(R.id.btnDataBaru);
        mainLoadingIndicator = findViewById(R.id.mainLoadingIndicator);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        btnEditData = findViewById(R.id.btnEditData);
        btnHapusData = findViewById(R.id.btnHapusData);
        searchNoKB = findViewById(R.id.searchNoKB);
        scrollView = findViewById(R.id.scrollView);
        btnCetak = findViewById(R.id.btnCetak);
        username = SharedPrefUtils.getUsername(this);


        loadDataAndDisplayTable();

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
            int diff = view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY());

            if (diff <= 100 && !isLoading && hasMoreData) {
                loadMoreData(false);
            }
        });

        searchNoKB.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Hentikan debounce jika user submit langsung
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
                currentSearchKeyword = query.trim();
                loadMoreData(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Cancel debounce sebelumnya
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                // Buat debounce baru yang akan dijalankan jika user berhenti mengetik 3 detik
                searchRunnable = () -> {
                    currentSearchKeyword = newText.trim();
                    loadMoreData(true);
                };

                // Jadwalkan debounce setelah 3 detik
                searchHandler.postDelayed(searchRunnable, SEARCH_DELAY);
                return true;
            }
        });


        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Menampilkan indikator swipe
            swipeRefreshLayout.setRefreshing(true);

            // Lakukan reload data
            new Handler().postDelayed(() -> {
                loadDataAndDisplayTable();
                swipeRefreshLayout.setRefreshing(false);
            }, 500); // delay kecil agar animasi kelihatan
        });

        btnDataBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewDataDialog();
            }
        });

        btnEditData.setOnClickListener(v -> {
            if (selectedRowMain != null) {
                SawmillData selectedData = (SawmillData) selectedRowMain.getTag();
                showEditDataDialog(selectedData);
            } else {
                Toast.makeText(this, "Pilih data terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });

        btnHapusData.setOnClickListener(v -> {
            if (selectedRowMain != null) {
                SawmillData selectedData = (SawmillData) selectedRowMain.getTag();

                new AlertDialog.Builder(this)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Yakin ingin menghapus data NoTely : " + selectedData.getNoSTSawmill() + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            deleteFullSawmillData(selectedData.getNoSTSawmill(), selectedData.getNamaJenisKayu());
                        })
                        .setNegativeButton("Batal", null)
                        .show();

            } else {
                Toast.makeText(this, "Pilih data terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });

        btnCetak.setOnClickListener(v -> {
            if (selectedRowMain != null) {
                SawmillData selectedData = (SawmillData) selectedRowMain.getTag();

                String noProduksi = selectedData.getNoSTSawmill(); // atau getNoProduksi(), tergantung field-nya
                String reportName = "CrLembarTally";

                String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                        + "?NoProduksi=" + noProduksi
                        + "&Username=" + username
                        + "&reportName=" + reportName;

                loadingDialogHelper.show(this);
                PdfUtils.downloadAndOpenPDF(
                        this,
                        url,
                        "STSawmill_" + noProduksi + ".pdf",
                        executorService,
                        loadingDialogHelper
                );
            } else {
                Toast.makeText(this, "Pilih data terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoadingRow() {
        loadingRow = new TableRow(this);

        ProgressBar spinner = new ProgressBar(this);
        spinner.setIndeterminate(true);

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 9; // total kolom kamu, sesuaikan jumlahnya
        spinner.setLayoutParams(params);

        loadingRow.addView(spinner);
        mainTable.addView(loadingRow);
    }

    private void hideLoadingRow() {
        if (loadingRow != null) {
            mainTable.removeView(loadingRow);
            loadingRow = null;
        }
    }

    private void filterDataByNoKayuBulat(String keyword) {
        if (dataList == null) return;

        List<SawmillData> filteredList = new ArrayList<>();

        for (SawmillData data : dataList) {
            if (data.getNoKayuBulat() != null && data.getNoKayuBulat().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(data);
            }
        }

        populateTable(filteredList);  // tampilkan hasil filter
    }


    private void deleteFullSawmillData(String noSTSawmill, String jenisKayu) {
        executorService.execute(() -> {
            try {
                SawmillApi.deleteSawmillData(noSTSawmill, jenisKayu); // method transactional hapus di server/db

                runOnUiThread(() -> {
                    loadDataAndDisplayTable();
                    Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                });

            } catch (SQLException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Gagal menghapus data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }



    // DIALOG EDIT DATA HEADER
    private void showEditDataDialog(SawmillData existingData) {
        // Membuat Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(Sawmill.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_insert_header_sawmill, null);
        builder.setView(view);

        // Menyiapkan EditText dan Spinner
        final TextView noSTSawmill = view.findViewById(R.id.tvNoSTSawmill);
        final AutoCompleteTextView noKB = view.findViewById(R.id.editNoKB);
        final EditText tanggal = view.findViewById(R.id.editTgl);
        final EditText jenisKayu = view.findViewById(R.id.editJenisKayu);
        final Spinner spinMeja = view.findViewById(R.id.spinMeja);
        final Spinner spinShift = view.findViewById(R.id.spinShift);
        final EditText jamMulai = view.findViewById(R.id.editJamMulai);
        final EditText jamBerhenti = view.findViewById(R.id.editJamBerhenti);
        final TextView totalJamKerja = view.findViewById(R.id.tvTotalJamKerja);
        final EditText hourMeter = view.findViewById(R.id.editHourMeter);
        final EditText jlhBalokTerpakai = view.findViewById(R.id.editJlhBalokTerpakai);
        final EditText beratBalokTim = view.findViewById(R.id.editBeratBalokTim);
        final EditText beratBalok = view.findViewById(R.id.editBeratBalok);
        final EditText remark = view.findViewById(R.id.editRemark);
        final EditText noTruk = view.findViewById(R.id.editNoTruk);
        final EditText noPlat = view.findViewById(R.id.editNoPlat);
        final EditText noSuket = view.findViewById(R.id.editSuket);
        final EditText supplier = view.findViewById(R.id.editSupplier);
        final EditText jlhBatang = view.findViewById(R.id.editJlhBatang);
        final Spinner spinSpecialCondition = view.findViewById(R.id.spinSpecialCondition);
        final Spinner spinOperator1 = view.findViewById(R.id.spinOperator1);
        final Spinner spinOperator2 = view.findViewById(R.id.spinOperator2);

        // Setup time picker untuk jam mulai dan jam berhenti
        setupTimePicker(jamMulai, jamMulai, jamBerhenti, totalJamKerja, tanggal,
                () -> {
                    MstMejaData selected = (MstMejaData) spinMeja.getSelectedItem();
                    return selected != null ? selected.getNoMeja() : "";
                }, existingData.getNoSTSawmill());

        setupTimePicker(jamBerhenti, jamMulai, jamBerhenti, totalJamKerja, tanggal,
                () -> {
                    MstMejaData selected = (MstMejaData) spinMeja.getSelectedItem();
                    return selected != null ? selected.getNoMeja() : "";
                }, existingData.getNoSTSawmill());


        // Siapkan adapter suggestion Kayu Bulat (untuk edit, biasanya disable autocomplete)
        ArrayAdapter<String> kbAdapter = new ArrayAdapter<>(Sawmill.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
        noKB.setAdapter(kbAdapter);

        // ===== SETUP NoMeja AutoComplete =====
        loadMejaToSpinner(spinMeja, existingData.getNoMeja(), spinOperator1, spinOperator2, tanggal, jamBerhenti, jamMulai, totalJamKerja, true);

        // Load data ke spinner terlebih dahulu dengan nilai yang sudah ada
        loadSpecialConditionToSpinner(spinSpecialCondition, existingData.getIdSawmillSpecialCondition());
        loadOperatorToSpinner(spinOperator1, existingData.getIdOperator1());
        loadOperatorToSpinner(spinOperator2, existingData.getIdOperator2());
        loadShiftToSpinner(spinShift, String.valueOf(existingData.getShift()));

        // Isi semua field dengan data yang sudah ada
        noSTSawmill.setText(existingData.getNoSTSawmill());
        noKB.setText(existingData.getNoKayuBulat());
        tanggal.setText(formatDate(existingData.getTglSawmill()));
        jamMulai.setText(formatTimeToHHmmss(existingData.getHourStart()));
        if (existingData.getHourEnd() == null || existingData.getHourEnd().trim().isEmpty()) {
            jamBerhenti.setText("");
        } else {
            jamBerhenti.setText(formatTimeToHHmmss(existingData.getHourEnd()));
        }
        totalJamKerja.setText(existingData.getJamKerja());
        hourMeter.setText(existingData.getHourMeter());
        jlhBalokTerpakai.setText(String.valueOf(existingData.getBalokTerpakai()));
        beratBalokTim.setText(decimalFormat.format(existingData.getBeratBalokTim()));
        beratBalok.setText(decimalFormat.format(existingData.getBeratBalok()));
        remark.setText(existingData.getRemark());
        jlhBatang.setText(String.valueOf(existingData.getJlhBatangRajang()));

        if (!existingData.getNoKayuBulat().isEmpty() && existingData.getNoKayuBulat() != null) {
            executorService.execute(() -> {
                KayuBulatData data = SawmillApi.getKayuBulatDetail(existingData.getNoKayuBulat());
                if (data != null) {
                    runOnUiThread(() -> {
                        // Isi ke field sesuai variabel
                        jenisKayu.setText(data.getJenis());
                        supplier.setText(data.getSupplier());
                        noPlat.setText(data.getNoPlat());
                        noTruk.setText(data.getNoTruk());
                        noSuket.setText(data.getSuket());

                        // Tampilkan icon ceklis di kanan AutoCompleteTextView
                        Drawable checkIcon = ContextCompat.getDrawable(view.getContext(), R.drawable.ic_done);
                        if (checkIcon != null) {
                            checkIcon.setTint(Color.parseColor("#4CAF50"));
                        }
                        noKB.setCompoundDrawablesWithIntrinsicBounds(null, null, checkIcon, null);
                        noKB.setBackgroundResource(R.drawable.border_accepted);
                        isValidNoKB = true;
                    });
                }
            });
        }

        // Setup TextWatcher untuk noKB
        setupNoKBTextWatcherEdit(noKB, kbAdapter, view, jenisKayu, supplier, noPlat, noTruk, noSuket, existingData.getNamaJenisKayu());

        // Deklarasikan objek dialog di luar listener
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        // Set OnClickListener untuk tanggal (datepicker)
        tanggal.setOnClickListener(v -> showDatePickerDialog(tanggal));

        // Menambahkan onClickListener untuk tombol tutup dialog
        ImageButton btnCloseDialogInputHeader = view.findViewById(R.id.btnCloseDialogInputHeader);
        btnCloseDialogInputHeader.setOnClickListener(v -> {
            dialog.dismiss();  // Menutup Dialog
        });

        // Tombol Update (ubah text dari "Simpan" ke "Update")
        Button btnSave = view.findViewById(R.id.btnSimpan);
        // Anggap tglTutupTransaksi sudah kamu ambil dari database sebelumnya
        if (isTutupTransaksi(existingData.getTglSawmill(), tglTutupTransaksi)) {
            btnSave.setText("Transaksi Ditutup");
            btnSave.setEnabled(false);
            btnSave.setAlpha(0.5f); // opsional
        } else {
            btnSave.setText("Update");
            btnSave.setEnabled(true);
            btnSave.setAlpha(1.0f); // pastikan aktif
        }

        btnSave.setOnClickListener(v -> {
            // Ambil data dari EditText dan Spinner
            btnSave.setEnabled(false);
            loadingDialogHelper.show(view.getContext());

            SpecialConditionData selectedSpecialCondition = (SpecialConditionData) spinSpecialCondition.getSelectedItem();
            MstOperatorData selectedOperator1 = (MstOperatorData) spinOperator1.getSelectedItem();
            MstOperatorData selectedOperator2 = (MstOperatorData) spinOperator2.getSelectedItem();
            String selectedShiftStr = (String) spinShift.getSelectedItem();

            final int idSpecialConditionVal = (selectedSpecialCondition != null && selectedSpecialCondition.getIdSawmillSpecialCondition() != null) ? selectedSpecialCondition.getIdSawmillSpecialCondition() : -1;
            final int idOperator1Val = (selectedOperator1 != null && selectedOperator1.getIdOperator() != null) ? selectedOperator1.getIdOperator() : -1;
            final int idOperator2Val = (selectedOperator2 != null && selectedOperator2.getIdOperator() != null) ? selectedOperator2.getIdOperator() : -1;
            final int shiftVal = (!selectedShiftStr.equals("PILIH")) ? Integer.parseInt(selectedShiftStr) : -1;

            String noKBVal = noKB.getText().toString();
            String tanggalVal = tanggal.getText().toString();
            String remarkVal = remark.getText().toString();
            String inputJlhBalokTerpakai = jlhBalokTerpakai.getText().toString().trim();
            int jlhBalokTerpakaiVal = inputJlhBalokTerpakai.isEmpty() ? 0 : Integer.parseInt(inputJlhBalokTerpakai);
            MstMejaData selectedMeja = (MstMejaData) spinMeja.getSelectedItem();
            String noMejaVal = selectedMeja != null ? selectedMeja.getNoMeja() : "";
            String hourMeterVal = hourMeter.getText().toString();
            String jlhBatangVal = jlhBatang.getText().toString();
            String beratBalokTimVal = beratBalokTim.getText().toString();
            String beratBalokVal = beratBalok.getText().toString();
            String jenisKayuVal = jenisKayu.getText().toString();
            String jamMulaiVal = jamMulai.getText().toString();
            String jamBerhentiVal = jamBerhenti.getText().toString();
            String totalJamKerjaVal = totalJamKerja.getText().toString();

            // Validasi: Cek apakah ada field yang kosong
            boolean valid = true;

            // Validasi Spinner untuk memastikan item yang dipilih bukan "PILIH"
            if (idSpecialConditionVal == -1) {
                spinSpecialCondition.setBackgroundResource(R.drawable.spinner_error);
                valid = false;
            }

            if (idOperator1Val == -1) {
                spinOperator1.setBackgroundResource(R.drawable.spinner_error);
                valid = false;
            }

            if (shiftVal == -1) {
                spinShift.setBackgroundResource(R.drawable.spinner_error);
                valid = false;
            }

            if (jlhBalokTerpakai.getText().toString().trim().isEmpty()) {
                jlhBalokTerpakai.setError("Jumlah Balok Terpakai Harus Diisi!");
                valid = false;
            }

//            if (beratBalokVal.isEmpty()) {
//                beratBalok.setError("Berat Balok harus diisi");
//                valid = false;
//            }

            if (selectedMeja == null || selectedMeja.getNoMeja() == null || selectedMeja.getNoMeja().isEmpty()) {
                spinMeja.setBackgroundResource(R.drawable.spinner_error);
                valid = false;
            }

            if (!isValidNoKB) {
                valid = false;
            }

            // Jika semua field valid, lanjutkan proses update
            if (valid) {
                // Menjalankan ExecutorService untuk update data
                executorService.execute(() -> {
                    try {
                        // Panggil method update dengan NoSTSawmill dari existingData
                        SawmillApi.updateSawmillData(
                                existingData.getNoSTSawmill(), // Primary key untuk update
                                shiftVal,
                                tanggalVal,
                                noKBVal,
                                noMejaVal,
                                idSpecialConditionVal,
                                jlhBalokTerpakaiVal,
                                jlhBatangVal,
                                hourMeterVal,
                                idOperator1Val,
                                idOperator2Val,
                                remarkVal,
                                beratBalokTimVal,
                                beratBalokVal,
                                jenisKayuVal,
                                jamMulaiVal,
                                jamBerhentiVal,
                                totalJamKerjaVal
                        );

                        // Setelah operasi selesai, update UI di main thread
                        runOnUiThread(() -> {
                            loadingDialogHelper.hide();
                            btnSave.setEnabled(true);

                            AlertDialog.Builder builderAlert = new AlertDialog.Builder(this);
                            View dialogView = getLayoutInflater().inflate(R.layout.alert_success, null);

                            TextView tvMessage = dialogView.findViewById(R.id.tvMessage);
                            Button btnOk = dialogView.findViewById(R.id.btnOk);

                            tvMessage.setText("Data berhasil diupdate untuk No.Telly " + existingData.getNoSTSawmill());

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
                                dialog.dismiss();  // tutup dialog edit
                            });
                        });

                    } catch (Exception e) {
                        // Jika terjadi error, beri tahu pengguna di main thread
                        runOnUiThread(() -> {
                            loadingDialogHelper.hide();
                            btnSave.setEnabled(true);
                            Toast.makeText(Sawmill.this, "Tidak dapat mengupdate data, " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                    }
                });
            } else {
                loadingDialogHelper.hide();
                Toast.makeText(Sawmill.this, "Data belum lengkap!", Toast.LENGTH_SHORT).show();
            }
        });

        // Menampilkan Dialog
        dialog.show();
    }


    public static boolean isTutupTransaksi(String tglData, String tglTutup) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date dateData = sdf.parse(tglData);
            Date dateTutup = sdf.parse(tglTutup);

            if (dateData != null && dateTutup != null) {
                // Jika tanggal sawmill sebelum atau sama dengan tanggal tutup, return true
                return !dateData.after(dateTutup);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true; // default: anggap ditutup kalau gagal parsing
    }
    

    private void showDatePickerDialog(final EditText dateEditText) {
        // Mendapatkan tanggal hari ini
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Membuat DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(Sawmill.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Membuat Calendar baru untuk tanggal yang dipilih
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);

                        // Format tanggal ke dd-MMM-yyyy (contoh: 11-Apr-2024)
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                        String formattedDate = sdf.format(selectedDate.getTime());

                        // Tampilkan tanggal ke EditText
                        dateEditText.setText(formattedDate);
                    }
                }, year, month, day);

        // Tampilkan DatePickerDialog
        datePickerDialog.show();
    }

    private void setTodayDateToEditText(final EditText dateEditText) {
        // Mendapatkan tanggal hari ini
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Membuat Calendar untuk tanggal hari ini
        calendar.set(year, month, day);

        // Format tanggal ke dd-MMM-yyyy (contoh: 11-Apr-2024)
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = sdf.format(calendar.getTime());

        // Tampilkan tanggal ke EditText
        dateEditText.setText(formattedDate);
    }


    private void loadSpecialConditionToSpinner(final Spinner spinSpecialCondition, @Nullable Integer selectedId) {
        final List<SpecialConditionData> conditionList = new ArrayList<>();
        conditionList.add(new SpecialConditionData(null, "PILIH"));

        executorService.execute(() -> {
            List<SpecialConditionData> conditions = SawmillApi.getSpecialConditionList();
            conditionList.addAll(conditions);

            runOnUiThread(() -> {
                ArrayAdapter<SpecialConditionData> adapter = new ArrayAdapter<>(Sawmill.this, android.R.layout.simple_spinner_item, conditionList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinSpecialCondition.setAdapter(adapter);

                // Setelah adapter diset, lakukan setSelection jika ada selectedId
                if (selectedId != null) {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        SpecialConditionData item = adapter.getItem(i);
                        if (item != null && selectedId.equals(item.getIdSawmillSpecialCondition())) {
                            spinSpecialCondition.setSelection(i);
                            break;
                        }
                    }
                }
            });
        });

        spinSpecialCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SpecialConditionData selectedCondition = (SpecialConditionData) spinSpecialCondition.getSelectedItem();

                int idSelected = -1;
                if (selectedCondition != null && selectedCondition.getIdSawmillSpecialCondition() != null) {
                    idSelected = selectedCondition.getIdSawmillSpecialCondition();
                }

                if (idSelected != -1) {
                    spinSpecialCondition.setBackgroundResource(R.drawable.border_input);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }


    private void loadShiftToSpinner(final Spinner spinShift, final String selectedShift) {
        final List<String> shiftList = new ArrayList<>();

        shiftList.add("PILIH");
        shiftList.add("1");
        shiftList.add("2");
        shiftList.add("3");

        runOnUiThread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    Sawmill.this,
                    android.R.layout.simple_spinner_item,
                    shiftList
            );
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinShift.setAdapter(adapter);

            // Atur selection berdasarkan nilai sebelumnya
            if (selectedShift != null) {
                int index = shiftList.indexOf(selectedShift);
                if (index >= 0) {
                    spinShift.setSelection(index);
                } else {
                    spinShift.setSelection(0); // default to "PILIH"
                }
            } else {
                spinShift.setSelection(0); // default to "PILIH"
            }
        });

        spinShift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) spinShift.getSelectedItem();
                if (!"PILIH".equals(selected)) {
                    spinShift.setBackgroundResource(R.drawable.border_input);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    private void loadOperatorToSpinner(final Spinner spinOperator, @Nullable Integer selectedId) {
        final List<MstOperatorData> operatorList = new ArrayList<>();
        operatorList.add(new MstOperatorData(null, "PILIH"));

        executorService.execute(() -> {
            List<MstOperatorData> data = SawmillApi.getOperatorList();
            operatorList.addAll(data);

            runOnUiThread(() -> {
                ArrayAdapter<MstOperatorData> adapter = new ArrayAdapter<>(Sawmill.this, android.R.layout.simple_spinner_item, operatorList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinOperator.setAdapter(adapter);

                // Setelah spinner terisi, pilih item berdasarkan selectedId
                if (selectedId != null) {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        MstOperatorData item = adapter.getItem(i);
                        if (item != null && selectedId.equals(item.getIdOperator())) {
                            spinOperator.setSelection(i);
                            break;
                        }
                    }
                }
            });
        });

        spinOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MstOperatorData selectedOperator = (MstOperatorData) spinOperator.getSelectedItem();
                if (selectedOperator != null && selectedOperator.getIdOperator() != null) {
                    spinOperator.setBackgroundResource(R.drawable.border_input);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    private void loadGradeKBToSpinner(
            final Spinner spinGradeKB,
            RadioButton radioBagus,
            RadioButton radioKulit,
            String jenisKayu,
            Integer selectedIdGradeKB,
            Integer isBagusKulitValue ) {

        final List<GradeKBData> gradeList = new ArrayList<>();

        // Tambahkan pilihan default
        gradeList.add(new GradeKBData(null, "PILIH"));

        executorService.execute(() -> {
            List<GradeKBData> data = SawmillApi.getGradeKBList(); // Ambil semua grade dari API

            // Filter data
            if (jenisKayu == null || !jenisKayu.toLowerCase().contains("rambung")) {
                for (GradeKBData grade : data) {
                    if (grade.getNamaGrade() != null &&
                            grade.getNamaGrade().toLowerCase().contains("kayu lat")) {
                        gradeList.add(grade);
                    }
                }
            } else {
                gradeList.addAll(data);
            }

            runOnUiThread(() -> {
                ArrayAdapter<GradeKBData> adapter = new ArrayAdapter<>(
                        Sawmill.this,
                        android.R.layout.simple_spinner_item,
                        gradeList
                );
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinGradeKB.setAdapter(adapter);

                // Set default berdasarkan selectedIdGradeKB
                if (selectedIdGradeKB != null) {
                    for (int i = 0; i < gradeList.size(); i++) {
                        GradeKBData g = gradeList.get(i);
                        if (g.getIdGradeKB() != null && g.getIdGradeKB().equals(selectedIdGradeKB)) {
                            spinGradeKB.setSelection(i);
                            break;
                        }
                    }
                } else {
                    spinGradeKB.setSelection(0); // fallback ke "PILIH"
                }
            });
        });

        spinGradeKB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GradeKBData selectedGrade = (GradeKBData) spinGradeKB.getSelectedItem();

                if (selectedGrade != null && selectedGrade.getIdGradeKB() != null) {
                    spinGradeKB.setBackgroundResource(R.drawable.border_input);
                }

                if (selectedGrade != null &&
                        selectedGrade.getNamaGrade() != null &&
                        selectedGrade.getNamaGrade().toLowerCase().contains("kayu lat")) {

                    radioBagus.setEnabled(true);
                    radioKulit.setEnabled(true);

                    if (isBagusKulitValue == 1 || isBagusKulitValue == 0) {
                        radioKulit.setChecked(true);
                        radioBagus.setChecked(true);
                    }

                } else {
                    radioBagus.setEnabled(false);
                    radioKulit.setEnabled(false);
                    radioBagus.setChecked(false);
                    radioKulit.setChecked(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // no-op
            }
        });
    }





    private void loadNoKayuBulatSuggestions(String input, ArrayAdapter<String> adapter, AutoCompleteTextView noKB) {
        executorService.execute(() -> {
            List<String> suggestions = SawmillApi.getNoKayuBulatSuggestions(input);
            runOnUiThread(() -> {
                adapter.clear();
                adapter.addAll(suggestions);
                adapter.notifyDataSetChanged();
                noKB.showDropDown();
            });
        });
    }


    private void hitungTotalJamKerja(EditText jamMulai, EditText jamBerhenti, TextView totalJamKerja) {
        String start = jamMulai.getText().toString();
        String end = jamBerhenti.getText().toString();

        if (start.isEmpty() || end.isEmpty()) {
            totalJamKerja.setText("");
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date dateStart = sdf.parse(start);
            Date dateEnd = sdf.parse(end);

            long diff = dateEnd.getTime() - dateStart.getTime();

            if (diff < 0) {
                // Tambahkan 1 hari jika end lebih kecil dari start
                diff += 24 * 60 * 60 * 1000;
            }

            long diffHours = diff / (60 * 60 * 1000);
            long diffMinutes = (diff / (60 * 1000)) % 60;

            String result = String.format(Locale.getDefault(), "%d.%02d", diffHours, diffMinutes);
            totalJamKerja.setText(result);
        } catch (ParseException e) {
            e.printStackTrace();
            totalJamKerja.setText("-");
        }
    }


    private void setupTimePicker(EditText targetEditText,
                                 EditText jamMulai,
                                 EditText jamBerhenti,
                                 TextView totalJamKerja,
                                 EditText tanggal,
                                 Supplier<String> noMejaSupplier,
                                 String noSTSawmill) {

        targetEditText.setInputType(InputType.TYPE_NULL);
        targetEditText.setFocusable(false);
        targetEditText.setClickable(true);

        targetEditText.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    targetEditText.getContext(),
                    (view, hourOfDay, minute1) -> {
                        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                        targetEditText.setText(timeFormatted);

                        String tgl = tanggal.getText().toString().trim();
                        String meja = noMejaSupplier.get(); // 🟢 Ambil dari supplier
                        String start = jamMulai.getText().toString().trim();
                        String end = jamBerhenti.getText().toString().trim();

                        if (!tgl.isEmpty() && !start.isEmpty() && !end.isEmpty()) {
                            executorService.execute(() -> {
                                boolean isOverlap = SawmillApi.isHourRangeOverlapping(tgl, meja, start, end, noSTSawmill);

                                runOnUiThread(() -> {
                                    if (isOverlap) {
                                        Toast.makeText(targetEditText.getContext(), "Rentang Jam tersebut telah digunakan!", Toast.LENGTH_SHORT).show();
                                        targetEditText.setText("");
                                        totalJamKerja.setText("");
                                    } else {
                                        hitungTotalJamKerja(jamMulai, jamBerhenti, totalJamKerja);
                                    }
                                });
                            });
                        } else {
                            hitungTotalJamKerja(jamMulai, jamBerhenti, totalJamKerja);
                        }
                    },
                    hour, minute, true
            );

            timePickerDialog.show();
        });
    }



    // DIALOG NEW DATA HEADER
    private void showNewDataDialog() {
        // Membuat Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(Sawmill.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_insert_header_sawmill, null);
        builder.setView(view);

        // Menyiapkan EditText dan Spinner
        final AutoCompleteTextView noKB = view.findViewById(R.id.editNoKB);
        final EditText tanggal = view.findViewById(R.id.editTgl);
        final EditText jenisKayu = view.findViewById(R.id.editJenisKayu);
        final Spinner spinMeja = view.findViewById(R.id.spinMeja);
        final Spinner spinShift = view.findViewById(R.id.spinShift);
        final EditText jamMulai = view.findViewById(R.id.editJamMulai);
        final EditText jamBerhenti = view.findViewById(R.id.editJamBerhenti);
        final TextView totalJamKerja = view.findViewById(R.id.tvTotalJamKerja);
        final EditText hourMeter = view.findViewById(R.id.editHourMeter);
        final EditText jlhBalokTerpakai = view.findViewById(R.id.editJlhBalokTerpakai);
        final EditText beratBalokTim = view.findViewById(R.id.editBeratBalokTim);
        final EditText beratBalok = view.findViewById(R.id.editBeratBalok);
        final EditText remark = view.findViewById(R.id.editRemark);
        final EditText noTruk = view.findViewById(R.id.editNoTruk);
        final EditText noPlat = view.findViewById(R.id.editNoPlat);
        final EditText noSuket = view.findViewById(R.id.editSuket);
        final EditText supplier = view.findViewById(R.id.editSupplier);
        final EditText jlhBatang = view.findViewById(R.id.editJlhBatang);
        final Spinner spinSpecialCondition = view.findViewById(R.id.spinSpecialCondition);
        final Spinner spinOperator1 = view.findViewById(R.id.spinOperator1);
        final Spinner spinOperator2 = view.findViewById(R.id.spinOperator2);

        setupTimePicker(jamMulai, jamMulai, jamBerhenti, totalJamKerja, tanggal,
                () -> {
                    MstMejaData selectedMeja = (MstMejaData) spinMeja.getSelectedItem();
                    return (selectedMeja != null) ? selectedMeja.getNoMeja() : "";
                },
                "");

        setupTimePicker(jamBerhenti, jamMulai, jamBerhenti, totalJamKerja, tanggal,
                () -> {
                    MstMejaData selectedMeja = (MstMejaData) spinMeja.getSelectedItem();
                    return (selectedMeja != null) ? selectedMeja.getNoMeja() : "";
                },
                "");

        // Set default jam 08:00 ke jamMulai
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String defaultTime = LocalTime.of(8, 0).format(formatter);
        jamMulai.setText(defaultTime);

        // Siapkan adapter suggestion Kayu Bulat
        ArrayAdapter<String> kbAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
        noKB.setAdapter(kbAdapter);

        // ===== SETUP NoMeja Spinner =====
        loadMejaToSpinner(spinMeja, null, spinOperator1, spinOperator2, tanggal, jamBerhenti, jamMulai, totalJamKerja, false);

        // Set default prefix "A."
        noKB.setText("A.");
        noKB.setSelection(noKB.getText().length());

        // Setup TextWatcher untuk noKB
        setupNoKBTextWatcher(noKB, kbAdapter, view, jenisKayu, supplier, noPlat, noTruk, noSuket);

        // Mengambil tanggal hari ini
        setTodayDateToEditText(tanggal);

        // Deklarasikan objek dialog di luar listener
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        // Memanggil metode untuk memuat data ke Spinner
        loadSpecialConditionToSpinner(spinSpecialCondition, 0);
        loadOperatorToSpinner(spinOperator1, -1);
        loadOperatorToSpinner(spinOperator2, -1);
        loadShiftToSpinner(spinShift, "1");

        // Set OnClickListener untuk tglMasuk (datepicker)
        tanggal.setOnClickListener(v -> showDatePickerDialog(tanggal));

        // Menambahkan onClickListener untuk tombol tutup dialog
        ImageButton btnCloseDialogInputHeader = view.findViewById(R.id.btnCloseDialogInputHeader);
        btnCloseDialogInputHeader.setOnClickListener(v -> {
            dialog.dismiss();
        });

        // Setup tombol simpan
        setupSaveButton(view, dialog, noKB, tanggal, jenisKayu, spinMeja, spinShift, jamMulai, jamBerhenti,
                totalJamKerja, hourMeter, jlhBalokTerpakai, beratBalokTim, beratBalok, remark, jlhBatang,
                spinSpecialCondition, spinOperator1, spinOperator2);

        // Menampilkan Dialog
        dialog.show();

        // Set ukuran dialog agar dropdown tidak terpotong
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }


    private void loadMejaToSpinner(final Spinner spinMeja, @Nullable String selectedNoMeja,
                                   Spinner spinOperator1, Spinner spinOperator2,
                                   EditText tanggal, EditText jamBerhenti, EditText jamMulai,
                                   TextView totalJamKerja, boolean isEdit) {

        final List<MstMejaData> mejaList = new ArrayList<>();
        mejaList.add(new MstMejaData(null, "PILIH"));

        executorService.execute(() -> {
            List<MstMejaData> result = SawmillApi.getAllMeja();
            mejaList.addAll(result);

            runOnUiThread(() -> {
                ArrayAdapter<MstMejaData> adapter = new ArrayAdapter<>(Sawmill.this,
                        android.R.layout.simple_spinner_item, mejaList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinMeja.setAdapter(adapter);

                // Pilih default jika sudah ada yang dipilih sebelumnya
                if (selectedNoMeja != null) {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        MstMejaData item = adapter.getItem(i);
                        if (item != null && selectedNoMeja.equals(item.getNoMeja())) {
                            spinMeja.setSelection(i);
                            break;
                        }
                    }
                }
            });
        });

        spinMeja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MstMejaData selected = (MstMejaData) spinMeja.getSelectedItem();
                if (selected != null && selected.getNoMeja() != null) {
                    final String noMeja = selected.getNoMeja();

                    // Ambil operator dan jam terakhir berdasarkan NoMeja
                    executorService.execute(() -> {
                        SawmillData data = SawmillApi.getOperatorByNoMeja(noMeja);
                        if (data != null && !isEdit) {
                            runOnUiThread(() -> {
                                loadOperatorToSpinner(spinOperator1, data.getIdOperator1());
                                loadOperatorToSpinner(spinOperator2, data.getIdOperator2());
                            });
                        }
                    });

                    String tgl = tanggal.getText().toString().trim();
                    executorService.execute(() -> {
                        String hourEnd = SawmillApi.getHourEndByTanggalShiftMeja(tgl, noMeja);

                        runOnUiThread(() -> {
                            boolean validHourEnd = hourEnd != null && !hourEnd.isEmpty() && !hourEnd.equals("-");

                            if (isEdit) {
                                // Dalam mode edit, selalu aktifkan input
                                jamMulai.setEnabled(true);
                                jamBerhenti.setEnabled(true);
                                jamMulai.setBackgroundResource(R.drawable.border_input);
                                jamBerhenti.setBackgroundResource(R.drawable.border_input);
                            } else if (validHourEnd) {
                                // Mode input baru dan hourEnd valid
                                jamMulai.setText(formatTimeToHHmmss(hourEnd));
                                jamMulai.setEnabled(true);
                                jamBerhenti.setEnabled(true);
                                jamMulai.setBackgroundResource(R.drawable.border_input);
                                jamBerhenti.setBackgroundResource(R.drawable.border_input);
                            } else if ("-".equals(hourEnd)) {
                                // hourEnd = "-" menandakan kondisi khusus
                                jamMulai.setText("08:00");
                                jamMulai.setEnabled(true);
                                jamBerhenti.setEnabled(true);
                                jamMulai.setBackgroundResource(R.drawable.border_input);
                                jamBerhenti.setBackgroundResource(R.drawable.border_input);
                            } else {
                                // hourEnd null atau tidak valid → disable input
                                jamMulai.setText("");
                                jamMulai.setHint("Proses..");
                                jamBerhenti.setText("");
                                totalJamKerja.setText("0");
                                jamMulai.setEnabled(false);
                                jamBerhenti.setEnabled(false);
                                jamMulai.setBackgroundResource(R.drawable.border_rejected);
                                jamBerhenti.setBackgroundResource(R.drawable.border_rejected);
                            }
                        });

                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }




    // Method terpisah untuk setup TextWatcher noKB
    private void setupNoKBTextWatcher(AutoCompleteTextView noKB, ArrayAdapter<String> kbAdapter, View view,
                                      EditText jenisKayu, EditText supplier, EditText noPlat,
                                      EditText noTruk, EditText noSuket) {

        noKB.addTextChangedListener(new TextWatcher() {
            boolean isEditing;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;
                isEditing = true;

                String text = s.toString();

                // Pastikan prefix "A." selalu ada
                if (!text.startsWith("A.")) {
                    noKB.setText("A.");
                    noKB.setSelection(noKB.getText().length());
                    isEditing = false;
                    return;
                }

                // Ambil substring angka setelah prefix "A."
                String angka = "";
                if (text.length() > 2) {
                    angka = text.substring(2);
                }

                // Batasi input angka maksimal 6 digit
                if (angka.length() > 6) {
                    angka = angka.substring(0, 6);
                    noKB.setText("A." + angka);
                    noKB.setSelection(noKB.getText().length());
                }

                // Tampilkan suggestion jika minimal 4 digit angka sudah diinput
                if (angka.length() >= 4 && angka.length() < 6) {
                    loadNoKayuBulatSuggestions("A." + angka, kbAdapter, noKB);
                    jenisKayu.setText("-");
                    supplier.setText("-");
                    noPlat.setText("-");
                    noTruk.setText("-");
                    noSuket.setText("-");

                    noKB.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    noKB.setBackgroundResource(R.drawable.border_input);
                }
                else if (angka.length() == 6) {
                    executorService.execute(() -> {
                        KayuBulatData data = SawmillApi.getKayuBulatDetail(text);
                        runOnUiThread(() -> {
                            if (data != null) {
                                // Jika data valid
                                jenisKayu.setText(data.getJenis());
                                supplier.setText(data.getSupplier());
                                noPlat.setText(data.getNoPlat());
                                noTruk.setText(data.getNoTruk());
                                noSuket.setText(data.getSuket());

                                Drawable checkIcon = ContextCompat.getDrawable(view.getContext(), R.drawable.ic_done);
                                if (checkIcon != null) {
                                    checkIcon.setTint(Color.parseColor("#4CAF50")); // hijau
                                    isValidNoKB = true;
                                }

                                noKB.setCompoundDrawablesWithIntrinsicBounds(null, null, checkIcon, null);
                                noKB.setBackgroundResource(R.drawable.border_accepted);
                            } else {
                                // Jika data tidak valid
                                jenisKayu.setText("-");
                                supplier.setText("-");
                                noPlat.setText("-");
                                noTruk.setText("-");
                                noSuket.setText("-");

                                Drawable errorIcon = ContextCompat.getDrawable(view.getContext(), R.drawable.ic_undone); // ikon salah
                                if (errorIcon != null) {
                                    errorIcon.setTint(Color.RED);
                                    isValidNoKB = false;
                                }

                                noKB.setCompoundDrawablesWithIntrinsicBounds(null, null, errorIcon, null);
                                noKB.setBackgroundResource(R.drawable.border_rejected);
                            }
                        });
                    });

                }
                else {
                    kbAdapter.clear();
                    kbAdapter.notifyDataSetChanged();
                }

                noKB.setSelection(noKB.getText().length());
                isEditing = false;
            }
        });
    }


    private void setupNoKBTextWatcherEdit(AutoCompleteTextView noKB, ArrayAdapter<String> kbAdapter, View view,
                                          EditText jenisKayu, EditText supplier, EditText noPlat,
                                          EditText noTruk, EditText noSuket, String jenisKayuRaw) {

        noKB.addTextChangedListener(new TextWatcher() {
            boolean isEditing;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;
                isEditing = true;

                String text = s.toString();

                // Pastikan prefix "A." selalu ada
                if (!text.startsWith("A.")) {
                    noKB.setText("A.");
                    noKB.setSelection(noKB.getText().length());
                    isEditing = false;
                    return;
                }

                // Ambil substring angka setelah prefix "A."
                String angka = "";
                if (text.length() > 2) {
                    angka = text.substring(2);
                }

                // Batasi input angka maksimal 6 digit
                if (angka.length() > 6) {
                    angka = angka.substring(0, 6);
                    noKB.setText("A." + angka);
                    noKB.setSelection(noKB.getText().length());
                }

                // Tampilkan suggestion jika minimal 4 digit angka sudah diinput
                if (angka.length() >= 4 && angka.length() < 6) {
                    loadNoKayuBulatSuggestions("A." + angka, kbAdapter, noKB);
                    jenisKayu.setText("-");
                    supplier.setText("-");
                    noPlat.setText("-");
                    noTruk.setText("-");
                    noSuket.setText("-");

                    noKB.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    noKB.setBackgroundResource(R.drawable.border_input);
                }
                else if (angka.length() == 6) {
                    executorService.execute(() -> {
                        KayuBulatData data = SawmillApi.getKayuBulatDetail(text);

                        runOnUiThread(() -> {
                            if (data != null) {
                                String jenisBaru = data.getJenis().toLowerCase();

                                boolean isLamaRambung = jenisKayuRaw.toLowerCase().contains("rambung");
                                boolean isBaruRambung = jenisBaru.toLowerCase().contains("rambung");

                                boolean isValidKategori = isLamaRambung == isBaruRambung;

                                if (isValidKategori) {
                                    // Set field karena valid
                                    jenisKayu.setText(data.getJenis());
                                    supplier.setText(data.getSupplier());
                                    noPlat.setText(data.getNoPlat());
                                    noTruk.setText(data.getNoTruk());
                                    noSuket.setText(data.getSuket());

                                    Drawable checkIcon = ContextCompat.getDrawable(view.getContext(), R.drawable.ic_done);
                                    if (checkIcon != null) {
                                        checkIcon.setTint(Color.parseColor("#4CAF50")); // hijau
                                        isValidNoKB = true;
                                    }

                                    noKB.setCompoundDrawablesWithIntrinsicBounds(null, null, checkIcon, null);
                                    noKB.setBackgroundResource(R.drawable.border_accepted);

                                } else {
                                    // Tidak valid karena beda kategori
                                    jenisKayu.setText(data.getJenis());
                                    supplier.setText(data.getSupplier());
                                    noPlat.setText(data.getNoPlat());
                                    noTruk.setText(data.getNoTruk());
                                    noSuket.setText(data.getSuket());

                                    Drawable errorIcon = ContextCompat.getDrawable(view.getContext(), R.drawable.ic_undone);
                                    if (errorIcon != null) {
                                        errorIcon.setTint(Color.RED);
                                        isValidNoKB = false;
                                    }

                                    if (jenisKayuRaw.toLowerCase().contains("rambung")) {
                                        noKB.setError("No KB hanya bisa diubah dari Rambung ke Rambung.");
                                    } else {
                                        noKB.setError("No KB hanya bisa diubah dari Non-Rambung ke Non-Rambung.");
                                    }

                                    noKB.setCompoundDrawablesWithIntrinsicBounds(null, null, errorIcon, null);
                                    noKB.setBackgroundResource(R.drawable.border_rejected);
                                }

                            } else {
                                // Jika data tidak ditemukan
                                jenisKayu.setText("-");
                                supplier.setText("-");
                                noPlat.setText("-");
                                noTruk.setText("-");
                                noSuket.setText("-");

                                Drawable errorIcon = ContextCompat.getDrawable(view.getContext(), R.drawable.ic_undone);
                                if (errorIcon != null) {
                                    errorIcon.setTint(Color.RED);
                                    isValidNoKB = false;
                                }

                                noKB.setCompoundDrawablesWithIntrinsicBounds(null, null, errorIcon, null);
                                noKB.setBackgroundResource(R.drawable.border_rejected);
                            }
                        });
                    });
                }
                else {
                    kbAdapter.clear();
                    kbAdapter.notifyDataSetChanged();
                }
                noKB.setSelection(noKB.getText().length());
                isEditing = false;
            }
        });
    }

    // Method terpisah untuk setup tombol simpan
    private void setupSaveButton(View view, AlertDialog dialog, AutoCompleteTextView noKB, EditText tanggal,
                                 EditText jenisKayu, Spinner spinMeja, Spinner spinShift,
                                 EditText jamMulai, EditText jamBerhenti, TextView totalJamKerja,
                                 EditText hourMeter, EditText jlhBalokTerpakai, EditText beratBalokTim, EditText beratBalok,
                                 EditText remark, EditText jlhBatang, Spinner spinSpecialCondition,
                                 Spinner spinOperator1, Spinner spinOperator2) {

        Button btnSave = view.findViewById(R.id.btnSimpan);
        btnSave.setOnClickListener(v -> {
            btnSave.setEnabled(false);
            loadingDialogHelper.show(view.getContext());

            // Ambil data dari EditText dan Spinner
            SpecialConditionData selectedSpecialCondition = (SpecialConditionData) spinSpecialCondition.getSelectedItem();
            MstOperatorData selectedOperator1 = (MstOperatorData) spinOperator1.getSelectedItem();
            MstOperatorData selectedOperator2 = (MstOperatorData) spinOperator2.getSelectedItem();
            String selectedShiftStr = (String) spinShift.getSelectedItem();

            final int idSpecialConditionVal = (selectedSpecialCondition != null && selectedSpecialCondition.getIdSawmillSpecialCondition() != null) ? selectedSpecialCondition.getIdSawmillSpecialCondition() : -1;
            final int idOperator1Val = (selectedOperator1 != null && selectedOperator1.getIdOperator() != null) ? selectedOperator1.getIdOperator() : -1;
            final int idOperator2Val = (selectedOperator2 != null && selectedOperator2.getIdOperator() != null) ? selectedOperator2.getIdOperator() : -1;
            final int shiftVal = (!selectedShiftStr.equals("PILIH")) ? Integer.parseInt(selectedShiftStr) : -1;

            String noKBVal = noKB.getText().toString();
            String tanggalVal = tanggal.getText().toString();
            String remarkVal = remark.getText().toString();
            String inputJlhBalokTerpakai = jlhBalokTerpakai.getText().toString().trim();
            int jlhBalokTerpakaiVal = inputJlhBalokTerpakai.isEmpty() ? 0 : Integer.parseInt(inputJlhBalokTerpakai);
            MstMejaData selectedMeja = (MstMejaData) spinMeja.getSelectedItem();
            String noMejaVal = selectedMeja != null ? selectedMeja.getNoMeja() : "";
            String hourMeterVal = hourMeter.getText().toString();
            String jlhBatangVal = jlhBatang.getText().toString();
            String beratBalokTimVal = beratBalokTim.getText().toString();
            String beratBalokVal = beratBalok.getText().toString();
            String jenisKayuVal = jenisKayu.getText().toString();
            String jamMulaiVal = jamMulai.getText().toString();
            String jamBerhentiVal = jamBerhenti.getText().toString();
            String totalJamKerjaVal = totalJamKerja.getText().toString();

            // Validasi
            if (validateInputs(spinSpecialCondition, spinOperator1, spinShift, jlhBalokTerpakai,
                    spinMeja, beratBalokTim, beratBalok, idSpecialConditionVal, idOperator1Val,
                    shiftVal, noMejaVal, beratBalokTimVal, beratBalokVal, noKB, jamMulai, jamMulaiVal)) {

                // Simpan data
                saveData(dialog, shiftVal, tanggalVal, noKBVal, noMejaVal, idSpecialConditionVal,
                        jlhBalokTerpakaiVal, jlhBatangVal, hourMeterVal, idOperator1Val, idOperator2Val,
                        remarkVal, beratBalokTimVal, beratBalokVal, jenisKayuVal, jamMulaiVal, jamBerhentiVal, totalJamKerjaVal,
                        btnSave, loadingDialogHelper);
            } else {
                loadingDialogHelper.hide();
                btnSave.setEnabled(true);
                Toast.makeText(this, "Data belum lengkap!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method untuk validasi input
    private boolean validateInputs(Spinner spinSpecialCondition, Spinner spinOperator1, Spinner spinShift,
                                   EditText jlhBalokTerpakai, Spinner spinMeja, EditText beratBalokTim, EditText beratBalok,
                                   int idSpecialConditionVal, int idOperator1Val, int shiftVal,
                                   String noMejaVal, String beratBalokTimVal, String beratBalokVal, AutoCompleteTextView noKB, EditText jamMulai, String jamMulaiVal) {
        boolean valid = true;

        if (idSpecialConditionVal == -1) {
            spinSpecialCondition.setBackgroundResource(R.drawable.spinner_error);
            valid = false;
        }

        if (idOperator1Val == -1) {
            spinOperator1.setBackgroundResource(R.drawable.spinner_error);
            valid = false;
        }

        if (shiftVal == -1) {
            spinShift.setBackgroundResource(R.drawable.spinner_error);
            valid = false;
        }

        if (jlhBalokTerpakai.getText().toString().trim().isEmpty()) {
            jlhBalokTerpakai.setError("Jumlah Balok Terpakai Harus Diisi!");
            valid = false;
        }

        if (jamMulaiVal.isEmpty()) {
            jamMulai.setError("Jam Mulai harus diisi!");
            valid = false;
        }

        // ✅ Validasi Spinner Meja
        MstMejaData selectedMeja = (MstMejaData) spinMeja.getSelectedItem();
        if (selectedMeja == null || selectedMeja.getNoMeja() == null || selectedMeja.getNoMeja().isEmpty()) {
            spinMeja.setBackgroundResource(R.drawable.spinner_error);
            valid = false;
        }

        if (!isValidNoKB) {
            noKB.setError("No Kayu Bulat Tidak Valid!");
            valid = false;
        }

        return valid;
    }


    // Method untuk menyimpan data
    private void saveData(AlertDialog dialog, int shiftVal, String tanggalVal, String noKBVal,
                          String noMejaVal, int idSpecialConditionVal, int jlhBalokTerpakaiVal,
                          String jlhBatangVal, String hourMeterVal, int idOperator1Val, int idOperator2Val,
                          String remarkVal, String beratBalokTimVal, String beratBalokVal, String jenisKayuVal,
                          String jamMulaiVal, String jamBerhentiVal, String totalJamKerjaVal,
                          Button btnSave, LoadingDialogHelper loadingDialogHelper) {

        executorService.execute(() -> {
            String newNoTellySawmill = SawmillApi.getNextNoTellySawmill();

            if (newNoTellySawmill != null && !newNoTellySawmill.isEmpty()) {
                try {
                    SawmillApi.insertSawmillData(
                            newNoTellySawmill, shiftVal, tanggalVal, noKBVal, noMejaVal,
                            idSpecialConditionVal, jlhBalokTerpakaiVal, jlhBatangVal, hourMeterVal,
                            idOperator1Val, idOperator2Val, remarkVal, beratBalokTimVal, beratBalokVal, jenisKayuVal,
                            jamMulaiVal, jamBerhentiVal, totalJamKerjaVal
                    );

                    runOnUiThread(() -> {
                        loadingDialogHelper.hide();
                        btnSave.setEnabled(true);
                        showSuccessDialog(newNoTellySawmill, dialog);
                    });

                } catch (Exception e) {
                    runOnUiThread(() -> {
                        loadingDialogHelper.hide();
                        btnSave.setEnabled(true);
                        Toast.makeText(this, "Tidak dapat menyimpan, " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });

                }
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Gagal mengambil NoTellySawmill", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            }
        });
    }

    // Method untuk menampilkan dialog sukses
    private void showSuccessDialog(String newNoTellySawmill, AlertDialog parentDialog) {
        AlertDialog.Builder builderAlert = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.alert_success, null);

        TextView tvMessage = dialogView.findViewById(R.id.tvMessage);
        Button btnOk = dialogView.findViewById(R.id.btnOk);

        tvMessage.setText("Berhasil disimpan dengan No.Telly " + newNoTellySawmill);

        builderAlert.setView(dialogView);
        AlertDialog dialogSuccess = builderAlert.create();
        dialogSuccess.setCancelable(false);
        dialogSuccess.show();

        dialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSuccess.getWindow().setLayout(1000, ViewGroup.LayoutParams.WRAP_CONTENT);

        btnOk.setOnClickListener(a -> {
            loadDataAndDisplayTable();
            dialogSuccess.dismiss();
            parentDialog.dismiss();
        });
    }

    private void loadDataAndDisplayTable() {
        mainLoadingIndicator.setVisibility(View.VISIBLE);
        isLoading = true;

        executorService.execute(() -> {
            // Ambil data halaman pertama
            List<SawmillData> newData = SawmillApi.getSawmillData(currentPage, PAGE_SIZE, currentSearchKeyword);
            tglTutupTransaksi = SawmillApi.getTanggalTutupTransaksi();

            runOnUiThread(() -> {
                dataList.clear(); // clear hanya saat page pertama
                dataList.addAll(newData);

                mainTable.removeAllViews(); // hapus tampilan lama
                for (SawmillData data : newData) {
                    addTableRow(data); // pakai method baru ini, pengganti populateTable
                }

                mainLoadingIndicator.setVisibility(View.GONE);
                isLoading = false;
            });
        });
    }

    private void addTableRow(SawmillData data) {
        TableRow row = new TableRow(this);
        row.setTag(data);

        int rowIndex = mainTable.getChildCount(); // untuk warna selang-seling

        TextView col1 = createTextView(data.getNoSTSawmill(), 1.0f);
        TextView col2 = createTextView(data.getTglSawmill(), 1.0f);
        TextView col3 = createTextView(data.getNoKayuBulat(), 1.0f);
        TextView col4 = createTextView(data.getNamaJenisKayu(), 1.0f);
        TextView col5 = createTextView(String.valueOf(data.getOperator()), 1.0f);

        String jamKerja = data.getJamKerja();
        if (jamKerja == null || jamKerja.trim().equals("0.0") || jamKerja.trim().isEmpty()) {
            jamKerja = "?";
        }
        TextView col6 = createTextView(jamKerja, 0.5f);

        TextView col7 = createTextView(data.getNamaMeja(), 0.5f);
        TextView col8 = createTextView(String.valueOf(data.getBalokTerpakai()), 0.5f);

        setDateToView(data.getTglSawmill(), col2);

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
        row.addView(col8);

        // Background warna selang-seling
        row.setBackgroundColor(ContextCompat.getColor(this,
                rowIndex % 2 == 0 ? R.color.background_cream : R.color.white));

        if (data.getJamKerja() == null || data.getJamKerja().trim().equals("0.0") || data.getJamKerja().trim().isEmpty()) {
            col6.setBackgroundColor(Color.parseColor("#FFF176"));
        }

        // Klik biasa
        row.setOnClickListener(v -> {
            if (selectedRowMain != null && selectedRowMain != row) {
                int previousIndex = mainTable.indexOfChild(selectedRowMain);
                SawmillData previousData = (SawmillData) selectedRowMain.getTag(); // tag sudah di-set sebelumnya
                resetRowColor(selectedRowMain, previousIndex, previousData);
            }

            row.setBackgroundResource(R.drawable.row_selector);
            setTextColor(row, R.color.white);

            // col6 ikut diubah saat dipilih
            TextView selectedCol6 = (TextView) row.getChildAt(10); // posisi col6
            selectedCol6.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent)); // hilangkan kuning, ikuti selector

            selectedRowMain = row;
        });


        // Long click dengan touch listener untuk mendapatkan koordinat
        row.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Simpan koordinat klik
                touchX = event.getRawX();
                touchY = event.getRawY();
            }
            return false; // Biarkan event dilanjutkan ke listener lain
        });

        // Long click
        row.setOnLongClickListener(v -> {
            if (selectedRowMain != null && selectedRowMain != row) {
                int previousIndex = mainTable.indexOfChild(selectedRowMain);
                SawmillData previousData = (SawmillData) selectedRowMain.getTag();

                // Reset background row sebelumnya
                int bgColor = (previousIndex % 2 == 0)
                        ? ContextCompat.getColor(this, R.color.background_cream)
                        : ContextCompat.getColor(this, R.color.white);
                selectedRowMain.setBackgroundColor(bgColor);

                // Reset col6 sebelumnya
                TextView previousCol6 = (TextView) selectedRowMain.getChildAt(10); // pastikan index col6 benar
                if (previousData.getJamKerja() == null || previousData.getJamKerja().trim().equals("0.0") || previousData.getJamKerja().trim().isEmpty()) {
                    previousCol6.setBackgroundColor(Color.parseColor("#FFF176")); // kembali kuning
                } else {
                    previousCol6.setBackgroundColor(bgColor);
                }

                resetTextColor(selectedRowMain);
            }

            // Highlight baris sekarang
            row.setBackgroundResource(R.drawable.row_selector);
            setTextColor(row, R.color.white);

            // Highlight col6 juga
            TextView selectedCol6 = (TextView) row.getChildAt(10); // pastikan index benar
            selectedCol6.setBackgroundColor(Color.TRANSPARENT); // biar ikut selector row

            selectedRowMain = row;

            showRowPopup(v, data, touchX, touchY);
            return true;
        });

        mainTable.addView(row);
    }

    private void loadMoreData(boolean reset) {
        if (isLoading) return;
        if (!hasMoreData && !reset) return;

        if (reset) {
            currentPage = 0;
            hasMoreData = true;
            dataList.clear();

            // 🔥 Bersihkan UI di thread utama langsung sebelum loading
            runOnUiThread(() -> {
                mainTable.removeAllViews();
                showLoadingRow(); // tampilkan loading di area yang sudah dibersihkan
            });
        } else {
            showLoadingRow(); // tampilkan loading jika bukan reset
        }

        isLoading = true;

        int pageToLoad = reset ? 1 : currentPage + 1;

        executorService.execute(() -> {
            Log.d("loadMoreData", "Page: " + pageToLoad + ", Keyword: '" + currentSearchKeyword + "'");

            List<SawmillData> moreData = SawmillApi.getSawmillData(pageToLoad, PAGE_SIZE, currentSearchKeyword);

            runOnUiThread(() -> {
                hideLoadingRow();

                if (!moreData.isEmpty()) {
                    currentPage = pageToLoad;
                    dataList.addAll(moreData);

                    for (SawmillData data : moreData) {
                        addTableRow(data);
                    }

                    if (moreData.size() < PAGE_SIZE) {
                        hasMoreData = false; // data sudah habis
                    }
                } else {
                    hasMoreData = false; // tidak ada data sama sekali
                }

                isLoading = false;
            });
        });
    }

    private void populateTable(List<SawmillData> dataList) {
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

        for (SawmillData data : dataList) {
            TableRow row = new TableRow(this);
            row.setTag(data);

            TextView col1 = createTextView(data.getNoSTSawmill(), 1.0f);
            TextView col2 = createTextView(data.getTglSawmill(), 1.0f);
            TextView col3 = createTextView(data.getNoKayuBulat(), 1.0f);
            TextView col4 = createTextView(data.getNamaJenisKayu(), 1.0f);
            TextView col5 = createTextView(String.valueOf(data.getOperator()), 1.0f);

            String jamKerja = data.getJamKerja();
            if (jamKerja == null || jamKerja.trim().equals("0.0") || jamKerja.trim().isEmpty()) {
                jamKerja = "?";
            }
            TextView col6 = createTextView(jamKerja, 0.5f);

            TextView col7 = createTextView(data.getNamaMeja(), 0.5f);
            TextView col8 = createTextView(String.valueOf(data.getBalokTerpakai()), 0.5f);

            setDateToView(data.getTglSawmill(), col2);

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
            row.addView(col8);

            // Background bergantian
            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }

            if (data.getJamKerja() == null || data.getJamKerja().trim().equals("0.0") || data.getJamKerja().trim().isEmpty()) {
                col6.setBackgroundColor(Color.parseColor("#FFF176")); // kuning terang (material yellow 300)
            }

            // Klik biasa
            row.setOnClickListener(v -> {
                if (selectedRowMain != null && selectedRowMain != row) {
                    int previousIndex = mainTable.indexOfChild(selectedRowMain);
                    SawmillData previousData = (SawmillData) selectedRowMain.getTag(); // tag sudah di-set sebelumnya
                    resetRowColor(selectedRowMain, previousIndex, previousData);
                }

                row.setBackgroundResource(R.drawable.row_selector);
                setTextColor(row, R.color.white);

                // col6 ikut diubah saat dipilih
                TextView selectedCol6 = (TextView) row.getChildAt(10); // posisi col6
                selectedCol6.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent)); // hilangkan kuning, ikuti selector

                selectedRowMain = row;
            });


            // Long click dengan touch listener untuk mendapatkan koordinat
            row.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Simpan koordinat klik
                    touchX = event.getRawX();
                    touchY = event.getRawY();
                }
                return false; // Biarkan event dilanjutkan ke listener lain
            });

            // Long click
            row.setOnLongClickListener(v -> {
                if (selectedRowMain != null && selectedRowMain != row) {
                    int previousIndex = mainTable.indexOfChild(selectedRowMain);
                    SawmillData previousData = (SawmillData) selectedRowMain.getTag();

                    // Reset background row sebelumnya
                    int bgColor = (previousIndex % 2 == 0)
                            ? ContextCompat.getColor(this, R.color.background_cream)
                            : ContextCompat.getColor(this, R.color.white);
                    selectedRowMain.setBackgroundColor(bgColor);

                    // Reset col6 sebelumnya
                    TextView previousCol6 = (TextView) selectedRowMain.getChildAt(10); // pastikan index col6 benar
                    if (previousData.getJamKerja() == null || previousData.getJamKerja().trim().equals("0.0") || previousData.getJamKerja().trim().isEmpty()) {
                        previousCol6.setBackgroundColor(Color.parseColor("#FFF176")); // kembali kuning
                    } else {
                        previousCol6.setBackgroundColor(bgColor);
                    }

                    resetTextColor(selectedRowMain);
                }

                // Highlight baris sekarang
                row.setBackgroundResource(R.drawable.row_selector);
                setTextColor(row, R.color.white);

                // Highlight col6 juga
                TextView selectedCol6 = (TextView) row.getChildAt(10); // pastikan index benar
                selectedCol6.setBackgroundColor(Color.TRANSPARENT); // biar ikut selector row

                selectedRowMain = row;

                showRowPopup(v, data, touchX, touchY);
                return true;
            });


            mainTable.addView(row);
            rowIndex++;
        }
    }

    private void resetRowColor(TableRow row, int index, SawmillData data) {
        int backgroundColor = (index % 2 == 0)
                ? ContextCompat.getColor(this, R.color.background_cream)
                : ContextCompat.getColor(this, R.color.white);

        row.setBackgroundColor(backgroundColor);

        // Reset col6 (JamKerja)
        TextView col6 = (TextView) row.getChildAt(10); // col6 = index ke-10 (kalau ada divider)
        if (data.getJamKerja() == null || data.getJamKerja().trim().equals("0.0") || data.getJamKerja().trim().isEmpty()) {
            col6.setBackgroundColor(Color.parseColor("#FFF176"));
        } else {
            col6.setBackgroundColor(backgroundColor); // samakan dengan row
        }

        resetTextColor(row);
    }



    private void showRowPopup(View anchorView, SawmillData data, float x, float y) {
        // Inflate popup layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_menu_row_sawmill, null);

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

        // Temukan tombol di popup
        Button btnFinishKB = popupView.findViewById(R.id.btnFinishKB);
        Button btnAddDetail = popupView.findViewById(R.id.btnAddDetail);

        // Anggap tglTutupTransaksi sudah kamu ambil dari database sebelumnya
        if (isTutupTransaksi(data.getTglSawmill(), tglTutupTransaksi)) {
            btnFinishKB.setEnabled(false);
            btnFinishKB.setAlpha(0.5f); // opsional
        } else {
            btnFinishKB.setEnabled(true);
            btnFinishKB.setAlpha(1.0f); // pastikan aktif
        }

        // Atur listener untuk tombol
        btnFinishKB.setOnClickListener(v -> {
            popupWindow.dismiss();
            setFinishedKayuBulat(data);
        });

        btnAddDetail.setOnClickListener(v -> {
            popupWindow.dismiss();
            onRowClick(data);
        });

        // Ukur popup untuk mendapatkan ukurannya
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = popupView.getMeasuredWidth();
        int popupHeight = popupView.getMeasuredHeight();

        // Dapatkan koordinat layar dan ukuran layar
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Hitung posisi popup
        int popupX = (int) x - (popupWidth / 2); // Center horizontally pada posisi klik
        int popupY = (int) y - popupHeight - 50; // Tampilkan di atas posisi klik dengan margin

        // Pastikan popup tidak keluar dari layar
        if (popupX < 0) {
            popupX = 10; // Margin dari kiri
        } else if (popupX + popupWidth > screenWidth) {
            popupX = screenWidth - popupWidth - 10; // Margin dari kanan
        }

        if (popupY < 0) {
            popupY = (int) y + 50; // Jika tidak muat di atas, tampilkan di bawah
        }

        // Tampilkan popup di koordinat yang dihitung
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, popupX, popupY);
    }


    private void setFinishedKayuBulat(SawmillData data) {
        // Jalankan dialog konfirmasi di UI Thread
        runOnUiThread(() -> {
            new AlertDialog.Builder(this)
                    .setTitle("Konfirmasi")
                    .setMessage("Apakah Anda yakin ingin menyelesaikan Kayu Bulat dengan Nomor " + data.getNoKayuBulat() +
                            " ?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        // Lanjutkan proses insert di background thread
                        executorService.execute(() -> {
                            if (data.getStokTersedia() > 0) {
                                runOnUiThread(() ->
                                        Toast.makeText(this, "Tidak dapat menyelesaikan. Masih tersisa " + data.getStokTersedia() + " balok!", Toast.LENGTH_SHORT).show()
                                );
                                return;
                            }

                            String success = SawmillApi.insertPenerimaanSTSawmillWithDetail(data.getNoKayuBulat());

                            runOnUiThread(() -> {
                                if (success == null) {
                                    // Tampilkan dialog sukses
                                    AlertDialog.Builder builderAlert = new AlertDialog.Builder(this);
                                    View dialogView = getLayoutInflater().inflate(R.layout.alert_success, null);

                                    TextView tvMessage = dialogView.findViewById(R.id.tvMessage);
                                    Button btnOk = dialogView.findViewById(R.id.btnOk);

                                    tvMessage.setText("Berhasil Update Status Kayu Bulat " + data.getNoKayuBulat());

                                    builderAlert.setView(dialogView);
                                    AlertDialog dialogSuccess = builderAlert.create();
                                    dialogSuccess.setCancelable(false);

                                    dialogSuccess.show();
                                    dialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialogSuccess.getWindow().setLayout(
                                            1000, ViewGroup.LayoutParams.WRAP_CONTENT
                                    );

                                    btnOk.setOnClickListener(a -> {
                                        loadDataAndDisplayTable();
                                        dialogSuccess.dismiss();
                                    });
                                } else {
                                    Toast.makeText(this, "No Kayu Bulat telah diselesaikan pada " + formatDate(success), Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });
    }




    // Modifikasi pada populateDetailTable
    private void populateDetailTable(TableLayout tableLayout, List<SawmillDetailData> dataList, String noSTSawmill, String jenisKayu, int idJenisKayu) {
        selectedRowDetail = null;
        tableLayout.removeAllViews();

        if (dataList == null || dataList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Tidak ada data");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            tableLayout.addView(noDataView);
            return;
        }

        int rowIndex = 0;

        for (SawmillDetailData data : dataList) {
            TableRow row = new TableRow(this);
            row.setTag(data);

            TextView col1 = createTextView(String.valueOf(data.getNoUrut()), 0.3f);
            TextView col2 = createTextView(decimalFormat.format(data.getTebal()), 0.6f);
            TextView col3 = createTextView(decimalFormat.format(data.getLebar()), 0.6f);
            TextView col4 = createTextView(decimalFormat.format(data.getPanjang()), 0.6f);
            TextView col5 = createTextView(String.valueOf(data.getPcs()), 0.6f);
            TextView col6 = createTextView(data.getNamaGrade() != null ? data.getNamaGrade() : "-", 1.0f);
            TextView col9 = createTextView(data.getIsBagusKulitLabel(), 1.0f);
            TextView col10 = createTextView(data.getNoSPK() != null ? data.getNoSPK() + "-" + data.getNamaProduk() : "-", 1.0f);

            row.addView(col1); row.addView(createDivider());
            row.addView(col2); row.addView(createDivider());
            row.addView(col3); row.addView(createDivider());
            row.addView(col4); row.addView(createDivider());
            row.addView(col5); row.addView(createDivider());
            row.addView(col6); row.addView(createDivider());
            row.addView(col9); row.addView(createDivider());
            row.addView(col10);

            int defaultColor = (rowIndex % 2 == 0) ? R.color.background_cream : R.color.white;
            row.setBackgroundColor(ContextCompat.getColor(this, defaultColor));

            // Tambahkan OnTouchListener untuk menangkap koordinat
            row.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchX = event.getRawX();
                    touchY = event.getRawY();
                }
                return false; // Tetap forward ke listener lain
            });

            // Event long click tetap sama, tapi sekarang menggunakan koordinat yang disimpan
            row.setOnLongClickListener(v -> {
                if (selectedRowDetail != null && selectedRowDetail != row) {
                    Object tag = selectedRowDetail.getTag();
                    if (tag instanceof SawmillDetailData) {
                        SawmillDetailData prevData = (SawmillDetailData) tag;
                        int prevIndex = dataList.indexOf(prevData);
                        int color = (prevIndex % 2 == 0) ? R.color.background_cream : R.color.white;
                        selectedRowDetail.setBackgroundColor(ContextCompat.getColor(this, color));
                        resetTextColor(selectedRowDetail);
                    }
                }

                row.setBackgroundResource(R.drawable.row_selector);
                setTextColor(row, R.color.white);
                selectedRowDetail = row;

                showRowPopupDetailAtTouch(v, data, noSTSawmill, idJenisKayu, jenisKayu, dataList, touchX, touchY);
                return true;
            });

            tableLayout.addView(row);
            rowIndex++;
        }
    }

    // Method untuk menampilkan popup di koordinat touch
    private void showRowPopupDetailAtTouch(View anchorView, SawmillDetailData data, String noSTSawmill, int idJenisKayu, String jenisKayu, List<SawmillDetailData> dataList, float touchX, float touchY) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_menu_edit_delete, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);

        Button btnDelete = popupView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> {
            popupWindow.dismiss();
            deleteDetail(noSTSawmill, jenisKayu, data, idJenisKayu);
        });

        Button btnEdit = popupView.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> {
            popupWindow.dismiss();
            editDetail(noSTSawmill, jenisKayu, data, idJenisKayu);
        });

        // Measure popup
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        // Hitung posisi berdasarkan koordinat touch
        int x = (int) touchX - ((popupView.getMeasuredWidth() * 2) - 100);
        int y = (int) touchY - popupView.getMeasuredHeight() - 75; // 100px di atas touch point

        // Boundary check
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        if (x < 0) x = 10;
        if (x + popupView.getMeasuredWidth() > displayMetrics.widthPixels) {
            x = displayMetrics.widthPixels - popupView.getMeasuredWidth() - 10;
        }
        if (y < 0) {
            y = (int) touchY + 50; // Tampilkan di bawah touch point
        }

        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);

        popupWindow.setOnDismissListener(() -> {
            if (selectedRowDetail != null) {
                SawmillDetailData previousData = (SawmillDetailData) selectedRowDetail.getTag();
                int previousIndex = detailDataList.indexOf(previousData);

                int color = (previousIndex % 2 == 0) ? R.color.background_cream : R.color.white;
                selectedRowDetail.setBackgroundColor(ContextCompat.getColor(this, color));
                resetTextColor(selectedRowDetail);
                selectedRowDetail = null;
            }
        });
    }

    private void editDetail(String noSTSawmill, String jenisKayu, SawmillDetailData data, int idJenisKayu) {
        boolean isRambung = jenisKayu != null && jenisKayu.toLowerCase().contains("rambung");

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_edit_detail_sawmill, null);

        EditText editTebal   = dialogView.findViewById(R.id.editTebal);
        EditText editLebar   = dialogView.findViewById(R.id.editLebar);
        EditText editPanjang = dialogView.findViewById(R.id.editPanjang);
        EditText editJumlah  = dialogView.findViewById(R.id.editJumlah);

        // Pre-fill data (pakai koma sesuai decimalFormat kamu)
        editTebal.setText(decimalFormat.format(data.getTebal()));
        editLebar.setText(decimalFormat.format(data.getLebar()));
        editPanjang.setText(decimalFormat.format(data.getPanjang()));
        editJumlah.setText(String.valueOf(data.getPcs()));

        final Spinner spinGradeDetail = dialogView.findViewById(R.id.spinGradeDetail);
        final Spinner spinSpkProduk = dialogView.findViewById(R.id.spinSpkProduk);

        RadioButton radioBagus = dialogView.findViewById(R.id.radioBagus);
        RadioButton radioKulit = dialogView.findViewById(R.id.radioKulit);

        // Pre-check radio berdasarkan data
        int isBagusKulit = data.getIsBagusKulit();
        if (isBagusKulit == 1) {
            radioBagus.setChecked(true);
        } else if (isBagusKulit == 2) {
            radioKulit.setChecked(true);
        }

        loadGradeKBToSpinner(
                spinGradeDetail,
                radioBagus,
                radioKulit,
                jenisKayu,
                data.getIdGradeKB(),
                data.getIsBagusKulit()
        );

        // ✅ Load SPK Produk ke spinner
        String token = TokenManager.getToken(this);
        if (token.isEmpty()) {
            Toast.makeText(this, "Token tidak ditemukan. Silakan login kembali", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable dulu saat loading
        spinSpkProduk.setEnabled(false);

        executorService.execute(() -> {
            List<SpkProdukData> apiResult = SawmillApi.getSpkProdukFromAPI(token, editTebal.toString(), editLebar.toString(), idJenisKayu);

            List<SpkProdukData> finalList = new ArrayList<>();

            // Default option
            finalList.add(new SpkProdukData(0, "PILIH -", "", 0.0));

            if (apiResult != null && !apiResult.isEmpty()) {
                finalList.addAll(apiResult);
            }

            runOnUiThread(() -> {
                ArrayAdapter<SpkProdukData> adapter = new ArrayAdapter<>(
                        Sawmill.this,
                        android.R.layout.simple_spinner_item,
                        finalList
                );
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinSpkProduk.setAdapter(adapter);

                // ✅ Pre-select SPK yang sudah ada di data (kalau ada)
                String existingNoSPK = data.getNoSPK();
                int existingIdProdukSPK = data.getIdProdukSPK();

                int selectedPosition = 0; // Default ke "PILIH -"

                if (existingNoSPK != null && !existingNoSPK.isEmpty() && !existingNoSPK.equals("PILIH -")) {
                    // Cari posisi SPK yang sesuai
                    for (int i = 0; i < finalList.size(); i++) {
                        SpkProdukData spk = finalList.get(i);
                        if (spk.getIdProdukSPK() == existingIdProdukSPK ||
                                (spk.getNoSPK() != null && spk.getNoSPK().equals(existingNoSPK))) {
                            selectedPosition = i;
                            break;
                        }
                    }
                }

                spinSpkProduk.setSelection(selectedPosition);
                spinSpkProduk.setEnabled(true);

                if (finalList.size() == 1) {
                    Toast.makeText(this, "SPK Produk tidak ditemukan!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        new AlertDialog.Builder(this)
                .setTitle("Edit Detail")
                .setView(dialogView)
                .setPositiveButton("Simpan", (dialog, which) -> {

                    // --- Ambil & validasi input sebagai String dulu ---
                    String tebalStr   = editTebal.getText().toString().trim();
                    String lebarStr   = editLebar.getText().toString().trim();
                    String panjangStr = editPanjang.getText().toString().trim();
                    String jumlahStr  = editJumlah.getText().toString().trim();

                    if (tebalStr.isEmpty() || lebarStr.isEmpty() ||
                            panjangStr.isEmpty() || jumlahStr.isEmpty()) {
                        Toast.makeText(this,
                                "Tebal, lebar, panjang, dan jumlah tidak boleh kosong",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    float tebal, lebar, panjang;
                    int jumlah;

                    try {
                        // Pakai DecimalFormat yang sama dengan tampilan
                        Number nTebal   = decimalFormat.parse(tebalStr);
                        Number nLebar   = decimalFormat.parse(lebarStr);
                        Number nPanjang = decimalFormat.parse(panjangStr);

                        tebal   = nTebal.floatValue();
                        lebar   = nLebar.floatValue();
                        panjang = nPanjang.floatValue();

                        // jumlah biasanya bilangan bulat
                        jumlah = Integer.parseInt(jumlahStr);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this,
                                "Format angka tidak valid. Contoh: 2,5",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Ambil nilai dari Spinner dan Radio
                    GradeKBData selectedGrade = (GradeKBData) spinGradeDetail.getSelectedItem();
                    int idGradeKB = (selectedGrade != null && selectedGrade.getIdGradeKB() != null)
                            ? selectedGrade.getIdGradeKB()
                            : -1;

                    int isBagusKulitUpdated = 0;
                    if (radioBagus.isChecked())      isBagusKulitUpdated = 1;
                    else if (radioKulit.isChecked()) isBagusKulitUpdated = 2;

                    // ✅ Ambil SPK Produk yang dipilih
                    SpkProdukData selectedSpk = (SpkProdukData) spinSpkProduk.getSelectedItem();
                    String updatedNoSPK = null;
                    int updatedIdProdukSPK = 0;
                    String updatedNamaProdukSPK = "";

                    if (selectedSpk != null &&
                            selectedSpk.getNoSPK() != null &&
                            !selectedSpk.getNoSPK().isEmpty() &&
                            !selectedSpk.getNoSPK().equals("PILIH -")) {
                        updatedNoSPK = selectedSpk.getNoSPK();
                        updatedIdProdukSPK = selectedSpk.getIdProdukSPK();
                        updatedNamaProdukSPK = selectedSpk.getNamaProduk();
                    }

                    // Update objek data (di memori)
                    data.setTebal(tebal);
                    data.setLebar(lebar);
                    data.setPanjang(panjang);
                    data.setPcs(jumlah);
                    data.setIdGradeKB(idGradeKB);
                    data.setIsBagusKulit(isBagusKulitUpdated);

                    // ✅ Update SPK data
                    data.setNoSPK(updatedNoSPK);
                    data.setIdProdukSPK(updatedIdProdukSPK);
                    data.setNamaProduk(updatedNamaProdukSPK);

                    // Proses update di background thread
                    executorService.execute(() -> {
                        // ✅ Update item individual tidak perlu lagi, langsung replace all
                        // boolean success = SawmillApi.updateSawmillDetailItem(
                        //         noSTSawmill, data.getNoUrut(), data, isRambung
                        // );

                        // ✅ Replace semua data (sudah termasuk perubahan di object data)
                        boolean success = SawmillApi.replaceAllSawmillDetailData(
                                noSTSawmill,
                                detailDataList,  // List yang sudah termasuk data yang di-update
                                jenisKayu
                        );

                        // Refresh data dari database
                        detailDataList = SawmillApi.fetchSawmillDetailData(noSTSawmill);

                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(this, "✅ Berhasil mengubah detail!", Toast.LENGTH_SHORT).show();
                                populateDetailTable(detailSawmillTableLayout, detailDataList, noSTSawmill, jenisKayu, idJenisKayu);

                                int totalPcs = 0;
                                float totalTon = calculateTotalTon(detailDataList);

                                for (SawmillDetailData d : detailDataList) {
                                    totalPcs += d.getPcs();
                                }

                                textJumlah.setText("Jumlah Batang : " + totalPcs);
                                textTon.setText("Total Ton : " + String.format("%.4f", totalTon));

                                textJumlah.setVisibility(totalPcs == 0 ? View.GONE : View.VISIBLE);
                                textTon.setVisibility(totalPcs == 0 ? View.GONE : View.VISIBLE);
                            } else {
                                Toast.makeText(this, "❌ Gagal mengubah detail!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                })
                .setNegativeButton("Batal", null)
                .show();
    }



    private void deleteDetail(String noSTSawmill, String jenisKayu, SawmillDetailData data, int idJenisKayu) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Yakin ingin menghapus detail ini?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    boolean isRambung = jenisKayu != null && jenisKayu.toLowerCase().contains("rambung");

                    executorService.execute(() -> {
                        boolean success = SawmillApi.deleteSawmillDetailItem(
                                noSTSawmill, data.getNoUrut(), isRambung
                        );

                        if (success) {
                            detailDataList.remove(data); // jika pakai ArrayList
                            // Reorder NoUrut setelah delete berhasil
                            SawmillApi.replaceAllSawmillDetailData(noSTSawmill, detailDataList, jenisKayu);
                        }

                        // Fetch ulang data setelah reorder
                        detailDataList = SawmillApi.fetchSawmillDetailData(noSTSawmill);

                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(this, "Berhasil menghapus detail!", Toast.LENGTH_SHORT).show();
                                populateDetailTable(detailSawmillTableLayout, detailDataList, noSTSawmill, jenisKayu, idJenisKayu);

                                int totalPcs = 0;
                                float totalTon = calculateTotalTon(detailDataList);

                                for (SawmillDetailData dataDetail : detailDataList) {
                                    totalPcs += dataDetail.getPcs();
                                }

                                textJumlah.setText("Jumlah Batang : " + totalPcs);
                                textTon.setText("Total Ton : " + String.format("%.4f", totalTon));

                                textJumlah.setVisibility(totalPcs == 0 ? View.GONE : View.VISIBLE);
                                textTon.setVisibility(totalPcs == 0 ? View.GONE : View.VISIBLE);
                            } else {
                                Toast.makeText(this, "Gagal menghapus detail!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                })
                .setNegativeButton("Batal", null)
                .show();
    }




    private float calculateTotalTon(List<SawmillDetailData> detailDataList) {
        float totalTon = 0f;

        for (SawmillDetailData data : detailDataList) {
            int idUOMTblLebar = data.getIdUOMTblLebar();
            int idUOMPanjang = data.getIdUOMPanjang();
            float tebal = data.getTebal();
            float lebar = data.getLebar();
            float panjang = data.getPanjang();
            int pcs = data.getPcs();

            float ton = 0f;

            if (idUOMTblLebar == 1 && idUOMPanjang == 4) {
                ton = (float) Math.floor(tebal * lebar * panjang * 304.8 * pcs / 1_000_000_000 / 1.416 * 10_000) / 10_000;
            } else if (idUOMTblLebar == 3 && idUOMPanjang == 4) {
                ton = (float) Math.floor(tebal * lebar * panjang * pcs / 7200.8 * 10_000) / 10_000;
            }

            totalTon += ton;
        }

        return totalTon;
    }

    private int hitungTotalPcsDariList() {
        int total = 0;
        for (SawmillDetailInputData data : inputList) {
            try {
                total += Integer.parseInt(data.getPcs());
            } catch (NumberFormatException ignored) {}
        }
        return total;
    }

    // ✅ TAMBAHKAN parameter untuk recalculate acuan
    private void updateSisaPcs(TextView textSisaPCS, String[] acuanData, TextView textSisaTarget,
                               RadioButton radioMillimeter, RadioButton radioFeet, RadioButton radioMeter) {
        sisaPcs = totalPcsAcuan - hitungTotalPcsDariList();
        textSisaPCS.setText("Sisa: " + sisaPcs);

        // ✅ Recalculate ton acuan setiap kali sisaPcs berubah
        recalculateAcuanTon(acuanData, textSisaTarget, radioMillimeter, radioFeet, radioMeter);
    }

    // Method untuk disable row (hanya EditText, bukan tombol delete)
    private void disableRowEditTexts(TableRow row) {
        for (int i = 0; i < row.getChildCount(); i++) {
            View child = row.getChildAt(i);
            if (child instanceof EditText) {
                child.setEnabled(false);
                child.setAlpha(0.6f); // Buat tampak disabled
            }

            // Tidak disable ImageButton (tombol delete tetap aktif)
        }
    }

    // Method untuk enable semua tombol delete
    private void enableAllDeleteButtons(TableLayout tablePjgPcs) {
        for (int i = 0; i < tablePjgPcs.getChildCount(); i++) {
            View child = tablePjgPcs.getChildAt(i);
            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                // Enable tombol delete (child terakhir adalah ImageButton)
                if (row.getChildCount() > 2) {
                    View deleteButton = row.getChildAt(2);
                    if (deleteButton instanceof ImageButton) {
                        deleteButton.setEnabled(true);
                        deleteButton.setAlpha(1.0f);
                    }
                }
            }
        }
    }


    // Method untuk mengecek apakah data duplikat (termasuk dengan data acuan)
    private boolean isDuplicateData(String tebal, String lebar, String panjang, String acuanPanjang, String acuanTebal, String acuanLebar) {
        // Cek dengan data acuan terlebih dahulu
        if (tebal.equals(acuanTebal) &&
                lebar.equals(acuanLebar) &&
                panjang.equals(acuanPanjang)) {
            return true;
        }

        // Cek dengan data yang sudah ada di inputList
        for (SawmillDetailInputData data : inputList) {
            if (tebal.equals(data.getTebal()) &&
                    lebar.equals(data.getLebar()) &&
                    panjang.equals(data.getPanjang()) &&
                    !data.getPanjang().isEmpty()) { // Hanya cek yang sudah diisi panjangnya
                return true;
            }
        }
        return false;
    }

    // Method untuk mengecek apakah PCS yang diinput tidak melebihi sisa
    private boolean isValidPcsInput(String pcsInput) {
        try {
            int pcsValue = Integer.parseInt(pcsInput);
            int currentTotal = hitungTotalPcsDariList();
            int newTotal = currentTotal + pcsValue;

            // Pastikan total tidak melebihi acuan dan sisa tidak kurang dari 1 (kecuali pas habis)
            if (newTotal > totalPcsAcuan) {
                return false;
            }

            int sisaSetelahInput = totalPcsAcuan - newTotal;
            return sisaSetelahInput > 0;

        } catch (NumberFormatException e) {
            return false;
        }
    }


    // ✅ REVISI - Ganti signature dan implementasi
    private TableRow createNewRow(String acuanTebal, String acuanLebar,
                                  TableLayout tablePjgPcs, TextView textSisaPCS, Button addRowButton,
                                  TextView textSisaTarget, RadioButton radioMillimeter,
                                  RadioButton radioFeet, RadioButton radioMeter,
                                  String[] acuanData) { // ✅ TAMBAHKAN parameter

        // 1. Disable EditText pada row terakhir
        int rowCount = tablePjgPcs.getChildCount();
        if (rowCount > 0) {
            TableRow lastRow = (TableRow) tablePjgPcs.getChildAt(rowCount - 1);
            disableRowEditTexts(lastRow);
        }

        // 2. Enable semua tombol delete agar row sebelumnya bisa dihapus
        enableAllDeleteButtons(tablePjgPcs);

        // 3. Buat TableRow baru
        TableRow newRow = new TableRow(this);
        newRow.setBackgroundColor(Color.WHITE);

        // Model
        SawmillDetailInputData inputData = new SawmillDetailInputData();
        inputData.setTebal(acuanTebal);
        inputData.setLebar(acuanLebar);

        // Kolom Panjang (decimal)
        EditText panjangEditText = new EditText(this);
        panjangEditText.setHint("Pjg");
        panjangEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        panjangEditText.setFilters(new InputFilter[]{
                new InputFilterMinDecimalNoLeadingZero(1),
                new InputFilter.LengthFilter(10)
        });

        TableRow.LayoutParams panjangParams =
                new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        panjangEditText.setLayoutParams(panjangParams);

        // Kolom Pcs (integer)
        EditText pcsEditText = new EditText(this);
        pcsEditText.setHint("Pcs");
        pcsEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        pcsEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        pcsEditText.setFilters(new InputFilter[]{
                new InputFilterMinIntNoLeadingZero(1),
                new InputFilter.LengthFilter(10)
        });

        TableRow.LayoutParams pcsParams =
                new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        pcsEditText.setLayoutParams(pcsParams);

        // ✅ TAMBAHKAN: TextWatcher untuk update sisa ton real-time
        TextWatcher tonWatcher = new TextWatcher() {
            private double previousTon = 0.0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String panjang = panjangEditText.getText().toString().trim();
                String pcs = pcsEditText.getText().toString().trim();

                if (!panjang.isEmpty() && !pcs.isEmpty()) {
                    double newTon = calculateRowTon(acuanTebal, acuanLebar, panjang, pcs,
                            radioMillimeter, radioFeet, radioMeter);

                    // Update current temp ton
                    currentTempTon -= previousTon; // kurangi ton lama
                    currentTempTon += newTon;      // tambah ton baru
                    previousTon = newTon;

                    // Simpan di tag row
                    newRow.setTag(R.id.row_ton_tag, newTon);

                    // Update tampilan sisa target
                    updateSisaTargetDisplay(textSisaTarget);

                    Log.d("TonWatcher", "Row ton: " + newTon + ", Total temp: " + currentTempTon);
                } else {
                    // Kalau kosong, kurangi ton sebelumnya
                    currentTempTon -= previousTon;
                    previousTon = 0.0;
                    newRow.setTag(R.id.row_ton_tag, 0.0);
                    updateSisaTargetDisplay(textSisaTarget);
                }
            }
        };

        panjangEditText.addTextChangedListener(tonWatcher);
        pcsEditText.addTextChangedListener(tonWatcher);

        // ✅ Store reference untuk cleanup
        newRow.setTag(R.id.ton_watcher_tag, tonWatcher);
        newRow.setTag(R.id.row_ton_tag, 0.0);

        // Kolom Delete
        ImageButton deleteButton = new ImageButton(this);
        deleteButton.setImageResource(R.drawable.ic_close);
        deleteButton.setContentDescription("Delete Button");
        deleteButton.setBackgroundColor(Color.TRANSPARENT);

        TableRow.LayoutParams deleteButtonParams =
                new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.5f);
        deleteButton.setLayoutParams(deleteButtonParams);

        deleteButton.setEnabled(false);
        deleteButton.setAlpha(0.5f);

        // Routing IME
        panjangEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        panjangEditText.setSingleLine(true);
        panjangEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                pcsEditText.requestFocus();
                return true;
            }
            return false;
        });

        // PCS tekan Next -> trigger addRowButton
        pcsEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        pcsEditText.setSingleLine(true);
        pcsEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                addRowButton.performClick();
                return true;
            }
            return false;
        });

        // ✅ MODIFIKASI delete button untuk update ton DAN recalculate acuan
        deleteButton.setOnClickListener(view -> {
            Object rowTonObj = newRow.getTag(R.id.row_ton_tag);
            if (rowTonObj instanceof Double) {
                double rowTon = (Double) rowTonObj;
                currentTempTon -= rowTon;
                Log.d("DeleteRow", "Removed ton: " + rowTon);
            }

            tablePjgPcs.removeView(newRow);
            inputList.remove(inputData);

            // ✅ UPDATE dengan recalculate acuan
            updateSisaPcs(textSisaPCS, acuanData, textSisaTarget, radioMillimeter, radioFeet, radioMeter);
        });

        newRow.addView(panjangEditText);
        newRow.addView(pcsEditText);
        newRow.addView(deleteButton);

        newRow.setTag(inputData);

        return newRow;
    }

    private void showDetailDialog(String noSTSawmill, String jenisKayu, String tgl, int idJenisKayu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Sawmill.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_detail_sawmill, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText inputTebal = dialogView.findViewById(R.id.inputTebal);
        EditText inputLebar = dialogView.findViewById(R.id.inputLebar);
        EditText inputPanjang = dialogView.findViewById(R.id.inputPanjang);
        EditText inputPcs = dialogView.findViewById(R.id.inputPcs);
        Button addRowButton = dialogView.findViewById(R.id.addRowButton);
        Button btnSubmit = dialogView.findViewById(R.id.BtnInputDetail);
        Button btnClear = dialogView.findViewById(R.id.BtnClearDetail);
        Button btnSaveDetail = dialogView.findViewById(R.id.btnSaveDetail);
        detailSawmillTableLayout = dialogView.findViewById(R.id.detailSawmillTableLayout);
        textJumlah = dialogView.findViewById(R.id.textJumlah);
        textTon = dialogView.findViewById(R.id.textTon);
        TextView textSisaPCS = dialogView.findViewById(R.id.textSisaPCS);
        TableLayout tablePjgPcs = dialogView.findViewById(R.id.TabelInputPjgPcs);

        RadioButton radioBagus = dialogView.findViewById(R.id.radioBagus);
        RadioButton radioKulit = dialogView.findViewById(R.id.radioKulit);
        RadioButton radioFeet = dialogView.findViewById(R.id.radioFeet);
        RadioButton radioMeter = dialogView.findViewById(R.id.radioMeter);
        RadioButton radioCentimeter = dialogView.findViewById(R.id.radioCentimeter);
        RadioButton radioMillimeter = dialogView.findViewById(R.id.radioMillimeter);

        final Spinner spinGradeDetail = dialogView.findViewById(R.id.spinGradeDetail);
        Spinner spinSpkProduk = dialogView.findViewById(R.id.spinSpkProduk);
        TextView textSisaTarget = dialogView.findViewById(R.id.tvSisaTarget);

        // Menambahkan onClickListener untuk tombol tutup dialog
        ImageButton btnCloseDialogInputDetail = dialogView.findViewById(R.id.btnCloseDialogInputDetail);
        btnCloseDialogInputDetail.setOnClickListener(v -> {
            dialog.dismiss();
        });

        inputTebal.setFilters(new InputFilter[]{
                new InputFilterMinDecimalNoLeadingZero(1),
                new InputFilter.LengthFilter(10)
        });

        inputLebar.setFilters(new InputFilter[]{
                new InputFilterMinDecimalNoLeadingZero(1),
                new InputFilter.LengthFilter(10)
        });

        inputPcs.setFilters(new InputFilter[]{
                new InputFilterMinIntNoLeadingZero(1),
                new InputFilter.LengthFilter(10)
        });

        inputPanjang.setFilters(new InputFilter[]{
                new InputFilterMinDecimalNoLeadingZero(1),
                new InputFilter.LengthFilter(10)
        });


        inputPanjang.setText("4");

        //ROUTING INPUT
        inputTebal.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                inputLebar.requestFocus();
                return true;
            }
            return false;
        });

        inputLebar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                inputPanjang.requestFocus();
                return true;
            }
            return false;
        });

        inputPanjang.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                inputPcs.requestFocus();
                return true;
            }
            return false;
        });

        inputPcs.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Jalankan logika tombol submit
                btnSubmit.performClick();
                return true;
            }
            return false;
        });

        inputList.clear();

        // ✅ Tambahkan indicator wajib untuk Rambung
        boolean isRambung = jenisKayu != null && jenisKayu.toLowerCase().contains("rambung");
        if (isRambung) {
            spinGradeDetail.setPrompt("Grade (Wajib untuk Rambung)");
        }

        loadGradeKBToSpinner(spinGradeDetail, radioBagus, radioKulit, jenisKayu, -1, 1);

        boolean isLocal = radioBagus.isChecked() || radioKulit.isChecked();

        // Variable untuk menyimpan data acuan
        String[] acuanData = new String[4]; // [tebal, lebar, panjang, pcs]

        // Initially hide add row button until acuan is set
        addRowButton.setVisibility(View.GONE);

        // ✅ DISABLE tombol Save dulu sampai data keload
        btnSaveDetail.setEnabled(false);
        btnSaveDetail.setText("Loading...");

        spinSpkProduk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SpkProdukData selected = (SpkProdukData) parent.getItemAtPosition(position);

                if (selected == null || selected.getIdProdukSPK() == 0) {
                    textSisaTarget.setText("");
                    return;
                }

                loadSisaTarget(
                        selected.getNoSPK(),
                        selected.getIdProdukSPK(),
                        textSisaTarget
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textSisaTarget.setText("");
            }
        });


        // btnSubmit untuk set acuan dan munculkan row pertama
        btnSubmit.setOnClickListener(v -> {
            String acuanTebal = inputTebal.getText().toString().trim();
            String acuanLebar = inputLebar.getText().toString().trim();
            String acuanPanjang = inputPanjang.getText().toString().trim();
            String acuanPcsStr = inputPcs.getText().toString().trim();

            // Validasi kosong (punyamu sudah)
            if (acuanTebal.isEmpty() || acuanLebar.isEmpty() || acuanPanjang.isEmpty() || acuanPcsStr.isEmpty()) {
                Toast.makeText(this, "Harap isi semua field acuan terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            // VALIDASI GRADE RAMBUNG (punyamu sudah)
            if (!validateGradeForRambung(jenisKayu, spinGradeDetail)) return;

            // ✅ Parse panjang acuan untuk cek standar
            double pjgAcuanVal;
            try {
                pjgAcuanVal = Double.parseDouble(acuanPanjang);
            } catch (Exception e) {
                Toast.makeText(this, "Format panjang acuan tidak valid", Toast.LENGTH_SHORT).show();
                inputPanjang.requestFocus();
                return;
            }

            spinSpkProduk.setEnabled(false);

            // Ambil token
            String token = TokenManager.getToken(this);

            if (token.isEmpty()) {
                Toast.makeText(this, "Token tidak ditemukan. Silakan login kembali", Toast.LENGTH_SHORT).show();
                return;
            }

            executorService.execute(() -> {

                List<SpkProdukData> apiResult =
                        SawmillApi.getSpkProdukFromAPI(
                                token,
                                acuanTebal,
                                acuanLebar,
                                idJenisKayu
                        );

                List<SpkProdukData> finalList = new ArrayList<>();

                // Default option
                finalList.add(new SpkProdukData(
                        0,
                        "PILIH -",
                        "",
                        0.0
                ));

                if (apiResult != null && !apiResult.isEmpty()) {
                    finalList.addAll(apiResult);
                }

                runOnUiThread(() -> {

                    ArrayAdapter<SpkProdukData> adapter =
                            new ArrayAdapter<>(
                                    Sawmill.this,
                                    android.R.layout.simple_spinner_item,   // ✅ same as before
                                    finalList
                            );

                    // ✅ Use your custom dropdown layout
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

                    spinSpkProduk.setAdapter(adapter);
                    spinSpkProduk.setSelection(0);
                    spinSpkProduk.setEnabled(true);

                    if (finalList.size() == 1) {
                        Toast.makeText(this,
                                "SPK Produk tidak ditemukan!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            });

// ✅ CARI bagian ini di dalam showDetailDialog dan GANTI
            Runnable continueSubmit = () -> {
                GradeKBData selectedGrade = (GradeKBData) spinGradeDetail.getSelectedItem();
                int idGradeKB = (selectedGrade != null && selectedGrade.getIdGradeKB() != null)
                        ? selectedGrade.getIdGradeKB() : -1;

                float tebal = Float.parseFloat(acuanTebal);
                float lebar = Float.parseFloat(acuanLebar);
                float panjang = Float.parseFloat(acuanPanjang);

                if (isDuplicateDetail(tebal, lebar, panjang, idGradeKB)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Duplikat Data")
                            .setMessage("Detail ini sudah ditambahkan sebelumnya, yakin ingin menambahkan lagi?")
                            .setPositiveButton("Ya", (dialogDuplicate, which) -> {
                                Toast.makeText(this, "Detail berhasil ditambahkan.", Toast.LENGTH_SHORT).show();
                                setupAcuan(acuanTebal, acuanLebar, acuanPanjang, acuanPcsStr, acuanData,
                                        textSisaPCS, tablePjgPcs, addRowButton, btnSubmit,
                                        inputTebal, inputLebar, inputPanjang, inputPcs,
                                        textSisaTarget, radioMillimeter, radioFeet, radioMeter); // ✅ TAMBAHKAN parameter
                            })
                            .setNegativeButton("Batal", null)
                            .show();
                    return;
                }

                setupAcuan(acuanTebal, acuanLebar, acuanPanjang, acuanPcsStr, acuanData,
                        textSisaPCS, tablePjgPcs, addRowButton, btnSubmit,
                        inputTebal, inputLebar, inputPanjang, inputPcs,
                        textSisaTarget, radioMillimeter, radioFeet, radioMeter); // ✅ TAMBAHKAN parameter
            };

            // ✅ Kalau panjang acuan tidak standar -> dialog dulu
            boolean dialogShown = confirmIfNotStandard(
                    Sawmill.this, pjgAcuanVal, acuanPanjang, continueSubmit
            );
            if (dialogShown) return;

            // ✅ standar -> langsung lanjut
            continueSubmit.run();
        });


        //BUTTON CLEAR (RESET DETAIL)
// ✅ CARI btnClear.setOnClickListener dan TAMBAHKAN reset
        btnClear.setOnClickListener(v -> {
            inputTebal.setText("");
            inputLebar.setText("");
            inputPanjang.setText("4");
            inputPcs.setText("");

            inputTebal.setEnabled(true);
            inputLebar.setEnabled(true);
            inputPanjang.setEnabled(true);
            inputPcs.setEnabled(true);

            addRowButton.setVisibility(View.INVISIBLE);
            btnSubmit.setEnabled(true);

            inputList.clear();

            loadGradeKBToSpinner(spinGradeDetail, radioBagus, radioKulit, jenisKayu, -1, 1);

            totalPcsAcuan = 0;

            updateSisaPcs(textSisaPCS, acuanData, textSisaTarget, radioMillimeter, radioFeet, radioMeter);

            tablePjgPcs.removeAllViews();

            // ✅ TAMBAHKAN reset temp ton
            currentTempTon = 0.0;
            updateSisaTargetDisplay(textSisaTarget);
        });

        // addRowButton untuk update data row saat ini dan buat row baru
        addRowButton.setOnClickListener(v -> {
            String acuanTebal = inputTebal.getText().toString();
            String acuanLebar = inputLebar.getText().toString();
            String acuanPanjang = inputPanjang.getText().toString();

            boolean hasValidData = false;
            boolean hasDuplicate = false;

            for (int i = 0; i < tablePjgPcs.getChildCount(); i++) {
                View child = tablePjgPcs.getChildAt(i);
                if (child instanceof TableRow) {
                    TableRow row = (TableRow) child;
                    SawmillDetailInputData inputData = (SawmillDetailInputData) row.getTag();

                    if (row.getChildCount() >= 2 && inputData != null) {
                        EditText panjangET = (EditText) row.getChildAt(0);
                        EditText pcsET = (EditText) row.getChildAt(1);

                        String panjangValue = panjangET.getText().toString().trim();
                        String pcsValue = pcsET.getText().toString().trim();

                        if (panjangValue.isEmpty() || pcsValue.isEmpty()) {
                            if (panjangET.isEnabled()) {
                                Toast.makeText(this, "Harap isi Panjang dan PCS pada baris yang aktif", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else if (panjangET.isEnabled()) {
                            if (!isValidPcsInput(pcsValue)) {
                                int sisaSekarang = totalPcsAcuan - hitungTotalPcsDariList();
                                Toast.makeText(this, "PCS tidak valid! Sisa PCS yang tersedia: " + sisaSekarang, Toast.LENGTH_SHORT).show();
                                return;
                            }

                            try {
                                float panjangInput = Float.parseFloat(panjangValue);

                                if (isDuplicateData(acuanTebal, acuanLebar, panjangValue, acuanPanjang, acuanTebal, acuanLebar)) {
                                    Toast.makeText(this, "Data dengan Tebal, Lebar, dan Panjang yang sama sudah ada!", Toast.LENGTH_SHORT).show();
                                    hasDuplicate = true;
                                    break;
                                }

                                boolean dialogShown = confirmIfNotStandard(
                                        Sawmill.this,
                                        (double) panjangInput,
                                        panjangValue,
                                        () -> {
                                            Toast.makeText(Sawmill.this, "Data ditambahkan meskipun panjang di luar standar.", Toast.LENGTH_SHORT).show();
                                            processRowData(row, inputData, panjangValue, pcsValue, acuanTebal, acuanLebar,
                                                    panjangValue, radioBagus, radioKulit, radioMillimeter, radioFeet,
                                                    radioMeter, spinGradeDetail, textSisaTarget);
                                            continueAddRowProcess(acuanTebal, acuanLebar, tablePjgPcs, textSisaPCS, addRowButton,
                                                    textSisaTarget, radioMillimeter, radioFeet, radioMeter, acuanData); // ✅ TAMBAHKAN acuanData
                                        }
                                );

                                if (dialogShown) return;

                                processRowData(row, inputData, panjangValue, pcsValue, acuanTebal, acuanLebar,
                                        panjangValue, radioBagus, radioKulit, radioMillimeter, radioFeet,
                                        radioMeter, spinGradeDetail, textSisaTarget);

                            } catch (NumberFormatException e) {
                                Toast.makeText(this, "Format panjang tidak valid", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            hasValidData = true;
                        }
                    }
                }
            }

            if (hasDuplicate) return;

            if (!hasValidData) {
                Toast.makeText(this, "Tidak ada data baru yang valid untuk diproses", Toast.LENGTH_SHORT).show();
                return;
            }

            updateSisaPcs(textSisaPCS, acuanData, textSisaTarget, radioMillimeter, radioFeet, radioMeter);
            enableAllDeleteButtons(tablePjgPcs);

            if (sisaPcs > 0) {
                // ✅ FIXED - Panggilan lengkap dengan semua parameter
                TableRow newRow = createNewRow(acuanTebal, acuanLebar, tablePjgPcs, textSisaPCS, addRowButton,
                        textSisaTarget, radioMillimeter, radioFeet, radioMeter, acuanData);

                tablePjgPcs.addView(newRow);

                EditText firstPanjangEditText = (EditText) newRow.getChildAt(0);
                firstPanjangEditText.post(() -> {
                    firstPanjangEditText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(firstPanjangEditText, InputMethodManager.SHOW_IMPLICIT);
                    }
                });

                Log.d("AddRow", "Row baru ditambahkan. Sisa PCS: " + sisaPcs);
            }
        });

        // PRECONDITION TUTUP TRANSAKSI
        if (isTutupTransaksi(tgl, tglTutupTransaksi)) {
            btnSaveDetail.setText("Transaksi Ditutup");
            btnSaveDetail.setEnabled(false);
            btnSaveDetail.setAlpha(0.5f);
        } else {
            btnSaveDetail.setText("Simpan");
            btnSaveDetail.setAlpha(1.0f);
            // Button akan dienable setelah data loaded
        }

        btnSaveDetail.setOnClickListener(view -> {
            // ✅ PREVENT DOUBLE CLICK
            if (!btnSaveDetail.isEnabled()) {
                return;
            }
            btnSaveDetail.setEnabled(false);

            // ✅ VALIDASI: Data lama sudah keload?
            synchronized (detailDataLock) {
                if (!isDetailDataLoaded) {
                    Toast.makeText(this, "⚠️ Data masih loading, tunggu sebentar...", Toast.LENGTH_SHORT).show();
                    btnSaveDetail.setEnabled(true);
                    return;
                }
            }

            // ✅ VALIDASI GRADE UNTUK RAMBUNG
            if (!validateGradeForRambung(jenisKayu, spinGradeDetail)) {
                btnSaveDetail.setEnabled(true);
                return;
            }

            // Baca ulang status RadioButton dan Spinner
            boolean currentIsLocal = radioBagus.isChecked() || radioKulit.isChecked();
            int currentIsBagusKulit = radioBagus.isChecked() ? 1 : (radioKulit.isChecked() ? 2 : 0);

            // Ambil idGradeKB langsung dari spinner
            GradeKBData selectedGrade = (GradeKBData) spinGradeDetail.getSelectedItem();
            int currentIdGradeKB = (selectedGrade != null && selectedGrade.getIdGradeKB() != null)
                    ? selectedGrade.getIdGradeKB() : -1;
            String currentNameGradeKB = (selectedGrade != null && selectedGrade.getNamaGrade() != null)
                    ? selectedGrade.getNamaGrade() : "-";

            // ✅ Validasi Bagus/Kulit untuk Kayu Lat
            if (currentNameGradeKB != null && currentNameGradeKB.toLowerCase().contains("kayu lat")) {
                if (!radioBagus.isChecked() && !radioKulit.isChecked()) {
                    Toast.makeText(this, "❌ Pilih Bagus atau Kulit untuk grade Kayu Lat!", Toast.LENGTH_LONG).show();
                    btnSaveDetail.setEnabled(true);
                    return;
                }
            }

            // Update UOM
            int currentIdUOMTblLebar = radioMillimeter.isChecked() ? 1 : 4;
            int currentIdUOMPanjang;
            if (radioFeet.isChecked()) {
                currentIdUOMPanjang = 4;
            } else if (radioMeter.isChecked()) {
                currentIdUOMPanjang = 3;
            } else {
                currentIdUOMPanjang = 1;
            }

            // ✅ Ambil SPK yang dipilih untuk DATA BARU
            SpkProdukData selectedSpk = (SpkProdukData) spinSpkProduk.getSelectedItem();
            String selectedNoSPK = selectedSpk.getNoSPK();
            int selectedIdProdukSPK = selectedSpk.getIdProdukSPK();

            // ✅ Validasi SPK untuk data BARU saja (bukan data lama)
//            boolean hasNewData = !inputList.isEmpty() || (acuanData[0] != null && sisaPcs > 0);
//            if (hasNewData && (selectedNoSPK == null || selectedNoSPK.isEmpty() || selectedNoSPK.equals("PILIH -"))) {
//                Toast.makeText(this, "⚠️ Pilih SPK Produk untuk data baru!", Toast.LENGTH_SHORT).show();
//                btnSaveDetail.setEnabled(true);
//                return;
//            }

            // ✅ Lock untuk prevent race condition
            synchronized (detailDataLock) {
                // Buat list baru untuk semua data
                List<SawmillDetailData> allDetailData = new ArrayList<>();

                // ✅ COPY DATA LAMA - PRESERVE noSPK dan idProdukSPK yang sudah ada
                if (detailDataList != null && !detailDataList.isEmpty()) {
                    for (SawmillDetailData existingData : detailDataList) {
                        // ✅ PENTING: Buat object baru dengan data yang sama (preserve SPK lama)
                        SawmillDetailData preservedData = new SawmillDetailData(
                                existingData.getNoUrut(),
                                existingData.getTebal(),
                                existingData.getLebar(),
                                existingData.getPanjang(),
                                existingData.getPcs(),
                                existingData.getIsBagusKulit() != 0,
                                existingData.getIdUOMTblLebar(),
                                existingData.getIdUOMPanjang(),
                                existingData.getIsBagusKulit(),
                                existingData.getIdGradeKB(),
                                existingData.getNamaGrade(),
                                existingData.getNoSPK(),
                                existingData.getIdProdukSPK(),
                                existingData.getNamaProduk()
                        );
                        allDetailData.add(preservedData);
                    }
                    Log.d("SaveDetail", "✅ Existing data preserved: " + detailDataList.size() + " rows");
                } else {
                    Log.w("SaveDetail", "⚠️ No existing data found");
                }

                // ✅ VALIDASI: Jangan save kalau tidak ada data sama sekali
                if (inputList.isEmpty() && (acuanData[0] == null || sisaPcs <= 0) && detailDataList.isEmpty()) {
                    Toast.makeText(this, "❌ Tidak ada data untuk disimpan!", Toast.LENGTH_LONG).show();
                    btnSaveDetail.setEnabled(true);
                    return;
                }

                // ✅ TAMBAH DATA BARU - Pakai SPK yang dipilih di spinner
                int startingNoUrut = allDetailData.size() + 1;
                for (SawmillDetailInputData inputData : inputList) {
                    float tebal = Float.parseFloat(inputData.getTebal());
                    float lebar = Float.parseFloat(inputData.getLebar());
                    float panjang = Float.parseFloat(inputData.getPanjang());
                    int pcs = Integer.parseInt(inputData.getPcs());

                    SawmillDetailData detailData = new SawmillDetailData(
                            startingNoUrut++, tebal, lebar, panjang, pcs,
                            currentIsLocal,
                            currentIdUOMTblLebar,
                            currentIdUOMPanjang,
                            currentIsBagusKulit,
                            currentIdGradeKB,
                            currentNameGradeKB,
                            selectedNoSPK,           // ✅ SPK BARU untuk data baru
                            selectedIdProdukSPK,     // ✅ ID SPK BARU untuk data baru
                            ""
                    );
                    allDetailData.add(detailData);
                }

                // ✅ Proses data acuan jika ada sisa - Pakai SPK yang dipilih
                if (acuanData[0] != null && sisaPcs > 0) {
                    float tebal = Float.parseFloat(acuanData[0]);
                    float lebar = Float.parseFloat(acuanData[1]);
                    float panjang = Float.parseFloat(acuanData[2]);
                    int pcs = sisaPcs;

                    SawmillDetailData acuanDetailData = new SawmillDetailData(
                            startingNoUrut, tebal, lebar, panjang, pcs,
                            currentIsLocal,
                            currentIdUOMTblLebar,
                            currentIdUOMPanjang,
                            currentIsBagusKulit,
                            currentIdGradeKB,
                            currentNameGradeKB,
                            selectedNoSPK,           // ✅ SPK BARU untuk data acuan
                            selectedIdProdukSPK,     // ✅ ID SPK BARU untuk data acuan
                            ""
                    );
                    allDetailData.add(acuanDetailData);
                }

                // ✅ VALIDASI FINAL
                if (allDetailData.isEmpty()) {
                    Log.e("SaveDetail", "❌ CRITICAL: allDetailData is EMPTY after processing!");
                    runOnUiThread(() -> {
                        Toast.makeText(this, "❌ Error: Data kosong setelah diproses!", Toast.LENGTH_LONG).show();
                        btnSaveDetail.setEnabled(true);
                    });
                    return;
                }

                // Log untuk debugging
                Log.i("SaveDetail", "=== PREPARE TO SAVE ===");
                Log.i("SaveDetail", "NoSTSawmill: " + noSTSawmill);
                Log.i("SaveDetail", "JenisKayu: " + jenisKayu);
                Log.i("SaveDetail", "Existing data (preserved): " + detailDataList.size());
                Log.i("SaveDetail", "New data: " + inputList.size());
                Log.i("SaveDetail", "Acuan sisa: " + sisaPcs);
                Log.i("SaveDetail", "Total to save: " + allDetailData.size());
                Log.i("SaveDetail", "Grade: " + currentNameGradeKB + " (ID: " + currentIdGradeKB + ")");
                Log.i("SaveDetail", "New SPK: " + selectedNoSPK + " (ID: " + selectedIdProdukSPK + ")");

                for (SawmillDetailData data : allDetailData) {
                    Log.d("FinalSubmit", "Row " + data.getNoUrut()
                            + ": " + data.getTebal() + "x" + data.getLebar() + "x" + data.getPanjang()
                            + " = " + data.getPcs() + " pcs"
                            + ", Grade=" + data.getNamaGrade() + " (ID=" + data.getIdGradeKB() + ")"
                            + ", BagusKulit=" + data.getIsBagusKulitLabel()
                            + ", SPK=" + data.getNoSPK() + " (ID=" + data.getIdProdukSPK() + ")");  // ✅ Log SPK
                }

                // Simpan ke database
                executorService.execute(() -> {
                    Log.i("SaveDetail", "Starting database save...");
                    boolean success = SawmillApi.replaceAllSawmillDetailData(
                            noSTSawmill,
                            allDetailData,
                            jenisKayu
                    );

                    Log.i("SaveDetail", success ? "✅ Save SUCCESS" : "❌ Save FAILED");

                    runOnUiThread(() -> {
                        if (success) {
                            Toast.makeText(Sawmill.this, "✅ Data berhasil disimpan! (" + allDetailData.size() + " baris)", Toast.LENGTH_SHORT).show();

                            // Reset form
                            inputTebal.setText("");
                            inputLebar.setText("");
                            inputPanjang.setText("4");
                            inputPcs.setText("");

                            inputTebal.setEnabled(true);
                            inputLebar.setEnabled(true);
                            inputPanjang.setEnabled(true);
                            inputPcs.setEnabled(true);

                            addRowButton.setVisibility(View.INVISIBLE);
                            btnSubmit.setEnabled(true);

                            inputList.clear();

                            loadGradeKBToSpinner(spinGradeDetail, radioBagus, radioKulit, jenisKayu, currentIdGradeKB, currentIsBagusKulit);

                            totalPcsAcuan = 0;
                            updateSisaPcs(textSisaPCS, acuanData, textSisaTarget, radioMillimeter, radioFeet, radioMeter);
                            tablePjgPcs.removeAllViews();

                            // Reset SPK spinner
                            spinSpkProduk.setSelection(0);
                            spinSpkProduk.setEnabled(true);

                            // ✅ TAMBAHKAN reset temp ton
                            currentTempTon = 0.0;
                            updateSisaTargetDisplay(textSisaTarget);

                            // Reset state
                            synchronized (detailDataLock) {
                                detailDataList.clear();
                                isDetailDataLoaded = false;
                            }

                            // Disable save button dulu
                            btnSaveDetail.setEnabled(false);
                            btnSaveDetail.setText("Loading...");

                            // Refresh tampilan
                            executorService.execute(() -> {
                                synchronized (detailDataLock) {
                                    detailDataList = SawmillApi.fetchSawmillDetailData(noSTSawmill);
                                    isDetailDataLoaded = true;
                                }

                                float totalTon = calculateTotalTon(detailDataList);

                                runOnUiThread(() -> {
                                    int totalPcs = 0;
                                    for (SawmillDetailData data : detailDataList) {
                                        totalPcs += data.getPcs();
                                    }

                                    populateDetailTable(detailSawmillTableLayout, detailDataList, noSTSawmill, jenisKayu, idJenisKayu);
                                    textJumlah.setText("Jumlah Batang : " + totalPcs);
                                    textTon.setText("Total Ton : " + String.format("%.4f", totalTon));

                                    textJumlah.setVisibility(totalPcs == 0 ? View.GONE : View.VISIBLE);
                                    textTon.setVisibility(totalPcs == 0 ? View.GONE : View.VISIBLE);

                                    // Enable save button kembali
                                    if (!isTutupTransaksi(tgl, tglTutupTransaksi)) {
                                        btnSaveDetail.setEnabled(true);
                                        btnSaveDetail.setText("Simpan");
                                    }
                                });
                            });

                        } else {
                            Toast.makeText(Sawmill.this, "❌ Gagal menyimpan data!", Toast.LENGTH_SHORT).show();
                            btnSaveDetail.setEnabled(true);
                        }
                    });
                });
            }
        });

        // ✅ Load data dengan lock dan loading indicator
        executorService.execute(() -> {
            synchronized (detailDataLock) {
                detailDataList.clear();
                detailDataList = SawmillApi.fetchSawmillDetailData(noSTSawmill);
                isDetailDataLoaded = true;
            }

            float totalTon = calculateTotalTon(detailDataList);

            runOnUiThread(() -> {
                int totalPcs = 0;
                for (SawmillDetailData data : detailDataList) {
                    totalPcs += data.getPcs();
                }

                populateDetailTable(detailSawmillTableLayout, detailDataList, noSTSawmill, jenisKayu, idJenisKayu);
                textJumlah.setText("Jumlah Batang : " + totalPcs);
                textTon.setText("Total Ton : " + String.format("%.4f", totalTon));

                textJumlah.setVisibility(totalPcs == 0 ? View.GONE : View.VISIBLE);
                textTon.setVisibility(totalPcs == 0 ? View.GONE : View.VISIBLE);

                // ✅ ENABLE tombol Save setelah data loaded
                if (!isTutupTransaksi(tgl, tglTutupTransaksi)) {
                    btnSaveDetail.setEnabled(true);
                    btnSaveDetail.setText("Simpan");
                }
            });
        });

        // ✅ Reset state saat dialog ditutup
        dialog.setOnDismissListener(d -> {
            synchronized (detailDataLock) {
                isDetailDataLoaded = false;
                detailDataList.clear();
            }
            inputList.clear();
            totalPcsAcuan = 0;
            sisaPcs = 0;
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.7),
                    (int) (getResources().getDisplayMetrics().heightPixels * 0.95)
            );
        }
    }

    // ✅ TAMBAHKAN method baru
    private void updateSisaTargetDisplay(TextView textSisaTarget) {
        if (textSisaTarget == null) return;

        // ✅ Total temp ton = input rows + acuan
        double totalTempTon = currentTempTon + acuanTempTon;
        double adjustedSisa = originalSisaTon[0] - totalTempTon;

        String displayText;
        if (totalTempTon > 0.0001) { // Ada input temp
            displayText = "Sisa Target : " +
                    String.format(Locale.US, "%.4f", adjustedSisa) +
                    " Ton (Temp: -" + String.format(Locale.US, "%.4f", totalTempTon) +
                    " | Rows: " + String.format(Locale.US, "%.4f", currentTempTon) +
                    " + Acuan: " + String.format(Locale.US, "%.4f", acuanTempTon) + ")";
        } else { // Tidak ada input temp
            displayText = "Sisa Target : " +
                    String.format(Locale.US, "%.4f", adjustedSisa) +
                    " Ton";
        }

        textSisaTarget.setText(displayText);

        if (adjustedSisa < 0) {
            textSisaTarget.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            textSisaTarget.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }

        Log.d("UpdateDisplay", "Total temp: " + totalTempTon + " (rows: " + currentTempTon + " + acuan: " + acuanTempTon + ")");
    }


    private double calculateRowTon(String tebal, String lebar, String panjang, String pcs,
                                   RadioButton radioMillimeter, RadioButton radioFeet, RadioButton radioMeter) {
        try {
            double tebalVal = Double.parseDouble(tebal);
            double lebarVal = Double.parseDouble(lebar);
            double panjangVal = Double.parseDouble(panjang);
            int pcsVal = Integer.parseInt(pcs);

            int idUOMLebar = radioMillimeter.isChecked() ? 1 : 4;
            int idUOMPanjang = radioFeet.isChecked() ? 4 : (radioMeter.isChecked() ? 3 : 1);

            return calculateTonLocal(tebalVal, lebarVal, panjangVal, pcsVal, idUOMLebar, idUOMPanjang);
        } catch (Exception e) {
            return 0.0;
        }
    }

    // ✅ REVISI - Reset currentTempTon saat load SPK baru
    private void loadSisaTarget(String noSPK, int idProdukSPK, TextView textSisaTarget) {

        String token = TokenManager.getToken(this);
        if (token == null || token.isEmpty()) {
            textSisaTarget.setText("⚠ Token invalid");
            return;
        }

        textSisaTarget.setText("Loading...");

        executorService.execute(() -> {

            Double sisaTon = SawmillApi.getSisaTonFromAPI(
                    token,
                    noSPK,
                    idProdukSPK
            );

            // ✅ FIX: Buat final variable untuk dipakai di lambda
            final double finalSisaTon = (sisaTon != null) ? sisaTon : 0.0;

            runOnUiThread(() -> {
                originalSisaTon[0] = finalSisaTon;

                // ✅ Reset temp ton saat ganti SPK
                currentTempTon = 0.0;

                updateSisaTargetDisplay(textSisaTarget);
            });
        });
    }

    // ✅ TAMBAHKAN method baru untuk recalculate ton acuan
    private void recalculateAcuanTon(String[] acuanData, TextView textSisaTarget,
                                     RadioButton radioMillimeter, RadioButton radioFeet, RadioButton radioMeter) {
        if (acuanData == null || acuanData[0] == null || sisaPcs <= 0) {
            acuanTempTon = 0.0;
            updateSisaTargetDisplay(textSisaTarget);
            return;
        }

        try {
            String tebal = acuanData[0];
            String lebar = acuanData[1];
            String panjang = acuanData[2];

            // Hitung ton berdasarkan SISA PCS yang belum diinput
            double newAcuanTon = calculateRowTon(tebal, lebar, panjang, String.valueOf(sisaPcs),
                    radioMillimeter, radioFeet, radioMeter);

            acuanTempTon = newAcuanTon;
            updateSisaTargetDisplay(textSisaTarget);

            Log.d("RecalcAcuan", "Sisa PCS: " + sisaPcs + ", Acuan ton: " + acuanTempTon);
        } catch (Exception e) {
            acuanTempTon = 0.0;
            Log.e("RecalcAcuan", "Error: " + e.getMessage());
        }
    }

    private double calculateTonLocal(
            double tebal,
            double lebar,
            double panjang,
            int pcs,
            int idUOMLebar,
            int idUOMPanjang
    ) {

        double ton = 0.0;

        if (idUOMLebar == 1 && idUOMPanjang == 4) {
            ton = Math.floor(
                    (tebal * lebar * panjang * 304.8 * pcs
                            / 1000000000.0 / 1.416) * 10000
            ) / 10000;
        }
        else if (idUOMLebar == 3 && idUOMPanjang == 4) {
            ton = Math.floor(
                    (tebal * lebar * panjang * pcs
                            / 7200.8) * 10000
            ) / 10000;
        }

        return ton;
    }



    // Method terpisah untuk melanjutkan proses add row setelah data diproses
    private void continueAddRowProcess(String acuanTebal, String acuanLebar, TableLayout tablePjgPcs,
                                       TextView textSisaPCS, Button addRowButton, TextView textSisaTarget,
                                       RadioButton radioMillimeter, RadioButton radioFeet, RadioButton radioMeter,
                                       String[] acuanData) { // ✅ TAMBAHKAN parameter

        // ✅ UPDATE panggilan updateSisaPcs
        updateSisaPcs(textSisaPCS, acuanData, textSisaTarget, radioMillimeter, radioFeet, radioMeter);

        enableAllDeleteButtons(tablePjgPcs);

        if (sisaPcs > 0) {
            // ✅ Pass acuanData ke createNewRow
            TableRow newRow = createNewRow(acuanTebal, acuanLebar, tablePjgPcs, textSisaPCS, addRowButton,
                    textSisaTarget, radioMillimeter, radioFeet, radioMeter, acuanData);
            tablePjgPcs.addView(newRow);

            EditText firstPanjangEditText = (EditText) newRow.getChildAt(0);
            firstPanjangEditText.post(() -> {
                firstPanjangEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(firstPanjangEditText, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        }
    }

    // Method terpisah untuk memproses data row (sama seperti sebelumnya)
// ✅ REVISI - Tambahkan parameter textSisaTarget
    private void processRowData(TableRow row, SawmillDetailInputData inputData, String panjangValue,
                                String pcsValue, String acuanTebal, String acuanLebar, String panjangInput,
                                RadioButton radioBagus, RadioButton radioKulit, RadioButton radioMillimeter,
                                RadioButton radioFeet, RadioButton radioMeter, Spinner spinGradeDetail,
                                TextView textSisaTarget) {

        // Ambil nilai dari UI saat dibutuhkan
        boolean currentIsLocal = radioBagus.isChecked() || radioKulit.isChecked();
        int currentIsBagusKulit = radioBagus.isChecked() ? 1 : (radioKulit.isChecked() ? 2 : 0);

        // Ambil idGradeKB langsung dari spinner
        GradeKBData selectedGrade = (GradeKBData) spinGradeDetail.getSelectedItem();
        int currentIdGradeKB = (selectedGrade != null && selectedGrade.getIdGradeKB() != null) ? selectedGrade.getIdGradeKB() : -1;
        String currentNameGradeKB = (selectedGrade != null && selectedGrade.getNamaGrade() != null) ? selectedGrade.getNamaGrade() : "-";

        // Ambil UOM dari RadioButton
        int currentIdUOMTblLebar = radioMillimeter.isChecked() ? 1 : 4;
        int currentIdUOMPanjang;
        if (radioFeet.isChecked()) {
            currentIdUOMPanjang = 4;
        } else if (radioMeter.isChecked()) {
            currentIdUOMPanjang = 3;
        } else {
            currentIdUOMPanjang = 1;
        }

        // ✅ TAMBAHKAN: Simpan ton row ini sebelum disable
        double rowTon = calculateRowTon(acuanTebal, acuanLebar, panjangValue, pcsValue,
                radioMillimeter, radioFeet, radioMeter);
        row.setTag(R.id.row_ton_tag, rowTon);

        // Update data
        inputData.setPanjang(panjangValue);
        inputData.setPcs(pcsValue);

        // Tambahkan ke inputList
        inputList.add(inputData);

        // Disable EditText pada row ini
        disableRowEditTexts(row);

        Log.d("ProcessRow", "Row processed - Ton: " + rowTon + ", Total temp: " + currentTempTon + ", Grade: " + currentIdGradeKB);
    }

    // Method terpisah untuk menangani setup acuan
    // ✅ REVISI - Tambahkan parameter textSisaTarget dan RadioButton
    private void setupAcuan(String acuanTebal, String acuanLebar, String acuanPanjang, String acuanPcsStr,
                            String[] acuanData, TextView textSisaPCS, TableLayout tablePjgPcs,
                            Button addRowButton, Button btnSubmit, EditText inputTebal,
                            EditText inputLebar, EditText inputPanjang, EditText inputPcs,
                            TextView textSisaTarget, RadioButton radioMillimeter,
                            RadioButton radioFeet, RadioButton radioMeter) {

        // Parsing PCS jika belum dilakukan
        try {
            totalPcsAcuan = Integer.parseInt(acuanPcsStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "PCS harus berupa angka", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simpan data acuan untuk digunakan nanti
        acuanData[0] = acuanTebal;
        acuanData[1] = acuanLebar;
        acuanData[2] = acuanPanjang;
        acuanData[3] = acuanPcsStr;

        // Set acuan berhasil, tampilkan sisa PCS
        updateSisaPcs(textSisaPCS, acuanData, textSisaTarget, radioMillimeter, radioFeet, radioMeter);
        textSisaPCS.setVisibility(View.VISIBLE);

        // Munculkan row pertama
        // ✅ Pass acuanData ke createNewRow
        TableRow firstRow = createNewRow(acuanTebal, acuanLebar, tablePjgPcs, textSisaPCS, addRowButton,
                textSisaTarget, radioMillimeter, radioFeet, radioMeter, acuanData);
        tablePjgPcs.addView(firstRow);

        // Fokus dan keyboard ke input panjang di row pertama
        EditText firstPanjangEditText = (EditText) firstRow.getChildAt(0);
        firstPanjangEditText.requestFocus();

        // Tampilkan button add row dan sembunyikan button submit
        addRowButton.setVisibility(View.VISIBLE);
        btnSubmit.setEnabled(false);

        // Disable editing field acuan
        inputTebal.setEnabled(false);
        inputLebar.setEnabled(false);
        inputPanjang.setEnabled(false);
        inputPcs.setEnabled(false);

        Log.d("SetAcuan", "Acuan ditetapkan: Tebal=" + acuanTebal + ", Lebar=" + acuanLebar +
                ", Panjang=" + acuanPanjang + ", PCS=" + totalPcsAcuan);
    }

    private boolean isDuplicateDetail(float tebal, float lebar, float panjang, int idGradeKB) {
        for (SawmillDetailData data : detailDataList) {
            if (Float.compare(data.getTebal(), tebal) == 0 &&
                    Float.compare(data.getLebar(), lebar) == 0 &&
                    Float.compare(data.getPanjang(), panjang) == 0 &&
                    ((data.getIdGradeKB() == idGradeKB) || data.getNamaGrade() == null)) {
                return true;
            }
        }
        return false;
    }

    // Tambahkan method baru di class Sawmill
    private boolean validateGradeForRambung(String jenisKayu, Spinner spinGradeDetail) {
        boolean isRambung = jenisKayu != null && jenisKayu.toLowerCase().contains("rambung");

        if (!isRambung) {
            return true; // Non-Rambung tidak perlu validasi grade
        }

        GradeKBData selectedGrade = (GradeKBData) spinGradeDetail.getSelectedItem();
        int idGradeKB = (selectedGrade != null && selectedGrade.getIdGradeKB() != null)
                ? selectedGrade.getIdGradeKB() : -1;
        String namaGrade = (selectedGrade != null && selectedGrade.getNamaGrade() != null)
                ? selectedGrade.getNamaGrade() : "PILIH";

        if (idGradeKB <= 0 || "PILIH".equalsIgnoreCase(namaGrade)) {
            spinGradeDetail.setBackgroundResource(R.drawable.spinner_error);
            spinGradeDetail.requestFocus();
            Toast.makeText(this, "❌ Grade harus dipilih untuk jenis kayu Rambung!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    private void onRowClick(SawmillData data) {
        executorService.execute(() -> {
            // Ambil data latar belakang
            String noSTSawmill = data.getNoSTSawmill();
            String jenisKayu = data.getNamaJenisKayu();
            String tgl = data.getTglSawmill();
            int idJenisKayu = data.getIdJenisKayu();

            runOnUiThread(() -> {
                showDetailDialog(noSTSawmill, jenisKayu, tgl, idJenisKayu);
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
                TableRow.LayoutParams.MATCH_PARENT, // Tinggi mengikuti konten
                weight // Berat untuk membagi lebar
        ));

        return textView;
    }

    public void setDateToView(String tanggal, TextView tanggalView) {
        // Gunakan metode dari DateTimeUtils untuk memformat tanggal
        String formattedDate = formatDate(tanggal);

        // Set tanggal terformat ke TextView
        tanggalView.setText(formattedDate);
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

        // ✅ Clean up handler untuk prevent memory leak
        if (searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }

        // ✅ Shutdown executor
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}