package com.example.myapplication.model;

public class STPembelianDataReject {

    private String noUrut;
    private float tebal;
    private float lebar;
    private float panjang;
    private int jmlhBatang;
    private int idUOMTblLebar;
    private int idUOMPanjang;
    private float ton;

    // Constructor
    public STPembelianDataReject(String noUrut, float tebal, float lebar, float panjang, int jmlhBatang, int idUOMTblLebar, int idUOMPanjang, float ton) {
        this.noUrut = noUrut;
        this.tebal = tebal;
        this.lebar = lebar;
        this.panjang = panjang;
        this.jmlhBatang = jmlhBatang;
        this.idUOMTblLebar = idUOMTblLebar;
        this.idUOMPanjang = idUOMPanjang;
        this.ton = ton;
    }

    // Getter methods with correct return types
    public String getNoUrut() {
        return noUrut;
    }

    public float getTebal() {
        return tebal;
    }

    public float getLebar() {
        return lebar;
    }

    public float getPanjang() {
        return panjang;
    }

    public int getJmlhBatang() {
        return jmlhBatang;
    }

    public int getIdUOMTblLebar() {
        return idUOMTblLebar;
    }

    public int getIdUOMPanjang() {
        return idUOMPanjang;
    }

    public float getTon() {
        return ton;
    }

    // Setter methods (optional, if you need to modify data outside this class)
    public void setNoUrut(String noUrut) {
        this.noUrut = noUrut;
    }

    public void setTebal(float tebal) {
        this.tebal = tebal;
    }

    public void setLebar(float lebar) {
        this.lebar = lebar;
    }

    public void setPanjang(float panjang) {
        this.panjang = panjang;
    }

    public void setJmlhBatang(int jmlhBatang) {
        this.jmlhBatang = jmlhBatang;
    }

    public void setIdUOMTblLebar(int idUOMTblLebar) {
        this.idUOMTblLebar = idUOMTblLebar;
    }

    public void setIdUOMPanjang(int idUOMPanjang) {
        this.idUOMPanjang = idUOMPanjang;
    }

    public void setTon(float ton) {
        this.ton = ton;
    }
}
