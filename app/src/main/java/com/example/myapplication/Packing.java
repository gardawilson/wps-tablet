package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import android.graphics.Color;
import android.content.Context;
import android.print.PrintManager;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PageRange;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import android.print.PrintJob;

import com.example.myapplication.api.BjApi;
import com.example.myapplication.api.MasterApi;
import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.model.MstJenisKayuData;
import com.example.myapplication.model.LabelDetailData;
import com.example.myapplication.model.LokasiData;
import com.example.myapplication.model.MstMesinData;
import com.example.myapplication.model.MstBjData;
import com.example.myapplication.model.MstProfileData;
import com.example.myapplication.model.BjData;
import com.example.myapplication.model.SpkData;
import com.example.myapplication.model.MstSusunData;
import com.example.myapplication.model.TellyData;
import com.example.myapplication.utils.DateTimeUtils;

import android.app.TimePickerDialog;
import android.widget.TimePicker;
import android.widget.AutoCompleteTextView;
import android.view.inputmethod.InputMethodManager;
import android.os.Handler;
import android.os.Looper;


import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;


import com.example.myapplication.utils.LoadingDialogHelper;
import com.example.myapplication.utils.PermissionUtils;
import com.example.myapplication.utils.SharedPrefUtils;
import com.example.myapplication.utils.TableUtils;
import com.example.myapplication.utils.TooltipUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.geom.Rectangle;


import java.io.File;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Packing extends AppCompatActivity {

    private String idUsername;
    private String username;
    private String noBarangJadi;
    private EditText NoBarangJadi;
    private EditText Date;
    private EditText Time;
    private EditText NoWIP;
    private Spinner SpinKayu;
    private Spinner SpinTelly;
    private Spinner SpinSPK;
    private Spinner SpinSPKAsal;
    private Spinner SpinProfile;
    private Spinner SpinBarangJadi;
    private Spinner SpinMesin;
    private Spinner SpinSusun;
    private Calendar calendar;
    private RadioGroup radioGroup;
    private RadioButton radioButtonMesin;
    private RadioButton radioButtonBSusun;
    private Button BtnDataBaru;
    private Button BtnSimpan;
    private Button BtnBatal;
    private Button BtnHapusDetail;
    private boolean isDataBaruClickedP = false;
    private CheckBox CBAfkir;
    private CheckBox CBLembur;
    private Button BtnInputDetail;
    private AutoCompleteTextView DetailLebar;
    private AutoCompleteTextView DetailTebal;
    private AutoCompleteTextView DetailPanjang;
    private EditText DetailPcs;
    private static int currentNumber = 1;
    private Button BtnPrint;
    private TextView M3;
    private TextView JumlahPcs;
    private boolean isCBAfkirP, isCBLemburP;
    private Button BtnSearchP;
    private int rowCount = 0;
    private TableLayout Tabel;
    boolean isCreateMode = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String rawDate;
    private TableLayout TabelOutput;
    private TextView tvLabelCount;
    private EditText remarkLabel;
    private ImageButton BtnExpandView;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private TableRow selectedRowHeader = null;
    int page = 1;
    int currentPage = 0;
    boolean isLoading = false;
    private List<LabelDetailData> temporaryDataListDetail = new ArrayList<>();
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();
    private Button btnUpdate;
    private List<String> userPermissions;
    private EditText mesinView;
    private EditText susunView;
    private Spinner spinLokasi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {


                finish();
            }
        });

