package com.example.paint;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class Canvas extends View implements View.OnTouchListener {

    private ArrayList<Pair<Path, Paint>> paths;

    private Paint paint = new Paint();
    private Path path = new Path();
    private GestureDetector mGestureDetector;

    public Canvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        paths = new ArrayList<>();
        initPaint();
    }

    public Canvas(Context context, AttributeSet attrs, GestureDetector gestureDetector) {
        super(context, attrs);
        this.mGestureDetector = gestureDetector;
        setOnTouchListener(this);
        paths = new ArrayList<>();
        initPaint();
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        for (Pair<Path, Paint> p : paths) {
            canvas.drawPath(p.first, p.second);// draws the path with the paint
        }
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        // TODO aqui precisa de retornar true para nao ir para o touchevent mas o gesture listener retorna sempre false...
        // TODO ele so retorna true depois do segundo clique, o seja dois false e um true. e no long press nem isso mas e normal
        return false; // let the event go to the rest of the listeners
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Paint tempPaint = new Paint(paint);

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
                paths.add(new Pair<>(path, tempPaint));
                path = new Path();
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
        initPaint();
        paths.clear();
    }

    public void undo() {
        Log.d("PATHS", (paths.toString()));
        // TODO adicionar a ultima posicao ao redo

        if (paths.isEmpty())
            Toast.makeText(getContext(), "Nothing to undo", Toast.LENGTH_SHORT).show();
        else {
            paths.remove(paths.size() - 2);
        }
    }

    private void initPaint() {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    private static final float SHAKE_THRESHOLD_GRAVITY = 1.2F;
    private static final int SHAKE_SLOP_TIME_MS = 1000;
    // sensor event listener
    public final SensorEventListener sensorEventListener = new SensorEventListener() {

        private long mShakeTimestamp;

        @Override
        public void onSensorChanged(SensorEvent event) {

            if (event.sensor.getType() != Sensor.TYPE_LINEAR_ACCELERATION)
                return;

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);
            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                mShakeTimestamp = now;
                reset();
                //TODO isto e uma merda e da reset quando se clica nao quando da shake...
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
