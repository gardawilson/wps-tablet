package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

public class FingerJoint extends AppCompatActivity {

    private String username;
    private SearchView NoFJ;
    private EditText DateFJ;
    private EditText TimeFJ;
    private EditText NoSTAFJ;
    private Spinner SpinKayuFJ;
    private Spinner SpinTellyFJ;
    private Spinner SpinSPKFJ;
    private Spinner SpinSPKAsalFJ;
    private Spinner SpinProfileFJ;
    private Spinner SpinFisikFJ;
    private Spinner SpinGradeFJ;
    private Spinner SpinMesinFJ;
    private Spinner SpinSusunFJ;
    private Calendar calendarFJ;
    private RadioGroup radioGroupFJ;
    private RadioButton radioButtonMesinFJ;
    private RadioButton radioButtonBSusunFJ;
    private Button BtnDataBaruFJ;
    private Button BtnSimpanFJ;
    private Button BtnBatalFJ;
    private Button BtnHapusDetailFJ;
    private boolean isDataBaruFJClicked = false;
    private CheckBox CBAfkirFJ;
    private CheckBox CBLemburFJ;
    private Button BtnInputDetailFJ;
    private AutoCompleteTextView DetailLebarFJ;
    private AutoCompleteTextView DetailTebalFJ;
    private AutoCompleteTextView DetailPanjangFJ;
    private EditText DetailPcsFJ;
    private static int currentNumber = 1;
    private Button BtnPrintFJ;
    private TextView M3FJ;
    private TextView JumlahPcsFJ;
    private boolean isCBAfkirFJ, isCBLemburFJ;
    private int rowCount = 0;
    private TableLayout Tabel;
    private boolean isCreateMode = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    private EditText NoFJ_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                new DeleteLatestNoFJTask().execute();

                finish();
            }
        });

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_finger_joint);

        NoSTAFJ = findViewById(R.id.NoSTAFJ);
        NoFJ = findViewById(R.id.NoFJ);
        DateFJ = findViewById(R.id.DateFJ);
        TimeFJ = findViewById(R.id.TimeFJ);
        SpinKayuFJ = findViewById(R.id.SpinKayuFJ);
        SpinTellyFJ = findViewById(R.id.SpinTellyFJ);
        SpinSPKFJ = findViewById(R.id.SpinSPKFJ);
        SpinSPKAsalFJ = findViewById(R.id.SpinSPKAsalFJ);
        SpinProfileFJ = findViewById(R.id.SpinProfileFJ);
        SpinFisikFJ = findViewById(R.id.SpinFisikFJ);
        SpinGradeFJ = findViewById(R.id.SpinGradeFJ);
        calendarFJ = Calendar.getInstance();
        SpinMesinFJ = findViewById(R.id.SpinMesinFJ);
        SpinSusunFJ = findViewById(R.id.SpinSusunFJ);
        radioButtonMesinFJ = findViewById(R.id.radioButtonMesinFJ);
        radioButtonBSusunFJ = findViewById(R.id.radioButtonBSusunFJ);
        BtnDataBaruFJ = findViewById(R.id.BtnDataBaruFJ);
        BtnSimpanFJ = findViewById(R.id.BtnSimpanFJ);
        BtnBatalFJ = findViewById(R.id.BtnBatalFJ);
        BtnHapusDetailFJ = findViewById(R.id.BtnHapusDetailFJ);
        CBLemburFJ = findViewById(R.id.CBLemburFJ);
        CBAfkirFJ = findViewById(R.id.CBAfkirFJ);
        BtnInputDetailFJ = findViewById(R.id.BtnInputDetailFJ);
        DetailPcsFJ = findViewById(R.id.DetailPcsFJ);
        DetailTebalFJ = findViewById(R.id.DetailTebalFJ);
        DetailPanjangFJ = findViewById(R.id.DetailPanjangFJ);
        DetailLebarFJ = findViewById(R.id.DetailLebarFJ);
        BtnPrintFJ = findViewById(R.id.BtnPrintFJ);
        M3FJ = findViewById(R.id.M3FJ);
        JumlahPcsFJ = findViewById(R.id.JumlahPcsFJ);
        Tabel = findViewById(R.id.Tabel);
        radioGroupFJ = findViewById(R.id.radioGroupFJ);
        NoFJ_display = findViewById(R.id.NoFJ_display);

        // Set imeOptions untuk memungkinkan pindah fokus
        DetailTebalFJ.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        DetailLebarFJ.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        DetailPanjangFJ.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        // Menangani aksi 'Enter' pada keyboard
        DetailTebalFJ.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Jika tombol 'Enter' ditekan, pindahkan fokus ke DetailLebarS4S
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Pastikan DetailLebarS4S bisa menerima fokus
                    DetailLebarFJ.requestFocus();
                    return true; // Menunjukkan bahwa aksi sudah ditangani
                }
                return false;
            }
        });

        DetailLebarFJ.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    DetailPanjangFJ.requestFocus();
                    return true;
                }
                return false;
            }
        });

        DetailPanjangFJ.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    DetailPcsFJ.requestFocus();
                    return true;
                }
                return false;
            }
        });

        NoFJ_display.setVisibility(View.GONE);
        disableForm();


        NoFJ.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                    if(!newText.isEmpty()){
                        disableForm();
                        loadSubmittedData(newText);
                        BtnPrintFJ.setEnabled(true);
                    }
                    else{
                        enableForm();
                    }
                }
                return true;
            }
        });


        radioButtonMesinFJ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpinMesinFJ.setEnabled(true);
                    SpinSusunFJ.setEnabled(false);
                }
            }
        });

        radioButtonBSusunFJ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpinSusunFJ.setEnabled(true);
                    SpinMesinFJ.setEnabled(false);
                }
            }
        });

        setCurrentDateTimeFJ();

        BtnDataBaruFJ.setOnClickListener(v -> {
            setCurrentDateTime();
            setCreateMode(true);

            new SetAndSaveNoFJoinTaskFJ().execute();
            new LoadJenisKayuTaskFJ().execute();
            new LoadTellyTaskFJ().execute();
            new LoadSPKTaskFJ().execute();
            new LoadSPKAsalTaskFJ().execute();
            new LoadProfileTaskFJ().execute();
            new LoadFisikTaskFJ().execute();
            new LoadGradeTaskFJ().execute();
            new LoadMesinTaskFJ().execute();
            new LoadSusunTaskFJ().execute();

            BtnSimpanFJ.setEnabled(true);
            BtnBatalFJ.setEnabled(true);
            BtnDataBaruFJ.setEnabled(false);
            BtnPrintFJ.setEnabled(false);

            clearData();
            resetDetailData();
            enableForm();
        });

        BtnSimpanFJ.setOnClickListener(v -> {

            String noFJ = NoFJ.getQuery().toString();
            String dateCreate = DateFJ.getText().toString();
            String time = TimeFJ.getText().toString();

            TellyFJ selectedTelly = (TellyFJ) SpinTellyFJ.getSelectedItem();
            SPKFJ selectedSPK = (SPKFJ) SpinSPKFJ.getSelectedItem();
            SPKAsalFJ selectedSPKasal = (SPKAsalFJ) SpinSPKAsalFJ.getSelectedItem();
            ProfileFJ selectedProfile = (ProfileFJ) SpinProfileFJ.getSelectedItem();
            FisikFJ selectedFisik = (FisikFJ) SpinFisikFJ.getSelectedItem();
            GradeFJ selectedGrade = (GradeFJ) SpinGradeFJ.getSelectedItem();
            JenisKayuFJ selectedJenisKayu = (JenisKayuFJ) SpinKayuFJ.getSelectedItem();
            MesinFJ selectedMesin = (MesinFJ) SpinMesinFJ.getSelectedItem();
            SusunFJ selectedSusun = (SusunFJ) SpinSusunFJ.getSelectedItem();
            RadioGroup radioGroupUOMTblLebar = findViewById(R.id.radioGroupUOMTblLebar);
            RadioGroup radioGroupUOMPanjang = findViewById(R.id.radioGroupUOMPanjang);

            String idGrade = selectedGrade != null ? selectedGrade.getIdGrade() : null;
            String idTelly = selectedTelly != null ? selectedTelly.getIdTelly() : null;
            String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
            String noSPKasal = selectedSPKasal != null ? selectedSPKasal.getNoSPKAsal() : null;
            String idProfile = selectedProfile != null ? selectedProfile.getIdFJProfile() : null;
            String idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;
            String noProduksi = selectedMesin != null ? selectedMesin.getNoProduksi() : null;
            String noBongkarSusun = selectedSusun != null ? selectedSusun.getNoBongkarSusun() : null;
            int isReject = CBAfkirFJ.isChecked() ? 1 : 0;
            int isLembur = CBLemburFJ.isChecked() ? 1 : 0;
            int idUOMTblLebar = radioGroupUOMTblLebar.getCheckedRadioButtonId() == R.id.radioMillimeter ? 1 : 4;
            int idUOMPanjang;
            if (radioGroupUOMPanjang.getCheckedRadioButtonId() == R.id.radioCentimeter) {
                idUOMPanjang = 1;
            } else if (radioGroupUOMPanjang.getCheckedRadioButtonId() == R.id.radioMeter) {
                idUOMPanjang = 2;
            } else {
                idUOMPanjang = 3;
            }


            if (noFJ.isEmpty() || dateCreate.isEmpty() || time.isEmpty() ||
                    selectedTelly == null || selectedTelly.getIdTelly().isEmpty() ||
                    selectedSPK == null || selectedSPK.getNoSPK().equals("PILIH") ||
                    selectedSPKasal == null || selectedSPKasal.getNoSPKAsal().equals("PILIH") ||
                    selectedProfile == null || selectedProfile.getIdFJProfile().isEmpty() ||
                    selectedFisik == null ||
                    selectedGrade == null || selectedGrade.getIdGrade().isEmpty() ||
                    selectedJenisKayu == null || selectedJenisKayu.getIdJenisKayu().isEmpty() ||
                    (!radioButtonMesinFJ.isChecked() && !radioButtonBSusunFJ.isChecked()) ||
                    (radioButtonMesinFJ.isChecked() && (selectedMesin == null || selectedMesin.getNoProduksi().isEmpty())) ||
                    (radioButtonBSusunFJ.isChecked() && (selectedSusun == null || selectedSusun.getNoBongkarSusun().isEmpty())) || temporaryDataListDetail.isEmpty()) {

                Toast.makeText(FingerJoint.this, "Pastikan semua field terisi dengan benar.", Toast.LENGTH_SHORT).show();
                return;
            }
            BtnDataBaruFJ.setEnabled(true);
            BtnPrintFJ.setEnabled(true);

            // Update database utama
            new UpdateDatabaseTaskFJ(
                    noFJ,
                    dateCreate,
                    time,
                    idTelly,
                    noSPK,
                    noSPKasal,
                    idGrade,
                    idJenisKayu,
                    idProfile,
                    isReject,
                    isLembur,
                    idUOMTblLebar,
                    idUOMPanjang
            ).execute();

            // Simpan sesuai pilihan radio button
            if (radioButtonMesinFJ.isChecked() && SpinMesinFJ.isEnabled() && noProduksi != null) {
                new SaveToDatabaseTaskFJ(noProduksi, noFJ).execute();
                for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                    FingerJoint.DataRow dataRow = temporaryDataListDetail.get(i);
                    saveDataDetailToDatabase(noFJ, i + 1,
                            Double.parseDouble(dataRow.tebal),
                            Double.parseDouble(dataRow.lebar),
                            Double.parseDouble(dataRow.panjang),
                            Integer.parseInt(dataRow.pcs));
                }
            } else if (radioButtonBSusunFJ.isChecked() && SpinSusunFJ.isEnabled() && noBongkarSusun != null) {
                new SaveBongkarSusunTaskFJ(noBongkarSusun, noFJ).execute();
                for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                    FingerJoint.DataRow dataRow = temporaryDataListDetail.get(i);
                    saveDataDetailToDatabase(noFJ, i + 1,
                            Double.parseDouble(dataRow.tebal),
                            Double.parseDouble(dataRow.lebar),
                            Double.parseDouble(dataRow.panjang),
                            Integer.parseInt(dataRow.pcs));
                }
            }

            // Update UI
            BtnDataBaruFJ.setEnabled(true);
            BtnPrintFJ.setEnabled(true);
            BtnSimpanFJ.setEnabled(false);
            disableForm();

            Toast.makeText(FingerJoint.this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
        });

        BtnBatalFJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCreateMode(false);
                resetDetailData();
                resetAllForm();
                disableForm();

                new DeleteLatestNoFJTask().execute();

                BtnDataBaruFJ.setEnabled(true);
                BtnSimpanFJ.setEnabled(false);
                NoFJ.setVisibility(View.VISIBLE);
                NoFJ_display.setVisibility(View.GONE);
