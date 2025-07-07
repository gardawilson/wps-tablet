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

    public static void show(Activity activity, OnDateRangeSelectedListener listener) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogView = inflater.inflate(R.layout.dialog_report_input_tgl_awal_akhir, null);

        EditText edtTglAwal = dialogView.findViewById(R.id.edtTglAwal);
        EditText edtTglAkhir = dialogView.findViewById(R.id.edtTglAkhir);
        Button btnLihat = dialogView.findViewById(R.id.btnLihat);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

        Calendar calAwal = Calendar.getInstance();
        calAwal.set(Calendar.DAY_OF_MONTH, 1); // Set ke tanggal 1 bulan ini
        String tglAwalStr = sdf.format(calAwal.getTime());

        Calendar calAkhir = Calendar.getInstance(); // Hari ini
        String tglAkhirStr = sdf.format(calAkhir.getTime());

        edtTglAwal.setText(tglAwalStr);
        edtTglAkhir.setText(tglAkhirStr);


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
                .setTitle("Pilih Tanggal")
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
