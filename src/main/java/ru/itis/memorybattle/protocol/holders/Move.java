package ru.itis.memorybattle.protocol.holders;

public class Move {
    private final int playerId;
    private final int x1, y1, x2, y2;

    public Move(int playerId, int x1, int y1, int x2, int y2) {
        this.playerId = playerId;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }
}

