package org.crock.order;

import org.crock.exceptions.IncorrectOrderStateException;

public interface SendMessageClient {
    public String getMessageClient() throws IncorrectOrderStateException;
}
