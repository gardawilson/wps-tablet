package com.example.myapplication.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.api.MasterApi;
import com.example.myapplication.model.SpkData;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class SPKTujuanDialogHelper {

    public interface OnSPKSelectedListener {
        void onSelected(SpkData selectedSPK);
    }

    private SPKTujuanDialogHelper() {
        // no instance
    }

    /**
     * Menampilkan dialog pemilihan SPK Tujuan.
     * Listener onSelected dipanggil hanya jika user memilih SPK valid dan menekan Simpan.
     *
     * @param activity        Activity pemanggil
     * @param executorService Executor untuk load data SPK di background
     * @param listener        Callback ketika SPK berhasil dipilih
     */
    public static void show(Activity activity, ExecutorService executorService, OnSPKSelectedListener listener) {
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_select_spk_tujuan, null);
        Spinner spinSPKDialog = dialogView.findViewById(R.id.spinSPKDialog);
        Button btnCancel = dialogView.findViewById(R.id.btnDialogCancel);
        Button btnSave = dialogView.findViewById(R.id.btnDialogSave);

        Dialog dialog = new Dialog(activity);
        dialog.setContentView(dialogView);
        dialog.setCancelable(false);

        // Load SPK list di background
        executorService.execute(() -> {
            List<SpkData> spkList = MasterApi.getSPKList();
            spkList.add(0, new SpkData("PILIH"));

            activity.runOnUiThread(() -> {
                ArrayAdapter<SpkData> adapter = new ArrayAdapter<>(
                        activity,
                        android.R.layout.simple_spinner_item,
                        spkList
                );
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinSPKDialog.setAdapter(adapter);
            });
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            SpkData selectedSPK = (SpkData) spinSPKDialog.getSelectedItem();

            if (selectedSPK == null || selectedSPK.getNoSPK().equals("PILIH")) {
                Toast.makeText(activity, "Harap pilih SPK terlebih dahulu!", Toast.LENGTH_SHORT).show();
                return;
            }

            dialog.dismiss();
            listener.onSelected(selectedSPK);
        });

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        dialog.show();
    }
}