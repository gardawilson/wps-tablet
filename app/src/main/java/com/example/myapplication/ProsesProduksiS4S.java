package com.example.myapplication;
import com.example.myapplication.api.MasterApi;
import com.example.myapplication.api.ProsesProduksiApi;
import com.example.myapplication.model.MesinProsesProduksiData;
import com.example.myapplication.model.MstOperatorData;
import com.example.myapplication.model.SawmillData;
import com.example.myapplication.model.SpkData;
import com.example.myapplication.model.TableConfig;
import com.example.myapplication.utils.CustomProgressDialog;
import com.example.myapplication.utils.DateTimeUtils;
import com.example.myapplication.utils.LoadingDialogHelper;
import com.example.myapplication.utils.PdfUtils;
import com.example.myapplication.utils.PermissionUtils;
import com.example.myapplication.utils.SharedPrefUtils;
import com.example.myapplication.utils.ScannerAnimationUtils;
import com.example.myapplication.utils.TableConfigUtils;




import static com.example.myapplication.api.ProsesProduksiApi.isTransactionPeriodClosed;
import static com.example.myapplication.config.ApiEndpoints.CRYSTAL_REPORT_WPS_EXPORT_PDF;


import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.app.AlertDialog;
import android.Manifest;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.model.HistoryItem;
import com.example.myapplication.model.ProductionData;
import com.example.myapplication.utils.CameraUtils;
import com.example.myapplication.utils.CameraXAnalyzer;
import com.example.myapplication.utils.TooltipUtils;
import com.example.myapplication.utils.ViewUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import android.util.DisplayMetrics;
import android.text.InputType;


public class ProsesProduksiS4S extends AppCompatActivity {

