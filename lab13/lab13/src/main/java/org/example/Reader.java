package org.example;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public record Reader() {
    private static String[] readFromFile(String path) throws IOException {
        try (var bufferFromFile = new BufferedInputStream(new FileInputStream(path), 200);) {
            String data = new String(bufferFromFile.readAllBytes(), StandardCharsets.UTF_8);
            return Arrays.stream(data.split("\n"))
                    .map(String::trim).toArray(String[]::new);
        }
    }

    public static Lot readLot(String path, long second) throws IOException {
        String[] data = readFromFile(path);
        if (data.length != 2) {
            throw new IOException("файле не соответсвует формату");
        }
        return new Lot(data[0], LocalDateTime.now().plusSeconds(second), new BigDecimal(data[1]));
    }


    public static ArrayList<String> readListOfParticipants(String path) throws IOException {
        var data = readFromFile(path);
        if (data.length == 0) {
            throw new IOException("Нет участников");
        }
        return new ArrayList<>(Arrays.stream(data).distinct().toList());
    }
}