package org.crock.winterschool.candidate;

import requests.ExpenseRequest;
import requests.IncomeInfoRequest;
import requests.Requests;
import requests.SetIncomeRequest;

import java.math.BigDecimal;
import java.net.Socket;
import java.util.Scanner;

public record Candidate(Socket socket) {
    public void processCandidature() {
        Scanner sc = new Scanner(System.in);
        System.out.println(socket.isClosed());
        int codeOperation = 0;
        while (codeOperation != 5) {
            System.out.println("PROCESSING CANDIDATURE");
            System.out.println("Выберите запрос, который хотите сделать:");
            System.out.println("1. отправить заявку на расход");
            System.out.println("2. узнать о своих доходах");
            System.out.println("3. узнать о своих расходах");
            System.out.println("4. добавить запись о доходе");
            System.out.println("5. выйти");
            codeOperation = sc.nextInt();
            switch (codeOperation) {
                case 1:
                    System.out.println("Введите сумму, которую хотите получить\n");
                    double price = 0;
                    while (true) {
                        price = sc.nextDouble();
                        if (price <= 0) {
                            System.out.println("Цена не может быть отрицательной, попробуйте еще раз");
                            continue;
                        }
                        break;

                    }
                    System.out.println("Напишите описание, зачем вам нужны деньги");
                    String description;
                    while (true) {
                        description = sc.next();
                        if (description == null || description.isEmpty()) {
                            System.out.println("Вы не ввели причину, попробуйте еще раз");
                            continue;
                        }
                        break;
                    }
                    String ans = Requests.requestForGetMoney(BigDecimal.valueOf(price), description, socket);
                    System.out.println(ans);
                    break;
                case 2:
                    String answer = Requests.incomeInfoRequest(new IncomeInfoRequest(), socket);
                    System.out.println(answer);
                    break;
                case 3:
                    String answerExpense = Requests.expenseRequest(new ExpenseRequest(), socket);
                    System.out.println(answerExpense);
                    break;
                case 4:
                    System.out.println("Введите сумму, которую вы заработали\n");
                    double income = 0;
                    while (true) {
                        income = sc.nextDouble();
                        if (income <= 0) {
                            System.out.println("сумма не может быть отрицательной, попробуйте еще раз");
                            continue;
                        }
                        break;

                    }
                    System.out.println("Напишите описание, как вы получили эти деньги");
                    String descriptionIncome;
                    while (true) {
                        descriptionIncome = sc.next();
                        if (descriptionIncome == null || descriptionIncome.isEmpty()) {
                            System.out.println("Вы не ввели причину, попробуйте еще раз");
                            continue;
                        }
                        break;
                    }
                    Requests.incomeSetRequest(new SetIncomeRequest(BigDecimal.valueOf(income), descriptionIncome), socket);
                    break;
                default:
                    break;
            }
        }
        sc.close();
        System.out.println("До свидания");
    }

}
