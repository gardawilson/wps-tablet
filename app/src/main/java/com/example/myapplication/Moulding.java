package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
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

public class Moulding extends AppCompatActivity {

    //deklarasi semua item yang ada di xml yang akan digunakan
    private SearchView NoMoulding;
    private EditText DateM;
    private EditText TimeM;
    private EditText NoSTAM;
    private Spinner SpinKayuM;
    private Spinner SpinTellyM;
    private Spinner SpinSPKM;
    private Spinner SpinSPKAsalM;
    private Spinner SpinProfileM;
    private Spinner SpinFisikM;
    private Spinner SpinGradeM;
    private Spinner SpinMesinM;
    private Spinner SpinSusunM;
    private Calendar calendarM;
    private RadioGroup radioGroupM;
    private RadioButton radioButtonMesinM;
    private RadioButton radioButtonBSusunM;
    private Button BtnDataBaruM;
    private Button BtnSimpanM;
    private Button BtnBatalM;
    private Button BtnHapusDetailM;
    private boolean isDataBaruClickedM = false;
    private CheckBox CBAfkirM;
    private CheckBox CBLemburM;
    private Button BtnInputDetailM;
    private AutoCompleteTextView DetailLebarM;
    private AutoCompleteTextView DetailTebalM;
    private AutoCompleteTextView DetailPanjangM;
    private EditText DetailPcsM;
    private static int currentNumber = 1;
    private Button BtnPrintM;
    private TextView M3M;
    private TextView JumlahPcsM;
    private int rowCount = 0;
    private TableLayout Tabel;
    private boolean isCreateMode = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    private EditText NoMoulding_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_moulding);

        //deklarasi semua item yang ada di xml yang akan digunakan sesuai ID yang tertera di XML
        NoSTAM = findViewById(R.id.NoSTAM);
        NoMoulding = findViewById(R.id.NoMoulding);
        DateM = findViewById(R.id.DateM);
        TimeM = findViewById(R.id.TimeM);
        SpinKayuM = findViewById(R.id.SpinKayuM);
        SpinTellyM = findViewById(R.id.SpinTellyM);
        SpinSPKM = findViewById(R.id.SpinSPKM);
        SpinSPKAsalM = findViewById(R.id.SpinSPKAsalM);
        SpinProfileM = findViewById(R.id.SpinProfileM);
        SpinFisikM = findViewById(R.id.SpinFisikM);
        SpinGradeM = findViewById(R.id.SpinGradeM);
        calendarM = Calendar.getInstance();
        SpinMesinM = findViewById(R.id.SpinMesinM);
        SpinSusunM = findViewById(R.id.SpinSusunM);
        radioButtonMesinM = findViewById(R.id.radioButtonMesinM);
        radioButtonBSusunM = findViewById(R.id.radioButtonBSusunM);
        BtnDataBaruM = findViewById(R.id.BtnDataBaruM);
        BtnSimpanM = findViewById(R.id.BtnSimpanM);
        BtnBatalM = findViewById(R.id.BtnBatalM);
        BtnHapusDetailM = findViewById(R.id.BtnHapusDetailM);
        CBLemburM = findViewById(R.id.CBLemburM);
        CBAfkirM = findViewById(R.id.CBAfkirM);
        BtnInputDetailM = findViewById(R.id.BtnInputDetailM);
        DetailPcsM = findViewById(R.id.DetailPcsM);
        DetailTebalM = findViewById(R.id.DetailTebalM);
        DetailPanjangM = findViewById(R.id.DetailPanjangM);
        DetailLebarM = findViewById(R.id.DetailLebarM);
        BtnPrintM = findViewById(R.id.BtnPrintM);
        M3M = findViewById(R.id.M3M);
        JumlahPcsM = findViewById(R.id.JumlahPcsM);
        Tabel = findViewById(R.id.Tabel);
        radioGroupM = findViewById(R.id.radioGroupM);
        NoMoulding_display = findViewById(R.id.NoMoulding_display);

        // Set imeOptions untuk memungkinkan pindah fokus
        DetailTebalM.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        DetailLebarM.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        DetailPanjangM.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        // Menangani aksi 'Enter' pada keyboard
        DetailTebalM.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Jika tombol 'Enter' ditekan, pindahkan fokus ke DetailLebarS4S
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Pastikan DetailLebarS4S bisa menerima fokus
                    DetailLebarM.requestFocus();
                    return true; // Menunjukkan bahwa aksi sudah ditangani
                }
                return false;
            }
        });

        DetailLebarM.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    DetailPanjangM.requestFocus();
                    return true;
                }
                return false;
            }
        });

        DetailPanjangM.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    DetailPcsM.requestFocus();
                    return true;
                }
                return false;
            }
        });

        NoMoulding_display.setVisibility(View.GONE);
        disableForm();

        NoMoulding.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                    }
                    else{
                        enableForm();
                    }
                }
                return true;
            }
        });

        //mengatur fungsi RadioButtonMesin
        radioButtonMesinM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpinMesinM.setEnabled(true);
                    SpinSusunM.setEnabled(false);
                }
            }
        });

        //mengatur fungsi RadioButtonSusun
        radioButtonBSusunM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpinSusunM.setEnabled(true);
                    SpinMesinM.setEnabled(false);
                }
            }
        });

        //memanggil fungsi untuk menampilkan tanggal realtime
        setCurrentDateTime();

        //fungsi button DataBaru
        BtnDataBaruM.setOnClickListener(v -> {
            setCurrentDateTime();
            setCreateMode(true);

            new SetAndSaveNoMouldingTask().execute();
            new LoadJenisKayuTask().execute();
            new LoadTellyTask().execute();
            new LoadSPKTask().execute();
            new LoadSPKAsalTask().execute();
            new LoadProfileTask().execute();
            new LoadFisikTask().execute();
            new LoadGradeTask().execute();
            new LoadMesinTask().execute();
            new LoadSusunTask().execute();


            BtnSimpanM.setEnabled(true);
            BtnBatalM.setEnabled(true);
            BtnPrintM.setEnabled(false);

            clearData();
            resetDetailData();
            enableForm();
        });

        //fungsi button Simpan
        BtnSimpanM.setOnClickListener(v -> {
            String noMoulding = NoMoulding.getQuery().toString();
            String dateCreate = DateM.getText().toString();
            String time = TimeM.getText().toString();

            Telly selectedTelly = (Telly) SpinTellyM.getSelectedItem();
            SPK selectedSPK = (SPK) SpinSPKM.getSelectedItem();
            SPKAsal selectedSPKAsal = (SPKAsal) SpinSPKAsalM.getSelectedItem();
            Profile selectedProfile = (Profile) SpinProfileM.getSelectedItem();
            Fisik selectedFisik = (Fisik) SpinFisikM.getSelectedItem();
            Grade selectedGrade = (Grade) SpinGradeM.getSelectedItem();
            JenisKayu selectedJenisKayu = (JenisKayu) SpinKayuM.getSelectedItem();
            Mesin selectedMesin = (Mesin) SpinMesinM.getSelectedItem();
            Susun selectedSusun = (Susun) SpinSusunM.getSelectedItem();
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
            int isReject = CBAfkirM.isChecked() ? 1 : 0;
            int isLembur = CBLemburM.isChecked() ? 1 : 0;
            int idUOMTblLebar = radioGroupUOMTblLebar.getCheckedRadioButtonId() == R.id.radioMillimeter ? 1 : 4;
            int idUOMPanjang;
            if (radioGroupUOMPanjang.getCheckedRadioButtonId() == R.id.radioCentimeter) {
                idUOMPanjang = 1;
            } else if (radioGroupUOMPanjang.getCheckedRadioButtonId() == R.id.radioMeter) {
                idUOMPanjang = 2;
            } else {
                idUOMPanjang = 3;
            }

            if (noMoulding.isEmpty() || dateCreate.isEmpty() || time.isEmpty() ||
                    selectedTelly == null || selectedTelly.getIdTelly().isEmpty() ||
                    selectedSPK == null || selectedSPK.getNoSPK().equals("PILIH") ||
                    selectedSPKAsal == null || selectedSPKAsal.getNoSPKAsal().equals("PILIH") ||
                    selectedProfile == null || selectedProfile.getIdFJProfile().isEmpty() ||
                    selectedFisik == null || selectedFisik.getIdWarehouse().isEmpty() ||
                    selectedGrade == null || selectedGrade.getIdGrade().isEmpty() ||
                    selectedJenisKayu == null || selectedJenisKayu.getIdJenisKayu().isEmpty() ||
                    (!radioButtonMesinM.isChecked() && !radioButtonBSusunM.isChecked()) ||
                    (radioButtonMesinM.isChecked() && (selectedMesin == null || selectedMesin.getNoProduksi().isEmpty())) ||
                    (radioButtonBSusunM.isChecked() && (selectedSusun == null || selectedSusun.getNoBongkarSusun().isEmpty()))) {


                Toast.makeText(Moulding.this, "Pastikan semua field terisi dengan benar.", Toast.LENGTH_SHORT).show();
                return;
            }
            BtnDataBaruM.setEnabled(true);
            BtnPrintM.setEnabled(true);

            new UpdateDatabaseTask(
                    noMoulding,
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

            if (radioButtonMesinM.isChecked() && SpinMesinM.isEnabled() && noProduksi != null) {
                new SaveToDatabaseTask(noProduksi, noMoulding).execute();
                for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                    Moulding.DataRow dataRow = temporaryDataListDetail.get(i);
                    saveDataDetailToDatabase(noMoulding, i + 1, Double.parseDouble(dataRow.tebal), Double.parseDouble(dataRow.lebar),
                            Double.parseDouble(dataRow.panjang), Integer.parseInt(dataRow.pcs));
                }
            } else if (radioButtonBSusunM.isChecked() && SpinSusunM.isEnabled() && noBongkarSusun != null) {
                new SaveBongkarSusunTask(noBongkarSusun, noMoulding).execute();
                for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                    Moulding.DataRow dataRow = temporaryDataListDetail.get(i);
                    saveDataDetailToDatabase(noMoulding, i + 1, Double.parseDouble(dataRow.tebal), Double.parseDouble(dataRow.lebar),
                            Double.parseDouble(dataRow.panjang), Integer.parseInt(dataRow.pcs));
                }
            } else {
                Toast.makeText(Moulding.this, "Pilih opsi yang valid untuk disimpan.", Toast.LENGTH_SHORT).show();
                return;
            }
            BtnDataBaruM.setEnabled(true);
            BtnPrintM.setEnabled(true);
            BtnSimpanM.setEnabled(false);
            disableForm();
            Toast.makeText(Moulding.this, "Data berhasil disimpan dan tampilan telah dikosongkan.", Toast.LENGTH_SHORT).show();

        });


       BtnBatalM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCreateMode(false);
                resetDetailData();
                resetAllForm();
                disableForm();
                NoMoulding.setVisibility(View.VISIBLE);
                NoMoulding_display.setVisibility(View.GONE);
                BtnDataBaruM.setEnabled(true);
                BtnSimpanM.setEnabled(false);
                BtnPrintM.setEnabled(true);
            }
        });

        //fungsi spinner JenisKayu
        SpinKayuM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        SpinSPKM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                                    Moulding.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    dimensionData.get("tebal")
                            );
                            ArrayAdapter<String> lebarAdapter = new ArrayAdapter<>(
                                    Moulding.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    dimensionData.get("lebar")
                            );
                            ArrayAdapter<String> panjangAdapter = new ArrayAdapter<>(
                                    Moulding.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    dimensionData.get("panjang")
                            );

                            // Set adapter untuk masing-masing AutoCompleteTextView
                            DetailTebalM.setAdapter(tebalAdapter);
                            DetailLebarM.setAdapter(lebarAdapter);
                            DetailPanjangM.setAdapter(panjangAdapter);

                            // Set threshold untuk semua AutoCompleteTextView
                            DetailTebalM.setThreshold(0);
                            DetailLebarM.setThreshold(0);
                            DetailPanjangM.setThreshold(0);

                            // Tampilkan status lock
                            if (isLocked) {
                                Toast.makeText(Moulding.this, "Dimension terkunci", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Moulding.this, "Dimension tidak terkunci", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                DetailTebalM.setText("");
                DetailPanjangM.setText("");
                DetailLebarM.setText("");
                DetailTebalM.setAdapter(null);
                DetailPanjangM.setAdapter(null);
                DetailLebarM.setAdapter(null);
            }
        });


        //fungsi kolom tanggal
        DateM.setOnClickListener(v -> showDatePickerDialog());

        //fungsi kolom jam
        TimeM.setOnClickListener(v -> showTimePickerDialog());

        //fungsi button Input Detail
        BtnInputDetailM.setOnClickListener(v -> {
            // Ambil input dari AutoCompleteTextView
            String noMoulding = NoMoulding.getQuery().toString();
            String tebal = DetailTebalM.getText().toString().trim();
            String lebar = DetailLebarM.getText().toString().trim();
            String panjang = DetailPanjangM.getText().toString().trim();

            // Ambil data SPK, Jenis Kayu, dan Grade dari Spinner
            SPK selectedSPK = (SPK) SpinSPKM.getSelectedItem();
            Grade selectedGrade = (Grade) SpinGradeM.getSelectedItem();
            JenisKayu selectedJenisKayu = (JenisKayu) SpinKayuM.getSelectedItem();

            String idGrade = selectedGrade != null ? selectedGrade.getIdGrade() : null;
            String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
            String idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;

            // Validasi input kosong
            if (noMoulding.isEmpty() || tebal.isEmpty() || lebar.isEmpty() || panjang.isEmpty()) {
                Toast.makeText(Moulding.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Jalankan validasi
            new CheckSPKDataTask(noSPK, tebal, lebar, panjang, idJenisKayu, idGrade) {
                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);

                    if (result.equals("SUCCESS")) {
                        // Jika validasi berhasil, tambahkan data ke daftar
                        addDataDetail(noMoulding);
                        jumlahpcs();
                        m3();
                        Toast.makeText(Moulding.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    } else {
                        // Tampilkan pesan error
                        Toast.makeText(Moulding.this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        });


        //fungsi button Hapus Detail
        BtnHapusDetailM.setOnClickListener(v -> {
            resetDetailData();
        });

        BtnPrintM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validasi input terlebih dahulu
                if (NoMoulding.getQuery() == null || NoMoulding.getQuery().toString().trim().isEmpty()) {
                    Toast.makeText(Moulding.this, "Nomor FJ tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validasi apakah ada data yang akan dicetak
                if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
                    Toast.makeText(Moulding.this, "Tidak ada data untuk dicetak", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cek status HasBeenPrinted di database
                String noMoulding = NoMoulding.getQuery().toString().trim();
                checkHasBeenPrinted(noMoulding, new HasBeenPrintedCallback() {
                    @Override
                    public void onResult(boolean hasBeenPrinted) {
                        try {
                            // Ambil data dari form dengan validasi null
                            String mesinSusun;
                            String jenisKayu = SpinKayuM.getSelectedItem() != null ? SpinKayuM.getSelectedItem().toString().trim() : "";
                            String date = DateM.getText() != null ? DateM.getText().toString().trim() : "";
                            String time = TimeM.getText() != null ? TimeM.getText().toString().trim() : "";
                            String tellyBy = SpinTellyM.getSelectedItem() != null ? SpinTellyM.getSelectedItem().toString().trim() : "";
                            String noSPK = SpinSPKM.getSelectedItem() != null ? SpinSPKM.getSelectedItem().toString().trim() : "";
                            String noSPKasal = SpinSPKAsalM.getSelectedItem() != null ? SpinSPKAsalM.getSelectedItem().toString().trim() : "";
                            String grade = SpinGradeM.getSelectedItem() != null ? SpinGradeM.getSelectedItem().toString().trim() : "";
                            String fisik = SpinFisikM.getSelectedItem() != null ? SpinFisikM.getSelectedItem().toString().trim() : "";
                            String jumlahPcs = JumlahPcsM.getText() != null ? JumlahPcsM.getText().toString().trim() : "";
                            String m3 = M3M.getText() != null ? M3M.getText().toString().trim() : "";
                            if(radioButtonMesinM.isChecked()){
                                mesinSusun = SpinMesinM.getSelectedItem() != null ? SpinMesinM.getSelectedItem().toString().trim() : "";
                            }
                            else{
                                mesinSusun = SpinSusunM.getSelectedItem() != null ? SpinSusunM.getSelectedItem().toString().trim() : "";
                            }

                            // Buat PDF dengan parameter hasBeenPrinted
                            Uri pdfUri = createPdf(noMoulding, jenisKayu, date, time, tellyBy, mesinSusun, noSPK, noSPKasal, grade,
                                    temporaryDataListDetail, jumlahPcs, m3, hasBeenPrinted, fisik);

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
                                                // Update database hanya jika printing selesai dan ini adalah cetakan pertama
                                                if (!hasBeenPrinted) {
                                                    updatePrintStatus(noMoulding);
                                                }
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
                                    Toast.makeText(Moulding.this,
                                            "Error printing: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error";
                            Toast.makeText(Moulding.this,
                                    "Terjadi kesalahan: " + errorMessage,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    //alamat koneksi database, WPS_Test dapat diubah ke database mana saja dengan mengganti sesuai dengan nama database yang diinginkan
    @SuppressLint("NewApi")
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
    
    
    //METHOD MOULDING

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

                // Query untuk validasi data
                String query = "SELECT * FROM MstSPK_dMoulding WHERE NoSPK = ?";
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
        String query = "SELECT " + columnName + " FROM MstSPK_dMoulding WHERE NoSPK = ?";
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
                String query = "SELECT DISTINCT Tebal, Lebar, Panjang FROM MstSPK_dMoulding WHERE NoSPK = ?";
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
                String query = "SELECT LockDimensionMLD FROM MstSPK_h WHERE NoSPK = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, noSPK);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            Integer lockDimension = rs.getInt("LockDimensionMLD");
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
        DateM.setEnabled(true);
        TimeM.setEnabled(true);
        SpinKayuM.setEnabled((true));
        radioButtonMesinM.setEnabled(true);
        radioButtonBSusunM.setEnabled(true);
        SpinMesinM.setEnabled(true);
        SpinSusunM.setEnabled(true);
        SpinTellyM.setEnabled(true);
        SpinSPKM.setEnabled(true);
        SpinSPKAsalM.setEnabled(true);
        SpinGradeM.setEnabled(true);
        SpinProfileM.setEnabled(true);
        DetailTebalM.setEnabled(true);
        DetailLebarM.setEnabled(true);
        DetailPanjangM.setEnabled(true);
        DetailPcsM.setEnabled(true);
        BtnHapusDetailM.setEnabled(true);
        BtnInputDetailM.setEnabled(true);
    }

    
    private void disableForm(){
        DateM.setEnabled(false);
        TimeM.setEnabled(false);
        SpinKayuM.setEnabled((false));
        radioButtonMesinM.setEnabled(false);
        radioButtonBSusunM.setEnabled(false);
        SpinMesinM.setEnabled(false);
        SpinSusunM.setEnabled(false);
        SpinTellyM.setEnabled(false);
        SpinSPKM.setEnabled(false);
        SpinSPKAsalM.setEnabled(false);
        SpinGradeM.setEnabled(false);
        SpinProfileM.setEnabled(false);
        DetailTebalM.setEnabled(false);
        DetailLebarM.setEnabled(false);
        DetailPanjangM.setEnabled(false);
        DetailPcsM.setEnabled(false);
        BtnHapusDetailM.setEnabled(false);
        BtnInputDetailM.setEnabled(false);
        BtnSimpanM.setEnabled(false);

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
        DateM.setText("");
        TimeM.setText("");
        NoMoulding.setQuery("",false);
        NoSTAM.setText("");

        setSpinnerValue(SpinKayuM, "-");
        setSpinnerValue(SpinTellyM, "-");
        setSpinnerValue(SpinSPKM, "-");
        setSpinnerValue(SpinSPKAsalM, "-");
        setSpinnerValue(SpinGradeM, "-");
        setSpinnerValue(SpinProfileM, "-");
        setSpinnerValue(SpinFisikM, "-");
        setSpinnerValue(SpinMesinM, "-");
        setSpinnerValue(SpinSusunM, "-");

        radioButtonBSusunM.setEnabled(false);
        radioButtonMesinM.setEnabled(false);



    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(NoMoulding.getWindowToken(), 0);
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

    private void loadSubmittedData(String noMoulding) {
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
                            "o.NoMoulding, " +
                            "p.IdMesin, " +
                            "m.NamaMesin, " +
                            "s.NoBongkarSusun, " +
                            "f.Profile, " +
                            "w.NamaWarehouse, " +
                            "h.IdJenisKayu, " +
                            "k.Jenis " +
                            "FROM Moulding_h h " +
                            "LEFT JOIN MouldingProduksiOutput o ON h.NoMoulding = o.NoMoulding " +
                            "LEFT JOIN MstGrade g ON h.IdGrade = g.IdGrade " +
                            "LEFT JOIN MouldingProduksi_h p ON o.NoProduksi = p.NoProduksi " +
                            "LEFT JOIN BongkarSusunOutputMoulding s ON h.NoMoulding = s.NoMoulding " +
                            "LEFT JOIN MstMesin m ON p.IdMesin = m.IdMesin " +
                            "LEFT JOIN MstOrgTelly t ON h.IdOrgTelly = t.IdOrgTelly " +
                            "LEFT JOIN MstFJProfile f ON h.IdFJProfile = f.IdFJProfile " +
                            "LEFT JOIN MstWarehouse w ON h.IdFisik = w.IdWarehouse " +
                            "LEFT JOIN MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                            "WHERE h.NoMoulding = ? ";

                    // Query untuk detail
                    String queryDetail = "SELECT Tebal, Lebar, Panjang, JmlhBatang " +
                            "FROM Moulding_d " +
                            "WHERE NoMoulding = ? " +
                            "ORDER BY NoUrut";

                    // Menggunakan try-with-resources untuk header
                    try (PreparedStatement stmt = connection.prepareStatement(queryHeader)) {
                        stmt.setString(1, noMoulding);
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
                                    stmtDetail.setString(1, noMoulding);
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
                                            radioButtonMesinM.setChecked(true);
                                            radioButtonBSusunM.setEnabled(false);
                                        } else {
                                            radioButtonBSusunM.setChecked(true);
                                            radioButtonMesinM.setEnabled(false);
                                        }
                                        // Update header fields
                                        DateM.setText(dateCreate);
                                        TimeM.setText(jam);
                                        setSpinnerValue(SpinTellyM, namaOrgTelly);
                                        setSpinnerValue(SpinSPKM, noSPK);
                                        setSpinnerValue(SpinSPKAsalM, noSPKAsal);
                                        setSpinnerValue(SpinKayuM, namaKayu);
                                        setSpinnerValue(SpinGradeM, namaGrade);
                                        setSpinnerValue(SpinProfileM, namaProfile);
                                        setSpinnerValue(SpinFisikM, namaWarehouse);
                                        setSpinnerValue(SpinMesinM, namaMesin + " - " + noProduksi);
                                        setSpinnerValue(SpinSusunM, noBongkarSusun);

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
//                                            "Data tidak ditemukan untuk NoMoulding: " + noMoulding,
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

    // Interface untuk callback
    interface HasBeenPrintedCallback {
        void onResult(boolean hasBeenPrinted);
    }

    // Method untuk mengecek status HasBeenPrinted secara asynchronous
    private void checkHasBeenPrinted(String noMoulding, Moulding.HasBeenPrintedCallback callback) {
        new Thread(() -> {
            boolean hasBeenPrinted = false;
            Connection connection = null;
            try {
                // Mendapatkan koneksi dari method ConnectionClass
                connection = ConnectionClass();
                if (connection != null) {
                    String query = "SELECT HasBeenPrinted FROM Moulding_h WHERE NoMoulding = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, noMoulding);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                Integer printStatus = rs.getInt("HasBeenPrinted");
                                hasBeenPrinted = (printStatus != null && printStatus == 1);
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

            final boolean finalHasBeenPrinted = hasBeenPrinted;
            runOnUiThread(() -> callback.onResult(finalHasBeenPrinted));
        }).start();
    }

    // Method untuk mengupdate status HasBeenPrinted
    private void updatePrintStatus(String noMoulding) {
        new Thread(() -> {
            Connection connection = null;
            try {
                // Mendapatkan koneksi dari method ConnectionClass
                connection = ConnectionClass();
                if (connection != null) {
                    String query = "UPDATE Moulding_h SET HasBeenPrinted = 1 WHERE NoMoulding = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, noMoulding);
                        int rowsAffected = stmt.executeUpdate();

                        if (rowsAffected > 0) {
                            runOnUiThread(() -> Toast.makeText(Moulding.this,
                                    "Status cetak berhasil diupdate",
                                    Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(Moulding.this,
                                    "Tidak ada data yang diupdate",
                                    Toast.LENGTH_SHORT).show());
                        }
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(Moulding.this,
                            "Koneksi database gagal",
                            Toast.LENGTH_SHORT).show());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("Database", "Error updating HasBeenPrinted status: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(Moulding.this,
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

    private void addDataDetail(String noMoulding) {
        String tebal = DetailTebalM.getText().toString();
        String panjang = DetailPanjangM.getText().toString();
        String lebar = DetailLebarM.getText().toString();
        String pcs = DetailPcsM.getText().toString();

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
            DetailTebalM.setText("");
            DetailPanjangM.setText("");
            DetailLebarM.setText("");
            DetailPcsM.setText("");

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
        if (DetailTebalM != null) {
            DetailTebalM.setText("");
        }
        if (DetailLebarM != null) {
            DetailLebarM.setText("");
        }
        if (DetailPanjangM != null) {
            DetailPanjangM.setText("");
        }
        if (DetailPcsM != null) {
            DetailPcsM.setText("");
        }
    }

    private void saveDataDetailToDatabase(String noMoulding, int noUrut, double tebal, double lebar, double panjang, int pcs) {
        new Moulding.SaveDataTaskDetail().execute(noMoulding, String.valueOf(noUrut), String.valueOf(tebal), String.valueOf(lebar),
                String.valueOf(panjang), String.valueOf(pcs));
    }

    private class SaveDataTaskDetail extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String noMoulding = params[0];
            String noUrut = params[1];
            String tebal = params[2];
            String lebar = params[3];
            String panjang = params[4];
            String pcs = params[5];

            try {
                Connection connection = ConnectionClass();
                if (connection != null) {
                    String query = "INSERT INTO dbo.Moulding_d (NoMoulding, NoUrut, Tebal, Lebar, Panjang, JmlhBatang) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, noMoulding);
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
        NoMoulding.setQuery("", false);
        M3M.setText("");
        JumlahPcsM.setText("");
        CBAfkirM.setChecked(false);
        CBLemburM.setChecked(false);
        SpinKayuM.setSelection(0);
        SpinTellyM.setSelection(0);
        SpinSPKM.setSelection(0);
        SpinSPKAsalM.setSelection(0);
        SpinGradeM.setSelection(0);
        NoSTAM.setText("");
        SpinProfileM.setSelection(0);
        SpinMesinM.setEnabled(false);
        SpinSusunM.setEnabled(false);
        radioGroupM.clearCheck();
    }


    // fungsi menghitung dan menampilkan jumlah M3
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
            TextView M3TextView = findViewById(R.id.M3M);
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

        JumlahPcsM.setText(String.valueOf(totalPcs));
    }

    // fungsi untuk memunculkan waktu dan tanggal realtime
    private void setCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        DateM.setText(currentDate);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        TimeM.setText(currentTime);
    }

    //fungsi untuk memunculkan picker tanggal
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Moulding.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                String selectedDate = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);
                DateM.setText(selectedDate);
                new LoadMesinTask().execute(selectedDate);
                new LoadSusunTask().execute(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }
//fungsi untuk memunculkan picker waktu
    private void showTimePickerDialog() {
        int hour = calendarM.get(Calendar.HOUR_OF_DAY);
        int minute = calendarM.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Moulding.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                calendarM.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendarM.set(Calendar.MINUTE, selectedMinute);

                SimpleDateFormat timeFormat = new SimpleDateFormat(" HH:mm:ss", Locale.getDefault());
                String updatedTime = timeFormat.format(calendarM.getTime());
                TimeM.setText(updatedTime);
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
                        .setFontSize(8)
                        .setMargin(0)
                        .setMultipliedLeading(1.2f)
                        .setTextAlignment(TextAlignment.LEFT));

        // Colon Cell
        Cell colonCell = new Cell()
                .setBorder(Border.NO_BORDER)
                .add(new Paragraph(":")
                        .setFont(font)
                        .setFontSize(8)
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
            if (line.length() + word.length() > 20) { // Batas karakter per baris
                finalText.append(line.toString().trim()).append("\n");
                line = new StringBuilder();
            }
            line.append(word).append(" ");
        }
        finalText.append(line.toString().trim());

        valueCell.add(new Paragraph(finalText.toString())
                .setFont(font)
                .setFontSize(8)
                .setMargin(0)
                .setMultipliedLeading(1.2f)
                .setTextAlignment(TextAlignment.LEFT));

        // Set minimum height untuk konsistensi
        float minHeight = 8f;
        labelCell.setMinHeight(minHeight);
        colonCell.setMinHeight(minHeight);
        valueCell.setMinHeight(minHeight);

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
            float fontSize = 125;
            float textWidth = font.getWidth(watermarkText, fontSize);
            float textHeight = 175;

            // Posisi watermark di tengah halaman
            float centerX = width / 2;
            float centerY = height / 2;

            // Rotasi 45 derajat
            double angle = Math.toRadians(45);
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

    private Uri createPdf(String noMoulding, String jenisKayu, String date, String time, String tellyBy, String mesinSusun, String noSPK, String noSPKasal, String grade, List<DataRow> temporaryDataListDetail, String jumlahPcs, String m3, boolean hasBeenPrinted, String fisik) throws IOException {
        // Validasi parameter wajib
        if (noMoulding == null || noMoulding.trim().isEmpty()) {
            throw new IOException("Nomor Moulding tidak boleh kosong");
        }

        if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
            throw new IOException("Data tidak boleh kosong");
        }

        // Validasi dan set default value untuk parameter opsional
        noMoulding = (noMoulding != null) ? noMoulding.trim() : "-";
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
        String fileName = "S4S_" + noMoulding + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".pdf";
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
                PdfFont timesNewRoman = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
                PdfWriter writer = new PdfWriter(outputStream);
                PdfDocument pdfDocument = new PdfDocument(writer);

                // Ukuran kertas yang disesuaikan secara manual
                float baseHeight = 575; // Tinggi dasar untuk elemen non-tabel (header, footer, margin, dll.)
                float rowHeight = 34; // Tinggi rata-rata per baris data
                float totalHeight = baseHeight + (rowHeight * temporaryDataListDetail.size());

                // Tetapkan ukuran halaman dinamis
                Rectangle pageSize = new Rectangle( PageSize.A6.getWidth(), totalHeight);
                pdfDocument.setDefaultPageSize(new PageSize(pageSize));

                Document document = new Document(pdfDocument);
                document.setMargins(0, 5, 0, 5);

                // Header
                Paragraph judul = new Paragraph("LABEL MOULDING")
                        .setUnderline()
                        .setBold()
                        .setFontSize(8)
                        .setTextAlignment(TextAlignment.CENTER);

                // Hitung lebar yang tersedia
                float pageWidth = PageSize.A6.getWidth() - 20;
                float[] mainColumnWidths = new float[]{pageWidth/2, pageWidth/2};

                Table mainTable = new Table(mainColumnWidths)
                        .setWidth(pageWidth)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setMarginTop(10)
                        .setBorder(Border.NO_BORDER);

                float[] infoColumnWidths = new float[]{50, 5, 80};

                // Buat tabel untuk kolom kiri
                Table leftColumn = new Table(infoColumnWidths)
                        .setWidth(pageWidth/2 - 5)
                        .setBorder(Border.NO_BORDER);

                // Isi kolom kiri
                addInfoRow(leftColumn, "No Moulding", noMoulding, timesNewRoman);
                addInfoRow(leftColumn, "Jenis", jenisKayu, timesNewRoman);
                addInfoRow(leftColumn, "Grade", grade, timesNewRoman);
                addInfoRow(leftColumn, "Fisik", fisik, timesNewRoman);

                // Buat tabel untuk kolom kanan
                Table rightColumn = new Table(infoColumnWidths)
                        .setWidth(pageWidth/2 - 5)
                        .setMarginLeft(20)
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

                Paragraph qrCodeID = new Paragraph(noMoulding).setTextAlignment(TextAlignment.CENTER).setFontSize(8).setMargins(-5, 0, 0, 0).setFont(timesNewRoman);
                Paragraph qrCodeIDbottom = new Paragraph(noMoulding).setTextAlignment(TextAlignment.RIGHT).setFontSize(8).setMargins(-5, 20, 0, 0).setFont(timesNewRoman);

                BarcodeQRCode qrCode = new BarcodeQRCode(noMoulding);
                PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
                Image qrCodeImage = new Image(qrCodeObject).setWidth(75).setHorizontalAlignment(HorizontalAlignment.CENTER).setMargins(-5, 0, 0, 0);

                BarcodeQRCode qrCodeBottom = new BarcodeQRCode(noMoulding);
                PdfFormXObject qrCodeBottomObject = qrCodeBottom.createFormXObject(ColorConstants.BLACK, pdfDocument);
                Image qrCodeBottomImage = new Image(qrCodeBottomObject).setWidth(75).setHorizontalAlignment(HorizontalAlignment.RIGHT).setMargins(-5, 0, 0, 0);

                Paragraph bottomLine = new Paragraph("-----------------------------------------------------------------------------------------------------").setTextAlignment(TextAlignment.CENTER).setFontSize(8).setMargins(0, 0, 0, 15).setFont(timesNewRoman);
                Paragraph outputText = new Paragraph("Output").setTextAlignment(TextAlignment.CENTER).setFontSize(8).setMargins(15, 0, 0, 0).setFont(timesNewRoman);
                Paragraph inputText = new Paragraph("Input").setTextAlignment(TextAlignment.RIGHT).setFontSize(8).setMargins(15, 28, 0, 0).setFont(timesNewRoman);

                // Tambahkan semua elemen ke dokumen


                document.add(judul);
                Log.d("DEBUG_TAG", "Value of hasBeenPrinted: " + hasBeenPrinted);
                if (hasBeenPrinted) {
                    addTextDitheringWatermark(pdfDocument, timesNewRoman);
                }
                document.add(mainTable);
                document.add(table);
                document.add(sumTable);
                document.add(outputText);
                document.add(qrCodeImage);
                document.add(qrCodeID);
                document.add(bottomLine);
                document.add(mainTable);
                document.add(table);
                document.add(sumTable);
                document.add(inputText);
                document.add(qrCodeBottomImage);
                document.add(qrCodeIDbottom);

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

    //fungsi untuk menghapus duplikat pdf, agar ketika di print pdf baru tidak tersimpan 2 pdf berbeda
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

    //menyimpan bongkar susun berdasarkan nomor moulding
    private class SaveBongkarSusunTask extends AsyncTask<Void, Void, Boolean> {
        private String noBongkarSusun;
        private String noMoulding;

        public SaveBongkarSusunTask(String noBongkarSusun, String noMoulding) {
            this.noBongkarSusun = noBongkarSusun;
            this.noMoulding = noMoulding;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();

            if (con != null) {
                try {

                    //query database sesuai yang dibutuhkan di database
                    String query = "INSERT INTO dbo.BongkarSusunOutputMoulding (NoMoulding, NoBongkarSusun) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noMoulding);
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
            if (success) {
                Toast.makeText(Moulding.this, "Data berhasil disimpan.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //menyimpan nomor produksi yang dipilih berdasarkan nomor moulding nya
    private class SaveToDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private String noProduksi, noMoulding;

        public SaveToDatabaseTask(String noProduksi, String noMoulding) {
            this.noProduksi = noProduksi;
            this.noMoulding = noMoulding;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.MouldingProduksiOutput (NoProduksi, NoMoulding) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noProduksi);
                    ps.setString(2, noMoulding);

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
                Toast.makeText(Moulding.this, "Data berhasil disimpan ke database.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Moulding.this, "Gagal menyimpan data ke database.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //mengupdate data berdasarkan nomor moulding
    private class UpdateDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private String noMoulding, dateCreate, time, idTelly, noSPK, noSPKasal, idGrade, idJenisKayu, idFJProfile;
        private int isReject, isLembur, IdUOMTblLebar, IdUOMPanjang;

        public UpdateDatabaseTask(String noMoulding, String dateCreate, String time, String idTelly, String noSPK, String noSPKasal,
                                  String idGrade, String idJenisKayu, String idFJProfile,
                                  int isReject, int isLembur, int IdUOMTblLebar, int IdUOMPanjang ){
            this.noMoulding = noMoulding;
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
                    String query = "UPDATE dbo.Moulding_h SET DateCreate = ?, Jam = ?, IdOrgTelly = ?, NoSPK = ?, NoSPKAsal = ?, IdGrade = ?, " +
                            "IdFJProfile = ?, IdFisik = 4, IdJenisKayu = ?, IdWarehouse = 4, IsReject = ?, IsLembur = ?, IdUOMTblLebar = ?, IdUOMPanjang = ? WHERE NoMoulding = ?";

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
                    ps.setString(13, noMoulding);

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

    //menyimpan dan mengambil nomor moulding
    private class SetAndSaveNoMouldingTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            String newNoMoulding = null;
            if (con != null) {
                try {
                    String query = "SELECT MAX(NoMoulding) FROM dbo.Moulding_h";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        String lastNoMoulding = rs.getString(1);

                        if (lastNoMoulding != null && lastNoMoulding.startsWith("T.")) {
                            String numericPart = lastNoMoulding.substring(2);
                            int numericValue = Integer.parseInt(numericPart);
                            int newNumericValue = numericValue + 1;

                            newNoMoulding = "T." + String.format("%06d", newNumericValue);
                        }
                    }

                    rs.close();
                    ps.close();

                    if (newNoMoulding != null) {
                        String insertQuery = "INSERT INTO dbo.Moulding_h (NoMoulding) VALUES (?)";
                        PreparedStatement insertPs = con.prepareStatement(insertQuery);
                        insertPs.setString(1, newNoMoulding);
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
            return newNoMoulding;
        }

        @Override
        protected void onPostExecute(String newNoMoulding) {
            if (newNoMoulding != null) {
                NoMoulding.setQuery(newNoMoulding, true);
                NoMoulding.setVisibility(View.GONE);
                NoMoulding_display.setVisibility(View.VISIBLE);
                NoMoulding_display.setText(newNoMoulding);
                NoMoulding_display.setEnabled(false);
                Toast.makeText(Moulding.this, "NoMoulding berhasil diatur dan disimpan.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Error", "Failed to set or save NoMoulding.");
                Toast.makeText(Moulding.this, "Gagal mengatur atau menyimpan NoMoulding.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //memuat spinner Jenis Kayu
    public class LoadJenisKayuTask extends AsyncTask<Void, Void, List<JenisKayu>> {
        @Override
        protected List<JenisKayu> doInBackground(Void... voids) {
            List<JenisKayu> jenisKayuList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT IdJenisKayu, Jenis FROM dbo.MstJenisKayu WHERE enable = 1";
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

            ArrayAdapter<JenisKayu> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, jenisKayuList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinKayuM.setAdapter(adapter);
            SpinKayuM.setSelection(0);
        }
    }

    //menampilkan jenis kayu ketika di klik tombol search
    public class LoadJenisKayuTask2 extends AsyncTask<String, Void, List<JenisKayu>> {
        private String noMoulding;


        public LoadJenisKayuTask2(String noMoulding) {
            this.noMoulding = noMoulding;
        }

        @Override
        protected List<JenisKayu> doInBackground(String... params) {
            List<JenisKayu> jenisKayuList = new ArrayList<>();
            Connection con = ConnectionClass(); // Ensure this method establishes a database connection

            if (con != null) {
                try {
                    String query = "SELECT j.IdJenisKayu, j.Jenis " +
                            "FROM dbo.MstJenisKayu AS j " +
                            "INNER JOIN dbo.Moulding_h AS h ON h.IdJenisKayu = j.IdJenisKayu " +
                            "WHERE h.NoMoulding = ? AND j.enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noMoulding);

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
                ArrayAdapter<JenisKayu> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, jenisKayuList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinKayuM.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load jenis kayu.");
            }
        }
    }

    //memuat spinner telly
private class LoadTellyTask extends AsyncTask<Void, Void, List<Telly>> {
        @Override
        protected List<Telly> doInBackground(Void... voids) {
            List<Telly> tellyList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT IdOrgTelly, NamaOrgTelly FROM dbo.MstOrgTelly WHERE enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
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
            // Tambahkan elemen dummy di awal
            Telly dummyTelly = new Telly("", "PILIH");
            tellyList.add(0, dummyTelly);

            // Buat adapter dengan data yang dimodifikasi
            ArrayAdapter<Telly> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, tellyList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Set adapter ke spinner
            SpinTellyM.setAdapter(adapter);

            // Atur spinner untuk menampilkan elemen pertama ("Pilih") secara default
            SpinTellyM.setSelection(0);
        }
    }


    //menampilkan data telly berdasarkan nomor moulding saat di klik tombol Search
    private class LoadTellyTask2 extends AsyncTask<String, Void, List<Telly>> {
        private String noMoulding;

        public LoadTellyTask2(String noMoulding) {
            this.noMoulding = noMoulding;
        }

        @Override
        protected List<Telly> doInBackground(String... params) {
            List<Telly> tellyList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT t.IdOrgTelly, t.NamaOrgTelly " +
                            "FROM dbo.MstOrgTelly AS t " +
                            "INNER JOIN dbo.Moulding_h AS h ON h.IdOrgTelly = t.IdOrgTelly " +
                            "WHERE h.NoMoulding= ? AND t.enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noMoulding);

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
                ArrayAdapter<Telly> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, tellyList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinTellyM.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load telly data.");
            }
        }
    }

    //memuat data SPK di Spinner
    private class LoadSPKTask extends AsyncTask<Void, Void, List<SPK>> {
        @Override
        protected List<SPK> doInBackground(Void... voids) {
            List<SPK> spkList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT NoSPK FROM dbo.MstSPK_h WHERE enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
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
            SPK dummySPK = new SPK("PILIH");
            spkList.add(0, dummySPK);

            ArrayAdapter<SPK> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, spkList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinSPKM.setAdapter(adapter);
            SpinSPKM.setSelection(0);
        }
    }

    private class LoadSPKAsalTask extends AsyncTask<Void, Void, List<SPKAsal>> {
        @Override
        protected List<SPKAsal> doInBackground(Void... voids) {
            List<SPKAsal> spkAsalList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT NoSPK FROM dbo.MstSPK_h WHERE enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String noSPKasal = rs.getString("NoSPK");

                        SPKAsal spkAsal = new SPKAsal(noSPKasal);
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

            ArrayAdapter<SPKAsal> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, spkAsalList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinSPKAsalM.setAdapter(adapter);
            SpinSPKAsalM.setSelection(0);
        }
    }

    //menampilkan data SPK berdasarkan nomor moulding saat di klik tombol Search
    private class LoadSPKTask2 extends AsyncTask<Void, Void, List<SPK>> {
        private String noMoulding;

        public LoadSPKTask2(String noMoulding) {
            this.noMoulding = noMoulding;
        }

        @Override
        protected List<SPK> doInBackground(Void... params) {
            List<SPK> spkList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT NoSPK FROM dbo.Moulding_h WHERE NoMoulding = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noMoulding);

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
                ArrayAdapter<SPK> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, spkList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSPKM.setAdapter(adapter);

                SpinSPKM.setEnabled(true);
            } else {
                Log.e("Error", "No SPK data found for the provided NoMoulding.");
                SpinSPKM.setAdapter(null);
                SpinSPKM.setEnabled(false);
                Toast.makeText(Moulding.this, "Tidak ada data SPK yang ditemukan.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // //memuat spinner Profile
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

            ArrayAdapter<Profile> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, profileList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinProfileM.setAdapter(adapter);
            SpinProfileM.setSelection(0);
        }
    }

    //menampilkan data profile berdasarkan nomor moulding saat di klik tombol Search
    private class LoadProfileTask2 extends AsyncTask<String, Void, List<Profile>> {
        private String noMoulding;

        public LoadProfileTask2(String noMoulding) {
            this.noMoulding = noMoulding;
        }

        @Override
        protected List<Profile> doInBackground(String... voids) {
            List<Profile> profileList = new ArrayList<>();
            Connection con = ConnectionClass(); // Assumes this method exists to establish a DB connection

            if (con != null) {
                try {
                    String query = "SELECT p.Profile, p.IdFJProfile " +
                            "FROM dbo.MstFJProfile AS p " +
                            "INNER JOIN dbo.Moulding_h AS h ON h.IdFJProfile = p.IdFJProfile " +
                            "WHERE h.NoMoulding = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noMoulding);

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
                ArrayAdapter<Profile> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, profileList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinProfileM.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load profile data.");
            }
        }
    }

    //memuat spinner fisik / warehouse
    private class LoadFisikTask extends AsyncTask<Void, Void, List<Fisik>> {
        @Override
        protected List<Fisik> doInBackground(Void... voids) {
            List<Fisik> fisikList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT NamaWarehouse FROM dbo.MstWarehouse WHERE IdWarehouse = 4";
                    PreparedStatement ps = con.prepareStatement(query);
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
                ArrayAdapter<Fisik> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, fisikList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinFisikM.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load fisik data.");
            }
        }
    }

    //menampilkan data profile berdasarkan nomor moulding saat di klik tombol Search
    private class LoadFisikTask2 extends AsyncTask<String, Void, List<Fisik>> {
        private String noMoulding;

        public LoadFisikTask2(String noMoulding) {
            this.noMoulding = noMoulding;
        }

        @Override
        protected List<Fisik> doInBackground(String... params) {
            List<Fisik> fisikList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT mw.NamaWarehouse " +
                            "FROM dbo.MstWarehouse mw " +
                            "INNER JOIN dbo.Moulding_h moulding ON mw.IdWarehouse = moulding.IdWarehouse " +
                            "WHERE moulding.noMoulding = ?";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noMoulding);


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
                ArrayAdapter<Fisik> adapter = new ArrayAdapter<>(Moulding.this,
                        android.R.layout.simple_spinner_item, fisikList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinFisikM.setAdapter(adapter);
            } else {
                Log.e("Error", "No warehouse found.");
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(Moulding.this,
                        android.R.layout.simple_spinner_item, new String[]{"Tidak ada Fisik"});
                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinFisikM.setAdapter(emptyAdapter);
            }
        }
    }


    //memuat spinner Grade
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

                    String category = "Moulding";

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

            ArrayAdapter<Grade> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, gradeList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SpinGradeM.setAdapter(adapter);
            SpinGradeM.setSelection(0);
        }
    }

    //menampilkan data grade berdasarkan nomor moulding saat di klik tombol Search
    private class LoadGradeTask2 extends AsyncTask<String, Void, List<Grade>> {
        private String noMoulding;

        public LoadGradeTask2(String noMoulding) {
            this.noMoulding = noMoulding;
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

                    String category = "Moulding";

                    String query = "SELECT DISTINCT a.IdGrade, a.NamaGrade " +
                            "FROM MstGrade a " +
                            "INNER JOIN MstGrade_d b ON a.IdGrade = b.IdGrade " +
                            "WHERE a.Enable = 1 AND b.IdJenisKayu = ? AND b.Category = ? AND b.NoMoulding = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setInt(1, idJenisKayu);
                    ps.setString(2, category);
                    ps.setString(3, noMoulding);
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
                ArrayAdapter<Grade> adapter = new ArrayAdapter<>(Moulding.this,
                        android.R.layout.simple_spinner_item, gradeList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeM.setAdapter(adapter);
            } else {
                Log.e("Error", "Tidak ada grade");
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(Moulding.this,
                        android.R.layout.simple_spinner_item, new String[]{"Tidak ada grade"});
                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeM.setAdapter(emptyAdapter);
            }
        }
    }


    // memuat spinne mesin
    private class LoadMesinTask extends AsyncTask<String, Void, List<Mesin>> {
        @Override
        protected List<Mesin> doInBackground(String... params) {
            List<Mesin> mesinList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String selectedDate = params[0];

                    String query = "SELECT a.IdMesin, b.NamaMesin, a.NoProduksi FROM dbo.MouldingProduksi_h a " +
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
                ArrayAdapter<Mesin> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinMesinM.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load mesin data.");
                SpinMesinM.setAdapter(null);
            }
        }
    }

    //menampilkan data mesin berdasarkan nomor moulding saat di klik tombol Search
    private class LoadMesinTask2 extends AsyncTask<Void, Void, List<Mesin>> {
        private String noMoulding;

        public LoadMesinTask2(String noMoulding) {
            this.noMoulding = noMoulding;
        }

        @Override
        protected List<Mesin> doInBackground(Void... params) {
            List<Mesin> mesinList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT b.NoProduksi, d.NamaMesin FROM Moulding_h a " +
                            "INNER JOIN MouldingProduksiOutput b ON b.NoMoulding = a.NoMoulding " +
                            "INNER JOIN MouldingProduksi_h c ON c.NoProduksi = b.NoProduksi " +
                            "INNER JOIN MstMesin d ON d.IdMesin = c.IdMesin " +
                            "WHERE a.NoMoulding = ?";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noMoulding);

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
                ArrayAdapter<Mesin> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinMesinM.setAdapter(adapter);

                radioButtonMesinM.setEnabled(true);
                radioButtonBSusunM.setEnabled(false);
            } else {
                Log.e("Error", "Failed to load mesin data.");
                radioButtonMesinM.setEnabled(false);
                radioButtonBSusunM.setEnabled(false);

                Toast.makeText(Moulding.this, "Tidak ada data mesin yang ditemukan.", Toast.LENGTH_SHORT).show();
                SpinMesinM.setAdapter(null);
            }
        }
    }

    //memuat spinner bongkar susun
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
                ArrayAdapter<Susun> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, susunList);
                SpinSusunM.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load susun data");
                SpinSusunM.setAdapter(null);
            }
        }
    }
    //menampilkan data bongkar susun berdasarkan nomor moulding saat di klik tombol Search
    private class LoadSusunTask2 extends AsyncTask<Void, Void, List<Susun>> {
        private String noMoulding;

        public LoadSusunTask2(String noMoulding) {
            this.noMoulding = noMoulding;
        }

        @Override
        protected List<Susun> doInBackground(Void... params) {
            List<Susun> susunList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT NoBongkarSusun FROM dbo.BongkarSusunOutputMoulding WHERE NoMoulding = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noMoulding);
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
                ArrayAdapter<Susun> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, susunList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSusunM.setAdapter(adapter);

                radioButtonMesinM.setEnabled(false);
                radioButtonBSusunM.setEnabled(true);
            } else {
                Log.e("Error", "Failed to load susun data.");
                radioButtonMesinM.setEnabled(false);
                radioButtonBSusunM.setEnabled(false);

                Toast.makeText(Moulding.this, "Tidak ada data susun yang ditemukan.", Toast.LENGTH_SHORT).show();
                SpinSusunM.setAdapter(null);
            }
        }
    }

//deklarasikan semua variable yang digunakan dalam semua class penampilan spinner dan text diatas ke bawah ini sesuai dengan nama class nya
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

        public SPK(String noSPK) {
            this.noSPK = noSPK;
        }

        public String getNoSPK() {
            return noSPK;
        }

        @Override
        public String toString() {
            return noSPK;
        }
    }

    public class SPKAsal {
        private String noSPKasal;

        public SPKAsal(String noSPKasal) {
            this.noSPKasal = noSPKasal;
        }

        public String getNoSPKAsal() {
            return noSPKasal;
        }

        @Override
        public String toString() {
            return noSPKasal;
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
        private String idWarehouse;
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


}