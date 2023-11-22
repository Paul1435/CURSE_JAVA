package org.crock.winterschool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private final static int PORT = 8000;
    private ArrayList<ClientHandler> clients = new ArrayList<>();

    public Server() {
        Socket newClient = null;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен");
            while (true) {
                newClient = serverSocket.accept();
                ClientHandler client = new ClientHandler(newClient, this);
                clients.add(client);
                new Thread(client).start();
            }

        } catch (IOException e) {
            System.err.println("Не удалось подключиться к порту " + PORT);
        } finally {
            try {
                newClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void sendMessageToAllClients(String message, ClientHandler clientToIgnore) {
        SendMessage.sendMessage(clients, clientToIgnore, message);
    }
}
