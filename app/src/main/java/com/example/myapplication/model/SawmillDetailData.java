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

    // ✅ NEW FIELDS
    private String noSPK;
    private int idProdukSPK;
    private String namaProduk;

    public SawmillDetailData(
            int noUrut,
            float tebal,
            float lebar,
            float panjang,
            int pcs,
            boolean isLocal,
            int idUOMTblLebar,
            int idUOMPanjang,
            int isBagusKulit,
            int idGradeKB,
            String namaGrade,
            String noSPK,
            int idProdukSPK,
            String namaProduk
    ) {
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
        this.noSPK = noSPK;
        this.idProdukSPK = idProdukSPK;
        this.namaProduk = namaProduk;
    }

    // ===== GETTERS =====

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
    public String getNoSPK() { return noSPK; }
    public int getIdProdukSPK() { return idProdukSPK; }
    public String getNamaProduk() { return namaProduk; }

    public String getNamaGrade() {
        if (isLocal) return "LOKAL";
        return namaGrade;
    }

    public String getIsBagusKulitLabel() {
        switch (isBagusKulit) {
            case 0: return "-";
            case 1: return "BAGUS";
            case 2: return "KULIT";
            default: return "-";
        }
    }

    // ===== SETTERS =====

    public void setTebal(float tebal) { this.tebal = tebal; }
    public void setLebar(float lebar) { this.lebar = lebar; }
    public void setPanjang(float panjang) { this.panjang = panjang; }
    public void setPcs(int pcs) { this.pcs = pcs; }
    public void setLocal(boolean local) { isLocal = local; }
    public void setIdUOMTblLebar(int idUOMTblLebar) { this.idUOMTblLebar = idUOMTblLebar; }
    public void setIdUOMPanjang(int idUOMPanjang) { this.idUOMPanjang = idUOMPanjang; }
    public void setIsBagusKulit(int isBagusKulit) { this.isBagusKulit = isBagusKulit; }
    public void setIdGradeKB(int idGradeKB) { this.idGradeKB = idGradeKB; }
    public void setNamaGrade(String namaGrade) { this.namaGrade = namaGrade; }
    public void setNoSPK(String noSPK) { this.noSPK = noSPK; }
    public void setIdProdukSPK(int idProdukSPK) { this.idProdukSPK = idProdukSPK; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }
}
