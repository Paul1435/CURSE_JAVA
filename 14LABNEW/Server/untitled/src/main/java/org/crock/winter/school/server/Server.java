package org.crock.winter.school.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable, AutoCloseable {

    private final ServerSocket serverSocket;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать сервер, подробнее: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        List<ClientHandler> clientsThread = new ArrayList<>();
        Socket client;
        try {
            while (!serverSocket.isClosed()) {
                client = serverSocket.accept();
                System.out.println("Новый пользователь подключен");
                clientsThread.add(new ClientHandler(client));
                clientsThread.get(clientsThread.size() - 1).start();
            }
        } catch (IOException ignored) {
            System.out.println("Сервер закрыт");
        } finally {
            for (var socket : clientsThread) {
                try {
                    socket.close();
                    socket.interrupt();
                } catch (Exception e) {
                    System.out.println("Socket already closed");
                }
            }
            clientsThread.clear();
        }
    }

    @Override
    public void close() throws Exception {
        serverSocket.close();
        System.out.println("Поток закрыт");
    }
}
