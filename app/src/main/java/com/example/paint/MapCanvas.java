package com.example.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.location.Location;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MapCanvas extends View {

    private final Paint paint = new Paint();
    private Path path = new Path();


    public MapCanvas(Context context, @Nullable AttributeSet attrs, Location userLocation) {
        super(context, attrs);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void initPaint() {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(15f);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }
}
