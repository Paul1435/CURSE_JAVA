package org.Crock.WinterSchool.lab5.ImportProduct;

import org.Crock.WinterSchool.lab5.Product.Dimensions;
import org.Crock.WinterSchool.lab5.Product.VacuumCleaner;

public class ImportVacuumCleaner extends VacuumCleaner {
    ImportGuarantee guarantee;

    public ImportGuarantee getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(ImportGuarantee guarantee) {
        this.guarantee = guarantee;
    }

    public ImportVacuumCleaner(double price, Dimensions size, int dataRelease, VacuumCleanerType type, double time,
                               ImportGuarantee guarantee) {
        super(price, size, dataRelease, type, time);
        this.guarantee = guarantee;
    }

    public ImportVacuumCleaner(double price, Dimensions size, int dataRelease, ImportGuarantee guarantee) {
        super(price, size, dataRelease);
        this.guarantee = guarantee;
    }

    public ImportVacuumCleaner(double price, Dimensions size, VacuumCleanerType typeVacuumCleaner,
                               ImportGuarantee guarantee) {
        super(price, size, typeVacuumCleaner);
        this.guarantee = guarantee;
    }

    @Override
    public String getDescription() {
        return super.getDescription() +
                "Наличие гарантии: " + guarantee.hasGuarantee() + '\n' +
                "Страна производства: " + guarantee.country() + '\n';
    }

}
