package com.example.myapplication;

import static android.content.ContentValues.TAG;
import static com.example.myapplication.config.ApiEndpoints.BASE_REPORT_MICROSERVICE;
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

import com.example.myapplication.utils.DateDialogHelper;
import com.example.myapplication.utils.DateRangeDialogHelper;
import com.example.myapplication.utils.LoadingDialogHelper;
import com.example.myapplication.utils.PdfMicroserviceUtils;
import com.example.myapplication.utils.PdfUtils;
import com.example.myapplication.utils.SharedPrefUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LaporanVerifikasiFragment extends Fragment {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();

    private CardView laporan_rangkuman_jumlah_label_input;
    private CardView laporan_bahan_terpakai;
    private CardView laporan_rangkuman_bongkar_susun;
    private CardView laporan_bahan_yang_dihasilkan;
    private CardView laporan_label_nyangkut;
    private CardView laporan_bagus_kulit_per_tanggal;
    private String username;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_laporan_verifikasi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        laporan_rangkuman_jumlah_label_input = view.findViewById(R.id.laporan_rangkuman_jumlah_label_input);
        laporan_bahan_terpakai = view.findViewById(R.id.laporan_bahan_terpakai);
        laporan_rangkuman_bongkar_susun = view.findViewById(R.id.laporan_rangkuman_bongkar_susun);
        laporan_bahan_yang_dihasilkan = view.findViewById(R.id.laporan_bahan_yang_dihasilkan);
        laporan_label_nyangkut = view.findViewById(R.id.laporan_label_nyangkut);
        laporan_bagus_kulit_per_tanggal = view.findViewById(R.id.laporan_bagus_kulit_per_tanggal);

        username = SharedPrefUtils.getUsername(requireContext());

        laporan_rangkuman_jumlah_label_input.setOnClickListener(v -> showLaporanRangkumanJumlahLabelInput());

        laporan_bahan_terpakai.setOnClickListener(v -> showLaporanBahanTerpakai());

        laporan_rangkuman_bongkar_susun.setOnClickListener(v -> showLaporanRangkumanBongkarSusun());

        laporan_bahan_yang_dihasilkan.setOnClickListener(v -> showLaporanBahanYgDihasilkan());

        laporan_label_nyangkut.setOnClickListener(v -> showLaporanLabelNyangkut());

        laporan_bagus_kulit_per_tanggal.setOnClickListener(v -> showLaporanBagusKulitPerTanggal());
    }

    private void showLaporanRangkumanJumlahLabelInput() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrLapProduksiSemua";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username;

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Rangkuman Jumlah Label Input (" + tglAwal + " sampai " + tglAkhir + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanBahanTerpakai() {
        DateDialogHelper.show(requireActivity(), DateDialogHelper.DefaultTanggalMode.HARI_INI, tanggal -> {
            String reportName = "CrLapBahanTerpakai";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tanggal
                    + "&Username=" + username;

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Bahan Terpakai (" + tanggal + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }

    private void showLaporanRangkumanBongkarSusun() {
        DateDialogHelper.show(requireActivity(), DateDialogHelper.DefaultTanggalMode.HARI_INI, tanggal -> {
            String reportName = "CrLapRangkumanBongkarSusun";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tanggal
                    + "&Username=" + username;

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Rangkuman Bongkar Susun (" + tanggal + ").pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }


    private void showLaporanBahanYgDihasilkan() {
        DateDialogHelper.show(requireActivity(), DateDialogHelper.DefaultTanggalMode.HARI_INI, tanggal -> {
            String reportName = "CrLapBahanDihasilkan";

            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?reportName=" + reportName
                    + "&TglAwal=" + tanggal
                    + "&Username=" + username;

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
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

        loadingDialogHelper.show(requireContext());

        PdfUtils.downloadAndOpenPDF(
                requireActivity(),
                url,
                "Laporan Nyangkut.pdf",
                executorService,
                loadingDialogHelper
        );
    }

    private void showLaporanBagusKulitPerTanggal() {
        DateDialogHelper.show(requireActivity(), DateDialogHelper.DefaultTanggalMode.HARI_INI, tanggal -> {
            String normalizedTanggal = normalizeDateForMicroservice(tanggal);

            String url = BASE_REPORT_MICROSERVICE
                    + "api/reports/sawn-timber/total-bagus-kulit-rambung/pdf"
                    + "?TglSawmill=" + Uri.encode(normalizedTanggal);

            PdfMicroserviceUtils.downloadAndOpenPDFWithToken(
                    requireActivity(),
                    url,
                    "Laporan Bagus Kulit Per Tanggal.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }

    private String normalizeDateForMicroservice(String rawDate) {
        Locale[] inputLocales = new Locale[]{new Locale("id", "ID"), Locale.ENGLISH, Locale.getDefault()};

        for (Locale locale : inputLocales) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy", locale);
                Date parsedDate = inputFormat.parse(rawDate);
                if (parsedDate == null) {
                    continue;
                }
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                return outputFormat.format(parsedDate);
            } catch (ParseException ignored) {
                // Try next locale.
            }
        }

        return rawDate;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
