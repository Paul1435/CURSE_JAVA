package org.example;

public class Rectangle extends Figure {
    private Point lowerLeftPoint;
    private Point upperRightPoint;

    public Point getLowerLeftPoint() {
        return lowerLeftPoint;
    }

    public void setLowerLeftPoint(Point lowerLeftPoint) {
        this.lowerLeftPoint = lowerLeftPoint;
    }

    public Point getUpperRightPoint() {
        return upperRightPoint;
    }

    public void setUpperRightPoint(Point upperRightPoint) {
        this.upperRightPoint = upperRightPoint;
    }

    public Rectangle(Point lowerLeftPoint, Point upperRightPoint) {
        this.lowerLeftPoint = lowerLeftPoint;
        this.upperRightPoint = upperRightPoint;
    }

    @Override
    boolean isPointInside(Point toCheck) {
        if (toCheck.getX() <= upperRightPoint.getX() &&
                toCheck.getX() >= lowerLeftPoint.getX() &&
                toCheck.getY() <= upperRightPoint.getY() &&
                toCheck.getY() >= lowerLeftPoint.getY()) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Rectangle " +
                lowerLeftPoint +
                ", " + upperRightPoint;
    }

    @Override
    public void move(int dx, int dy) {
        lowerLeftPoint.setX(lowerLeftPoint.getX() + dx);
        lowerLeftPoint.setY(lowerLeftPoint.getY() + dy);
        upperRightPoint.setX(upperRightPoint.getX() + dx);
        upperRightPoint.setY(upperRightPoint.getY() + dy);
    }
}
