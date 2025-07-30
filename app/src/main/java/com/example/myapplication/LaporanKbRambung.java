package com.example.myapplication;

import static com.example.myapplication.config.ApiEndpoints.CRYSTAL_REPORT_WPS_EXPORT_PDF;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myapplication.utils.DateRangeDialogHelper;
import com.example.myapplication.utils.DateRangeNumberDialogHelper;
import com.example.myapplication.utils.LoadingDialogHelper;
import com.example.myapplication.utils.PdfUtils;
import com.example.myapplication.utils.SharedPrefUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LaporanKbRambung extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();

    private CardView laporan_rekap_penerimaan_st_dari_sawmill;
    private CardView laporan_mutasi_kb_gantung_rambung;
    private CardView laporan_umur_kayu_bulat_rambung;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_kb_rambung);

        laporan_rekap_penerimaan_st_dari_sawmill = findViewById(R.id.laporan_rekap_penerimaan_st_dari_sawmill);
        laporan_mutasi_kb_gantung_rambung = findViewById(R.id.laporan_mutasi_kb_gantung_rambung);
        laporan_umur_kayu_bulat_rambung = findViewById(R.id.laporan_umur_kayu_bulat_rambung);

        username = SharedPrefUtils.getUsername(this);

        laporan_rekap_penerimaan_st_dari_sawmill.setOnClickListener(view -> showLaporanRekapPenerimaanStDariSawmill());
        laporan_mutasi_kb_gantung_rambung.setOnClickListener(view -> showLaporanMutasiKbGantungRambung());
        laporan_umur_kayu_bulat_rambung.setOnClickListener(view -> showLaporanUmurKayuBulatRambung());
    }

    private void showLaporanRekapPenerimaanStDariSawmill() {
        DateRangeNumberDialogHelper.show(this, (tglAwal, tglAkhir, angka) -> {
            String reportName = "CrRekapPenSTDariSawmillKGRP";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username
                    + "&Upah=" + angka;

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


    private void showLaporanMutasiKbGantungRambung() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "crmutasikayubulatkg";
            String judul = "Laporan Mutasi Kayu Bulat (Gantung) - Timbang KG";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username
                    + "&TxtJudul=" + Uri.encode(judul); // penting: encode judul untuk URL

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Mutasi Kayu Bulat Gantung.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }

    private void showLaporanUmurKayuBulatRambung() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrUmurKayuBulatRambung";
            String type = "Rambung";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username
                    + "&Type=" + Uri.encode(type); // penting: encode judul untuk URL

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Umur Kayu Bulat Rambung.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


}