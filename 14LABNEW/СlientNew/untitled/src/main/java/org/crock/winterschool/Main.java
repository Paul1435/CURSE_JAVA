package org.crock.winterschool;


public class Main {

    public static void main(String[] args) {
        Client paul = new Client("localhost", 8000, "Petr");
        paul.start();
    }
}