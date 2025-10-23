package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import com.example.myapplication.api.MasterApi;
import com.example.myapplication.api.SawnTimberApi;
import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.model.GradeDetailData;
import com.example.myapplication.model.LabelDetailData;
import com.example.myapplication.model.LokasiData;
import com.example.myapplication.model.MstGradeStickData;
import com.example.myapplication.model.MstJenisKayuData;
import com.example.myapplication.model.SpkData;
import com.example.myapplication.model.MstStickData;
import com.example.myapplication.model.MstSusunData;
import com.example.myapplication.model.OutputDataST;
import com.example.myapplication.model.StData;
import com.example.myapplication.model.TellyData;
import com.example.myapplication.utils.DateTimeUtils;

import android.view.inputmethod.InputMethodManager;
import android.content.SharedPreferences;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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


import com.example.myapplication.utils.LoadingDialogHelper;
import com.example.myapplication.utils.PermissionUtils;
import com.example.myapplication.utils.SharedPrefUtils;
import com.example.myapplication.utils.TableUtils;
import com.example.myapplication.utils.TooltipUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SawnTimber extends AppCompatActivity {

    private String idUsername;
    private String username;
    private String noST;
    private Button BtnSimpanST;
    private Button BtnBatalST;
    private Button BtnDataBaruST;
    private Button BtnPrintST;
    private Button BtnTambahStickST;
    private Button BtnClearDetailST;
    private EditText NoST;
    private EditText NoKayuBulat;
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
    private TableLayout TabelInputPjgPcs;
    private List<LabelDetailData> temporaryDataListDetail = new ArrayList<>();
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
    private List<GradeDetailData> temporaryDataListGrade = new ArrayList<>();
    private EditText susunView;
    private ImageButton BtnExpandView;
    private List<String> userPermissions;
    private Button btnUpdate;
    private TableRow selectedRowHeader = null;
    int page = 1;
    int currentPage = 0;
    boolean isLoading = false;
    private FloatingActionButton fabAddDetailData;
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();




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
        radioGroupUOMTblLebar = findViewById(R.id.radioGroupUOMTblLebar);
        radioMillimeter = findViewById(R.id.radioMillimeter);
        radioInch = findViewById(R.id.radioInch);
        radioFeet = findViewById(R.id.radioFeet);
        radioBagus = findViewById(R.id.radioBagus);
        radioKulit = findViewById(R.id.radioKulit);
        radioBagusKulit = findViewById(R.id.radioGroupBagusKulit);
        M3 = findViewById(R.id.M3ST);
        Ton = findViewById(R.id.Ton);
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
        susunView = findViewById(R.id.susunView);
        BtnExpandView = findViewById(R.id.BtnExpandView);
        btnUpdate = findViewById(R.id.btnUpdate);
        noPenerimaanST = findViewById(R.id.noPenerimaanST);
        fabAddDetailData = findViewById(R.id.fabAddDetailData);

        inner_card_detail.setVisibility(View.GONE);
        inner_card_top_right.setVisibility(View.GONE);

        disableForm();

        BtnExpandView.setOnClickListener(v -> showListDialogOnDemand());

        //GET USERNAME
        idUsername = SharedPrefUtils.getIdUsername(this);

        //PERMISSION CHECK
        userPermissions = SharedPrefUtils.getPermissions(this);
        PermissionUtils.permissionCheck(this, btnUpdate, "label_st:update");
        PermissionUtils.permissionCheck(this, fabAddDetailData, "label_st:update");


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
        noPenerimaanST.setText(noPenST);


        radioMillimeter.setOnClickListener(v -> {
            jumlahPcsST();
            m3();
            ton();
        });

        radioInch.setOnClickListener(v -> {
            jumlahPcsST();
            m3();
            ton();
        });

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


        NoST.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Tidak perlu
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isCreateMode) {
                    String newText = s.toString();

                    if(!newText.isEmpty()) {
                        loadSubmittedData(newText);
                        BtnPrintST.setEnabled(true);
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


        NoKayuBulat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Tidak dipakai
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Panggil fungsi setiap kali teks berubah
                loadKayuBulat(s.toString());
                NoKayuBulat.setBackgroundResource(R.drawable.border_input);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Tidak dipakai
            }
        });


        BtnDataBaruST.setOnClickListener(v -> {

            loadingDialogHelper.show(this);

            // ====== init state sinkron ======
            isCreateMode = true;
            setCurrentDateTime();
            setCurrentDateTimeVacuum();
            enableForm();

            // ====== counter untuk semua loader async ======
            final int totalTasks = 7; // sesuaikan jumlah loader async di bawah
            final AtomicInteger doneCount = new AtomicInteger(0);

            Runnable checkAllDone = () -> {
                if (doneCount.incrementAndGet() == totalTasks) {
                    runOnUiThread(() -> {
                        // Semua spinner selesai → tutup loading & update UI
                        loadingDialogHelper.hide();

                        addRowButton.setVisibility(View.GONE);
                        BtnSimpanST.setEnabled(true);
                        BtnTambahStickST.setEnabled(true);
                        BtnInputDetailST.setEnabled(true);
                        BtnBatalST.setEnabled(true);
                        BtnPrintST.setEnabled(false);

                        BtnDataBaruST.setVisibility(View.GONE);
                        BtnSimpanST.setVisibility(View.VISIBLE);
                        btnUpdate.setVisibility(View.GONE);

                        NoKayuBulat.setText("");
                        cbSLP.setChecked(false);
                        cbVacuum.setChecked(false);
                        cbBongkarSusun.setChecked(false);
                        radioBagus.setChecked(false);
                        radioKulit.setChecked(false);
                        fabAddDetailData.setVisibility(View.GONE);

                        resetDetailData();
                        resetGradeData();
                        DetailPanjangST.setText("4");
                        remarkLabel.setText("");
                        susunView.setVisibility(View.GONE);
                        SpinBongkarSusun.setVisibility(View.VISIBLE);

                        if (labelVersion == -1) {
                            noPenerimaanST.setText("");
                        }
                    });
                }
            };

            // ====== panggil semua loader async dgn callback ======
            // Pastikan setiap fungsi loader punya overload (param terakhir Runnable onDone)
            loadSPKSpinner("", checkAllDone);
            loadTellyByIdUsernameSpinner(idUsername, checkAllDone);
            loadStickBySpinner("", checkAllDone);
            loadJenisKayuSpinner(0, checkAllDone);
            loadGradeStickSpinner(0, checkAllDone);
            loadLokasiSpinner("", "", checkAllDone);
            loadSusunSpinner(checkAllDone, rawDate);
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

        fabAddDetailData.setOnClickListener(view -> {
            showAddDetailDialog();
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
                BtnDataBaruST.setVisibility(View.VISIBLE);
                BtnSimpanST.setVisibility(View.GONE);
                TabelInputPjgPcs.removeAllViews();
                BtnPrintST.setEnabled(true);
                btnUpdate.setVisibility(View.GONE);
                fabAddDetailData.setVisibility(View.GONE);

            }
        });


        BtnSimpanST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialogHelper.show(SawnTimber.this);

                String noKayuBulat = NoKayuBulat.getText().toString().trim();
                String noPenST = noPenerimaanST.getText().toString().trim();
                MstJenisKayuData selectedJenisKayu = (MstJenisKayuData) SpinKayu.getSelectedItem();
                int jenisKayu = selectedJenisKayu.getIdJenisKayu();
                String namaJenisKayu = selectedJenisKayu.getJenis();
                SpkData selectedSPK = (SpkData) SpinSPK.getSelectedItem();
                String noSPK = selectedSPK.getNoSPK();
                MstSusunData selectedSusun = (MstSusunData) SpinBongkarSusun.getSelectedItem();
                String noBongkarSusun = selectedSusun != null ? selectedSusun.getNoBongkarSusun() : null;
                String telly = ((TellyData) SpinTelly.getSelectedItem()).getIdOrgTelly();
                String stickBy = ((MstStickData) SpinStickBy.getSelectedItem()).getIdStickBy();
                String dateCreate = DateTimeUtils.formatToDatabaseDate(TglStickBundel.getText().toString().trim());
                String dateVacuum = rawDateVacuum;
                String remark = remarkLabel.getText().toString();
                int isSLP = cbSLP.isChecked() ? 1 : 0;
                String isVacuum = cbVacuum.isChecked() ? dateVacuum : null;
                int isSticked = CBStick.isChecked() ? 1 : 0;
                int isKering = CBKering.isChecked() ? 1 : 0;
                int isUpah = CBUpah.isChecked() ? 1 : 0;
                int idUOMTblLebar = radioMillimeter.isChecked() ? 1 : 3;

                // Validasi input
                if (!validateInputs(noKayuBulat, namaJenisKayu, jenisKayu, stickBy, noSPK)) {
                    Toast.makeText(SawnTimber.this, "Silahkan lengkapi data!", Toast.LENGTH_SHORT).show();
                    loadingDialogHelper.hide();
                    return;
                }

                // Check kayu bulat exists
                checkKayuBulatExists(noKayuBulat, namaJenisKayu, exists -> {
                    if (!exists) {
                        Toast.makeText(SawnTimber.this, "No Kayu Bulat tidak ditemukan dalam database!", Toast.LENGTH_SHORT).show();
                        loadingDialogHelper.hide();
                        return;
                    }

                    // Process save in background thread
                    executorService.execute(() -> {
                        try {
                            // Prepare grade list untuk kayu non-lat
                            List<GradeDetailData> gradeListToSave = null;
                            if (!selectedJenisKayu.getJenis().toLowerCase().contains("kayu lat")) {
                                gradeListToSave = temporaryDataListGrade;
                            }

                            int isBagusKulit = 0;
                            if (selectedJenisKayu.getJenis().toLowerCase().contains("kayu lat")) {
                                isBagusKulit = radioBagus.isChecked() ? 1 : (radioKulit.isChecked() ? 2 : 0);
                            }

                            // Call transaction method - return String NoST yang di-generate
                            String generatedNoST = SawnTimberApi.saveSawnTimberTransaction(
                                    noKayuBulat, String.valueOf(jenisKayu), noSPK, telly, stickBy,
                                    dateCreate, isVacuum, remark, isSLP, isSticked, isKering,
                                    isBagusKulit, isUpah, idUOMTblLebar, 4,
                                    temporaryDataListDetail, gradeListToSave, noPenST,
                                    labelVersion, noBongkarSusun, cbBongkarSusun.isChecked()
                            );

                            runOnUiThread(() -> {
                                if (generatedNoST != null) {
                                    // Update UI dengan NoST yang baru di-generate
                                    NoST.setText(generatedNoST);
                                    noST = generatedNoST; // Update variable global juga

                                    BtnDataBaruST.setVisibility(View.VISIBLE);
                                    BtnSimpanST.setVisibility(View.GONE);
                                    BtnPrintST.setEnabled(true);
                                    fabAddDetailData.setVisibility(View.GONE);
                                    disableForm();
                                    onClickDateOutput(dateCreate);

                                    loadingDialogHelper.hide();

                                    Toast.makeText(SawnTimber.this,
                                            "Data berhasil disimpan",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    loadingDialogHelper.hide();
                                    Toast.makeText(SawnTimber.this,
                                            "Gagal menyimpan data. Silakan coba lagi.",
                                            Toast.LENGTH_LONG).show();
                                }
                            });

                        } catch (Exception e) {
                            runOnUiThread(() -> {
                                loadingDialogHelper.hide();

                                Toast.makeText(SawnTimber.this,
                                        "Error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            });

                        }
                    });
                });
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Pastikan noST sudah ada (sedang dalam mode edit)
                if (noST == null || noST.isEmpty()) {
                    Toast.makeText(SawnTimber.this, "Tidak ada data yang dipilih untuk diupdate!", Toast.LENGTH_SHORT).show();
                    loadingDialogHelper.hide();
                    return;
                }

                String noKayuBulat = NoKayuBulat.getText().toString().trim();
                String noPenST = noPenerimaanST.getText().toString().trim();
                MstJenisKayuData selectedJenisKayu = (MstJenisKayuData) SpinKayu.getSelectedItem();
                int jenisKayu = selectedJenisKayu.getIdJenisKayu();
                String namaJenisKayu = selectedJenisKayu.getJenis();
                SpkData selectedSPK = (SpkData) SpinSPK.getSelectedItem();
                String noSPK = selectedSPK.getNoSPK();
                MstSusunData selectedSusun = (MstSusunData) SpinBongkarSusun.getSelectedItem();
                String noBongkarSusun = selectedSusun != null ? selectedSusun.getNoBongkarSusun() : null;
                String telly = ((TellyData) SpinTelly.getSelectedItem()).getIdOrgTelly();
                String stickBy = ((MstStickData) SpinStickBy.getSelectedItem()).getIdStickBy();
                String dateCreate = DateTimeUtils.formatToDatabaseDate(TglStickBundel.getText().toString().trim());
                String dateVacuum = rawDateVacuum;
                String remark = remarkLabel.getText().toString();
                int isSLP = cbSLP.isChecked() ? 1 : 0;
                String isVacuum = cbVacuum.isChecked() ? dateVacuum : null;
                int isSticked = CBStick.isChecked() ? 1 : 0;
                int isKering = CBKering.isChecked() ? 1 : 0;
                int isUpah = CBUpah.isChecked() ? 1 : 0;
                int idUOMTblLebar = radioMillimeter.isChecked() ? 1 : 3;

                // Validasi input
                if (!validateInputs(noKayuBulat, namaJenisKayu, jenisKayu, stickBy, noSPK)) {
                    Toast.makeText(SawnTimber.this, "Silahkan lengkapi data!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check kayu bulat exists (jika bukan kayu lat dan noKayuBulat tidak kosong)
                if (noKayuBulat.isEmpty() && !noPenST.isEmpty()) {
                    checkKayuBulatExists(noKayuBulat, namaJenisKayu, exists -> {
                        if (!exists) {
                            Toast.makeText(SawnTimber.this, "No Kayu Bulat tidak ditemukan dalam database!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Lanjutkan proses update jika kayu bulat valid
                        processUpdateData(noKayuBulat, jenisKayu, noSPK, telly, stickBy, dateCreate,
                                isVacuum, remark, isSLP, isSticked, isKering, isUpah, selectedJenisKayu,
                                idUOMTblLebar, noPenST, noBongkarSusun);
                    });
                } else {
                    // Langsung proses update jika kayu lat atau noKayuBulat kosong
                    processUpdateData(noKayuBulat, jenisKayu, noSPK, telly, stickBy, dateCreate,
                            isVacuum, remark, isSLP, isSticked, isKering, isUpah, selectedJenisKayu,
                            idUOMTblLebar, noPenST, noBongkarSusun);
                }
            }
        });


        SpinKayu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isCreateMode){
                    SpinKayu.setBackgroundResource(R.drawable.border_input);
                    MstJenisKayuData selectedJenisKayu = (MstJenisKayuData) parent.getItemAtPosition(position);
                    if (selectedJenisKayu.getIsUpah() == 1) {
                        CBUpah.setChecked(true);
                    } else {
                        CBUpah.setChecked(false);
                    }

                    if (selectedJenisKayu.getJenis().toLowerCase().contains("kayu lat")) {
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
                if (NoST.getText() == null || NoST.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SawnTimber.this, "Nomor ST tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validasi apakah ada data yang akan dicetak
                if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
                    Toast.makeText(SawnTimber.this, "Tidak ada data untuk dicetak", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cek status HasBeenPrinted di database
                String noST = NoST.getText().toString().trim();
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
                            String noKayuBulat = NoKayuBulat.getText() != null ? NoKayuBulat.getText().toString().trim() : "";
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


        // Atur listener untuk cb Bongkar MstSusunData
        cbBongkarSusun.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Jika checkbox dicentang, aktifkan Bongkar MstSusunData Spinner
            if (isChecked) {
                SpinBongkarSusun.setEnabled(true);
            } else {
                // Jika checkbox tidak dicentang, nonaktifkan Bongkar MstSusunData Spinner
                SpinBongkarSusun.setEnabled(false);
            }
        });

        btnSwapToUOM.setOnClickListener(v -> {
            flipCard(inner_card_label_list, inner_card_top_right);
        });

        btnSwapToLabelList.setOnClickListener(v -> {
            flipCard(inner_card_top_right, inner_card_label_list);
        });


        radioGroupUOMTblLebar.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioMillimeter) {
                // Kalau Millimeter dipilih
                m3();
                ton();
                jumlahPcsST();

            } else if (checkedId == R.id.radioInch) {
                // Kalau Inch dipilih
                m3();
                ton();
                jumlahPcsST();
            }
        });
    }



    //METHOD SAWN TIMBER

    // Method helper untuk proses update
    private void processUpdateData(String noKayuBulat, int jenisKayu, String noSPK, String telly,
                                   String stickBy, String dateCreate, String isVacuum, String remark,
                                   int isSLP, int isSticked, int isKering, int isUpah,
                                   MstJenisKayuData selectedJenisKayu,
                                   int idUOMTblLebar, String noPenST, String noBongkarSusun) {

        loadingDialogHelper.show(this);

        // Process update in background thread
        executorService.execute(() -> {
            try {
                // ⬇️ Tambahkan log untuk memeriksa nilai dateCreate
                Log.d("processUpdateData", "dateCreate dikirim: " + dateCreate);

                // Prepare grade list untuk kayu non-lat
                List<GradeDetailData> gradeListToSave = null;
                if (!selectedJenisKayu.getJenis().toLowerCase().contains("kayu lat")) {
                    gradeListToSave = temporaryDataListGrade;
                }

                int isBagusKulit = 0;
                if (selectedJenisKayu.getJenis().toLowerCase().contains("kayu lat")) {
                    isBagusKulit = radioBagus.isChecked() ? 1 : (radioKulit.isChecked() ? 2 : 0);
                }

                // Call update transaction method
                boolean updateSuccess = SawnTimberApi.updateSawnTimberTransaction(
                        noST, noKayuBulat, String.valueOf(jenisKayu), noSPK, telly, stickBy,
                        dateCreate, isVacuum, remark, isSLP, isSticked, isKering,
                        isBagusKulit, isUpah, idUOMTblLebar, 4,
                        temporaryDataListDetail, gradeListToSave, noPenST,
                        labelVersion, noBongkarSusun, cbBongkarSusun.isChecked()
                );

                runOnUiThread(() -> {
                    if (updateSuccess) {
                        btnUpdate.setVisibility(View.GONE);
                        BtnSimpanST.setVisibility(View.GONE);
                        BtnDataBaruST.setVisibility(View.VISIBLE);
                        BtnPrintST.setEnabled(true);
                        fabAddDetailData.setVisibility(View.GONE);
                        disableForm();

                        Toast.makeText(SawnTimber.this,
                                "Data berhasil diupdate untuk No ST: " + noST,
                                Toast.LENGTH_SHORT).show();

                        loadingDialogHelper.hide();
                    } else {
                        Toast.makeText(SawnTimber.this,
                                "Gagal mengupdate data. Silakan coba lagi.",
                                Toast.LENGTH_LONG).show();

                        loadingDialogHelper.hide();
                    }
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(SawnTimber.this,
                            "Error update: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    loadingDialogHelper.hide();

                });
            }
        });
    }



    // Method validasi terpisah untuk cleaner code
    private boolean validateInputs(String noKayuBulat, String namaJenisKayu, int jenisKayu,
                                   String stickBy, String noSPK) {
        boolean valid = true;

        // Reset background
        NoKayuBulat.setBackgroundResource(R.drawable.border_input);
        SpinKayu.setBackgroundResource(R.drawable.border_input);
        SpinStickBy.setBackgroundResource(R.drawable.border_input);
        SpinSPK.setBackgroundResource(R.drawable.border_input);

        if (noKayuBulat.isEmpty() && labelVersion == -1 && !namaJenisKayu.toLowerCase().contains("kayu lat")) {
            NoKayuBulat.setBackgroundResource(R.drawable.spinner_error);
            valid = false;
        }

        if (jenisKayu <= 0) {
            SpinKayu.setBackgroundResource(R.drawable.spinner_error);
            valid = false;
        }

        if (stickBy.isEmpty()) {
            SpinStickBy.setBackgroundResource(R.drawable.spinner_error);
            valid = false;
        }

        if (noSPK.isEmpty() || noSPK.equals("PILIH")) {
            SpinSPK.setBackgroundResource(R.drawable.spinner_error);
            valid = false;
        }

        return valid;
    }


    //LABEL LIST
    private void showListDialogOnDemand() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_list_item_st, null);

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
            int totalLabel = SawnTimberApi.getTotalLabelCount("");

            // 🔹 Update UI kembali
            runOnUiThread(() -> {
                tvSumLabel.setText("LIST LABEL ST " + "(" + String.valueOf(totalLabel) + ")");

            });

        });


        // Variable untuk menyimpan data yang dipilih
        final StData[] selectedData = {null};

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

                // Mengisi NoS4S dengan data yang dipilih
                NoST.setText(selectedData[0].getNoST());

                // Tutup dialog
                dialog.dismiss();

                btnUpdate.setVisibility(View.VISIBLE);
                BtnSimpanST.setVisibility(View.GONE);
                BtnDataBaruST.setVisibility(View.GONE);


                SpinBongkarSusun.setVisibility(View.GONE);
                susunView.setVisibility(View.VISIBLE);

                fabAddDetailData.setVisibility(View.VISIBLE);

                btnUpdate.setVisibility(View.VISIBLE);


                // Optional: tampilkan pesan sukses
//                Toast.makeText(this, "Data dipilih: " + selectedData[0].getNoS4S(), Toast.LENGTH_SHORT).show();
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
                        .setMessage("Apakah Anda yakin ingin menghapus data " + selectedData[0].getNoST() + "?")
                        .setPositiveButton("Ya", (dialogInterface, i) -> {
                            executorService.execute(() -> {
                                try {
                                    // 🔹 Jalankan delete di background thread
                                    boolean success = SawnTimberApi.deleteSawnTimberTransaction(selectedData[0].getNoST());

                                    // 🔹 Update UI kembali
                                    runOnUiThread(() -> {
                                        if (success) {
                                            Toast.makeText(this,
                                                    "Data " + selectedData[0].getNoST() + " dihapus",
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
            List<StData> list = SawnTimberApi.getSawnTimberData(page, 50, "");

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

    private void loadSearchData(TableLayout tableLayout, ProgressBar loadingIndicator, String keyword, StData[] selectedData) {
        loadingIndicator.setVisibility(View.VISIBLE);
        tableLayout.removeAllViews();

        executorService.execute(() -> {
            List<StData> list = SawnTimberApi.getSawnTimberData(page, 50, keyword);

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

    private void loadMoreData(TableLayout tableLayout, StData[] selectedData) {
        executorService.execute(() -> {
            List<StData> moreData = SawnTimberApi.getSawnTimberData(currentPage, 50, "");
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
    private void addRowsToTable(TableLayout tableLayout, List<StData> list, int startRowIndex, StData[] selectedData) {
        int rowIndex = startRowIndex;

        for (StData data : list) {
            TableRow row = new TableRow(this);
            row.setTag(rowIndex);

            TextView col1 = TableUtils.createTextView(this, data.getNoST(), 1f);
            TextView col2 = TableUtils.createTextView(this, DateTimeUtils.formatDate(data.getDateCreate()), 1f);
            TextView col3 = TableUtils.createTextView(this, data.getNoKayuBulat(), 1f);
            TextView col4 = TableUtils.createTextView(this, data.getJenis(), 1f);
            TextView col5 = TableUtils.createTextView(this, data.getNoSPK(), 1f);
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

    private void onRowClick(StData data) {

        noST = data.getNoST();

        // Tampilkan tooltip
        TooltipUtils.fetchDataAndShowTooltip(
                this,
                executorService,
                selectedRowHeader,
                data.getNoST(), // currentNoS4S
                "ST_h",
                "ST_d",
                "NoST",
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
                for (LabelDetailData row : temporaryDataListDetail) {
                    if (Float.parseFloat(row.getPanjang()) == panjangAcuan) {
                        int updatedPcs = Integer.parseInt(row.getPcs()) + Integer.parseInt(pcs); // Tambahkan pcs yang dihapus
                        row.setPcs(String.valueOf(updatedPcs)); // Update pcs
                        sisaPCS.setText("Sisa : " + row.getPcs());
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
                for (LabelDetailData row : temporaryDataListDetail) {
                    if (row.getPanjang().equals(panjangToDelete)) {
                        temporaryDataListDetail.remove(row);  // Hapus data dari list berdasarkan panjang
                        break;
                    }
                }
                // Memperbarui tabel setelah penghapusan data
                // Gunakan updateTableFromTemporaryDataDetail() instead of updateTable()
                updateTableFromTemporaryDataDetail();
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

            for (LabelDetailData row : temporaryDataListDetail) {
                if (Float.parseFloat(row.getPanjang()) == panjangAcuan) {
                    if (Integer.parseInt(row.getPcs()) <= Integer.parseInt(pcs)) {
                        sisaPCS.setText("Sisa : " + row.getPcs());
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

    private void disableForm() {
        NoKayuBulat.setEnabled(false);
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
        TglStickBundel.setEnabled(false);
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

        // Loop semua row di tabel2
        for (int i = 0; i < Tabel2.getChildCount(); i++) {
            View rowView = Tabel2.getChildAt(i);
            if (rowView instanceof TableRow) {
                TableRow row = (TableRow) rowView;
                // Loop semua child di row
                for (int j = 0; j < row.getChildCount(); j++) {
                    View child = row.getChildAt(j);
                    if (child instanceof ImageButton) {
                        child.setEnabled(false);
                        child.setAlpha(0.5f);
                    }
                    else if (child instanceof LinearLayout) {
                        // fallback kalau nanti ada actionLayout juga
                        LinearLayout actionLayout = (LinearLayout) child;
                        for (int k = 0; k < actionLayout.getChildCount(); k++) {
                            View actionChild = actionLayout.getChildAt(k);
                            if (actionChild instanceof ImageButton) {
                                actionChild.setEnabled(false);
                                actionChild.setAlpha(0.5f);
                            }
                        }
                    }
                }
            }
        }

    }

    private void enableForm() {
        NoKayuBulat.setEnabled(true);
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
        NoKayuBulat.setVisibility(View.VISIBLE);
        remarkLabel.setEnabled(true);
        CBKering.setEnabled(true);
        cbSLP.setEnabled(true);
        cbVacuum.setEnabled(true);
        cbBongkarSusun.setEnabled(true);
        radioBagus.setEnabled(true);
        radioKulit.setEnabled(true);
        TglStickBundel.setEnabled(true);

        if (isCreateMode) {
            flipOneToTwo(inner_card_detail, inner_card_top_left, inner_card_top_center);
        } else {
            flipTwoToOne(inner_card_top_left, inner_card_top_center, inner_card_detail);
        }
    }



    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(NoKayuBulat.getWindowToken(), 0);
        }
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

    private void loadSubmittedData(String noST) {
        loadingDialogHelper.show(this);

        // Reset data dan tampilkan loading
        resetDetailData();
        resetGradeData();

        // Gunakan executor service seperti contoh loadSubmittedData
        executorService.execute(() -> {
            try {
                // Load header data menggunakan API class
                StData headerData = SawnTimberApi.getSawnTimberHeader(noST);

                if (headerData != null) {
                    // Load detail dan grade data menggunakan API class
                    List<LabelDetailData> detailDataList = SawnTimberApi.getSawnTimberDetail(noST);
                    List<GradeDetailData> gradeDataList = SawnTimberApi.getSawnTimberGrade(noST);

                    // Update temporary data
                    temporaryDataListDetail.clear();
                    temporaryDataListDetail.addAll(detailDataList);

                    temporaryDataListGrade.clear();
                    temporaryDataListGrade.addAll(gradeDataList);

                    // Update UI di thread utama
                    runOnUiThread(() -> {
                        try {
                            updateUIWithSawnTimberData(headerData);
                            updateTableFromTemporaryDataDetail();
                            updateTableFromTemporaryDataGrade();
                            m3();
                            ton();
                            jumlahPcsST();

                            if (userPermissions.contains("label_st:update")) {
                                enableForm();
                            } else {
                                disableForm();
                            }

                            Toast.makeText(getApplicationContext(),
                                        "Data berhasil dimuat",
                                        Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.e("UI Update Error", "Error updating UI: " + e.getMessage());
                                Toast.makeText(getApplicationContext(),
                                        "Gagal memperbarui tampilan",
                                        Toast.LENGTH_SHORT).show();
                                loadingDialogHelper.hide();
                            }
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(),
                                    "Data Gagal dimuat",
                                    Toast.LENGTH_SHORT).show();
                            loadingDialogHelper.hide();
                        });
                    }
                } catch (Exception e) {
                    final String errorMessage = e.getMessage();
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(),
                                "Error: " + errorMessage,
                                Toast.LENGTH_LONG).show();
                        loadingDialogHelper.hide();
                        Log.e("Database Error", "Error loading sawn timber data", e);
                    });
                }
            });
    }

    /**
     * Method untuk update UI dengan data header yang sudah di-load
     */
    private void updateUIWithSawnTimberData(StData headerData) {
        // ====== total loader async ======
        final int totalTasks = 5; // jumlah spinner yang kamu load di sini
        final AtomicInteger doneCount = new AtomicInteger(0);

        Runnable checkAllDone = () -> {
            if (doneCount.incrementAndGet() == totalTasks) {
                runOnUiThread(() -> loadingDialogHelper.hide());
            }
        };

        // ====== field biasa (langsung set) ======
        NoKayuBulat.setText(headerData.getNoKayuBulat());
        TglStickBundel.setText(DateTimeUtils.formatDate(headerData.getDateCreate()));
        remarkLabel.setText(headerData.getRemark());
        cbSLP.setChecked(headerData.getIsSLP() == 1);

        susunView.setText(headerData.getNoBongkarSusun());
        cbBongkarSusun.setChecked(!headerData.getNoBongkarSusun().equals("-"));

        TglVacuum.setText(headerData.getVacuumDate());
        cbVacuum.setChecked(!headerData.getVacuumDate().equals("-"));

        // ====== panggil semua loader spinner dengan callback ======
        loadJenisKayuSpinner(headerData.getIdJenisKayu(), checkAllDone);
        loadStickBySpinner(headerData.getIdStickBy(), checkAllDone);
        loadSPKSpinner(headerData.getNoSPK(), checkAllDone);
        loadTellySpinner(headerData.getIdOrgTelly(), checkAllDone);
        loadGradeStickSpinner(0, checkAllDone);

        // ====== Logic untuk penerimaan ST (bukan async spinner) ======
        if (headerData.getNoKayuBulat().equals("-") && headerData.getNoPenerimaanSTUpah().equals("-")) {
            loadPenerimaanSTPembelian(headerData.getNoPenerimaanSTPembelian());
            noPenerimaanST.setText(headerData.getNoPenerimaanSTPembelian());
        } else if (headerData.getNoKayuBulat().equals("-") && headerData.getNoPenerimaanSTPembelian().equals("-")) {
            loadPenerimaanSTUpah(headerData.getNoPenerimaanSTUpah());
            noPenerimaanST.setText(headerData.getNoPenerimaanSTUpah());
        } else {
            loadKayuBulat(headerData.getNoKayuBulat());
            noPenerimaanST.setText("-");
        }
    }


    // Method untuk memperbarui tabel dari temporaryDataListDetail
    private void updateTableFromTemporaryDataDetail() {
        rowCount = 0;
        Tabel.removeAllViews(); // reset table sebelum render ulang
        DecimalFormat df = new DecimalFormat("#,###.##");

        for (LabelDetailData data : temporaryDataListDetail) {
            TableRow newRow = new TableRow(this);
            TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            newRow.setLayoutParams(rowParams);

            // Set background warna alternate
            if (rowCount % 2 == 0) {
                newRow.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            // Tambahkan kolom-kolom
            addTextViewToRowWithWeight(newRow, String.valueOf(++rowCount), 0.5f);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(data.getTebal())), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(data.getLebar())), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(data.getPanjang())), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Integer.parseInt(data.getPcs())), 1f);

            // Layout untuk tombol Edit & Delete
            LinearLayout actionLayout = new LinearLayout(this);
            actionLayout.setOrientation(LinearLayout.HORIZONTAL);

            // === Tombol Edit ===
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

            // === Tombol Delete ===
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
                // hapus data dari list
                temporaryDataListDetail.remove(data);

                // render ulang tabel (rowCount otomatis reset)
                updateTableFromTemporaryDataDetail();

                // hitung ulang total
                jumlahPcsST();
                m3();
                ton();
            });

            actionLayout.addView(deleteButton);

            // tambahkan actionLayout ke row
            newRow.addView(actionLayout);

            Tabel.addView(newRow);
        }
    }

    private void showAddDetailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Detail");

        // Inflate layout XML yg sama
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_detail_label, null);
        builder.setView(dialogView);

        TextInputEditText editTebal = dialogView.findViewById(R.id.editTebal);
        TextInputEditText editLebar = dialogView.findViewById(R.id.editLebar);
        TextInputEditText editPanjang = dialogView.findViewById(R.id.editPanjang);
        TextInputEditText editJumlah = dialogView.findViewById(R.id.editJumlah);

        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String tebal = editTebal.getText().toString().trim();
            String lebar = editLebar.getText().toString().trim();
            String panjang = editPanjang.getText().toString().trim();
            String pcs = editJumlah.getText().toString().trim();

            // Validasi input
            if (tebal.isEmpty() || lebar.isEmpty() || panjang.isEmpty() || pcs.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cek duplikasi (hanya itu precondition yg dipakai untuk ADD)
            boolean isDuplicate = false;
            for (LabelDetailData existingData : temporaryDataListDetail) {
                if (existingData.getTebal().equals(tebal) &&
                        existingData.getLebar().equals(lebar) &&
                        existingData.getPanjang().equals(panjang)) {
                    isDuplicate = true;
                    break;
                }
            }

            if (isDuplicate) {
                Toast.makeText(this, "Data dengan ukuran tersebut sudah ada!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tambahkan data baru
            LabelDetailData newData = new LabelDetailData(tebal, lebar, panjang, pcs);
            temporaryDataListDetail.add(newData);

            // Refresh tabel
            Tabel.removeViews(1, Tabel.getChildCount() - 1);
            updateTableFromTemporaryDataDetail();
            jumlahPcsST();
            m3();
            ton();
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
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
            updateTableFromTemporaryDataDetail();
            jumlahPcsST();
            m3();
            ton();
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void updateTableFromTemporaryDataGrade() {
        // Reset table sebelum render ulang
        rowCount = 0;
        Tabel2.removeAllViews();

        for (GradeDetailData data : temporaryDataListGrade) {
            TableRow newRow = new TableRow(this);
            newRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // === Kolom Grade Name (1f)
            addTextViewToRowWithWeight(newRow, data.getGradeName(), 1f);

            // === Kolom Jumlah (0.5f)
            addTextViewToRowWithWeight(newRow, data.getJumlah(), 0.5f);

            // === Kolom Delete Button (0.5f)
            ImageButton deleteButton = new ImageButton(this);
            deleteButton.setImageResource(R.drawable.ic_delete);
            deleteButton.setBackgroundColor(Color.TRANSPARENT);
            deleteButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            deleteButton.setPadding(15, 5, 5, 5);

            TableRow.LayoutParams btnParams = new TableRow.LayoutParams(
                    0, // penting: width 0 supaya weight bekerja
                    TableRow.LayoutParams.WRAP_CONTENT,
                    0.5f // weight 0.5f
            );
            deleteButton.setLayoutParams(btnParams);

            deleteButton.setOnClickListener(v -> {
                temporaryDataListGrade.remove(data);
                updateTableFromTemporaryDataGrade();
                // hitungTotalGrade(); // kalau ada
            });

            newRow.addView(deleteButton);

            // Tambahkan row ke tabel
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


    private void clearTableData() {
        NoST.setText("");
        NoKayuBulat.setText("");
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
        NoKayuBulat.setText("");
        setSpinnerValue(SpinKayu, "-");
        setSpinnerValue(SpinStickBy, "-");
        setSpinnerValue(SpinTelly, "-");
        cbVacuum.setChecked(false);
        cbBongkarSusun.setChecked(false);
        cbSLP.setChecked(false);


    }

    private void ton() {
        try {
            double totalTON = 0.0;
            boolean isMillimeter = radioMillimeter.isChecked();

            for (LabelDetailData row : temporaryDataListDetail) {

                // Parse nilai-nilai langsung tanpa membersihkan
                double tebal = Double.parseDouble(row.getTebal());
                double lebar = Double.parseDouble(row.getLebar());
                double panjang = Double.parseDouble(row.getPanjang());
                int pcs = Integer.parseInt(row.getPcs());

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
            BigDecimal totalM3 = BigDecimal.ZERO;
            boolean isMillimeter = radioMillimeter.isChecked();

            for (LabelDetailData row : temporaryDataListDetail) {

                // Parse nilai-nilai
                BigDecimal tebal = new BigDecimal(row.getTebal());
                BigDecimal lebar = new BigDecimal(row.getLebar());
                BigDecimal panjang = new BigDecimal(row.getPanjang());
                BigDecimal pcs = new BigDecimal(row.getPcs());

                BigDecimal rowM3;

                if (isMillimeter) {
                    // ((tebal * lebar * panjang * pcs * 304.8 / 1000000000 / 1.416 * 10000) / 10000) * 1.416
                    rowM3 = tebal
                            .multiply(lebar)
                            .multiply(panjang)
                            .multiply(pcs)
                            .multiply(BigDecimal.valueOf(304.8))
                            .divide(BigDecimal.valueOf(1000000000), 10, RoundingMode.HALF_UP)
                            .divide(BigDecimal.valueOf(1.416), 10, RoundingMode.HALF_UP);

                    // floor 4 desimal
                    rowM3 = rowM3.multiply(BigDecimal.valueOf(10000));
                    rowM3 = rowM3.setScale(0, RoundingMode.FLOOR);
                    rowM3 = rowM3.divide(BigDecimal.valueOf(10000), 4, RoundingMode.HALF_UP);

                    rowM3 = rowM3.multiply(BigDecimal.valueOf(1.416));

                } else {
                    // ((tebal * lebar * panjang * pcs / 7200.8 * 10000) / 10000) * 1.416
                    rowM3 = tebal
                            .multiply(lebar)
                            .multiply(panjang)
                            .multiply(pcs)
                            .divide(BigDecimal.valueOf(7200.8), 10, RoundingMode.HALF_UP);

                    // floor 4 desimal
                    rowM3 = rowM3.multiply(BigDecimal.valueOf(10000));
                    rowM3 = rowM3.setScale(0, RoundingMode.FLOOR);
                    rowM3 = rowM3.divide(BigDecimal.valueOf(10000), 4, RoundingMode.HALF_UP);

                    rowM3 = rowM3.multiply(BigDecimal.valueOf(1.416));
                }

                totalM3 = totalM3.add(rowM3);
            }

            // Format hasil dengan 4 desimal
            DecimalFormat df = new DecimalFormat("0.0000");
            String formattedM3 = df.format(totalM3);

            // Update TextView
            TextView M3TextView = findViewById(R.id.M3ST);
            if (M3TextView != null) {
                M3TextView.setText(formattedM3);
                Log.d("M3_DEBUG", "TextView updated with: " + formattedM3);
            } else {
                Log.e("M3_DEBUG", "M3TextView is null!");
            }

        } catch (Exception e) {
            Log.e("M3_DEBUG", "Error in M3 calculation: " + e.getMessage(), e);
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
        runOnUiThread(() -> {
            TextView labelCountView = findViewById(R.id.labelCount);

            if (TableOutput == null) {
                Log.e("populateTableOutput", "TableOutput tidak ditemukan di layout!");
                return;
            }

            TableOutput.removeAllViews();
            selectedRowHeader = null; // Reset setiap kali load data baru

            int labelCount = 0;

            if (noSTList == null || noSTList.isEmpty()) {
                TextView noData = new TextView(this);
                noData.setText("Tidak ada label");
                noData.setPadding(16, 16, 16, 16);
                noData.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                TableOutput.addView(noData);

                if (labelCountView != null) {
                    labelCountView.setText("Total : 0");
                }
                return;
            }

            for (int i = 0; i < noSTList.size(); i++) {
                OutputDataST data = noSTList.get(i);

                TableRow row = new TableRow(this);
                TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                );
                row.setLayoutParams(rowParams);
                row.setPadding(0, 10, 0, 10);

                // Set tag index row
                row.setTag(i);

                // Warna selang-seling
                if (i % 2 == 0) {
                    row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
                } else {
                    row.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                }

                // === Kolom 1: NoST ===
                TextView noSTView = new TextView(this);
                noSTView.setText(data.getNoST());
                noSTView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                noSTView.setGravity(Gravity.CENTER);
                noSTView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                row.addView(noSTView);

                // === Kolom 2: Ikon Printed ===
                ImageView iconView = new ImageView(this);
                iconView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
                iconView.setScaleType(ImageView.ScaleType.CENTER);
                int iconRes = data.isHasBeenPrinted()
                        ? R.drawable.ic_done
                        : R.drawable.ic_undone;
                iconView.setImageResource(iconRes);
                iconView.setColorFilter(ContextCompat.getColor(this, R.color.primary_dark));
                row.addView(iconView);

                // === Event klik row ===
                row.setOnClickListener(v -> {
                    // Reset row sebelumnya kalau ada
                    if (selectedRowHeader != null && selectedRowHeader.getTag() != null) {
                        int prevIndex = (int) selectedRowHeader.getTag();
                        if (prevIndex % 2 == 0) {
                            selectedRowHeader.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
                        } else {
                            selectedRowHeader.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                        }
                    }

                    // Highlight row baru
                    selectedRowHeader = row;
                    row.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
                    TableUtils.setTextColor(this, row, R.color.white);

                    // Tooltip dengan callback reset warna
                    TooltipUtils.fetchDataAndShowTooltip(
                            this,
                            executorService,
                            selectedRowHeader,
                            data.getNoST(),
                            "ST_h",
                            "ST_d",
                            "NoST",
                            () -> {
                                if (selectedRowHeader != null && selectedRowHeader.getTag() != null) {
                                    int currentIndex = (int) selectedRowHeader.getTag();
                                    if (currentIndex % 2 == 0) {
                                        selectedRowHeader.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
                                    } else {
                                        selectedRowHeader.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                                    }
                                    TableUtils.setTextColor(this, row, R.color.black);
                                    selectedRowHeader = null;
                                } else {
                                    selectedRowHeader = null;
                                }
                            }
                    );
                });

                TableOutput.addView(row);
                labelCount++;
            }

            if (labelCountView != null) {
                labelCountView.setText("Total : " + labelCount);
            }
        });
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
                          List<LabelDetailData> temporaryDataListDetail, String noKayuBulat, String namaSupplier, String noTruk, String jumlahPcs, String m3, String ton,
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

                for (LabelDetailData row : temporaryDataListDetail) {
                    String tebal = (row.getTebal() != null) ? df.format(Float.parseFloat(row.getTebal())) : "-";
                    String lebar = (row.getLebar() != null) ? df.format(Float.parseFloat(row.getLebar())) : "-";
                    String panjang = (row.getPanjang() != null) ? df.format(Float.parseFloat(row.getPanjang())) : "-";
                    String pcs = (row.getPcs() != null) ? df.format(Integer.parseInt(row.getPcs())) : "-";

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
                    loadSusunSpinner(rawDate);
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
        for (LabelDetailData existingData : temporaryDataListDetail) {
            if (existingData.getTebal().equals(tebal) && existingData.getPanjang().equals(panjang) && existingData.getLebar().equals(lebar)) {
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
                for (LabelDetailData dataRow : temporaryDataListDetail) {
                    if (Float.parseFloat(dataRow.getPanjang()) == panjangAcuan) {
                        int updatedPcs = Integer.parseInt(dataRow.getPcs()) - Integer.parseInt(pcs); // Kurangi pcs
                        dataRow.setPcs(String.valueOf(updatedPcs)); // Update nilai pcs
                        sisaPCS.setText("Sisa : " + dataRow.getPcs());
                    }
                }
            }

            // Buat objek LabelDetailData baru
            LabelDetailData newDataRow = new LabelDetailData(tebal, lebar, panjang, pcs);
            temporaryDataListDetail.add(newDataRow);

            // Urutkan data berdasarkan panjang
            Collections.sort(temporaryDataListDetail, new Comparator<LabelDetailData>() {
                @Override
                public int compare(LabelDetailData o1, LabelDetailData o2) {
                    return Float.compare(Float.parseFloat(o1.getPanjang()), Float.parseFloat(o2.getPanjang()));
                }
            });

            // Gunakan updateTableFromTemporaryDataDetail() instead of updateTable()
            updateTableFromTemporaryDataDetail();

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
    private List<ImageButton> deleteButtons = new ArrayList<>();


    private void addDataGrade(String noST) {
        MstGradeStickData selectedGrade = (MstGradeStickData) SpinGrade.getSelectedItem();
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
            // Buat objek GradeDetailData baru
            GradeDetailData newDataRow = new GradeDetailData(gradeId, gradeName, jumlah);
            temporaryDataListGrade.add(newDataRow);

            // ✅ Gunakan method render ulang agar konsisten
            updateTableFromTemporaryDataGrade();

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
        for (GradeDetailData data : temporaryDataListGrade) {
            if (data.getGradeId() == gradeId) {  // Akses langsung ke field gradeId
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







    private void loadGradeStickSpinner(int selectedIdGrade, @Nullable Runnable onDone) {
        executorService.execute(() -> {
            // Ambil data grade stick
            List<MstGradeStickData> gradeStickList = SawnTimberApi.getGradeStickList();

            runOnUiThread(() -> {
                ArrayAdapter<MstGradeStickData> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        gradeStickList
                );
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinGrade.setAdapter(adapter);

                // Set default selection
                if (selectedIdGrade == 0) {
                    SpinGrade.setSelection(0);
                } else {
                    for (int i = 0; i < gradeStickList.size(); i++) {
                        if (gradeStickList.get(i).getIdGradeStick() == selectedIdGrade) {
                            SpinGrade.setSelection(i);
                            break;
                        }
                    }
                }

                if (onDone != null) onDone.run();
            });
        });
    }

    // Overload tanpa callback
    private void loadGradeStickSpinner(int selectedIdGrade) {
        loadGradeStickSpinner(selectedIdGrade, null);
    }




    // Versi baru dengan callback
    private void loadSusunSpinner(@Nullable Runnable onDone, String... params) {
        executorService.execute(() -> {
            String selectedDate;
            if (params != null && params.length > 0) {
                selectedDate = params[0];
            } else {
                selectedDate = TglStickBundel.getText().toString();
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
                    SpinBongkarSusun.setAdapter(adapter);
                } else {
                    Log.e("Error", "Failed to load susun data.");
                    SpinBongkarSusun.setAdapter(null);
                    TableOutput.removeAllViews();
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
            List<MstJenisKayuData> jenisKayuList = MasterApi.getJenisKayuSTList();
            jenisKayuList.add(0, new MstJenisKayuData(0, "PILIH", 0));

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
                SpinLokasi.setAdapter(adapter);

                // Set default selection
                if (selectedIdLokasi == null || selectedIdLokasi.equals("0")) {
                    SpinLokasi.setSelection(0);
                } else {
                    for (int i = 0; i < lokasiList.size(); i++) {
                        if (lokasiList.get(i).getIdLokasi().equals(selectedIdLokasi)) {
                            SpinLokasi.setSelection(i);
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
    private void loadStickBySpinner(String selectedIdStickBy, @Nullable Runnable onDone) {
        executorService.execute(() -> {
            // Ambil data stick by dari DB
            List<MstStickData> stickByList = SawnTimberApi.getStickByList();

            runOnUiThread(() -> {
                ArrayAdapter<MstStickData> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        stickByList
                );
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                SpinStickBy.setAdapter(adapter);

                // Set default selection berdasarkan selectedIdStickBy
                if (selectedIdStickBy == null || selectedIdStickBy.isEmpty()) {
                    SpinStickBy.setSelection(0); // pilih dummy "PILIH"
                } else {
                    for (int i = 0; i < stickByList.size(); i++) {
                        if (stickByList.get(i).getIdStickBy().equals(selectedIdStickBy)) {
                            SpinStickBy.setSelection(i);
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
    private void loadStickBySpinner(String selectedIdStickBy) {
        loadStickBySpinner(selectedIdStickBy, null);
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
