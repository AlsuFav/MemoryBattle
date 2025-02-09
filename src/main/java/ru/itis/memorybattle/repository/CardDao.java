package ru.itis.memorybattle.repository;

import ru.itis.memorybattle.core.Card;
import ru.itis.memorybattle.exceptions.DbConfigException;

import java.sql.SQLException;
import java.util.List;

public interface CardDao {
    public List<Card> getAllCards() throws SQLException, DbConfigException;
}