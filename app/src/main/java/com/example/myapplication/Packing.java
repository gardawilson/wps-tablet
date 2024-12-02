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
import android.widget.SearchView;
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
import android.app.TimePickerDialog;
import android.widget.TimePicker;
import android.widget.AutoCompleteTextView;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;




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
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


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

public class Packing extends AppCompatActivity {

    private SearchView NoBarangJadi;
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
    private RadioGroup RadioGroupP;
    private RadioButton radioButtonMesinP;
    private RadioButton radioButtonBSusunP;
    private Button BtnDataBaruP;
    private Button BtnSimpanP;
    private Button BtnBatalP;
    private Button BtnHapusDetailP;
    private boolean isDataBaruClickedP = false;
    private CheckBox CBAfkirP;
    private CheckBox CBLemburP;
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
    private int rowCount = 0;
    private TableLayout Tabel;
    boolean isCreateMode = false;


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
        BtnInputDetailP = findViewById(R.id.BtnInputDetailP);
        DetailPcsP = findViewById(R.id.DetailPcsP);
        DetailTebalP = findViewById(R.id.DetailTebalP);
        DetailPanjangP = findViewById(R.id.DetailPanjangP);
        DetailLebarP = findViewById(R.id.DetailLebarP);
        BtnPrintP = findViewById(R.id.BtnPrintP);
        M3P = findViewById(R.id.M3P);
        JumlahPcsP = findViewById(R.id.JumlahPcsP);
        Tabel = findViewById(R.id.Tabel);
        RadioGroupP = findViewById(R.id.RadioGroupP);


