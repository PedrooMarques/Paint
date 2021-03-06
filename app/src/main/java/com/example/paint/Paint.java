package com.example.paint;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class Paint {

    private boolean paintAntiAlias;
    private float paintStrokeWidth;
    private long paintColor;
    private android.graphics.Paint.Style paintStyle;
    private android.graphics.Paint.Join paintStrokeJoin;

    public Paint() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Paint(android.graphics.Paint paint) {
        setPaintColor(paint.getColor());
        setPaintStyle(paint.getStyle());
        setPaintStrokeWidth(paint.getStrokeWidth());
        setPaintAntiAlias(paint.isAntiAlias());
        setPaintStrokeJoin(paint.getStrokeJoin());
    }

    public boolean isPaintAntiAlias() {
        return paintAntiAlias;
    }

    public void setPaintAntiAlias(boolean paintAntiAlias) {
        this.paintAntiAlias = paintAntiAlias;
    }

    public float getPaintStrokeWidth() {
        return paintStrokeWidth;
    }

    public void setPaintStrokeWidth(float paintStrokeWidth) {
        this.paintStrokeWidth = paintStrokeWidth;
    }

    public long getPaintColor() {
        return paintColor;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setPaintColor(long paintColor) {
        this.paintColor = paintColor;
    }

    public android.graphics.Paint.Style getPaintStyle() {
        return paintStyle;
    }

    public void setPaintStyle(android.graphics.Paint.Style paintStyle) {
        this.paintStyle = paintStyle;
    }

    public android.graphics.Paint.Join getPaintStrokeJoin() {
        return paintStrokeJoin;
    }

    public void setPaintStrokeJoin(android.graphics.Paint.Join paintStrokeJoin) {
        this.paintStrokeJoin = paintStrokeJoin;
    }
}
