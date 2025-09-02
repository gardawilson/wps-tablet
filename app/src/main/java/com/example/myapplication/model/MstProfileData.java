package com.example.myapplication.model;

public class MstProfileData {
    private String namaProfile;
    private String idFJProfile;

    public MstProfileData(String namaProfile, String idFJProfile) {
        this.namaProfile = namaProfile;
        this.idFJProfile = idFJProfile;
    }

    public String getNamaProfile() {
        return namaProfile;
    }

    public String getIdFJProfile() {
        return idFJProfile;
    }

    @Override
    public String toString() {
        return namaProfile; // supaya tampil di Spinner
    }
}
