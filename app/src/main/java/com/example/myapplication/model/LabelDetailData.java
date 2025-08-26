package com.example.myapplication.model;

public class LabelDetailData {
    private String tebal;
    private String lebar;
    private String panjang;
    private String pcs;
    private final int rowId;

    private static int nextId = 1;

    public LabelDetailData(String tebal, String lebar, String panjang, String pcs) {
        this.tebal = tebal;
        this.lebar = lebar;
        this.panjang = panjang;
        this.pcs = pcs;
        this.rowId = nextId++;
    }

    // Getter
    public String getTebal() { return tebal; }
    public String getLebar() { return lebar; }
    public String getPanjang() { return panjang; }
    public String getPcs() { return pcs; }
    public int getRowId() { return rowId; }

    // Setter untuk edit
    public void setTebal(String tebal) { this.tebal = tebal; }
    public void setLebar(String lebar) { this.lebar = lebar; }
    public void setPanjang(String panjang) { this.panjang = panjang; }
    public void setPcs(String pcs) { this.pcs = pcs; }
}
