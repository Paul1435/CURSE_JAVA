package org.crock.winterschool;

import org.crock.winterschool.connection.DatabaseConnection;
import org.crock.winterschool.containers.Client;
import org.crock.winterschool.containers.Pet;
import org.crock.winterschool.csvparser.CSVParser;
import org.crock.winterschool.dao.ClientDao;
import org.crock.winterschool.dao.PetDao;
import org.crock.winterschool.exception.ClientException;

import java.io.IOException;
import java.sql.*;
import java.util.*;


public class Main {
    private static void initDB(Collection<Client> clients, Collection<Pet> pets) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String insertClientRequest = "INSERT INTO clients VALUES(?,?,?,?,?)";
        String insertPetRequest = "INSERT INTO pets VALUES(?,?,?)";
        var petHandler = new PetDao();
        try (PreparedStatement statement = connection.prepareStatement(insertPetRequest)) {
            for (Pet pet : pets) {
                statement.setInt(1, pet.petId());
                statement.setString(2, pet.name());
                statement.setInt(3, pet.age());
                statement.execute();
            }
        }
        try (PreparedStatement statement = connection.prepareStatement(insertClientRequest)) {
            for (Client client : clients) {
                statement.setInt(1, client.clientId());
                statement.setString(2, client.secondName());
                statement.setString(3, client.firstName());
                statement.setString(4, client.numberPhone());
                statement.setInt(5, client.petId());
                statement.execute();
            }
        }
    }

    public static void main(String[] args) {
        String path = "./src/main/resources/data.csv";
        PetDao petHandler = new PetDao();
        ClientDao clientHandler = new ClientDao();
        Set<Client> clients = new HashSet<>();
        Set<Pet> pets = new HashSet<>();
        try {
            CSVParser.clientsAndPetsFill(path, clients, pets);
            initDB(clients, pets);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Проверка вывода таблицы животных:\n");
            System.out.println(petHandler.tableToString());
            System.out.println("Проверка вывода таблицы людей:\n");
            System.out.println(clientHandler.tableToString());
            System.out.println();

            System.out.println("Добавление нового пользователя:");
            System.out.println(clientHandler.createClient(new Client(21, "New USER", "CROCK", "+792178977732", 3)));
            System.out.println(clientHandler.tableToString());
            System.out.println();

            System.out.println("Найти нужно пользователя:");
            System.out.println(clientHandler.findClient(4));
            System.out.println();

            System.out.println("Получить всех животных пользователя:");
            System.out.println(clientHandler.getAllPetsOf(clientHandler.findClient(1)));
            System.out.println();

            System.out.println("Получить номера владельцев зверушки:");
            System.out.println(petHandler.findClientPhoneNumbersBy(petHandler.findPet(1)));


            System.out.println("Обновить пользователя");
            System.out.println(clientHandler.updateClient(new Client(21, "New USER", "CROCK YOUNG GENERATION", "+0000000000", 3)));
            System.out.println();
            System.out.println(clientHandler.tableToString());
            DatabaseConnection.closeConnection();
        } catch (SQLException | ClientException e) {
            e.printStackTrace();
        }
    }
}