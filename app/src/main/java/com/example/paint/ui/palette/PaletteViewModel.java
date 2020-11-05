package com.example.paint.ui.palette;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PaletteViewModel extends ViewModel {

    // Live data instances

    // Tracks the brush color
    private final MutableLiveData<Integer> brushColor;

    // Tracks the brush color
    private final MutableLiveData<Float> brushSize;

    public PaletteViewModel() {
        brushColor = new MutableLiveData<>();
        brushSize = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getBrushColor() {
        return brushColor;
    }

    public void setBrushColor(int value) {
        this.brushColor.setValue(value);
    }

    public MutableLiveData<Float> getBrushSize() {
        return brushSize;
    }

    public void setBrushSize(float brushSize) {
        this.brushSize.setValue(brushSize);
    }

}