package org.crock.winter.school;

import org.crock.winter.school.server.Server;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Server server = new Server(8000)) {
            Thread start = new Thread(server);
            start.start();
            Scanner sc = new Scanner(System.in);
            while (!sc.next().equals("q")) {

            }
            sc.close();
            start.interrupt();
        } catch (Exception e) {
            System.err.println("Сервер на данном порте и адресе уже запущен");
        }
    }
}