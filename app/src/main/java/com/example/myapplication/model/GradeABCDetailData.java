package com.example.myapplication.model;

public class GradeABCDetailData {
    private String noGradeABC;
    private int idGradeABC;
    private String namaGrade;   // ⬅️ baru
    private int jmlhBatang;

    public GradeABCDetailData(String noGradeABC, int idGradeABC, String namaGrade, int jmlhBatang) {
        this.noGradeABC = noGradeABC;
        this.idGradeABC = idGradeABC;
        this.namaGrade = namaGrade;
        this.jmlhBatang = jmlhBatang;
    }

    public String getNoGradeABC() { return noGradeABC; }
    public int getIdGradeABC() { return idGradeABC; }
    public String getNamaGrade() { return namaGrade; }     // ⬅️ baru
    public int getJmlhBatang() { return jmlhBatang; }

    @Override
    public String toString() {
        return noGradeABC + " | " + idGradeABC + " - " + (namaGrade == null ? "-" : namaGrade)
                + " | " + jmlhBatang;
    }
}
