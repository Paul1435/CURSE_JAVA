package org.school;


import org.school.Client.Client;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            String name;
            System.out.println("Введите свой ник: ");
            name = sc.nextLine();
            try (Client client = new Client("localhost", 8000, name)) {
                client.start();
                System.out.println("Чтобы выйти впишите команду q");
                while (client.isConnected()) {

                }
            }
        }
    }
}