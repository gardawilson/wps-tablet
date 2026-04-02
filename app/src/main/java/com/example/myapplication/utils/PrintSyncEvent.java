package com.example.myapplication.utils;

import androidx.lifecycle.MutableLiveData;

/**
 * Singleton LiveData event bus untuk memberi tahu activity agar refresh output list
 * setelah Worker berhasil mengupdate HasBeenPrinted ke SQL Server.
 *
 * Worker memanggil PrintSyncEvent.notifySuccess("S4S_h") dari background thread.
 * Activity mengobservasi PrintSyncEvent.get() dan refresh jika tableName cocok.
 */
public class PrintSyncEvent {

    private static final MutableLiveData<String> lastSyncedTable = new MutableLiveData<>();

    /** Dipanggil oleh PrintStatusSyncWorker setelah update berhasil. Thread-safe. */
    public static void notifySuccess(String tableName) {
        lastSyncedTable.postValue(tableName);
    }

    /** Diobservasi oleh activity di onCreate(). */
    public static MutableLiveData<String> get() {
        return lastSyncedTable;
    }
}
