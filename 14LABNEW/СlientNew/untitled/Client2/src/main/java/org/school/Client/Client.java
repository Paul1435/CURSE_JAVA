package org.school.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client implements Writable, AutoCloseable {
    private final String name;
    private Socket socket;
    private volatile boolean isConnected;
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
            isConnected = true;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
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
            while (isConnected) {
                String receivedMessage = reader.readLine();
                if (receivedMessage == null) {
                    isConnected = false;
                    throw new IOException();
                }
                System.out.println(receivedMessage);
            }
        } catch (IOException e) {
            System.err.println("Cоединение было закрыт+ " + e.getMessage());
        } finally {
            try {
                reader.close();
            } catch (IOException ignored) {

            }
        }
    }

    private void sendMessages() {
        try (Scanner sc = new Scanner(System.in)) {
            while (isConnected) {
                String message = sc.nextLine();
                if (message.equalsIgnoreCase("q")) {
                    isConnected = false;
                    break;
                }
                sendMessage(message);
            }
        } finally {
            writer.close();
        }
    }

    public void sendMessage(String message) {
        writer.println(this.name + ": " + message);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void close() {
        try {
            socket.close();
            System.out.println("Сокет закрыт");
            executorService.shutdownNow();
            writer.close();
            reader.close();
            System.out.println("Ресурсы очищены");
        } catch (Exception e) {
            System.out.println("Ресурсы уже были закрыт");
        }
    }
}