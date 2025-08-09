package com.example.myapplication.model;

public class ProductionData {
    private String noProduksi;
    private String shift;
    private String tanggal;
    private String mesin;
    private String operator;
    private String jamKerja;
    private int jumlahAnggota;
    private double hourMeter;
    private int idMesin;
    private int idOperator;

    public ProductionData(String noProduksi, String shift, String tanggal, String mesin, String operator,
                          String jamKerja, int jumlahAnggota, double hourMeter, int idMesin, int idOperator) {
        this.noProduksi = noProduksi;
        this.shift = shift;
        this.tanggal = tanggal;
        this.mesin = mesin;
        this.operator = operator;
        this.jamKerja = jamKerja;
        this.jumlahAnggota = jumlahAnggota;
        this.hourMeter = hourMeter;
        this.idMesin = idMesin;
        this.idOperator = idOperator;
    }

    public String getNoProduksi() { return noProduksi; }
    public String getShift() { return shift; }
    public String getTanggal() { return tanggal; }
    public String getMesin() { return mesin; }
    public String getOperator() { return operator; }

    public String getJamKerja() { return jamKerja; }
    public int getJumlahAnggota() { return jumlahAnggota; }
    public double getHourMeter() { return hourMeter; }

    public int getIdMesin() { return idMesin; }
    public int getIdOperator() { return idOperator; }
}
