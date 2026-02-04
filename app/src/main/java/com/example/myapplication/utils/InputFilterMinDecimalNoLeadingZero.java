package com.example.myapplication.utils;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterMinDecimalNoLeadingZero implements InputFilter {

    private final double minValue;

    public InputFilterMinDecimalNoLeadingZero(double minValue) {
        this.minValue = minValue;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        try {
            String inserted = source.subSequence(start, end).toString();

            // allow delete/backspace
            if (inserted.isEmpty()) return null;

            String newValue = dest.subSequence(0, dstart).toString()
                    + inserted
                    + dest.subSequence(dend, dest.length()).toString();

            if (newValue.isEmpty()) return null;
            if (newValue.equals(".")) return null;

            // allow digits and dot only
            if (!newValue.matches("\\d*(\\.\\d*)?")) return "";

            // ✅ prevent leading zero in integer-part when more than 1 digit before dot
            // examples rejected: "08", "08.5", "000.2"
            if (newValue.startsWith("0") && newValue.length() > 1 && !newValue.startsWith("0.")) {
                return "";
            }

            double value = Double.parseDouble(newValue);

            if (value < minValue) return "";
        } catch (Exception ignored) {
            return "";
        }
        return null;
    }
}
