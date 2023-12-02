package org.server.dao;

import org.server.database.ConnectionDatabase;
import requests.MoneyRequest;

import java.math.BigDecimal;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DaoMoneyRequest {
    public static synchronized DaoMoneyRequest getInstance() {
        if (instance == null) {
            instance = new DaoMoneyRequest();
        }
        return instance;
    }

    private DaoMoneyRequest() {
    }

    private static DaoMoneyRequest instance;

    public String allRecords(){
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM money_requests";

        try (
                Statement statement = ConnectionDatabase.getConnection().createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int requestId = resultSet.getInt("request_id");
                int employeeId = resultSet.getInt("employee_id");
                BigDecimal amount = resultSet.getBigDecimal("amount");
                String description = resultSet.getString("description");
                String status = resultSet.getString("status");
                Date dateRequested = resultSet.getDate("date_requested");
                Date dateResponded = resultSet.getDate("date_responded");

                // Формируйте строку с результатами
                result.append("Request ID: ").append(requestId)
                        .append(", Employee ID: ").append(employeeId)
                        .append(", Amount: ").append(amount)
                        .append(", Description: ").append(description)
                        .append(", Status: ").append(status)
                        .append(", Date Requested: ").append(dateRequested)
                        .append(", Date Responded: ").append(dateResponded)
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Возвращаем сформированную строку с результатами
        return result.toString();
    }
    public void insertMoneyRequest(int id, MoneyRequest moneyRequest) throws SQLException {
        synchronized (this) {
            String sql = "INSERT INTO money_requests (employee_id, amount, description, status, date_requested, date_responded) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            ZonedDateTime moscowTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
            try (PreparedStatement preparedStatement = ConnectionDatabase.getConnection().prepareStatement(sql)) {

                preparedStatement.setInt(1, id);
                preparedStatement.setBigDecimal(2, moneyRequest.getDecimal());
                preparedStatement.setString(3, moneyRequest.getDescription());
                preparedStatement.setString(4, "В обработке");

                preparedStatement.setDate(5, Date.valueOf(moscowTime.toLocalDate()));

                preparedStatement.setDate(6, null);

                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new SQLException("Не удалось заполнить в таблицу");
            }
        }
    }
}
