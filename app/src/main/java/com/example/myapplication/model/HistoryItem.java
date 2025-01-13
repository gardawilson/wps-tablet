package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.List;

public class HistoryItem {
    private String label; // Jika ini grup, label bisa kosong/null
    private String labelCount; // Jika ini grup, labelCount bisa menjadi total semua label
    private String dateTimeSaved; // Tanggal/waktu
    private List<HistoryItem> items; // Daftar item di dalam grup

    // Constructor untuk item individu
    public HistoryItem(String label, String labelCount, String dateTimeSaved) {
        this.label = label;
        this.labelCount = labelCount;
        this.dateTimeSaved = dateTimeSaved;
        this.items = new ArrayList<>();
    }

    // Constructor untuk grup
    public HistoryItem(String dateTimeSaved) {
        this.dateTimeSaved = dateTimeSaved;
        this.items = new ArrayList<>();
    }

    // Getter dan Setter
    public String getLabel() {
        return label;
    }

    public String getLabelCount() {
        return labelCount;
    }

    public String getDateTimeSaved() {
        return dateTimeSaved;
    }

    public List<HistoryItem> getItems() {
        return items;
    }

    public void addItem(HistoryItem item) {
        items.add(item);
    }

    public int getTotalCount() {
        return items.stream()
                .mapToInt(item -> Integer.parseInt(item.getLabelCount()))
                .sum();
    }
}
