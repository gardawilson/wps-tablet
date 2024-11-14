package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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
import java.util.Locale;

import java.text.DecimalFormat;
import android.view.Gravity;
import android.widget.TableRow;

public class CrossCut extends AppCompatActivity {

    private EditText NoCC;
    private EditText DateCC;
    private EditText TimeCC;
    private EditText NoLaminatingAsal;
    private Spinner SpinKayuCC;
    private Spinner SpinTellyCC;
    private Spinner SpinSPKCC;
    private Spinner SpinSPKAsalCC;
    private EditText NoFJAsal;
    private Spinner SpinFisikCC;
    private Spinner SpinGradeCC;
    private Spinner SpinMesinCC;
    private Spinner SpinSusunCC;
    private Calendar calendarCC;
    private RadioGroup radioGroupCC;
    private RadioButton radioButtonMesinCC;
    private RadioButton radioButtonBSusunCC;
    private Button BtnDataBaruCC;
    private Button BtnSimpanCC;
    private Button BtnBatalCC;
    private Button BtnHapusDetailCC;
    private boolean isDataBaruClickedCC = false;
    private CheckBox CBAfkirCC;
    private CheckBox CBLemburCC;
    private Button BtnInputDetailCC;
    private EditText DetailLebarCC;
    private EditText DetailTebalCC;
    private EditText DetailPanjangCC;
    private EditText DetailPcsCC;
    private static int currentNumber = 1;
    private Button BtnPrintCC;
    private TextView M3CC;
    private TextView JumlahPcsCC;
    private boolean isCBAfkirCC, isCBLemburCC;
    private Button BtnSearchCC;
    private Spinner SpinProfileCC;
    private int rowCount = 0;
    private TableLayout Tabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cross_cut);

        NoLaminatingAsal = findViewById(R.id.NoLaminatingAsal);
        NoCC = findViewById(R.id.NoCC);
        DateCC = findViewById(R.id.DateCC);
        TimeCC = findViewById(R.id.TimeCC);
        SpinKayuCC = findViewById(R.id.SpinKayuCC);
        SpinTellyCC = findViewById(R.id.SpinTellyCC);
        SpinSPKCC = findViewById(R.id.SpinSPKCC);
        SpinSPKAsalCC = findViewById(R.id.SpinSPKAsalCC);
        NoFJAsal = findViewById(R.id.NoFJAsal);
        SpinFisikCC = findViewById(R.id.SpinFisikCC);
        SpinGradeCC = findViewById(R.id.SpinGradeCC);
        calendarCC = Calendar.getInstance();
        SpinMesinCC = findViewById(R.id.SpinMesinCC);
        SpinSusunCC = findViewById(R.id.SpinSusunCC);
        radioButtonMesinCC = findViewById(R.id.radioButtonMesinCC);
        radioButtonBSusunCC = findViewById(R.id.radioButtonBSusunCC);
        BtnDataBaruCC = findViewById(R.id.BtnDataBaruCC);
        BtnSimpanCC = findViewById(R.id.BtnSimpanCC);
        BtnBatalCC = findViewById(R.id.BtnBatalCC);
        BtnHapusDetailCC = findViewById(R.id.BtnHapusDetailCC);
        CBLemburCC = findViewById(R.id.CBLemburCC);
        CBAfkirCC = findViewById(R.id.CBAfkirCC);
        BtnInputDetailCC = findViewById(R.id.BtnInputDetailCC);
        DetailPcsCC = findViewById(R.id.DetailPcsCC);
        DetailTebalCC = findViewById(R.id.DetailTebalCC);
        DetailPanjangCC = findViewById(R.id.DetailPanjangCC);
        DetailLebarCC = findViewById(R.id.DetailLebarCC);
        BtnPrintCC = findViewById(R.id.BtnPrintCC);
        M3CC = findViewById(R.id.M3CC);
        JumlahPcsCC = findViewById(R.id.JumlahPcsCC);
        BtnSearchCC = findViewById(R.id.BtnSearchCC);
        BtnPrintCC.setEnabled(false);
        SpinProfileCC = findViewById(R.id.SpinProfileCC);
        Tabel = findViewById(R.id.Tabel);

         radioButtonMesinCC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpinMesinCC.setEnabled(true);
                    SpinSusunCC.setEnabled(false);
                }
            }
        });

        radioButtonBSusunCC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpinSusunCC.setEnabled(true);
                    SpinMesinCC.setEnabled(false);
                }
            }
        });

        setCurrentDateTime();

        BtnDataBaruCC.setOnClickListener(v -> {
            if (!isDataBaruClickedCC) {
                resetSpinners();
                new LoadJenisKayuTask().execute();
                new LoadTellyTask().execute();
                new LoadSPKTask().execute();
                new LoadSPKAsalTask().execute();
                new LoadProfileTask().execute();
                new LoadFisikTask().execute();
                new LoadGradeTask().execute();
                new LoadMesinTask().execute();
                new LoadSusunTask().execute();

                isDataBaruClickedCC = true;
                setCurrentDateTime();
            } else {
                Toast.makeText(CrossCut.this, "Tombol Data Baru sudah diklik. Klik Simpan terlebih dahulu.", Toast.LENGTH_SHORT).show();
            }
            BtnSimpanCC.setEnabled(true);
            new SetAndSaveNoCCTask().execute();
            BtnPrintCC.setEnabled(false);
            BtnBatalCC.setEnabled(true);
            radioButtonMesinCC.setEnabled(true);
            radioButtonBSusunCC.setEnabled(true);
            setCurrentDateTime();
            clearTableData2();
            BtnDataBaruCC.setEnabled(false);
        });


        BtnSimpanCC.setOnClickListener(v -> {
            String noCC = NoCC.getText().toString();
            String dateCreate = DateCC.getText().toString();
            String time = TimeCC.getText().toString();

            Telly selectedTelly = (Telly) SpinTellyCC.getSelectedItem();
            SPK selectedSPK = (SPK) SpinSPKCC.getSelectedItem();
            SPKAsal selectedSPKAsal = (SPKAsal) SpinSPKAsalCC.getSelectedItem();
            Profile selectedProfile = (Profile) SpinProfileCC.getSelectedItem();
            Fisik selectedFisik = (Fisik) SpinFisikCC.getSelectedItem();
            Grade selectedGrade = (Grade) SpinGradeCC.getSelectedItem();
            JenisKayu selectedJenisKayu = (JenisKayu) SpinKayuCC.getSelectedItem();
            Mesin selectedMesin = (Mesin) SpinMesinCC.getSelectedItem();
            Susun selectedSusun = (Susun) SpinSusunCC.getSelectedItem();

            String idGrade = selectedGrade != null ? selectedGrade.getIdGrade() : null;
            String idTelly = selectedTelly != null ? selectedTelly.getIdTelly() : null;
            String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
            String noSPKasal = selectedSPKAsal != null ? selectedSPKAsal.getNoSPKAsal() : null;
            String idProfile = selectedProfile != null ? selectedProfile.getIdFJProfile() : null;
            String idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;
            String noProduksi = selectedMesin != null ? selectedMesin.getNoProduksi() : null;
            String noBongkarSusun = selectedSusun != null ? selectedSusun.getNoBongkarSusun() : null;
            int isReject = CBAfkirCC.isChecked() ? 1 : 0;
            int isLembur = CBLemburCC.isChecked() ? 1 : 0;

            if (noCC.isEmpty() || dateCreate.isEmpty() || time.isEmpty() ||
                    selectedTelly == null ||
                    selectedSPK == null ||
                    selectedProfile == null ||
                    selectedFisik == null ||
                    selectedGrade == null ||
                    selectedJenisKayu == null ||
                    (!radioButtonMesinCC.isChecked() && !radioButtonBSusunCC.isChecked()) ||
                    (radioButtonMesinCC.isChecked() && selectedMesin == null) ||
                    (radioButtonBSusunCC.isChecked() && selectedSusun == null)) {

                Toast.makeText(CrossCut.this, "Pastikan semua field terisi dengan benar.", Toast.LENGTH_SHORT).show();
                return;
            }
            BtnDataBaruCC.setEnabled(true);
            BtnPrintCC.setEnabled(true);

            new UpdateDatabaseTask(
                    noCC,
                    dateCreate,
                    time,
                    idTelly,
                    noSPK,
                    noSPKasal,
                    idGrade,
                    idJenisKayu,
                    idProfile,
                    isReject,
                    isLembur
            ).execute();

            if (radioButtonMesinCC.isChecked() && SpinMesinCC.isEnabled() && noProduksi != null) {
                new SaveToDatabaseTask(noProduksi, noCC).execute();
                for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                    CrossCut.DataRow dataRow = temporaryDataListDetail.get(i);
                    saveDataDetailToDatabase(noCC, i + 1, Double.parseDouble(dataRow.tebal), Double.parseDouble(dataRow.lebar),
                            Double.parseDouble(dataRow.panjang), Integer.parseInt(dataRow.pcs));
                }
            } else if (radioButtonBSusunCC.isChecked() && SpinSusunCC.isEnabled() && noBongkarSusun != null) {
                new SaveBongkarSusunTask(noBongkarSusun, noCC).execute();
                for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                    CrossCut.DataRow dataRow = temporaryDataListDetail.get(i);
                    saveDataDetailToDatabase(noCC, i + 1, Double.parseDouble(dataRow.tebal), Double.parseDouble(dataRow.lebar),
                            Double.parseDouble(dataRow.panjang), Integer.parseInt(dataRow.pcs));
                }
            } else {
                Toast.makeText(CrossCut.this, "Pilih opsi yang valid untuk disimpan.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(CrossCut.this, "Data berhasil disimpan dan tampilan telah dikosongkan.", Toast.LENGTH_SHORT).show();

        });

        BtnHapusDetailCC.setOnClickListener(v -> {
            resetDetailData();
        });


        BtnBatalCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noCC = NoCC.getText().toString().trim();

                if (!noCC.isEmpty()) {
                }

                clearTableData2();
                Toast.makeText(CrossCut.this, "Tampilan telah dikosongkan.", Toast.LENGTH_SHORT).show();
            }
        });

        BtnSearchCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noCC = NoCC.getText().toString();


                DetailLebarCC.setText("");
                DetailPanjangCC.setText("");
                DetailTebalCC.setText("");
                DetailPcsCC.setText("");
                NoCC.setText("");

                if (!noCC.isEmpty()) {
                    new LoadMesinTask2(noCC).execute();
                    new LoadSusunTask2(noCC).execute();
                    new LoadFisikTask2(noCC).execute();
                    new LoadGradeTask2(noCC).execute();
                    new LoadJenisKayuTask2(noCC).execute();
                    new LoadTellyTask2(noCC).execute();
                    new LoadSPKTask2(noCC).execute();
                    new LoadProfileTask2(noCC).execute();
                    new SearchAllDataTask(noCC).execute();

                    radioButtonMesinCC.setEnabled(true);
                    SpinSPKCC.setEnabled(true);
                    radioButtonBSusunCC.setEnabled(true);
                } else {
                    Log.e("Input Error", "NoCC is empty");
                    radioButtonMesinCC.setEnabled(false);
                    radioButtonBSusunCC.setEnabled(false);
                }

                BtnSimpanCC.setEnabled(false);
                BtnBatalCC.setEnabled(false);
                BtnPrintCC.setEnabled(true);
            }
        });

        SpinKayuCC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JenisKayu selectedJenisKayu = (JenisKayu) parent.getItemAtPosition(position);
                String idJenisKayu = selectedJenisKayu.getIdJenisKayu();
                new LoadGradeTask().execute(idJenisKayu);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        DateCC.setOnClickListener(v -> showDatePickerDialog());

        TimeCC.setOnClickListener(v -> showTimePickerDialog());

        BtnInputDetailCC.setOnClickListener(v -> {
            String noCC = NoCC.getText().toString();

            if (!noCC.isEmpty()) {
                addDataDetail(noCC);
            } else {
                Toast.makeText(CrossCut.this, "NoCC tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            jumlahpcs();
        });

//        BtnPrintCC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    String noCC = NoCC.getText() != null ? NoCC.getText().toString() : "";
//                    String Kayu = SpinKayuCC.getSelectedItem() != null ? SpinKayuCC.getSelectedItem().toString() : "";
//                    String Grade = SpinGradeCC.getSelectedItem() != null ? SpinGradeCC.getSelectedItem().toString() : "";
//                    String Fisik = SpinFisikCC.getSelectedItem() != null ? SpinFisikCC.getSelectedItem().toString() : "";
//                    String Tanggal = DateCC.getText() != null ? DateCC.getText().toString() : "";
//                    String Waktu = TimeCC.getText() != null ? TimeCC.getText().toString() : "";
//                    String Telly = SpinTellyCC.getSelectedItem() != null ? SpinTellyCC.getSelectedItem().toString() : "";
//                    String Mesin = SpinMesinCC.getSelectedItem() != null ? SpinMesinCC.getSelectedItem().toString() : "";
//                    String noSPK = SpinSPKCC.getSelectedItem() != null ? SpinSPKCC.getSelectedItem().toString() : "";
//                    String tebal = TabelTebalCC.getText() != null ? TabelTebalCC.getText().toString() : "";
//                    String lebar = TabelLebarCC.getText() != null ? TabelLebarCC.getText().toString() : "";
//                    String panjang = TabelPanjangCC.getText() != null ? TabelPanjangCC.getText().toString() : "";
//                    String pcs = TabelPcsCC.getText() != null ? TabelPcsCC.getText().toString() : "";
//                    String jlh = JumlahPcsCC.getText() != null ? JumlahPcsCC.getText().toString() : "";
//                    String m3 = M3CC.getText() != null ? M3CC.getText().toString() : "";
//                    String Susun = SpinSusunCC.getSelectedItem() != null ? SpinSusunCC.getSelectedItem().toString() : "";
//
//                    Uri pdfUri = createPdf(noCC, Kayu, Grade, Fisik, Tanggal, Waktu, Telly, Mesin, Susun, noSPK, tebal, lebar, panjang, pcs, jlh, m3);
//
//                    if (pdfUri != null) {
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setDataAndType(pdfUri, "application/pdf");
//                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                        intent.setPackage("com.mi.globalbrowser");
//
//                        try {
//                            startActivity(intent);
//                        } catch (ActivityNotFoundException e) {
//                            Toast.makeText(CrossCut.this, "Mi Browser not found. Please install Mi Browser or use another app to open the PDF.", Toast.LENGTH_LONG).show();
//
//                            Intent fallbackIntent = new Intent(Intent.ACTION_VIEW);
//                            fallbackIntent.setDataAndType(pdfUri, "application/pdf");
//                            fallbackIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            startActivity(Intent.createChooser(fallbackIntent, "Open PDF with"));
//                        }
//                    } else {
//                        Toast.makeText(CrossCut.this, "Error creating PDF", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(CrossCut.this, "Error creating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(CrossCut.this, "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//                clearTableData();
//            }
//        });
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

    private class SearchAllDataTask extends AsyncTask<String, Void, Boolean> {
        private String noCC;

        public SearchAllDataTask(String noCC) {
            this.noCC = noCC;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("SearchAllDataTask", "Searching for NoCCAkhir: " + noCC);
            Connection con = ConnectionClass();
            boolean isDataFound = false;

            if (con != null) {
                try {
                    String query = "SELECT h.DateCreate, h.Jam, " +
                            "d.Lebar, d.Panjang, d.Tebal, d.JmlhBatang, d.NoUrut, " +
                            "h.IsReject, h.IsLembur " +
                            "FROM dbo.CCAkhir_h AS h " +
                            "INNER JOIN dbo.CCAkhir_d AS d ON h.NoCCAkhir = d.NoCCAkhir " +
                            "WHERE h.NoCCAkhir = ?";

                    Log.d("SearchAllDataTask", "Preparing statement: " + query);
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noCC);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        final String dateCreate = rs.getString("DateCreate");
                        final int no = rs.getInt("NoUrut");
                        final String jam = rs.getString("Jam");
                        final int lebar = rs.getInt("Lebar");
                        final int panjang = rs.getInt("Panjang");
                        final int tebal = rs.getInt("Tebal");
                        final int jmlhBatang = rs.getInt("JmlhBatang");
                        final boolean isReject = rs.getInt("IsReject") == 1;
                        final boolean isLembur = rs.getInt("IsLembur") == 1;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                NoCC.setText(noCC);
                                DateCC.setText(dateCreate != null ? dateCreate : "");
                                TimeCC.setText(jam != null ? jam : "");
                                CBAfkirCC.setChecked(isReject);
                                CBLemburCC.setChecked(isLembur);

                                m3();
                                jumlahpcs();
                            }
                        });

                        isDataFound = true;
                    } else {
                        Log.e("SearchAllDataTask", "No data found for NoCCAkhir: " + noCC);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (SQLException e) {
                    Log.e("Database Error", "SQL Exception: " + e.getMessage());
                    if (con != null) {
                        try {
                            con.close();
                        } catch (SQLException e1) {
                            Log.e("Connection Close Error", e1.getMessage());
                        }
                    }
                } catch (Exception e) {
                    Log.e("General Error", e.getMessage());
                    if (con != null) {
                        try {
                            con.close();
                        } catch (SQLException e1) {
                            Log.e("Connection Close Error", e1.getMessage());
                        }
                    }
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }

            return isDataFound;
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

    private void addDataDetail(String noCC) {
        String tebal = DetailTebalCC.getText().toString();
        String panjang = DetailPanjangCC.getText().toString();
        String lebar = DetailLebarCC.getText().toString();
        String pcs = DetailPcsCC.getText().toString();

        if (tebal.isEmpty() || panjang.isEmpty() || lebar.isEmpty() || pcs.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
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
            DetailTebalCC.setText("");
            DetailPanjangCC.setText("");
            DetailLebarCC.setText("");
            DetailPcsCC.setText("");

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
        if (DetailTebalCC != null) {
            DetailTebalCC.setText("");
        }
        if (DetailLebarCC != null) {
            DetailLebarCC.setText("");
        }
        if (DetailPanjangCC != null) {
            DetailPanjangCC.setText("");
        }
        if (DetailPcsCC != null) {
            DetailPcsCC.setText("");
        }
    }

    private void saveDataDetailToDatabase(String noCC, int noUrut, double tebal, double lebar, double panjang, int pcs) {
        new CrossCut.SaveDataTaskDetail().execute(noCC, String.valueOf(noUrut), String.valueOf(tebal), String.valueOf(lebar),
                String.valueOf(panjang), String.valueOf(pcs));
    }

    private class SaveDataTaskDetail extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String noCC = params[0];
            String noUrut = params[1];
            String tebal = params[2];
            String lebar = params[3];
            String panjang = params[4];
            String pcs = params[5];

            try {
                Connection connection = ConnectionClass();
                if (connection != null) {
                    String query = "INSERT INTO dbo.CCAkhir_d (NoCCAkhir, NoUrut, Tebal, Lebar, Panjang, JmlhBatang) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, noCC);
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


    private void clearTableData2() {
        NoCC.setText("");
        M3CC.setText("");
        JumlahPcsCC.setText("");
        CBAfkirCC.setChecked(false);
        CBLemburCC.setChecked(false);

        currentNumber = 1;
    }

    private void resetSpinners() {
        if (SpinKayuCC.getAdapter() != null) {
            SpinKayuCC.setSelection(0);
        }
        if (SpinMesinCC.getAdapter() != null) {
            SpinMesinCC.setSelection(0);
        }
        if (SpinSusunCC.getAdapter() != null) {
            SpinSusunCC.setSelection(0);
        }
        if (SpinTellyCC.getAdapter() != null) {
            SpinTellyCC.setSelection(0);
        }
        if (SpinGradeCC.getAdapter() != null) {
            SpinGradeCC.setSelection(0);
        }
        if (SpinProfileCC.getAdapter() != null) {
            SpinProfileCC.setSelection(0);
        }
        if (SpinFisikCC.getAdapter() != null) {
            SpinFisikCC.setSelection(0);
        }
        if (SpinSPKCC.getAdapter() != null) {
            SpinSPKCC.setSelection(0);
        }

        BtnDataBaruCC.setEnabled(true);
        isDataBaruClickedCC = true;
    }


    private void m3() {
//        TextView tabeltebalTextView = TabelTebalCC;
//        TextView tabelpanjangTextView = TabelPanjangCC;
//        TextView tabellebarTextView = TabelLebarCC;
//        TextView tabelpcsTextView = TabelPcsCC;
//
//        String tebalString = tabeltebalTextView.getText().toString();
//        String panjangString = tabelpanjangTextView.getText().toString();
//        String lebarString = tabellebarTextView.getText().toString();
//        String jmlhBatangString = tabelpcsTextView.getText().toString();
//
//        int tebal = Integer.parseInt(tebalString.isEmpty() ? "0" : tebalString);
//        int panjang = Integer.parseInt(panjangString.isEmpty() ? "0" : panjangString);
//        int lebar = Integer.parseInt(lebarString.isEmpty() ? "0" : lebarString);
//        int jmlhBatang = Integer.parseInt(jmlhBatangString.isEmpty() ? "0" : jmlhBatangString);
//
//        float result = (tebal * panjang);
//        float result2 = ( lebar * jmlhBatang);
//        float result3 = (result * result2 / 1000000000);
//
//        TextView M3 = findViewById(R.id.M3CC);
//        M3.setText(String.format("%.4f" , result3));
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

        JumlahPcsCC.setText(String.valueOf(totalPcs));
    }


    private void setCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        DateCC.setText(currentDate);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        TimeCC.setText(currentTime);
    }

    private void showDatePickerDialog() {
        Calendar calendarCC = Calendar.getInstance();
        int year = calendarCC.get(Calendar.YEAR);
        int month = calendarCC.get(Calendar.MONTH);
        int day = calendarCC.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CrossCut.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                String selectedDate = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);
                DateCC.setText(selectedDate);
                new LoadMesinTask().execute(selectedDate);
                new LoadSusunTask().execute(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        int hour = calendarCC.get(Calendar.HOUR_OF_DAY);
        int minute = calendarCC.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CrossCut.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                calendarCC.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendarCC.set(Calendar.MINUTE, selectedMinute);

                SimpleDateFormat timeFormat = new SimpleDateFormat(" HH:mm:ss", Locale.getDefault());
                String updatedTime = timeFormat.format(calendarCC.getTime());
                TimeCC.setText(updatedTime);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private Uri createPdf(String noCC, String Kayu, String Grade, String Fisik, String Tanggal, String Waktu, String Telly, String Mesin, String Susun, String noSPK, String tebal, String lebar, String panjang, String pcs, String jlh, String m3) throws IOException {
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
                    PdfWriter writer = new PdfWriter(outputStream);
                    PdfDocument pdfDocument = new PdfDocument(writer);
                    Document document = new Document(pdfDocument);

                    pdfDocument.setDefaultPageSize(PageSize.A6);
                    document.setMargins(0, 5, 0, 5);

                    String mesinAtauSusun;
                    if (Mesin.isEmpty()) {
                        mesinAtauSusun = Susun.isEmpty() ? "Mesin & Susun tidak tersedia" : "B.Susun : " + Susun;
                    } else {
                        mesinAtauSusun = "Mesin : " + Mesin;
                    }

                    Paragraph judul = new Paragraph("LABEL CCAkhir\n").setBold().setFontSize(8).setTextAlignment(TextAlignment.CENTER);
                    Paragraph isi = new Paragraph("").setFontSize(7)
                            .add("No CC  : " + noCC + "                                              ")
                            .add("Tanggal : " + Tanggal + " " + Waktu + "\n")
                            .add("Kayu    : " + Kayu + "                                                    ")
                            .add("Telly   : " + Telly + "\n")
                            .add("Grade   : " + Grade + "                                          ")
                            .add(mesinAtauSusun + "\n")
                            .add("Fisik   : " + Fisik + "                                                           ")
                            .add("No SPK  : " + noSPK + "\n");

                    float[] width = {50f, 50f, 50f, 50f};
                    Table table = new Table(width);
                    table.setHorizontalAlignment(HorizontalAlignment.CENTER).setFontSize(7);
                    table.addCell(new Cell().add(new Paragraph("Tebal (mm)").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph("Lebar (mm)").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph("Panjang (mm)").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph("Pcs").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph(tebal).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph(lebar).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph(panjang).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph(pcs).setTextAlignment(TextAlignment.CENTER)));

                    BarcodeQRCode qrCode = new BarcodeQRCode(noCC);
                    PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
                    Image qrCodeImage = new Image(qrCodeObject).setWidth(45).setHorizontalAlignment(HorizontalAlignment.CENTER).setMarginBottom(0).setMarginTop(0);

                    Paragraph pcsm3 = new Paragraph("").setFontSize(7).setTextAlignment(TextAlignment.RIGHT).setMarginRight(67)
                            .add("Jmlh Pcs = " + jlh + "\t" + "\n")
                            .add("m3 = " + m3 + "\n");

                    Paragraph garis = new Paragraph("--------------------------------------------------------------").setTextAlignment(TextAlignment.CENTER);
                    Paragraph output = new Paragraph("Output").setTextAlignment(TextAlignment.CENTER).setFontSize(6).setMarginTop(0).setMarginBottom(0);
                    Paragraph input = new Paragraph("Input").setTextAlignment(TextAlignment.CENTER).setFontSize(6).setMarginTop(0).setMarginBottom(0);
                    Paragraph CC = new Paragraph(noCC).setTextAlignment(TextAlignment.CENTER).setFontSize(6).setMarginTop(0).setMarginBottom(0);
                    document.add(judul);
                    document.add(isi);
                    document.add(table);
                    document.add(pcsm3);
                    document.add(output);
                    document.add(qrCodeImage);
                    document.add(CC);
                    document.add(garis);
                    document.add(judul);
                    document.add(isi);
                    document.add(table);
                    document.add(pcsm3);
                    document.add(input);
                    document.add(qrCodeImage);
                    document.add(CC);

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



    private String getIdJenisKayu(String namaJenisKayu) {
        if (namaJenisKayu != null) {
            return "IdJenisKayu";
        }
        return null;
    }

    private class SaveBongkarSusunTask extends AsyncTask<Void, Void, Boolean> {
        private String noBongkarSusun;
        private String noCC;

        public SaveBongkarSusunTask(String noBongkarSusun, String noCC) {
            this.noBongkarSusun = noBongkarSusun;
            this.noCC = noCC;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.BongkarSusunOutputCCAkhir (NoCCAkhir, NoBongkarSusun) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noCC);
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
                Toast.makeText(CrossCut.this, "Data berhasil disimpan.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SaveToDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private String noProduksi, noCC;

        public SaveToDatabaseTask(String noProduksi, String noCC) {
            this.noProduksi = noProduksi;
            this.noCC = noCC;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.CCAkhirProduksiOutput (NoProduksi, NoCCAkhir) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noProduksi);
                    ps.setString(2, noCC);

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
                Toast.makeText(CrossCut.this, "Data berhasil disimpan ke database.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CrossCut.this, "Gagal menyimpan data ke database.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private String noCC, dateCreate, time, idTelly, noSPK, noSPKasal, idGrade, idJenisKayu, idFJProfile;
        private int isReject, isLembur;

        public UpdateDatabaseTask(String noCC, String dateCreate, String time, String idTelly, String noSPK, String noSPKasal,
                                  String idGrade, String idJenisKayu, String idFJProfile,
                                  int isReject, int isLembur) {
            this.noCC = noCC;
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
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "UPDATE dbo.CCAkhir_h SET DateCreate = ?, Jam = ?, IdOrgTelly = ?, NoSPK = ?, NoSPKAsal = ?, IdGrade = ?, " +
                            "IdFJProfile = ?, IdFisik = 4, IdJenisKayu = ?, IdWarehouse = 4, IsReject = ?, IsLembur = ? WHERE NoCCAkhir = ?";

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
                    ps.setString(11, noCC);

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
    private class SetAndSaveNoCCTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            String newNoCC = null;
            if (con != null) {
                try {
                    String query = "SELECT MAX(NoCCAkhir) FROM dbo.CCAkhir_h";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        String lastNoCC = rs.getString(1);

                        if (lastNoCC != null && lastNoCC.startsWith("V.")) {
                            String numericPart = lastNoCC.substring(2);
                            int numericValue = Integer.parseInt(numericPart);
                            int newNumericValue = numericValue + 1;

                            newNoCC = "V." + String.format("%06d", newNumericValue);
                        }
                    }

                    rs.close();
                    ps.close();

                    if (newNoCC != null) {
                        String insertQuery = "INSERT INTO dbo.CCAkhir_h (NoCCAkhir) VALUES (?)";
                        PreparedStatement insertPs = con.prepareStatement(insertQuery);
                        insertPs.setString(1, newNoCC);
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
            return newNoCC;
        }

        @Override
        protected void onPostExecute(String newNoCC) {
            if (newNoCC != null) {
                NoCC.setText(newNoCC);
                Toast.makeText(CrossCut.this, "NoCCAkhir berhasil diatur dan disimpan.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Error", "Failed to set or save NoCCAkhir.");
                Toast.makeText(CrossCut.this, "Gagal mengatur atau menyimpan NoCCAkhir.", Toast.LENGTH_SHORT).show();
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
            if (!jenisKayuList.isEmpty()) {
                ArrayAdapter<JenisKayu> adapter = new ArrayAdapter<>(CrossCut.this, android.R.layout.simple_spinner_item, jenisKayuList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinKayuCC.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load jenis kayu.");
            }
        }
    }
    public class LoadJenisKayuTask2 extends AsyncTask<String, Void, List<JenisKayu>> {
        private String noCC;

        public LoadJenisKayuTask2(String noCC) {
            this.noCC = noCC;
        }

        @Override
        protected List<JenisKayu> doInBackground(String... params) {
            List<JenisKayu> jenisKayuList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT j.IdJenisKayu, j.Jenis " +
                            "FROM dbo.MstJenisKayu AS j " +
                            "INNER JOIN dbo.CCAkhir_h AS h ON h.IdJenisKayu = j.IdJenisKayu " +
                            "WHERE h.NoCCAkhir = ? AND j.enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noCC);

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
                ArrayAdapter<JenisKayu> adapter = new ArrayAdapter<>(CrossCut.this, android.R.layout.simple_spinner_item, jenisKayuList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinKayuCC.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load jenis kayu.");
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
                ArrayAdapter<Telly> adapter = new ArrayAdapter<>(CrossCut.this, android.R.layout.simple_spinner_item, tellyList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                SpinTellyCC.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load telly data.");
            }
        }
    }


    private class LoadTellyTask2 extends AsyncTask<String, Void, List<Telly>> {
        private String noCC;

        public LoadTellyTask2(String noCC) {
            this.noCC = noCC;
        }

        @Override
        protected List<Telly> doInBackground(String... params) {
            List<Telly> tellyList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT t.IdOrgTelly, t.NamaOrgTelly " +
                            "FROM dbo.MstOrgTelly AS t " +
                            "INNER JOIN dbo.CCAkhir_h AS h ON h.IdOrgTelly = t.IdOrgTelly " +
                            "WHERE h.NoCCAkhir = ? AND t.enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noCC);

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
                ArrayAdapter<Telly> adapter = new ArrayAdapter<>(CrossCut.this, android.R.layout.simple_spinner_item, tellyList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinTellyCC.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load telly data.");
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
                ArrayAdapter<SPK> adapter = new ArrayAdapter<>(CrossCut.this, android.R.layout.simple_spinner_item, spkList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSPKCC.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load SPK data.");
            }
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
            if (!spkAsalList.isEmpty()) {
                ArrayAdapter<SPKAsal> adapter = new ArrayAdapter<>(CrossCut.this, android.R.layout.simple_spinner_item, spkAsalList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSPKAsalCC.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load SPK data.");
            }
        }
    }

    private class LoadSPKTask2 extends AsyncTask<Void, Void, List<SPK>> {
        private String noCC;

        public LoadSPKTask2(String noCC) {
            this.noCC = noCC;
        }

        @Override
        protected List<SPK> doInBackground(Void... params) {
            List<SPK> spkList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT NoSPK FROM dbo.CCAkhir_h WHERE NoCCAkhir = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noCC);

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
                ArrayAdapter<SPK> adapter = new ArrayAdapter<>(CrossCut.this, android.R.layout.simple_spinner_item, spkList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSPKCC.setAdapter(adapter);

                SpinSPKCC.setEnabled(true);
            } else {
                Log.e("Error", "No SPK data found for the provided NoCCAkhir.");
                SpinSPKCC.setAdapter(null);
                SpinSPKCC.setEnabled(false);
                Toast.makeText(CrossCut.this, "Tidak ada data SPK yang ditemukan.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class LoadProfileTask extends AsyncTask<Void, Void, List<Profile>> {
        @Override
        protected List<Profile> doInBackground(Void... voids) {
            List<Profile> profileList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT Profile, IdFJProfile FROM dbo.MstFJProfile";
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
            if (!profileList.isEmpty()) {
                ArrayAdapter<Profile> adapter = new ArrayAdapter<>(CrossCut.this, android.R.layout.simple_spinner_item, profileList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinProfileCC.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load profile data.");
            }
        }
    }

    private class LoadProfileTask2 extends AsyncTask<String, Void, List<Profile>> {
        private String noCC;

        public LoadProfileTask2(String noCC) {
            this.noCC = noCC;
        }

        @Override
        protected List<Profile> doInBackground(String... voids) {
            List<Profile> profileList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT p.Profile, p.IdFJProfile " +
                            "FROM dbo.MstFJProfile AS p " +
                            "INNER JOIN dbo.CCAkhir_h AS h ON h.IdFJProfile = p.IdFJProfile " +
                            "WHERE h.NoCCAkhir = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noCC);

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
                ArrayAdapter<Profile> adapter = new ArrayAdapter<>(CrossCut.this, android.R.layout.simple_spinner_item, profileList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinProfileCC.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load profile data.");
            }
        }
    }

    private class LoadFisikTask extends AsyncTask<Void, Void, List<Fisik>> {
        @Override
        protected List<Fisik> doInBackground(Void... voids) {
            List<Fisik> fisikList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT NamaWarehouse FROM dbo.MstWarehouse";
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
                ArrayAdapter<Fisik> adapter = new ArrayAdapter<>(CrossCut.this, android.R.layout.simple_spinner_item, fisikList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinFisikCC.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load fisik data.");
            }
        }
    }

    private class LoadFisikTask2 extends AsyncTask<String, Void, List<Fisik>> {
        private String noCC;

        public LoadFisikTask2(String noCC) {
            this.noCC = noCC;
        }

        @Override
        protected List<Fisik> doInBackground(String... params) {
            List<Fisik> fisikList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT mw.NamaWarehouse " +
                            "FROM dbo.MstWarehouse mw " +
                            "INNER JOIN dbo.CCAkhir_h ccakhir ON mw.IdWarehouse = ccakhir.IdWarehouse " +
                            "WHERE ccakhir.NoCCAkhir = ?";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noCC);

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
                ArrayAdapter<Fisik> adapter = new ArrayAdapter<>(CrossCut.this,
                        android.R.layout.simple_spinner_item, fisikList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinFisikCC.setAdapter(adapter);
            } else {
                Log.e("Error", "No warehouse found.");
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(CrossCut.this,
                        android.R.layout.simple_spinner_item, new String[]{"Tidak ada Fisik"});
                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinFisikCC.setAdapter(emptyAdapter);
            }
        }
    }


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

                    String category = "CCAkhir";

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
                ArrayAdapter<Grade> adapter = new ArrayAdapter<>(CrossCut.this,
                        android.R.layout.simple_spinner_item, gradeList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeCC.setAdapter(adapter);
            } else {
                Log.e("Error", "Tidak ada grade");
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(CrossCut.this,
                        android.R.layout.simple_spinner_item, new String[]{"Pilih Menu"});
                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeCC.setAdapter(emptyAdapter);
            }
        }
    }

    private class LoadGradeTask2 extends AsyncTask<String, Void, List<Grade>> {
        private String noCC;

        public LoadGradeTask2(String noCC) {
            this.noCC = noCC;
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

                    String category = "CCAkhir";

                    String query = "SELECT DISTINCT a.IdGrade, a.NamaGrade " +
                            "FROM MstGrade a " +
                            "INNER JOIN MstGrade_d b ON a.IdGrade = b.IdGrade " +
                            "WHERE a.Enable = 1 AND b.IdJenisKayu = ? AND b.Category = ? AND b.NoCCAkhir = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setInt(1, idJenisKayu);
                    ps.setString(2, category);
                    ps.setString(3, noCC);
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
                ArrayAdapter<Grade> adapter = new ArrayAdapter<>(CrossCut.this,
                        android.R.layout.simple_spinner_item, gradeList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeCC.setAdapter(adapter);
            } else {
                Log.e("Error", "Tidak ada grade");
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(CrossCut.this,
                        android.R.layout.simple_spinner_item, new String[]{"Tidak ada grade"});
                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeCC.setAdapter(emptyAdapter);
            }
        }
    }


    private class LoadMesinTask extends AsyncTask<String, Void, List<Mesin>> {
        @Override
        protected List<Mesin> doInBackground(String... params) {
            List<Mesin> mesinList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String selectedDate = params[0];

                    String query = "SELECT a.IdMesin, b.NamaMesin, a.NoProduksi FROM dbo.CCAkhirProduksi_h a " +
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
                ArrayAdapter<Mesin> adapter = new ArrayAdapter<>(CrossCut.this, android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinMesinCC.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load mesin data.");
            }
        }
    }

    private class LoadMesinTask2 extends AsyncTask<Void, Void, List<Mesin>> {
        private String noCC;

        public LoadMesinTask2(String noCC) {
            this.noCC = noCC;
        }

        @Override
        protected List<Mesin> doInBackground(Void... params) {
            List<Mesin> mesinList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT b.NoProduksi, d.NamaMesin FROM CCAkhir_h a " +
                            "INNER JOIN CCAkhirProduksiOutput b ON b.NoCCAkhir = a.NoCCAkhir " +
                            "INNER JOIN CCAkhirProduksi_h c ON c.NoProduksi = b.NoProduksi " +
                            "INNER JOIN MstMesin d ON d.IdMesin = c.IdMesin " +
                            "WHERE a.NoCCAkhir = ?";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noCC);

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
                ArrayAdapter<Mesin> adapter = new ArrayAdapter<>(CrossCut.this, android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinMesinCC.setAdapter(adapter);

                radioButtonMesinCC.setEnabled(true);
                radioButtonBSusunCC.setEnabled(false);
            } else {
                Log.e("Error", "Failed to load mesin data.");
                radioButtonMesinCC.setEnabled(false);
                radioButtonBSusunCC.setEnabled(false);

                Toast.makeText(CrossCut.this, "Tidak ada data mesin yang ditemukan.", Toast.LENGTH_SHORT).show();
                SpinMesinCC.setAdapter(null);
            }
        }
    }


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
                ArrayAdapter<Susun> adapter = new ArrayAdapter<>(CrossCut.this, android.R.layout.simple_spinner_item, susunList);
                SpinSusunCC.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load susun data");
            }
        }
    }
    private class LoadSusunTask2 extends AsyncTask<Void, Void, List<Susun>> {
        private String noCC;

        public LoadSusunTask2(String noCC) {
            this.noCC = noCC;
        }

        @Override
        protected List<Susun> doInBackground(Void... params) {
            List<Susun> susunList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT NoBongkarSusun FROM dbo.BongkarSusunOutputCCAkhir WHERE NoCCAkhir = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noCC);
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
                ArrayAdapter<Susun> adapter = new ArrayAdapter<>(CrossCut.this, android.R.layout.simple_spinner_item, susunList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSusunCC.setAdapter(adapter);

                radioButtonMesinCC.setEnabled(false);
                radioButtonBSusunCC.setEnabled(true);
            } else {
                Log.e("Error", "Failed to load susun data.");
                radioButtonMesinCC.setEnabled(false);
                radioButtonBSusunCC.setEnabled(false);

                Toast.makeText(CrossCut.this, "Tidak ada data susun yang ditemukan.", Toast.LENGTH_SHORT).show();
                SpinSusunCC.setAdapter(null);
            }
        }
    }

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
        private String idWarehouse; // Jika diperlukan
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