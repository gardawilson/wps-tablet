package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import androidx.appcompat.app.AlertDialog;

import android.app.ProgressDialog;
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
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
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

import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.utils.DateTimeUtils;

import android.app.TimePickerDialog;
import android.widget.TimePicker;
import android.widget.AutoCompleteTextView;
import android.view.inputmethod.InputMethodManager;
import android.os.Handler;
import android.os.Looper;


import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;


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
import java.text.ParseException;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;

import java.util.concurrent.CountDownLatch;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Sanding extends AppCompatActivity {

    private String username;
    private String noSanding;
    private SearchView NoSanding;
    private EditText DateS;
    private EditText TimeS;
    private EditText NoMouldingAsal;
    private Spinner SpinKayuS;
    private Spinner SpinTellyS;
    private Spinner SpinSPKS;
    private Spinner SpinSPKAsalS;
    private Spinner SpinProfileS;
    private Spinner SpinFisikS;
    private Spinner SpinGradeS;
    private Spinner SpinMesinS;
    private Spinner SpinSusunS;
    private Calendar calendarS;
    private RadioGroup RadioGroupS;
    private RadioButton radioButtonMesinS;
    private RadioButton radioButtonBSusunS;
    private Button BtnDataBaruS;
    private Button BtnSimpanS;
    private Button BtnBatalS;
    private Button BtnHapusDetailS;
    private boolean isDataBaruClickedS = false;
    private CheckBox CBAfkirS;
    private CheckBox CBLemburS;
    private Button BtnInputDetailS;
    private AutoCompleteTextView DetailLebarS;
    private AutoCompleteTextView DetailTebalS;
    private AutoCompleteTextView DetailPanjangS;
    private EditText DetailPcsS;
    private static int currentNumber = 1;
    private Button BtnPrintS;
    private TextView M3S;
    private TextView JumlahPcsS;
    private boolean isCBAfkirS, isCBLemburS;
    private Button BtnSearchS;
    private int rowCount = 0;
    private TableLayout Tabel;
    boolean isCreateMode = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    private EditText NoSanding_display;
    private String rawDate;
    private TableLayout TabelOutput;
    private TextView tvLabelCount;
    private EditText remarkLabel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sanding);

        NoMouldingAsal = findViewById(R.id.NoMouldingAsal);
        NoSanding = findViewById(R.id.NoSanding);
        DateS = findViewById(R.id.DateS);
        TimeS = findViewById(R.id.TimeS);
        SpinKayuS = findViewById(R.id.SpinKayuS);
        SpinTellyS = findViewById(R.id.SpinTellyS);
        SpinSPKS = findViewById(R.id.SpinSPKS);
        SpinSPKAsalS = findViewById(R.id.SpinSPKAsalS);
        SpinProfileS = findViewById(R.id.SpinProfileS);
        SpinFisikS = findViewById(R.id.SpinFisikS);
        SpinGradeS = findViewById(R.id.SpinGradeS);
        calendarS = Calendar.getInstance();
        SpinMesinS = findViewById(R.id.SpinMesinS);
        SpinSusunS = findViewById(R.id.SpinSusunS);
        radioButtonMesinS = findViewById(R.id.radioButtonMesinS);
        radioButtonBSusunS = findViewById(R.id.radioButtonBSusunS);
        BtnDataBaruS = findViewById(R.id.BtnDataBaruS);
        BtnSimpanS = findViewById(R.id.BtnSimpanS);
        BtnBatalS = findViewById(R.id.BtnBatalS);
        BtnHapusDetailS = findViewById(R.id.BtnHapusDetailS);
        CBLemburS = findViewById(R.id.CBLemburS);
        CBAfkirS = findViewById(R.id.CBAfkirS);
        BtnInputDetailS = findViewById(R.id.BtnInputDetailS);
        DetailPcsS = findViewById(R.id.DetailPcsS);
        DetailTebalS = findViewById(R.id.DetailTebalS);
        DetailPanjangS = findViewById(R.id.DetailPanjangS);
        DetailLebarS = findViewById(R.id.DetailLebarS);
        BtnPrintS = findViewById(R.id.BtnPrintS);
        M3S = findViewById(R.id.M3S);
        JumlahPcsS = findViewById(R.id.JumlahPcsS);
        Tabel = findViewById(R.id.Tabel);
        RadioGroupS = findViewById(R.id.RadioGroupS);
        NoSanding_display = findViewById(R.id.NoSanding_display);
        TabelOutput = findViewById(R.id.TabelOutput);
        tvLabelCount = findViewById(R.id.labelCount);
        remarkLabel = findViewById(R.id.remarkLabel);


        // Menangani aksi 'Enter' pada keyboard
        DetailTebalS.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Jika tombol 'Enter' ditekan, pindahkan fokus ke DetailLebarSanding
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Pastikan DetailLebarSanding bisa menerima fokus
                    DetailLebarS.requestFocus();
                    return true; // Menunjukkan bahwa aksi sudah ditangani
                }
                return false;
            }
        });

        DetailLebarS.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    DetailPanjangS.requestFocus();
                    return true;
                }
                return false;
            }
        });

        DetailPanjangS.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    DetailPcsS.requestFocus();
                    return true;
                }
                return false;
            }
        });

        DetailPcsS.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {  // Mengubah ke IME_ACTION_DONE
                    // Ambil input dari AutoCompleteTextView
                    String noSanding = NoSanding.getQuery().toString();
                    String tebal = DetailTebalS.getText().toString().trim();
                    String lebar = DetailLebarS.getText().toString().trim();
                    String panjang = DetailPanjangS.getText().toString().trim();

                    // Ambil data SPK, Jenis Kayu, dan Grade dari Spinner
                    SPK selectedSPK = (SPK) SpinSPKS.getSelectedItem();
                    Grade selectedGrade = (Grade) SpinGradeS.getSelectedItem();
                    JenisKayu selectedJenisKayu = (JenisKayu) SpinKayuS.getSelectedItem();

                    String idGrade = selectedGrade != null ? selectedGrade.getIdGrade() : null;
                    String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
                    String idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;

                    // Validasi input kosong
                    if (tebal.isEmpty() || lebar.isEmpty() || panjang.isEmpty()) {
                        Toast.makeText(Sanding.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    // Jalankan validasi
                    new CheckSPKDataTask(noSPK, tebal, lebar, panjang, idJenisKayu, idGrade) {
                        @Override
                        protected void onPostExecute(String result) {
                            super.onPostExecute(result);

                            if (result.equals("SUCCESS")) {
                                // Jika validasi berhasil, tambahkan data ke daftar
                                addDataDetail(noSanding);
                                jumlahpcs();
                                m3();
//                                Toast.makeText(Sanding.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();

                                // Sembunyikan keyboard setelah selesai
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            } else {
                                // Tampilkan pesan error
                                Toast.makeText(Sanding.this, result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute();

                    return true;
                }
                return false;
            }
        });

        NoSanding_display.setVisibility(View.GONE);
        disableForm();

        int searchEditTextId = NoSanding.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = NoSanding.findViewById(searchEditTextId);

        if (searchEditText != null) {
            searchEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        NoSanding.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!isCreateMode) {
                    loadSubmittedData(query);
                    closeKeyboard();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!isCreateMode) {
                    if (!newText.startsWith("W.")) {
                        NoSanding.setQuery("W." + newText, false);  // false untuk mencegah pemanggilan ulang listener
                    }

                    if(!newText.isEmpty()){
                        disableForm();
                        loadSubmittedData(newText);
                        BtnPrintS.setEnabled(true);

                    }
                    else{
                        enableForm();
                    }
                }
                return true;
            }
        });

        SpinMesinS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (radioButtonMesinS.isChecked()) {
                    Object selectedItem = parent.getItemAtPosition(position);
                    if (selectedItem instanceof Mesin) {
                        Mesin selectedMesin = (Mesin) selectedItem;
                        String noProduksi = selectedMesin.getNoProduksi();
                        loadOutputByMesinSusun(noProduksi, true);
                    } else {
                        Log.e("Error", "Item bukan tipe Mesin");
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

        SpinSusunS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (radioButtonBSusunS.isChecked()) {
                    Object selectedItem = parent.getItemAtPosition(position);
                    if (selectedItem instanceof Susun) {
                        Susun selectedSusun = (Susun) selectedItem;
                        String noBongkarSusun = selectedSusun.getNoBongkarSusun();
                        loadOutputByMesinSusun(noBongkarSusun, false);
                    } else {
                        Log.e("Error", "Item bukan tipe Susun");
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

        radioButtonMesinS.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SpinMesinS.setEnabled(true);
                SpinSusunS.setEnabled(false);

                Mesin selectedMesin = (Mesin) SpinMesinS.getSelectedItem();
                if (selectedMesin != null) {
                    String noProduksi = selectedMesin.getNoProduksi();
                    loadOutputByMesinSusun(noProduksi, true);
                }
            } else if (radioButtonBSusunS.isChecked()) {
                TabelOutput.removeAllViews();
                tvLabelCount.setText("Total Label : 0");
            }
        });

        radioButtonBSusunS.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SpinMesinS.setEnabled(false);
                SpinSusunS.setEnabled(true);

                Susun selectedSusun = (Susun) SpinSusunS.getSelectedItem();
                if (selectedSusun != null) {
                    String noBongkarSusun = selectedSusun.getNoBongkarSusun();
                    loadOutputByMesinSusun(noBongkarSusun, false);
                }
            } else if (radioButtonMesinS.isChecked()) {
                TabelOutput.removeAllViews();
                tvLabelCount.setText("Total Label : 0");
            }
        });
        setCurrentDateTime();

        BtnDataBaruS.setOnClickListener(v -> {
            // Tampilkan Dialog Loading
            AlertDialog.Builder builder = new AlertDialog.Builder(Sanding.this);
            builder.setCancelable(false); // Tidak bisa ditutup oleh pengguna
            builder.setView(R.layout.progress_dialog); // Layout custom dengan ProgressBar
            AlertDialog loadingDialog = builder.create();
            loadingDialog.show();

            // Timeout jika jaringan lambat
            Handler handler = new Handler(Looper.getMainLooper());
            boolean[] isTimeout = {false};
            handler.postDelayed(() -> {
                isTimeout[0] = true;
                runOnUiThread(() -> {
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                        Toast.makeText(Sanding.this, "Koneksi terlalu lambat. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                    }
                });
            }, 20000); // Timeout 20 detik

            new Thread(() -> {
                try {
                    // Set mode input dan waktu saat ini
                    setCurrentDateTime();
                    setCreateMode(true);

                    // Jalankan tugas asinkron untuk memuat data
                    runOnUiThread(() -> {
//                        new SetAndSaveNoSandingTask().execute();
                        new LoadJenisKayuTask().execute();
                        new LoadTellyTask().execute();
                        new LoadSPKTask().execute();
                        new LoadSPKAsalTask().execute();
                        new LoadProfileTask().execute();
                        new LoadFisikTask().execute();
                        new LoadGradeTask().execute();
                        new LoadMesinTask().execute(rawDate);
                        new LoadSusunTask().execute(rawDate);

                        BtnSimpanS.setEnabled(true);
                        BtnBatalS.setEnabled(true);
                        BtnPrintS.setEnabled(false);
                        BtnDataBaruS.setEnabled(false);
                        BtnDataBaruS.setVisibility(View.GONE);
                        BtnSimpanS.setVisibility(View.VISIBLE);
                        NoSanding.setVisibility(View.GONE);
                        NoSanding_display.setVisibility(View.VISIBLE);
                        NoSanding_display.setEnabled(false);

                        clearData();
                        resetDetailData();
                        enableForm();

                        if (loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        if (loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                        }
                        Toast.makeText(Sanding.this, "Kesalahan saat memuat data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                    Log.e("BtnDataBaruS", "Kesalahan: " + e.getMessage(), e);
                }
            }).start();
        });

        BtnSimpanS.setOnClickListener(v -> {

            String dateCreate = rawDate;
            String time = TimeS.getText().toString();
            String remark = remarkLabel.getText().toString();

            Telly selectedTelly = (Telly) SpinTellyS.getSelectedItem();
            SPK selectedSPK = (SPK) SpinSPKS.getSelectedItem();
            SPKAsal selectedSPKAsal = (SPKAsal) SpinSPKAsalS.getSelectedItem();
            Profile selectedProfile = (Profile) SpinProfileS.getSelectedItem();
            Fisik selectedFisik = (Fisik) SpinFisikS.getSelectedItem();
            Grade selectedGrade = (Grade) SpinGradeS.getSelectedItem();
            JenisKayu selectedJenisKayu = (JenisKayu) SpinKayuS.getSelectedItem();
            Mesin selectedMesin = (Mesin) SpinMesinS.getSelectedItem();
            Susun selectedSusun = (Susun) SpinSusunS.getSelectedItem();
            RadioGroup radioGroupUOMTblLebar = findViewById(R.id.radioGroupUOMTblLebar);
            RadioGroup radioGroupUOMPanjang = findViewById(R.id.radioGroupUOMPanjang);

            String idGrade = selectedGrade != null ? selectedGrade.getIdGrade() : null;
            String idTelly = selectedTelly != null ? selectedTelly.getIdTelly() : null;
            String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
            String noSPKasal = selectedSPKAsal != null ? selectedSPKAsal.getNoSPKAsal() : null;
            String idProfile = selectedProfile != null ? selectedProfile.getIdFJProfile() : null;
            String idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;
            String noProduksi = selectedMesin != null ? selectedMesin.getNoProduksi() : null;
            String noBongkarSusun = selectedSusun != null ? selectedSusun.getNoBongkarSusun() : null;

            if (!isInternetAvailable()) {
                Toast.makeText(Sanding.this, "Tidak ada koneksi internet. Coba lagi nanti.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dateCreate.isEmpty() || time.isEmpty() ||
                    selectedTelly == null || selectedTelly.getIdTelly().isEmpty() ||
                    selectedSPK == null || selectedSPK.getNoSPK().equals("PILIH") ||
                    selectedSPKAsal == null || selectedSPKAsal.getNoSPKAsal().equals("PILIH") ||
                    selectedFisik == null ||
                    selectedGrade == null || selectedGrade.getIdGrade().isEmpty() ||
                    selectedJenisKayu == null || selectedJenisKayu.getIdJenisKayu().isEmpty() ||
                    (!radioButtonMesinS.isChecked() && !radioButtonBSusunS.isChecked()) ||
                    (radioButtonMesinS.isChecked() && (selectedMesin == null || selectedMesin.getNoProduksi().isEmpty())) ||
                    (radioButtonBSusunS.isChecked() && (selectedSusun == null || selectedSusun.getNoBongkarSusun().isEmpty())) ||
                    temporaryDataListDetail.isEmpty()) {

                Toast.makeText(Sanding.this, "Pastikan semua field terisi dengan benar.", Toast.LENGTH_SHORT).show();
                return;
            }

                    CountDownLatch latch = new CountDownLatch(1);
                    setAndSaveNoSanding(latch);
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (latch.getCount() == 0) {

                        ProgressDialog progressDialog = new ProgressDialog(Sanding.this);
                        progressDialog.setMessage("Menyimpan data...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        Completable.create(emitter -> {
                                    checkMaxPeriod(dateCreate, (canProceed, message) -> {
                                        if (!canProceed) {
                                            emitter.onError(new Exception(message));
                                        } else {
                                            try {
                                                int isReject = CBAfkirS.isChecked() ? 1 : 0;
                                                int isLembur = CBLemburS.isChecked() ? 1 : 0;
                                                int idUOMTblLebar = radioGroupUOMTblLebar.getCheckedRadioButtonId() == R.id.radioMillimeter ? 1 : 4;
                                                int idUOMPanjang;
                                                if (radioGroupUOMPanjang.getCheckedRadioButtonId() == R.id.radioCentimeter) {
                                                    idUOMPanjang = 1;
                                                } else if (radioGroupUOMPanjang.getCheckedRadioButtonId() == R.id.radioMeter) {
                                                    idUOMPanjang = 2;
                                                } else {
                                                    idUOMPanjang = 3;
                                                }

                                                new UpdateDatabaseTask(
                                                        noSanding, dateCreate, time, idTelly, noSPK, noSPKasal, idGrade,
                                                        idJenisKayu, idProfile, isReject, isLembur, idUOMTblLebar, idUOMPanjang, remark
                                                ).execute();

                                                if (radioButtonMesinS.isChecked() && SpinMesinS.isEnabled() && noProduksi != null) {
                                                    new SaveToDatabaseTask(noProduksi, noSanding).execute();
                                                    for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                                                        Sanding.DataRow dataRow = temporaryDataListDetail.get(i);
                                                        saveDataDetailToDatabase(noSanding, i + 1,
                                                                Double.parseDouble(dataRow.tebal),
                                                                Double.parseDouble(dataRow.lebar),
                                                                Double.parseDouble(dataRow.panjang),
                                                                Integer.parseInt(dataRow.pcs));
                                                    }
                                                } else if (radioButtonBSusunS.isChecked() && SpinSusunS.isEnabled() && noBongkarSusun != null) {
                                                    new SaveBongkarSusunTask(noBongkarSusun, noSanding).execute();
                                                    for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                                                        Sanding.DataRow dataRow = temporaryDataListDetail.get(i);
                                                        saveDataDetailToDatabase(noSanding, i + 1,
                                                                Double.parseDouble(dataRow.tebal),
                                                                Double.parseDouble(dataRow.lebar),
                                                                Double.parseDouble(dataRow.panjang),
                                                                Integer.parseInt(dataRow.pcs));
                                                    }
                                                }

                                                SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                                                String username = prefs.getString("username", "");
                                                String capitalizedUsername = capitalizeFirstLetter(username);
                                                String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                                                String activity = String.format("Menyimpan Data %s Pada Label Sanding (Mobile)", noSanding);
                                                new SaveToRiwayatTask(capitalizedUsername, currentDateTime, activity).execute();

                                                emitter.onComplete();
                                            } catch (Exception e) {
                                                emitter.onError(e);
                                            }
                                        }
                                    });
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    progressDialog.dismiss();
                                    noSanding = null;
                                    BtnDataBaruS.setEnabled(true);
                                    BtnPrintS.setEnabled(true);
                                    BtnSimpanS.setEnabled(false);
                                    BtnDataBaruS.setVisibility(View.VISIBLE);
                                    BtnSimpanS.setVisibility(View.GONE);
                                    disableForm();
                                    Toast.makeText(Sanding.this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
                                }, throwable -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(Sanding.this, "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                                });
                    }
        });


        BtnBatalS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCreateMode(false);
                resetDetailData();
                resetAllForm();
                disableForm();


                BtnDataBaruS.setEnabled(true);
                BtnSimpanS.setEnabled(false);
                NoSanding.setVisibility(View.VISIBLE);
                NoSanding_display.setVisibility(View.GONE);
                BtnDataBaruS.setVisibility(View.VISIBLE);
                BtnSimpanS.setVisibility(View.GONE);
                CBLemburS.setChecked(false);
                CBAfkirS.setChecked(false);

            }
        });

        SpinKayuS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Hanya jalankan jika dalam mode create
                if (isCreateMode) {
                    JenisKayu selectedJenisKayu = (JenisKayu) parent.getItemAtPosition(position);
                    String idJenisKayu = selectedJenisKayu.getIdJenisKayu();
                    new LoadGradeTask().execute(idJenisKayu);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SpinSPKS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                                    Sanding.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    dimensionData.get("tebal")
                            );
                            ArrayAdapter<String> lebarAdapter = new ArrayAdapter<>(
                                    Sanding.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    dimensionData.get("lebar")
                            );
                            ArrayAdapter<String> panjangAdapter = new ArrayAdapter<>(
                                    Sanding.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    dimensionData.get("panjang")
                            );

                            // Set adapter untuk masing-masing AutoCompleteTextView
                            DetailTebalS.setAdapter(tebalAdapter);
                            DetailLebarS.setAdapter(lebarAdapter);
                            DetailPanjangS.setAdapter(panjangAdapter);

                            // Set threshold untuk semua AutoCompleteTextView
                            DetailTebalS.setThreshold(0);
                            DetailLebarS.setThreshold(0);
                            DetailPanjangS.setThreshold(0);

                            // Tampilkan status lock
                            if (isLocked) {
                                Toast.makeText(Sanding.this, "Dimensi Kunci Aktif", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                DetailTebalS.setText("");
                DetailPanjangS.setText("");
                DetailLebarS.setText("");
                DetailTebalS.setAdapter(null);
                DetailPanjangS.setAdapter(null);
                DetailLebarS.setAdapter(null);
            }
        });


        DateS.setOnClickListener(v -> showDatePickerDialog());

        TimeS.setOnClickListener(v -> showTimePickerDialog());

        BtnInputDetailS.setOnClickListener(v -> {
            // Ambil input dari AutoCompleteTextView
            String noSanding = NoSanding.getQuery().toString();
            String tebal = DetailTebalS.getText().toString().trim();
            String lebar = DetailLebarS.getText().toString().trim();
            String panjang = DetailPanjangS.getText().toString().trim();

            // Ambil data SPK, Jenis Kayu, dan Grade dari Spinner
            SPK selectedSPK = (SPK) SpinSPKS.getSelectedItem();
            Grade selectedGrade = (Grade) SpinGradeS.getSelectedItem();
            JenisKayu selectedJenisKayu = (JenisKayu) SpinKayuS.getSelectedItem();

            String idGrade = selectedGrade != null ? selectedGrade.getIdGrade() : null;
            String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
            String idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;

            // Validasi input kosong
            if (noSanding.isEmpty() || tebal.isEmpty() || lebar.isEmpty() || panjang.isEmpty()) {
                Toast.makeText(Sanding.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Jalankan validasi
            new CheckSPKDataTask(noSPK, tebal, lebar, panjang, idJenisKayu, idGrade) {
                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);

                    if (result.equals("SUCCESS")) {
                        // Jika validasi berhasil, tambahkan data ke daftar
                        addDataDetail(noSanding);
                        jumlahpcs();
                        m3();
//                        Toast.makeText(Sanding.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    } else {
                        // Tampilkan pesan error
                        Toast.makeText(Sanding.this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        });


        BtnHapusDetailS.setOnClickListener(v -> {
            resetDetailData();
        });

        BtnPrintS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validasi input
                if (NoSanding.getQuery() == null || NoSanding.getQuery().toString().trim().isEmpty()) {
                    Toast.makeText(Sanding.this, "Nomor Sanding tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validasi apakah ada data untuk dicetak
                if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
                    Toast.makeText(Sanding.this, "Tidak ada data untuk dicetak", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cek status HasBeenPrinted di database
                String noSanding = NoSanding.getQuery().toString().trim();
                checkHasBeenPrinted(noSanding, new Sanding.HasBeenPrintedCallback() {
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
                            String jenisKayu = SpinKayuS.getSelectedItem() != null ? SpinKayuS.getSelectedItem().toString().trim() : "";
                            String date = DateS.getText() != null ? DateS.getText().toString().trim() : "";
                            String time = TimeS.getText() != null ? TimeS.getText().toString().trim() : "";
                            String tellyBy = SpinTellyS.getSelectedItem() != null ? SpinTellyS.getSelectedItem().toString().trim() : "";
                            String noSPK = SpinSPKS.getSelectedItem() != null ? SpinSPKS.getSelectedItem().toString().trim() : "";
                            String noSPKasal = SpinSPKAsalS.getSelectedItem() != null ? SpinSPKAsalS.getSelectedItem().toString().trim() : "";
                            String grade = SpinGradeS.getSelectedItem() != null ? SpinGradeS.getSelectedItem().toString().trim() : "";
                            String fisik = SpinFisikS.getSelectedItem() != null ? SpinFisikS.getSelectedItem().toString().trim() : "";
                            String jumlahPcs = JumlahPcsS.getText() != null ? JumlahPcsS.getText().toString().trim() : "";
                            String m3 = M3S.getText() != null ? M3S.getText().toString().trim() : "";
                            String remark = remarkLabel.getText() != null ? remarkLabel.getText().toString().trim() : "";

                            if(radioButtonMesinS.isChecked()){
                                mesinSusun = SpinMesinS.getSelectedItem() != null ? SpinMesinS.getSelectedItem().toString().trim() : "";
                            }
                            else{
                                mesinSusun = SpinSusunS.getSelectedItem() != null ? SpinSusunS.getSelectedItem().toString().trim() : "";
                            }

                            // Buat PDF dengan parameter printCount
                            Uri pdfUri = createPdf(noSanding, jenisKayu, date, time, tellyBy, mesinSusun, noSPK, noSPKasal, grade,
                                    temporaryDataListDetail, jumlahPcs, m3, printCount, fisik, remark);

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
                                                updatePrintStatus(noSanding); // Update nilai HasBeenPrinted setelah print selesai
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
                                    Toast.makeText(Sanding.this,
                                            "Error printing: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error";
                            Toast.makeText(Sanding.this,
                                    "Terjadi kesalahan: " + errorMessage,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }


    //METHOD SANDING

    private void loadOutputByMesinSusun(String parameter, boolean isNoProduksi) {
        new Thread(() -> {
            Connection connection = null;
            try {
                connection = ConnectionClass();
                if (connection != null) {
                    String query;
                    if (isNoProduksi) {
                        query = "SELECT spo.NoSanding, h.HasBeenPrinted " +
                                "FROM dbo.SandingProduksiOutput spo " +
                                "JOIN dbo.Sanding_h h ON spo.NoSanding = h.NoSanding " +
                                "WHERE spo.NoProduksi = ?" +
                                "ORDER BY spo.NoSanding DESC";
                    } else {
                        query = "SELECT bs.NoSanding, h.HasBeenPrinted " +
                                "FROM dbo.BongkarSusunOutputSanding bs " +
                                "JOIN dbo.Sanding_h h ON bs.NoSanding = h.NoSanding " +
                                "WHERE bs.NoBongkarSusun = ?" +
                                "ORDER BY bs.NoBongkarSusun DESC";
                    }

                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, parameter);
                        try (ResultSet rs = stmt.executeQuery()) {
                            List<String> noSandingList = new ArrayList<>();
                            List<Integer> hasBeenPrintedList = new ArrayList<>();

                            while (rs.next()) {
                                noSandingList.add(rs.getString("NoSanding"));
                                hasBeenPrintedList.add(rs.getInt("HasBeenPrinted"));
                            }

                            runOnUiThread(() -> {
                                TabelOutput.removeAllViews();

                                int labelCount = 0;

                                if (!noSandingList.isEmpty() && noSandingList.size() == hasBeenPrintedList.size()) {
                                    for (int i = 0; i < noSandingList.size(); i++) {
                                        String noSanding = noSandingList.get(i);
                                        int hasBeenPrinted = hasBeenPrintedList.get(i);

                                        TableRow row = new TableRow(this);
                                        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(
                                                TableLayout.LayoutParams.MATCH_PARENT,
                                                TableLayout.LayoutParams.WRAP_CONTENT
                                        );

                                        // Tambahkan margin bawah untuk jarak antar baris
                                        row.setLayoutParams(rowParams);

                                        row.setPadding(0, 10, 0, 10);

                                        // Ubah warna latar belakang berdasarkan indeks
                                        if (i % 2 == 0) {
                                            row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream)); // Warna untuk baris genap
                                        } else {
                                            row.setBackgroundColor(ContextCompat.getColor(this, R.color.white)); // Warna untuk baris ganjil
                                        }

                                        TextView labelTextView = new TextView(this);
                                        labelTextView.setText(noSanding);
                                        labelTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                                        labelTextView.setGravity(Gravity.CENTER);
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
                                            iIcon.setImageResource(R.drawable.ic_undone); // Ganti dengan ikon untuk "-"
                                            oIcon.setImageResource(R.drawable.ic_undone); // Ganti dengan ikon untuk "-"
                                        } else if (hasBeenPrinted == 1) {
                                            iIcon.setImageResource(R.drawable.ic_done); // Ganti dengan ikon untuk "I"
                                            oIcon.setImageResource(R.drawable.ic_undone); // Ganti dengan ikon untuk "-"
                                        } else if (hasBeenPrinted == 2) {
                                            iIcon.setImageResource(R.drawable.ic_done); // Ganti dengan ikon untuk "I"
                                            oIcon.setImageResource(R.drawable.ic_done); // Ganti dengan ikon untuk "O"
                                        } else {
                                            iIcon.setImageResource(R.drawable.ic_done_all); // Ganti dengan ikon untuk "D"
                                            oIcon.setImageResource(R.drawable.ic_done_all); // Ganti dengan ikon untuk "D"
                                        }

                                        row.addView(iIcon);
                                        row.addView(oIcon);

                                        row.setOnClickListener(v -> {
                                            // Tampilkan tooltip ketika baris diklik
                                            fetchDataAndShowTooltip(v, noSanding);
                                        });


                                        TabelOutput.addView(row);
                                        labelCount++;

                                    }

                                    tvLabelCount.setText("Total Label : " + labelCount);

                                } else {
                                    //Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
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

    private void fetchDataAndShowTooltip(View anchorView, String noSanding) {
        new Thread(() -> {
            Connection connection = null;
            try {
                connection = ConnectionClass(); // Koneksi ke database
                if (connection != null) {
                    // Query utama untuk mengambil detail tooltip
                    String detailQuery = "SELECT h.NoSanding, h.DateCreate, h.Jam, k.Jenis, h.NoSPK, b1.Buyer AS BuyerNoSPK, " +
                            "h.NoSPKAsal, b2.Buyer AS BuyerNoSPKAsal, g.NamaGrade, h.IsLembur " +
                            "FROM Sanding_h h " +
                            "LEFT JOIN MstGrade g ON h.IdGrade = g.IdGrade " +
                            "LEFT JOIN MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                            "LEFT JOIN MstSPK_h s1 ON h.NoSPK = s1.NoSPK " +
                            "LEFT JOIN MstBuyer b1 ON s1.IdBuyer = b1.IdBuyer " +
                            "LEFT JOIN MstSPK_h s2 ON h.NoSPKAsal = s2.NoSPK " +
                            "LEFT JOIN MstBuyer b2 ON s2.IdBuyer = b2.IdBuyer " +
                            "WHERE h.NoSanding = ?";

                    PreparedStatement detailStmt = connection.prepareStatement(detailQuery);
                    detailStmt.setString(1, noSanding);
                    ResultSet detailRs = detailStmt.executeQuery();

                    String retrievedNoSanding = null;
                    String formattedDateTime = null;
                    String jenis = null;
                    String spkDetail = null;
                    String spkAsalDetail = null;
                    String namaGrade = null;
                    boolean isLembur = false;

                    if (detailRs.next()) {
                        retrievedNoSanding = detailRs.getString("NoSanding");
                        String dateCreate = detailRs.getString("DateCreate");
                        String jam = detailRs.getString("Jam");
                        jenis = detailRs.getString("Jenis");
                        String noSPK = detailRs.getString("NoSPK");
                        String buyerNoSPK = detailRs.getString("BuyerNoSPK");
                        String noSPKAsal = detailRs.getString("NoSPKAsal");
                        String buyerNoSPKAsal = detailRs.getString("BuyerNoSPKAsal");
                        namaGrade = detailRs.getString("NamaGrade");
                        isLembur = detailRs.getBoolean("IsLembur");

                        spkDetail = (noSPK != null && buyerNoSPK != null) ? noSPK + " - " + buyerNoSPK : "No data";
                        spkAsalDetail = (noSPKAsal != null && buyerNoSPKAsal != null) ? noSPKAsal + " - " + buyerNoSPKAsal : "No data";
                        formattedDateTime = combineDateTime(dateCreate, jam);
                    }

                    // Query untuk mengambil data tabel
                    String tableQuery = "SELECT Tebal, Lebar, Panjang, JmlhBatang FROM Sanding_d WHERE NoSanding = ? ORDER BY NoUrut";
                    PreparedStatement tableStmt = connection.prepareStatement(tableQuery);
                    tableStmt.setString(1, noSanding);

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
                    String finalRetrievedNoSanding = retrievedNoSanding;
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
                            finalRetrievedNoSanding,
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

    private void showTooltip(View anchorView, String noSanding, String formattedDateTime, String jenis, String spkDetail, String spkAsalDetail, String namaGrade, boolean isLembur, List<String[]> tableData, int totalPcs, double totalM3) {
        // Inflate layout tooltip
        View tooltipView = LayoutInflater.from(this).inflate(R.layout.tooltip_layout_right, null);

        // Set data pada TextView
        ((TextView) tooltipView.findViewById(R.id.tvNoLabel)).setText(noSanding);
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

    //Fungsi untuk membuat huruf kapital
    public String capitalizeFirstLetter(String inputUsername) {
        if (inputUsername == null || inputUsername.isEmpty()) {
            return inputUsername; // Jika null atau kosong, kembalikan string asli
        }
        return inputUsername.substring(0, 1).toUpperCase() + inputUsername.substring(1).toLowerCase();
    }

    private class SaveToRiwayatTask extends AsyncTask<Void, Void, Boolean> {
        private String username;
        private String currentDate;
        private String activity;

        public SaveToRiwayatTask(String username, String currentDate, String activity) {
            this.username = username;
            this.currentDate = currentDate;
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            boolean success = false;

            if (con != null) {
                try {
                    // Query untuk insert ke tabel Riwayat
                    String query = "INSERT INTO dbo.Riwayat (Nip, Tgl, Aktivitas) VALUES (?, ?, ?)";
                    Log.d("SQL Query", "Executing query: " + query);
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, username);
                    ps.setString(2, currentDate);
                    ps.setString(3, activity);

                    int rowsAffected = ps.executeUpdate();
                    Log.d("Database", "Rows affected: " + rowsAffected);

                    ps.close();
                    con.close();

                    success = rowsAffected > 0;
                    Log.d("Riwayat", "Data successfully inserted into Riwayat.");

                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // Update UI atau beri feedback ke pengguna setelah data disimpan
            if (success) {
                Log.d("Riwayat", "Data berhasil disimpan di Riwayat");
            } else {
                Log.e("Riwayat", "Gagal menyimpan data di Riwayat");
            }
        }
    }

    // Add this method to check max period
    private void checkMaxPeriod(String dateToCheck, OnPeriodCheckListener listener) {
        new AsyncTask<Void, Void, String[]>() {  // Ubah return type jadi String[] untuk menampung 2 period
            @Override
            protected String[] doInBackground(Void... voids) {
                String[] periods = new String[2];  // Array untuk menyimpan period dari 2 tabel
                Connection conn = null;

                try {
                    conn = ConnectionClass();

                    // Check MstTutupTransaksi
                    String query1 = "SELECT MAX(Period) as max_period FROM MstTutupTransaksi";
                    PreparedStatement stmt1 = conn.prepareStatement(query1);
                    ResultSet rs1 = stmt1.executeQuery();
                    if (rs1.next()) {
                        periods[0] = rs1.getString("max_period");
                    }

                    // Check MstTutupTransaksiHarian
                    String query2 = "SELECT MAX(PeriodHarian) as max_period FROM MstTutupTransaksiHarian";
                    PreparedStatement stmt2 = conn.prepareStatement(query2);
                    ResultSet rs2 = stmt2.executeQuery();
                    if (rs2.next()) {
                        periods[1] = rs2.getString("max_period");
                    }

                    return periods;

                } catch (SQLException e) {
                    e.printStackTrace();
                    return periods;
                } finally {
                    try {
                        if (conn != null) conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onPostExecute(String[] periods) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    Date inputDate = sdf.parse(dateToCheck);

                    // Check period dari MstTutupTransaksi
                    if (periods[0] != null) {
                        Date maxPeriodDate = sdf.parse(periods[0]);
                        if (inputDate.before(maxPeriodDate) || inputDate.equals(maxPeriodDate)) {
                            listener.onResult(false, "Periode Transaksi Bulanan Telah di Tutup!");
                            return;
                        }
                    }

                    // Check period dari MstTutupTransaksiHarian
                    if (periods[1] != null) {
                        Date maxPeriodHarianDate = sdf.parse(periods[1]);
                        if (inputDate.before(maxPeriodHarianDate) || inputDate.equals(maxPeriodHarianDate)) {
                            listener.onResult(false, "Periode Transaksi Harian Telah di Tutup!");
                            return;
                        }
                    }

                    // Jika lolos kedua pengecekan
                    listener.onResult(true, "");

                } catch (ParseException e) {
                    e.printStackTrace();
                    listener.onResult(false, "Error parsing date!");
                }
            }
        }.execute();
    }

    // Interface for callback
    interface OnPeriodCheckListener {
        void onResult(boolean canProceed, String message);
    }

    // Deklarasi CheckSPKDataTask di level class
    private class CheckSPKDataTask extends AsyncTask<Void, Void, String> {
        private final String noSPK;
        private final String tebal;
        private final String lebar;
        private final String panjang;
        private final String idJenisKayu;
        private final String idGrade;

        public CheckSPKDataTask(String noSPK, String tebal, String lebar, String panjang, String idJenisKayu, String idGrade) {
            this.noSPK = noSPK;
            this.tebal = tebal;
            this.lebar = lebar;
            this.panjang = panjang;
            this.idJenisKayu = idJenisKayu;
            this.idGrade = idGrade;
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
                String unlockQuery = "SELECT UnlockGradeSND FROM MstSPK_h WHERE NoSPK = ?";
                PreparedStatement unlockStmt = connection.prepareStatement(unlockQuery);
                unlockStmt.setString(1, noSPK);
                ResultSet unlockRs = unlockStmt.executeQuery();
                boolean isUnlockGrade = false;

                if (unlockRs.next()) {
                    isUnlockGrade = unlockRs.getInt("UnlockGradeSND") == 1;
                }

                // Query untuk validasi data
                String query = "SELECT * FROM MstSPK_dSanding WHERE NoSPK = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, noSPK);
                ResultSet rs = stmt.executeQuery();

                boolean matchFound = false;

                while (rs.next()) {
                    if (Double.parseDouble(tebal) == rs.getDouble("Tebal") &&
                            Double.parseDouble(lebar) == rs.getDouble("Lebar") &&
                            Double.parseDouble(panjang) == rs.getDouble("Panjang") &&
                            idJenisKayu.equals(rs.getString("IdJenisKayu"))) {

                        if (!isUnlockGrade && idGrade.equals(rs.getString("IdGrade"))) {
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
                    if (!columnMatches(connection, "IdJenisKayu", noSPK, idJenisKayu)) {
                        mismatchMessage.append("Jenis Kayu, ");
                    }
                    if (!isUnlockGrade && !columnMatches(connection, "IdGrade", noSPK, idGrade)) {
                        mismatchMessage.append("Grade, ");
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
        String query = "SELECT " + columnName + " FROM MstSPK_dSanding WHERE NoSPK = ?";
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
                String query = "SELECT DISTINCT Tebal, Lebar, Panjang FROM MstSPK_dSanding WHERE NoSPK = ?";
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
                String query = "SELECT LockDimensionSAND FROM MstSPK_h WHERE NoSPK = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, noSPK);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            Integer lockDimension = rs.getInt("LockDimensionSAND");
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
        DateS.setEnabled(true);
        TimeS.setEnabled(true);
        SpinKayuS.setEnabled(true);
        radioButtonMesinS.setEnabled(true);
        radioButtonBSusunS.setEnabled(true);
        SpinMesinS.setEnabled(true);
        SpinSusunS.setEnabled(true);
        SpinTellyS.setEnabled(true);
        SpinSPKS.setEnabled(true);
        SpinSPKAsalS.setEnabled(true);
        SpinGradeS.setEnabled(true);
        SpinProfileS.setEnabled(true);
        DetailTebalS.setEnabled(true);
        DetailLebarS.setEnabled(true);
        DetailPanjangS.setEnabled(true);
        DetailPcsS.setEnabled(true);
        BtnHapusDetailS.setEnabled(true);
        BtnInputDetailS.setEnabled(true);
        CBLemburS.setEnabled(true);
        CBAfkirS.setEnabled(true);
        remarkLabel.setEnabled(true);
    }

    private void disableForm(){
        DateS.setEnabled(false);
        TimeS.setEnabled(false);
        SpinKayuS.setEnabled(false);
        radioButtonMesinS.setEnabled(false);
        radioButtonBSusunS.setEnabled(false);
        SpinMesinS.setEnabled(false);
        SpinSusunS.setEnabled(false);
        SpinTellyS.setEnabled(false);
        SpinSPKS.setEnabled(false);
        SpinSPKAsalS.setEnabled(false);
        SpinGradeS.setEnabled(false);
        SpinProfileS.setEnabled(false);
        DetailTebalS.setEnabled(false);
        DetailLebarS.setEnabled(false);
        DetailPanjangS.setEnabled(false);
        DetailPcsS.setEnabled(false);
        BtnHapusDetailS.setEnabled(false);
        BtnInputDetailS.setEnabled(false);
        BtnSimpanS.setEnabled(false);
        CBLemburS.setEnabled(false);
        CBAfkirS.setEnabled(false);
        remarkLabel.setEnabled(false);
        SpinFisikS.setEnabled(false);


        // Disable semua tombol hapus yang ada di tabel
        for (int i = 0; i < Tabel.getChildCount(); i++) {
            View row = Tabel.getChildAt(i);
            if (row instanceof TableRow) {
                TableRow tableRow = (TableRow) row;
                for (int j = 0; j < tableRow.getChildCount(); j++) {
                    View view = tableRow.getChildAt(j);
                    if (view instanceof Button) {
                        view.setEnabled(false);
                    }
                }
            }
        }
    }

    private void resetAllForm() {
        DateS.setText("");
        TimeS.setText("");
        NoSanding.setQuery("",false);
        NoMouldingAsal.setText("");
        remarkLabel.setText("");

        setSpinnerValue(SpinKayuS, "-");
        setSpinnerValue(SpinTellyS, "-");
        setSpinnerValue(SpinSPKS, "-");
        setSpinnerValue(SpinSPKAsalS, "-");
        setSpinnerValue(SpinGradeS, "-");
        setSpinnerValue(SpinProfileS, "-");
        setSpinnerValue(SpinFisikS, "-");
        setSpinnerValue(SpinMesinS, "-");
        setSpinnerValue(SpinSusunS, "-");

        radioButtonBSusunS.setEnabled(false);
        radioButtonMesinS.setEnabled(false);

    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(NoSanding.getWindowToken(), 0);
        }
    }

    //SET false jika ingin search data
    private void setCreateMode(boolean isCreate) {
        this.isCreateMode = isCreate;
    }

    // Method untuk set single value ke spinner
    private void setSpinnerValue(Spinner spinner, String value) {
        if (spinner != null) {
            String displayValue = value != null ? value : "-";
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item,
                    Collections.singletonList(displayValue));
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }

    private void loadSubmittedData(String noSanding) {
        // Tampilkan progress dialog
        resetDetailData();
        new Thread(() -> {
            Connection connection = null;
            try {
                connection = ConnectionClass();
                if (connection != null) {
                    // Query untuk header
                    String queryHeader = "SELECT " +
                            "o.NoProduksi, " +
                            "h.DateCreate, " +
                            "h.Jam, " +
                            "h.IdOrgTelly, " +
                            "t.NamaOrgTelly, " +
                            "h.NoSPK, " +
                            "h.NoSPKAsal, " +
                            "h.IdGrade, " +
                            "g.NamaGrade, " +
                            "h.IdFJProfile, " +
                            "h.IdFisik, " +
                            "p.IdMesin, " +
                            "m.NamaMesin, " +
                            "s.NoBongkarSusun, " +
                            "f.Profile, " +
                            "w.NamaWarehouse, " +
                            "h.IdJenisKayu, " +
                            "k.Jenis, " +
                            "h.IsLembur, " +
                            "h.IsReject, " +
                            "h.Remark " +
                            "FROM Sanding_h h " +
                            "LEFT JOIN SandingProduksiOutput o ON h.NoSanding = o.NoSanding " +
                            "LEFT JOIN MstGrade g ON h.IdGrade = g.IdGrade " +
                            "LEFT JOIN SandingProduksi_h p ON o.NoProduksi = p.NoProduksi " +
                            "LEFT JOIN BongkarSusunOutputSanding s ON h.NoSanding = s.NoSanding " +
                            "LEFT JOIN MstMesin m ON p.IdMesin = m.IdMesin " +
                            "LEFT JOIN MstOrgTelly t ON h.IdOrgTelly = t.IdOrgTelly " +
                            "LEFT JOIN MstFJProfile f ON h.IdFJProfile = f.IdFJProfile " +
                            "LEFT JOIN MstWarehouse w ON h.IdFisik = w.IdWarehouse " +
                            "LEFT JOIN MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                            "WHERE h.NoSanding = ?";

                    // Query untuk detail
                    String queryDetail = "SELECT Tebal, Lebar, Panjang, JmlhBatang " +
                            "FROM Sanding_d " +
                            "WHERE NoSanding = ? " +
                            "ORDER BY NoUrut";

                    // Menggunakan try-with-resources untuk header
                    try (PreparedStatement stmt = connection.prepareStatement(queryHeader)) {
                        stmt.setString(1, noSanding);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                // Mengambil data header
                                final String noProduksi = rs.getString("NoProduksi") != null ? rs.getString("NoProduksi") : "-";
                                final String dateCreate = rs.getString("DateCreate") != null ? rs.getString("DateCreate") : "-";
                                final String jam = rs.getString("Jam") != null ? rs.getString("Jam") : "-";
                                final String namaOrgTelly = rs.getString("NamaOrgTelly") != null ? rs.getString("NamaOrgTelly") : "-";
                                final String noSPK = rs.getString("NoSPK") != null ? rs.getString("NoSPK") : "-";
                                final String noSPKAsal = rs.getString("NoSPKAsal") != null ? rs.getString("NoSPKAsal") : "-";
                                final String namaGrade = rs.getString("NamaGrade") != null ? rs.getString("NamaGrade") : "-";
                                final String namaMesin = rs.getString("NamaMesin") != null ? rs.getString("NamaMesin") : "-";
                                final String noBongkarSusun = rs.getString("NoBongkarSusun") != null ? rs.getString("NoBongkarSusun") : "-";
                                final String namaProfile = rs.getString("Profile") != null ? rs.getString("Profile") : "-";
                                final String namaWarehouse = rs.getString("NamaWarehouse") != null ? rs.getString("NamaWarehouse") : "-";
                                final String namaKayu = rs.getString("Jenis") != null ? rs.getString("Jenis") : "-";
                                final int isLembur = rs.getInt("IsLembur");
                                final int isReject = rs.getInt("IsReject");
                                final String remark = rs.getString("Remark") != null ? rs.getString("Remark") : "-";


                                // Mengambil data detail
                                try (PreparedStatement stmtDetail = connection.prepareStatement(queryDetail)) {
                                    stmtDetail.setString(1, noSanding);
                                    try (ResultSet rsDetail = stmtDetail.executeQuery()) {
                                        while (rsDetail.next()) {
                                            String tebal = rsDetail.getString("Tebal");
                                            String lebar = rsDetail.getString("Lebar");
                                            String panjang = rsDetail.getString("Panjang");
                                            String pcs = rsDetail.getString("JmlhBatang");

                                            // Buat objek DataRow baru dan tambahkan ke list
                                            DataRow newRow = new DataRow(tebal, lebar, panjang, pcs);
                                            temporaryDataListDetail.add(newRow);
                                        }
                                    }
                                }

                                // Update UI di thread utama
                                runOnUiThread(() -> {
                                    try {
                                        if (!namaMesin.equals("-")) {
                                            radioButtonMesinS.setChecked(true);
                                            radioButtonBSusunS.setEnabled(false);
                                        } else {
                                            radioButtonBSusunS.setChecked(true);
                                            radioButtonMesinS.setEnabled(false);
                                        }
                                        // Update header fields
                                        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                        Date date = inputDateFormat.parse(dateCreate);
                                        String formattedDate = outputDateFormat.format(date);

                                        DateS.setText(formattedDate);
                                        TimeS.setText(jam);
                                        setSpinnerValue(SpinTellyS, namaOrgTelly);
                                        setSpinnerValue(SpinSPKS, noSPK);
                                        setSpinnerValue(SpinSPKAsalS, noSPKAsal);
                                        setSpinnerValue(SpinKayuS, namaKayu);
                                        setSpinnerValue(SpinGradeS, namaGrade);
                                        setSpinnerValue(SpinProfileS, namaProfile);
                                        setSpinnerValue(SpinFisikS, namaWarehouse);
                                        setSpinnerValue(SpinMesinS, namaMesin + " - " + noProduksi);
                                        setSpinnerValue(SpinSusunS, noBongkarSusun);
                                        CBAfkirS.setChecked(isReject == 1);
                                        CBLemburS.setChecked(isLembur == 1);
                                        remarkLabel.setText(remark);

                                        // Update tabel detail
                                        updateTableFromTemporaryData();
                                        m3();
                                        jumlahpcs();

//                                        Toast.makeText(getApplicationContext(),
//                                                "Data berhasil dimuat",
//                                                Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {

                                        Log.e("UI Update Error", "Error updating UI: " + e.getMessage());
//                                        Toast.makeText(getApplicationContext(),
//                                                "Gagal memperbarui tampilan",
//                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(() -> {

//                                    Toast.makeText(getApplicationContext(),
//                                            "Data tidak ditemukan untuk NoSanding: " + noSanding,
//                                            Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                    }
                } else {
                    runOnUiThread(() -> {

                        Toast.makeText(getApplicationContext(),
                                "Koneksi database gagal",
                                Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (SQLException e) {
                final String errorMessage = e.getMessage();
                runOnUiThread(() -> {

                    Toast.makeText(getApplicationContext(),
                            "Error: " + errorMessage,
                            Toast.LENGTH_LONG).show();
                    Log.e("Database Error", "Error executing query: " + errorMessage);
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

    // Method baru untuk memperbarui tabel dari temporaryDataListDetail
    private void updateTableFromTemporaryData() {
        // Reset tabel terlebih dahulu (hapus semua baris kecuali header)

        // Perbarui rowCount
        rowCount = 0;

        // Tambahkan setiap data ke tabel
        DecimalFormat df = new DecimalFormat("#,###.##");

        for (DataRow data : temporaryDataListDetail) {
            TableRow newRow = new TableRow(this);
            newRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // Tambahkan kolom-kolom dengan format yang sama seperti addDataDetail
            addTextViewToRowWithWeight(newRow, String.valueOf(++rowCount), 0);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(data.tebal)), 0);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(data.lebar)), 0);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(data.panjang)), 0);
            addTextViewToRowWithWeight(newRow, df.format(Integer.parseInt(data.pcs)), 0);

            // Tambahkan tombol hapus
            Button deleteButton = new Button(this);
            deleteButton.setText("");
            deleteButton.setTextSize(12);

            TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            buttonParams.setMargins(5, 5, 5, 5);
            deleteButton.setLayoutParams(buttonParams);
            deleteButton.setPadding(10, 5, 10, 5);
            deleteButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            deleteButton.setTextColor(Color.BLACK);

            newRow.addView(deleteButton);
            Tabel.addView(newRow);
        }
    }

    interface HasBeenPrintedCallback {
        void onResult(int count);  // Callback menerima count
    }

    // Method untuk mengecek status HasBeenPrinted dengan penanganan NULL
    private void checkHasBeenPrinted(String noSanding, Sanding.HasBeenPrintedCallback callback) {
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

                    String queryCheckH = "SELECT HasBeenPrinted, DateUsage FROM Sanding_h WHERE NoSanding = ?";
                    String queryCheckD = "SELECT 1 FROM Sanding_d WHERE NoSanding = ?";

                    // Cek keberadaan di Sanding_h
                    try (PreparedStatement stmtH = connection.prepareStatement(queryCheckH)) {
                        stmtH.setString(1, noSanding);
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

                    // Cek keberadaan di Sanding_d
                    try (PreparedStatement stmtD = connection.prepareStatement(queryCheckD)) {
                        stmtD.setString(1, noSanding);
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
    private void updatePrintStatus(String noSanding) {
        new Thread(() -> {
            Connection connection = null;
            try {
                // Mendapatkan koneksi dari method ConnectionClass
                connection = ConnectionClass();
                if (connection != null) {
                    // Query untuk menambah 1 pada nilai HasBeenPrinted
                    String query = "UPDATE Sanding_h SET HasBeenPrinted = COALESCE(HasBeenPrinted, 0) + 1, LastPrintDate = GETDATE() WHERE NoSanding = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, noSanding);
                        int rowsAffected = stmt.executeUpdate();

                        if (rowsAffected > 0) {
                            runOnUiThread(() -> Toast.makeText(Sanding.this,
                                    "Status cetak berhasil diupdate",
                                    Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(Sanding.this,
                                    "Tidak ada data yang diupdate",
                                    Toast.LENGTH_SHORT).show());
                        }
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(Sanding.this,
                            "Koneksi database gagal",
                            Toast.LENGTH_SHORT).show());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("Database", "Error updating HasBeenPrinted status: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(Sanding.this,
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

    private static class DataRow {
        String tebal;
        String lebar;
        String panjang;
        String pcs;
        int rowId;
        private static int nextId = 1;

        DataRow(String tebal, String lebar, String panjang, String pcs) {
            this.tebal = tebal;
            this.lebar = lebar;
            this.panjang = panjang;
            this.pcs = pcs;
            this.rowId = nextId++;
        }
    }

    private List<DataRow> temporaryDataListDetail = new ArrayList<>();

    private void addDataDetail(String noSanding) {
        String tebal = DetailTebalS.getText().toString();
        String panjang = DetailPanjangS.getText().toString();
        String lebar = DetailLebarS.getText().toString();
        String pcs = DetailPcsS.getText().toString();

        if (tebal.isEmpty() || panjang.isEmpty() || lebar.isEmpty() || pcs.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cek duplikasi data
        boolean isDuplicate = false;
        for (DataRow existingData : temporaryDataListDetail) {
            if (existingData.tebal.equals(tebal) && existingData.panjang.equals(panjang) && existingData.lebar.equals(lebar)) {
                isDuplicate = true;
                break;
            }
        }

        if (isDuplicate) {
            Toast.makeText(this, "Data dengan ukuran yang sama sudah ada dalam tabel", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Buat objek DataRow baru
            DataRow newDataRow = new DataRow(tebal, lebar, panjang, pcs);
            temporaryDataListDetail.add(newDataRow);

            // Buat baris tabel baru
            TableRow newRow = new TableRow(this);
            newRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            DecimalFormat df = new DecimalFormat("#,###.##");

            // Tambahkan kolom-kolom data dengan weight
            addTextViewToRowWithWeight(newRow, String.valueOf(++rowCount), 0);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(tebal)), 0);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(lebar)), 0);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(panjang)), 0);
            addTextViewToRowWithWeight(newRow, String.valueOf(Integer.parseInt(pcs)), 0);

            // Buat dan tambahkan tombol hapus
            Button deleteButton = new Button(this);
            deleteButton.setText("Hapus");
            deleteButton.setTextSize(12);

            // Atur style tombol
            TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            buttonParams.setMargins(5, 5, 5, 5);
            deleteButton.setLayoutParams(buttonParams);
            deleteButton.setPadding(10, 5, 10, 5);
            deleteButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

            // Set listener tombol hapus
            deleteButton.setOnClickListener(v -> {
                Tabel.removeView(newRow);
                temporaryDataListDetail.remove(newDataRow);
                updateRowNumbers();
                jumlahpcs();
                m3();
            });

            newRow.addView(deleteButton);
            Tabel.addView(newRow);

            // Bersihkan field input
            DetailTebalS.setText("");
            DetailPanjangS.setText("");
            DetailLebarS.setText("");
            DetailPcsS.setText("");

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
        if (DetailTebalS != null) {
            DetailTebalS.setText("");
        }
        if (DetailLebarS != null) {
            DetailLebarS.setText("");
        }
        if (DetailPanjangS != null) {
            DetailPanjangS.setText("");
        }
        if (DetailPcsS != null) {
            DetailPcsS.setText("");
        }
    }

    private void saveDataDetailToDatabase(String noSanding, int noUrut, double tebal, double lebar, double panjang, int pcs) {
        new Sanding.SaveDataTaskDetail().execute(noSanding, String.valueOf(noUrut), String.valueOf(tebal), String.valueOf(lebar),
                String.valueOf(panjang), String.valueOf(pcs));
    }

    private class SaveDataTaskDetail extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String noSanding = params[0];
            String noUrut = params[1];
            String tebal = params[2];
            String lebar = params[3];
            String panjang = params[4];
            String pcs = params[5];

            try {
                Connection connection = ConnectionClass();
                if (connection != null) {
                    String query = "INSERT INTO dbo.Sanding_d (NoSanding, NoUrut, Tebal, Lebar, Panjang, JmlhBatang) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, noSanding);
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
            if (success) {
                Log.d("DB_INSERT", "Data Detail berhasil disimpan");
            } else {
                Log.e("DB_INSERT", "Data gagal disimpan");
            }
        }
    }

    private void clearData() {
        NoSanding.setQuery("", false);
        M3S.setText("");
        JumlahPcsS.setText("");
        NoMouldingAsal.setText("");
        CBAfkirS.setChecked(false);
        CBLemburS.setChecked(false);
        SpinKayuS.setSelection(0);
        SpinTellyS.setSelection(0);
        SpinSPKS.setSelection(0);
        SpinSPKAsalS.setSelection(0);
        SpinGradeS.setSelection(0);
        SpinProfileS.setSelection(0);
        SpinMesinS.setEnabled(false);
        SpinSusunS.setEnabled(false);
        RadioGroupS.clearCheck();
        remarkLabel.setText("");
        NoSanding_display.setText("");
    }


    private void m3() {
        try {
            double totalM3 = 0.0;

            for (DataRow row : temporaryDataListDetail) {

                // Parse nilai-nilai langsung tanpa membersihkan
                double tebal = Double.parseDouble(row.tebal);
                double lebar = Double.parseDouble(row.lebar);
                double panjang = Double.parseDouble(row.panjang);
                int pcs = Integer.parseInt(row.pcs);

                // Hitung M3 untuk baris ini
                double rowM3 = (tebal * lebar * panjang * pcs) / 1000000000.0;

                totalM3 += rowM3;
            }

            // Format hasil
            DecimalFormat df = new DecimalFormat("0.0000");
            String formattedM3 = df.format(totalM3);

            // Update TextView
            TextView M3TextView = findViewById(R.id.M3S);
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

        JumlahPcsS.setText(String.valueOf(totalPcs));
    }


    private void setCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        SimpleDateFormat saveFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        DateS.setText(currentDate);
        rawDate = saveFormat.format(new Date());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        TimeS.setText(currentTime);

        new LoadMesinTask().execute(currentDate);
        new LoadSusunTask().execute(currentDate);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Sanding.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                // Format input (dari DatePicker)
                rawDate = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);

                try {
                    SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

                    Date date = inputDateFormat.parse(rawDate);

                    String formattedDate = outputDateFormat.format(date);

                    DateS.setText(formattedDate);

                    new LoadMesinTask().execute(rawDate);
                    new LoadSusunTask().execute(rawDate);

                } catch (Exception e) {
                    e.printStackTrace();
                    DateS.setText("Invalid Date");
                }
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        int hour = calendarS.get(Calendar.HOUR_OF_DAY);
        int minute = calendarS.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Sanding.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                calendarS.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendarS.set(Calendar.MINUTE, selectedMinute);

                SimpleDateFormat timeFormat = new SimpleDateFormat(" HH:mm:ss", Locale.getDefault());
                String updatedTime = timeFormat.format(calendarS.getTime());
                TimeS.setText(updatedTime);
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

    private Uri createPdf(String noSanding, String jenisKayu, String date, String time, String tellyBy, String mesinSusun, String noSPK, String noSPKasal, String grade, List<DataRow> temporaryDataListDetail, String jumlahPcs, String m3, int printCount, String fisik, String remark) throws IOException {
        // Validasi parameter wajib
        if (noSanding == null || noSanding.trim().isEmpty()) {
            throw new IOException("Nomor FJ tidak boleh kosong");
        }

        if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
            throw new IOException("Data tidak boleh kosong");
        }

        String formattedTime = DateTimeUtils.formatTimeToHHmm(time);

        // Validasi dan set default value untuk parameter opsional
        noSanding = (noSanding != null) ? noSanding.trim() : "-";
        jenisKayu = (jenisKayu != null) ? jenisKayu.trim() : "-";
        date = (date != null) ? date.trim() : "-";
        formattedTime = (formattedTime != null) ? formattedTime.trim() : "-";
        grade = (grade != null) ? grade.trim() : "-";
        tellyBy = (tellyBy != null) ? tellyBy.trim() : "-";
        noSPK = (noSPK != null) ? noSPK.trim() : "-";
        jumlahPcs = (jumlahPcs != null) ? jumlahPcs.trim() : "-";
        m3 = (m3 != null) ? m3.trim() : "-";
        remark = (remark != null) ? remark.trim() : "-";

        String[] nama = tellyBy.split(" ");
        String namaTelly = nama[0]; // namaDepan sekarang berisi "Windiar"


        Uri pdfUri = null;
        ContentResolver resolver = getContentResolver();
        String fileName = "S4S_" + noSanding + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".pdf";
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
//                addInfoRow(leftColumn, "No", noSanding, timesNewRoman);
                addInfoRow(leftColumn, "Jenis", jenisKayu, timesNewRoman);
                addInfoRow(leftColumn, "Grade", grade, timesNewRoman);
                addInfoRow(leftColumn, "Fisik", fisik, timesNewRoman);

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
                        .setMarginTop(5)
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

                for (DataRow row : temporaryDataListDetail) {
                    String tebal = (row.tebal != null) ? df.format(Float.parseFloat(row.tebal)) : "-";
                    String lebar = (row.lebar != null) ? df.format(Float.parseFloat(row.lebar)) : "-";
                    String panjang = (row.panjang != null) ? df.format(Float.parseFloat(row.panjang)) : "-";
                    String pcs = (row.pcs != null) ? df.format(Integer.parseInt(row.pcs)) : "-";

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

                Paragraph qrCodeIDbottom = new Paragraph(noSanding).setTextAlignment(TextAlignment.LEFT).setFontSize(12).setMargins(-15, 0, 0, 47).setFont(timesNewRoman);

                BarcodeQRCode qrCode = new BarcodeQRCode(noSanding);
                PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);

                BarcodeQRCode qrCodeBottom = new BarcodeQRCode(noSanding);
                PdfFormXObject qrCodeBottomObject = qrCodeBottom.createFormXObject(ColorConstants.BLACK, pdfDocument);
                Image qrCodeBottomImage = new Image(qrCodeBottomObject).setWidth(115).setHorizontalAlignment(HorizontalAlignment.LEFT).setMargins(-55, 0, 0, 15);

                String formattedDate = DateTimeUtils.formatDateToDdYY(date);
                Paragraph textBulanTahunBold = new Paragraph(formattedDate).setTextAlignment(TextAlignment.RIGHT).setFontSize(50).setMargins(-75
                        , 0, 0, 0).setFont(timesNewRoman).setBold();

//                Paragraph namaFisik = new Paragraph("Fisik\t: " + fisik).setTextAlignment(TextAlignment.LEFT).setFontSize(11).setMargins(0, 0, 0, 7).setFont(timesNewRoman);
                Paragraph namaMesin = new Paragraph("Mesin   : " + mesinSusun).setTextAlignment(TextAlignment.LEFT).setFontSize(11).setMargins(0, 0, 0, 7).setFont(timesNewRoman);
                Paragraph textHeader = new Paragraph("LABEL SND").setUnderline().setTextAlignment(TextAlignment.LEFT).setFontSize(14).setMargins(0, 0, 0, 7).setFont(timesNewRomanBold);
                Paragraph textHeaderNomor = new Paragraph("NO : " + noSanding).setUnderline().setTextAlignment(TextAlignment.LEFT).setFontSize(14).setMargins(-21, 0, 0, 148).setFont(timesNewRomanBold);

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

                if(CBAfkirS.isChecked()){
                    document.add(afkirText);
                }

                if(CBLemburS.isChecked()){
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
        private String noSanding;

        public SaveBongkarSusunTask(String noBongkarSusun, String noSanding) {
            this.noBongkarSusun = noBongkarSusun;
            this.noSanding = noSanding;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.BongkarSusunOutputSanding (NoSanding, NoBongkarSusun) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding);
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
        private String noProduksi, noSanding;

        public SaveToDatabaseTask(String noProduksi, String noSanding) {
            this.noProduksi = noProduksi;
            this.noSanding = noSanding;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.SandingProduksiOutput (NoProduksi, NoSanding) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noProduksi);
                    ps.setString(2, noSanding);

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
    private class InsertDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private String lebar, panjang, tebal, pcs, noUrut, noSanding;

        public InsertDatabaseTask(String lebar, String panjang, String tebal, String pcs, String noUrut, String noSanding) {
            this.lebar = lebar;
            this.panjang = panjang;
            this.tebal = tebal;
            this.pcs = pcs;
            this.noUrut = noUrut;
            this.noSanding = noSanding;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.Sanding_d (Lebar, Panjang, Tebal, JmlhBatang, NoUrut, NoSanding) VALUES (?, ?, ?, ?, ?, ?)";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, lebar);
                    ps.setString(2, panjang);
                    ps.setString(3, tebal);
                    ps.setString(4, pcs);
                    ps.setString(5, noUrut);
                    ps.setString(6, noSanding);

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
            if (success) {
                Toast.makeText(Sanding.this, "Data berhasil disimpan ke database.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Sanding.this, "Gagal menyimpan data ke database.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class DeleteDataTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String noSanding = params[0];
            Connection con = ConnectionClass();
            boolean success = false;

            if (con != null) {
                try {
                    String query = "DELETE FROM dbo.Sanding_h WHERE NoSanding = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding);

                    int rowsAffected = ps.executeUpdate();
                    success = rowsAffected > 0;

                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(Sanding.this, "Data berhasil dihapus.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Sanding.this, "Gagal menghapus data.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private String noSanding, dateCreate, time, idTelly, noSPK, noSPKasal, idGrade, idJenisKayu, idFJProfile, remark;
        private int isReject, isLembur, IdUOMTblLebar, IdUOMPanjang;

        public UpdateDatabaseTask(String noSanding, String dateCreate, String time, String idTelly, String noSPK, String noSPKasal,
                                  String idGrade, String idJenisKayu, String idFJProfile,
                                  int isReject, int isLembur, int IdUOMTblLebar, int IdUOMPanjang, String remark) {
            this.noSanding = noSanding;
            this.dateCreate = dateCreate;
            this.time = time;
            this.idTelly = idTelly;
            this.noSPK = noSPK;
            this.noSPKasal = noSPKasal;
            this.idGrade = idGrade;
            this.idJenisKayu = idJenisKayu;
            this.idFJProfile = idFJProfile;
            this.isReject = isReject;
            this.isLembur = isLembur;
            this.IdUOMTblLebar = IdUOMTblLebar;
            this.IdUOMPanjang = IdUOMPanjang;
            this.remark = remark;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.Sanding_h (NoSanding, DateCreate, Jam, IdOrgTelly, NoSPK, NoSPKAsal, IdGrade, " +
                            "IdFJProfile, IdFisik, IdJenisKayu, IdWarehouse, IsReject, IsLembur, IdUOMTblLebar, IdUOMPanjang, Remark) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding); // NoSanding sebagai primary key
                    ps.setString(2, dateCreate);
                    ps.setString(3, time);
                    ps.setString(4, idTelly);
                    ps.setString(5, noSPK);
                    ps.setString(6, noSPKasal);
                    ps.setString(7, idGrade);
                    ps.setString(8, idFJProfile);
                    ps.setInt(9, 9); // IdFisik sesuai dengan nilai yang diminta
                    ps.setString(10, idJenisKayu);
                    ps.setInt(11, 9); // IdWarehouse sesuai dengan nilai yang diminta
                    ps.setInt(12, isReject);
                    ps.setInt(13, isLembur);
                    ps.setInt(14, IdUOMTblLebar);
                    ps.setInt(15, IdUOMPanjang);
                    ps.setString(16, remark);


                    int rowsUpdated = ps.executeUpdate();
                    ps.close();
                    con.close();
                    return rowsUpdated > 0;
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                    return false;
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
                return false;
            }
        }
    }

    private void setAndSaveNoSanding(final CountDownLatch latch) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection con = ConnectionClass();
                noSanding = null;
                boolean success = false;

                if (con != null) {
                    try {
                        // Query untuk mendapatkan NoSanding terakhir
                        String query = "SELECT MAX(NoSanding) FROM dbo.Sanding_h";
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                            String lastNoSanding = rs.getString(1);

                            if (lastNoSanding != null && lastNoSanding.startsWith("W.")) {
                                String numericPart = lastNoSanding.substring(2);
                                int numericValue = Integer.parseInt(numericPart);
                                int newNumericValue = numericValue + 1;

                                // Membuat NoSanding baru
                                noSanding = "W." + String.format("%06d", newNumericValue);
                            }
                        }

                        rs.close();
                        ps.close();
                        con.close();
                        success = true;
                    } catch (Exception e) {
                        Log.e("Database Error", e.getMessage());
                        success = false;
                    }
                } else {
                    Log.e("Connection Error", "Failed to connect to the database.");
                    success = false;
                }

                // Setelah operasi selesai, lakukan update UI di thread utama
                if (success) {
                    final String finalNewNoSanding = noSanding;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NoSanding.setQuery(finalNewNoSanding, true);
                            NoSanding.setVisibility(View.GONE);
                            NoSanding_display.setVisibility(View.VISIBLE);
                            NoSanding_display.setText(finalNewNoSanding);
                            NoSanding_display.setEnabled(false);
//                            Toast.makeText(Sanding.this, "NoSanding berhasil diatur dan disimpan.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("Error", "Failed to set or save NoSanding.");
                            Toast.makeText(Sanding.this, "Gagal mengatur atau menyimpan NoSanding.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                // Memberitahukan bahwa thread selesai
                latch.countDown();
            }
        }).start();
    }


     public class LoadJenisKayuTask extends AsyncTask<Void, Void, List<JenisKayu>> {
        @Override
        protected List<JenisKayu> doInBackground(Void... voids) {
            List<JenisKayu> jenisKayuList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT IdJenisKayu, Jenis FROM dbo.MstJenisKayu WHERE Enable = 1 AND IsInternal = 1 AND IsNonST = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idJenisKayu = rs.getString("IdJenisKayu");
                        String namaJenisKayu = rs.getString("Jenis");

                        JenisKayu jenisKayu = new JenisKayu(idJenisKayu, namaJenisKayu);
                        jenisKayuList.add(jenisKayu);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return jenisKayuList;
        }

        @Override
        protected void onPostExecute(List<JenisKayu> jenisKayuList) {
            JenisKayu dummyKayu = new JenisKayu("", "PILIH");
            jenisKayuList.add(0, dummyKayu);

            ArrayAdapter<JenisKayu> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, jenisKayuList);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

            SpinKayuS.setAdapter(adapter);
            SpinKayuS.setSelection(0);
        }
    }
    public class LoadJenisKayuTask2 extends AsyncTask<String, Void, List<JenisKayu>> {
        private String noSanding;

        // Constructor to accept NoSanding
        public LoadJenisKayuTask2(String noSanding) {
            this.noSanding = noSanding;
        }

        @Override
        protected List<JenisKayu> doInBackground(String... params) {
            List<JenisKayu> jenisKayuList = new ArrayList<>();
            Connection con = ConnectionClass(); // Ensure this method establishes a database connection

            if (con != null) {
                try {
                    // Update query to fetch jenis kayu based on noSanding
                    String query = "SELECT j.IdJenisKayu, j.Jenis " +
                            "FROM dbo.MstJenisKayu AS j " +
                            "INNER JOIN dbo.Sanding_h AS h ON h.IdJenisKayu = j.IdJenisKayu " +
                            "WHERE h.NoSanding = ? AND j.enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding); // Set the noSanding parameter

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idJenisKayu = rs.getString("IdJenisKayu");
                        String namaJenisKayu = rs.getString("Jenis");

                        JenisKayu jenisKayu = new JenisKayu(idJenisKayu, namaJenisKayu);
                        jenisKayuList.add(jenisKayu);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return jenisKayuList;
        }

        @Override
        protected void onPostExecute(List<JenisKayu> jenisKayuList) {
            if (!jenisKayuList.isEmpty()) {
                ArrayAdapter<JenisKayu> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, jenisKayuList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinKayuS.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load jenis kayu.");
            }
        }
    }


    private class LoadTellyTask extends AsyncTask<Void, Void, List<Telly>> {
        @Override
        protected List<Telly> doInBackground(Void... voids) {
            List<Telly> tellyList = new ArrayList<>();

            SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            username = prefs.getString("username", "");
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query =  "SELECT A.IdOrgTelly, A.NamaOrgTelly " +
                                    "FROM MstOrgTelly A " +
                                    "INNER JOIN ( " +
                                    "    SELECT Username, FName + ' ' + LName AS NamaTelly " +
                                    "    FROM MstUsername " +
                                    "    WHERE Username = ? " +
                                    ") B ON B.NamaTelly = A.NamaOrgTelly " +
                                    "WHERE A.Enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);

                    ps.setString(1, username);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idOrgTelly = rs.getString("IdOrgTelly");
                        String namaOrgTelly = rs.getString("NamaOrgTelly");

                        Telly telly = new Telly(idOrgTelly, namaOrgTelly);
                        tellyList.add(telly);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }

            return tellyList;
        }

        @Override
        protected void onPostExecute(List<Telly> tellyList) {
            // Buat adapter dengan data yang dimodifikasi
            ArrayAdapter<Telly> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, tellyList);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

            // Set adapter ke spinner
            SpinTellyS.setAdapter(adapter);
        }
    }


    private class LoadTellyTask2 extends AsyncTask<String, Void, List<Telly>> {
        private String noSanding;

        public LoadTellyTask2(String noSanding) {
            this.noSanding = noSanding;
        }

        @Override
        protected List<Telly> doInBackground(String... params) {
            List<Telly> tellyList = new ArrayList<>();
            Connection con = ConnectionClass(); // Ensure this method establishes a database connection

            if (con != null) {
                try {
                    // Update query to fetch telly data based on noSanding
                    String query = "SELECT t.IdOrgTelly, t.NamaOrgTelly " +
                            "FROM dbo.MstOrgTelly AS t " +
                            "INNER JOIN dbo.Sanding_h AS h ON h.IdOrgTelly = t.IdOrgTelly " +
                            "WHERE h.NoSanding = ? AND t.enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding); // Set the noSanding parameter

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idOrgTelly = rs.getString("IdOrgTelly");
                        String namaOrgTelly = rs.getString("NamaOrgTelly");

                        Telly telly = new Telly(idOrgTelly, namaOrgTelly);
                        tellyList.add(telly);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return tellyList;
        }

        @Override
        protected void onPostExecute(List<Telly> tellyList) {
            if (!tellyList.isEmpty()) {
                ArrayAdapter<Telly> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, tellyList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinTellyS.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load telly data.");
            }
        }
    }

    private class LoadSPKTask extends AsyncTask<Void, Void, List<SPK>> {
        @Override
        protected List<SPK> doInBackground(Void... voids) {
            List<SPK> spkList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT s.NoSPK, b.Buyer " +
                            "FROM MstSPK_h s " +
                            "INNER JOIN MstBuyer b ON s.IdBuyer = b.IdBuyer " +
                            "WHERE s.enable = 1 ";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String noSPK = rs.getString("NoSPK");
                        String buyer = rs.getString("Buyer");

                        // Buat objek SPK dengan kedua nilai
                        SPK spk = new SPK(noSPK, buyer);
                        spkList.add(spk);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return spkList;
        }

        @Override
        protected void onPostExecute(List<SPK> spkList) {
            // Tambahkan item PILIH di awal list
            SPK dummySPK = new SPK("PILIH");
            spkList.add(0, dummySPK);

            ArrayAdapter<SPK> adapter = new ArrayAdapter<>(Sanding.this,
                    android.R.layout.simple_spinner_item, spkList);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

            SpinSPKS.setAdapter(adapter);
            SpinSPKS.setSelection(0);
        }
    }

    private class LoadSPKAsalTask extends AsyncTask<Void, Void, List<SPKAsal>> {
        @Override
        protected List<SPKAsal> doInBackground(Void... voids) {
            List<SPKAsal> spkAsalList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query =  "SELECT s.NoSPK, b.Buyer " +
                            "FROM MstSPK_h s " +
                            "INNER JOIN MstBuyer b ON s.IdBuyer = b.IdBuyer " +
                            "WHERE s.enable = 1 ";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String noSPKasal = rs.getString("NoSPK");
                        String buyer = rs.getString("Buyer");

                        SPKAsal spkAsal = new SPKAsal(noSPKasal, buyer);
                        spkAsalList.add(spkAsal);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return spkAsalList;
        }

        @Override
        protected void onPostExecute(List<SPKAsal> spkAsalList) {
            SPKAsal dummySPKAsal = new SPKAsal("PILIH");
            spkAsalList.add(0, dummySPKAsal);

            ArrayAdapter<SPKAsal> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, spkAsalList);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

            SpinSPKAsalS.setAdapter(adapter);
            SpinSPKAsalS.setSelection(0);
        }
    }

    private class LoadSPKTask2 extends AsyncTask<Void, Void, List<SPK>> {
        private String noSanding;

        // Constructor to accept NoSanding
        public LoadSPKTask2(String noSanding) {
            this.noSanding = noSanding;
        }

        @Override
        protected List<SPK> doInBackground(Void... params) {
            List<SPK> spkList = new ArrayList<>();
            Connection con = ConnectionClass(); // Ensure this method establishes a database connection

            if (con != null) {
                try {
                    String query = "SELECT NoSPK FROM dbo.Sanding_h WHERE NoSanding = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding); // Set the noSanding parameter

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String noSPK = rs.getString("NoSPK");

                        SPK spk = new SPK(noSPK); // Assuming SPK class has a constructor that accepts noSPK
                        spkList.add(spk);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return spkList;
        }

        @Override
        protected void onPostExecute(List<SPK> spkList) {
            if (!spkList.isEmpty()) {
                ArrayAdapter<SPK> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, spkList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinSPKS.setAdapter(adapter);

                SpinSPKS.setEnabled(true);
            } else {
                Log.e("Error", "No SPK data found for the provided NoSanding.");
                SpinSPKS.setAdapter(null);
                SpinSPKS.setEnabled(false);
                SpinSPKS.setEnabled(false);
                Toast.makeText(Sanding.this, "Tidak ada data SPK yang ditemukan.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class LoadProfileTask extends AsyncTask<Void, Void, List<Profile>> {
        @Override
        protected List<Profile> doInBackground(Void... voids) {
            List<Profile> profileList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT Profile, IdFJProfile FROM dbo.MstFJProfile WHERE IdFJProfile != 0";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String namaProfile = rs.getString("Profile");
                        String idFJProfile = rs.getString("IdFJProfile");
                        Profile profileObj = new Profile(namaProfile, idFJProfile);
                        profileList.add(profileObj);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return profileList;
        }

        @Override
        protected void onPostExecute(List<Profile> profileList) {
            Profile dummyProfile = new Profile("PILIH", "");
            profileList.add(0, dummyProfile);

            ArrayAdapter<Profile> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, profileList);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

            SpinProfileS.setAdapter(adapter);
            SpinProfileS.setSelection(0);
        }
    }

    private class LoadProfileTask2 extends AsyncTask<String, Void, List<Profile>> {
        private String noSanding;

        public LoadProfileTask2(String noSanding) {
            this.noSanding = noSanding;
        }

        @Override
        protected List<Profile> doInBackground(String... voids) {
            List<Profile> profileList = new ArrayList<>();
            Connection con = ConnectionClass(); // Assumes this method exists to establish a DB connection

            if (con != null) {
                try {
                    String query = "SELECT p.Profile, p.IdFJProfile " +
                            "FROM dbo.MstFJProfile AS p " +
                            "INNER JOIN dbo.Sanding_h AS h ON h.IdFJProfile = p.IdFJProfile " +
                            "WHERE h.NoSanding = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String namaProfile = rs.getString("Profile");
                        String idFJProfile = rs.getString("IdFJProfile");
                        Profile profileObj = new Profile(namaProfile, idFJProfile);
                        profileList.add(profileObj);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return profileList;
        }

        @Override
        protected void onPostExecute(List<Profile> profileList) {
            if (!profileList.isEmpty()) {
                ArrayAdapter<Profile> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, profileList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinProfileS.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load profile data.");
            }
        }
    }

    private class LoadFisikTask extends AsyncTask<Void, Void, List<Fisik>> {
        @Override
        protected List<Fisik> doInBackground(Void... voids) {
            List<Fisik> fisikList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT Singkatan FROM dbo.MstWarehouse WHERE IdWarehouse = 9";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String namaWarehouse = rs.getString("Singkatan");

                        Fisik fisik = new Fisik(namaWarehouse);
                        fisikList.add(fisik);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return fisikList;
        }

        @Override
        protected void onPostExecute(List<Fisik> fisikList) {
            if (!fisikList.isEmpty()) {
                ArrayAdapter<Fisik> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, fisikList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinFisikS.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load fisik data.");
            }
        }
    }

    private class LoadFisikTask2 extends AsyncTask<String, Void, List<Fisik>> {
        private String noSanding;

        // Constructor menerima noSanding, bukan idWarehouse
        public LoadFisikTask2(String noSanding) {
            this.noSanding= noSanding;
        }

        @Override
        protected List<Fisik> doInBackground(String... params) {
            List<Fisik> fisikList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    // Query untuk mencocokkan IdWarehouse berdasarkan noSanding
                    String query = "SELECT mw.NamaWarehouse " +
                            "FROM dbo.MstWarehouse mw " +
                            "INNER JOIN dbo.Sanding_h Sanding ON mw.IdWarehouse = Sanding.IdWarehouse " +
                            "WHERE Sanding.noSanding = ?";

                    // Menyiapkan query dengan parameter noSanding
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding);  // Menggunakan noSanding untuk query

                    // Menjalankan query dan mendapatkan hasilnya
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String namaWarehouse = rs.getString("NamaWarehouse");
                        Fisik fisik = new Fisik(namaWarehouse);
                        fisikList.add(fisik);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", "Error during query execution: " + e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }

            return fisikList;
        }

        @Override
        protected void onPostExecute(List<Fisik> fisikList) {
            if (!fisikList.isEmpty()) {
                ArrayAdapter<Fisik> adapter = new ArrayAdapter<>(Sanding.this,
                        android.R.layout.simple_spinner_item, fisikList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinFisikS.setAdapter(adapter);
            } else {
                Log.e("Error", "No warehouse found.");
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(Sanding.this,
                        android.R.layout.simple_spinner_item, new String[]{"Tidak ada Fisik"});
                emptyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinFisikS.setAdapter(emptyAdapter);
            }
        }
    }


    private class LoadGradeTask extends AsyncTask<String, Void, List<Grade>> {
        @Override
        protected List<Grade> doInBackground(String... params) {
            List<Grade> gradeList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String idJenisKayuStr = params[0];
                    int idJenisKayu;

                    try {
                        idJenisKayu = Integer.parseInt(idJenisKayuStr);
                    } catch (NumberFormatException e) {
                        Log.e("Conversion Error", "IdJenisKayu should be an integer: " + idJenisKayuStr);
                        return gradeList;
                    }

                    String category = "Sanding";

                    String query = "SELECT DISTINCT a.IdGrade, a.NamaGrade " +
                            "FROM MstGrade a " +
                            "INNER JOIN MstGrade_d b ON a.IdGrade = b.IdGrade " +
                            "WHERE a.Enable = 1 AND b.IdJenisKayu = ? AND b.Category = ? " +
                            "ORDER BY a.NamaGrade ASC";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setInt(1, idJenisKayu);
                    ps.setString(2, category);

                    Log.d("LoadGradeTask", "Executing query: " + query + " with IdJenisKayu: " + idJenisKayu);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idGrade = rs.getString("IdGrade");
                        String namaGrade = rs.getString("NamaGrade");

                        if (idGrade != null && namaGrade != null) {
                            Log.d("LoadGradeTask", "Fetched Grade: IdGrade = " + idGrade + ", NamaGrade = " + namaGrade);
                            Grade gradeObj = new Grade(idGrade, namaGrade);
                            gradeList.add(gradeObj);
                        }
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return gradeList;
        }

        @Override
        protected void onPostExecute(List<Grade> gradeList) {
                if (!gradeList.isEmpty()) {
                    Grade dummyGrade = new Grade("", "PILIH");
                    gradeList.add(0, dummyGrade);

                } else {
                    Log.e("Error", "Tidak ada grade");
                    gradeList = new ArrayList<>();
                    gradeList.add(new Grade("", "GRADE TIDAK TERSEDIA"));
                }

                ArrayAdapter<Grade> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, gradeList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinGradeS.setAdapter(adapter);
                SpinGradeS.setSelection(0);
        }
    }

    private class LoadGradeTask2 extends AsyncTask<String, Void, List<Grade>> {
        private String noSanding;

        public LoadGradeTask2(String noSanding) {
            this.noSanding = noSanding;
        }

        @Override
        protected List<Grade> doInBackground(String... params) {
            List<Grade> gradeList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String idJenisKayuStr = params[0];
                    int idJenisKayu;

                    try {
                        idJenisKayu = Integer.parseInt(idJenisKayuStr);
                    } catch (NumberFormatException e) {
                        Log.e("Conversion Error", "IdJenisKayu should be an integer: " + idJenisKayuStr);
                        return gradeList;
                    }

                    String category = "Sanding";

                    String query = "SELECT DISTINCT a.IdGrade, a.NamaGrade " +
                            "FROM MstGrade a " +
                            "INNER JOIN MstGrade_d b ON a.IdGrade = b.IdGrade " +
                            "WHERE a.Enable = 1 AND b.IdJenisKayu = ? AND b.Category = ? AND b.NoSanding = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setInt(1, idJenisKayu);
                    ps.setString(2, category);
                    ps.setString(3, noSanding);
                    Log.d("LoadGradeTask", "Executing query: " + query + " with IdJenisKayu: " + idJenisKayu);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idGrade = rs.getString("IdGrade");
                        String namaGrade = rs.getString("NamaGrade");

                        if (idGrade != null && namaGrade != null) {
                            Log.d("LoadGradeTask", "Fetched Grade: IdGrade = " + idGrade + ", NamaGrade = " + namaGrade);
                            Grade gradeObj = new Grade(idGrade, namaGrade);
                            gradeList.add(gradeObj);
                        }
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return gradeList;
        }

        @Override
        protected void onPostExecute(List<Grade> gradeList) {
            if (!gradeList.isEmpty()) {
                ArrayAdapter<Grade> adapter = new ArrayAdapter<>(Sanding.this,
                        android.R.layout.simple_spinner_item, gradeList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinGradeS.setAdapter(adapter);
            } else {
                Log.e("Error", "Tidak ada grade");
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(Sanding.this,
                        android.R.layout.simple_spinner_item, new String[]{"Tidak ada grade"});
                emptyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinGradeS.setAdapter(emptyAdapter);
            }
        }
    }


    private class LoadMesinTask extends AsyncTask<String, Void, List<Mesin>> {
        @Override
        protected List<Mesin> doInBackground(String... params) {
            List<Mesin> mesinList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String selectedDate = params[0];

                    String query = "SELECT a.IdMesin, " +
                            "CONCAT(b.NamaMesin, ' - (SHIFT ', a.Shift, ')') AS NamaMesin, " +
                            "a.NoProduksi " +
                            "FROM dbo.SandingProduksi_h a " +
                            "INNER JOIN dbo.MstMesin b ON a.IdMesin = b.IdMesin " +
                            "WHERE Tanggal = ?";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, selectedDate);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idMesin = rs.getString("IdMesin");
                        String nomorProduksi = rs.getString("NoProduksi");
                        String namaMesin = rs.getString("NamaMesin");

                        Mesin mesin = new Mesin(nomorProduksi, namaMesin);
                        mesinList.add(mesin);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return mesinList;
        }

        @Override
        protected void onPostExecute(List<Mesin> mesinList) {
            if (!mesinList.isEmpty()) {
                ArrayAdapter<Mesin> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinMesinS.setAdapter(adapter);
            } else {
                SpinMesinS.setAdapter(null);
                Log.e("Error", "Failed to load mesin data.");
            }
        }
    }

    private class LoadMesinTask2 extends AsyncTask<Void, Void, List<Mesin>> {
        private String noSanding;

        public LoadMesinTask2(String noSanding) {
            this.noSanding = noSanding;
        }

        @Override
        protected List<Mesin> doInBackground(Void... params) {
            List<Mesin> mesinList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT b.NoProduksi, d.NamaMesin FROM Sanding_h a " +
                            "INNER JOIN SandingProduksiOutput b ON b.NoSanding = a.NoSanding " +
                            "INNER JOIN SandingProduksi_h c ON c.NoProduksi = b.NoProduksi " +
                            "INNER JOIN MstMesin d ON d.IdMesin = c.IdMesin " +
                            "WHERE a.NoSanding = ?";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String nomorProduksi = rs.getString("NoProduksi");
                        String namaMesin = rs.getString("NamaMesin");

                        Mesin mesin = new Mesin(nomorProduksi, namaMesin);
                        mesinList.add(mesin);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return mesinList;
        }

        @Override
        protected void onPostExecute(List<Mesin> mesinList) {
            if (!mesinList.isEmpty()) {
                ArrayAdapter<Mesin> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinMesinS.setAdapter(adapter);

                radioButtonMesinS.setEnabled(true);
                radioButtonBSusunS.setEnabled(false);
            } else {
                Log.e("Error", "Failed to load mesin data.");
                radioButtonMesinS.setEnabled(false);
                radioButtonBSusunS.setEnabled(false);

                Toast.makeText(Sanding.this, "Tidak ada data mesin yang ditemukan.", Toast.LENGTH_SHORT).show();
                SpinMesinS.setAdapter(null);
            }
        }
    }


    private class LoadSusunTask extends AsyncTask<String, Void, List<Susun>> {
        @Override
        protected List<Susun> doInBackground(String... params) {
            List<Susun> susunList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String selectedDate = params[0];

                    String query = "SELECT NoBongkarSusun FROM dbo.BongkarSusun_h WHERE Tanggal = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, selectedDate);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String nomorBongkarSusun = rs.getString("NoBongkarSusun");

                        Susun susun = new Susun(nomorBongkarSusun);
                        susunList.add(susun);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return susunList;
        }

        @Override
        protected void onPostExecute(List<Susun> susunList) {
            if (!susunList.isEmpty()) {
                ArrayAdapter<Susun> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, susunList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinSusunS.setAdapter(adapter);
            } else {
                SpinSusunS.setAdapter(null);
                Log.e("Error", "Failed to load susun data");
            }
        }
    }
    private class LoadSusunTask2 extends AsyncTask<Void, Void, List<Susun>> {
        private String noSanding;

        public LoadSusunTask2(String noSanding) {
            this.noSanding = noSanding;
        }

        @Override
        protected List<Susun> doInBackground(Void... params) {
            List<Susun> susunList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT NoBongkarSusun FROM dbo.BongkarSusunOutputSanding WHERE NoSanding = ?"; // Filter by noSandingSanding
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String nomorBongkarSusun = rs.getString("NoBongkarSusun");

                        Susun susun = new Susun(nomorBongkarSusun);
                        susunList.add(susun);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return susunList;
        }

        @Override
        protected void onPostExecute(List<Susun> susunList) {
            if (!susunList.isEmpty()) {
                ArrayAdapter<Susun> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, susunList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinSusunS.setAdapter(adapter);

                radioButtonMesinS.setEnabled(false);
                radioButtonBSusunS.setEnabled(true);
            } else {
                Log.e("Error", "Failed to load susun data.");
                radioButtonMesinS.setEnabled(false);
                radioButtonBSusunS.setEnabled(false);

                Toast.makeText(Sanding.this, "Tidak ada data susun yang ditemukan.", Toast.LENGTH_SHORT).show();
                SpinSusunS.setAdapter(null);
            }
        }
    }

     public class JenisKayu {
        private String idJenisKayu;
        private String namaJenisKayu;

        public JenisKayu(String idJenisKayu, String namaJenisKayu) {
            this.idJenisKayu = idJenisKayu;
            this.namaJenisKayu = namaJenisKayu;
        }

        public String getIdJenisKayu() {
            return idJenisKayu;
        }

        public String getNamaJenisKayu() {
            return namaJenisKayu;
        }

        @Override
        public String toString() {
            return namaJenisKayu;
        }
    }


    public class Telly {
        private String idTelly;
        private String namaTelly;

        public Telly(String idTelly, String namaTelly) {
            this.idTelly = idTelly;
            this.namaTelly = namaTelly;
        }

        public String getIdTelly() {
            return idTelly;
        }

        public String getNamaTelly() {
            return namaTelly;
        }

        @Override
        public String toString() {
            return namaTelly;
        }
    }


    public class SPK {
        private String noSPK;
        private String buyer;

        public SPK(String noSPK, String buyer) {
            this.noSPK = noSPK;
            this.buyer = buyer;
        }

        // Constructor untuk dummy/placeholder
        public SPK(String noSPK) {
            this.noSPK = noSPK;
            this.buyer = "";
        }

        public String getNoSPK() {
            return noSPK;
        }

        public String getBuyer() {
            return buyer;
        }

        // Override toString untuk tampilan di spinner
        @Override
        public String toString() {
            if (buyer.isEmpty()) {
                return noSPK;
            }
            return noSPK + " - " + buyer;
        }
    }

    public class SPKAsal {
        private String noSPKAsal;
        private String buyer;

        public SPKAsal(String noSPKAsal, String buyer) {
            this.noSPKAsal = noSPKAsal;
            this.buyer = buyer;
        }

        // Constructor untuk dummy/placeholder
        public SPKAsal(String noSPKAsal) {
            this.noSPKAsal = noSPKAsal;
            this.buyer = "";
        }

        public String getNoSPKAsal() {
            return noSPKAsal;
        }

        public String getBuyer() {
            return buyer;
        }

        // Override toString untuk tampilan di spinner
        @Override
        public String toString() {
            if (buyer.isEmpty()) {
                return noSPKAsal; // Untuk item "PILIH"
            }
            return noSPKAsal + " - " + buyer;
        }
    }


    public class Profile {
        private String idFJProfile;
        private String namaProfile;

        public Profile(String namaProfile, String idFJProfile) {
            this.namaProfile = namaProfile;
            this.idFJProfile = idFJProfile;
        }

        public String getIdFJProfile() {
            return idFJProfile;
        }

        public String getNamaProfile() {
            return namaProfile;
        }

        @Override
        public String toString() {
            return namaProfile;
        }
    }



    public class Fisik {
        private String idWarehouse; // Jika diperlukan
        private String namaWarehouse;

        public Fisik(String namaWarehouse) {
            this.namaWarehouse = namaWarehouse;
        }

        public String getIdWarehouse() {
            return idWarehouse;
        }

        public void setIdWarehouse(String idWarehouse) {
            this.idWarehouse = idWarehouse;
        }

        public String getNamaWarehouse() {
            return namaWarehouse;
        }

        @Override
        public String toString() {
            return namaWarehouse;
        }
    }


    public class Grade {
        private String idGrade;
        private String namaGrade;

        public Grade(String idGrade, String namaGrade) {
            this.idGrade = idGrade;
            this.namaGrade = namaGrade;
        }

        public String getIdGrade() {
            return idGrade;
        }

        public String getNamaGrade() {
            return namaGrade;
        }

        @Override
        public String toString() {
            return namaGrade;
        }
    }

    public class Mesin {
        private String noProduksi;
        private String namaMesin;

        public Mesin(String noProduksi, String namaMesin) {
            this.noProduksi = noProduksi;
            this.namaMesin = namaMesin;
        }

        public String getNoProduksi() {
            return noProduksi;
        }

        public String getNamaMesin() {
            return namaMesin;
        }

        @Override
        public String toString() {
            return namaMesin + " - " + noProduksi;
        }
    }

    public class Susun {
        private String nomorBongkarSusun;

        public Susun(String nomorBongkarSusun) {
            this.nomorBongkarSusun = nomorBongkarSusun;
        }

        public String getNoBongkarSusun() {
            return nomorBongkarSusun;
        }

        public void setNoBongkarSusun(String nomorBongkarSusun) {
            this.nomorBongkarSusun = nomorBongkarSusun;
        }

        @Override
        public String toString() {
            return nomorBongkarSusun;
        }
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