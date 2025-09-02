package com.example.myapplication.model;

public class MstJenisKayuData {
    private int idJenisKayu;
    private String jenis;
    private int isUpah;

    // Constructor lengkap (kalau memang ada nilai isUpah)
    public MstJenisKayuData(int idJenisKayu, String jenis, int isUpah) {
        this.idJenisKayu = idJenisKayu;
        this.jenis = jenis;
        this.isUpah = isUpah;
    }

    // Constructor overload (default isUpah = 0)
    public MstJenisKayuData(int idJenisKayu, String jenis) {
        this.idJenisKayu = idJenisKayu;
        this.jenis = jenis;
        this.isUpah = 0; // default
    }

    public int getIdJenisKayu() {
        return idJenisKayu;
    }

    public void setIdJenisKayu(int idJenisKayu) {
        this.idJenisKayu = idJenisKayu;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public int getIsUpah() {
        return isUpah;
    }

    public void setIsUpah(int isUpah) {
        this.isUpah = isUpah;
    }

    @Override
    public String toString() {
        return jenis; // agar spinner menampilkan nama jenis
    }
}
