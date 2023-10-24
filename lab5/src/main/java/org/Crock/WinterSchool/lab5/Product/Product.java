package org.Crock.WinterSchool.lab5.Product;

public abstract class Product {
    protected double price;
    protected int dataRelease;

    public Product(double price, Dimensions size, int dataRelease) {
        this.price = price;
        this.size = size;
        this.dataRelease = dataRelease;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    protected Dimensions size;

    abstract public String getDescription();

}
