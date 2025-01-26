package ru.itis.memorybattle.utils;

/**
 * This class provides strings that are shown in exceptions
 */
public class LogMessages {
    public static final String LOST_CONNECTION_SERVER_EXCEPTIONS = "Connection to the client is lost.";
    public static final String READ_SERVER_EXCEPTION = "Server can't read a message.";
    public static final String LOST_CONNECTION_CLIENT_EXCEPTION = "Connection to the server is lost.";
    public static final String WRITE_CLIENT_EXCEPTION = "Can't send data to the server.";
    public static final String READ_CLIENT_EXCEPTION = "Client can't read a message.";
    public static final String ESTABLISH_CONNECTION_DB_EXCEPTION = "Can not connect to DB.";
    public static final String INVALID_PROTOCOL_VERSION_EXCEPTION = "Versions of the protocols don't match. Message first bytes must be: ";
    public static final String INVALID_MESSAGE_TYPE_EXCEPTION = "Wrong message type: %d.";
    public static final String INVALID_MESSAGE_LENGTH_EXCEPTION = "Protocol doesn't support this message length: %d. Message length can't be greater than %d bytes length.";
    public static final String READ_MESSAGE_EXCEPTION = "Can't read message.";
    public static final String WRITE_MESSAGE_EXCEPTION = "Can't write message.";

    private LogMessages() {}
}