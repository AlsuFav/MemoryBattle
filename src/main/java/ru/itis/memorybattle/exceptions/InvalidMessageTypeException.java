package ru.itis.memorybattle.exceptions;

public class InvalidMessageTypeException extends RuntimeException {
    public InvalidMessageTypeException() {
    }

    public InvalidMessageTypeException(String message) {
        super(message);
    }

    public InvalidMessageTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMessageTypeException(Throwable cause) {
        super(cause);
    }

    public InvalidMessageTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
