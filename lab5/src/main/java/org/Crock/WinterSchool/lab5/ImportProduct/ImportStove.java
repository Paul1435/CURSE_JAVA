package org.Crock.WinterSchool.lab5.ImportProduct;

import org.Crock.WinterSchool.lab5.Product.Dimensions;
import org.Crock.WinterSchool.lab5.Product.Stove;

public class ImportStove extends Stove {
    ImportGuarantee guarantee;


    public ImportStove(double price, Dimensions size, int dataRelease, StoveType typeStove,
                       double power, ImportGuarantee guarantee) {
        super(price, size, dataRelease, typeStove, power);
        this.guarantee = guarantee;
    }

    public ImportStove(double price, Dimensions size, int dataRelease,
                       ImportGuarantee guarantee) {
        super(price, size, dataRelease);
        this.guarantee = guarantee;
    }

    public ImportStove(double price, Dimensions size, StoveType typeStove,
                       ImportGuarantee guarantee) {
        super(price, size, typeStove);
        this.guarantee = guarantee;
    }

    public ImportGuarantee getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(ImportGuarantee guarantee) {
        this.guarantee = guarantee;
    }

    @Override
    public String getDescription() {
        return super.getDescription() +
                "Наличие гарантии: " + guarantee.hasGuarantee() + '\n' +
                "Страна производства: " + guarantee.country() + '\n';
    }
}
