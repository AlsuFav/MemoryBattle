package ru.itis.memorybattle.service.impl;

import ru.itis.memorybattle.core.Card;
import ru.itis.memorybattle.exceptions.DbConfigException;
import ru.itis.memorybattle.repository.CardDao;
import ru.itis.memorybattle.service.CardService;

import java.sql.SQLException;
import java.util.List;

public class CardServiceImpl implements CardService {
    private final CardDao cardDao;

    public CardServiceImpl(CardDao cardDao) {
        this.cardDao = cardDao;
    }

    @Override
    public List<Card> getAllCards() throws SQLException, DbConfigException {
        return cardDao.getAllCards();
    }
}