package com.example.myapplication.model;

public class MstGradeStickData {
    private int idGradeStick;
    private String namaGradeStick;

    public MstGradeStickData(int idGradeStick, String namaGradeStick) {
        this.idGradeStick = idGradeStick;
        this.namaGradeStick = namaGradeStick;
    }

    public int getIdGradeStick() {
        return idGradeStick;
    }

    public void setIdGradeStick(int idGradeStick) {
        this.idGradeStick = idGradeStick;
    }

    public String getNamaGradeStick() {
        return namaGradeStick;
    }

    public void setNamaGradeStick(String namaGradeStick) {
        this.namaGradeStick = namaGradeStick;
    }

    @Override
    public String toString() {
        return namaGradeStick; // supaya Spinner tampilkan nama grade
    }
}
