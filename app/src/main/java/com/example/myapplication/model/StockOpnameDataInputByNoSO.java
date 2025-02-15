package com.example.myapplication.model;

public class StockOpnameDataInputByNoSO {
    private String noLabelInput;
    private String idLokasiInput;
    private String userIDInput;

    public StockOpnameDataInputByNoSO(String noLabelInput, String idLokasiInput, String userIDInput) {
        this.noLabelInput = noLabelInput;
        this.idLokasiInput = idLokasiInput;
        this.userIDInput = userIDInput;
    }

    // Getter and Setter
    public String getNoLabelInput() {
        return noLabelInput;
    }

    public void setNoLabelInput(String noLabel) {
        this.noLabelInput = noLabel;
    }

    public String getIdLokasiInput() {
        return idLokasiInput;
    }

    public void setIdLokasiInput(String idLokasi) {
        this.idLokasiInput = idLokasi;
    }

    public String getUserIdInput() {
        return userIDInput;
    }

    public void setUserIdInput(String userId) {
        this.userIDInput = userId;
    }
}
