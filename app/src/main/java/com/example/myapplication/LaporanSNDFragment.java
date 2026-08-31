package com.example.myapplication;

import static com.example.myapplication.config.ApiEndpoints.CRYSTAL_REPORT_WPS_EXPORT_PDF;

import android.os.Bundle;

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

public class LaporanSNDFragment extends Fragment {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();

    private CardView laporan_mutasi_snd;
    private String username;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_laporan_snd, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        laporan_mutasi_snd = view.findViewById(R.id.laporan_mutasi_snd);

        username = SharedPrefUtils.getUsername(requireContext());

        laporan_mutasi_snd.setOnClickListener(v -> showLaporanMutasiSND());

    }

    private void showLaporanMutasiSND() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrMutasiSanding";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Mutasi Sanding.pdf",
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
