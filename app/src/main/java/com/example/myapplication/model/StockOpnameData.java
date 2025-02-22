package com.example.myapplication.model;

import com.example.myapplication.utils.DateTimeUtils;  // Import DateTimeUtils

public class StockOpnameData {
    private String noSO;
    private String tgl;

    // Constructor
    public StockOpnameData(String noSO, String tgl) {
        this.noSO = noSO;
        this.tgl = tgl;
    }

    // Getter methods
    public String getNoSO() {
        return noSO;
    }

    public String getTgl() {
        return tgl;
    }

    // Override toString() untuk menampilkan NoSO dan tanggal yang diformat
    @Override
    public String toString() {
        String formattedDate = DateTimeUtils.formatDate(tgl);  // Format tanggal
        return noSO + " (" + formattedDate + ")";  // Contoh: "SO123 (01 Okt 2023)"
    }
}