package com.example.myapplication.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.myapplication.R;

public class LoadingDialogHelper {

    private Dialog dialog;

    public void show(Context context) {
        if (!(context instanceof Activity)) return; // optional safety check
        Activity activity = (Activity) context;

        activity.runOnUiThread(() -> {
            if (dialog == null) {
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
                dialog = new Dialog(context);
                dialog.setContentView(view);
                dialog.setCancelable(false);

                Window window = dialog.getWindow();
                if (window != null) {
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    window.setLayout(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                }
            }

            if (!dialog.isShowing()) {
                dialog.show();
            }
        });
    }


    public void hide() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
