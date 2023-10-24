package org.Crock.WinterSchool.lab5.Product;

public record Dimensions(double length, double width, double height) {
    public double getVolume() {
        return length * width * height;
    }

    public Dimensions(double size) {
        this(size, size, size);
    }


    @Override
    public String toString() {
        return length + " x " + width + " x " + height;
    }
}
