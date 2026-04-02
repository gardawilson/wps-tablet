package com.example.myapplication.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.myapplication.AppDatabase;
import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.utils.PrintSyncEvent;
import com.example.myapplication.model.PendingPrintUpdate;
import com.example.myapplication.model.PendingPrintUpdateDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.UUID;

/**
 * WorkManager Worker yang memproses antrian update HasBeenPrinted ke SQL Server.
 *
 * Cara kerja:
 * 1. Worker dipanggil oleh WorkManager (otomatis retry jika gagal, survive app restart).
 * 2. Ambil semua item dari Room table pending_print_updates.
 * 3. Untuk setiap item: jalankan UPDATE ke SQL Server, lalu hapus dari Room jika sukses.
 * 4. Jika ada item yang gagal diupdate (koneksi error), lempar exception → WorkManager retry.
 *
 * WorkManager menggunakan ExponentialBackoffPolicy otomatis, dengan constraint NETWORK_CONNECTED
 * yang di-set di PrintStatusQueue saat enqueue.
 */
public class PrintStatusSyncWorker extends Worker {

    private static final String TAG = "PrintStatusSync";

    public PrintStatusSyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        PendingPrintUpdateDao dao = db.pendingPrintUpdateDao();
        List<PendingPrintUpdate> pendingList = dao.getAll();

        if (pendingList.isEmpty()) {
            return Result.success();
        }

        Log.d(TAG, "Processing " + pendingList.size() + " pending print update(s)");

        boolean anyFailed = false;

        for (PendingPrintUpdate item : pendingList) {
            boolean success = processItem(item);
            if (success) {
                dao.deleteById(item.id);
                Log.d(TAG, "Updated and removed: " + item.tableName + " / " + item.keyValue);
                PrintSyncEvent.notifySuccess(item.tableName);
            } else {
                item.retryCount++;
                dao.update(item);
                anyFailed = true;
                Log.w(TAG, "Failed (retry " + item.retryCount + "): "
                        + item.tableName + " / " + item.keyValue);
            }
        }

        // Jika ada yang gagal, kembalikan retry agar WorkManager jadwalkan ulang dengan backoff
        return anyFailed ? Result.retry() : Result.success();
    }

    private boolean processItem(PendingPrintUpdate item) {
        Connection connection = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
            connection.setAutoCommit(false);

            String actorId = item.actorId != null ? item.actorId : "";
            String actorName = item.actorName != null ? item.actorName : "";
            String requestId = UUID.randomUUID().toString();
            AuditSessionContextHelper.apply(connection, actorId, actorName, requestId);

            // Query generik: tableName dan keyColumn sudah divalidasi saat enqueue
            String query = "UPDATE " + item.tableName
                    + " SET HasBeenPrinted = COALESCE(HasBeenPrinted, 0) + 1,"
                    + " LastPrintDate = GETDATE()"
                    + " WHERE " + item.keyColumn + " = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, item.keyValue);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    Log.w(TAG, "0 rows affected for " + item.tableName + " / " + item.keyValue);
                    return false;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "DB error for " + item.tableName + " / " + item.keyValue + ": " + e.getMessage());
            if (connection != null) {
                try { connection.rollback(); } catch (Exception ignored) {}
            }
            return false;
        } finally {
            if (connection != null) {
                try { connection.close(); } catch (Exception ignored) {}
            }
        }
    }
}
