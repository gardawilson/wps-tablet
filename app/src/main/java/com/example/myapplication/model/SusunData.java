package com.example.myapplication.model;

public class SusunData {
    private String noBongkarSusun;

    public SusunData(String noBongkarSusun) {
        this.noBongkarSusun = noBongkarSusun;
    }

    public String getNoBongkarSusun() {
        return noBongkarSusun;
    }

    public void setNoBongkarSusun(String noBongkarSusun) {
        this.noBongkarSusun = noBongkarSusun;
    }

    @Override
    public String toString() {
        return noBongkarSusun; // yang ditampilkan di spinner
    }
}