    private TableLayout tableLayout;
    private TableLayout headerTableProduksi;
    private PreviewView cameraPreview;
    private Button btnCameraControl;
    private Button btnSimpan;
    private TableRow selectedRowHeader;
    private TableRow selectedRow;
    private TextView qrResultText;
    private TextView noProduksiView;
    private TextView tglProduksiView;
    private TextView mesinProduksiView;
    private boolean isShowingResult = false;
    private ConstraintLayout scanLayout;
    private String noProduksi; // Variabel global
    private String tglProduksi; // Variabel global
    private String mesinProduksi; // Variabel global
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ProcessCameraProvider cameraProvider;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final List<String> scannedResults = new ArrayList<>();
    private boolean isCameraActive = false;
    private ProductionData selectedProductionData;
    private EditText kodeLabel;
    private EditText searchMainTable;
    private TextInputLayout textLayoutSearchMainTable;
    private ImageButton btnInputKodeLabel;
    private LinearLayout inputKodeManual;
    private List<String> noS4SList = new ArrayList<>(); // Daftar untuk kode 'R'
    private List<String> noSTList = new ArrayList<>();  // Daftar untuk kode 'E'
    private List<String> noMouldingList = new ArrayList<>();  // Daftar untuk kode 'T'
    private List<String> noFJList = new ArrayList<>();  // Daftar untuk kode 'S'
    private List<String> noCCList = new ArrayList<>();  // Daftar untuk kode 'V'
    private List<String> noReprosesList = new ArrayList<>();  // Daftar untuk kode 'Y'
    private List<String> noLaminatingList = new ArrayList<>();  // Daftar untuk kode 'U'
    private List<String> noSandingList = new ArrayList<>();  // Daftar untuk kode 'W'
    private List<String> noPackingList = new ArrayList<>();  // Daftar untuk kode 'I'
    private List<ProductionData> dataList; // Data asli yang tidak difilter
    private ProgressBar loadingIndicator;
    private TableLayout noS4STableLayout;
    private TableLayout noSTTableLayout;
    private TableLayout noMouldingTableLayout;
    private TableLayout noFJTableLayout;
    private TableLayout noCCTableLayout;
    private TableLayout noReprosesTableLayout;
    private LinearLayout jumlahLabel;
    private LinearLayout jumlahLabelHeader;
    private TextView sumS4SLabel;
    private TextView sumSTLabel;
    private TextView sumMouldingLabel;
    private TextView sumFJLabel;
    private TextView sumCCLabel;
    private TextView sumReprosesLabel;
    private View borderTop;
    private View borderBottom;
    private View borderLeft;
    private View borderRight;
    private View btnHistorySave;
    private AlertDialog dialog;
    private CustomProgressDialog customProgressDialog;
    private View scannerOverlay;
    private final Map<String, Long> recentlyAddedResults = new HashMap<>();
    private static final long RECENTLY_ADDED_INTERVAL = 2000; // 2 detik
    private final Handler handler = new Handler(Looper.getMainLooper());
    private long lastToastTime = 0; // Untuk throttling toast
    private static final long TOAST_INTERVAL = 2000; // Interval toast (2 detik)
    private ProgressBar loadingIndicatorNoS4S;
    private ProgressBar loadingIndicatorNoST;
    private ProgressBar loadingIndicatorNoMoulding;
    private ProgressBar loadingIndicatorNoFJ;
    private ProgressBar loadingIndicatorNoCC;
    private ProgressBar loadingIndicatorNoReproses;
    private LinearLayout textScanQR;
    private Button btnInputManual;
    private Button btnEdit;
    private Button btnPrint;
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();
    private final String mainTable = "S4SProduksi_h";
    private List<String> userPermissions;
    private String username;
    private Spinner spinSPK;
    private MaterialSwitch switchSPK;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_produksi_s4_s);

        // Inisialisasi komponen UI
        tableLayout = findViewById(R.id.tableLayout);
        headerTableProduksi = findViewById(R.id.headerTableProduksi);
        cameraPreview = findViewById(R.id.cameraPreview);
        btnCameraControl = findViewById(R.id.btnCameraControl);
        qrResultText = findViewById(R.id.qrResultText);
        scanLayout = findViewById(R.id.scanLayout);
        noProduksiView = findViewById(R.id.noProduksiView);
        tglProduksiView = findViewById(R.id.tglProduksiView);
        mesinProduksiView = findViewById(R.id.mesinProduksiView);
        btnSimpan = findViewById(R.id.btnSimpan);
        kodeLabel = findViewById(R.id.kodeLabel);
        searchMainTable = findViewById(R.id.searchMainTable);
        btnInputKodeLabel = findViewById(R.id.btnInputKodeLabel);
        inputKodeManual = findViewById(R.id.inputKodeManual);
        textLayoutSearchMainTable = findViewById(R.id.textLayoutSearchMainTable);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        noS4STableLayout = findViewById(R.id.noS4STableLayout);
        noSTTableLayout = findViewById(R.id.noSTTableLayout);
        noMouldingTableLayout = findViewById(R.id.noMouldingTableLayout);
        noFJTableLayout = findViewById(R.id.noFJTableLayout);
        noCCTableLayout = findViewById(R.id.noCCTableLayout);
        noReprosesTableLayout = findViewById(R.id.noReprosesTableLayout);
        jumlahLabel = findViewById(R.id.jumlahLabel);
        jumlahLabelHeader = findViewById(R.id.jumlahLabelHeader);
        sumS4SLabel = findViewById(R.id.sumS4SLabel);
        sumSTLabel = findViewById(R.id.sumSTLabel);
        sumMouldingLabel = findViewById(R.id.sumMouldingLabel);
        sumFJLabel = findViewById(R.id.sumFJLabel);
        sumCCLabel = findViewById(R.id.sumCCLabel);
        sumReprosesLabel = findViewById(R.id.sumReprosesLabel);
        borderTop = findViewById(R.id.borderTop);
        borderBottom = findViewById(R.id.borderBottom);
        borderLeft = findViewById(R.id.borderLeft);
        borderRight = findViewById(R.id.borderRight);
        btnHistorySave = findViewById(R.id.btnHistorySave);
        loadingIndicatorNoS4S = findViewById(R.id.loadingIndicatorNoS4S);
        loadingIndicatorNoST = findViewById(R.id.loadingIndicatorNoST);
        loadingIndicatorNoMoulding = findViewById(R.id.loadingIndicatorNoMoulding);
        loadingIndicatorNoFJ = findViewById(R.id.loadingIndicatorNoFJ);
        loadingIndicatorNoCC = findViewById(R.id.loadingIndicatorNoCC);
        loadingIndicatorNoReproses = findViewById(R.id.loadingIndicatorNoReproses);
        btnInputManual = findViewById(R.id.btnInputManual);
        textScanQR = findViewById(R.id.textScanQR);
        btnEdit = findViewById(R.id.btnEdit);
        btnPrint = findViewById(R.id.btnPrint);
        spinSPK = findViewById(R.id.spinSPK);
        switchSPK = findViewById(R.id.switchSPK);

        username = SharedPrefUtils.getUsername(this);

        loadingIndicator.setVisibility(View.VISIBLE);
        spinSPK.setVisibility(View.GONE);
        switchSPK.setVisibility(View.GONE);


        // Inisialisasi View scanner overlay
        scannerOverlay = findViewById(R.id.scannerOverlay);

        // Dapatkan DisplayMetrics untuk tinggi layar
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        // Mulai animasi scanner menggunakan ScannerAnimationUtils
        ScannerAnimationUtils.startScanningAnimation(scannerOverlay, displayMetrics);

        Log.d("ScannedResult Value", scannedResults.toString());



        //PERMISSION CHECK
        userPermissions = SharedPrefUtils.getPermissions(this);
        PermissionUtils.permissionCheck(this, btnEdit, "proses_s4s:update");


        // 🔹 Set listener untuk perubahan ON/OFF
        switchSPK.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.d("SPK_SWITCH", "SPK diaktifkan");

                // Pastikan spinner terlihat tapi transparan di awal
                spinSPK.setVisibility(View.VISIBLE);
                spinSPK.setAlpha(0f);
                spinSPK.setTranslationY(30f); // turun 30px untuk efek "slide up"

                // Animasi muncul (fade in + slide up)
                spinSPK.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(200)
                        .setInterpolator(new android.view.animation.DecelerateInterpolator())
                        .start();

            } else {
                Log.d("SPK_SWITCH", "SPK dimatikan");

                // Animasi hilang (fade out + slide down)
                spinSPK.animate()
                        .alpha(0f)
                        .translationY(30f)
                        .setDuration(150)
                        .setInterpolator(new android.view.animation.AccelerateInterpolator())
                        .withEndAction(() -> spinSPK.setVisibility(View.GONE))
                        .start();

                loadSPKSpinner(null, null);
            }
        });



        // Menangani tombol back menggunakan OnBackPressedDispatcher
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();

        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (scannedResults != null && !scannedResults.isEmpty()) {
                    showExitConfirmationDialog();
                } else {
                    remove();  // Hapus callback agar tidak dipanggil lagi
                    // Jika tidak ada data di list, lakukan back seperti biasa
                    onBackPressedDispatcher.onBackPressed();  // Memanggil aksi default back
                }
            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noProduksi = noProduksiView.getText().toString();
                if (!noProduksi.isEmpty()) {
                    showEditProductionDialog();
                } else {
                    Toast.makeText(ProsesProduksiS4S.this, "Pilih NoProduksi Terlebih Dahulu!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPrint.setOnClickListener(v -> {
            if (selectedProductionData != null) {

                String reportName = "CrProduksiS4S";

                String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                        + "?NoProduksi=" + noProduksi
                        + "&Username=" + username
                        + "&reportName=" + reportName;

                loadingDialogHelper.show(this);
                PdfUtils.downloadAndOpenPDF(
                        this,
                        url,
                        "ProsesProduksiS4S_" + noProduksi + ".pdf",
                        executorService,
                        loadingDialogHelper
                );
            } else {
                Toast.makeText(this, "Pilih data terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });

        btnInputManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noProduksi = noProduksiView.getText().toString();

                if (!noProduksi.isEmpty()) {
                    // Membuat layout untuk dialog
                    LinearLayout layout = new LinearLayout(ProsesProduksiS4S.this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(50, 50, 50, 50);
                    layout.setGravity(Gravity.CENTER_HORIZONTAL);

                    // Membuat TextInputLayout untuk EditText yang lebih modern
                    TextInputLayout textInputLayout = new TextInputLayout(ProsesProduksiS4S.this);
                    textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_FILLED);

                    // Membuat EditText di dalam TextInputLayout
                    final EditText inputNoLabelManual = new EditText(ProsesProduksiS4S.this);  // Nama diganti menjadi inputNoLabelManual
                    inputNoLabelManual.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    inputNoLabelManual.setHint("No. Label");
                    inputNoLabelManual.setPadding(40, 40, 40, 40);
                    inputNoLabelManual.setTextColor(Color.BLACK);
                    inputNoLabelManual.setBackgroundColor(getResources().getColor(R.color.white));

                    // Set input type untuk huruf kapital
                    inputNoLabelManual.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

                    // Menambahkan filter untuk membatasi panjang input menjadi 8 karakter
                    inputNoLabelManual.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});

                    // Menambahkan EditText ke dalam TextInputLayout
                    textInputLayout.addView(inputNoLabelManual);

                    // Menambahkan TextInputLayout ke dalam LinearLayout
                    layout.addView(textInputLayout);

                    // Membuat MaterialAlertDialogBuilder untuk tampilan modern
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ProsesProduksiS4S.this);
                    builder.setTitle("Input Label")
                            .setView(layout)
                            .setBackground(ContextCompat.getDrawable(ProsesProduksiS4S.this, R.drawable.tooltip_background))
                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String result = inputNoLabelManual.getText().toString();  // Menggunakan inputNoLabelManual
                                    if (!result.isEmpty()) {
                                        // Panggil metode yang sama dengan hasil scan
                                        addScanResultToTable(result);
                                    } else {
                                        Toast.makeText(ProsesProduksiS4S.this, "Kode tidak boleh kosong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    // Buat dialog dan tampilkan
                    final androidx.appcompat.app.AlertDialog dialog = builder.create();
                    dialog.show();

                    // Menghitung 40% dari lebar layar
                    WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                    Display display = windowManager.getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = (int) (size.x * 0.4); // 40% dari lebar layar

                    // Mengatur ukuran dialog
                    if (dialog.getWindow() != null) {
                        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
                        layoutParams.width = width; // Atur lebar 40% dari layar
                        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // Ukuran tinggi mengikuti konten
                        dialog.getWindow().setAttributes(layoutParams);
                    }
                }
                else{
                    Toast.makeText(ProsesProduksiS4S.this, "Kode tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });



        searchMainTable.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (dataList == null || dataList.isEmpty()) {
                    Log.e("SearchError", "Data list is null or empty.");
                    return; // Hindari iterasi jika dataList null
                }

                String query = s.toString().toLowerCase();

                final int PAGE_SIZE = 50;
                List<ProductionData> filteredList = new ArrayList<>();
                int count = 0;

                for (ProductionData data : dataList) {
                    if (count >= PAGE_SIZE) break;

                    if ((data.getNoProduksi() != null && data.getNoProduksi().toLowerCase().contains(query)) ||
                            (data.getOperator() != null && data.getOperator().toLowerCase().contains(query)) ||
                            (data.getShift() != null && data.getShift().toLowerCase().contains(query)) ||
                            (data.getTanggal() != null && data.getTanggal().toLowerCase().contains(query)) ||
                            (data.getMesin() != null && data.getMesin().toLowerCase().contains(query))) {
                        filteredList.add(data);
                        count++;
                    }
                }

                populateTable(filteredList);

            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveScannedResultsToDatabase();
            }
        });

        btnInputKodeLabel.setOnClickListener(v -> {
            String result = kodeLabel.getText().toString().trim();

            if (!result.isEmpty()) {
                // Panggil metode yang sama dengan hasil scan
                addScanResultToTable(result);
            } else {
                Toast.makeText(this, "Kode tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });


        // Memuat data dari API dan menampilkan ke tabel
        executorService.execute(() -> {
            dataList = ProsesProduksiApi.getProductionData(mainTable);

            runOnUiThread(() -> {
                populateTable(dataList);
                // Sembunyikan loading indicator
                loadingIndicator.setVisibility(View.GONE);
            });

        });

        // Mendapatkan instance ProcessCameraProvider
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                setupCameraControlButton();
            } catch (Exception e) {
                Log.e("ProsesProduksiS4S", "Error initializing camera provider: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this));

        // Inisialisasi requestPermissionLauncher
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission diberikan, aktifkan kamera
                        activateCamera();
                        setupCameraControlButton();
                    } else {
                        // Permission ditolak, beri tahu user
                        Toast.makeText(this, "Permission kamera dibutuhkan untuk mengaktifkan kamera", Toast.LENGTH_SHORT).show();
                    }
                });

        btnHistorySave.setOnClickListener(v -> showHistoryDialog(noProduksi));

    }

    //METHOD S4S



//------------------------------------------------------------------------------------------------------------------------------------------------------//
//----------------------------------METHOD EDIT HEADER--------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------------------------------------------------------------//

    private void showEditProductionDialog() {
        // Inflate dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_edit_proses_produksi, null);

        // Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Make dialog background transparent for custom styling
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Get references to dialog views
        TextView tvNoProduksi = dialogView.findViewById(R.id.tvNoProduksi);
        Spinner spinShift = dialogView.findViewById(R.id.spinShift);
        EditText editTanggal = dialogView.findViewById(R.id.editTanggal);
        Spinner spinMesin = dialogView.findViewById(R.id.spinMesin);
        Spinner spinOperator = dialogView.findViewById(R.id.spinOperator);
        EditText editJlhAnggota = dialogView.findViewById(R.id.editJlhAnggota);
        EditText editJamKerja = dialogView.findViewById(R.id.editJamKerja);
        EditText editHourMeter = dialogView.findViewById(R.id.editHourMeter);
        ImageButton btnClose = dialogView.findViewById(R.id.btnCloseDialogInput);
        Button btnSimpan = dialogView.findViewById(R.id.btnSimpanProduction);

        // Setup spinner for shift
        setupShiftSpinner(spinShift);

        loadMesinSpinner(spinMesin, selectedProductionData.getIdMesin());

        loadOperatorSpinner(spinOperator, selectedProductionData.getIdOperator(), selectedProductionData.getOperator());

        // Populate fields with selected data
        populateDialogFields(tvNoProduksi, spinShift, editTanggal, spinMesin, spinOperator, editJlhAnggota, editJamKerja, editHourMeter, selectedProductionData);


        editTanggal.setInputType(InputType.TYPE_NULL);
        editTanggal.setFocusable(false);
        editTanggal.setOnClickListener(v -> {
            DateTimeUtils.showDatePicker(ProsesProduksiS4S.this, editTanggal);
        });

        // Close button click listener
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Save button click listener
        btnSimpan.setOnClickListener(v -> {
            loadingDialogHelper.show(this);
            if (validateInputs(tvNoProduksi, editTanggal, spinMesin, spinOperator)) {
                // Ambil data dari UI
                String noProduksi = tvNoProduksi.getText().toString().trim();
                String shift = spinShift.getSelectedItem().toString();
                String tanggal = editTanggal.getText().toString().trim(); // Pastikan ini dalam format yyyy-MM-dd
                int idMesin = ((MesinProsesProduksiData) spinMesin.getSelectedItem()).getIdMesin();
                int idOperator = ((MstOperatorData) spinOperator.getSelectedItem()).getIdOperator();
                String jamKerja = editJamKerja.getText().toString().trim();
                int jumlahAnggota = Integer.parseInt(editJlhAnggota.getText().toString().trim());
                double hourMeter = Double.parseDouble(editHourMeter.getText().toString().trim());

                // Buat objek ProductionData
                ProductionData updatedData = new ProductionData(
                        noProduksi, shift, tanggal, "", "", jamKerja,
                        jumlahAnggota, hourMeter, idMesin, idOperator
                );

                executorService.execute(() -> {
                    boolean success = ProsesProduksiApi.updateProductionData(mainTable, updatedData);
                    dataList = ProsesProduksiApi.getProductionData(mainTable);


                    runOnUiThread(() -> {
                        if (success) {
                            dialog.dismiss();
                            Toast.makeText(ProsesProduksiS4S.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                            populateTable(dataList);
                            loadingDialogHelper.hide();
                        } else {
                            Toast.makeText(ProsesProduksiS4S.this, "Gagal mengupdate data", Toast.LENGTH_SHORT).show();
                            loadingDialogHelper.hide();
                        }
                    });
                });
            }
        });



        dialog.show();
    }



    private void populateDialogFields(TextView tvNoProduksi, Spinner spinShift, EditText editTanggal,
                                      Spinner spinMesin, Spinner spinOperator, EditText editJlhAnggota, EditText editJamKerja, EditText editHourMeter, ProductionData data) {
        tvNoProduksi.setText(data.getNoProduksi());
        editTanggal.setText(DateTimeUtils.formatDate(data.getTanggal()));
        editJlhAnggota.setText(String.valueOf(data.getJumlahAnggota()));
        editJamKerja.setText(data.getJamKerja());
        editHourMeter.setText(String.valueOf(data.getHourMeter()));

        // Set spinner selection based on shift value
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinShift.getAdapter();
        if (adapter != null && data.getShift() != null) {
            int position = adapter.getPosition(data.getShift());
            if (position >= 0) {
                spinShift.setSelection(position);
            }
        }
    }

    private boolean validateInputs(TextView tvNoProduksi, EditText editTanggal,
                                   Spinner spinMesin, Spinner spinOperator) {

        if (editTanggal.getText().toString().trim().isEmpty()) {
            editTanggal.setError("Tanggal harus diisi");
            editTanggal.requestFocus();
            return false;
        }


        return true;
    }


    private void setupShiftSpinner(Spinner spinShift) {
        // Setup spinner adapter for shift options
        String[] shiftOptions = {"1", "2", "3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, shiftOptions);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinShift.setAdapter(adapter);
    }


    private void loadMesinSpinner(Spinner spinner, int selectedIdMesin) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            List<MesinProsesProduksiData> mesinList = ProsesProduksiApi.getAllMesinData(1);

            // Kembali ke UI thread untuk update Spinner
            runOnUiThread(() -> {
                ArrayAdapter<MesinProsesProduksiData> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinner.setAdapter(adapter);

                // Set item terpilih jika ada
                for (int i = 0; i < mesinList.size(); i++) {
                    if (mesinList.get(i).getIdMesin() == selectedIdMesin) {
                        spinner.setSelection(i);
                        break;
                    }
                }
            });
        });
    }


    private void loadOperatorSpinner(Spinner spinner, int selectedIdOperator, String selectedOperatorName) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            List<MstOperatorData> operatorList = ProsesProduksiApi.getAllOperatorData(1);

            // Cek apakah selectedIdOperator ada dalam list
            boolean found = false;
            for (MstOperatorData operator : operatorList) {
                if (operator.getIdOperator() == selectedIdOperator) {
                    found = true;
                    break;
                }
            }

            // Jika tidak ditemukan, tambahkan operator dummy dengan id dan nama yang sama
            if (!found && selectedIdOperator != 0) {
                MstOperatorData newOperator = new MstOperatorData(selectedIdOperator, selectedOperatorName);
                operatorList.add(0, newOperator); // bisa ditaruh di awal agar langsung muncul
            }

            runOnUiThread(() -> {
                ArrayAdapter<MstOperatorData> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, operatorList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinner.setAdapter(adapter);

                // Set item terpilih
                for (int i = 0; i < operatorList.size(); i++) {
                    if (operatorList.get(i).getIdOperator() == selectedIdOperator) {
                        spinner.setSelection(i);
                        break;
                    }
                }
            });
        });
    }


