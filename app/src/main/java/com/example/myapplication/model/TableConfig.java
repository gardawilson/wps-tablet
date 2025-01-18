package com.example.myapplication.model;

import java.util.List;
import java.util.function.Function;

public class TableConfig {
    public String tableNameH;
    public String tableNameD;
    public String columnName;
    public int tableLayoutId;
    public List<String> list;
    public Function<String, String> resultChecker;
    public int sumLabelId; // ID TextView untuk menampilkan jumlah

    /**
     * Constructor untuk konfigurasi tabel.
     *
     * @param tableNameH     Nama tabel header
     * @param tableNameD     Nama tabel detail
     * @param columnName     Nama kolom untuk validasi data
     * @param tableLayoutId  ID layout tabel di UI
     * @param list           Daftar data hasil scan
     * @param resultChecker  Fungsi untuk memeriksa data hasil scan
     * @param sumLabelId     ID TextView untuk menampilkan jumlah
     */
    public TableConfig(String tableNameH, String tableNameD, String columnName, int tableLayoutId, List<String> list, Function<String, String> resultChecker, int sumLabelId) {
        this.tableNameH = tableNameH;
        this.tableNameD = tableNameD;
        this.columnName = columnName;
        this.tableLayoutId = tableLayoutId;
        this.list = list;
        this.resultChecker = resultChecker;
        this.sumLabelId = sumLabelId;
    }
}
