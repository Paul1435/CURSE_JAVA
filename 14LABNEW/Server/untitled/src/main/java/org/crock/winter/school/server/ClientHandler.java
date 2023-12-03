package org.crock.winter.school.server;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientHandler extends Thread implements AutoCloseable {
    /**
     * allClients - содержит всех пользователей, нужен для отправки им сообщений. Как только Сокет закрывается,
     * allClients удаляет пустой сокет из потока (90 строка)
     */
    private static final List<Socket> allClients = Collections.synchronizedList(new ArrayList<>());

    private final Socket socket;
    private static final Object lock = new Object();
    private final ReaderAndWriter reader;

    public ClientHandler(Socket client) {
        socket = client;
        allClients.add(client);
        reader = new ReaderAndWriter(socket);
    }

    @Override
    public void run() {
        reader.run();
    }

    @Override
    public void close() throws Exception {
        socket.close();
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
                System.out.println("Пользователь был отключен");
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
                System.out.println("Пользователь был отключен");
            } finally {
                removeClient(src);
            }
        }

        private void broadcast(String message, Socket srcToIgnore) {
            synchronized (lock) {
                for (Socket client : allClients) {
                    if (client == srcToIgnore) {
                        continue;
                    }
                    try {
                        PrintWriter clientOut = new PrintWriter(client.getOutputStream(), true);
                        clientOut.println(message);
                    } catch (IOException e) {
                        System.out.println("Не удалось отправить сообщение клиенту");
                    }
                }
            }
        }
    }

    private static void removeClient(Socket client) {
        allClients.remove(client);
    }

}
