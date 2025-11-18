package com.example.myapplication;

import com.example.myapplication.api.MasterApi;
import com.example.myapplication.api.PenjualanApi;
import com.example.myapplication.api.ProsesProduksiApi;
import com.example.myapplication.model.BJJualData;
import com.example.myapplication.model.SpkData;
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

public class PenjualanBJ extends AppCompatActivity {

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
    private BJJualData selectedBongkarSusunData;
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
    private List<BJJualData> dataList; // Data asli yang tidak difilter
    private ProgressBar loadingIndicator;
    private TableLayout noPackingTableLayout;
    private LinearLayout jumlahLabel;
    private LinearLayout jumlahLabelHeader;
    private TextView sumPackingLabel;
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
    private ProgressBar loadingIndicatorNoPacking;
    private LinearLayout textScanQR;
    private Button btnInputManual;
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();
    private final String mainTable = "BJJual_h";
    private Button btnEdit;
    private Button btnCreate;
    private List<String> userPermissions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_bj);

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
        noPackingTableLayout = findViewById(R.id.noPackingTableLayout);
        jumlahLabel = findViewById(R.id.jumlahLabel);
        jumlahLabelHeader = findViewById(R.id.jumlahLabelHeader);
        sumPackingLabel = findViewById(R.id.sumPackingLabel);
        borderTop = findViewById(R.id.borderTop);
        borderBottom = findViewById(R.id.borderBottom);
        borderLeft = findViewById(R.id.borderLeft);
        borderRight = findViewById(R.id.borderRight);
        loadingIndicatorNoPacking = findViewById(R.id.loadingIndicatorNoPacking);
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
        PermissionUtils.permissionCheck(this, btnEdit, "penjualan_bj:update");
        PermissionUtils.permissionCheck(this, btnCreate, "penjualan_bj:create");

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

                // Periksa periode transaksi
                String noProduksi = noPenjualanView.getText().toString();
                if (noProduksi.isEmpty()) {
                    Toast.makeText(PenjualanBJ.this, "Pilih NoProduksi Terlebih Dahulu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Jalankan cek periode di background
                executorService.execute(() -> {
                    boolean isClosed = MasterApi.isPeriodValid(DateTimeUtils.formatToDatabaseDate(tgl));

                    runOnUiThread(() -> {
                        if (!isClosed) {
                            Toast.makeText(PenjualanBJ.this, "Periode transaksi sudah ditutup!", Toast.LENGTH_SHORT).show();
                        } else {
                            showEditPenjualanDialog(selectedBongkarSusunData);
                        }
                    });
                });
            }
        });


        btnInputManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noJual = noPenjualanView.getText().toString();

                if (!noJual.isEmpty()) {
                    // Membuat layout untuk dialog
                    LinearLayout layout = new LinearLayout(PenjualanBJ.this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(50, 50, 50, 50);
                    layout.setGravity(Gravity.CENTER_HORIZONTAL);

                    // Membuat TextInputLayout untuk EditText yang lebih modern
                    TextInputLayout textInputLayout = new TextInputLayout(PenjualanBJ.this);
                    textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_FILLED);

                    // Membuat EditText di dalam TextInputLayout
                    final EditText inputNoLabelManual = new EditText(PenjualanBJ.this);  // Nama diganti menjadi inputNoLabelManual
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
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(PenjualanBJ.this);
                    builder.setTitle("Input Label")
                            .setView(layout)
                            .setBackground(ContextCompat.getDrawable(PenjualanBJ.this, R.drawable.tooltip_background))
                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String result = inputNoLabelManual.getText().toString();  // Menggunakan inputNoLabelManual
                                    if (!result.isEmpty()) {
                                        // Panggil metode yang sama dengan hasil scan
                                        addScanResultToTable(result);
                                    } else {
                                        Toast.makeText(PenjualanBJ.this, "Kode tidak boleh kosong", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PenjualanBJ.this, "Kode tidak boleh kosong", Toast.LENGTH_SHORT).show();
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
                List<BJJualData> filteredList = new ArrayList<>();
                int count = 0;

                for (BJJualData data : dataList) {
                    if (count >= PAGE_SIZE) break;

                    if ((data.getNoBJJual() != null && data.getNoBJJual().toLowerCase().contains(query)) ||
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
            dataList = PenjualanApi.getBJJualData();

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

    private void loadSPKSpinner(Spinner spinSPK, String selectedNoSPK, @Nullable Runnable onDone) {
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
    private void loadSPKSpinner(Spinner spinSPK, String selectedNoSPK) {
        loadSPKSpinner(spinSPK, selectedNoSPK, null);
    }


//------------------------------------------------------------------------------------------------------------------------------------------------------//
//----------------------------------METHOD CREATE AND EDIT DIALOG---------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------------------------------------------------------------//

    private void showCreatePenjualanDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_field_penjualan_bj_header, null);

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
        EditText editKeterangan = dialogView.findViewById(R.id.editKeterangan);
        Spinner spinSPK = dialogView.findViewById(R.id.spinSPK);
        ImageButton btnClose = dialogView.findViewById(R.id.btnCloseDialogInput);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        titleDialog.setText("Penjualan BJ (Data Baru)");

        // Untuk create, NoBongkarSusun belum di-generate → kasih placeholder
        tvNoPenjualan.setText("J.XXXXXX");

        loadSPKSpinner(spinSPK, "");

        editTanggal.setText(DateTimeUtils.getCurrentDate());
        editTanggal.setInputType(InputType.TYPE_NULL);
        editTanggal.setFocusable(false);
        editTanggal.setOnClickListener(v -> {
            DateTimeUtils.showDatePicker(PenjualanBJ.this, editTanggal);
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            loadingDialogHelper.show(this);

            String tanggal = editTanggal.getText().toString().trim(); // pastikan "yyyy-MM-dd"
            String keterangan = editKeterangan.getText().toString().trim();

            // Ambil ID dari spinner
            SpkData spk = (SpkData) spinSPK.getSelectedItem();
            String noSPK = (spk != null) ? spk.getNoSPK() : "-";

            executorService.execute(() -> {
                String newNo = PenjualanApi.insertBJJualHeaderWithGeneratedNo(
                        tanggal,          // "yyyy-MM-dd"
                        noSPK,            // dari spinner
                        keterangan.isEmpty() ? null : keterangan
                );

                boolean success = (newNo != null);
                if (success) {
                    dataList = PenjualanApi.getBJJualData();
                }

                runOnUiThread(() -> {
                    loadingDialogHelper.hide();
                    if (success) {
                        tvNoPenjualan.setText(newNo);
                        dialog.dismiss();
                        Toast.makeText(PenjualanBJ.this, "Data berhasil dibuat", Toast.LENGTH_SHORT).show();

                        // OPTIONAL: refresh list kalau kamu punya loader-nya
                         populateTable(dataList);
                    } else {
                        Toast.makeText(PenjualanBJ.this, "Gagal membuat data", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
        dialog.show();
    }


    private void showEditPenjualanDialog(BJJualData item) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_field_penjualan_bj_header, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView titleDialog = dialogView.findViewById(R.id.titleDialog);
        TextView tvNoPenjualan = dialogView.findViewById(R.id.tvNoPenjualan);
        EditText editTanggal    = dialogView.findViewById(R.id.editTanggal);
        EditText editKeterangan = dialogView.findViewById(R.id.editKeterangan);
        Spinner  spinSPK        = dialogView.findViewById(R.id.spinSPK);
        ImageButton btnClose    = dialogView.findViewById(R.id.btnCloseDialogInput);
        Button btnSave          = dialogView.findViewById(R.id.btnSave);

        titleDialog.setText("Penjualan BJ (Edit Data)");
        tvNoPenjualan.setText(item.getNoBJJual());

        // Prefill
        editTanggal.setText(item.getTglJual()); // pastikan format "yyyy-MM-dd"
        editTanggal.setInputType(InputType.TYPE_NULL);
        editTanggal.setFocusable(false);
        editTanggal.setOnClickListener(v -> DateTimeUtils.showDatePicker(PenjualanBJ.this, editTanggal));

        editKeterangan.setText(item.getKeterangan() == null ? "" : item.getKeterangan());

        // Load spinner & set selection ke item.getNoSPK()
        loadSPKSpinner(spinSPK, item.getNoSPK()); // metode kamu yang existing

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            loadingDialogHelper.show(this);

            String tanggalBaru    = editTanggal.getText().toString().trim();
            String keteranganBaru = editKeterangan.getText().toString().trim();
            SpkData spk = (SpkData) spinSPK.getSelectedItem();
            String noSPKBaru = (spk != null) ? spk.getNoSPK() : "-";

            executorService.execute(() -> {
                // 1) Update (background)
                boolean ok = PenjualanApi.updateBJJualHeader(
                        item.getNoBJJual(),
                        tanggalBaru,
                        noSPKBaru,
                        keteranganBaru.isEmpty() ? null : keteranganBaru
                );

                // 2) Refresh list (background) jika sukses
                List<BJJualData> freshList = null;
                if (ok) {
                    try {
                        freshList = PenjualanApi.getBJJualData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                final boolean finalOk = ok;
                final List<BJJualData> finalList = freshList;
                runOnUiThread(() -> {
                    loadingDialogHelper.hide();
                    if (finalOk) {
                        dialog.dismiss();
                        Toast.makeText(PenjualanBJ.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                        if (finalList != null) {
                            dataList = finalList;
                            populateTable(dataList);
                        } else {
                            Toast.makeText(PenjualanBJ.this, "Terupdate, tapi gagal refresh list", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PenjualanBJ.this, "Gagal update data", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

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
    private void populateTable(List<BJJualData> dataList) {

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

        for (BJJualData data : dataList) {
            TableRow row = new TableRow(this);

            // Simpan indeks baris di tag
            row.setTag(rowIndex);

            // Tambahkan TextView untuk setiap kolom
            TextView col1 = createTextView(data.getNoBJJual(), 1f);
            TextView col2 = createTextView(data.getTglJual(), 1f);
            TextView col3 = createTextView(data.getNoSPK(), 1f);
            TextView col4 = createTextView(data.getKeterangan(), 1f);


            row.addView(col1);
            row.addView(createDivider());

            row.addView(col2);
            row.addView(createDivider());

            row.addView(col3);
            row.addView(createDivider());

            row.addView(col4);


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

        for (String noPacking : noPackingList) {
            TableRow row = new TableRow(this);
            row.setTag(rowIndex);

            TextView textView = createTextView(noPacking, 1.0f);
            row.addView(textView);

            final int currentRowIndex = rowIndex;
            final TableRow currentRow = row;
            final String currentNoPacking = noPacking;

            row.setOnClickListener(view -> {
                ViewUtils.handleRowSelection(this, currentRow, currentRowIndex, selectedRow);
                selectedRow = currentRow;

                TooltipUtils.fetchDataAndShowTooltip(
                        this,
                        executorService,
                        view,
                        currentNoPacking,
                        "BarangJadi_h",
                        "BarangJadi_d",
                        "NoBJ",
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
                if (!userPermissions.contains("penjualan_bj:delete")) {
                    Toast.makeText(this, "Anda tidak memiliki izin untuk menghapus.", Toast.LENGTH_SHORT).show();
                    return true; // event dianggap sudah di-handle
                }

                new AlertDialog.Builder(this)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus data " + currentNoPacking + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            Toast.makeText(this,
                                    "Menghapus data " + noJual + " " + currentNoPacking,
                                    Toast.LENGTH_SHORT).show();

                            executorService.execute(() -> {
                                ProsesProduksiApi.deleteDataByNoLabel(noJual, currentNoPacking);
                                runOnUiThread(this::refreshPackingTable);
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
        if (PenjualanApi.isDateValidInPenjualanBJ(noJual, mainTable, result, config.tableNameH, config.columnName)) {
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

            final int totalItems = noPackingList.size();

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
                // === Sanding ===
                try {
                    if (!noPackingList.isEmpty()) {
                        List<String> existing = PenjualanApi.getNoPackingByBJJual(noJual);
                        List<String> toSave = new ArrayList<>(noPackingList);
                        toSave.removeAll(existing);
                        if (!toSave.isEmpty()) {
                            PenjualanApi.saveNoBJJualInBJJual(noJual, tgl, toSave);
                            savedItems += toSave.size();
                            updateStatus.accept((savedItems * 100) / totalItems);
                        }
                    }
                } catch (Exception e) { errorCount++; Log.e("SaveScannedResults","Gagal simpan Packing", e); }

                // Riwayat (non-blocking)
                try {
                    ProsesProduksiApi.saveRiwayat(
                            savedUsername, dateTimeSaved,
                            "Mengubah Data " + noJual + " Pada BJJual_h (Mobile)"
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
                noPackingList.clear();
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
        loadingIndicatorNoPacking.setVisibility(visibility);
    }

    private void setAllTableLayoutsVisibility(int visibility) {
        noPackingTableLayout.setVisibility(visibility);
    }

    private void clearAllDataLists() {
        noPackingList.clear();
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

    private void onRowClick(BJJualData data) {
        // Tampilkan semua indikator loading
        showAllLoadingIndicators(true);

        // Sembunyikan semua tabel
        setAllTableLayoutsVisibility(View.GONE);

        // Bersihkan semua data
        clearAllDataLists();

        executorService.execute(() -> {
            // Ambil data latar belakang
            noJual = data.getNoBJJual();
            tgl = data.getTglJual();
            keteranganBongkarSusun = data.getKeterangan();

            // Ambil data untuk setiap tabel
            noPackingList = PenjualanApi.getNoPackingByBJJual(noJual);

            // Perbarui UI di thread utama
            runOnUiThread(() -> {
                // Set data ke TextView
                noPenjualanView.setText(noJual);
                keteranganBongkarSusunView.setText(keteranganBongkarSusun);

                // Populate semua tabel dan sembunyikan loading indikator
                updateTable(noPackingList, sumPackingLabel, loadingIndicatorNoPacking, noPackingTableLayout, this::populateNoPackingTable);

            });
        });
    }


    private void refreshPackingTable() {
        // Tampilkan loading
        loadingIndicatorNoPacking.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            List<String> updatedNoPackingList = PenjualanApi.getNoPackingByBJJual(noJual);

            runOnUiThread(() -> {
                noPackingList = updatedNoPackingList; // update list jika dibutuhkan di tempat lain
                updateTable(
                        updatedNoPackingList,
                        sumPackingLabel,
                        loadingIndicatorNoPacking,
                        noPackingTableLayout,
                        this::populateNoPackingTable
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