package com.example.myapplication.model;

public class TellyData {
    private String idOrgTelly;
    private String namaOrgTelly;

    public TellyData(String idOrgTelly, String namaOrgTelly) {
        this.idOrgTelly = idOrgTelly;
        this.namaOrgTelly = namaOrgTelly;
    }

    public String getIdOrgTelly() {
        return idOrgTelly;
    }

    public String getNamaOrgTelly() {
        return namaOrgTelly;
    }

    @Override
    public String toString() {
        return namaOrgTelly; // supaya Spinner menampilkan nama
    }
}
