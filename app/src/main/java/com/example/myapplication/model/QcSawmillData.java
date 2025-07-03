package com.example.myapplication.model;

public class QcSawmillData {
    private String noQc;
    private String tgl;
    private int idJenisKayu;
    private String namaJenisKayu;
    private String meja;

    public QcSawmillData(String noQc, String tgl, int idJenisKayu, String namaJenisKayu, String meja) {
        this.noQc = noQc;
        this.tgl = tgl;
        this.idJenisKayu = idJenisKayu;
        this.namaJenisKayu = namaJenisKayu;
        this.meja = meja;
    }

    // Getter dan Setter
    public String getNoQc() {
        return noQc;
    }

    public void setNoQc(String noQc) {
        this.noQc = noQc;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public int getIdJenisKayu() {
        return idJenisKayu;
    }

    public void setIdJenisKayu(int idJenisKayu) {
        this.idJenisKayu = idJenisKayu;
    }

    public String getNamaJenisKayu() {
        return namaJenisKayu;
    }

    public void setNamaJenisKayu(String namaJenisKayu) {
        this.namaJenisKayu = namaJenisKayu;
    }

    public String getMeja() {
        return meja;
    }

    public void setMeja(String meja) {
        this.meja = meja;
    }
}
