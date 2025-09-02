package com.example.myapplication.model;

public class MstFisikData {
    private String singkatan;

    public MstFisikData(String singkatan) {
        this.singkatan = singkatan;
    }

    public String getSingkatan() {
        return singkatan;
    }

    @Override
    public String toString() {
        return singkatan; // Spinner akan menampilkan singkatan
    }
}
