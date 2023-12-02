package org.crock.winterschool;

import autorization.EmployeeRegister;
import autorization.Entrance;
import org.crock.winterschool.candidate.Candidate;
import org.crock.winterschool.manager.Manager;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean stop = false;

        while (!stop) {
            System.out.println("Выберите пункт:\n1. Регистрация\n2. Вход");
            String command = sc.next();

            if (command.equalsIgnoreCase("1") || command.equalsIgnoreCase("регистрация")) {
                EmployeeRegister employee = registerNewEmployee(sc);
                try (Socket socket = new Socket("localhost", 5555)) {
                    if (processRegistration(employee, socket, sc)) {
                        stop = true;
                        System.out.println("Регистрация прошла успешно, зайдите в свой аккаунт");
                    }
                } catch (IOException e) {
                    System.out.println("Нет подключения к серверу");
                }
            } else {
                Entrance entrance = loginExistingEmployee(sc);
                try (Socket socket = new Socket("localhost", 5555)) {
                    if (processLogin(entrance, socket, sc)) {
                        stop = true;
                    }
                } catch (IOException e) {
                    System.out.println("Нет подключения к серверу");
                }
            }
        }
        sc.close();
    }

    private static EmployeeRegister registerNewEmployee(Scanner sc) {
        System.out.println("Введите данные пользователя:\nЛогин: ");
        String login = sc.next();
        System.out.println("\nВведите Пароль: ");
        String password = String.valueOf(sc.next().hashCode());
        System.out.println("\nВведите Имя: ");
        String firstName = sc.next();
        System.out.println("\nВведите фамилию: ");
        String secondName = sc.next();
        System.out.println("\nВведите отчество: ");
        String middleName = sc.next();

        while (true) {
            System.out.println("Выберите свою должность:\n1. Менеджер\n 2. Кандидат ");
            String role = sc.next();

            if ("1".equalsIgnoreCase(role)) {
                return new EmployeeRegister(login, password, firstName, secondName, middleName, EmployeeRegister.Role.MANAGER);
            } else if ("2".equalsIgnoreCase(role)) {
                return new EmployeeRegister(login, password, firstName, secondName, middleName, EmployeeRegister.Role.CANDIDATE);
            } else {
                System.out.println("Вы должны ввести 1 или 2, попробуйте еще раз");
            }
        }
    }

    private static boolean processRegistration(EmployeeRegister employee, Socket socket, Scanner sc) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            objectOutputStream.writeObject(employee);
            objectOutputStream.flush();

            String serverMessage = reader.readLine();
            System.out.println("Сообщение от сервера: " + serverMessage);

            if (serverMessage.toLowerCase().contains("пользователь с таким логином уже есть")) {
                System.out.println("Придумайте другой логин");
                return false;
            }
            return true;
        }
    }

    private static Entrance loginExistingEmployee(Scanner sc) {
        System.out.println("\nВведите логин: ");
        String login = sc.next();
        System.out.println("Введите пароль");
        String password = String.valueOf(sc.next().hashCode());
        return new Entrance(login, password);
    }

    private static boolean processLogin(Entrance entrance, Socket socket, Scanner sc) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            objectOutputStream.writeObject(entrance);
            objectOutputStream.flush();

            String serverMessage = reader.readLine();
            System.out.println("Сообщение от сервера: " + serverMessage);

            if (serverMessage.toLowerCase().contains("неправильный")) {
                return false;
            }
            try {
                var roleEnum = EmployeeRegister.Role.valueOf(serverMessage.toUpperCase());
                if (roleEnum == EmployeeRegister.Role.CANDIDATE) {
                    new Candidate(socket).processCandidature();
                } else if (roleEnum == EmployeeRegister.Role.MANAGER) {
                    new Manager(socket).processManager();
                }
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true;
    }
}
