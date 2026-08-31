package com.example.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.config.ApiEndpoints;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Pengecekan & unduh pembaruan APK lewat HTTP API backend
 * (endpoint {@code /api/update/tablet/...}) - menggantikan mekanisme SMB share.
 *
 * Kontrak publik (initialize / checkForUpdates / downloadUpdate / cleanup +
 * callback) dipertahankan agar pemakaian di MainActivity tidak berubah.
 */
public class UpdateManager {
    private static final String TAG = "UpdateManager";
    private static final String APP_ID = "tablet";
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int READ_TIMEOUT = 30000;

    private final Context context;

    public UpdateManager(Context context) {
        this.context = context.getApplicationContext();
    }

    // Informasi update
    public static class UpdateInfo {
        public final String version;
        public final String changelog;
        public final String fileName;
        public final String minVersion;
        public final boolean forceUpdate;

        public UpdateInfo(String version, String changelog, String fileName,
                          String minVersion, boolean forceUpdate) {
            this.version = version;
            this.changelog = changelog;
            this.fileName = fileName;
            this.minVersion = minVersion;
            this.forceUpdate = forceUpdate;
        }
    }

    public interface InitCallback {
        void onInitComplete();
        void onInitFailed(String error);
    }

    public interface UpdateCallback {
        void onUpdateAvailable(UpdateInfo updateInfo);
        void onUpdateNotAvailable();
        void onUpdateCheckFailed(String errorMessage);
    }

    public interface DownloadCallback {
        void onDownloadProgress(int percentage);
        void onDownloadComplete(java.io.File updateFile);
        void onDownloadFailed(String errorMessage);
    }

    /** Tidak ada koneksi persisten pada mode HTTP - langsung siap. */
    public void initialize(InitCallback callback) {
        if (callback == null) {
            return;
        }
        if (isNetworkAvailable()) {
            callback.onInitComplete();
        } else {
            callback.onInitFailed("Tidak ada koneksi jaringan");
        }
    }

    public void checkForUpdates(final UpdateCallback callback) {
        if (!isNetworkAvailable()) {
            callback.onUpdateCheckFailed("Tidak ada koneksi jaringan");
            return;
        }

        new AsyncTask<Void, Void, UpdateInfo>() {
            private String errorMessage;

            @Override
            protected UpdateInfo doInBackground(Void... voids) {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(baseUrl() + "/api/update/" + APP_ID + "/version");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(CONNECT_TIMEOUT);
                    conn.setReadTimeout(READ_TIMEOUT);

                    int code = conn.getResponseCode();
                    InputStream in = (code >= 200 && code < 300)
                            ? conn.getInputStream() : conn.getErrorStream();
                    String body = readStream(in);

                    if (code == 404) {
                        // Belum ada versi yang dipublish untuk 'tablet' - anggap tidak ada update.
                        Log.d(TAG, "checkForUpdates: version belum dipublish (404)");
                        return null;
                    }
                    if (code < 200 || code >= 300) {
                        errorMessage = "Server membalas HTTP " + code;
                        return null;
                    }

                    JSONObject json = new JSONObject(body);
                    JSONObject data = json.optJSONObject("data");
                    if (data == null) {
                        errorMessage = "Respons server tidak valid";
                        return null;
                    }

                    String serverVersion = data.optString("latestVersion", "").trim();
                    String fileName = data.optString("fileName", "").trim();
                    String changelog = data.optString("changelog", "").trim();
                    String minVersion = data.optString("minVersion", "0.0.0").trim();
                    boolean forceUpdate = data.optBoolean("forceUpdate", false);

                    if (serverVersion.isEmpty() || fileName.isEmpty()) {
                        errorMessage = "Info versi tidak lengkap dari server";
                        return null;
                    }

                    String currentVersion = context.getPackageManager()
                            .getPackageInfo(context.getPackageName(), 0).versionName;

                    if (compareVersions(serverVersion, currentVersion) <= 0) {
                        return null; // sudah versi terbaru
                    }

                    return new UpdateInfo(serverVersion, changelog, fileName, minVersion, forceUpdate);

                } catch (Exception e) {
                    Log.e(TAG, "checkForUpdates error: " + e.getMessage(), e);
                    errorMessage = e.getMessage();
                    return null;
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }

            @Override
            protected void onPostExecute(UpdateInfo info) {
                if (info != null) {
                    callback.onUpdateAvailable(info);
                } else if (errorMessage != null) {
                    callback.onUpdateCheckFailed(errorMessage);
                } else {
                    callback.onUpdateNotAvailable();
                }
            }
        }.execute();
    }

    public void downloadUpdate(final String fileName, final DownloadCallback callback) {
        new AsyncTask<Void, Integer, java.io.File>() {
            private String errorMessage;

            @Override
            protected java.io.File doInBackground(Void... voids) {
                HttpURLConnection conn = null;
                try {
                    String encoded = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
                    URL url = new URL(baseUrl() + "/api/update/" + APP_ID + "/download/" + encoded);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(CONNECT_TIMEOUT);
                    conn.setReadTimeout(READ_TIMEOUT);

                    int code = conn.getResponseCode();
                    if (code < 200 || code >= 300) {
                        errorMessage = "Server membalas HTTP " + code;
                        return null;
                    }

                    long total = conn.getContentLengthLong();
                    java.io.File outFile = new java.io.File(context.getFilesDir(), fileName);
                    long downloaded = 0;

                    try (InputStream in = conn.getInputStream();
                         FileOutputStream out = new FileOutputStream(outFile)) {
                        byte[] buffer = new byte[8192];
                        int read;
                        int lastPct = -1;
                        while ((read = in.read(buffer)) > 0) {
                            out.write(buffer, 0, read);
                            downloaded += read;
                            if (total > 0) {
                                int pct = (int) ((downloaded * 100) / total);
                                if (pct != lastPct) {
                                    lastPct = pct;
                                    publishProgress(pct);
                                }
                            }
                        }
                    }
                    return outFile;

                } catch (Exception e) {
                    Log.e(TAG, "downloadUpdate error: " + e.getMessage(), e);
                    errorMessage = e.getMessage();
                    return null;
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                if (values.length > 0) {
                    callback.onDownloadProgress(values[0]);
                }
            }

            @Override
            protected void onPostExecute(java.io.File result) {
                if (result != null) {
                    callback.onDownloadComplete(result);
                } else {
                    callback.onDownloadFailed(errorMessage != null ? errorMessage : "Unduhan gagal");
                }
            }
        }.execute();
    }

    /** Tidak ada resource yang perlu ditutup pada mode HTTP. */
    public void cleanup() {
        // no-op
    }

    // --- helpers ------------------------------------------------------------

    private String baseUrl() {
        String url = ApiEndpoints.BASE_URL_API;
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    private int compareVersions(String v1, String v2) {
        String[] p1 = v1.split("\\.");
        String[] p2 = v2.split("\\.");
        int len = Math.max(p1.length, p2.length);
        for (int i = 0; i < len; i++) {
            int a = i < p1.length ? parseIntSafe(p1[i]) : 0;
            int b = i < p2.length ? parseIntSafe(p2[i]) : 0;
            if (a != b) {
                return a < b ? -1 : 1;
            }
        }
        return 0;
    }

    private static int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    private String readStream(InputStream in) throws Exception {
        if (in == null) {
            return "";
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}
