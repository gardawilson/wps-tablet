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

public class DateDialogHelper {

    public interface OnDateSelectedListener {
        void onDateSelected(String tanggal);
    }

    public enum DefaultTanggalMode {
        HARI_INI,
        AWAL_BULAN,
        KEMARIN
    }

    public static void show(Activity activity, DefaultTanggalMode mode, OnDateSelectedListener listener) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogView = inflater.inflate(R.layout.dialog_input_date, null);

        EditText edtTgl = dialogView.findViewById(R.id.edtTglAwal);
        Button btnLihat = dialogView.findViewById(R.id.btnLihat);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        // Atur default date sesuai mode
        switch (mode) {
            case AWAL_BULAN:
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case KEMARIN:
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case HARI_INI:
            default:
                // default = hari ini (sudah by default)
                break;
        }

        edtTgl.setText(sdf.format(calendar.getTime()));

        // Date picker
        edtTgl.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(activity,
                    (DatePicker view, int year, int month, int dayOfMonth) -> {
                        c.set(year, month, dayOfMonth);
                        edtTgl.setText(sdf.format(c.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });

        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle("Pilih Tanggal")
                .setView(dialogView)
                .create();

        btnLihat.setOnClickListener(v -> {
            String tanggal = edtTgl.getText().toString();
            alertDialog.dismiss();
            listener.onDateSelected(tanggal);
        });

        alertDialog.show();
    }
}
