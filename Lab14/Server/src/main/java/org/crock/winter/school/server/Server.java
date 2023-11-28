package org.crock.winter.school.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
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
                ClientHandler clientHandler = new ClientHandler(client);
                boolean reused = false;
                //Попытки не занимать лишнюю память, здесь пригодилось бы, наверное ExecutorService
                for (Thread thread : clientsThread) {
                    if (!thread.isAlive()) {
                        thread.interrupt();
                        thread = new Thread(clientHandler);
                        thread.start();
                        reused = true;
                        break;
                    }
                }
                // Если все потоки работают, значит, создаем новый
                if (!reused) {
                    Thread newClientThread = new Thread(clientHandler);
                    clientsThread.add(newClientThread);
                    newClientThread.start();
                }
                clientsThread.add(new Thread(clientHandler));
                clientsThread.get(clientsThread.size() - 1).start();
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

    // Остановить работу потока
    public void stop() {
        start = false;
        for (Thread thread : clientsThread) {
            thread.interrupt();
        }
        clientsThread.clear();
    }


}
