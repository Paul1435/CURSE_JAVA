package org.Crock.WinterSchool.lab5.importproduct;

public record ImportGuarantee(String country, boolean hasGuarantee) {
    String getDescription() {
        return "Наличие гарантии: " + hasGuarantee() + '\n' +
                "Страна производства: " + country() + '\n';
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
