package com.example.myapplication.model;

public class SawmillDetailInputData {
    private String panjang;
    private String pcs;

    public SawmillDetailInputData() {
        this.panjang = "";
        this.pcs = "";
    }

    public SawmillDetailInputData(String panjang, String pcs) {
        this.panjang = panjang;
        this.pcs = pcs;
    }

    public String getPanjang() {
        return panjang;
    }

    public void setPanjang(String panjang) {
        this.panjang = panjang;
    }

    public String getPcs() {
        return pcs;
    }

    public void setPcs(String pcs) {
        this.pcs = pcs;
    }
}
