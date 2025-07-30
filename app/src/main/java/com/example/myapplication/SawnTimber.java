package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import android.graphics.Color;
import android.content.Context;
import android.widget.ImageView;

import java.io.OutputStream;

import com.example.myapplication.api.ProsesProduksiApi;
import com.example.myapplication.api.SawnTimberApi;
import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.model.OutputDataST;
import com.example.myapplication.model.TooltipData;
import com.example.myapplication.utils.DateTimeUtils;

import android.view.inputmethod.InputMethodManager;
import android.content.SharedPreferences;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Collections;


import com.itextpdf.kernel.pdf.canvas.draw.DashedLine;
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
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.geom.Rectangle;


import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SawnTimber extends AppCompatActivity {

    private String username;
    private String noST;
    private Button BtnSimpanST;
    private Button BtnBatalST;
    private Button BtnDataBaruST;
    private Button BtnPrintST;
    private Button BtnTambahStickST;
    private Button BtnClearDetailST;
    private SearchView NoST;
    private SearchView NoKayuBulat;
    private EditText Supplier;
    private EditText NoTruk;
    private EditText NoPlatTruk;
    private EditText NoSuket;
    private EditText TglStickBundel;
    private EditText TglVacuum;
    private EditText JenisKayuKB;
    private EditText JumlahStick;
    private Spinner SpinKayu;
    private Spinner SpinSPK;
    private Spinner SpinLokasi;
    private Spinner SpinTelly;
    private Spinner SpinStickBy;
    private Spinner SpinGrade;
    private TextView JumlahPcsST;
    private TextView M3;
    private TextView Ton;
    private TextView DetailTebalST;
    private TextView DetailLebarST;
    private TextView DetailPanjangST;
    private TextView DetailPcsST;
    private TextView sisaPCS;
    private boolean isDataBaruClickedST = false;
    private int currentTableNo = 1;
    private TableLayout Tabel;
    private int rowCount = 0;
    private TableLayout Tabel2;
    private CheckBox CBKering;
    private CheckBox CBStick;
    private CheckBox CBUpah;
    private CheckBox cbSLP;
    private CheckBox cbVacuum;
    private Button BtnInputDetailST;
    private RadioGroup radioGroupUOMTblLebar;
    private RadioButton radioMillimeter;
    private RadioButton radioInch;
    private RadioButton radioFeet;
    private RadioGroup radioBagusKulit;
    private RadioButton radioBagus;
    private RadioButton radioKulit;
    boolean isCreateMode = false;
    private EditText NoST_display;
    private EditText NoKB_display;
    private TableLayout TabelInputPjgPcs;
    private List<DataRow> temporaryDataListDetail = new ArrayList<>();
    private Button addRowButton;
    private CardView inner_card_detail;
    private CardView inner_card_top_left;
    private CardView inner_card_top_center;
    private CardView inner_card_top_right;
    private EditText remarkLabel;
    private String rawDate;
    private String rawDateVacuum;
    private TextView judulLabel;
    private EditText noPenerimaanST;
    private LinearLayout fieldKB;
    private LinearLayout fieldJenisKB;
    private LinearLayout fieldNoSuket;
    private EditText Customer;
    private int labelVersion;
    private ImageButton btnScrollBottom;
    private ScrollView scrollHeader;
    private Spinner SpinBongkarSusun;
    private CheckBox cbBongkarSusun;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private TableLayout TableOutput;
    private CardView inner_card_label_list;
    private ImageButton btnSwapToUOM;
    private ImageButton btnSwapToLabelList ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sawn_timber);

        BtnBatalST = findViewById(R.id.BtnBatalST);
        BtnDataBaruST = findViewById(R.id.BtnDataBaruST);
        BtnPrintST = findViewById(R.id.BtnPrintST);
        BtnTambahStickST = findViewById(R.id.BtnTambahStickST);
        BtnSimpanST = findViewById(R.id.BtnSimpanST);
        BtnInputDetailST = findViewById(R.id.BtnInputDetailST);
        NoST = findViewById(R.id.NoST);
        NoSuket = findViewById(R.id.NoSuket);
        NoTruk = findViewById(R.id.NoTruk);
        NoKayuBulat = findViewById(R.id.NoKayuBulat);
        NoPlatTruk = findViewById(R.id.NoPlatTruk);
        DetailLebarST = findViewById(R.id.DetailLebarST);
        DetailTebalST = findViewById(R.id.DetailTebalST);
        DetailPanjangST = findViewById(R.id.DetailPanjangST);
        DetailPcsST = findViewById(R.id.DetailPcsST);
        Supplier = findViewById(R.id.Supplier);
        TglStickBundel = findViewById(R.id.TglStickBundel);
        TglVacuum = findViewById(R.id.TglVacuum);
        SpinGrade = findViewById(R.id.SpinGrade);
        SpinLokasi = findViewById(R.id.SpinLokasi);
        SpinSPK = findViewById(R.id.SpinSPK);
        SpinKayu = findViewById(R.id.SpinKayu);
        SpinStickBy = findViewById(R.id.SpinStickBy);
        SpinTelly = findViewById(R.id.SpinTelly);
        JumlahStick = findViewById(R.id.JumlahStick);
        JumlahPcsST = findViewById(R.id.JumlahPcsST);
        JenisKayuKB = findViewById(R.id.JenisKayuKB);
        Tabel = findViewById(R.id.Tabel);
        Tabel2 = findViewById(R.id.Tabel2);
        CBKering = findViewById(R.id.CBKering);
        CBStick = findViewById(R.id.CBStick);
        CBUpah = findViewById(R.id.CBUpah);
        radioMillimeter = findViewById(R.id.radioMillimeter);
        radioInch = findViewById(R.id.radioInch);
        radioFeet = findViewById(R.id.radioFeet);
        radioBagus = findViewById(R.id.radioBagus);
        radioKulit = findViewById(R.id.radioKulit);
        radioBagusKulit = findViewById(R.id.radioGroupBagusKulit);
        M3 = findViewById(R.id.M3ST);
        Ton = findViewById(R.id.Ton);
        NoST_display = findViewById(R.id.NoST_display);
        NoKB_display = findViewById(R.id.NoKB_display);
        TabelInputPjgPcs = findViewById(R.id.TabelInputPjgPcs);
        addRowButton = findViewById(R.id.addRowButton);
        remarkLabel = findViewById(R.id.remarkLabel);
        cbSLP = findViewById(R.id.cbSLP);
        cbVacuum = findViewById(R.id.cbVacuum);
        BtnClearDetailST = findViewById(R.id.BtnClearDetailST);
        sisaPCS = findViewById(R.id.sisaPCS);
        judulLabel = findViewById(R.id.judulLabel);
        fieldKB = findViewById(R.id.fieldKB);
        fieldJenisKB = findViewById(R.id.fieldJenisKB);
        fieldNoSuket = findViewById(R.id.fieldNoSuket);
        Customer = findViewById(R.id.Customer);
        btnScrollBottom = findViewById(R.id.btnScrollBottom);
        scrollHeader = findViewById(R.id.scrollHeader);
        SpinBongkarSusun = findViewById(R.id.SpinBongkarSusun);
        cbBongkarSusun = findViewById(R.id.cbBongkarSusun);
        TableOutput = findViewById(R.id.TableOutput);
        inner_card_detail = findViewById(R.id.inner_card_detail);
        inner_card_top_left = findViewById(R.id.inner_card_top_left);
        inner_card_top_right = findViewById(R.id.inner_card_top_right);

        inner_card_top_center = findViewById(R.id.inner_card_top_center);
        inner_card_label_list = findViewById(R.id.inner_card_label_list);
        btnSwapToUOM = findViewById(R.id.btnSwapToUOM);
        btnSwapToLabelList = findViewById(R.id.btnSwapToLabelList);
        inner_card_detail.setVisibility(View.GONE);
        NoST_display.setVisibility(View.GONE);
        SpinLokasi.setEnabled(false);
        inner_card_top_right.setVisibility(View.GONE);
        disableForm();


        //VERSI ST

        Intent intent = getIntent();

        String noPenST = intent.getStringExtra("noPenST"); // Ambil integer (default -1 jika tidak ada)
        labelVersion = intent.getIntExtra("labelVersion", -1); // Ambil string

        if (labelVersion == -1) {
            judulLabel.setText("LABEL ST");
        }
        else if (labelVersion == 1) {
            judulLabel.setText("LABEL ST (Pembelian)");
            fieldKB.setVisibility(View.GONE);
            fieldJenisKB.setVisibility(View.GONE);
            fieldNoSuket.setVisibility(View.GONE);
            loadPenerimaanSTPembelian(noPenST);
        }
        else if (labelVersion == 2) {
            judulLabel.setText("LABEL ST (Upah)");
            fieldKB.setVisibility(View.GONE);
            fieldJenisKB.setVisibility(View.GONE);
            fieldNoSuket.setVisibility(View.GONE);
            loadPenerimaanSTUpah(noPenST);
        }

        setCurrentDateTime();
        onClickDateOutput(rawDate);

        noPenerimaanST = findViewById(R.id.noPenerimaanST);
        noPenerimaanST.setText(noPenST);

        btnScrollBottom.setOnClickListener(v -> {
            scrollHeader.post(() -> scrollHeader.smoothScrollTo(0, scrollHeader.getBottom()));
        });

        scrollHeader.getViewTreeObserver().addOnScrollChangedListener(() -> {
            boolean isAtBottom = scrollHeader.getScrollY() == (scrollHeader.getChildAt(0).getHeight() - scrollHeader.getHeight());

            if (isAtBottom && btnScrollBottom.getVisibility() == View.VISIBLE) {
                btnScrollBottom.animate()
                        .alpha(0f)
                        .setDuration(300)
                        .withEndAction(() -> btnScrollBottom.setVisibility(View.GONE))
                        .start();
            } else if (!isAtBottom && btnScrollBottom.getVisibility() != View.VISIBLE) {
                btnScrollBottom.setAlpha(0f); // Set transparan dulu
                btnScrollBottom.setVisibility(View.VISIBLE); // Munculkan
                btnScrollBottom.animate()
                        .alpha(0.5f)
                        .setDuration(300)
                        .start();
            }
        });

        // Menangani aksi 'Enter' pada keyboard
        DetailTebalST.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Jika tombol 'Enter' ditekan, pindahkan fokus ke DetailLebarS4S
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Pastikan DetailLebarS4S bisa menerima fokus
                    DetailLebarST.requestFocus();
                    return true; // Menunjukkan bahwa aksi sudah ditangani
                }
                return false;
            }
        });

        DetailLebarST.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    DetailPanjangST.requestFocus();
                    return true;
                }
                return false;
            }
        });

        DetailPanjangST.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    DetailPcsST.requestFocus();
                    return true;
                }
                return false;
            }
        });

        DetailPcsST.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {  // Mengubah ke IME_ACTION_DONE
                    // Ambil input dari AutoCompleteTextView
                    String tebal = DetailTebalST.getText().toString().trim();
                    String lebar = DetailLebarST.getText().toString().trim();
                    String panjang = DetailPanjangST.getText().toString().trim();
                    String pcs = DetailPcsST.getText().toString().trim();

                    // Validasi input kosong
                    if (tebal.isEmpty() || lebar.isEmpty() || panjang.isEmpty()) {
                        Toast.makeText(SawnTimber.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    addDataDetail(tebal, lebar, panjang, pcs);
                    jumlahPcsST();
                    m3();
                    ton();

                    return true;
                }
                return false;
            }
        });


        NoKayuBulat.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadKayuBulat(query);
