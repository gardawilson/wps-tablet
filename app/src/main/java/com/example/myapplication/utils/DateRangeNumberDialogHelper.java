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
import java.util.Date;
import java.util.Locale;

public class DateRangeNumberDialogHelper {

    public interface OnDateRangeNumberSelectedListener {
        void onDateRangeNumberSelected(String tglAwal, String tglAkhir, String angka);
    }

    public static void show(Activity activity, OnDateRangeNumberSelectedListener listener) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogView = inflater.inflate(R.layout.dialog_input_date_range_with_number, null);

        EditText edtTglAwal = dialogView.findViewById(R.id.edtTglAwal);
        EditText edtTglAkhir = dialogView.findViewById(R.id.edtTglAkhir);
        EditText edtAngka = dialogView.findViewById(R.id.edtAngka);
        Button btnLihat = dialogView.findViewById(R.id.btnLihat);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

        // Ambil tanggal Senin dan Minggu minggu sebelumnya
        Date[] weekRange = getStartAndEndOfPreviousWeek();
        edtTglAwal.setText(sdf.format(weekRange[0]));
        edtTglAkhir.setText(sdf.format(weekRange[1]));

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
            String angka = edtAngka.getText().toString().trim();

            alertDialog.dismiss();
            listener.onDateRangeNumberSelected(tglAwal, tglAkhir, angka);
        });

        alertDialog.show();
    }

    private static Date[] getStartAndEndOfPreviousWeek() {
        Calendar cal = Calendar.getInstance();

        // Atur ke hari Senin minggu ini
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        // Mundur 7 hari ke Senin minggu lalu
        cal.add(Calendar.DATE, -7);
        Date startOfWeek = cal.getTime();

        // Tambah 6 hari untuk dapat Minggu minggu lalu
        cal.add(Calendar.DATE, 6);
        Date endOfWeek = cal.getTime();

        return new Date[]{startOfWeek, endOfWeek};
    }
}
