package org.Crock.WinterSchool.lab5.importproduct;

import org.Crock.WinterSchool.lab5.product.Dimensions;
import org.Crock.WinterSchool.lab5.product.WashingMachine;

public class ImportWashingMachine extends WashingMachine {
    ImportGuarantee guarantee;

    public ImportGuarantee getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(ImportGuarantee guarantee) {
        this.guarantee = guarantee;
    }

    public ImportWashingMachine(double price, Dimensions size, int dataRelease, ImportGuarantee guarantee) {
        super(price, size, dataRelease);
        this.guarantee = guarantee;
    }

    public ImportWashingMachine(double price, Dimensions size, WashingMachineType typeMachine,
                                ImportGuarantee guarantee) {
        super(price, size, typeMachine);
        this.guarantee = guarantee;
    }

    public ImportWashingMachine(double price, Dimensions size, int dataRelease, WashingMachineType typeMachine,
                                ImportGuarantee guarantee) {
        super(price, size, dataRelease, typeMachine);
        this.guarantee = guarantee;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + guarantee;
    }
}
