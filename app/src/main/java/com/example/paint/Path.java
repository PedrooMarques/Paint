package com.example.paint;

public class Path {

    private android.graphics.Path path;
    private float initX, initY;
    private float finalX, finalY;

    public Path() {
    }

    public android.graphics.Path getPath() {
        return path;
    }

    public void setPath(android.graphics.Path path) {
        this.path = path;
    }

    public float getInitX() {
        return initX;
    }

    public void setInitX(float initX) {
        this.initX = initX;
    }

    public float getInitY() {
        return initY;
    }

    public void setInitY(float initY) {
        this.initY = initY;
    }

    public float getFinalX() {
        return finalX;
    }

    public void setFinalX(float finalX) {
        this.finalX = finalX;
    }

    public float getFinalY() {
        return finalY;
    }

    public void setFinalY(float finalY) {
        this.finalY = finalY;
    }
}
