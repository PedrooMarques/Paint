package com.example.paint.ui.canvas;

import android.graphics.Paint;
import android.graphics.Path;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CanvasViewModel extends ViewModel {

    // Live data instances

    // Tracks the canvas color in sharedPrefs
    private MutableLiveData<Integer> canvasColor;

    // tracks the existing paths to draw
    private MutableLiveData<ArrayList<Pair<Path, Paint>>> paths;

    public CanvasViewModel() {
        canvasColor = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getCanvasColor() {
        return canvasColor;
    }

    public void setCanvasColor(int value) {
        this.canvasColor.setValue(value);
    }
}