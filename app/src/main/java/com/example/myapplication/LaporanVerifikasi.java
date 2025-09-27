package com.example.myapplication;

import static android.content.ContentValues.TAG;
import static com.example.myapplication.config.ApiEndpoints.CRYSTAL_REPORT_WPS_EXPORT_PDF;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myapplication.utils.DateDialogHelper;
import com.example.myapplication.utils.DateRangeDialogHelper;
import com.example.myapplication.utils.LoadingDialogHelper;
import com.example.myapplication.utils.PdfUtils;
import com.example.myapplication.utils.SharedPrefUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LaporanVerifikasi extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();

    private CardView laporan_rangkuman_jumlah_label_input;
    private CardView laporan_bahan_terpakai;
    private CardView laporan_rangkuman_bongkar_susun;
    private CardView laporan_bahan_yang_dihasilkan;
    private CardView laporan_label_nyangkut;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_verifikasi);

        laporan_rangkuman_jumlah_label_input = findViewById(R.id.laporan_rangkuman_jumlah_label_input);
        laporan_bahan_terpakai = findViewById(R.id.laporan_bahan_terpakai);
        laporan_rangkuman_bongkar_susun = findViewById(R.id.laporan_rangkuman_bongkar_susun);
        laporan_bahan_yang_dihasilkan = findViewById(R.id.laporan_bahan_yang_dihasilkan);
        laporan_label_nyangkut = findViewById(R.id.laporan_label_nyangkut);

        username = SharedPrefUtils.getUsername(this);

        laporan_rangkuman_jumlah_label_input.setOnClickListener(view -> showLaporanRangkumanJumlahLabelInput());

        laporan_bahan_terpakai.setOnClickListener(view -> showLaporanBahanTerpakai());

        laporan_rangkuman_bongkar_susun.setOnClickListener(view -> showLaporanRangkumanBongkarSusun());

        laporan_bahan_yang_dihasilkan.setOnClickListener(view -> showLaporanBahanYgDihasilkan());

        laporan_label_nyangkut.setOnClickListener(view -> showLaporanLabelNyangkut());
    }

    private void showLaporanRangkumanJumlahLabelInput() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrLapProduksiSemua";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Rangkuman Jumlah Label Input (" + tglAwal + " sampai " + tglAkhir + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanBahanTerpakai() {
        DateDialogHelper.show(this, DateDialogHelper.DefaultTanggalMode.HARI_INI, tanggal -> {
            String reportName = "CrLapBahanTerpakai";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tanggal
                    + "&Username=" + username;

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Bahan Terpakai (" + tanggal + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }

    private void showLaporanRangkumanBongkarSusun() {
        DateDialogHelper.show(this, DateDialogHelper.DefaultTanggalMode.HARI_INI, tanggal -> {
            String reportName = "CrLapRangkumanBongkarSusun";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tanggal
                    + "&Username=" + username;

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Rangkuman Bongkar Susun (" + tanggal + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanBahanYgDihasilkan() {
        DateDialogHelper.show(this, DateDialogHelper.DefaultTanggalMode.HARI_INI, tanggal -> {
            String reportName = "CrLapBahanDihasilkan";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tanggal
                    + "&Username=" + username;

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Bahan Yang Dihasilkan (" + tanggal + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanLabelNyangkut() {
        String reportName = "CrLapNyangkut";

        String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                + "?reportName=" + reportName
                + "&Username=" + username;

        loadingDialogHelper.show(this);

        PdfUtils.downloadAndOpenPDF(
                this,
                url,
                "Laporan Nyangkut.pdf",
                executorService,
                loadingDialogHelper
        );
    }



}