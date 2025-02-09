package ru.itis.memorybattle.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageType {

    public static final int WAIT_ANOTHER_PLAYER = 2;
    public static final int INITIALIZE_PLAYER = 3;
    public static final int START_GAME = 4;

    public static final int PLAYER_MOVE = 5;

    public static final int MATCH = 6;
    public static final int NO_MATCH = 7;

    public static final int TURN = 8;
    public static final int NOT_YOUR_TURN = 16;

    public static final int OPEN_CARD_REQUEST = 17;
    public static final int OPEN_CARD_RESPONSE = 18;

    public static final int SCORES_UPDATE = 22;

    public static final int OPEN_SPECIAL_CARD = 19;
    public static final int MOVE_AFTER_SPECIAL_CARD_OPEN = 9;

    public static final int SPECIAL_CARD_EXTRA_TURN = 23;
    public static final int EXTRA_TURN = 20;
    public static final int NOT_YOUR_EXTRA_TURN = 21;

    public static final int SPECIAL_CARD_SHUFFLE = 10;

    public static final int END_GAME = 14;


    public static List<Integer> getAllTypes() {
        return Arrays.stream(MessageType.class.getFields()).map(field -> {
            try {
                return field.getInt(MessageType.class.getDeclaredConstructor());
            } catch (IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    private MessageType() {
    }
}