package com.example.myapplication.model;

public class LokasiItem {
    private String idLokasi;
    private String blok;

    public LokasiItem(String idLokasi, String blok) {
        this.idLokasi = idLokasi;
        this.blok = blok;
    }

    public String getIdLokasi() {
        return idLokasi;
    }

    public String getBlok() {
        return blok;
    }

    @Override
    public String toString() {
        return idLokasi; // Untuk ditampilkan di Spinner
    }
}