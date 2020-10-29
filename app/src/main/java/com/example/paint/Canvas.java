package com.example.paint;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class Canvas extends View implements View.OnTouchListener {

    private Paint paint = new Paint();
    private Path path = new Path();
    private GestureDetector mGestureDetector;

    public Canvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        initPaint();
    }

    public Canvas(Context context, AttributeSet attrs, GestureDetector gestureDetector) {
        super(context, attrs);
        this.mGestureDetector = gestureDetector;
        setOnTouchListener(this);
        initPaint();
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        canvas.drawPath(path, paint);// draws the path with the paint
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return false; // let the event go to the rest of the listeners
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);// updates the path initial point
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(eventX, eventY);// makes a line to the point each time this event is fired
                break;
            case MotionEvent.ACTION_UP:// when you lift your finger
                performClick();
                break;
            default:
                return false;
        }

        // Schedules a repaint.
        invalidate();
        return true;
    }

    public void setBrushSize(float size) {
        paint.setStrokeWidth(size);
    }

    public float getBrushSize() {
        return paint.getStrokeWidth();
    }

    public int getBrushColor() {
        return paint.getColor();
    }

    public void setBrushColor(int color) {
        paint.setColor(color);
    }

    public void setCanvasColor(int color) {
        setBackgroundColor(color);
    }

    public void reset() {
        setBackgroundColor(Color.WHITE);
        path.reset();
        initPaint();
    }

    public void undo() {
    }

    private void initPaint() {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }
}
