package com.example.myapplication;

import com.example.myapplication.model.TableConfig;
import com.example.myapplication.model.TooltipData;
import com.example.myapplication.utils.CustomProgressDialog;
import com.example.myapplication.utils.DateTimeUtils;
import com.example.myapplication.utils.ScannerAnimationUtils;
import com.example.myapplication.utils.SharedPrefUtils;
import com.example.myapplication.utils.TableConfigUtils;

import static com.example.myapplication.api.ProductionApi.isTransactionPeriodClosed;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
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
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.api.ProductionApi;
import com.example.myapplication.model.HistoryItem;
import com.example.myapplication.model.ProductionData;
import com.example.myapplication.utils.CameraUtils;
import com.example.myapplication.utils.CameraXAnalyzer;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

import android.animation.ObjectAnimator;
import android.util.DisplayMetrics;

public class ProsesProduksiPacking extends AppCompatActivity {

    private TableLayout tableLayout;
    private TableLayout headerTableProduksi;
    private PreviewView cameraPreview;
    private Button btnCameraControl;
    private Button btnSimpan;
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
    private TableLayout noMouldingTableLayout;
    private TableLayout noCCTableLayout;
    private TableLayout noSandingTableLayout;
    private TableLayout noPackingTableLayout;
    private LinearLayout jumlahLabel;
    private LinearLayout jumlahLabelHeader;
    private TextView sumMouldingLabel;
    private TextView sumCCLabel;
    private TextView sumSandingLabel;
    private TextView sumPackingLabel;
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
    private ProgressBar loadingIndicatorNoMoulding;
    private ProgressBar loadingIndicatorNoCC;
    private ProgressBar loadingIndicatorNoSanding;
    private ProgressBar loadingIndicatorNoPacking;
    private LinearLayout textScanQR;
    private Button btnInputManual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_produksi_packing);

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
        noMouldingTableLayout = findViewById(R.id.noMouldingTableLayout);
        noCCTableLayout = findViewById(R.id.noCCTableLayout);
        noSandingTableLayout = findViewById(R.id.noSandingTableLayout);
        noPackingTableLayout = findViewById(R.id.noPackingTableLayout);
        jumlahLabel = findViewById(R.id.jumlahLabel);
        jumlahLabelHeader = findViewById(R.id.jumlahLabelHeader);
        sumMouldingLabel = findViewById(R.id.sumMouldingLabel);
        sumCCLabel = findViewById(R.id.sumCCLabel);
        sumSandingLabel = findViewById(R.id.sumSandingLabel);
        sumPackingLabel = findViewById(R.id.sumPackingLabel);
        borderTop = findViewById(R.id.borderTop);
        borderBottom = findViewById(R.id.borderBottom);
        borderLeft = findViewById(R.id.borderLeft);
        borderRight = findViewById(R.id.borderRight);
        btnHistorySave = findViewById(R.id.btnHistorySave);
        loadingIndicatorNoMoulding = findViewById(R.id.loadingIndicatorNoMoulding);
        loadingIndicatorNoCC = findViewById(R.id.loadingIndicatorNoCC);
        loadingIndicatorNoSanding = findViewById(R.id.loadingIndicatorNoSanding);
        loadingIndicatorNoPacking = findViewById(R.id.loadingIndicatorNoPacking);
        btnInputManual = findViewById(R.id.btnInputManual);
        textScanQR = findViewById(R.id.textScanQR);

        loadingIndicator.setVisibility(View.VISIBLE);


        // Inisialisasi View scanner overlay
        scannerOverlay = findViewById(R.id.scannerOverlay);

        // Dapatkan DisplayMetrics untuk tinggi layar
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        // Mulai animasi scanner menggunakan ScannerAnimationUtils
        ScannerAnimationUtils.startScanningAnimation(scannerOverlay, displayMetrics);


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

        btnInputManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noProduksi = noProduksiView.getText().toString();

                if (!noProduksi.isEmpty()) {
                    // Membuat layout untuk dialog
                    LinearLayout layout = new LinearLayout(ProsesProduksiPacking.this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(50, 50, 50, 50);
                    layout.setGravity(Gravity.CENTER_HORIZONTAL);

                    // Membuat TextInputLayout untuk EditText yang lebih modern
                    TextInputLayout textInputLayout = new TextInputLayout(ProsesProduksiPacking.this);
                    textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_FILLED);

                    // Membuat EditText di dalam TextInputLayout
                    final EditText inputNoLabelManual = new EditText(ProsesProduksiPacking.this);  // Nama diganti menjadi inputNoLabelManual
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
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ProsesProduksiPacking.this);
                    builder.setTitle("Input Label")
                            .setView(layout)
                            .setBackground(ContextCompat.getDrawable(ProsesProduksiPacking.this, R.drawable.tooltip_background))
                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String result = inputNoLabelManual.getText().toString();  // Menggunakan inputNoLabelManual
                                    if (!result.isEmpty()) {
                                        // Panggil metode yang sama dengan hasil scan
                                        addScanResultToTable(result);
                                    } else {
                                        Toast.makeText(ProsesProduksiPacking.this, "Kode tidak boleh kosong", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ProsesProduksiPacking.this, "Kode tidak boleh kosong", Toast.LENGTH_SHORT).show();
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
            dataList = ProductionApi.getProductionData("PackingProduksi_h");

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
                Log.e("ProsesProduksiPacking", "Error initializing camera provider: " + e.getMessage());
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
                if (selectedRow != null) {
                    int previousRowIndex = (int) selectedRow.getTag();
                    if (previousRowIndex % 2 == 0) {
                        selectedRow.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
                    } else {
                        selectedRow.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                    }
                    resetTextColor(selectedRow); // Kembalikan warna teks ke hitam
                }

                // Tandai baris yang baru dipilih
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.primary)); // Warna penandaan
                setTextColor(row, R.color.white); // Ubah warna teks menjadi putih
                selectedRow = row;

                // Simpan data yang dipilih
                selectedProductionData = data;

                // Tangani aksi tambahan
                onRowClick(data);
            });


            tableLayout.addView(row);
            rowIndex++; // Tingkatkan indeks
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

        // Data tabel
        for (String noMoulding : noMouldingList) {
            TableRow row = new TableRow(this);

            row.setTag(rowIndex);

            TextView textView = createTextView(noMoulding, 1.0f);
            row.addView(textView);

            row.setOnClickListener(view -> fetchDataAndShowTooltip(view, noMoulding, "Moulding_h", "Moulding_d", "NoMoulding"));

            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream)); // Warna untuk baris genap
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white)); // Warna untuk baris ganjil
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

        // Data tabel
        for (String noCC : noCCList) {
            TableRow row = new TableRow(this);

            row.setTag(rowIndex);

            TextView textView = createTextView(noCC, 1.0f);
            row.addView(textView);
            row.setOnClickListener(view -> fetchDataAndShowTooltip(view, noCC, "CCAkhir_h", "CCAkhir_d", "NoCCAkhir"));

            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream)); // Warna untuk baris genap
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white)); // Warna untuk baris ganjil
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

        // Data tabel
        for (String noSanding : noSandingList) {
            TableRow row = new TableRow(this);

            row.setTag(rowIndex);

            TextView textView = createTextView(noSanding, 1.0f);
            row.addView(textView);
            row.setOnClickListener(view -> fetchDataAndShowTooltip(view, noSanding, "Sanding_h", "Sanding_d", "NoSanding"));

            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream)); // Warna untuk baris genap
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white)); // Warna untuk baris ganjil
            }

            noSandingTableLayout.addView(row);
            rowIndex++;
        }
    }

    private void populateNoPackingTable(List<String> noPackingList) {
        noPackingTableLayout.removeAllViews();
        int rowIndex = 0;

        if (noPackingList == null || noPackingList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Tidak ada Data");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            noPackingTableLayout.addView(noDataView);
            return;
        }

        // Data tabel
        for (String noPacking : noPackingList) {
            TableRow row = new TableRow(this);

            row.setTag(rowIndex);

            TextView textView = createTextView(noPacking, 1.0f);
            row.addView(textView);
            row.setOnClickListener(view -> fetchDataAndShowTooltip(view, noPacking, "BarangJadi_h", "BarangJadi_d", "NoBJ"));

            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream)); // Warna untuk baris genap
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white)); // Warna untuk baris ganjil
            }

            noPackingTableLayout.addView(row);
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

                // Filter prefix:
                if (!result.startsWith("T") && !result.startsWith("V")  && !result.startsWith("W")  && !result.startsWith("I")) {
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

                    if (ProductionApi.isDataExists(result, config.tableNameH, config.tableNameD, config.columnName)) {
                        if (ProductionApi.isDateUsageNull(result, config.tableNameH, config.columnName)) {
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
        if (ProductionApi.isDateValid(noProduksi, "PackingProduksi_h", result, config.tableNameH, config.columnName)) {
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

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Log.d("SaveScannedResults", "Memulai proses penyimpanan hasil scan ke database");

            int totalItems = noMouldingList.size() + noCCList.size() + noSandingList.size() + noPackingList.size();
            int savedItems = 0;

            // Proses penyimpanan untuk tabel Moulding
            if (!noMouldingList.isEmpty()) {
                List<String> existingNoMoulding = ProductionApi.getNoMouldingByNoProduksi(noProduksi, "PackingProduksiInputMoulding");
                List<String> newNoMoulding = new ArrayList<>(noMouldingList);
                newNoMoulding.removeAll(existingNoMoulding);
                ProductionApi.saveNoMoulding(noProduksi, tglProduksi, newNoMoulding, dateTimeSaved, "PackingProduksiInputMoulding");
                savedItems += newNoMoulding.size();
                int progress = (savedItems * 100) / totalItems;
                runOnUiThread(() -> customProgressDialog.updateProgress(progress));
            }

            // Proses penyimpanan untuk tabel CC
            if (!noCCList.isEmpty()) {
                List<String> existingNoCC = ProductionApi.getNoCCByNoProduksi(noProduksi, "PackingProduksiInputCCAkhir");
                List<String> newNoCC = new ArrayList<>(noCCList);
                newNoCC.removeAll(existingNoCC);
                ProductionApi.saveNoCC(noProduksi, tglProduksi, newNoCC, dateTimeSaved, "PackingProduksiInputCCAkhir");
                savedItems += newNoCC.size();
                int progress = (savedItems * 100) / totalItems;
                runOnUiThread(() -> customProgressDialog.updateProgress(progress));
            }

            // Proses penyimpanan untuk tabel Sanding
            if (!noSandingList.isEmpty()) {
                List<String> existingNoSanding = ProductionApi.getNoSandingByNoProduksi(noProduksi, "PackingProduksiInputSanding");
                List<String> newNoSanding = new ArrayList<>(noSandingList);
                newNoSanding.removeAll(existingNoSanding);
                ProductionApi.saveNoSanding(noProduksi, tglProduksi, newNoSanding, dateTimeSaved, "PackingProduksiInputSanding");
                savedItems += newNoSanding.size();
                int progress = (savedItems * 100) / totalItems;
                runOnUiThread(() -> customProgressDialog.updateProgress(progress));
            }

            // Proses penyimpanan untuk tabel Packing
            if (!noPackingList.isEmpty()) {
                List<String> existingNoPacking = ProductionApi.getNoPackingByNoProduksi(noProduksi, "PackingProduksiInputBarangJadi");
                List<String> newNoPacking = new ArrayList<>(noPackingList);
                newNoPacking.removeAll(existingNoPacking);
                ProductionApi.saveNoPacking(noProduksi, tglProduksi, newNoPacking, dateTimeSaved, "PackingProduksiInputBarangJadi");
                savedItems += newNoPacking.size();
                int progress = (savedItems * 100) / totalItems;
                runOnUiThread(() -> customProgressDialog.updateProgress(progress));
            }

            ProductionApi.saveRiwayat(savedUsername, dateTimeSaved, "Mengubah Data " + noProduksi + " Pada Proses Produksi Packing (Mobile)");

            // Kosongkan semua list setelah penyimpanan berhasil
            noS4SList.clear();
            noMouldingList.clear();
            noFJList.clear();
            noCCList.clear();
            noLaminatingList.clear();
            noSandingList.clear();
            noPackingList.clear();
            scannedResults.clear();

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

            Log.d("SaveScannedResults", "Proses penyimpanan hasil scan selesai");
        });
    }


//------------------------------------------------------------------------------------------------------------------------------------------------------//
//-------------------------------------------HISTORY METHOD------------------------- -------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------------------------------------------------------------//

    private void showHistoryDialog(String noProduksi) {
        executorService.execute(() -> {
            String filterQuery =
                            "SELECT 'Moulding' AS Label, NoMoulding AS KodeLabel, DateTimeSaved FROM PackingProduksiInputMoulding WHERE NoProduksi = ? " +
                            "UNION ALL " +
                            "SELECT 'CrossCut' AS Label, NoCCAkhir AS KodeLabel, DateTimeSaved FROM PackingProduksiInputCCAkhir WHERE NoProduksi = ? " +
                            "UNION ALL " +
                            "SELECT 'Sanding' AS Label, NoSanding AS KodeLabel, DateTimeSaved FROM PackingProduksiInputSanding WHERE NoProduksi = ? " +
                            "UNION ALL " +
                            "SELECT 'Packing' AS Label, NoBJ AS KodeLabel, DateTimeSaved FROM PackingProduksiInputBarangJadi WHERE NoProduksi = ? " ;


            // 1. Ambil data history dari API
            List<HistoryItem> historyGroups = ProductionApi.getHistoryItems(noProduksi, filterQuery, 4);

            // 2. Siapkan dan proses data (di latar belakang)
            HistorySummary summary = prepareHistorySummary(historyGroups);

            // 3. Tampilkan dialog di UI thread
            runOnUiThread(() -> showHistoryDialogUI(summary, historyGroups));
        });
    }

    private HistorySummary prepareHistorySummary(List<HistoryItem> historyGroups) {
        int totalS4S = 0;
        int totalMoulding = 0;
        int totalFJ = 0;
        int totalCCAkhir = 0;
        int totalLaminating = 0;
        int totalSanding = 0;
        int totalPacking = 0;

        for (HistoryItem group : historyGroups) {
            totalS4S += group.getTotalS4S();
            totalMoulding += group.getTotalMoulding();
            totalFJ += group.getTotalFJ();
            totalCCAkhir += group.getTotalCrossCut();
            totalLaminating += group.getTotalLaminating();
            totalSanding += group.getTotalSanding();
            totalPacking += group.getTotalPacking();
        }

        // Kembalikan hasil summary
        return new HistorySummary(totalS4S, totalMoulding, totalFJ, totalCCAkhir, totalLaminating, totalSanding, totalPacking);
    }
    public class HistorySummary {
        public int totalS4S, totalMoulding, totalFJ, totalCCAkhir, totalLaminating, totalSanding, totalPacking;

        public HistorySummary(int totalS4S, int totalMoulding, int totalFJ, int totalCCAkhir, int totalLaminating, int totalSanding, int totalPacking) {
            this.totalS4S = totalS4S;
            this.totalMoulding = totalMoulding;
            this.totalFJ = totalFJ;
            this.totalCCAkhir = totalCCAkhir;
            this.totalLaminating = totalLaminating;
            this.totalSanding = totalSanding;
            this.totalPacking = totalPacking;
        }

        public int getTotalAllLabels() {
            return totalS4S + totalMoulding + totalFJ + totalCCAkhir + totalLaminating + totalSanding + totalPacking;
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
        TextView tvSumMoulding = dialogView.findViewById(R.id.tvSumMoulding);
        TextView tvSumFJ = dialogView.findViewById(R.id.tvSumFJ);
        TextView tvSumCCAkhir = dialogView.findViewById(R.id.tvSumCCAkhir);
        TextView tvSumLaminating = dialogView.findViewById(R.id.tvSumLaminating);
        TextView tvSumSanding = dialogView.findViewById(R.id.tvSumSanding);
        TextView tvSumPacking = dialogView.findViewById(R.id.tvSumPacking);
        TextView tvSumLabel = dialogView.findViewById(R.id.tvSumLabel);

        // Tambahkan data ke historyContainer
        populateHistoryItems(historyGroups, historyContainer, inflater);

        // Tampilkan jumlah masing-masing label
        tvSumS4S.setText(String.valueOf(summary.totalS4S));
        tvSumMoulding.setText(String.valueOf(summary.totalMoulding));
        tvSumFJ.setText(String.valueOf(summary.totalFJ));
        tvSumCCAkhir.setText(String.valueOf(summary.totalCCAkhir));
        tvSumLaminating.setText(String.valueOf(summary.totalLaminating));
        tvSumSanding.setText(String.valueOf(summary.totalSanding));
        tvSumPacking.setText(String.valueOf(summary.totalPacking));
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


//------------------------------------------------------------------------------------------------------------------------------------------------------//
//-------------------------------------------TOOLTIP METHOD------------------------- -------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------------------------------------------------------------//

    // Mengambil data tooltip dan menampilkan tooltip
    private void fetchDataAndShowTooltip(View anchorView, String noLabel, String tableH, String tableD, String mainColumn) {
        executorService.execute(() -> {
            // Ambil data tooltip menggunakan ProductionApi
            TooltipData tooltipData = ProductionApi.getTooltipData(noLabel, tableH, tableD, mainColumn);

            // Pindahkan eksekusi ke UI thread untuk menampilkan tooltip
            runOnUiThread(() -> {
                if (tooltipData != null) {
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
                            tableH
                    );
                } else {
                    // Tampilkan pesan error jika data tidak ditemukan
                    Toast.makeText(this, "Error fetching tooltip data", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


    private void showTooltip(View anchorView, String noLabel, String formattedDateTime, String jenis, String spkDetail, String spkAsalDetail, String namaGrade, boolean isLembur, List<String[]> tableData, int totalPcs, double totalM3, String tableH) {
        // Inflate layout tooltip
        View tooltipView = LayoutInflater.from(this).inflate(R.layout.tooltip_layout, null);

        // Set data pada TextView
        ((TextView) tooltipView.findViewById(R.id.tvNoLabel)).setText(noLabel);
        ((TextView) tooltipView.findViewById(R.id.tvDateTime)).setText(formattedDateTime);
        ((TextView) tooltipView.findViewById(R.id.tvJenis)).setText(jenis);
        ((TextView) tooltipView.findViewById(R.id.tvNoSPK)).setText(spkDetail);
        ((TextView) tooltipView.findViewById(R.id.tvNoSPKAsal)).setText(spkAsalDetail);
        ((TextView) tooltipView.findViewById(R.id.tvNamaGrade)).setText(namaGrade);
        ((TextView) tooltipView.findViewById(R.id.tvIsLembur)).setText(isLembur ? "Yes" : "No");

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


//------------------------------------------------------------------------------------------------------------------------------------------------------//
//-------------------------------------------HELPER METHOD-------------------------- -------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------------------------------------------------------------//

    private void showExitConfirmationDialog() {
        // Inflate layout custom_dialog_layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.custom_dialog_confirmation_layout, null);

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
        loadingIndicatorNoMoulding.setVisibility(visibility);
        loadingIndicatorNoCC.setVisibility(visibility);
        loadingIndicatorNoSanding.setVisibility(visibility);
        loadingIndicatorNoPacking.setVisibility(visibility);
    }

    private void setAllTableLayoutsVisibility(int visibility) {
        noMouldingTableLayout.setVisibility(visibility);
        noCCTableLayout.setVisibility(visibility);
        noSandingTableLayout.setVisibility(visibility);
        noPackingTableLayout.setVisibility(visibility);
    }

    private void clearAllDataLists() {
        noS4SList.clear();
        noMouldingList.clear();
        noFJList.clear();
        noCCList.clear();
        noLaminatingList.clear();
        noSandingList.clear();
        noPackingList.clear();
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
            noMouldingList = ProductionApi.getNoMouldingByNoProduksi(noProduksi, "PackingProduksiInputMoulding");
            noCCList = ProductionApi.getNoCCByNoProduksi(noProduksi, "PackingProduksiInputCCAkhir");
            noSandingList = ProductionApi.getNoSandingByNoProduksi(noProduksi, "PackingProduksiInputSanding");
            noPackingList = ProductionApi.getNoPackingByNoProduksi(noProduksi, "PackingProduksiInputBarangJadi");

            // Perbarui UI di thread utama
            runOnUiThread(() -> {
                // Set data ke TextView
                noProduksiView.setText(noProduksi);
                setDateToView(tglProduksi, tglProduksiView);
                mesinProduksiView.setText(mesinProduksi);

                // Populate semua tabel dan sembunyikan loading indikator
                updateTable(noMouldingList, sumMouldingLabel, loadingIndicatorNoMoulding, noMouldingTableLayout, this::populateNoMouldingTable);
                updateTable(noCCList, sumCCLabel, loadingIndicatorNoCC, noCCTableLayout, this::populateNoCCTable);
                updateTable(noSandingList, sumSandingLabel, loadingIndicatorNoSanding, noSandingTableLayout, this::populateNoSandingTable);
                updateTable(noPackingList, sumPackingLabel, loadingIndicatorNoPacking, noPackingTableLayout, this::populateNoPackingTable);
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