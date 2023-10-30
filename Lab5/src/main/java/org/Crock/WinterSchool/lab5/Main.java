package org.Crock.WinterSchool.lab5;

import org.Crock.WinterSchool.lab5.importproduct.ImportGuarantee;
import org.Crock.WinterSchool.lab5.importproduct.ImportVacuumCleaner;
import org.Crock.WinterSchool.lab5.product.*;

public class Main {
    public static void main(String[] args) {
        Product[] products = new Product[]{new Refrigerator(20_000, new Dimensions(100)),
                new Refrigerator(30_000, new Dimensions(150, 20, 13)),
                new Stove(15_000, new Dimensions(12, 11, 1), Stove.StoveType.ELECTRIC),
                new Stove(20_000, new Dimensions(15, 12, 1), 2019,
                        Stove.StoveType.ELECTRIC, 50),
                new Stove(9_998, new Dimensions(10), 2015, Stove.StoveType.GAS, 20),
                new VacuumCleaner(20_000, new Dimensions(14, 13, 12), 2019,
                        VacuumCleaner.VacuumCleanerType.ROBOT_VACUUM_CLEANER, 15),
                new WashingMachine(15_400.99, new Dimensions(120),
                        WashingMachine.WashingMachineType.AUTOMATIC_WASHING_MACHINE),
                new CoffeeMachine(12_000, new Dimensions(13, 30, 50),
                        CoffeeMachine.CoffeeMachineType.FRENCH_PRESS),
                new ImportVacuumCleaner(20_000, new Dimensions(14, 13, 12), 2019,
                        VacuumCleaner.VacuumCleanerType.ROBOT_VACUUM_CLEANER, 15,
                        new ImportGuarantee("Korea", true))};

        for (var product : products) {
            System.out.println(product.getDescription());
        }

    }
}