//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_packing);

        NoWIP = findViewById(R.id.NoWIP);
        NoBarangJadi = findViewById(R.id.NoBarangJadi);
        Date = findViewById(R.id.Date);
        Time = findViewById(R.id.Time);
        SpinKayu = findViewById(R.id.SpinKayu);
        SpinTelly = findViewById(R.id.SpinTelly);
        SpinSPK = findViewById(R.id.SpinSPK);
        SpinSPKAsal = findViewById(R.id.SpinSPKAsal);
        SpinProfile = findViewById(R.id.SpinProfile);
        SpinBarangJadi = findViewById(R.id.SpinBarangJadi);
        calendar = Calendar.getInstance();
        SpinMesin = findViewById(R.id.SpinMesin);
        SpinSusun = findViewById(R.id.SpinSusun);
        radioButtonMesin = findViewById(R.id.radioButtonMesin);
        radioButtonBSusun = findViewById(R.id.radioButtonBSusun);
        BtnDataBaru = findViewById(R.id.BtnDataBaru);
        BtnSimpan = findViewById(R.id.BtnSimpan);
        BtnBatal = findViewById(R.id.BtnBatal);
        BtnHapusDetail = findViewById(R.id.BtnHapusDetail);
        CBLembur = findViewById(R.id.CBLembur);
        CBAfkir = findViewById(R.id.CBAfkir);
        BtnInputDetail = findViewById(R.id.BtnInputDetail);
        DetailPcs = findViewById(R.id.DetailPcs);
        DetailTebal = findViewById(R.id.DetailTebal);
        DetailPanjang = findViewById(R.id.DetailPanjang);
        DetailLebar = findViewById(R.id.DetailLebar);
        BtnPrint = findViewById(R.id.BtnPrint);
        M3 = findViewById(R.id.M3);
        JumlahPcs = findViewById(R.id.JumlahPcs);
        Tabel = findViewById(R.id.Tabel);
        radioGroup = findViewById(R.id.radioGroup);
        TabelOutput = findViewById(R.id.TabelOutput);
        tvLabelCount = findViewById(R.id.labelCount);
        remarkLabel = findViewById(R.id.remarkLabel);
        mesinView = findViewById(R.id.mesinView);
        susunView = findViewById(R.id.susunView);
        btnUpdate = findViewById(R.id.btnUpdate);
        spinLokasi = findViewById(R.id.spinLokasi);
        BtnExpandView = findViewById(R.id.BtnExpandView);

        //GET USERNAME
        idUsername = SharedPrefUtils.getIdUsername(this);

        //PERMISSION CHECK
        userPermissions = SharedPrefUtils.getPermissions(this);
        PermissionUtils.permissionCheck(this, btnUpdate, "label_bj:update");


        // Set imeOptions untuk memungkinkan pindah fokus
        DetailTebal.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        DetailLebar.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        DetailPanjang.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        BtnExpandView.setOnClickListener(v -> showListDialogOnDemand());

        // Menangani aksi 'Enter' pada keyboard
        DetailTebal.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Jika tombol 'Enter' ditekan, pindahkan fokus ke DetailLebarBarangJadi
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Pastikan DetailLebarBarangJadi bisa menerima fokus
                    DetailLebar.requestFocus();
                    return true; // Menunjukkan bahwa aksi sudah ditangani
                }
                return false;
            }
        });

        DetailLebar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    DetailPanjang.requestFocus();
                    return true;
                }
                return false;
            }
        });

        DetailPanjang.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    DetailPcs.requestFocus();
                    return true;
                }
                return false;
            }
        });

        DetailPcs.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {  // Mengubah ke IME_ACTION_DONE
                    // Ambil input dari AutoCompleteTextView
                    String noLaminating = NoBarangJadi.getText().toString();
                    String tebal = DetailTebal.getText().toString().trim();
                    String lebar = DetailLebar.getText().toString().trim();
                    String panjang = DetailPanjang.getText().toString().trim();

                    // Ambil data SpkData, Jenis Kayu, dan GradeData dari Spinner
                    SpkData selectedSPK = (SpkData) SpinSPK.getSelectedItem();
                    MstBjData selectedBj = (MstBjData) SpinBarangJadi.getSelectedItem();
                    MstJenisKayuData selectedJenisKayu = (MstJenisKayuData) SpinKayu.getSelectedItem();

                    int idBarangJadi = selectedBj != null ? selectedBj.getIdBarangJadi() : null;
                    String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
                    int idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;

                    // Validasi input kosong
                    if (tebal.isEmpty() || lebar.isEmpty() || panjang.isEmpty()) {
                        Toast.makeText(Packing.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    // Jalankan validasi
                    new CheckSPKDataTask(noSPK, tebal, lebar, panjang, idJenisKayu, idBarangJadi) {
                        @Override
                        protected void onPostExecute(String result) {
                            super.onPostExecute(result);

                            if (result.equals("SUCCESS")) {
                                // Jika validasi berhasil, tambahkan data ke daftar
                                addDataDetail(noLaminating);
                                jumlahpcs();
                                m3();
                                Toast.makeText(Packing.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();

                                // Sembunyikan keyboard setelah selesai
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            } else {
                                // Tampilkan pesan error
                                Toast.makeText(Packing.this, result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute();

                    return true;
                }
                return false;
            }
        });

        disableForm();

        int searchEditTextId = NoBarangJadi.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = NoBarangJadi.findViewById(searchEditTextId);

        if (searchEditText != null) {
            searchEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        NoBarangJadi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Tidak perlu
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isCreateMode) {
                    String newText = s.toString();

                    if(!newText.isEmpty()) {
                        if (userPermissions.contains("label_bj:update")) {
                            enableForm();
                        } else {
                            disableForm();
                        }
                        loadSubmittedData(newText);
                        BtnPrint.setEnabled(true);
                    } else {
                        enableForm();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Tidak perlu
            }
        });

        SpinMesin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (radioButtonMesin.isChecked()) {
                    Object selectedItem = parent.getItemAtPosition(position);
                    if (selectedItem instanceof MstMesinData) {
                        MstMesinData selectedMesin = (MstMesinData) selectedItem;
                        String noProduksi = selectedMesin.getNoProduksi();
                        loadOutputByMesinSusun(noProduksi, true);
                    } else {
                        Log.e("Error", "Item bukan tipe MesinData");
                        TabelOutput.removeAllViews();
                        tvLabelCount.setText("Total Label : 0");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Tidak ada yang dipilih
            }
        });

        SpinSusun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (radioButtonBSusun.isChecked()) {
                    Object selectedItem = parent.getItemAtPosition(position);
                    if (selectedItem instanceof MstSusunData) {
                        MstSusunData selectedSusun = (MstSusunData) selectedItem;
                        String noBongkarSusun = selectedSusun.getNoBongkarSusun();
                        loadOutputByMesinSusun(noBongkarSusun, false);
                    } else {
                        Log.e("Error", "Item bukan tipe SusunData");
                        TabelOutput.removeAllViews();
                        tvLabelCount.setText("Total Label : 0");
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Tidak ada yang dipilih
            }
        });

        radioButtonMesin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SpinMesin.setEnabled(true);
                SpinSusun.setEnabled(false);

                MstMesinData selectedMesin = (MstMesinData) SpinMesin.getSelectedItem();
                if (selectedMesin != null && isCreateMode) {
                    String noProduksi = selectedMesin.getNoProduksi();
                    loadOutputByMesinSusun(noProduksi, true);
                }
            } else if (radioButtonBSusun.isChecked()) {
                TabelOutput.removeAllViews();
                tvLabelCount.setText("Total Label : 0");
            }
        });

        radioButtonBSusun.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SpinMesin.setEnabled(false);
                SpinSusun.setEnabled(true);

                MstSusunData selectedSusun = (MstSusunData) SpinSusun.getSelectedItem();
                if (selectedSusun != null && isCreateMode) {
                    String noBongkarSusun = selectedSusun.getNoBongkarSusun();
                    loadOutputByMesinSusun(noBongkarSusun, false);
                }
            } else if (radioButtonMesin.isChecked()) {
                TabelOutput.removeAllViews();
                tvLabelCount.setText("Total Label : 0");
            }
        });

        setCurrentDateTime();

        BtnDataBaru.setOnClickListener(v -> {

            loadingDialogHelper.show(this);

            // Counter untuk semua spinner yang async
            int totalTasks = 6; // sesuaikan jumlah spinner async
            AtomicInteger doneCount = new AtomicInteger(0);

            Runnable checkAllDone = () -> {
                if (doneCount.incrementAndGet() == totalTasks) {
                    // Semua spinner selesai, dismiss loading
                    runOnUiThread(() -> {
                        loadingDialogHelper.hide();

                        // Update UI
                        BtnSimpan.setEnabled(true);
                        BtnBatal.setEnabled(true);
                        BtnPrint.setEnabled(false);
                        BtnDataBaru.setEnabled(false);
                        BtnDataBaru.setVisibility(View.GONE);
                        btnUpdate.setVisibility(View.GONE);
                        BtnSimpan.setVisibility(View.VISIBLE);
                        radioGroup.clearCheck();
                        radioButtonMesin.setEnabled(true);
                        radioButtonBSusun.setEnabled(true);

                        //AKTIFKAN SPINNER MESIN SUSUN UNTUK MODE CREATE
                        SpinMesin.setVisibility(View.VISIBLE);
                        SpinSusun.setVisibility(View.VISIBLE);
                        mesinView.setVisibility(View.GONE);
                        susunView.setVisibility(View.GONE);


                        loadMesinSpinner(rawDate);
                        loadSusunSpinner(rawDate);
                        loadLokasiSpinner("NONE", "");

                        clearData();
                        resetDetailData();
                        enableForm();
                    });
                }
            };

            // Jalankan semua loader async dengan callback
            setCurrentDateTime(); // synchronous → langsung dipanggil
            isCreateMode = true;  // synchronous → langsung dipanggil
            loadJenisKayuSpinner(0, checkAllDone);
            loadTellyByIdUsernameSpinner(idUsername, checkAllDone);
            loadSPKSpinner("0", checkAllDone);
            loadSPKAsalSpinner("0", checkAllDone);
            loadProfileSpinner("", checkAllDone);
            loadBjSpinner(0, checkAllDone);
        });

        BtnSimpan.setOnClickListener(v -> {

            String time = Time.getText().toString();
            String remark = remarkLabel.getText().toString();

            TellyData selectedTelly = (TellyData) SpinTelly.getSelectedItem();
            SpkData selectedSPK = (SpkData) SpinSPK.getSelectedItem();
            SpkData selectedSPKasal = (SpkData) SpinSPKAsal.getSelectedItem();
            MstProfileData selectedProfile = (MstProfileData) SpinProfile.getSelectedItem();
            MstBjData selectedBj = (MstBjData) SpinBarangJadi.getSelectedItem();
            MstJenisKayuData selectedJenisKayu = (MstJenisKayuData) SpinKayu.getSelectedItem();
            MstMesinData selectedMesin = (MstMesinData) SpinMesin.getSelectedItem();
            MstSusunData selectedSusun = (MstSusunData) SpinSusun.getSelectedItem();
            LokasiData selectedLokasi = (LokasiData) spinLokasi.getSelectedItem();
            RadioGroup radioGroupUOMTblLebar = findViewById(R.id.radioGroupUOMTblLebar);
            RadioGroup radioGroupUOMPanjang = findViewById(R.id.radioGroupUOMPanjang);

            int idBarangJadi = selectedBj != null ? selectedBj.getIdBarangJadi() : null;
            String idTelly = selectedTelly != null ? selectedTelly.getIdOrgTelly() : null;
            String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
            String noSPKasal = selectedSPKasal != null ? selectedSPKasal.getNoSPK() : null;
            String idProfile = selectedProfile != null ? selectedProfile.getIdFJProfile() : null;
            int idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;
            String noProduksi = selectedMesin != null ? selectedMesin.getNoProduksi() : null;
            String noBongkarSusun = selectedSusun != null ? selectedSusun.getNoBongkarSusun() : null;
            String idLokasi = selectedLokasi.getIdLokasi();


            if (!isInternetAvailable()) {
                Toast.makeText(Packing.this, "Tidak ada koneksi internet. Coba lagi nanti.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (rawDate.isEmpty() || time.isEmpty() ||
                    selectedTelly == null || selectedTelly.getIdOrgTelly().isEmpty() ||
                    selectedSPK == null || selectedSPK.getNoSPK().equals("PILIH") ||
                    selectedSPKasal == null || selectedSPKasal.getNoSPK().equals("PILIH") ||
                    selectedBj == null || selectedBj.getIdBarangJadi() == -1 ||
                    selectedJenisKayu == null || selectedJenisKayu.getJenis().isEmpty() ||
                    (!radioButtonMesin.isChecked() && !radioButtonBSusun.isChecked()) ||
                    (radioButtonMesin.isChecked() && (selectedMesin == null || selectedMesin.getNoProduksi().isEmpty())) ||
                    (radioButtonBSusun.isChecked() && (selectedSusun == null || selectedSusun.getNoBongkarSusun().isEmpty())) ||
                    temporaryDataListDetail.isEmpty()) {

                Toast.makeText(Packing.this, "Pastikan semua field terisi dengan benar.", Toast.LENGTH_SHORT).show();
                return;
            }

            CountDownLatch latch = new CountDownLatch(1);
            setAndSaveNewNumber(latch);
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (latch.getCount() == 0) {

                loadingDialogHelper.show(this);

                executorService.execute(() -> {
                    try {
                        // 1. cek periode dulu
                        boolean canProceed = MasterApi.isPeriodValid(rawDate);

                        if (!canProceed) {
                            runOnUiThread(() -> {
                                loadingDialogHelper.hide();
                                Toast.makeText(Packing.this, "Periode sudah ditutup!", Toast.LENGTH_LONG).show();
                            });
                            return; // stop proses
                        }

                        // 2. ambil nilai checkbox & radio button
                        int isReject = CBAfkir.isChecked() ? 1 : 0;
                        int isLembur = CBLembur.isChecked() ? 1 : 0;

                        boolean isProduksiOutput = radioButtonMesin.isChecked() && noProduksi != null;
                        boolean isBongkarSusun = radioButtonBSusun.isChecked() && noBongkarSusun != null;

                        int idUOMTblLebar = radioGroupUOMTblLebar.getCheckedRadioButtonId() == R.id.radioMillimeter ? 1 : 4;

                        int idUOMPanjang;
                        if (radioGroupUOMPanjang.getCheckedRadioButtonId() == R.id.radioCentimeter) {
                            idUOMPanjang = 1;
                        } else if (radioGroupUOMPanjang.getCheckedRadioButtonId() == R.id.radioMeter) {
                            idUOMPanjang = 2;
                        } else {
                            idUOMPanjang = 3;
                        }

                        // 3. insert header
                        BjApi.saveData(
                                noBarangJadi, rawDate, time, idTelly, noSPK, noSPKasal,
                                idBarangJadi, idJenisKayu, idProfile,
                                isReject, isLembur, idUOMTblLebar, idUOMPanjang,
                                remark, idLokasi, isProduksiOutput, noProduksi, isBongkarSusun, noBongkarSusun, temporaryDataListDetail
                        );

                        // 4. insert detail (mesin / bongkar susun)
                        if (radioButtonMesin.isChecked() && SpinMesin.isEnabled() && noProduksi != null) {
                            loadOutputByMesinSusun(noProduksi, true);

                        } else if (radioButtonBSusun.isChecked() && SpinSusun.isEnabled() && noBongkarSusun != null) {
                            loadOutputByMesinSusun(noBongkarSusun, false);
                        }

                        // 5. kalau sukses
                        runOnUiThread(() -> {
                            loadingDialogHelper.hide();
                            BtnDataBaru.setEnabled(true);
                            BtnPrint.setEnabled(true);
                            BtnSimpan.setEnabled(false);
                            BtnDataBaru.setVisibility(View.VISIBLE);
                            BtnSimpan.setVisibility(View.GONE);
                            disableForm();
                            Toast.makeText(Packing.this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
                        });

                    } catch (Exception e) {
                        // 6. kalau error
                        runOnUiThread(() -> {
                            loadingDialogHelper.hide();
                            Toast.makeText(Packing.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                    }
                });
            }
        });

        btnUpdate.setOnClickListener(v -> {

            String noBarangJadi = NoBarangJadi.getText().toString();
            String dateCreate = DateTimeUtils.formatToDatabaseDate(Date.getText().toString());
            String time = Time.getText().toString();
            String remark = remarkLabel.getText().toString();

            TellyData selectedTelly = (TellyData) SpinTelly.getSelectedItem();
            SpkData selectedSPK = (SpkData) SpinSPK.getSelectedItem();
            SpkData selectedSPKasal = (SpkData) SpinSPKAsal.getSelectedItem();
            MstProfileData selectedProfile = (MstProfileData) SpinProfile.getSelectedItem();
            MstBjData selectedBj = (MstBjData) SpinBarangJadi.getSelectedItem();
            MstJenisKayuData selectedJenisKayu = (MstJenisKayuData) SpinKayu.getSelectedItem();
            LokasiData selectedLokasi = (LokasiData) spinLokasi.getSelectedItem();

            RadioGroup radioGroupUOMTblLebar = findViewById(R.id.radioGroupUOMTblLebar);
            RadioGroup radioGroupUOMPanjang = findViewById(R.id.radioGroupUOMPanjang);

            String idTelly = selectedTelly != null ? selectedTelly.getIdOrgTelly() : null;
            String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
            String noSPKasal = selectedSPKasal != null ? selectedSPKasal.getNoSPK() : null;
            String idProfile = selectedProfile != null ? selectedProfile.getIdFJProfile() : null;
            int idBarangJadi = selectedBj != null ? selectedBj.getIdBarangJadi() : null;
            int idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;
            String idLokasi = selectedLokasi != null ? selectedLokasi.getIdLokasi() : null;


            if (!isInternetAvailable()) {
                Toast.makeText(Packing.this, "Tidak ada koneksi internet. Coba lagi nanti.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dateCreate.isEmpty() || time.isEmpty() ||
                    selectedTelly == null || selectedTelly.getIdOrgTelly().isEmpty() ||
                    selectedSPK == null || selectedSPK.getNoSPK().equals("PILIH") ||
                    selectedSPKasal == null || selectedSPKasal.getNoSPK().equals("PILIH") ||
                    selectedBj == null || selectedBj.getIdBarangJadi() == -1 ||
                    selectedJenisKayu == null || selectedJenisKayu.getJenis().isEmpty() ||
                    (!radioButtonMesin.isChecked() && !radioButtonBSusun.isChecked()) ||
                    temporaryDataListDetail.isEmpty()) {

                Toast.makeText(Packing.this, "Pastikan semua field terisi dengan benar.", Toast.LENGTH_SHORT).show();
                return;
            }

            loadingDialogHelper.show(this);

            executorService.execute(() -> {
                try {
                    // 1. cek periode dulu
                    boolean canProceed = MasterApi.isPeriodValid(dateCreate);

                    if (!canProceed) {
                        runOnUiThread(() -> {
                            loadingDialogHelper.hide();
                            Toast.makeText(Packing.this, "Periode sudah ditutup!", Toast.LENGTH_LONG).show();
                        });
                        return; // stop proses
                    }

                    // 2. ambil nilai checkbox & radio button
                    int isReject = CBAfkir.isChecked() ? 1 : 0;
                    int isLembur = CBLembur.isChecked() ? 1 : 0;


                    int idUOMTblLebar = radioGroupUOMTblLebar.getCheckedRadioButtonId() == R.id.radioMillimeter ? 1 : 4;

                    int idUOMPanjang;
                    if (radioGroupUOMPanjang.getCheckedRadioButtonId() == R.id.radioCentimeter) {
                        idUOMPanjang = 1;
                    } else if (radioGroupUOMPanjang.getCheckedRadioButtonId() == R.id.radioMeter) {
                        idUOMPanjang = 2;
                    } else {
                        idUOMPanjang = 3;
                    }

                    // 3. insert header
                    BjApi.updateData(
                            noBarangJadi, dateCreate, time, idTelly, noSPK, noSPKasal,
                            idBarangJadi, idJenisKayu, idProfile,
                            isReject, isLembur, idUOMTblLebar, idUOMPanjang,
                            remark, idLokasi, temporaryDataListDetail
                    );

                    // 5. kalau sukses
                    runOnUiThread(() -> {
                        loadingDialogHelper.hide();
                        BtnDataBaru.setEnabled(true);
                        BtnPrint.setEnabled(true);
                        BtnSimpan.setEnabled(false);
                        BtnDataBaru.setVisibility(View.VISIBLE);
                        btnUpdate.setVisibility(View.GONE);
                        disableForm();
                        Toast.makeText(Packing.this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
                    });

                } catch (Exception e) {
                    // 6. kalau error
                    runOnUiThread(() -> {
                        loadingDialogHelper.hide();
                        Toast.makeText(Packing.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            });
        });

        BtnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDetailData();
                resetAllForm();
                disableForm();

                isCreateMode = false;
                BtnDataBaru.setEnabled(true);
                BtnSimpan.setEnabled(false);
                BtnPrint.setEnabled(false);
                BtnDataBaru.setVisibility(View.VISIBLE);
                btnUpdate.setVisibility(View.GONE);
                BtnSimpan.setVisibility(View.GONE);
                CBAfkir.setChecked(false);
                CBLembur.setChecked(false);

            }
        });

        Date.setOnClickListener(v -> showDatePickerDialog());

        Time.setOnClickListener(v -> showTimePickerDialog());

        SpinSPK.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isCreateMode) {
                    resetDetailData();
                    String selectedSPK = parent.getItemAtPosition(position) != null ?
                            parent.getItemAtPosition(position).toString() : "";

                    // Memisahkan string berdasarkan " - " dan mengambil bagian pertama (noSPK)
                    String[] parts = selectedSPK.split(" - ");
                    String noSPK = parts.length > 0 ? parts[0] : ""; // Mengambil bagian pertama (noSPK)

                    // Pindahkan operasi berat ke thread terpisah
                    new Thread(() -> {
                        // Pengecekan sinkron apakah SPK terkunci
                        boolean isLocked = isSPKLocked(noSPK);

                        // Ambil rekomendasi dari database (operasi berat)
                        Map<String, List<String>> dimensionData = listSPKDetailRecommendation(noSPK);

                        // Update UI di main thread
                        handler.post(() -> {
                            // Buat adapter untuk masing-masing AutoCompleteTextView
                            ArrayAdapter<String> tebalAdapter = new ArrayAdapter<>(
                                    Packing.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    dimensionData.get("tebal")
                            );
                            ArrayAdapter<String> lebarAdapter = new ArrayAdapter<>(
                                    Packing.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    dimensionData.get("lebar")
                            );
                            ArrayAdapter<String> panjangAdapter = new ArrayAdapter<>(
                                    Packing.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    dimensionData.get("panjang")
                            );

                            // Set adapter untuk masing-masing AutoCompleteTextView
                            DetailTebal.setAdapter(tebalAdapter);
                            DetailLebar.setAdapter(lebarAdapter);
                            DetailPanjang.setAdapter(panjangAdapter);

                            // Set threshold untuk semua AutoCompleteTextView
                            DetailTebal.setThreshold(0);
                            DetailLebar.setThreshold(0);
                            DetailPanjang.setThreshold(0);

                            // Tampilkan status lock
                            if (isLocked) {
                                Toast.makeText(Packing.this, "Dimensi Kunci Aktif", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                DetailTebal.setText("");
                DetailPanjang.setText("");
                DetailLebar.setText("");
                DetailTebal.setAdapter(null);
                DetailPanjang.setAdapter(null);
                DetailLebar.setAdapter(null);
            }
        });

        BtnInputDetail.setOnClickListener(v -> {
            // Ambil input dari AutoCompleteTextView
            String noBarangJadi = NoBarangJadi.getText().toString();
            String tebal = DetailTebal.getText().toString().trim();
            String lebar = DetailLebar.getText().toString().trim();
            String panjang = DetailPanjang.getText().toString().trim();

            // Ambil data SpkData, Jenis Kayu, dan GradeData dari Spinner
            SpkData selectedSPK = (SpkData) SpinSPK.getSelectedItem();
            MstBjData selectedBj = (MstBjData) SpinBarangJadi.getSelectedItem();
            MstJenisKayuData selectedJenisKayu = (MstJenisKayuData) SpinKayu.getSelectedItem();

            int idBarangJadi = selectedBj != null ? selectedBj.getIdBarangJadi() : null;
            String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
            int idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;

            // Validasi input kosong
            if (noBarangJadi.isEmpty() || tebal.isEmpty() || lebar.isEmpty() || panjang.isEmpty()) {
                Toast.makeText(Packing.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Jalankan validasi
            new CheckSPKDataTask(noSPK, tebal, lebar, panjang, idJenisKayu, idBarangJadi) {
                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);

                    if (result.equals("SUCCESS")) {
                        // Jika validasi berhasil, tambahkan data ke daftar
                        addDataDetail(noBarangJadi);
                        jumlahpcs();
                        m3();
                        Toast.makeText(Packing.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    } else {
                        // Tampilkan pesan error
                        Toast.makeText(Packing.this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        });


        BtnHapusDetail.setOnClickListener(v -> {
            resetDetailData();
        });

        BtnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validasi input
                if (NoBarangJadi.getText() == null || NoBarangJadi.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Packing.this, "Nomor BJ tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validasi apakah ada data untuk dicetak
                if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
                    Toast.makeText(Packing.this, "Tidak ada data untuk dicetak", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cek status HasBeenPrinted di database
                String noBarangJadi = NoBarangJadi.getText().toString().trim();
                checkHasBeenPrinted(noBarangJadi, new Packing.HasBeenPrintedCallback() {
                    @Override
                    public void onResult(int printCount) {
                        if (printCount == -1){
                            return;
                        }
                        // Menggunakan printCount untuk menentukan jumlah print sebelumnya
                        // Tidak ada logika boolean, hanya menghitung dan menambah nilai HasBeenPrinted

                        try {
                            // Ambil data dari form
                            String mesinSusun;
                            String jenisKayu = SpinKayu.getSelectedItem() != null ? SpinKayu.getSelectedItem().toString().trim() : "";
                            String date = Date.getText() != null ? Date.getText().toString().trim() : "";
                            String time = Time.getText() != null ? Time.getText().toString().trim() : "";
                            String tellyBy = SpinTelly.getSelectedItem() != null ? SpinTelly.getSelectedItem().toString().trim() : "";
                            String noSPK = SpinSPK.getSelectedItem() != null ? SpinSPK.getSelectedItem().toString().trim() : "";
                            String noSPKasal = SpinSPKAsal.getSelectedItem() != null ? SpinSPKAsal.getSelectedItem().toString().trim() : "";
                            String jumlahPcs = JumlahPcs.getText() != null ? JumlahPcs.getText().toString().trim() : "";
                            String m3 = M3.getText() != null ? M3.getText().toString().trim() : "";
                            String namaBJ = SpinBarangJadi.getSelectedItem() != null ? SpinBarangJadi.getSelectedItem().toString().trim() : "";
                            String remark = remarkLabel.getText() != null ? remarkLabel.getText().toString().trim() : "";

                            if (radioButtonMesin.isChecked()) {
                                mesinSusun = SpinMesin.getSelectedItem() != null && isCreateMode ? SpinMesin.getSelectedItem().toString().trim() : mesinView.getText().toString();
                            } else {
                                mesinSusun = SpinSusun.getSelectedItem() != null && isCreateMode ? SpinSusun.getSelectedItem().toString().trim() : susunView.getText().toString();
                            }


                            // Buat PDF dengan parameter printCount
                            Uri pdfUri = createPdf(noBarangJadi, jenisKayu, date, time, tellyBy, mesinSusun, noSPK, noSPKasal,
                                    temporaryDataListDetail, jumlahPcs, m3, printCount, namaBJ, remark);


                            if (pdfUri != null) {
                                // Siapkan PrintManager
                                PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                                String jobName = getString(R.string.app_name) + " Document";

                                // Buat PrintDocumentAdapter
                                PrintDocumentAdapter pda = new PrintDocumentAdapter() {
                                    @Override
                                    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
                                                         CancellationSignal cancellationSignal,
                                                         LayoutResultCallback callback, Bundle extras) {
                                        if (cancellationSignal.isCanceled()) {
                                            callback.onLayoutCancelled();
                                            return;
                                        }

                                        PrintDocumentInfo info = new PrintDocumentInfo.Builder(jobName)
                                                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                                                .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                                                .build();

                                        callback.onLayoutFinished(info, true);
                                    }

                                    @Override
                                    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination,
                                                        CancellationSignal cancellationSignal,
                                                        WriteResultCallback callback) {
                                        try {
                                            InputStream input = getContentResolver().openInputStream(pdfUri);
                                            OutputStream output = new FileOutputStream(destination.getFileDescriptor());

                                            byte[] buf = new byte[1024];
                                            int bytesRead;

                                            while ((bytesRead = input.read(buf)) > 0) {
                                                output.write(buf, 0, bytesRead);
                                            }

                                            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});

                                            input.close();
                                            output.close();
                                        } catch (Exception e) {
                                            callback.onWriteFailed(e.getMessage());
                                        }
                                    }
                                };

                                // Mulai proses pencetakan
                                PrintAttributes attributes = new PrintAttributes.Builder()
                                        .setMediaSize(new PrintAttributes.MediaSize("CUSTOM", "Custom Roll Paper", 72, 3000)) // 72mm lebar
                                        .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 300, 300))
                                        .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                                        .build();

                                try {
                                    // Dapatkan PrintJob
                                    PrintJob printJob = printManager.print(jobName, pda, attributes);

                                    // Monitor status PrintJob
                                    new Thread(() -> {
                                        boolean isPrinting = true;
                                        while (isPrinting) {
                                            if (printJob.isCompleted()) {
                                                // Update database setiap kali print selesai
                                                updatePrintStatus(noBarangJadi); // Update nilai HasBeenPrinted setelah print selesai
                                                isPrinting = false;
                                            } else if (printJob.isFailed() || printJob.isCancelled()) {
                                                isPrinting = false;
                                            }

                                            try {
                                                Thread.sleep(1000); // Check setiap 1 detik
                                            } catch (InterruptedException e) {
                                                Thread.currentThread().interrupt();
                                                break;
                                            }
                                        }
                                    }).start();

                                } catch (Exception e) {
                                    Toast.makeText(Packing.this,
                                            "Error printing: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error";
                            Toast.makeText(Packing.this,
                                    "Terjadi kesalahan: " + errorMessage,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    //METHOD PACKING


    //LABEL LIST
    private void showListDialogOnDemand() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_list_item_bj, null);

        TableLayout tableLayout = dialogView.findViewById(R.id.tableHeaderLabel);
        ProgressBar loadingIndicator = dialogView.findViewById(R.id.listLabelLoadingIndicator);

        ScrollView scrollView = dialogView.findViewById(R.id.scrollViewTable);

        EditText searchInput = dialogView.findViewById(R.id.searchInput);
        ImageView clearButton = dialogView.findViewById(R.id.clearButton);

        Button btnEditData = dialogView.findViewById(R.id.btnEditData);
        Button btnDeleteData = dialogView.findViewById(R.id.btnDeleteData);

        TextView tvSumLabel = dialogView.findViewById(R.id.tvSumLabel);

        // Reset selection state
        selectedRowHeader = null;

        executorService.execute(() -> {
            // 🔹 Jalankan delete di background thread
            int totalLabel = BjApi.getTotalLabelCount("");

            // 🔹 Update UI kembali
            runOnUiThread(() -> {
                tvSumLabel.setText("LIST LABEL BARANG JADI " + "(" + String.valueOf(totalLabel) + ")");

            });
        });


        // Variable untuk menyimpan data yang dipilih
        final BjData[] selectedData = {null};

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
            int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

            if (diff <= 50 && !isLoading) { // 50px sebelum mentok bawah
                isLoading = true;
                currentPage++;
                loadMoreData(tableLayout, selectedData);
            }
        });

        tableLayout.removeAllViews();
        loadingIndicator.setVisibility(View.VISIBLE); // tampilkan loading

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.7);
            layoutParams.height = (int) (getResources().getDisplayMetrics().heightPixels);
            window.setAttributes(layoutParams);
        }

        // Click listener untuk button Edit
        btnEditData.setOnClickListener(v -> {
            if (selectedData[0] != null) {
                isCreateMode = false;

                // Mengisi noLaminating dengan data yang dipilih
                NoBarangJadi.setText(selectedData[0].getNoBarangJadi());

                // Tutup dialog
                dialog.dismiss();

                btnUpdate.setVisibility(View.VISIBLE);
                BtnSimpan.setVisibility(View.GONE);
                BtnDataBaru.setVisibility(View.GONE);

                SpinMesin.setVisibility(View.GONE);
                SpinSusun.setVisibility(View.GONE);
                mesinView.setVisibility(View.VISIBLE);
                susunView.setVisibility(View.VISIBLE);

                Date.setEnabled(false);
                Time.setEnabled(false);

                // Optional: tampilkan pesan sukses
//                Toast.makeText(this, "Data dipilih: " + selectedData[0].getNoBarangJadi(), Toast.LENGTH_SHORT).show();
            } else {
                // Tampilkan pesan jika belum ada row yang dipilih
                Toast.makeText(this, "Silakan pilih data terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });

        // Click listener untuk button Delete (opsional, sesuai kebutuhan)
        btnDeleteData.setOnClickListener(v -> {
            if (selectedData[0] != null) {
                new AlertDialog.Builder(this)
                        .setTitle("Konfirmasi")
                        .setMessage("Apakah Anda yakin ingin menghapus data " + selectedData[0].getNoBarangJadi() + "?")
                        .setPositiveButton("Ya", (dialogInterface, i) -> {
                            executorService.execute(() -> {
                                try {
                                    // 🔹 Jalankan delete di background thread
                                    boolean success = BjApi.deleteData(selectedData[0].getNoBarangJadi());

                                    // 🔹 Update UI kembali
                                    runOnUiThread(() -> {
                                        if (success) {
                                            Toast.makeText(this,
                                                    "Data " + selectedData[0].getNoBarangJadi() + " dihapus",
                                                    Toast.LENGTH_SHORT).show();

                                            // contoh: refresh tabel/list setelah delete
                                            loadSearchData(tableLayout, loadingIndicator, "", selectedData);
                                        } else {
                                            Toast.makeText(this,
                                                    "Gagal menghapus data",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        dialogInterface.dismiss();
                                    });
                                } catch (Exception e) {
                                    runOnUiThread(() ->
                                            Toast.makeText(this,
                                                    "Error: " + e.getMessage(),
                                                    Toast.LENGTH_SHORT).show()
                                    );
                                }
                            });
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
            } else {
                Toast.makeText(this, "Silakan pilih data terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Munculkan / sembunyikan tombol clear
                if (s.length() > 0) {
                    clearButton.setVisibility(View.VISIBLE);
                } else {
                    clearButton.setVisibility(View.GONE);
                }

                // Reset selection saat search
                selectedRowHeader = null;
                selectedData[0] = null; // Reset selected data

                // Logika search
                String keyword = s.toString().trim();
                page = 1; // reset halaman
                loadSearchData(tableLayout, loadingIndicator, keyword, selectedData);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Aksi tombol clear
        clearButton.setOnClickListener(v -> {
            searchInput.setText(""); // Hapus teks
        });

        // Jalankan fetch data di background setelah dialog ditampilkan
        executorService.execute(() -> {
            List<BjData> list = BjApi.getBjData(page, 50, "");

            runOnUiThread(() -> {
                loadingIndicator.setVisibility(View.GONE); // sembunyikan loading
                tableLayout.removeAllViews(); // hapus semua tampilan sebelumnya

                if (list == null || list.isEmpty()) {
                    TextView noDataView = new TextView(this);
                    noDataView.setText("Data tidak ditemukan");
                    noDataView.setGravity(Gravity.CENTER);
                    noDataView.setPadding(16, 16, 16, 16);
                    tableLayout.addView(noDataView);
                    return;
                }

                addRowsToTable(tableLayout, list, 0, selectedData);
            });
        });
    }

    private void loadSearchData(TableLayout tableLayout, ProgressBar loadingIndicator, String keyword, BjData[] selectedData) {
        loadingIndicator.setVisibility(View.VISIBLE);
        tableLayout.removeAllViews();

        executorService.execute(() -> {
            List<BjData> list = BjApi.getBjData(page, 50, keyword);

            runOnUiThread(() -> {
                loadingIndicator.setVisibility(View.GONE);
                if (list == null || list.isEmpty()) {
                    TextView noDataView = new TextView(this);
                    noDataView.setText("Data tidak ditemukan");
                    noDataView.setGravity(Gravity.CENTER);
                    noDataView.setPadding(16, 16, 16, 16);
                    tableLayout.addView(noDataView);
                    return;
                }

                addRowsToTable(tableLayout, list, 0, selectedData);
            });
        });
    }

    private void loadMoreData(TableLayout tableLayout, BjData[] selectedData) {
        executorService.execute(() -> {
            List<BjData> moreData = BjApi.getBjData(currentPage, 50, "");
            runOnUiThread(() -> {
                if (moreData != null && !moreData.isEmpty()) {
                    int startIndex = tableLayout.getChildCount();
                    addRowsToTable(tableLayout, moreData, startIndex, selectedData);
                }
                isLoading = false;
            });
        });
    }

    // Method yang sudah diupdate untuk menyimpan selected data
    private void addRowsToTable(TableLayout tableLayout, List<BjData> list, int startRowIndex, BjData[] selectedData) {
        int rowIndex = startRowIndex;

        for (BjData data : list) {
            TableRow row = new TableRow(this);
            row.setTag(rowIndex);

            TextView col1 = TableUtils.createTextView(this, data.getNoBarangJadi(), 1f);
            TextView col2 = TableUtils.createTextView(this, DateTimeUtils.formatDate(data.getDateCreate()), 1f);
            TextView col3 = TableUtils.createTextView(this, data.getNamaJenisKayu(), 1f);
            TextView col4 = TableUtils.createTextView(this, data.getNamaBarangJadi(), 1f);
            TextView col5 = TableUtils.createTextView(this, data.getIdLokasi(), 0.5f);
            TextView col6 = TableUtils.createTextView(this, data.getNamaOrgTelly(), 1f);

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

            // Set background color berdasarkan index
            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }

            // Set click listener yang konsisten untuk semua row
            row.setOnClickListener(v -> {
                // Reset previous selection
                if (selectedRowHeader != null) {
                    int prevIndex = (int) selectedRowHeader.getTag();
                    if (prevIndex % 2 == 0) {
                        selectedRowHeader.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
                    } else {
                        selectedRowHeader.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                    }
                    TableUtils.resetTextColor(this, selectedRowHeader);
                }

                // Set new selection
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
                TableUtils.setTextColor(this, row, R.color.white);
                selectedRowHeader = row;

                // Simpan data yang dipilih
                selectedData[0] = data;

                // Call click handler
                onRowClick(data);
            });

            tableLayout.addView(row);
            rowIndex++;
        }
    }

    private void onRowClick(BjData data) {
        noBarangJadi = data.getNoBarangJadi();

        // Tampilkan tooltip
        TooltipUtils.fetchDataAndShowTooltip(
                this,
                executorService,
                selectedRowHeader,
                data.getNoBarangJadi(),
                "BarangJadi_h",
                "BarangJadi_d",
                "NoBJ",
                () -> {
                    // Callback saat popup ditutup
                    if (selectedRowHeader != null) {
                        int currentIndex = (int) selectedRowHeader.getTag();
//                        ViewUtils.resetRowSelection(this, selectedRowHeader, currentIndex);
//                        selectedRowHeader = null;
                    }
                }
        );
    }


    private void loadOutputByMesinSusun(String parameter, boolean isNoProduksi) {
        new Thread(() -> {
            Connection connection = null;
            try {
                connection = ConnectionClass();
                if (connection != null) {
                    String query;
                    if (isNoProduksi) {
                        query = "SELECT spo.NoBJ, h.HasBeenPrinted " +
                                "FROM dbo.PackingProduksiOutput spo " +
                                "JOIN dbo.BarangJadi_h h ON spo.NoBJ = h.NoBJ " +
                                "WHERE spo.NoProduksi = ?" +
                                "ORDER BY spo.NoBJ DESC";
                    } else {
                        query = "SELECT bs.NoBJ, h.HasBeenPrinted " +
                                "FROM dbo.BongkarSusunOutputBarangJadi bs " +
                                "JOIN dbo.BarangJadi_h h ON bs.NoBJ = h.NoBJ " +
                                "WHERE bs.NoBongkarSusun = ?" +
                                "ORDER BY bs.NoBJ DESC";
                    }

                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, parameter);
                        try (ResultSet rs = stmt.executeQuery()) {
                            List<String> noBJList = new ArrayList<>();
                            List<Integer> hasBeenPrintedList = new ArrayList<>();

                            while (rs.next()) {
                                noBJList.add(rs.getString("NoBJ"));
                                hasBeenPrintedList.add(rs.getInt("HasBeenPrinted"));
                            }

                            runOnUiThread(() -> {
                                TabelOutput.removeAllViews();

                                // Reset selected row saat memuat data baru
                                selectedRowHeader = null;

                                int labelCount = 0;

                                if (!noBJList.isEmpty() && noBJList.size() == hasBeenPrintedList.size()) {
                                    for (int i = 0; i < noBJList.size(); i++) {
                                        String noBarangJadi = noBJList.get(i);
                                        int hasBeenPrinted = hasBeenPrintedList.get(i);

                                        TableRow row = new TableRow(this);
                                        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(
                                                TableLayout.LayoutParams.MATCH_PARENT,
                                                TableLayout.LayoutParams.WRAP_CONTENT
                                        );

                                        row.setLayoutParams(rowParams);
                                        row.setPadding(0, 10, 0, 10);

                                        // Set tag untuk menyimpan index
                                        row.setTag(i);

                                        // Ubah warna latar belakang berdasarkan indeks
                                        if (i % 2 == 0) {
                                            row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
                                        } else {
                                            row.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                                        }

                                        TextView labelTextView = new TextView(this);
                                        labelTextView.setText(noBarangJadi);
                                        labelTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                                        labelTextView.setGravity(Gravity.CENTER);
                                        labelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                                        row.addView(labelTextView);

                                        ImageView iIcon = new ImageView(this);
                                        iIcon.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
                                        iIcon.setScaleType(ImageView.ScaleType.CENTER);
                                        iIcon.setColorFilter(ContextCompat.getColor(this, R.color.primary_dark));

                                        ImageView oIcon = new ImageView(this);
                                        oIcon.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
                                        oIcon.setScaleType(ImageView.ScaleType.CENTER);
                                        oIcon.setColorFilter(ContextCompat.getColor(this, R.color.primary_dark));

                                        if (hasBeenPrinted == 0) {
                                            iIcon.setImageResource(R.drawable.ic_undone);
                                            oIcon.setImageResource(R.drawable.ic_undone);
                                        } else if (hasBeenPrinted == 1) {
                                            iIcon.setImageResource(R.drawable.ic_done);
                                            oIcon.setImageResource(R.drawable.ic_undone);
                                        } else if (hasBeenPrinted == 2) {
                                            iIcon.setImageResource(R.drawable.ic_done);
                                            oIcon.setImageResource(R.drawable.ic_done);
                                        } else {
                                            iIcon.setImageResource(R.drawable.ic_done_all);
                                            oIcon.setImageResource(R.drawable.ic_done_all);
                                        }

                                        row.addView(iIcon);
                                        row.addView(oIcon);

                                        row.setOnClickListener(v -> {
                                            // Reset selected row sebelumnya jika ada
                                            if (selectedRowHeader != null && selectedRowHeader.getTag() != null) {
                                                int prevIndex = (int) selectedRowHeader.getTag();
                                                // Kembalikan warna asli row sebelumnya
                                                if (prevIndex % 2 == 0) {
                                                    selectedRowHeader.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
                                                } else {
                                                    selectedRowHeader.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                                                }
                                            }

                                            // Set row yang baru dipilih
                                            selectedRowHeader = row;
                                            // Highlight row yang dipilih (gunakan warna berbeda)
                                            row.setBackgroundColor(ContextCompat.getColor(this, R.color.primary)); // atau warna highlight lainnya
                                            TableUtils.setTextColor(this, row, R.color.white);

                                            // Tampilkan tooltip dengan parameter yang benar
                                            TooltipUtils.fetchDataAndShowTooltip(
                                                    this,
                                                    executorService,
                                                    selectedRowHeader, // Parameter ketiga adalah selectedRow, bukan connection
                                                    noBarangJadi, // currentNoBJ
                                                    "BarangJadi_h",
                                                    "BarangJadi_d",
                                                    "NoBJ",
                                                    () -> {
                                                        // Callback saat popup ditutup - dengan null check
                                                        if (selectedRowHeader != null && selectedRowHeader.getTag() != null) {
                                                            int currentIndex = (int) selectedRowHeader.getTag();
                                                            // Reset warna row ke warna asli
                                                            if (currentIndex % 2 == 0) {
                                                                selectedRowHeader.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
                                                            } else {
                                                                selectedRowHeader.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                                                            }
                                                            TableUtils.setTextColor(this, row, R.color.black);
                                                            selectedRowHeader = null;
                                                        } else {
                                                            // Jika tag null, tetap reset selectedRowHeader
                                                            selectedRowHeader = null;
                                                        }
                                                    }
                                            );
                                        });

                                        TabelOutput.addView(row);
                                        labelCount++;
                                    }

                                    tvLabelCount.setText("Total Label : " + labelCount);

                                } else {
                                    Log.d("LabelNull", "Tidak ada data pada mesin atau susun");
                                }
                            });

                        }
                    }
                } else {
                    runOnUiThread(() -> {
                        // Handle koneksi database gagal
                        Toast.makeText(this, "Gagal terhubung ke database", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (SQLException e) {
                runOnUiThread(() -> {
                    // Handle error SQL
                    Log.e("Database Error", "Error: " + e.getMessage());
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                });
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        Log.e("Database Error", "Error closing connection: " + e.getMessage());
                    }
                }
            }
        }).start();
    }

    private void fetchDataAndShowTooltip(View anchorView, String noBJ) {
        new Thread(() -> {
            Connection connection = null;
            try {
                connection = ConnectionClass(); // Koneksi ke database
                if (connection != null) {
                    // Query utama untuk mengambil detail tooltip
                    String detailQuery = "SELECT h.NoBJ , h.DateCreate, h.Jam, k.Jenis, h.NoSPK, " +
                            "b1.Buyer AS BuyerNoSPK, h.NoSPKAsal, b2.Buyer AS BuyerNoSPKAsal, bj.NamaBarangJadi, h.IsLembur " +
                            "FROM BarangJadi_h h " +
                            "LEFT JOIN MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                            "LEFT JOIN MstSPK_h s1 ON h.NoSPK = s1.NoSPK " +
                            "LEFT JOIN MstBuyer b1 ON s1.IdBuyer = b1.IdBuyer " +
                            "LEFT JOIN MstSPK_h s2 ON h.NoSPKAsal = s2.NoSPK " +
                            "LEFT JOIN MstBuyer b2 ON s2.IdBuyer = b2.IdBuyer " +
                            "LEFT JOIN MstBarangJadi bj ON h.IdBarangJadi = bj.IdBarangJadi " +
                            "WHERE h.NoBJ = ?";

                    PreparedStatement detailStmt = connection.prepareStatement(detailQuery);
                    detailStmt.setString(1, noBJ);
                    ResultSet detailRs = detailStmt.executeQuery();

                    String retrievedNoBJ = null;
                    String formattedDateTime = null;
                    String jenis = null;
                    String spkDetail = null;
                    String spkAsalDetail = null;
                    String namaGrade = null;
                    boolean isLembur = false;

                    if (detailRs.next()) {
                        retrievedNoBJ = detailRs.getString("NoBJ");
                        String dateCreate = detailRs.getString("DateCreate");
                        String jam = detailRs.getString("Jam");
                        jenis = detailRs.getString("Jenis");
                        String noSPK = detailRs.getString("NoSPK");
                        String buyerNoSPK = detailRs.getString("BuyerNoSPK");
                        String noSPKAsal = detailRs.getString("NoSPKAsal");
                        String buyerNoSPKAsal = detailRs.getString("BuyerNoSPKAsal");
                        namaGrade = detailRs.getString("NamaBarangJadi");
                        isLembur = detailRs.getBoolean("IsLembur");

                        spkDetail = (noSPK != null && buyerNoSPK != null) ? noSPK + " - " + buyerNoSPK : "No data";
                        spkAsalDetail = (noSPKAsal != null && buyerNoSPKAsal != null) ? noSPKAsal + " - " + buyerNoSPKAsal : "No data";
                        formattedDateTime = combineDateTime(dateCreate, jam);
                    }

                    // Query untuk mengambil data tabel
                    String tableQuery = "SELECT Tebal, Lebar, Panjang, JmlhBatang FROM BarangJadi_d WHERE NoBJ = ? ORDER BY NoUrut";
                    PreparedStatement tableStmt = connection.prepareStatement(tableQuery);
                    tableStmt.setString(1, noBJ);

                    ResultSet tableRs = tableStmt.executeQuery();
                    List<String[]> tableData = new ArrayList<>();
                    int totalPcs = 0;
                    double totalM3 = 0.0;

                    while (tableRs.next()) {
                        // Ambil data dari tabel
                        double tebal = tableRs.getDouble("Tebal");
                        double lebar = tableRs.getDouble("Lebar");
                        double panjang = tableRs.getDouble("Panjang");
                        int pcs = tableRs.getInt("JmlhBatang");

                        totalPcs += pcs;

                        // Hitung M3 untuk baris ini
                        double rowM3 = (tebal * lebar * panjang * pcs) / 1000000000.0;
                        rowM3 = Math.floor(rowM3 * 10000) / 10000;
                        totalM3 += rowM3;

                        // Format data untuk tabel
                        tableData.add(new String[]{
                                String.valueOf((int) tebal),
                                String.valueOf((int) lebar),
                                String.valueOf((int) panjang),
                                String.valueOf(pcs)
                        });
                    }

                    // Pindahkan eksekusi ke UI thread untuk menampilkan tooltip
                    String finalRetrievedNoBJ = retrievedNoBJ;
                    String finalFormattedDateTime = formattedDateTime;
                    String finalJenis = jenis;
                    String finalSpkDetail = spkDetail;
                    String finalSpkAsalDetail = spkAsalDetail;
                    String finalNamaGrade = namaGrade;
                    boolean finalIsLembur = isLembur;
                    int finalTotalPcs = totalPcs;
                    double finalTotalM3 = totalM3;

                    runOnUiThread(() -> showTooltip(
                            anchorView,
                            finalRetrievedNoBJ,
                            finalFormattedDateTime,
                            finalJenis,
                            finalSpkDetail,
                            finalSpkAsalDetail,
                            finalNamaGrade,
                            finalIsLembur,
                            tableData,
                            finalTotalPcs,
                            finalTotalM3
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show());
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }



    // Metode untuk menggabungkan Date dan Time
    private String combineDateTime(String date, String time) {
        try {
            // Format input Date (dari database)
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date parsedDate = inputDateFormat.parse(date);

            // Gabungkan Date dan Time
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String formattedDateTime = outputFormat.format(parsedDate) + " (" + time + ")";
            return formattedDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return date + " " + time; // Jika terjadi error, kembalikan format default
        }
    }

    private void showTooltip(View anchorView, String noBJ, String formattedDateTime, String jenis, String spkDetail, String spkAsalDetail, String namaGrade, boolean isLembur, List<String[]> tableData, int totalPcs, double totalM3) {
        // Inflate layout tooltip
        View tooltipView = LayoutInflater.from(this).inflate(R.layout.tooltip_layout_right, null);

        // Set data pada TextView
        ((TextView) tooltipView.findViewById(R.id.tvNoLabel)).setText(noBJ);
        ((TextView) tooltipView.findViewById(R.id.tvDateTime)).setText(formattedDateTime);
        ((TextView) tooltipView.findViewById(R.id.tvJenis)).setText(jenis);
        ((TextView) tooltipView.findViewById(R.id.tvNoSPK)).setText(spkDetail);
        ((TextView) tooltipView.findViewById(R.id.tvNoSPKAsal)).setText(spkAsalDetail);
        ((TextView) tooltipView.findViewById(R.id.tvNamaGrade)).setText(namaGrade);
        ((TextView) tooltipView.findViewById(R.id.tvIsLembur)).setText(isLembur ? "Yes" : "No");

        tooltipView.findViewById(R.id.tvNoKBSuket).setVisibility(View.GONE);
        tooltipView.findViewById(R.id.fieldPlatTruk).setVisibility(View.GONE);

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

        ImageView trianglePointer = tooltipView.findViewById(R.id.trianglePointer);

        // Menaikkan pointer ketika popup melebihi batas layout
        if (y < 60) {
            trianglePointer.setY(y - 60);
        }


        // Tampilkan tooltip
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);
    }


    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }


    // Deklarasi CheckSPKDataTask di level class
    private class CheckSPKDataTask extends AsyncTask<Void, Void, String> {
        private final String noSPK;
        private final String tebal;
        private final String lebar;
        private final String panjang;
        private final int idJenisKayu;
        private final int idBarangJadi;

        public CheckSPKDataTask(String noSPK, String tebal, String lebar, String panjang, int idJenisKayu, int idBarangJadi) {
            this.noSPK = noSPK;
            this.tebal = tebal;
            this.lebar = lebar;
            this.panjang = panjang;
            this.idJenisKayu = idJenisKayu;
            this.idBarangJadi = idBarangJadi;
        }

        @Override
        protected String doInBackground(Void... voids) {
            Connection connection = null;
            try {
                // Cek apakah SPK terkunci
                if (!isSPKLocked(noSPK)) {
                    return "SUCCESS"; // Tidak perlu pengecekan jika tidak terkunci
                }

                connection = ConnectionClass();
                if (connection == null) {
                    return "Koneksi database gagal";
                }

                // Query untuk cek UnlockGrade
                String unlockQuery = "SELECT UnlockGradeBJ FROM MstSPK_h WHERE NoSPK = ?";
                PreparedStatement unlockStmt = connection.prepareStatement(unlockQuery);
                unlockStmt.setString(1, noSPK);
                ResultSet unlockRs = unlockStmt.executeQuery();
                boolean isUnlockGrade = false;

                if (unlockRs.next()) {
                    isUnlockGrade = unlockRs.getInt("UnlockGradeBJ") == 1;
                }

                // Query untuk validasi data
                String query = "SELECT * FROM MstSPK_d WHERE NoSPK = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, noSPK);
                ResultSet rs = stmt.executeQuery();

                boolean matchFound = false;

                while (rs.next()) {
                    if (Double.parseDouble(tebal) == rs.getDouble("Tebal") &&
                            Double.parseDouble(lebar) == rs.getDouble("Lebar") &&
                            Double.parseDouble(panjang) == rs.getDouble("Panjang") &&
                            idJenisKayu == rs.getInt("IdJenisKayu")) {

                        if (!isUnlockGrade && idBarangJadi == (rs.getInt("IdBarangJadi"))) {
                            matchFound = true;
                            break;
                        } else if (isUnlockGrade) {
                            matchFound = true;
                            break;
                        }
                    }
                }

                if (!matchFound) {
                    // Tentukan kolom yang tidak sesuai
                    StringBuilder mismatchMessage = new StringBuilder("Data Tidak Sesuai: ");
                    if (!columnMatches(connection, "Tebal", noSPK, tebal)) {
                        mismatchMessage.append("Tebal, ");
                    }
                    if (!columnMatches(connection, "Lebar", noSPK, lebar)) {
                        mismatchMessage.append("Lebar, ");
                    }
                    if (!columnMatches(connection, "Panjang", noSPK, panjang)) {
                        mismatchMessage.append("Panjang, ");
                    }
                    if (!columnMatches(connection, "IdJenisKayu", noSPK, String.valueOf(idJenisKayu))) {
                        mismatchMessage.append("Jenis Kayu, ");
                    }
                    if (!isUnlockGrade && !columnMatches(connection, "IdBarangJadi", noSPK, String.valueOf(idBarangJadi))) {
                        mismatchMessage.append("Barang Jadi, ");
                    }

                    // Hapus koma terakhir
                    if (mismatchMessage.toString().endsWith(", ")) {
                        mismatchMessage.setLength(mismatchMessage.length() - 2);
                    }
                    return mismatchMessage.toString();
                }
                return "SUCCESS";

            } catch (SQLException e) {
                e.printStackTrace();
                return "Error database: " + e.getMessage();
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean columnMatches(Connection connection, String columnName, String noSPK, String value) throws SQLException {
        String query = "SELECT " + columnName + " FROM MstSPK_d WHERE NoSPK = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, noSPK);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    if (columnName.equalsIgnoreCase("Tebal") || columnName.equalsIgnoreCase("Lebar") || columnName.equalsIgnoreCase("Panjang")) {
                        return Double.parseDouble(value) == rs.getDouble(columnName);
                    } else {
                        return value.equals(rs.getString(columnName));
                    }
                }
            }
        }
        return false;
    }

    // Method untuk mengambil semua data dimensi dari database
    private Map<String, List<String>> listSPKDetailRecommendation(String noSPK) {
        Map<String, List<String>> dimensionData = new HashMap<>();
        dimensionData.put("tebal", new ArrayList<>());
        dimensionData.put("lebar", new ArrayList<>());
        dimensionData.put("panjang", new ArrayList<>());

        Connection connection = null;

        try {
            connection = ConnectionClass();
            if (connection != null) {
                String query = "SELECT DISTINCT Tebal, Lebar, Panjang FROM MstSPK_d WHERE NoSPK = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, noSPK);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    dimensionData.get("tebal").add(rs.getString("Tebal"));
                    dimensionData.get("lebar").add(rs.getString("Lebar"));
                    dimensionData.get("panjang").add(rs.getString("Panjang"));
                }
            } else {
                Log.e("Database", "Koneksi database gagal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Database", "Error fetching dimension data: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return dimensionData;
    }

    private boolean isSPKLocked(String noSPK) {
        Connection connection = null;
        boolean isLocked = false;
        try {
            connection = ConnectionClass();
            if (connection != null) {
                String query = "SELECT LockDimensionBJ FROM MstSPK_h WHERE NoSPK = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, noSPK);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            Integer lockDimension = rs.getInt("LockDimensionBJ");
                            isLocked = (lockDimension != null && lockDimension == 1);
                        }
                    }
                }
            } else {
                Log.e("Database", "Koneksi database gagal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Database", "Error checking lock dimension: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return isLocked;
    }

    private void enableForm(){
        Date.setEnabled(true);
        Time.setEnabled(true);
        SpinKayu.setEnabled(true);
        radioButtonMesin.setEnabled(true);
        radioButtonBSusun.setEnabled(true);
        SpinMesin.setEnabled(true);
        SpinSusun.setEnabled(true);
        SpinTelly.setEnabled(true);
        SpinSPK.setEnabled(true);
        SpinSPKAsal.setEnabled(true);
        SpinProfile.setEnabled(true);
        DetailTebal.setEnabled(true);
        DetailLebar.setEnabled(true);
        DetailPanjang.setEnabled(true);
        DetailPcs.setEnabled(true);
        BtnHapusDetail.setEnabled(true);
        BtnInputDetail.setEnabled(true);
        SpinBarangJadi.setEnabled(true);
        CBLembur.setEnabled(true);
        CBAfkir.setEnabled(true);
        remarkLabel.setEnabled(true);
        spinLokasi.setEnabled(true);
    }

    private void disableForm(){
        Date.setEnabled(false);
        Time.setEnabled(false);
        SpinKayu.setEnabled(false);
        radioButtonMesin.setEnabled(false);
        radioButtonBSusun.setEnabled(false);
        SpinMesin.setEnabled(false);
        SpinSusun.setEnabled(false);
        SpinTelly.setEnabled(false);
        SpinSPK.setEnabled(false);
        SpinSPKAsal.setEnabled(false);
        SpinProfile.setEnabled(false);
        DetailTebal.setEnabled(false);
        DetailLebar.setEnabled(false);
        DetailPanjang.setEnabled(false);
        DetailPcs.setEnabled(false);
        BtnHapusDetail.setEnabled(false);
        BtnInputDetail.setEnabled(false);
        SpinBarangJadi.setEnabled(false);
        BtnSimpan.setEnabled(false);
        CBLembur.setEnabled(false);
        CBAfkir.setEnabled(false);
        remarkLabel.setEnabled(false);
        spinLokasi.setEnabled(false);


        // Loop semua row di tabel
        for (int i = 0; i < Tabel.getChildCount(); i++) {
            View rowView = Tabel.getChildAt(i);
            if (rowView instanceof TableRow) {
                TableRow row = (TableRow) rowView;
                // Loop semua child di row
                for (int j = 0; j < row.getChildCount(); j++) {
                    View child = row.getChildAt(j);
                    if (child instanceof LinearLayout) {
                        LinearLayout actionLayout = (LinearLayout) child;
                        for (int k = 0; k < actionLayout.getChildCount(); k++) {
                            View actionChild = actionLayout.getChildAt(k);
                            if (actionChild instanceof ImageButton) {
                                actionChild.setEnabled(false); // disable tombol edit & delete
                                actionChild.setAlpha(0.5f); // opsional: kasih efek transparan biar keliatan disable
                            }
                        }
                    }
                }
            }
        }
    }

    private void resetAllForm() {
        Date.setText("");
        Time.setText("");
        NoBarangJadi.setText("");
        remarkLabel.setText("");

        loadJenisKayuSpinner(0);
        loadLokasiSpinner("","");
        loadSPKSpinner("0");
        loadSPKAsalSpinner("0");
    }
    

    private void loadSubmittedData(String noBarangJadi) {
        // Tampilkan progress dialog
        loadingDialogHelper.show(this);
        resetDetailData();

        executorService.execute(() -> {
            try {
                // Load header data using BjApi - connection sudah dikelola internal
                BjData headerData = BjApi.getSndHeader(noBarangJadi);

                if (headerData != null) {
                    // Load detail data using BjApi - connection sudah dikelola internal
                    List<LabelDetailData> detailDataList = BjApi.getSndDetail(noBarangJadi);

                    // Update temporary data
                    temporaryDataListDetail.clear();
                    temporaryDataListDetail.addAll(detailDataList);

                    // Update UI di thread utama
                    runOnUiThread(() -> {
                        try {
                            updateUIWithHeaderData(headerData);
                            updateTableFromTemporaryData();
                            m3();
                            jumlahpcs();

                            Toast.makeText(getApplicationContext(),
                                    "Data berhasil dimuat",
                                    Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            loadingDialogHelper.hide();
                            Log.e("UI Update Error", "Error updating UI: " + e.getMessage(), e);
                        }
                    });
                } else {
                    runOnUiThread(() -> {
                        loadingDialogHelper.hide();
                        Toast.makeText(getApplicationContext(),
                                "Data tidak ditemukan untuk noBarangJadi: " + noBarangJadi,
                                Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                final String errorMessage = e.getMessage();
                runOnUiThread(() -> {
                    loadingDialogHelper.hide();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + errorMessage,
                            Toast.LENGTH_LONG).show();
                    Log.e("Database Error", "Error loading data", e);
                });
            }
        });
    }


    private void updateUIWithHeaderData(BjData headerData) {
        radioGroup.clearCheck();
        radioButtonMesin.setEnabled(false);
        radioButtonBSusun.setEnabled(false);

        if (headerData.getDateCreate() != null) {
            String formattedDate = DateTimeUtils.formatDate(headerData.getDateCreate());
            Date.setText(formattedDate);
            rawDate = formattedDate;
        }
        Time.setText(headerData.getJam());

        // Jumlah spinner yang harus selesai
        final int totalSpinners = 7;
        final AtomicInteger completedCount = new AtomicInteger(0);

        Runnable onSpinnerDone = () -> {
            if (completedCount.incrementAndGet() == totalSpinners) {
                // Semua spinner selesai, baru hide loading
                loadingDialogHelper.hide();
            }
        };

        // Panggil spinner dengan callback onSpinnerDone
        loadTellySpinner(headerData.getIdOrgTelly(), onSpinnerDone);
        loadSPKSpinner(headerData.getNoSPK(), onSpinnerDone);
        loadSPKAsalSpinner(headerData.getNoSPKAsal(), onSpinnerDone);

        if (headerData.getIdJenisKayu() != null) {
            loadJenisKayuSpinner(Integer.parseInt(headerData.getIdJenisKayu()), onSpinnerDone);
        } else {
            // Kalau skip spinner, tetap hitung selesai
            onSpinnerDone.run();
            onSpinnerDone.run();
        }

        loadLokasiSpinner(headerData.getIdLokasi(), "", onSpinnerDone);
        loadProfileSpinner(headerData.getIdFJProfile(), onSpinnerDone);
        loadBjSpinner(headerData.getIdBarangJadi(), onSpinnerDone);

        // Set checkboxes & remark (ini synchronous, langsung aja)
        CBAfkir.setChecked("1".equals(headerData.getIsReject()));
        CBLembur.setChecked("1".equals(headerData.getIsLembur()));
        remarkLabel.setText(headerData.getRemark());

        // Radio button
        if (headerData.getNamaMesin() != null) {
            radioGroup.check(R.id.radioButtonMesin);
            mesinView.setText(headerData.getNamaMesin() + " - " + headerData.getNoProduksi());
            susunView.setText("-");
            loadOutputByMesinSusun(headerData.getNoProduksi(), true);
        }
        if (headerData.getNoBongkarSusun() != null) {
            radioGroup.check(R.id.radioButtonBSusun);
            susunView.setText(headerData.getNoBongkarSusun());
            mesinView.setText("-");
            loadOutputByMesinSusun(headerData.getNoBongkarSusun(), false);
        }
    }

    // Method baru untuk memperbarui tabel dari temporaryDataListDetail
    private void updateTableFromTemporaryData() {
        rowCount = 0;
        DecimalFormat df = new DecimalFormat("#,###.##");

        for (LabelDetailData data : temporaryDataListDetail) {
            TableRow newRow = new TableRow(this);
            newRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            addTextViewToRowWithWeight(newRow, String.valueOf(++rowCount), 0);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(data.getTebal())), 0);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(data.getLebar())), 0);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(data.getPanjang())), 0);
            addTextViewToRowWithWeight(newRow, df.format(Integer.parseInt(data.getPcs())), 0);

            // layout untuk tombol
            LinearLayout actionLayout = new LinearLayout(this);
            actionLayout.setOrientation(LinearLayout.HORIZONTAL);

            // ✅ Cek permission edit
            if (userPermissions.contains("label_bj:update")) {
                ImageButton editButton = new ImageButton(this);
                editButton.setImageResource(R.drawable.ic_edit);
                editButton.setBackgroundColor(Color.TRANSPARENT);
                editButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                editButton.setPadding(5, 5, 5, 5);

                LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                editParams.setMargins(0, 0, 10, 0);
                editButton.setLayoutParams(editParams);

                editButton.setOnClickListener(v -> showEditDetailDialog(data));

                actionLayout.addView(editButton);
            }

            // ✅ Cek permission delete
            if (userPermissions.contains("label_bj:delete")) {
                ImageButton deleteButton = new ImageButton(this);
                deleteButton.setImageResource(R.drawable.ic_delete);
                deleteButton.setBackgroundColor(Color.TRANSPARENT);
                deleteButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                deleteButton.setPadding(15, 5, 5, 5);

                LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                deleteParams.setMargins(10, 0, 0, 0);
                deleteButton.setLayoutParams(deleteParams);

                deleteButton.setOnClickListener(v -> {
                    Tabel.removeView(newRow);
                    temporaryDataListDetail.remove(data);
                    updateRowNumbers();
                    jumlahpcs();
                    m3();
                });

                actionLayout.addView(deleteButton);
            }

            // hanya tambahkan actionLayout kalau ada tombol di dalamnya
            if (actionLayout.getChildCount() > 0) {
                newRow.addView(actionLayout);
            }

            Tabel.addView(newRow);
        }
    }


    private void showEditDetailDialog(LabelDetailData data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Detail");

        // Inflate layout XML
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_detail_label, null);
        builder.setView(dialogView);

        TextInputEditText editTebal = dialogView.findViewById(R.id.editTebal);
        TextInputEditText editLebar = dialogView.findViewById(R.id.editLebar);
        TextInputEditText editPanjang = dialogView.findViewById(R.id.editPanjang);
        TextInputEditText editJumlah = dialogView.findViewById(R.id.editJumlah);

        // Set nilai awal
        editTebal.setText(data.getTebal());
        editLebar.setText(data.getLebar());
        editPanjang.setText(data.getPanjang());
        editJumlah.setText(data.getPcs());

        builder.setPositiveButton("Simpan", (dialog, which) -> {
            // Update data
            data.setTebal(editTebal.getText().toString());
            data.setLebar(editLebar.getText().toString());
            data.setPanjang(editPanjang.getText().toString());
            data.setPcs(editJumlah.getText().toString());

            // Refresh tabel
            Tabel.removeViews(1, Tabel.getChildCount() - 1);
            updateTableFromTemporaryData();
            jumlahpcs();
            m3();
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    interface HasBeenPrintedCallback {
        void onResult(int count);  // Callback menerima count
    }

    // Method untuk mengecek status HasBeenPrinted dengan penanganan NULL
    private void checkHasBeenPrinted(String noBarangJadi, Packing.HasBeenPrintedCallback callback) {
        new Thread(() -> {
            int hasBeenPrintedValue = -1;
            boolean existsInH = false;
            boolean existsInD = false;
            String dateUsage = null;
            boolean hasBeenProcess = false;
            Connection connection = null;

            try {
                // Mendapatkan koneksi dari method ConnectionClass
                connection = ConnectionClass();
                if (connection != null) {

                    String queryCheckH = "SELECT HasBeenPrinted, DateUsage FROM BarangJadi_h WHERE NoBJ = ?";
                    String queryCheckD = "SELECT 1 FROM BarangJadi_d WHERE NoBJ = ?";

                    // Cek keberadaan di BarangJadi_h
                    try (PreparedStatement stmtH = connection.prepareStatement(queryCheckH)) {
                        stmtH.setString(1, noBarangJadi);
                        try (ResultSet rsH = stmtH.executeQuery()) {
                            if (rsH.next()) {
                                hasBeenPrintedValue = rsH.getInt("HasBeenPrinted");
                                dateUsage = rsH.getString("DateUsage");
                                existsInH = true;
                                if (dateUsage != null) {
                                    hasBeenProcess = true;
                                }
                            }
                        }
                    }

                    // Cek keberadaan di BarangJadi_d
                    try (PreparedStatement stmtD = connection.prepareStatement(queryCheckD)) {
                        stmtD.setString(1, noBarangJadi);
                        try (ResultSet rsD = stmtD.executeQuery()) {
                            if (rsD.next()) {
                                existsInD = true;
                            }
                        }
                    }
                } else {
                    Log.e("Database", "Koneksi database gagal");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("Database", "Error checking HasBeenPrinted status: " + e.getMessage());
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            final int finalHasBeenPrintedValue = (existsInH && existsInD) ? hasBeenPrintedValue : -1; // Set -1 jika tidak ditemukan di kedua tabel
            final boolean finalIsAvailable = existsInH && existsInD; // Hanya valid jika ada di kedua tabel
            final boolean finalHasBeenProcess = hasBeenProcess;
            final String finalDateUsage =  DateTimeUtils.formatDate(dateUsage);

            runOnUiThread(() -> {
                if (!finalIsAvailable) {
                    // Data tidak ditemukan di kedua tabel
                    Toast.makeText(getApplicationContext(), "Data tidak tersedia", Toast.LENGTH_SHORT).show();
                    callback.onResult(-1); // Indikasikan gagal
                }

                else if (finalHasBeenProcess) {
                    // Data tidak ditemukan di kedua tabel
                    Toast.makeText(getApplicationContext(), "Label Telah diproses pada " + finalDateUsage + "!", Toast.LENGTH_SHORT).show();
                    callback.onResult(-1); // Indikasikan gagal
                }

                else {
                    // Data ditemukan, kirimkan hasil HasBeenPrinted
                    callback.onResult(finalHasBeenPrintedValue);
                }
            });
        }).start();
    }


    // Method untuk mengupdate status HasBeenPrinted pada database
    private void updatePrintStatus(String noBarangJadi) {
        new Thread(() -> {
            Connection connection = null;
            try {
                // Mendapatkan koneksi dari method ConnectionClass
                connection = ConnectionClass();
                if (connection != null) {
                    // Query untuk menambah 1 pada nilai HasBeenPrinted
                    String query = "UPDATE BarangJadi_h SET HasBeenPrinted = COALESCE(HasBeenPrinted, 0) + 1, LastPrintDate = GETDATE() WHERE NoBJ = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, noBarangJadi);
                        int rowsAffected = stmt.executeUpdate();

                        if (rowsAffected > 0) {
                            runOnUiThread(() -> Toast.makeText(Packing.this,
                                    "Status cetak berhasil diupdate",
                                    Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(Packing.this,
                                    "Tidak ada data yang diupdate",
                                    Toast.LENGTH_SHORT).show());
                        }
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(Packing.this,
                            "Koneksi database gagal",
                            Toast.LENGTH_SHORT).show());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("Database", "Error updating HasBeenPrinted status: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(Packing.this,
                        "Gagal mengupdate status cetak: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show());
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }



    //Fungsi untuk add Data Detail
    private void addDataDetail(String noSanding) {
        String tebal = DetailTebal.getText().toString();
        String panjang = DetailPanjang.getText().toString();
        String lebar = DetailLebar.getText().toString();
        String pcs = DetailPcs.getText().toString();

        if (tebal.isEmpty() || panjang.isEmpty() || lebar.isEmpty() || pcs.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cek duplikasi data
        boolean isDuplicate = false;
        for (LabelDetailData existingData : temporaryDataListDetail) {
            if (existingData.getTebal().equals(tebal) &&
                    existingData.getPanjang().equals(panjang) &&
                    existingData.getLebar().equals(lebar)) {
                isDuplicate = true;
                break;
            }
        }

        if (isDuplicate) {
            Toast.makeText(this, "Data dengan ukuran yang sama sudah ada dalam tabel", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Buat objek DetailLabelData baru
            LabelDetailData newLabelDetailData = new LabelDetailData(tebal, lebar, panjang, pcs);
            temporaryDataListDetail.add(newLabelDetailData);

            // Buat baris tabel baru
            TableRow newRow = new TableRow(this);
            newRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            DecimalFormat df = new DecimalFormat("#,###.##");

            // Tambahkan kolom-kolom data
            addTextViewToRowWithWeight(newRow, String.valueOf(++rowCount), 0);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(tebal)), 0);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(lebar)), 0);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(panjang)), 0);
            addTextViewToRowWithWeight(newRow, String.valueOf(Integer.parseInt(pcs)), 0);

            // Layout untuk action (edit/delete)
            LinearLayout actionLayout = new LinearLayout(this);
            actionLayout.setOrientation(LinearLayout.HORIZONTAL);

            ImageButton editButton = new ImageButton(this);
            editButton.setImageResource(R.drawable.ic_edit);
            editButton.setBackgroundColor(Color.TRANSPARENT);
            editButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            editButton.setPadding(5, 5, 5, 5);

            LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            editParams.setMargins(0, 0, 10, 0);
            editButton.setLayoutParams(editParams);

            editButton.setOnClickListener(v -> showEditDetailDialog(newLabelDetailData));
            actionLayout.addView(editButton);


            ImageButton deleteButton = new ImageButton(this);
            deleteButton.setImageResource(R.drawable.ic_delete);
            deleteButton.setBackgroundColor(Color.TRANSPARENT);
            deleteButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            deleteButton.setPadding(15, 5, 5, 5);

            LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            deleteParams.setMargins(10, 0, 0, 0);
            deleteButton.setLayoutParams(deleteParams);

            deleteButton.setOnClickListener(v -> {
                Tabel.removeView(newRow);
                temporaryDataListDetail.remove(newLabelDetailData);
                updateRowNumbers();
                jumlahpcs();
                m3();
            });

            actionLayout.addView(deleteButton);


            if (actionLayout.getChildCount() > 0) {
                newRow.addView(actionLayout);
            }

            Tabel.addView(newRow);

            // Bersihkan field input
            DetailTebal.setText("");
            DetailPanjang.setText("");
            DetailLebar.setText("");
            DetailPcs.setText("");

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Format angka tidak valid", Toast.LENGTH_SHORT).show();
        }
    }

    // Metode helper yang baru untuk menambahkan TextView dengan weight
    private void addTextViewToRowWithWeight(TableRow row, String text, float weight) {
        TextView textView = new TextView(this);
        textView.setText(text);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0,
                TableRow.LayoutParams.WRAP_CONTENT, weight);
        params.setMargins(5, 5, 5, 5);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(10, 10, 10, 10);
        row.addView(textView);
    }

    // Metode untuk memperbarui nomor baris setelah penghapusan
    private void updateRowNumbers() {
        for (int i = 1; i < Tabel.getChildCount(); i++) {
            TableRow row = (TableRow) Tabel.getChildAt(i);
            TextView numberView = (TextView) row.getChildAt(0);
            numberView.setText(String.valueOf(i));
        }
        rowCount = Tabel.getChildCount() - 1;
    }
    private void resetDetailData() {
        // Reset temporary list detail
        temporaryDataListDetail.clear();

        // Reset row counter
        rowCount = 0;

        // Reset tabel detail (hapus semua baris kecuali header)
        if (Tabel.getChildCount() > 1) {
            Tabel.removeViews(1, Tabel.getChildCount() - 1);
        }

        // Reset input fields
        if (DetailTebal != null) {
            DetailTebal.setText("");
        }
        if (DetailLebar != null) {
            DetailLebar.setText("");
        }
        if (DetailPanjang != null) {
            DetailPanjang.setText("");
        }
        if (DetailPcs != null) {
            DetailPcs.setText("");
        }
    }

    private void saveDataDetailToDatabase(String noBarangJadi, int noUrut, double tebal, double lebar, double panjang, int pcs) {
        new Packing.SaveDataTaskDetail().execute(noBarangJadi, String.valueOf(noUrut), String.valueOf(tebal), String.valueOf(lebar),
                String.valueOf(panjang), String.valueOf(pcs));
    }

    private class SaveDataTaskDetail extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String noBarangJadi = params[0];
            String noUrut = params[1];
            String tebal = params[2];
            String lebar = params[3];
            String panjang = params[4];
            String pcs = params[5];

            try {
                Connection connection = ConnectionClass();
                if (connection != null) {
                    String query = "INSERT INTO dbo.BarangJadi_d (NoBJ, NoUrut, Tebal, Lebar, Panjang, JmlhBatang) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, noBarangJadi);
                    preparedStatement.setInt(2, Integer.parseInt(noUrut));
                    preparedStatement.setDouble(3, Double.parseDouble(tebal));
                    preparedStatement.setDouble(4, Double.parseDouble(lebar));
                    preparedStatement.setDouble(5, Double.parseDouble(panjang));
                    preparedStatement.setInt(6, Integer.parseInt(pcs));

                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                } else {
                    Log.e("DB_CONNECTION", "Koneksi ke database gagal");
                }
            } catch (SQLException e) {
                Log.e("DB_ERROR", "SQL Exception: " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
//            if (success) {
//                Log.d("DB_INSERT", "Data Detail berhasil disimpan");
//            } else {
//                Log.e("DB_INSERT", "Data gagal disimpan");
//            }
        }
    }


    private void clearData() {
        NoBarangJadi.setText("");
        M3.setText("");
        JumlahPcs.setText("");
        CBAfkir.setChecked(false);
        CBLembur.setChecked(false);
        SpinTelly.setSelection(0);
        SpinKayu.setSelection(0);
        SpinSPK.setSelection(0);
        SpinSPKAsal.setSelection(0);
        SpinProfile.setSelection(0);
        SpinSusun.setEnabled(false);
        SpinMesin.setEnabled(false);
        radioGroup.clearCheck();
        remarkLabel.setText("");

    }


    private void m3() {
        try {
            double totalM3 = 0.0;

            for (LabelDetailData row : temporaryDataListDetail) {

                // Parse nilai-nilai langsung tanpa membersihkan
                double tebal = Double.parseDouble(row.getTebal());
                double lebar = Double.parseDouble(row.getLebar());
                double panjang = Double.parseDouble(row.getPanjang());
                int pcs = Integer.parseInt(row.getPcs());

                // Hitung M3 untuk baris ini
                double rowM3 = (tebal * lebar * panjang * pcs) / 1000000000.0;
                rowM3 = Math.floor(rowM3 * 10000) / 10000;

                totalM3 += rowM3;
            }

            // Format hasil
            DecimalFormat df = new DecimalFormat("0.0000");
            String formattedM3 = df.format(totalM3);

            // Update TextView
            TextView M3TextView = findViewById(R.id.M3);
            if (M3TextView != null) {
                M3TextView.setText(formattedM3);
                // Debug: Konfirmasi setText
                Log.d("M3_DEBUG", "TextView updated with: " + formattedM3);
            } else {
                Log.e("M3_DEBUG", "M3TextView is null!");
            }

        } catch (Exception e) {
            Log.e("M3_DEBUG", "Error in m3 calculation: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void jumlahpcs() {
        TableLayout table = findViewById(R.id.Tabel);
        int childCount = table.getChildCount();

        long totalPcs = 0;  // Menggunakan long untuk menangani angka besar

        for (int i = 1; i < childCount; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            TextView pcsTextView = (TextView) row.getChildAt(4); // Indeks pcs

            String pcsString = pcsTextView.getText().toString().replace(",", "").replace(".", ""); // Hapus koma dan titik

            try {
                long pcs = Long.parseLong(pcsString);  // Gunakan Long.parseLong untuk angka lebih besar
                totalPcs += pcs;
            } catch (NumberFormatException e) {
                // Menangani jika ada input yang tidak valid
                Log.e("JumlahPcs", "Format angka tidak valid: " + pcsString);
            }
        }

        JumlahPcs.setText(String.valueOf(totalPcs));
    }


    private void setCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        SimpleDateFormat saveFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        Date.setText(currentDate);
        rawDate = saveFormat.format(new Date());


        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        Time.setText(currentTime);

        loadMesinSpinner(currentDate);
        loadSusunSpinner(currentDate);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Packing.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                // Format input (dari DatePicker)
                rawDate = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);

                try {
                    SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

                    Date date = inputDateFormat.parse(rawDate);

                    String formattedDate = outputDateFormat.format(date);

                    Date.setText(formattedDate);

                    loadMesinSpinner(rawDate);
                    loadSusunSpinner(rawDate);

                } catch (Exception e) {
                    e.printStackTrace();
                    Date.setText("Invalid Date");
                }
            }

        }, year, month, day);

        datePickerDialog.show();
    }


    private void showTimePickerDialog() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Packing.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendar.set(Calendar.MINUTE, selectedMinute);

                SimpleDateFormat timeFormat = new SimpleDateFormat(" HH:mm:ss", Locale.getDefault());
                String updatedTime = timeFormat.format(calendar.getTime());
                Time.setText(updatedTime);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    } 

   

    // Helper method yang diperbarui untuk menangani wrap text
    private void addInfoRow(Table table, String label, String value, PdfFont font) {
        // Label Cell
        Cell labelCell = new Cell()
                .setBorder(Border.NO_BORDER)
                .add(new Paragraph(label)
                        .setFont(font)
                        .setFontSize(11)
                        .setMargin(0)
                        .setMultipliedLeading(1.2f)
                        .setTextAlignment(TextAlignment.LEFT));

        // Colon Cell
        Cell colonCell = new Cell()
                .setBorder(Border.NO_BORDER)
                .add(new Paragraph(":")
                        .setFont(font)
                        .setFontSize(11)
                        .setMargin(0)
                        .setMultipliedLeading(1.2f)
                        .setTextAlignment(TextAlignment.CENTER));

        // Value Cell with text wrapping
        Cell valueCell = new Cell()
                .setBorder(Border.NO_BORDER);

        // Split panjang text jika melebihi batas
        String[] words = value.split(" ");
        StringBuilder line = new StringBuilder();
        StringBuilder finalText = new StringBuilder();

        for (String word : words) {
            if (line.length() + word.length() > 30) { // Batas karakter per baris
                finalText.append(line.toString().trim()).append("\n");
                line = new StringBuilder();
            }
            line.append(word).append(" ");

        }
        finalText.append(line.toString().trim());

        valueCell.add(new Paragraph(finalText.toString())
                .setFont(font)
                .setFontSize(11)
                .setMargin(0)
                .setMultipliedLeading(1.2f)
                .setTextAlignment(TextAlignment.LEFT));

        // Set minimum height untuk konsistensi
        float minHeight = 8f;
        labelCell.setMinHeight(minHeight);
        colonCell.setMinHeight(minHeight);
        valueCell.setMinHeight(minHeight);

        float pageWidth = PageSize.A6.getWidth() - 20;
        float tableWidth = table.getWidth().getValue();

        if (tableWidth == pageWidth * 0.4f) { // Kolom kiri
            valueCell.setWidth(pageWidth * 0.4f - 5);
        } else if (tableWidth == pageWidth * 0.6f) { // Kolom kanan lebih lebar
            valueCell.setWidth(pageWidth * 0.6f);
        }

        // Tambahkan semua cell ke tabel
        table.addCell(labelCell);
        table.addCell(colonCell);
        table.addCell(valueCell);
    }

    private void addTextDitheringWatermark(PdfDocument pdfDocument, PdfFont font) {
        for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
            PdfPage page = pdfDocument.getPage(i);
            // Menggunakan newContentStreamBefore() untuk menempatkan watermark di belakang
            PdfCanvas canvas = new PdfCanvas(
                    page.newContentStreamBefore(),
                    page.getResources(),
                    pdfDocument
            );

            Rectangle pageSize = page.getPageSize();
            float width = pageSize.getWidth();
            float height = pageSize.getHeight();

            canvas.saveState();

            String watermarkText = "COPY";
            float fontSize = 95;
            float textWidth = font.getWidth(watermarkText, fontSize);
            float textHeight = 175;

            // Posisi watermark di tengah halaman
            float centerX = width / 2 - 25;
            float centerY = height / 2 + 100;

            // Rotasi derajat
            double angle = Math.toRadians(0);
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            // Terapkan matriks transformasi untuk rotasi
            canvas.concatMatrix(cos, sin, -sin, cos, centerX, centerY);

            // Gambar teks watermark
            canvas.setFontAndSize(font, fontSize);
            canvas.setFillColor(ColorConstants.BLACK);

            float textX = (-textWidth / 2) + 25; // Offset teks ke tengah setelah rotasi
            float textY = (-textHeight / 2) + 50;

            canvas.beginText();
            canvas.setTextMatrix(textX, textY);
            canvas.showText(watermarkText);
            canvas.endText();

            // Pattern dithering (opsional, jika tetap ingin digunakan)
            float boxWidth = textWidth + 200;
            float boxHeight = textHeight + 200;
            float dotSize = 1.4f;
            float dotSpacing = 1f;

            canvas.setFillColor(ColorConstants.WHITE);

            for (float x = -boxWidth / 2; x < boxWidth / 2; x += dotSpacing) {
                for (float y = -boxHeight / 2; y < boxHeight / 2; y += dotSpacing) {
                    if ((Math.round(x) + Math.round(y)) % 4 == 0) {
                        canvas.circle(x, y, dotSize);
                        canvas.fill();
                    }
                }
            }
            canvas.restoreState();
        }
    }

    private Uri createPdf(String noBJ, String jenisKayu, String date, String time, String tellyBy, String mesinSusun, String noSPK, String noSPKasal, List<LabelDetailData> temporaryDataListDetail, String jumlahPcs, String m3, int printCount, String namaBJ, String remark) throws IOException {
        // Validasi parameter wajib
        if (noBJ == null || noBJ.trim().isEmpty()) {
            throw new IOException("Nomor FJ tidak boleh kosong");
        }

        if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
            throw new IOException("Data tidak boleh kosong");
        }

        String formattedTime = DateTimeUtils.formatTimeToHHmm(time);

        // Validasi dan set default value untuk parameter opsional
        noBJ = (noBJ != null) ? noBJ.trim() : "-";
        jenisKayu = (jenisKayu != null) ? jenisKayu.trim() : "-";
        date = (date != null) ? date.trim() : "-";
        formattedTime = (formattedTime != null) ? formattedTime.trim() : "-";
        tellyBy = (tellyBy != null) ? tellyBy.trim() : "-";
        noSPK = (noSPK != null) ? noSPK.trim() : "-";
        jumlahPcs = (jumlahPcs != null) ? jumlahPcs.trim() : "-";
        m3 = (m3 != null) ? m3.trim() : "-";
        remark = (remark != null) ? remark.trim() : "-";

        String[] nama = tellyBy.split(" ");
        String namaTelly = nama[0]; // namaDepan sekarang berisi "Windiar"


        Uri pdfUri = null;
        ContentResolver resolver = getContentResolver();
        String fileName = "BJ_" + noBJ + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".pdf";
        String relativePath = Environment.DIRECTORY_DOWNLOADS;

        try {
            // Hapus file yang sudah ada jika perlu
            Thread.sleep(500);

            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath);

            Uri uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
            if (uri == null) {
                throw new IOException("Gagal membuat file PDF");
            }

            OutputStream outputStream = resolver.openOutputStream(uri);
            if (outputStream == null) {
                throw new IOException("Gagal membuka output stream");
            }

            try {
                // Inisialisasi font dan dokumen
                PdfFont timesNewRoman = PdfFontFactory.createFont(StandardFonts.HELVETICA);
                PdfFont timesNewRomanBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

                PdfWriter writer = new PdfWriter(outputStream);
                PdfDocument pdfDocument = new PdfDocument(writer);

                // Ukuran kertas yang disesuaikan secara manual
                float baseHeight = 350; // Tinggi dasar untuk elemen non-tabel (header, footer, margin, dll.)
                float rowHeight = 20; // Tinggi rata-rata per baris data
                float totalHeight = baseHeight + (rowHeight * temporaryDataListDetail.size());

                // Tetapkan ukuran halaman dinamis
                Rectangle pageSize = new Rectangle( PageSize.A6.getWidth(), totalHeight);
                pdfDocument.setDefaultPageSize(new PageSize(pageSize));

                Document document = new Document(pdfDocument);
                document.setMargins(0, 5, 0, 5);

                // Header
                Paragraph judul = new Paragraph("LABEL FINGER JOIN")
                        .setUnderline()
                        .setBold()
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER);

                // Hitung lebar yang tersedia
                float pageWidth = PageSize.A6.getWidth() - 20;
                float[] mainColumnWidths = new float[]{pageWidth * 0.5f, pageWidth * 0.5f};

                Table mainTable = new Table(mainColumnWidths)
                        .setWidth(pageWidth)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setBorder(Border.NO_BORDER);

                float[] infoColumnWidths = new float[]{15, 5, 80};

                // Buat tabel untuk kolom kiri
                Table leftColumn = new Table(infoColumnWidths)
                        .setWidth(pageWidth * 0.453f)
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.LEFT);
                ;

                // Isi kolom kiri
//                addInfoRow(leftColumn, "No", noBJ, timesNewRoman);
                addInfoRow(leftColumn, "Jenis", jenisKayu, timesNewRoman);
                addInfoRow(leftColumn, "NamaBJ", namaBJ, timesNewRoman);
                addInfoRow(leftColumn, "Fisik", "BJ", timesNewRoman);

                // Buat tabel untuk kolom kanan
                Table rightColumn = new Table(infoColumnWidths)
                        .setWidth(pageWidth * 0.6f)
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.LEFT);

                // Isi kolom kanan
                addInfoRow(rightColumn, "Tgl", date + " (" + formattedTime + ")", timesNewRoman);
                addInfoRow(rightColumn, "Telly", namaTelly, timesNewRoman);
//                addInfoRow(rightColumn, "Mesin", mesinSusun, timesNewRoman);
                addInfoRow(rightColumn, "SPK", noSPK, timesNewRoman);

                // Tambahkan kolom kiri dan kanan ke tabel utama
                Cell leftCell = new Cell()
                        .add(leftColumn)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(0);

                Cell rightCell = new Cell()
                        .add(rightColumn)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(0);

                mainTable.addCell(leftCell);
                mainTable.addCell(rightCell);

                // Tabel data
                float[] width = {70f, 70f, 70f, 70f};
                Table table = new Table(width)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setMarginTop(1)
                        .setFontSize(13);

                // Header tabel
                String[] headers = {"Tebal", "Lebar", "Panjang", "Pcs"};
                for (String header : headers) {
                    table.addCell(new Cell()
                            .add(new Paragraph(header)
                                    .setTextAlignment(TextAlignment.CENTER)
                                    .setFont(timesNewRoman)));
                }

                // Isi tabel
                DecimalFormat df = new DecimalFormat("#,###.##");

                for (LabelDetailData row : temporaryDataListDetail) {
                    String tebal = (row.getTebal() != null) ? df.format(Float.parseFloat(row.getTebal())) : "-";
                    String lebar = (row.getLebar() != null) ? df.format(Float.parseFloat(row.getLebar())) : "-";
                    String panjang = (row.getPanjang() != null) ? df.format(Float.parseFloat(row.getPanjang())) : "-";
                    String pcs = (row.getPcs() != null) ? df.format(Integer.parseInt(row.getPcs())) : "-";

                    table.addCell(new Cell().add(new Paragraph(tebal + " mm").setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                    table.addCell(new Cell().add(new Paragraph(lebar + " mm").setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                    table.addCell(new Cell().add(new Paragraph(panjang + " mm").setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                    table.addCell(new Cell().add(new Paragraph(pcs).setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                }

                // Detail Pcs, Ton, M3
                float[] columnWidths = {30f, 10f, 70f};
                Table sumTable = new Table(columnWidths)
                        .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                        .setMarginTop(5)
                        .setFontSize(14)
                        .setBorder(Border.NO_BORDER);

                sumTable.addCell(new Cell().add(new Paragraph("Jumlah").setFixedLeading(15)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setFont(timesNewRoman));
                sumTable.addCell(new Cell().add(new Paragraph(":").setFixedLeading(15)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setFont(timesNewRoman));
                sumTable.addCell(new Cell().add(new Paragraph(String.valueOf(jumlahPcs)).setFixedLeading(15)).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER).setFont(timesNewRoman));

                sumTable.addCell(new Cell().add(new Paragraph("m\u00B3").setFixedLeading(15)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setFont(timesNewRoman));
                sumTable.addCell(new Cell().add(new Paragraph(":").setFixedLeading(15)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setFont(timesNewRoman));
                sumTable.addCell(new Cell().add(new Paragraph(String.valueOf(m3)).setFixedLeading(15)).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER).setFont(timesNewRoman));

                Paragraph qrCodeIDbottom = new Paragraph(noBJ).setTextAlignment(TextAlignment.LEFT).setFontSize(12).setMargins(-15, 0, 0, 47).setFont(timesNewRoman);

                BarcodeQRCode qrCode = new BarcodeQRCode(noBJ);
                PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);

                BarcodeQRCode qrCodeBottom = new BarcodeQRCode(noBJ);
                PdfFormXObject qrCodeBottomObject = qrCodeBottom.createFormXObject(ColorConstants.BLACK, pdfDocument);
                Image qrCodeBottomImage = new Image(qrCodeBottomObject).setWidth(115).setHorizontalAlignment(HorizontalAlignment.LEFT).setMargins(-55, 0, 0, 15);

                String formattedDate = DateTimeUtils.formatDateToDdYY(date);
                Paragraph textBulanTahunBold = new Paragraph(formattedDate).setTextAlignment(TextAlignment.RIGHT).setFontSize(50).setMargins(-75
                        , 0, 0, 0).setFont(timesNewRoman).setBold();

//                Paragraph namaFisik = new Paragraph("Fisik\t: " + fisik).setTextAlignment(TextAlignment.LEFT).setFontSize(11).setMargins(0, 0, 0, 7).setFont(timesNewRoman);
                Paragraph namaMesin = new Paragraph("Mesin\t : " + mesinSusun).setTextAlignment(TextAlignment.LEFT).setFontSize(11).setMargins(0, 0, 0, 7).setFont(timesNewRoman);
                Paragraph textHeader = new Paragraph("LABEL BJ").setUnderline().setTextAlignment(TextAlignment.LEFT).setFontSize(14).setMargins(0, 0, 0, 7).setFont(timesNewRomanBold);
                Paragraph textHeaderNomor = new Paragraph("NO : " + noBJ).setUnderline().setTextAlignment(TextAlignment.LEFT).setFontSize(14).setMargins(-21, 0, 0, 148).setFont(timesNewRomanBold);

                Paragraph afkirText = new Paragraph("Reject").setTextAlignment(TextAlignment.RIGHT).setFontSize(14).setMargins(-20, 75, 0, 0).setFont(timesNewRoman);
                Paragraph lemburTextOutput = new Paragraph("Lembur").setTextAlignment(TextAlignment.RIGHT).setFontSize(14).setMargins(-20, 0, 0, 0).setFont(timesNewRoman);
                Paragraph remarkText = new Paragraph("Remark : " + remark).setTextAlignment(TextAlignment.CENTER).setFontSize(12).setMargins(0, 0, 0, 0).setFont(timesNewRoman);

                // Tambahkan semua elemen ke dokumen
                document.add(textHeader);
                document.add(textHeaderNomor);

//                document.add(judul);
                if (printCount > 0) {
                    addTextDitheringWatermark(pdfDocument, timesNewRoman);
                }

                document.add(mainTable);
//                document.add(namaFisik);
                document.add(namaMesin);
                document.add(table);
                document.add(sumTable);
                document.add(qrCodeBottomImage);
                document.add(qrCodeIDbottom);
                document.add(textBulanTahunBold);

                if(CBAfkir.isChecked()){
                    document.add(afkirText);
                }

                if(CBLembur.isChecked()){
                    document.add(lemburTextOutput);
                }

                if (!remark.isEmpty() && !remark.equals("-")) {
                    document.add(remarkText);
                }

                document.close();
                pdfUri = uri;

                Toast.makeText(this, "PDF berhasil dibuat di " + uri.getPath(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException("Gagal membuat PDF: " + e.getMessage(), e);
            } finally {
                outputStream.close();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IOException("Proses pembuatan PDF terganggu", e);
        }

        return pdfUri;
    }
    
    private void deleteExistingPdf(String fileName, String relativePath) {
        Uri uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns._ID};
        String selection = MediaStore.MediaColumns.DISPLAY_NAME + "=? AND " + MediaStore.MediaColumns.RELATIVE_PATH + "=?";
        String[] selectionArgs = {fileName, relativePath};

        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
            Uri existingUri = ContentUris.withAppendedId(uri, id);
            getContentResolver().delete(existingUri, null, null);
            Log.d("Delete PDF", "Old PDF deleted: " + existingUri.toString());
        } else {
            Log.d("Delete PDF", "No existing PDF found");
        }

        if (cursor != null) {
            cursor.close();
        }

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        if (file.exists()) {
            if (file.delete()) {
                Log.d("Delete PDF", "File deleted from file system: " + file.getPath());
            } else {
                Log.d("Delete PDF", "Failed to delete file from file system: " + file.getPath());
            }
        } else {
            Log.d("Delete PDF", "File not found in file system: " + file.getPath());
        }
    }

    private String getIdJenisKayu(String namaJenisKayu) {
        if (namaJenisKayu != null) {
            return "IdJenisKayu";
        }
        return null;
    }

    private class SaveBongkarSusunTask extends AsyncTask<Void, Void, Boolean> {
        private String noBongkarSusun;
        private String noBarangJadi;

        public SaveBongkarSusunTask(String noBongkarSusun, String noBarangJadi) {
            this.noBongkarSusun = noBongkarSusun;
            this.noBarangJadi = noBarangJadi;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.BongkarSusunOutputBarangJadi (NoBJ, NoBongkarSusun) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarangJadi);
                    ps.setString(2, noBongkarSusun);
                    ps.executeUpdate();
                    ps.close();
                    con.close();
                    return true;
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                    return false;
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            loadOutputByMesinSusun(noBongkarSusun, false);

        }
    }

    private class SaveToDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private String noProduksi, noBarangJadi;

        public SaveToDatabaseTask(String noProduksi, String noBarangJadi) {
            this.noProduksi = noProduksi;
            this.noBarangJadi = noBarangJadi;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.PackingProduksiOutput (NoProduksi, NoBJ) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noProduksi);
                    ps.setString(2, noBarangJadi);

                    int rowsInserted = ps.executeUpdate();
                    ps.close();
                    con.close();
                    return rowsInserted > 0;
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                    return false;
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            loadOutputByMesinSusun(noProduksi, true);

        }
    }

    private class UpdateDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private String noBarangJadi, dateCreate, time, idTelly, noSPK, noSPKasal, idJenisKayu, idFJProfile, idBarangJadi, remark;
        private int isReject, isLembur;

        public UpdateDatabaseTask(String noBarangJadi, String dateCreate, String time,
                                  String idTelly, String noSPK, String noSPKasal,
                                  String idJenisKayu, String idFJProfile,
                                  int isReject, int isLembur, String idBarangJadi, String remark) {
            this.noBarangJadi = noBarangJadi;
            this.dateCreate = dateCreate;
            this.time = time;
            this.idTelly = idTelly;
            this.noSPK = noSPK;
            this.noSPKasal = noSPKasal;
            this.idJenisKayu = idJenisKayu;
            this.idFJProfile = idFJProfile;
            this.isReject = isReject;
            this.isLembur = isLembur;
            this.idBarangJadi = idBarangJadi;
            this.remark = remark;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {

                    String query = "INSERT INTO dbo.BarangJadi_h (NoBJ, DateCreate, Jam, IdOrgTelly, NoSPK, NoSPKAsal, IdFJProfile, IdJenisKayu, " +
                            "IdWarehouse, IsReject, IsLembur, IdBarangJadi, Remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    PreparedStatement ps = con.prepareStatement(query);

                    // Set nilai parameter dengan benar dan lengkap
                    ps.setString(1, noBarangJadi); // NoBJ sebagai primary key di index pertama
                    ps.setString(2, dateCreate);
                    ps.setString(3, time);
                    ps.setString(4, idTelly);
                    ps.setString(5, noSPK);
                    ps.setString(6, noSPKasal);
                    ps.setString(7, idFJProfile);
                    ps.setString(8, idJenisKayu);
                    ps.setInt(9, 11); // Asumsi IdWarehouse diisi dengan 11
                    ps.setInt(10, isReject);
                    ps.setInt(11, isLembur);
                    ps.setString(12, idBarangJadi); // IdBarangJadi
                    ps.setString(13, remark);

                    int rowsUpdated = ps.executeUpdate();

                    Log.d("UpdateDatabase", "Rows updated: " + rowsUpdated);

                    ps.close();
                    con.close();

                    return rowsUpdated > 0;

                } catch (Exception e) {
                    Log.e("Database Error", "Error updating BarangJadi_h: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
//            if (success) {
//                Log.d("UpdateDatabase", "Successfully updated BarangJadi_h");
//                Toast.makeText(Packing.this, "Data header berhasil diupdate.", Toast.LENGTH_SHORT).show();
//            } else {
//                Log.e("UpdateDatabase", "Failed to update BarangJadi_h");
//                Toast.makeText(Packing.this, "Gagal mengupdate data header.", Toast.LENGTH_SHORT).show();
//            }
        }
    }


    //SET NOMOR LABEL TERAKHIR
    private void setAndSaveNewNumber(final CountDownLatch latch) {
        executorService.execute(() -> {
            String newNumber = BjApi.generateNewNumber();

            runOnUiThread(() -> {
                if (newNumber != null) {
                    noBarangJadi = newNumber;

                    NoBarangJadi.setText(newNumber);

                } else {
                    Log.e("Error", "Failed to set or save LMT.");
                    Toast.makeText(Packing.this, "Gagal mengatur atau menyimpan LMT.", Toast.LENGTH_SHORT).show();
                }
            });
            // kasih sinyal ke CountDownLatch
            latch.countDown();
        });
    }


    // LOAD DATA SPINNER

    // Versi baru dengan callback
    private void loadLokasiSpinner(String selectedIdLokasi, String selectedLokasiName, @Nullable Runnable onDone) {
        executorService.execute(() -> {
            // Ambil data lokasi dari DB
            List<LokasiData> lokasiList = MasterApi.getLokasiList();

            // Tambahkan item default di posisi pertama
            lokasiList.add(0, new LokasiData("PILIH", "PILIH LOKASI", true, ""));

            runOnUiThread(() -> {
                ArrayAdapter<LokasiData> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        lokasiList
                );
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinLokasi.setAdapter(adapter);

                // Set default selection
                if (selectedIdLokasi == null || selectedIdLokasi.equals("0")) {
                    spinLokasi.setSelection(0);
                } else {
                    for (int i = 0; i < lokasiList.size(); i++) {
                        if (lokasiList.get(i).getIdLokasi().equals(selectedIdLokasi)) {
                            spinLokasi.setSelection(i);
                            break;
                        }
                    }
                }

                // 🔑 Jalankan callback setelah spinner selesai diisi
                if (onDone != null) onDone.run();
            });
        });
    }

    // Overload versi lama tetap bisa dipanggil tanpa callback
    private void loadLokasiSpinner(String selectedIdLokasi, String selectedLokasiName) {
        loadLokasiSpinner(selectedIdLokasi, selectedLokasiName, null);
    }



    // Versi baru dengan callback
    private void loadSPKSpinner(String selectedNoSPK, @Nullable Runnable onDone) {
        executorService.execute(() -> {
            List<SpkData> spkList = MasterApi.getSPKList();
            spkList.add(0, new SpkData("PILIH")); // Tambahkan default item

            runOnUiThread(() -> {
                ArrayAdapter<SpkData> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        spkList
                );
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinSPK.setAdapter(adapter);

                // Set default selection
                if (selectedNoSPK == null || selectedNoSPK.isEmpty() || selectedNoSPK.equals("PILIH")) {
                    SpinSPK.setSelection(0);
                } else {
                    for (int i = 0; i < spkList.size(); i++) {
                        if (spkList.get(i).getNoSPK().equals(selectedNoSPK)) {
                            SpinSPK.setSelection(i);
                            break;
                        }
                    }
                }

                // 🔑 Jalankan callback setelah spinner selesai diisi
                if (onDone != null) onDone.run();
            });
        });
    }

    // Overload versi lama tetap bisa dipanggil tanpa callback
    private void loadSPKSpinner(String selectedNoSPK) {
        loadSPKSpinner(selectedNoSPK, null);
    }



    // Versi baru dengan callback
    private void loadSPKAsalSpinner(String selectedNoSPKAsal, @Nullable Runnable onDone) {
        executorService.execute(() -> {
            List<SpkData> spkAsalList = MasterApi.getSPKList();
            spkAsalList.add(0, new SpkData("PILIH"));

            runOnUiThread(() -> {
                ArrayAdapter<SpkData> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        spkAsalList
                );
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinSPKAsal.setAdapter(adapter);

                if (selectedNoSPKAsal == null || selectedNoSPKAsal.isEmpty() || selectedNoSPKAsal.equals("PILIH")) {
                    SpinSPKAsal.setSelection(0);
                } else {
                    for (int i = 0; i < spkAsalList.size(); i++) {
                        if (spkAsalList.get(i).getNoSPK().equals(selectedNoSPKAsal)) {
                            SpinSPKAsal.setSelection(i);
                            break;
                        }
                    }
                }

                // 🔑 Jalankan callback setelah spinner selesai diisi
                if (onDone != null) onDone.run();
            });
        });
    }

    // Overload versi lama tetap bisa dipanggil tanpa callback
    private void loadSPKAsalSpinner(String selectedNoSPKAsal) {
        loadSPKAsalSpinner(selectedNoSPKAsal, null);
    }


    // Versi baru dengan callback
    private void loadTellyByIdUsernameSpinner(String selectedIdTelly, @Nullable Runnable onDone) {
        executorService.execute(() -> {
            List<TellyData> tellyList = MasterApi.getTellyByIdUsername(idUsername);

            runOnUiThread(() -> {
                ArrayAdapter<TellyData> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        tellyList
                );
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinTelly.setAdapter(adapter);

                // Set default selection
                if (selectedIdTelly == null || selectedIdTelly.equals("0")) {
                    SpinTelly.setSelection(0);
                } else {
                    for (int i = 0; i < tellyList.size(); i++) {
                        if (tellyList.get(i).getIdOrgTelly().equals(selectedIdTelly)) {
                            SpinTelly.setSelection(i);
                            break;
                        }
                    }
                }

                // 🔑 Jalankan callback setelah spinner selesai diisi
                if (onDone != null) onDone.run();
            });
        });
    }

    // Overload versi lama tetap bisa dipanggil
    private void loadTellyByIdUsernameSpinner(String selectedIdTelly) {
        loadTellySpinner(selectedIdTelly, null);
    }


    // Versi baru dengan callback
    private void loadTellySpinner(String selectedIdTelly, @Nullable Runnable onDone) {
        executorService.execute(() -> {
            List<TellyData> tellyList = MasterApi.getTellyList();

            runOnUiThread(() -> {
                ArrayAdapter<TellyData> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        tellyList
                );
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinTelly.setAdapter(adapter);

                // Set default selection
                if (selectedIdTelly == null || selectedIdTelly.equals("0")) {
                    SpinTelly.setSelection(0);
                } else {
                    for (int i = 0; i < tellyList.size(); i++) {
                        if (tellyList.get(i).getIdOrgTelly().equals(selectedIdTelly)) {
                            SpinTelly.setSelection(i);
                            break;
                        }
                    }
                }

                // 🔑 Jalankan callback setelah spinner selesai diisi
                if (onDone != null) onDone.run();
            });
        });
    }

    // Overload versi lama tetap bisa dipanggil
    private void loadTellySpinner(String selectedIdTelly) {
        loadTellySpinner(selectedIdTelly, null);
    }


    // Versi baru dengan callback
    private void loadJenisKayuSpinner(int selectedIdJenisKayu, @Nullable Runnable onDone) {
        executorService.execute(() -> {
            List<MstJenisKayuData> jenisKayuList = MasterApi.getJenisKayuList();
            jenisKayuList.add(0, new MstJenisKayuData(0, "PILIH"));

            runOnUiThread(() -> {
                ArrayAdapter<MstJenisKayuData> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenisKayuList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinKayu.setAdapter(adapter);

                if (selectedIdJenisKayu == 0) {
                    SpinKayu.setSelection(0);
                } else {
                    for (int i = 0; i < jenisKayuList.size(); i++) {
                        if (jenisKayuList.get(i).getIdJenisKayu() == selectedIdJenisKayu) {
                            SpinKayu.setSelection(i);
                            break;
                        }
                    }
                }

                // 🔑 Jalankan callback setelah spinner selesai diisi
                if (onDone != null) onDone.run();
            });
        });
    }

    // Overload versi lama tetap bisa dipanggil tanpa callback
    private void loadJenisKayuSpinner(int selectedIdJenisKayu) {
        loadJenisKayuSpinner(selectedIdJenisKayu, null);
    }




    // Versi baru dengan callback
    private void loadProfileSpinner(String selectedIdFJProfile, @Nullable Runnable onDone) {
        executorService.execute(() -> {
            List<MstProfileData> profileList = MasterApi.getProfileList();

            runOnUiThread(() -> {
                if (profileList != null && !profileList.isEmpty()) {
                    // Tambah dummy di index 0
                    profileList.add(0, new MstProfileData("PILIH", ""));

                    ArrayAdapter<MstProfileData> adapter = new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            profileList
                    );
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    SpinProfile.setAdapter(adapter);

                    // default set ke "PILIH"
                    SpinProfile.setSelection(0);

                    // kalau ada selectedIdFJProfile, pilih berdasarkan id
                    if (selectedIdFJProfile != null && !selectedIdFJProfile.isEmpty()) {
                        for (int i = 0; i < profileList.size(); i++) {
                            if (profileList.get(i).getIdFJProfile().equals(selectedIdFJProfile)) {
                                SpinProfile.setSelection(i);
                                break;
                            }
                        }
                    }
                } else {
                    Log.e("Error", "Failed to load profile data.");
                }

                // 🔑 Jalankan callback setelah spinner selesai diisi
                if (onDone != null) onDone.run();
            });
        });
    }

    // Overload versi lama tetap bisa dipanggil
    private void loadProfileSpinner(String selectedIdFJProfile) {
        loadProfileSpinner(selectedIdFJProfile, null);
    }



    // Versi baru dengan callback
    private void loadBjSpinner(int selectedIdBarangJadi, @Nullable Runnable onDone) {
        executorService.execute(() -> {
            List<MstBjData> bjList = MasterApi.getBarangJadiList();

            runOnUiThread(() -> {
                if (bjList != null && !bjList.isEmpty()) {
                    bjList.add(0, new MstBjData(0, "PILIH"));

                    ArrayAdapter<MstBjData> adapter = new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            bjList
                    );
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    SpinBarangJadi.setAdapter(adapter);

                    // default set ke "PILIH"
                    SpinBarangJadi.setSelection(0);

                    // kalau id > 0, pilih
                    if (selectedIdBarangJadi > 0) {
                        for (int i = 0; i < bjList.size(); i++) {
                            if (bjList.get(i).getIdBarangJadi() == selectedIdBarangJadi) {
                                SpinBarangJadi.setSelection(i);
                                break;
                            }
                        }
                    }
                } else {
                    Log.e("Error", "Failed to load barang jadi data.");
                }

                if (onDone != null) onDone.run();
            });
        });
    }

    // Overload tanpa callback
    private void loadBjSpinner(int selectedIdBarangJadi) {
        loadBjSpinner(selectedIdBarangJadi, null);
    }


    // Versi baru dengan callback
    private void loadMesinSpinner(@Nullable Runnable onDone, String... params) {
        executorService.execute(() -> {
            // Ambil tanggal dari parameter atau dari EditText Date
            String selectedDate;
            if (params != null && params.length > 0) {
                selectedDate = params[0];
            } else {
                selectedDate = Date.getText().toString();
            }

            // Ambil data mesin dari MasterApi
            List<MstMesinData> mesinList = BjApi.getMesinList(selectedDate);

            runOnUiThread(() -> {
                if (!mesinList.isEmpty()) {
                    ArrayAdapter<MstMesinData> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, mesinList);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    SpinMesin.setAdapter(adapter);
                } else {
                    Log.e("Error", "Failed to load mesin data.");
                    SpinMesin.setAdapter(null);
                    TabelOutput.removeAllViews();
                }

                // 🔑 Jalankan callback setelah spinner selesai diisi
                if (onDone != null) onDone.run();
            });
        });
    }

    // Overload versi lama tetap bisa dipanggil
    private void loadMesinSpinner(String... params) {
        loadMesinSpinner(null, params);
    }



    // Versi baru dengan callback
    private void loadSusunSpinner(@Nullable Runnable onDone, String... params) {
        executorService.execute(() -> {
            String selectedDate;
            if (params != null && params.length > 0) {
                selectedDate = params[0];
            } else {
                selectedDate = Date.getText().toString();
            }

            // Ambil data susun dari MasterApi
            List<MstSusunData> susunList = MasterApi.getSusunList(selectedDate);

            runOnUiThread(() -> {
                if (!susunList.isEmpty()) {
                    ArrayAdapter<MstSusunData> adapter = new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            susunList
                    );
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    SpinSusun.setAdapter(adapter);
                } else {
                    Log.e("Error", "Failed to load susun data.");
                    SpinSusun.setAdapter(null);
                    TabelOutput.removeAllViews();
                }

                // 🔑 Jalankan callback setelah spinner selesai diisi
                if (onDone != null) onDone.run();
            });
        });
    }

    // Overload versi lama tetap bisa dipanggil
    private void loadSusunSpinner(String... params) {
        loadSusunSpinner(null, params);
    }



    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown(); // Tutup supaya tidak ada memory leak
    }



    //Koneksi Database
    @SuppressLint("NewApi")
    private Connection ConnectionClass() {
        Connection con = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
        } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        }
        return con;
    }
}

