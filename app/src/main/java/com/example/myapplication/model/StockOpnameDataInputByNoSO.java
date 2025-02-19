package com.example.myapplication.model;

import java.util.Objects;

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

    // Override equals() and hashCode() for proper comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockOpnameDataInputByNoSO that = (StockOpnameDataInputByNoSO) o;
        return Objects.equals(noLabelInput, that.noLabelInput) &&
                Objects.equals(idLokasiInput, that.idLokasiInput) &&
                Objects.equals(userIDInput, that.userIDInput);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noLabelInput, idLokasiInput, userIDInput);
    }
}
