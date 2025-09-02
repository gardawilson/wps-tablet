package com.example.myapplication.model;

public class MstGradeData {
    private int idGrade;
    private String namaGrade;

    public MstGradeData(int idGrade, String namaGrade) {
        this.idGrade = idGrade;
        this.namaGrade = namaGrade;
    }

    public int getIdGrade() {
        return idGrade;
    }

    public String getNamaGrade() {
        return namaGrade;
    }

    @Override
    public String toString() {
        return namaGrade;
    }
}