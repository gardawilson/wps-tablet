package com.example.myapplication.model;

public class STPembelianData {
    private String noSTPembelian;
    private String tglLaporan;
    private String tglMasuk;
    private String supplier;
    private String noTruk;
    private String noPlat;
    private String tonSJ;
    private String keteranganPembelian;


    public STPembelianData(String noSTPembelian, String tglLaporan, String tglMasuk, String supplier, String noTruk, String noPlat, String tonSJ, String keteranganPembelian) {
        this.noSTPembelian = noSTPembelian;
        this.tglLaporan = tglLaporan;
        this.tglMasuk = tglMasuk;
        this.supplier = supplier;
        this.noTruk = noTruk;
        this.noPlat = noPlat;
        this.tonSJ = tonSJ;
        this.keteranganPembelian = keteranganPembelian;
    }

    public String getNoSTPembelian() { return noSTPembelian; }
    public String getTglLaporan() { return tglLaporan; }
    public String getTglMasuk() { return tglMasuk; }
    public String getSupplier() { return supplier; }
    public String getNoTruk() { return noTruk; }
    public String getNoPlat() { return noPlat; }
    public String getTonSJ() { return tonSJ; }
    public String getKeteranganPembelian() { return keteranganPembelian; }
}
