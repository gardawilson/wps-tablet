package com.example.myapplication.model;

public class SpecialConditionData {
    private Integer idSawmillSpecialCondition;
    private String condition;

    // Konstruktor
    public SpecialConditionData(Integer idSawmillSpecialCondition, String condition) {
        this.idSawmillSpecialCondition = idSawmillSpecialCondition;
        this.condition = condition;
    }

    // Getter dan Setter
    public Integer getIdSawmillSpecialCondition() {
        return idSawmillSpecialCondition;
    }

    public void setIdSawmillSpecialCondition(Integer idSawmillSpecialCondition) {
        this.idSawmillSpecialCondition = idSawmillSpecialCondition;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return condition; // Tampilkan kondisi di Spinner atau List
    }
}
