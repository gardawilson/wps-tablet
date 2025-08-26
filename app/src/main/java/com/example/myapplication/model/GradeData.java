package com.example.myapplication.model;

public class GradeData {
    private int idGrade;
    private String namaGrade;

    public GradeData(int idGrade, String namaGrade) {
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