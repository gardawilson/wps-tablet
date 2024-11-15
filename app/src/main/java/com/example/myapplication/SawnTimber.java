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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import android.graphics.Color;

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
    private TextView DetailTebalST;
    private TextView DetailLebarST;
    private TextView DetailPanjangST;
    private TextView DetailPcsST;
    private CheckBox CBBagus;
    private CheckBox CBKulit;
    private boolean isDataBaruClickedST = false;
    private int currentTableNo = 1;
    private TableLayout Tabel;
    private int rowCount = 0;
    private TableLayout Tabel2;
    private CheckBox CBKering;
    private CheckBox CBStick;
    private CheckBox CBUpah;
    private CheckBox CBInch;
    private CheckBox CBMilimeter2;
    private CheckBox CBMilimeter;
    private static int currentNumber = 1;
    private Button BtnInputDetailST;
    private List<DataRow> dataRows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sawn_timber);

        BtnBatalST = findViewById(R.id.BtnBatalST);
        BtnSearchST = findViewById(R.id.BtnSearchST);
        BtnBatalST = findViewById(R.id.BtnBatalST);
        BtnDataBaruST = findViewById(R.id.BtnDataBaruST);
        BtnPrintST = findViewById(R.id.BtnPrintST);
        BtnHapusSemuaStickST = findViewById(R.id.BtnHapusSemuaStickST);
        BtnHapusStickST = findViewById(R.id.BtnHapusStickST);
        BtnTambahStickST = findViewById(R.id.BtnTambahStickST);
        BtnSimpanST = findViewById(R.id.BtnSimpanST);
        CBBagus = findViewById(R.id.CBBagus);
        CBKulit = findViewById(R.id.CBKulit);
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
        CBMilimeter2 = findViewById(R.id.CBMilimeter2);
        CBMilimeter = findViewById(R.id.CBMilimeter);
        CBInch = findViewById(R.id.CBInch);

        CBMilimeter2.setChecked(true);
        BtnPrintST.setEnabled(false);

        CBBagus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CBKulit.setChecked(false);
                }
            }
        });

        CBKulit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CBBagus.setChecked(false);
                }
            }
        });



        CBMilimeter2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CBInch.setChecked(false);
                }
            }
        });

        CBInch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CBMilimeter2.setChecked(false);
                }
            }
        });

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
                resetSpinners();
                new LoadSPKTask().execute();
                new LoadTellyTask().execute();
                new LoadStickByTask().execute();
                new LoadJenisKayuTask().execute();
                new LoadGradeStickTask().execute();
                new LoadLokasiTask().execute();
                isDataBaruClickedST = true;
                setCurrentDateTime();
            } else {
                Toast.makeText(SawnTimber.this, "Data baru sudah dimuat.", Toast.LENGTH_SHORT).show();
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
                GradeStick selectedGrade = (GradeStick) parent.getItemAtPosition(position);

                int selectedGradeId = selectedGrade.getIdGradeStick();
                String selectedGradeName = selectedGrade.getNamaGradeStick();

                Toast.makeText(getApplicationContext(), "Selected Grade: " + selectedGradeName + ", ID: " + selectedGradeId, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SpinLokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Lokasi selectedLokasi = (Lokasi) parent.getItemAtPosition(position);

                String selectedLokasiId = selectedLokasi.getIdLokasi();
                String selectedNamaLokasi = selectedLokasi.getNamaLokasi();

                Toast.makeText(getApplicationContext(), "Selected Lokasi: " + selectedLokasiId + ", ID: " + selectedNamaLokasi, Toast.LENGTH_SHORT).show();
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


                if (!noST.isEmpty() && !noKayuBulat.isEmpty() && !jenisKayu.isEmpty() && !noSPK.isEmpty() && !telly.isEmpty() && !stickBy.isEmpty() && CBMilimeter2.isChecked() || CBInch.isChecked()) {
                    new UpdateDataTask().execute(noST, noKayuBulat, jenisKayu, noSPK, telly, stickBy, idLokasi, dateCreate);

                    int isBagusKulit = CBBagus.isChecked() ? 1 : (CBKulit.isChecked() ? 2 : 0);
                    int isSticked = CBStick.isChecked() ? 1 : 0;
                    int startKering = CBKering.isChecked() ? 1 : 0;

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

                    BtnSimpanST.setEnabled(false);
                    BtnBatalST.setEnabled(false);
                    BtnPrintST.setEnabled(true);

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
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                CBUpah.setChecked(false);
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
                try {
                    String noST = NoST.getText() != null ? NoST.getText().toString() : "";
                    String Kayu = SpinKayu.getSelectedItem() != null ? SpinKayu.getSelectedItem().toString() : "";
                    String Tanggal = TglStickBundel.getText() != null ? TglStickBundel.getText().toString() : "";
                    String Telly = SpinTelly.getSelectedItem() != null ? SpinTelly.getSelectedItem().toString() : "";
                    String noSPK = SpinSPK.getSelectedItem() != null ? SpinSPK.getSelectedItem().toString() : "";
                    String jlh = JumlahPcsST.getText() != null ? JumlahPcsST.getText().toString() : "";
                    String StickBy = SpinStickBy.getSelectedItem() != null? SpinStickBy.getSelectedItem().toString() : "";
                    String Plat = NoPlatTruk.getText() != null ? NoPlatTruk.getText().toString() : "";
                    String KBAsal = JenisKayuKB.getText() != null ? JenisKayuKB.getText().toString() : "";

                    String m3 = "";

                    Uri pdfUri = createPdf(noST, Kayu, Tanggal, Telly, noSPK, jlh, StickBy,Plat,KBAsal, m3, dataRows);

                    if (pdfUri != null) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(pdfUri, "application/pdf");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setPackage("com.mi.globalbrowser");

                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(SawnTimber.this, "Mi Browser not found. Please install Mi Browser or use another app to open the PDF.", Toast.LENGTH_LONG).show();

                            Intent fallbackIntent = new Intent(Intent.ACTION_VIEW);
                            fallbackIntent.setDataAndType(pdfUri, "application/pdf");
                            fallbackIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(Intent.createChooser(fallbackIntent, "Open PDF with"));
                        }
                    } else {
                        Toast.makeText(SawnTimber.this, "Error creating PDF", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(SawnTimber.this, "Error creating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SawnTimber.this, "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                clearTableData();
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
        DetailPanjangST.setText("");
        DetailTebalST.setText("");
        DetailLebarST.setText("");
        DetailPcsST.setText("");
        NoST.setText("");
        NoKayuBulat.setText("");
        Supplier.setText("");
        NoTruk.setText("");
        NoPlatTruk.setText("");
        NoSuket.setText("");
        JenisKayuKB.setText("");
        JumlahStick.setText("");
        CBBagus.setChecked(false);
        CBKulit.setChecked(false);
        CBStick.setChecked(false);
        CBKering.setChecked(false);
        CBUpah.setChecked(false);
        currentNumber = 1;
    }

    private void resetSpinners() {
        if (SpinKayu.getAdapter() != null) {
            SpinKayu.setSelection(0);
        }
        if (SpinTelly.getAdapter() != null) {
            SpinTelly.setSelection(0);
        }
        if (SpinGrade.getAdapter() != null) {
            SpinGrade.setSelection(0);
        }
        if (SpinSPK.getAdapter() != null) {
            SpinSPK.setSelection(0);
        }
        if (SpinStickBy.getAdapter() != null) {
            SpinStickBy.setSelection(0);
        }
        BtnDataBaruST.setEnabled(true);
        isDataBaruClickedST = true;
    }


    private void ton() {
        try {
            double totalTON = 0.0;
            boolean isMillimeter = CBMilimeter2.isChecked();

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
            boolean isMillimeter = CBMilimeter2.isChecked();

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

    private Uri createPdf(String noST, String Kayu, String Tanggal, String Telly, String noSPK, String jlh, String StickBy, String Plat, String KBAsal, String m3, List<DataRow> dataRows) throws IOException {
        Uri pdfUri = null;
        ContentResolver resolver = getContentResolver();
        String fileName = "myPDF.pdf";
        String relativePath = Environment.DIRECTORY_DOWNLOADS;

        deleteExistingPdf(fileName, relativePath);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath);

        Uri uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
        if (uri != null) {
            OutputStream outputStream = resolver.openOutputStream(uri);
            if (outputStream != null) {
                try {
                    PdfFont timesNewRoman = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);

                    PdfWriter writer = new PdfWriter(outputStream);
                    PdfDocument pdfDocument = new PdfDocument(writer);
                    Document document = new Document(pdfDocument);

                    pdfDocument.setDefaultPageSize(PageSize.A6);
                    document.setMargins(0, 5, 0, 5);

                    Paragraph judul = new Paragraph("LABEL SAWN TIMBER (KG)\n")
                            .setBold().setFontSize(8)
                            .setTextAlignment(TextAlignment.CENTER);

                    Paragraph isi = new Paragraph("").setFont(timesNewRoman).setFontSize(8)
                            .add("Stick By :" + StickBy + "                                                         ")
                            .add("Telly By :" + Telly + "\n")
                            .add("No ST    : " + noST + "                                                       ")
                            .add("Tanggal  : " + Tanggal + "\n")
                            .add("Jenis Kayu : " + Kayu + "                                                      ")
                            .add("Plat Truk: " + Plat + "\n")
                            .add("No KB Asal  : " + KBAsal + "                                          ")
                            .add("No SPK :" + noSPK + "\n");

                    Paragraph garis = new Paragraph("---------------------------------------------------------------")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFont(timesNewRoman);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Paragraph bulanParagraf;
                    try {
                        Date date = sdf.parse(Tanggal);
                        SimpleDateFormat monthFormat = new SimpleDateFormat("M");
                        String monthNumber = monthFormat.format(date);
                        bulanParagraf = new Paragraph(" " + monthNumber)
                                .setFont(timesNewRoman)
                                .setFontSize(40).setTextAlignment(TextAlignment.LEFT).setMarginLeft(40)
                                .add("   A");

                    } catch (ParseException e) {
                        e.printStackTrace();
                        throw new IOException("Error parsing the date", e);
                    }

                    float[] width = {50f, 70f, 50f, 70f};
                    Table table = new Table(width);
                    table.setHorizontalAlignment(HorizontalAlignment.CENTER).setFontSize(8);
                    table.addCell(new Cell().add(new Paragraph("Tebal (mm)").setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                    table.addCell(new Cell().add(new Paragraph("Lebar (mm)").setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                    table.addCell(new Cell().add(new Paragraph("Panjang (mm)").setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                    table.addCell(new Cell().add(new Paragraph("Pcs").setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));

                    for (DataRow row : dataRows) {
                        table.addCell(new Cell().add(new Paragraph(row.tebal).setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                        table.addCell(new Cell().add(new Paragraph(row.lebar).setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                        table.addCell(new Cell().add(new Paragraph(row.panjang).setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                        table.addCell(new Cell().add(new Paragraph(row.pcs).setTextAlignment(TextAlignment.CENTER).setFont(timesNewRoman)));
                    }

                    BarcodeQRCode qrCode = new BarcodeQRCode(noST);
                    PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
                    Image qrCodeImage = new Image(qrCodeObject).setWidth(60).setHorizontalAlignment(HorizontalAlignment.CENTER).setMargins(0, 0, 0, 0);

                    BarcodeQRCode qrCode2 = new BarcodeQRCode(noST);
                    PdfFormXObject qrCodeObject2 = qrCode2.createFormXObject(ColorConstants.BLACK, pdfDocument);
                    Image qrCodeImage2 = new Image(qrCodeObject2).setWidth(60).setHorizontalAlignment(HorizontalAlignment.RIGHT).setMargins(0, 0, 0, 0);

                    Paragraph St = new Paragraph(noST).setTextAlignment(TextAlignment.CENTER).setFontSize(8).setMargins(0, 0, 0, 0).setFont(timesNewRoman);
                    Paragraph St2 = new Paragraph(noST).setTextAlignment(TextAlignment.RIGHT).setFontSize(8).setMargins(0, 10, 0, 0).setFont(timesNewRoman);

                    document.add(judul);
                    document.add(isi);
                    document.add(table);
                    document.add(St);
                    document.add(qrCodeImage);
                    document.add(bulanParagraf);
                    document.add(garis);
                    document.add(St2);
                    document.add(qrCodeImage2);

                    document.close();
                    pdfUri = uri;

                    Toast.makeText(this, "PDF Created at " + uri.getPath(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new IOException("Failed to create PDF", e);
                } finally {
                    outputStream.close();
                }
            }
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
    }



    //Fungsi untuk add data Grade
    private List<DataRow2> temporaryDataListGrade = new ArrayList<>();

    private void addDataGrade(String noST) {
        GradeStick selectedGrade = (GradeStick) SpinGrade.getSelectedItem();
        String gradeName = selectedGrade.getNamaGradeStick();
        int gradeId = selectedGrade.getIdGradeStick();
        String jumlah = JumlahStick.getText().toString();

        if (gradeName.isEmpty() || jumlah.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
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
            if (!jenisKayuList.isEmpty()) {
                ArrayAdapter<JenisKayu> adapter = new ArrayAdapter<>(SawnTimber.this, android.R.layout.simple_spinner_item, jenisKayuList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinKayu.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load jenis kayu.");
            }
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
            if (!spkList.isEmpty()) {
                ArrayAdapter<SPK> adapter = new ArrayAdapter<>(SawnTimber.this, android.R.layout.simple_spinner_item, spkList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSPK.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load SPK data.");
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
            if (!tellyList.isEmpty()) {
                ArrayAdapter<Telly> adapter = new ArrayAdapter<>(SawnTimber.this, android.R.layout.simple_spinner_item, tellyList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                SpinTelly.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load telly data.");
            }
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
            if (!stickByList.isEmpty()) {
                ArrayAdapter<StickBy> adapter = new ArrayAdapter<>(SawnTimber.this,
                        android.R.layout.simple_spinner_item, stickByList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinStickBy.setAdapter(adapter);
            } else {
                Toast.makeText(SawnTimber.this, "Data StickBy tidak ditemukan.", Toast.LENGTH_SHORT).show();
            }
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
            if (CBInch.isChecked()) {
                idUOMTblLebar = 1;  // Value for "Inch" = 1
            } else if (CBMilimeter2.isChecked()) {
                idUOMTblLebar = 3;  // Value for "Millimeter 2" = 3
            }

            if (CBMilimeter.isChecked()) {
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

