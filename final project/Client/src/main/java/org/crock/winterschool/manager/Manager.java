package org.crock.winterschool.manager;

import requests.Requests;

import java.net.Socket;
import java.util.Scanner;

public record Manager(Socket socket) {
    public void processManager() {
        Scanner sc = new Scanner(System.in);
        System.out.println(socket.isClosed());
        int codeOperation = 0;
        while (codeOperation != 5) {
            System.out.println("PROCESSING MANAGER");
            System.out.println("Выберите запрос, который хотите сделать:");
            System.out.println("1. Посмотреть список заявок на расходы");
            System.out.println("5. Выйти");
            codeOperation = sc.nextInt();
            switch (codeOperation) {
                case 1:
                    String ans = Requests.getListMoneyRequest(socket);
                    System.out.println(ans);
                    break;
                default:
                    break;
            }
        }
        sc.close();
        System.out.println("До свидания");
    }
}
