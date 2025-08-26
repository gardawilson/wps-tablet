package com.example.myapplication.model;

public class SpkData {
    private String noSPK;
    private String buyer;

    // Constructor utama
    public SpkData(String noSPK, String buyer) {
        this.noSPK = noSPK;
        this.buyer = buyer;
    }

    // Constructor untuk dummy/placeholder
    public SpkData(String noSPK) {
        this.noSPK = noSPK;
        this.buyer = "";
    }

    public String getNoSPK() {
        return noSPK;
    }

    public String getBuyer() {
        return buyer;
    }

    // Override toString untuk tampilan di spinner
    @Override
    public String toString() {
        if (buyer == null || buyer.isEmpty()) {
            return noSPK;
        }
        return noSPK + " - " + buyer;
    }
}