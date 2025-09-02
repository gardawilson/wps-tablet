package com.example.myapplication.model;

public class GradeDetailData {
    private int gradeId;
    private String gradeName;
    private String jumlah;
    private int rowId;
    private static int nextId = 1;

    public GradeDetailData(int gradeId, String gradeName, String jumlah) {
        this.gradeId = gradeId;
        this.gradeName = gradeName;
        this.jumlah = jumlah;
        this.rowId = nextId++;
    }

    // Getter & Setter
    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public int getRowId() {
        return rowId;
    }
}