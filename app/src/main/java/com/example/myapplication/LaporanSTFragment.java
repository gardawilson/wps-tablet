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

public class LaporanSTFragment extends Fragment {

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_laporan_st, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        laporan_rekap_hasil_sawmill_meja = view.findViewById(R.id.laporan_rekap_hasil_sawmill_meja);
        laporan_mutasi_st = view.findViewById(R.id.laporan_mutasi_st);
        laporan_rekap_penerimaan_st_dari_sawmill = view.findViewById(R.id.laporan_rekap_penerimaan_st_dari_sawmill);
        laporan_sawmill_perhari_perlebar_pertebal = view.findViewById(R.id.laporan_sawmill_perhari_perlebar_pertebal);
        laporan_stock_st_basah = view.findViewById(R.id.laporan_stock_st_basah);
        laporan_rekap_hasil_sawmill_semua_meja = view.findViewById(R.id.laporan_rekap_hasil_sawmill_semua_meja);

        username = SharedPrefUtils.getUsername(requireContext());

        laporan_rekap_hasil_sawmill_meja.setOnClickListener(v -> showLaporanRekapHasilSawmillMeja());
        laporan_rekap_penerimaan_st_dari_sawmill.setOnClickListener(v -> showLaporanRekapPenerimaanStDariSawmill());
        laporan_mutasi_st.setOnClickListener(v -> showLaporanMutasiST());
        laporan_sawmill_perhari_perlebar_pertebal.setOnClickListener(v -> showLaporanSawmillPerhariPerlebarPertebal());
        laporan_stock_st_basah.setOnClickListener(v -> showLaporanStockStBasah());
        laporan_rekap_hasil_sawmill_semua_meja.setOnClickListener(v -> showLaporanRekapHasilSawmillSemuaMeja());
    }

    private void showLaporanRekapHasilSawmillMeja() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.MINGGU_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrRekapHasilSawmillPerMejaUpahBorongan";

            // Pastikan tanggal diformat yyyy-MM-dd agar sesuai dengan query string
            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username
                    + "&reportName=" + reportName;

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Rekap Hasil Sawmill Per Meja.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }

    private void showLaporanRekapPenerimaanStDariSawmill() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.MINGGU_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrRekapPenSTDariSawmill";

            // Pastikan tanggal diformat yyyy-MM-dd agar sesuai dengan query string
            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username
                    + "&reportName=" + reportName;

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Rekap Penerimaan ST dari Sawmill.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }

    private void showLaporanMutasiST() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrMutasiSawnTimber";

            // Pastikan tanggal diformat yyyy-MM-dd agar sesuai dengan query string
            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?TglAwal=" + tglAwal
                    + "&TglAkhir=" + tglAkhir
                    + "&StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username
                    + "&reportName=" + reportName;

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Mutasi Sawn Timber.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }

    private void showLaporanSawmillPerhariPerlebarPertebal() {
        DateRangeDialogHelper.show(requireActivity(), DateRangeDialogHelper.DefaultTanggalMode.BULAN_LALU, (tglAwal, tglAkhir) -> {
            String reportName = "CrSTSawmillPerHariPerTebalPerLebar";

            // Format URL endpoint baru
            String url = CRYSTAL_REPORT_WPS_EXPORT_PDF
                    + "?StartDate=" + tglAwal
                    + "&EndDate=" + tglAkhir
                    + "&Username=" + username
                    + "&reportName=" + reportName;

            loadingDialogHelper.show(requireContext());

            PdfUtils.downloadAndOpenPDF(
                    requireActivity(),
                    url,
                    "Laporan ST Per Hari Per Tebal Per Lebar.pdf",
                    executorService,
                    loadingDialogHelper
            );
        });
    }

    private void showLaporanStockStBasah() {
        DateDialogHelper.show(requireActivity(),
                DateDialogHelper.DefaultTanggalMode.HARI_INI,
                (tanggal) -> {

                    String url = BASE_REPORT_MICROSERVICE
                            + "api/reports/sawn-timber/stock-st-basah/pdf"
                            + "?end_date=" + tanggal;

                    PdfMicroserviceUtils.downloadAndOpenPDFWithToken(
                            requireActivity(),
                            url,
                            "Stock ST Basah.pdf",
                            executorService,
                            loadingDialogHelper
                    );
                });
    }

    private void showLaporanRekapHasilSawmillSemuaMeja() {
        DateRangeDialogHelper.show(
                requireActivity(),
                DateRangeDialogHelper.DefaultTanggalMode.MINGGU_LALU,
                (tglAwal, tglAkhir) -> {
                    String normalizedTglAwal = normalizeDateForMicroservice(tglAwal);
                    String normalizedTglAkhir = normalizeDateForMicroservice(tglAkhir);

                    String url = BASE_REPORT_MICROSERVICE
                            + "api/reports/sawn-timber/rekap-hasil-sawmill-per-meja-upah-borongan-v2/pdf"
                            + "?start_date=" + Uri.encode(normalizedTglAwal)
                            + "&end_date=" + Uri.encode(normalizedTglAkhir);

                    PdfMicroserviceUtils.downloadAndOpenPDFWithToken(
                            requireActivity(),
                            url,
                            "Rekap Hasil Sawmill Semua Meja.pdf",
                            executorService,
                            loadingDialogHelper
                    );
                }
        );
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
