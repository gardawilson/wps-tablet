package com.example.myapplication.model;

public class PenerimaanSTSawmillData {
    private String noPenerimaanST;
    private String tglLaporan;
    private String noKayuBulat;

    public PenerimaanSTSawmillData(String noPenerimaanST, String tglLaporan, String noKayuBulat) {
        this.noPenerimaanST = noPenerimaanST;
        this.tglLaporan = tglLaporan;
        this.noKayuBulat = noKayuBulat;
    }

    public String getNoPenerimaanST() {
        return noPenerimaanST;
    }

    public String getTglLaporan() {
        return tglLaporan;
    }

    public String getNoKayuBulat() {
        return noKayuBulat;
    }

    @Override
    public String toString() {
        return noPenerimaanST + " | " + tglLaporan + " | " + noKayuBulat;
    }
}
