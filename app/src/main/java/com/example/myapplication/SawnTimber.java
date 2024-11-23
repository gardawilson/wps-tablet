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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class SawnTimber extends AppCompatActivity {

    private Button BtnSimpanST;
    private Button BtnBatalST;
    private Button BtnDataBaruST;
    private Button BtnSearchST;
    private Button BtnHapusDetailST;
    private Button BtnPrintST;
    private Button BtnTambahStickST;
    private Button BtnHapusStickST;
    private Button BtnHapusSemuaStickST;
    private EditText NoST;
    private EditText NoKayuBulat;
    private EditText Supplier;
    private EditText NoTruk;
    private EditText NoPlatTruk;
    private EditText NoSuket;
    private EditText TglStickBundel;
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
    private boolean isDataBaruClickedST = false;
    private int currentTableNo = 1;
    private TableLayout Tabel;
    private int rowCount = 0;
    private TableLayout Tabel2;
    private CheckBox CBKering;
    private CheckBox CBStick;
    private CheckBox CBUpah;
    private Button BtnInputDetailST;
    private RadioGroup radioGroupUOMTblLebar;
    private RadioButton radioMillimeter;
    private RadioButton radioInch;
    private RadioButton radioCentimeter;
    private RadioGroup radioBagusKulit;
    private RadioButton radioBagus;
    private RadioButton radioKulit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sawn_timber);

        BtnSearchST = findViewById(R.id.BtnSearchST);
        BtnBatalST = findViewById(R.id.BtnBatalST);
        BtnDataBaruST = findViewById(R.id.BtnDataBaruST);
        BtnPrintST = findViewById(R.id.BtnPrintST);
        BtnHapusSemuaStickST = findViewById(R.id.BtnHapusSemuaStickST);
        BtnHapusStickST = findViewById(R.id.BtnHapusStickST);
        BtnTambahStickST = findViewById(R.id.BtnTambahStickST);
        BtnSimpanST = findViewById(R.id.BtnSimpanST);
        BtnInputDetailST = findViewById(R.id.BtnInputDetailST);
        BtnHapusDetailST = findViewById(R.id.BtnHapusDetailST);
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
        radioCentimeter = findViewById(R.id.radioCentimeter);
        radioBagus = findViewById(R.id.radioBagus);
        radioKulit = findViewById(R.id.radioKulit);
        radioBagusKulit = findViewById(R.id.radioGroupBagusKulit);
        M3 = findViewById(R.id.M3ST);
        Ton = findViewById(R.id.Ton);


        BtnSearchST.setOnClickListener(v -> {
            String noKayuBulat = NoKayuBulat.getText().toString();

            if (noKayuBulat.isEmpty()) {
                Toast.makeText(SawnTimber.this, "Masukkan NoKayuBulat terlebih dahulu.", Toast.LENGTH_SHORT).show();
            } else {
                new SearchKayuBulatTask(noKayuBulat).execute();
                new LoadSupplierTask(noKayuBulat).execute();
            }
        });

        BtnDataBaruST.setOnClickListener(v -> {
            if (!isDataBaruClickedST) {
                new LoadSPKTask().execute();
                new LoadTellyTask().execute();
                new LoadStickByTask().execute();
                new LoadJenisKayuTask().execute();
                new LoadGradeStickTask().execute();
                new LoadLokasiTask().execute();
                isDataBaruClickedST = true;
                setCurrentDateTime();
            }
            new SetAndSaveNoSTTask().execute();
            setCurrentDateTime();
            BtnBatalST.setEnabled(true);
            BtnSimpanST.setEnabled(true);
            clearTableData();
        });

        BtnInputDetailST.setOnClickListener(view -> {
            String noST = NoST.getText().toString();

            if (!noST.isEmpty()) {
                addDataDetail(noST);
            } else {
                Toast.makeText(SawnTimber.this, "NoST tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }

            jumlahPcsST();
            m3();
            ton();
        });

        BtnTambahStickST.setOnClickListener(view -> {
            String noST = NoST.getText().toString();
            if (!noST.isEmpty()) {
                addDataGrade(noST);
            } else {
                Toast.makeText(SawnTimber.this, "NoST tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });

        TglStickBundel.setOnClickListener(v -> showDatePickerDialog());

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
                String noST = NoST.getText().toString().trim();

                if (!noST.isEmpty()) {
                    new DeleteDataTask().execute(noST);
                }
                clearTableData();
                resetGradeData();
                resetDetailData();
                BtnSimpanST.setEnabled(false);
            }
        });

        BtnSimpanST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noST = NoST.getText().toString().trim();
                String noKayuBulat = NoKayuBulat.getText().toString().trim();
                JenisKayu selectedJenisKayu = (JenisKayu) SpinKayu.getSelectedItem();
                String jenisKayu = selectedJenisKayu.getIdJenisKayu();
                String noSPK = SpinSPK.getSelectedItem().toString();
                String telly = ((Telly) SpinTelly.getSelectedItem()).getIdOrgTelly();
                String stickBy = ((StickBy) SpinStickBy.getSelectedItem()).getIdStickBy();
                Lokasi selectedLokasi = (Lokasi) SpinLokasi.getSelectedItem();
                String idLokasi = selectedLokasi.getIdLokasi();
                String dateCreate = TglStickBundel.getText().toString().trim();

                if (!validateKayuLatSelection()) {
                    return;
                }

                if (!noST.isEmpty() && !noKayuBulat.isEmpty() && !jenisKayu.isEmpty() && !noSPK.equals("PILIH") && !telly.isEmpty() && !stickBy.isEmpty() && (radioMillimeter.isChecked() || radioInch.isChecked()) && !temporaryDataListDetail.isEmpty() && !temporaryDataListGrade.isEmpty()) {

                    int isBagusKulit = 0;

                    JenisKayu selectedKayu = (JenisKayu) SpinKayu.getSelectedItem();

                    if (selectedKayu != null && selectedKayu.getNamaJenisKayu().toLowerCase().contains("kayu lat")) {
                        isBagusKulit = radioBagus.isChecked() ? 1 : (radioKulit.isChecked() ? 2 : 0);
                    }

                    int isSticked = CBStick.isChecked() ? 1 : 0;
                    int startKering = CBKering.isChecked() ? 1 : 0;

                    new UpdateDataTask().execute(noST, noKayuBulat, jenisKayu, noSPK, telly, stickBy, idLokasi, dateCreate);
                    new UpdateCheckboxDataTask().execute(noST, isBagusKulit, isSticked, startKering);

                    for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                        DataRow dataRow = temporaryDataListDetail.get(i);
                        saveDataToDatabase(noST, i + 1, Double.parseDouble(dataRow.tebal), Double.parseDouble(dataRow.lebar),
                                Double.parseDouble(dataRow.panjang), Integer.parseInt(dataRow.pcs));
                    }

                    for (int i = 0; i < temporaryDataListGrade.size(); i++) {
                        DataRow2 dataRow2 = temporaryDataListGrade.get(i);
                        saveDataToDatabase2(noST, dataRow2.gradeId, dataRow2.jumlah);
                    }

//                    BtnSimpanST.setEnabled(false);
//                    BtnBatalST.setEnabled(false);
//                    BtnPrintST.setEnabled(true);
//                    clearTableData();
//                    resetDetailData();
//                    resetGradeData();

                    Toast.makeText(SawnTimber.this, "Data berhasil disimpan.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SawnTimber.this, "Semua field harus diisi.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SpinKayu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JenisKayu selectedJenisKayu = (JenisKayu) parent.getItemAtPosition(position);

                if (selectedJenisKayu.getIsUpah() == 1) {
                    CBUpah.setChecked(true);
                } else {
                    CBUpah.setChecked(false);
                }

                if (selectedJenisKayu != null && selectedJenisKayu.getNamaJenisKayu().toLowerCase().contains("kayu lat")) {

                    radioBagus.setEnabled(true);
                    radioKulit.setEnabled(true);

                } else {
                    radioBagus.setEnabled(false);
                    radioKulit.setEnabled(false);
                    radioBagus.setChecked(false);
                    radioKulit.setChecked(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                CBUpah.setChecked(false);
                radioBagus.setEnabled(false);
                radioKulit.setEnabled(false);
                radioBagus.setChecked(false);
                radioKulit.setChecked(false);
            }
        });

        BtnHapusStickST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noST = NoST.getText().toString().trim();
                if(!noST.isEmpty()) {
                    resetGradeData(); // Tambahkan pemanggilan fungsi reset
                } else {
                    Toast.makeText(SawnTimber.this, "NoST tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        BtnHapusDetailST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noST = NoST.getText().toString().trim();

                if (!noST.isEmpty()) {
                    resetDetailData();
                    jumlahPcsST();
                } else {
                    Toast.makeText(SawnTimber.this, "NoST tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
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
                    public void onResult(boolean hasBeenPrinted) {
                        try {
                            // Ambil data dari form dengan validasi null
                            String jenisKayu = SpinKayu.getSelectedItem() != null ? SpinKayu.getSelectedItem().toString().trim() : "";
                            String tglStickBundle = TglStickBundel.getText() != null ? TglStickBundel.getText().toString().trim() : "";
                            String tellyBy = SpinTelly.getSelectedItem() != null ? SpinTelly.getSelectedItem().toString().trim() : "";
                            String noSPK = SpinSPK.getSelectedItem() != null ? SpinSPK.getSelectedItem().toString().trim() : "";
                            String stickBy = SpinStickBy.getSelectedItem() != null ? SpinStickBy.getSelectedItem().toString().trim() : "";
                            String platTruk = NoPlatTruk.getText() != null ? NoPlatTruk.getText().toString().trim() : "";
                            String noKayuBulat = NoKayuBulat.getText() != null ? NoKayuBulat.getText().toString().trim() : "";
                            String namaSupplier = Supplier.getText() != null ? Supplier.getText().toString().trim() : "";
                            String noTruk = NoTruk.getText() != null ? NoTruk.getText().toString().trim() : "";
                            String jumlahPcs = JumlahPcsST.getText() != null ? JumlahPcsST.getText().toString().trim() : "";
                            String m3 = M3.getText() != null ? M3.getText().toString().trim() : "";
                            String ton = Ton.getText() != null ? Ton.getText().toString().trim() : "";

                            // Buat PDF dengan parameter hasBeenPrinted
                            Uri pdfUri = createPdf(noST, jenisKayu, tglStickBundle, tellyBy, noSPK, stickBy, platTruk,
                                    temporaryDataListDetail, noKayuBulat, namaSupplier, noTruk, jumlahPcs, m3, ton,
                                    hasBeenPrinted); // Parameter baru untuk watermark

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
                                                    updatePrintStatus(noST);
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
                                    Toast.makeText(SawnTimber.this,
                                            "Error printing: " + e.getMessage(),
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

    }

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

    // Interface untuk callback
    interface HasBeenPrintedCallback {
        void onResult(boolean hasBeenPrinted);
    }

    // Method untuk mengecek status HasBeenPrinted secara asynchronous
    private void checkHasBeenPrinted(String noST, HasBeenPrintedCallback callback) {
        new Thread(() -> {
            boolean hasBeenPrinted = false;
            Connection connection = null;
            try {
                // Mendapatkan koneksi dari method ConnectionClass
                connection = ConnectionClass();
                if (connection != null) {
                    String query = "SELECT HasBeenPrinted FROM ST_h WHERE NoST = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, noST);
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
        if (selectedKayu != null &&
                selectedKayu.getNamaJenisKayu().toLowerCase().contains("kayu lat")) {
            if (!radioBagus.isChecked() && !radioKulit.isChecked()) {
                Toast.makeText(this, "Silahkan pilih (Bagus/Kulit)", Toast.LENGTH_SHORT).show();
                return false;
            }
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
        NoST.setText("");
        NoKayuBulat.setText("");
        Supplier.setText("");
        NoTruk.setText("");
        NoPlatTruk.setText("");
        NoSuket.setText("");
        JenisKayuKB.setText("");
        JumlahStick.setText("");
        radioBagusKulit.clearCheck();
        CBStick.setChecked(false);
        CBKering.setChecked(false);
        CBUpah.setChecked(false);
        SpinKayu.setSelection(0);
        SpinStickBy.setSelection(0);
        SpinSPK.setSelection(0);
        SpinTelly.setSelection(0);

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

                // Hitung ton untuk baris ini
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

        for (int i = 1; i < childCount; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            TextView pcsTextView = (TextView) row.getChildAt(4); // Indeks pcs

            String pcsString = pcsTextView.getText().toString().replace(",", "");
            int pcs = Integer.parseInt(pcsString);
            totalPcs += pcs;
        }
        JumlahPcsST.setText(String.valueOf(totalPcs));
    }


    private void setCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        TglStickBundel.setText(currentDate);
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


    private Uri createPdf(String noST, String jenisKayu, String tglStickBundle, String tellyBy, String noSPK,
                          String stickBy, String platTruk, List<DataRow> temporaryDataListDetail, String noKayuBulat,
                          String namaSupplier, String noTruk, String jumlahPcs, String m3, String ton,
                          boolean hasBeenPrinted) throws IOException {
        // Validasi parameter wajib
        if (noST == null || noST.trim().isEmpty()) {
            throw new IOException("Nomor ST tidak boleh kosong");
        }

        if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
            throw new IOException("Data tidak boleh kosong");
        }

        // Validasi dan set default value untuk parameter opsional
        noKayuBulat = (noKayuBulat != null) ? noKayuBulat.trim() : "-";
        namaSupplier = (namaSupplier != null) ? namaSupplier.trim() : "-";
        noTruk = (noTruk != null) ? noTruk.trim() : "-";
        noST = (noST != null) ? noST.trim() : "-";
        jenisKayu = (jenisKayu != null) ? jenisKayu.trim() : "-";
        tglStickBundle = (tglStickBundle != null) ? tglStickBundle.trim() : "-";
        tellyBy = (tellyBy != null) ? tellyBy.trim() : "-";
        noSPK = (noSPK != null) ? noSPK.trim() : "-";
        stickBy = (stickBy != null) ? stickBy.trim() : "-";
        platTruk = (platTruk != null) ? platTruk.trim() : "-";
        jumlahPcs = (jumlahPcs != null) ? jumlahPcs.trim() : "-";
        m3 = (m3 != null) ? m3.trim() : "-";
        ton = (ton != null) ? ton.trim() : "-";

        Uri pdfUri = null;
        ContentResolver resolver = getContentResolver();
        String fileName = "ST_" + noST + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".pdf";
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
                float baseHeight = 450; // Tinggi dasar untuk elemen non-tabel (header, footer, margin, dll.)
                float rowHeight = 15; // Tinggi rata-rata per baris data
                float totalHeight = baseHeight + (rowHeight * temporaryDataListDetail.size());

                // Tetapkan ukuran halaman dinamis
                Rectangle pageSize = new Rectangle( PageSize.A6.getWidth(), totalHeight);
                pdfDocument.setDefaultPageSize(new PageSize(pageSize));

                Document document = new Document(pdfDocument);
                document.setMargins(0, 5, 0, 5);

                // Header
                Paragraph judul = new Paragraph("LABEL SAWN TIMBER (KG)")
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
                addInfoRow(leftColumn, "Stick By", stickBy, timesNewRoman);
                addInfoRow(leftColumn, "NoST", noST, timesNewRoman);
                addInfoRow(leftColumn, "Jenis Kayu", jenisKayu, timesNewRoman);
                addInfoRow(leftColumn, "NoKB Asal", noKayuBulat + " / " + namaSupplier + " - " + noTruk, timesNewRoman);
                addInfoRow(leftColumn, "No SPK", noSPK, timesNewRoman);

                // Buat tabel untuk kolom kanan
                Table rightColumn = new Table(infoColumnWidths)
                        .setWidth(pageWidth/2 - 5)
                        .setMarginLeft(20)
                        .setBorder(Border.NO_BORDER);

                // Isi kolom kanan
                addInfoRow(rightColumn, "Telly By", tellyBy, timesNewRoman);
                addInfoRow(rightColumn, "Print By", "-", timesNewRoman);
                addInfoRow(rightColumn, "Tanggal", tglStickBundle, timesNewRoman);
                addInfoRow(rightColumn, "Plat Truk", platTruk, timesNewRoman);

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

                // Format bulan
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Paragraph bulanParagraf;
                try {
                    Date date = sdf.parse(tglStickBundle);
                    if (date != null) {
                        SimpleDateFormat monthFormat = new SimpleDateFormat("M", Locale.US);
                        String monthNumber = monthFormat.format(date);

                        float[] containerWidths = {30f, 30f};
                        Table containerTable = new Table(containerWidths)
                                .setMarginLeft(25)
                                .setMarginTop(-35)
                                .setBorder(Border.NO_BORDER);

                        Cell monthCell = new Cell()
                                .setBorder(Border.NO_BORDER)
                                .setPadding(0);

                        Paragraph monthParagraph = new Paragraph(monthNumber)
                                .setFont(timesNewRoman)
                                .setFontSize(50)
                                .setTextAlignment(TextAlignment.RIGHT)
                                .setMarginRight(5);

                        monthCell.add(monthParagraph);

                        Cell aCell = new Cell()
                                .setBorder(Border.NO_BORDER)
                                .setPadding(0);

                        Paragraph aParagraph = new Paragraph("A")
                                .setFont(timesNewRoman)
                                .setFontSize(50)
                                .setTextAlignment(TextAlignment.LEFT)
                                .setMarginLeft(5)
                                .setPaddingTop(10);

                        aCell.add(aParagraph);

                        containerTable.addCell(monthCell);
                        containerTable.addCell(aCell);

                        bulanParagraf = new Paragraph()
                                .add(containerTable);
                    } else {
                        bulanParagraf = new Paragraph(" - A")
                                .setFont(timesNewRoman)
                                .setFontSize(40)
                                .setTextAlignment(TextAlignment.LEFT)
                                .setMarginLeft(40)
                                .setMarginTop(15)
                                .setMarginBottom(15);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    bulanParagraf = new Paragraph(" - A")
                            .setFont(timesNewRoman)
                            .setFontSize(40)
                            .setTextAlignment(TextAlignment.LEFT)
                            .setMarginLeft(40)
                            .setMarginTop(15)
                            .setMarginBottom(15);
                }

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
                for (DataRow row : temporaryDataListDetail) {
                    String tebal = (row.tebal != null) ? row.tebal : "-";
                    String lebar = (row.lebar != null) ? row.lebar : "-";
                    String panjang = (row.panjang != null) ? row.panjang : "-";
                    String pcs = (row.pcs != null) ? row.pcs : "-";

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

                sumTable.addCell(new Cell().add(new Paragraph("Ton")).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));
                sumTable.addCell(new Cell().add(new Paragraph(":")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                sumTable.addCell(new Cell().add(new Paragraph(String.valueOf(ton))).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));

                sumTable.addCell(new Cell().add(new Paragraph("m3")).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));
                sumTable.addCell(new Cell().add(new Paragraph(":")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                sumTable.addCell(new Cell().add(new Paragraph(String.valueOf(m3))).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));

                Paragraph qrCodeID = new Paragraph(noST).setTextAlignment(TextAlignment.LEFT).setFontSize(8).setMargins(0, 0, 0, 24).setFont(timesNewRoman);
                Paragraph qrCodeIDbottom = new Paragraph(noST).setTextAlignment(TextAlignment.RIGHT).setFontSize(8).setMargins(0, 20, 0, 0).setFont(timesNewRoman);

                BarcodeQRCode qrCode = new BarcodeQRCode(noST);
                PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
                Image qrCodeImage = new Image(qrCodeObject).setWidth(75).setHorizontalAlignment(HorizontalAlignment.LEFT).setMargins(-5, 0, 0, 0);

                BarcodeQRCode qrCodeBottom = new BarcodeQRCode(noST);
                PdfFormXObject qrCodeBottomObject = qrCodeBottom.createFormXObject(ColorConstants.BLACK, pdfDocument);
                Image qrCodeBottomImage = new Image(qrCodeBottomObject).setWidth(75).setHorizontalAlignment(HorizontalAlignment.RIGHT).setMargins(-5, 0, 0, 0);

                Paragraph bottomLine = new Paragraph("-----------------------------------------------------------------------------------------------------").setTextAlignment(TextAlignment.CENTER).setFontSize(8).setMargins(0, 0, 0, 15).setFont(timesNewRoman);

                // Tambahkan semua elemen ke dokumen


                document.add(judul);
                if (hasBeenPrinted) {
                    addTextDitheringWatermark(pdfDocument, timesNewRoman);
                }
                document.add(mainTable);
                document.add(table);
                document.add(sumTable);
                document.add(bulanParagraf);
                document.add(qrCodeID);
                document.add(qrCodeImage);
                document.add(bottomLine);
                document.add(qrCodeIDbottom);
                document.add(qrCodeBottomImage);

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

    private class DeleteDataTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String noST = params[0];
            Connection con = ConnectionClass();
            boolean success = false;

            if (con != null) {
                try {
                    Log.d("DeleteDataTask", "Koneksi berhasil. Memulai penghapusan data...");

                    String query = "DELETE FROM dbo.ST_h WHERE NoST = ?";
                    Log.d("DeleteDataTask", "Query yang dieksekusi: " + query);

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noST);

                    int rowsAffected = ps.executeUpdate();
                    Log.d("DeleteDataTask", "Jumlah baris yang dihapus: " + rowsAffected);

                    success = rowsAffected > 0;

                    ps.close();
                    con.close();
                } catch (SQLException e) {
                    Log.e("Database Error", "SQL Error: " + e.getMessage());
                } catch (Exception e) {
                    Log.e("Database Error", "General Error: " + e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return success;
        }
        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(SawnTimber.this, "Data berhasil dihapus.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SawnTimber.this, "Gagal menghapus data.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SawnTimber.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                String selectedDate = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);
                TglStickBundel.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }


    //Fungsi untuk add Data Detail
    private List<DataRow> temporaryDataListDetail = new ArrayList<>();

    private void addDataDetail(String noST) {
        String tebal = DetailTebalST.getText().toString();
        String panjang = DetailPanjangST.getText().toString();
        String lebar = DetailLebarST.getText().toString();
        String pcs = DetailPcsST.getText().toString();

        if (tebal.isEmpty() || panjang.isEmpty() || lebar.isEmpty() || pcs.isEmpty()) {
            Toast.makeText(this, "Isi semua form detail", Toast.LENGTH_SHORT).show();
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
            addTextViewToRowWithWeight(newRow, String.valueOf(++rowCount), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(tebal)), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(lebar)), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(panjang)), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Integer.parseInt(pcs)), 1f);

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
            deleteButton.setTextColor(Color.BLACK);

            // Set listener tombol hapus
            deleteButton.setOnClickListener(v -> {
                Tabel.removeView(newRow);
                temporaryDataListDetail.remove(newDataRow);
                updateRowNumbers();
                jumlahPcsST();
                m3();
                ton();
            });

            newRow.addView(deleteButton);
            Tabel.addView(newRow);

            // Bersihkan field input
            DetailTebalST.setText("");
            DetailPanjangST.setText("");
            DetailLebarST.setText("");
            DetailPcsST.setText("");

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

    private void addDataGrade(String noST) {
        GradeStick selectedGrade = (GradeStick) SpinGrade.getSelectedItem();
        String gradeName = selectedGrade.getNamaGradeStick();
        int gradeId = selectedGrade.getIdGradeStick();
        String jumlah = JumlahStick.getText().toString();

        if (gradeName.isEmpty() || jumlah.isEmpty()) {
            Toast.makeText(this, "Masukkan Jumlah Stick", Toast.LENGTH_SHORT).show();
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

            // ID Grade
            TextView idGradeTextView = new TextView(this);
            idGradeTextView.setText(String.valueOf(gradeId));
            idGradeTextView.setLayoutParams(new TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f));
            idGradeTextView.setGravity(Gravity.CENTER);
            idGradeTextView.setPadding(10, 10, 10, 10);
            newRow.addView(idGradeTextView);

            // Nama Grade
            TextView gradeTextView = new TextView(this);
            gradeTextView.setText(gradeName);
            gradeTextView.setLayoutParams(new TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f));
            gradeTextView.setGravity(Gravity.CENTER);
            gradeTextView.setPadding(10, 10, 10, 10);
            newRow.addView(gradeTextView);

            // Jumlah
            TextView jumlahTextView = new TextView(this);
            jumlahTextView.setText(jumlah);
            jumlahTextView.setLayoutParams(new TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f));
            jumlahTextView.setGravity(Gravity.CENTER);
            jumlahTextView.setPadding(10, 10, 10, 10);
            newRow.addView(jumlahTextView);

            // Tombol Hapus
            Button deleteButton = new Button(this);
            deleteButton.setText("-");
            deleteButton.setTextSize(12);
            TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            buttonParams.setMargins(5, 5, 5, 5);
            deleteButton.setPadding(10, 5, 10, 5);
            deleteButton.setLayoutParams(buttonParams);
            deleteButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            deleteButton.setTextColor(Color.BLACK);


            // Set listener tombol hapus
            deleteButton.setOnClickListener(v -> {
                Tabel2.removeView(newRow);
                temporaryDataListGrade.remove(newDataRow);
            });

            newRow.addView(deleteButton);

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

        // Membersihkan tampilan tabel
        // Hapus semua baris kecuali header (index 0)
        if (Tabel2.getChildCount() > 1) {
            Tabel2.removeViews(1, Tabel2.getChildCount() - 1);
        }

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
                Log.d("SaveDataGrade", "noST: " + noST + ", gradeId: " + gradeId + ", jumlah: " + jumlah);

                Connection connection = ConnectionClass();
                if (connection != null) {
                    String query = "INSERT INTO dbo.STStick (NoST, IdGradeStick, JumlahStick) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, noST);
                    preparedStatement.setInt(2, Integer.parseInt(gradeId));  // Pastikan gradeId valid
                    preparedStatement.setString(3, jumlah);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        Log.d("DB_INSERT", "Data Grade berhasil disimpan");
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
                Toast.makeText(SawnTimber.this, "Data Grade berhasil disimpan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SawnTimber.this, "Gagal menyimpan data Grade", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private class SetAndSaveNoSTTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            String newNoST = null;

            if (con != null) {
                try {
                    String query = "SELECT MAX(NoST) FROM dbo.ST_h";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        String lastNoST = rs.getString(1);

                        if (lastNoST != null && lastNoST.startsWith("E.")) {
                            String numericPart = lastNoST.substring(2);
                            int numericValue = Integer.parseInt(numericPart);
                            int newNumericValue = numericValue + 1;

                            newNoST = "E." + String.format("%06d", newNumericValue);
                        }
                    }

                    rs.close();
                    ps.close();

                    if (newNoST != null) {
                        String insertQuery = "INSERT INTO dbo.ST_h (NoST) VALUES (?)";
                        PreparedStatement insertPs = con.prepareStatement(insertQuery);
                        insertPs.setString(1, newNoST);
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

            return newNoST;
        }

        @Override
        protected void onPostExecute(String newNoST) {
            if (newNoST != null) {
                NoST.setText(newNoST);
                Toast.makeText(SawnTimber.this, "NoST berhasil dibuat: " + newNoST, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SawnTimber.this, "Gagal membuat NoST baru.", Toast.LENGTH_SHORT).show();
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
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SpinKayu.setAdapter(adapter);
            SpinKayu.setSelection(0);
        }
    }
    private class SearchKayuBulatTask extends AsyncTask<String, Void, KayuBulat> {
        private String noKayuBulat;
        private String errorMessage = null;
        private String jenisKayu = null;

        public SearchKayuBulatTask(String noKayuBulat) {
            this.noKayuBulat = noKayuBulat;
        }

        @Override
        protected KayuBulat doInBackground(String... params) {
            Connection con = ConnectionClass();
            KayuBulat kayuBulat = null;

            if (con != null) {
                try {
                    String query = "SELECT NoPlat, NoTruk, Suket, IdJenisKayu FROM dbo.KayuBulat_h WHERE NoKayuBulat = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noKayuBulat);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        String noPlat = rs.getString("NoPlat");
                        String noTruk = rs.getString("NoTruk");
                        String noSuket = rs.getString("Suket");
                        int idJenisKayu = rs.getInt("IdJenisKayu");

                        kayuBulat = new KayuBulat(noPlat, noTruk, noSuket);

                        String queryJenisKayu = "SELECT Jenis FROM dbo.MstJenisKayu WHERE IdJenisKayu = ?";
                        PreparedStatement psJenisKayu = con.prepareStatement(queryJenisKayu);
                        psJenisKayu.setInt(1, idJenisKayu);
                        ResultSet rsJenisKayu = psJenisKayu.executeQuery();

                        if (rsJenisKayu.next()) {
                            jenisKayu = rsJenisKayu.getString("Jenis");
                        }

                        rsJenisKayu.close();
                        psJenisKayu.close();
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (SQLException e) {
                    errorMessage = "SQL Error: " + e.getMessage();
                    Log.e("Database Error", errorMessage);
                } catch (Exception e) {
                    errorMessage = "General Error: " + e.getMessage();
                    Log.e("Database Error", errorMessage);
                }
            } else {
                errorMessage = "Failed to connect to the database.";
                Log.e("Connection Error", errorMessage);
            }

            return kayuBulat;
        }

        @Override
        protected void onPostExecute(KayuBulat kayuBulat) {
            if (kayuBulat != null) {
                NoPlatTruk.setText(kayuBulat.getNoPlat());
                NoTruk.setText(kayuBulat.getNoTruk());
                NoSuket.setText(kayuBulat.getNoSuket());

                if (jenisKayu != null) {
                    EditText jenisKayuTextView = findViewById(R.id.JenisKayuKB);
                    jenisKayuTextView.setText(jenisKayu);
                } else {
                    Toast.makeText(SawnTimber.this, "Jenis kayu tidak ditemukan.", Toast.LENGTH_LONG).show();
                }

                Toast.makeText(SawnTimber.this, "Data ditemukan.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SawnTimber.this, "Data tidak ditemukan atau NoKayuBulat salah.", Toast.LENGTH_LONG).show();
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

            ArrayAdapter<SPK> adapter = new ArrayAdapter<>(SawnTimber.this, android.R.layout.simple_spinner_item, spkList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinSPK.setAdapter(adapter);
            SpinSPK.setSelection(0);
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
                SpinLokasi.setEnabled(false);
            } else {
                Log.e("Error", "Tidak ada grade ditemukan");
            }
        }
    }

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
            ArrayAdapter<Telly> adapter = new ArrayAdapter<>(SawnTimber.this, android.R.layout.simple_spinner_item, tellyList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Set adapter ke spinner
            SpinTelly.setAdapter(adapter);

            // Atur spinner untuk menampilkan elemen pertama ("Pilih") secara default
            SpinTelly.setSelection(0);
        }
    }

    private class LoadSupplierTask extends AsyncTask<String, Void, String> {
        private String noKayuBulat;

        public LoadSupplierTask(String noKayuBulat) {
            this.noKayuBulat = noKayuBulat;
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection con = ConnectionClass();
            String namaSupplier = null;

            if (con != null) {
                try {
                    String query = "SELECT kb.IdSupplier, ms.NmSupplier " +
                            "FROM dbo.KayuBulat_h kb " +
                            "JOIN dbo.MstSupplier ms ON kb.IdSupplier = ms.IdSupplier " +
                            "WHERE kb.NoKayuBulat = ?";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noKayuBulat);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        namaSupplier = rs.getString("NmSupplier");
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

            return namaSupplier;
        }

        @Override
        protected void onPostExecute(String namaSupplier) {
            if (namaSupplier != null) {
                Supplier.setText(namaSupplier);
            } else {
                Toast.makeText(SawnTimber.this, "Supplier tidak ditemukan untuk NoKayuBulat ini.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class LoadStickByTask extends AsyncTask<Void, Void, List<StickBy>> {
        @Override
        protected List<StickBy> doInBackground(Void... voids) {
            List<StickBy> stickByList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT IdStickBy, NamaStickBy FROM dbo.MstStickBy WHERE Enable = 1 AND IdStickBy != 0";
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
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
            String idLokasi = params[6];
            String dateCreate = params[7];

            int isUpah = CBUpah.isChecked() ? 1 : 0;

            // Default values for UOM columns
            int idUOMTblLebar = 0;
            int idUOMPanjang = 0;

            // Check which checkbox is checked and set values accordingly
            if (radioInch.isChecked()) {
                idUOMTblLebar = 1;  // Value for "Inch" = 1
            } else if (radioMillimeter.isChecked()) {
                idUOMTblLebar = 3;  // Value for "Millimeter 2" = 3
            }

            if (radioCentimeter.isChecked()) {
                idUOMPanjang = 4;   // Value for "Millimeter" = 4
            }

            Connection con = null;
            String message = "";

            try {
                con = ConnectionClass();
                if (con != null) {
                    String query = "UPDATE dbo.ST_h SET NoKayuBulat = ?, IdJenisKayu = ?, NoSPK = ?, IdOrgTelly = ?, IdStickBy = ?, IsUpah = ?, IdUOMTblLebar = ?, IdUOMPanjang = ?, IdLokasi = ?, DateCreate = ? WHERE NoST = ?";
                    PreparedStatement ps = con.prepareStatement(query);

                    ps.setString(1, noKayuBulat);
                    ps.setString(2, jenisKayu);
                    ps.setString(3, noSPK);
                    ps.setString(4, telly);
                    ps.setString(5, stickBy);
                    ps.setInt(6, isUpah);
                    ps.setInt(7, idUOMTblLebar);
                    ps.setInt(8, idUOMPanjang);
                    ps.setString(9, idLokasi);
                    ps.setString(10, dateCreate);
                    ps.setString(11, noST);

                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        message = "Data berhasil diperbarui.";
                    } else {
                        message = "Gagal memperbarui data.";
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
            Toast.makeText(SawnTimber.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private class UpdateCheckboxDataTask extends AsyncTask<Object, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Object... params) {
            String noST = (String) params[0];
            int isBagusKulit = (int) params[1];
            int isSticked = (int) params[2];
            int startKering = (int) params[3];

            Connection con = ConnectionClass();
            boolean success = false;

            if (con != null) {
                try {
                    String query = "UPDATE dbo.ST_h SET IsBagusKulit = ?, IsSticked = ?, StartKering = ? WHERE NoST = ?";
                    PreparedStatement ps = con.prepareStatement(query);

                    ps.setInt(1, isBagusKulit);
                    ps.setInt(2, isSticked);
                    ps.setInt(3, startKering);
                    ps.setString(4, noST);

                    int rowsAffected = ps.executeUpdate();
                    success = rowsAffected > 0;

                    ps.close();
                    con.close();
                } catch (SQLException e) {
                    Log.e("Database Error", "SQL Error: " + e.getMessage());
                } catch (Exception e) {
                    Log.e("Database Error", "General Error: " + e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }

            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(SawnTimber.this, "Data berhasil diperbarui.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SawnTimber.this, "Gagal memperbarui data.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class KayuBulat {
    private String noPlat;
    private String noTruk;
    private String noSuket;

    public KayuBulat(String noPlat, String noTruk, String noSuket) {
        this.noPlat = noPlat;
        this.noTruk = noTruk;
        this.noSuket = noSuket;
    }

    public String getNoPlat() {
        return noPlat;
    }

    public String getNoTruk() {
        return noTruk;
    }

    public String getNoSuket() {
        return noSuket;
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
}

