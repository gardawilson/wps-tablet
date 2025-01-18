package com.example.myapplication.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.R;

public class CustomProgressDialog {

    private AlertDialog progressDialog;
    private TextView progressText;
    private ProgressBar progressBar;

    public CustomProgressDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, null);
        builder.setView(dialogView);

        progressDialog = builder.create();

        progressText = dialogView.findViewById(R.id.progress_text);
        progressBar = dialogView.findViewById(R.id.progress_bar);

        progressText.setText("0%");
        progressBar.setProgress(0);
    }

    public void show() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void updateProgress(int progress) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressText.setText(progress + "%");
            progressBar.setProgress(progress);
        }
    }

    public void dismiss() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
