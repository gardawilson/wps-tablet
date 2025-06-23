package com.example.myapplication.model;

public class SawmillDetailData {
    private int noUrut;
    private float tebal;
    private float lebar;
    private float panjang;
    private int pcs;
    private boolean isLocal;
    private int idUOMTblLebar;
    private int idUOMPanjang;
    private int isBagusKulit;
    private int idGradeKB;
    private String namaGrade;

    public SawmillDetailData(int noUrut, float tebal, float lebar, float panjang, int pcs,
                             boolean isLocal, int idUOMTblLebar, int idUOMPanjang, int isBagusKulit,
                             int idGradeKB, String namaGrade) {
        this.noUrut = noUrut;
        this.tebal = tebal;
        this.lebar = lebar;
        this.panjang = panjang;
        this.pcs = pcs;
        this.isLocal = isLocal;
        this.idUOMTblLebar = idUOMTblLebar;
        this.idUOMPanjang = idUOMPanjang;
        this.isBagusKulit = isBagusKulit;
        this.idGradeKB = idGradeKB;
        this.namaGrade = namaGrade;
    }


    public int getNoUrut() { return noUrut; }
    public float getTebal() { return tebal; }
    public float getLebar() { return lebar; }
    public float getPanjang() { return panjang; }
    public int getPcs() { return pcs; }
    public boolean isLocal() { return isLocal; }
    public int getIdUOMTblLebar() { return idUOMTblLebar; }
    public int getIdUOMPanjang() { return idUOMPanjang; }
    public int getIsBagusKulit() { return isBagusKulit; }
    public int getIdGradeKB() { return idGradeKB; }

    public String getNamaGrade() {
        if (isLocal) {
            return "LOKAL";
        } else {
            return namaGrade;
        }
    }

    public void setPcs(int pcs) {
        this.pcs = pcs;
    }

    public String getIsBagusKulitLabel() {
        switch (isBagusKulit) {
            case 0: return "-";
            case 1: return "BAGUS";
            case 2: return "KULIT";
            default: return "Tidak diketahui";
        }
    }
}
