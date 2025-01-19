package ru.itis.memorybattle.exceptions;

public class EventListenerException extends Exception {
    public EventListenerException() {
    }

    public EventListenerException(String message) {
        super(message);
    }

    public EventListenerException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventListenerException(Throwable cause) {
        super(cause);
    }

    public EventListenerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
