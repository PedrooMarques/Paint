package com.example.paint;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class Canvas extends View implements View.OnTouchListener, SensorEventListener {

    private ArrayList<Pair<Path, Paint>> paths;

    private Paint paint = new Paint();
    private Path path = new Path();

    private GestureDetector mGestureDetector;

    private float initialX;
    private float initialY;

    // motion sensor
    private static final float ACCELERATION_THRESHOLD = 10F;
    private static final int SHAKE_SLOP_TIME_MS = 1000;
    private long mShakeTimestamp;

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
        return false; // let the event go to the rest of the listeners
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Paint tempPaint = new Paint(paint);

        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // updates the path initial point
                path.moveTo(eventX, eventY);
                initialX = eventX;
                initialY = eventY;
                return true;
            case MotionEvent.ACTION_MOVE:
                // makes a line to the point each time this event is fired
                path.lineTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP:// when you lift your finger
                // if the position of the click changed (if the user drew) add the path to the list
                if (initialX != eventX && initialY != eventY) {
                    paths.add(new Pair<>(path, tempPaint));
                    path = new Path();
                }
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
            paths.remove(paths.size() - 1);
        }
    }

    private void initPaint() {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    // sensor methods
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // gForce will be close to 1 when there is no movement.
            float accelMagnitude = (float) Math.sqrt(Math.pow(x, 2) +
                    Math.pow(y, 2) + Math.pow(z, 2));

            if (accelMagnitude > ACCELERATION_THRESHOLD) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                mShakeTimestamp = now;
                reset();
                invalidate();
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            // if the write_settings permission was granted

            // change brightness mode to manual
            Settings.System.putInt(getContext().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

            float lSensorValue = event.values[0];
            float lSensorValueMaxValue = event.sensor.getMaximumRange();

            int brightness = (int) (255 - (lSensorValue * (255 / lSensorValueMaxValue)));

            Settings.System.putInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
