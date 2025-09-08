package com.example.myapplication.model;

public class MstBuyerData {
    private int idBuyer;
    private String buyer;
    private boolean isExport; // mapping dari kolom BIT

    public MstBuyerData(int idBuyer, String buyer, boolean isExport) {
        this.idBuyer = idBuyer;
        this.buyer = buyer;
        this.isExport = isExport;
    }

    public int getIdBuyer() {
        return idBuyer;
    }

    public String getBuyer() {
        return buyer;
    }

    public boolean isExport() {
        return isExport;
    }

    @Override
    public String toString() {
        return buyer; // Spinner akan menampilkan nama buyer
    }
}
