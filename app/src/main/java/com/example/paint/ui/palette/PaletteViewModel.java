package com.example.paint.ui.palette;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PaletteViewModel extends ViewModel {

    // Live data instances

    // Tracks the brush color
    private MutableLiveData<Integer> brushColor;

    // Tracks the canvas color
    private MutableLiveData<Integer> canvasColor;

    //TESTING CASE
    //TODO delete
    private MutableLiveData<String> message = new MutableLiveData<>();

    public PaletteViewModel() {
        brushColor = new MutableLiveData<>();
        canvasColor = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getBrushColor() {
        return brushColor;
    }

    public void setBrushColor(int value) {
        this.brushColor.setValue(value);
    }

    public MutableLiveData<Integer> getCanvasColor() {
        return canvasColor;
    }

    public void setCanvasColor(int value) {
        this.canvasColor.setValue(value);
    }

    //TESTING CASE
    public void setMessage() {
        //TODO delete
        this.message.setValue("Palette fragment YOOOOOOOOO");
    }

    //TESTING CASE
    public LiveData<String> getText() {
        //TODO delete
        return message;
    }
}