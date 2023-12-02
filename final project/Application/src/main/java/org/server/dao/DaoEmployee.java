package org.server.dao;

import autorization.EmployeeRegister;
import org.server.database.ConnectionDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoEmployee {
    public static synchronized DaoEmployee getInstance() {
        if (instance == null) {
            instance = new DaoEmployee();
        }
        return instance;
    }

    private DaoEmployee() {
    }

    private static DaoEmployee instance;


    public String getRole(String login) throws SQLException {
        String findRequest = "SELECT post FROM work_info_employees WHERE username = ?";
        try (PreparedStatement statement = ConnectionDatabase.getConnection().prepareStatement(findRequest)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("post");
                } else {
                    throw new SQLException("Не найден пользователь");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public int getId(String login) throws SQLException {
        String findRequest = "SELECT employee_id FROM work_info_employees WHERE username = ?";
        try (PreparedStatement statement = ConnectionDatabase.getConnection().prepareStatement(findRequest)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("employee_id");
                } else {
                    throw new SQLException("Не найден пользователь");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public String getRole(Integer id) throws SQLException {
        String findRequest = "SELECT post FROM work_info_employees WHERE employee_id = ?";
        try (PreparedStatement statement = ConnectionDatabase.getConnection().prepareStatement(findRequest)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("post");
                } else {
                    throw new SQLException("Не найден пользователь");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Неизвестная ошибка: " + e.getMessage());
        }
    }


    public boolean checkUserLogin(String username, String password) {
        String findRequest = "SELECT * FROM work_info_employees WHERE username = ? AND password_hash = ?";
        try (PreparedStatement statement = ConnectionDatabase.getConnection().prepareStatement(findRequest)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private static boolean checkLogin(String username) {
        String findRequest = "SELECT * FROM work_info_employees WHERE username = ?";
        try (PreparedStatement statement = ConnectionDatabase.getConnection().prepareStatement(findRequest)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean register(EmployeeRegister info) {
        boolean isAuthenticate = checkLogin(info.username());
        if (isAuthenticate) {
            return false;
        }
        String addRequest = "INSERT INTO work_info_employees (last_name, first_name, middle_name, post, username, password_hash)" +
                " VALUES (?,?,?,?,?,?)";
        try (PreparedStatement statement = ConnectionDatabase.getConnection().prepareStatement(addRequest)) {
            statement.setString(1, info.lastName());
            statement.setString(2, info.firstName());
            statement.setString(3, info.middleName());
            statement.setString(4, info.role().name().toLowerCase());
            statement.setString(5, info.username());
            statement.setString(6, info.password());
            statement.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

}
