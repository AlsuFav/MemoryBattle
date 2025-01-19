package ru.itis.memorybattle.utils;

/**
 * This class provides strings that are shown in exceptions
 */
public class LogMessages {
    public static final String ESTABLISH_CONNECTION_SERVER_EXCEPTION = "Can not establish a connection.";
    public static final String LOST_CONNECTION_SERVER_EXCEPTIONS = "Connection to the client is lost.";
    public static final String READ_SERVER_EXCEPTION = "Server can't read a message.";
    public static final String LISTENER_SERVER_EXCEPTION = "Server has a not initialized listener.";
    public static final String ESTABLISH_CONNECTION_CLIENT_EXCEPTION = "Can't connect to the server.";
    public static final String LOST_CONNECTION_CLIENT_EXCEPTION = "Connection to the server is lost.";
    public static final String WRITE_CLIENT_EXCEPTION = "Can't send data to the server.";
    public static final String READ_CLIENT_EXCEPTION = "Client can't read a message.";
    public static final String INITIALIZE_LISTENER_EXCEPTION = "Listener: %s hasn't been initialized yet.";
    public static final String GET_SINGLE_SERVER_DB_EXCEPTION = "Can't get server model from DB.";
    public static final String GET_ALL_SERVERS_DB_EXCEPTION = "Can't get server model list from DB.";
    public static final String SAVE_DB_EXCEPTION =  "Can't save server model into DB.";
    public static final String UPDATE_DB_EXCEPTION = "Can't update server model into DB.";
    public static final String REMOVE_DB_EXCEPTION = "Can't remove server model from DB.";
    public static final String WRITE_DB_CONFIG_EXCEPTION = "Can not write data into the property file.";
    public static final String READ_DB_CONFIG_EXCEPTION = "Can not read data from the property file.";
    public static final String NOT_FOUND_DB_CONFIG_EXCEPTION = "Unable to find: %s.";
    public static final String ESTABLISH_CONNECTION_DB_EXCEPTION = "Can not connect to DB.";
    public static final String GET_CONFIGURATION_DB_EXCEPTION = "Can not get configurations for current DB.";
    public static final String INVALID_PROTOCOL_VERSION_EXCEPTION = "Versions of the protocols don't match. Message first bytes must be: ";
    public static final String INVALID_MESSAGE_TYPE_EXCEPTION = "Wrong message type: %d.";
    public static final String INVALID_MESSAGE_LENGTH_EXCEPTION = "Protocol doesn't support this message length: %d. Message length can't be greater than %d bytes length.";
    public static final String READ_MESSAGE_EXCEPTION = "Can't read message.";
    public static final String WRITE_MESSAGE_EXCEPTION = "Can't write message.";
    public static final String INITIALIZE_UPDATER_EXCEPTION = "Screen updater: %s hasn't been initialized yet.";
    public static final String DECODE_IMAGE_EXCEPTION = "Can't decode image.";
    public static final String LOCAL_HOST_EXCEPTION = "Local host name cannot be resolved into an address.";

    private LogMessages() {}
}
