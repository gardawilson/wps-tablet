package com.example.myapplication.model;

public class MstWarnaData {
    private int idWarna;
    private String namaWarna;

    public MstWarnaData(int idWarna, String namaWarna) {
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
