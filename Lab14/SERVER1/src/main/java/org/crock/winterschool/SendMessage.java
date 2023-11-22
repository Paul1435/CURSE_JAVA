package org.crock.winterschool;

import java.util.ArrayList;

public class SendMessage {
    public static void sendMessage(ArrayList<ClientHandler> clients, ClientHandler toIgnore, String message) {
        for (var client : clients) {
            if (client == toIgnore) {
                continue;
            }
            try {
                client.sendMsg(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
