package org.crock.winterschool.dataAccessObject;

/**
 * @param clientId    - поле таблицы в БД
 * @param firstName   - поле таблицы в БД
 * @param lastName    - поле таблицы в БД
 * @param numberPhone - поле таблицы в БД
 *                    реализует интерфейс DataAccessObject
 */
public record Client(int clientId, String firstName,
                     String lastName, String numberPhone) implements DataAccessObject {
    @Override
    public Object[] getValues() {
        return new Object[]{clientId, lastName, firstName, numberPhone};
    }
}
