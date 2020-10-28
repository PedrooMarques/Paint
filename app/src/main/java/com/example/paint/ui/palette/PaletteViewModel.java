package com.example.paint.ui.palette;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PaletteViewModel extends ViewModel {

    // Live data instances

    // Tracks the brush color
    private MutableLiveData<Integer> brushColor;

    // Tracks the brush color
    private MutableLiveData<Float> brushSize;

    // Tracks the canvas color
    //private MutableLiveData<Integer> canvasColor;

    public PaletteViewModel() {
        brushColor = new MutableLiveData<>();
        brushSize = new MutableLiveData<>();
        //canvasColor = new MutableLiveData<>();
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

//TODO estes metodos abaixo so servem se metermos o background na palette
//    public MutableLiveData<Integer> getCanvasColor() {
//        return canvasColor;
//    }
//
//    public void setCanvasColor(int value) {
//        this.canvasColor.setValue(value);
//    }
}