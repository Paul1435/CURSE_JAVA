package org.example;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Lot lot = null;
        try {
            lot = Reader.readLot("./src/main/resources/test.txt", 1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (lot == null) {
            System.out.println("Конец программы");
            return;
        }

        ArrayList<String> listOfParticipants = null;
        try {
            listOfParticipants = Reader.readListOfParticipants("./src/main/resources/participants.txt");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (listOfParticipants == null) {
            return;
        }
        ArrayList<Thread> people = new ArrayList<>(listOfParticipants.size());
        final Lot finalLot = lot;
        int counter = 0;
        // Запуск аукциона
        for (String participant : listOfParticipants) {
            people.add(new Thread(() -> {
                for (int i = 0; i < 100; i++) {
                    int bidAmount = Math.abs((int) (Math.random() * 10000000));
                    finalLot.makeBet(new BigDecimal(bidAmount), participant);
                    System.out.println(participant + " placed bid: " + bidAmount);
                }
            }));
            people.get(counter++).start();


            System.out.println(finalLot.getBet());
            System.out.println(finalLot.getWinner());
        }
        for (var person : people) {
            try {
                person.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Winner:" + finalLot.getWinner() + " " + finalLot.getBet());
    }
}