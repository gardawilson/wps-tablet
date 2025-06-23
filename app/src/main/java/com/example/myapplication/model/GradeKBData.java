package com.example.myapplication.model;

public class GradeKBData {
    private Integer idGradeKB;
    private String namaGrade;

    public GradeKBData(Integer idGradeKB, String namaGrade) {
        this.idGradeKB = idGradeKB;
        this.namaGrade = namaGrade;
    }

    public Integer getIdGradeKB() {
        return idGradeKB;
    }

    public String getNamaGrade() {
        return namaGrade;
    }

    @Override
    public String toString() {
        return namaGrade; // Ini yang akan ditampilkan di Spinner
    }
}
