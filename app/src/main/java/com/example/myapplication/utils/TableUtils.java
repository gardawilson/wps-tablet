package com.example.myapplication.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class TableUtils {

    public static TextView createTextView(Context context, String text, float weight) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setPadding(8, 15, 8, 15);
        textView.setGravity(Gravity.CENTER);

        textView.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                weight
        ));

        return textView;
    }

    public static View createDivider(Context context) {
        View divider = new View(context);
        divider.setBackgroundColor(Color.GRAY);

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                1,
                TableRow.LayoutParams.MATCH_PARENT
        );
        divider.setLayoutParams(params);

        return divider;
    }

    public static void setTextColor(Context context, TableRow row, int colorRes) {
        for (int i = 0; i < row.getChildCount(); i++) {
            View child = row.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(ContextCompat.getColor(context, colorRes));
            }
        }
    }

    public static void resetTextColor(Context context, TableRow row) {
        setTextColor(context, row, android.R.color.black); // default ke hitam
    }
}