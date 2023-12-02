package org.server.serverhandler.transmitterAndReceiver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public record ObjectReceiver() {

    public static Object receiveObject(Socket socket) throws IOException, ClassNotFoundException {
        try {
            System.out.println(socket.isClosed());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();

        } catch (IOException ioException) {
            throw new IOException("Не удалось прочитать файл: " + ioException.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new ClassNotFoundException("Не удалось найти пакета");
        }

    }

}
