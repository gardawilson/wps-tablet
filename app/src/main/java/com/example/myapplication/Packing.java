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
public class Packing extends AppCompatActivity {

    private EditText NoBarangJadi;
    private EditText DateP;
    private EditText TimeP;
    private EditText NoWIP;
    private Spinner SpinKayuP;
    private Spinner SpinTellyP;
    private Spinner SpinSPKP;
    private Spinner SpinSPKAsalP;
    private Spinner SpinProfileP;
    private Spinner SpinBarangJadiP;
    private Spinner SpinMesinP;
    private Spinner SpinSusunP;
    private Calendar calendarP;
    private RadioGroup radioGroup;
    private RadioButton radioButtonMesinP;
    private RadioButton radioButtonBSusunP;
    private Button BtnDataBaruP;
    private Button BtnSimpanP;
    private Button BtnBatalP;
    private Button BtnHapusDetailP;
    private boolean isDataBaruClickedP = false;
    private CheckBox CBAfkirP;
    private CheckBox CBLemburP;
    private TextView TabelTebalP;
    private TextView TabelNoP;
    private TextView TabelLebarP;
    private TextView TabelPanjangP;
    private TextView TabelPcsP;
    private Button BtnInputDetailP;
    private EditText DetailLebarP;
    private EditText DetailTebalP;
    private EditText DetailPanjangP;
    private EditText DetailPcsP;
    private static int currentNumber = 1;
    private Button BtnPrintP;
    private TextView M3P;
    private TextView JumlahPcsP;
    private boolean isCBAfkirP, isCBLemburP;
    private Button BtnSearchP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_packing);


        NoWIP = findViewById(R.id.NoWIP);
        NoBarangJadi = findViewById(R.id.NoBarangJadi);
        DateP = findViewById(R.id.DateP);
        TimeP = findViewById(R.id.TimeP);
        SpinKayuP = findViewById(R.id.SpinKayuP);
        SpinTellyP = findViewById(R.id.SpinTellyP);
        SpinSPKP = findViewById(R.id.SpinSPKP);
        SpinSPKAsalP = findViewById(R.id.SpinSPKAsalP);
        SpinProfileP = findViewById(R.id.SpinProfileP);
        SpinBarangJadiP = findViewById(R.id.SpinBarangJadiP);
        calendarP = Calendar.getInstance();
        SpinMesinP = findViewById(R.id.SpinMesinP);
        SpinSusunP = findViewById(R.id.SpinSusunP);
        radioButtonMesinP = findViewById(R.id.radioButtonMesinP);
        radioButtonBSusunP = findViewById(R.id.radioButtonBSusunP);
        BtnDataBaruP = findViewById(R.id.BtnDataBaruP);
        BtnSimpanP = findViewById(R.id.BtnSimpanP);
        BtnBatalP = findViewById(R.id.BtnBatalP);
        BtnHapusDetailP = findViewById(R.id.BtnHapusDetailP);
        CBLemburP = findViewById(R.id.CBLemburP);
        CBAfkirP = findViewById(R.id.CBAfkirP);
        TabelTebalP = findViewById(R.id.TabelTebalP);
        TabelPcsP = findViewById(R.id.TabelPcsP);
        TabelPanjangP = findViewById(R.id.TabelPanjangP);
        TabelNoP = findViewById(R.id.TabelNoP);
        TabelLebarP = findViewById(R.id.TabelLebarP);
        BtnInputDetailP = findViewById(R.id.BtnInputDetailP);
        DetailPcsP = findViewById(R.id.DetailPcsP);
        DetailTebalP = findViewById(R.id.DetailTebalP);
        DetailPanjangP = findViewById(R.id.DetailPanjangP);
        DetailLebarP = findViewById(R.id.DetailLebarP);
        BtnPrintP = findViewById(R.id.BtnPrintP);
        M3P = findViewById(R.id.M3P);
        JumlahPcsP = findViewById(R.id.JumlahPcsP);
        BtnSearchP = findViewById(R.id.BtnSearchP);
        BtnPrintP.setEnabled(false);


        radioButtonMesinP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpinMesinP.setEnabled(true);
                    SpinSusunP.setEnabled(false);
                }
            }
        });

        radioButtonBSusunP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpinSusunP.setEnabled(true);
                    SpinMesinP.setEnabled(false);
                }
            }
        });

        setCurrentDateTime();

        BtnDataBaruP.setOnClickListener(v -> {
            if (!isDataBaruClickedP) {
                resetSpinners();
                new LoadJenisKayuTask().execute();
                new LoadTellyTask().execute();
                new LoadSPKTask().execute();
                new LoadSPKAsalTask().execute();
                new LoadProfileTask().execute();
                new LoadFisikTask().execute();
                new LoadMesinTask().execute();
                new LoadSusunTask().execute();

                isDataBaruClickedP = true;
                setCurrentDateTime();
            } else {
                Toast.makeText(Packing.this, "Tombol Data Baru sudah diklik. Klik Simpan terlebih dahulu.", Toast.LENGTH_SHORT).show();
            }
            BtnSimpanP.setEnabled(true);
            new SetAndSaveNoS4STask().execute();
            BtnPrintP.setEnabled(false);
            BtnBatalP.setEnabled(true);
            radioButtonMesinP.setEnabled(true);
            radioButtonBSusunP.setEnabled(true);
            setCurrentDateTime();
            clearTableData2();
            BtnDataBaruP.setEnabled(false);
        });


        BtnSimpanP.setOnClickListener(v -> {
            String noBarangJadi = NoBarangJadi.getText().toString();
            String dateCreate = DateP.getText().toString();
            String time = TimeP.getText().toString();

            Telly selectedTelly = (Telly) SpinTellyP.getSelectedItem();
            SPK selectedSPK = (SPK) SpinSPKP.getSelectedItem();
            SPKAsal selectedSPKAsal = (SPKAsal) SpinSPKAsalP.getSelectedItem();
            Profile selectedProfile = (Profile) SpinProfileP.getSelectedItem();
            Fisik selectedFisik = (Fisik) SpinBarangJadiP.getSelectedItem();
            JenisKayu selectedJenisKayu = (JenisKayu) SpinKayuP.getSelectedItem();
            Mesin selectedMesin = (Mesin) SpinMesinP.getSelectedItem();
            Susun selectedSusun = (Susun) SpinSusunP.getSelectedItem();


            String idTelly = selectedTelly != null ? selectedTelly.getIdTelly() : null;
            String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
            String noSPKasal = selectedSPKAsal != null ? selectedSPKAsal.getNoSPKAsal() : null;
            String idProfile = selectedProfile != null ? selectedProfile.getIdFJProfile() : null;
            String idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;
            String noProduksi = selectedMesin != null ? selectedMesin.getNoProduksi() : null;
            String noBongkarSusun = selectedSusun != null ? selectedSusun.getNoBongkarSusun() : null;
            int isReject = CBAfkirP.isChecked() ? 1 : 0;
            int isLembur = CBLemburP.isChecked() ? 1 : 0;

            if (noBarangJadi.isEmpty() || dateCreate.isEmpty() || time.isEmpty() ||
                    TabelLebarP.getText().toString().isEmpty() ||
                    NoWIP.getText().toString().isEmpty() ||
                    TabelPanjangP.getText().toString().isEmpty() ||
                    TabelTebalP.getText().toString().isEmpty() ||
                    TabelPcsP.getText().toString().isEmpty() ||
                    TabelNoP.getText().toString().isEmpty() ||
                    selectedTelly == null ||
                    selectedSPK == null ||
                    selectedProfile == null ||
                    selectedFisik == null ||
                    selectedJenisKayu == null ||
                    (!radioButtonMesinP.isChecked() && !radioButtonBSusunP.isChecked()) ||
                    (radioButtonMesinP.isChecked() && selectedMesin == null) ||
                    (radioButtonBSusunP.isChecked() && selectedSusun == null)) {

                Toast.makeText(Packing.this, "Pastikan semua field terisi dengan benar.", Toast.LENGTH_SHORT).show();
                return;
            }
            BtnDataBaruP.setEnabled(true);
            BtnPrintP.setEnabled(true);

            new InsertDatabaseTask(
                    TabelLebarP.getText().toString(),
                    TabelPanjangP.getText().toString(),
                    TabelTebalP.getText().toString(),
                    TabelPcsP.getText().toString(),
                    TabelNoP.getText().toString(),
                    noBarangJadi
            ).execute();

            new UpdateDatabaseTask(
                    noBarangJadi,
                    dateCreate,
                    time,
                    idTelly,
                    noSPK,
                    noSPKasal != null ? noSPKasal : "", // Handle jika null
                    idJenisKayu,
                    idProfile,
                    isReject,
                    isLembur
            ).execute();
//            new UpdateNoSTAsalTask(
//                    noBarangJadi,
//                    noWIP
//            ).execute();

            if (radioButtonMesinP.isChecked() && SpinMesinP.isEnabled() && noProduksi != null) {
                new SaveToDatabaseTask(noProduksi, noBarangJadi).execute();
            } else if (radioButtonBSusunP.isChecked() && SpinSusunP.isEnabled() && noBongkarSusun != null) {
                new SaveBongkarSusunTask(noBongkarSusun, noBarangJadi).execute();
            } else {
                Toast.makeText(Packing.this, "Pilih opsi yang valid untuk disimpan.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(Packing.this, "Data berhasil disimpan dan tampilan telah dikosongkan.", Toast.LENGTH_SHORT).show();

        });

        BtnBatalP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noFJoin = NoBarangJadi.getText().toString().trim();

                if (!noFJoin.isEmpty()) {
                    new DeleteDataTask().execute(noFJoin);
                }

                clearTableData2();
                clearTableData();
                Toast.makeText(Packing.this, "Tampilan telah dikosongkan.", Toast.LENGTH_SHORT).show();
            }
        });

        BtnSearchP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noBarangJadi = NoBarangJadi.getText().toString();


                DetailLebarP.setText("");
                DetailPanjangP.setText("");
                DetailTebalP.setText("");
                DetailPcsP.setText("");
                NoBarangJadi.setText("");
                NoWIP.setText("");

                if (!noBarangJadi.isEmpty()) {
                    new LoadMesinTask2(noBarangJadi).execute();
                    new LoadSusunTask2(noBarangJadi).execute();
                    new LoadFisikTask2(noBarangJadi).execute();

                    new LoadJenisKayuTask2(noBarangJadi).execute();
                    new LoadTellyTask2(noBarangJadi).execute();
                    new LoadSPKTask2(noBarangJadi).execute();
                    new LoadProfileTask2(noBarangJadi).execute();
                    new SearchAllDataTask(noBarangJadi).execute();

                    radioButtonMesinP.setEnabled(true);
                    SpinSPKP.setEnabled(true);
                    radioButtonBSusunP.setEnabled(true);
                } else {
                    Log.e("Input Error", "NoBarangJadi is empty");
                    radioButtonMesinP.setEnabled(false);
                    radioButtonBSusunP.setEnabled(false);
                }

                BtnSimpanP.setEnabled(false);
                BtnBatalP.setEnabled(false);
                BtnPrintP.setEnabled(true);
            }
        });
        DateP.setOnClickListener(v -> showDatePickerDialog());

        TimeP.setOnClickListener(v -> showTimePickerDialog());

        BtnInputDetailP.setOnClickListener(v -> {
            updateTableData();
            m3();
            jumlahpcs();
            clearTableData();
        });

        BtnHapusDetailP.setOnClickListener(v -> {
            clearTableData();
        });

        BtnPrintP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String noBarangJadi = NoBarangJadi.getText() != null ? NoBarangJadi.getText().toString() : "";
                    String Kayu = SpinKayuP.getSelectedItem() != null ? SpinKayuP.getSelectedItem().toString() : "";
                    String Fisik = SpinBarangJadiP.getSelectedItem() != null ? SpinBarangJadiP.getSelectedItem().toString() : "";
                    String Tanggal = DateP.getText() != null ? DateP.getText().toString() : "";
                    String Waktu = TimeP.getText() != null ? TimeP.getText().toString() : "";
                    String Telly = SpinTellyP.getSelectedItem() != null ? SpinTellyP.getSelectedItem().toString() : "";
                    String Mesin = SpinMesinP.getSelectedItem() != null ? SpinMesinP.getSelectedItem().toString() : "";
                    String noSPK = SpinSPKP.getSelectedItem() != null ? SpinSPKP.getSelectedItem().toString() : "";
                    String tebal = TabelTebalP.getText() != null ? TabelTebalP.getText().toString() : "";
                    String lebar = TabelLebarP.getText() != null ? TabelLebarP.getText().toString() : "";
                    String panjang = TabelPanjangP.getText() != null ? TabelPanjangP.getText().toString() : "";
                    String pcs = TabelPcsP.getText() != null ? TabelPcsP.getText().toString() : "";
                    String jlh = JumlahPcsP.getText() != null ? JumlahPcsP.getText().toString() : "";
                    String m3 = M3P.getText() != null ? M3P.getText().toString() : "";
                    String Susun = SpinSusunP.getSelectedItem() != null ? SpinSusunP.getSelectedItem().toString() : "";

                    Uri pdfUri = createPdf(noBarangJadi, Kayu, Fisik, Tanggal, Waktu, Telly, Mesin, Susun, noSPK, tebal, lebar, panjang, pcs, jlh, m3);

                    if (pdfUri != null) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(pdfUri, "application/pdf");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        intent.setPackage("com.mi.globalbrowser");

                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(Packing.this, "Mi Browser not found. Please install Mi Browser or use another app to open the PDF.", Toast.LENGTH_LONG).show();

                            Intent fallbackIntent = new Intent(Intent.ACTION_VIEW);
                            fallbackIntent.setDataAndType(pdfUri, "application/pdf");
                            fallbackIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(Intent.createChooser(fallbackIntent, "Open PDF with"));
                        }
                    } else {
                        Toast.makeText(Packing.this, "Error creating PDF", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Packing.this, "Error creating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Packing.this, "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private class SearchAllDataTask extends AsyncTask<String, Void, Boolean> {
        private String noBarangJadi;

        public SearchAllDataTask(String noBarangJadi) {
            this.noBarangJadi = noBarangJadi;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("SearchAllDataTask", "Searching for NoBarangJadi: " + noBarangJadi);
            Connection con = ConnectionClass();
            boolean isDataFound = false;

            if (con != null) {
                try {
                    String query = "SELECT h.DateCreate, h.Jam, " +
                            "d.Lebar, d.Panjang, d.Tebal, d.JmlhBatang, d.NoUrut, " +
                            "h.IsReject, h.IsLembur " +
                            "FROM dbo.BarangJadi_h AS h " +
                            "INNER JOIN dbo.BarangJadi_d AS d ON h.NoBJ = d.NoBJ " +
                            "WHERE h.NoBJ = ?";

                    Log.d("SearchAllDataTask", "Preparing statement: " + query);
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarangJadi);
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
                                NoBarangJadi.setText(noBarangJadi);
                                DateP.setText(dateCreate != null ? dateCreate : "");
                                TabelNoP.setText(String.valueOf(no));
                                TimeP.setText(jam != null ? jam : "");
                                TabelLebarP.setText(String.valueOf(lebar));
                                TabelPanjangP.setText(String.valueOf(panjang));
                                TabelTebalP.setText(String.valueOf(tebal));
                                TabelPcsP.setText(String.valueOf(jmlhBatang));
                                CBAfkirP.setChecked(isReject);
                                CBLemburP.setChecked(isLembur);

                                m3();
                                jumlahpcs();
                            }
                        });

                        isDataFound = true;
                    } else {
                        Log.e("SearchAllDataTask", "No data found for NoBarangJadi: " + noBarangJadi);
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


    private void updateTableData() {
        String tebal = DetailTebalP.getText().toString();
        String panjang = DetailPanjangP.getText().toString();
        String lebar = DetailLebarP.getText().toString();
        String pcs = DetailPcsP.getText().toString();

        String no = String.valueOf(1);

        TabelTebalP.setText(tebal);
        TabelPanjangP.setText(panjang);
        TabelLebarP.setText(lebar);
        TabelNoP.setText(no);
        TabelPcsP.setText(pcs);

    }

    private void clearTableData() {
        DetailPanjangP.setText("");
        DetailTebalP.setText("");
        DetailLebarP.setText("");
        DetailPcsP.setText("");

        currentNumber = 1;
    }

    private void clearTableData2() {
        TabelPanjangP.setText("");
        TabelTebalP.setText("");
        TabelLebarP.setText("");
        TabelPcsP.setText("");
        TabelNoP.setText("");
        NoBarangJadi.setText("");
        M3P.setText("");
        JumlahPcsP.setText("");
        CBAfkirP.setChecked(false);
        CBLemburP.setChecked(false);

        currentNumber = 1;
    }

    private void resetSpinners() {
        if (SpinKayuP.getAdapter() != null) {
            SpinKayuP.setSelection(0);
        }
        if (SpinMesinP.getAdapter() != null) {
            SpinMesinP.setSelection(0);
        }
        if (SpinSusunP.getAdapter() != null) {
            SpinSusunP.setSelection(0);
        }
        if (SpinTellyP.getAdapter() != null) {
            SpinTellyP.setSelection(0);
        }
        if (SpinProfileP.getAdapter() != null) {
            SpinProfileP.setSelection(0);
        }
        if (SpinBarangJadiP.getAdapter() != null) {
            SpinBarangJadiP.setSelection(0);
        }
        if (SpinSPKP.getAdapter() != null) {
            SpinSPKP.setSelection(0);
        }

        BtnDataBaruP.setEnabled(true);
        isDataBaruClickedP = true;
    }

    private void m3() {
        TextView tabeltebalTextView = TabelTebalP;
        TextView tabelpanjangTextView = TabelPanjangP;
        TextView tabellebarTextView = TabelLebarP;
        TextView tabelpcsTextView = TabelPcsP;

        String tebalString = tabeltebalTextView.getText().toString();
        String panjangString = tabelpanjangTextView.getText().toString();
        String lebarString = tabellebarTextView.getText().toString();
        String jmlhBatangString = tabelpcsTextView.getText().toString();

        int tebal = Integer.parseInt(tebalString.isEmpty() ? "0" : tebalString);
        int panjang = Integer.parseInt(panjangString.isEmpty() ? "0" : panjangString);
        int lebar = Integer.parseInt(lebarString.isEmpty() ? "0" : lebarString);
        int jmlhBatang = Integer.parseInt(jmlhBatangString.isEmpty() ? "0" : jmlhBatangString);

        float result = (tebal * panjang);
        float result2 = ( lebar * jmlhBatang);
        float result3 = (result * result2 / 1000000000);

        TextView M3 = findViewById(R.id.M3P);
        M3.setText(String.format("%.4f" , result3));
    }


    private void jumlahpcs() {
        String pcsString = TabelPcsP.getText().toString();

        if (!pcsString.isEmpty()) {
            try {
                int jumlahPcs = Integer.parseInt(pcsString);
                TextView JumlahPcsP = findViewById(R.id.JumlahPcsP);
                JumlahPcsP.setText(String.valueOf(jumlahPcs));
            } catch (NumberFormatException e) {
                Log.e("JumlahPCS", "Invalid PCS number: " + pcsString);
                TextView JumlahPcsP = findViewById(R.id.JumlahPcsP);
                JumlahPcsP.setText("0");
            }
        } else {
            TextView JumlahPcsP = findViewById(R.id.JumlahPcsP);
            JumlahPcsP.setText("0");
        }
    }


    private void setCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        DateP.setText(currentDate);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        TimeP.setText(currentTime);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Packing.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                String selectedDate = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);
                DateP.setText(selectedDate);
                new LoadMesinTask().execute(selectedDate);
                new LoadSusunTask().execute(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        int hour = calendarP.get(Calendar.HOUR_OF_DAY);
        int minute = calendarP.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Packing.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                calendarP.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendarP.set(Calendar.MINUTE, selectedMinute);

                SimpleDateFormat timeFormat = new SimpleDateFormat(" HH:mm:ss", Locale.getDefault());
                String updatedTime = timeFormat.format(calendarP.getTime());
                TimeP.setText(updatedTime);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private Uri createPdf(String noBarangJadi, String Kayu,String Fisik, String Tanggal, String Waktu, String Telly, String Mesin, String Susun, String noSPK, String tebal, String lebar, String panjang, String pcs, String jlh, String m3) throws IOException {
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

                    Paragraph judul = new Paragraph("LABEL BarangJadi\n").setBold().setFontSize(8).setTextAlignment(TextAlignment.CENTER);
                    Paragraph isi = new Paragraph("").setFontSize(7)
                            .add("No Barang Jadi  : " + noBarangJadi + "                                              ")
                            .add("Tanggal : " + Tanggal + " " + Waktu + "\n")
                            .add("Kayu    : " + Kayu + "                                                    ")
                            .add("Telly   : " + Telly + "\n")
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

                    BarcodeQRCode qrCode = new BarcodeQRCode(noBarangJadi);
                    PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
                    Image qrCodeImage = new Image(qrCodeObject).setWidth(45).setHorizontalAlignment(HorizontalAlignment.CENTER).setMarginBottom(0).setMarginTop(0);

                    Paragraph pcsm3 = new Paragraph("").setFontSize(7).setTextAlignment(TextAlignment.RIGHT).setMarginRight(67)
                            .add("Jmlh Pcs = " + jlh + "\t" + "\n")
                            .add("m3 = " + m3 + "\n");

                    Paragraph garis = new Paragraph("--------------------------------------------------------------").setTextAlignment(TextAlignment.CENTER);
                    Paragraph output = new Paragraph("Output").setTextAlignment(TextAlignment.CENTER).setFontSize(6).setMarginTop(0).setMarginBottom(0);
                    Paragraph input = new Paragraph("Input").setTextAlignment(TextAlignment.CENTER).setFontSize(6).setMarginTop(0).setMarginBottom(0);
                    Paragraph BarangJadi = new Paragraph(noBarangJadi).setTextAlignment(TextAlignment.CENTER).setFontSize(6).setMarginTop(0).setMarginBottom(0);
                    document.add(judul);
                    document.add(isi);
                    document.add(table);
                    document.add(pcsm3);
                    document.add(output);
                    document.add(qrCodeImage);
                    document.add(BarangJadi);
                    document.add(garis);
                    document.add(judul);
                    document.add(isi);
                    document.add(table);
                    document.add(pcsm3);
                    document.add(input);
                    document.add(qrCodeImage);
                    document.add(BarangJadi);

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
        private String noBarangJadi;

        public SaveBongkarSusunTask(String noBongkarSusun, String noBarangJadi) {
            this.noBongkarSusun = noBongkarSusun;
            this.noBarangJadi = noBarangJadi;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.BongkarSusunOutputBarangJadi (NoBJ, NoBongkarSusun) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarangJadi);
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
                Toast.makeText(Packing.this, "Data berhasil disimpan.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SaveToDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private String noProduksi, noBarangJadi;

        public SaveToDatabaseTask(String noProduksi, String noBarangJadi) {
            this.noProduksi = noProduksi;
            this.noBarangJadi = noBarangJadi;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.PackingProduksiOutput (NoProduksi, NoBJ) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noProduksi);
                    ps.setString(2, noBarangJadi);

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
                Toast.makeText(Packing.this, "Data berhasil disimpan ke database.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Packing.this, "Gagal menyimpan data ke database.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class InsertDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private String lebar, panjang, tebal, pcs, noUrut, noBarangJadi;

        public InsertDatabaseTask(String lebar, String panjang, String tebal, String pcs, String noUrut, String noBarangJadi) {
            this.lebar = lebar;
            this.panjang = panjang;
            this.tebal = tebal;
            this.pcs = pcs;
            this.noUrut = noUrut;
            this.noBarangJadi = noBarangJadi;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.BarangJadi_d (Lebar, Panjang, Tebal, JmlhBatang, NoUrut, NoBJ) VALUES (?, ?, ?, ?, ?, ?)";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, lebar);
                    ps.setString(2, panjang);
                    ps.setString(3, tebal);
                    ps.setString(4, pcs);
                    ps.setString(5, noUrut);
                    ps.setString(6, noBarangJadi);

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
                Toast.makeText(Packing.this, "Data berhasil disimpan ke database.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Packing.this, "Gagal menyimpan data ke database.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class DeleteDataTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String noBarangJadi = params[0];
            Connection con = ConnectionClass();
            boolean success = false;

            if (con != null) {
                try {
                    String query = "DELETE FROM dbo.BarangJadi_h WHERE NoBJ = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarangJadi);

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
                Toast.makeText(Packing.this, "Data berhasil dihapus.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Packing.this, "Gagal menghapus data.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private String noBarangJadi, dateCreate, time, idTelly, noSPK, noSPKasal, idJenisKayu, idFJProfile;
        private int isReject, isLembur;

        public UpdateDatabaseTask(String noBarangJadi, String dateCreate, String time,
                                  String idTelly, String noSPK, String noSPKasal,
                                  String idJenisKayu, String idFJProfile,
                                  int isReject, int isLembur) {
            this.noBarangJadi = noBarangJadi;
            this.dateCreate = dateCreate;
            this.time = time;
            this.idTelly = idTelly;
            this.noSPK = noSPK;
            this.noSPKasal = noSPKasal;
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
                    // Log untuk debugging
                    Log.d("UpdateDatabase", "Starting update for NoBJ: " + noBarangJadi);

                    // Perbaiki query - tambahkan koma setelah NoSPKAsal
                    String query = "UPDATE dbo.BarangJadi_h SET " +
                            "DateCreate = ?, " +
                            "Jam = ?, " +
                            "IdOrgTelly = ?, " +
                            "NoSPK = ?, " +
                            "NoSPKAsal = ?, " +  // Tambahkan koma di sini
                            "IdFJProfile = ?, " +
                            "IdJenisKayu = ?, " +
                            "IdWarehouse = ?, " +
                            "IsReject = ?, " +
                            "IsLembur = ? " +
                            "WHERE NoBJ = ?";

                    PreparedStatement ps = con.prepareStatement(query);

                    // Set nilai parameter dengan benar dan lengkap
                    ps.setString(1, dateCreate);
                    ps.setString(2, time);
                    ps.setString(3, idTelly);
                    ps.setString(4, noSPK);
                    ps.setString(5, noSPKasal);
                    ps.setString(6, idFJProfile);
                    ps.setString(7, idJenisKayu);
                    ps.setInt(8, 11); // IdWarehouse default 11 untuk BarangJadi
                    ps.setInt(9, isReject);
                    ps.setInt(10, isLembur);
                    ps.setString(11, noBarangJadi);

                    // Log parameter values untuk debugging
                    Log.d("UpdateDatabase", "Parameters: " +
                            "NoBJ=" + noBarangJadi +
                            ", DateCreate=" + dateCreate +
                            ", Jam=" + time +
                            ", IdTelly=" + idTelly +
                            ", NoSPK=" + noSPK +
                            ", NoSPKAsal=" + noSPKasal +
                            ", IdFJProfile=" + idFJProfile +
                            ", IdJenisKayu=" + idJenisKayu);

                    int rowsUpdated = ps.executeUpdate();

                    Log.d("UpdateDatabase", "Rows updated: " + rowsUpdated);

                    ps.close();
                    con.close();

                    return rowsUpdated > 0;

                } catch (Exception e) {
                    Log.e("Database Error", "Error updating BarangJadi_h: " + e.getMessage());
                    e.printStackTrace();
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
                Log.d("UpdateDatabase", "Successfully updated BarangJadi_h");
                Toast.makeText(Packing.this, "Data header berhasil diupdate.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("UpdateDatabase", "Failed to update BarangJadi_h");
                Toast.makeText(Packing.this, "Gagal mengupdate data header.", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private class UpdateNoSTAsalTask extends AsyncTask<Void, Void, Boolean> {
//        private String noBarangJadi;
//        private String noWIP;
//        private String errorMessage = null;
//
//        public UpdateNoSTAsalTask(String noBarangJadi, String noWIP) {
//            this.noBarangJadi = noBarangJadi;
//            this.noWIP = noWIP;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//            Connection con = ConnectionClass();
//            if (con != null) {
//                try {
//                    Log.d("UpdateNoSTAsalTask", "Checking and updating NoSTAsal for NoBJ: " + noBarangJadi);
//
//                    String checkQuery = "SELECT NoSTAsal FROM dbo.BarangJadi_h WHERE NoSTAsal = ?";
//                    PreparedStatement checkPs = con.prepareStatement(checkQuery);
//                    checkPs.setString(1, noWIP);
//                    ResultSet rs = checkPs.executeQuery();
//
//                    if (!rs.next()) {
//                        errorMessage = "NoSTAsal tidak ditemukan di tabel ST_h.";
//                        Log.e("UpdateNoSTAsalTask", errorMessage);
//                        return false;
//                    }
//
//                    String selectQuery = "SELECT NoSTAsal FROM dbo.BarangJadi_h WHERE NoBJ = ?";
//                    PreparedStatement selectPs = con.prepareStatement(selectQuery);
//                    selectPs.setString(1, noBarangJadi);
//                    ResultSet selectRs = selectPs.executeQuery();
//
//                    if (selectRs.next()) {
//                        String currentNoSTAsal = selectRs.getString("NoSTAsal");
//                        if (currentNoSTAsal == null) {
//                            String updateQuery = "UPDATE dbo.BarangJadi_h SET NoSTAsal = ? WHERE NoBJ = ?";
//                            PreparedStatement updatePs = con.prepareStatement(updateQuery);
//                            updatePs.setString(1, noWIP);
//                            updatePs.setString(2, noBarangJadi);
//
//                            int rowsUpdated = updatePs.executeUpdate();
//                            updatePs.close();
//                            Log.d("UpdateNoSTAsalTask", "Rows updated: " + rowsUpdated);
//                            return rowsUpdated > 0;
//                        } else {
//                            errorMessage = "NoSTAsal sudah memiliki nilai: " + currentNoSTAsal;
//                            Log.e("UpdateNoSTAsalTask", errorMessage);
//                            return false;
//                        }
//                    } else {
//                        errorMessage = "NoBJ tidak ditemukan di tabel S4S_h.";
//                        Log.e("UpdateNoSTAsalTask", errorMessage);
//                        return false;
//                    }
//                } catch (SQLException e) {
//                    errorMessage = "SQL Error: " + e.getMessage();
//                    Log.e("Database Error", errorMessage);
//                    return false;
//                } finally {
//                    try {
//                        con.close();
//                    } catch (SQLException e) {
//                        Log.e("Connection Close Error", e.getMessage());
//                    }
//                }
//            } else {
//                errorMessage = "Koneksi ke database gagal.";
//                Log.e("Connection Error", errorMessage);
//                return false;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Boolean success) {
//            if (success) {
//                Toast.makeText(Packing.this, "NoSTAsal berhasil diperbarui.", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(Packing.this, "Gagal memperbarui NoSTAsal: " + errorMessage, Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    private class SetAndSaveNoS4STask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            String newNoBarangJadi = null;
            if (con != null) {
                try {
                    String query = "SELECT MAX(NoBJ) FROM dbo.BarangJadi_h";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        String lastNoBarangJadi = rs.getString(1);

                        if (lastNoBarangJadi != null && lastNoBarangJadi.startsWith("I.")) {
                            String numericPart = lastNoBarangJadi.substring(2);
                            int numericValue = Integer.parseInt(numericPart);
                            int newNumericValue = numericValue + 1;

                            newNoBarangJadi = "I." + String.format("%06d", newNumericValue);
                        }
                    }

                    rs.close();
                    ps.close();

                    if (newNoBarangJadi != null) {
                        String insertQuery = "INSERT INTO dbo.BarangJadi_h (NoBJ) VALUES (?)";
                        PreparedStatement insertPs = con.prepareStatement(insertQuery);
                        insertPs.setString(1, newNoBarangJadi);
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
            return newNoBarangJadi;
        }

        @Override
        protected void onPostExecute(String newNoBarangJadi) {
            if (newNoBarangJadi != null) {
                NoBarangJadi.setText(newNoBarangJadi);
                Toast.makeText(Packing.this, "NoBJ berhasil diatur dan disimpan.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Error", "Failed to set or save NoBJ.");
                Toast.makeText(Packing.this, "Gagal mengatur atau menyimpan NoBJ.", Toast.LENGTH_SHORT).show();
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
                ArrayAdapter<JenisKayu> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, jenisKayuList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinKayuP.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load jenis kayu.");
            }
        }
    }
    public class LoadJenisKayuTask2 extends AsyncTask<String, Void, List<JenisKayu>> {
        private String noBarangJadi;

        public LoadJenisKayuTask2(String noBarangJadi) {
            this.noBarangJadi = noBarangJadi;
        }

        @Override
        protected List<JenisKayu> doInBackground(String... params) {
            List<JenisKayu> jenisKayuList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT j.IdJenisKayu, j.Jenis " +
                            "FROM dbo.MstJenisKayu AS j " +
                            "INNER JOIN dbo.BarangJadi_h AS h ON h.IdJenisKayu = j.IdJenisKayu " +
                            "WHERE h.NoBJ = ? AND j.enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarangJadi);

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
                ArrayAdapter<JenisKayu> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, jenisKayuList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinKayuP.setAdapter(adapter);
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
                ArrayAdapter<Telly> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, tellyList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                SpinTellyP.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load telly data.");
            }
        }
    }


    private class LoadTellyTask2 extends AsyncTask<String, Void, List<Telly>> {
        private String noBarajangJadi;

        public LoadTellyTask2(String noBarajangJadi) {
            this.noBarajangJadi = noBarajangJadi;
        }

        @Override
        protected List<Telly> doInBackground(String... params) {
            List<Telly> tellyList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT t.IdOrgTelly, t.NamaOrgTelly " +
                            "FROM dbo.MstOrgTelly AS t " +
                            "INNER JOIN dbo.BarangJadi_h AS h ON h.IdOrgTelly = t.IdOrgTelly " +
                            "WHERE h.NoBJ = ? AND t.enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarajangJadi);

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
                ArrayAdapter<Telly> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, tellyList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinTellyP.setAdapter(adapter);
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
                ArrayAdapter<SPK> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, spkList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSPKP.setAdapter(adapter);
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
                ArrayAdapter<SPKAsal> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, spkAsalList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSPKAsalP.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load SPK data.");
            }
        }
    }

    private class LoadSPKTask2 extends AsyncTask<Void, Void, List<SPK>> {
        private String noBarajangJadi;

        public LoadSPKTask2(String noBarajangJadi) {
            this.noBarajangJadi = noBarajangJadi;
        }

        @Override
        protected List<SPK> doInBackground(Void... params) {
            List<SPK> spkList = new ArrayList<>();
            Connection con = ConnectionClass(); // Ensure this method establishes a database connection

            if (con != null) {
                try {
                    String query = "SELECT NoSPK FROM dbo.BarangJadi_h WHERE NoBJ = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarajangJadi);

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
                ArrayAdapter<SPK> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, spkList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSPKP.setAdapter(adapter);

                SpinSPKP.setEnabled(true);
            } else {
                Log.e("Error", "No SPK data found for the provided NoBJ.");
                SpinSPKP.setAdapter(null);
                SpinSPKP.setEnabled(false);
                Toast.makeText(Packing.this, "Tidak ada data SPK yang ditemukan.", Toast.LENGTH_SHORT).show();
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
                ArrayAdapter<Profile> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, profileList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinProfileP.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load profile data.");
            }
        }
    }

    private class LoadProfileTask2 extends AsyncTask<String, Void, List<Profile>> {
        private String noBarajangJadi;

        public LoadProfileTask2(String noBarajangJadi) {
            this.noBarajangJadi = noBarajangJadi;
        }

        @Override
        protected List<Profile> doInBackground(String... voids) {
            List<Profile> profileList = new ArrayList<>();
            Connection con = ConnectionClass(); // Assumes this method exists to establish a DB connection

            if (con != null) {
                try {
                    String query = "SELECT p.Profile, p.IdFJProfile " +
                            "FROM dbo.MstFJProfile AS p " +
                            "INNER JOIN dbo.BarangJadi_h AS h ON h.IdFJProfile = p.IdFJProfile " +
                            "WHERE h.NoBJ = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarajangJadi);

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
                ArrayAdapter<Profile> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, profileList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinProfileP.setAdapter(adapter);
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
                    String query = "SELECT NamaBarangJadi FROM dbo.MstBarangJadi";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String namaWarehouse = rs.getString("NamaBarangJadi");

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
                ArrayAdapter<Fisik> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, fisikList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinBarangJadiP.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load fisik data.");
            }
        }
    }

    private class LoadFisikTask2 extends AsyncTask<String, Void, List<Fisik>> {
        private String noBarangJadi;

        public LoadFisikTask2(String noBarangJadi) {
            this.noBarangJadi = noBarangJadi;
        }

        @Override
        protected List<Fisik> doInBackground(String... params) {
            List<Fisik> fisikList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "SELECT mw.NamaWarehouse " +
                            "FROM dbo.MstWarehouse mw " +
                            "INNER JOIN dbo.BarangJadi_h bj ON mw.IdWarehouse = bj.IdWarehouse " +
                            "WHERE bj.NoBJ = ?";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarangJadi);

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
                ArrayAdapter<Fisik> adapter = new ArrayAdapter<>(Packing.this,
                        android.R.layout.simple_spinner_item, fisikList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinBarangJadiP.setAdapter(adapter);
            } else {
                Log.e("Error", "No warehouse found.");
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(Packing.this,
                        android.R.layout.simple_spinner_item, new String[]{"Tidak ada Fisik"});
                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinBarangJadiP.setAdapter(emptyAdapter);
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

                    String query = "SELECT a.IdMesin, b.NamaMesin, a.NoProduksi FROM dbo.PackingProduksi_h a " +
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
                ArrayAdapter<Mesin> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinMesinP.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load mesin data.");
            }
        }
    }

    private class LoadMesinTask2 extends AsyncTask<Void, Void, List<Mesin>> {
        private String noBarangJadi;

        public LoadMesinTask2(String noBarangJadi) {
            this.noBarangJadi = noBarangJadi;
        }

        @Override
        protected List<Mesin> doInBackground(Void... params) {
            List<Mesin> mesinList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT b.NoProduksi, d.NamaMesin FROM BarangJadi_h a " +
                            "INNER JOIN PackingProduksiOutput b ON b.NoBJ = a.NoBJ " +
                            "INNER JOIN PackingProduksi_h c ON c.NoProduksi = b.NoProduksi " +
                            "INNER JOIN MstMesin d ON d.IdMesin = c.IdMesin " +
                            "WHERE a.NoBJ = ?";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarangJadi);

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
                ArrayAdapter<Mesin> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinMesinP.setAdapter(adapter);

                radioButtonMesinP.setEnabled(true);
                radioButtonBSusunP.setEnabled(false);
            } else {
                Log.e("Error", "Failed to load mesin data.");
                radioButtonMesinP.setEnabled(false);
                radioButtonBSusunP.setEnabled(false);

                Toast.makeText(Packing.this, "Tidak ada data mesin yang ditemukan.", Toast.LENGTH_SHORT).show();
                SpinMesinP.setAdapter(null);
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
                ArrayAdapter<Susun> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, susunList);
                SpinSusunP.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load susun data");
            }
        }
    }
    private class LoadSusunTask2 extends AsyncTask<Void, Void, List<Susun>> {
        private String noBarangJadi;

        public LoadSusunTask2(String noBarangJadi) {
            this.noBarangJadi = noBarangJadi;
        }

        @Override
        protected List<Susun> doInBackground(Void... params) {
            List<Susun> susunList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT NoBongkarSusun FROM dbo.BongkarSusunOutputBarangJadi WHERE NoBJ = ?"; // Filter by noS4S
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noBarangJadi);
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
                ArrayAdapter<Susun> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, susunList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSusunP.setAdapter(adapter);

                radioButtonMesinP.setEnabled(false);
                radioButtonBSusunP.setEnabled(true);
            } else {
                Log.e("Error", "Failed to load susun data.");
                radioButtonMesinP.setEnabled(false);
                radioButtonBSusunP.setEnabled(false);

                Toast.makeText(Packing.this, "Tidak ada data susun yang ditemukan.", Toast.LENGTH_SHORT).show();
                SpinSusunP.setAdapter(null);
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

