package ru.itis.memorybattle.exceptions;

public class ScreenUpdaterException extends RuntimeException {
    public ScreenUpdaterException() {
    }

    public ScreenUpdaterException(String message) {
        super(message);
    }

    public ScreenUpdaterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScreenUpdaterException(Throwable cause) {
        super(cause);
    }

    public ScreenUpdaterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