//------------------------------------------------------------------------------------------------------------------------------------------------------//
//----------------------------------METHOD UNTUK PENANGANAN KAMERA DENGAN SCAN QR-----------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------------------------------------------------------------//
    /**
     * Menyiapkan tombol kontrol kamera
     */
    private void setupCameraControlButton() {
        updateButtonText(); // Set teks awal tombol
        btnCameraControl.setOnClickListener(v -> {
            if (isCameraActive) {
                // 🔹 Animasi smooth hide (fade out + slide up)
                switchSPK.animate()
                        .alpha(0f)                // memudar
                        .translationY(-30f)       // geser ke atas 30px
                        .setDuration(300)         // durasi animasi (ms)
                        .setInterpolator(new android.view.animation.AccelerateInterpolator())
                        .withEndAction(() -> {
                            switchSPK.setChecked(false);   // reset state switch
                            switchSPK.setVisibility(View.GONE);
                            deactivateCamera();            // panggil setelah animasi selesai
                        })
                        .start();


            } else {
                String noProduksi = noProduksiView.getText().toString();

                if (!noProduksi.isEmpty()) {
                    executorService.execute(() -> {
                        if (tglProduksi == null) {
                            runOnUiThread(() ->
                                    Toast.makeText(this, "Tanggal Produksi tidak valid!", Toast.LENGTH_SHORT).show()
                            );
                            return;
                        }

                        // Periksa periode transaksi
                        boolean isClosed = isTransactionPeriodClosed(tglProduksi);

                        runOnUiThread(() -> {
                            if (!isClosed) {
                                Toast.makeText(this, "Periode transaksi sudah ditutup!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Jika valid, minta permission kamera
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                            loadSPKSpinner(null, null);

                            // 🔹 Tampilkan switch dengan animasi smooth dari atas ke bawah
                            switchSPK.setAlpha(0f);
                            switchSPK.setTranslationY(-40f); // mulai sedikit di atas posisi normal
                            switchSPK.setVisibility(View.VISIBLE);

                            switchSPK.animate()
                                    .alpha(1f)
                                    .translationY(0f) // turun ke posisi normal
                                    .setDuration(350)
                                    .setInterpolator(new android.view.animation.DecelerateInterpolator())
                                    .start();

                        });
                    });
                } else {
                    Toast.makeText(this, "Pilih NoProduksi Terlebih Dahulu!", Toast.LENGTH_SHORT).show();
                }
            }

            updateButtonText(); // Ubah teks tombol sesuai status
        });

    }

    /**
     * Mengaktifkan kamera
     */
    private void activateCamera() {
        if (cameraProvider != null && !isCameraActive) {
            // Periksa permission kamera
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                // Permission sudah diberikan, setup kamera

                CameraUtils.setupCamera(
                        this,
                        cameraPreview,
                        new CameraXAnalyzer(result -> {
                            runOnUiThread(() -> {
                                if (!isShowingResult) {
                                    // QR code terdeteksi
                                    qrResultText.setText(result);
                                    qrResultText.setVisibility(View.VISIBLE);
                                    isShowingResult = true;

                                    // Kembalikan warna border ke default setelah 3 detik
                                    new android.os.Handler().postDelayed(() -> {
                                        borderTop.setBackgroundResource(R.drawable.border_default);
                                        borderBottom.setBackgroundResource(R.drawable.border_default);
                                        borderLeft.setBackgroundResource(R.drawable.border_default);
                                        borderRight.setBackgroundResource(R.drawable.border_default);
                                    }, 3000);

                                    // Sembunyikan teks hasil setelah 3 detik
                                    new android.os.Handler().postDelayed(() -> {
                                        qrResultText.setVisibility(View.GONE);
                                        isShowingResult = false;
                                    }, 3000);
                                }

                                // Tambahkan hasil ke tabel
                                addScanResultToTable(result);
                            });
                        }),
                        androidx.camera.core.CameraSelector.LENS_FACING_BACK,
                        ProcessCameraProvider.getInstance(this)
                );

                // Atur UI saat kamera aktif
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                ScannerAnimationUtils.startScanningAnimation(scannerOverlay, displayMetrics);
                scanLayout.setVisibility(View.VISIBLE);
                cameraPreview.setVisibility(View.VISIBLE);
//                inputKodeManual.setVisibility(View.VISIBLE);
                tableLayout.setVisibility(View.GONE);
                headerTableProduksi.setVisibility(View.GONE);
//                searchMainTable.setVisibility(View.INVISIBLE);
//                textLayoutSearchMainTable.setVisibility(View.INVISIBLE);
                jumlahLabelHeader.setVisibility(View.VISIBLE);
                jumlahLabel.setVisibility(View.VISIBLE);
                textScanQR.setVisibility(View.VISIBLE);
                btnSimpan.setEnabled(true);
                btnInputManual.setEnabled(true);

                isCameraActive = true;
//                Toast.makeText(this, "Kamera diaktifkan", Toast.LENGTH_SHORT).show();
            } else {
                // Permission belum diberikan, minta permission
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        } else {
            Toast.makeText(this, "Kamera sudah aktif", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Menonaktifkan kamera
     */
    private void deactivateCamera() {
        if (cameraProvider != null && isCameraActive) {
            cameraProvider.unbindAll(); // Hentikan semua instance kamera

            ScannerAnimationUtils.stopScanningAnimation();
            scanLayout.setVisibility(View.GONE);
            cameraPreview.setVisibility(View.GONE);
//            inputKodeManual.setVisibility(View.GONE);
            jumlahLabelHeader.setVisibility(View.GONE);
            jumlahLabel.setVisibility(View.GONE);
            tableLayout.setVisibility(View.VISIBLE);
            headerTableProduksi.setVisibility(View.VISIBLE);
            textScanQR.setVisibility(View.GONE);
//            searchMainTable.setVisibility(View.VISIBLE);
//            textLayoutSearchMainTable.setVisibility(View.VISIBLE);
            btnSimpan.setEnabled(false);
            btnInputManual.setEnabled(false);



            isCameraActive = false;
//            Toast.makeText(this, "Kamera dinonaktifkan", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Kamera sudah tidak aktif", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateButtonText() {
        if (isCameraActive) {
            btnCameraControl.setText("Kembali");
        } else {
            btnCameraControl.setText("Input");
        }
    }

//------------------------------------------------------------------------------------------------------------------------------------------------------//
//-------------------------------------------METHOD UNTUK DISPLAY KE TABEL------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------------------------------------------------------------//
    /**
     * Menampilkan data produksi ke dalam tabel utama
     */
    private void populateTable(List<ProductionData> dataList) {

        tableLayout.removeAllViews();

        if (dataList == null || dataList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Data tidak ditemukan");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            tableLayout.addView(noDataView);
            return;
        }

        int rowIndex = 0; // Untuk melacak indeks baris

        for (ProductionData data : dataList) {
            TableRow row = new TableRow(this);

            // Simpan indeks baris di tag
            row.setTag(rowIndex);

            // Tambahkan TextView untuk setiap kolom
            TextView col1 = createTextView(data.getNoProduksi(), 1.0f);
            TextView col2 = createTextView(data.getShift(), 0.5f);
            TextView col3 = createTextView(data.getTanggal(), 1.0f);
            TextView col4 = createTextView(data.getMesin(), 1.0f);
            TextView col5 = createTextView(data.getOperator(), 1.0f);

            setDateToView(data.getTanggal(), col3);

            row.addView(col1);
            row.addView(createDivider());

            row.addView(col2);
            row.addView(createDivider());

            row.addView(col3);
            row.addView(createDivider());

            row.addView(col4);
            row.addView(createDivider());

            row.addView(col5);

            // Tetapkan warna latar belakang berdasarkan indeks baris
            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream)); // Warna untuk baris genap
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white)); // Warna untuk baris ganjil
            }

            row.setOnClickListener(v -> {
                // Reset warna baris sebelumnya (jika ada)
                if (selectedRowHeader != null) {
                    int previousRowIndex = (int) selectedRowHeader.getTag();
                    if (previousRowIndex % 2 == 0) {
                        selectedRowHeader.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
                    } else {
                        selectedRowHeader.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                    }
                    resetTextColor(selectedRowHeader); // Kembalikan warna teks ke hitam
                }

                // Tandai baris yang baru dipilih
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.primary)); // Warna penandaan
                setTextColor(row, R.color.white); // Ubah warna teks menjadi putih
                selectedRowHeader = row;

                // Simpan data yang dipilih
                selectedProductionData = data;

                // Tangani aksi tambahan
                onRowClick(data);
            });


            tableLayout.addView(row);
            rowIndex++; // Tingkatkan indeks
        }
    }



    private void populateNoSTTable(List<String> noSTList) {
        noSTTableLayout.removeAllViews();
        int rowIndex = 0;

        if (noSTList == null || noSTList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Tidak ada Data");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            noSTTableLayout.addView(noDataView);
            return;
        }

        // Isi tabel
        for (String noST : noSTList) {
            TableRow row = new TableRow(this);
            row.setTag(rowIndex);

            TextView textView = createTextView(noST, 1.0f);
            row.addView(textView);

            // Buat salinan final untuk digunakan dalam lambda
            final int currentRowIndex = rowIndex;
            final TableRow currentRow = row;
            final String currentNoST = noST;

            row.setOnClickListener(view -> {
                ViewUtils.handleRowSelection(this, currentRow, currentRowIndex, selectedRow);
                selectedRow = currentRow;

                TooltipUtils.fetchDataAndShowTooltip(
                        this,
                        executorService,
                        view,
                        currentNoST,
                        "ST_h",
                        "ST_d",
                        "NoST",
                        () -> {
                            // Callback saat popup ditutup
                            if (selectedRow != null) {
                                int currentIndex = (int) selectedRow.getTag();
                                ViewUtils.resetRowSelection(this, selectedRow, currentIndex);
                                selectedRow = null;
                            }
                        }
                );
            });

            row.setOnLongClickListener(v -> {
                if (!userPermissions.contains("proses_s4s:delete")) {
                    Toast.makeText(this, "Anda tidak memiliki izin untuk menghapus.", Toast.LENGTH_SHORT).show();
                    return true; // event dianggap sudah di-handle
                }

                new AlertDialog.Builder(this)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus data NoST " + currentNoST + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            Toast.makeText(this,
                                    "Menghapus data " + noProduksi + " " + currentNoST,
                                    Toast.LENGTH_SHORT).show();

                            executorService.execute(() -> {
                                ProsesProduksiApi.deleteDataByNoLabel(noProduksi, currentNoST);
                                runOnUiThread(this::refreshSTTable);
                            });
                        })
                        .setNegativeButton("Batal", null)
                        .show();
                return true; // True = long press sudah di-handle
            });


            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }

            noSTTableLayout.addView(row);
            rowIndex++;
        }
    }


    private void populateNoS4STable(List<String> noS4SList) {
        noS4STableLayout.removeAllViews();

        int rowIndex = 0;

        if (noS4SList == null || noS4SList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Tidak ada Data");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            noS4STableLayout.addView(noDataView);
            return;
        }

        // Isi tabel
        for (String noS4S : noS4SList) {
            TableRow row = new TableRow(this);
            row.setTag(rowIndex);

            TextView textView = createTextView(noS4S, 1.0f);
            row.addView(textView);

            // Buat salinan final untuk digunakan dalam lambda
            final int currentRowIndex = rowIndex;
            final TableRow currentRow = row;
            final String currentNoS4S = noS4S;

            row.setOnClickListener(view -> {
                ViewUtils.handleRowSelection(this, currentRow, currentRowIndex, selectedRow);
                selectedRow = currentRow;

                // ✅ Ini callback ketika DELETE berhasil
                TooltipUtils.fetchDataAndShowTooltip(
                        this,
                        executorService,
                        view,
                        currentNoS4S,
                        "S4S_h",
                        "S4S_d",
                        "NoS4S",
                        () -> {
                            // Ini callback saat popup ditutup
                            if (selectedRow != null) {
                                int currentIndex = (int) selectedRow.getTag();
                                ViewUtils.resetRowSelection(this, selectedRow, currentIndex);
                                selectedRow = null;
                            }
                        }
                );
            });

            row.setOnLongClickListener(v -> {
                if (!userPermissions.contains("proses_s4s:delete")) {
                    Toast.makeText(this, "Anda tidak memiliki izin untuk menghapus.", Toast.LENGTH_SHORT).show();
                    return true; // event dianggap sudah di-handle
                }

                new AlertDialog.Builder(this)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus data NoS4S " + currentNoS4S + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            Toast.makeText(this,
                                    "Menghapus data " + noProduksi + " " + currentNoS4S,
                                    Toast.LENGTH_SHORT).show();

                            executorService.execute(() -> {
                                ProsesProduksiApi.deleteDataByNoLabel(noProduksi, currentNoS4S);
                                runOnUiThread(this::refreshS4STable);
                            });
                        })
                        .setNegativeButton("Batal", null)
                        .show();
                return true; // True = long press sudah di-handle
            });


            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }

            noS4STableLayout.addView(row);
            rowIndex++;
        }
    }


    private void populateNoFJTable(List<String> noFJList) {
        noFJTableLayout.removeAllViews();
        int rowIndex = 0;

        if (noFJList == null || noFJList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Tidak ada Data");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            noFJTableLayout.addView(noDataView);
            return;
        }

        // Isi tabel
        for (String noFJ : noFJList) {
            TableRow row = new TableRow(this);
            row.setTag(rowIndex);

            TextView textView = createTextView(noFJ, 1.0f);
            row.addView(textView);

            final int currentRowIndex = rowIndex;
            final TableRow currentRow = row;
            final String currentNoFJ = noFJ;

            row.setOnClickListener(view -> {
                ViewUtils.handleRowSelection(this, currentRow, currentRowIndex, selectedRow);
                selectedRow = currentRow;

                TooltipUtils.fetchDataAndShowTooltip(
                        this,
                        executorService,
                        view,
                        currentNoFJ,
                        "FJ_h",
                        "FJ_d",
                        "NoFJ",
                        () -> {
                            // Callback saat popup ditutup
                            if (selectedRow != null) {
                                int currentIndex = (int) selectedRow.getTag();
                                ViewUtils.resetRowSelection(this, selectedRow, currentIndex);
                                selectedRow = null;
                            }
                        }
                );
            });

            row.setOnLongClickListener(v -> {
                if (!userPermissions.contains("proses_s4s:delete")) {
                    Toast.makeText(this, "Anda tidak memiliki izin untuk menghapus.", Toast.LENGTH_SHORT).show();
                    return true; // event dianggap sudah di-handle
                }

                new AlertDialog.Builder(this)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus data NoS4S " + currentNoFJ + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            Toast.makeText(this,
                                    "Menghapus data " + noProduksi + " " + currentNoFJ,
                                    Toast.LENGTH_SHORT).show();

                            executorService.execute(() -> {
                                ProsesProduksiApi.deleteDataByNoLabel(noProduksi, currentNoFJ);
                                runOnUiThread(this::refreshFJTable);
                            });
                        })
                        .setNegativeButton("Batal", null)
                        .show();
                return true; // True = long press sudah di-handle
            });



            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }

            noFJTableLayout.addView(row);
            rowIndex++;
        }
    }


    private void populateNoMouldingTable(List<String> noMouldingList) {
        noMouldingTableLayout.removeAllViews();
        int rowIndex = 0;

        if (noMouldingList == null || noMouldingList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Tidak ada Data");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            noMouldingTableLayout.addView(noDataView);
            return;
        }

        // Isi tabel
        for (String noMoulding : noMouldingList) {
            TableRow row = new TableRow(this);
            row.setTag(rowIndex);

            TextView textView = createTextView(noMoulding, 1.0f);
            row.addView(textView);

            final int currentRowIndex = rowIndex;
            final TableRow currentRow = row;
            final String currentNoMoulding = noMoulding;

            row.setOnClickListener(view -> {
                ViewUtils.handleRowSelection(this, currentRow, currentRowIndex, selectedRow);
                selectedRow = currentRow;

                TooltipUtils.fetchDataAndShowTooltip(
                        this,
                        executorService,
                        view,
                        currentNoMoulding,
                        "Moulding_h",
                        "Moulding_d",
                        "NoMoulding",
                        () -> {
                            // Callback saat popup ditutup
                            if (selectedRow != null) {
                                int currentIndex = (int) selectedRow.getTag();
                                ViewUtils.resetRowSelection(this, selectedRow, currentIndex);
                                selectedRow = null;
                            }
                        }
                );
            });

            row.setOnLongClickListener(v -> {
                if (!userPermissions.contains("proses_s4s:delete")) {
                    Toast.makeText(this, "Anda tidak memiliki izin untuk menghapus.", Toast.LENGTH_SHORT).show();
                    return true; // event dianggap sudah di-handle
                }

                new AlertDialog.Builder(this)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus data NoMoulding " + currentNoMoulding + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            Toast.makeText(this,
                                    "Menghapus data " + noProduksi + " " + currentNoMoulding,
                                    Toast.LENGTH_SHORT).show();

                            executorService.execute(() -> {
                                ProsesProduksiApi.deleteDataByNoLabel(noProduksi, currentNoMoulding);
                                runOnUiThread(this::refreshMouldingTable);
                            });
                        })
                        .setNegativeButton("Batal", null)
                        .show();
                return true; // True = long press sudah di-handle
            });

            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }

            noMouldingTableLayout.addView(row);
            rowIndex++;
        }
    }



    private void populateNoCCTable(List<String> noCCList) {
        noCCTableLayout.removeAllViews();
        int rowIndex = 0;

        if (noCCList == null || noCCList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Tidak ada Data");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            noCCTableLayout.addView(noDataView);
            return;
        }

        // Isi tabel
        for (String noCC : noCCList) {
            TableRow row = new TableRow(this);
            row.setTag(rowIndex);

            TextView textView = createTextView(noCC, 1.0f);
            row.addView(textView);

            final int currentRowIndex = rowIndex;
            final TableRow currentRow = row;
            final String currentNoCC = noCC;

            row.setOnClickListener(view -> {
                ViewUtils.handleRowSelection(this, currentRow, currentRowIndex, selectedRow);
                selectedRow = currentRow;

                TooltipUtils.fetchDataAndShowTooltip(
                        this,
                        executorService,
                        view,
                        currentNoCC,
                        "CCAkhir_h",
                        "CCAkhir_d",
                        "NoCCAkhir",
                        () -> {
                            // Callback saat popup ditutup
                            if (selectedRow != null) {
                                int currentIndex = (int) selectedRow.getTag();
                                ViewUtils.resetRowSelection(this, selectedRow, currentIndex);
                                selectedRow = null;
                            }
                        }
                );
            });

            row.setOnLongClickListener(v -> {
                if (!userPermissions.contains("proses_s4s:delete")) {
                    Toast.makeText(this, "Anda tidak memiliki izin untuk menghapus.", Toast.LENGTH_SHORT).show();
                    return true; // event dianggap sudah di-handle
                }

                new AlertDialog.Builder(this)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus data NoCCA " + currentNoCC + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            Toast.makeText(this,
                                    "Menghapus data " + noProduksi + " " + currentNoCC,
                                    Toast.LENGTH_SHORT).show();

                            executorService.execute(() -> {
                                ProsesProduksiApi.deleteDataByNoLabel(noProduksi, currentNoCC);
                                runOnUiThread(this::refreshCCTable);
                            });
                        })
                        .setNegativeButton("Batal", null)
                        .show();
                return true; // True = long press sudah di-handle
            });

            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }

            noCCTableLayout.addView(row);
            rowIndex++;
        }
    }


    private void populateNoReprosesTable(List<String> noReprosesList) {
        noReprosesTableLayout.removeAllViews();

        if (noReprosesList == null || noReprosesList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Tidak ada Data");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            noReprosesTableLayout.addView(noDataView);
            return;
        }

        // Data tabel
        for (String noReproses : noReprosesList) {
            TableRow row = new TableRow(this);
            row.addView(createTextView(noReproses, 1.0f));
            noReprosesTableLayout.addView(row);
        }
    }


