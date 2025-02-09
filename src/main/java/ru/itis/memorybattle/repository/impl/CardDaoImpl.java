package ru.itis.memorybattle.repository.impl;

import ru.itis.memorybattle.core.Card;
import ru.itis.memorybattle.core.CardType;
import ru.itis.memorybattle.exceptions.DbConfigException;
import ru.itis.memorybattle.exceptions.DbException;
import ru.itis.memorybattle.repository.CardDao;
import ru.itis.memorybattle.utils.ConnectionProvider;
import ru.itis.memorybattle.utils.LogMessages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardDaoImpl implements CardDao {

    @Override
    public List<Card> getAllCards() throws SQLException, DbConfigException {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT id, unique_card_id, type, image_path FROM cards";
        Connection connection = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int uniqueCardId = resultSet.getInt("unique_card_id");
                    String type = resultSet.getString("type");
                    String imagePath = resultSet.getString("image_path");

                    CardType cardType = CardType.valueOf(type.toUpperCase());
                    cards.add(new Card(id, uniqueCardId ,cardType, imagePath));
                }
            }
        } finally {
            if (connection != null) {
                ConnectionProvider.getInstance().releaseConnection(connection);
            }
        }
        return cards;
    }
}