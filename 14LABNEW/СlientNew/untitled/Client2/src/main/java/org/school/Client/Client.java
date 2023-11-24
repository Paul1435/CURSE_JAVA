package org.school.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client implements Writable {
    private final String name;
    private Socket socket;
    private BufferedReader reader;
    private final String address;
    private final int PORT;
    private PrintWriter writer;
    private ExecutorService executorService;

    public Client(String address, int port, String name) {
        Objects.requireNonNull(name, "Не ввели имя");
        this.name = name;
        Objects.requireNonNull(address, "Не ввели address");
        this.address = address;
        if (port <= 0) {
            throw new IllegalArgumentException("Порт не может быть отрицательным");
        }
        PORT = port;
    }

    public void start() {
        try {
            this.socket = new Socket(this.address, PORT);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.executorService = Executors.newFixedThreadPool(2);

            writer.println("NEW CLIENT: " + name);
            executorService.submit(this::receiveMessages);
            executorService.submit(this::sendMessages);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось подключиться к серверу");
        }
    }

    private void receiveMessages() {
        try {
            while (true) {
                String receivedMessage = reader.readLine();
                System.out.println(receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessages() {
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                sendMessage(sc.nextLine());
            }
        }
    }

    public void sendMessage(String message) {
        writer.println(this.name + ": " + message);
    }

    public void close() {
        try {
            executorService.shutdownNow(); // Terminate all threads
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}