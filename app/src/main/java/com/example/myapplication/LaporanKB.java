package com.example.myapplication;

import static com.example.myapplication.config.ApiEndpoints.CRYSTAL_REPORT_WPS_EXPORT_PDF;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myapplication.utils.DateRangeDialogHelper;
import com.example.myapplication.utils.LoadingDialogHelper;
import com.example.myapplication.utils.PdfUtils;
import com.example.myapplication.utils.SharedPrefUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LaporanKB extends AppCompatActivity {

    private static final String TAG = "LaporanKB";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();

    private CardView laporan_mutasi_racip_detail;
    private CardView laporan_kayu_bulat_hidup;
    private CardView laporan_mutasi_racip;
    private CardView laporan_mutasi_kayu_bulat_gantung;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_kb);

        laporan_mutasi_racip_detail = findViewById(R.id.laporan_mutasi_racip_detail);
        laporan_kayu_bulat_hidup = findViewById(R.id.laporan_kayu_bulat_hidup);
        laporan_mutasi_racip = findViewById(R.id.laporan_mutasi_racip);
        laporan_mutasi_kayu_bulat_gantung = findViewById(R.id.laporan_mutasi_kayu_bulat_gantung);

        username = SharedPrefUtils.getUsername(this);

        laporan_mutasi_racip_detail.setOnClickListener(view -> showLaporanMutasiRacipDetail());
        laporan_kayu_bulat_hidup.setOnClickListener(view -> showLaporanKayuBulatHidup());
        laporan_mutasi_racip.setOnClickListener(view -> showLaporanMutasiRacip());
        laporan_mutasi_kayu_bulat_gantung.setOnClickListener(view -> showLaporanMutasiKayuBulatGantung());

    }

    private void showLaporanMutasiRacipDetail() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrLapMutasiHasilRacipDetail";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            Log.d(TAG, "Mulai download dari URL: " + url);

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Mutasi Hasil Racip Detail.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanKayuBulatHidup() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.HARI_INI, (tglAwal, tglAkhir) -> {
            String reportName = "CrlapKayuBulatHidup";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&PerTgl=" + tglAkhir
                    + "&Username=" + username;

            Log.d(TAG, "Mulai download laporan kayu bulat hidup dari URL: " + url);

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Kayu Bulat Hidup.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanMutasiRacip() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrMutasiRacip";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            Log.d(TAG, "Mulai download laporan mutasi racip dari URL: " + url);

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Mutasi Racip.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }



    private void showLaporanMutasiKayuBulatGantung() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrMutasiKayuBulat";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username
                    + "&TxtJudul=Laporan%20Mutasi%20Kayu%20Bulat%20Gantung";

            Log.d(TAG, "Mulai download laporan kayu bulat hidup dari URL: " + url);

            loadingDialogHelper.show(LaporanKB.this);

            PdfUtils.downloadAndOpenPDF(
                    LaporanKB.this,
                    url,
                    "Mutasi Kayu Bulat Gantung.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }




//    private void downloadAndOpenPDF(String urlString, String fileName) {
//        executorService.execute(() -> {
//            try {
//                URL url = new URL(urlString);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//
//                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                    Log.e(TAG, "Server returned HTTP " + connection.getResponseCode()
//                            + " " + connection.getResponseMessage());
//                    loadingDialogHelper.hide();
//                    return;
//                }
//
//                InputStream input = connection.getInputStream();
//                File pdfFile = new File(getCacheDir(), fileName);
//                FileOutputStream output = new FileOutputStream(pdfFile);
//
//                byte[] buffer = new byte[4096];
//                int bytesRead;
//                while ((bytesRead = input.read(buffer)) != -1) {
//                    output.write(buffer, 0, bytesRead);
//                }
//
//                output.close();
//                input.close();
//
//                runOnUiThread(() -> {
//                    try {
//                        Uri pdfUri = FileProvider.getUriForFile(
//                                LaporanKB.this,
//                                getPackageName() + ".provider",
//                                pdfFile
//                        );
//
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setDataAndType(pdfUri, "application/pdf");
//                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NO_HISTORY);
//
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        Log.e(TAG, "Gagal membuka PDF", e);
//                    } finally {
//                        loadingDialogHelper.hide();
//                    }
//                });
//
//            } catch (Exception e) {
//                Log.e(TAG, "Download error", e);
//                loadingDialogHelper.hide();
//            }
//        });
//    }


}
