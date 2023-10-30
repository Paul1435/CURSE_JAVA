package org.Crock.WinterSchool.lab5.product;

import java.time.Year;

public class Refrigerator extends Product {
    public static enum RefrigeratorType {
        TWO_CHAMBER, SIDE_BY_SAID, FRENCH_DOOR, MINI_FRIDGE,
        GAS_COOLER, WITHOUT_FREEZER_COMPARTMENT, WITH_FREEZER_COMPARTMENT
    }

    private int temperatureOfFreezerCompartment = 0;

    public int getTemperature() {
        return temperatureOfFreezerCompartment;
    }

    public void setTemperature(int temperature) {
        this.temperatureOfFreezerCompartment = temperature;
    }

    private final RefrigeratorType typeFridge;
    private static final int[] typeCount = new int[RefrigeratorType.values().length];

    public Refrigerator(double price, Dimensions size, int year, int temperature) {
        this(price, size, year, RefrigeratorType.WITH_FREEZER_COMPARTMENT, temperature);
    }

    public Refrigerator(double price, Dimensions size) {
        this(price, size, Year.now().getValue(), RefrigeratorType.WITH_FREEZER_COMPARTMENT, 0);
    }

    public Refrigerator(double price, Dimensions size, int year, RefrigeratorType typeFridge, int temperature) {
        super(price, size, year);
        this.typeFridge = typeFridge;
        int index = typeFridge.ordinal();
        ++typeCount[index];
        temperatureOfFreezerCompartment = temperature;
    }


    @Override
    public String getDescription() {
        return "Холодильник: " + typeFridge + '\n' +
                "Количество похожих товаров: " + (typeCount[typeFridge.ordinal()] - 1) + '\n' +
                super.getDescription() +
                "Температура холодильной камеры: " + temperatureOfFreezerCompartment + '\n';
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
