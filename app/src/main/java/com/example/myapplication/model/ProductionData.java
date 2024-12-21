package com.example.myapplication.model;

public class ProductionData {
    private String noProduksi;
    private String shift;
    private String tanggal;
    private String mesin;
    private String operator;

    public ProductionData(String noProduksi, String shift, String tanggal, String mesin, String operator) {
        this.noProduksi = noProduksi;
        this.shift = shift;
        this.tanggal = tanggal;
        this.mesin = mesin;
        this.operator = operator;
    }

    public String getNoProduksi() { return noProduksi; }
    public String getShift() { return shift; }
    public String getTanggal() { return tanggal; }
    public String getMesin() { return mesin; }
    public String getOperator() { return operator; }
}
