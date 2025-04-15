package com.example.myapplication.model;

public class SupplierData {
    private Integer idSupplier;  // Ubah menjadi Integer
    private String nmSupplier;

    // Konstruktor
    public SupplierData(Integer idSupplier, String nmSupplier) {
        this.idSupplier = idSupplier;
        this.nmSupplier = nmSupplier;
    }

    // Getter dan Setter
    public Integer getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(Integer idSupplier) {
        this.idSupplier = idSupplier;
    }

    public String getNmSupplier() {
        return nmSupplier;
    }

    public void setNmSupplier(String nmSupplier) {
        this.nmSupplier = nmSupplier;
    }

    @Override
    public String toString() {
        return nmSupplier;  // Tampilkan nama supplier di Spinner
    }
}
