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
import android.app.TimePickerDialog;
import android.widget.TimePicker;


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


public class Sanding extends AppCompatActivity {
    private EditText NoSanding;
    private EditText DateS;
    private EditText TimeS;
    private EditText NoMouldingAsal;
    private Spinner SpinKayuS;
    private Spinner SpinTellyS;
    private Spinner SpinSPKS;
    private Spinner SpinSPKAsalS;
    private Spinner SpinProfileS;
    private Spinner SpinFisikS;
    private Spinner SpinGradeS;
    private Spinner SpinMesinS;
    private Spinner SpinSusunS;
    private Calendar calendarS;
    private RadioGroup RadioGroupS;
    private RadioButton radioButtonMesinS;
    private RadioButton radioButtonBSusunS;
    private Button BtnDataBaruS;
    private Button BtnSimpanS;
    private Button BtnBatalS;
    private Button BtnHapusDetailS;
    private boolean isDataBaruClickedS = false;
    private CheckBox CBAfkirS;
    private CheckBox CBLemburS;
    private Button BtnInputDetailS;
    private EditText DetailLebarS;
    private EditText DetailTebalS;
    private EditText DetailPanjangS;
    private EditText DetailPcsS;
    private static int currentNumber = 1;
    private Button BtnPrintS;
    private TextView M3S;
    private TextView JumlahPcsS;
    private boolean isCBAfkirS, isCBLemburS;
    private Button BtnSearchS;
    private int rowCount = 0;
    private TableLayout Tabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sanding);

        NoMouldingAsal = findViewById(R.id.NoMouldingAsal);
        NoSanding = findViewById(R.id.NoSanding);
        DateS = findViewById(R.id.DateS);
        TimeS = findViewById(R.id.TimeS);
        SpinKayuS = findViewById(R.id.SpinKayuS);
        SpinTellyS = findViewById(R.id.SpinTellyS);
        SpinSPKS = findViewById(R.id.SpinSPKS);
        SpinSPKAsalS = findViewById(R.id.SpinSPKAsalS);
        SpinProfileS = findViewById(R.id.SpinProfileS);
        SpinFisikS = findViewById(R.id.SpinFisikS);
        SpinGradeS = findViewById(R.id.SpinGradeS);
        calendarS = Calendar.getInstance();
        SpinMesinS = findViewById(R.id.SpinMesinS);
        SpinSusunS = findViewById(R.id.SpinSusunS);
        radioButtonMesinS = findViewById(R.id.radioButtonMesinS);
        radioButtonBSusunS = findViewById(R.id.radioButtonBSusunS);
        BtnDataBaruS = findViewById(R.id.BtnDataBaruS);
        BtnSimpanS = findViewById(R.id.BtnSimpanS);
        BtnBatalS = findViewById(R.id.BtnBatalS);
        BtnHapusDetailS = findViewById(R.id.BtnHapusDetailS);
        CBLemburS = findViewById(R.id.CBLemburS);
        CBAfkirS = findViewById(R.id.CBAfkirS);
        BtnInputDetailS = findViewById(R.id.BtnInputDetailS);
        DetailPcsS = findViewById(R.id.DetailPcsS);
        DetailTebalS = findViewById(R.id.DetailTebalS);
        DetailPanjangS = findViewById(R.id.DetailPanjangS);
        DetailLebarS = findViewById(R.id.DetailLebarS);
        BtnPrintS = findViewById(R.id.BtnPrintS);
        M3S = findViewById(R.id.M3S);
        JumlahPcsS = findViewById(R.id.JumlahPcsS);
        BtnSearchS = findViewById(R.id.BtnSearchS);
        Tabel = findViewById(R.id.Tabel);
        RadioGroupS = findViewById(R.id.RadioGroupS);

        radioButtonMesinS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpinMesinS.setEnabled(true);
                    SpinSusunS.setEnabled(false);
                }
            }
        });

        radioButtonBSusunS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpinSusunS.setEnabled(true);
                    SpinMesinS.setEnabled(false);
                }
            }
        });

        setCurrentDateTime();

        BtnDataBaruS.setOnClickListener(v -> {
            if (!isDataBaruClickedS) {
                new LoadJenisKayuTask().execute();
                new LoadTellyTask().execute();
                new LoadSPKTask().execute();
                new LoadSPKAsalTask().execute();
                new LoadProfileTask().execute();
                new LoadFisikTask().execute();
                new LoadGradeTask().execute();
                new LoadMesinTask().execute();
                new LoadSusunTask().execute();

                isDataBaruClickedS = true;
                setCurrentDateTime();
            } else {
                Toast.makeText(Sanding.this, "Tombol Data Baru sudah diklik. Klik Simpan terlebih dahulu.", Toast.LENGTH_SHORT).show();
            }
            BtnSimpanS.setEnabled(true);
            new SetAndSaveNoSandingTask().execute();
            BtnBatalS.setEnabled(true);
            radioButtonMesinS.setEnabled(true);
            radioButtonBSusunS.setEnabled(true);
            setCurrentDateTime();
            BtnDataBaruS.setEnabled(false);
        });

        BtnSimpanS.setOnClickListener(v -> {
            String noSanding = NoSanding.getText().toString();
            String dateCreate = DateS.getText().toString();
            String time = TimeS.getText().toString();

            Telly selectedTelly = (Telly) SpinTellyS.getSelectedItem();
            SPK selectedSPK = (SPK) SpinSPKS.getSelectedItem();
            SPKAsal selectedSPKAsal = (SPKAsal) SpinSPKAsalS.getSelectedItem();
            Profile selectedProfile = (Profile) SpinProfileS.getSelectedItem();
            Fisik selectedFisik = (Fisik) SpinFisikS.getSelectedItem();
            Grade selectedGrade = (Grade) SpinGradeS.getSelectedItem();
            JenisKayu selectedJenisKayu = (JenisKayu) SpinKayuS.getSelectedItem();
            Mesin selectedMesin = (Mesin) SpinMesinS.getSelectedItem();
            Susun selectedSusun = (Susun) SpinSusunS.getSelectedItem();
            RadioGroup radioGroupUOMTblLebar = findViewById(R.id.radioGroupUOMTblLebar);
            RadioGroup radioGroupUOMPanjang = findViewById(R.id.radioGroupUOMPanjang);

            String idGrade = selectedGrade != null ? selectedGrade.getIdGrade() : null;
            String idTelly = selectedTelly != null ? selectedTelly.getIdTelly() : null;
            String noSPK = selectedSPK != null ? selectedSPK.getNoSPK() : null;
            String noSPKasal = selectedSPKAsal != null ? selectedSPKAsal.getNoSPKAsal() : null;
            String idProfile = selectedProfile != null ? selectedProfile.getIdFJProfile() : null;
            String idJenisKayu = selectedJenisKayu != null ? selectedJenisKayu.getIdJenisKayu() : null;
            String noProduksi = selectedMesin != null ? selectedMesin.getNoProduksi() : null;
            String noBongkarSusun = selectedSusun != null ? selectedSusun.getNoBongkarSusun() : null;
            int isReject = CBAfkirS.isChecked() ? 1 : 0;
            int isLembur = CBLemburS.isChecked() ? 1 : 0;
            int idUOMTblLebar = radioGroupUOMTblLebar.getCheckedRadioButtonId() == R.id.radioMillimeter ? 1 : 4;
            int idUOMPanjang;
            if (radioGroupUOMPanjang.getCheckedRadioButtonId() == R.id.radioCentimeter) {
                idUOMPanjang = 3;
            } else if (radioGroupUOMPanjang.getCheckedRadioButtonId() == R.id.radioMeter) {
                idUOMPanjang = 2;
            } else {
                idUOMPanjang = 1;
            }

            if (noSanding.isEmpty() || dateCreate.isEmpty() || time.isEmpty() ||
                    selectedTelly == null || selectedTelly.getIdTelly().isEmpty() ||
                    selectedSPK == null || selectedSPK.getNoSPK().equals("PILIH") ||
                    selectedSPKAsal == null || selectedSPKAsal.getNoSPKAsal().equals("PILIH") ||
                    selectedProfile == null || selectedProfile.getIdFJProfile().isEmpty() ||
                    selectedFisik == null || selectedFisik.getNamaWarehouse().equals("PILIH") ||
                    selectedGrade == null || selectedGrade.getIdGrade().isEmpty() ||
                    selectedJenisKayu == null || selectedJenisKayu.getIdJenisKayu().isEmpty() ||
                    (!radioButtonMesinS.isChecked() && !radioButtonBSusunS.isChecked()) ||
                    (radioButtonMesinS.isChecked() && (selectedMesin == null || selectedMesin.getNoProduksi().isEmpty())) ||
                    (radioButtonBSusunS.isChecked() && (selectedSusun == null || selectedSusun.getNoBongkarSusun().isEmpty())) || temporaryDataListDetail.isEmpty()) {


                Toast.makeText(Sanding.this, "Pastikan semua field terisi dengan benar.", Toast.LENGTH_SHORT).show();
                return;
            }
            BtnDataBaruS.setEnabled(true);
            BtnPrintS.setEnabled(true);

            new UpdateDatabaseTask(
                    noSanding,
                    dateCreate,
                    time,
                    idTelly,
                    noSPK,
                    noSPKasal,
                    idGrade,
                    idJenisKayu,
                    idProfile,
                    isReject,
                    isLembur,
                    idUOMTblLebar,
                    idUOMPanjang
            ).execute();

            if (radioButtonMesinS.isChecked() && SpinMesinS.isEnabled() && noProduksi != null) {
                new SaveToDatabaseTask(noProduksi, noSanding).execute();
                for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                    Sanding.DataRow dataRow = temporaryDataListDetail.get(i);
                    saveDataDetailToDatabase(noSanding, i + 1, Double.parseDouble(dataRow.tebal), Double.parseDouble(dataRow.lebar),
                            Double.parseDouble(dataRow.panjang), Integer.parseInt(dataRow.pcs));
                }
            } else if (radioButtonBSusunS.isChecked() && SpinSusunS.isEnabled() && noBongkarSusun != null) {
                new SaveBongkarSusunTask(noBongkarSusun, noSanding).execute();
                for (int i = 0; i < temporaryDataListDetail.size(); i++) {
                    Sanding.DataRow dataRow = temporaryDataListDetail.get(i);
                    saveDataDetailToDatabase(noSanding, i + 1, Double.parseDouble(dataRow.tebal), Double.parseDouble(dataRow.lebar),
                            Double.parseDouble(dataRow.panjang), Integer.parseInt(dataRow.pcs));
                }
            } else {
                Toast.makeText(Sanding.this, "Pilih opsi yang valid untuk disimpan.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(Sanding.this, "Data berhasil disimpan dan tampilan telah dikosongkan.", Toast.LENGTH_SHORT).show();

        });

        BtnBatalS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentDateTime();
                resetDetailData();
                clearData();
                BtnDataBaruS.setEnabled(true);
            }
        });

        BtnSearchS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noSanding = NoSanding.getText().toString();


                DetailLebarS.setText("");
                DetailPanjangS.setText("");
                DetailTebalS.setText("");
                DetailPcsS.setText("");
                NoSanding.setText("");

                if (!noSanding.isEmpty()) {
                    new LoadMesinTask2(noSanding).execute();
                    new LoadSusunTask2(noSanding).execute();
                    new LoadFisikTask2(noSanding).execute();
                    new LoadGradeTask2(noSanding).execute();
                    new LoadJenisKayuTask2(noSanding).execute();
                    new LoadTellyTask2(noSanding).execute();
                    new LoadSPKTask2(noSanding).execute();
                    new LoadProfileTask2(noSanding).execute();
                    new SearchAllDataTask(noSanding).execute();

                    radioButtonMesinS.setEnabled(true);
                    SpinSPKS.setEnabled(true);
                    radioButtonBSusunS.setEnabled(true);
                } else {
                    Log.e("Input Error", "NoSanding is empty");
                    radioButtonMesinS.setEnabled(false);
                    radioButtonBSusunS.setEnabled(false);
                }

                BtnSimpanS.setEnabled(false);
                BtnBatalS.setEnabled(false);
                BtnPrintS.setEnabled(true);
            }
        });

        SpinKayuS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        DateS.setOnClickListener(v -> showDatePickerDialog());

        TimeS.setOnClickListener(v -> showTimePickerDialog());

        BtnInputDetailS.setOnClickListener(v -> {
            String noSanding = NoSanding.getText().toString();

            if (!noSanding.isEmpty()) {
                addDataDetail(noSanding);
            } else {
                Toast.makeText(Sanding.this, "NoSanding tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            jumlahpcs();
            m3();
        });

        BtnHapusDetailS.setOnClickListener(v -> {
            resetDetailData();
        });

        BtnPrintS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validasi input terlebih dahulu
                if (NoSanding.getText() == null || NoSanding.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Sanding.this, "Nomor FJ tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validasi apakah ada data yang akan dicetak
                if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
                    Toast.makeText(Sanding.this, "Tidak ada data untuk dicetak", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cek status HasBeenPrinted di database
                String noSanding = NoSanding.getText().toString().trim();
                checkHasBeenPrinted(noSanding, new HasBeenPrintedCallback() {
                    @Override
                    public void onResult(boolean hasBeenPrinted) {
                        try {
                            // Ambil data dari form dengan validasi null
                            String mesinSusun;
                            String jenisKayu = SpinKayuS.getSelectedItem() != null ? SpinKayuS.getSelectedItem().toString().trim() : "";
                            String date = DateS.getText() != null ? DateS.getText().toString().trim() : "";
                            String time = TimeS.getText() != null ? TimeS.getText().toString().trim() : "";
                            String tellyBy = SpinTellyS.getSelectedItem() != null ? SpinTellyS.getSelectedItem().toString().trim() : "";
                            String noSPK = SpinSPKS.getSelectedItem() != null ? SpinSPKS.getSelectedItem().toString().trim() : "";
                            String noSPKasal = SpinSPKAsalS.getSelectedItem() != null ? SpinSPKAsalS.getSelectedItem().toString().trim() : "";
                            String grade = SpinGradeS.getSelectedItem() != null ? SpinGradeS.getSelectedItem().toString().trim() : "";
                            String fisik = SpinFisikS.getSelectedItem() != null ? SpinFisikS.getSelectedItem().toString().trim() : "";
                            String jumlahPcs = JumlahPcsS.getText() != null ? JumlahPcsS.getText().toString().trim() : "";
                            String m3 = M3S.getText() != null ? M3S.getText().toString().trim() : "";
                            if(radioButtonMesinS.isChecked()){
                                mesinSusun = SpinMesinS.getSelectedItem() != null ? SpinMesinS.getSelectedItem().toString().trim() : "";
                            }
                            else{
                                mesinSusun = SpinSusunS.getSelectedItem() != null ? SpinSusunS.getSelectedItem().toString().trim() : "";
                            }

                            // Buat PDF dengan parameter hasBeenPrinted
                            Uri pdfUri = createPdf(noSanding, jenisKayu, date, time, tellyBy, mesinSusun, noSPK, noSPKasal, grade,
                                    temporaryDataListDetail, jumlahPcs, m3, hasBeenPrinted, fisik);

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
                                                    updatePrintStatus(noSanding);
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
                                    Toast.makeText(Sanding.this,
                                            "Error printing: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error";
                            Toast.makeText(Sanding.this,
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
    private void checkHasBeenPrinted(String noSanding, Sanding.HasBeenPrintedCallback callback) {
        new Thread(() -> {
            boolean hasBeenPrinted = false;
            Connection connection = null;
            try {
                // Mendapatkan koneksi dari method ConnectionClass
                connection = ConnectionClass();
                if (connection != null) {
                    String query = "SELECT HasBeenPrinted FROM Sanding_h WHERE NoSanding = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, noSanding);
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
    private void updatePrintStatus(String noSanding) {
        new Thread(() -> {
            Connection connection = null;
            try {
                // Mendapatkan koneksi dari method ConnectionClass
                connection = ConnectionClass();
                if (connection != null) {
                    String query = "UPDATE Sanding_h SET HasBeenPrinted = 1 WHERE NoSanding = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, noSanding);
                        int rowsAffected = stmt.executeUpdate();

                        if (rowsAffected > 0) {
                            runOnUiThread(() -> Toast.makeText(Sanding.this,
                                    "Status cetak berhasil diupdate",
                                    Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(Sanding.this,
                                    "Tidak ada data yang diupdate",
                                    Toast.LENGTH_SHORT).show());
                        }
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(Sanding.this,
                            "Koneksi database gagal",
                            Toast.LENGTH_SHORT).show());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("Database", "Error updating HasBeenPrinted status: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(Sanding.this,
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
        private String noSanding;

        public SearchAllDataTask(String noSanding) {
            this.noSanding = noSanding;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("SearchAllDataTask", "Searching for NoSanding: " + noSanding);
            Connection con = ConnectionClass();
            boolean isDataFound = false;

            if (con != null) {
                try {
                    String query = "SELECT h.DateCreate, h.Jam, " +
                            "d.Lebar, d.Panjang, d.Tebal, d.JmlhBatang, d.NoUrut, " +
                            "h.IsReject, h.IsLembur " +
                            "FROM dbo.Sanding_h AS h " +
                            "INNER JOIN dbo.Sanding_d AS d ON h.NoSanding = d.NoSanding " +
                            "WHERE h.NoSanding = ?";

                    Log.d("SearchAllDataTask", "Preparing statement: " + query);
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding);
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
                                NoSanding.setText(noSanding);
                                DateS.setText(dateCreate != null ? dateCreate : "");
                                TimeS.setText(jam != null ? jam : "");
                                CBAfkirS.setChecked(isReject);
                                CBLemburS.setChecked(isLembur);

                                m3();
                                jumlahpcs();
                            }
                        });

                        isDataFound = true;
                    } else {
                        Log.e("SearchAllDataTask", "No data found for NoSanding: " + noSanding);
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

    private void addDataDetail(String noSanding) {
        String tebal = DetailTebalS.getText().toString();
        String panjang = DetailPanjangS.getText().toString();
        String lebar = DetailLebarS.getText().toString();
        String pcs = DetailPcsS.getText().toString();

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
            DetailTebalS.setText("");
            DetailPanjangS.setText("");
            DetailLebarS.setText("");
            DetailPcsS.setText("");

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
        if (DetailTebalS != null) {
            DetailTebalS.setText("");
        }
        if (DetailLebarS != null) {
            DetailLebarS.setText("");
        }
        if (DetailPanjangS != null) {
            DetailPanjangS.setText("");
        }
        if (DetailPcsS != null) {
            DetailPcsS.setText("");
        }
    }

    private void saveDataDetailToDatabase(String noSanding, int noUrut, double tebal, double lebar, double panjang, int pcs) {
        new Sanding.SaveDataTaskDetail().execute(noSanding, String.valueOf(noUrut), String.valueOf(tebal), String.valueOf(lebar),
                String.valueOf(panjang), String.valueOf(pcs));
    }

    private class SaveDataTaskDetail extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String noSanding = params[0];
            String noUrut = params[1];
            String tebal = params[2];
            String lebar = params[3];
            String panjang = params[4];
            String pcs = params[5];

            try {
                Connection connection = ConnectionClass();
                if (connection != null) {
                    String query = "INSERT INTO dbo.Sanding_d (NoSanding, NoUrut, Tebal, Lebar, Panjang, JmlhBatang) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, noSanding);
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
        NoSanding.setText("");
        M3S.setText("");
        JumlahPcsS.setText("");
        NoMouldingAsal.setText("");
        CBAfkirS.setChecked(false);
        CBLemburS.setChecked(false);
        SpinKayuS.setSelection(0);
        SpinTellyS.setSelection(0);
        SpinSPKS.setSelection(0);
        SpinSPKAsalS.setSelection(0);
        SpinGradeS.setSelection(0);
        SpinProfileS.setSelection(0);
        SpinMesinS.setEnabled(false);
        SpinSusunS.setEnabled(false);
        RadioGroupS.clearCheck();
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
            TextView M3TextView = findViewById(R.id.M3S);
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

        JumlahPcsS.setText(String.valueOf(totalPcs));
    }


    private void setCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        DateS.setText(currentDate);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        TimeS.setText(currentTime);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Sanding.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                String selectedDate = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);
                DateS.setText(selectedDate);
                new LoadMesinTask().execute(selectedDate);
                new LoadSusunTask().execute(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        int hour = calendarS.get(Calendar.HOUR_OF_DAY);
        int minute = calendarS.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Sanding.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                calendarS.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendarS.set(Calendar.MINUTE, selectedMinute);

                SimpleDateFormat timeFormat = new SimpleDateFormat(" HH:mm:ss", Locale.getDefault());
                String updatedTime = timeFormat.format(calendarS.getTime());
                TimeS.setText(updatedTime);
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

    private Uri createPdf(String noSanding, String jenisKayu, String date, String time, String tellyBy, String mesinSusun, String noSPK, String noSPKasal, String grade, List<DataRow> temporaryDataListDetail, String jumlahPcs, String m3, boolean hasBeenPrinted, String fisik) throws IOException {
        // Validasi parameter wajib
        if (noSanding == null || noSanding.trim().isEmpty()) {
            throw new IOException("Nomor Sanding tidak boleh kosong");
        }

        if (temporaryDataListDetail == null || temporaryDataListDetail.isEmpty()) {
            throw new IOException("Data tidak boleh kosong");
        }

        // Validasi dan set default value untuk parameter opsional
        noSanding = (noSanding != null) ? noSanding.trim() : "-";
        jenisKayu = (jenisKayu != null) ? jenisKayu.trim() : "-";
        date = (date != null) ? date.trim() : "-";
        time = (time != null) ? time.trim() : "-";
        grade = (grade != null) ? grade.trim() : "-";
        tellyBy = (tellyBy != null) ? tellyBy.trim() : "-";
        noSPK = (noSPK != null) ? noSPK.trim() : "-";
        jumlahPcs = (jumlahPcs != null) ? jumlahPcs.trim() : "-";
        m3 = (m3 != null) ? m3.trim() : "-";

        Uri pdfUri = null;
        ContentResolver resolver = getContentResolver();
        String fileName = "S4S_" + noSanding + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".pdf";
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
                float baseHeight = 575; // Tinggi dasar untuk elemen non-tabel (header, footer, margin, dll.)
                float rowHeight = 34; // Tinggi rata-rata per baris data
                float totalHeight = baseHeight + (rowHeight * temporaryDataListDetail.size());

                // Tetapkan ukuran halaman dinamis
                Rectangle pageSize = new Rectangle( PageSize.A6.getWidth(), totalHeight);
                pdfDocument.setDefaultPageSize(new PageSize(pageSize));

                Document document = new Document(pdfDocument);
                document.setMargins(0, 5, 0, 5);

                // Header
                Paragraph judul = new Paragraph("LABEL CROSS CUT")
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
                addInfoRow(leftColumn, "No Sanding", noSanding, timesNewRoman);
                addInfoRow(leftColumn, "Jenis", jenisKayu, timesNewRoman);
                addInfoRow(leftColumn, "Grade", grade, timesNewRoman);
                addInfoRow(leftColumn, "Fisik", fisik, timesNewRoman);

                // Buat tabel untuk kolom kanan
                Table rightColumn = new Table(infoColumnWidths)
                        .setWidth(pageWidth/2 - 5)
                        .setMarginLeft(20)
                        .setBorder(Border.NO_BORDER);

                // Isi kolom kanan
                addInfoRow(rightColumn, "Tanggal", date + " (" + time + ")", timesNewRoman);
                addInfoRow(rightColumn, "Telly", tellyBy, timesNewRoman);
                addInfoRow(rightColumn, "Mesin", mesinSusun, timesNewRoman);
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

                Paragraph qrCodeID = new Paragraph(noSanding).setTextAlignment(TextAlignment.CENTER).setFontSize(8).setMargins(-5, 0, 0, 0).setFont(timesNewRoman);
                Paragraph qrCodeIDbottom = new Paragraph(noSanding).setTextAlignment(TextAlignment.RIGHT).setFontSize(8).setMargins(-5, 20, 0, 0).setFont(timesNewRoman);

                BarcodeQRCode qrCode = new BarcodeQRCode(noSanding);
                PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
                Image qrCodeImage = new Image(qrCodeObject).setWidth(75).setHorizontalAlignment(HorizontalAlignment.CENTER).setMargins(-5, 0, 0, 0);

                BarcodeQRCode qrCodeBottom = new BarcodeQRCode(noSanding);
                PdfFormXObject qrCodeBottomObject = qrCodeBottom.createFormXObject(ColorConstants.BLACK, pdfDocument);
                Image qrCodeBottomImage = new Image(qrCodeBottomObject).setWidth(75).setHorizontalAlignment(HorizontalAlignment.RIGHT).setMargins(-5, 0, 0, 0);

                Paragraph bottomLine = new Paragraph("-----------------------------------------------------------------------------------------------------").setTextAlignment(TextAlignment.CENTER).setFontSize(8).setMargins(0, 0, 0, 15).setFont(timesNewRoman);
                Paragraph outputText = new Paragraph("Output").setTextAlignment(TextAlignment.CENTER).setFontSize(8).setMargins(15, 0, 0, 0).setFont(timesNewRoman);
                Paragraph inputText = new Paragraph("Input").setTextAlignment(TextAlignment.RIGHT).setFontSize(8).setMargins(15, 28, 0, 0).setFont(timesNewRoman);

                // Tambahkan semua elemen ke dokumen


                document.add(judul);
                Log.d("DEBUG_TAG", "Value of hasBeenPrinted: " + hasBeenPrinted);
                if (hasBeenPrinted) {
                    addTextDitheringWatermark(pdfDocument, timesNewRoman);
                }
                document.add(mainTable);
                document.add(table);
                document.add(sumTable);
                document.add(outputText);
                document.add(qrCodeImage);
                document.add(qrCodeID);
                document.add(bottomLine);
                document.add(mainTable);
                document.add(table);
                document.add(sumTable);
                document.add(inputText);
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
        private String noSanding;

        public SaveBongkarSusunTask(String noBongkarSusun, String noSanding) {
            this.noBongkarSusun = noBongkarSusun;
            this.noSanding = noSanding;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.BongkarSusunOutputSanding (NoSanding, NoBongkarSusun) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding);
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
                Toast.makeText(Sanding.this, "Data berhasil disimpan.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SaveToDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private String noProduksi, noSanding;

        public SaveToDatabaseTask(String noProduksi, String noSanding) {
            this.noProduksi = noProduksi;
            this.noSanding = noSanding;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.SandingProduksiOutput (NoProduksi, NoSanding) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noProduksi);
                    ps.setString(2, noSanding);

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
                Toast.makeText(Sanding.this, "Data berhasil disimpan ke database.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Sanding.this, "Gagal menyimpan data ke database.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class InsertDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private String lebar, panjang, tebal, pcs, noUrut, noSanding;

        public InsertDatabaseTask(String lebar, String panjang, String tebal, String pcs, String noUrut, String noSanding) {
            this.lebar = lebar;
            this.panjang = panjang;
            this.tebal = tebal;
            this.pcs = pcs;
            this.noUrut = noUrut;
            this.noSanding = noSanding;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "INSERT INTO dbo.Sanding_d (Lebar, Panjang, Tebal, JmlhBatang, NoUrut, NoSanding) VALUES (?, ?, ?, ?, ?, ?)";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, lebar);
                    ps.setString(2, panjang);
                    ps.setString(3, tebal);
                    ps.setString(4, pcs);
                    ps.setString(5, noUrut);
                    ps.setString(6, noSanding);

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
                Toast.makeText(Sanding.this, "Data berhasil disimpan ke database.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Sanding.this, "Gagal menyimpan data ke database.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class DeleteDataTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String noSanding = params[0];
            Connection con = ConnectionClass();
            boolean success = false;

            if (con != null) {
                try {
                    String query = "DELETE FROM dbo.Sanding_h WHERE NoSanding = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding);

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
                Toast.makeText(Sanding.this, "Data berhasil dihapus.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Sanding.this, "Gagal menghapus data.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private String noSanding, dateCreate, time, idTelly, noSPK, noSPKasal, idGrade, idJenisKayu, idFJProfile;
        private int isReject, isLembur, IdUOMTblLebar, IdUOMPanjang;

        public UpdateDatabaseTask(String noSanding, String dateCreate, String time, String idTelly, String noSPK, String noSPKasal,
                                  String idGrade, String idJenisKayu, String idFJProfile,
                                  int isReject, int isLembur, int IdUOMTblLebar, int IdUOMPanjang) {
            this.noSanding = noSanding;
            this.dateCreate = dateCreate;
            this.time = time;
            this.idTelly = idTelly;
            this.noSPKasal = noSPKasal;
            this.idGrade = idGrade;
            this.idJenisKayu = idJenisKayu;
            this.idFJProfile = idFJProfile;
            this.isReject = isReject;
            this.isLembur = isLembur;
            this.IdUOMTblLebar = IdUOMTblLebar;
            this.IdUOMPanjang = IdUOMPanjang;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    String query = "UPDATE dbo.Sanding_h SET DateCreate = ?, Jam = ?, IdOrgTelly = ?, NoSPK = ?, NoSPKAsal = ?, IdGrade = ?, " +
                            "IdFJProfile = ?, IdFisik = 9, IdJenisKayu = ?, IdWarehouse = 9, IsReject = ?, IsLembur = ?, IdUOMTblLebar = ?, IdUOMPanjang = ? WHERE NoSanding = ?";

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
                    ps.setInt(11, IdUOMTblLebar);
                    ps.setInt(12, IdUOMPanjang);
                    ps.setString(13, noSanding);

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

    private class SetAndSaveNoSandingTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Connection con = ConnectionClass();
            String newNoSanding = null;
            if (con != null) {
                try {
                    String query = "SELECT MAX(NoSanding) FROM dbo.Sanding_h";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        String lastNoSanding = rs.getString(1);

                        if (lastNoSanding != null && lastNoSanding.startsWith("W.")) {
                            String numericPart = lastNoSanding.substring(2);
                            int numericValue = Integer.parseInt(numericPart);
                            int newNumericValue = numericValue + 1;

                            newNoSanding = "W." + String.format("%06d", newNumericValue);
                        }
                    }

                    rs.close();
                    ps.close();

                    if (newNoSanding != null) {
                        String insertQuery = "INSERT INTO dbo.Sanding_h (NoSanding) VALUES (?)";
                        PreparedStatement insertPs = con.prepareStatement(insertQuery);
                        insertPs.setString(1, newNoSanding);
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
            return newNoSanding;
        }

        @Override
        protected void onPostExecute(String newNoSanding) {
            if (newNoSanding != null) {
                NoSanding.setText(newNoSanding);
                Toast.makeText(Sanding.this, "NoSanding berhasil diatur dan disimpan.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Error", "Failed to set or save NoSanding.");
                Toast.makeText(Sanding.this, "Gagal mengatur atau menyimpan NoSanding.", Toast.LENGTH_SHORT).show();
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

            ArrayAdapter<JenisKayu> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, jenisKayuList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinKayuS.setAdapter(adapter);
            SpinKayuS.setSelection(0);
        }
    }
    public class LoadJenisKayuTask2 extends AsyncTask<String, Void, List<JenisKayu>> {
        private String noSanding;

        // Constructor to accept NoSanding
        public LoadJenisKayuTask2(String noSanding) {
            this.noSanding = noSanding;
        }

        @Override
        protected List<JenisKayu> doInBackground(String... params) {
            List<JenisKayu> jenisKayuList = new ArrayList<>();
            Connection con = ConnectionClass(); // Ensure this method establishes a database connection

            if (con != null) {
                try {
                    // Update query to fetch jenis kayu based on noSanding
                    String query = "SELECT j.IdJenisKayu, j.Jenis " +
                            "FROM dbo.MstJenisKayu AS j " +
                            "INNER JOIN dbo.Sanding_h AS h ON h.IdJenisKayu = j.IdJenisKayu " +
                            "WHERE h.NoSanding = ? AND j.enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding); // Set the noSanding parameter

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
                ArrayAdapter<JenisKayu> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, jenisKayuList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinKayuS.setAdapter(adapter);
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
            ArrayAdapter<Telly> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, tellyList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Set adapter ke spinner
            SpinTellyS.setAdapter(adapter);

            // Atur spinner untuk menampilkan elemen pertama ("Pilih") secara default
            SpinTellyS.setSelection(0);
        }
    }


    private class LoadTellyTask2 extends AsyncTask<String, Void, List<Telly>> {
        private String noSanding;

        public LoadTellyTask2(String noSanding) {
            this.noSanding = noSanding;
        }

        @Override
        protected List<Telly> doInBackground(String... params) {
            List<Telly> tellyList = new ArrayList<>();
            Connection con = ConnectionClass(); // Ensure this method establishes a database connection

            if (con != null) {
                try {
                    // Update query to fetch telly data based on noSanding
                    String query = "SELECT t.IdOrgTelly, t.NamaOrgTelly " +
                            "FROM dbo.MstOrgTelly AS t " +
                            "INNER JOIN dbo.Sanding_h AS h ON h.IdOrgTelly = t.IdOrgTelly " +
                            "WHERE h.NoSanding = ? AND t.enable = 1";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding); // Set the noSanding parameter

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
                ArrayAdapter<Telly> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, tellyList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinTellyS.setAdapter(adapter);
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

            ArrayAdapter<SPK> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, spkList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinSPKS.setAdapter(adapter);
            SpinSPKS.setSelection(0);
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

            ArrayAdapter<SPKAsal> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, spkAsalList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinSPKAsalS.setAdapter(adapter);
            SpinSPKAsalS.setSelection(0);
        }
    }

    private class LoadSPKTask2 extends AsyncTask<Void, Void, List<SPK>> {
        private String noSanding;

        // Constructor to accept NoSanding
        public LoadSPKTask2(String noSanding) {
            this.noSanding = noSanding;
        }

        @Override
        protected List<SPK> doInBackground(Void... params) {
            List<SPK> spkList = new ArrayList<>();
            Connection con = ConnectionClass(); // Ensure this method establishes a database connection

            if (con != null) {
                try {
                    String query = "SELECT NoSPK FROM dbo.Sanding_h WHERE NoSanding = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding); // Set the noSanding parameter

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String noSPK = rs.getString("NoSPK");

                        SPK spk = new SPK(noSPK); // Assuming SPK class has a constructor that accepts noSPK
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
                ArrayAdapter<SPK> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, spkList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSPKS.setAdapter(adapter);

                SpinSPKS.setEnabled(true);
            } else {
                Log.e("Error", "No SPK data found for the provided NoSanding.");
                SpinSPKS.setAdapter(null);
                SpinSPKS.setEnabled(false);
                SpinSPKS.setEnabled(false);
                Toast.makeText(Sanding.this, "Tidak ada data SPK yang ditemukan.", Toast.LENGTH_SHORT).show();
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
                    String query = "SELECT Profile, IdFJProfile FROM dbo.MstFJProfile WHERE IdFJProfile != 0";
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

            ArrayAdapter<Profile> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, profileList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinProfileS.setAdapter(adapter);
            SpinProfileS.setSelection(0);
        }
    }

    private class LoadProfileTask2 extends AsyncTask<String, Void, List<Profile>> {
        private String noSanding;

        public LoadProfileTask2(String noSanding) {
            this.noSanding = noSanding;
        }

        @Override
        protected List<Profile> doInBackground(String... voids) {
            List<Profile> profileList = new ArrayList<>();
            Connection con = ConnectionClass(); // Assumes this method exists to establish a DB connection

            if (con != null) {
                try {
                    String query = "SELECT p.Profile, p.IdFJProfile " +
                            "FROM dbo.MstFJProfile AS p " +
                            "INNER JOIN dbo.Sanding_h AS h ON h.IdFJProfile = p.IdFJProfile " +
                            "WHERE h.NoSanding = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding);

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
                ArrayAdapter<Profile> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, profileList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinProfileS.setAdapter(adapter);
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
                    String query = "SELECT NamaWarehouse FROM dbo.MstWarehouse WHERE IdWarehouse = 9";
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
                ArrayAdapter<Fisik> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, fisikList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinFisikS.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load fisik data.");
            }
        }
    }

    private class LoadFisikTask2 extends AsyncTask<String, Void, List<Fisik>> {
        private String noSanding;

        // Constructor menerima noSanding, bukan idWarehouse
        public LoadFisikTask2(String noSanding) {
            this.noSanding= noSanding;
        }

        @Override
        protected List<Fisik> doInBackground(String... params) {
            List<Fisik> fisikList = new ArrayList<>();
            Connection con = ConnectionClass();
            if (con != null) {
                try {
                    // Query untuk mencocokkan IdWarehouse berdasarkan noSanding
                    String query = "SELECT mw.NamaWarehouse " +
                            "FROM dbo.MstWarehouse mw " +
                            "INNER JOIN dbo.Sanding_h Sanding ON mw.IdWarehouse = Sanding.IdWarehouse " +
                            "WHERE Sanding.noSanding = ?";

                    // Menyiapkan query dengan parameter noSanding
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding);  // Menggunakan noSanding untuk query

                    // Menjalankan query dan mendapatkan hasilnya
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
                ArrayAdapter<Fisik> adapter = new ArrayAdapter<>(Sanding.this,
                        android.R.layout.simple_spinner_item, fisikList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinFisikS.setAdapter(adapter);
            } else {
                Log.e("Error", "No warehouse found.");
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(Sanding.this,
                        android.R.layout.simple_spinner_item, new String[]{"Tidak ada Fisik"});
                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinFisikS.setAdapter(emptyAdapter);
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

                    String category = "Sanding";

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
                if (!gradeList.isEmpty()) {
                    Grade dummyGrade = new Grade("", "PILIH");
                    gradeList.add(0, dummyGrade);

                } else {
                    Log.e("Error", "Tidak ada grade");
                    gradeList = new ArrayList<>();
                    gradeList.add(new Grade(null, "GRADE TIDAK TERSEDIA"));
                }

                ArrayAdapter<Grade> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, gradeList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeS.setAdapter(adapter);
                SpinGradeS.setSelection(0);
            }
        }
    }

    private class LoadGradeTask2 extends AsyncTask<String, Void, List<Grade>> {
        private String noSanding;

        public LoadGradeTask2(String noSanding) {
            this.noSanding = noSanding;
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

                    String category = "Sanding";

                    String query = "SELECT DISTINCT a.IdGrade, a.NamaGrade " +
                            "FROM MstGrade a " +
                            "INNER JOIN MstGrade_d b ON a.IdGrade = b.IdGrade " +
                            "WHERE a.Enable = 1 AND b.IdJenisKayu = ? AND b.Category = ? AND b.NoSanding = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setInt(1, idJenisKayu);
                    ps.setString(2, category);
                    ps.setString(3, noSanding);
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
                ArrayAdapter<Grade> adapter = new ArrayAdapter<>(Sanding.this,
                        android.R.layout.simple_spinner_item, gradeList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeS.setAdapter(adapter);
            } else {
                Log.e("Error", "Tidak ada grade");
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(Sanding.this,
                        android.R.layout.simple_spinner_item, new String[]{"Tidak ada grade"});
                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinGradeS.setAdapter(emptyAdapter);
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

                    String query = "SELECT a.IdMesin, b.NamaMesin, a.NoProduksi FROM dbo.SandingProduksi_h a " +
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
                ArrayAdapter<Mesin> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinMesinS.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load mesin data.");
            }
        }
    }

    private class LoadMesinTask2 extends AsyncTask<Void, Void, List<Mesin>> {
        private String noSanding;

        public LoadMesinTask2(String noSanding) {
            this.noSanding = noSanding;
        }

        @Override
        protected List<Mesin> doInBackground(Void... params) {
            List<Mesin> mesinList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT b.NoProduksi, d.NamaMesin FROM Sanding_h a " +
                            "INNER JOIN SandingProduksiOutput b ON b.NoSanding = a.NoSanding " +
                            "INNER JOIN SandingProduksi_h c ON c.NoProduksi = b.NoProduksi " +
                            "INNER JOIN MstMesin d ON d.IdMesin = c.IdMesin " +
                            "WHERE a.NoSanding = ?";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding);

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
                ArrayAdapter<Mesin> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinMesinS.setAdapter(adapter);

                radioButtonMesinS.setEnabled(true);
                radioButtonBSusunS.setEnabled(false);
            } else {
                Log.e("Error", "Failed to load mesin data.");
                radioButtonMesinS.setEnabled(false);
                radioButtonBSusunS.setEnabled(false);

                Toast.makeText(Sanding.this, "Tidak ada data mesin yang ditemukan.", Toast.LENGTH_SHORT).show();
                SpinMesinS.setAdapter(null);
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
                ArrayAdapter<Susun> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, susunList);
                SpinSusunS.setAdapter(adapter);
            } else {
                Log.e("Error", "Failed to load susun data");
            }
        }
    }
    private class LoadSusunTask2 extends AsyncTask<Void, Void, List<Susun>> {
        private String noSanding;

        public LoadSusunTask2(String noSanding) {
            this.noSanding = noSanding;
        }

        @Override
        protected List<Susun> doInBackground(Void... params) {
            List<Susun> susunList = new ArrayList<>();
            Connection con = ConnectionClass();

            if (con != null) {
                try {
                    String query = "SELECT NoBongkarSusun FROM dbo.BongkarSusunOutputSanding WHERE NoSanding = ?"; // Filter by noSandingSanding
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, noSanding);
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
                ArrayAdapter<Susun> adapter = new ArrayAdapter<>(Sanding.this, android.R.layout.simple_spinner_item, susunList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinSusunS.setAdapter(adapter);

                radioButtonMesinS.setEnabled(false);
                radioButtonBSusunS.setEnabled(true);
            } else {
                Log.e("Error", "Failed to load susun data.");
                radioButtonMesinS.setEnabled(false);
                radioButtonBSusunS.setEnabled(false);

                Toast.makeText(Sanding.this, "Tidak ada data susun yang ditemukan.", Toast.LENGTH_SHORT).show();
                SpinSusunS.setAdapter(null);
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