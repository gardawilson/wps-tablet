package com.example.myapplication.utils;

import android.app.Activity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.myapplication.R; // pastikan ini sesuai dengan package app kamu

public class ViewUtils {

    // Warna default dan selected
    private static final int COLOR_EVEN = R.color.background_cream;
    private static final int COLOR_ODD = R.color.white;
    private static final int COLOR_SELECTED = R.color.primary;
    private static final int TEXT_DEFAULT = R.color.black;
    private static final int TEXT_SELECTED = R.color.white;

    public static void handleRowSelection(Activity activity,
                                          TableRow newRow,
                                          int rowIndex,
                                          TableRow previousSelectedRow) {

        // Reset row sebelumnya
        if (previousSelectedRow != null) {
            int prevIndex = (int) previousSelectedRow.getTag();
            int bgColor = (prevIndex % 2 == 0) ? COLOR_EVEN : COLOR_ODD;
            previousSelectedRow.setBackgroundColor(ContextCompat.getColor(activity, bgColor));

            // Reset text color
            for (int i = 0; i < previousSelectedRow.getChildCount(); i++) {
                View child = previousSelectedRow.getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setTextColor(ContextCompat.getColor(activity, TEXT_DEFAULT));
                }
            }
        }

        // Tandai dan ubah warna row baru
        newRow.setBackgroundColor(ContextCompat.getColor(activity, COLOR_SELECTED));

        for (int i = 0; i < newRow.getChildCount(); i++) {
            View child = newRow.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(ContextCompat.getColor(activity, TEXT_SELECTED));
            }
        }
    }

    public static void resetRowSelection(Activity activity,
                                         TableRow row,
                                         int rowIndex) {

        int bgColor = (rowIndex % 2 == 0) ? COLOR_EVEN : COLOR_ODD;
        row.setBackgroundColor(ContextCompat.getColor(activity, bgColor));

        for (int i = 0; i < row.getChildCount(); i++) {
            View child = row.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(ContextCompat.getColor(activity, TEXT_DEFAULT));
            }
        }
    }
}
