package org.server.serverhandler;

import autorization.EmployeeRegister;
import autorization.Entrance;
import org.server.dao.DaoEmployee;
import org.server.exceptions.CheckAuthorizationException;
import org.server.serverhandler.transmitterAndReceiver.ObjectReceiver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Server implements AutoCloseable {


    private List<Thread> clientsThread;
    private final int PORT;
    private volatile boolean start;

    public Server(int port) {
        PORT = port;
        start = false;
    }

    public void start() {
        clientsThread = new ArrayList<>();
        Socket client = null;
        start = true;
        try (var serverSocket = new ServerSocket(PORT)) {
            while (start) {
                client = serverSocket.accept();
                try {
                    int idClient = checkInput(client);
                    ClientHandler clientHandler = new ClientHandler(client, idClient);
                    clientsThread.removeIf(el -> !el.isAlive()); //
                    clientsThread.add(new Thread(clientHandler));
                    clientsThread.get(clientsThread.size() - 1).start();
                } catch (CheckAuthorizationException e) {
                    disconnectSocket(client, e.getMessage());
                    continue;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                client.close();
                for (var socket : clientsThread) {
                    socket.interrupt();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private int checkInput(Socket socket) throws CheckAuthorizationException {
        try {
            Object receivedObject = ObjectReceiver.receiveObject(socket);
            if (receivedObject instanceof Entrance entrance) {
                boolean isCorrect = DaoEmployee.getInstance().checkUserLogin(entrance.login(), entrance.password());
                if (!isCorrect) {
                    throw new CheckAuthorizationException("Неверный логин или пароль");
                }
                return DaoEmployee.getInstance().getId(entrance.login());
            } else if (receivedObject instanceof EmployeeRegister employeeInfo) {
                boolean isAdded = DaoEmployee.getInstance().register(employeeInfo);
                if (!isAdded) {
                    throw new CheckAuthorizationException("Пользователь с таким логином уже есть");
                }

                return DaoEmployee.getInstance().getId(employeeInfo.username());

            } else {
                throw new IOException();
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            throw new CheckAuthorizationException("Технические неполадки не сервере, обратитесь к администратору " + e.getMessage());
        }
    }

    private void disconnectSocket(Socket socket, String message) {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(message);
            socket.close();
        } catch (IOException e) {
            System.out.println("Пользователь сам закрыл соединение");

        }
    }

    // Остановить работу потока
    private void stop() {
        start = false;
        for (Thread thread : clientsThread) {
            thread.interrupt();
        }
        clientsThread.clear();
    }


    @Override
    public void close() throws Exception {
        stop();
    }
}