//                closeKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadKayuBulat(newText);
                NoKayuBulat.setBackgroundResource(R.drawable.border_input);
                return true;
            }
        });

        NoST.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!isCreateMode) {
                    loadSawnTimber(query);
//                    closeKeyboard();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!isCreateMode) {
                    if(!newText.isEmpty()){
                        disableForm();
                        loadSawnTimber(newText);
                    }
                    else{
//                        enableForm();
                    }
                }
                return true;
            }
        });

        BtnDataBaruST.setOnClickListener(v -> {
            setCreateMode(true);
            setCurrentDateTime();
            enableForm();

            new LoadSPKTask().execute();
            new LoadTellyTask().execute();
            new LoadStickByTask().execute();
            new LoadJenisKayuTask().execute();
            new LoadGradeStickTask().execute();
            new LoadLokasiTask().execute();
            new LoadSusunTask().execute(rawDate);

            setCurrentDateTimeVacuum();

            addRowButton.setVisibility(View.GONE);
            BtnSimpanST.setEnabled(true);
            BtnTambahStickST.setEnabled(true);
            BtnInputDetailST.setEnabled(true);
            BtnBatalST.setEnabled(true);
            BtnPrintST.setEnabled(false);
            BtnDataBaruST.setVisibility(View.GONE);
            BtnSimpanST.setVisibility(View.VISIBLE);
            NoST.setVisibility(View.GONE);
            NoST_display.setVisibility(View.VISIBLE);
            NoST_display.setEnabled(false);
            NoST_display.setText("");
            NoKayuBulat.setQuery("", false);
            cbSLP.setChecked(false);
            cbVacuum.setChecked(false);
            cbBongkarSusun.setChecked(false);
            radioBagus.setChecked(false);
            radioKulit.setChecked(false);
            resetDetailData();
            resetGradeData();
            DetailPanjangST.setText("4");
            remarkLabel.setText("");
            addRowButton.setVisibility(View.GONE);


            if (labelVersion == -1) {
                noPenerimaanST.setText("");
            }

        });

        BtnInputDetailST.setOnClickListener(view -> {
            String tebal = DetailTebalST.getText().toString();
            String lebar = DetailLebarST.getText().toString();
            String panjang = DetailPanjangST.getText().toString();
            String pcs = DetailPcsST.getText().toString();

            addDataDetail(tebal, lebar, panjang, pcs);
            jumlahPcsST();
            m3();
            ton();
        });

        BtnTambahStickST.setOnClickListener(view -> {
            addDataGrade(noST);
        });

        JumlahStick.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Tangani enter atau tombol "Done" di keyboard
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                    addDataGrade(noST);

                    closeKeyboard();

                    return true;
                }
                return false;
            }
        });

        BtnClearDetailST.setOnClickListener(view -> {
            TabelInputPjgPcs.removeAllViews();
            Tabel.removeAllViews();
            temporaryDataListDetail.clear();

            jumlahPcsST();
            m3();
            ton();

            BtnInputDetailST.setEnabled(true);
            DetailTebalST.setEnabled(true);
            DetailLebarST.setEnabled(true);
            DetailPcsST.setEnabled(true);
            DetailPanjangST.setEnabled(true);

            DetailTebalST.setText("");
            DetailLebarST.setText("");
            DetailPcsST.setText("");

            addRowButton.setVisibility(View.GONE);
            sisaPCS.setText("Sisa : 0");

        });

        TglStickBundel.setOnClickListener(v -> showDatePickerDialogStick());

        TglVacuum.setOnClickListener(v -> showDatePickerDialogVacuum());

        SpinGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SpinLokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Lokasi selectedLokasi = (Lokasi) parent.getItemAtPosition(position);
//
//                String selectedLokasiId = selectedLokasi.getIdLokasi();
//                String selectedNamaLokasi = selectedLokasi.getNamaLokasi();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        BtnBatalST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isCreateMode = false;

                disableForm();
                clearTableData();
                resetGradeData();
                resetDetailData();

                BtnDataBaruST.setEnabled(true);
                BtnSimpanST.setEnabled(false);
                BtnPrintST.setEnabled(false);
                NoST.setVisibility(View.VISIBLE);
                NoST_display.setVisibility(View.GONE);
                BtnDataBaruST.setVisibility(View.VISIBLE);
                BtnSimpanST.setVisibility(View.GONE);
                TabelInputPjgPcs.removeAllViews();
                BtnPrintST.setEnabled(true);
            }
        });

        BtnSimpanST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noKayuBulat = NoKayuBulat.getQuery().toString().trim();
                String noPenST = noPenerimaanST.getText().toString().trim();
                JenisKayu selectedJenisKayu = (JenisKayu) SpinKayu.getSelectedItem();
                String jenisKayu = selectedJenisKayu.getIdJenisKayu();
                String namaJenisKayu = selectedJenisKayu.getNamaJenisKayu();
                SPK selectedSPK = (SPK) SpinSPK.getSelectedItem();
                String noSPK = selectedSPK.getNoSPK();
                Susun selectedSusun = (Susun) SpinBongkarSusun.getSelectedItem();
                String noBongkarSusun = selectedSusun != null ? selectedSusun.getNoBongkarSusun() : null;
                String telly = ((Telly) SpinTelly.getSelectedItem()).getIdOrgTelly();
                String stickBy = ((StickBy) SpinStickBy.getSelectedItem()).getIdStickBy();
                String dateCreate = rawDate;
                String dateVacuum = rawDateVacuum;
                String remark = remarkLabel.getText().toString();
                String isSLP = cbSLP.isChecked() ? "1" : "0";
                String isVacuum = cbVacuum.isChecked() ? dateVacuum : null;
                String isSticked = CBStick.isChecked() ? "1" : "0";
                String isKering = CBKering.isChecked() ? "1" : "0";

                // Validasi: Cek apakah ada field yang kosong
                boolean valid = true;
                NoKayuBulat.setBackgroundResource(R.drawable.border_input);


                if (noKayuBulat.isEmpty() && labelVersion == -1 && !namaJenisKayu.toLowerCase().contains("kayu lat")) {
                    NoKayuBulat.setBackgroundResource(R.drawable.spinner_error);  // Background merah untuk error
                    valid = false;
                }

                if (jenisKayu.isEmpty()) {
                    // Menambahkan error pada spinner dan memberi pesan kesalahan
                    SpinKayu.setBackgroundResource(R.drawable.spinner_error);
                    valid = false;
                }

                if (stickBy.isEmpty()) {
                    // Menambahkan error pada spinner dan memberi pesan kesalahan
                    SpinStickBy.setBackgroundResource(R.drawable.spinner_error);
                    valid = false;
                }

                if (noSPK.isEmpty() || noSPK.equals("PILIH")) {
                    // Menambahkan error pada spinner dan memberi pesan kesalahan
                    SpinSPK.setBackgroundResource(R.drawable.spinner_error);
                    valid = false;
                }

