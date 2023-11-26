package org.crock.winterschool.database;

import org.crock.winterschool.dataAccessObject.DataAccessObject;

import java.sql.*;
import java.util.List;
import java.util.Objects;

public class DataBase {
    private final String tableName;
    private final String fieldsOfTable;
    private Connection connection;

    private DataBase(DataBaseBuilder builder) {
        Objects.requireNonNull(builder.tableName, "Нет имени таблицы");
        Objects.requireNonNull(builder.fields, "Нет полей");
        tableName = builder.tableName;
        fieldsOfTable = builder.fields;
    }

    public static DataBaseBuilder newDataBaseBuilder() {
        return new DataBaseBuilder();
    }

    public void connect(String user, String psw) throws SQLException {
        Objects.requireNonNull(user, "Не передан аргумент имя пользователя");
        Objects.requireNonNull(psw, "Не передан аргумент пароля");
        try {
            connection = DriverManager.getConnection("jdbc:h2:mem:~/test", user, psw);
        } catch (SQLException e) {
            throw new SQLException(/*"Не удалось открыть соединение"*/e);
        }
    }

    public void createTable() throws SQLException {
        checkConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " " +
                    fieldsOfTable);
        }
    }

    private void checkConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Не было открыто соединение");
        }
    }

    private void checkValues(List<? extends DataAccessObject> dataObjects) throws SQLException {
        if (dataObjects == null || dataObjects.size() == 0) {
            throw new SQLException("Пустые данные");
        }
        for (var data : dataObjects) {
            if (data == null || data.getValues() == null) {
                throw new SQLException("Пустые данные в " + dataObjects);
            }
        }
    }

    /**
     * @param dataObjects - для записи берем только те значения, которые возвращают элементы в гарантированном порядке
     * @return - количество вставленных новых записей. Повторения игнорируются
     * @throws SQLException - Если случится непредвиденная ошибка и значения не будут записаны
     */

    public int insertValue(List<? extends DataAccessObject> dataObjects) throws SQLException {
        checkConnection();
        checkValues(dataObjects);
        StringBuilder sqlRequest = new StringBuilder("INSERT INTO " + tableName + " VALUES (");
        for (int i = 0; i < dataObjects.get(0).getValues().length; i++) {
            if (i > 0) {
                sqlRequest.append(", ");
            }
            sqlRequest.append("?");
        }
        sqlRequest.append(")");
        int rowInputed = 0;
        try (PreparedStatement statement = connection.prepareStatement(sqlRequest.toString())) {
            for (DataAccessObject dataObject : dataObjects) {
                Object[] values = dataObject.getValues();
                for (int i = 0; i < values.length; i++) {
                    statement.setObject(i + 1, values[i]);
                }
                try {
                    statement.executeUpdate();
                    rowInputed++;
                } catch (SQLIntegrityConstraintViolationException e) {
                    continue;
                }
            }
            return rowInputed;
        }

    }

    public void closeConnect() throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new SQLException("Не удалось закрыть соединение");
        }
    }

    /**
     * toString - метод, который выводит таблицы для проверки работы
     *
     * @return - таблица данных
     */

    @Override
    public String toString() {
        StringBuilder result = null;
        try {
            checkConnection();
            result = new StringBuilder(tableName + "\n");
            try (Statement statement = connection.createStatement()) {
                String selectQuery = "SELECT * FROM " + tableName;
                try (ResultSet resultSet = statement.executeQuery(selectQuery)) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    // Append column names
                    for (int i = 1; i <= columnCount; i++) {
                        result.append(metaData.getColumnName(i)).append("\t");
                    }
                    result.append("\n");

                    // Append rows
                    while (resultSet.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            result.append(resultSet.getString(i)).append("\t");
                        }
                        result.append("\n");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result == null ? null : result.toString();
    }

    /**
     * DataBaseBuilder - Builder,который помогает сгенерировать класс
     */
    static public class DataBaseBuilder {
        private String tableName;
        private String fields;

        public DataBaseBuilder() {
        }

        public DataBaseBuilder setTableName(String tableName) {
            Objects.requireNonNull(tableName, "Incorrect name of table");
            if (tableName.length() == 0) {
                throw new IllegalArgumentException("name can't be empty");
            }
            this.tableName = tableName;
            return this;
        }

        public DataBaseBuilder setFields(String fields) {
            Objects.requireNonNull(fields, "No column information was transmitted");
            if (fields.length() == 0) {
                throw new IllegalArgumentException("No column information was transmitted");
            }
            this.fields = fields;
            return this;
        }

        public DataBase build() {
            return new DataBase(this);
        }
    }

}
