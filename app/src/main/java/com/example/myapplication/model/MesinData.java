package com.example.myapplication.model;

public class MesinData {
    private int idMesin;
    private String namaMesin;

    public MesinData(int idMesin, String namaMesin) {
        this.idMesin = idMesin;
        this.namaMesin = namaMesin;
    }

    public int getIdMesin() {
        return idMesin;
    }

    public String getNamaMesin() {
        return namaMesin;
    }

    @Override
    public String toString() {
        return namaMesin; // Yang akan ditampilkan di spinner
    }
}
