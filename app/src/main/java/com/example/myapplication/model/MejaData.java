package com.example.myapplication.model;

public class MejaData {
    private String noMeja;
    private String namaMeja;

    public MejaData(String noMeja, String namaMeja) {
        this.noMeja = noMeja;
        this.namaMeja = namaMeja;
    }

    public String getNoMeja() {
        return noMeja;
    }

    public String getNamaMeja() {
        return namaMeja;
    }

    @Override
    public String toString() {
        return namaMeja; // supaya yang tampil di Spinner adalah NamaMeja
    }
}
