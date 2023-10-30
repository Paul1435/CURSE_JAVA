package org.Crock.WinterSchool.lab5.product;

import java.time.Year;

public class CoffeeMachine extends Product {
    private static final int[] typeCount = new int[CoffeeMachine.CoffeeMachineType.values().length];
    private final CoffeeMachineType typeCoffeeMachine;


    public static enum CoffeeMachineType {
        DRIP_COFFEE_MAKER,
        FRENCH_PRESS
    }

    public CoffeeMachine(double price, Dimensions size, int dataRelease, CoffeeMachineType typeCoffeeMachine) {
        super(price, size, dataRelease);
        this.typeCoffeeMachine = typeCoffeeMachine;
        int index = typeCoffeeMachine.ordinal();
        ++typeCount[index];
    }

    public CoffeeMachine(double price, Dimensions size, int dataRelease) {
        this(price, size, dataRelease, CoffeeMachineType.FRENCH_PRESS);
    }

    public CoffeeMachine(double price, Dimensions size, CoffeeMachine.CoffeeMachineType typeCoffeeMachine) {
        this(price, size, Year.now().getValue(), typeCoffeeMachine);
    }

    @Override
    public String getDescription() {
        return "Кофемашина: " + typeCoffeeMachine + "\n" +
                "Количество похожих товаров: " + (typeCount[typeCoffeeMachine.ordinal()] - 1) + '\n' +
                super.getDescription();
    }
}
