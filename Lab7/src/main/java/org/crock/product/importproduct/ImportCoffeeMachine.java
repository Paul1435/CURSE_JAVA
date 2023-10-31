package org.crock.product.importproduct;

import org.crock.product.localproduct.CoffeeMachine;
import org.crock.product.localproduct.Dimensions;

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
        return super.getDescription() + guarantee;
    }
}
