package ru.itis.memorybattle.protocol;

import ru.itis.memorybattle.exceptions.*;
import ru.itis.memorybattle.model.Message;
import ru.itis.memorybattle.utils.GameMessageProvider;
import ru.itis.memorybattle.utils.GameSettings;
import ru.itis.memorybattle.utils.LogMessages;
import ru.itis.memorybattle.utils.MessageType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Protocol {

    public static final byte[] VERSION_BYTES = {0x0, 0x1};
    public static final int MAX_MESSAGE_LENGTH = 100 * 1024;

    public static Message readMessage(InputStream in) throws MessageReadException {
        byte[] buffer = new byte[VERSION_BYTES.length + GameSettings.INTEGER_BYTES * 2];
        try {
            in.read(buffer, 0, VERSION_BYTES.length);
            int counter = 0;
            for (int i = 0; i < VERSION_BYTES.length; i++) {
                if (buffer[i] == 0) counter++;
            }
            if (counter == VERSION_BYTES.length) return null;
            if (!Arrays.equals(Arrays.copyOfRange(buffer, 0, VERSION_BYTES.length), VERSION_BYTES)) {
                throw new InvalidProtocolVersionException(LogMessages.INVALID_PROTOCOL_VERSION_EXCEPTION + Arrays.toString(VERSION_BYTES));
            }

            in.read(buffer, 0, GameSettings.INTEGER_BYTES);
            int messageType = ByteBuffer.wrap(buffer, 0, GameSettings.INTEGER_BYTES).getInt();
            if (!MessageType.getAllTypes().contains(messageType)) {
                throw new InvalidMessageTypeException(String.format(LogMessages.INVALID_MESSAGE_TYPE_EXCEPTION, messageType));
            }

            in.read(buffer, 0, GameSettings.INTEGER_BYTES);
            int messageLength = ByteBuffer.wrap(buffer, 0, GameSettings.INTEGER_BYTES).getInt();
            if (messageLength > MAX_MESSAGE_LENGTH) {
                throw new InvalidMessageLengthException(String.format(LogMessages.INVALID_MESSAGE_LENGTH_EXCEPTION, messageLength, MAX_MESSAGE_LENGTH));
            }

            buffer = new byte[messageLength];
            in.read(buffer, 0, messageLength);
            return GameMessageProvider.createMessage(messageType, buffer);
        } catch (IOException e) {
            try {
                in.close();
            } catch (IOException ex) {
                throw new MessageReadException(LogMessages.READ_MESSAGE_EXCEPTION, e);
            }
        }
        return null;
    }

    public static void writeMessage(OutputStream out, Message message) throws MessageWriteException {
        try {
            out.write(Protocol.getBytes(message));
            out.flush();
        } catch (IOException e) {
            throw new MessageWriteException(LogMessages.WRITE_MESSAGE_EXCEPTION, e);
        }
    }

    public static byte[] getBytes(Message message) {
        ByteBuffer buffer = ByteBuffer.allocate(VERSION_BYTES.length + GameSettings.INTEGER_BYTES * 2 + message.getData().length);
        buffer.put(VERSION_BYTES);

        int type = message.getType();
        if (!MessageType.getAllTypes().contains(type)) {
            throw new InvalidMessageTypeException(String.format(LogMessages.INVALID_MESSAGE_TYPE_EXCEPTION, type));
        }
        buffer.putInt(type);

        int length = message.getData().length;
        if (length > MAX_MESSAGE_LENGTH) {
            throw new InvalidMessageLengthException(String.format(LogMessages.INVALID_MESSAGE_LENGTH_EXCEPTION, length, MAX_MESSAGE_LENGTH));
        }
        buffer.putInt(length);

        buffer.put(message.getData());
        return buffer.array();
    }
}