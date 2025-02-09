package ru.itis.memorybattle.core;

import java.util.Objects;

public class Player {
    private int id;
    private String name;
    private int scores;
    private boolean hasExtraTurn;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.scores = 0;
        this.hasExtraTurn = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public boolean hasExtraTurn() {
        return hasExtraTurn;
    }

    public void hasExtraTurn(boolean hasExtraTurn) {
        this.hasExtraTurn = hasExtraTurn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id && Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}