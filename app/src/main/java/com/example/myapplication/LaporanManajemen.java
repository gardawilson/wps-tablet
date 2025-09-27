package com.example.myapplication;

import static android.content.ContentValues.TAG;
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

public class LaporanManajemen extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();

    private CardView laporan_rekap_stock_on_hand;
    private CardView laporan_rekap_mutasi;
    private CardView laporan_flow_produksi;
    private CardView laporan_produksi_semua_mesin;
    private CardView laporan_label_perhari;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_manajemen);

        laporan_rekap_stock_on_hand = findViewById(R.id.laporan_rekap_stock_on_hand);
        laporan_rekap_mutasi = findViewById(R.id.laporan_rekap_mutasi);
        laporan_flow_produksi = findViewById(R.id.laporan_flow_produksi);
        laporan_produksi_semua_mesin = findViewById(R.id.laporan_produksi_semua_mesin);
        laporan_label_perhari = findViewById(R.id.laporan_label_perhari);

        username = SharedPrefUtils.getUsername(this);


        laporan_rekap_stock_on_hand.setOnClickListener(view -> showLaporanStockOnHand());
        laporan_rekap_mutasi.setOnClickListener(view -> showLaporanRekapMutasi());
        laporan_flow_produksi.setOnClickListener(view -> showLaporanFlowProduksi());
        laporan_produksi_semua_mesin.setOnClickListener(view -> showLaporanProduksiSemuaMesin());
        laporan_label_perhari.setOnClickListener(view -> showLaporanLabelPerHari());
    }

    private void showLaporanStockOnHand() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrStockOnHand";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Stock on Hand (" + tglAwal + " sampai " + tglAkhir + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanRekapMutasi() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrRekapMutasi";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Rekap Mutasi (" + tglAwal + " sampai " + tglAkhir + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanFlowProduksi() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrFlowProduksiPerPeriode";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Flow Produksi Per Periode (" + tglAwal + " sampai " + tglAkhir + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanProduksiSemuaMesin() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrLapRekapProsuksiSemuaMesin";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Produksi Semua Mesin (" + tglAwal + " sampai " + tglAkhir + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanLabelPerHari() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrLapLabelPerhari";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Label Per Hari (" + tglAwal + " sampai " + tglAkhir + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


}