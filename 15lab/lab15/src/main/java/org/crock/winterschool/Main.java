package org.crock.winterschool;

import org.crock.winterschool.dataAccessObject.Client;
import org.crock.winterschool.dataAccessObject.Pet;
import org.crock.winterschool.database.DataBaseTableHelper;
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
        DataBaseTableHelper clients = DataBaseTableHelper.newDataBaseBuilder().setTableName("clients").setFields(
                        "(client_id INT NOT NULL, " +
                        "last_name VARCHAR(255) NOT NULL, " +
                        "first_name VARCHAR(255) NOT NULL, " +
                        "phone_number VARCHAR(20) NOT NULL, " +
                        "pet_id INT NOT NULL, " +
                        "FOREIGN KEY (pet_id) REFERENCES pets(pet_id))"
        ).build();
        DataBaseTableHelper pets = DataBaseTableHelper.newDataBaseBuilder().setTableName("pets")
                .setFields(
                        "(pet_id INT PRIMARY KEY NOT NULL, " +
                                "pet_name VARCHAR(255) NOT NULL, " +
                                "age INT)").build();
        try {
            pets.connect("lab15", "");
            clients.connect("lab15", "");
            Set<Client> clientSet = new HashSet<>();
            Set<Pet> petSet = new HashSet<>();
            CSVParser.clientsAndPetsFill(args[0], clientSet, petSet);
            pets.createTable();
            clients.createTable();
            pets.insertValue(petSet.stream().toList());
            clients.insertValue(clientSet.stream().toList());
            System.out.println(clients);
            System.out.println(pets);
            clients.closeConnect();
            pets.closeConnect();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}