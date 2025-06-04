package com.example.myapplication.model;

public class SawmillData {
    private String noSTSawmill;
    private String tglSawmill;
    private String noKayuBulat;
    private String noMeja;
    private String operator;
    private int idSawmillSpecialCondition;
    private String balokTerpakai;
    private String jamKerja;
    private int jlhBatangRajang;
    private String hourMeter;
    private String remark;

    public SawmillData(String noSTSawmill, String tglSawmill, String noKayuBulat, String noMeja, String operator,
                       int idSawmillSpecialCondition, String balokTerpakai, String jamKerja, int jlhBatangRajang,
                       String hourMeter, String remark) {
        this.noSTSawmill = noSTSawmill;
        this.tglSawmill = tglSawmill;
        this.noKayuBulat = noKayuBulat;
        this.noMeja = noMeja;
        this.operator = operator;
        this.idSawmillSpecialCondition = idSawmillSpecialCondition;
        this.balokTerpakai = balokTerpakai;
        this.jamKerja = jamKerja;
        this.jlhBatangRajang = jlhBatangRajang;
        this.hourMeter = hourMeter;
        this.remark = remark;
    }

    public String getNoSTSawmill() {
        return noSTSawmill;
    }

    public void setNoSTSawmill(String noSTSawmill) {
        this.noSTSawmill = noSTSawmill;
    }

    public String getTglSawmill() {
        return tglSawmill;
    }

    public void setTglSawmill(String tglSawmill) {
        this.tglSawmill = tglSawmill;
    }

    public String getNoKayuBulat() {
        return noKayuBulat;
    }

    public void setNoKayuBulat(String noKayuBulat) {
        this.noKayuBulat = noKayuBulat;
    }

    public String getNoMeja() {
        return noMeja;
    }

    public void setNoMeja(String noMeja) {
        this.noMeja = noMeja;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getIdSawmillSpecialCondition() {
        return idSawmillSpecialCondition;
    }

    public void setIdSawmillSpecialCondition(int idSawmillSpecialCondition) {
        this.idSawmillSpecialCondition = idSawmillSpecialCondition;
    }

    public String getBalokTerpakai() {
        return balokTerpakai;
    }

    public void setBalokTerpakai(String balokTerpakai) {
        this.balokTerpakai = balokTerpakai;
    }

    public String getJamKerja() {
        return jamKerja;
    }

    public void setJamKerja(String jamKerja) {
        this.jamKerja = jamKerja;
    }

    public int getJlhBatangRajang() {
        return jlhBatangRajang;
    }

    public void setJlhBatangRajang(int jlhBatangRajang) {
        this.jlhBatangRajang = jlhBatangRajang;
    }

    public String getHourMeter() {
        return hourMeter;
    }

    public void setHourMeter(String hourMeter) {
        this.hourMeter = hourMeter;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
