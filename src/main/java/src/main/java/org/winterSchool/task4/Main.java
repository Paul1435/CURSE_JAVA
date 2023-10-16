package org.winterSchool.task4;

public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Вы не ввели недостаточно аргументов");
            return;
        }
        int element = Integer.parseInt(args[0]);
        int difference = Integer.parseInt(args[1]);
        int NumOfProgressionMembers = Integer.parseInt(args[2]);
        if (NumOfProgressionMembers < 0) {
            System.out.println("Количество членов прогрессии не может быть отрицательным");
            return;
        }
        long sum = 0;
        for (int i = 0; i < NumOfProgressionMembers; i++) {
            sum += element;
            element += difference;
        }
        System.out.println("sum = " + sum);
    }
}