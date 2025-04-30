package com.example.myapplication.model;

public class CustomerData {
    private Integer idCustomer;  // Ubah menjadi Integer
    private String namaCustomer;

    // Konstruktor
    public CustomerData(Integer idCustomer, String namaCustomer) {
        this.idCustomer = idCustomer;
        this.namaCustomer = namaCustomer;
    }

    // Getter dan Setter
    public Integer getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Integer idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getNamaCustomer() {
        return namaCustomer;
    }

    public void setNamaCustomer(String namaCustomer) {
        this.namaCustomer = namaCustomer;
    }

    @Override
    public String toString() {
        return namaCustomer;  // Tampilkan nama supplier di Spinner
    }
}
