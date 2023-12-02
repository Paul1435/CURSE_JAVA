package org.server.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * DatabaseHelper - синглтон класс, который работает только с данной ДБ
 */
public class DatabaseHelper implements AutoCloseable {
    private static DatabaseHelper instance;

    private static final String CREATE_EMPLOYEE_JOB_INFO_TABLE_REQUEST = "CREATE TABLE work_info_employees " +
            "(employee_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL, " +
            "last_name VARCHAR(255) NOT NULL, " +
            "first_name VARCHAR(255) NOT NULL, " +
            "middle_name VARCHAR(255) NOT NULL, " +
            "post VARCHAR(255) NOT NULL, " +
            "username VARCHAR(50) UNIQUE NOT NULL, " +
            "password_hash VARCHAR(255) NOT NULL)";

    private static final String CREATE_INCOMES_TABLE = "CREATE TABLE Incomes " +
            "(income_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL, " +
            "employee_id INT NOT NULL, " +
            "amount DECIMAL(10, 2) NOT NULL, " +
            "description VARCHAR(255) NOT NULL, " +
            "date DATE, " +
            "FOREIGN KEY(employee_id) REFERENCES work_info_employees(employee_id))";

    private static final String CREATE_EXPENSES_TABLE = "CREATE TABLE expenses " +
            "(expenses_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL, " +
            "employee_id INT NOT NULL, " +
            "amount DECIMAL(10, 2) NOT NULL, " +
            "description VARCHAR(255) NOT NULL, " +
            "date DATE, " +
            "responding_user_id INT NOT NULL, " +
            "FOREIGN KEY (responding_user_id) REFERENCES work_info_employees(employee_id), " +
            "FOREIGN KEY(employee_id) REFERENCES work_info_employees(employee_id))";

    private static final String CREATE_MONEY_REQUEST_TABLE = "CREATE TABLE money_requests " +
            "(request_id INT PRIMARY KEY AUTO_INCREMENT, " +
            "employee_id INT NOT NULL, " +
            "amount DECIMAL(10, 2) NOT NULL, " +
            "description VARCHAR(255) NOT NULL, " +
            "status VARCHAR(20) NOT NULL, " +
            "date_requested DATE, " +
            "date_responded DATE, " +
            "FOREIGN KEY (employee_id) REFERENCES work_info_employees(employee_id))";
    private final Connection connection;

    /**
     * @param connection -  соединение к нашему БД
     *                   Приватный конструктор для предотвращения создания объекта извне класса
     *                   <p>
     *                   RuntimeException -  потому что база данных создается при проектировании приложения, а значит, программист
     *                   никак повлиять на этот процесс
     */

    private DatabaseHelper(Connection connection) {
        this.connection = connection;
        try {
            executeUpdate(CREATE_EMPLOYEE_JOB_INFO_TABLE_REQUEST);
            executeUpdate(CREATE_INCOMES_TABLE);
            executeUpdate(CREATE_EXPENSES_TABLE);
            executeUpdate(CREATE_MONEY_REQUEST_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось  создать базу данных, причина: " + e.getMessage());
        }

    }

    // Метод для получения экземпляра класса (Singleton)
    public static synchronized DatabaseHelper getInstance(Connection connection) {
        if (instance == null) {
            instance = new DatabaseHelper(connection);
        }
        return instance;
    }

    private void executeUpdate(String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new SQLException("Не удалось выполнить запрос, причина: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws Exception {
        getConnection().close();
    }
}