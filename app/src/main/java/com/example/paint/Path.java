package com.example.paint;

import java.util.List;

public class Path {

    private android.graphics.Path path;

    private List<Point> points;

    public Path() {
    }

    public android.graphics.Path getPath() {
        return path;
    }

    public void setPath(android.graphics.Path path) {
        this.path = path;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
