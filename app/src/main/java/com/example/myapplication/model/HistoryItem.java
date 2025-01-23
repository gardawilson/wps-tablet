package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.List;

public class HistoryItem {
    private String dateTimeSaved;
    private String label;
    private int labelCount;

    private int totalS4S;
    private int totalST;
    private int totalMoulding;
    private int totalFJ;
    private int totalCrossCut;
    private int totalReproses;
    private int totalLaminating;
    private int totalSanding;
    private int totalPacking;
    private int totalAllLabels;

    private List<HistoryItem> items; // Tambahkan properti untuk daftar item detail

    // Constructor untuk item individual
    public HistoryItem(String label, String labelCount, String dateTimeSaved) {
        this.label = label;
        this.labelCount = Integer.parseInt(labelCount);
        this.dateTimeSaved = dateTimeSaved;
        this.items = new ArrayList<>();
    }

    // Constructor untuk grup
    public HistoryItem(String dateTimeSaved) {
        this.dateTimeSaved = dateTimeSaved;
        this.items = new ArrayList<>();
    }

    // Getters dan setters
    public String getDateTimeSaved() {
        return dateTimeSaved;
    }

    public void setDateTimeSaved(String dateTimeSaved) {
        this.dateTimeSaved = dateTimeSaved;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getLabelCount() {
        return labelCount;
    }

    public void setLabelCount(int labelCount) {
        this.labelCount = labelCount;
    }

    public int getTotalS4S() {
        return totalS4S;
    }

    public void setTotalS4S(int totalS4S) {
        this.totalS4S = totalS4S;
    }

    public int getTotalST() {
        return totalST;
    }

    public void setTotalST(int totalST) {
        this.totalST = totalST;
    }

    public int getTotalMoulding() {
        return totalMoulding;
    }

    public void setTotalMoulding(int totalMoulding) {
        this.totalMoulding = totalMoulding;
    }

    public int getTotalFJ() {
        return totalFJ;
    }

    public void setTotalFJ(int totalFJ) {
        this.totalFJ = totalFJ;
    }

    public int getTotalCrossCut() {
        return totalCrossCut;
    }

    public void setTotalCrossCut(int totalCrossCut) {
        this.totalCrossCut = totalCrossCut;
    }

    public int getTotalReproses() {
        return totalReproses;
    }

    public void setTotalReproses(int totalReproses) { this.totalReproses = totalReproses; }

    public int getTotalLaminating() {
        return totalLaminating;
    }

    public void setTotalLaminating(int totalLaminating) {
        this.totalLaminating = totalLaminating;
    }

    public int getTotalSanding() {
        return totalSanding;
    }

    public void setTotalSanding(int totalSanding) {
        this.totalSanding = totalSanding;
    }

    public int getTotalPacking() {
        return totalPacking;
    }

    public void setTotalPacking(int totalPacking) { this.totalPacking = totalPacking; }

    public int getTotalAllLabels() {
        return totalAllLabels;
    }

    public void setTotalAllLabels(int totalAllLabels) {
        this.totalAllLabels = totalAllLabels;
    }

    // Tambahkan item detail ke grup
    public void addItem(HistoryItem item) {
        this.items.add(item);
    }

    // Dapatkan daftar item detail
    public List<HistoryItem> getItems() {
        return items;
    }
}
