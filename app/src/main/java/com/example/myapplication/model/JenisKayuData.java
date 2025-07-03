package com.example.myapplication.model;

public class JenisKayuData {
    private int idJenisKayu;
    private String jenis;

    public JenisKayuData(int idJenisKayu, String jenis) {
        this.idJenisKayu = idJenisKayu;
        this.jenis = jenis;
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

    @Override
    public String toString() {
        return jenis; // agar spinner menampilkan nama jenis
    }
}
