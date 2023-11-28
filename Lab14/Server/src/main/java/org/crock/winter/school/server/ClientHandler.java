package org.crock.winter.school.server;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientHandler implements Runnable {
    /**
     * allClients - содержит всех пользователей, нужен для отправки им сообщений. Как только Сокет закрывается,
     * allClients удаляет пустой сокет из потока (90 строка)
     */
    private static List<Socket> allClients = Collections.synchronizedList(new ArrayList<>());

    private Socket socket;
    private ReaderAndWriter reader;

    public ClientHandler(Socket client) {
        socket = client;
        allClients.add(client);
        // writer = new Writer(socket);
        reader = new ReaderAndWriter(socket);
    }

    /**
     * {@link #run() Start of ClientHandler}
     */
    @Override
    public void run() {
        reader.run();
    }


    // Когда читаем сообщение пользователя, то отправляем его другим пользователям
    private static class ReaderAndWriter implements Runnable {
        private BufferedReader in;
        private Socket src;

        public ReaderAndWriter(Socket client) {
            try {
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                src = client;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String message = in.readLine();
                    if (message == null) {
                        break;
                    }
                    broadcast(message, src);
                }
            } catch (IOException e) {
                removeClient(src);
                e.printStackTrace();
            }
        }

        private void broadcast(String message, Socket srcToIgnore) {
            for (Socket client : allClients) {
                if (client == srcToIgnore) {
                    continue;
                }
                try {
                    PrintWriter clientOut = new PrintWriter(client.getOutputStream(), true);
                    clientOut.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void removeClient(Socket client) {
        allClients.remove(client);
    }
}
