package org.Crock.WinterSchool.lab5.product;

import java.time.Year;

public class VacuumCleaner extends Product {
    public double getAutonomousTime() {
        return timeAutonomousOperation;
    }

    public void setTimeAutonomousOperation(double timeAutonomousOperation) {
        this.timeAutonomousOperation = timeAutonomousOperation;
    }

    double timeAutonomousOperation;
    private static final int[] typeCount = new int[VacuumCleanerType.values().length];
    private final VacuumCleanerType typeVacuumCleaner;

    public static enum VacuumCleanerType {
        MANUAL_VACUUM_CLEANER,
        ROBOT_VACUUM_CLEANER
    }

    public VacuumCleaner(double price, Dimensions size, int dataRelease, VacuumCleanerType type, double time) {
        super(price, size, dataRelease);
        timeAutonomousOperation = time;
        this.typeVacuumCleaner = type;
        int index = typeVacuumCleaner.ordinal();
        ++typeCount[index];
    }

    public VacuumCleaner(double price, Dimensions size, int dataRelease) {
        this(price, size, dataRelease, VacuumCleanerType.MANUAL_VACUUM_CLEANER, 0);
    }

    public VacuumCleaner(double price, Dimensions size, VacuumCleanerType typeVacuumCleaner) {
        this(price, size, Year.now().getValue(), typeVacuumCleaner, 0);
    }

    @Override
    public String getDescription() {
        return "Пылесос: " + typeVacuumCleaner + "\n" +
                "Количество товаров такой же категории: " + (typeCount[typeVacuumCleaner.ordinal()] - 1) + '\n' +
                "Время работы от аккумулятора: " + getAutonomousTime() + '\n' +
                super.getDescription();
    }
}
