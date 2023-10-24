package org.example;

abstract public class Figure implements Movable {
    abstract boolean isPointInside(Point toCheck);

    @Override
    public abstract void move(int dx, int dy);
}
