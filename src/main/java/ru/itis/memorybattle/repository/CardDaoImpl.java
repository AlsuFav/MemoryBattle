package ru.itis.memorybattle.repository;

import ru.itis.memorybattle.core.Card;
import ru.itis.memorybattle.core.CardType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardDaoImpl implements CardDao {

    @Override
    public List<Card> getAllCards() throws SQLException {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                ConnectionProvider.getInstance().releaseConnection(connection);
            }
        }
        return cards;
    }
}
