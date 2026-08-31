package com.example.myapplication;

import static android.content.ContentValues.TAG;
import static com.example.myapplication.config.ApiEndpoints.CRYSTAL_REPORT_WPS_EXPORT_PDF;

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

public class LaporanManajemenFragment extends Fragment {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();

    private CardView laporan_rekap_stock_on_hand;
    private CardView laporan_rekap_mutasi;
    private CardView laporan_flow_produksi;
    private CardView laporan_produksi_semua_mesin;
    private CardView laporan_label_perhari;
    private CardView laporan_produksi_mesin_lembur_nonlembur;
    private String username;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_laporan_manajemen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        laporan_rekap_stock_on_hand = view.findViewById(R.id.laporan_rekap_stock_on_hand);
        laporan_rekap_mutasi = view.findViewById(R.id.laporan_rekap_mutasi);
        laporan_flow_produksi = view.findViewById(R.id.laporan_flow_produksi);
        laporan_produksi_semua_mesin = view.findViewById(R.id.laporan_produksi_semua_mesin);
        laporan_label_perhari = view.findViewById(R.id.laporan_label_perhari);
        laporan_produksi_mesin_lembur_nonlembur = view.findViewById(R.id.laporan_produksi_mesin_lembur_nonlembur);

        username = SharedPrefUtils.getUsername(requireContext());


        laporan_rekap_stock_on_hand.setOnClickListener(v -> showLaporanStockOnHand());
        laporan_rekap_mutasi.setOnClickListener(v -> showLaporanRekapMutasi());
        laporan_flow_produksi.setOnClickListener(v -> showLaporanFlowProduksi());
        laporan_produksi_semua_mesin.setOnClickListener(v -> showLaporanProduksiSemuaMesin());
        laporan_label_perhari.setOnClickListener(v -> showLaporanLabelPerHari());
        laporan_produksi_mesin_lembur_nonlembur.setOnClickListener(v -> showLaporanMesinLemburNonLembur());
    }

    private void showLaporanStockOnHand() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrStockOnHand";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Stock on Hand (" + tglAwal + " sampai " + tglAkhir + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanRekapMutasi() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrRekapMutasi";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Rekap Mutasi (" + tglAwal + " sampai " + tglAkhir + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanFlowProduksi() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrFlowProduksiPerPeriode";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Flow Produksi Per Periode (" + tglAwal + " sampai " + tglAkhir + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanProduksiSemuaMesin() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrLapRekapProsuksiSemuaMesin";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Produksi Semua Mesin (" + tglAwal + " sampai " + tglAkhir + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanLabelPerHari() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrLapLabelPerhari";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Label Per Hari (" + tglAwal + " sampai " + tglAkhir + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }

    private void showLaporanMesinLemburNonLembur() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrLapLemburPerMesin";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Mesin Lembur dan NonLembur (" + tglAwal + " sampai " + tglAkhir + ").pdf",
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
