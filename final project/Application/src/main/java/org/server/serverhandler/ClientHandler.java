package org.server.serverhandler;

import org.server.dao.DaoEmployee;
import org.server.serverhandler.transmitterAndReceiver.ObjectReceiver;
import requests.*;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;

public class ClientHandler implements Runnable {
    private static final List<Socket> allClients = Collections.synchronizedList(new ArrayList<>());

    private final Socket socket;

    public static enum Role {MANAGER, CANDIDATE}

    private Role role;
    private final int idClient;
    private final ReaderAndWriter reader;

    public ClientHandler(Socket client, int id) {
        socket = client;
        idClient = id;
        try {
            this.role = Role.valueOf(DaoEmployee.getInstance().getRole(id).toUpperCase());
        } catch (SQLException e) {
            System.err.println("Не найдена роль");
        }
        allClients.add(client);
        reader = new ReaderAndWriter(socket);
        reader.broadcast(role.name());
    }

    /**
     * {@link #run() Start of ClientHandler}
     */
    @Override
    public void run() {
        reader.run();
    }


    private class ReaderAndWriter implements Runnable {
        private final Socket src;

        public ReaderAndWriter(Socket client) {
            src = client;
            System.out.println("is opened" + src.isClosed());
        }

        @Override
        public void run() {
            try {
                while (true) {
                    try {
                        Object request = ObjectReceiver.receiveObject(socket);
                        if (request instanceof MoneyRequest moneyRequest) {
                            Requests.doMoneyRequest(idClient, moneyRequest);
                            System.out.println("Операция прошла успешно");
                            broadcast("Запрос отправлен");
                        } else if (request instanceof IncomeInfoRequest) {
                            broadcast(Requests.doPersonalIncomeRequest(idClient));
                        } else if (request instanceof ExpenseInfoRequest) {
                            broadcast(Requests.doPersonalExpenseRequest(idClient));
                        } else if (request instanceof SetIncomeRequest income) {
                            Requests.addIncome(idClient, income);
                        } else if (request instanceof GetListMoneyRequest) {
                            broadcast(Requests.getList());
                        } else {
                            broadcast("Неопознанный запрос");
                        }
                    } catch (ClassNotFoundException | SQLException e) {
                        System.out.println("Не удалось найти запрос");
                        broadcast("Не удалось выполнить запрос");
                    }
                }
            } catch (IOException e) {
                if (src.isClosed()) {
                    removeClient(src);
                    System.out.println("Пользователь отключился");
                } else {
                    System.out.println("Не удалось прочитать запрос: " + e.getMessage());
                    broadcast("Не удалось выполнить запрос");
                }
            }
        }


        private void broadcast(String message) {
            try {
                PrintWriter clientOut = new PrintWriter(this.src.getOutputStream(), true);
                clientOut.println(message);
            } catch (IOException e) {
                System.out.println("Пользователь отключился");
            }
        }
    }

    private static void removeClient(Socket client) {
        try {
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            System.out.println("Пользователь отключился");
        }
        allClients.remove(client);
    }
}