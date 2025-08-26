package com.example.myapplication.model;

public class LokasiData {
    private String idLokasi; // ubah dari int ke String
    private String description;
    private boolean enable;
    private String blok;

    public LokasiData(String idLokasi, String description, boolean enable, String blok) {
        this.idLokasi = idLokasi;
        this.description = description;
        this.enable = enable;
        this.blok = blok;
    }

    // Getter
    public String getIdLokasi() {
        return idLokasi;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnable() {
        return enable;
    }

    public String getBlok() {
        return blok;
    }

    // Optional: toString untuk debugging
    @Override
    public String toString() {
        return idLokasi; // hanya menampilkan nama lokasi
    }
}
