package com.example.myapplication;

import static com.example.myapplication.config.ApiEndpoints.CRYSTAL_REPORT_WPS_EXPORT_PDF;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myapplication.utils.DateRangeDialogHelper;
import com.example.myapplication.utils.LoadingDialogHelper;
import com.example.myapplication.utils.PdfUtils;
import com.example.myapplication.utils.SharedPrefUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LaporanBJ extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();

    private CardView laporan_mutasi_bj;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_bj);

        laporan_mutasi_bj = findViewById(R.id.laporan_mutasi_bj);

        username = SharedPrefUtils.getUsername(this);

        laporan_mutasi_bj.setOnClickListener(view -> showLaporanMutasiBJ());

    }

//    private void showLaporanMutasiBJ() {
//        DateRangeDialogHelper.show(this, (tglAwal, tglAkhir) -> {
//            String reportName = "CrMutasiBJ";
//
//            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
//                    + "?reportName=" + reportName
//                    + "&TglAwal=" + tglAwal
//                    + "&TglAkhir=" + tglAkhir
//                    + "&StartDate=" + tglAwal
//                    + "&EndDate=" + tglAkhir
//                    + "&Username=" + username;
//
//            loadingDialogHelper.show(this);
//
//            PdfUtils.downloadAndOpenPDF(
//                    this,
//                    url,
//                    "Mutasi Barang Jadi.pdf",
//                    executorService,
//                    loadingDialogHelper
//            );
//        });
//    }

    private void showLaporanMutasiBJ() {
        DateRangeDialogHelper.show(this, DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrMutasiBJ";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            loadingDialogHelper.show(this);

            PdfUtils.downloadAndOpenPDF(
                    this,
                    url,
                    "Mutasi Barang Jadi.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


}