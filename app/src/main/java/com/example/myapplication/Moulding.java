package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.kernel.colors.ColorConstants;
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

import java.text.DecimalFormat;
import android.view.Gravity;

public class Moulding extends AppCompatActivity {

    //deklarasi semua item yang ada di xml yang akan digunakan
    private EditText NoMoulding;
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
    private EditText DetailLebarM;
    private EditText DetailTebalM;
    private EditText DetailPanjangM;
    private EditText DetailPcsM;
    private static int currentNumber = 1;
    private Button BtnPrintM;
    private TextView M3M;
    private TextView JumlahPcsM;
    private boolean isCBAfkirM, isCBLemburM;
    private Button BtnSearchM;
    private int rowCount = 0;
    private TableLayout Tabel;

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
        BtnSearchM = findViewById(R.id.BtnSearchM);
        Tabel = findViewById(R.id.Tabel);

        BtnPrintM.setEnabled(false);

        //mengatur fungsu RadioButtonMesin
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
            if (!isDataBaruClickedM) {
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

                isDataBaruClickedM = true;
                setCurrentDateTime();
            } else {
                Toast.makeText(Moulding.this, "Tombol Data Baru sudah diklik. Klik Simpan terlebih dahulu.", Toast.LENGTH_SHORT).show();
            }
            BtnSimpanM.setEnabled(true);
            new SetAndSaveNoMouldingTask().execute();
            BtnPrintM.setEnabled(false);
            BtnBatalM.setEnabled(true);
            radioButtonMesinM.setEnabled(true);
            radioButtonBSusunM.setEnabled(true);
            setCurrentDateTime();
            clearTableData2();
            BtnDataBaruM.setEnabled(false);
        });

        //fungsi button Simpan
        BtnSimpanM.setOnClickListener(v -> {
            String noMoulding = NoMoulding.getText().toString();
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

            if (noMoulding.isEmpty() || dateCreate.isEmpty() || time.isEmpty() ||
                    selectedTelly == null ||
                    selectedSPK == null ||
                    selectedProfile == null ||
                    selectedFisik == null ||
                    selectedGrade == null ||
                    selectedJenisKayu == null ||
                    (!radioButtonMesinM.isChecked() && !radioButtonBSusunM.isChecked()) ||
                    (radioButtonMesinM.isChecked() && selectedMesin == null) ||
                    (radioButtonBSusunM.isChecked() && selectedSusun == null)) {

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
                    isLembur
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

            Toast.makeText(Moulding.this, "Data berhasil disimpan dan tampilan telah dikosongkan.", Toast.LENGTH_SHORT).show();

        });

        //fungsi button Batal
//       BtnBatalM.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String noFJoin = NoMoulding.getText().toString().trim();
//
//                if (!noFJoin.isEmpty()) {
//                    new DeleteDataTask().execute(noFJoin);
//                }
//
//                clearTableData2();
//                Toast.makeText(Moulding.this, "Tampilan telah dikosongkan.", Toast.LENGTH_SHORT).show();
//            }
//        });


        //fungsi button Search
        BtnSearchM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noMoulding = NoMoulding.getText().toString();


                DetailLebarM.setText("");
                DetailPanjangM.setText("");
                DetailTebalM.setText("");
                DetailPcsM.setText("");
                NoMoulding.setText("");

                if (!noMoulding.isEmpty()) {
                    new LoadMesinTask2(noMoulding).execute();
                    new LoadSusunTask2(noMoulding).execute();
                    new LoadFisikTask2(noMoulding).execute();
                    new LoadGradeTask2(noMoulding).execute();
                    new LoadJenisKayuTask2(noMoulding).execute();
                    new LoadTellyTask2(noMoulding).execute();
                    new LoadSPKTask2(noMoulding).execute();
                    new LoadProfileTask2(noMoulding).execute();
                    new SearchAllDataTask(noMoulding).execute();

                    radioButtonMesinM.setEnabled(true);
                    SpinSPKM.setEnabled(true);
                    radioButtonBSusunM.setEnabled(true);
                } else {
                    Log.e("Input Error", "NoMoulding is empty");
                    radioButtonMesinM.setEnabled(false);
                    radioButtonBSusunM.setEnabled(false);
                }

                BtnSimpanM.setEnabled(false);
                BtnBatalM.setEnabled(false);
                BtnPrintM.setEnabled(true);
            }
        });

        //fungsi spinner JenisKayu
        SpinKayuM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        //fungsi kolom tanggal
        DateM.setOnClickListener(v -> showDatePickerDialog());

        //fungsi kolom jam
        TimeM.setOnClickListener(v -> showTimePickerDialog());

        //fungsi button Input Detail
        BtnInputDetailM.setOnClickListener(v -> {
            String noMoulding = NoMoulding.getText().toString();

            if (!noMoulding.isEmpty()) {
                addDataDetail(noMoulding);
            } else {
                Toast.makeText(Moulding.this, "NoFJ tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            jumlahpcs();
        });

        //fungsi button Hapus Detail
        BtnHapusDetailM.setOnClickListener(v -> {
            resetDetailData();
        });

        //fungsi button Print
//        BtnPrintM.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//
//                    //deklarasikan semua data yang akan tampil di pdf, sesuai dari sumber nya masing seperti spinner/textview
//                    String noMoulding = NoMoulding.getText() != null ? NoMoulding.getText().toString() : "";
//                    String Kayu = SpinKayuM.getSelectedItem() != null ? SpinKayuM.getSelectedItem().toString() : "";
//                    String Grade = SpinGradeM.getSelectedItem() != null ? SpinGradeM.getSelectedItem().toString() : "";
//                    String Fisik = SpinFisikM.getSelectedItem() != null ? SpinFisikM.getSelectedItem().toString() : "";
//                    String Tanggal = DateM.getText() != null ? DateM.getText().toString() : "";
//                    String Waktu = TimeM.getText() != null ? TimeM.getText().toString() : "";
//                    String Telly = SpinTellyM.getSelectedItem() != null ? SpinTellyM.getSelectedItem().toString() : "";
//                    String Mesin = SpinMesinM.getSelectedItem() != null ? SpinMesinM.getSelectedItem().toString() : "";
//                    String noSPK = SpinSPKM.getSelectedItem() != null ? SpinSPKM.getSelectedItem().toString() : "";
//                    String jlh = JumlahPcsM.getText() != null ? JumlahPcsM.getText().toString() : "";
//                    String m3 = M3M.getText() != null ? M3M.getText().toString() : "";
//                    String Susun = SpinSusunM.getSelectedItem() != null ? SpinSusunM.getSelectedItem().toString() : "";
//
//                    Uri pdfUri = createPdf(noMoulding, Kayu, Grade, Fisik, Tanggal, Waktu, Telly, Mesin, Susun, noSPK, jlh, m3);
//
//                    if (pdfUri != null) {
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setDataAndType(pdfUri, "application/pdf");
//                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                        //aplikasi yang dipilih untuk membuka pdf
//                        intent.setPackage("com.mi.globalbrowser");
//
//                        try {
//                            startActivity(intent);
//                        } catch (ActivityNotFoundException e) {
//                            Toast.makeText(Moulding.this, "Mi Browser not found. Please install Mi Browser or use another app to open the PDF.", Toast.LENGTH_LONG).show();
//
//                            Intent fallbackIntent = new Intent(Intent.ACTION_VIEW);
//                            fallbackIntent.setDataAndType(pdfUri, "application/pdf");
//                            fallbackIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            startActivity(Intent.createChooser(fallbackIntent, "Open PDF with"));
//                        }
//                    } else {
//                        Toast.makeText(Moulding.this, "Error creating PDF", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(Moulding.this, "Error creating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(Moulding.this, "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//                clearTableData();
//            }
//        });
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

    //class untuk fungsi pencarian database sesuai dengan nomor moulding nya
    private class SearchAllDataTask extends AsyncTask<String, Void, Boolean> {
        private String noMoulding;

        public SearchAllDataTask(String noMoulding) {
            this.noMoulding = noMoulding;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("SearchAllDataTask", "Searching for NoMoulding: " + noMoulding);
            Connection con = ConnectionClass();
            boolean isDataFound = false;

            if (con != null) {
                try {
                    String query = "SELECT h.DateCreate, h.Jam, " +
                            "d.Lebar, d.Panjang, d.Tebal, d.JmlhBatang, d.NoUrut, " +
                            "h.IsReject, h.IsLembur " +
                            "FROM dbo.Moulding_h AS h " +
                            "INNER JOIN dbo.Moulding_d AS d ON h.NoMoulding = d.NoMoulding " +
                            "WHERE h.NoMoulding = ?";

                    Log.d("SearchAllDataTask", "Preparing statement: " + query);
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noMoulding);
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
                                NoMoulding.setText(noMoulding);
                                DateM.setText(dateCreate != null ? dateCreate : "");
                                TimeM.setText(jam != null ? jam : "");
                                CBAfkirM.setChecked(isReject);
                                CBLemburM.setChecked(isLembur);

                                m3();
                                jumlahpcs();
                            }
                        });

                        isDataFound = true;
                    } else {
                        Log.e("SearchAllDataTask", "No data found for NoMoulding: " + noMoulding);
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

    private void addDataDetail(String noMoulding) {
        String tebal = DetailTebalM.getText().toString();
        String panjang = DetailPanjangM.getText().toString();
        String lebar = DetailLebarM.getText().toString();
        String pcs = DetailPcsM.getText().toString();

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

    //// fungsi mengosongkan
    private void clearTableData2() {
        NoMoulding.setText("");
        M3M.setText("");
        JumlahPcsM.setText("");
        CBAfkirM.setChecked(false);
        CBLemburM.setChecked(false);

        currentNumber = 1;
    }

    //fungsi untuk mereset spinner kembali di tampilan awal
    private void resetSpinners() {
        if (SpinKayuM.getAdapter() != null) {
            SpinKayuM.setSelection(0);
        }
        if (SpinMesinM.getAdapter() != null) {
            SpinMesinM.setSelection(0);
        }
        if (SpinSusunM.getAdapter() != null) {
            SpinSusunM.setSelection(0);
        }
        if (SpinTellyM.getAdapter() != null) {
            SpinTellyM.setSelection(0);
        }
        if (SpinGradeM.getAdapter() != null) {
            SpinGradeM.setSelection(0);
        }
        if (SpinProfileM.getAdapter() != null) {
            SpinProfileM.setSelection(0);
        }
        if (SpinFisikM.getAdapter() != null) {
            SpinFisikM.setSelection(0);
        }
        if (SpinSPKM.getAdapter() != null) {
            SpinSPKM.setSelection(0);
        }
        if (SpinSPKAsalM.getAdapter() != null) {
            SpinSPKAsalM.setSelection(0);
        }

        BtnDataBaruM.setEnabled(true);
        isDataBaruClickedM = true;
    }

    // fungsi menghitung dan menampilkan jumlah M3
    private void m3() {
//        TextView tabeltebalTextView = TabelTebalM;
//        TextView tabelpanjangTextView = TabelPanjangM;
//        TextView tabellebarTextView = TabelLebarM;
//        TextView tabelpcsTextView = TabelPcsM;
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
//        TextView M3 = findViewById(R.id.M3M);
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
////fungsi untuk memunculkan picker waktu
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

    //fungsi untuk mendelarasikan pencetakan PDF
     private Uri createPdf(String noMoulding, String Kayu, String Grade, String Fisik, String Tanggal, String Waktu, String Telly, String Mesin, String Susun, String noSPK, String tebal, String lebar, String panjang, String pcs, String jlh, String m3) throws IOException {
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

                    Paragraph judul = new Paragraph("LABEL Moulding\n").setBold().setFontSize(8).setTextAlignment(TextAlignment.CENTER);
                    Paragraph isi = new Paragraph("").setFontSize(7)
                            .add("No Moulding  : " + noMoulding + "                                              ")
                            .add("Tanggal : " + Tanggal + " " + Waktu + "\n")
                            .add("Kayu    : " + Kayu + "                                                    ")
                            .add("Telly   : " + Telly + "\n")
                            .add("Grade   : " + Grade + "                                          ")
                            .add(mesinAtauSusun + "\n")
                            .add("Fisik   : " + Fisik + "                                                           ")
                            .add("No SPK  : " + noSPK + "\n");

                    //mengatur tabel
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

                    //mengatur barcode
                    BarcodeQRCode qrCode = new BarcodeQRCode(noMoulding);
                    PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
                    Image qrCodeImage = new Image(qrCodeObject).setWidth(45).setHorizontalAlignment(HorizontalAlignment.CENTER).setMarginBottom(0).setMarginTop(0);

                    //mengatur penampilan m3 dan jumlah batang
                    Paragraph pcsm3 = new Paragraph("").setFontSize(7).setTextAlignment(TextAlignment.RIGHT).setMarginRight(67)
                            .add("Jmlh Pcs = " + jlh + "\t" + "\n")
                            .add("m3 = " + m3 + "\n");

                    Paragraph garis = new Paragraph("--------------------------------------------------------------").setTextAlignment(TextAlignment.CENTER);
                    Paragraph output = new Paragraph("Output").setTextAlignment(TextAlignment.CENTER).setFontSize(6).setMarginTop(0).setMarginBottom(0);
                    Paragraph input = new Paragraph("Input").setTextAlignment(TextAlignment.CENTER).setFontSize(6).setMarginTop(0).setMarginBottom(0);
                    Paragraph Moulding = new Paragraph(noMoulding).setTextAlignment(TextAlignment.CENTER).setFontSize(6).setMarginTop(0).setMarginBottom(0);

                    //mengatur urutan tampilan berdasarkan yang sudah dideklarasikan diatas dan dapat diulangi sesuai kebutuhan
                    document.add(judul);
                    document.add(isi);
                    document.add(table);
                    document.add(pcsm3);
                    document.add(output);
                    document.add(qrCodeImage);
                    document.add(Moulding);
                    document.add(garis);
                    document.add(judul);
                    document.add(isi);
                    document.add(table);
                    document.add(pcsm3);
                    document.add(input);
                    document.add(qrCodeImage);
                    document.add(Moulding);

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
        private int isReject, isLembur;

        public UpdateDatabaseTask(String noMoulding, String dateCreate, String time, String idTelly, String noSPK, String noSPKasal,
                                  String idGrade, String idJenisKayu, String idFJProfile,
                                  int isReject, int isLembur) {
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
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "UPDATE dbo.Moulding_h SET DateCreate = ?, Jam = ?, IdOrgTelly = ?, NoSPK = ?, NoSPKAsal = ?, IdGrade = ?, " +
                            "IdFJProfile = ?, IdFisik = 4, IdJenisKayu = ?, IdWarehouse = 4, IsReject = ?, IsLembur = ? WHERE NoMoulding = ?";

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
                    ps.setString(11, noMoulding);

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
                NoMoulding.setText(newNoMoulding);
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
            if (!jenisKayuList.isEmpty()) {
                ArrayAdapter<JenisKayu> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, jenisKayuList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinKayuM.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load jenis kayu.");
            }
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
            if (!tellyList.isEmpty()) {
                ArrayAdapter<Telly> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, tellyList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                SpinTellyM.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load telly data.");
            }
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
            if (!spkList.isEmpty()) {
                ArrayAdapter<SPK> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, spkList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSPKM.setAdapter(adapter);
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
                ArrayAdapter<SPKAsal> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, spkAsalList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSPKAsalM.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load SPK data.");
            }
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
                ArrayAdapter<Profile> adapter = new ArrayAdapter<>(Moulding.this, android.R.layout.simple_spinner_item, profileList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinProfileM.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load profile data.");
            }
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
                ArrayAdapter<Grade> adapter = new ArrayAdapter<>(Moulding.this,
                        android.R.layout.simple_spinner_item, gradeList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeM.setAdapter(adapter);
            } else {
                Log.e("Error", "Tidak ada grade");
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(Moulding.this,
                        android.R.layout.simple_spinner_item, new String[]{"Pilih Menu"});
                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeM.setAdapter(emptyAdapter);
            }
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