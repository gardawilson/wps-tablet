package com.example.myapplication.model;

import com.example.myapplication.utils.DateTimeUtils;

public class PlanningMesinData {
    private int idMesin;
    private String namaMesin; // ambil dari tabel MstMesin via JOIN
    private String tanggal;
    private int planningJamKerja;

    public PlanningMesinData(int idMesin, String namaMesin, String tanggal, int planningJamKerja) {
        this.idMesin = idMesin;
        this.namaMesin = namaMesin;
        this.tanggal = DateTimeUtils.formatDate(tanggal);
        this.planningJamKerja = planningJamKerja;
    }

    public int getIdMesin() {
        return idMesin;
    }

    public String getNamaMesin() {
        return namaMesin;
    }

    public String getTanggal() {
        return tanggal;
    }

    public int getPlanningJamKerja() {
        return planningJamKerja;
    }

    @Override
    public String toString() {
        return idMesin + " | " + namaMesin + " | " + tanggal + " | " + planningJamKerja;
    }
}
