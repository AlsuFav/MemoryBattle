package ru.itis.memorybattle.gui;

import ru.itis.memorybattle.client.Client;
import ru.itis.memorybattle.core.Card;
import ru.itis.memorybattle.gui.components.CardButton;
import ru.itis.memorybattle.repository.CardDaoImpl;
import ru.itis.memorybattle.service.CardService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class MainUI extends JFrame {
    private final int rows;
    private final int cols;
    private final JPanel boardPanel;
    private final Client client;
    private CardButton firstSelected = null;
    private final Map<String, CardButton> cardButtons = new HashMap<>();
    private final List<Card> cards = new ArrayList<>();
    private boolean isPlayerTurn = false; // Флаг хода текущего игрока
    private final CardService cardService;

    public MainUI(Client client, int rows, int cols, CardService cardService) {
        this.client = client;
        this.rows = rows;
        this.cols = cols;
        this.cardService = cardService;

        setTitle("Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(rows, cols));
        add(boardPanel, BorderLayout.CENTER);
        // Загрузить карты из базы данных
        loadCards();


        // Слушаем события от сервера
        client.addGameListener(message -> {
            if (message.startsWith("START_GAME")) {
                initializeGameBoard();
            } else if (message.startsWith("TURN")) {
                String playerName = message.split(" ")[1];
                isPlayerTurn = playerName.equals(client.getName());
                if (isPlayerTurn) {
                    JOptionPane.showMessageDialog(this, "Ваш ход!");
                } else {
                    JOptionPane.showMessageDialog(this, "Ход другого игрока.");
                }
            } else if (message.startsWith("MATCH")) {
                // Отобразить совпавшие карты
                handleMatch(message);
            } else if (message.startsWith("NO_MATCH")) {
                // Отобразить, что карты не совпали
                handleNoMatch(message);
            } else if (message.startsWith("END_GAME")) {
                // Обработать конец игры
                handleEndGame(message);
            }
        });

        pack();
        setVisible(true);
    }

    private void loadCards() {
        try {
            List<Card> loadedCards = cardService.getAllCards();
            cards.addAll(loadedCards);

            // Перемешиваем карты
//            Collections.shuffle(cards);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка загрузки карт из базы данных!");
        }
    }


    // Инициализация доски
    private void initializeGameBoard() {
        boardPanel.removeAll();
        cardButtons.clear();

        Iterator<Card> iterator = cards.iterator();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!iterator.hasNext()) break;
                Card card = iterator.next();
                CardButton button = new CardButton(i, j, card.getId(), card.getImagePath());
                button.addActionListener(this::handleCardClick);
                cardButtons.put(i + "-" + j, button);
                boardPanel.add(button);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    // Обработка клика на карточку
    private void handleCardClick(ActionEvent e) {
        if (!isPlayerTurn) {
            JOptionPane.showMessageDialog(this, "Сейчас не ваш ход!");
            return; // Если не наш ход, не можем сделать движение
        }

        CardButton clickedButton = (CardButton) e.getSource();

        if (firstSelected == null) {
            firstSelected = clickedButton;
            firstSelected.flip();
        } else {
            if (firstSelected.equals(clickedButton)) return; // Если это та же кнопка

            clickedButton.flip();
            client.sendMove(firstSelected.getRow(), firstSelected.getCol(), clickedButton.getRow(), clickedButton.getCol());
            firstSelected = null; // Сбросить после хода
        }
    }

    // Обработка совпадения карт
    private void handleMatch(String message) {
        String[] parts = message.split(" ");
        int x1 = Integer.parseInt(parts[1]);
        int y1 = Integer.parseInt(parts[2]);
        int x2 = Integer.parseInt(parts[3]);
        int y2 = Integer.parseInt(parts[4]);

        SwingUtilities.invokeLater(() -> {
            CardButton card1 = cardButtons.get(x1 + "-" + y1);
            CardButton card2 = cardButtons.get(x2 + "-" + y2);

            card1.setMatched(true);
            card2.setMatched(true);

            card1.setEnabled(false);
            card2.setEnabled(false);
        });
    }

    // Обработка несоответствия карт
    private void handleNoMatch(String message) {
        String[] parts = message.split(" ");
        int x1 = Integer.parseInt(parts[1]);
        int y1 = Integer.parseInt(parts[2]);
        int x2 = Integer.parseInt(parts[3]);
        int y2 = Integer.parseInt(parts[4]);

        SwingUtilities.invokeLater(() -> {
            CardButton card1 = cardButtons.get(x1 + "-" + y1);
            CardButton card2 = cardButtons.get(x2 + "-" + y2);

            // Переворачиваем карты обратно
            card1.flip();
            card2.flip();
        });
    }

    // Обработка окончания игры
    private void handleEndGame(String message) {
        String[] parts = message.split(" ");
        String result = "Игра окончена! Результаты: ";

        for (int i = 1; i < parts.length; i++) {
            result += parts[i] + " ";
        }

        JOptionPane.showMessageDialog(this, result);
        System.exit(0);
    }
}
