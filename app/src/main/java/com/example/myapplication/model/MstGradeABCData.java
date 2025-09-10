package com.example.myapplication.model;

public class MstGradeABCData {
    private int idGradeABC;
    private String namaGrade;

    public MstGradeABCData(int idGradeABC, String namaGrade) {
        this.idGradeABC = idGradeABC;
        this.namaGrade = namaGrade;
    }

    public int getIdGradeABC() {
        return idGradeABC;
    }

    public String getNamaGrade() {
        return namaGrade;
    }

    @Override
    public String toString() {
        // Supaya Spinner/ArrayAdapter tampil nama grade
        return namaGrade;
    }
}
