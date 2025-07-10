package com.example.myapplication.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateRangeDialogHelper {

    public interface OnDateRangeSelectedListener {
        void onDateRangeSelected(String tglAwal, String tglAkhir);
    }

    public enum DefaultTanggalMode {
        BULAN_LALU,
        MINGGU_LALU,
        HARI_INI,
        BULAN_INI
    }


    public static void show(Activity activity, DefaultTanggalMode mode, OnDateRangeSelectedListener listener) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogView = inflater.inflate(R.layout.dialog_input_date_range, null);

        EditText edtTglAwal = dialogView.findViewById(R.id.edtTglAwal);
        EditText edtTglAkhir = dialogView.findViewById(R.id.edtTglAkhir);
        Button btnLihat = dialogView.findViewById(R.id.btnLihat);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

        Calendar calAwal = Calendar.getInstance();
        Calendar calAkhir = Calendar.getInstance();

        switch (mode) {
            case BULAN_LALU:
                calAwal.add(Calendar.MONTH, -1);
                calAwal.set(Calendar.DAY_OF_MONTH, 1);

                calAkhir.add(Calendar.MONTH, -1);
                calAkhir.set(Calendar.DAY_OF_MONTH, calAkhir.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;

            case MINGGU_LALU:
                Calendar today = Calendar.getInstance();

                // Pindah ke minggu lalu (Senin)
                today.add(Calendar.WEEK_OF_YEAR, -1);
                today.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calAwal = (Calendar) today.clone();

                // Tanggal akhir = tambah 6 hari dari Senin
                Calendar akhirMinggu = (Calendar) today.clone();
                akhirMinggu.add(Calendar.DAY_OF_MONTH, 6);
                calAkhir = akhirMinggu;
                break;

            case HARI_INI:
                calAkhir = Calendar.getInstance(); // Hari ini
                calAwal = (Calendar) calAkhir.clone();
                calAwal.set(Calendar.DAY_OF_MONTH, 1); // Tanggal 1 bulan ini
                break;


            case BULAN_INI:
            default:
                calAwal.set(Calendar.DAY_OF_MONTH, 1);
                // calAkhir sudah hari ini
                break;
        }

        edtTglAwal.setText(sdf.format(calAwal.getTime()));
        edtTglAkhir.setText(sdf.format(calAkhir.getTime()));

        // Picker logic tetap sama...
        View.OnClickListener dateClickListener = v -> {
            final EditText target = (EditText) v;
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(activity,
                    (DatePicker view, int year, int month, int dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        target.setText(sdf.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        };

        edtTglAwal.setOnClickListener(dateClickListener);
        edtTglAkhir.setOnClickListener(dateClickListener);

        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle("Filter Laporan")
                .setView(dialogView)
                .create();

        btnLihat.setOnClickListener(v -> {
            String tglAwal = edtTglAwal.getText().toString();
            String tglAkhir = edtTglAkhir.getText().toString();
            alertDialog.dismiss();
            listener.onDateRangeSelected(tglAwal, tglAkhir);
        });

        alertDialog.show();
    }

}
