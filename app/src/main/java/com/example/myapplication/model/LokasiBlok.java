package com.example.myapplication.model;

public class LokasiBlok {
    private String idLokasi;
    private String blok;

    public LokasiBlok(String idLokasi, String blok) {
        this.idLokasi = idLokasi;
        this.blok = blok;
    }

    public String getIdLokasi() {
        return idLokasi;
    }

    public String getBlok() {
        return blok;
    }

    // Override toString agar Spinner dapat menampilkan data yang diinginkan
    @Override
    public String toString() {
        return idLokasi + " - " + blok;  // Menampilkan kombinasi idLokasi dan blok
    }
}
