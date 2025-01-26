package ru.itis.memorybattle.exceptions;

public class InvalidProtocolVersionException extends RuntimeException {
    public InvalidProtocolVersionException() {
    }

    public InvalidProtocolVersionException(String message) {
        super(message);
    }

    public InvalidProtocolVersionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidProtocolVersionException(Throwable cause) {
        super(cause);
    }

    public InvalidProtocolVersionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
