package ru.itis.memorybattle.settings;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageType {
    public static final int MOVEMENT_REQUEST = 1;
    public static final int MOVEMENT_RESPONSE = 2;
    public static final int POWER_UP_REQUEST = 3;
    public static final int POWER_UP_RESPONSE = 4;
    public static final int EAT_PLAYER_REQUEST = 5;
    public static final int EAT_PLAYER_RESPONSE = 6;
    public static final int USER_COUNT_INFO_REQUEST = 7;
    public static final int USER_COUNT_INFO_RESPONSE = 8;
    public static final int USER_ID_REQUEST = 9;
    public static final int USER_ID_RESPONSE = 10;
    public static final int CREATE_WALLS_REQUEST = 11;
    public static final int CREATE_WALLS_RESPONSE = 12;
    public static final int CREATE_PACMAN_REQUEST = 13;
    public static final int CREATE_PACMAN_RESPONSE = 14;
    public static final int CREATE_GHOST_REQUEST = 15;
    public static final int CREATE_GHOST_RESPONSE = 16;
    public static final int CREATE_BONUSES_REQUEST = 17;
    public static final int CREATE_BONUSES_RESPONSE = 18;
    public static final int CREATE_PELLETS_REQUEST = 19;
    public static final int CREATE_PELLETS_RESPONSE = 20;
    public static final int BLINK_BONUSES_REQUEST = 21;
    public static final int BLINK_BONUSES_RESPONSE = 22;
    public static final int PACMAN_EAT_BONUS_REQUEST = 23;
    public static final int PACMAN_EAT_BONUS_RESPONSE = 24;
    public static final int PACMAN_EAT_PELLET_REQUEST = 25;
    public static final int PACMAN_EAT_PELLET_RESPONSE = 26;
    public static final int CHANGE_SCORES_REQUEST = 27;
    public static final int CHANGE_SCORES_RESPONSE = 28;
    public static final int GAME_WIN_REQUEST = 29;
    public static final int GAME_WIN_RESPONSE = 30;
    public static final int GAME_LOSE_REQUEST = 31;
    public static final int GAME_LOSE_RESPONSE = 32;
    public static final int PLAYERS_MOVE_REQUEST = 33;
    public static final int PLAYERS_MOVE_RESPONSE = 34;
    public static final int RUSH_MODE_REQUEST = 35;
    public static final int RUSH_MODE_RESPONSE = 36;
    public static final int GHOST_SCORES_REQUEST = 37;
    public static final int GHOST_SCORES_RESPONSE = 38;
    public static final int GAME_END_REQUEST = 39;
    public static final int GAME_END_RESPONSE = 40;
    public static final int TIME_REQUEST = 41;
    public static final int TIME_RESPONSE = 42;


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
