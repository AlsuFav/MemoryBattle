package ru.itis.memorybattle.utils;


import ru.itis.memorybattle.exceptions.InvalidMessageTypeException;
import ru.itis.memorybattle.model.Message;

public class GameMessageProvider {
    public static Message createMessage(int type, byte[] data) {
        if (!MessageType.getAllTypes().contains(type)) {
            throw new InvalidMessageTypeException(String.format(LogMessages.INVALID_MESSAGE_TYPE_EXCEPTION, type));
        }

        return new Message(type, data);
    }
}
