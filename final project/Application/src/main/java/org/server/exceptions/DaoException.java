package org.server.exceptions;

public class DaoException extends Exception {
    Object incorrectObject;

    public DaoException(Object incorrectObject, String s) {
        super(s);
        this.incorrectObject = incorrectObject;
    }

    public Object getIncorrectObject() {
        return incorrectObject;
    }
}
