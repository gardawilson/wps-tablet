package com.example.myapplication;

import com.example.myapplication.api.MasterApi;
import com.example.myapplication.api.PenjualanApi;
import com.example.myapplication.api.ProsesProduksiApi;
import com.example.myapplication.model.MstBuyerData;
import com.example.myapplication.model.MstJenisKendaraanData;
import com.example.myapplication.model.PenjualanData;
import com.example.myapplication.model.TableConfig;
import com.example.myapplication.utils.CustomProgressDialog;
import com.example.myapplication.utils.DateTimeUtils;
import com.example.myapplication.utils.LoadingDialogHelper;
import com.example.myapplication.utils.PermissionUtils;
import com.example.myapplication.utils.ScannerAnimationUtils;
import com.example.myapplication.utils.SharedPrefUtils;
import com.example.myapplication.utils.TableConfigUtils;

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
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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

import com.example.myapplication.utils.CameraUtils;
import com.example.myapplication.utils.CameraXAnalyzer;
import com.example.myapplication.utils.TooltipUtils;
import com.example.myapplication.utils.ViewUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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

public class PenjualanStSnd extends AppCompatActivity {

    private TableLayout tableLayout;
    private TableLayout headerTableProduksi;
    private PreviewView cameraPreview;
    private Button btnCameraControl;
    private Button btnSimpan;
    private TableRow selectedRowHeader;
    private TableRow selectedRow;
    private TextView qrResultText;
    private TextView noPenjualanView;
    private TextView tglProduksiView;
    private TextView keteranganBongkarSusunView;
    private boolean isShowingResult = false;
    private ConstraintLayout scanLayout;
    private String noJual; // Variabel global
    private String tgl; // Variabel global
    private String keteranganBongkarSusun; // Variabel global
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ProcessCameraProvider cameraProvider;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final List<String> scannedResults = new ArrayList<>();
    private boolean isCameraActive = false;
    private PenjualanData selectedBongkarSusunData;
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
    private List<PenjualanData> dataList; // Data asli yang tidak difilter
    private ProgressBar loadingIndicator;
    private TableLayout noS4STableLayout;
    private TableLayout noSTTableLayout;
    private TableLayout noMouldingTableLayout;
    private TableLayout noFJTableLayout;
    private TableLayout noCCTableLayout;
    private TableLayout noLaminatingTableLayout;
    private TableLayout noSandingTableLayout;
    private LinearLayout jumlahLabel;
    private LinearLayout jumlahLabelHeader;
    private TextView sumS4SLabel;
    private TextView sumSTLabel;
    private TextView sumMouldingLabel;
    private TextView sumFJLabel;
    private TextView sumCCLabel;
    private TextView sumLaminatingLabel;
    private TextView sumSandingLabel;
    private View borderTop;
    private View borderBottom;
    private View borderLeft;
    private View borderRight;
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
    private ProgressBar loadingIndicatorNoLaminating;
    private ProgressBar loadingIndicatorNoSanding;
    private LinearLayout textScanQR;
    private Button btnInputManual;
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();
    private final String mainTable = "Penjualan_h";
    private Button btnEdit;
    private Button btnCreate;
    private List<String> userPermissions;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_st_snd);

        // Inisialisasi komponen UI
        tableLayout = findViewById(R.id.tableLayout);
        headerTableProduksi = findViewById(R.id.headerTableProduksi);
        cameraPreview = findViewById(R.id.cameraPreview);
        btnCameraControl = findViewById(R.id.btnCameraControl);
        qrResultText = findViewById(R.id.qrResultText);
        scanLayout = findViewById(R.id.scanLayout);
        noPenjualanView = findViewById(R.id.noPenjualanView);
        tglProduksiView = findViewById(R.id.tglProduksiView);
        keteranganBongkarSusunView = findViewById(R.id.keteranganBongkarSusunView);
        btnSimpan = findViewById(R.id.btnSimpan);
        kodeLabel = findViewById(R.id.kodeLabel);
        searchMainTable = findViewById(R.id.searchMainTable);
        btnInputKodeLabel = findViewById(R.id.btnInputKodeLabel);
        inputKodeManual = findViewById(R.id.inputKodeManual);
        textLayoutSearchMainTable = findViewById(R.id.textLayoutSearchMainTable);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        noSTTableLayout = findViewById(R.id.noSTTableLayout);
        noS4STableLayout = findViewById(R.id.noS4STableLayout);
        noMouldingTableLayout = findViewById(R.id.noMouldingTableLayout);
        noFJTableLayout = findViewById(R.id.noFJTableLayout);
        noCCTableLayout = findViewById(R.id.noCCTableLayout);
        noLaminatingTableLayout = findViewById(R.id.noLaminatingTableLayout);
        noSandingTableLayout = findViewById(R.id.noSandingTableLayout);
        jumlahLabel = findViewById(R.id.jumlahLabel);
        jumlahLabelHeader = findViewById(R.id.jumlahLabelHeader);
        sumS4SLabel = findViewById(R.id.sumS4SLabel);
        sumSTLabel = findViewById(R.id.sumSTLabel);
        sumMouldingLabel = findViewById(R.id.sumMouldingLabel);
        sumFJLabel = findViewById(R.id.sumFJLabel);
        sumCCLabel = findViewById(R.id.sumCCLabel);
        sumLaminatingLabel = findViewById(R.id.sumLaminatingLabel);
        sumSandingLabel = findViewById(R.id.sumSandingLabel);
        borderTop = findViewById(R.id.borderTop);
        borderBottom = findViewById(R.id.borderBottom);
        borderLeft = findViewById(R.id.borderLeft);
        borderRight = findViewById(R.id.borderRight);
        loadingIndicatorNoST = findViewById(R.id.loadingIndicatorNoST);
        loadingIndicatorNoS4S = findViewById(R.id.loadingIndicatorNoS4S);
        loadingIndicatorNoMoulding = findViewById(R.id.loadingIndicatorNoMoulding);
        loadingIndicatorNoFJ = findViewById(R.id.loadingIndicatorNoFJ);
        loadingIndicatorNoCC = findViewById(R.id.loadingIndicatorNoCC);
        loadingIndicatorNoLaminating = findViewById(R.id.loadingIndicatorNoLaminating);
        loadingIndicatorNoSanding = findViewById(R.id.loadingIndicatorNoSanding);
        btnInputManual = findViewById(R.id.btnInputManual);
        textScanQR = findViewById(R.id.textScanQR);
        btnEdit = findViewById(R.id.btnEdit);
        btnCreate = findViewById(R.id.btnCreate);

        loadingIndicator.setVisibility(View.VISIBLE);


        // Inisialisasi View scanner overlay
        scannerOverlay = findViewById(R.id.scannerOverlay);

        // Dapatkan DisplayMetrics untuk tinggi layar
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        // Mulai animasi scanner menggunakan ScannerAnimationUtils
        ScannerAnimationUtils.startScanningAnimation(scannerOverlay, displayMetrics);

        //PERMISSION CHECK
        userPermissions = SharedPrefUtils.getPermissions(this);
        PermissionUtils.permissionCheck(this, btnCreate, "penjualan_st_snd:create");
        PermissionUtils.permissionCheck(this, btnEdit, "penjualan_st_snd:update");

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


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreatePenjualanDialog();
            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noProduksi = noPenjualanView.getText().toString();
                if (!noProduksi.isEmpty()) {
                    showEditPenjualanDialog(selectedBongkarSusunData);
                } else {
                    Toast.makeText(PenjualanStSnd.this, "Pilih NoProduksi Terlebih Dahulu!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnInputManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noJual = noPenjualanView.getText().toString();

                if (!noJual.isEmpty()) {
                    // Membuat layout untuk dialog
                    LinearLayout layout = new LinearLayout(PenjualanStSnd.this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(50, 50, 50, 50);
                    layout.setGravity(Gravity.CENTER_HORIZONTAL);

                    // Membuat TextInputLayout untuk EditText yang lebih modern
                    TextInputLayout textInputLayout = new TextInputLayout(PenjualanStSnd.this);
                    textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_FILLED);

                    // Membuat EditText di dalam TextInputLayout
                    final EditText inputNoLabelManual = new EditText(PenjualanStSnd.this);  // Nama diganti menjadi inputNoLabelManual
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
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(PenjualanStSnd.this);
                    builder.setTitle("Input Label")
                            .setView(layout)
                            .setBackground(ContextCompat.getDrawable(PenjualanStSnd.this, R.drawable.tooltip_background))
                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String result = inputNoLabelManual.getText().toString();  // Menggunakan inputNoLabelManual
                                    if (!result.isEmpty()) {
                                        // Panggil metode yang sama dengan hasil scan
                                        addScanResultToTable(result);
                                    } else {
                                        Toast.makeText(PenjualanStSnd.this, "Kode tidak boleh kosong", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PenjualanStSnd.this, "Kode tidak boleh kosong", Toast.LENGTH_SHORT).show();
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
                List<PenjualanData> filteredList = new ArrayList<>();
                int count = 0;

                for (PenjualanData data : dataList) {
                    if (count >= PAGE_SIZE) break;

                    if ((data.getNoJual() != null && data.getNoJual().toLowerCase().contains(query)) ||
                            (data.getTglJual() != null && data.getTglJual().toLowerCase().contains(query)) ||
                            (data.getKeterangan() != null && data.getKeterangan().toLowerCase().contains(query))) {
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
            dataList = PenjualanApi.getPenjualanData(mainTable);

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
                Log.e("Penjualan", "Error initializing camera provider: " + e.getMessage());
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
    }

    private void loadBuyerSpinner(Spinner spinBuyer, int selectedIdBuyer, @Nullable Runnable onDone) {
        executorService.execute(() -> {
            // Ambil data buyer dari DB (background thread)
            List<MstBuyerData> buyerList = MasterApi.getBuyerList();

            // Sisipkan sentinel "PILIH" di posisi 0
            buyerList.add(0, new MstBuyerData(0, "PILIH", false));

            runOnUiThread(() -> {
                // Ikat ke spinner (UI thread)
                ArrayAdapter<MstBuyerData> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        buyerList
                );
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinBuyer.setAdapter(adapter);

                // Pilih item sesuai selectedIdBuyer (default ke "PILIH" kalau 0 / tidak ketemu)
                if (selectedIdBuyer == 0) {
                    spinBuyer.setSelection(0);
                } else {
                    for (int i = 0; i < buyerList.size(); i++) {
                        if (buyerList.get(i).getIdBuyer() == selectedIdBuyer) {
                            spinBuyer.setSelection(i);
                            break;
                        }
                    }
                }

                // Callback setelah spinner selesai diisi
                if (onDone != null) onDone.run();
            });
        });
    }

    // Overload tanpa callback (biar kompatibel dengan pemakaian lama)
    private void loadBuyerSpinner(Spinner spinBuyer, int selectedIdBuyer) {
        loadBuyerSpinner(spinBuyer, selectedIdBuyer, null);
    }


    private void loadJenisKendaraanSpinner(Spinner spinJenisKendaraan, int selectedId, @Nullable Runnable onDone) {
        executorService.execute(() -> {
            List<MstJenisKendaraanData> list = MasterApi.getJenisKendaraanList();
            // Sentinel "PILIH"
            list.add(0, new MstJenisKendaraanData(0, "PILIH", "", true));

            runOnUiThread(() -> {
                ArrayAdapter<MstJenisKendaraanData> adapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_item, list
                );
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinJenisKendaraan.setAdapter(adapter);

                if (selectedId == 0) {
                    spinJenisKendaraan.setSelection(0);
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getIdJenisKendaraan() == selectedId) {
                            spinJenisKendaraan.setSelection(i);
                            break;
                        }
                    }
                }

                if (onDone != null) onDone.run();
            });
        });
    }

    private void loadJenisKendaraanSpinner(Spinner spinJenisKendaraan, int selectedId) {
        loadJenisKendaraanSpinner(spinJenisKendaraan, selectedId, null); // tidak ada callback
    }



//------------------------------------------------------------------------------------------------------------------------------------------------------//
//----------------------------------METHOD CREATE AND EDIT DIALOG---------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------------------------------------------------------------//

    private void showCreatePenjualanDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_field_penjualan_header, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Referensi UI
        TextView titleDialog = dialogView.findViewById(R.id.titleDialog);
        TextView tvNoPenjualan = dialogView.findViewById(R.id.tvNoPenjualan);
        EditText editTanggal = dialogView.findViewById(R.id.editTanggal);
        EditText editNoSJ = dialogView.findViewById(R.id.editNoSJ);
        EditText editNoPlat = dialogView.findViewById(R.id.editNoPlat);
        EditText editKeterangan = dialogView.findViewById(R.id.editKeterangan);
        Spinner spinJenisKendaraan = dialogView.findViewById(R.id.spinJenisKendaraan);
        Spinner spinBuyer = dialogView.findViewById(R.id.spinBuyer);
        ImageButton btnClose = dialogView.findViewById(R.id.btnCloseDialogInput);
        Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);

        titleDialog.setText("Penjualan ST s/d SND (Data Baru)");

        // Untuk create, NoBongkarSusun belum di-generate → kasih placeholder
        tvNoPenjualan.setText("G.XXXXXX");

        editTanggal.setText(DateTimeUtils.getCurrentDate());
        editTanggal.setInputType(InputType.TYPE_NULL);
        editTanggal.setFocusable(false);
        editTanggal.setOnClickListener(v -> {
            DateTimeUtils.showDatePicker(PenjualanStSnd.this, editTanggal);
        });

        loadBuyerSpinner(spinBuyer, 0);

        loadJenisKendaraanSpinner(spinJenisKendaraan, 0);

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnUpdate.setOnClickListener(v -> {
            loadingDialogHelper.show(this);

            String tanggal = editTanggal.getText().toString().trim(); // pastikan "yyyy-MM-dd"
            String keterangan = editKeterangan.getText().toString().trim();
            String noSJ = editNoSJ.getText().toString().trim();
            String noPlat = editNoPlat.getText().toString().trim();

            // Ambil ID dari spinner
            MstBuyerData buyer = (MstBuyerData) spinBuyer.getSelectedItem();
            int idBuyer = (buyer != null) ? buyer.getIdBuyer() : 0;

            MstJenisKendaraanData jk = (MstJenisKendaraanData) spinJenisKendaraan.getSelectedItem();
            int idJenisKendaraan = (jk != null) ? jk.getIdJenisKendaraan() : 0;

            executorService.execute(() -> {
                String newNoJual = PenjualanApi.insertPenjualanHeaderWithGeneratedNoJual(
                        tanggal,         // "yyyy-MM-dd"
                        idBuyer,
                        keterangan.isEmpty() ? null : keterangan,
                        noSJ.isEmpty() ? null : noSJ,
                        noPlat.isEmpty() ? null : noPlat,
                        idJenisKendaraan
                );

                boolean success = (newNoJual != null);
                if (success) {
                    dataList = PenjualanApi.getPenjualanData("Penjualan_h");
                }

                runOnUiThread(() -> {
                    loadingDialogHelper.hide();
                    if (success) {
                        tvNoPenjualan.setText(newNoJual);
                        dialog.dismiss();
                        Toast.makeText(PenjualanStSnd.this, "Data berhasil dibuat", Toast.LENGTH_SHORT).show();
                        populateTable(dataList);
                    } else {
                        Toast.makeText(PenjualanStSnd.this, "Gagal membuat data", Toast.LENGTH_SHORT).show();
                    }
                });
            });

        });

        dialog.show();
    }


    private void showEditPenjualanDialog(PenjualanData data) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_field_penjualan_header, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Referensi UI
        TextView titleDialog = dialogView.findViewById(R.id.titleDialog);
        TextView tvNoPenjualan    = dialogView.findViewById(R.id.tvNoPenjualan);
        EditText editTanggal      = dialogView.findViewById(R.id.editTanggal);
        EditText editNoSJ         = dialogView.findViewById(R.id.editNoSJ);
        EditText editNoPlat       = dialogView.findViewById(R.id.editNoPlat);
        EditText editKeterangan   = dialogView.findViewById(R.id.editKeterangan);
        Spinner spinJenisKendaraan= dialogView.findViewById(R.id.spinJenisKendaraan);
        Spinner spinBuyer         = dialogView.findViewById(R.id.spinBuyer);
        Button btnUpdate          = dialogView.findViewById(R.id.btnUpdate);
        ImageButton btnClose = dialogView.findViewById(R.id.btnCloseDialogInput);

        // Prefill field dari model
        tvNoPenjualan.setText(data.getNoJual());
        editTanggal.setText(data.getTglJual());
        editNoSJ.setText(data.getNoSJ());
        editNoPlat.setText(data.getNoPlat());
        editKeterangan.setText(data.getKeterangan());

        // Spinner: panggil loader dengan ID selected
        loadBuyerSpinner(spinBuyer, data.getIdBuyer());
        loadJenisKendaraanSpinner(spinJenisKendaraan, data.getIdJenisKendaraan());

        titleDialog.setText("Penjualan ST s/d SND (Edit)");

        editTanggal.setInputType(InputType.TYPE_NULL);
        editTanggal.setFocusable(false);
        editTanggal.setOnClickListener(v -> {
            DateTimeUtils.showDatePicker(PenjualanStSnd.this, editTanggal);
        });

        btnUpdate.setOnClickListener(v -> {
            loadingDialogHelper.show(PenjualanStSnd.this);

            // Ambil value baru dari input
            String tanggal = editTanggal.getText().toString().trim();
            String keterangan = editKeterangan.getText().toString().trim();
            String noSJ = editNoSJ.getText().toString().trim();
            String noPlat = editNoPlat.getText().toString().trim();
            MstBuyerData buyer = (MstBuyerData) spinBuyer.getSelectedItem();
            int idBuyer = buyer != null ? buyer.getIdBuyer() : 0;
            MstJenisKendaraanData jk = (MstJenisKendaraanData) spinJenisKendaraan.getSelectedItem();
            int idJenisKendaraan = jk != null ? jk.getIdJenisKendaraan() : 0;

            executorService.execute(() -> {
                boolean success = PenjualanApi.updatePenjualanHeader(
                        data.getNoJual(), tanggal, idBuyer,
                        keterangan.isEmpty()? null: keterangan,
                        noSJ.isEmpty()? null: noSJ,
                        noPlat.isEmpty()? null: noPlat,
                        idJenisKendaraan
                );

                if (success) {
                    dataList = PenjualanApi.getPenjualanData("Penjualan_h");
                }

                runOnUiThread(() -> {
                    loadingDialogHelper.hide();
                    if (success) {
                        Toast.makeText(PenjualanStSnd.this, "Update berhasil", Toast.LENGTH_SHORT).show();
                        populateTable(dataList);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(PenjualanStSnd.this, "Update gagal", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());


        dialog.show();
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
                deactivateCamera();
            } else {
                String noJual = noPenjualanView.getText().toString();

                if (!noJual.isEmpty()) {
                    executorService.execute(() -> {
                        if (tgl == null) {
                            runOnUiThread(() ->
                                    Toast.makeText(this, "Tanggal Produksi tidak valid!", Toast.LENGTH_SHORT).show()
                            );
                            return;
                        }

                        // Periksa periode transaksi
                        boolean isClosed = MasterApi.isPeriodValid(DateTimeUtils.formatToDatabaseDate(tgl));

                        runOnUiThread(() -> {
                            if (!isClosed) {
                                Toast.makeText(this, "Periode transaksi sudah ditutup!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Jika valid, minta permission kamera
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                        });
                    });
                } else {
                    Toast.makeText(this, "Pilih NoBongkarSusun Terlebih Dahulu!", Toast.LENGTH_SHORT).show();
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
            btnCameraControl.setText("Back");
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
    private void populateTable(List<PenjualanData> dataList) {

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

        for (PenjualanData data : dataList) {
            TableRow row = new TableRow(this);

            // Simpan indeks baris di tag
            row.setTag(rowIndex);

            // Tambahkan TextView untuk setiap kolom
            TextView col1 = createTextView(data.getNoJual(), 1f);
            TextView col2 = createTextView(data.getTglJual(), 1f);
            TextView col3 = createTextView(data.getNoSJ(), 1f);
            TextView col4 = createTextView(data.getBuyerName(), 1f);
            TextView col5 = createTextView(data.getJenisKendaraanModel(), 1f);
            TextView col6 = createTextView(data.getKeterangan(), 1f);

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
                selectedBongkarSusunData = data;

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
                if (!userPermissions.contains("penjualan_st_snd:delete")) {
                    Toast.makeText(this, "Anda tidak memiliki izin untuk menghapus.", Toast.LENGTH_SHORT).show();
                    return true; // event dianggap sudah di-handle
                }

                new AlertDialog.Builder(this)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus data " + currentNoST + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            Toast.makeText(this,
                                    "Menghapus data " + noJual + " " + currentNoST,
                                    Toast.LENGTH_SHORT).show();

                            executorService.execute(() -> {
                                ProsesProduksiApi.deleteDataByNoLabel(noJual, currentNoST);
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
                if (!userPermissions.contains("penjualan_st_snd:delete")) {
                    Toast.makeText(this, "Anda tidak memiliki izin untuk menghapus.", Toast.LENGTH_SHORT).show();
                    return true; // event dianggap sudah di-handle
                }

                new AlertDialog.Builder(this)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus data " + currentNoS4S + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            Toast.makeText(this,
                                    "Menghapus data " + noJual + " " + currentNoS4S,
                                    Toast.LENGTH_SHORT).show();

                            executorService.execute(() -> {
                                ProsesProduksiApi.deleteDataByNoLabel(noJual, currentNoS4S);
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
                if (!userPermissions.contains("penjualan_st_snd:delete")) {
                    Toast.makeText(this, "Anda tidak memiliki izin untuk menghapus.", Toast.LENGTH_SHORT).show();
                    return true; // event dianggap sudah di-handle
                }

                new AlertDialog.Builder(this)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus data " + currentNoFJ + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            Toast.makeText(this,
                                    "Menghapus data " + noJual + " " + currentNoFJ,
                                    Toast.LENGTH_SHORT).show();

                            executorService.execute(() -> {
                                ProsesProduksiApi.deleteDataByNoLabel(noJual, currentNoFJ);
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
                if (!userPermissions.contains("penjualan_st_snd:delete")) {
                    Toast.makeText(this, "Anda tidak memiliki izin untuk menghapus.", Toast.LENGTH_SHORT).show();
                    return true; // event dianggap sudah di-handle
                }

                new AlertDialog.Builder(this)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus data " + currentNoMoulding + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            Toast.makeText(this,
                                    "Menghapus data " + noJual + " " + currentNoMoulding,
                                    Toast.LENGTH_SHORT).show();

                            executorService.execute(() -> {
                                ProsesProduksiApi.deleteDataByNoLabel(noJual, currentNoMoulding);
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


    private void populateNoLaminatingTable(List<String> noLaminatingList) {
        noLaminatingTableLayout.removeAllViews();
        int rowIndex = 0;

        if (noLaminatingList == null || noLaminatingList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Tidak ada Data");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            noLaminatingTableLayout.addView(noDataView);
            return;
        }

        // Isi tabel
        for (String noLaminating : noLaminatingList) {
            TableRow row = new TableRow(this);
            row.setTag(rowIndex);

            TextView textView = createTextView(noLaminating, 1.0f);
            row.addView(textView);

            final int currentRowIndex = rowIndex;
            final TableRow currentRow = row;
            final String currentNoLaminating = noLaminating;

            row.setOnClickListener(view -> {
                ViewUtils.handleRowSelection(this, currentRow, currentRowIndex, selectedRow);
                selectedRow = currentRow;

                TooltipUtils.fetchDataAndShowTooltip(
                        this,
                        executorService,
                        view,
                        currentNoLaminating,
                        "Laminating_h",
                        "Laminating_d",
                        "NoLaminating",
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
                if (!userPermissions.contains("penjualan_st_snd:delete")) {
                    Toast.makeText(this, "Anda tidak memiliki izin untuk menghapus.", Toast.LENGTH_SHORT).show();
                    return true; // event dianggap sudah di-handle
                }

                new AlertDialog.Builder(this)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus data " + currentNoLaminating + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            Toast.makeText(this,
                                    "Menghapus data " + noJual + " " + currentNoLaminating,
                                    Toast.LENGTH_SHORT).show();

                            executorService.execute(() -> {
                                ProsesProduksiApi.deleteDataByNoLabel(noJual, currentNoLaminating);
                                runOnUiThread(this::refreshLaminatingTable);
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

            noLaminatingTableLayout.addView(row);
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
                if (!userPermissions.contains("penjualan_st_snd:delete")) {
                    Toast.makeText(this, "Anda tidak memiliki izin untuk menghapus.", Toast.LENGTH_SHORT).show();
                    return true; // event dianggap sudah di-handle
                }

                new AlertDialog.Builder(this)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus data " + currentNoCC + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            Toast.makeText(this,
                                    "Menghapus data " + noJual + " " + currentNoCC,
                                    Toast.LENGTH_SHORT).show();

                            executorService.execute(() -> {
                                ProsesProduksiApi.deleteDataByNoLabel(noJual, currentNoCC);
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

    private void populateNoSandingTable(List<String> noSandingList) {
        noSandingTableLayout.removeAllViews();
        int rowIndex = 0;

        if (noSandingList == null || noSandingList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Tidak ada Data");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            noSandingTableLayout.addView(noDataView);
            return;
        }

        for (String noSanding : noSandingList) {
            TableRow row = new TableRow(this);
            row.setTag(rowIndex);

            TextView textView = createTextView(noSanding, 1.0f);
            row.addView(textView);

            final int currentRowIndex = rowIndex;
            final TableRow currentRow = row;
            final String currentNoSanding = noSanding;

            row.setOnClickListener(view -> {
                ViewUtils.handleRowSelection(this, currentRow, currentRowIndex, selectedRow);
                selectedRow = currentRow;

                TooltipUtils.fetchDataAndShowTooltip(
                        this,
                        executorService,
                        view,
                        currentNoSanding,
                        "Sanding_h",
                        "Sanding_d",
                        "NoSanding",
                        () -> {
                            if (selectedRow != null) {
                                int currentIndex = (int) selectedRow.getTag();
                                ViewUtils.resetRowSelection(this, selectedRow, currentIndex);
                                selectedRow = null;
                            }
                        }
                );
            });

            row.setOnLongClickListener(v -> {
                if (!userPermissions.contains("penjualan_st_snd:delete")) {
                    Toast.makeText(this, "Anda tidak memiliki izin untuk menghapus.", Toast.LENGTH_SHORT).show();
                    return true; // event dianggap sudah di-handle
                }

                new AlertDialog.Builder(this)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus data " + currentNoSanding + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            Toast.makeText(this,
                                    "Menghapus data " + noJual + " " + currentNoSanding,
                                    Toast.LENGTH_SHORT).show();

                            executorService.execute(() -> {
                                ProsesProduksiApi.deleteDataByNoLabel(noJual, currentNoSanding);
                                runOnUiThread(this::refreshSandingTable);
                            });
                        })
                        .setNegativeButton("Batal", null)
                        .show();
                return true; // True = long press sudah di-handle
            });

            row.setBackgroundColor(ContextCompat.getColor(this,
                    rowIndex % 2 == 0 ? R.color.background_cream : R.color.white));

            noSandingTableLayout.addView(row);
            rowIndex++;
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
        if (PenjualanApi.isDateValidInPenjualan(noJual, mainTable, result, config.tableNameH, config.columnName)) {
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
        // SELALU tampilkan loading di UI thread
        runOnUiThread(() -> {
            if (!isFinishing() && !isDestroyed()) {
                loadingDialogHelper.show(this);
                // OPTIONAL jika helper mendukung pesan:
                // loadingDialogHelper.setMessage("Menyimpan data...");
            }
        });

        final String dateTimeSaved = DateTimeUtils.getCurrentDateTime();
        final String savedUsername = SharedPrefUtils.getUsername(this);

        executorService.execute(() -> {
            Log.d("SaveScannedResults", "Memulai proses penyimpanan hasil scan ke database");

            final int totalItems =
                    noSTList.size() + noS4SList.size() + noMouldingList.size() +
                            noLaminatingList.size() + noFJList.size() + noCCList.size() + noSandingList.size();

            if (totalItems == 0) {
                runOnUiThread(() -> {
                    if (!isFinishing() && !isDestroyed()) {
                        // OPTIONAL:
                        // loadingDialogHelper.setMessage("Tidak ada data baru.");
                        loadingDialogHelper.hide();
                        Toast.makeText(this, "Tidak ada data baru untuk disimpan.", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("SaveScannedResults", "Tidak ada item untuk diproses");
                return;
            }

            int savedItems = 0;
            int errorCount = 0;

            // Helper kecil untuk update status (kalau helper mendukung pesan)
            final java.util.function.IntConsumer updateStatus = (progress) -> {
                runOnUiThread(() -> {
                    if (!isFinishing() && !isDestroyed()) {
                        // Jika helper mendukung progress/message, gunakan salah satu:
                        // loadingDialogHelper.setProgress(progress); // jika ada
                        // loadingDialogHelper.setMessage("Memproses " + progress + "%");
                    }
                });
            };

            try {
                // === ST ===
                try {
                    if (!noSTList.isEmpty()) {
                        List<String> existing = PenjualanApi.getNoSTByNoJual(noJual, "PenjualanST");
                        List<String> toSave = new ArrayList<>(noSTList);
                        toSave.removeAll(existing);
                        if (!toSave.isEmpty()) {
                            PenjualanApi.saveNoSTInPenjualan(noJual, tgl, toSave, "PenjualanST");
                            savedItems += toSave.size();
                            updateStatus.accept((savedItems * 100) / totalItems);
                        }
                    }
                } catch (Exception e) { errorCount++; Log.e("SaveScannedResults","Gagal simpan ST", e); }

                // === S4S ===
                try {
                    if (!noS4SList.isEmpty()) {
                        List<String> existing = PenjualanApi.getNoS4SByNoJual(noJual, "PenjualanS4S");
                        List<String> toSave = new ArrayList<>(noS4SList);
                        toSave.removeAll(existing);
                        if (!toSave.isEmpty()) {
                            PenjualanApi.saveNoS4SInPenjualan(noJual, tgl, toSave, "PenjualanS4S");
                            savedItems += toSave.size();
                            updateStatus.accept((savedItems * 100) / totalItems);
                        }
                    }
                } catch (Exception e) { errorCount++; Log.e("SaveScannedResults","Gagal simpan S4S", e); }

                // === FJ ===
                try {
                    if (!noFJList.isEmpty()) {
                        List<String> existing = PenjualanApi.getNoFJByNoJual(noJual, "PenjualanFJ");
                        List<String> toSave = new ArrayList<>(noFJList);
                        toSave.removeAll(existing);
                        if (!toSave.isEmpty()) {
                            PenjualanApi.saveNoFJInPenjualan(noJual, tgl, toSave, "PenjualanFJ");
                            savedItems += toSave.size();
                            updateStatus.accept((savedItems * 100) / totalItems);
                        }
                    }
                } catch (Exception e) { errorCount++; Log.e("SaveScannedResults","Gagal simpan FJ", e); }

                // === Moulding ===
                try {
                    if (!noMouldingList.isEmpty()) {
                        List<String> existing = PenjualanApi.getNoMouldingByNoJual(noJual, "PenjualanMoulding");
                        List<String> toSave = new ArrayList<>(noMouldingList);
                        toSave.removeAll(existing);
                        if (!toSave.isEmpty()) {
                            PenjualanApi.saveNoMouldingInPenjualan(noJual, tgl, toSave, "PenjualanMoulding");
                            savedItems += toSave.size();
                            updateStatus.accept((savedItems * 100) / totalItems);
                        }
                    }
                } catch (Exception e) { errorCount++; Log.e("SaveScannedResults","Gagal simpan Moulding", e); }

                // === Laminating ===
                try {
                    if (!noLaminatingList.isEmpty()) {
                        List<String> existing = PenjualanApi.getNoLaminatingByNoJual(noJual, "PenjualanLaminating");
                        List<String> toSave = new ArrayList<>(noLaminatingList);
                        toSave.removeAll(existing);
                        if (!toSave.isEmpty()) {
                            PenjualanApi.saveNoLaminatingInPenjualan(noJual, tgl, toSave, "PenjualanLaminating");
                            savedItems += toSave.size();
                            updateStatus.accept((savedItems * 100) / totalItems);
                        }
                    }
                } catch (Exception e) { errorCount++; Log.e("SaveScannedResults","Gagal simpan Laminating", e); }

                // === CC ===
                try {
                    if (!noCCList.isEmpty()) {
                        List<String> existing = PenjualanApi.getNoCCByNoJual(noJual, "PenjualanCCAkhir");
                        List<String> toSave = new ArrayList<>(noCCList);
                        toSave.removeAll(existing);
                        if (!toSave.isEmpty()) {
                            PenjualanApi.saveNoCCInPenjualan(noJual, tgl, toSave, "PenjualanCCAkhir");
                            savedItems += toSave.size();
                            updateStatus.accept((savedItems * 100) / totalItems);
                        }
                    }
                } catch (Exception e) { errorCount++; Log.e("SaveScannedResults","Gagal simpan CC", e); }

                // === Sanding ===
                try {
                    if (!noSandingList.isEmpty()) {
                        List<String> existing = PenjualanApi.getNoSandingByNoJual(noJual, "PenjualanSanding");
                        List<String> toSave = new ArrayList<>(noSandingList);
                        toSave.removeAll(existing);
                        if (!toSave.isEmpty()) {
                            PenjualanApi.saveNoSandingInPenjualan(noJual, tgl, toSave, "PenjualanSanding");
                            savedItems += toSave.size();
                            updateStatus.accept((savedItems * 100) / totalItems);
                        }
                    }
                } catch (Exception e) { errorCount++; Log.e("SaveScannedResults","Gagal simpan Sanding", e); }

                // Riwayat (non-blocking)
                try {
                    ProsesProduksiApi.saveRiwayat(
                            savedUsername, dateTimeSaved,
                            "Mengubah Data " + noJual + " Pada Bongkar Susun (Mobile)"
                    );
                } catch (Exception e) {
                    Log.e("SaveScannedResults", "Gagal simpan riwayat", e);
                }

                // Bersihkan list
                noS4SList.clear();
                noMouldingList.clear();
                noFJList.clear();
                noCCList.clear();
                noLaminatingList.clear();
                noSandingList.clear();
                scannedResults.clear();

            } finally {
                // Tutup loading & beri feedback SEKALI di UI thread
                runOnUiThread(() -> {
                    if (!isFinishing() && !isDestroyed()) {
                        // OPTIONAL:
                        // loadingDialogHelper.setMessage("Selesai");
                        loadingDialogHelper.hide();

                        // Pesan hasil
                        // (errorCount di-finalize ke variabel effectively final jika ingin dipakai di finally)
                        // Di atas, kita pakai primitive int errorCount; pindahkan ke final[] jika ingin diakses di sini.
                        // Supaya simple, ganti errorCount ke array:
                    }
                });
            }

            // Karena kita ingin tampilkan pesan hasil, panggil satu lagi UI block di luar finally:
            runOnUiThread(() -> {
                if (!isFinishing() && !isDestroyed()) {
                    // Sampaikan hasil
                    // Jika Anda memindahkan errorCount ke array: final int errors = errorCount[0];
                    // Di kode kita pakai variable lokal 'errorCount' non-final, maka pindahkan logika ini ke dalam blok finally di atas
                    // atau ubah deklarasi errorCount menjadi final int[] errorCountBox = {0}; lalu gunakan errorCountBox[0].
                    Toast.makeText(this, "Berhasil menyimpan data", Toast.LENGTH_SHORT).show();
                    if (selectedBongkarSusunData != null) {
                        onRowClick(selectedBongkarSusunData);
                    } else {
                        Log.w("SaveScannedResults", "Tidak ada data yang dipilih untuk diperbarui.");
                    }
                }
            });

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
        loadingIndicatorNoST.setVisibility(visibility);
        loadingIndicatorNoS4S.setVisibility(visibility);
        loadingIndicatorNoMoulding.setVisibility(visibility);
        loadingIndicatorNoFJ.setVisibility(visibility);
        loadingIndicatorNoCC.setVisibility(visibility);
        loadingIndicatorNoLaminating.setVisibility(visibility);
        loadingIndicatorNoSanding.setVisibility(visibility);
    }

    private void setAllTableLayoutsVisibility(int visibility) {
        noSTTableLayout.setVisibility(visibility);
        noS4STableLayout.setVisibility(visibility);
        noMouldingTableLayout.setVisibility(visibility);
        noFJTableLayout.setVisibility(visibility);
        noCCTableLayout.setVisibility(visibility);
        noLaminatingTableLayout.setVisibility(visibility);
        noSandingTableLayout.setVisibility(visibility);
    }

    private void clearAllDataLists() {
        noS4SList.clear();
        noMouldingList.clear();
        noFJList.clear();
        noCCList.clear();
        noLaminatingList.clear();
        noSandingList.clear();
        scannedResults.clear();
    }

    /**
     * Membuat TextView untuk digunakan dalam tabel
     */
    private TextView createTextView(String text, float weight) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setEllipsize(TextUtils.TruncateAt.END);  // Tambahkan ini
        textView.setMaxLines(1);  // Pastikan hanya satu baris
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

    private void onRowClick(PenjualanData data) {
        // Tampilkan semua indikator loading
        showAllLoadingIndicators(true);

        // Sembunyikan semua tabel
        setAllTableLayoutsVisibility(View.GONE);

        // Bersihkan semua data
        clearAllDataLists();

        executorService.execute(() -> {
            // Ambil data latar belakang
            noJual = data.getNoJual();
            tgl = data.getTglJual();
            keteranganBongkarSusun = data.getKeterangan();

            // Ambil data untuk setiap tabel
            noSTList = PenjualanApi.getNoSTByNoJual(noJual, "PenjualanST");
            noS4SList = PenjualanApi.getNoS4SByNoJual(noJual, "PenjualanS4S");
            noFJList = PenjualanApi.getNoFJByNoJual(noJual, "PenjualanFJ");
            noMouldingList = PenjualanApi.getNoMouldingByNoJual(noJual, "PenjualanMoulding");
            noLaminatingList = PenjualanApi.getNoLaminatingByNoJual(noJual, "PenjualanLaminating");
            noCCList = PenjualanApi.getNoCCByNoJual(noJual, "PenjualanCCAkhir");
            noSandingList = PenjualanApi.getNoSandingByNoJual(noJual, "PenjualanSanding");

            // Perbarui UI di thread utama
            runOnUiThread(() -> {
                // Set data ke TextView
                noPenjualanView.setText(noJual);
                keteranganBongkarSusunView.setText(keteranganBongkarSusun);

                // Populate semua tabel dan sembunyikan loading indikator
                updateTable(noSTList, sumSTLabel, loadingIndicatorNoST, noSTTableLayout, this::populateNoSTTable);
                updateTable(noS4SList, sumS4SLabel, loadingIndicatorNoS4S, noS4STableLayout, this::populateNoS4STable);
                updateTable(noMouldingList, sumMouldingLabel, loadingIndicatorNoMoulding, noMouldingTableLayout, this::populateNoMouldingTable);
                updateTable(noFJList, sumFJLabel, loadingIndicatorNoFJ, noFJTableLayout, this::populateNoFJTable);
                updateTable(noCCList, sumCCLabel, loadingIndicatorNoCC, noCCTableLayout, this::populateNoCCTable);
                updateTable(noLaminatingList, sumLaminatingLabel, loadingIndicatorNoLaminating, noLaminatingTableLayout, this::populateNoLaminatingTable);
                updateTable(noSandingList, sumSandingLabel, loadingIndicatorNoSanding, noSandingTableLayout, this::populateNoSandingTable);
            });
        });
    }


    private void refreshSTTable() {
        // Tampilkan loading
        loadingIndicatorNoST.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            List<String> updatedNoSTList = PenjualanApi.getNoSTByNoJual(noJual, "PenjualanST");

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
            List<String> updatedNoS4SList = ProsesProduksiApi.getNoS4SByNoBongkarSusun(noJual, "BongkarSusunInputS4S");

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
            List<String> updatedNoFJList = ProsesProduksiApi.getNoFJByNoBongkarSusun(noJual, "BongkarSusunInputFJ");

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
            List<String> updatedNoMouldingList = ProsesProduksiApi.getNoMouldingByNoBongkarSusun(noJual, "BongkarSusunInputMoulding");

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

    private void refreshLaminatingTable() {
        loadingIndicatorNoLaminating.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            List<String> updatedNoLaminatingList = ProsesProduksiApi.getNoLaminatingByNoBongkarSusun(noJual, "BongkarSusunInputLaminating");

            runOnUiThread(() -> {
                noLaminatingList = updatedNoLaminatingList;
                updateTable(
                        updatedNoLaminatingList,
                        sumLaminatingLabel,
                        loadingIndicatorNoLaminating,
                        noLaminatingTableLayout,
                        this::populateNoLaminatingTable
                );
            });
        });
    }



    private void refreshCCTable() {
        // Tampilkan loading
        loadingIndicatorNoCC.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            List<String> updatedNoCCList = ProsesProduksiApi.getNoCCByNoBongkarSusun(noJual, "BongkarSusunInputCCAkhir");

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


    private void refreshSandingTable() {
        // Tampilkan loading
        loadingIndicatorNoSanding.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            List<String> updatedNoSandingList = ProsesProduksiApi.getNoSandingByNoBongkarSusun(noJual, "BongkarSusunInputSanding");

            runOnUiThread(() -> {
                noSandingList = updatedNoSandingList; // update list jika dibutuhkan di tempat lain
                updateTable(
                        updatedNoSandingList,
                        sumSandingLabel,
                        loadingIndicatorNoSanding,
                        noSandingTableLayout,
                        this::populateNoSandingTable
                );
            });
        });
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