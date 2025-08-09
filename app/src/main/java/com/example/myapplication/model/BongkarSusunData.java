package com.example.myapplication.model;

public class BongkarSusunData {
    private String noBongkarSusun;
    private String tanggal;
    private boolean isPemakaian; // Tipe boolean untuk bit/flag
    private String noPenerimaanST;
    private String keterangan;

    public BongkarSusunData(String noBongkarSusun, String tanggal, boolean isPemakaian,
                            String noPenerimaanST, String keterangan) {
        this.noBongkarSusun = noBongkarSusun;
        this.tanggal = tanggal;
        this.isPemakaian = isPemakaian;
        this.noPenerimaanST = noPenerimaanST;
        this.keterangan = keterangan;
    }

    // Getter methods
    public String getNoBongkarSusun() { return noBongkarSusun; }
    public String getTanggal() { return tanggal; }
    public boolean isPemakaian() { return isPemakaian; }
    public String getNoPenerimaanST() { return noPenerimaanST; }
    public String getKeterangan() { return keterangan; }
}