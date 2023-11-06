package org.crock.exceptions;

public class IncorrectOrderStateException extends Exception {
    public IncorrectOrderStateException() {
        super("Неправильное состояние заказа");
    }

    public IncorrectOrderStateException(String message) {
        super(message);
    }

    public IncorrectOrderStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectOrderStateException(Throwable cause) {
        super(cause);
    }

    protected IncorrectOrderStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
