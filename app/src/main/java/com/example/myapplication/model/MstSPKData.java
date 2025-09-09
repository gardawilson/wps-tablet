package com.example.myapplication.model;

import com.example.myapplication.utils.DateTimeUtils;

public class MstSPKData {
    private String noSPK;
    private String tanggal;
    private String noContract;
    private int idBuyer;
    private String buyerName;
    private String tujuan;
    private int enable;
    private int lockDimensionS4S;
    private int lockDimensionFJ;
    private int lockDimensionMLD;
    private int lockDimensionLMT;
    private int lockDimensionCCA;
    private int lockDimensionSAND;
    private int lockDimensionBJ;
    private int lockDimensionST;
    private int unlockGradeS4S;
    private int unlockGradeFJ;
    private int unlockGradeMLD;
    private int unlockGradeLMT;
    private int unlockGradeCCA;
    private int unlockGradeSAND;
    private int unlockGradeBJ;
    private int unlockGradeST;

    // Constructor
    public MstSPKData(String noSPK, String tanggal, String noContract, int idBuyer, String buyerName, String tujuan,
                      int enable, int lockDimensionS4S, int lockDimensionFJ, int lockDimensionMLD,
                      int lockDimensionLMT, int lockDimensionCCA, int lockDimensionSAND,
                      int lockDimensionBJ, int lockDimensionST, int unlockGradeS4S, int unlockGradeFJ,
                      int unlockGradeMLD, int unlockGradeLMT, int unlockGradeCCA, int unlockGradeSAND,
                      int unlockGradeBJ, int unlockGradeST) {
        this.noSPK = noSPK;
        this.tanggal = DateTimeUtils.formatDate(tanggal);
        this.noContract = noContract;
        this.idBuyer = idBuyer;
        this.buyerName = buyerName;
        this.tujuan = tujuan;
        this.enable = enable;
        this.lockDimensionS4S = lockDimensionS4S;
        this.lockDimensionFJ = lockDimensionFJ;
        this.lockDimensionMLD = lockDimensionMLD;
        this.lockDimensionLMT = lockDimensionLMT;
        this.lockDimensionCCA = lockDimensionCCA;
        this.lockDimensionSAND = lockDimensionSAND;
        this.lockDimensionBJ = lockDimensionBJ;
        this.lockDimensionST = lockDimensionST;
        this.unlockGradeS4S = unlockGradeS4S;
        this.unlockGradeFJ = unlockGradeFJ;
        this.unlockGradeMLD = unlockGradeMLD;
        this.unlockGradeLMT = unlockGradeLMT;
        this.unlockGradeCCA = unlockGradeCCA;
        this.unlockGradeSAND = unlockGradeSAND;
        this.unlockGradeBJ = unlockGradeBJ;
        this.unlockGradeST = unlockGradeST;
    }

    // Getter
    public String getNoSPK() { return noSPK; }
    public String getTanggal() { return tanggal; }
    public String getNoContract() { return noContract; }
    public int getIdBuyer() { return idBuyer; }
    public String getBuyerName() { return buyerName; }
    public String getTujuan() { return tujuan; }
    public int getEnable() { return enable; }
    public int getLockDimensionS4S() { return lockDimensionS4S; }
    public int getLockDimensionFJ() { return lockDimensionFJ; }
    public int getLockDimensionMLD() { return lockDimensionMLD; }
    public int getLockDimensionLMT() { return lockDimensionLMT; }
    public int getLockDimensionCCA() { return lockDimensionCCA; }
    public int getLockDimensionSAND() { return lockDimensionSAND; }
    public int getLockDimensionBJ() { return lockDimensionBJ; }
    public int getLockDimensionST() { return lockDimensionST; }
    public int getUnlockGradeS4S() { return unlockGradeS4S; }
    public int getUnlockGradeFJ() { return unlockGradeFJ; }
    public int getUnlockGradeMLD() { return unlockGradeMLD; }
    public int getUnlockGradeLMT() { return unlockGradeLMT; }
    public int getUnlockGradeCCA() { return unlockGradeCCA; }
    public int getUnlockGradeSAND() { return unlockGradeSAND; }
    public int getUnlockGradeBJ() { return unlockGradeBJ; }
    public int getUnlockGradeST() { return unlockGradeST; }

    // --- Helpers biar UI rapi ---
    public boolean isEnabled() {
        return enable == 1;
    }

    public String getEnableLabel() {
        return isEnabled() ? "AKTIF" : "NONAKTIF";
    }

    // --- Setter untuk toggle di UI (wajib) ---
    public void setEnable(int enable) {
        this.enable = enable;
    }

    // Opsional: toggle langsung
    public void toggleEnable() {
        this.enable = isEnabled() ? 0 : 1;
    }

}