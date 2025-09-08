package com.example.myapplication.model;

public class MstJenisKendaraanData {
    private int idJenisKendaraan;
    private String type;
    private String model;
    private boolean enable;

    public MstJenisKendaraanData(int idJenisKendaraan, String type, String model, boolean enable) {
        this.idJenisKendaraan = idJenisKendaraan;
        this.type = type;
        this.model = model;
        this.enable = enable;
    }

    public int getIdJenisKendaraan() {
        return idJenisKendaraan;
    }

    public String getType() {
        return type;
    }

    public String getModel() {
        return model;
    }

    public boolean isEnable() {
        return enable;
    }

    @Override
    public String toString() {
        // Agar Spinner menampilkan kombinasi yang informatif
        if (model == null || model.trim().isEmpty()) return type;
        return type + " - " + model;
    }
}
