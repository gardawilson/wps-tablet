package com.example.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

public class UpdateManager {
    private static final String TAG = "UpdateManager";
    private static final String SERVER_IP = "192.168.10.100";
    private static final String SHARE_NAME = "IS Department";
    private static final String UPDATE_PATH = "updateAndroid";
    private static final String USERNAME = "garda";
    private static final String PASSWORD = "Ljpqstu9q";
    private static final String DOMAIN = "";

    private final Context context;
    private SMBClient smbClient;
    private Connection connection;
    private Session session;
    private DiskShare share;

    public UpdateManager(Context context) {
        this.context = context;
    }

    // Inner class untuk informasi update
    public static class UpdateInfo {
        public final String version;
        public final String changelog;
        public final String fileName;

        public UpdateInfo(String version, String changelog, String fileName) {
            this.version = version;
            this.changelog = changelog;
            this.fileName = fileName;
        }
    }

    // Interface callbacks
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

    public void initialize(InitCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {
            private String errorMessage;

            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    // Inisialisasi SMBJ client
                    smbClient = new SMBClient();

                    // Membuat koneksi ke server
                    connection = smbClient.connect(SERVER_IP);
                    AuthenticationContext authContext = new AuthenticationContext(
                            USERNAME,
                            PASSWORD.toCharArray(),
                            DOMAIN
                    );

                    // Membuat session dan mengakses share
                    session = connection.authenticate(authContext);
                    share = (DiskShare) session.connectShare(SHARE_NAME);

                    return true;
                } catch (Exception e) {
                    errorMessage = e.getMessage();
                    Log.e(TAG, "Failed to initialize SMBJ: " + e.getMessage(), e);
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success && callback != null) {
                    callback.onInitComplete();
                } else if (callback != null) {
                    callback.onInitFailed(errorMessage);
                }
            }
        }.execute();
    }

    public void checkForUpdates(final UpdateCallback callback) {
        if (!isNetworkAvailable()) {
            callback.onUpdateCheckFailed("Network not available");
            return;
        }

        new AsyncTask<Void, Void, UpdateInfo>() {
            @Override
            protected UpdateInfo doInBackground(Void... voids) {
                try {
                    Log.d(TAG, "Checking for updates...");
                    String versionFilePath = UPDATE_PATH + "/version.txt";

                    // Dapatkan versi aplikasi saat ini
                    String currentVersion = context.getPackageManager()
                            .getPackageInfo(context.getPackageName(), 0).versionName;

                    if (!share.fileExists(versionFilePath)) {
                        throw new IOException("version.txt not found in SMB directory");
                    }

                    // Baca versi dari server
                    String serverVersion = null;
                    String apkFileName = null;
                    StringBuilder changelog = new StringBuilder();

                    File versionFile = share.openFile(
                            versionFilePath,
                            EnumSet.of(AccessMask.GENERIC_READ),
                            null,
                            SMB2ShareAccess.ALL,
                            SMB2CreateDisposition.FILE_OPEN,
                            null
                    );

                    try (InputStream in = versionFile.getInputStream()) {
                        java.util.Scanner scanner = new java.util.Scanner(in, "UTF-8");
                        if (scanner.hasNextLine()) {
                            serverVersion = scanner.nextLine().trim();
                        }
                        if (scanner.hasNextLine()) {
                            apkFileName = scanner.nextLine().trim();
                        }
                        while (scanner.hasNextLine()) {
                            changelog.append(scanner.nextLine()).append("\n");
                        }
                    }

                    // Bandingkan versi
                    if (compareVersions(serverVersion, currentVersion) <= 0) {
                        // Versi server sama atau lebih lama
                        return null;
                    }

                    return new UpdateInfo(serverVersion, changelog.toString().trim(), apkFileName);

                } catch (Exception e) {
                    Log.e(TAG, "Error checking for updates: " + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(UpdateInfo updateInfo) {
                if (updateInfo != null) {
                    callback.onUpdateAvailable(updateInfo);
                } else {
                    callback.onUpdateNotAvailable();
                }
            }
        }.execute();
    }

    // Fungsi untuk membandingkan versi
    private int compareVersions(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");
        int length = Math.max(parts1.length, parts2.length);

        for (int i = 0; i < length; i++) {
            int part1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int part2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;

            if (part1 < part2) return -1;
            if (part1 > part2) return 1;
        }
        return 0;
    }

    public void downloadUpdate(final String fileName, final DownloadCallback callback) {
        new AsyncTask<Void, Integer, java.io.File>() {
            @Override
            protected java.io.File doInBackground(Void... voids) {
                try {
                    Log.d(TAG, "Downloading update: " + fileName);
                    String remoteFilePath = UPDATE_PATH + "/" + fileName;

                    File remoteFile = share.openFile(
                            remoteFilePath,
                            EnumSet.of(AccessMask.GENERIC_READ),
                            null,
                            SMB2ShareAccess.ALL,
                            SMB2CreateDisposition.FILE_OPEN,
                            null
                    );

                    long fileSize = remoteFile.getFileInformation().getStandardInformation().getEndOfFile();
                    java.io.File localFile = new java.io.File(context.getFilesDir(), fileName);
                    long downloaded = 0;

                    try (InputStream in = remoteFile.getInputStream();
                         FileOutputStream out = new FileOutputStream(localFile)) {
                        byte[] buffer = new byte[8192]; // Increased buffer size for better performance
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) > 0) {
                            out.write(buffer, 0, bytesRead);
                            downloaded += bytesRead;
                            int progress = (int) ((downloaded * 100) / fileSize);
                            publishProgress(progress);
                        }
                    }

                    return localFile;

                } catch (IOException e) {
                    Log.e(TAG, "Download failed: " + e.getMessage());
                    return null;
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
                    callback.onDownloadFailed("Download failed");
                }
            }
        }.execute();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    // Method untuk membersihkan resources
    public void cleanup() {
        Log.d(TAG, "Starting cleanup process");
        try {
            if (share != null) {
                try {
                    Log.d(TAG, "Closing share");
                    share.close();
                } finally {
                    share = null;
                }
            }

            if (session != null) {
                try {
                    Log.d(TAG, "Closing session");
                    session.close();
                } finally {
                    session = null;
                }
            }

            if (connection != null) {
                try {
                    Log.d(TAG, "Closing connection");
                    connection.close();
                } finally {
                    connection = null;
                }
            }

            if (smbClient != null) {
                try {
                    Log.d(TAG, "Closing SMB client");
                    smbClient.close();
                } finally {
                    smbClient = null;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error during cleanup", e);
        } finally {
            share = null;
            session = null;
            connection = null;
            smbClient = null;
            Log.d(TAG, "Cleanup process completed");
        }
    }
}