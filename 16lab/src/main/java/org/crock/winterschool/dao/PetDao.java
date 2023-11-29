package org.crock.winterschool.dao;

import org.crock.winterschool.connection.DatabaseConnection;
import org.crock.winterschool.containers.Client;
import org.crock.winterschool.containers.Pet;
import org.crock.winterschool.exception.ClientException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDao implements PetDaoInterface {
    public static Connection connection = DatabaseConnection.getConnection();

    static {
        try (Statement statement = connection.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS pets " +
                    "(pet_id INT PRIMARY KEY NOT NULL, " +
                    "pet_name VARCHAR(255) NOT NULL, " +
                    "age INT)";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Pet createPet(String name, Integer age, List<Client> clients) throws SQLException {
        String insertRequest = "INSERT INTO pets VALUES(?,?,?)";
        Pet pet = null;
        try (PreparedStatement statement = connection.prepareStatement(insertRequest)) {
            int id = getLastPetId() + 1;
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setInt(3, age);
            statement.execute();
            pet = new Pet(id, name, age);
        }
        ClientDao clientHandler = new ClientDao();
        for (Client client : clients) {
            try {
                clientHandler.createClient(client);
            } catch (SQLException | ClientException e) {
                e.printStackTrace();
                continue;
            }
        }
        return pet;

    }

    public int getLastPetId() throws SQLException {

        try (Statement statement = connection.createStatement()) {
            try (ResultSet result = statement.executeQuery("SELECT pet_id FROM pets ORDER BY pets.pet_id DESC")) {
                if (result.next()) {
                    return result.getInt("pet_id");
                }
                return 0;
            }
        }
    }


    @Override
    public Pet findPet(Integer medicalCardNumber) throws SQLException {
        String request = "SELECT * FROM pets WHERE pet_id = ?";
        Pet pet = null;

        try (PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setInt(1, medicalCardNumber);

            try (ResultSet resultOfFind = statement.executeQuery()) {
                if (resultOfFind.next()) {
                    int retrievedPetId = resultOfFind.getInt("pet_id");
                    String petName = resultOfFind.getString("pet_name");
                    int age = resultOfFind.getInt("age");
                    pet = new Pet(retrievedPetId, petName, age);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to execute pet search: " + e.getMessage());
        }

        return pet;
    }

    @Override
    public Pet updatePet(Pet pet) throws SQLException {
        String requestToUpdate = "UPDATE pets set pet_name = ?," + " age = ?" +
                " WHERE pet_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(requestToUpdate)) {
            statement.setString(1, pet.name());
            statement.setInt(2, pet.age());
            statement.setInt(3, pet.petId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Не удалось обновить");
        }
        return pet;
    }

    @Override
    public void deletePet(Integer medicalCardNumber) throws SQLException {
        String deleteRequest = "DELETE FROM pets WHERE pet_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteRequest)) {
            statement.setInt(1, medicalCardNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Не удалось удалить пользователя");
        }
    }

    @Override
    public List<String> findClientPhoneNumbersBy(Pet pet) throws SQLException {
        String getNumberPhones = "SELECT phone_number FROM clients WHERE clients.pet_id = ?";
        List<String> clientPhoneNumbers = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(getNumberPhones)) {
            statement.setInt(1, pet.petId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    clientPhoneNumbers.add(resultSet.getString(1));
                }
            }
        }
        return clientPhoneNumbers;
    }


    public String tableToString() throws SQLException {
        StringBuilder result = null;
        try (Statement statement = connection.createStatement()) {
            result = new StringBuilder("pets" + "\n");
            String selectQuery = "SELECT * FROM pets" + " ORDER BY " + "pet_id" + " ASC";
            try (ResultSet resultSet = statement.executeQuery(selectQuery)) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Append column names
                for (int i = 1; i <= columnCount; i++) {
                    result.append(metaData.getColumnName(i)).append("\t");
                }
                result.append("\n");

                // Append rows
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        result.append(resultSet.getString(i)).append("\t");
                    }
                    result.append("\n");
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Не удалось заполнить строку таблицей");
        }
        return result == null ? null : result.toString();
    }
}
