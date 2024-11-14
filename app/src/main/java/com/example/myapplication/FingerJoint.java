package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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

public class FingerJoint extends AppCompatActivity {
    private EditText NoFJ;
    private EditText DateFJ;
    private EditText TimeFJ;
    private EditText NoSTAFJ;
    private Spinner SpinKayuFJ;
    private Spinner SpinTellyFJ;
    private Spinner SpinSPKFJ;
    private Spinner SpinSPKAsalFJ;
    private Spinner SpinProfileFJ;
    private Spinner SpinFisikFJ;
    private Spinner SpinGradeFJ;
    private Spinner SpinMesinFJ;
    private Spinner SpinSusunFJ;
    private Calendar calendarFJ;
    private RadioGroup radioGroupFJ;
    private RadioButton radioButtonMesinFJ;
    private RadioButton radioButtonBSusunFJ;
    private Button BtnDataBaruFJ;
    private Button BtnSimpanFJ;
    private Button BtnBatalFJ;
    private Button BtnHapusDetailFJ;
    private boolean isDataBaruFJClicked = false;
    private CheckBox CBAfkirFJ;
    private CheckBox CBLemburFJ;
    private Button BtnInputDetailFJ;
    private EditText DetailLebarFJ;
    private EditText DetailTebalFJ;
    private EditText DetailPanjangFJ;
    private EditText DetailPcsFJ;
    private static int currentNumber = 1;
    private Button BtnPrintFJ;
    private TextView M3FJ;
    private TextView JumlahPcsFJ;
    private boolean isCBAfkirFJ, isCBLemburFJ;
    private Button BtnSearchFJ;
    private int rowCount = 0;
    private TableLayout Tabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_finger_joint);

        NoSTAFJ = findViewById(R.id.NoSTAFJ);
        NoFJ = findViewById(R.id.NoFJ);
        DateFJ = findViewById(R.id.DateFJ);
        TimeFJ = findViewById(R.id.TimeFJ);
        SpinKayuFJ = findViewById(R.id.SpinKayuFJ);
        SpinTellyFJ = findViewById(R.id.SpinTellyFJ);
        SpinSPKFJ = findViewById(R.id.SpinSPKFJ);
        SpinSPKAsalFJ = findViewById(R.id.SpinSPKAsalFJ);
        SpinProfileFJ = findViewById(R.id.SpinProfileFJ);
        SpinFisikFJ = findViewById(R.id.SpinFisikFJ);
        SpinGradeFJ = findViewById(R.id.SpinGradeFJ);
        calendarFJ = Calendar.getInstance();
        SpinMesinFJ = findViewById(R.id.SpinMesinFJ);
        SpinSusunFJ = findViewById(R.id.SpinSusunFJ);
        radioButtonMesinFJ = findViewById(R.id.radioButtonMesinFJ);
        radioButtonBSusunFJ = findViewById(R.id.radioButtonBSusunFJ);
        BtnDataBaruFJ = findViewById(R.id.BtnDataBaruFJ);
        BtnSimpanFJ = findViewById(R.id.BtnSimpanFJ);
        BtnBatalFJ = findViewById(R.id.BtnBatalFJ);
        BtnHapusDetailFJ = findViewById(R.id.BtnHapusDetailFJ);
        CBLemburFJ = findViewById(R.id.CBLemburFJ);
        CBAfkirFJ = findViewById(R.id.CBAfkirFJ);
        BtnInputDetailFJ = findViewById(R.id.BtnInputDetailFJ);
        DetailPcsFJ = findViewById(R.id.DetailPcsFJ);
        DetailTebalFJ = findViewById(R.id.DetailTebalFJ);
        DetailPanjangFJ = findViewById(R.id.DetailPanjangFJ);
        DetailLebarFJ = findViewById(R.id.DetailLebarFJ);
        BtnPrintFJ = findViewById(R.id.BtnPrintFJ);
        M3FJ = findViewById(R.id.M3FJ);
        JumlahPcsFJ = findViewById(R.id.JumlahPcsFJ);
        BtnSearchFJ = findViewById(R.id.BtnSearchFJ);
        Tabel = findViewById(R.id.Tabel);

        BtnPrintFJ.setEnabled(false);

        radioButtonMesinFJ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpinMesinFJ.setEnabled(true);
                    SpinSusunFJ.setEnabled(false);
                }
            }
        });

        radioButtonBSusunFJ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpinSusunFJ.setEnabled(true);
                    SpinMesinFJ.setEnabled(false);
                }
            }
        });

        setCurrentDateTimeFJ();

        BtnDataBaruFJ.setOnClickListener(v -> {
            if (!isDataBaruFJClicked) {
                resetSpinnersFJ();
                new LoadJenisKayuTaskFJ().execute();
                new LoadTellyTaskFJ().execute();
                new LoadSPKTaskFJ().execute();
                new LoadSPKAsalTaskFJ().execute();
                new LoadProfileTaskFJ().execute();
                new LoadFisikTaskFJ().execute();
                new LoadGradeTaskFJ().execute();
                new LoadMesinTaskFJ().execute();
                new LoadSusunTaskFJ().execute();

                isDataBaruFJClicked = true;
                setCurrentDateTimeFJ();
            } else {
                Toast.makeText(FingerJoint.this,
                        "Tombol Data Baru sudah diklik. Klik Simpan terlebih dahulu.",
                        Toast.LENGTH_SHORT).show();
            }

            BtnSimpanFJ.setEnabled(true);
            new SetAndSaveNoFJoinTaskFJ().execute();
            BtnPrintFJ.setEnabled(false);
            BtnBatalFJ.setEnabled(true);
            radioButtonMesinFJ.setEnabled(true);
            radioButtonBSusunFJ.setEnabled(true);
            setCurrentDateTimeFJ();
            clearTableData2FJ();
            BtnDataBaruFJ.setEnabled(false);
        });

        BtnSimpanFJ.setOnClickListener(v -> {
            String noFJ = NoFJ.getText().toString();
            String dateCreate = DateFJ.getText().toString();
            String time = TimeFJ.getText().toString();

            TellyFJ selectedTelly = (TellyFJ) SpinTellyFJ.getSelectedItem();
            SPKFJ selectedSPK = (SPKFJ) SpinSPKFJ.getSelectedItem();
            SPKAsalFJ selectedSPKasal = (SPKAsalFJ) SpinSPKAsalFJ.getSelectedItem();
            ProfileFJ selectedProfile = (ProfileFJ) SpinProfileFJ.getSelectedItem();
            FisikFJ selectedFisik = (FisikFJ) SpinFisikFJ.getSelectedItem();
            GradeFJ selectedGrade = (GradeFJ) SpinGradeFJ.getSelectedItem();
            JenisKayuFJ selectedJenisKayu = (JenisKayuFJ) SpinKayuFJ.getSelectedItem();
            MesinFJ selectedMesin = (MesinFJ) SpinMesinFJ.getSelectedItem();
            SusunFJ selectedSusun = (SusunFJ) SpinSusunFJ.getSelectedItem();

            String idGrade = selectedGrade != null ? selectedGrade.getIdGrade() : null;
            String idTelly = selectedTelly != null ? selectedTelly.getIdTelly() : null;
            String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
            String noSPKasal = selectedSPKasal != null ? selectedSPKasal.getNoSPKAsal() : null;
            String idProfile = selectedProfile != null ? selectedProfile.getIdFJProfile() : null;
            String idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;
            String noProduksi = selectedMesin != null ? selectedMesin.getNoProduksi() : null;
            String noBongkarSusun = selectedSusun != null ? selectedSusun.getNoBongkarSusun() : null;
            int isReject = CBAfkirFJ.isChecked() ? 1 : 0;
            int isLembur = CBLemburFJ.isChecked() ? 1 : 0;

            if (noFJ.isEmpty() || dateCreate.isEmpty() || time.isEmpty() ||
                    selectedTelly == null ||
                    selectedSPK == null ||
                    selectedProfile == null ||
                    selectedFisik == null ||
                    selectedGrade == null ||
                    selectedJenisKayu == null ||
                    (!radioButtonMesinFJ.isChecked() && !radioButtonBSusunFJ.isChecked()) ||
                    (radioButtonMesinFJ.isChecked() && selectedMesin == null) ||
                    (radioButtonBSusunFJ.isChecked() && selectedSusun == null)) {

                Toast.makeText(FingerJoint.this, "Pastikan semua field terisi dengan benar.", Toast.LENGTH_SHORT).show();
                return;
            }
            BtnDataBaruFJ.setEnabled(true);
            BtnPrintFJ.setEnabled(true);


            new UpdateDatabaseTaskFJ(
                    noFJ,
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

            if (radioButtonMesinFJ.isChecked() && SpinMesinFJ.isEnabled() && noProduksi != null) {
                new SaveToDatabaseTaskFJ(noProduksi, noFJ).execute();
                for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                    FingerJoint.DataRow dataRow = temporaryDataListDetail.get(i);
                    saveDataDetailToDatabase(noFJ, i + 1, Double.parseDouble(dataRow.tebal), Double.parseDouble(dataRow.lebar),
                            Double.parseDouble(dataRow.panjang), Integer.parseInt(dataRow.pcs));
                }
            } else if (radioButtonBSusunFJ.isChecked() && SpinSusunFJ.isEnabled() && noBongkarSusun != null) {
                new SaveBongkarSusunTaskFJ(noBongkarSusun, noFJ).execute();
                for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                    FingerJoint.DataRow dataRow = temporaryDataListDetail.get(i);
                    saveDataDetailToDatabase(noFJ, i + 1, Double.parseDouble(dataRow.tebal), Double.parseDouble(dataRow.lebar),
                            Double.parseDouble(dataRow.panjang), Integer.parseInt(dataRow.pcs));
                }
            } else {
                Toast.makeText(FingerJoint.this, "Pilih opsi yang valid untuk disimpan.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(FingerJoint.this, "Data berhasil disimpan dan tampilan telah dikosongkan.", Toast.LENGTH_SHORT).show();

        });

        BtnBatalFJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noFJ = NoFJ.getText().toString().trim();

                if (!noFJ.isEmpty()) {
                    new DeleteDataTaskFJ().execute(noFJ);
                }

                clearTableData2FJ();
                clearTableDataFJ();
                Toast.makeText(FingerJoint.this, "Tampilan telah dikosongkan.", Toast.LENGTH_SHORT).show();
            }
        });

        BtnSearchFJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noFJ = NoFJ.getText().toString();

                if (SpinFisikFJ.getAdapter() != null) {
                    SpinFisikFJ.setSelection(0);
                }
                if (SpinKayuFJ.getAdapter() != null) {
                    SpinKayuFJ.setSelection(0);
                }
                if (SpinSPKFJ.getAdapter() != null) {
                    SpinSPKFJ.setSelection(0);
                }
                if (SpinSPKAsalFJ.getAdapter() != null) {
                    SpinSPKAsalFJ.setSelection(0);
                }
                if (SpinTellyFJ.getAdapter() != null) {
                    SpinTellyFJ.setSelection(0);
                }
                if (SpinProfileFJ.getAdapter() != null) {
                    SpinProfileFJ.setSelection(0);
                }

                if (!noFJ.isEmpty()) {
                    new LoadJenisKayuTask2FJ(noFJ).execute();
                    new LoadTellyTask2FJ(noFJ).execute();
                    new LoadSPKTask2FJ(noFJ).execute();
                    new LoadProfileTask2FJ(noFJ).execute();
                    new SearchAllDataTaskFJ(noFJ).execute();
                    new LoadGradeTask2FJ(noFJ).execute();
                    new LoadMesinTask2FJ(noFJ).execute();
                    new LoadSusunTask2FJ(noFJ).execute();
                    new LoadFisikTask2FJ(noFJ).execute();

                    radioButtonMesinFJ.setEnabled(true);
                    SpinSPKFJ.setEnabled(true);
                    SpinSPKAsalFJ.setEnabled(true);
                    radioButtonBSusunFJ.setEnabled(true);
                } else {
                    Log.e("Input Error", "No Finger Join is empty");
                    radioButtonMesinFJ.setEnabled(false);
                    radioButtonBSusunFJ.setEnabled(false);
                }
                BtnSimpanFJ.setEnabled(false);
                BtnBatalFJ.setEnabled(false);
                BtnPrintFJ.setEnabled(true);
            }
        });


        SpinKayuFJ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JenisKayuFJ selectedJenisKayu = (JenisKayuFJ) parent.getItemAtPosition(position);
                String idJenisKayu = selectedJenisKayu.getIdJenisKayu();
                new LoadGradeTaskFJ().execute(idJenisKayu);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        DateFJ.setOnClickListener(v -> showDatePickerDialog());

        TimeFJ.setOnClickListener(v -> showTimePickerDialog());

        BtnInputDetailFJ.setOnClickListener(v -> {
            String noFJ = NoFJ.getText().toString();

            if (!noFJ.isEmpty()) {
                addDataDetail(noFJ);
            } else {
                Toast.makeText(FingerJoint.this, "NoFJ tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            jumlahpcs();
        });

        BtnHapusDetailFJ.setOnClickListener(v -> {
            resetDetailData();

        });

//        BtnPrintFJ.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    String noFJ = NoFJ.getText() != null ? NoFJ.getText().toString() : "";
//                    String Kayu = SpinKayuFJ.getSelectedItem() != null ? SpinKayuFJ.getSelectedItem().toString() : "";
//                    String Grade = SpinGradeFJ.getSelectedItem() != null ? SpinGradeFJ.getSelectedItem().toString() : "";
//                    String Fisik = SpinFisikFJ.getSelectedItem() != null ? SpinFisikFJ.getSelectedItem().toString() : "";
//                    String Tanggal = DateFJ.getText() != null ? DateFJ.getText().toString() : "";
//                    String Waktu = TimeFJ.getText() != null ? TimeFJ.getText().toString() : "";
//                    String Telly = SpinTellyFJ.getSelectedItem() != null ? SpinTellyFJ.getSelectedItem().toString() : "";
//                    String Mesin = SpinMesinFJ.getSelectedItem() != null ? SpinMesinFJ.getSelectedItem().toString() : "";
//                    String noSPK = SpinSPKFJ.getSelectedItem() != null ? SpinSPKFJ.getSelectedItem().toString() : "";
//                    String jlh = JumlahPcsFJ.getText() != null ? JumlahPcsFJ.getText().toString() : "";
//                    String m3 = M3FJ.getText() != null ? M3FJ.getText().toString() : "";
//                    String Susun = SpinSusunFJ.getSelectedItem() != null ? SpinSusunFJ.getSelectedItem().toString() : "";
//
//                    Uri pdfUri = createPdfFJ(noFJ, Kayu, Grade, Fisik, Tanggal, Waktu, Telly, Mesin, Susun, noSPK, tebal, lebar, panjang, pcs, jlh, m3);
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
//                            Toast.makeText(FingerJoint.this, "Mi Browser not found. Please install Mi Browser or use another app to open the PDF.", Toast.LENGTH_LONG).show();
//
//                            Intent fallbackIntent = new Intent(Intent.ACTION_VIEW);
//                            fallbackIntent.setDataAndType(pdfUri, "application/pdf");
//                            fallbackIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            startActivity(Intent.createChooser(fallbackIntent, "Open PDF with"));
//                        }
//                    } else {
//                        Toast.makeText(FingerJoint.this, "Error creating PDF", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(FingerJoint.this, "Error creating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(FingerJoint.this, "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }

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

    private String getIdJenisKayuStr() {
        if (SpinKayuFJ.getSelectedItem() != null) {
            JenisKayuFJ selectedJenisKayu = (JenisKayuFJ) SpinKayuFJ.getSelectedItem();
            return selectedJenisKayu.getIdJenisKayu();
        }
        return "";
    }


    private void setCurrentDateTimeFJ() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        DateFJ.setText(currentDate);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        TimeFJ.setText(currentTime);
    }

    private class LoadProfileTask2FJ extends AsyncTask<String, Void, List<ProfileFJ>> {
        private String noFJ;

        public LoadProfileTask2FJ(String noFJ) {
            this.noFJ = noFJ;
        }

        @Override
        protected List<ProfileFJ> doInBackground(String... voids) {
            List<ProfileFJ> profileList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT p.Profile, p.IdFJProfile " +
                            "FROM dbo.MstFJProfile AS p " +
                            "INNER JOIN dbo.FJ_h AS h ON h.IdFJProfile = p.IdFJProfile " +
                            "WHERE h.NoFJ = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String namaProfile = rs.getString("Profile");
                        String idFJProfile = rs.getString("IdFJProfile");
                        ProfileFJ profileObj = new ProfileFJ(namaProfile, idFJProfile);
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
        protected void onPostExecute(List<ProfileFJ> profileList) {
            if (!profileList.isEmpty()) {
                ArrayAdapter<ProfileFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, profileList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinProfileFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load profile data.");
            }
        }
    }


    private class SearchAllDataTaskFJ extends AsyncTask<String, Void, Boolean> {
        private String noFJ;

        public SearchAllDataTaskFJ(String noFJ) {
            this.noFJ = noFJ;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("SearchAllDataTask", "Searching for NoFJ: " + noFJ);
            Connection con = ConnectionClass();
            boolean isDataFound = false;

            if (con != null) {
                try {
                    String query = "SELECT h.DateCreate, h.Jam, " +
                            "d.Lebar, d.Panjang, d.Tebal, d.JmlhBatang, d.NoUrut, " +
                            "h.IsReject, h.IsLembur " +
                            "FROM dbo.FJ_h AS h " +
                            "INNER JOIN dbo.FJ_d AS d ON h.NoFJ = d.NoFJ " +
                            "WHERE h.NoFJ = ?";

                    Log.d("SearchAllDataTask", "Preparing statement: " + query);
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);
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
                                NoFJ.setText(noFJ);
                                DateFJ.setText(dateCreate != null ? dateCreate : "");
                                TimeFJ.setText(jam != null ? jam : "");
                                CBAfkirFJ.setChecked(isReject);
                                CBLemburFJ.setChecked(isLembur);

                                m3FJ();
                                jumlahpcs();
                            }
                        });

                        isDataFound = true;
                    } else {
                        Log.e("SearchAllDataTask", "No data found for NoFJ: " + noFJ);
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

    private void addDataDetail(String noFJ) {
        String tebal = DetailTebalFJ.getText().toString();
        String panjang = DetailPanjangFJ.getText().toString();
        String lebar = DetailLebarFJ.getText().toString();
        String pcs = DetailPcsFJ.getText().toString();

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
            DetailTebalFJ.setText("");
            DetailPanjangFJ.setText("");
            DetailLebarFJ.setText("");
            DetailPcsFJ.setText("");

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
        if (DetailTebalFJ != null) {
            DetailTebalFJ.setText("");
        }
        if (DetailLebarFJ != null) {
            DetailLebarFJ.setText("");
        }
        if (DetailPanjangFJ != null) {
            DetailPanjangFJ.setText("");
        }
        if (DetailPcsFJ != null) {
            DetailPcsFJ.setText("");
        }
    }

    private void saveDataDetailToDatabase(String noFJ, int noUrut, double tebal, double lebar, double panjang, int pcs) {
        new FingerJoint.SaveDataTaskDetail().execute(noFJ, String.valueOf(noUrut), String.valueOf(tebal), String.valueOf(lebar),
                String.valueOf(panjang), String.valueOf(pcs));
    }

    private class SaveDataTaskDetail extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String noFJ = params[0];
            String noUrut = params[1];
            String tebal = params[2];
            String lebar = params[3];
            String panjang = params[4];
            String pcs = params[5];

            try {
                Connection connection = ConnectionClass();
                if (connection != null) {
                    String query = "INSERT INTO dbo.FJ_d (NoFJ, NoUrut, Tebal, Lebar, Panjang, JmlhBatang) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, noFJ);
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

    private void clearTableDataFJ() {
        DetailPanjangFJ.setText("");
        DetailTebalFJ.setText("");
        DetailLebarFJ.setText("");
        DetailPcsFJ.setText("");

        currentNumber = 1;
    }

    private void clearTableData2FJ() {
        NoFJ.setText("");
        M3FJ.setText("");
        JumlahPcsFJ.setText("");
        CBAfkirFJ.setChecked(false);
        CBLemburFJ.setChecked(false);

        currentNumber = 1;
    }

    private void resetSpinnersFJ() {
        if (SpinKayuFJ.getAdapter() != null) {
            SpinKayuFJ.setSelection(0);
        }
        if (SpinMesinFJ.getAdapter() != null) {
            SpinMesinFJ.setSelection(0);
        }
        if (SpinSusunFJ.getAdapter() != null) {
            SpinSusunFJ.setSelection(0);
        }
        if (SpinTellyFJ.getAdapter() != null) {
            SpinTellyFJ.setSelection(0);
        }
        if (SpinGradeFJ.getAdapter() != null) {
            SpinGradeFJ.setSelection(0);
        }
        if (SpinProfileFJ.getAdapter() != null) {
            SpinProfileFJ.setSelection(0);
        }
        if (SpinFisikFJ.getAdapter() != null) {
            SpinFisikFJ.setSelection(0);
        }
        if (SpinSPKFJ.getAdapter() != null) {
            SpinSPKFJ.setSelection(0);
        }
        if (SpinSPKAsalFJ.getAdapter() != null) {
            SpinSPKAsalFJ.setSelection(0);
        }

        BtnDataBaruFJ.setEnabled(true);
        isDataBaruFJClicked = true;
    }

    private void m3FJ() {

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

        JumlahPcsFJ.setText(String.valueOf(totalPcs));
    }


    private void setCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        DateFJ.setText(currentDate);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        TimeFJ.setText(currentTime);
    }

    private void showDatePickerDialog() {
        Calendar calendarFJ = Calendar.getInstance();
        int year = calendarFJ.get(Calendar.YEAR);
        int month = calendarFJ.get(Calendar.MONTH);
        int day = calendarFJ.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(FingerJoint.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                String selectedDate = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);
                DateFJ.setText(selectedDate);
                new LoadMesinTaskFJ().execute(selectedDate);
                new LoadSusunTaskFJ().execute(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        int hour = calendarFJ.get(Calendar.HOUR_OF_DAY);
        int minute = calendarFJ.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(FingerJoint.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                calendarFJ.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendarFJ.set(Calendar.MINUTE, selectedMinute);

                SimpleDateFormat timeFormat = new SimpleDateFormat(" HH:mm:ss", Locale.getDefault());
                String updatedTime = timeFormat.format(calendarFJ.getTime());
                TimeFJ.setText(updatedTime);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private Uri createPdfFJ(String noFJ, String Kayu, String Grade, String Fisik, String Tanggal, String Waktu, String Telly, String Mesin, String Susun, String noSPK, String tebal, String lebar, String panjang, String pcs, String jlh, String m3) throws IOException {
        Uri pdfUri = null;
        ContentResolver resolver = getContentResolver();
        String fileName = "myPDF.pdf";
        String relativePath = Environment.DIRECTORY_DOWNLOADS;

        deleteExistingPdfFJ(fileName, relativePath);

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

                    Paragraph judul = new Paragraph("LABEL FINGER JOIN\n").setBold().setFontSize(8).setTextAlignment(TextAlignment.CENTER);
                    Paragraph isi = new Paragraph("").setFontSize(7)
                            .add("No FJoin  : " + noFJ + "                                              ")
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

                    BarcodeQRCode qrCode = new BarcodeQRCode(noFJ);
                    PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
                    Image qrCodeImage = new Image(qrCodeObject).setWidth(45).setHorizontalAlignment(HorizontalAlignment.CENTER).setMarginBottom(0).setMarginTop(0);

                    Paragraph pcsm3 = new Paragraph("").setFontSize(7).setTextAlignment(TextAlignment.RIGHT).setMarginRight(67)
                            .add("Jmlh Pcs = " + jlh + "\t" + "\n")
                            .add("m3 = " + m3 + "\n");

                    Paragraph garis = new Paragraph("--------------------------------------------------------------").setTextAlignment(TextAlignment.CENTER);
                    Paragraph output = new Paragraph("Output").setTextAlignment(TextAlignment.CENTER).setFontSize(6).setMarginTop(0).setMarginBottom(0);
                    Paragraph input = new Paragraph("Input").setTextAlignment(TextAlignment.CENTER).setFontSize(6).setMarginTop(0).setMarginBottom(0);
                    Paragraph fjoin = new Paragraph(noFJ).setTextAlignment(TextAlignment.CENTER).setFontSize(6).setMarginTop(0).setMarginBottom(0);
                    document.add(judul);
                    document.add(isi);
                    document.add(table);
                    document.add(pcsm3);
                    document.add(output);
                    document.add(qrCodeImage);
                    document.add(fjoin);
                    document.add(garis);
                    document.add(judul);
                    document.add(isi);
                    document.add(table);
                    document.add(pcsm3);
                    document.add(input);
                    document.add(qrCodeImage);
                    document.add(fjoin);

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

    private void deleteExistingPdfFJ(String fileName, String relativePath) {
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

    private class SaveBongkarSusunTaskFJ extends AsyncTask<Void, Void, Boolean> {
        private String noBongkarSusun;
        private String noFJ;

        public SaveBongkarSusunTaskFJ(String noBongkarSusun, String noFJ) {
            this.noBongkarSusun = noBongkarSusun;
            this.noFJ = noFJ;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.BongkarSusunOutputFJ (NoFJ, NoBongkarSusun) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);
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
                Toast.makeText(FingerJoint.this, "Data berhasil disimpan.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SaveToDatabaseTaskFJ extends AsyncTask<Void, Void, Boolean> {
        private String noProduksi, noFJ;

        public SaveToDatabaseTaskFJ(String noProduksi, String noFJ) {
            this.noProduksi = noProduksi;
            this.noFJ = noFJ;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.FJProduksiOutput (NoProduksi, NoFJ) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noProduksi);
                    ps.setString(2, noFJ);

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
                Toast.makeText(FingerJoint.this, "Data berhasil disimpan ke database.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FingerJoint.this, "Gagal menyimpan data ke database.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    

    private class DeleteDataTaskFJ extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String noFJ = params[0];
            Connection con = ConnectionClass();
            boolean success = false;

            if (con != null) {
                try {
                    String query = "DELETE FROM dbo.FJ_h WHERE NoFJ = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);

                    int rowsAffected = ps.executeUpdate();
                    success = rowsAffected > 0;

                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("Database Error", e.getMessage());
                }
            } else {
                Log.e("Connection Error", "Failed to connect to the database.");
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(FingerJoint.this, "Data berhasil dihapus.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FingerJoint.this, "Gagal menghapus data.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateDatabaseTaskFJ extends AsyncTask<Void, Void, Boolean> {
        private String noFJ, dateCreate, time, idTelly, noSPK, noSPKasal, idGrade, idJenisKayu, idFJProfile;
        private int isReject, isLembur;

        public UpdateDatabaseTaskFJ(String noFJ, String dateCreate, String time, String idTelly, String noSPK,String noSPKasal,
                                    String idGrade, String idJenisKayu, String idFJProfile,
                                    int isReject, int isLembur) {
            this.noFJ = noFJ;
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
                    String query = "UPDATE dbo.FJ_h SET DateCreate = ?, Jam = ?, IdOrgTelly = ?, NoSPK = ?, NoSPKAsal = ?, IdGrade = ?, " +
                            "IdFJProfile = ?, IdFisik = 5, IdJenisKayu = ?, IdWarehouse = 5, IsReject = ?, IsLembur = ? WHERE NoFJ = ?";

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
                    ps.setString(11, noFJ);

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

    private class SetAndSaveNoFJoinTaskFJ extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            String newNoJoin = null;
            if (con != null) {
                try {
                    String query = "SELECT MAX(NoFJ) FROM dbo.FJ_h";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        String lastNoFJoin = rs.getString(1);

                        if (lastNoFJoin != null && lastNoFJoin.startsWith("S.")) {
                            String numericPart = lastNoFJoin.substring(2);
                            int numericValue = Integer.parseInt(numericPart);
                            int newNumericValue = numericValue + 1;

                            newNoJoin = "S." + String.format("%06d", newNumericValue);
                        }
                    }

                    rs.close();
                    ps.close();

                    if (newNoJoin != null) {
                        String insertQuery = "INSERT INTO dbo.FJ_h (NoFJ) VALUES (?)";
                        PreparedStatement insertPs = con.prepareStatement(insertQuery);
                        insertPs.setString(1, newNoJoin);
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
            return newNoJoin;
        }

        @Override
        protected void onPostExecute(String newNoJoin) {
            if (newNoJoin != null) {
                NoFJ.setText(newNoJoin);
                Toast.makeText(FingerJoint.this, "NoFJ berhasil diatur dan disimpan.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Error", "Failed to set or save NoFJ.");
                Toast.makeText(FingerJoint.this, "Gagal mengatur atau menyimpan NoFJ.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class LoadJenisKayuTaskFJ extends AsyncTask<Void, Void, List<JenisKayuFJ>> {
        @Override
        protected List<JenisKayuFJ> doInBackground(Void... voids) {
            List<JenisKayuFJ> jenisKayuList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT IdJenisKayu, Jenis FROM dbo.MstJenisKayu WHERE enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idJenisKayu = rs.getString("IdJenisKayu");
                        String namaJenisKayu = rs.getString("Jenis");

                        JenisKayuFJ jenisKayu = new JenisKayuFJ(idJenisKayu, namaJenisKayu);
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
        protected void onPostExecute(List<JenisKayuFJ> jenisKayuList) {
            if (!jenisKayuList.isEmpty()) {
                ArrayAdapter<JenisKayuFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, jenisKayuList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinKayuFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load jenis kayu.");
            }
        }
    }

    public class LoadJenisKayuTask2FJ extends AsyncTask<String, Void, List<JenisKayuFJ>> {
        private String noFJ;

        public LoadJenisKayuTask2FJ(String noFJ) {
            this.noFJ = noFJ;
        }

        @Override
        protected List<JenisKayuFJ> doInBackground(String... params) {
            List<JenisKayuFJ> jenisKayuList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT j.IdJenisKayu, j.Jenis " +
                            "FROM dbo.MstJenisKayu AS j " +
                            "INNER JOIN dbo.FJ_h AS h ON h.IdJenisKayu = j.IdJenisKayu " +
                            "WHERE h.NoFJ = ? AND j.enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idJenisKayu = rs.getString("IdJenisKayu");
                        String namaJenisKayu = rs.getString("Jenis");
                        JenisKayuFJ jenisKayu = new JenisKayuFJ(idJenisKayu, namaJenisKayu);
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
        protected void onPostExecute(List<JenisKayuFJ> jenisKayuList) {
            if (!jenisKayuList.isEmpty()) {
                ArrayAdapter<JenisKayuFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, jenisKayuList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinKayuFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load jenis kayu.");
            }
        }
    }


    private class LoadTellyTaskFJ extends AsyncTask<Void, Void, List<TellyFJ>> {
        @Override
        protected List<TellyFJ> doInBackground(Void... voids) {
            List<TellyFJ> tellyList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT IdOrgTelly, NamaOrgTelly FROM dbo.MstOrgTelly WHERE enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idOrgTelly = rs.getString("IdOrgTelly");
                        String namaOrgTelly = rs.getString("NamaOrgTelly");

                        TellyFJ telly = new TellyFJ(idOrgTelly, namaOrgTelly);
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
        protected void onPostExecute(List<TellyFJ> tellyList) {
            if (!tellyList.isEmpty()) {
                ArrayAdapter<TellyFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, tellyList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinTellyFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load telly data.");
            }
        }
    }

    private class LoadTellyTask2FJ extends AsyncTask<String, Void, List<TellyFJ>> {
        private String noFJ;

        public LoadTellyTask2FJ(String noFJ) {
            this.noFJ = noFJ;
        }

        @Override
        protected List<TellyFJ> doInBackground(String... params) {
            List<TellyFJ> tellyList = new ArrayList<>();
            Connection con = ConnectionClass(); // Ensure this method establishes a database connection

            if (con != null) {
                try {

                    String query = "SELECT t.IdOrgTelly, t.NamaOrgTelly " +
                            "FROM dbo.MstOrgTelly AS t " +
                            "INNER JOIN dbo.FJ_h AS h ON h.IdOrgTelly = t.IdOrgTelly " +
                            "WHERE h.NoFJ = ? AND t.enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idOrgTelly = rs.getString("IdOrgTelly");
                        String namaOrgTelly = rs.getString("NamaOrgTelly");

                        TellyFJ telly = new TellyFJ(idOrgTelly, namaOrgTelly);
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
        protected void onPostExecute(List<TellyFJ> tellyList) {
            if (!tellyList.isEmpty()) {
                ArrayAdapter<TellyFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, tellyList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinTellyFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load telly data.");
            }
        }
    }

    private class LoadSPKTaskFJ extends AsyncTask<Void, Void, List<SPKFJ>> {
        @Override
        protected List<SPKFJ> doInBackground(Void... voids) {
            List<SPKFJ> spkList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT NoSPK FROM dbo.MstSPK_h WHERE enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String noSPK = rs.getString("NoSPK");

                        SPKFJ spk = new SPKFJ(noSPK);
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
        protected void onPostExecute(List<SPKFJ> spkList) {
            if (!spkList.isEmpty()) {
                ArrayAdapter<SPKFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, spkList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSPKFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load SPK data.");
            }
        }
    }

    private class LoadSPKAsalTaskFJ extends AsyncTask<Void, Void, List<SPKAsalFJ>> {
        @Override
        protected List<SPKAsalFJ> doInBackground(Void... voids) {
            List<SPKAsalFJ> spkAsalList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT NoSPK FROM dbo.MstSPK_h WHERE enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String noSPKasal = rs.getString("NoSPK");

                        SPKAsalFJ spkAsal = new SPKAsalFJ(noSPKasal);
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
        protected void onPostExecute(List<SPKAsalFJ> spkAsalList) {
            if (!spkAsalList.isEmpty()) {
                ArrayAdapter<SPKAsalFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, spkAsalList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSPKAsalFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load SPK data.");
            }
        }
    }

    private class LoadSPKTask2FJ extends AsyncTask<Void, Void, List<SPKFJ>> {
        private String noFJ;

        public LoadSPKTask2FJ(String noFJ) {
            this.noFJ = noFJ;
        }

        @Override
        protected List<SPKFJ> doInBackground(Void... params) {
            List<SPKFJ> spkList = new ArrayList<>();
            Connection con = ConnectionClass(); // Ensure this method establishes a database connection

            if (con != null) {
                try {
                    String query = "SELECT NoSPK FROM dbo.FJ_h WHERE NoFJ = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String noSPK = rs.getString("NoSPK");

                        SPKFJ spk = new SPKFJ(noSPK);
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
        protected void onPostExecute(List<SPKFJ> spkList) {
            if (!spkList.isEmpty()) {
                ArrayAdapter<SPKFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, spkList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSPKFJ.setAdapter(adapter);

                SpinSPKFJ.setEnabled(true);
            } else {
                Log.e("Error", "No SPK data found for the provided NoFJ.");
                SpinSPKFJ.setAdapter(null);
                SpinSPKFJ.setEnabled(false);
                Toast.makeText(FingerJoint.this, "Tidak ada data SPK yang ditemukan.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class LoadProfileTaskFJ extends AsyncTask<Void, Void, List<ProfileFJ>> {
        @Override
        protected List<ProfileFJ> doInBackground(Void... voids) {
            List<ProfileFJ> profileList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT Profile, IdFJProfile FROM dbo.MstFJProfile";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String namaProfile = rs.getString("Profile");
                        String idFJProfile = rs.getString("IdFJProfile");

                        ProfileFJ profileObj = new ProfileFJ(namaProfile, idFJProfile);
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
        protected void onPostExecute(List<ProfileFJ> profileList) {
            if (!profileList.isEmpty()) {
                ArrayAdapter<ProfileFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, profileList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinProfileFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load profile data.");
            }
        }
    }

    private class LoadFisikTaskFJ extends AsyncTask<Void, Void, List<FisikFJ>> {
        @Override
        protected List<FisikFJ> doInBackground(Void... voids) {
            List<FisikFJ> fisikList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT NamaWarehouse FROM dbo.MstWarehouse WHERE IdWarehouse = 5";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String namaWarehouse = rs.getString("NamaWarehouse");

                        FisikFJ fisik = new FisikFJ(namaWarehouse);
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
        protected void onPostExecute(List<FisikFJ> fisikList) {
            if (!fisikList.isEmpty()) {
                ArrayAdapter<FisikFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, fisikList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinFisikFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load fisik data.");
            }
        }
    }

    private class LoadFisikTask2FJ extends AsyncTask<String, Void, List<FisikFJ>> {
        private String noFJ;

        public LoadFisikTask2FJ(String noFJ) {
            this.noFJ = noFJ;
        }

        @Override
        protected List<FisikFJ> doInBackground(String... params) {
            List<FisikFJ> fisikList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT mw.NamaWarehouse " +
                            "FROM dbo.MstWarehouse mw " +
                            "INNER JOIN dbo.FJ_h fj ON mw.IdWarehouse = fj.IdWarehouse " +
                            "WHERE fj.NoFJ = ?";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String namaWarehouse = rs.getString("NamaWarehouse");
                        FisikFJ fisik = new FisikFJ(namaWarehouse);
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
        protected void onPostExecute(List<FisikFJ> fisikList) {
            if (!fisikList.isEmpty()) {
                ArrayAdapter<FisikFJ> adapter = new ArrayAdapter<>(FingerJoint.this,
                        android.R.layout.simple_spinner_item, fisikList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinFisikFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "No warehouse found.");
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(FingerJoint.this,
                        android.R.layout.simple_spinner_item, new String[]{"Tidak ada Fisik"});
                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinFisikFJ.setAdapter(emptyAdapter);
            }
        }
    }


    private class LoadGradeTaskFJ extends AsyncTask<String, Void, List<GradeFJ>> {
        @Override
        protected List<GradeFJ> doInBackground(String... params) {
            List<GradeFJ> gradeList = new ArrayList<>();
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

                    String category = "FINGERJOIN";

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
                            GradeFJ gradeObj = new GradeFJ(idGrade, namaGrade);
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
        protected void onPostExecute(List<GradeFJ> gradeList) {
            if (!gradeList.isEmpty()) {
                ArrayAdapter<GradeFJ> adapter = new ArrayAdapter<>(FingerJoint.this,
                        android.R.layout.simple_spinner_item, gradeList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Tidak ada grade");
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(FingerJoint.this,
                        android.R.layout.simple_spinner_item, new String[]{"Pilih Menu"});
                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeFJ.setAdapter(emptyAdapter);
            }
        }
    }

    private class LoadGradeTask2FJ extends AsyncTask<String, Void, List<GradeFJ>> {
        private String noFJ;

        public LoadGradeTask2FJ(String noFJ) {
            this.noFJ = noFJ;
        }

        @Override
        protected List<GradeFJ> doInBackground(String... params) {
            List<GradeFJ> gradeList = new ArrayList<>();
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

                    String category = "FingerJoin";

                    String query = "SELECT DISTINCT a.IdGrade, a.NamaGrade " +
                            "FROM MstGrade a " +
                            "INNER JOIN MstGrade_d b ON a.IdGrade = b.IdGrade " +
                            "WHERE a.Enable = 1 AND b.IdJenisKayu = ? AND b.Category = ? AND b.NoFJ = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setInt(1, idJenisKayu);
                    ps.setString(2, category);
                    ps.setString(3, noFJ);
                    Log.d("LoadGradeTask", "Executing query: " + query + " with IdJenisKayu: " + idJenisKayu);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idGrade = rs.getString("IdGrade");
                        String namaGrade = rs.getString("NamaGrade");

                        if (idGrade != null && namaGrade != null) {
                            Log.d("LoadGradeTask", "Fetched Grade: IdGrade = " + idGrade + ", NamaGrade = " + namaGrade);
                            GradeFJ gradeObj = new GradeFJ(idGrade, namaGrade);
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
        protected void onPostExecute(List<GradeFJ> gradeList) {
            if (!gradeList.isEmpty()) {
                ArrayAdapter<GradeFJ> adapter = new ArrayAdapter<>(FingerJoint.this,
                        android.R.layout.simple_spinner_item, gradeList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Tidak ada grade");
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(FingerJoint.this,
                        android.R.layout.simple_spinner_item, new String[]{"Tidak ada grade"});
                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeFJ.setAdapter(emptyAdapter);
            }
        }
    }

    private class LoadMesinTaskFJ extends AsyncTask<String, Void, List<MesinFJ>> {
        @Override
        protected List<MesinFJ> doInBackground(String... params) {
            List<MesinFJ> mesinList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    // Ambil tanggal saat ini jika tidak ada parameter
                    String selectedDate;
                    if (params != null && params.length > 0) {
                        selectedDate = params[0];
                    } else {
                        selectedDate = DateFJ.getText().toString();
                    }

                    String query = "SELECT a.IdMesin, b.NamaMesin, a.NoProduksi FROM dbo.FJProduksi_h a " +
                            "INNER JOIN dbo.MstMesin b ON a.IdMesin = b.IdMesin WHERE CONVERT(date, a.Tanggal) = CONVERT(date, ?)";

                    Log.d("LoadMesinTask", "Query: " + query + " dengan tanggal: " + selectedDate);

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, selectedDate);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String idMesin = rs.getString("IdMesin");
                        String nomorProduksi = rs.getString("NoProduksi");
                        String namaMesin = rs.getString("NamaMesin");

                        Log.d("LoadMesinTask", "Found: ID=" + idMesin + ", No=" + nomorProduksi + ", Name=" + namaMesin);

                        MesinFJ mesin = new MesinFJ(nomorProduksi, namaMesin);
                        mesinList.add(mesin);
                    }

                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    Log.e("LoadMesinTask", "Error: " + e.getMessage(), e);
                }
            } else {
                Log.e("LoadMesinTask", "Connection is null");
            }
            return mesinList;
        }

        @Override
        protected void onPostExecute(List<MesinFJ> mesinList) {
            if (!mesinList.isEmpty()) {
                ArrayAdapter<MesinFJ> adapter = new ArrayAdapter<>(FingerJoint.this,
                        android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinMesinFJ.setAdapter(adapter);

                Log.d("LoadMesinTask", "Loaded " + mesinList.size() + " items");
            } else {
                Log.d("LoadMesinTask", "No data found");
                SpinMesinFJ.setAdapter(null);
            }
        }
    }

    private class LoadMesinTask2FJ extends AsyncTask<Void, Void, List<MesinFJ>> {
        private String noFJ;

        public LoadMesinTask2FJ(String noFJ) {
            this.noFJ = noFJ;
        }

        @Override
        protected List<MesinFJ> doInBackground(Void... params) {
            List<MesinFJ> mesinList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT b.NoProduksi, d.NamaMesin FROM FJ_h a " +
                            "INNER JOIN FJProduksiOutput b ON b.NoFJ = a.NoFJ " +
                            "INNER JOIN FJProduksi_h c ON c.NoProduksi = b.NoProduksi " +
                            "INNER JOIN MstMesin d ON d.IdMesin = c.IdMesin " +
                            "WHERE a.NoFJ = ?";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String nomorProduksi = rs.getString("NoProduksi");
                        String namaMesin = rs.getString("NamaMesin");

                        MesinFJ mesin = new MesinFJ(nomorProduksi, namaMesin);
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
        protected void onPostExecute(List<MesinFJ> mesinList) {
            if (!mesinList.isEmpty()) {
                ArrayAdapter<MesinFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinMesinFJ.setAdapter(adapter);

                radioButtonMesinFJ.setEnabled(true);
                radioButtonBSusunFJ.setEnabled(false);
            } else {
                Log.e("Error", "Failed to load mesin data.");
                radioButtonMesinFJ.setEnabled(false);
                radioButtonBSusunFJ.setEnabled(false);

                Toast.makeText(FingerJoint.this, "Tidak ada data mesin yang ditemukan.", Toast.LENGTH_SHORT).show();
                SpinMesinFJ.setAdapter(null);
            }
        }
    }


    private class LoadSusunTaskFJ extends AsyncTask<String, Void, List<SusunFJ>> {
        @Override
        protected List<SusunFJ> doInBackground(String... params) {
            List<SusunFJ> susunList = new ArrayList<>();
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

                        SusunFJ susun = new SusunFJ(nomorBongkarSusun);
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
        protected void onPostExecute(List<SusunFJ> susunList) {
            if (!susunList.isEmpty()) {
                ArrayAdapter<SusunFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, susunList);
                SpinSusunFJ.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load susun data");
            }
        }
    }

    private class LoadSusunTask2FJ extends AsyncTask<Void, Void, List<SusunFJ>> {
        private String noFJ;

        public LoadSusunTask2FJ(String noFJ) {
            this.noFJ = noFJ;
        }

        @Override
        protected List<SusunFJ> doInBackground(Void... params) {
            List<SusunFJ> susunList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT NoBongkarSusun FROM dbo.BongkarSusunOutputFJ WHERE NoFJ = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noFJ);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String nomorBongkarSusun = rs.getString("NoBongkarSusun");

                        SusunFJ susun = new SusunFJ(nomorBongkarSusun);
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
        protected void onPostExecute(List<SusunFJ> susunList) {
            if (!susunList.isEmpty()) {
                ArrayAdapter<SusunFJ> adapter = new ArrayAdapter<>(FingerJoint.this, android.R.layout.simple_spinner_item, susunList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSusunFJ.setAdapter(adapter);

                radioButtonMesinFJ.setEnabled(false);
                radioButtonBSusunFJ.setEnabled(true);
            } else {
                Log.e("Error", "Failed to load susun data.");
                radioButtonMesinFJ.setEnabled(false);
                radioButtonBSusunFJ.setEnabled(false);

                Toast.makeText(FingerJoint.this, "Tidak ada data susun yang ditemukan.", Toast.LENGTH_SHORT).show();
                SpinSusunFJ.setAdapter(null);
            }
        }
    }

    public class JenisKayuFJ {
        private String idJenisKayu;
        private String namaJenisKayu;

        public JenisKayuFJ(String idJenisKayu, String namaJenisKayu) {
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



    public class TellyFJ {
        private String idTelly;
        private String namaTelly;

        public TellyFJ(String idTelly, String namaTelly) {
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


    public class SPKFJ {
        private String noSPK;

        public SPKFJ(String noSPK) {
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

    public class SPKAsalFJ {
        private String noSPKasal;

        public SPKAsalFJ(String noSPKasal) {
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

    public class ProfileFJ {
        private String idFJProfile;
        private String namaProfile;

        public ProfileFJ(String namaProfile, String idFJProfile) {
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


    public class FisikFJ {
        private String idWarehouse; // Jika diperlukan
        private String namaWarehouse;

        public FisikFJ(String namaWarehouse) {
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


    public class GradeFJ {
        private String idGrade;
        private String namaGrade;

        public GradeFJ(String idGrade, String namaGrade) {
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


    public class MesinFJ {
        private String noProduksi;
        private String namaMesin;

        public MesinFJ(String noProduksi, String namaMesin) {
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

    public class SusunFJ {
        private String nomorBongkarSusun;

        public SusunFJ(String nomorBongkarSusun) {
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