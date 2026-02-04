package com.example.myapplication.utils;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterMinIntNoLeadingZero implements InputFilter {

    private final int minValue;

    public InputFilterMinIntNoLeadingZero(int minValue) {
        this.minValue = minValue;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        try {
            String inserted = source.subSequence(start, end).toString();

            // allow delete/backspace
            if (inserted.isEmpty()) return null;

            // build new string result
            String newValue = dest.subSequence(0, dstart).toString()
                    + inserted
                    + dest.subSequence(dend, dest.length()).toString();

            // allow user still typing
            if (newValue.isEmpty()) return null;

            // reject non digit
            if (!newValue.matches("\\d+")) return "";

            // ✅ prevent leading zero like "08", "0007"
            if (newValue.length() > 1 && newValue.startsWith("0")) return "";

            int value = Integer.parseInt(newValue);

            // ✅ min check
            if (value < minValue) return "";
        } catch (Exception ignored) {
            return "";
        }
        return null;
    }
}
