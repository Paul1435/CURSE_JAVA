package org.example.figure;

import java.util.Objects;

public class Point {
    private double x;
    private double y;

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    @Override
    public String toString() {
        return "(" + x +
                ", " + y +
                ')';
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }

    public double calculateDistance(Point secondPoint) {
        double dx = x - secondPoint.x;
        double dy = y - secondPoint.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
