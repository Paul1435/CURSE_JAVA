package org.crock.winterschool.parcer;

import org.crock.winterschool.dataAccessObject.Client;
import org.crock.winterschool.dataAccessObject.Pet;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


public class CSVParser {
    /**
     * @param path - путь к файлу
     * @return - список слов в каждом строке. Каждая строка представляет собой массив,
     * поэтому получается двумерный массив(Храним строки, строка - массив, содержащая значения каждой строки)
     * @throws IOException - Ошибка при открытии файла
     */
    private static ArrayList<List<String>> readFromFile2List(String path) throws IOException {
        try (var bufferFromFile = new BufferedInputStream(new FileInputStream(path), 200)) {
            String data = new String(bufferFromFile.readAllBytes(), StandardCharsets.UTF_8);
            ArrayList<List<String>> log = new ArrayList<>();
            for (String entry : data.split("\n")) {
                if (entry != null && !"".equals(entry)) {
                    log.add((Arrays.stream(entry.split(",")).map(String::trim).collect(Collectors.toList())));
                }
            }
            return log;
        }
    }

    /**
     * @param wordsInString - массив значений строки
     * @param indexOfInt    - индекс, где находится предполагаемое число
     * @return возвращает число, которое там находится
     */
    private static int parseInt(List<String> wordsInString, int indexOfInt) {
        try {
            return Integer.parseInt(wordsInString.get(indexOfInt));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Записано не число " + wordsInString.get(indexOfInt));
        }
    }

    /**
     * @param data - значение строки, берем только необходимые данные после их проверки в основном методе
     * @return Client - данные клиента для записи в БД
     */
    private static Client createClient(List<String> data) {
        int clientId = parseInt(data, 0);
        int petId = parseInt(data, 4);
        String lastName = data.get(1);
        String firstName = data.get(2);
        String numberPhone = data.get(3);
        return new Client(clientId, firstName, lastName, numberPhone, petId);
    }

    /**
     * @param data - значение строки, берем только необходимые данные после их проверки в основном методе
     * @return Pet - данные зверушки для записи в БД
     */
    private static Pet createPet(List<String> data) {
        int petId = parseInt(data, 4);
        String petName = data.get(5);
        int age = parseInt(data, 6);
        return new Pet(petId, petName, age);
    }

    /**
     * @param path    - путь к файлу
     * @param clients - коллекция для сохранения значений клиента
     * @param pets    - коллекция для сохранения значений зверушки
     *                Данный метод проверяет записанные значения
     * @throws IOException
     */
    public static void clientsAndPetsFill(String path, Collection<Client> clients, Collection<Pet> pets) throws IOException {
        Objects.requireNonNull(path, "Null path to file");
        Objects.requireNonNull(clients, "Null clients");
        Objects.requireNonNull(pets, "Null pets");
        try {
            var ArrayOfString = readFromFile2List(path);
            for (List<String> wordsInString : ArrayOfString) {
                try {
                    if (wordsInString.size() != 7) {
                        throw new RuntimeException("Неверная запись, количество строк: " + wordsInString.size() + " Вместо 7");
                    }
                    clients.add(createClient(wordsInString));
                    pets.add(createPet(wordsInString));

                } catch (RuntimeException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        } catch (IOException e) {
            throw new IOException("Не удалось открыть файл");
        }
        if (clients.size() == 0 || pets.size() == 0) {
            throw new IOException("В файле содержатся некорректные данные");
        }
    }
}
