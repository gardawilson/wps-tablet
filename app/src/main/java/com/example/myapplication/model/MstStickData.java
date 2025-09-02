package com.example.myapplication.model;

public class MstStickData {
    private String idStickBy;
    private String namaStickBy;

    public MstStickData(String idStickBy, String namaStickBy) {
        this.idStickBy = idStickBy;
        this.namaStickBy = namaStickBy;
    }

    public String getIdStickBy() {
        return idStickBy;
    }

    public void setIdStickBy(String idStickBy) {
        this.idStickBy = idStickBy;
    }

    public String getNamaStickBy() {
        return namaStickBy;
    }

    public void setNamaStickBy(String namaStickBy) {
        this.namaStickBy = namaStickBy;
    }

    @Override
    public String toString() {
        return namaStickBy; // supaya Spinner menampilkan nama stick by
    }
}
