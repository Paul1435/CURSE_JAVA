package org.winterSchool.task3;

public class Main {
    public static boolean isPrime(long num) {
        boolean isPrime = true;
        int sqrtOfNum = (int) Math.sqrt(num);
        for (int i = 2; i < sqrtOfNum + 1 && isPrime; ++i) {
            isPrime = num % i != 0;
        }
        return isPrime;
    }

    public static boolean isTwins(long num) {
        if (num % 2 == 0 || num == 1) {
            return false;
        }
        boolean isTwins = isPrime(num);
        if (isTwins) {
            isTwins = ((num - 2) != 1 && isPrime(num - 2));
            isTwins = (isPrime(num + 2));
        }
        return isTwins;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Вы не ввели никакого значения");
            return;
        }
        long num = Long.parseLong(args[0]);
        System.out.println("Число: " + num
                + (isPrime(num) ? " является " : " не является ") + "простым");
        System.out.println("А также " + (isTwins(num) ? "" : "не") + " является числом близнецом");
    }
}