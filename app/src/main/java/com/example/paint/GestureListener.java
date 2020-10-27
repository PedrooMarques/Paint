package com.example.paint;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureListener extends GestureDetector.SimpleOnGestureListener
        implements GestureDetector.OnDoubleTapListener {

    private Canvas canvas;

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    /**
     * SimpleOnGestureListener
     *
     * @param motionEvent
     */
    @Override
    public void onLongPress(MotionEvent motionEvent) {
        canvas.reset();
    }

    /**
     * OnDoubleTapListener
     *
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        canvas.undo();
        return false;
    }
}
