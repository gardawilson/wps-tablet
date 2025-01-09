package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import androidx.appcompat.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
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
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import android.print.PrintJob;
import com.itextpdf.kernel.geom.AffineTransform;
import android.print.PrintManager;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.Bundle;
import android.app.TimePickerDialog;
import android.widget.TimePicker;
import android.widget.AutoCompleteTextView;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;





import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.bouncycastle.cms.PasswordRecipientId;
import org.bouncycastle.jcajce.provider.symmetric.Serpent;

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
import java.util.List;
import java.util.ArrayList;


import com.itextpdf.layout.element.LineSeparator;
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
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.kernel.geom.Rectangle;




import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import android.text.TextUtils;
import com.itextpdf.layout.element.Paragraph;
import java.math.RoundingMode;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Packing extends AppCompatActivity {

    private String username;
    private SearchView NoBarangJadi;
    private EditText DateP;
    private EditText TimeP;
    private EditText NoWIP;
    private Spinner SpinKayuP;
    private Spinner SpinTellyP;
    private Spinner SpinSPKP;
    private Spinner SpinSPKAsalP;
    private Spinner SpinProfileP;
    private Spinner SpinBarangJadiP;
    private Spinner SpinMesinP;
    private Spinner SpinSusunP;
    private Calendar calendarP;
    private RadioGroup RadioGroupP;
    private RadioButton radioButtonMesinP;
    private RadioButton radioButtonBSusunP;
    private Button BtnDataBaruP;
    private Button BtnSimpanP;
    private Button BtnBatalP;
    private Button BtnHapusDetailP;
    private boolean isDataBaruClickedP = false;
    private CheckBox CBAfkirP;
    private CheckBox CBLemburP;
    private Button BtnInputDetailP;
    private AutoCompleteTextView DetailLebarP;
    private AutoCompleteTextView DetailTebalP;
    private AutoCompleteTextView DetailPanjangP;
    private EditText DetailPcsP;
    private static int currentNumber = 1;
    private Button BtnPrintP;
    private TextView M3P;
    private TextView JumlahPcsP;
    private boolean isCBAfkirP, isCBLemburP;
    private Button BtnSearchP;
    private int rowCount = 0;
    private TableLayout Tabel;
    boolean isCreateMode = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    private EditText NoBarangJadi_display;
    private String rawDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                new DeleteLatestNoBJTask().execute();

                finish();
            }
        });

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_packing);

        NoWIP = findViewById(R.id.NoWIP);
        NoBarangJadi = findViewById(R.id.NoBarangJadi);
        DateP = findViewById(R.id.DateP);
        TimeP = findViewById(R.id.TimeP);
        SpinKayuP = findViewById(R.id.SpinKayuP);
        SpinTellyP = findViewById(R.id.SpinTellyP);
        SpinSPKP = findViewById(R.id.SpinSPKP);
        SpinSPKAsalP = findViewById(R.id.SpinSPKAsalP);
        SpinProfileP = findViewById(R.id.SpinProfileP);
        SpinBarangJadiP = findViewById(R.id.SpinBarangJadiP);
        calendarP = Calendar.getInstance();
        SpinMesinP = findViewById(R.id.SpinMesinP);
        SpinSusunP = findViewById(R.id.SpinSusunP);
        radioButtonMesinP = findViewById(R.id.radioButtonMesinP);
        radioButtonBSusunP = findViewById(R.id.radioButtonBSusunP);
        BtnDataBaruP = findViewById(R.id.BtnDataBaruP);
        BtnSimpanP = findViewById(R.id.BtnSimpanP);
        BtnBatalP = findViewById(R.id.BtnBatalP);
        BtnHapusDetailP = findViewById(R.id.BtnHapusDetailP);
        CBLemburP = findViewById(R.id.CBLemburP);
        CBAfkirP = findViewById(R.id.CBAfkirP);
        BtnInputDetailP = findViewById(R.id.BtnInputDetailP);
        DetailPcsP = findViewById(R.id.DetailPcsP);
        DetailTebalP = findViewById(R.id.DetailTebalP);
        DetailPanjangP = findViewById(R.id.DetailPanjangP);
        DetailLebarP = findViewById(R.id.DetailLebarP);
        BtnPrintP = findViewById(R.id.BtnPrintP);
        M3P = findViewById(R.id.M3P);
        JumlahPcsP = findViewById(R.id.JumlahPcsP);
        Tabel = findViewById(R.id.Tabel);
        RadioGroupP = findViewById(R.id.RadioGroupP);
        NoBarangJadi_display = findViewById(R.id.NoBarangJadi_display);

        // Set imeOptions untuk memungkinkan pindah fokus
        DetailTebalP.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        DetailLebarP.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        DetailPanjangP.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        // Menangani aksi 'Enter' pada keyboard
        DetailTebalP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Jika tombol 'Enter' ditekan, pindahkan fokus ke DetailLebarS4S
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Pastikan DetailLebarS4S bisa menerima fokus
                    DetailLebarP.requestFocus();
                    return true; // Menunjukkan bahwa aksi sudah ditangani
                }
                return false;
            }
        });

        DetailLebarP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    DetailPanjangP.requestFocus();
                    return true;
                }
                return false;
            }
        });

        DetailPanjangP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    DetailPcsP.requestFocus();
                    return true;
                }
                return false;
            }
        });

        DetailPcsP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {  // Mengubah ke IME_ACTION_DONE
                    // Ambil input dari AutoCompleteTextView
                    String noBarangJadi = NoBarangJadi.getQuery().toString();
                    String tebal = DetailTebalP.getText().toString().trim();
                    String lebar = DetailLebarP.getText().toString().trim();
                    String panjang = DetailPanjangP.getText().toString().trim();

                    // Ambil data SPK, Jenis Kayu, dan Grade dari Spinner
                    SPK selectedSPK = (SPK) SpinSPKP.getSelectedItem();
                    Fisik selectedFisik = (Fisik) SpinBarangJadiP.getSelectedItem();
                    JenisKayu selectedJenisKayu = (JenisKayu) SpinKayuP.getSelectedItem();

                    String idBarangJadi = selectedFisik != null ? selectedFisik.getIdBarangJadi() : null;
                    String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
                    String idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;

                    // Validasi input kosong
                    if (noBarangJadi.isEmpty() || tebal.isEmpty() || lebar.isEmpty() || panjang.isEmpty()) {
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
                                addDataDetail(noBarangJadi);
                                jumlahpcs();
                                m3();
//                                Toast.makeText(Packing.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                closeKeyboard();
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

        NoBarangJadi_display.setVisibility(View.GONE);
        disableForm();

        int searchEditTextId = NoBarangJadi.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = NoBarangJadi.findViewById(searchEditTextId);

        if (searchEditText != null) {
            searchEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        NoBarangJadi.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                    if (!newText.startsWith("I.")) {
                        NoBarangJadi.setQuery("I." + newText, false);  // false untuk mencegah pemanggilan ulang listener
                    }

                    if(!newText.isEmpty()){
                        disableForm();
                        loadSubmittedData(newText);
                        BtnPrintP.setEnabled(true);
                    }
                    else{
                        enableForm();
                    }
                }
                return true;
            }
        });


        radioButtonMesinP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpinMesinP.setEnabled(true);
                    SpinSusunP.setEnabled(false);
                }
            }
        });

        radioButtonBSusunP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpinSusunP.setEnabled(true);
                    SpinMesinP.setEnabled(false);
                }
            }
        });

        setCurrentDateTime();

        BtnDataBaruP.setOnClickListener(v -> {
            // Tampilkan Dialog Loading
            AlertDialog.Builder builder = new AlertDialog.Builder(Packing.this);
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
                        Toast.makeText(Packing.this, "Koneksi terlalu lambat. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                    }
                });
            }, 20000); // Timeout 20 detik

            // Gunakan RxJava untuk menjalankan semua tugas asinkron secara paralel
            Completable.mergeArray(
                            Completable.fromAction(this::setCurrentDateTime),
                            Completable.fromAction(() -> setCreateMode(true)),
                            Completable.fromAction(() -> new SetAndSaveNoBJTask().execute()),
                            Completable.fromAction(() -> new LoadJenisKayuTask().execute()),
                            Completable.fromAction(() -> new LoadTellyTask().execute()),
                            Completable.fromAction(() -> new LoadSPKTask().execute()),
                            Completable.fromAction(() -> new LoadSPKAsalTask().execute()),
                            Completable.fromAction(() -> new LoadProfileTask().execute()),
                            Completable.fromAction(() -> new LoadFisikTask().execute())
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnTerminate(() -> {
                        // Dismiss loading dialog jika semua selesai
                        if (!isTimeout[0] && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                        }
                        // Aktifkan tombol dan reset data
                        BtnSimpanP.setEnabled(true);
                        BtnBatalP.setEnabled(true);
                        BtnPrintP.setEnabled(false);
                        BtnDataBaruP.setEnabled(false);
                        BtnDataBaruP.setVisibility(View.GONE);
                        BtnSimpanP.setVisibility(View.VISIBLE);

                        clearData();
                        resetDetailData();
                        enableForm();
                    })
                    .subscribe(
                            () -> Log.d("BtnDataBaruP", "Semua data berhasil dimuat."),
                            throwable -> {
                                if (loadingDialog.isShowing()) {
                                    loadingDialog.dismiss();
                                }
                                Toast.makeText(Packing.this, "Kesalahan saat memuat data: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("BtnDataBaruP", "Kesalahan: " + throwable.getMessage(), throwable);
                            }
                    );
        });

        BtnSimpanP.setOnClickListener(v -> {
            ProgressDialog progressDialog = new ProgressDialog(Packing.this);
            progressDialog.setMessage("Menyimpan data...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            String noBarangJadi = NoBarangJadi.getQuery().toString();
            String dateCreate = rawDate;
            String time = TimeP.getText().toString();

            Telly selectedTelly = (Telly) SpinTellyP.getSelectedItem();
            SPK selectedSPK = (SPK) SpinSPKP.getSelectedItem();
            SPKAsal selectedSPKAsal = (SPKAsal) SpinSPKAsalP.getSelectedItem();
            Profile selectedProfile = (Profile) SpinProfileP.getSelectedItem();
            Fisik selectedFisik = (Fisik) SpinBarangJadiP.getSelectedItem();
            JenisKayu selectedJenisKayu = (JenisKayu) SpinKayuP.getSelectedItem();
            Mesin selectedMesin = (Mesin) SpinMesinP.getSelectedItem();
            Susun selectedSusun = (Susun) SpinSusunP.getSelectedItem();
            RadioGroup radioGroupUOMTblLebar = findViewById(R.id.radioGroupUOMTblLebar);
            RadioGroup radioGroupUOMPanjang = findViewById(R.id.radioGroupUOMPanjang);

            String idTelly = selectedTelly != null ? selectedTelly.getIdTelly() : null;
            String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
            String noSPKasal = selectedSPKAsal != null ? selectedSPKAsal.getNoSPKAsal() : null;
            String idProfile = selectedProfile != null ? selectedProfile.getIdFJProfile() : null;
            String idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;
            String noProduksi = selectedMesin != null ? selectedMesin.getNoProduksi() : null;
            String noBongkarSusun = selectedSusun != null ? selectedSusun.getNoBongkarSusun() : null;
            String idBarangJadi = selectedFisik != null ? selectedFisik.getIdBarangJadi() : null;
            int isReject = CBAfkirP.isChecked() ? 1 : 0;
            int isLembur = CBLemburP.isChecked() ? 1 : 0;
            int idUOMTblLebar = radioGroupUOMTblLebar.getCheckedRadioButtonId() == R.id.radioMillimeter ? 1 : 4;
            int idUOMPanjang;
            if (radioGroupUOMPanjang.getCheckedRadioButtonId() == R.id.radioCentimeter) {
                idUOMPanjang = 1;
            } else if (radioGroupUOMPanjang.getCheckedRadioButtonId() == R.id.radioMeter) {
                idUOMPanjang = 2;
            } else {
                idUOMPanjang = 3;
            }

            if (!isInternetAvailable()) {
                progressDialog.dismiss();
                Toast.makeText(Packing.this, "Tidak ada koneksi internet. Coba lagi nanti.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (noBarangJadi.isEmpty() || dateCreate.isEmpty() || time.isEmpty() ||
                    selectedTelly == null || selectedTelly.getIdTelly().isEmpty() ||
                    selectedSPK == null || selectedSPK.getNoSPK().equals("PILIH") ||
                    selectedSPKAsal == null || selectedSPKAsal.getNoSPKAsal().equals("PILIH") ||
                    selectedProfile == null || selectedProfile.getIdFJProfile().isEmpty() ||
                    selectedFisik == null || selectedFisik.getIdBarangJadi().isEmpty() ||
                    selectedJenisKayu == null || selectedJenisKayu.getIdJenisKayu().isEmpty() ||
                    (!radioButtonMesinP.isChecked() && !radioButtonBSusunP.isChecked()) ||
                    (radioButtonMesinP.isChecked() && (selectedMesin == null || selectedMesin.getNoProduksi().isEmpty())) ||
                    (radioButtonBSusunP.isChecked() && (selectedSusun == null || selectedSusun.getNoBongkarSusun().isEmpty())) ||
                    temporaryDataListDetail.isEmpty()) {

                progressDialog.dismiss();
                Toast.makeText(Packing.this, "Pastikan semua field terisi dengan benar.", Toast.LENGTH_SHORT).show();
                return;
            }

            Completable.create(emitter -> {
                        checkMaxPeriod(dateCreate, (canProceed, message) -> {
                            if (!canProceed) {
                                emitter.onError(new Exception(message));
                            } else {
                                try {
                                    new UpdateDatabaseTask(
                                            noBarangJadi, dateCreate, time, idTelly, noSPK, noSPKasal,
                                            idJenisKayu, idProfile, isReject, isLembur, idBarangJadi
                                    ).execute();

                                    if (radioButtonMesinP.isChecked() && SpinMesinP.isEnabled() && noProduksi != null) {
                                        new SaveToDatabaseTask(noProduksi, noBarangJadi).execute();
                                        for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                                            Packing.DataRow dataRow = temporaryDataListDetail.get(i);
                                            saveDataDetailToDatabase(noBarangJadi, i + 1,
                                                    Double.parseDouble(dataRow.tebal),
                                                    Double.parseDouble(dataRow.lebar),
                                                    Double.parseDouble(dataRow.panjang),
                                                    Integer.parseInt(dataRow.pcs));
                                        }
                                    } else if (radioButtonBSusunP.isChecked() && SpinSusunP.isEnabled() && noBongkarSusun != null) {
                                        new SaveBongkarSusunTask(noBongkarSusun, noBarangJadi).execute();
                                        for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                                            Packing.DataRow dataRow = temporaryDataListDetail.get(i);
                                            saveDataDetailToDatabase(noBarangJadi, i + 1,
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
                                    String activity = String.format("Menyimpan Data %s Pada Label Packing (Mobile)", noBarangJadi);
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
                        BtnDataBaruP.setEnabled(true);
                        BtnPrintP.setEnabled(true);
                        BtnSimpanP.setEnabled(false);
                        BtnDataBaruP.setVisibility(View.VISIBLE);
                        BtnSimpanP.setVisibility(View.GONE);
                        disableForm();
                        Toast.makeText(Packing.this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
                    }, throwable -> {
                        progressDialog.dismiss();
                        Toast.makeText(Packing.this, "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });


        BtnBatalP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCreateMode(false);
                resetDetailData();
                resetAllForm();
                disableForm();

                new DeleteLatestNoBJTask().execute();

                BtnDataBaruP.setEnabled(true);
                BtnSimpanP.setEnabled(false);
                NoBarangJadi.setVisibility(View.VISIBLE);
                NoBarangJadi_display.setVisibility(View.GONE);
                BtnDataBaruP.setVisibility(View.VISIBLE);
                BtnSimpanP.setVisibility(View.GONE);
                CBLemburP.setChecked(false);
                CBAfkirP.setChecked(false);
            }
        });

        DateP.setOnClickListener(v -> showDatePickerDialog());

        TimeP.setOnClickListener(v -> showTimePickerDialog());

        SpinSPKP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isCreateMode) {
                    resetDetailData();
                    String selectedSPK = parent.getItemAtPosition(position) != null ?
                            parent.getItemAtPosition(position).toString() : "";

                    // Pindahkan operasi berat ke thread terpisah
                    new Thread(() -> {
                        // Pengecekan sinkron apakah SPK terkunci
                        boolean isLocked = isSPKLocked(selectedSPK);

                        // Ambil rekomendasi dari database (operasi berat)
                        Map<String, List<String>> dimensionData = listSPKDetailRecommendation(selectedSPK);

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
                            DetailTebalP.setAdapter(tebalAdapter);
                            DetailLebarP.setAdapter(lebarAdapter);
                            DetailPanjangP.setAdapter(panjangAdapter);

                            // Set threshold untuk semua AutoCompleteTextView
                            DetailTebalP.setThreshold(0);
                            DetailLebarP.setThreshold(0);
                            DetailPanjangP.setThreshold(0);

                            // Tampilkan status lock
//                            if (isLocked) {
//                                Toast.makeText(Packing.this, "Dimension terkunci", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(Packing.this, "Dimension tidak terkunci", Toast.LENGTH_SHORT).show();
//                            }
                        });
                    }).start();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                DetailTebalP.setText("");
                DetailPanjangP.setText("");
                DetailLebarP.setText("");
                DetailTebalP.setAdapter(null);
                DetailPanjangP.setAdapter(null);
                DetailLebarP.setAdapter(null);
            }
        });

        BtnInputDetailP.setOnClickListener(v -> {
            // Ambil input dari AutoCompleteTextView
            String noBarangJadi = NoBarangJadi.getQuery().toString();
            String tebal = DetailTebalP.getText().toString().trim();
            String lebar = DetailLebarP.getText().toString().trim();
            String panjang = DetailPanjangP.getText().toString().trim();

            // Ambil data SPK, Jenis Kayu, dan Grade dari Spinner
            SPK selectedSPK = (SPK) SpinSPKP.getSelectedItem();
            Fisik selectedFisik = (Fisik) SpinBarangJadiP.getSelectedItem();
            JenisKayu selectedJenisKayu = (JenisKayu) SpinKayuP.getSelectedItem();

            String idBarangJadi = selectedFisik != null ? selectedFisik.getIdBarangJadi() : null;
            String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
            String idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;

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
//                        Toast.makeText(Packing.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    } else {
                        // Tampilkan pesan error
                        Toast.makeText(Packing.this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        });

        BtnHapusDetailP.setOnClickListener(v -> {
            resetDetailData();
        });

        BtnPrintP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validasi input
                if (NoBarangJadi.getQuery() == null || NoBarangJadi.getQuery().toString().trim().isEmpty()) {
                    Toast.makeText(Packing.this, "Nomor ST tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validasi apakah ada data untuk dicetak
                if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
                    Toast.makeText(Packing.this, "Tidak ada data untuk dicetak", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cek status HasBeenPrinted di database
                String noBarangJadi = NoBarangJadi.getQuery().toString().trim();
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
                            String jenisKayu = SpinKayuP.getSelectedItem() != null ? SpinKayuP.getSelectedItem().toString().trim() : "";
                            String date = DateP.getText() != null ? DateP.getText().toString().trim() : "";
                            String time = TimeP.getText() != null ? TimeP.getText().toString().trim() : "";
                            String tellyBy = SpinTellyP.getSelectedItem() != null ? SpinTellyP.getSelectedItem().toString().trim() : "";
                            String noSPK = SpinSPKP.getSelectedItem() != null ? SpinSPKP.getSelectedItem().toString().trim() : "";
                            String noSPKasal = SpinSPKAsalP.getSelectedItem() != null ? SpinSPKAsalP.getSelectedItem().toString().trim() : "";
                            String jumlahPcs = JumlahPcsP.getText() != null ? JumlahPcsP.getText().toString().trim() : "";
                            String m3 = M3P.getText() != null ? M3P.getText().toString().trim() : "";
                            String namaBJ = SpinBarangJadiP.getSelectedItem() != null ? SpinBarangJadiP.getSelectedItem().toString().trim() : "";
                            if(radioButtonMesinP.isChecked()){
                                mesinSusun = SpinMesinP.getSelectedItem() != null ? SpinMesinP.getSelectedItem().toString().trim() : "";
                            }
                            else{
                                mesinSusun = SpinSusunP.getSelectedItem() != null ? SpinSusunP.getSelectedItem().toString().trim() : "";
                            }

                            // Buat PDF dengan parameter printCount
                            Uri pdfUri = createPdf(noBarangJadi, jenisKayu, date, time, tellyBy, mesinSusun, noSPK, noSPKasal,
                                    temporaryDataListDetail, jumlahPcs, m3, printCount, namaBJ);


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

    private class DeleteLatestNoBJTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            String noBJ = NoBarangJadi_display.getText().toString().trim();
            boolean success = false;
            if (con != null) {
                try {
                    String query = "DELETE FROM dbo.BarangJadi_h WHERE NoBJ = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBJ);
                    int rowsAffected = ps.executeUpdate();
                    ps.close();
                    con.close();

                    success = rowsAffected > 0;
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
//            if (success) {
//                Toast.makeText(Packing.this, "NoBarangJadi terbaru berhasil dihapus.", Toast.LENGTH_SHORT).show();
//                // Lakukan tindakan lain setelah penghapusan NoBarangJadi, jika diperlukan
//            } else {
//                Log.e("Error", "Failed to delete the latest NoBarangJadi.");
//                Toast.makeText(Packing.this, "Gagal menghapus NoBarangJadi terbaru.", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    // Deklarasi CheckSPKDataTask di level class
    private class CheckSPKDataTask extends AsyncTask<Void, Void, String> {
        private final String noSPK;
        private final String tebal;
        private final String lebar;
        private final String panjang;
        private final String idJenisKayu;
        private final String idBarangJadi;

        public CheckSPKDataTask(String noSPK, String tebal, String lebar, String panjang, String idJenisKayu, String idBarangJadi) {
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

                // Query untuk validasi data
                String query = "SELECT * FROM MstSPK_d WHERE NoSPK = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, noSPK);
                ResultSet rs = stmt.executeQuery();

                boolean matchFound = false;
                while (rs.next()) {
                    // Bandingkan setiap kolom
                    if (Double.parseDouble(tebal) == rs.getDouble("Tebal") &&
                            Double.parseDouble(lebar) == rs.getDouble("Lebar") &&
                            Double.parseDouble(panjang) == rs.getDouble("Panjang") &&
                            idJenisKayu.equals(rs.getString("IdJenisKayu")) &&
                            idBarangJadi.equals(rs.getString("IdBarangJadi"))) {
                        matchFound = true;
                        break;
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
                    if (!columnMatches(connection, "IdBarangJadi", noSPK, idBarangJadi)) {
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
                    // Menggunakan metode 1
                    float tebal = rs.getFloat("Tebal");
                    float lebar = rs.getFloat("Lebar");
                    float panjang = rs.getFloat("Panjang");

                    dimensionData.get("tebal").add((tebal == (int)tebal) ?
                            String.valueOf((int)tebal) : String.valueOf(tebal));
                    dimensionData.get("lebar").add((lebar == (int)lebar) ?
                            String.valueOf((int)lebar) : String.valueOf(lebar));
                    dimensionData.get("panjang").add((panjang == (int)panjang) ?
                            String.valueOf((int)panjang) : String.valueOf(panjang));
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
        DateP.setEnabled(true);
        TimeP.setEnabled(true);
        SpinKayuP.setEnabled(true);
        radioButtonMesinP.setEnabled(true);
        radioButtonBSusunP.setEnabled(true);
        SpinMesinP.setEnabled(true);
        SpinSusunP.setEnabled(true);
        SpinTellyP.setEnabled(true);
        SpinSPKP.setEnabled(true);
        SpinSPKAsalP.setEnabled(true);
        SpinProfileP.setEnabled(true);
        DetailTebalP.setEnabled(true);
        DetailLebarP.setEnabled(true);
        DetailPanjangP.setEnabled(true);
        DetailPcsP.setEnabled(true);
        BtnHapusDetailP.setEnabled(true);
        BtnInputDetailP.setEnabled(true);
        SpinBarangJadiP.setEnabled(true);
        CBLemburP.setEnabled(true);
        CBAfkirP.setEnabled(true);
    }

    private void disableForm(){
        DateP.setEnabled(false);
        TimeP.setEnabled(false);
        SpinKayuP.setEnabled(false);
        radioButtonMesinP.setEnabled(false);
        radioButtonBSusunP.setEnabled(false);
        SpinMesinP.setEnabled(false);
        SpinSusunP.setEnabled(false);
        SpinTellyP.setEnabled(false);
        SpinSPKP.setEnabled(false);
        SpinSPKAsalP.setEnabled(false);
        SpinProfileP.setEnabled(false);
        DetailTebalP.setEnabled(false);
        DetailLebarP.setEnabled(false);
        DetailPanjangP.setEnabled(false);
        DetailPcsP.setEnabled(false);
        BtnHapusDetailP.setEnabled(false);
        BtnInputDetailP.setEnabled(false);
        SpinBarangJadiP.setEnabled(false);
        BtnSimpanP.setEnabled(false);
        CBLemburP.setEnabled(false);
        CBAfkirP.setEnabled(false);


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
        DateP.setText("");
        TimeP.setText("");
        NoBarangJadi.setQuery("",false);
        NoWIP.setText("");

        setSpinnerValue(SpinKayuP, "-");
        setSpinnerValue(SpinTellyP, "-");
        setSpinnerValue(SpinSPKP, "-");
        setSpinnerValue(SpinSPKAsalP, "-");
        setSpinnerValue(SpinProfileP, "-");
        setSpinnerValue(SpinMesinP, "-");
        setSpinnerValue(SpinSusunP, "-");

        radioButtonBSusunP.setEnabled(false);
        radioButtonMesinP.setEnabled(false);

    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(NoBarangJadi.getWindowToken(), 0);
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
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }

    private void loadSubmittedData(String noBarangJadi) {
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
                            "h.IdFJProfile, " +
                            "p.IdMesin, " +
                            "m.NamaMesin, " +
                            "s.NoBongkarSusun, " +
                            "f.Profile, " +
                            "h.IdJenisKayu, " +
                            "k.Jenis, " +
                            "h.IdBarangJadi, " +
                            "j.NamaBarangJadi, " +
                            "h.IsLembur, " +
                            "h.IsReject " +
                            "FROM BarangJadi_h h " +
                            "LEFT JOIN PackingProduksiOutput o ON h.NoBJ = o.NoBJ " +
                            "LEFT JOIN PackingProduksi_h p ON o.NoProduksi = p.NoProduksi " +
                            "LEFT JOIN BongkarSusunOutputBarangJadi s ON h.NoBJ = s.NoBJ " +
                            "LEFT JOIN MstMesin m ON p.IdMesin = m.IdMesin " +
                            "LEFT JOIN MstOrgTelly t ON h.IdOrgTelly = t.IdOrgTelly " +
                            "LEFT JOIN MstFJProfile f ON h.IdFJProfile = f.IdFJProfile " +
                            "LEFT JOIN MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                            "LEFT JOIN MstBarangJadi j ON h.IdBarangJadi = j.IdBarangJadi " +
                            "WHERE h.NoBJ = ?";

                    // Query untuk detail
                    String queryDetail = "SELECT Tebal, Lebar, Panjang, JmlhBatang " +
                            "FROM BarangJadi_d " +
                            "WHERE NoBJ = ? " +
                            "ORDER BY NoUrut";

                    // Menggunakan try-with-resources untuk header
                    try (PreparedStatement stmt = connection.prepareStatement(queryHeader)) {
                        stmt.setString(1, noBarangJadi);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                // Mengambil data header
                                final String noProduksi = rs.getString("NoProduksi") != null ? rs.getString("NoProduksi") : "-";
                                final String dateCreate = rs.getString("DateCreate") != null ? rs.getString("DateCreate") : "-";
                                final String jam = rs.getString("Jam") != null ? rs.getString("Jam") : "-";
                                final String namaOrgTelly = rs.getString("NamaOrgTelly") != null ? rs.getString("NamaOrgTelly") : "-";
                                final String noSPK = rs.getString("NoSPK") != null ? rs.getString("NoSPK") : "-";
                                final String noSPKAsal = rs.getString("NoSPKAsal") != null ? rs.getString("NoSPKAsal") : "-";
                                final String namaMesin = rs.getString("NamaMesin") != null ? rs.getString("NamaMesin") : "-";
                                final String noBongkarSusun = rs.getString("NoBongkarSusun") != null ? rs.getString("NoBongkarSusun") : "-";
                                final String namaProfile = rs.getString("Profile") != null ? rs.getString("Profile") : "-";
                                final String namaKayu = rs.getString("Jenis") != null ? rs.getString("Jenis") : "-";
                                final String namaBarangJadi = rs.getString("NamaBarangJadi") != null ? rs.getString("NamaBarangJadi") : "-";
                                final int isLembur = rs.getInt("IsLembur");
                                final int isReject = rs.getInt("IsReject");


                                // Mengambil data detail
                                try (PreparedStatement stmtDetail = connection.prepareStatement(queryDetail)) {
                                    stmtDetail.setString(1, noBarangJadi);
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
                                            radioButtonMesinP.setChecked(true);
                                            radioButtonBSusunP.setEnabled(false);
                                        } else {
                                            radioButtonBSusunP.setChecked(true);
                                            radioButtonMesinP.setEnabled(false);
                                        }
                                        // Update header fields
                                        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                        Date date = inputDateFormat.parse(dateCreate);
                                        String formattedDate = outputDateFormat.format(date);

                                        DateP.setText(formattedDate);
                                        TimeP.setText(jam);
                                        setSpinnerValue(SpinTellyP, namaOrgTelly);
                                        setSpinnerValue(SpinSPKP, noSPK);
                                        setSpinnerValue(SpinSPKAsalP, noSPKAsal);
                                        setSpinnerValue(SpinKayuP, namaKayu);
                                        setSpinnerValue(SpinProfileP, namaProfile);
                                        setSpinnerValue(SpinMesinP, namaMesin + " - " + noProduksi);
                                        setSpinnerValue(SpinSusunP, noBongkarSusun);
                                        setSpinnerValue(SpinBarangJadiP, namaBarangJadi);
                                        CBAfkirP.setChecked(isReject == 1);
                                        CBLemburP.setChecked(isLembur == 1);

                                        // Update tabel detail
                                        updateTableFromTemporaryData();
                                        m3();
                                        jumlahpcs();

                                        Toast.makeText(getApplicationContext(),
                                                "Data berhasil dimuat",
                                                Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {

                                        Log.e("UI Update Error", "Error updating UI: " + e.getMessage());
                                        Toast.makeText(getApplicationContext(),
                                                "Gagal memperbarui tampilan",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(() -> {

//                                    Toast.makeText(getApplicationContext(),
//                                            "Data tidak ditemukan untuk NoBarangJadi: " + noBarangJadi,
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
    private void checkHasBeenPrinted(String noBarangJadi, Packing.HasBeenPrintedCallback callback) {
        new Thread(() -> {
            int hasBeenPrintedValue = -1;
            boolean existsInH = false;
            boolean existsInD = false;
            Connection connection = null;

            try {
                // Mendapatkan koneksi dari method ConnectionClass
                connection = ConnectionClass();
                if (connection != null) {

                    String queryCheckH = "SELECT HasBeenPrinted FROM BarangJadi_h WHERE NoBJ = ?";
                    String queryCheckD = "SELECT 1 FROM BarangJadi_d WHERE NoBJ = ?";

                    // Cek keberadaan di s4s_h
                    try (PreparedStatement stmtH = connection.prepareStatement(queryCheckH)) {
                        stmtH.setString(1, noBarangJadi);
                        try (ResultSet rsH = stmtH.executeQuery()) {
                            if (rsH.next()) {
                                hasBeenPrintedValue = rsH.getInt("HasBeenPrinted");
                                existsInH = true; // Data ditemukan di s4s_h
                            }
                        }
                    }

                    // Cek keberadaan di s4s_d
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

            runOnUiThread(() -> {
                if (!finalIsAvailable) {
                    // Data tidak ditemukan di kedua tabel
                    Toast.makeText(getApplicationContext(), "Data tidak tersedia", Toast.LENGTH_SHORT).show();
                    callback.onResult(-1); // Indikasikan gagal
                } else {
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
                    String query = "UPDATE BarangJadi_h SET HasBeenPrinted = COALESCE(HasBeenPrinted, 0) + 1 WHERE NoBJ = ?";
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

    private class SearchAllDataTask extends AsyncTask<String, Void, Boolean> {
        private String noBarangJadi;

        public SearchAllDataTask(String noBarangJadi) {
            this.noBarangJadi = noBarangJadi;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("SearchAllDataTask", "Searching for NoBarangJadi: " + noBarangJadi);
            Connection con = ConnectionClass();
            boolean isDataFound = false;

            if (con != null) {
                try {
                    String query = "SELECT h.DateCreate, h.Jam, " +
                            "d.Lebar, d.Panjang, d.Tebal, d.JmlhBatang, d.NoUrut, " +
                            "h.IsReject, h.IsLembur " +
                            "FROM dbo.BarangJadi_h AS h " +
                            "INNER JOIN dbo.BarangJadi_d AS d ON h.NoBJ = d.NoBJ " +
                            "WHERE h.NoBJ = ?";

                    Log.d("SearchAllDataTask", "Preparing statement: " + query);
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarangJadi);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        final String dateCreate = rs.getString("DateCreate");
                        final int no = rs.getInt("NoUrut");
                        final String jam = rs.getString("Jam");
                        final int lebar = rs.getInt("Lebar");
                        final int panjang = rs.getInt("Panjang");
                        final int tebal = rs.getInt("Tebal");
                        final int jmlhBatang = rs.getInt("JmlhBatang");
                        final boolean isReject = rs.getInt("IsReject") == 1;
                        final boolean isLembur = rs.getInt("IsLembur") == 1;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                NoBarangJadi.setQuery(noBarangJadi, true);
                                DateP.setText(dateCreate != null ? dateCreate : "");
                                TimeP.setText(jam != null ? jam : "");
                                CBAfkirP.setChecked(isReject);
                                CBLemburP.setChecked(isLembur);

                                m3();
                                jumlahpcs();
                            }
                        });

                        isDataFound = true;
                    } else {
                        Log.e("SearchAllDataTask", "No data found for NoBarangJadi: " + noBarangJadi);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (SQLException e) {
                    Log.e("Database Error", "SQL Exception: " + e.getMessage());
                    if (con != null) {
                        try {
                            con.close();
                        } catch (SQLException e1) {
                            Log.e("Connection Close Error", e1.getMessage());
                        }
                    }
                } catch (Exception e) {
                    Log.e("General Error", e.getMessage());
                    if (con != null) {
                        try {
                            con.close();
                        } catch (SQLException e1) {
                            Log.e("Connection Close Error", e1.getMessage());
                        }
                    }
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }

            return isDataFound;
        }
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

    private void addDataDetail(String noBarangJadi) {
        String tebal = DetailTebalP.getText().toString();
        String panjang = DetailPanjangP.getText().toString();
        String lebar = DetailLebarP.getText().toString();
        String pcs = DetailPcsP.getText().toString();

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
            DetailTebalP.setText("");
            DetailPanjangP.setText("");
            DetailLebarP.setText("");
            DetailPcsP.setText("");

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
        if (DetailTebalP != null) {
            DetailTebalP.setText("");
        }
        if (DetailLebarP != null) {
            DetailLebarP.setText("");
        }
        if (DetailPanjangP != null) {
            DetailPanjangP.setText("");
        }
        if (DetailPcsP != null) {
            DetailPcsP.setText("");
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
        NoBarangJadi.setQuery("", false);
        M3P.setText("");
        JumlahPcsP.setText("");
        NoWIP.setText("");
        CBAfkirP.setChecked(false);
        CBLemburP.setChecked(false);
        SpinKayuP.setSelection(0);
        SpinTellyP.setSelection(0);
        SpinSPKP.setSelection(0);
        SpinSPKAsalP.setSelection(0);
        SpinProfileP.setSelection(0);
        SpinMesinP.setEnabled(false);
        SpinSusunP.setEnabled(false);
        RadioGroupP.clearCheck();
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
            TextView M3TextView = findViewById(R.id.M3P);
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

        int totalPcs = 0;

        for (int i = 1; i < childCount; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            TextView pcsTextView = (TextView) row.getChildAt(4); // Indeks pcs

            String pcsString = pcsTextView.getText().toString().replace(",", "");
            int pcs = Integer.parseInt(pcsString);
            totalPcs += pcs;
        }

        JumlahPcsP.setText(String.valueOf(totalPcs));
    }


    private void setCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        SimpleDateFormat saveFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        DateP.setText(currentDate);
        rawDate = saveFormat.format(new Date());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        TimeP.setText(currentTime);

        new LoadMesinTask().execute(currentDate);
        new LoadSusunTask().execute(currentDate);
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

                    DateP.setText(formattedDate);

                    new LoadMesinTask().execute(rawDate);
                    new LoadSusunTask().execute(rawDate);

                } catch (Exception e) {
                    e.printStackTrace();
                    DateP.setText("Invalid Date");
                }
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        int hour = calendarP.get(Calendar.HOUR_OF_DAY);
        int minute = calendarP.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Packing.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                calendarP.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendarP.set(Calendar.MINUTE, selectedMinute);

                SimpleDateFormat timeFormat = new SimpleDateFormat(" HH:mm:ss", Locale.getDefault());
                String updatedTime = timeFormat.format(calendarP.getTime());
                TimeP.setText(updatedTime);
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
                        .setFontSize(9)
                        .setMargin(0)
                        .setMultipliedLeading(1.2f)
                        .setTextAlignment(TextAlignment.LEFT));

        // Colon Cell
        Cell colonCell = new Cell()
                .setBorder(Border.NO_BORDER)
                .add(new Paragraph(":")
                        .setFont(font)
                        .setFontSize(9)
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
                .setFontSize(9)
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
            float centerY = height / 2;

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

    private Uri createPdf(String noBarangJadi, String jenisKayu, String date, String time, String tellyBy, String mesinSusun, String noSPK, String noSPKasal, List<DataRow> temporaryDataListDetail, String jumlahPcs, String m3, int printCount, String namaBJ) throws IOException {
        // Validasi parameter wajib
        if (noBarangJadi == null || noBarangJadi.trim().isEmpty()) {
            throw new IOException("Nomor Packing tidak boleh kosong");
        }

        if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
            throw new IOException("Data tidak boleh kosong");
        }

        // Validasi dan set default value untuk parameter opsional
        noBarangJadi = (noBarangJadi != null) ? noBarangJadi.trim() : "-";
        jenisKayu = (jenisKayu != null) ? jenisKayu.trim() : "-";
        date = (date != null) ? date.trim() : "-";
        time = (time != null) ? time.trim() : "-";
        tellyBy = (tellyBy != null) ? tellyBy.trim() : "-";
        noSPK = (noSPK != null) ? noSPK.trim() : "-";
        jumlahPcs = (jumlahPcs != null) ? jumlahPcs.trim() : "-";
        m3 = (m3 != null) ? m3.trim() : "-";
        namaBJ = (namaBJ != null) ? namaBJ.trim() : "-";

        Uri pdfUri = null;
        ContentResolver resolver = getContentResolver();
        String fileName = "S4S_" + noBarangJadi + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".pdf";
        String relativePath = Environment.DIRECTORY_DOWNLOADS;

        try {
            // Hapus file yang sudah ada jika perlu
            deleteExistingPdf(fileName, relativePath);
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
                Paragraph judul = new Paragraph("LABEL PACKING")
                        .setUnderline()
                        .setBold()
                        .setFontSize(8)
                        .setTextAlignment(TextAlignment.CENTER);

                // Hitung lebar yang tersedia
                float pageWidth = PageSize.A6.getWidth() - 20;
                float[] mainColumnWidths = new float[]{pageWidth * 0.4f, pageWidth * 0.6f};

                Table mainTable = new Table(mainColumnWidths)
                        .setWidth(pageWidth)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setMarginTop(10)
                        .setBorder(Border.NO_BORDER);

                float[] infoColumnWidths = new float[]{60, 5, 80};

                // Buat tabel untuk kolom kiri
                Table leftColumn = new Table(infoColumnWidths)
                        .setWidth(pageWidth * 0.4f - 5)
                        .setBorder(Border.NO_BORDER);

                // Isi kolom kiri
                addInfoRow(leftColumn, "No BJ", noBarangJadi, timesNewRoman);
                addInfoRow(leftColumn, "J. Kayu", jenisKayu, timesNewRoman);
                addInfoRow(leftColumn, "Nama BJ", namaBJ, timesNewRoman);
//                addInfoRow(leftColumn, "Fisik", "-", timesNewRoman);

                // Buat tabel untuk kolom kanan
                Table rightColumn = new Table(infoColumnWidths)
                        .setWidth(pageWidth * 0.6f)
                        .setMarginLeft(20)
                        .setBorder(Border.NO_BORDER);

                // Isi kolom kanan
                addInfoRow(rightColumn, "Tanggal", date + " (" + time + ")", timesNewRoman);
                addInfoRow(rightColumn, "Telly", tellyBy, timesNewRoman);
//                addInfoRow(rightColumn, "Mesin", mesinSusun, timesNewRoman);
                addInfoRow(rightColumn, "No SPK", noSPK, timesNewRoman);

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
                float[] width = {60f, 60f, 60f, 60f};
                Table table = new Table(width)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setMarginTop(10)
                        .setFontSize(8);

                // Header tabel
                String[] headers = {"Tebal (mm)", "Lebar (mm)", "Panjang (mm)", "Pcs"};
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

                    table.addCell(new Cell().add(new Paragraph(tebal).setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                    table.addCell(new Cell().add(new Paragraph(lebar).setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                    table.addCell(new Cell().add(new Paragraph(panjang).setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                    table.addCell(new Cell().add(new Paragraph(pcs).setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                }

                // Detail Pcs, Ton, M3
                float[] columnWidths = {60f, 5f, 70f};
                Table sumTable = new Table(columnWidths)
                        .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                        .setMarginTop(10)
                        .setFontSize(10)
                        .setBorder(Border.NO_BORDER);

                sumTable.addCell(new Cell().add(new Paragraph("Jumlah Pcs")).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));
                sumTable.addCell(new Cell().add(new Paragraph(":")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                sumTable.addCell(new Cell().add(new Paragraph(String.valueOf(jumlahPcs))).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));

                sumTable.addCell(new Cell().add(new Paragraph("m3")).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));
                sumTable.addCell(new Cell().add(new Paragraph(":")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                sumTable.addCell(new Cell().add(new Paragraph(String.valueOf(m3))).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));

                Paragraph qrCodeIDbottom = new Paragraph(noBarangJadi).setTextAlignment(TextAlignment.RIGHT).setFontSize(10).setMargins(-10, 30, 0, 0).setFont(timesNewRoman);

                BarcodeQRCode qrCode = new BarcodeQRCode(noBarangJadi);
                PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);

                Image qrCodeInputLeft = new Image(qrCodeObject).setWidth(100).setHorizontalAlignment(HorizontalAlignment.LEFT);
                Image qrCodeInputRight = new Image(qrCodeObject).setWidth(100).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                Image qrCodeBottomImage = new Image(qrCodeObject).setWidth(100).setHorizontalAlignment(HorizontalAlignment.RIGHT).setMargins(-10, 0, 0, 0);

                Paragraph bottomLine = new Paragraph("-----------------------------------------------------------------------------------------------------").setTextAlignment(TextAlignment.CENTER).setFontSize(8).setMargins(0, 0, 0, 15).setFont(timesNewRoman);
                Paragraph outputTextBottom = new Paragraph("Output").setTextAlignment(TextAlignment.RIGHT).setFontSize(10).setMargins(25, 34, 0, 0).setFont(timesNewRoman);

                float[] columnWidthsQR = {140f, 140f};
                Table qrTable = new Table(columnWidthsQR)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setMarginTop(25)
                        .setFontSize(10)
                        .setBorder(Border.NO_BORDER);

                qrTable.addCell(new Cell().add(new Paragraph("Input")).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER).setPaddings(0, 0, -10, 40));
                qrTable.addCell(new Cell().add(new Paragraph("Input")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setPaddings(0, 40, -10, 0));

                qrTable.addCell(new Cell().add(qrCodeInputLeft).setBorder(Border.NO_BORDER));
                qrTable.addCell(new Cell().add(qrCodeInputRight).setBorder(Border.NO_BORDER));

                qrTable.addCell(new Cell().add(new Paragraph(String.valueOf(noBarangJadi))).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER).setPaddings(-10,0,0,33));
                qrTable.addCell(new Cell().add(new Paragraph(String.valueOf(noBarangJadi))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setPaddings(-10,33,0,0));


                Paragraph lemburTextInput = new Paragraph("Lembur").setTextAlignment(TextAlignment.LEFT).setFontSize(10).setMargins(10, 0, 0, 10).setFont(timesNewRoman);
                Paragraph afkirText = new Paragraph("Reject").setTextAlignment(TextAlignment.LEFT).setFontSize(10).setMargins(-30, 0, 0, 10).setFont(timesNewRoman);
                Paragraph lemburTextOutput = new Paragraph("Lembur").setTextAlignment(TextAlignment.LEFT).setFontSize(10).setMargins(-40, 0, 0, 20).setFont(timesNewRoman);

                // Tambahkan semua elemen ke dokumen
                document.add(judul);
                if (printCount > 1) {
                    addTextDitheringWatermark(pdfDocument, timesNewRoman);
                }

                document.add(mainTable);
                document.add(table);
                document.add(sumTable);

                if(CBAfkirP.isChecked()){
                    document.add(afkirText);
                }

                if(printCount % 2 != 0) {
                    document.add(outputTextBottom);
                    document.add(qrCodeBottomImage);
                    document.add(qrCodeIDbottom);
                    if(CBLemburP.isChecked()){
                        document.add(lemburTextOutput);
                    }
                }
                else{
                    document.add(qrTable);
                    if(CBLemburP.isChecked()){
                        document.add(lemburTextInput);
                    }
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
//            if (success) {
//                Toast.makeText(Packing.this, "Data berhasil disimpan.", Toast.LENGTH_SHORT).show();
//            }
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
//            if (success) {
//                Toast.makeText(Packing.this, "Data berhasil disimpan ke database.", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(Packing.this, "Gagal menyimpan data ke database.", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    private class UpdateDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private String noBarangJadi, dateCreate, time, idTelly, noSPK, noSPKasal, idJenisKayu, idFJProfile, idBarangJadi;
        private int isReject, isLembur;

        public UpdateDatabaseTask(String noBarangJadi, String dateCreate, String time,
                                  String idTelly, String noSPK, String noSPKasal,
                                  String idJenisKayu, String idFJProfile,
                                  int isReject, int isLembur, String idBarangJadi) {
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
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    // Log untuk debugging
                    Log.d("UpdateDatabase", "Starting update for NoBJ: " + noBarangJadi);

                    // Perbaiki query - tambahkan koma setelah NoSPKAsal
                    String query = "UPDATE dbo.BarangJadi_h SET " +
                            "DateCreate = ?, " +
                            "Jam = ?, " +
                            "IdOrgTelly = ?, " +
                            "NoSPK = ?, " +
                            "NoSPKAsal = ?, " +
                            "IdFJProfile = ?, " +
                            "IdJenisKayu = ?, " +
                            "IdWarehouse = ?, " +
                            "IsReject = ?, " +
                            "IsLembur = ?, " +
                            "IdBarangJadi = ? " +
                            "WHERE NoBJ = ?";

                    PreparedStatement ps = con.prepareStatement(query);

                    // Set nilai parameter dengan benar dan lengkap
                    ps.setString(1, dateCreate);
                    ps.setString(2, time);
                    ps.setString(3, idTelly);
                    ps.setString(4, noSPK);
                    ps.setString(5, noSPKasal);
                    ps.setString(6, idFJProfile);
                    ps.setString(7, idJenisKayu);
                    ps.setInt(8, 11);
                    ps.setInt(9, isReject);
                    ps.setInt(10, isLembur);
                    ps.setString(11, idBarangJadi);
                    ps.setString(12, noBarangJadi);

                    // Log parameter values untuk debugging
                    Log.d("UpdateDatabase", "Parameters: " +
                            "NoBJ=" + noBarangJadi +
                            ", DateCreate=" + dateCreate +
                            ", Jam=" + time +
                            ", IdTelly=" + idTelly +
                            ", NoSPK=" + noSPK +
                            ", NoSPKAsal=" + noSPKasal +
                            ", IdFJProfile=" + idFJProfile +
                            ", IdJenisKayu=" + idJenisKayu);

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

//    private class UpdateNoSTAsalTask extends AsyncTask<Void, Void, Boolean> {
//        private String noBarangJadi;
//        private String noWIP;
//        private String errorMessage = null;
//
//        public UpdateNoSTAsalTask(String noBarangJadi, String noWIP) {
//            this.noBarangJadi = noBarangJadi;
//            this.noWIP = noWIP;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//            Connection con = ConnectionClass();
//            if (con != null) {
//                try {
//                    Log.d("UpdateNoSTAsalTask", "Checking and updating NoSTAsal for NoBJ: " + noBarangJadi);
//
//                    String checkQuery = "SELECT NoSTAsal FROM dbo.BarangJadi_h WHERE NoSTAsal = ?";
//                    PreparedStatement checkPs = con.prepareStatement(checkQuery);
//                    checkPs.setString(1, noWIP);
//                    ResultSet rs = checkPs.executeQuery();
//
//                    if (!rs.next()) {
//                        errorMessage = "NoSTAsal tidak ditemukan di tabel ST_h.";
//                        Log.e("UpdateNoSTAsalTask", errorMessage);
//                        return false;
//                    }
//
//                    String selectQuery = "SELECT NoSTAsal FROM dbo.BarangJadi_h WHERE NoBJ = ?";
//                    PreparedStatement selectPs = con.prepareStatement(selectQuery);
//                    selectPs.setString(1, noBarangJadi);
//                    ResultSet selectRs = selectPs.executeQuery();
//
//                    if (selectRs.next()) {
//                        String currentNoSTAsal = selectRs.getString("NoSTAsal");
//                        if (currentNoSTAsal == null) {
//                            String updateQuery = "UPDATE dbo.BarangJadi_h SET NoSTAsal = ? WHERE NoBJ = ?";
//                            PreparedStatement updatePs = con.prepareStatement(updateQuery);
//                            updatePs.setString(1, noWIP);
//                            updatePs.setString(2, noBarangJadi);
//
//                            int rowsUpdated = updatePs.executeUpdate();
//                            updatePs.close();
//                            Log.d("UpdateNoSTAsalTask", "Rows updated: " + rowsUpdated);
//                            return rowsUpdated > 0;
//                        } else {
//                            errorMessage = "NoSTAsal sudah memiliki nilai: " + currentNoSTAsal;
//                            Log.e("UpdateNoSTAsalTask", errorMessage);
//                            return false;
//                        }
//                    } else {
//                        errorMessage = "NoBJ tidak ditemukan di tabel S4S_h.";
//                        Log.e("UpdateNoSTAsalTask", errorMessage);
//                        return false;
//                    }
//                } catch (SQLException e) {
//                    errorMessage = "SQL Error: " + e.getMessage();
//                    Log.e("Database Error", errorMessage);
//                    return false;
//                } finally {
//                    try {
//                        con.close();
//                    } catch (SQLException e) {
//                        Log.e("Connection Close Error", e.getMessage());
//                    }
//                }
//            } else {
//                errorMessage = "Koneksi ke database gagal.";
//                Log.e("Connection Error", errorMessage);
//                return false;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Boolean success) {
//            if (success) {
//                Toast.makeText(Packing.this, "NoSTAsal berhasil diperbarui.", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(Packing.this, "Gagal memperbarui NoSTAsal: " + errorMessage, Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    private class SetAndSaveNoBJTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            String newNoBarangJadi = null;
            if (con != null) {
                try {
                    String query = "SELECT MAX(NoBJ) FROM dbo.BarangJadi_h";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        String lastNoBarangJadi = rs.getString(1);

                        if (lastNoBarangJadi != null && lastNoBarangJadi.startsWith("I.")) {
                            String numericPart = lastNoBarangJadi.substring(2);
                            int numericValue = Integer.parseInt(numericPart);
                            int newNumericValue = numericValue + 1;

                            newNoBarangJadi = "I." + String.format("%06d", newNumericValue);
                        }
                    }

                    rs.close();
                    ps.close();

                    if (newNoBarangJadi != null) {
                        String insertQuery = "INSERT INTO dbo.BarangJadi_h (NoBJ) VALUES (?)";
                        PreparedStatement insertPs = con.prepareStatement(insertQuery);
                        insertPs.setString(1, newNoBarangJadi);
                        insertPs.executeUpdate();
                        insertPs.close();
                    }

                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return newNoBarangJadi;
        }

        @Override
        protected void onPostExecute(String newNoBarangJadi) {
            if (newNoBarangJadi != null) {
                NoBarangJadi.setQuery(newNoBarangJadi, true);
                NoBarangJadi.setVisibility(View.GONE);
                NoBarangJadi_display.setVisibility(View.VISIBLE);
                NoBarangJadi_display.setText(newNoBarangJadi);
                NoBarangJadi_display.setEnabled(false);
                Toast.makeText(Packing.this, "NoBJ berhasil diatur dan disimpan.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Error", "Failed to set or save NoBJ.");
                Toast.makeText(Packing.this, "Gagal mengatur atau menyimpan NoBJ.", Toast.LENGTH_SHORT).show();
            }
        }
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

            ArrayAdapter<JenisKayu> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, jenisKayuList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinKayuP.setAdapter(adapter);
            SpinKayuP.setSelection(0);
        }
    }
    public class LoadJenisKayuTask2 extends AsyncTask<String, Void, List<JenisKayu>> {
        private String noBarangJadi;

        public LoadJenisKayuTask2(String noBarangJadi) {
            this.noBarangJadi = noBarangJadi;
        }

        @Override
        protected List<JenisKayu> doInBackground(String... params) {
            List<JenisKayu> jenisKayuList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT j.IdJenisKayu, j.Jenis " +
                            "FROM dbo.MstJenisKayu AS j " +
                            "INNER JOIN dbo.BarangJadi_h AS h ON h.IdJenisKayu = j.IdJenisKayu " +
                            "WHERE h.NoBJ = ? AND j.enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarangJadi);

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
                ArrayAdapter<JenisKayu> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, jenisKayuList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinKayuP.setAdapter(adapter);
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
            ArrayAdapter<Telly> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, tellyList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Set adapter ke spinner
            SpinTellyP.setAdapter(adapter);
        }
    }


    private class LoadTellyTask2 extends AsyncTask<String, Void, List<Telly>> {
        private String noBarajangJadi;

        public LoadTellyTask2(String noBarajangJadi) {
            this.noBarajangJadi = noBarajangJadi;
        }

        @Override
        protected List<Telly> doInBackground(String... params) {
            List<Telly> tellyList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT t.IdOrgTelly, t.NamaOrgTelly " +
                            "FROM dbo.MstOrgTelly AS t " +
                            "INNER JOIN dbo.BarangJadi_h AS h ON h.IdOrgTelly = t.IdOrgTelly " +
                            "WHERE h.NoBJ = ? AND t.enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarajangJadi);

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
                ArrayAdapter<Telly> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, tellyList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinTellyP.setAdapter(adapter);
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

            ArrayAdapter<SPK> adapter = new ArrayAdapter<>(Packing.this,
                    android.R.layout.simple_spinner_item, spkList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinSPKP.setAdapter(adapter);
            SpinSPKP.setSelection(0);
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

            ArrayAdapter<SPKAsal> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, spkAsalList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinSPKAsalP.setAdapter(adapter);

            SpinSPKAsalP.setSelection(0);
        }
    }
    
    private class LoadSPKTask2 extends AsyncTask<Void, Void, List<SPK>> {
        private String noBarajangJadi;

        public LoadSPKTask2(String noBarajangJadi) {
            this.noBarajangJadi = noBarajangJadi;
        }

        @Override
        protected List<SPK> doInBackground(Void... params) {
            List<SPK> spkList = new ArrayList<>();
            Connection con = ConnectionClass(); // Ensure this method establishes a database connection

            if (con != null) {
                try {
                    String query = "SELECT NoSPK FROM dbo.BarangJadi_h WHERE NoBJ = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarajangJadi);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String noSPK = rs.getString("NoSPK");

                        SPK spk = new SPK(noSPK);
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
                ArrayAdapter<SPK> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, spkList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSPKP.setAdapter(adapter);

                SpinSPKP.setEnabled(true);
            } else {
                Log.e("Error", "No SPK data found for the provided NoBJ.");
                SpinSPKP.setAdapter(null);
                SpinSPKP.setEnabled(false);
                Toast.makeText(Packing.this, "Tidak ada data SPK yang ditemukan.", Toast.LENGTH_SHORT).show();
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

            ArrayAdapter<Profile> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, profileList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinProfileP.setAdapter(adapter);
            SpinProfileP.setSelection(0);
        }
    }

    private class LoadProfileTask2 extends AsyncTask<String, Void, List<Profile>> {
        private String noBarajangJadi;

        public LoadProfileTask2(String noBarajangJadi) {
            this.noBarajangJadi = noBarajangJadi;
        }

        @Override
        protected List<Profile> doInBackground(String... voids) {
            List<Profile> profileList = new ArrayList<>();
            Connection con = ConnectionClass(); // Assumes this method exists to establish a DB connection

            if (con != null) {
                try {
                    String query = "SELECT p.Profile, p.IdFJProfile " +
                            "FROM dbo.MstFJProfile AS p " +
                            "INNER JOIN dbo.BarangJadi_h AS h ON h.IdFJProfile = p.IdFJProfile " +
                            "WHERE h.NoBJ = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarajangJadi);

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
                ArrayAdapter<Profile> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, profileList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinProfileP.setAdapter(adapter);
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
                    String query =  "SELECT IdBarangJadi, NamaBarangJadi " +
                            "FROM dbo.MstBarangJadi " +
                            "WHERE enable = 1 " +
                            "ORDER BY NamaBarangJadi ASC";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String namaBarangJadi = rs.getString("NamaBarangJadi");
                        String idBarangJadi = rs.getString("IdBarangJadi");
                        Fisik fisikObj = new Fisik(namaBarangJadi, idBarangJadi);
                        fisikList.add(fisikObj);
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
            Fisik dummyFisik = new Fisik("PILIH", "");
            fisikList.add(0, dummyFisik);

            ArrayAdapter<Fisik> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, fisikList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinBarangJadiP.setAdapter(adapter);
            SpinBarangJadiP.setSelection(0);
        }
    }

//    private class LoadFisikTask2 extends AsyncTask<String, Void, List<Fisik>> {
//        private String noBarangJadi;
//
//        public LoadFisikTask2(String noBarangJadi) {
//            this.noBarangJadi = noBarangJadi;
//        }
//
//        @Override
//        protected List<Fisik> doInBackground(String... params) {
//            List<Fisik> fisikList = new ArrayList<>();
//            Connection con = ConnectionClass();
//            if (con != null) {
//                try {
//                    String query = "SELECT mw.NamaWarehouse " +
//                            "FROM dbo.MstWarehouse mw " +
//                            "INNER JOIN dbo.BarangJadi_h bj ON mw.IdWarehouse = bj.IdWarehouse " +
//                            "WHERE bj.NoBJ = ?";
//
//                    PreparedStatement ps = con.prepareStatement(query);
//                    ps.setString(1, noBarangJadi);
//
//                    ResultSet rs = ps.executeQuery();
//
//                    while (rs.next()) {
//                        String namaWarehouse = rs.getString("NamaWarehouse");
//                        Fisik fisik = new Fisik(namaWarehouse);
//                        fisikList.add(fisik);
//                    }
//
//                    rs.close();
//                    ps.close();
//                    con.close();
//                } catch (Exception e) {
//                    Log.e("Database Error", "Error during query execution: " + e.getMessage());
//                }
//            } else {
//                Log.e("Connection Error", "Failed to connect to the database.");
//            }
//
//            return fisikList;
//        }
//
//        @Override
//        protected void onPostExecute(List<Fisik> fisikList) {
//            if (!fisikList.isEmpty()) {
//                ArrayAdapter<Fisik> adapter = new ArrayAdapter<>(Packing.this,
//                        android.R.layout.simple_spinner_item, fisikList);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                SpinBarangJadiP.setAdapter(adapter);
//            } else {
//                Log.e("Error", "No warehouse found.");
//                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(Packing.this,
//                        android.R.layout.simple_spinner_item, new String[]{"Tidak ada Fisik"});
//                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                SpinBarangJadiP.setAdapter(emptyAdapter);
//            }
//        }
//    }

    private class LoadMesinTask extends AsyncTask<String, Void, List<Mesin>> {
        @Override
        protected List<Mesin> doInBackground(String... params) {
            List<Mesin> mesinList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String selectedDate = params[0];

                    String query = "SELECT a.IdMesin, b.NamaMesin, a.NoProduksi FROM dbo.PackingProduksi_h a " +
                            "INNER JOIN dbo.MstMesin b ON a.IdMesin = b.IdMesin WHERE Tanggal = ?";
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
                ArrayAdapter<Mesin> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinMesinP.setAdapter(adapter);
            } else {
                SpinMesinP.setAdapter(null);
                Log.e("Error", "Failed to load mesin data.");
            }
        }
    }

    private class LoadMesinTask2 extends AsyncTask<Void, Void, List<Mesin>> {
        private String noBarangJadi;

        public LoadMesinTask2(String noBarangJadi) {
            this.noBarangJadi = noBarangJadi;
        }

        @Override
        protected List<Mesin> doInBackground(Void... params) {
            List<Mesin> mesinList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT b.NoProduksi, d.NamaMesin FROM BarangJadi_h a " +
                            "INNER JOIN PackingProduksiOutput b ON b.NoBJ = a.NoBJ " +
                            "INNER JOIN PackingProduksi_h c ON c.NoProduksi = b.NoProduksi " +
                            "INNER JOIN MstMesin d ON d.IdMesin = c.IdMesin " +
                            "WHERE a.NoBJ = ?";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarangJadi);

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
                ArrayAdapter<Mesin> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinMesinP.setAdapter(adapter);

                radioButtonMesinP.setEnabled(true);
                radioButtonBSusunP.setEnabled(false);
            } else {
                Log.e("Error", "Failed to load mesin data.");
                radioButtonMesinP.setEnabled(false);
                radioButtonBSusunP.setEnabled(false);

                Toast.makeText(Packing.this, "Tidak ada data mesin yang ditemukan.", Toast.LENGTH_SHORT).show();
                SpinMesinP.setAdapter(null);
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
                ArrayAdapter<Susun> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, susunList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSusunP.setAdapter(adapter);
            } else {
                SpinSusunP.setAdapter(null);
                Log.e("Error", "Failed to load susun data");
            }
        }
    }
    private class LoadSusunTask2 extends AsyncTask<Void, Void, List<Susun>> {
        private String noBarangJadi;

        public LoadSusunTask2(String noBarangJadi) {
            this.noBarangJadi = noBarangJadi;
        }

        @Override
        protected List<Susun> doInBackground(Void... params) {
            List<Susun> susunList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT NoBongkarSusun FROM dbo.BongkarSusunOutputBarangJadi WHERE NoBJ = ?"; // Filter by noBarangJadi
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarangJadi);
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
                ArrayAdapter<Susun> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, susunList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSusunP.setAdapter(adapter);

                radioButtonMesinP.setEnabled(false);
                radioButtonBSusunP.setEnabled(true);
            } else {
                Log.e("Error", "Failed to load susun data.");
                radioButtonMesinP.setEnabled(false);
                radioButtonBSusunP.setEnabled(false);

                Toast.makeText(Packing.this, "Tidak ada data susun yang ditemukan.", Toast.LENGTH_SHORT).show();
                SpinSusunP.setAdapter(null);
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
        private String idBarangJadi;
        private String namaBarangJadi;

        public Fisik(String namaBarangJadi, String idBarangJadi) {
            this.namaBarangJadi = namaBarangJadi;
            this.idBarangJadi = idBarangJadi;
        }

        public String getIdBarangJadi() {
            return idBarangJadi;
        }

        public String getNamaBarangJadi() {
            return namaBarangJadi;
        }

        @Override
        public String toString() {
            return namaBarangJadi;
        }
    }

    public class Grade {
        private String idBarangJadi;
        private String namaGrade;

        public Grade(String idBarangJadi, String namaGrade) {
            this.idBarangJadi = idBarangJadi;
            this.namaGrade = namaGrade;
        }

        public String getIdGrade() {
            return idBarangJadi;
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

