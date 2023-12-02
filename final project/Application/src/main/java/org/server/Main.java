package org.server;


import org.server.dao.DaoIncome;
import org.server.database.ConnectionDatabase;
import org.server.database.DatabaseHelper;
import org.server.serverhandler.Server;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(ConnectionDatabase.getConnection());
        try (Server server = new Server(5555)) {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            databaseHelper.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}