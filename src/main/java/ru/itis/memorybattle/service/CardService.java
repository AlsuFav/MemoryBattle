package ru.itis.memorybattle.service;

import ru.itis.memorybattle.core.Card;
import ru.itis.memorybattle.exceptions.DbConfigException;

import java.sql.SQLException;
import java.util.List;

public interface CardService {
    public List<Card> getAllCards() throws SQLException, DbConfigException;
}
