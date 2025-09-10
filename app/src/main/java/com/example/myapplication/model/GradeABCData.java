package com.example.myapplication.model;

import com.example.myapplication.utils.DateTimeUtils;

public class GradeABCData {
    private String noGradeABC;
    private String tanggal;
    private String keterangan;

    public GradeABCData(String noGradeABC, String tanggal, String keterangan) {
        this.noGradeABC = noGradeABC;
        this.tanggal = DateTimeUtils.formatDate(tanggal);
        this.keterangan = keterangan;
    }

    public String getNoGradeABC() {
        return noGradeABC;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    @Override
    public String toString() {
        return noGradeABC + " | " + tanggal + " | " + keterangan;
    }
}
