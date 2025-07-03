package com.example.myapplication.model;

public class QcSawmillDetailData {
    private String noQc;
    private int noUrut;
    private String noST;
    private float cuttingTebal;
    private float cuttingLebar;
    private float actualTebal;
    private float actualLebar;
    private float susutTebal;
    private float susutLebar;

    // Constructor
    public QcSawmillDetailData(String noQc, int noUrut, String noST,
                               float cuttingTebal, float cuttingLebar,
                               float actualTebal, float actualLebar,
                               float susutTebal, float susutLebar) {
        this.noQc = noQc;
        this.noUrut = noUrut;
        this.noST = noST;
        this.cuttingTebal = cuttingTebal;
        this.cuttingLebar = cuttingLebar;
        this.actualTebal = actualTebal;
        this.actualLebar = actualLebar;
        this.susutTebal = susutTebal;
        this.susutLebar = susutLebar;
    }

    // Getter dan Setter
    public String getNoQc() {
        return noQc;
    }

    public void setNoQc(String noQc) {
        this.noQc = noQc;
    }

    public int getNoUrut() {
        return noUrut;
    }

    public void setNoUrut(int noUrut) {
        this.noUrut = noUrut;
    }

    public String getNoST() {
        return noST;
    }

    public void setNoST(String noST) {
        this.noST = noST;
    }

    public float getCuttingTebal() {
        return cuttingTebal;
    }

    public void setCuttingTebal(float cuttingTebal) {
        this.cuttingTebal = cuttingTebal;
    }

    public float getCuttingLebar() {
        return cuttingLebar;
    }

    public void setCuttingLebar(float cuttingLebar) {
        this.cuttingLebar = cuttingLebar;
    }

    public float getActualTebal() {
        return actualTebal;
    }

    public void setActualTebal(float actualTebal) {
        this.actualTebal = actualTebal;
    }

    public float getActualLebar() {
        return actualLebar;
    }

    public void setActualLebar(float actualLebar) {
        this.actualLebar = actualLebar;
    }

    public float getSusutTebal() {
        return susutTebal;
    }

    public void setSusutTebal(float susutTebal) {
        this.susutTebal = susutTebal;
    }

    public float getSusutLebar() {
        return susutLebar;
    }

    public void setSusutLebar(float susutLebar) {
        this.susutLebar = susutLebar;
    }
}
