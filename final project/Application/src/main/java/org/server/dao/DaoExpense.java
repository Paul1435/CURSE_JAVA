package org.server.dao;

import org.server.database.ConnectionDatabase;
import org.server.containers.Expense;
import org.server.exceptions.DaoException;

import java.math.BigDecimal;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DaoExpense implements Dao<Expense> {
    public static synchronized DaoExpense getInstance() {
        if (instance == null) {
            instance = new DaoExpense();
        }
        return instance;
    }

    private DaoExpense() {
    }

    private static DaoExpense instance;

    @Override
    public synchronized void saveRecord(Expense record) throws DaoException {
        String insertQuery = "INSERT INTO expenses (employee_id, amount, description, date, responding_user_id) " +
                "VALUES(?,?,?,?,?)";
        ZonedDateTime moscowTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        try (PreparedStatement statement = ConnectionDatabase.getConnection().prepareStatement(insertQuery)) {
            statement.setInt(1, record.candidateId());
            statement.setBigDecimal(2, record.amount());
            statement.setString(3, record.description());
            statement.setDate(4, Date.valueOf(moscowTime.toLocalDate()));
            statement.setInt(5, record.managerId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(record, "Не удалось сохранить запись, подробнее:" + e.getMessage());
        }
    }

    @Override
    public BigDecimal getSumRecords() throws SQLException {
        String sumQuery = "SELECT COALESCE(SUM(amount),0) AS total FROM expense";
        try (Statement sumStatement = ConnectionDatabase.getConnection().createStatement()) {
            ResultSet resultSet = sumStatement.executeQuery(sumQuery);
            BigDecimal expenseSum = null;
            if (resultSet.next()) {
                expenseSum = BigDecimal.valueOf(resultSet.getDouble("total"));
            } else {
                expenseSum = BigDecimal.ZERO;
            }
            return expenseSum;
        } catch (SQLException e) {
            throw new SQLException("Не удалось получить Потраченную сумму прчина:" + e.getMessage());
        }

    }

    @Override
    public String getDescription(Integer expenses_id) throws SQLException {
        String getDescriptionQuery = "SELECT description FROM Incomes WHERE expenses_id = ?";

        // Установка уровня изоляции транзакции (пример для READ_COMMITTED)
        Connection connection = ConnectionDatabase.getConnection();
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.setAutoCommit(false); // Начало транзакции

        try (PreparedStatement sumStatement = connection.prepareStatement(getDescriptionQuery)) {
            sumStatement.setInt(1, expenses_id);
            ResultSet resultSet = sumStatement.executeQuery();
            String description = "";
            if (resultSet.next()) {
                description = resultSet.getString("description");
            }
            connection.commit(); // Подтверждение транзакции
            return description;

        } catch (SQLException e) {
            connection.rollback(); // Откат транзакции в случае ошибки
            throw new SQLException("Не удалось получить описания для траты: " + expenses_id + " причина:" + e.getMessage());
        } finally {
            connection.setAutoCommit(true); // Восстановление автокоммита

        }
    }

    @Override
    public String getDescription(String firstname, String secondName, String middleName) throws SQLException {
        String findEmployeeQuery = "SELECT employee_id FROM work_info_employees WHERE first_name = ? AND last_name = ? AND middle_name = ?";

        String getDescriptionQuery = "SELECT description FROM Incomes WHERE expenses_id = ?";

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
        String sumQuery = "SELECT COALESCE(SUM(amount), 0) AS total FROM expenses WHERE employee_id = ?";

        // Установка уровня изоляции транзакции (пример для READ_COMMITTED)
        Connection connection = ConnectionDatabase.getConnection();
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.setAutoCommit(false); // Начало транзакции

        try (PreparedStatement sumStatement = connection.prepareStatement(sumQuery)) {
            sumStatement.setInt(1, candidateId);
            ResultSet resultSet = sumStatement.executeQuery();
            BigDecimal expense = null;
            if (resultSet.next()) {
                expense = BigDecimal.valueOf(resultSet.getDouble("total"));
            } else {
                expense = BigDecimal.ZERO;
            }
            connection.commit(); // Подтверждение транзакции
            return expense;
        } catch (SQLException e) {
            connection.rollback(); // Откат транзакции в случае ошибки
            throw new SQLException("Не удалось получить заработанную сумму кандидата: " + candidateId + " причина:" + e.getMessage());
        } finally {
            connection.setAutoCommit(true); // Восстановление автокоммита
        }
    }

}
