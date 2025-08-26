package com.example.myapplication.model;

public class ProfileData {
    private String namaProfile;
    private String idFJProfile;

    public ProfileData(String namaProfile, String idFJProfile) {
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
