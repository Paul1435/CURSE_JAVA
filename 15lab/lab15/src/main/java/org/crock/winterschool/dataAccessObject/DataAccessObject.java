package org.crock.winterschool.dataAccessObject;

/**
 * Все объекты, которые записываются в базу данных должны реализовать данный интерфейс, возращающий элементы в той последовательности,
 * в какой они в таблице
 */
public interface DataAccessObject {
    Object[] getValues();
}
