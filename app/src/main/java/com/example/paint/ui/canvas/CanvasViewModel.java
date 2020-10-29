package com.example.paint.ui.canvas;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CanvasViewModel extends ViewModel {

    // Live data instances

    //TODO talvez de para usar para guardar os path

    // Tracks the canvas color in sharedPrefs
    private MutableLiveData<Integer> canvasColor;

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