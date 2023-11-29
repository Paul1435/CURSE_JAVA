package org.crock.winterschool.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;

    static {
        String url = "jdbc:h2:mem:~/test";
        String user = "lab16";
        String pwd = "";
        try {
            connection = DriverManager.getConnection(url, user, pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new SQLException("Не удалось закрыть соединение, причина" + e.getMessage());
        }
    }
}
