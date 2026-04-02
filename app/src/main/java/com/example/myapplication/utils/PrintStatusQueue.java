package com.example.myapplication.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.myapplication.AppDatabase;
import com.example.myapplication.model.PendingPrintUpdate;

import java.util.concurrent.TimeUnit;

/**
 * Helper untuk enqueue update HasBeenPrinted dari activity manapun.
 *
 * Cara pakai (ganti semua updatePrintStatus() di activity dengan ini):
 *
 *   PrintStatusQueue.enqueue(this, "S4S_h", "NoS4S", noS4S, idUsername, actorName);
 *
 * Setelah enqueue, WorkManager akan:
 * - Langsung coba jalankan (jika network tersedia)
 * - Retry otomatis dengan exponential backoff jika gagal (10s, 20s, 40s, ...)
 * - Tetap antri meski app di-kill atau device restart
 */
public class PrintStatusQueue {

    private static final String TAG = "PrintStatusQueue";
    private static final String WORK_TAG = "print_status_sync";

    /**
     * @param context    context (activity atau application)
     * @param tableName  nama tabel header, contoh: "S4S_h", "ST_h", "Moulding_h"
     * @param keyColumn  nama kolom PK, contoh: "NoS4S", "NoST", "NoMoulding"
     * @param keyValue   nilai PK, contoh: "S4S-2024-001"
     * @param actorId    ID user login (untuk audit trail)
     * @param actorName  nama user login (untuk audit trail)
     */
    public static void enqueue(Context context, String tableName, String keyColumn,
                                String keyValue, String actorId, String actorName) {
        enqueue(context, tableName, keyColumn, keyValue, actorId, actorName, null);
    }

    /**
     * @param onEnqueued callback yang dipanggil di main thread SETELAH Room insert selesai.
     *                   Gunakan ini untuk memanggil refreshCurrentOutputList() agar tidak
     *                   terjadi race condition (refresh sebelum insert selesai).
     */
    public static void enqueue(Context context, String tableName, String keyColumn,
                                String keyValue, String actorId, String actorName,
                                Runnable onEnqueued) {
        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        PendingPrintUpdate item = new PendingPrintUpdate(tableName, keyColumn, keyValue,
                actorId, actorName);

        new Thread(() -> {
            db.pendingPrintUpdateDao().insert(item);
            Log.d(TAG, "Enqueued: " + tableName + " / " + keyValue);
            scheduleWorker(context);
            if (onEnqueued != null) {
                new Handler(Looper.getMainLooper()).post(onEnqueued);
            }
        }).start();
    }

    /**
     * Jadwalkan WorkManager Worker dengan:
     * - Constraint: harus ada jaringan (CONNECTED)
     * - Backoff policy: EXPONENTIAL, mulai dari 10 detik
     * - Tag: print_status_sync (untuk deduplication — Worker baru tidak dobel jika sudah ada)
     */
    private static void scheduleWorker(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(PrintStatusSyncWorker.class)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
                .addTag(WORK_TAG)
                .build();

        // KEEP_EXISTING: jika Worker dengan tag yang sama sudah antri/jalan, tidak dobel
        WorkManager.getInstance(context.getApplicationContext())
                .enqueueUniqueWork(
                        WORK_TAG,
                        androidx.work.ExistingWorkPolicy.APPEND_OR_REPLACE,
                        workRequest
                );

        Log.d(TAG, "Worker scheduled");
    }
}
