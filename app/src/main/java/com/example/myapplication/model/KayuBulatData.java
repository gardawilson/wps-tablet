package com.example.myapplication.model;

public class KayuBulatData {
    private String noPlat;
    private String jenis;
    private String supplier;
    private String noTruk;
    private String suket;

    public KayuBulatData(String noPlat, String jenis, String supplier, String noTruk, String suket) {
        this.noPlat = noPlat;
        this.jenis = jenis;
        this.supplier = supplier;
        this.noTruk = noTruk;
        this.suket = suket;
    }

    public String getNoPlat() {
        return noPlat;
    }

    public String getJenis() {
        return jenis;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getNoTruk() {
        return noTruk;
    }

    public String getSuket() {
        return suket;
    }
}
