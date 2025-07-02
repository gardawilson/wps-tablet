package com.example.myapplication.model;

public class OutputDataST {
    private String noST;
    private boolean hasBeenPrinted;

    public OutputDataST(String noST, boolean hasBeenPrinted) {
        this.noST = noST;
        this.hasBeenPrinted = hasBeenPrinted;
    }

    public String getNoST() {
        return noST;
    }

    public boolean isHasBeenPrinted() {
        return hasBeenPrinted;
    }
}
