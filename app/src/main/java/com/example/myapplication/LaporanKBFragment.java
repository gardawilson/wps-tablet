package com.example.myapplication;

import static com.example.myapplication.config.ApiEndpoints.CRYSTAL_REPORT_WPS_EXPORT_PDF;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import com.example.myapplication.utils.DateRangeDialogHelper;
import com.example.myapplication.utils.LoadingDialogHelper;
import com.example.myapplication.utils.PdfUtils;
import com.example.myapplication.utils.SharedPrefUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LaporanKBFragment extends Fragment {

    private static final String TAG = "LaporanKB";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();

    private CardView laporan_mutasi_racip_detail;
    private CardView laporan_kayu_bulat_hidup;
    private CardView laporan_mutasi_racip;
    private CardView laporan_mutasi_kayu_bulat_gantung;
    private CardView laporan_umur_kayu_bulat_non_rambung;
    private String username;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_laporan_kb, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        laporan_mutasi_racip_detail = view.findViewById(R.id.laporan_mutasi_racip_detail);
        laporan_kayu_bulat_hidup = view.findViewById(R.id.laporan_kayu_bulat_hidup);
        laporan_mutasi_racip = view.findViewById(R.id.laporan_mutasi_racip);
        laporan_mutasi_kayu_bulat_gantung = view.findViewById(R.id.laporan_mutasi_kayu_bulat_gantung);
        laporan_umur_kayu_bulat_non_rambung = view.findViewById(R.id.laporan_umur_kayu_bulat_non_rambung);

        username = SharedPrefUtils.getUsername(requireContext());

        laporan_mutasi_racip_detail.setOnClickListener(v -> showLaporanMutasiRacipDetail());
        laporan_kayu_bulat_hidup.setOnClickListener(v -> showLaporanKayuBulatHidup());
        laporan_mutasi_racip.setOnClickListener(v -> showLaporanMutasiRacip());
        laporan_mutasi_kayu_bulat_gantung.setOnClickListener(v -> showLaporanMutasiKayuBulatGantung());
        laporan_umur_kayu_bulat_non_rambung.setOnClickListener(v -> showLaporanUmurKayuBulatNonRambung());

    }

    private void showLaporanMutasiRacipDetail() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrLapMutasiHasilRacipDetail";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            Log.d(TAG, "Mulai download dari URL: " + url);

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Mutasi Hasil Racip Detail.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanKayuBulatHidup() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.HARI_INI, (tglAwal, tglAkhir) -> {
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

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Kayu Bulat Hidup.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanMutasiRacip() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrMutasiRacip";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            Log.d(TAG, "Mulai download laporan mutasi racip dari URL: " + url);

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Mutasi Racip.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }



    private void showLaporanMutasiKayuBulatGantung() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
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

            loadingDialogHelper.show(requireActivity());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Mutasi Kayu Bulat Gantung.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanUmurKayuBulatNonRambung() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrUmurKayuBulatNonRambung";
            String type = "Non Rambung";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username
                    + "&Type=" + Uri.encode(type); // penting: encode judul untuk URL

            Log.d(TAG, "Mulai download laporan kayu bulat hidup dari URL: " + url);

            loadingDialogHelper.show(requireActivity());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Mutasi Kayu Bulat Gantung.pdf",
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
