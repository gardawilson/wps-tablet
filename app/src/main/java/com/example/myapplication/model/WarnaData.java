package com.example.myapplication.model;

public class WarnaData {
    private int idWarna;
    private String namaWarna;

    public WarnaData(int idWarna, String namaWarna) {
        this.idWarna = idWarna;
        this.namaWarna = namaWarna;
    }

    public int getIdWarna() {
        return idWarna;
    }

    public String getNamaWarna() {
        return namaWarna;
    }

    @Override
    public String toString() {
        return namaWarna != null ? namaWarna : "";
    }
}
