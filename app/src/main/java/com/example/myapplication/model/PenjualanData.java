package com.example.myapplication.model;

import com.example.myapplication.utils.DateTimeUtils;

public class PenjualanData {
    private String noJual;
    private String tglJual;
    private int idBuyer;
    private String buyerName;           // <-- tambahan
    private String keterangan;
    private String noSJ;
    private String noPlat;
    private int idJenisKendaraan;
    private String jenisKendaraanModel; // <-- tambahan

    public PenjualanData(String noJual, String tglJual, int idBuyer, String buyerName,
                         String keterangan, String noSJ, String noPlat,
                         int idJenisKendaraan, String jenisKendaraanModel) {
        this.noJual = noJual;
        this.tglJual = DateTimeUtils.formatDate(tglJual);
        this.idBuyer = idBuyer;
        this.buyerName = buyerName;
        this.keterangan = keterangan;
        this.noSJ = noSJ;
        this.noPlat = noPlat;
        this.idJenisKendaraan = idJenisKendaraan;
        this.jenisKendaraanModel = jenisKendaraanModel;
    }

    // Getter & Setter
    public String getNoJual() { return noJual; }
    public void setNoJual(String noJual) { this.noJual = noJual; }

    public String getTglJual() { return tglJual; }
    public void setTglJual(String tglJual) { this.tglJual = tglJual; }

    public int getIdBuyer() { return idBuyer; }
    public void setIdBuyer(int idBuyer) { this.idBuyer = idBuyer; }

    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    public String getNoSJ() { return noSJ; }
    public void setNoSJ(String noSJ) { this.noSJ = noSJ; }

    public String getNoPlat() { return noPlat; }
    public void setNoPlat(String noPlat) { this.noPlat = noPlat; }

    public int getIdJenisKendaraan() { return idJenisKendaraan; }
    public void setIdJenisKendaraan(int idJenisKendaraan) { this.idJenisKendaraan = idJenisKendaraan; }

    public String getJenisKendaraanModel() { return jenisKendaraanModel; }
    public void setJenisKendaraanModel(String jenisKendaraanModel) { this.jenisKendaraanModel = jenisKendaraanModel; }
}
