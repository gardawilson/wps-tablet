package com.example.myapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BarChartView extends View {

    private final List<String> labels = new ArrayList<>();
    private final List<Float> values = new ArrayList<>();
    private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint valuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int barColor = Color.parseColor("#8FA888");

    public BarChartView(Context context) {
        super(context);
        init();
    }

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        barPaint.setColor(barColor);
        labelPaint.setColor(Color.parseColor("#4A371F"));
        labelPaint.setTextAlign(Paint.Align.CENTER);
        labelPaint.setTextSize(spToPx(11));
        valuePaint.setColor(Color.parseColor("#4A371F"));
        valuePaint.setTextAlign(Paint.Align.CENTER);
        valuePaint.setTextSize(spToPx(12));
        valuePaint.setFakeBoldText(true);
    }

    public void setBarColor(int color) {
        this.barColor = color;
        barPaint.setColor(color);
        invalidate();
    }

    public void setData(List<String> newLabels, List<Float> newValues) {
        labels.clear();
        labels.addAll(newLabels);
        values.clear();
        values.addAll(newValues);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (values.isEmpty()) {
            return;
        }

        float width = getWidth();
        float height = getHeight();
        float labelHeight = spToPx(14);
        float valueHeight = spToPx(16);
        float chartTop = valueHeight;
        float chartBottom = height - labelHeight;
        float chartHeight = chartBottom - chartTop;

        float maxValue = 0f;
        for (float value : values) {
            maxValue = Math.max(maxValue, value);
        }
        if (maxValue <= 0f) {
            maxValue = 1f;
        }

        int count = values.size();
        float slotWidth = width / count;
        float barWidth = slotWidth * 0.45f;
        float cornerRadius = dpToPx(6);

        for (int i = 0; i < count; i++) {
            float value = values.get(i);
            float barHeight = chartHeight * (value / maxValue);
            float centerX = slotWidth * i + slotWidth / 2f;
            float top = chartBottom - barHeight;

            RectF bar = new RectF(centerX - barWidth / 2f, top, centerX + barWidth / 2f, chartBottom);
            canvas.drawRoundRect(bar, cornerRadius, cornerRadius, barPaint);

            canvas.drawText(formatValue(value), centerX, top - dpToPx(6), valuePaint);
            canvas.drawText(labels.get(i), centerX, height - dpToPx(2), labelPaint);
        }
    }

    private String formatValue(float value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    private float dpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    private float spToPx(float sp) {
        return sp * getResources().getDisplayMetrics().scaledDensity;
    }
}
