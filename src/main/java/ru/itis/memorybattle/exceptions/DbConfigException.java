package ru.itis.memorybattle.exceptions;

public class DbConfigException extends Exception {
    public DbConfigException() {
    }

    public DbConfigException(String message) {
        super(message);
    }

    public DbConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbConfigException(Throwable cause) {
        super(cause);
    }

    public DbConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
