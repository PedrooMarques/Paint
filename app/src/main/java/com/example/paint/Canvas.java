package com.example.paint;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class Canvas extends View implements View.OnTouchListener {

    private static int selectedColor;
    public int defaultValue = 0;
    private Paint paint = new Paint();
    private Path path = new Path();
    private GestureDetector mGestureDetector;
    private SharedPreferences sharedPref;

    public Canvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        selectedColor = sharedPref.getInt(
                context.getString(R.string.background_color_preference), defaultValue);
        setOnTouchListener(this);
        setBackgroundColor(selectedColor);
        initPaint();
    }

    public Canvas(Context context, AttributeSet attrs, GestureDetector gestureDetector) {
        super(context, attrs);
        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        selectedColor = sharedPref.getInt(
                context.getString(R.string.background_color_preference), defaultValue);
        this.mGestureDetector = gestureDetector;
        setOnTouchListener(this);
        setBackgroundColor(selectedColor);
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

    public void erase() {
        paint.setColor(11);
    }

    public void reset() {
        //TODO erase other things and stuff
        setBackgroundColor(Color.WHITE);
    }

    public void undo() {
    }

//    private void changeBackgroundColor(int selectedColor) {
//        setBackgroundColor(selectedColor);
//    }
//
//    public static void setSelectedColor(int color) {
//        selectedColor = color;
//    }

    private void initPaint() {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }
}
