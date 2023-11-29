package org.crock.winterschool.exception;

import org.crock.winterschool.containers.Client;

public class ClientException extends Exception {
    private Client client;

    public ClientException(Client client, String message) {
        super(message);
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
