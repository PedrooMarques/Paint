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
    private final MutableLiveData<Integer> canvasColor;

    // tracks the existing paths to draw
    private final MutableLiveData<ArrayList<Pair<Path, Paint>>> paths;

    // custom paths
    private final MutableLiveData<ArrayList<Pair<com.example.paint.Path, com.example.paint.Paint>>> customPaths;

    public CanvasViewModel() {
        canvasColor = new MutableLiveData<>();
        paths = new MutableLiveData<>();
        customPaths = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getCanvasColor() {
        return canvasColor;
    }

    public void setCanvasColor(int value) {
        this.canvasColor.setValue(value);
    }

    public MutableLiveData<ArrayList<Pair<Path, Paint>>> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<Pair<Path, Paint>> paths) {
        this.paths.setValue(paths);
    }

    public MutableLiveData<ArrayList<Pair<com.example.paint.Path, com.example.paint.Paint>>> getCustomPaths() {
        return customPaths;
    }

    public void setCustomPaths(ArrayList<Pair<com.example.paint.Path, com.example.paint.Paint>> customPaths) {
        this.customPaths.setValue(customPaths);
    }
}