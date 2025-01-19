package ru.itis.memorybattle.exceptions;

public class MessageWriteException extends Exception {
    public MessageWriteException() {
    }

    public MessageWriteException(String message) {
        super(message);
    }

    public MessageWriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageWriteException(Throwable cause) {
        super(cause);
    }

    public MessageWriteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
