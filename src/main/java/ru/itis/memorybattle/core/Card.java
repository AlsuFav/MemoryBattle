package ru.itis.memorybattle.core;

import java.util.Objects;

public class Card {
    private final int id; // Идентификатор карточки
    private boolean isMatched; // Флаг, найдена ли пара
    private boolean isRevealed; // Флаг, открыта ли карточка
    private final int uniqueCardId; // Идентификатор уникальной карты (если NORMAL)
    private final CardType type; // Тип карты
    private final String imagePath; // Путь до изображения карты

    public Card(int id, int uniqueCardId, CardType type, String imagePath) {
        this.id = id;
        this.isMatched = false; // По умолчанию false
        this.isRevealed = false; // По умолчанию false
        this.uniqueCardId = uniqueCardId; // Уникальный ID только для NORMAL карт
        this.type = type;
        this.imagePath = imagePath;
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

    public Integer getUniqueCardId() {
        return uniqueCardId;
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
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", isMatched=" + isMatched +
                ", isRevealed=" + isRevealed +
                ", uniqueCardId=" + uniqueCardId +
                ", type=" + type +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id &&
                Objects.equals(uniqueCardId, card.uniqueCardId) &&
                type == card.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uniqueCardId, type);
    }
}
