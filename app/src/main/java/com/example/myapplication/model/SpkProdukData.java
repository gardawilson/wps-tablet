package com.example.myapplication.model;

public class SpkProdukData {

    private Integer idProdukSPK;
    private String namaProduk;
    private String noSPK;
    private Double ton;

    public SpkProdukData(Integer idProdukSPK, String namaProduk, String noSPK, Double ton) {
        this.idProdukSPK = idProdukSPK;
        this.namaProduk = namaProduk;
        this.noSPK = noSPK;
        this.ton = ton;
    }

    public Integer getIdProdukSPK() { return idProdukSPK; }
    public String getNamaProduk() { return namaProduk; }
    public String getNoSPK() { return noSPK; }
    public Double getTon() { return ton; }

    @Override
    public String toString() {
        return noSPK + " - " + namaProduk;
    }
}
