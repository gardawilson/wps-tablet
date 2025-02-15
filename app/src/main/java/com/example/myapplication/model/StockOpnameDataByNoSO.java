package com.example.myapplication.model;

public class StockOpnameDataByNoSO {
    private String noLabel;
    private String idLokasi;

    // Constructor, Getter dan Setter
    public StockOpnameDataByNoSO(String noLabel, String idLokasi) {
        this.noLabel = noLabel;
        this.idLokasi = idLokasi;
    }

    public String getNoLabel() {
        return noLabel;
    }

    public void setNoLabel(String noLabel) {
        this.noLabel = noLabel;
    }

    public String getIdLokasi() {
        return idLokasi;
    }

    public void setIdLokasi(String idLokasi) {
        this.idLokasi = idLokasi;
    }
}
