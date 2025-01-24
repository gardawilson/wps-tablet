package com.example.myapplication.model;

public class BongkarSusunData {
    private String noBongkarSusun;
    private String tanggal;
    private String keterangan;

    public BongkarSusunData(String noBongkarSusun, String tanggal, String keterangan) {
        this.noBongkarSusun = noBongkarSusun;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
    }

    public String getNoBongkarSusun() { return noBongkarSusun; }
    public String getTanggal() { return tanggal; }
    public String getKeterangan() { return keterangan; }

}