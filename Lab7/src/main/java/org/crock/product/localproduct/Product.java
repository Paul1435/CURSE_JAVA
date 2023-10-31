package org.crock.product.localproduct;

public abstract class Product {
    protected double price;
    protected int dataRelease;

    public Product(double price, Dimensions size, int dataRelease) {
        this.price = price;
        this.size = size;
        this.dataRelease = dataRelease;
    }

    public double getPrice() {
        price = Math.round(price * 100);
        price = price / 100;
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    protected Dimensions size;

    public String getDescription() {
        return "Цена: " + getPrice() + " руб." + '\n' +
                "Год издания: " + dataRelease + " г." + '\n' +
                "Размеры: " + size + '\n' +
                "Объем: " + size.getVolume() + " см3\n";
    }

}
