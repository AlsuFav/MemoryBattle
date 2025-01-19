package ru.itis.memorybattle.core;

public class Card {
    private final int id; // Идентификатор пары карточки
    private boolean isMatched; // Флаг, найдена ли пара
    private boolean isRevealed; // Флаг, открыта ли карточка

    public Card(int id) {
        this.id = id;
        this.isMatched = false;
        this.isRevealed = false;
    }

    public int getId() {
        return id;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    @Override
    public String toString() {
        return isRevealed ? String.valueOf(id) : "X"; // Показать ID, если карточка открыта
    }
}