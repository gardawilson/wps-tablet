package com.example.myapplication.model;

public class StockOpnameAscendData {
    private String noSO;
    private String itemID;
    private String itemCode;
    private String itemName;
    private double pcs;         // tetap primitive (selalu ada nilai dari master)
    private Double qtyFound;    // pakai wrapper Double → bisa null
    private double qtyUsage;    // tetap primitive
    private String usageRemark;
    private boolean isUpdateUsage; // tetap primitive

    public StockOpnameAscendData(String noSO, String itemID, String itemCode, String itemName,
                                 double pcs, Double qtyFound, double qtyUsage,
                                 String usageRemark, boolean isUpdateUsage) {
        this.noSO = noSO;
        this.itemID = itemID;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.pcs = pcs;
        this.qtyFound = qtyFound;
        this.qtyUsage = qtyUsage;
        this.usageRemark = usageRemark;
        this.isUpdateUsage = isUpdateUsage;
    }

    // Overload constructor kalau belum ada hasil opname
    public StockOpnameAscendData(String noSO, String itemID, String itemCode, String itemName,
                                 double pcs) {
        this(noSO, itemID, itemCode, itemName, pcs, null, 0.0, "", false);
    }

    public String getNoSO() {
        return noSO;
    }

    public String getItemID() {
        return itemID;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public double getPcs() {
        return pcs;
    }

    public Double getQtyFound() {
        return qtyFound;
    }

    public void setQtyFound(Double qtyFound) {
        this.qtyFound = qtyFound;
    }

    public double getQtyUsage() {
        return qtyUsage;
    }

    public void setQtyUsage(double qtyUsage) {
        this.qtyUsage = qtyUsage;
    }

    public String getUsageRemark() {
        return usageRemark;
    }

    public void setUsageRemark(String usageRemark) {
        this.usageRemark = usageRemark;
    }

    public boolean isUpdateUsage() {
        return isUpdateUsage;
    }

    public void setUpdateUsage(boolean updateUsage) {
        isUpdateUsage = updateUsage;
    }

    @Override
    public String toString() {
        return noSO + " | " + itemID + " | " + itemName + " | " + pcs +
                " | Fisik: " + (qtyFound != null ? qtyFound : "NULL") +
                " | Usage: " + qtyUsage +
                " | Remark: " + usageRemark +
                " | UpdateUsage: " + isUpdateUsage;
    }
}
