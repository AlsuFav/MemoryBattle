package ru.itis.memorybattle.core;

import java.util.Objects;

public class Card {
    private final int id;
    private boolean isMatched;
    private boolean isRevealed;
    private final int uniqueCardId;
    private final CardType type;
    private final String imagePath;

    public Card(int id, int uniqueCardId, CardType type, String imagePath) {
        this.id = id;
        this.isMatched = false;
        this.isRevealed = false;
        this.uniqueCardId = uniqueCardId;
        this.type = type;
        this.imagePath = imagePath;
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

    public CardType getType() {
        return type;
    }

    public String getImagePath() {
        return imagePath;
    }


    public boolean isSimilar(Object o) {
        if (this == o) return false;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return uniqueCardId == card.uniqueCardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
