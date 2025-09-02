package com.example.myapplication.model;

public class MstBjData {
    private int idBarangJadi;
    private String namaBarangJadi;

    public MstBjData(int idBarangJadi, String namaBarangJadi) {
        this.idBarangJadi = idBarangJadi;
        this.namaBarangJadi = namaBarangJadi;
    }

    public int getIdBarangJadi() {
        return idBarangJadi;
    }

    public String getNamaBarangJadi() {
        return namaBarangJadi;
    }

    @Override
    public String toString() {
        return namaBarangJadi; // Spinner akan menampilkan NamaBarangJadi
    }
}