//------------------------------------------------------------------------------------------------------------------------------------------------------//
//-------------------------------------------METHOD ADD DATA KE TABLE LIST DENGAN PRE-CONDITION---------------------------------------------------------//
//------------------------------------------------------------------------------------------------------------------------------------------------------//


    private void addRowToTable(TableLayout tableLayout, String result, List<String> list, TextView sumLabel) {
        // Buat TableRow untuk baris baru
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));

        // Buat TextView kosong (placeholder) di sebelah kiri
        TextView emptyView = new TextView(this);
        emptyView.setText(""); // Kosongkan teks
        emptyView.setPadding(0, 0, 0, 0); // Tidak ada padding
        emptyView.setGravity(Gravity.CENTER); // Pusatkan konten (jika ada)
        emptyView.setLayoutParams(new TableRow.LayoutParams(50, 50));

        // Buat TextView untuk menampilkan hasil
        TextView textView = new TextView(this);
        textView.setText(result);
        textView.setPadding(8, 15, 8, 15); // Padding untuk jarak
        textView.setGravity(Gravity.CENTER); // Pusatkan teks di tengah
        textView.setLayoutParams(new TableRow.LayoutParams(
                0, // Lebar proporsional (diatur oleh weight)
                TableRow.LayoutParams.WRAP_CONTENT, // Tinggi mengikuti konten
                1f // Berat untuk membagi lebar secara proporsional
        ));

        ImageButton deleteButton = new ImageButton(this);
        deleteButton.setImageResource(R.drawable.ic_close);
        deleteButton.setBackground(null);
        deleteButton.setPadding(0, 10, 0, 0);
        deleteButton.setLayoutParams(new TableRow.LayoutParams(50, TableRow.LayoutParams.WRAP_CONTENT));


        deleteButton.setOnClickListener(v -> {
            try {
                // Hapus baris dari tabel
                tableLayout.removeView(row);

                // Hapus data dari daftar
                list.remove(result);
                scannedResults.remove(result);

                sumLabel.setText(Integer.toString(list.size()));

            } catch (Exception e) {
                Log.e("DeleteButton", "Error saat menghapus baris: " + e.getMessage());
            }
        });

        // Tambahkan placeholder, TextView, dan tombol delete ke TableRow
        row.addView(emptyView); // Tambahkan ruang kosong di kolom pertama
        row.addView(textView); // Tambahkan teks di kolom kedua
        row.addView(deleteButton); // Tambahkan tombol delete di kolom ketiga

        // Tambahkan TableRow ke TableLayout
        tableLayout.addView(row, 0);
    }

    private void addScanResultToTable(String result) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            synchronized (scannedResults) {
                // Abaikan jika hasil baru saja ditambahkan
                if (recentlyAddedResults.containsKey(result) &&
                        System.currentTimeMillis() - recentlyAddedResults.get(result) < RECENTLY_ADDED_INTERVAL) {
                    return;
                }

                // Filter prefix: hanya terima awalan R (S4S) dan V (CC Akhir)
                if (!result.startsWith("E") && !result.startsWith("R") && !result.startsWith("T") && !result.startsWith("S") && !result.startsWith("V")) {
                    runOnUiThread(() -> displayErrorState(
                            "Label " + result + " Tidak Sesuai", R.raw.denied_data));
                    return;
                }

                if (!scannedResults.contains(result)) {
                    // Tambahkan hasil baru
                    scannedResults.add(result);
                    recentlyAddedResults.put(result, System.currentTimeMillis());

                    // Jadwalkan penghapusan elemen dari recentlyAddedResults
                    handler.postDelayed(() -> {
                        synchronized (recentlyAddedResults) {
                            recentlyAddedResults.remove(result);
                        }
                    }, RECENTLY_ADDED_INTERVAL);

                    // Ambil konfigurasi tabel berdasarkan prefix hasil
                    Map<String, TableConfig> tableConfigMap = TableConfigUtils.getTableConfigMap(
                            noS4SList, noSTList, noMouldingList, noFJList, noCCList, noReprosesList, noLaminatingList, noSandingList, noPackingList
                    );
                    TableConfig config = tableConfigMap.get(result.substring(0, 1));

                    if (config == null) {
                        displayErrorState("Label " + result + " tidak sesuai", R.raw.denied_data);
                        scannedResults.remove(result);
                        return;
                    }

                    if (ProsesProduksiApi.isDataExists(result, config.tableNameH, config.tableNameD, config.columnName)) {
                        if (ProsesProduksiApi.isDateUsageNull(result, config.tableNameH, config.columnName)) {
                            handleValidData(result, config);
                        } else {
                            handleDuplicateOrInvalidUsage(result, config);
                        }
                    } else {
                        displayErrorState("Label " + result + " Tidak ditemukan di Database", R.raw.denied_data);
                        scannedResults.remove(result);
                    }
                } else {
                    displayDuplicateScanError(result);
                }
            }
        });
        executor.shutdown();
    }

    private void handleValidData(String result, TableConfig config) {
        if (ProsesProduksiApi.isDateValid(noProduksi, mainTable, result, config.tableNameH, config.columnName)) {
            runOnUiThread(() -> {
                TableLayout targetTableLayout = findViewById(config.tableLayoutId);
                TextView targetSumLabel = findViewById(config.sumLabelId);

                if (config.list == null || config.list.isEmpty()) {
                    targetTableLayout.removeAllViews();
                }

                config.list.add(result);
                targetSumLabel.setText(String.valueOf(config.list.size()));
                addRowToTable(targetTableLayout, result, config.list, targetSumLabel);

                // Ubah warna border menjadi hijau
                setBorderState(R.drawable.border_granted);
                kodeLabel.setText("");
                playSound(R.raw.granted_data);
            });
        } else {
            displayErrorState("Tgl Label lebih besar dari Tgl Produksi " + result, R.raw.denied_data);
            scannedResults.remove(result);
        }
    }

    private void handleDuplicateOrInvalidUsage(String result, TableConfig config) {
        setBorderState(R.drawable.border_denied);

        String namaTabel = config.resultChecker.apply(result);

        if (namaTabel == null) {
            displayErrorState("Label " + result + " tidak ada di Proses manapun", R.raw.denied_data);
        } else {
            displayErrorState("Label " + result + " sudah ada pada " + namaTabel, R.raw.denied_data);
        }
        scannedResults.remove(result);
    }

    private void displayDuplicateScanError(String result) {
        setBorderState(R.drawable.border_denied);
        Log.d("DuplicateScan", "Hasil scan sudah ada: " + result);
        showToastAndPlaySound("Label " + result + " Sudah di Masukkan ke dalam Tabel", R.raw.denied_data);
    }

    private void displayErrorState(String message, int soundResId) {
        runOnUiThread(() -> {
            setBorderState(R.drawable.border_denied);
            showToastAndPlaySound(message, soundResId);
        });
    }

    private void setBorderState(int drawableResId) {
        borderTop.setBackgroundResource(drawableResId);
        borderBottom.setBackgroundResource(drawableResId);
        borderLeft.setBackgroundResource(drawableResId);
        borderRight.setBackgroundResource(drawableResId);
    }

    private void showToastAndPlaySound(String message, int soundResId) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastToastTime > TOAST_INTERVAL) {
            runOnUiThread(() -> {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                playSound(soundResId);
            });
            lastToastTime = currentTime;
        }
    }


