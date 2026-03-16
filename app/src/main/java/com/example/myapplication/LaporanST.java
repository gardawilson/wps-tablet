package com.example.myapplication;

import static com.example.myapplication.config.ApiEndpoints.BASE_REPORT_MICROSERVICE;
import static com.example.myapplication.config.ApiEndpoints.CRYSTAL_REPORT_WPS_EXPORT_PDF;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myapplication.utils.DateDialogHelper;
import com.example.myapplication.utils.DateRangeDialogHelper;
import com.example.myapplication.utils.LoadingDialogHelper;
import com.example.myapplication.utils.PdfMicroserviceUtils;
import com.example.myapplication.utils.PdfUtils;
import com.example.myapplication.utils.SharedPrefUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LaporanST extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();

    private CardView laporan_rekap_hasil_sawmill_meja;
    private CardView laporan_mutasi_st;
    private CardView laporan_rekap_penerimaan_st_dari_sawmill;
    private CardView laporan_sawmill_perhari_perlebar_pertebal;
    private CardView laporan_stock_st_basah;
    private CardView laporan_rekap_hasil_sawmill_semua_meja;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_st);

        laporan_rekap_hasil_sawmill_meja = findViewById(R.id.laporan_rekap_hasil_sawmill_meja);
        laporan_mutasi_st = findViewById(R.id.laporan_mutasi_st);
        laporan_rekap_penerimaan_st_dari_sawmill = findViewById(R.id.laporan_rekap_penerimaan_st_dari_sawmill);
        laporan_sawmill_perhari_perlebar_pertebal = findViewById(R.id.laporan_sawmill_perhari_perlebar_pertebal);
        laporan_stock_st_basah = findViewById(R.id.laporan_stock_st_basah);
        laporan_rekap_hasil_sawmill_semua_meja = findViewById(R.id.laporan_rekap_hasil_sawmill_semua_meja);

        username = SharedPrefUtils.getUsername(this);

        laporan_rekap_hasil_sawmill_meja.setOnClickListener(view -> showLaporanRekapHasilSawmillMeja());
        laporan_rekap_penerimaan_st_dari_sawmill.setOnClickListener(view -> showLaporanRekapPenerimaanStDariSawmill());
        laporan_mutasi_st.setOnClickListener(view -> showLaporanMutasiST());
        laporan_sawmill_perhari_perlebar_pertebal.setOnClickListener(view -> showLaporanSawmillPerhariPerlebarPertebal());
        laporan_stock_st_basah.setOnClickListener(view -> showLaporanStockStBasah());
        laporan_rekap_hasil_sawmill_semua_meja.setOnClickListener(view -> showLaporanRekapHasilSawmillSemuaMeja());
    }

    private void showLaporanRekapHasilSawmillMeja() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.MINGGU_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrRekapHasilSawmillPerMejaUpahBorongan";

            // Pastikan tanggal diformat yyyy-MM-dd agar sesuai dengan query string
            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username
                    + "&reportName=" + reportName;

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Rekap Hasil Sawmill Per Meja.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }

    private void showLaporanRekapPenerimaanStDariSawmill() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.MINGGU_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrRekapPenSTDariSawmill";

            // Pastikan tanggal diformat yyyy-MM-dd agar sesuai dengan query string
            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username
                    + "&reportName=" + reportName;

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Rekap Penerimaan ST dari Sawmill.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }

    private void showLaporanMutasiST() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrMutasiSawnTimber";

            // Pastikan tanggal diformat yyyy-MM-dd agar sesuai dengan query string
            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username
                    + "&reportName=" + reportName;

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Mutasi Sawn Timber.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }

    private void showLaporanSawmillPerhariPerlebarPertebal() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrSTSawmillPerHariPerTebalPerLebar";

            // Format URL endpoint baru
            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username
                    + "&reportName=" + reportName;

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Laporan ST Per Hari Per Tebal Per Lebar.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }

    private void showLaporanStockStBasah() {
        DateDialogHelper.show(this,
                DateDialogHelper.DefaultTanggalMode.HARI_INI,
                (tanggal) -> {

                    String url = BASE_REPORT_MICROSERVICE
                            + "api/reports/sawn-timber/stock-st-basah/pdf"
                            + "?end_date=" + tanggal;

                    PdfMicroserviceUtils.downloadAndOpenPDFWithToken(
                            this,
                            url,
                            "Stock ST Basah.pdf",
                            executorService,
                            loadingDialogHelper
                    );
                });
    }

    private void showLaporanRekapHasilSawmillSemuaMeja() {
        DateRangeDialogHelper.show(
                this,
                DateRangeDialogHelper.DefaultTanggalMode.MINGGU_LALU,
                (tglAwal, tglAkhir) -> {

                    String url = BASE_REPORT_MICROSERVICE
                            + "api/reports/sawn-timber/rekap-hasil-sawmill-per-meja-upah-borongan-v2/pdf"
                            + "?start_date=" + tglAwal
                            + "&end_date=" + tglAkhir;

                    PdfMicroserviceUtils.downloadAndOpenPDFWithToken(
                            this,
                            url,
                            "Rekap Hasil Sawmill Semua Meja.pdf",
                            executorService,
                            loadingDialogHelper
                    );
                }
        );
    }

}