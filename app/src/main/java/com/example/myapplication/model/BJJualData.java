package com.example.myapplication.model;

import com.example.myapplication.utils.DateTimeUtils;

public class BJJualData {
    private String noBJJual;
    private String tglJual;
    private String noSPK;
    private String keterangan;

    public BJJualData(String noBJJual, String tglJual, String noSPK, String keterangan) {
        this.noBJJual = noBJJual;
        this.tglJual = DateTimeUtils.formatDate(tglJual);
        this.noSPK = noSPK;
        this.keterangan = keterangan;
    }

    // Getter & Setter
    public String getNoBJJual() { return noBJJual; }
    public void setNoBJJual(String noBJJual) { this.noBJJual = noBJJual; }

    public String getTglJual() { return tglJual; }
    public void setTglJual(String tglJual) { this.tglJual = tglJual; }

    public String getNoSPK() { return noSPK; }
    public void setNoSPK(String noSPK) { this.noSPK = noSPK; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
}
