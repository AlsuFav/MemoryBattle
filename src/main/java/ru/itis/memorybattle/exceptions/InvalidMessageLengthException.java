package ru.itis.memorybattle.exceptions;

public class InvalidMessageLengthException extends RuntimeException {
    public InvalidMessageLengthException() {
    }

    public InvalidMessageLengthException(String message) {
        super(message);
    }

    public InvalidMessageLengthException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMessageLengthException(Throwable cause) {
        super(cause);
    }

    public InvalidMessageLengthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
