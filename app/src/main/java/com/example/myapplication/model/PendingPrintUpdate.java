package com.example.myapplication.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room entity untuk menyimpan antrian update HasBeenPrinted yang belum berhasil dikirim ke DB.
 * Setara dengan Hive box di Flutter — data persist meski app di-kill.
 *
 * Query yang akan dijalankan oleh Worker:
 *   UPDATE {tableName} SET HasBeenPrinted = COALESCE(HasBeenPrinted, 0) + 1,
 *                          LastPrintDate = GETDATE()
 *   WHERE {keyColumn} = {keyValue}
 */
@Entity(tableName = "pending_print_updates")
public class PendingPrintUpdate {

    @PrimaryKey(autoGenerate = true)
    public long id;

    /** Nama tabel header, contoh: S4S_h, ST_h, Moulding_h, FJ_h, CCAkhir_h, dll */
    @ColumnInfo(name = "table_name")
    public String tableName;

    /** Nama kolom primary key, contoh: NoS4S, NoST, NoMoulding, NoFJ, NoCCAkhir, dll */
    @ColumnInfo(name = "key_column")
    public String keyColumn;

    /** Nilai primary key yang akan diupdate */
    @ColumnInfo(name = "key_value")
    public String keyValue;

    /** ID user untuk audit trail */
    @ColumnInfo(name = "actor_id")
    public String actorId;

    /** Nama user untuk audit trail */
    @ColumnInfo(name = "actor_name")
    public String actorName;

    /** Timestamp saat data dimasukkan ke queue (epoch millis) */
    @ColumnInfo(name = "created_at")
    public long createdAt;

    /** Jumlah percobaan yang sudah dilakukan (informasi saja, retry dikelola WorkManager) */
    @ColumnInfo(name = "retry_count")
    public int retryCount;

    public PendingPrintUpdate(String tableName, String keyColumn, String keyValue,
                               String actorId, String actorName) {
        this.tableName = tableName;
        this.keyColumn = keyColumn;
        this.keyValue = keyValue;
        this.actorId = actorId;
        this.actorName = actorName;
        this.createdAt = System.currentTimeMillis();
        this.retryCount = 0;
    }
}
