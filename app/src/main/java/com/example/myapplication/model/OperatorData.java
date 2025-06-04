package com.example.myapplication.model;

public class OperatorData {
    private Integer idOperator;
    private String namaOperator;

    public OperatorData(Integer idOperator, String namaOperator) {
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