//                Toast.makeText(FingerJoint.this, "Tampilan telah dikosongkan.", Toast.LENGTH_SHORT).show();
            }
        });


        SpinKayuFJ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Hanya jalankan jika dalam mode create
                if (isCreateMode) {
                    JenisKayuFJ selectedJenisKayu = (JenisKayuFJ) parent.getItemAtPosition(position);
                    String idJenisKayu = selectedJenisKayu.getIdJenisKayu();
                    new LoadGradeTaskFJ().execute(idJenisKayu);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SpinSPKFJ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                                    FingerJoint.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    dimensionData.get("tebal")
                            );
                            ArrayAdapter<String> lebarAdapter = new ArrayAdapter<>(
                                    FingerJoint.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    dimensionData.get("lebar")
                            );
                            ArrayAdapter<String> panjangAdapter = new ArrayAdapter<>(
                                    FingerJoint.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    dimensionData.get("panjang")
                            );

                            // Set adapter untuk masing-masing AutoCompleteTextView
                            DetailTebalFJ.setAdapter(tebalAdapter);
                            DetailLebarFJ.setAdapter(lebarAdapter);
                            DetailPanjangFJ.setAdapter(panjangAdapter);

                            // Set threshold untuk semua AutoCompleteTextView
                            DetailTebalFJ.setThreshold(0);
                            DetailLebarFJ.setThreshold(0);
                            DetailPanjangFJ.setThreshold(0);

                            // Tampilkan status lock