//------------------------------------------------------------------------------------------------------------------------------------------------------//
//-------------------------------------------METHOD MENYIMPAN DATA KE DALAM DATABASE -------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------------------------------------------------------------//

    private void saveScannedResultsToDatabase() {
        customProgressDialog = new CustomProgressDialog(this);
        customProgressDialog.show(); // Tampilkan progress dialog

        String dateTimeSaved = DateTimeUtils.getCurrentDateTime();
        String savedUsername = SharedPrefUtils.getUsername(this);

        //AMBIL DATA DARI SPINNER SPK
        SpkData selectedSPK = (SpkData) spinSPK.getSelectedItem();
        String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;


        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Log.d("SaveScannedResults", "Memulai proses penyimpanan hasil scan ke database");

            int totalItems = noS4SList.size() + noSTList.size() + noMouldingList.size() +
                    noFJList.size() + noCCList.size() + noReprosesList.size();
            int savedItems = 0;

            // Proses penyimpanan untuk tabel S4S
            if (!noS4SList.isEmpty()) {
                List<String> existingNoS4S = ProsesProduksiApi.getNoS4SByNoProduksi(noProduksi, "S4SProduksiInputS4S");
                List<String> newNoS4S = new ArrayList<>(noS4SList);
                newNoS4S.removeAll(existingNoS4S);
                ProsesProduksiApi.saveNoS4S(noProduksi, tglProduksi, newNoS4S, dateTimeSaved, "S4SProduksiInputS4S");
                savedItems += newNoS4S.size();
                int progress = (savedItems * 100) / totalItems;
                runOnUiThread(() -> customProgressDialog.updateProgress(progress));
            }

            // Proses penyimpanan untuk tabel ST
            if (!noSTList.isEmpty()) {
                List<String> existingNoST = ProsesProduksiApi.getNoSTByNoProduksi(noProduksi);
                List<String> newNoST = new ArrayList<>(noSTList);
                newNoST.removeAll(existingNoST);
                ProsesProduksiApi.saveNoST(noProduksi, tglProduksi, newNoST, dateTimeSaved, noSPK);
                savedItems += newNoST.size();
                int progress = (savedItems * 100) / totalItems;
                runOnUiThread(() -> customProgressDialog.updateProgress(progress));
            }

            // Proses penyimpanan untuk tabel Moulding
            if (!noMouldingList.isEmpty()) {
                List<String> existingNoMoulding = ProsesProduksiApi.getNoMouldingByNoProduksi(noProduksi, "S4SProduksiInputMoulding");
                List<String> newNoMoulding = new ArrayList<>(noMouldingList);
                newNoMoulding.removeAll(existingNoMoulding);
                ProsesProduksiApi.saveNoMoulding(noProduksi, tglProduksi, newNoMoulding, dateTimeSaved, "S4SProduksiInputMoulding");
                savedItems += newNoMoulding.size();
                int progress = (savedItems * 100) / totalItems;
                runOnUiThread(() -> customProgressDialog.updateProgress(progress));
            }

            // Proses penyimpanan untuk tabel FJ
            if (!noFJList.isEmpty()) {
                List<String> existingNoFJ = ProsesProduksiApi.getNoFJByNoProduksi(noProduksi, "S4SProduksiInputFJ");
                List<String> newNoFJ = new ArrayList<>(noFJList);
                newNoFJ.removeAll(existingNoFJ);
                ProsesProduksiApi.saveNoFJ(noProduksi, tglProduksi, newNoFJ, dateTimeSaved, "S4SProduksiInputFJ");
                savedItems += newNoFJ.size();
                int progress = (savedItems * 100) / totalItems;
                runOnUiThread(() -> customProgressDialog.updateProgress(progress));
            }

            // Proses penyimpanan untuk tabel CC
            if (!noCCList.isEmpty()) {
                List<String> existingNoCC = ProsesProduksiApi.getNoCCByNoProduksi(noProduksi, "S4SProduksiInputCCAkhir");
                List<String> newNoCC = new ArrayList<>(noCCList);
                newNoCC.removeAll(existingNoCC);
                ProsesProduksiApi.saveNoCC(noProduksi, tglProduksi, newNoCC, dateTimeSaved, "S4SProduksiInputCCAkhir");
                savedItems += newNoCC.size();
                int progress = (savedItems * 100) / totalItems;
                runOnUiThread(() -> customProgressDialog.updateProgress(progress));
            }

            // Proses penyimpanan untuk tabel Reproses
            if (!noReprosesList.isEmpty()) {
                List<String> existingNoReproses = ProsesProduksiApi.getNoReprosesByNoProduksi(noProduksi);
                List<String> newNoReproses = new ArrayList<>(noReprosesList);
                newNoReproses.removeAll(existingNoReproses);
                ProsesProduksiApi.saveNoReproses(noProduksi, tglProduksi, newNoReproses, dateTimeSaved);
                savedItems += newNoReproses.size();
                int progress = (savedItems * 100) / totalItems;
                runOnUiThread(() -> customProgressDialog.updateProgress(progress));
            }

            ProsesProduksiApi.saveRiwayat(savedUsername, dateTimeSaved, "Mengubah Data " + noProduksi + " Pada Proses Produksi S4S (Mobile)");

            // Kosongkan semua list setelah penyimpanan berhasil
            clearAllDataLists();

            runOnUiThread(() -> {
                customProgressDialog.dismiss(); // Tutup progress dialog
                showHistoryDialog(noProduksi);
                Toast.makeText(this, "Proses penyimpanan selesai.", Toast.LENGTH_SHORT).show();

                // Panggil onRowClick dengan data yang terakhir dipilih
                if (selectedProductionData != null) {
                    onRowClick(selectedProductionData);
                } else {
                    Log.w("SaveScannedResults", "Tidak ada data yang dipilih untuk diperbarui.");
                }
            });

            loadSPKSpinner(null,null);
            Log.d("SaveScannedResults", "Proses penyimpanan hasil scan selesai");
        });
    }


