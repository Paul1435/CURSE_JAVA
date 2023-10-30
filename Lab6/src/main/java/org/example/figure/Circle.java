package org.example.figure;

public class Circle extends Figure {
    private Point center;
    private double radius;

    @Override
    public String toString() {
        return "Circle " +
                center + ", " + radius;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public boolean isPointInside(Point toCheck) {
        double distance = center.calculateDistance(toCheck);
        return radius >= distance;
    }

    @Override
    public void move(int dx, int dy) {
        center.setX(center.getX() + dx);
        center.setY(center.getY() + dy);
    }
}
