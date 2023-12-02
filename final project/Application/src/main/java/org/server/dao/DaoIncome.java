package org.server.dao;

import org.server.database.ConnectionDatabase;
import org.server.containers.Income;
import org.server.exceptions.DaoException;

import java.math.BigDecimal;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DaoIncome implements Dao<Income> {
    public static synchronized DaoIncome getInstance() {
        if (instance == null) {
            instance = new DaoIncome();
        }
        return instance;
    }

    private DaoIncome() {
    }

    private static DaoIncome instance;


    @Override
    public synchronized void saveRecord(Income record) throws DaoException {
        String insertQuery = "INSERT INTO Incomes (employee_id, amount, description, date) " +
                "VALUES(?,?,?,?)";
        ZonedDateTime moscowTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        try (PreparedStatement statement = ConnectionDatabase.getConnection().prepareStatement(insertQuery)) {
            statement.setInt(1, record.candidateId());
            statement.setBigDecimal(2, record.price());
            statement.setString(3, record.description());
            statement.setDate(4, Date.valueOf(moscowTime.toLocalDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(record, "Не удалось сохранить запись, подробнее:" + e.getMessage());
        }
    }

    @Override
    public BigDecimal getSumRecords() throws SQLException {
        String sumQuery = "SELECT COALESCE(SUM(amount),0) AS total FROM Incomes";
        try (Statement sumStatement = ConnectionDatabase.getConnection().createStatement()) {
            ResultSet resultSet = sumStatement.executeQuery(sumQuery);
            BigDecimal income = null;
            if (resultSet.next()) {
                income = BigDecimal.valueOf(resultSet.getDouble("total"));
            } else {
                income = BigDecimal.ZERO;
            }
            return income;
        } catch (SQLException e) {
            throw new SQLException("Не удалось получить заработанную сумму прчина:" + e.getMessage());
        }

    }

    @Override
    public String getDescription(Integer idIncome) throws SQLException {
        String getDescriptionQuery = "SELECT description FROM Incomes WHERE income_id = ?";

        // Установка уровня изоляции транзакции (пример для READ_COMMITTED)
        Connection connection = ConnectionDatabase.getConnection();
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.setAutoCommit(false); // Начало транзакции

        try (PreparedStatement sumStatement = connection.prepareStatement(getDescriptionQuery)) {
            sumStatement.setInt(1, idIncome);
            ResultSet resultSet = sumStatement.executeQuery();
            String description = "";
            if (resultSet.next()) {
                description = resultSet.getString("description");
            }
            connection.commit(); // Подтверждение транзакции
            return description;

        } catch (SQLException e) {
            connection.rollback(); // Откат транзакции в случае ошибки
            throw new SQLException("Не удалось получить описания для прибыли : " + idIncome + " причина:" + e.getMessage());
        } finally {
            connection.setAutoCommit(true); // Восстановление автокоммита

        }

    }

    @Override
    public String getDescription(String firstname, String secondName, String middleName) throws SQLException {
        String findEmployeeQuery = "SELECT employee_id FROM work_info_employees WHERE first_name = ? AND last_name = ? AND middle_name = ?";

        String getDescriptionQuery = "SELECT description FROM expenses WHERE expenses_id = ?";

        Connection connection = ConnectionDatabase.getConnection();
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.setAutoCommit(false);

        try (PreparedStatement findEmployeeStatement = connection.prepareStatement(findEmployeeQuery)) {
            findEmployeeStatement.setString(1, firstname);
            findEmployeeStatement.setString(2, secondName);
            findEmployeeStatement.setString(3, middleName);

            ResultSet employeeResultSet = findEmployeeStatement.executeQuery();
            if (employeeResultSet.next()) {
                int employeeId = employeeResultSet.getInt("employee_id");

                try (PreparedStatement sumStatement = connection.prepareStatement(getDescriptionQuery)) {
                    sumStatement.setInt(1, employeeId);
                    ResultSet resultSet = sumStatement.executeQuery();
                    String description = "";
                    if (resultSet.next()) {
                        description = resultSet.getString("description");
                    }
                    connection.commit();
                    return description;
                } catch (SQLException e) {
                    connection.rollback();
                    throw new SQLException("Failed to get description for employee: " + employeeId + " Reason: " + e.getMessage());
                }
            } else {
                throw new SQLException("Employee not found with name: " + firstname + " " + secondName + " " + middleName);
            }
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Failed to find employee. Reason: " + e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }


    @Override
    public BigDecimal getSumRecordsOfCandidate(Integer candidateId) throws SQLException {
        String sumQuery = "SELECT COALESCE(SUM(amount), 0) AS total FROM Incomes WHERE employee_id = ?";

        // Установка уровня изоляции транзакции (пример для READ_COMMITTED)
        Connection connection = ConnectionDatabase.getConnection();
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.setAutoCommit(false);

        try (PreparedStatement sumStatement = connection.prepareStatement(sumQuery)) {
            sumStatement.setInt(1, candidateId);
            ResultSet resultSet = sumStatement.executeQuery();
            BigDecimal income = null;
            if (resultSet.next()) {
                income = BigDecimal.valueOf(resultSet.getDouble("total"));
            } else {
                income = BigDecimal.ZERO;
            }

            connection.commit();
            return income;
        } catch (SQLException e) {
            connection.rollback(); // Откат транзакции в случае ошибки
            throw new SQLException("Не удалось получить заработанную сумму кандидата: " + candidateId + " причина:" + e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

}
