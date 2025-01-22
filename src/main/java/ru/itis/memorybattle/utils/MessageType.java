package ru.itis.memorybattle.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageType {
    // Подключение и инициализация
    public static final int CONNECT = 1;
    public static final int PLAYER_CONNECTED = 2;
    public static final int START_GAME = 3;

    // Игровой процесс
    public static final int PLAYER_MOVE = 4;
    public static final int MATCH = 5;
    public static final int NO_MATCH = 6;
    public static final int TURN = 7;

    // Особые карточки
    public static final int SPECIAL_CARD_EXTRA_TURN = 8;
    public static final int SPECIAL_CARD_SHUFFLE = 9;
    public static final int SPECIAL_CARD_HINT = 10;

    // Синхронизация состояния игры
    public static final int GAME_UPDATE = 11;
    public static final int SCORE_UPDATE = 12;

    // Завершение игры
    public static final int END_GAME = 13;
    public static final int DISCONNECT = 14;

    // Получение всех возможных типов сообщений
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