//                if (temporaryDataListGrade.isEmpty() && !namaJenisKayu.toLowerCase().contains("kayu lat")) {
//                    valid = false;
//                }

                if (valid) {
                    checkKayuBulatExists(noKayuBulat, namaJenisKayu, exists -> {

                        CountDownLatch latch = new CountDownLatch(1);
                        setAndSaveNoST(latch);
                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (latch.getCount() == 0) {
                            if (exists) {
                                String isBagusKulit = "0";

                                if (selectedJenisKayu.getNamaJenisKayu().toLowerCase().contains("kayu lat")) {
                                    isBagusKulit = radioBagus.isChecked() ? "1" : (radioKulit.isChecked() ? "2" : "0");
                                }

                                new UpdateDataTask().execute(noST, noKayuBulat, jenisKayu, noSPK, telly, stickBy, dateCreate, isVacuum, remark, isSLP, isSticked, isKering, isBagusKulit);

                                for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                                    DataRow dataRow = temporaryDataListDetail.get(i);
                                    saveDataToDatabase(noST, i + 1, Double.parseDouble(dataRow.tebal), Double.parseDouble(dataRow.lebar),
                                            Double.parseDouble(dataRow.panjang), Integer.parseInt(dataRow.pcs));
                                }

                                if (!selectedJenisKayu.getNamaJenisKayu().toLowerCase().contains("kayu lat")) {
                                    for (int i = 0; i < temporaryDataListGrade.size(); i++) {
                                        DataRow2 dataRow2 = temporaryDataListGrade.get(i);
                                        saveDataToDatabase2(noST, dataRow2.gradeId, dataRow2.jumlah);
                                    }
                                }

                                if (labelVersion == 1) {
                                    savePenerimaanSTPembelian(noPenST, noST);
                                } else if (labelVersion == 2) {
                                    savePenerimaanSTUpah(noPenST, noST);
                                }

                                //check apakah masuk Bongkar Susun
                                if (cbBongkarSusun.isChecked()) {
                                    new SaveBongkarSusunTask(noBongkarSusun, noST).execute();

                                }

                                NoKB_display.setText(noKayuBulat);
                                NoKayuBulat.setVisibility(View.GONE);
                                NoKB_display.setVisibility(View.VISIBLE);
                                BtnDataBaruST.setVisibility(View.VISIBLE);
                                BtnSimpanST.setVisibility(View.GONE);
                                BtnPrintST.setEnabled(true);
                                BtnBatalST.setEnabled(false);
                                NoKB_display.setEnabled(false);
                                BtnPrintST.setEnabled(true);
                                disableForm();
//                                Toast.makeText(SawnTimber.this, "Data berhasil disimpan.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SawnTimber.this, "No Kayu Bulat tidak ditemukan dalam database!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(SawnTimber.this, "Silahkan lengkapi data!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SpinKayu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isCreateMode){
                    SpinKayu.setBackgroundResource(R.drawable.border_input);
                    JenisKayu selectedJenisKayu = (JenisKayu) parent.getItemAtPosition(position);
                    if (selectedJenisKayu.getIsUpah() == 1) {
                        CBUpah.setChecked(true);
                    } else {
                        CBUpah.setChecked(false);
                    }

                    if (selectedJenisKayu.getNamaJenisKayu().toLowerCase().contains("kayu lat")) {
                        radioBagus.setEnabled(true);
                        radioKulit.setEnabled(true);
                        radioBagus.setChecked(true);


                    } else {
                        radioBagus.setEnabled(false);
                        radioKulit.setEnabled(false);
                        radioBagus.setChecked(false);
                        radioKulit.setChecked(false);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpinStickBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinStickBy.setBackgroundResource(R.drawable.border_input);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpinSPK.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinSPK.setBackgroundResource(R.drawable.border_input);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        BtnPrintST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validasi input terlebih dahulu
                if (NoST.getQuery() == null || NoST.getQuery().toString().trim().isEmpty()) {
                    Toast.makeText(SawnTimber.this, "Nomor ST tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validasi apakah ada data yang akan dicetak
                if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
                    Toast.makeText(SawnTimber.this, "Tidak ada data untuk dicetak", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cek status HasBeenPrinted di database
                String noST = NoST.getQuery().toString().trim();
                checkHasBeenPrinted(noST, new HasBeenPrintedCallback() {
                    @Override
                    public void onResult(int printCount) {

                        if (printCount == -1) {
                            return;
                        }
                        try {
                            // Ambil data dari form dengan validasi null
                            String jenisKayu = SpinKayu.getSelectedItem() != null ? SpinKayu.getSelectedItem().toString().trim() : "";
                            String tglStickBundle = TglStickBundel.getText() != null ? TglStickBundel.getText().toString().trim() : "";
                            String tellyBy = SpinTelly.getSelectedItem() != null ? SpinTelly.getSelectedItem().toString().trim() : "";
                            String noSPK = SpinSPK.getSelectedItem() != null ? SpinSPK.getSelectedItem().toString().trim() : "";
                            String stickBy = SpinStickBy.getSelectedItem() != null ? SpinStickBy.getSelectedItem().toString().trim() : "";
                            String platTruk = NoPlatTruk.getText() != null ? NoPlatTruk.getText().toString().trim() : "";
                            String noKayuBulat = NoKayuBulat.getQuery() != null ? NoKayuBulat.getQuery().toString().trim() : "";
                            String noPenST = noPenerimaanST.getText() != null ? noPenerimaanST.getText().toString().trim() : "";
                            String namaSupplier = Supplier.getText() != null ? Supplier.getText().toString().trim() : "";
                            String noTruk = NoTruk.getText() != null ? NoTruk.getText().toString().trim() : "";
                            String jumlahPcs = JumlahPcsST.getText() != null ? JumlahPcsST.getText().toString().trim() : "";
                            String m3 = M3.getText() != null ? M3.getText().toString().trim() : "";
                            String ton = Ton.getText() != null ? Ton.getText().toString().trim() : "";
                            String remark = remarkLabel.getText() != null ? remarkLabel.getText().toString().trim() : "";
                            String customer = Customer.getText() != null ? Customer.getText().toString().trim() : "";
                            SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                            username = prefs.getString("username", "");
                            int isSLP = cbSLP.isChecked() ? 1 : 0;
                            // Default values for UOM columns
                            String idUOMTblLebar;
                            String idUOMPanjang;

                            // Check which checkbox is checked and set values accordingly
                            if (radioInch.isChecked()) {
                                idUOMTblLebar = "\"";
                            } else if (radioMillimeter.isChecked()) {
                                idUOMTblLebar = "mm";
                            } else {
                                idUOMTblLebar = "";
                            }

                            if (radioFeet.isChecked()) {
                                idUOMPanjang = "ft";
                            } else {
                                idUOMPanjang = "";
                            }

                            // Buat PDF dengan parameter hasBeenPrinted
                            Uri pdfUri = createPdf(noST, jenisKayu, tglStickBundle, tellyBy, noSPK, stickBy, platTruk,
                                    temporaryDataListDetail, noKayuBulat, namaSupplier, noTruk, jumlahPcs, m3, ton,
                                    printCount, username, remark, isSLP, idUOMTblLebar, idUOMPanjang, noPenST, labelVersion, customer); // Parameter baru untuk watermark

                            if (pdfUri != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(pdfUri, "application/pdf");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setPackage("com.dynamixsoftware.printershare"); // ← arahkan langsung ke PrinterShare

                                try {
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(SawnTimber.this,
                                            "PrinterShare not installed.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error";
                            Toast.makeText(SawnTimber.this,
                                    "Terjadi kesalahan: " + errorMessage,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // Atur listener untuk cbVacuum
        cbVacuum.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Jika checkbox dicentang, aktifkan EditText TglVacuum
            if (isChecked) {
                TglVacuum.setEnabled(true);
            } else {
                // Jika checkbox tidak dicentang, nonaktifkan EditText TglVacuum
                TglVacuum.setEnabled(false);
            }
        });


        // Atur listener untuk cb Bongkar Susun
        cbBongkarSusun.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Jika checkbox dicentang, aktifkan Bongkar Susun Spinner
            if (isChecked) {
                SpinBongkarSusun.setEnabled(true);
            } else {
                // Jika checkbox tidak dicentang, nonaktifkan Bongkar Susun Spinner
                SpinBongkarSusun.setEnabled(false);
            }
        });

        btnSwapToUOM.setOnClickListener(v -> {
            flipCard(inner_card_label_list, inner_card_top_right);
        });

        btnSwapToLabelList.setOnClickListener(v -> {
            flipCard(inner_card_top_right, inner_card_label_list);
        });
    }

    //METHOD SAWN TIMBER

    private void flipTwoToOne(View toHide1, View toHide2, View toShow) {
        // Animasi hide kedua view secara paralel
        toHide1.animate()
                .rotationY(90f)
                .setDuration(100)
                .start();

        toHide2.animate()
                .rotationY(90f)
                .setDuration(100)
                .withEndAction(() -> {
                    toHide1.setVisibility(View.GONE);
                    toHide2.setVisibility(View.GONE);
                    toHide1.setRotationY(0f);
                    toHide2.setRotationY(0f);

                    // Munculkan view baru
                    toShow.setRotationY(-90f);
                    toShow.setVisibility(View.VISIBLE);
                    toShow.animate()
                            .rotationY(0f)
                            .setDuration(100)
                            .start();
                })
                .start();
    }

    private void flipOneToTwo(View toHide, View toShow1, View toShow2) {
        // Animasi hide view pertama
        toHide.animate()
                .rotationY(90f)
                .setDuration(100)
                .withEndAction(() -> {
                    toHide.setVisibility(View.GONE);
                    toHide.setRotationY(0f);

                    // Setel rotasi awal untuk kedua view yang akan muncul
                    toShow1.setRotationY(-90f);
                    toShow2.setRotationY(-90f);
                    toShow1.setVisibility(View.VISIBLE);
                    toShow2.setVisibility(View.VISIBLE);

                    // Animasi muncul bersamaan
                    toShow1.animate()
                            .rotationY(0f)
                            .setDuration(100)
                            .start();

                    toShow2.animate()
                            .rotationY(0f)
                            .setDuration(100)
                            .start();
                })
                .start();
    }


    private void flipCard(View toHide, View toShow) {
        // Animasi keluar (rotasi keluar)
        toHide.animate()
                .rotationY(90f)
                .setDuration(100)
                .withEndAction(() -> {
                    toHide.setVisibility(View.GONE);
                    toHide.setRotationY(0f); // reset rotasi

                    // Munculkan view baru dengan rotasi Y -90°
                    toShow.setRotationY(-90f);
                    toShow.setVisibility(View.VISIBLE);
                    toShow.animate()
                            .rotationY(0f)
                            .setDuration(100)
                            .start();
                })
                .start();
    }


    private void addNewRow() {
        // Membuat TableRow baru
        TableRow newRow = new TableRow(this);
        newRow.setBackgroundColor(Color.parseColor("#FFFFFF"));

        // Membuat EditText untuk "Panjang"
        EditText panjangEditText = new EditText(this);
        panjangEditText.setHint("Pjg");
        panjangEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        // Mengatur layout params untuk EditText dan memberikan weight
        TableRow.LayoutParams panjangParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f); // 1f berarti 1 bagian dari total weight
        panjangEditText.setLayoutParams(panjangParams);

        // Membuat EditText untuk "Pcs"
        EditText pcsEditText = new EditText(this);
        pcsEditText.setHint("Pcs");
        pcsEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        pcsEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // Mengatur layout params untuk EditText dan memberikan weight
        TableRow.LayoutParams pcsParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f); // 1f berarti 1 bagian dari total weight
        pcsEditText.setLayoutParams(pcsParams);

        panjangEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Cek apakah tombol Enter ditekan
                if (actionId == EditorInfo.IME_ACTION_NEXT ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    pcsEditText.requestFocus(); // Pindahkan fokus ke EditText pcs
                    return true; // Tindakan sudah ditangani
                }
                return false;
            }
        });

        pcsEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Tangani enter atau tombol "Done" di keyboard
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    String panjang = panjangEditText.getText().toString().trim();
                    String pcs = pcsEditText.getText().toString().trim();

                    submitDetailInput(panjang, pcs);

                    return true;
                }
                return false;
            }
        });

        // Membuat ImageButton "Delete"
        ImageButton deleteButton = new ImageButton(this);
        deleteButton.setImageResource(R.drawable.ic_close);
        deleteButton.setContentDescription("Delete Button"); // Deskripsi konten untuk aksesibilitas (opsional)
        deleteButton.setBackgroundColor(Color.TRANSPARENT);  // Membuat latar belakang transparan

        // Mengatur layout params untuk ImageButton dan memberikan weight
        TableRow.LayoutParams deleteButtonParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.5f); // 1f berarti 1 bagian dari total weight
        deleteButton.setLayoutParams(deleteButtonParams);

        // Set OnClickListener untuk tombol delete
        deleteButton.setOnClickListener(v -> {
            // Ambil nilai panjang yang akan dihapus dari EditText
            String panjangToDelete = panjangEditText.getText().toString().trim();
            String pcs = pcsEditText.getText().toString().trim();
            float panjangAcuan = Float.parseFloat(DetailPanjangST.getText().toString());

            // Periksa apakah panjang atau pcs tidak terisi
            if (panjangToDelete.isEmpty() || pcs.isEmpty()) {
                // Jika salah satu atau keduanya kosong, tampilkan Toast
                Toast.makeText(this, "Tidak ada nilai!", Toast.LENGTH_SHORT).show();
            } else {
                // Tambahkan pcs yang dihapus ke data dengan panjang == panjangAcuan
                for (DataRow row : temporaryDataListDetail) {
                    if (Float.parseFloat(row.panjang) == panjangAcuan) {
                        int updatedPcs = Integer.parseInt(row.pcs) + Integer.parseInt(pcs); // Tambahkan pcs yang dihapus
                        row.pcs = String.valueOf(updatedPcs); // Update pcs
                        sisaPCS.setText("Sisa : " + row.pcs);
                    }
                }

                // Menghapus baris pada TabelInputPjgPcs berdasarkan panjang yang sesuai
                for (int i = 0; i < Tabel.getChildCount(); i++) {
                    TableRow row = (TableRow) Tabel.getChildAt(i);
                    // Ambil nilai pada kolom pertama (panjang)
                    TextView panjangInRow = (TextView) row.getChildAt(3);  // Kolom panjang berada di indeks 3
                    if (panjangInRow.getText().toString().equals(panjangToDelete)) {
                        // Jika panjang pada baris tersebut sesuai dengan panjang yang dihapus, hapus baris tersebut
                        Tabel.removeViewAt(i);
                        break;  // Keluar dari loop setelah baris yang sesuai dihapus
                    }
                }

                // Hapus baris yang baru ditambahkan pada TabelInputPjgPcs
                TabelInputPjgPcs.removeView(newRow);

                // Menghapus data terkait dari temporaryDataListDetail berdasarkan panjang
                for (DataRow row : temporaryDataListDetail) {
                    if (row.panjang.equals(panjangToDelete)) {
                        temporaryDataListDetail.remove(row);  // Hapus data dari list berdasarkan panjang
                        break;
                    }
                }
                // Memperbarui tabel setelah penghapusan data
                updateTable(Float.parseFloat(panjangToDelete));  // Memperbarui tabel utama
                jumlahPcsST();
                m3();
                ton();
            }
        });


        // Set OnClickListener untuk tombol add
        addRowButton.setOnClickListener(v -> {
            String panjang = panjangEditText.getText().toString().trim();
            String pcs = pcsEditText.getText().toString().trim();
            submitDetailInput(panjang, pcs);


        });


        // Menambahkan EditText dan Button ke dalam TableRow
        newRow.addView(panjangEditText);
        newRow.addView(pcsEditText);
        newRow.addView(deleteButton);
        deleteButton.setEnabled(false);

        // Menambahkan baris baru ke TableLayout
        TabelInputPjgPcs.addView(newRow);
        panjangEditText.requestFocus();

    }

    private void submitDetailInput(String panjang, String pcs) {
        String tebal = DetailTebalST.getText().toString();
        String lebar = DetailLebarST.getText().toString();

        float panjangAcuan = Float.parseFloat(DetailPanjangST.getText().toString());

        if (!panjang.isEmpty() && !pcs.isEmpty() && !panjang.equals("0") && !pcs.equals("0")) {

            for (DataRow row : temporaryDataListDetail) {
                if (Float.parseFloat(row.panjang) == panjangAcuan) {
                    if (Integer.parseInt(row.pcs) <= Integer.parseInt(pcs)) {
                        sisaPCS.setText("Sisa : " + row.pcs);
                        Toast.makeText(this, "Pcs melebihi jumlah sisa", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            addDataDetail(tebal, lebar, panjang, pcs);
            jumlahPcsST();
            m3();
            ton();

            // Menonaktifkan semua EditText yang sudah ada di tabel
            for (int i = 0; i < TabelInputPjgPcs.getChildCount() - 1; i++) {
                TableRow row = (TableRow) TabelInputPjgPcs.getChildAt(i);
                for (int j = 0; j < row.getChildCount(); j++) {
                    View child = row.getChildAt(j);
                    if (child instanceof EditText) {
                        child.setEnabled(false);
                    } else if (child instanceof ImageButton) {
                        child.setEnabled(true);
                    }
                }
            }
        } else {
            Toast.makeText(this, "Isi semua form detail", Toast.LENGTH_SHORT).show();
        }
    }



    private void checkKayuBulatExists(String noKayuBulat, String namaJenisKayu, KayuBulatExistsCallback callback) {

        // Periksa versiLabel terlebih dahulu
        if (labelVersion != -1 || namaJenisKayu.toLowerCase().contains("kayu lat")) {
            // Jika versiLabel tidak sama dengan -1, abaikan query dan langsung kembalikan true
            runOnUiThread(() -> callback.onResult(true));
            return;
        }

        new Thread(() -> {
            boolean exists = false;

            Connection connection = null;
            try {
                // Mendapatkan koneksi dari method ConnectionClass
                connection = ConnectionClass();
                if (connection != null) {
                    String query = "SELECT COUNT(*) as count FROM KayuBulat_h WHERE NoKayuBulat = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, noKayuBulat);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                int count = rs.getInt("count");
                                exists = (count > 0);
                            }
                        }
                    }
                } else {
                    Log.e("Database", "Koneksi database gagal");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("Database", "Error checking nokayubulat existence: " + e.getMessage());
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            final boolean kayuBulatExists = exists;
            runOnUiThread(() -> callback.onResult(kayuBulatExists));
        }).start();
    }

    interface KayuBulatExistsCallback {
        void onResult(boolean exists);
    }

    private void disableForm(){
        SpinKayu.setEnabled(false);
        SpinGrade.setEnabled(false);
        SpinSPK.setEnabled(false);
        SpinTelly.setEnabled(false);
        SpinStickBy.setEnabled(false);
        JumlahStick.setEnabled(false);
        DetailTebalST.setEnabled(false);
        DetailLebarST.setEnabled(false);
        BtnTambahStickST.setEnabled(false);
        DetailPanjangST.setEnabled(false);
        DetailPcsST.setEnabled(false);
        BtnInputDetailST.setEnabled(false);
        TglVacuum.setEnabled(false);
        SpinBongkarSusun.setEnabled(false);
        BtnSimpanST.setEnabled(false);
        inner_card_detail.setVisibility(View.VISIBLE);
        inner_card_top_left.setVisibility(View.GONE);
        inner_card_top_center.setVisibility(View.GONE);
        remarkLabel.setEnabled(false);
        CBKering.setEnabled(false);
        cbSLP.setEnabled(false);
        cbVacuum.setEnabled(false);
        cbBongkarSusun.setEnabled(false);
        radioBagus.setEnabled(false);
        radioKulit.setEnabled(false);

        flipTwoToOne(inner_card_top_left, inner_card_top_center, inner_card_detail);

        for (ImageButton btn : deleteButtons) {
            btn.setVisibility(View.INVISIBLE);
        }
    }

    private void enableForm(){
        SpinKayu.setEnabled(true);
        SpinGrade.setEnabled(true);
        SpinSPK.setEnabled(true);
        SpinTelly.setEnabled(true);
        SpinStickBy.setEnabled(true);
        JumlahStick.setEnabled(true);
        DetailTebalST.setEnabled(true);
        DetailLebarST.setEnabled(true);
        BtnTambahStickST.setEnabled(true);
        DetailPanjangST.setEnabled(true);
        DetailPcsST.setEnabled(true);
        BtnInputDetailST.setEnabled(true);
        BtnSimpanST.setEnabled(true);
        BtnPrintST.setEnabled(true);
        NoKB_display.setVisibility(View.GONE);
        NoKayuBulat.setVisibility(View.VISIBLE);
        remarkLabel.setEnabled(true);
        CBKering.setEnabled(true);
        cbSLP.setEnabled(true);
        cbVacuum.setEnabled(true);
        cbBongkarSusun.setEnabled(true);
        radioBagus.setEnabled(true);
        radioKulit.setEnabled(true);

        flipOneToTwo(inner_card_detail, inner_card_top_left, inner_card_top_center);
    }



    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(NoKayuBulat.getWindowToken(), 0);
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


    private void loadKayuBulat(String noKayuBulat) {
        // Tampilkan progress dialog
        new Thread(() -> {
            Connection connection = null;
            try {
                connection = ConnectionClass();
                if (connection != null) {
                    // Query untuk KayuBulat
                    String queryKayuBulat = "SELECT " +
                            "s.NmSupplier, " +
                            "h.NoTruk, " +
                            "h.NoPlat, " +
                            "h.Suket, " +
                            "k.Jenis " +
                            "FROM KayuBulat_h h " +
                            "LEFT JOIN MstSupplier s ON h.IdSupplier = s.IdSupplier " +
                            "LEFT JOIN MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                            "WHERE h.NoKayuBulat = ?";

                    // Menggunakan try-with-resources untuk header
                    try (PreparedStatement stmt = connection.prepareStatement(queryKayuBulat)) {
                        stmt.setString(1, noKayuBulat);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                // Mengambil data KayuBulat
                                final String namaSupplier = rs.getString("NmSupplier") != null ? rs.getString("NmSupplier") : "-";
                                final String noTruk = rs.getString("NoTruk") != null ? rs.getString("NoTruk") : "-";
                                final String noPlat = rs.getString("NoPlat") != null ? rs.getString("NoPlat") : "-";
                                final String noSuket = rs.getString("Suket") != null ? rs.getString("Suket") : "-";
                                final String namaKayu = rs.getString("Jenis") != null ? rs.getString("Jenis") : "-";

                                // Update UI di thread utama
                                runOnUiThread(() -> {
                                    try {
                                        // Update  fields
                                        Supplier.setText(namaSupplier);
                                        NoTruk.setText(noTruk);
                                        NoPlatTruk.setText(noPlat);
                                        NoSuket.setText(noSuket);
                                        JenisKayuKB.setText(namaKayu);

                                        Supplier.setEnabled(false);
                                        NoTruk.setEnabled(false);
                                        NoPlatTruk.setEnabled(false);
                                        NoSuket.setEnabled(false);
                                        JenisKayuKB.setEnabled(false);

//                                        Toast.makeText(getApplicationContext(),
//                                                "Data berhasil dimuat",
//                                                Toast.LENGTH_SHORT).show();
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
//                                            "Data tidak ditemukan untuk NoKayuBulat: " + noST,
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

    private void loadPenerimaanSTPembelian(String noPenST) {
        // Tampilkan progress dialog
        new Thread(() -> {
            Connection connection = null;
            try {
                connection = ConnectionClass();
                if (connection != null) {
                    // Query untuk KayuBulat
                    String queryKayuBulat = "SELECT " +
                            "s.NmSupplier, " +
                            "h.NoTruk, " +
                            "h.NoPlat " +
                            "FROM PenerimaanSTPembelian_h h " +
                            "LEFT JOIN MstSupplier s ON h.IdSupplier = s.IdSupplier " +
                            "WHERE h.NoPenerimaanST = ?";

                    // Menggunakan try-with-resources untuk header
                    try (PreparedStatement stmt = connection.prepareStatement(queryKayuBulat)) {
                        stmt.setString(1, noPenST);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                // Mengambil data KayuBulat
                                final String namaSupplier = rs.getString("NmSupplier") != null ? rs.getString("NmSupplier") : "-";
                                final String noTruk = rs.getString("NoTruk") != null ? rs.getString("NoTruk") : "-";
                                final String noPlat = rs.getString("NoPlat") != null ? rs.getString("NoPlat") : "-";

                                // Update UI di thread utama
                                runOnUiThread(() -> {
                                    try {
                                        // Update  fields
                                        Supplier.setText(namaSupplier);
                                        NoTruk.setText(noTruk);
                                        NoPlatTruk.setText(noPlat);

                                        Supplier.setEnabled(false);
                                        NoTruk.setEnabled(false);
                                        NoPlatTruk.setEnabled(false);

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
//                                            "Data tidak ditemukan untuk NoKayuBulat: " + noST,
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

    private void loadPenerimaanSTUpah(String noPenST) {
        // Tampilkan progress dialog
        new Thread(() -> {
            Connection connection = null;
            try {
                connection = ConnectionClass();
                if (connection != null) {
                    // Query untuk KayuBulat
                    String queryKayuBulat = "SELECT " +
                            "s.NamaCustomer, " +
                            "h.NoTruk, " +
                            "h.NoPlat " +
                            "FROM PenerimaanSTUpah_h h " +
                            "LEFT JOIN MstCustomerUpah s ON h.IdCustomer = s.IdCustomer " +
                            "WHERE h.NoPenerimaanST = ?";

                    // Menggunakan try-with-resources untuk header
                    try (PreparedStatement stmt = connection.prepareStatement(queryKayuBulat)) {
                        stmt.setString(1, noPenST);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                // Mengambil data KayuBulat
                                final String namaCustomer = rs.getString("NamaCustomer") != null ? rs.getString("NamaCustomer") : "-";
                                final String noTruk = rs.getString("NoTruk") != null ? rs.getString("NoTruk") : "-";
                                final String noPlat = rs.getString("NoPlat") != null ? rs.getString("NoPlat") : "-";

                                // Update UI di thread utama
                                runOnUiThread(() -> {
                                    try {
                                        // Update  fields
                                        Customer.setText(namaCustomer);
                                        NoTruk.setText(noTruk);
                                        NoPlatTruk.setText(noPlat);

                                        Supplier.setEnabled(false);
                                        NoTruk.setEnabled(false);
                                        NoPlatTruk.setEnabled(false);

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
//                                            "Data tidak ditemukan untuk NoKayuBulat: " + noST,
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

    private void loadSawnTimber(String noST) {
        // Tampilkan progress dialog
        resetDetailData();
        resetGradeData();
        new Thread(() -> {
            Connection connection = null;
            try {
                connection = ConnectionClass();
                if (connection != null) {
                    // Query untuk header
                    String queryHeader = "SELECT " +
                            "h.NoKayuBulat, " +
                            "k.Jenis, " +
                            "s.NamaStickBy, " +
                            "h.NoSPK, " +
                            "h.DateCreate, " +
                            "t.NamaOrgTelly, " +
                            "h.Remark, " +
                            "p.NoPenerimaanST AS NoPenerimaanSTPembelian, " +
                            "u.NoPenerimaanST AS NoPenerimaanSTUpah, " +
                            "h.IsSLP, " +
                            "h.VacuumDate " +
                            "FROM ST_h h " +
                            "LEFT JOIN MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                            "LEFT JOIN MstStickBy s ON h.IdStickBy = s.IdStickBy " +
                            "LEFT JOIN MstOrgTelly t ON h.IdOrgTelly = t.IdOrgTelly " +
                            "LEFT JOIN PenerimaanSTPembelian_d p ON h.NoST = p.NoST " +
                            "LEFT JOIN PenerimaanSTUpah_d u ON h.NoST = u.NoST " +
                            "WHERE h.NoST = ?";

                    // Query untuk detail
                    String queryDetail = "SELECT Tebal, Lebar, Panjang, JmlhBatang " +
                            "FROM ST_d " +
                            "WHERE NoST = ? " +
                            "ORDER BY NoUrut";

                    // Query untuk grade
                    String queryGrade = "SELECT " +
                            "s.IdGradeStick, " +
                            "s.JumlahStick, " +
                            "m.NamaGradeStick " +
                            "FROM STStick s " +
                            "INNER JOIN MstGradeStick m ON m.IdGradeStick = s.IdGradeStick " +
                            "WHERE NoST = ?" +
                            "ORDER BY IdGradeStick";

                    // Menggunakan try-with-resources untuk header
                    try (PreparedStatement stmt = connection.prepareStatement(queryHeader)) {
                        stmt.setString(1, noST);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                // Mengambil data header
                                final String noKayuBulat = rs.getString("NoKayuBulat") != null ? rs.getString("NoKayuBulat") : "-";
                                final String namaKayu = rs.getString("Jenis") != null ? rs.getString("Jenis") : "-";
                                final String namaStickBy = rs.getString("NamaStickBy") != null ? rs.getString("NamaStickBy") : "-";
                                final String noSPK = rs.getString("NoSPK") != null ? rs.getString("NoSPK") : "-";
                                final String tglStickBundel = rs.getString("DateCreate") != null ? rs.getString("DateCreate") : "-";
                                final String namaOrgTelly = rs.getString("NamaOrgTelly") != null ? rs.getString("NamaOrgTelly") : "-";
                                final String remark = rs.getString("Remark") != null ? rs.getString("Remark") : "-";
                                final String noPenSTPembelian = rs.getString("NoPenerimaanSTPembelian") != null ? rs.getString("NoPenerimaanSTPembelian") : "-";
                                final String noPenSTUpah = rs.getString("NoPenerimaanSTUpah") != null ? rs.getString("NoPenerimaanSTUpah") : "-";
                                final int isSLP = rs.getInt("IsSLP");
                                final String vacuumDate = rs.getString("VacuumDate") != null ? rs.getString("VacuumDate") : "-";

                                // Mengambil data detail
                                try (PreparedStatement stmtDetail = connection.prepareStatement(queryDetail)) {
                                    stmtDetail.setString(1, noST);
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

                                try (PreparedStatement stmtGrade = connection.prepareStatement(queryGrade)) {
                                    stmtGrade.setString(1, noST);
                                    try (ResultSet rsGrade = stmtGrade.executeQuery()) {
                                        while (rsGrade.next()) {
                                            int idGradeStick = rsGrade.getInt("IdGradeStick");
                                            String namaGradeStick = rsGrade.getString("NamaGradeStick");
                                            String jumlahGradeStick = rsGrade.getString("JumlahStick");

                                            // Buat objek DataRow baru dan tambahkan ke list
                                            DataRow2 newRow = new DataRow2(idGradeStick, namaGradeStick, jumlahGradeStick);
                                            temporaryDataListGrade.add(newRow);
                                        }
                                    }
                                }

                                // Update UI di thread utama
                                runOnUiThread(() -> {
                                    try {
                                        // Update header fields
                                        NoKayuBulat.setQuery(noKayuBulat, true);
                                        setSpinnerValue(SpinKayu, namaKayu);
                                        setSpinnerValue(SpinStickBy, namaStickBy);
                                        setSpinnerValue(SpinSPK, noSPK);
                                        setSpinnerValue(SpinTelly, namaOrgTelly);
                                        TglStickBundel.setText(tglStickBundel);
                                        remarkLabel.setText(remark);
                                        cbSLP.setChecked(isSLP == 1);

                                        TglVacuum.setText(vacuumDate);
                                        if (!vacuumDate.equals("-")) {
                                            cbVacuum.setChecked(true);
                                        }


                                        // Update tabel detail
                                        updateTableFromTemporaryDataDetail();
                                        updateTableFromTemporaryDataGrade();
                                        m3();
                                        ton();
                                        jumlahPcsST();

                                        if (noKayuBulat.equals("-") && noPenSTUpah.equals("-")) {
                                            loadPenerimaanSTPembelian(noPenSTPembelian);
                                            noPenerimaanST.setText(noPenSTPembelian);

                                        } else if (noKayuBulat.equals("-") && noPenSTPembelian.equals("-")) {
                                            loadPenerimaanSTUpah(noPenSTUpah);
                                            noPenerimaanST.setText(noPenSTUpah);

                                        } else {
                                            loadKayuBulat(noKayuBulat);
                                            noPenerimaanST.setText("-");
                                        }


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
//                                            "Data tidak ditemukan untuk NoST: " + noST,
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

    //     Method baru untuk memperbarui tabel dari temporaryDataListDetail
    private void updateTableFromTemporaryDataDetail() {
        // Perbarui rowCount
        rowCount = 0;

        DecimalFormat df = new DecimalFormat("#,###.##");

        for (DataRow data : temporaryDataListDetail) {
            TableRow newRow = new TableRow(this);
            TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            newRow.setLayoutParams(rowParams);

            // Set background warna untuk baris alternate (opsional)
            if (rowCount % 2 == 0) {
                newRow.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            // Tambahkan kolom-kolom
            addTextViewToRowWithWeight(newRow, String.valueOf(++rowCount), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(data.tebal)), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(data.lebar)), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(data.panjang)), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Integer.parseInt(data.pcs)), 1f);

//            // Tambahkan tombol hapus dengan lebar tetap
//            Button deleteButton = new Button(this);
//            deleteButton.setText("");
//            deleteButton.setTextSize(12);

//            // Atur style tombol
//            TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(0,
//                    TableRow.LayoutParams.WRAP_CONTENT, 1f);
//            buttonParams.setMargins(5, 5, 5, 5);
//            deleteButton.setLayoutParams(buttonParams);
//            deleteButton.setPadding(10, 5, 10, 5);
//            deleteButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//            deleteButton.setTextColor(Color.BLACK);
//
//            newRow.addView(deleteButton);
            Tabel.addView(newRow);
        }
    }

    private void updateTableFromTemporaryDataGrade() {
        // Perbarui rowCount
        rowCount = 0;

        for (DataRow2 data : temporaryDataListGrade) {
            TableRow newRow = new TableRow(this);
            newRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // Tambahkan kolom-kolom dengan format yang sama seperti addDataDetail
//            addTextViewToRowWithWeight(newRow, String.valueOf(data.gradeId), 1f);
            addTextViewToRowWithWeight(newRow, data.gradeName, 1f);
            addTextViewToRowWithWeight(newRow, data.jumlah, 0.5f);
            addTextViewToRowWithWeight(newRow, "", 0.5f);

            Tabel2.addView(newRow);
        }
    }



    // Interface untuk callback
    interface HasBeenPrintedCallback {
        void onResult(int count); // Callback menerima count
    }

    // Method untuk mengecek status HasBeenPrinted secara asynchronous
    private void checkHasBeenPrinted(String noST, SawnTimber.HasBeenPrintedCallback callback) {
        new Thread(() -> {
            int hasBeenPrintedValue = -1; // Default jika tidak ditemukan
            boolean existsInH = false; // Cek keberadaan di s4s_h
            boolean existsInD = false; // Cek keberadaan di s4s_d
            String dateUsage = null;
            boolean hasBeenProcess = false;
            Connection connection = null;

            try {
                // Mendapatkan koneksi dari method ConnectionClass
                connection = ConnectionClass();
                if (connection != null) {
                    // Query untuk mengecek keberadaan di s4s_h dan mengambil HasBeenPrinted
                    String queryCheckH = "SELECT HasBeenPrinted, DateUsage FROM ST_h WHERE NoST = ?";
                    String queryCheckD = "SELECT 1 FROM ST_d WHERE NoST = ?";

                    // Cek keberadaan di s4s_h
                    try (PreparedStatement stmtH = connection.prepareStatement(queryCheckH)) {
                        stmtH.setString(1, noST);
                        try (ResultSet rsH = stmtH.executeQuery()) {
                            if (rsH.next()) {
                                hasBeenPrintedValue = rsH.getInt("HasBeenPrinted");
                                dateUsage = rsH.getString("DateUsage");
                                existsInH = true; // Data ditemukan di s4s_h

                                if (dateUsage != null) {
                                    hasBeenProcess = true;
                                }
                            }
                        }
                    }

                    // Cek keberadaan di s4s_d
                    try (PreparedStatement stmtD = connection.prepareStatement(queryCheckD)) {
                        stmtD.setString(1, noST);
                        try (ResultSet rsD = stmtD.executeQuery()) {
                            if (rsD.next()) {
                                existsInD = true; // Data ditemukan di s4s_d
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
            final boolean finalIsAvailable = existsInH && existsInD; // Hanya valid jika ada di kedua tabel\
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


    // Method untuk mengupdate status HasBeenPrinted
    private void updatePrintStatus(String noST) {
        new Thread(() -> {
            Connection connection = null;
            try {
                // Mendapatkan koneksi dari method ConnectionClass
                connection = ConnectionClass();
                if (connection != null) {
                    String query = "UPDATE ST_h SET HasBeenPrinted = 1 WHERE NoST = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, noST);
                        int rowsAffected = stmt.executeUpdate();

                        if (rowsAffected > 0) {
                            runOnUiThread(() -> Toast.makeText(SawnTimber.this,
                                    "Status cetak berhasil diupdate",
                                    Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(SawnTimber.this,
                                    "Tidak ada data yang diupdate",
                                    Toast.LENGTH_SHORT).show());
                        }
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(SawnTimber.this,
                            "Koneksi database gagal",
                            Toast.LENGTH_SHORT).show());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("Database", "Error updating HasBeenPrinted status: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(SawnTimber.this,
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

    private boolean validateKayuLatSelection() {
        JenisKayu selectedKayu = (JenisKayu) SpinKayu.getSelectedItem();
        if (selectedKayu != null && selectedKayu.getNamaJenisKayu().toLowerCase().contains("kayu lat")) {
//            if (!radioBagus.isChecked() && !radioKulit.isChecked()) {
//                Toast.makeText(this, "Silahkan pilih (Bagus/Kulit)", Toast.LENGTH_SHORT).show();
//                return false;
//            }
        }
        return true;
    }

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

    private static class DataRow2 {
        int gradeId;
        String gradeName;
        String jumlah;
        int rowId;
        private static int nextId = 1;

        DataRow2(int gradeId, String gradeName, String jumlah) {
            this.gradeId = gradeId;
            this.gradeName = gradeName;
            this.jumlah = jumlah;
            this.rowId = nextId++;
        }
    }

    private void clearTableData() {
        NoST.setQuery("", false);
        NoST_display.setText("");
        NoKayuBulat.setQuery("", false);
        Supplier.setText("");
        NoTruk.setText("");
        NoPlatTruk.setText("");
        NoSuket.setText("");
        JenisKayuKB.setText("");
        JumlahStick.setText("");
//        radioBagusKulit.clearCheck();
        CBStick.setChecked(false);
        CBKering.setChecked(false);
        CBUpah.setChecked(false);
        SpinSPK.setSelection(0);
        TglVacuum.setText("");
        NoKayuBulat.setQuery("", false);
        setSpinnerValue(SpinKayu, "-");
        setSpinnerValue(SpinStickBy, "-");
        setSpinnerValue(SpinTelly, "-");
        cbVacuum.setChecked(false);
        cbBongkarSusun.setChecked(false);
        cbSLP.setChecked(false);

        NoST_display.setVisibility(View.GONE);

    }

    private void ton() {
        try {
            double totalTON = 0.0;
            boolean isMillimeter = radioMillimeter.isChecked();

            for (DataRow row : temporaryDataListDetail) {

                // Parse nilai-nilai langsung tanpa membersihkan
                double tebal = Double.parseDouble(row.tebal);
                double lebar = Double.parseDouble(row.lebar);
                double panjang = Double.parseDouble(row.panjang);
                int pcs = Integer.parseInt(row.pcs);

                // Hitung ton untuk baris ini
                double rowTON;

                if(isMillimeter){
                    rowTON = ((tebal * lebar * panjang * pcs * 304.8 / 1000000000 / 1.416 * 10000) / 10000);
                    rowTON = Math.floor(rowTON * 10000) / 10000;
                }
                else{
                    rowTON = ((tebal * lebar * panjang * pcs / 7200.8 * 10000) / 10000);
                    rowTON = Math.floor(rowTON * 10000) / 10000;
                }
                totalTON += rowTON;

            }

            // Format hasil
            DecimalFormat df = new DecimalFormat("0.0000");
            String formattedTON = df.format(totalTON);

            // Update TextView
            TextView TONTextView = findViewById(R.id.Ton);
            if (TONTextView != null) {
                TONTextView.setText(formattedTON);
                // Debug: Konfirmasi setText
                Log.d("TON_DEBUG", "TextView updated with: " + formattedTON);
            } else {
                Log.e("TON_DEBUG", "TONTextView is null!");
            }

        } catch (Exception e) {
            Log.e("TON_DEBUG", "Error in ton calculation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void m3() {
        try {
            double totalM3 = 0.0;
            boolean isMillimeter = radioMillimeter.isChecked();

            for (DataRow row : temporaryDataListDetail) {

                // Parse nilai-nilai langsung tanpa membersihkan
                double tebal = Double.parseDouble(row.tebal);
                double lebar = Double.parseDouble(row.lebar);
                double panjang = Double.parseDouble(row.panjang);
                int pcs = Integer.parseInt(row.pcs);

                // Hitung m3 untuk baris ini
                double rowM3;

                if(isMillimeter){
                    rowM3 = ((tebal * lebar * panjang * pcs * 304.8 / 1000000000 / 1.416 * 10000) / 10000) * 1.416;
                    rowM3 = Math.floor(rowM3 * 10000) / 10000;
                }
                else{
                    rowM3 = ((tebal * lebar * panjang * pcs / 7200.8 * 10000) / 10000) * 1.416;
                    rowM3 = Math.floor(rowM3 * 10000) / 10000;
                }
                totalM3 += rowM3;
            }

            // Format hasil
            DecimalFormat df = new DecimalFormat("0.0000");
            String formattedM3 = df.format(totalM3);

            // Update TextView
            TextView M3TextView = findViewById(R.id.M3ST);
            if (M3TextView != null) {
                M3TextView.setText(formattedM3);
                // Debug: Konfirmasi setText
                Log.d("M3_DEBUG", "TextView updated with: " + formattedM3);
            } else {
                Log.e("M3_DEBUG", "M3TextView is null!");
            }

        } catch (Exception e) {
            Log.e("M3_DEBUG", "Error in ton calculation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void jumlahPcsST() {
        TableLayout table = findViewById(R.id.Tabel);
        int childCount = table.getChildCount();

        int totalPcs = 0;

        for (int i = 0; i < childCount; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            TextView pcsTextView = (TextView) row.getChildAt(4); // Indeks pcs

            String pcsString = pcsTextView.getText().toString().replace(",", "");
            int pcs = Integer.parseInt(pcsString);
            totalPcs += pcs;
        }
        JumlahPcsST.setText(String.valueOf(totalPcs));
    }

    private void setCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        SimpleDateFormat saveFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        TglStickBundel.setText(currentDate);
        rawDate = saveFormat.format(new Date());
    }

    private void onClickDateOutput(String rawDate) {
        executorService.execute(() -> {
            List<OutputDataST> noSTList = SawnTimberApi.getNoSTByDateCreate(rawDate);

            runOnUiThread(() -> {
                populateTableOutput(noSTList);
            });
        });
    }

    private void populateTableOutput(List<OutputDataST> noSTList) {
        TextView labelCountView = findViewById(R.id.labelCount);

        if (TableOutput == null) {
            Log.e("populateTableOutput", "TableOutput tidak ditemukan di layout!");
            return;
        }

        TableOutput.removeAllViews();

        if (noSTList == null || noSTList.isEmpty()) {
            TextView noData = new TextView(this);
            noData.setText("Tidak ada label");
            noData.setPadding(16, 16, 16, 16);
            noData.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            TableOutput.addView(noData);

            if (labelCountView != null) {
                labelCountView.setText("Total : " + noSTList.size());
            }
            return;
        }

        for (int i = 0; i < noSTList.size(); i++) {
            OutputDataST data = noSTList.get(i);
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT  // ⬅️ ini penting
            ));

            // === Warna selang-seling ===
            int bgColor = (i % 2 == 0)
                    ? ContextCompat.getColor(this, R.color.background_cream) // genap
                    : ContextCompat.getColor(this, R.color.white);            // ganjil
            row.setBackgroundColor(bgColor);

            // === Kolom 1: NoST ===
            TextView noSTView = new TextView(this);
            noSTView.setText(data.getNoST());
            noSTView.setPadding(16, 16, 16, 16);
            noSTView.setTextSize(16);
            noSTView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            noSTView.setGravity(Gravity.CENTER);

            TableRow.LayoutParams noSTParams = new TableRow.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.7f  // ⬅️ WRAP_CONTENT
            );
            noSTParams.gravity = Gravity.CENTER;
            noSTView.setLayoutParams(noSTParams);


            // === Divider ===
            View divider = new View(this);
            divider.setLayoutParams(new TableRow.LayoutParams(
                    1, ViewGroup.LayoutParams.MATCH_PARENT));
            divider.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));

            // === Kolom 2: Ikon Printed ===
            ImageView iconView = new ImageView(this);
            int iconRes = data.isHasBeenPrinted()
                    ? R.drawable.ic_done     // printed
                    : R.drawable.ic_undone;  // not printed
            iconView.setImageResource(iconRes);
            iconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            iconView.setPadding(8, 8, 8, 8);

            TableRow.LayoutParams iconParams = new TableRow.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.3f  // ⬅️ WRAP_CONTENT
            );
            iconParams.gravity = Gravity.CENTER;
            iconView.setLayoutParams(iconParams);

            // Tambahkan ke row
            row.addView(noSTView);
            row.addView(divider);
            row.addView(iconView);

            // Tambahkan ke tabel
            TableOutput.addView(row);

            // Tambahkan OnClickListener untuk menampilkan tooltip
            row.setOnClickListener(view -> {
                String currentNoST = data.getNoST(); // Ambil nilai noST dari objek data saat ini
                fetchDataAndShowTooltip(
                        view,
                        currentNoST,  // Gunakan noST dari data iterasi saat ini
                        "ST_h",
                        "ST_d",
                        "NoST"
                );
            });
            if (labelCountView != null) {
                labelCountView.setText("Total : " + noSTList.size());
            }
        }
    }


    // Mengambil data tooltip dan menampilkan tooltip
    private void fetchDataAndShowTooltip(View anchorView, String noLabel, String tableH, String tableD, String mainColumn) {
        executorService.execute(() -> {
            // Ambil data tooltip menggunakan ProsesProduksiApi
            TooltipData tooltipData = ProsesProduksiApi.getTooltipData(noLabel, tableH, tableD, mainColumn);

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



    private void setCurrentDateTimeVacuum() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        SimpleDateFormat saveFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        TglVacuum.setText(currentDate);
        rawDateVacuum = saveFormat.format(new Date());

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

    private Uri createPdf(String noST, String jenisKayu, String tglStickBundle, String tellyBy, String noSPK, String stickBy, String platTruk,
                          List<DataRow> temporaryDataListDetail, String noKayuBulat, String namaSupplier, String noTruk, String jumlahPcs, String m3, String ton,
                          int printCount, String username, String remark, int isSLP, String idUOMTblLebar, String idUOMPanjang, String noPenST, int labelVersion, String customer) throws IOException {
        // Validasi parameter wajib
        if (noST == null || noST.trim().isEmpty()) {
            throw new IOException("NoST tidak boleh kosong");
        }

        if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
            throw new IOException("Data tidak boleh kosong");
        }

        String formattedDateStick = DateTimeUtils.formatDate(tglStickBundle);

        // Validasi dan set default value untuk parameter opsional
        noST = (noST != null) ? noST.trim() : "-";
        jenisKayu = (jenisKayu != null) ? jenisKayu.trim() : "-";
        formattedDateStick = (formattedDateStick != null) ? formattedDateStick.trim() : "-";
        stickBy = (stickBy != null) ? stickBy.trim() : "-";
        tellyBy = (tellyBy != null) ? tellyBy.trim() : "-";
        noSPK = (noSPK != null) ? noSPK.trim() : "-";
        jumlahPcs = (jumlahPcs != null) ? jumlahPcs.trim() : "-";
        m3 = (m3 != null) ? m3.trim() : "-";
        noKayuBulat = (noKayuBulat != null) ? noKayuBulat.trim() : "-";
        namaSupplier = (namaSupplier != null) ? namaSupplier.trim() : "-";
        platTruk = (platTruk != null) ? platTruk.trim() : "-";
        noTruk = (noTruk != null) ? noTruk.trim() : "-";
        ton = (ton != null) ? ton.trim() : "-";
        remark = (remark != null) ? remark.trim() : "-";
        idUOMTblLebar = (idUOMTblLebar != null) ? idUOMTblLebar.trim() : "-";
        idUOMPanjang = (idUOMPanjang != null) ? idUOMPanjang.trim() : "-";
        noPenST = (noPenST != null) ? noPenST.trim() : "-";
        customer = (customer != null) ? customer.trim() : "-";



        String[] nama = tellyBy.split(" ");
        String namaTelly = nama[0]; // namaDepan sekarang berisi "Windiar"


        Uri pdfUri = null;
        ContentResolver resolver = getContentResolver();
        String fileName = "ST_" + noST + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".pdf";
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
                float baseHeight = 400; // Tinggi dasar untuk elemen non-tabel (header, footer, margin, dll.)
                float rowHeight = 20; // Tinggi rata-rata per baris data
                float totalHeight = baseHeight + (rowHeight * temporaryDataListDetail.size());

                // Tetapkan ukuran halaman dinamis
                Rectangle pageSize = new Rectangle( PageSize.A6.getWidth(), totalHeight);
                pdfDocument.setDefaultPageSize(new PageSize(pageSize));

                Document document = new Document(pdfDocument);
                document.setMargins(0, 5, 0, 5);

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
//                addInfoRow(leftColumn, "No", noST, timesNewRoman);
                addInfoRow(leftColumn, "Jenis", jenisKayu, timesNewRoman);
                addInfoRow(leftColumn, "Plat", platTruk, timesNewRoman);
                addInfoRow(leftColumn, "SPK", noSPK, timesNewRoman);

                // Buat tabel untuk kolom kanan
                Table rightColumn = new Table(infoColumnWidths)
                        .setWidth(pageWidth * 0.6f)
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.LEFT);

                // Isi kolom kanan
                addInfoRow(rightColumn, "Tgl", formattedDateStick, timesNewRoman);
                addInfoRow(rightColumn, "Telly", namaTelly, timesNewRoman);
//                addInfoRow(rightColumn, "NoKB", noKayuBulat + "-" + namaSupplier, timesNewRoman);
                addInfoRow(rightColumn, "Stick", stickBy, timesNewRoman);

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

                    table.addCell(new Cell().add(new Paragraph(tebal + " " + idUOMTblLebar).setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                    table.addCell(new Cell().add(new Paragraph(lebar + " " + idUOMTblLebar).setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                    table.addCell(new Cell().add(new Paragraph(panjang + " " + idUOMPanjang).setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
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

                sumTable.addCell(new Cell().add(new Paragraph("Ton").setFixedLeading(15)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setFont(timesNewRoman));
                sumTable.addCell(new Cell().add(new Paragraph(":").setFixedLeading(15)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setFont(timesNewRoman));
                sumTable.addCell(new Cell().add(new Paragraph(String.valueOf(ton)).setFixedLeading(15)).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER).setFont(timesNewRoman));

                sumTable.addCell(new Cell().add(new Paragraph("m\u00B3").setFixedLeading(15)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setFont(timesNewRoman));
                sumTable.addCell(new Cell().add(new Paragraph(":").setFixedLeading(15)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setFont(timesNewRoman));
                sumTable.addCell(new Cell().add(new Paragraph(String.valueOf(m3)).setFixedLeading(15)).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER).setFont(timesNewRoman));

                Paragraph qrCodeIDbottomLeft = new Paragraph(noST).setTextAlignment(TextAlignment.LEFT).setFontSize(12).setMargins(-15, 0, 0, 35).setFont(timesNewRoman);
                Paragraph qrCodeIDbottomRight = new Paragraph(noST).setTextAlignment(TextAlignment.CENTER).setFontSize(25).setMargins(-77, 75, 0, 0).setFont(timesNewRoman).setBold();

                BarcodeQRCode qrCode = new BarcodeQRCode(noST);
                PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);

                BarcodeQRCode qrCodeBottom = new BarcodeQRCode(noST);
                PdfFormXObject qrCodeBottomObject = qrCodeBottom.createFormXObject(ColorConstants.BLACK, pdfDocument);
                Image qrCodeBottomImageLeft = new Image(qrCodeBottomObject).setWidth(115).setHorizontalAlignment(HorizontalAlignment.LEFT).setMargins(-70, 0, 0, 0);
                Image qrCodeBottomImageRight = new Image(qrCodeBottomObject).setWidth(115).setHorizontalAlignment(HorizontalAlignment.RIGHT).setMargins(0, 0, 0, 0);

                String formattedDate = DateTimeUtils.formatDateToDdYY(formattedDateStick);
                Paragraph textBulanTahunBold = new Paragraph(formattedDate).setTextAlignment(TextAlignment.RIGHT).setFontSize(50).setMargins(-60, 0, 0, 15).setFont(timesNewRoman).setBold();

                Paragraph kayuBulat = new Paragraph("No. KB : " + noKayuBulat + " - " + namaSupplier + " - " + noTruk).setTextAlignment(TextAlignment.LEFT).setFontSize(11).setMargins(0, 0, 0, 7).setFont(timesNewRoman);
                Paragraph pembelianST = new Paragraph("No. Pbl : " + noPenST + " - " + namaSupplier + " - " + noTruk).setTextAlignment(TextAlignment.LEFT).setFontSize(11).setMargins(0, 0, 0, 7).setFont(timesNewRoman);
                Paragraph upahST = new Paragraph("No. Upah : " + noPenST + " - " + customer + " - " + noTruk).setTextAlignment(TextAlignment.LEFT).setFontSize(11).setMargins(0, 0, 0, 7).setFont(timesNewRoman);
                Paragraph textHeader = new Paragraph("LABEL ST").setUnderline().setTextAlignment(TextAlignment.LEFT).setFontSize(14).setMargins(0, 0, 0, 7).setFont(timesNewRomanBold);
                Paragraph textHeaderPembelian = new Paragraph("LABEL ST (Pbl)").setUnderline().setTextAlignment(TextAlignment.LEFT).setFontSize(14).setMargins(0, 0, 0, 7).setFont(timesNewRomanBold);
                Paragraph textHeaderUpah = new Paragraph("LABEL ST (Upah)").setUnderline().setTextAlignment(TextAlignment.LEFT).setFontSize(14).setMargins(0, 0, 0, 7).setFont(timesNewRomanBold);
                Paragraph textHeaderNomor = new Paragraph("NO : " + noST).setUnderline().setTextAlignment(TextAlignment.LEFT).setFontSize(14).setMargins(-21, 0, 0, 145).setFont(timesNewRomanBold);

                Paragraph afkirText = new Paragraph("Reject").setTextAlignment(TextAlignment.RIGHT).setFontSize(14).setMargins(-20, 75, 0, 0).setFont(timesNewRoman);
                Paragraph lemburTextOutput = new Paragraph("Lembur").setTextAlignment(TextAlignment.RIGHT).setFontSize(14).setMargins(-20, 0, 0, 0).setFont(timesNewRoman);
                Paragraph remarkText = new Paragraph("Remark : " + remark).setTextAlignment(TextAlignment.CENTER).setFontSize(12).setMargins(-10, 0, 0, 0).setFont(timesNewRoman);
                Paragraph slpText = new Paragraph("SLP").setTextAlignment(TextAlignment.CENTER).setFontSize(12).setMargins(-175, 0, 0, 0).setFont(timesNewRomanBold).setFontSize(25);

                // Buat DashedLine dengan setting panjang garis dan jarak antar garis
                DashedLine dashedLine = new DashedLine(3f); // 3f = gap antar putus-putus (default 3pt)
                dashedLine.setLineWidth(1);
                LineSeparator dashedSeparator = new LineSeparator(dashedLine);

                // Tambahkan semua elemen ke dokumen
                if (labelVersion == 1 || noPenST.startsWith("BA")) {
                    document.add(textHeaderPembelian);
                } else if (labelVersion == 2 || noPenST.startsWith("O")) {
                    document.add(textHeaderUpah);
                } else {
                    document.add(textHeader);
                }

                document.add(textHeaderNomor);

                if (printCount > 0) {
                    addTextDitheringWatermark(pdfDocument, timesNewRoman);
                }

                document.add(mainTable);

                if (labelVersion == 1 || noPenST.startsWith("BA")){
                    document.add(pembelianST);
                } else if (labelVersion == 2 || noPenST.startsWith("O")) {
                    document.add(upahST);
                } else {
                    document.add(kayuBulat);
                }

                document.add(table);
                document.add(sumTable);
                document.add(qrCodeBottomImageLeft);
                document.add(qrCodeIDbottomLeft);
                document.add(textBulanTahunBold);

                if (!remark.isEmpty() && !remark.equals("-")) {
                    document.add(remarkText);
                }
                document.add(dashedSeparator);

                document.add(qrCodeBottomImageRight);
                document.add(qrCodeIDbottomRight);

                if (isSLP == 1) {
                    document.add(slpText);
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


    private void showDatePickerDialogStick() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SawnTimber.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                // Format input (dari DatePicker)
                rawDate = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);

                try {
                    SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

                    Date date = inputDateFormat.parse(rawDate);

                    String formattedDate = outputDateFormat.format(date);

                    TglStickBundel.setText(formattedDate);
                    new LoadSusunTask().execute(rawDate);
                    onClickDateOutput(rawDate);

                } catch (Exception e) {
                    e.printStackTrace();
                    TglStickBundel.setText("Invalid Date");
                }
            }

        }, year, month, day);

        datePickerDialog.show();
    }

    private void showDatePickerDialogVacuum() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SawnTimber.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                // Format input (dari DatePicker)
                rawDateVacuum = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);

                try {
                    SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

                    Date date = inputDateFormat.parse(rawDateVacuum);

                    String formattedDate = outputDateFormat.format(date);

                    TglVacuum.setText(formattedDate);


                } catch (Exception e) {
                    e.printStackTrace();
                    TglVacuum.setText("Invalid Date");
                }
            }

        }, year, month, day);

        datePickerDialog.show();
    }


    //Fungsi untuk add Data Detail
    private void addDataDetail(String tebal, String lebar, String panjang, String pcs) {

        if (tebal.isEmpty() || panjang.isEmpty() || lebar.isEmpty() || pcs.isEmpty()) {
            Toast.makeText(this, "Isi semua form detail!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Ukuran panjang sudah ada!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float panjangAcuan = Float.parseFloat(DetailPanjangST.getText().toString());

            sisaPCS.setText("Sisa : " + pcs);


            // Pengecekan: jika temporaryDataListDetail kosong, jangan lanjutkan pengurangan
            if (!temporaryDataListDetail.isEmpty()) {
                // Lakukan pengecekan dan pengurangan pcs untuk panjang == panjangAcuan
                for (DataRow dataRow : temporaryDataListDetail) {
                    if (Float.parseFloat(dataRow.panjang) == panjangAcuan) {
                        int updatedPcs = Integer.parseInt(dataRow.pcs) - Integer.parseInt(pcs); // Kurangi pcs
                        dataRow.pcs = String.valueOf(updatedPcs); // Update nilai pcs
                        sisaPCS.setText("Sisa : " + dataRow.pcs);
                    }
                }
            }

            // Buat objek DataRow baru
            DataRow newDataRow = new DataRow(tebal, lebar, panjang, pcs);
            temporaryDataListDetail.add(newDataRow);

            // Urutkan data berdasarkan panjang
            Collections.sort(temporaryDataListDetail, new Comparator<DataRow>() {
                @Override
                public int compare(DataRow o1, DataRow o2) {
                    return Float.compare(Float.parseFloat(o1.panjang), Float.parseFloat(o2.panjang));
                }
            });

            updateTable(panjangAcuan);

            addNewRow();  // Panggil fungsi untuk menambahkan row baru

            addRowButton.setVisibility(View.VISIBLE);
            BtnInputDetailST.setEnabled(false);
            DetailTebalST.setEnabled(false);
            DetailLebarST.setEnabled(false);
            DetailPcsST.setEnabled(false);
            DetailPanjangST.setEnabled(false);


        } catch (NumberFormatException e) {
            Toast.makeText(this, "Format angka tidak valid", Toast.LENGTH_SHORT).show();
        }
    }

    // Method untuk memperbarui tampilan tabel setelah perubahan data
    private void updateTable(float panjangAcuan) {
        // Hapus semua baris yang ada di tabel
        Tabel.removeAllViews();

        // Tambahkan semua baris yang ada di temporaryDataListDetail
        for (int i = 0; i < temporaryDataListDetail.size(); i++) {
            DataRow dataRow = temporaryDataListDetail.get(i);

            // Buat baris tabel baru
            TableRow newRow = new TableRow(this);
            newRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            DecimalFormat df = new DecimalFormat("#,###.##");

            // Tambahkan kolom-kolom data dengan weight
            addTextViewToRowWithWeight(newRow, String.valueOf(i + 1), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(dataRow.tebal)), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(dataRow.lebar)), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(dataRow.panjang)), 1f);
            addTextViewToRowWithWeight(newRow, String.valueOf(Integer.parseInt(dataRow.pcs)), 1f);

            // Buat dan tambahkan tombol hapus
            Button deleteButton = new Button(this);
            deleteButton.setText("Hapus");
            deleteButton.setTextSize(12);

            // Atur style tombol
            TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f);
            buttonParams.setMargins(5, 5, 5, 5);
            deleteButton.setLayoutParams(buttonParams);
            deleteButton.setPadding(10, 5, 10, 5);
            deleteButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            deleteButton.setTextColor(Color.BLACK);

            // Set listener tombol hapus
            deleteButton.setOnClickListener(v -> {
                // Ambil nilai pcs yang akan dihapus
                int pcsDeleted = Integer.parseInt(dataRow.pcs);

                // Hapus baris dari tabel
                Tabel.removeView(newRow);
                temporaryDataListDetail.remove(dataRow);

                // Tambahkan pcs yang dihapus ke data dengan panjang == panjangAcuan
                for (DataRow row : temporaryDataListDetail) {
                    if (Float.parseFloat(row.panjang) == panjangAcuan) {
                        int updatedPcs = Integer.parseInt(row.pcs) + pcsDeleted; // Tambahkan pcs yang dihapus
                        row.pcs = String.valueOf(updatedPcs); // Update pcs
                    }
                }

                // Update tabel setelah penghapusan dan pengembalian pcs
                updateRowNumbers();
                jumlahPcsST();
                m3();
                ton();
                updateTable(panjangAcuan);
            });

//            newRow.addView(deleteButton);
            Tabel.addView(newRow);
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
        Tabel.removeAllViews();
        TabelInputPjgPcs.removeAllViews();

        // Reset row counter
        rowCount = 0;

        // Reset tabel detail (hapus semua baris kecuali header)
        if (Tabel.getChildCount() > 1) {
            Tabel.removeViews(1, Tabel.getChildCount() - 1);
        }

        // Reset input fields
        if (DetailTebalST != null) {
            DetailTebalST.setText("");
        }
        if (DetailLebarST != null) {
            DetailLebarST.setText("");
        }
        if (DetailPanjangST != null) {
            DetailPanjangST.setText("");
        }
        if (DetailPcsST != null) {
            DetailPcsST.setText("");
        }

        m3();
        ton();
        jumlahPcsST();
    }



    //Fungsi untuk add data Grade
    private List<DataRow2> temporaryDataListGrade = new ArrayList<>();
    private List<ImageButton> deleteButtons = new ArrayList<>();


    private void addDataGrade(String noST) {
        GradeStick selectedGrade = (GradeStick) SpinGrade.getSelectedItem();
        String gradeName = selectedGrade.getNamaGradeStick();
        int gradeId = selectedGrade.getIdGradeStick();
        String jumlah = JumlahStick.getText().toString();

        if (gradeName.isEmpty() || jumlah.isEmpty()) {
            Toast.makeText(this, "Masukkan jumlah Stick!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkIfGradeIdExists(gradeId)) {
            return;
        }

        try {
            // Buat objek DataRow2 baru
            DataRow2 newDataRow = new DataRow2(gradeId, gradeName, jumlah);
            temporaryDataListGrade.add(newDataRow);

            TableRow newRow = new TableRow(this);
            newRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));


            // Nama Grade
            TextView gradeTextView = new TextView(this);
            gradeTextView.setText(gradeName);
            gradeTextView.setLayoutParams(new TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f));
            gradeTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            gradeTextView.setPadding(10, 35, 10, 15); // Menambahkan padding yang seimbang
            newRow.addView(gradeTextView);

            // Jumlah
            TextView jumlahTextView = new TextView(this);
            jumlahTextView.setText(jumlah);
            jumlahTextView.setLayoutParams(new TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
            jumlahTextView.setGravity(Gravity.CENTER);
            jumlahTextView.setPadding(10, 10, 10, 10);
            newRow.addView(jumlahTextView);

            // Tombol Hapus (ImageButton)
            ImageButton deleteButton = new ImageButton(this);
            deleteButton.setImageResource(R.drawable.ic_close); // Ganti dengan gambar ikon sesuai kebutuhan
            deleteButton.setContentDescription("Delete button"); // Deskripsi untuk aksesibilitas

            // Atur latar belakang tombol menjadi merah
            deleteButton.setBackgroundColor(Color.TRANSPARENT); // Mengubah latar belakang menjadi merah

            // Beri tint warna putih pada ikon
            deleteButton.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN); // Menyaring warna ikon menjadi putih

            // Atur layout button
            TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT, 0.5f); // Bobot 0.5f sesuai permintaan
            buttonParams.setMargins(5, 5, 5, 5);
            deleteButton.setLayoutParams(buttonParams);

            // Mengatur gambar agar proporsional
            deleteButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);


            // Set listener tombol hapus
            deleteButton.setOnClickListener(v -> {
                Tabel2.removeView(newRow);
                temporaryDataListGrade.remove(newDataRow);
            });

            newRow.addView(deleteButton);
            deleteButtons.add(deleteButton);


            // Tambahkan baris ke tabel
            Tabel2.addView(newRow);

            // Bersihkan form input
            SpinGrade.setSelection(0);
            JumlahStick.setText("");

        } catch (Exception e) {
            Toast.makeText(this, "Terjadi kesalahan saat menambah data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // Metode untuk mengecek ID grade yang sudah ada
    private boolean checkIfGradeIdExists(int gradeId) {
        for (DataRow2 data : temporaryDataListGrade) {
            if (data.gradeId == gradeId) {  // Akses langsung ke field gradeId
                Toast.makeText(this, "Data Grade telah terisi", Toast.LENGTH_SHORT).show();

                return true;
            }
        }
        return false;
    }

    private void resetGradeData() {
        // Membersihkan list temporary
        temporaryDataListGrade.clear();
        Tabel2.removeAllViews();

        // Reset spinner dan input field
        if (SpinGrade != null && SpinGrade.getAdapter() != null) {
            SpinGrade.setSelection(0);
        }
        if (JumlahStick != null) {
            JumlahStick.setText("");
        }
    }


    // Save Data Detail
    private void saveDataToDatabase(String noST, int noUrut, double tebal, double lebar, double panjang, int pcs) {
        new SaveDataTaskDetail().execute(noST, String.valueOf(noUrut), String.valueOf(tebal), String.valueOf(lebar),
                String.valueOf(panjang), String.valueOf(pcs));
    }

    private class SaveDataTaskDetail extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String noST = params[0];
            String noUrut = params[1];
            String tebal = params[2];
            String lebar = params[3];
            String panjang = params[4];
            String pcs = params[5];

            try {
                Connection connection = ConnectionClass();
                if (connection != null) {
                    String query = "INSERT INTO dbo.ST_d (NoST, NoUrut, Tebal, Lebar, Panjang, JmlhBatang) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, noST);
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

    // Save Data Grade
    private void saveDataToDatabase2(String noST, int gradeId, String jumlah) {
        new SaveDataGrade().execute(noST, String.valueOf(gradeId), jumlah);
    }

    private class SaveDataGrade extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String noST = params[0];
            String gradeId = params[1];
            String jumlah = params[2];

            try {
                // Cek nilai parameter
//                Log.d("SaveDataGrade", "noST: " + noST + ", gradeId: " + gradeId + ", jumlah: " + jumlah);

                Connection connection = ConnectionClass();
                if (connection != null) {
                    String query = "INSERT INTO dbo.STStick (NoST, IdGradeStick, JumlahStick) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, noST);
                    preparedStatement.setInt(2, Integer.parseInt(gradeId));  // Pastikan gradeId valid
                    preparedStatement.setString(3, jumlah);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
//                        Log.d("DB_INSERT", "Data Grade berhasil disimpan");
                        return true;
                    } else {
                        Log.e("DB_INSERT", "Data Grade gagal disimpan");
                    }
                } else {
                    Log.e("DB_CONNECTION", "Koneksi ke database gagal");
                }
            } catch (SQLException e) {
                Log.e("DB_ERROR", "SQL Exception: " + e.getMessage(), e);
            } catch (Exception e) {
                Log.e("GENERAL_ERROR", "General Exception: " + e.getMessage(), e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
//                Toast.makeText(SawnTimber.this, "Data Grade berhasil disimpan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SawnTimber.this, "Gagal menyimpan data Grade", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @SuppressWarnings("deprecation")
    private class SaveBongkarSusunTask extends AsyncTask<Void, Void, Boolean> {
        private String noBongkarSusun;
        private String noST;

        public SaveBongkarSusunTask(String noBongkarSusun, String noST) {
            this.noBongkarSusun = noBongkarSusun;
            this.noST = noST;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.BongkarSusunOutputST (NoST, NoBongkarSusun) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noST);
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
//            loadOutputByMesinSusun(noBongkarSusun, false);
        }
    }


    private void setAndSaveNoST(final CountDownLatch latch) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection con = ConnectionClass();
                noST = null;
                boolean success = false;

                if (con != null) {
                    try {
                        // Query untuk mendapatkan NoST terakhir
                        String query = "SELECT MAX(NoST) FROM dbo.ST_h";
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                            String lastNoST = rs.getString(1);

                            if (lastNoST != null && lastNoST.startsWith("E.")) {
                                String numericPart = lastNoST.substring(2);
                                int numericValue = Integer.parseInt(numericPart);
                                int newNumericValue = numericValue + 1;

                                // Membuat NoST baru
                                noST = "E." + String.format("%06d", newNumericValue);
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
                    String finalNewNoST = noST;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NoST.setQuery(finalNewNoST, true);
                            NoST.setVisibility(View.GONE);
                            NoST_display.setVisibility(View.VISIBLE);
                            NoST_display.setText(finalNewNoST);
                            NoST_display.setEnabled(false);
//                            Toast.makeText(S4S.this, "NoST berhasil diatur dan disimpan.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("Error", "Failed to set or save NoST.");
                            Toast.makeText(SawnTimber.this, "Gagal mengatur atau menyimpan NoST.", Toast.LENGTH_SHORT).show();
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
                    String query = "SELECT IdJenisKayu, Jenis, IsUpah FROM dbo.MstJenisKayu WHERE IsST = 1"; // Ambil IsUpah
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idJenisKayu = rs.getString("IdJenisKayu");
                        String namaJenisKayu = rs.getString("Jenis");
                        int isUpah = rs.getInt("IsUpah"); // Ambil nilai IsUpah

                        JenisKayu jenisKayu = new JenisKayu(idJenisKayu, namaJenisKayu, isUpah);
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
            JenisKayu dummyKayu1 = new JenisKayu("", "PILIH", 0);
            jenisKayuList.add(0, dummyKayu1);

            ArrayAdapter<JenisKayu> adapter = new ArrayAdapter<>(SawnTimber.this, android.R.layout.simple_spinner_item, jenisKayuList);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            SpinKayu.setAdapter(adapter);
            SpinKayu.setSelection(0);
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

            ArrayAdapter<SPK> adapter = new ArrayAdapter<>(SawnTimber.this,
                    android.R.layout.simple_spinner_item, spkList);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

            SpinSPK.setAdapter(adapter);
            SpinSPK.setSelection(0);
        }
    }


    private class LoadSusunTask extends AsyncTask<String, Void, List<Susun>> {
        @Override
        protected List<Susun> doInBackground(String... params) {
            List<Susun> susunList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    // Ambil tanggal saat ini jika tidak ada parameter
                    String selectedDate;
                    if (params != null && params.length > 0) {
                        selectedDate = params[0];
                    } else {
                        selectedDate = TglStickBundel.getText().toString();
                    }

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
                ArrayAdapter<Susun> adapter = new ArrayAdapter<>(SawnTimber.this, android.R.layout.simple_spinner_item, susunList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinBongkarSusun.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load susun data");
                SpinBongkarSusun.setAdapter(null);
//                TabelOutput.removeAllViews();
            }
        }
    }


    private class LoadGradeStickTask extends AsyncTask<Void, Void, List<GradeStick>> {
        @Override
        protected List<GradeStick> doInBackground(Void... voids) {
            List<GradeStick> gradeStickList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT IdGradeStick, NamaGradeStick FROM dbo.MstGradeStick";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int idGradeStick = rs.getInt("IdGradeStick");
                        String namaGradeStick = rs.getString("NamaGradeStick");

                        GradeStick gradeStick = new GradeStick(idGradeStick, namaGradeStick);
                        gradeStickList.add(gradeStick);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (SQLException e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Gagal terhubung ke database.");
            }
            return gradeStickList;
        }

        @Override
        protected void onPostExecute(List<GradeStick> gradeStickList) {
            if (!gradeStickList.isEmpty()) {
                ArrayAdapter<GradeStick> adapter = new ArrayAdapter<>(SawnTimber.this, android.R.layout.simple_spinner_item, gradeStickList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGrade.setAdapter(adapter);
            } else {
                Log.e("Error", "Tidak ada grade ditemukan");
            }
        }
    }

    private class LoadLokasiTask extends AsyncTask<Void, Void, List<Lokasi>> {
        @Override
        protected List<Lokasi> doInBackground(Void... voids) {
            List<Lokasi> lokasiList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT IdLokasi, Description FROM dbo.MstLokasi WHERE Enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idLokasi = rs.getString("IdLokasi");
                        String namaLokasi = rs.getString("Description");

                        Lokasi lokasi = new Lokasi(idLokasi, namaLokasi);
                        lokasiList.add(lokasi);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (SQLException e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Gagal terhubung ke database.");
            }
            return lokasiList;
        }

        @Override
        protected void onPostExecute(List<Lokasi> lokasiList) {
            if (!lokasiList.isEmpty()) {
                ArrayAdapter<Lokasi> adapter = new ArrayAdapter<>(SawnTimber.this, android.R.layout.simple_spinner_item, lokasiList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinLokasi.setAdapter(adapter);
            } else {
                Log.e("Error", "Tidak ada grade ditemukan");
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
            ArrayAdapter<Telly> adapter = new ArrayAdapter<>(SawnTimber.this, android.R.layout.simple_spinner_item, tellyList);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

            // Set adapter ke spinner
            SpinTelly.setAdapter(adapter);

            // Nonaktifkan spinner agar tidak bisa dipilih
            SpinTelly.setEnabled(false); // Spinner tidak bisa diklik
            SpinTelly.setClickable(false); // Spinner tidak bisa diinteraksi

        }
    }

    private class LoadStickByTask extends AsyncTask<Void, Void, List<StickBy>> {
        @Override
        protected List<StickBy> doInBackground(Void... voids) {
            List<StickBy> stickByList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT IdStickBy, NamaStickBy FROM dbo.MstStickBy WHERE Enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idStickBy = rs.getString("IdStickBy");
                        String namaStickBy = rs.getString("NamaStickBy");

                        StickBy stickBy = new StickBy(idStickBy, namaStickBy);
                        stickByList.add(stickBy);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (SQLException e) {
                    Log.e("Database Error", "SQL Error: " + e.getMessage());
                } catch (Exception e) {
                    Log.e("Database Error", "Error: " + e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Gagal terhubung dengan database.");
            }
            return stickByList;
        }

        @Override
        protected void onPostExecute(List<StickBy> stickByList) {
            // Tambahkan elemen dummy di awal
            StickBy dummyStickBy = new StickBy("", "PILIH");
            stickByList.add(0, dummyStickBy);

            // Buat adapter dengan data yang dimodifikasi
            ArrayAdapter<StickBy> adapter = new ArrayAdapter<>(SawnTimber.this, android.R.layout.simple_spinner_item, stickByList);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

            // Set adapter ke spinner
            SpinStickBy.setAdapter(adapter);

            // Atur spinner untuk menampilkan elemen pertama ("Pilih") secara default
            SpinStickBy.setSelection(0);
        }
    }

    private class UpdateDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String noST = params[0];
            String noKayuBulat = params[1];
            String jenisKayu = params[2];
            String noSPK = params[3];
            String telly = params[4];
            String stickBy = params[5];
            String dateCreate = params[6];
            String isVacuum = params[7];
            String remark = params[8];
            int isSLP = Integer.parseInt(params[9]);
            int isSticked = Integer.parseInt(params[10]);
            int isKering = Integer.parseInt(params[11]);
            int isBagusKulit = Integer.parseInt(params[12]);

            int isUpah = (labelVersion == 2) ? 1 : 0;

            Log.d("UpdateDataTask", "Tanggal dateCreate: " + dateCreate);



            // Default values for UOM columns
            int idUOMTblLebar = 0;
            int idUOMPanjang = 0;

            // Check which checkbox is checked and set values accordingly
            if (radioInch.isChecked()) {
                idUOMTblLebar = 3;
            } else if (radioMillimeter.isChecked()) {
                idUOMTblLebar = 1;
            }

            if (radioFeet.isChecked()) {
                idUOMPanjang = 4;
            }

            noKayuBulat = (noKayuBulat == null || noKayuBulat.trim().isEmpty()) ? null : noKayuBulat;

            Connection con = null;
            String message = "";

            try {
                con = ConnectionClass();
                if (con != null) {

                    String query = "INSERT INTO ST_h (NoST, NoKayuBulat, IdJenisKayu, NoSPK, IdOrgTelly, IdStickBy, IsUpah, IdUOMTblLebar, IdUOMPanjang, DateCreate, VacuumDate, Remark, IsSLP, IsSticked, StartKering, IsBagusKulit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);

                    ps.setString(1, noST);
                    ps.setString(2, noKayuBulat);
                    ps.setString(3, jenisKayu);
                    ps.setString(4, noSPK);
                    ps.setString(5, telly);
                    ps.setString(6, stickBy);
                    ps.setInt(7, isUpah);
                    ps.setInt(8, idUOMTblLebar);
                    ps.setInt(9, idUOMPanjang);
                    ps.setString(10, dateCreate);
                    ps.setString(11, isVacuum);
                    ps.setString(12, remark);
                    ps.setInt(13, isSLP);
                    ps.setInt(14, isSticked);
                    ps.setInt(15, isKering);
                    ps.setInt(16, isBagusKulit);

                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        message = "Data berhasil disimpan!";
                    } else {
                        message = "Gagal memperbarui data";
                    }

                    ps.close();
                    con.close();
                } else {
                    message = "Gagal terhubung ke database.";
                }
            } catch (SQLException e) {
                Log.e("Database Error", "SQL Error: " + e.getMessage());
                message = "Gagal memperbarui data: " + e.getMessage();
            } catch (Exception e) {
                Log.e("Database Error", "General Error: " + e.getMessage());
                message = "Terjadi kesalahan saat memperbarui data: " + e.getMessage();
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        Log.e("Database Error", "Error closing connection: " + e.getMessage());
                    }
                }
            }

            return message;
        }

        @Override
        protected void onPostExecute(String message) {
            onClickDateOutput(rawDate);
            Toast.makeText(SawnTimber.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void savePenerimaanSTPembelian(String noPenerimaanST, String noST) {
        // Tampilkan progress dialog
        new Thread(() -> {
            Connection connection = null;
            try {
                connection = ConnectionClass(); // Buat koneksi ke database
                if (connection != null) {
                    // Query untuk menyimpan data ke tabel
                    String queryInsert = "INSERT INTO PenerimaanSTPembelian_d (NoPenerimaanST, NoST) VALUES (?, ?)";

                    // Menggunakan try-with-resources untuk PreparedStatement
                    try (PreparedStatement stmt = connection.prepareStatement(queryInsert)) {
                        // Set parameter untuk query
                        stmt.setString(1, noPenerimaanST);
                        stmt.setString(2, noST);

                        // Eksekusi query
                        int rowsInserted = stmt.executeUpdate();

                        // Cek apakah data berhasil disimpan
                        if (rowsInserted > 0) {
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(),
                                        "Data berhasil disimpan",
                                        Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(),
                                        "Gagal menyimpan data",
                                        Toast.LENGTH_SHORT).show();
                            });
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
                        connection.close(); // Tutup koneksi
                    } catch (SQLException e) {
                        Log.e("Database Error", "Error closing connection: " + e.getMessage());
                    }
                }
            }
        }).start();
    }

    private void savePenerimaanSTUpah(String noPenerimaanST, String noST) {
        // Tampilkan progress dialog
        new Thread(() -> {
            Connection connection = null;
            try {
                connection = ConnectionClass(); // Buat koneksi ke database
                if (connection != null) {
                    // Query untuk menyimpan data ke tabel
                    String queryInsert = "INSERT INTO PenerimaanSTUpah_d (NoPenerimaanST, NoST) VALUES (?, ?)";

                    // Menggunakan try-with-resources untuk PreparedStatement
                    try (PreparedStatement stmt = connection.prepareStatement(queryInsert)) {
                        // Set parameter untuk query
                        stmt.setString(1, noPenerimaanST);
                        stmt.setString(2, noST);

                        // Eksekusi query
                        int rowsInserted = stmt.executeUpdate();

                        // Cek apakah data berhasil disimpan
                        if (rowsInserted > 0) {
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(),
                                        "Data berhasil disimpan",
                                        Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(),
                                        "Gagal menyimpan data",
                                        Toast.LENGTH_SHORT).show();
                            });
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
                        connection.close(); // Tutup koneksi
                    } catch (SQLException e) {
                        Log.e("Database Error", "Error closing connection: " + e.getMessage());
                    }
                }
            }
        }).start();
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

    public class Telly {
        private String idOrgTelly;
        private String namaTelly;

        public Telly(String idOrgTelly, String namaTelly) {
            this.idOrgTelly = idOrgTelly;
            this.namaTelly = namaTelly;
        }

        public String getIdOrgTelly() {
            return idOrgTelly;
        }

        public String getNamaTelly() {
            return namaTelly;
        }

        @Override
        public String toString() {
            return namaTelly;
        }
    }


    public class StickBy {
        private String idStickBy;
        private String namaStickBy;

        public StickBy(String idStickBy, String namaStickBy) {
            this.idStickBy = idStickBy;
            this.namaStickBy = namaStickBy;
        }

        public String getIdStickBy() {
            return idStickBy;
        }

        public String getNamaStickBy() {
            return namaStickBy;
        }

        @Override
        public String toString() {
            return namaStickBy;
        }
    }

    public class JenisKayu {
        private String idJenisKayu;
        private String namaJenisKayu;
        private int isUpah;

        public JenisKayu(String idJenisKayu, String namaJenisKayu, int isUpah) {
            this.idJenisKayu = idJenisKayu;
            this.namaJenisKayu = namaJenisKayu;
            this.isUpah = isUpah;
        }

        public String getIdJenisKayu() {
            return idJenisKayu;
        }

        public String getNamaJenisKayu() {
            return namaJenisKayu;
        }

        public int getIsUpah() {
            return isUpah;
        }

        @Override
        public String toString() {
            return namaJenisKayu;
        }
    }


    public class GradeStick {
        private int idGradeStick;
        private String namaGradeStick;

        public GradeStick(int idGradeStick, String namaGradeStick) {
            this.idGradeStick = idGradeStick;
            this.namaGradeStick = namaGradeStick;
        }

        public int getIdGradeStick() {
            return idGradeStick;
        }

        public String getNamaGradeStick() {
            return namaGradeStick;
        }

        @Override
        public String toString() {
            return namaGradeStick;
        }
    }

    public class Lokasi {
        private String idLokasi;
        private String namaLokasi;

        public Lokasi(String idLokasi, String namaLokasi) {
            this.idLokasi = idLokasi;
            this.namaLokasi = namaLokasi;
        }

        public String getIdLokasi() {
            return idLokasi;
        }

        public String getNamaLokasi() {
            return namaLokasi;
        }

        @Override
        public String toString() {
            return namaLokasi;
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
