package com.example.myapplication.model;

public class StockOpnameData {
    private String noSO;
    private String tgl;

    // Constructor
    public StockOpnameData(String noSO, String tgl) {
        this.noSO = noSO;
        this.tgl = tgl;
    }

    // Getter dan Setter
    public String getNoSO() {
        return noSO;
    }

    public void setNoSO(String noSO) {
        this.noSO = noSO;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }
}