//                            if (isLocked) {
//                                Toast.makeText(FingerJoint.this, "Dimension terkunci", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(FingerJoint.this, "Dimension tidak terkunci", Toast.LENGTH_SHORT).show();
//                            }
                        });
                    }).start();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                DetailTebalFJ.setText("");
                DetailPanjangFJ.setText("");
                DetailLebarFJ.setText("");
                DetailTebalFJ.setAdapter(null);
                DetailPanjangFJ.setAdapter(null);
                DetailLebarFJ.setAdapter(null);
            }
        });


        DateFJ.setOnClickListener(v -> showDatePickerDialog());

        TimeFJ.setOnClickListener(v -> showTimePickerDialog());

        BtnInputDetailFJ.setOnClickListener(v -> {
            // Ambil input dari AutoCompleteTextView
            String noFJ = NoFJ.getQuery().toString();
            String tebal = DetailTebalFJ.getText().toString().trim();
            String lebar = DetailLebarFJ.getText().toString().trim();
            String panjang = DetailPanjangFJ.getText().toString().trim();

            // Ambil data SPK, Jenis Kayu, dan Grade dari Spinner
            SPKFJ selectedSPK = (SPKFJ) SpinSPKFJ.getSelectedItem();
            GradeFJ selectedGrade = (GradeFJ) SpinGradeFJ.getSelectedItem();
            JenisKayuFJ selectedJenisKayu = (JenisKayuFJ) SpinKayuFJ.getSelectedItem();

            String idGrade = selectedGrade != null ? selectedGrade.getIdGrade() : null;
            String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
            String idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;

            // Validasi input kosong
            if (noFJ.isEmpty() || tebal.isEmpty() || lebar.isEmpty() || panjang.isEmpty()) {
                Toast.makeText(FingerJoint.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Jalankan validasi
            new CheckSPKDataTask(noSPK, tebal, lebar, panjang, idJenisKayu, idGrade) {
                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);

                    if (result.equals("SUCCESS")) {
                        // Jika validasi berhasil, tambahkan data ke daftar
                        addDataDetail(noFJ);
                        jumlahpcs();
                        m3();
                        Toast.makeText(FingerJoint.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    } else {
                        // Tampilkan pesan error
                        Toast.makeText(FingerJoint.this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        });

        BtnHapusDetailFJ.setOnClickListener(v -> {
            resetDetailData();

        });

        BtnPrintFJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validasi input
                if (NoFJ.getQuery() == null || NoFJ.getQuery().toString().trim().isEmpty()) {
                    Toast.makeText(FingerJoint.this, "Nomor ST tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validasi apakah ada data untuk dicetak
                if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
                    Toast.makeText(FingerJoint.this, "Tidak ada data untuk dicetak", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cek status HasBeenPrinted di database
                String noFJ = NoFJ.getQuery().toString().trim();
                checkHasBeenPrinted(noFJ, new HasBeenPrintedCallback() {
                    @Override
                    public void onResult(int printCount) {
                        // Menggunakan printCount untuk menentukan jumlah print sebelumnya
                        // Tidak ada logika boolean, hanya menghitung dan menambah nilai HasBeenPrinted

                        try {
                            // Ambil data dari form
                            String mesinSusun;
                            String jenisKayu = SpinKayuFJ.getSelectedItem() != null ? SpinKayuFJ.getSelectedItem().toString().trim() : "";
                            String date = DateFJ.getText() != null ? DateFJ.getText().toString().trim() : "";
                            String time = TimeFJ.getText() != null ? TimeFJ.getText().toString().trim() : "";
                            String tellyBy = SpinTellyFJ.getSelectedItem() != null ? SpinTellyFJ.getSelectedItem().toString().trim() : "";
                            String noSPK = SpinSPKFJ.getSelectedItem() != null ? SpinSPKFJ.getSelectedItem().toString().trim() : "";
                            String noSPKasal = SpinSPKAsalFJ.getSelectedItem() != null ? SpinSPKAsalFJ.getSelectedItem().toString().trim() : "";
                            String grade = SpinGradeFJ.getSelectedItem() != null ? SpinGradeFJ.getSelectedItem().toString().trim() : "";
                            String fisik = SpinFisikFJ.getSelectedItem() != null ? SpinFisikFJ.getSelectedItem().toString().trim() : "";
                            String jumlahPcs = JumlahPcsFJ.getText() != null ? JumlahPcsFJ.getText().toString().trim() : "";
                            String m3 = M3FJ.getText() != null ? M3FJ.getText().toString().trim() : "";
                            if (radioButtonMesinFJ.isChecked()) {
                                mesinSusun = SpinMesinFJ.getSelectedItem() != null ? SpinMesinFJ.getSelectedItem().toString().trim() : "";
                            } else {
                                mesinSusun = SpinSusunFJ.getSelectedItem() != null ? SpinSusunFJ.getSelectedItem().toString().trim() : "";
                            }

                            // Buat PDF dengan parameter printCount
                            Uri pdfUri = createPdf(noFJ, jenisKayu, date, time, tellyBy, mesinSusun, noSPK, noSPKasal, grade,
                                    temporaryDataListDetail, jumlahPcs, m3, printCount, fisik);

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
                                                updatePrintStatus(noFJ); // Update nilai HasBeenPrinted setelah print selesai
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
                                    Toast.makeText(FingerJoint.this,
                                            "Error printing: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error";
                            Toast.makeText(FingerJoint.this,
                                    "Terjadi kesalahan: " + errorMessage,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private Connection ConnectionClass() {
        Connection con = null;
        String ip = "192.168.10.100";
        String port = "1433";
        String username = "sa";
        String password = "Utama1234";
        String databasename = "WPS_Test";

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + databasename + ";User=" + username + ";password=" + password + ";";
            con = DriverManager.getConnection(connectionUrl);
        } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        }

        return con;
    }

    //Method FingerJoint

    private class DeleteLatestNoFJTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            String noFJ = NoFJ_display.getText().toString().trim();
            boolean success = false;
            if (con != null) {
                try {
                    String query = "DELETE FROM dbo.FJ_h WHERE NoFJ = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);
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
//                Toast.makeText(FingerJoint.this, "NoFJ terbaru berhasil dihapus.", Toast.LENGTH_SHORT).show();
//            } else {
//                Log.e("Error", "Failed to delete the latest NoS4S.");
//                Toast.makeText(FingerJoint.this, "Gagal menghapus NoS4S terbaru.", Toast.LENGTH_SHORT).show();
//            }
        }
    }


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

                // Query untuk validasi data
                String query = "SELECT * FROM MstSPK_dFJ WHERE NoSPK = ?";
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
                            idGrade.equals(rs.getString("IdGrade"))) {
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
                    if (!columnMatches(connection, "IdGrade", noSPK, idGrade)) {
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
        String query = "SELECT " + columnName + " FROM MstSPK_dFJ WHERE NoSPK = ?";
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
                String query = "SELECT DISTINCT Tebal, Lebar, Panjang FROM MstSPK_dFJ WHERE NoSPK = ?";
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
                String query = "SELECT LockDimensionFJ FROM MstSPK_h WHERE NoSPK = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, noSPK);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            Integer lockDimension = rs.getInt("LockDimensionFJ");
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
        DateFJ.setEnabled(true);
        TimeFJ.setEnabled(true);
        SpinKayuFJ.setEnabled((true));
        radioButtonMesinFJ.setEnabled(true);
        radioButtonBSusunFJ.setEnabled(true);
        SpinMesinFJ.setEnabled(true);
        SpinSusunFJ.setEnabled(true);
        SpinTellyFJ.setEnabled(true);
        SpinSPKFJ.setEnabled(true);
        SpinSPKAsalFJ.setEnabled(true);
        SpinGradeFJ.setEnabled(true);
        SpinProfileFJ.setEnabled(true);
        DetailTebalFJ.setEnabled(true);
        DetailLebarFJ.setEnabled(true);
        DetailPanjangFJ.setEnabled(true);
        DetailPcsFJ.setEnabled(true);
        BtnHapusDetailFJ.setEnabled(true);
        BtnInputDetailFJ.setEnabled(true);
    }

    private void disableForm(){
        DateFJ.setEnabled(false);
        TimeFJ.setEnabled(false);
        SpinKayuFJ.setEnabled(false);
        radioButtonMesinFJ.setEnabled(false);
        radioButtonBSusunFJ.setEnabled(false);
        SpinMesinFJ.setEnabled(false);
        SpinSusunFJ.setEnabled(false);
        SpinTellyFJ.setEnabled(false);
        SpinSPKFJ.setEnabled(false);
        SpinSPKAsalFJ.setEnabled(false);
        SpinGradeFJ.setEnabled(false);
        SpinProfileFJ.setEnabled(false);
        DetailTebalFJ.setEnabled(false);
        DetailLebarFJ.setEnabled(false);
        DetailPanjangFJ.setEnabled(false);
        DetailPcsFJ.setEnabled(false);
        BtnHapusDetailFJ.setEnabled(false);
        BtnInputDetailFJ.setEnabled(false);
        BtnSimpanFJ.setEnabled(false);

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
        DateFJ.setText("");
        TimeFJ.setText("");
        NoFJ.setQuery("",false);
        NoSTAFJ.setText("");

        setSpinnerValue(SpinKayuFJ, "-");
        setSpinnerValue(SpinTellyFJ, "-");
        setSpinnerValue(SpinSPKFJ, "-");
        setSpinnerValue(SpinSPKAsalFJ, "-");
        setSpinnerValue(SpinGradeFJ, "-");
        setSpinnerValue(SpinProfileFJ, "-");
        setSpinnerValue(SpinFisikFJ, "-");
        setSpinnerValue(SpinMesinFJ, "-");
        setSpinnerValue(SpinSusunFJ, "-");

    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(NoFJ.getWindowToken(), 0);
        }
    }

    //SET false jika ingin search data
    private void setCreateMode(boolean isCreate) {
        this.isCreateMode = isCreate;
    }

    // Method untuk set single value ke spinner
    private void setSpinnerValue(Spinner spinner, String value) {
        if (spinner == null) return;
        runOnUiThread(() -> {
            try {
                String displayValue = value != null ? value : "-";
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        Collections.singletonList(displayValue));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            } catch (Exception e) {
                Log.e("Spinner Error", "Error setting spinner value: " + e.getMessage());
            }
        });
    }

    private void loadSubmittedData(String noFJ) {
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
                            "o.NoFJ, " +
                            "p.IdMesin, " +
                            "m.NamaMesin, " +
                            "s.NoBongkarSusun, " +
                            "f.Profile, " +
                            "w.NamaWarehouse, " +
                            "h.IdJenisKayu, " +
                            "k.Jenis " +
                            "FROM FJ_H h " +
                            "LEFT JOIN FJProduksiOutput o ON h.NoFJ = o.NoFJ " +
                            "LEFT JOIN MstGrade g ON h.IdGrade = g.IdGrade " +
                            "LEFT JOIN FJProduksi_h p ON o.NoProduksi = p.NoProduksi " +
                            "LEFT JOIN BongkarSusunOutputFJ s ON h.NoFJ = s.NoFJ " +
                            "LEFT JOIN MstMesin m ON p.IdMesin = m.IdMesin " +
                            "LEFT JOIN MstOrgTelly t ON h.IdOrgTelly = t.IdOrgTelly " +
                            "LEFT JOIN MstFJProfile f ON h.IdFJProfile = f.IdFJProfile " +
                            "LEFT JOIN MstWarehouse w ON h.IdFisik = w.IdWarehouse " +
                            "LEFT JOIN MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                            "WHERE h.NoFJ = ? ";


                    // Query untuk detail
                    String queryDetail = "SELECT Tebal, Lebar, Panjang, JmlhBatang " +
                            "FROM FJ_d " +
                            "WHERE NoFJ = ? " +
                            "ORDER BY NoUrut";

                    // Menggunakan try-with-resources untuk header
                    try (PreparedStatement stmt = connection.prepareStatement(queryHeader)) {
                        stmt.setString(1, noFJ);
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



                                // Mengambil data detail
                                try (PreparedStatement stmtDetail = connection.prepareStatement(queryDetail)) {
                                    stmtDetail.setString(1, noFJ);
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
                                            radioButtonMesinFJ.setChecked(true);
                                            radioButtonBSusunFJ.setEnabled(false);
                                        } else {
                                            radioButtonBSusunFJ.setChecked(true);
                                            radioButtonMesinFJ.setEnabled(false);
                                        }
                                        // Update header fields
                                        DateFJ.setText(dateCreate);
                                        TimeFJ.setText(jam);
                                        setSpinnerValue(SpinTellyFJ, namaOrgTelly);
                                        setSpinnerValue(SpinSPKFJ, noSPK);
                                        setSpinnerValue(SpinSPKAsalFJ, noSPKAsal);
                                        setSpinnerValue(SpinKayuFJ, namaKayu);
                                        setSpinnerValue(SpinGradeFJ, namaGrade);
                                        setSpinnerValue(SpinProfileFJ, namaProfile);
                                        setSpinnerValue(SpinFisikFJ, namaWarehouse);
                                        setSpinnerValue(SpinMesinFJ, namaMesin + " - " + noProduksi);
                                        setSpinnerValue(SpinSusunFJ, noBongkarSusun);

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
//                                            "Data tidak ditemukan untuk NoFJ: " + noFJ,
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
    private void checkHasBeenPrinted(String noFJ, FingerJoint.HasBeenPrintedCallback callback) {
        new Thread(() -> {
            int count = 0;
            Connection connection = null;
            try {
                // Mendapatkan koneksi dari method ConnectionClass
                connection = ConnectionClass();
                if (connection != null) {
                    // Query untuk menghitung jumlah HasBeenPrinted lebih besar dari 0 dan menangani NULL
                    String query = "SELECT HasBeenPrinted FROM FJ_h WHERE NoFJ = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, noFJ);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                count = rs.getInt(1);  // Ambil hasil hitungan
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

            final int finalCount = count;
            runOnUiThread(() -> callback.onResult(finalCount));  // Mengembalikan count, bukan boolean
        }).start();
    }

    // Method untuk mengupdate status HasBeenPrinted pada database
    private void updatePrintStatus(String noFJ) {
        new Thread(() -> {
            Connection connection = null;
            try {
                // Mendapatkan koneksi dari method ConnectionClass
                connection = ConnectionClass();
                if (connection != null) {
                    // Query untuk menambah 1 pada nilai HasBeenPrinted
                    String query = "UPDATE FJ_h SET HasBeenPrinted = COALESCE(HasBeenPrinted, 0) + 1 WHERE NoFJ = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, noFJ);
                        int rowsAffected = stmt.executeUpdate();

                        if (rowsAffected > 0) {
                            runOnUiThread(() -> Toast.makeText(FingerJoint.this,
                                    "Status cetak berhasil diupdate",
                                    Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(FingerJoint.this,
                                    "Tidak ada data yang diupdate",
                                    Toast.LENGTH_SHORT).show());
                        }
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(FingerJoint.this,
                            "Koneksi database gagal",
                            Toast.LENGTH_SHORT).show());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("Database", "Error updating HasBeenPrinted status: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(FingerJoint.this,
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


    private void setCurrentDateTimeFJ() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        DateFJ.setText(currentDate);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        TimeFJ.setText(currentTime);
    }

    private class CheckNoFJDataTask extends AsyncTask<String, Void, Boolean> {
        private String errorMessage;
        private String source = ""; // Untuk menyimpan sumber data (Mesin atau Bongkar Susun)

        @Override
        protected Boolean doInBackground(String... params) {
            String noFJ = params[0];
            Connection con = ConnectionClass();
            boolean hasData = false;

            if (con != null) {
                try {
                    String queryMesin = "SELECT NoProduksi FROM dbo.FJProduksiOutput WHERE NoFJ = ?";
                    PreparedStatement psMesin = con.prepareStatement(queryMesin);
                    psMesin.setString(1, noFJ);
                    ResultSet rsMesin = psMesin.executeQuery();

                    if (rsMesin.next()) {
                        hasData = true;
                        source = "Mesin";
                    } else {

                        String querySusun = "SELECT NoBongkarSusun FROM dbo.BongkarSusunOutputFJ WHERE NoFJ = ?";
                        PreparedStatement psSusun = con.prepareStatement(querySusun);
                        psSusun.setString(1, noFJ);
                        ResultSet rsSusun = psSusun.executeQuery();

                        if (rsSusun.next()) {
                            hasData = true;
                            source = "Bongkar Susun";
                        }

                        rsSusun.close();
                        psSusun.close();
                    }

                    rsMesin.close();
                    psMesin.close();
                    con.close();
                } catch (Exception e) {
                    errorMessage = "Error: " + e.getMessage();
                    Log.e("CheckNoFJDataTask", errorMessage);
                }
            }
            return hasData;
        }

        @Override
        protected void onPostExecute(Boolean hasData) {
            if (hasData) {
                Toast.makeText(FingerJoint.this,
                        "Data NoFJ ini sudah pernah disimpan di " + source + "!",
                        Toast.LENGTH_SHORT).show();
                // Disable tombol simpan
                BtnSimpanFJ.setEnabled(false);
            } else {
                // Data belum pernah disimpan, enable tombol simpan
                BtnSimpanFJ.setEnabled(true);
            }
        }
    }

    private class LoadProfileTask2FJ extends AsyncTask<String, Void, List<ProfileFJ>> {
        private String noFJ;

        public LoadProfileTask2FJ(String noFJ) {
            this.noFJ = noFJ;
        }

        @Override
        protected List<ProfileFJ> doInBackground(String... voids) {
            List<ProfileFJ> profileList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT p.Profile, p.IdFJProfile " +
                            "FROM dbo.MstFJProfile AS p " +
                            "INNER JOIN dbo.FJ_h AS h ON h.IdFJProfile = p.IdFJProfile " +
                            "WHERE h.NoFJ = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String namaProfile = rs.getString("Profile");
                        String idFJProfile = rs.getString("IdFJProfile");
                        ProfileFJ profileObj = new ProfileFJ(namaProfile, idFJProfile);
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
        protected void onPostExecute(List<ProfileFJ> profileList) {
            if (!profileList.isEmpty()) {
                ArrayAdapter<ProfileFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, profileList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinProfileFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load profile data.");
            }
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

    private void addDataDetail(String noFJ) {
        String tebal = DetailTebalFJ.getText().toString();
        String panjang = DetailPanjangFJ.getText().toString();
        String lebar = DetailLebarFJ.getText().toString();
        String pcs = DetailPcsFJ.getText().toString();

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
            });

            newRow.addView(deleteButton);
            Tabel.addView(newRow);

            // Bersihkan field input
            DetailTebalFJ.setText("");
            DetailPanjangFJ.setText("");
            DetailLebarFJ.setText("");
            DetailPcsFJ.setText("");

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
        if (DetailTebalFJ != null) {
            DetailTebalFJ.setText("");
        }
        if (DetailLebarFJ != null) {
            DetailLebarFJ.setText("");
        }
        if (DetailPanjangFJ != null) {
            DetailPanjangFJ.setText("");
        }
        if (DetailPcsFJ != null) {
            DetailPcsFJ.setText("");
        }
    }

    private void saveDataDetailToDatabase(String noFJ, int noUrut, double tebal, double lebar, double panjang, int pcs) {
        new FingerJoint.SaveDataTaskDetail().execute(noFJ, String.valueOf(noUrut), String.valueOf(tebal), String.valueOf(lebar),
                String.valueOf(panjang), String.valueOf(pcs));
    }

    private class SaveDataTaskDetail extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String noFJ = params[0];
            String noUrut = params[1];
            String tebal = params[2];
            String lebar = params[3];
            String panjang = params[4];
            String pcs = params[5];

            try {
                Connection connection = ConnectionClass();
                if (connection != null) {
                    String query = "INSERT INTO dbo.FJ_d (NoFJ, NoUrut, Tebal, Lebar, Panjang, JmlhBatang) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, noFJ);
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
        NoFJ.setQuery("", false);
        M3FJ.setText("");
        JumlahPcsFJ.setText("");
        CBAfkirFJ.setChecked(false);
        CBLemburFJ.setChecked(false);
        SpinKayuFJ.setSelection(0);
        SpinTellyFJ.setSelection(0);
        SpinSPKFJ.setSelection(0);
        SpinSPKAsalFJ.setSelection(0);
        SpinGradeFJ.setSelection(0);
        NoSTAFJ.setText("");
        SpinProfileFJ.setSelection(0);
        SpinMesinFJ.setEnabled(false);
        SpinSusunFJ.setEnabled(false);
        radioGroupFJ.clearCheck();
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
            TextView M3TextView = findViewById(R.id.M3FJ);
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

        JumlahPcsFJ.setText(String.valueOf(totalPcs));
    }


    private void setCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        DateFJ.setText(currentDate);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        TimeFJ.setText(currentTime);
    }

    private void showDatePickerDialog() {
        Calendar calendarFJ = Calendar.getInstance();
        int year = calendarFJ.get(Calendar.YEAR);
        int month = calendarFJ.get(Calendar.MONTH);
        int day = calendarFJ.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(FingerJoint.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                String selectedDate = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);
                DateFJ.setText(selectedDate);
                new LoadMesinTaskFJ().execute(selectedDate);
                new LoadSusunTaskFJ().execute(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        int hour = calendarFJ.get(Calendar.HOUR_OF_DAY);
        int minute = calendarFJ.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(FingerJoint.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                calendarFJ.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendarFJ.set(Calendar.MINUTE, selectedMinute);

                SimpleDateFormat timeFormat = new SimpleDateFormat(" HH:mm:ss", Locale.getDefault());
                String updatedTime = timeFormat.format(calendarFJ.getTime());
                TimeFJ.setText(updatedTime);
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

    private Uri createPdf(String noFJ, String jenisKayu, String date, String time, String tellyBy, String mesinSusun, String noSPK, String noSPKasal, String grade, List<DataRow> temporaryDataListDetail, String jumlahPcs, String m3, int printCount, String fisik) throws IOException {
        // Validasi parameter wajib
        if (noFJ == null || noFJ.trim().isEmpty()) {
            throw new IOException("Nomor FingerJoint tidak boleh kosong");
        }

        if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
            throw new IOException("Data tidak boleh kosong");
        }

        // Validasi dan set default value untuk parameter opsional
        noFJ = (noFJ != null) ? noFJ.trim() : "-";
        jenisKayu = (jenisKayu != null) ? jenisKayu.trim() : "-";
        date = (date != null) ? date.trim() : "-";
        time = (time != null) ? time.trim() : "-";
        grade = (grade != null) ? grade.trim() : "-";
        tellyBy = (tellyBy != null) ? tellyBy.trim() : "-";
        noSPK = (noSPK != null) ? noSPK.trim() : "-";
        jumlahPcs = (jumlahPcs != null) ? jumlahPcs.trim() : "-";
        m3 = (m3 != null) ? m3.trim() : "-";

        Uri pdfUri = null;
        ContentResolver resolver = getContentResolver();
        String fileName = "FJ_" + noFJ + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".pdf";
        String relativePath = Environment.DIRECTORY_DOWNLOADS;

        try {
            // Hapus file yang sudah ada jika perlu
            deleteExistingPdfFJ(fileName, relativePath);
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
                PdfFont timesNewRoman = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
                PdfWriter writer = new PdfWriter(outputStream);
                PdfDocument pdfDocument = new PdfDocument(writer);

                // Ukuran kertas yang disesuaikan secara manual
                float baseHeight = 300; // Tinggi dasar untuk elemen non-tabel (header, footer, margin, dll.)
                float rowHeight = 20; // Tinggi rata-rata per baris data
                float totalHeight = baseHeight + (rowHeight * temporaryDataListDetail.size());

                // Tetapkan ukuran halaman dinamis
                Rectangle pageSize = new Rectangle( PageSize.A6.getWidth(), totalHeight);
                pdfDocument.setDefaultPageSize(new PageSize(pageSize));

                Document document = new Document(pdfDocument);
                document.setMargins(0, 5, 0, 5);

                // Header
                Paragraph judul = new Paragraph("LABEL FINGER JOINT")
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

                float[] infoColumnWidths = new float[]{50, 5, 80};

                // Buat tabel untuk kolom kiri
                Table leftColumn = new Table(infoColumnWidths)
                        .setWidth(pageWidth * 0.4f - 5)
                        .setBorder(Border.NO_BORDER);

                // Isi kolom kiri
                addInfoRow(leftColumn, "No FJ", noFJ, timesNewRoman);
                addInfoRow(leftColumn, "Jenis", jenisKayu, timesNewRoman);
                addInfoRow(leftColumn, "Grade", grade, timesNewRoman);
                addInfoRow(leftColumn, "Fisik", fisik, timesNewRoman);

                // Buat tabel untuk kolom kanan
                Table rightColumn = new Table(infoColumnWidths)
                        .setWidth(pageWidth * 0.6f)
                        .setBorder(Border.NO_BORDER);

                // Isi kolom kanan
                addInfoRow(rightColumn, "Tanggal", date + " (" + time + ")", timesNewRoman);
                addInfoRow(rightColumn, "Telly", tellyBy, timesNewRoman);
                addInfoRow(rightColumn, "Mesin", mesinSusun, timesNewRoman);
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
                float[] columnWidths = {50f, 5f, 70f};
                Table sumTable = new Table(columnWidths)
                        .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                        .setMarginTop(10)
                        .setFontSize(8)
                        .setBorder(Border.NO_BORDER);

                sumTable.addCell(new Cell().add(new Paragraph("Jumlah Pcs")).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));
                sumTable.addCell(new Cell().add(new Paragraph(":")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                sumTable.addCell(new Cell().add(new Paragraph(String.valueOf(jumlahPcs))).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));

                sumTable.addCell(new Cell().add(new Paragraph("m3")).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));
                sumTable.addCell(new Cell().add(new Paragraph(":")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                sumTable.addCell(new Cell().add(new Paragraph(String.valueOf(m3))).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));

                Paragraph qrCodeID = new Paragraph(noFJ).setTextAlignment(TextAlignment.CENTER).setFontSize(8).setMargins(-5, 0, 0, 0).setFont(timesNewRoman);
                Paragraph qrCodeIDbottom = new Paragraph(noFJ).setTextAlignment(TextAlignment.RIGHT).setFontSize(8).setMargins(-5, 20, 0, 0).setFont(timesNewRoman);

                BarcodeQRCode qrCode = new BarcodeQRCode(noFJ);
                PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
                Image qrCodeImage = new Image(qrCodeObject).setWidth(75).setHorizontalAlignment(HorizontalAlignment.CENTER).setMargins(-5, 0, 0, 0);

                BarcodeQRCode qrCodeBottom = new BarcodeQRCode(noFJ);
                PdfFormXObject qrCodeBottomObject = qrCodeBottom.createFormXObject(ColorConstants.BLACK, pdfDocument);
                Image qrCodeBottomImage = new Image(qrCodeBottomObject).setWidth(75).setHorizontalAlignment(HorizontalAlignment.RIGHT).setMargins(-5, 0, 0, 0);

                Paragraph bottomLine = new Paragraph("-----------------------------------------------------------------------------------------------------").setTextAlignment(TextAlignment.CENTER).setFontSize(8).setMargins(0, 0, 0, 15).setFont(timesNewRoman);
                Paragraph outputText = new Paragraph("Output").setTextAlignment(TextAlignment.CENTER).setFontSize(8).setMargins(15, 0, 0, 0).setFont(timesNewRoman);
                Paragraph inputText = new Paragraph("Input").setTextAlignment(TextAlignment.RIGHT).setFontSize(8).setMargins(15, 28, 0, 0).setFont(timesNewRoman);

                // Tambahkan semua elemen ke dokumen

                document.add(judul);
                if (printCount > 1) {
                    addTextDitheringWatermark(pdfDocument, timesNewRoman);
                }

                document.add(mainTable);
                document.add(table);
                document.add(sumTable);

                if(printCount % 2 != 0) {
                    document.add(inputText);
                    document.add(qrCodeBottomImage);
                    document.add(qrCodeIDbottom);
                }
                else{
                    document.add(outputText);
                    document.add(qrCodeImage);
                    document.add(qrCodeID);
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

    private void deleteExistingPdfFJ(String fileName, String relativePath) {
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

    private class SaveBongkarSusunTaskFJ extends AsyncTask<Void, Void, Boolean> {
        private String noBongkarSusun;
        private String noFJ;

        public SaveBongkarSusunTaskFJ(String noBongkarSusun, String noFJ) {
            this.noBongkarSusun = noBongkarSusun;
            this.noFJ = noFJ;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.BongkarSusunOutputFJ (NoFJ, NoBongkarSusun) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);
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
//                Toast.makeText(FingerJoint.this, "Data berhasil disimpan.", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    private class SaveToDatabaseTaskFJ extends AsyncTask<Void, Void, Boolean> {
        private String noProduksi, noFJ;

        public SaveToDatabaseTaskFJ(String noProduksi, String noFJ) {
            this.noProduksi = noProduksi;
            this.noFJ = noFJ;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.FJProduksiOutput (NoProduksi, NoFJ) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noProduksi);
                    ps.setString(2, noFJ);

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
                Toast.makeText(FingerJoint.this, "Data berhasil disimpan ke database.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FingerJoint.this, "Gagal menyimpan data ke database.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    

    private class DeleteDataTaskFJ extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String noFJ = params[0];
            Connection con = ConnectionClass();
            boolean success = false;

            if (con != null) {
                try {
                    String query = "DELETE FROM dbo.FJ_h WHERE NoFJ = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);

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
                Toast.makeText(FingerJoint.this, "Data berhasil dihapus.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FingerJoint.this, "Gagal menghapus data.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateDatabaseTaskFJ extends AsyncTask<Void, Void, Boolean> {
        private String noFJ, dateCreate, time, idTelly, noSPK, noSPKasal, idGrade, idJenisKayu, idFJProfile;
        private int isReject, isLembur, IdUOMTblLebar, IdUOMPanjang;

        public UpdateDatabaseTaskFJ(String noFJ, String dateCreate, String time, String idTelly, String noSPK,String noSPKasal,
                                    String idGrade, String idJenisKayu, String idFJProfile,
                                    int isReject, int isLembur,  int IdUOMTblLebar, int IdUOMPanjang) {
            this.noFJ = noFJ;
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
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "UPDATE dbo.FJ_h SET DateCreate = ?, Jam = ?, IdOrgTelly = ?, NoSPK = ?, NoSPKAsal = ?, IdGrade = ?, " +
                            "IdFJProfile = ?, IdJenisKayu = ?, IdFisik = 5, IdWarehouse = 5, IsReject = ?, IsLembur = ?, IdUOMTblLebar =?, IdUOMPanjang = ? WHERE NoFJ = ?";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, dateCreate);
                    ps.setString(2, time);
                    ps.setString(3, idTelly);
                    ps.setString(4, noSPK);
                    ps.setString(5, noSPKasal);
                    ps.setString(6, idGrade);
                    ps.setString(7, idFJProfile);
                    ps.setString(8, idJenisKayu);
                    ps.setInt(9, isReject);
                    ps.setInt(10, isLembur);
                    ps.setInt(11, IdUOMTblLebar);
                    ps.setInt(12, IdUOMPanjang);
                    ps.setString(13, noFJ);

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

    private class SetAndSaveNoFJoinTaskFJ extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            String newNoJoin = null;
            if (con != null) {
                try {
                    String query = "SELECT MAX(NoFJ) FROM dbo.FJ_h";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        String lastNoFJoin = rs.getString(1);

                        if (lastNoFJoin != null && lastNoFJoin.startsWith("S.")) {
                            String numericPart = lastNoFJoin.substring(2);
                            int numericValue = Integer.parseInt(numericPart);
                            int newNumericValue = numericValue + 1;

                            newNoJoin = "S." + String.format("%06d", newNumericValue);
                        }
                    }

                    rs.close();
                    ps.close();

                    if (newNoJoin != null) {
                        String insertQuery = "INSERT INTO dbo.FJ_h (NoFJ) VALUES (?)";
                        PreparedStatement insertPs = con.prepareStatement(insertQuery);
                        insertPs.setString(1, newNoJoin);
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
            return newNoJoin;
        }

        @Override
        protected void onPostExecute(String newNoJoin) {
            if (newNoJoin != null) {
                NoFJ.setQuery(newNoJoin, true);
                NoFJ.setVisibility(View.GONE);
                NoFJ_display.setVisibility(View.VISIBLE);
                NoFJ_display.setText(newNoJoin);
                NoFJ_display.setEnabled(false);
                Toast.makeText(FingerJoint.this, "NoFJ berhasil diatur dan disimpan.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Error", "Failed to set or save NoFJ.");
                Toast.makeText(FingerJoint.this, "Gagal mengatur atau menyimpan NoFJ.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class LoadJenisKayuTaskFJ extends AsyncTask<Void, Void, List<JenisKayuFJ>> {
        @Override
        protected List<JenisKayuFJ> doInBackground(Void... voids) {
            List<JenisKayuFJ> jenisKayuList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT IdJenisKayu, Jenis FROM dbo.MstJenisKayu WHERE Enable = 1 AND IsInternal = 1 AND IsNonST = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idJenisKayu = rs.getString("IdJenisKayu");
                        String namaJenisKayu = rs.getString("Jenis");

                        JenisKayuFJ jenisKayu = new JenisKayuFJ(idJenisKayu, namaJenisKayu);
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
        protected void onPostExecute(List<JenisKayuFJ> jenisKayuList) {
            JenisKayuFJ dummyKayu = new JenisKayuFJ("", "PILIH");
            jenisKayuList.add(0, dummyKayu);

            ArrayAdapter<JenisKayuFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, jenisKayuList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinKayuFJ.setAdapter(adapter);
            SpinKayuFJ.setSelection(0);
        }
    }

    public class LoadJenisKayuTask2FJ extends AsyncTask<String, Void, List<JenisKayuFJ>> {
        private String noFJ;

        public LoadJenisKayuTask2FJ(String noFJ) {
            this.noFJ = noFJ;
        }

        @Override
        protected List<JenisKayuFJ> doInBackground(String... params) {
            List<JenisKayuFJ> jenisKayuList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT j.IdJenisKayu, j.Jenis " +
                            "FROM dbo.MstJenisKayu AS j " +
                            "INNER JOIN dbo.FJ_h AS h ON h.IdJenisKayu = j.IdJenisKayu " +
                            "WHERE h.NoFJ = ? AND j.enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idJenisKayu = rs.getString("IdJenisKayu");
                        String namaJenisKayu = rs.getString("Jenis");
                        JenisKayuFJ jenisKayu = new JenisKayuFJ(idJenisKayu, namaJenisKayu);
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
        protected void onPostExecute(List<JenisKayuFJ> jenisKayuList) {
            if (!jenisKayuList.isEmpty()) {
                ArrayAdapter<JenisKayuFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, jenisKayuList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinKayuFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load jenis kayu.");
            }
        }
    }

    private class LoadTellyTaskFJ extends AsyncTask<Void, Void, List<TellyFJ>> {
        @Override
        protected List<TellyFJ> doInBackground(Void... voids) {
            List<TellyFJ> tellyList = new ArrayList<>();

            SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            username = prefs.getString("username", "");
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query =  "SELECT t.IdOrgTelly, t.NamaOrgTelly " +
                                    "FROM dbo.MstOrgTelly t " +
                                    "INNER JOIN MstUsername u ON t.NamaOrgTelly = u.Username " +
                                    "WHERE t.enable = 1 AND u.Username = ?";
                    PreparedStatement ps = con.prepareStatement(query);

                    ps.setString(1, username);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idOrgTelly = rs.getString("IdOrgTelly");
                        String namaOrgTelly = rs.getString("NamaOrgTelly");

                        TellyFJ telly = new TellyFJ(idOrgTelly, namaOrgTelly);
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
        protected void onPostExecute(List<TellyFJ> tellyList) {

            // Buat adapter dengan data yang dimodifikasi
            ArrayAdapter<TellyFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, tellyList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Set adapter ke spinner
            SpinTellyFJ.setAdapter(adapter);

        }
    }

    private class LoadTellyTask2FJ extends AsyncTask<String, Void, List<TellyFJ>> {
        private String noFJ;

        public LoadTellyTask2FJ(String noFJ) {
            this.noFJ = noFJ;
        }

        @Override
        protected List<TellyFJ> doInBackground(String... params) {
            List<TellyFJ> tellyList = new ArrayList<>();
            Connection con = ConnectionClass(); // Ensure this method establishes a database connection

            if (con != null) {
                try {

                    String query = "SELECT t.IdOrgTelly, t.NamaOrgTelly " +
                            "FROM dbo.MstOrgTelly AS t " +
                            "INNER JOIN dbo.FJ_h AS h ON h.IdOrgTelly = t.IdOrgTelly " +
                            "WHERE h.NoFJ = ? AND t.enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idOrgTelly = rs.getString("IdOrgTelly");
                        String namaOrgTelly = rs.getString("NamaOrgTelly");

                        TellyFJ telly = new TellyFJ(idOrgTelly, namaOrgTelly);
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
        protected void onPostExecute(List<TellyFJ> tellyList) {
            if (!tellyList.isEmpty()) {
                ArrayAdapter<TellyFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, tellyList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinTellyFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load telly data.");
            }
        }
    }

    private class LoadSPKTaskFJ extends AsyncTask<Void, Void, List<SPKFJ>> {
        @Override
        protected List<SPKFJ> doInBackground(Void... voids) {
            List<SPKFJ> spkList = new ArrayList<>();
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

                        // Buat objek SPKFJ dengan kedua nilai
                        SPKFJ spk = new SPKFJ(noSPK, buyer);
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
        protected void onPostExecute(List<SPKFJ> spkList) {
            // Tambahkan item PILIH di awal list
            SPKFJ dummySPK = new SPKFJ("PILIH");
            spkList.add(0, dummySPK);

            ArrayAdapter<SPKFJ> adapter = new ArrayAdapter<>(FingerJoint.this,
                    android.R.layout.simple_spinner_item, spkList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinSPKFJ.setAdapter(adapter);
            SpinSPKFJ.setSelection(0);

            // Optional: Log untuk debugging
            Log.d("SPKFJ_DEBUG", "Loaded " + (spkList.size() - 1) + " SPK FJ items");
        }
    }

    private class LoadSPKAsalTaskFJ extends AsyncTask<Void, Void, List<SPKAsalFJ>> {
        @Override
        protected List<SPKAsalFJ> doInBackground(Void... voids) {
            List<SPKAsalFJ> spkAsalList = new ArrayList<>();
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
                        String noSPKAsal = rs.getString("NoSPK");
                        String buyer = rs.getString("Buyer");

                        // Buat objek SPKAsalFJ dengan kedua nilai
                        SPKAsalFJ spkAsal = new SPKAsalFJ(noSPKAsal, buyer);
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
        protected void onPostExecute(List<SPKAsalFJ> spkAsalList) {
            // Tambahkan item PILIH di awal list
            SPKAsalFJ dummySPKAsal = new SPKAsalFJ("PILIH");
            spkAsalList.add(0, dummySPKAsal);

            ArrayAdapter<SPKAsalFJ> adapter = new ArrayAdapter<>(FingerJoint.this,
                    android.R.layout.simple_spinner_item, spkAsalList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinSPKAsalFJ.setAdapter(adapter);
            SpinSPKAsalFJ.setSelection(0);

            // Optional: Log untuk debugging
            Log.d("SPKAsalFJ_DEBUG", "Loaded " + (spkAsalList.size() - 1) + " SPK Asal FJ items");
        }
    }

    private class LoadProfileTaskFJ extends AsyncTask<Void, Void, List<ProfileFJ>> {
        @Override
        protected List<ProfileFJ> doInBackground(Void... voids) {
            List<ProfileFJ> profileList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT Profile, IdFJProfile FROM dbo.MstFJProfile WHERE IdFJProfile != 0";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String namaProfile = rs.getString("Profile");
                        String idFJProfile = rs.getString("IdFJProfile");

                        ProfileFJ profileObj = new ProfileFJ(namaProfile, idFJProfile);
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
        protected void onPostExecute(List<ProfileFJ> profileList) {
            ProfileFJ dummyProfile = new ProfileFJ("PILIH", "");
            profileList.add(0, dummyProfile);

            ArrayAdapter<ProfileFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, profileList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinProfileFJ.setAdapter(adapter);
            SpinProfileFJ.setSelection(0);
        }
    }

    private class LoadFisikTaskFJ extends AsyncTask<Void, Void, List<FisikFJ>> {
        @Override
        protected List<FisikFJ> doInBackground(Void... voids) {
            List<FisikFJ> fisikList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT NamaWarehouse FROM dbo.MstWarehouse WHERE IdWarehouse = 5";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String namaWarehouse = rs.getString("NamaWarehouse");

                        FisikFJ fisik = new FisikFJ(namaWarehouse);
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
        protected void onPostExecute(List<FisikFJ> fisikList) {
            if (!fisikList.isEmpty()) {
                ArrayAdapter<FisikFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, fisikList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinFisikFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load fisik data.");
            }
        }
    }

    private class LoadFisikTask2FJ extends AsyncTask<String, Void, List<FisikFJ>> {
        private String noFJ;

        public LoadFisikTask2FJ(String noFJ) {
            this.noFJ = noFJ;
        }

        @Override
        protected List<FisikFJ> doInBackground(String... params) {
            List<FisikFJ> fisikList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT mw.NamaWarehouse " +
                            "FROM dbo.MstWarehouse mw " +
                            "INNER JOIN dbo.FJ_h fj ON mw.IdWarehouse = fj.IdWarehouse " +
                            "WHERE fj.NoFJ = ?";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String namaWarehouse = rs.getString("NamaWarehouse");
                        FisikFJ fisik = new FisikFJ(namaWarehouse);
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
        protected void onPostExecute(List<FisikFJ> fisikList) {
            if (!fisikList.isEmpty()) {
                ArrayAdapter<FisikFJ> adapter = new ArrayAdapter<>(FingerJoint.this,
                        android.R.layout.simple_spinner_item, fisikList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinFisikFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "No warehouse found.");
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(FingerJoint.this,
                        android.R.layout.simple_spinner_item, new String[]{"Tidak ada Fisik"});
                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinFisikFJ.setAdapter(emptyAdapter);
            }
        }
    }


    private class LoadGradeTaskFJ extends AsyncTask<String, Void, List<GradeFJ>> {
        @Override
        protected List<GradeFJ> doInBackground(String... params) {
            List<GradeFJ> gradeList = new ArrayList<>();
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

                    String category = "FINGERJOIN";

                    String query = "SELECT DISTINCT a.IdGrade, a.NamaGrade " +
                            "FROM MstGrade a " +
                            "INNER JOIN MstGrade_d b ON a.IdGrade = b.IdGrade " +
                            "WHERE a.Enable = 1 AND b.IdJenisKayu = ? AND b.Category = ?";

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
                            GradeFJ gradeObj = new GradeFJ(idGrade, namaGrade);
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
        protected void onPostExecute(List<GradeFJ> gradeList) {
            if (!gradeList.isEmpty()) {
                GradeFJ dummyGrade = new GradeFJ("", "PILIH");
                gradeList.add(0, dummyGrade);

            } else {
                Log.e("Error", "Tidak ada grade");
                gradeList = new ArrayList<>();
                gradeList.add(new GradeFJ("", "GRADE TIDAK TERSEDIA"));
            }

            ArrayAdapter<GradeFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, gradeList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SpinGradeFJ.setAdapter(adapter);
            SpinGradeFJ.setSelection(0);
        }
    }

    private class LoadGradeTask2FJ extends AsyncTask<String, Void, List<GradeFJ>> {
        private String noFJ;

        public LoadGradeTask2FJ(String noFJ) {
            this.noFJ = noFJ;
        }

        @Override
        protected List<GradeFJ> doInBackground(String... params) {
            List<GradeFJ> gradeList = new ArrayList<>();
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

                    String category = "FingerJoin";

                    String query = "SELECT DISTINCT a.IdGrade, a.NamaGrade " +
                            "FROM MstGrade a " +
                            "INNER JOIN MstGrade_d b ON a.IdGrade = b.IdGrade " +
                            "WHERE a.Enable = 1 AND b.IdJenisKayu = ? AND b.Category = ? AND b.NoFJ = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setInt(1, idJenisKayu);
                    ps.setString(2, category);
                    ps.setString(3, noFJ);
                    Log.d("LoadGradeTask", "Executing query: " + query + " with IdJenisKayu: " + idJenisKayu);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idGrade = rs.getString("IdGrade");
                        String namaGrade = rs.getString("NamaGrade");

                        if (idGrade != null && namaGrade != null) {
                            Log.d("LoadGradeTask", "Fetched Grade: IdGrade = " + idGrade + ", NamaGrade = " + namaGrade);
                            GradeFJ gradeObj = new GradeFJ(idGrade, namaGrade);
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
        protected void onPostExecute(List<GradeFJ> gradeList) {
            if (!gradeList.isEmpty()) {
                ArrayAdapter<GradeFJ> adapter = new ArrayAdapter<>(FingerJoint.this,
                        android.R.layout.simple_spinner_item, gradeList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Tidak ada grade");
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(FingerJoint.this,
                        android.R.layout.simple_spinner_item, new String[]{"Tidak ada grade"});
                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeFJ.setAdapter(emptyAdapter);
            }
        }
    }

    private class LoadMesinTaskFJ extends AsyncTask<String, Void, List<MesinFJ>> {
        @Override
        protected List<MesinFJ> doInBackground(String... params) {
            List<MesinFJ> mesinList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    // Ambil tanggal saat ini jika tidak ada parameter
                    String selectedDate;
                    if (params != null && params.length > 0) {
                        selectedDate = params[0];
                    } else {
                        selectedDate = DateFJ.getText().toString();
                    }

                    String query = "SELECT a.IdMesin, b.NamaMesin, a.NoProduksi FROM dbo.FJProduksi_h a " +
                            "INNER JOIN dbo.MstMesin b ON a.IdMesin = b.IdMesin WHERE CONVERT(date, a.Tanggal) = CONVERT(date, ?)";

                    Log.d("LoadMesinTask", "Query: " + query + " dengan tanggal: " + selectedDate);

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, selectedDate);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idMesin = rs.getString("IdMesin");
                        String nomorProduksi = rs.getString("NoProduksi");
                        String namaMesin = rs.getString("NamaMesin");

                        Log.d("LoadMesinTask", "Found: ID=" + idMesin + ", No=" + nomorProduksi + ", Name=" + namaMesin);

                        MesinFJ mesin = new MesinFJ(nomorProduksi, namaMesin);
                        mesinList.add(mesin);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("LoadMesinTask", "Error: " + e.getMessage(), e);
                }
            } else {
                Log.e("LoadMesinTask", "Connection is null");
            }
            return mesinList;
        }

        @Override
        protected void onPostExecute(List<MesinFJ> mesinList) {
            if (!mesinList.isEmpty()) {
                ArrayAdapter<MesinFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinMesinFJ.setAdapter(adapter);
            } else {
                Log.d("LoadMesinTask", "No data found");
                SpinMesinFJ.setAdapter(null);
            }
        }
    }




    private class LoadSusunTaskFJ extends AsyncTask<String, Void, List<SusunFJ>> {
        @Override
        protected List<SusunFJ> doInBackground(String... params) {
            List<SusunFJ> susunList = new ArrayList<>();
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

                        SusunFJ susun = new SusunFJ(nomorBongkarSusun);
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
        protected void onPostExecute(List<SusunFJ> susunList) {
            if (!susunList.isEmpty()) {
                ArrayAdapter<SusunFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, susunList);
                SpinSusunFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load susun data");
                SpinSusunFJ.setAdapter(null);

            }
        }
    }


    public class JenisKayuFJ {
        private String idJenisKayu;
        private String namaJenisKayu;

        public JenisKayuFJ(String idJenisKayu, String namaJenisKayu) {
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



    public class TellyFJ {
        private String idTelly;
        private String namaTelly;

        public TellyFJ(String idTelly, String namaTelly) {
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


    public class SPKFJ {
        private String noSPK;
        private String buyer;

        public SPKFJ(String noSPK, String buyer) {
            this.noSPK = noSPK;
            this.buyer = buyer;
        }

        // Constructor untuk dummy/placeholder
        public SPKFJ(String noSPK) {
            this.noSPK = noSPK;
            this.buyer = "";
        }

        public String getNoSPK() {
            return noSPK;
        }

        public String getBuyer() {
            return buyer;
        }

        @Override
        public String toString() {
            if (buyer.isEmpty()) {
                return noSPK; // Untuk item "PILIH"
            }
            return noSPK + " - " + buyer;
        }
    }

    public class SPKAsalFJ {
        private String noSPKAsal;
        private String buyer;

        public SPKAsalFJ(String noSPKAsal, String buyer) {
            this.noSPKAsal = noSPKAsal;
            this.buyer = buyer;
        }

        // Constructor untuk dummy/placeholder
        public SPKAsalFJ(String noSPKAsal) {
            this.noSPKAsal = noSPKAsal;
            this.buyer = "";
        }

        public String getNoSPKAsal() {
            return noSPKAsal;
        }

        public String getBuyer() {
            return buyer;
        }

        @Override
        public String toString() {
            if (buyer.isEmpty()) {
                return noSPKAsal; // Untuk item "PILIH"
            }
            return noSPKAsal + " - " + buyer;
        }
    }

    public class ProfileFJ {
        private String idFJProfile;
        private String namaProfile;

        public ProfileFJ(String namaProfile, String idFJProfile) {
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


    public class FisikFJ {
        private String idWarehouse; // Jika diperlukan
        private String namaWarehouse;

        public FisikFJ(String namaWarehouse) {
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


    public class GradeFJ {
        private String idGrade;
        private String namaGrade;

        public GradeFJ(String idGrade, String namaGrade) {
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


    public class MesinFJ {
        private String noProduksi;
        private String namaMesin;

        public MesinFJ(String noProduksi, String namaMesin) {
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

    public class SusunFJ {
        private String nomorBongkarSusun;

        public SusunFJ(String nomorBongkarSusun) {
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
}