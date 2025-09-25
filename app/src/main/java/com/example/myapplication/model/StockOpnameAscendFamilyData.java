package com.example.myapplication.model;

public class StockOpnameAscendFamilyData {
    private String noSO;
    private String categoryID;
    private String familyID;
    private String familyName; // 🔑 ambil dari IC_StockFamily
    private int totalItem;
    private int completeItem; // 🔹 baru

    public StockOpnameAscendFamilyData(String noSO, String categoryID, String familyID, String familyName, int totalItem, int completeItem) {
        this.noSO = noSO;
        this.categoryID = categoryID;
        this.familyID = familyID;
        this.familyName = familyName;
        this.totalItem = totalItem;
        this.completeItem = completeItem;
    }

    public String getNoSO() {
        return noSO;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public String getFamilyID() {
        return familyID;
    }

    public String getFamilyName() {
        return familyName;
    }

    public int getTotalItem() {return totalItem; }

    public int getCompleteItem() {return completeItem; }


    @Override
    public String toString() {
        return "NoSO: " + noSO +
                " | CategoryID: " + categoryID +
                " | FamilyID: " + familyID +
                " | FamilyName: " + familyName;
    }
}