//------------------------------------------------------------------------------------------------------------------------------------------------------//
//-------------------------------------------HELPER METHOD-------------------------- -------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------------------------------------------------------------//

    private void showExitConfirmationDialog() {
        // Inflate layout custom_dialog_layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_confirmation_layout, null);

        // Buat AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(false);  // Nonaktifkan untuk tidak bisa keluar tanpa konfirmasi

        // Ambil referensi tombol dari layout dan set listeners
        Button btnSave = dialogView.findViewById(R.id.btn_save);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnNo = dialogView.findViewById(R.id.btn_no);

        // Buat dan tampilkan dialog
        AlertDialog dialog = builder.create();

        // Set listener untuk tombol "Simpan"
        btnSave.setOnClickListener(v -> {
            saveScannedResultsToDatabase();  // Simpan data
            dialog.dismiss();  // Tutup dialog setelah simpan
        });

        // Set listener untuk tombol "Tidak"
        btnNo.setOnClickListener(v -> {
            scannedResults.clear();
            getOnBackPressedDispatcher().onBackPressed();  // Menyimulasikan tombol back untuk keluar dari halaman
            dialog.dismiss();  // Tutup dialog setelah aksi "Tidak"
        });

        // Set listener untuk tombol "Batal"
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();  // Tutup dialog tanpa melakukan aksi lainnya
        });

        // Tampilkan dialog
        dialog.show();
    }


    private void playSound(int soundResource) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, soundResource);
        mediaPlayer.setOnCompletionListener(MediaPlayer::release); // Bebaskan resources setelah selesai
        mediaPlayer.start();
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

    private void showAllLoadingIndicators(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        loadingIndicatorNoS4S.setVisibility(visibility);
        loadingIndicatorNoST.setVisibility(visibility);
        loadingIndicatorNoMoulding.setVisibility(visibility);
        loadingIndicatorNoFJ.setVisibility(visibility);
        loadingIndicatorNoCC.setVisibility(visibility);
        loadingIndicatorNoReproses.setVisibility(visibility);
    }

    private void setAllTableLayoutsVisibility(int visibility) {
        noS4STableLayout.setVisibility(visibility);
        noSTTableLayout.setVisibility(visibility);
        noMouldingTableLayout.setVisibility(visibility);
        noFJTableLayout.setVisibility(visibility);
        noCCTableLayout.setVisibility(visibility);
        noReprosesTableLayout.setVisibility(visibility);
    }

    private void clearAllDataLists() {
        noS4SList.clear();
        noSTList.clear();
        noMouldingList.clear();
        noFJList.clear();
        noCCList.clear();
        noReprosesList.clear();
        scannedResults.clear();
    }

    /**
     * Membuat TextView untuk digunakan dalam tabel
     */
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

    private void showHistoryDialog(String noProduksi) {
        executorService.execute(() -> {
            String filterQuery =
                            "SELECT 'S4S' AS Label, NoS4S AS KodeLabel, DateTimeSaved FROM S4SProduksiInputS4S WHERE NoProduksi = ? " +
                            "UNION ALL " +
                            "SELECT 'ST' AS Label, NoST AS KodeLabel, DateTimeSaved FROM S4SProduksiInputST WHERE NoProduksi = ? " +
                            "UNION ALL " +
                            "SELECT 'Moulding' AS Label, NoMoulding AS KodeLabel, DateTimeSaved FROM S4SProduksiInputMoulding WHERE NoProduksi = ? " +
                            "UNION ALL " +
                            "SELECT 'FJ' AS Label, NoFJ AS KodeLabel, DateTimeSaved FROM S4SProduksiInputFJ WHERE NoProduksi = ? " +
                            "UNION ALL " +
                            "SELECT 'CrossCut' AS Label, NoCCAkhir AS KodeLabel, DateTimeSaved FROM S4SProduksiInputCCAkhir WHERE NoProduksi = ? " +
                            "UNION ALL " +
                            "SELECT 'Reproses' AS Label, NoReproses AS KodeLabel, DateTimeSaved FROM S4SProduksiInputReproses WHERE NoProduksi = ?";

            // 1. Ambil data history dari API
            List<HistoryItem> historyGroups = ProsesProduksiApi.getHistoryItems(noProduksi, filterQuery, 6);

            // 2. Siapkan dan proses data (di latar belakang)
            HistorySummary summary = prepareHistorySummary(historyGroups);

            // 3. Tampilkan dialog di UI thread
            runOnUiThread(() -> showHistoryDialogUI(summary, historyGroups));
        });
    }


    private HistorySummary prepareHistorySummary(List<HistoryItem> historyGroups) {
        int totalS4S = 0;
        int totalST = 0;
        int totalMoulding = 0;
        int totalFJ = 0;
        int totalCCAkhir = 0;
        int totalReproses = 0;

        for (HistoryItem group : historyGroups) {
            totalS4S += group.getTotalS4S();
            totalST += group.getTotalST();
            totalMoulding += group.getTotalMoulding();
            totalFJ += group.getTotalFJ();
            totalCCAkhir += group.getTotalCrossCut();
            totalReproses += group.getTotalReproses();
        }

        // Kembalikan hasil summary
        return new HistorySummary(totalS4S, totalST, totalMoulding, totalFJ, totalCCAkhir, totalReproses);
    }
    public class HistorySummary {
        public int totalS4S, totalST, totalMoulding, totalFJ, totalCCAkhir, totalReproses;

        public HistorySummary(int totalS4S, int totalST, int totalMoulding, int totalFJ, int totalCCAkhir, int totalReproses) {
            this.totalS4S = totalS4S;
            this.totalST = totalST;
            this.totalMoulding = totalMoulding;
            this.totalFJ = totalFJ;
            this.totalCCAkhir = totalCCAkhir;
            this.totalReproses = totalReproses;
        }

        public int getTotalAllLabels() {
            return totalS4S + totalST + totalMoulding + totalFJ + totalCCAkhir + totalReproses;
        }
    }

    private void showHistoryDialogUI(HistorySummary summary, List<HistoryItem> historyGroups) {
        // Siapkan dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_history_save, null);
        builder.setView(dialogView);

        // Inisialisasi elemen UI
        LinearLayout historyContainer = dialogView.findViewById(R.id.historyContainer);
        ImageButton btnCloseDialog = dialogView.findViewById(R.id.btnCloseDialog);
        TextView tvSumS4S = dialogView.findViewById(R.id.tvSumS4S);
        TextView tvSumST = dialogView.findViewById(R.id.tvSumST);
        TextView tvSumMoulding = dialogView.findViewById(R.id.tvSumMoulding);
        TextView tvSumFJ = dialogView.findViewById(R.id.tvSumFJ);
        TextView tvSumCCAkhir = dialogView.findViewById(R.id.tvSumCCAkhir);
        TextView tvSumLabel = dialogView.findViewById(R.id.tvSumLabel);

        // Tambahkan data ke historyContainer
        populateHistoryItems(historyGroups, historyContainer, inflater);

        // Tampilkan jumlah masing-masing label
        tvSumS4S.setText(String.valueOf(summary.totalS4S));
        tvSumST.setText(String.valueOf(summary.totalST));
        tvSumMoulding.setText(String.valueOf(summary.totalMoulding));
        tvSumFJ.setText(String.valueOf(summary.totalFJ));
        tvSumCCAkhir.setText(String.valueOf(summary.totalCCAkhir));
        tvSumLabel.setText(String.valueOf(summary.getTotalAllLabels()));

        // Tutup dialog
        btnCloseDialog.setOnClickListener(v -> {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    private void populateHistoryItems(List<HistoryItem> historyGroups, LinearLayout historyContainer, LayoutInflater inflater) {
        for (HistoryItem group : historyGroups) {
            View itemView = inflater.inflate(R.layout.history_item, null);

            // Header data
            TextView tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            TextView tvTotalCount = itemView.findViewById(R.id.tvTotalCount);
            MaterialCardView dropdownContainer = itemView.findViewById(R.id.dropdownContainer);
            LinearLayout dropdownContent = dropdownContainer.findViewById(R.id.dropdownContent);
            ImageView dropdownIcon = itemView.findViewById(R.id.dropdownIcon);

            // Set data untuk header
            tvTimestamp.setText(DateTimeUtils.formatDateTime(group.getDateTimeSaved()));
            tvTotalCount.setText(String.valueOf(group.getTotalAllLabels()));

            // Tambahkan detail item ke dropdownContent
            for (HistoryItem labelItem : group.getItems()) {
                TextView detailView = new TextView(this);
                detailView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                detailView.setText("x" + labelItem.getLabelCount() + " " + labelItem.getLabel());
                detailView.setTextSize(12);
                detailView.setTextColor(getResources().getColor(R.color.black));
                detailView.setPadding(4, 4, 4, 4);

                dropdownContent.addView(detailView);
            }

            // Klik listener untuk toggle dropdown
            itemView.setOnClickListener(v -> {
                if (dropdownContainer.getVisibility() == View.GONE) {
                    dropdownContainer.setVisibility(View.VISIBLE);
                    dropdownIcon.setRotation(360);
                } else {
                    dropdownContainer.setVisibility(View.GONE);
                    dropdownIcon.setRotation(270);
                }
            });

            historyContainer.addView(itemView);
        }
    }

    private void onRowClick(ProductionData data) {
        // Tampilkan semua indikator loading
        showAllLoadingIndicators(true);

        // Sembunyikan semua tabel
        setAllTableLayoutsVisibility(View.GONE);

        // Bersihkan semua data
        clearAllDataLists();

        executorService.execute(() -> {
            // Ambil data latar belakang
            noProduksi = data.getNoProduksi();
            tglProduksi = data.getTanggal();
            mesinProduksi = data.getMesin();

            // Ambil data untuk setiap tabel
            noS4SList = ProsesProduksiApi.getNoS4SByNoProduksi(noProduksi, "S4SProduksiInputS4S");
            noSTList = ProsesProduksiApi.getNoSTByNoProduksi(noProduksi);
            noMouldingList = ProsesProduksiApi.getNoMouldingByNoProduksi(noProduksi, "S4SProduksiInputMoulding");
            noFJList = ProsesProduksiApi.getNoFJByNoProduksi(noProduksi, "S4SProduksiInputFJ");
            noCCList = ProsesProduksiApi.getNoCCByNoProduksi(noProduksi, "S4SProduksiInputCCAkhir");
            noReprosesList = ProsesProduksiApi.getNoReprosesByNoProduksi(noProduksi);

            // Perbarui UI di thread utama
            runOnUiThread(() -> {
                // Set data ke TextView
                noProduksiView.setText(noProduksi);
                setDateToView(tglProduksi, tglProduksiView);
                mesinProduksiView.setText(mesinProduksi);

                // Populate semua tabel dan sembunyikan loading indikator
                updateTable(noS4SList, sumS4SLabel, loadingIndicatorNoS4S, noS4STableLayout, this::populateNoS4STable);
                updateTable(noSTList, sumSTLabel, loadingIndicatorNoST, noSTTableLayout, this::populateNoSTTable);
                updateTable(noMouldingList, sumMouldingLabel, loadingIndicatorNoMoulding, noMouldingTableLayout, this::populateNoMouldingTable);
                updateTable(noFJList, sumFJLabel, loadingIndicatorNoFJ, noFJTableLayout, this::populateNoFJTable);
                updateTable(noCCList, sumCCLabel, loadingIndicatorNoCC, noCCTableLayout, this::populateNoCCTable);
                updateTable(noReprosesList, sumReprosesLabel, loadingIndicatorNoReproses, noReprosesTableLayout, this::populateNoReprosesTable);
            });
        });
    }


    private void refreshSTTable() {
        // Tampilkan loading
        loadingIndicatorNoST.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            List<String> updatedNoSTList = ProsesProduksiApi.getNoSTByNoProduksi(noProduksi);

            runOnUiThread(() -> {
                noSTList = updatedNoSTList; // jika Anda ingin menyimpan global
                updateTable(
                        updatedNoSTList,
                        sumSTLabel,
                        loadingIndicatorNoST,
                        noSTTableLayout,
                        this::populateNoSTTable
                );
            });
        });
    }


    private void refreshS4STable() {
        // Tampilkan loading
        loadingIndicatorNoS4S.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            List<String> updatedNoS4SList = ProsesProduksiApi.getNoS4SByNoProduksi(noProduksi, "S4SProduksiInputS4S");

            runOnUiThread(() -> {
                noS4SList = updatedNoS4SList; // update list jika diperlukan di tempat lain
                updateTable(
                        updatedNoS4SList,
                        sumS4SLabel,
                        loadingIndicatorNoS4S,
                        noS4STableLayout,
                        this::populateNoS4STable
                );
            });
        });
    }


    private void refreshFJTable() {
        // Tampilkan loading
        loadingIndicatorNoFJ.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            List<String> updatedNoFJList = ProsesProduksiApi.getNoFJByNoProduksi(noProduksi, "S4SProduksiInputFJ");

            runOnUiThread(() -> {
                noFJList = updatedNoFJList; // update list jika diperlukan di tempat lain
                updateTable(
                        updatedNoFJList,
                        sumFJLabel,
                        loadingIndicatorNoFJ,
                        noFJTableLayout,
                        this::populateNoFJTable
                );
            });
        });
    }

    private void refreshMouldingTable() {
        // Tampilkan loading
        loadingIndicatorNoMoulding.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            List<String> updatedNoMouldingList = ProsesProduksiApi.getNoMouldingByNoProduksi(noProduksi, "S4SProduksiInputMoulding");

            runOnUiThread(() -> {
                noMouldingList = updatedNoMouldingList;
                updateTable(
                        updatedNoMouldingList,
                        sumMouldingLabel,
                        loadingIndicatorNoMoulding,
                        noMouldingTableLayout,
                        this::populateNoMouldingTable
                );
            });
        });
    }


    private void refreshCCTable() {
        // Tampilkan loading
        loadingIndicatorNoCC.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            List<String> updatedNoCCList = ProsesProduksiApi.getNoCCByNoProduksi(noProduksi, "S4SProduksiInputCCAkhir");

            runOnUiThread(() -> {
                noCCList = updatedNoCCList; // update list jika diperlukan di tempat lain
                updateTable(
                        updatedNoCCList,
                        sumCCLabel,
                        loadingIndicatorNoCC,
                        noCCTableLayout,
                        this::populateNoCCTable
                );
            });
        });
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
                spinSPK.setAdapter(adapter);

                // Set default selection
                if (selectedNoSPK == null || selectedNoSPK.isEmpty() || selectedNoSPK.equals("PILIH")) {
                    spinSPK.setSelection(0);
                } else {
                    for (int i = 0; i < spkList.size(); i++) {
                        if (spkList.get(i).getNoSPK().equals(selectedNoSPK)) {
                            spinSPK.setSelection(i);
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





    private <T> void updateTable(
            List<T> dataList,
            TextView sumLabel,
            ProgressBar loadingIndicator,
            View tableLayout,
            Consumer<List<T>> populateTableMethod
    ) {
        populateTableMethod.accept(dataList);
        sumLabel.setText(String.valueOf(dataList.size()));
        loadingIndicator.setVisibility(View.GONE);
        tableLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}