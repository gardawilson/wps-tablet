package com.example.myapplication.model;

public class FisikData {
    private String singkatan;

    public FisikData(String singkatan) {
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
