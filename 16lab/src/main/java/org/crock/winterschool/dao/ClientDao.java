package org.crock.winterschool.dao;

import org.crock.winterschool.connection.DatabaseConnection;
import org.crock.winterschool.containers.Client;
import org.crock.winterschool.containers.Pet;
import org.crock.winterschool.exception.ClientException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDao implements ClientDaoInterface {
    private static final Connection connection = DatabaseConnection.getConnection();

    static {
        try (Statement statement = connection.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS clients " +
                    "(client_id INT NOT NULL, " +
                    "last_name VARCHAR(255) NOT NULL, " +
                    "first_name VARCHAR(255) NOT NULL, " +
                    "phone_number VARCHAR(20) NOT NULL, " +
                    "pet_id INT NOT NULL, " +
                    "FOREIGN KEY (pet_id) REFERENCES pets(pet_id))";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Client createClient(Client client) throws ClientException, SQLException {

        if (findClient(client.clientId()) != null) {
            throw new ClientException(client, "Такой клиент уже существует в базе");
        }
        String insertRequest = "INSERT INTO clients VALUES(?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(insertRequest)) {
            statement.setInt(1, client.clientId());
            statement.setString(2, client.secondName());
            statement.setString(3, client.firstName());
            statement.setString(4, client.numberPhone());
            statement.setInt(5, client.petId());
            statement.execute();
        }
        return client;
    }

    @Override
    public Client findClient(Integer id) throws SQLException {
        String request = "SELECT * FROM clients WHERE clients.client_id = ?";
        Client client = null;

        try (PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setInt(1, id);
            try (ResultSet resultOfFind = statement.executeQuery()) {
                if (resultOfFind.next()) {
                    int clientId = resultOfFind.getInt("client_id");
                    String firstName = resultOfFind.getString("first_name");
                    String secondName = resultOfFind.getString("last_name");
                    int petId = resultOfFind.getInt("pet_id");
                    String numberPhone = resultOfFind.getString("phone_number");
                    client = new Client(clientId, firstName, secondName, numberPhone, petId);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to execute client search: " + e.getMessage());
        }
        return client;
    }

    @Override
    public Client updateClient(Client client) throws ClientException {
        String requestToUpdate = "UPDATE clients set last_name = ?," + " first_name = ?," +
                " phone_number = ? WHERE client_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(requestToUpdate)) {
            statement.setString(1, client.secondName());
            statement.setString(2, client.firstName());
            statement.setString(3, client.numberPhone());
            statement.setInt(4, client.clientId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new ClientException(client, "Не удалось обновить");
        }
        return client;
    }

    @Override
    public List<Pet> getAllPetsOf(Client client) throws SQLException {
        String requestALlPets = "SELECT pet_id FROM clients WHERE client_id = ?";
        List<Pet> pets = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(requestALlPets)) {
            statement.setInt(1, client.clientId());
            try (ResultSet resultSet = statement.executeQuery()) {
                PetDao petDao = new PetDao();
                while (resultSet.next()) {
                    pets.add(petDao.findPet(resultSet.getInt(1)));
                }
            }
        }
        return pets;
    }

    @Override
    public void deleteClient(Integer id) throws ClientException, SQLException {
        String deleteRequest = "DELETE FROM clients WHERE client_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteRequest)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ClientException(findClient(id), "Не удалось удалить пользователя");
        }
    }

    public String tableToString() throws SQLException {
        StringBuilder result = null;
        try (Statement statement = connection.createStatement()) {
            result = new StringBuilder("clients" + "\n");
            String selectQuery = "SELECT * FROM clients" + " ORDER BY " + "client_id" + " ASC";
            try (ResultSet resultSet = statement.executeQuery(selectQuery)) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    result.append(metaData.getColumnName(i)).append("\t");
                }
                result.append("\n");

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
