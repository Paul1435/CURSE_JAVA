package org.crock.winterschool;

import org.crock.winterschool.dataAccessObject.Client;
import org.crock.winterschool.dataAccessObject.Pet;
import org.crock.winterschool.database.DataBase;
import org.crock.winterschool.parcer.CSVParser;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Нет пути к файлу");
        }
        DataBase clients = DataBase.newDataBaseBuilder().setTableName("clients").setFields(
                "(client_id INT PRIMARY KEY, " +
                        "last_name VARCHAR(255), " +
                        "first_name VARCHAR(255), " +
                        "phone_number VARCHAR(20))").build();
        DataBase pets = DataBase.newDataBaseBuilder().setTableName("pets")
                .setFields(
                        "(pet_id INT PRIMARY KEY, " +
                                "client_id INT, " +
                                "pet_name VARCHAR(255), " +
                                "age INT, " +
                                "FOREIGN KEY (client_id) REFERENCES clients(client_id))").build();
        try {
            clients.connect("lab15", "");
            pets.connect("lab15", "");
            Set<Client> clientSet = new HashSet<>();
            Set<Pet> petSet = new HashSet<>();
            CSVParser.parseRecords(args[0], clientSet, petSet);
            clients.createTable();
            pets.createTable();
            clients.insertValue(clientSet.stream().toList());
            pets.insertValue(petSet.stream().toList());
            System.out.println(clients);
            System.out.println(pets);
            clients.closeConnect();
            pets.closeConnect();

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}