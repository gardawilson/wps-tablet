package com.example.myapplication.model;

public class MstOperatorData {
    private Integer idOperator;
    private String namaOperator;

    public MstOperatorData(Integer idOperator, String namaOperator) {
        this.idOperator = idOperator;
        this.namaOperator = namaOperator;
    }

    public Integer getIdOperator() {
        return idOperator;
    }

    public String getNamaOperator() {
        return namaOperator;
    }

    @Override
    public String toString() {
        return namaOperator;
    }
}