        NoBarangJadi.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!isCreateMode) {
                    loadSubmittedData(query);
                    closeKeyboard();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!isCreateMode) {
                    if(!newText.isEmpty()){
                        disableForm();
                        loadSubmittedData(newText);
                    }
                    else{
                        enableForm();
                    }
                }
                return true;
            }
        });


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
            setCurrentDateTime();
            setCreateMode(true);

            new SetAndSaveNoBJTask().execute();
            new LoadJenisKayuTask().execute();
            new LoadTellyTask().execute();
            new LoadSPKTask().execute();
            new LoadSPKAsalTask().execute();
            new LoadProfileTask().execute();
            new LoadFisikTask().execute();
            new LoadMesinTask().execute();
            new LoadSusunTask().execute();

            BtnSimpanP.setEnabled(true);
            BtnBatalP.setEnabled(true);
            radioButtonMesinP.setEnabled(true);
            radioButtonBSusunP.setEnabled(true);
            BtnDataBaruP.setEnabled(false);

            clearData();
            resetDetailData();
            enableForm();
        });

        BtnSimpanP.setOnClickListener(v -> {
            String noBarangJadi = NoBarangJadi.getQuery().toString();
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
            RadioGroup radioGroupUOMTblLebar = findViewById(R.id.radioGroupUOMTblLebar);
            RadioGroup radioGroupUOMPanjang = findViewById(R.id.radioGroupUOMPanjang);


            String idTelly = selectedTelly != null ? selectedTelly.getIdTelly() : null;
            String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
            String noSPKasal = selectedSPKAsal != null ? selectedSPKAsal.getNoSPKAsal() : null;
            String idProfile = selectedProfile != null ? selectedProfile.getIdFJProfile() : null;
            String idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;
            String noProduksi = selectedMesin != null ? selectedMesin.getNoProduksi() : null;
            String noBongkarSusun = selectedSusun != null ? selectedSusun.getNoBongkarSusun() : null;
            int isReject = CBAfkirP.isChecked() ? 1 : 0;
            int isLembur = CBLemburP.isChecked() ? 1 : 0;
            String idBarangJadi = selectedFisik != null ? selectedFisik.getIdBarangJadi() : null;
            int idUOMTblLebar = radioGroupUOMTblLebar.getCheckedRadioButtonId() == R.id.radioMillimeter ? 1 : 4;
            int idUOMPanjang;
            if (radioGroupUOMPanjang.getCheckedRadioButtonId() == R.id.radioCentimeter) {
                idUOMPanjang = 3;
            } else if (radioGroupUOMPanjang.getCheckedRadioButtonId() == R.id.radioMeter) {
                idUOMPanjang = 2;
            } else {
                idUOMPanjang = 1;
            }

            if (noBarangJadi.isEmpty() || dateCreate.isEmpty() || time.isEmpty() ||
                    NoWIP.getText().toString().isEmpty() ||
                    selectedTelly == null || selectedTelly.getIdTelly().isEmpty() ||
                    selectedSPK == null || selectedSPK.getNoSPK().equals("PILIH") ||
                    selectedSPKAsal == null || selectedSPKAsal.getNoSPKAsal().equals("PILIH") ||
                    selectedProfile == null || selectedProfile.getIdFJProfile().isEmpty() ||
                    selectedFisik == null || selectedFisik.getIdBarangJadi().equals("PILIH") ||
                    selectedJenisKayu == null || selectedJenisKayu.getIdJenisKayu().isEmpty() ||
                    (!radioButtonMesinP.isChecked() && !radioButtonBSusunP.isChecked()) ||
                    (radioButtonMesinP.isChecked() && (selectedMesin == null || selectedMesin.getNoProduksi().isEmpty())) ||
                    (radioButtonBSusunP.isChecked() && (selectedSusun == null || selectedSusun.getNoBongkarSusun().isEmpty())) || temporaryDataListDetail.isEmpty()) {


                Toast.makeText(Packing.this, "Pastikan semua field terisi dengan benar.", Toast.LENGTH_SHORT).show();
                return;
            }
            BtnDataBaruP.setEnabled(true);

            new UpdateDatabaseTask(
                    noBarangJadi,
                    dateCreate,
                    time,
                    idTelly,
                    noSPK,
                    noSPKasal,
                    idJenisKayu,
                    idProfile,
                    isReject,
                    isLembur,
                    idBarangJadi
            ).execute();

            if (radioButtonMesinP.isChecked() && SpinMesinP.isEnabled() && noProduksi != null) {
                new SaveToDatabaseTask(noProduksi, noBarangJadi).execute();
                for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                    Packing.DataRow dataRow = temporaryDataListDetail.get(i);
                    saveDataDetailToDatabase(noBarangJadi, i + 1, Double.parseDouble(dataRow.tebal), Double.parseDouble(dataRow.lebar),
                            Double.parseDouble(dataRow.panjang), Integer.parseInt(dataRow.pcs));
                }
            } else if (radioButtonBSusunP.isChecked() && SpinSusunP.isEnabled() && noBongkarSusun != null) {
                new SaveBongkarSusunTask(noBongkarSusun, noBarangJadi).execute();
                for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                    Packing.DataRow dataRow = temporaryDataListDetail.get(i);
                    saveDataDetailToDatabase(noBarangJadi, i + 1, Double.parseDouble(dataRow.tebal), Double.parseDouble(dataRow.lebar),
                            Double.parseDouble(dataRow.panjang), Integer.parseInt(dataRow.pcs));
                }
            } else {
                Toast.makeText(Packing.this, "Pilih opsi yang valid untuk disimpan.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(Packing.this, "Data berhasil disimpan dan tampilan telah dikosongkan.", Toast.LENGTH_SHORT).show();

        });

        BtnBatalP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCreateMode(false);
                resetDetailData();
                resetAllForm();
                disableForm();

                BtnDataBaruP.setEnabled(true);
                BtnSimpanP.setEnabled(false);
                Toast.makeText(Packing.this, "Tampilan telah dikosongkan.", Toast.LENGTH_SHORT).show();
            }
        });

        DateP.setOnClickListener(v -> showDatePickerDialog());

        TimeP.setOnClickListener(v -> showTimePickerDialog());

        BtnInputDetailP.setOnClickListener(v -> {
            String noBarangJadi = NoBarangJadi.getQuery().toString();

            if (!noBarangJadi.isEmpty()) {
                addDataDetail(noBarangJadi);
            } else {
                Toast.makeText(Packing.this, "NoBJ tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            m3();
            jumlahpcs();
        });

        BtnHapusDetailP.setOnClickListener(v -> {
            resetDetailData();
        });

        BtnPrintP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validasi input terlebih dahulu
                if (NoBarangJadi.getQuery() == null || NoBarangJadi.getQuery().toString().trim().isEmpty()) {
                    Toast.makeText(Packing.this, "Nomor FJ tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validasi apakah ada data yang akan dicetak
                if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
                    Toast.makeText(Packing.this, "Tidak ada data untuk dicetak", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cek status HasBeenPrinted di database
                String noBarangJadi = NoBarangJadi.getQuery().toString().trim();
                checkHasBeenPrinted(noBarangJadi, new HasBeenPrintedCallback() {
                    @Override
                    public void onResult(boolean hasBeenPrinted) {
                        try {
                            // Ambil data dari form dengan validasi null
                            String mesinSusun;
                            String jenisKayu = SpinKayuP.getSelectedItem() != null ? SpinKayuP.getSelectedItem().toString().trim() : "";
                            String date = DateP.getText() != null ? DateP.getText().toString().trim() : "";
                            String time = TimeP.getText() != null ? TimeP.getText().toString().trim() : "";
                            String tellyBy = SpinTellyP.getSelectedItem() != null ? SpinTellyP.getSelectedItem().toString().trim() : "";
                            String noSPK = SpinSPKP.getSelectedItem() != null ? SpinSPKP.getSelectedItem().toString().trim() : "";
                            String noSPKasal = SpinSPKAsalP.getSelectedItem() != null ? SpinSPKAsalP.getSelectedItem().toString().trim() : "";
                            String jumlahPcs = JumlahPcsP.getText() != null ? JumlahPcsP.getText().toString().trim() : "";
                            String m3 = M3P.getText() != null ? M3P.getText().toString().trim() : "";
                            String namaBJ = SpinBarangJadiP.getSelectedItem() != null ? SpinBarangJadiP.getSelectedItem().toString().trim() : "";
                            if(radioButtonMesinP.isChecked()){
                                mesinSusun = SpinMesinP.getSelectedItem() != null ? SpinMesinP.getSelectedItem().toString().trim() : "";
                            }
                            else{
                                mesinSusun = SpinSusunP.getSelectedItem() != null ? SpinSusunP.getSelectedItem().toString().trim() : "";
                            }

                            // Buat PDF dengan parameter hasBeenPrinted
                            Uri pdfUri = createPdf(noBarangJadi, jenisKayu, date, time, tellyBy, mesinSusun, noSPK, noSPKasal,
                                    temporaryDataListDetail, jumlahPcs, m3, hasBeenPrinted, namaBJ);

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
                                                    updatePrintStatus(noBarangJadi);
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
                                    Toast.makeText(Packing.this,
                                            "Error printing: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error";
                            Toast.makeText(Packing.this,
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

    //METHOD PACKING
    private void disableForm(){
        DetailTebalP.setEnabled(false);
        DetailLebarP.setEnabled(false);
        DetailPanjangP.setEnabled(false);
        DetailPcsP.setEnabled(false);
        BtnHapusDetailP.setEnabled(false);
        BtnInputDetailP.setEnabled(false);
        DateP.setEnabled(false);
        TimeP.setEnabled(false);
    }

    private void resetAllForm() {
        DateP.setText("");
        TimeP.setText("");
        NoBarangJadi.setQuery("",false);
        NoWIP.setText("");

        setSpinnerValue(SpinKayuP, "-");
        setSpinnerValue(SpinTellyP, "-");
        setSpinnerValue(SpinSPKP, "-");
        setSpinnerValue(SpinSPKAsalP, "-");
        setSpinnerValue(SpinProfileP, "-");
        setSpinnerValue(SpinMesinP, "-");
        setSpinnerValue(SpinSusunP, "-");

        radioButtonBSusunP.setEnabled(false);
        radioButtonMesinP.setEnabled(false);

    }


    private void enableForm(){
        DetailTebalP.setEnabled(true);
        DetailLebarP.setEnabled(true);
        DetailPanjangP.setEnabled(true);
        DetailPcsP.setEnabled(true);
        BtnHapusDetailP.setEnabled(true);
        BtnInputDetailP.setEnabled(true);
        DateP.setEnabled(true);
        TimeP.setEnabled(true);
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(NoBarangJadi.getWindowToken(), 0);
        }
    }

    //SET false jika ingin search data
    private void setCreateMode(boolean isCreate) {
        this.isCreateMode = isCreate;
    }

    // Method untuk set single value ke spinner
    private void setSpinnerValue(Spinner spinner, String value) {
        if (spinner != null) {
            String displayValue = value != null ? value : "-";
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item,
                    Collections.singletonList(displayValue));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }

    private void loadSubmittedData(String noBarangJadi) {
        // Tampilkan progress dialog
        resetDetailData();
        new Thread(() -> {
            Connection connection = null;
            try {
                connection = ConnectionClass();
                if (connection != null) {
                    // Query untuk header
                    String queryHeader = "SELECT " +
                            "o.NoProduksi, " +
                            "h.DateCreate, " +
                            "h.Jam, " +
                            "h.IdOrgTelly, " +
                            "t.NamaOrgTelly, " +
                            "h.NoSPK, " +
                            "h.NoSPKAsal, " +
                            "h.IdFJProfile, " +
                            "p.IdMesin, " +
                            "m.NamaMesin, " +
                            "s.NoBongkarSusun, " +
                            "f.Profile, " +
                            "h.IdJenisKayu, " +
                            "k.Jenis, " +
                            "h.IdBarangJadi, " +
                            "j.NamaBarangJadi " +
                            "FROM BarangJadi_h h " +
                            "LEFT JOIN PackingProduksiOutput o ON h.NoBJ = o.NoBJ " +
                            "LEFT JOIN PackingProduksi_h p ON o.NoProduksi = p.NoProduksi " +
                            "LEFT JOIN BongkarSusunOutputBarangJadi s ON h.NoBJ = s.NoBJ " +
                            "LEFT JOIN MstMesin m ON p.IdMesin = m.IdMesin " +
                            "LEFT JOIN MstOrgTelly t ON h.IdOrgTelly = t.IdOrgTelly " +
                            "LEFT JOIN MstFJProfile f ON h.IdFJProfile = f.IdFJProfile " +
                            "LEFT JOIN MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                            "LEFT JOIN MstBarangJadi j ON h.IdBarangJadi = j.IdBarangJadi " +
                            "WHERE h.NoBJ = ?";

                    // Query untuk detail
                    String queryDetail = "SELECT Tebal, Lebar, Panjang, JmlhBatang " +
                            "FROM BarangJadi_d " +
                            "WHERE NoBJ = ? " +
                            "ORDER BY NoUrut";

                    // Menggunakan try-with-resources untuk header
                    try (PreparedStatement stmt = connection.prepareStatement(queryHeader)) {
                        stmt.setString(1, noBarangJadi);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                // Mengambil data header
                                final String noProduksi = rs.getString("NoProduksi") != null ? rs.getString("NoProduksi") : "-";
                                final String dateCreate = rs.getString("DateCreate") != null ? rs.getString("DateCreate") : "-";
                                final String jam = rs.getString("Jam") != null ? rs.getString("Jam") : "-";
                                final String namaOrgTelly = rs.getString("NamaOrgTelly") != null ? rs.getString("NamaOrgTelly") : "-";
                                final String noSPK = rs.getString("NoSPK") != null ? rs.getString("NoSPK") : "-";
                                final String noSPKAsal = rs.getString("NoSPKAsal") != null ? rs.getString("NoSPKAsal") : "-";
                                final String namaMesin = rs.getString("NamaMesin") != null ? rs.getString("NamaMesin") : "-";
                                final String noBongkarSusun = rs.getString("NoBongkarSusun") != null ? rs.getString("NoBongkarSusun") : "-";
                                final String namaProfile = rs.getString("Profile") != null ? rs.getString("Profile") : "-";
                                final String namaKayu = rs.getString("Jenis") != null ? rs.getString("Jenis") : "-";
                                final String namaBarangJadi = rs.getString("NamaBarangJadi") != null ? rs.getString("NamaBarangJadi") : "-";




                                // Mengambil data detail
                                try (PreparedStatement stmtDetail = connection.prepareStatement(queryDetail)) {
                                    stmtDetail.setString(1, noBarangJadi);
                                    try (ResultSet rsDetail = stmtDetail.executeQuery()) {
                                        while (rsDetail.next()) {
                                            String tebal = rsDetail.getString("Tebal");
                                            String lebar = rsDetail.getString("Lebar");
                                            String panjang = rsDetail.getString("Panjang");
                                            String pcs = rsDetail.getString("JmlhBatang");

                                            // Buat objek DataRow baru dan tambahkan ke list
                                            DataRow newRow = new DataRow(tebal, lebar, panjang, pcs);
                                            temporaryDataListDetail.add(newRow);
                                        }
                                    }
                                }

                                // Update UI di thread utama
                                runOnUiThread(() -> {
                                    try {
                                        if (!namaMesin.equals("-")) {
                                            radioButtonMesinP.setChecked(true);
                                            radioButtonBSusunP.setEnabled(false);
                                        } else {
                                            radioButtonBSusunP.setChecked(true);
                                            radioButtonMesinP.setEnabled(false);
                                        }
                                        // Update header fields
                                        DateP.setText(dateCreate);
                                        TimeP.setText(jam);
                                        setSpinnerValue(SpinTellyP, namaOrgTelly);
                                        setSpinnerValue(SpinSPKP, noSPK);
                                        setSpinnerValue(SpinSPKAsalP, noSPKAsal);
                                        setSpinnerValue(SpinKayuP, namaKayu);
                                        setSpinnerValue(SpinProfileP, namaProfile);
                                        setSpinnerValue(SpinMesinP, namaMesin + " - " + noProduksi);
                                        setSpinnerValue(SpinSusunP, noBongkarSusun);
                                        setSpinnerValue(SpinBarangJadiP, namaBarangJadi);

                                        // Update tabel detail
                                        updateTableFromTemporaryData();
                                        m3();
                                        jumlahpcs();

                                        Toast.makeText(getApplicationContext(),
                                                "Data berhasil dimuat",
                                                Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {

                                        Log.e("UI Update Error", "Error updating UI: " + e.getMessage());
                                        Toast.makeText(getApplicationContext(),
                                                "Gagal memperbarui tampilan",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(() -> {

//                                    Toast.makeText(getApplicationContext(),
//                                            "Data tidak ditemukan untuk NoBarangJadi: " + noBarangJadi,
//                                            Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                    }
                } else {
                    runOnUiThread(() -> {

                        Toast.makeText(getApplicationContext(),
                                "Koneksi database gagal",
                                Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (SQLException e) {
                final String errorMessage = e.getMessage();
                runOnUiThread(() -> {

                    Toast.makeText(getApplicationContext(),
                            "Error: " + errorMessage,
                            Toast.LENGTH_LONG).show();
                    Log.e("Database Error", "Error executing query: " + errorMessage);
                });
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        Log.e("Database Error", "Error closing connection: " + e.getMessage());
                    }
                }
            }
        }).start();
    }

    // Method baru untuk memperbarui tabel dari temporaryDataListDetail
    private void updateTableFromTemporaryData() {
        // Reset tabel terlebih dahulu (hapus semua baris kecuali header)

        // Perbarui rowCount
        rowCount = 0;

        // Tambahkan setiap data ke tabel
        DecimalFormat df = new DecimalFormat("#,###.##");

        for (DataRow data : temporaryDataListDetail) {
            TableRow newRow = new TableRow(this);
            newRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // Tambahkan kolom-kolom dengan format yang sama seperti addDataDetail
            addTextViewToRowWithWeight(newRow, String.valueOf(++rowCount), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(data.tebal)), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(data.lebar)), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Float.parseFloat(data.panjang)), 1f);
            addTextViewToRowWithWeight(newRow, df.format(Integer.parseInt(data.pcs)), 1f);

            // Tambahkan tombol hapus
            Button deleteButton = new Button(this);
            deleteButton.setText("");
            deleteButton.setTextSize(12);

            TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            buttonParams.setMargins(5, 5, 5, 5);
            deleteButton.setLayoutParams(buttonParams);
            deleteButton.setPadding(10, 5, 10, 5);
            deleteButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            deleteButton.setTextColor(Color.BLACK);

            newRow.addView(deleteButton);
            Tabel.addView(newRow);
        }
    }


    // Interface untuk callback
    interface HasBeenPrintedCallback {
        void onResult(boolean hasBeenPrinted);
    }

    // Method untuk mengecek status HasBeenPrinted secara asynchronous
    private void checkHasBeenPrinted(String noBarangJadi, Packing.HasBeenPrintedCallback callback) {
        new Thread(() -> {
            boolean hasBeenPrinted = false;
            Connection connection = null;
            try {
                // Mendapatkan koneksi dari method ConnectionClass
                connection = ConnectionClass();
                if (connection != null) {
                    String query = "SELECT HasBeenPrinted FROM BarangJadi_h WHERE NoBJ = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, noBarangJadi);
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
    private void updatePrintStatus(String noBarangJadi) {
        new Thread(() -> {
            Connection connection = null;
            try {
                // Mendapatkan koneksi dari method ConnectionClass
                connection = ConnectionClass();
                if (connection != null) {
                    String query = "UPDATE BarangJadi_h SET HasBeenPrinted = 1 WHERE NoBJ = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, noBarangJadi);
                        int rowsAffected = stmt.executeUpdate();

                        if (rowsAffected > 0) {
                            runOnUiThread(() -> Toast.makeText(Packing.this,
                                    "Status cetak berhasil diupdate",
                                    Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(Packing.this,
                                    "Tidak ada data yang diupdate",
                                    Toast.LENGTH_SHORT).show());
                        }
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(Packing.this,
                            "Koneksi database gagal",
                            Toast.LENGTH_SHORT).show());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("Database", "Error updating HasBeenPrinted status: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(Packing.this,
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
                                NoBarangJadi.setQuery(noBarangJadi, true);
                                DateP.setText(dateCreate != null ? dateCreate : "");
                                TimeP.setText(jam != null ? jam : "");
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

    private void addDataDetail(String noBarangJadi) {
        String tebal = DetailTebalP.getText().toString();
        String panjang = DetailPanjangP.getText().toString();
        String lebar = DetailLebarP.getText().toString();
        String pcs = DetailPcsP.getText().toString();

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

            // Set listener tombol hapus
            deleteButton.setOnClickListener(v -> {
                Tabel.removeView(newRow);
                temporaryDataListDetail.remove(newDataRow);
                updateRowNumbers();
                jumlahpcs();
                m3();
            });

            newRow.addView(deleteButton);
            Tabel.addView(newRow);

            // Bersihkan field input
            DetailTebalP.setText("");
            DetailPanjangP.setText("");
            DetailLebarP.setText("");
            DetailPcsP.setText("");

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
        if (DetailTebalP != null) {
            DetailTebalP.setText("");
        }
        if (DetailLebarP != null) {
            DetailLebarP.setText("");
        }
        if (DetailPanjangP != null) {
            DetailPanjangP.setText("");
        }
        if (DetailPcsP != null) {
            DetailPcsP.setText("");
        }
    }

    private void saveDataDetailToDatabase(String noBarangJadi, int noUrut, double tebal, double lebar, double panjang, int pcs) {
        new Packing.SaveDataTaskDetail().execute(noBarangJadi, String.valueOf(noUrut), String.valueOf(tebal), String.valueOf(lebar),
                String.valueOf(panjang), String.valueOf(pcs));
    }

    private class SaveDataTaskDetail extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String noBarangJadi = params[0];
            String noUrut = params[1];
            String tebal = params[2];
            String lebar = params[3];
            String panjang = params[4];
            String pcs = params[5];

            try {
                Connection connection = ConnectionClass();
                if (connection != null) {
                    String query = "INSERT INTO dbo.BarangJadi_d (NoBJ, NoUrut, Tebal, Lebar, Panjang, JmlhBatang) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, noBarangJadi);
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


    private void clearData() {
        NoBarangJadi.setQuery("", false);
        M3P.setText("");
        JumlahPcsP.setText("");
        NoWIP.setText("");
        CBAfkirP.setChecked(false);
        CBLemburP.setChecked(false);
        SpinKayuP.setSelection(0);
        SpinTellyP.setSelection(0);
        SpinSPKP.setSelection(0);
        SpinSPKAsalP.setSelection(0);
        SpinProfileP.setSelection(0);
        SpinMesinP.setEnabled(false);
        SpinSusunP.setEnabled(false);
        RadioGroupP.clearCheck();
    }


    private void m3() {
        try {
            double totalM3 = 0.0;

            for (DataRow row : temporaryDataListDetail) {

                // Parse nilai-nilai langsung tanpa membersihkan
                double tebal = Double.parseDouble(row.tebal);
                double lebar = Double.parseDouble(row.lebar);
                double panjang = Double.parseDouble(row.panjang);
                int pcs = Integer.parseInt(row.pcs);

                // Hitung M3 untuk baris ini
                double rowM3 = (tebal * lebar * panjang * pcs) / 1000000000.0;

                totalM3 += rowM3;
            }

            // Format hasil
            DecimalFormat df = new DecimalFormat("0.0000");
            String formattedM3 = df.format(totalM3);

            // Update TextView
            TextView M3TextView = findViewById(R.id.M3P);
            if (M3TextView != null) {
                M3TextView.setText(formattedM3);
                // Debug: Konfirmasi setText
                Log.d("M3_DEBUG", "TextView updated with: " + formattedM3);
            } else {
                Log.e("M3_DEBUG", "M3TextView is null!");
            }

        } catch (Exception e) {
            Log.e("M3_DEBUG", "Error in m3 calculation: " + e.getMessage());
            e.printStackTrace();
        }
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

        JumlahPcsP.setText(String.valueOf(totalPcs));
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

    private Uri createPdf(String noBarangJadi, String jenisKayu, String date, String time, String tellyBy, String mesinSusun, String noSPK, String noSPKasal, List<DataRow> temporaryDataListDetail, String jumlahPcs, String m3, boolean hasBeenPrinted, String namaBJ) throws IOException {
        // Validasi parameter wajib
        if (noBarangJadi == null || noBarangJadi.trim().isEmpty()) {
            throw new IOException("Nomor Packing tidak boleh kosong");
        }

        if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
            throw new IOException("Data tidak boleh kosong");
        }

        // Validasi dan set default value untuk parameter opsional
        noBarangJadi = (noBarangJadi != null) ? noBarangJadi.trim() : "-";
        jenisKayu = (jenisKayu != null) ? jenisKayu.trim() : "-";
        date = (date != null) ? date.trim() : "-";
        time = (time != null) ? time.trim() : "-";
        tellyBy = (tellyBy != null) ? tellyBy.trim() : "-";
        noSPK = (noSPK != null) ? noSPK.trim() : "-";
        jumlahPcs = (jumlahPcs != null) ? jumlahPcs.trim() : "-";
        m3 = (m3 != null) ? m3.trim() : "-";
        namaBJ = (namaBJ != null) ? namaBJ.trim() : "-";

        Uri pdfUri = null;
        ContentResolver resolver = getContentResolver();
        String fileName = "S4S_" + noBarangJadi + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".pdf";
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
                float baseHeight = 375; // Tinggi dasar untuk elemen non-tabel (header, footer, margin, dll.)
                float rowHeight = 20; // Tinggi rata-rata per baris data
                float totalHeight = baseHeight + (rowHeight * temporaryDataListDetail.size());

                // Tetapkan ukuran halaman dinamis
                Rectangle pageSize = new Rectangle( PageSize.A6.getWidth(), totalHeight);
                pdfDocument.setDefaultPageSize(new PageSize(pageSize));

                Document document = new Document(pdfDocument);
                document.setMargins(0, 5, 0, 5);

                // Header
                Paragraph judul = new Paragraph("LABEL PACKING")
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
                addInfoRow(leftColumn, "No BJ", noBarangJadi, timesNewRoman);
                addInfoRow(leftColumn, "Jenis Kayu", jenisKayu, timesNewRoman);
                addInfoRow(leftColumn, "Nama BJ", namaBJ, timesNewRoman);
//                addInfoRow(leftColumn, "Fisik", "-", timesNewRoman);

                // Buat tabel untuk kolom kanan
                Table rightColumn = new Table(infoColumnWidths)
                        .setWidth(pageWidth/2 - 5)
                        .setMarginLeft(20)
                        .setBorder(Border.NO_BORDER);

                // Isi kolom kanan
                addInfoRow(rightColumn, "Tanggal", date + " (" + time + ")", timesNewRoman);
                addInfoRow(rightColumn, "Telly", tellyBy, timesNewRoman);
//                addInfoRow(rightColumn, "Mesin", mesinSusun, timesNewRoman);
                addInfoRow(rightColumn, "No SPK", noSPK, timesNewRoman);

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

                sumTable.addCell(new Cell().add(new Paragraph("m3")).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));
                sumTable.addCell(new Cell().add(new Paragraph(":")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                sumTable.addCell(new Cell().add(new Paragraph(String.valueOf(m3))).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));

                Paragraph qrCodeIDbottom = new Paragraph(noBarangJadi).setTextAlignment(TextAlignment.RIGHT).setFontSize(8).setMargins(-5, 20, 0, 0).setFont(timesNewRoman);

                BarcodeQRCode qrCode = new BarcodeQRCode(noBarangJadi);
                PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);

                Image qrCodeInputLeft = new Image(qrCodeObject).setWidth(75).setHorizontalAlignment(HorizontalAlignment.LEFT);
                Image qrCodeInputRight = new Image(qrCodeObject).setWidth(75).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                Image qrCodeBottomImage = new Image(qrCodeObject).setWidth(75).setHorizontalAlignment(HorizontalAlignment.RIGHT).setMargins(-5, 0, 0, 0);

                Paragraph bottomLine = new Paragraph("-----------------------------------------------------------------------------------------------------").setTextAlignment(TextAlignment.CENTER).setFontSize(8).setMargins(0, 0, 0, 15).setFont(timesNewRoman);
                Paragraph outputTextBottom = new Paragraph("Output").setTextAlignment(TextAlignment.RIGHT).setFontSize(8).setMargins(15, 28, 0, 0).setFont(timesNewRoman);

                float[] columnWidthsQR = {140f, 140f};
                Table qrTable = new Table(columnWidthsQR)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setMarginTop(10)
                        .setFontSize(8)
                        .setBorder(Border.NO_BORDER);




                qrTable.addCell(new Cell().add(new Paragraph("Input")).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER).setPaddings(0, 0, -10, 30));
                qrTable.addCell(new Cell().add(new Paragraph("Input")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setPaddings(0, 30, -10, 0));


                qrTable.addCell(new Cell().add(qrCodeInputLeft).setBorder(Border.NO_BORDER));
                qrTable.addCell(new Cell().add(qrCodeInputRight).setBorder(Border.NO_BORDER));


                qrTable.addCell(new Cell().add(new Paragraph(String.valueOf(noBarangJadi))).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER).setPaddings(-10,0,0,25));
                qrTable.addCell(new Cell().add(new Paragraph(String.valueOf(noBarangJadi))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setPaddings(-10,25,0,0));


                // Tambahkan semua elemen ke dokumen
                document.add(judul);
                Log.d("DEBUG_TAG", "Value of hasBeenPrinted: " + hasBeenPrinted);
                if (hasBeenPrinted) {
                    addTextDitheringWatermark(pdfDocument, timesNewRoman);
                }
                document.add(mainTable);
                document.add(table);
                document.add(sumTable);
                document.add(qrTable);
                document.add(bottomLine);
                document.add(outputTextBottom);
                document.add(qrCodeBottomImage);
                document.add(qrCodeIDbottom);

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

    private class UpdateDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private String noBarangJadi, dateCreate, time, idTelly, noSPK, noSPKasal, idJenisKayu, idFJProfile, idBarangJadi;
        private int isReject, isLembur;

        public UpdateDatabaseTask(String noBarangJadi, String dateCreate, String time,
                                  String idTelly, String noSPK, String noSPKasal,
                                  String idJenisKayu, String idFJProfile,
                                  int isReject, int isLembur, String idBarangJadi) {
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
            this.idBarangJadi = idBarangJadi;
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
                            "NoSPKAsal = ?, " +
                            "IdFJProfile = ?, " +
                            "IdJenisKayu = ?, " +
                            "IdWarehouse = ?, " +
                            "IsReject = ?, " +
                            "IsLembur = ?, " +
                            "IdBarangJadi = ? " +
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
                    ps.setInt(8, 11);
                    ps.setInt(9, isReject);
                    ps.setInt(10, isLembur);
                    ps.setString(11, idBarangJadi);
                    ps.setString(12, noBarangJadi);

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

    private class SetAndSaveNoBJTask extends AsyncTask<Void, Void, String> {
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
                NoBarangJadi.setQuery(newNoBarangJadi, true);
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
            JenisKayu dummyKayu = new JenisKayu("", "PILIH");
            jenisKayuList.add(0, dummyKayu);

            ArrayAdapter<JenisKayu> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, jenisKayuList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinKayuP.setAdapter(adapter);
            SpinKayuP.setSelection(0);
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
            // Tambahkan elemen dummy di awal
            Telly dummyTelly = new Telly("", "PILIH");
            tellyList.add(0, dummyTelly);

            // Buat adapter dengan data yang dimodifikasi
            ArrayAdapter<Telly> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, tellyList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Set adapter ke spinner
            SpinTellyP.setAdapter(adapter);

            // Atur spinner untuk menampilkan elemen pertama ("Pilih") secara default
            SpinTellyP.setSelection(0);
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
            SPK dummySPK = new SPK("PILIH");
            spkList.add(0, dummySPK);

            ArrayAdapter<SPK> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, spkList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinSPKP.setAdapter(adapter);
            SpinSPKP.setSelection(0);
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
            SPKAsal dummySPKAsal = new SPKAsal("PILIH");
            spkAsalList.add(0, dummySPKAsal);

            ArrayAdapter<SPKAsal> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, spkAsalList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinSPKAsalP.setAdapter(adapter);
            SpinSPKAsalP.setSelection(0);
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
            Profile dummyProfile = new Profile("PILIH", "");
            profileList.add(0, dummyProfile);

            ArrayAdapter<Profile> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, profileList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinProfileP.setAdapter(adapter);
            SpinProfileP.setSelection(0);
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
                    String query = "SELECT IdBarangJadi, NamaBarangJadi FROM dbo.MstBarangJadi";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String namaBarangJadi = rs.getString("NamaBarangJadi");
                        String idBarangJadi = rs.getString("IdBarangJadi");
                        Fisik fisikObj = new Fisik(namaBarangJadi, idBarangJadi);
                        fisikList.add(fisikObj);
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
            Fisik dummyFisik = new Fisik("PILIH", "");
            fisikList.add(0, dummyFisik);

            ArrayAdapter<Fisik> adapter = new ArrayAdapter<>(Packing.this, android.R.layout.simple_spinner_item, fisikList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinBarangJadiP.setAdapter(adapter);
            SpinBarangJadiP.setSelection(0);
        }
    }

//    private class LoadFisikTask2 extends AsyncTask<String, Void, List<Fisik>> {
//        private String noBarangJadi;
//
//        public LoadFisikTask2(String noBarangJadi) {
//            this.noBarangJadi = noBarangJadi;
//        }
//
//        @Override
//        protected List<Fisik> doInBackground(String... params) {
//            List<Fisik> fisikList = new ArrayList<>();
//            Connection con = ConnectionClass();
//            if (con != null) {
//                try {
//                    String query = "SELECT mw.NamaWarehouse " +
//                            "FROM dbo.MstWarehouse mw " +
//                            "INNER JOIN dbo.BarangJadi_h bj ON mw.IdWarehouse = bj.IdWarehouse " +
//                            "WHERE bj.NoBJ = ?";
//
//                    PreparedStatement ps = con.prepareStatement(query);
//                    ps.setString(1, noBarangJadi);
//
//                    ResultSet rs = ps.executeQuery();
//
//                    while (rs.next()) {
//                        String namaWarehouse = rs.getString("NamaWarehouse");
//                        Fisik fisik = new Fisik(namaWarehouse);
//                        fisikList.add(fisik);
//                    }
//
//                    rs.close();
//                    ps.close();
//                    con.close();
//                } catch (Exception e) {
//                    Log.e("Database Error", "Error during query execution: " + e.getMessage());
//                }
//            } else {
//                Log.e("Connection Error", "Failed to connect to the database.");
//            }
//
//            return fisikList;
//        }
//
//        @Override
//        protected void onPostExecute(List<Fisik> fisikList) {
//            if (!fisikList.isEmpty()) {
//                ArrayAdapter<Fisik> adapter = new ArrayAdapter<>(Packing.this,
//                        android.R.layout.simple_spinner_item, fisikList);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                SpinBarangJadiP.setAdapter(adapter);
//            } else {
//                Log.e("Error", "No warehouse found.");
//                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(Packing.this,
//                        android.R.layout.simple_spinner_item, new String[]{"Tidak ada Fisik"});
//                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                SpinBarangJadiP.setAdapter(emptyAdapter);
//            }
//        }
//    }

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
                SpinMesinP.setAdapter(null);
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
                SpinSusunP.setAdapter(null);
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
        private String idBarangJadi;
        private String namaBarangJadi;

        public Fisik(String namaBarangJadi, String idBarangJadi) {
            this.namaBarangJadi = namaBarangJadi;
            this.idBarangJadi = idBarangJadi;
        }

        public String getIdBarangJadi() {
            return idBarangJadi;
        }

        public String getNamaBarangJadi() {
            return namaBarangJadi;
        }

        @Override
        public String toString() {
            return namaBarangJadi;
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

