package org.Crock.WinterSchool.lab5.ImportProduct;

import org.Crock.WinterSchool.lab5.Product.CoffeeMachine;
import org.Crock.WinterSchool.lab5.Product.Dimensions;

public class ImportCoffeeMachine extends CoffeeMachine {
    private ImportGuarantee guarantee;

    public ImportCoffeeMachine(double price, Dimensions size, int dataRelease, CoffeeMachineType typeCoffeeMachine,
                               ImportGuarantee guarantee) {
        super(price, size, dataRelease, typeCoffeeMachine);
        this.guarantee = guarantee;
    }

    public ImportCoffeeMachine(double price, Dimensions size, int dataRelease, ImportGuarantee guarantee) {
        super(price, size, dataRelease);
        this.guarantee = guarantee;
    }

    public ImportCoffeeMachine(double price, Dimensions size, CoffeeMachineType typeCoffeeMachine,
                               ImportGuarantee guarantee) {
        super(price, size, typeCoffeeMachine);
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
