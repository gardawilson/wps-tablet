package com.example.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;

public class PdfUtils {
    private static final String TAG = "PdfUtils";

    public static void downloadAndOpenPDF(
            Activity activity,
            String urlString,
            String fileName,
            ExecutorService executorService,
            LoadingDialogHelper loadingDialogHelper // opsional, bisa null jika tidak dipakai
    ) {
        if (loadingDialogHelper != null) loadingDialogHelper.show(activity);

        executorService.execute(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage());
                    if (loadingDialogHelper != null) loadingDialogHelper.hide();

                    activity.runOnUiThread(() -> {
                        Toast.makeText(activity, "Tidak ada data untuk ditampilkan! ", Toast.LENGTH_LONG).show();
                    });


                    return;
                }

                InputStream input = connection.getInputStream();
                File pdfFile = new File(activity.getCacheDir(), fileName);
                FileOutputStream output = new FileOutputStream(pdfFile);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }

                output.close();
                input.close();

                activity.runOnUiThread(() -> {
                    try {
                        Uri pdfUri = FileProvider.getUriForFile(
                                activity,
                                activity.getPackageName() + ".provider",
                                pdfFile
                        );

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(pdfUri, "application/pdf");
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NO_HISTORY);

                        activity.startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "Gagal membuka PDF", e);
                    } finally {
                        if (loadingDialogHelper != null) loadingDialogHelper.hide();
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "Download error", e);
                if (loadingDialogHelper != null) loadingDialogHelper.hide();
            }
        });
    }
}
