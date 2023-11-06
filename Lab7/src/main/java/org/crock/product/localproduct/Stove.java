package org.crock.product.localproduct;

import java.time.Year;

public class Stove extends Product {
    private final StoveType typeStove;
    private double power;

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public static enum StoveType {
        GAS, ELECTRIC, INDUCTION
    }

    private static final int[] typeCount = new int[StoveType.values().length];

    public Stove(double price, Dimensions size, int dataRelease, StoveType typeStove, double power) {
        super(price, size, dataRelease);
        this.typeStove = typeStove;
        int index = typeStove.ordinal();
        this.power = power;
        ++typeCount[index];
    }

    public Stove(double price, Dimensions size, int dataRelease) {
        this(price, size, dataRelease, StoveType.GAS, 0);
    }

    public Stove(double price, Dimensions size, StoveType typeStove) {
        this(price, size, Year.now().getValue(), typeStove, 0);
    }

    @Override
    public String getDescription() {
        return "Плита: " + typeStove + '\n' +
                "Количество похожих товаров: " + (typeCount[typeStove.ordinal()] - 1) + '\n' +
                super.getDescription();
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
