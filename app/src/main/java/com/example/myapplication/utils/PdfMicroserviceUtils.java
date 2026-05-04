package com.example.myapplication.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;

public class PdfMicroserviceUtils {

    private static final String TAG = "PdfMicroserviceUtils";

    public static void downloadAndOpenPDFWithToken(
            Activity activity,
            String urlString,
            String fileName,
            ExecutorService executorService,
            LoadingDialogHelper loadingDialogHelper
    ) {

        if (loadingDialogHelper != null) loadingDialogHelper.show(activity);

        executorService.execute(() -> {
            try {

                String token = TokenManager.getToken(activity);
                Log.d(TAG, "Requesting PDF: " + urlString);

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.setRequestProperty("Accept", "application/pdf");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    String errorBody = readStreamSafely(connection.getErrorStream());

                    Log.e(TAG, "Server returned HTTP "
                            + connection.getResponseCode()
                            + " "
                            + connection.getResponseMessage()
                            + " for URL: " + urlString
                            + (errorBody.isEmpty() ? "" : " | body: " + errorBody));

                    activity.runOnUiThread(() ->
                            Toast.makeText(activity,
                                    "Tidak ada data untuk ditampilkan!",
                                    Toast.LENGTH_LONG).show()
                    );

                    if (loadingDialogHelper != null)
                        activity.runOnUiThread(loadingDialogHelper::hide);

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
                connection.disconnect();

                activity.runOnUiThread(() -> {
                    try {

                        Uri pdfUri = FileProvider.getUriForFile(
                                activity,
                                activity.getPackageName() + ".provider",
                                pdfFile
                        );

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(pdfUri, "application/pdf");
                        intent.setFlags(
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        | Intent.FLAG_ACTIVITY_NO_HISTORY
                        );

                        activity.startActivity(intent);

                    } catch (Exception e) {
                        Log.e(TAG, "Gagal membuka PDF", e);
                    } finally {
                        if (loadingDialogHelper != null)
                            loadingDialogHelper.hide();
                    }
                });

            } catch (Exception e) {

                Log.e(TAG, "Download error", e);

                activity.runOnUiThread(() -> {
                    if (loadingDialogHelper != null)
                        loadingDialogHelper.hide();
                });
            }
        });
    }

    private static String readStreamSafely(InputStream stream) {
        if (stream == null) {
            return "";
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (builder.length() > 0) {
                    builder.append(' ');
                }
                builder.append(line);
            }
            return builder.toString();
        } catch (Exception e) {
            Log.e(TAG, "Gagal membaca error response", e);
            return "";
        }
    }
}
