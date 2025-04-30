package com.example.myapplication.model;

public class STUpahData {
    private String noSTUpah;
    private String tgl;
    private String customer;
    private String noPlat;
    private String noTruk;
    private String noSJ;
    private String keteranganUpah;

    public STUpahData(String noSTUpah, String tgl, String customer, String noPlat, String noTruk, String noSJ, String keteranganUpah) {
        this.noSTUpah = noSTUpah;
        this.tgl = tgl;
        this.customer = customer;
        this.noPlat = noPlat;
        this.noTruk = noTruk;
        this.noSJ = noSJ;
        this.keteranganUpah = keteranganUpah;
    }

    public String getNoSTUpah() {
        return noSTUpah;
    }

    public String getTgl() {
        return tgl;
    }

    public String getCustomer() {
        return customer;
    }

    public String getNoPlat() {
        return noPlat;
    }

    public String getNoTruk() {
        return noTruk;
    }

    public String getNoSJ() {
        return noSJ;
    }

    public String getKeteranganUpah() {
        return keteranganUpah;
    }
}
