package org.crock.winterschool.dao;

import org.crock.winterschool.containers.Client;
import org.crock.winterschool.containers.Pet;

import java.sql.SQLException;
import java.util.List;

public interface PetDaoInterface {
    Pet createPet(String name, Integer age, List<Client> clients) throws SQLException;


    Pet findPet(Integer medicalCardNumber) throws SQLException;


    Pet updatePet(Pet pet) throws SQLException;


    void deletePet(Integer medicalCardNumber) throws SQLException;


    List<String> findClientPhoneNumbersBy(Pet pet) throws SQLException;

}
