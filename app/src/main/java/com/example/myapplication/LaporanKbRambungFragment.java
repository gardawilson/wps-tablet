package com.example.myapplication;

import static com.example.myapplication.config.ApiEndpoints.BASE_REPORT_MICROSERVICE;
import static com.example.myapplication.config.ApiEndpoints.CRYSTAL_REPORT_WPS_EXPORT_PDF;

import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import com.example.myapplication.utils.DateRangeDialogHelper;
import com.example.myapplication.utils.DateRangeNumberDialogHelper;
import com.example.myapplication.utils.LoadingDialogHelper;
import com.example.myapplication.utils.PdfMicroserviceUtils;
import com.example.myapplication.utils.PdfUtils;
import com.example.myapplication.utils.SharedPrefUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LaporanKbRambungFragment extends Fragment {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();

    private CardView laporan_rekap_penerimaan_st_dari_sawmill;
    private CardView laporan_mutasi_kb_gantung_rambung;
    private CardView laporan_umur_kayu_bulat_rambung;
    private String username;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_laporan_kb_rambung, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        laporan_rekap_penerimaan_st_dari_sawmill = view.findViewById(R.id.laporan_rekap_penerimaan_st_dari_sawmill);
        laporan_mutasi_kb_gantung_rambung = view.findViewById(R.id.laporan_mutasi_kb_gantung_rambung);
        laporan_umur_kayu_bulat_rambung = view.findViewById(R.id.laporan_umur_kayu_bulat_rambung);

        username = SharedPrefUtils.getUsername(requireContext());

        laporan_rekap_penerimaan_st_dari_sawmill.setOnClickListener(v -> showLaporanRekapPenerimaanStDariSawmill());
        laporan_mutasi_kb_gantung_rambung.setOnClickListener(v -> showLaporanMutasiKbGantungRambung());
        laporan_umur_kayu_bulat_rambung.setOnClickListener(v -> showLaporanUmurKayuBulatRambung());
    }

    private void showLaporanRekapPenerimaanStDariSawmill() {
        DateRangeNumberDialogHelper.show(requireActivity(), (tglAwal, tglAkhir, angka) -> {
            String url = BASE_REPORT_MICROSERVICE
                    + "api/reports/kayu-bulat/rekap-produktivitas-sawmill-rp/pdf"
                    + "?TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&UpahRacip=" + angka;

            PdfMicroserviceUtils.downloadAndOpenPDFWithToken(
                    requireActivity(),
                    url,
                    "Rekap Penerimaan ST dari Sawmill.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanMutasiKbGantungRambung() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
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

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Mutasi Kayu Bulat Gantung.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }

    private void showLaporanUmurKayuBulatRambung() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
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

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Umur Kayu Bulat Rambung.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
