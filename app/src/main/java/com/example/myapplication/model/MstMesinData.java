package com.example.myapplication.model;

public class MstMesinData {
    private String noProduksi;
    private String namaMesin;

    public MstMesinData(String noProduksi, String namaMesin) {
        this.noProduksi = noProduksi;
        this.namaMesin = namaMesin;
    }

    public String getNoProduksi() {
        return noProduksi;
    }

    public String getNamaMesin() {
        return namaMesin;
    }

    @Override
    public String toString() {
        return namaMesin + " - " + noProduksi;
    }
}