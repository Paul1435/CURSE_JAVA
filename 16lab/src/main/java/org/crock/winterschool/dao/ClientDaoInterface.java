package org.crock.winterschool.dao;


import org.crock.winterschool.containers.Client;
import org.crock.winterschool.exception.ClientException;
import org.crock.winterschool.containers.Pet;

import java.sql.SQLException;
import java.util.List;

public interface ClientDaoInterface {


    Client createClient(Client client) throws ClientException, SQLException;

    Client findClient(Integer id) throws SQLException;

    Client updateClient(Client client) throws ClientException;

    List<Pet> getAllPetsOf(Client client) throws SQLException;

    void deleteClient(Integer id) throws SQLException, ClientException;


}
