package org.crock.winterschool.dataAccessObject;


public record Pet(int petId, String name, int clientId, int age) implements DataAccessObject {
    @Override
    public Object[] getValues() {
        return new Object[]{petId, clientId, name, age};
    }
}
