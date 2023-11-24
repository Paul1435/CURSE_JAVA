package org.school;


import org.school.Client.Client;

public class Main {
    public static void main(String[] args) {
        Client Petr = new Client("localhost", 8000, "Paul");
        Petr.start();
    }
}