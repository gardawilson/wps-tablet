package com.example.myapplication.model;

public class NyangkutData {
    private String noNyangkut;
    private String tgl;

    public NyangkutData(String noNyangkut, String tgl) {
        this.noNyangkut = noNyangkut;
        this.tgl = tgl;
    }

    public String getNoNyangkut() { return noNyangkut; }
    public String getTgl() { return tgl; }
}
