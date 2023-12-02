package org.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConnectionDatabase - класс  синглтон который выдает на соединении к нашему БД
 */
public class ConnectionDatabase implements AutoCloseable {
    private static Connection connection = null;

    static {
        String url = "jdbc:h2:mem:~/test";
        String user = "Server";
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


    @Override
    public void close() throws Exception {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new Exception("Не удалось закрыть соединение, причина" + e.getMessage());
        }

    }
}
