package org.Crock.WinterSchool.lab5.ImportProduct;

import org.Crock.WinterSchool.lab5.Product.Dimensions;
import org.Crock.WinterSchool.lab5.Product.Refrigerator;

public class ImportRefrigerator extends Refrigerator {
    private ImportGuarantee guarantee;

    public ImportGuarantee getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(ImportGuarantee Guarantee) {
        guarantee = Guarantee;
    }

    public ImportRefrigerator(double price, Dimensions size, int year, int temperature, ImportGuarantee guarantee) {
        super(price, size, year, temperature);
        this.guarantee = guarantee;
    }

    public ImportRefrigerator(double price, Dimensions size, ImportGuarantee guarantee) {
        super(price, size);
        this.guarantee = guarantee;
    }

    public ImportRefrigerator(double price, Dimensions size, int year, RefrigeratorType typeFridge,
                              int temperature, ImportGuarantee guarantee) {
        super(price, size, year, typeFridge, temperature);
        this.guarantee = guarantee;
    }

    @Override
    public String getDescription() {
        return super.getDescription() +
                "Наличие гарантии: " + guarantee.hasGuarantee() + '\n' +
                "Страна производства: " + guarantee.country() + '\n';
    }
}
