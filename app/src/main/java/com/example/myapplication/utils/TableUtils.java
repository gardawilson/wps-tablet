package com.example.myapplication.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
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

    public static EditText createEditTextNumber(Context context, String value, float weight) {
        EditText editText = new EditText(context);
        editText.setText(value);
        editText.setHint("0");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setGravity(Gravity.CENTER);
        editText.setBackground(null); // hilangkan garis bawah default EditText
        editText.setPadding(8, 15, 8, 15);

        // Atur ukuran font (misalnya 14sp)
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        // 🔑 Set Bold
        editText.setTypeface(editText.getTypeface(), Typeface.BOLD);

        editText.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                weight
        ));

        // 🔑 Paksa hanya angka & titik desimal
        editText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));

        return editText;
    }



    public static EditText createEditTextText(Context context, String value, float weight) {
        EditText editText = new EditText(context);
        editText.setText(value != null ? value : "");
        editText.setHint("Remark...");
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        editText.setBackground(null); // hilangkan underline default
        editText.setPadding(8, 15, 8, 15);

        // Atur ukuran font sama dengan angka biar konsisten
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        // 🔑 Set Bold
        editText.setTypeface(editText.getTypeface(), Typeface.BOLD);

        editText.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                weight
        ));

        return editText;
    }


}