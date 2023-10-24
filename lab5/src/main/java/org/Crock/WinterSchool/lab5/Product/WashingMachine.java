package org.Crock.WinterSchool.lab5.Product;

import java.time.Year;

public class WashingMachine extends Product {
    private static final int[] typeCount = new int[WashingMachineType.values().length];
    private final WashingMachineType typeMachine;

    public static enum WashingMachineType {
        MANUAL_WASHING_MACHINE,
        SEMI_AUTOMATIC_WASHING_MACHINE,
        AUTOMATIC_WASHING_MACHINE,
        AUTOMATIC_WITH_DRYER_WASHING_MACHINE
    }

    public WashingMachine(double price, Dimensions size, int dataRelease, WashingMachineType typeMachine) {
        super(price, size, dataRelease);
        this.typeMachine = typeMachine;
        int index = typeMachine.ordinal();
        ++typeCount[index];
    }

    public WashingMachine(double price, Dimensions size, int dataRelease) {
        this(price, size, dataRelease, WashingMachineType.AUTOMATIC_WASHING_MACHINE);
    }

    public WashingMachine(double price, Dimensions size, WashingMachineType typeMachine) {
        this(price, size, Year.now().getValue(), typeMachine);
    }

    @Override
    public String getDescription() {
        return "Стиральная машина: " + typeMachine + "\n" +
                "Количество похожих товаров: " + (typeCount[typeMachine.ordinal()] - 1) + '\n' +
                "Цена: " + price + " руб." + '\n' +
                "Год издания: " + dataRelease + " г." + '\n' +
                "Размеры: " + size + '\n' +
                "Объем: " + size.getVolume() + " см3\n";

    }
}
