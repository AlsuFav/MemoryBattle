package ru.itis.memorybattle.service;

import ru.itis.memorybattle.core.Card;
import ru.itis.memorybattle.repository.CardDaoImpl;

import java.sql.SQLException;
import java.util.List;

public class CardService {
    private final CardDaoImpl cardDao;

    public CardService() {
        this.cardDao = new CardDaoImpl();
    }

    public List<Card> getAllCards() throws SQLException {
        return cardDao.getAllCards();
    }
}
