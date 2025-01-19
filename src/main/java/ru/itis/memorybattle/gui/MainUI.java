package ru.itis.memorybattle.gui;

import ru.itis.memorybattle.client.Client;
import ru.itis.memorybattle.gui.components.CardButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class MainUI extends JFrame {
    private final int rows;
    private final int cols;
    private final JPanel boardPanel;
    private final Client client;
    private CardButton firstSelected = null;
    private final Map<String, CardButton> cardButtons = new HashMap<>();
    private boolean isPlayerTurn = false; // Флаг хода текущего игрока

    public MainUI(Client client, int rows, int cols) {
        this.client = client;
        this.rows = rows;
        this.cols = cols;

        setTitle("Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(rows, cols));
        add(boardPanel, BorderLayout.CENTER);

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

    // Инициализация доски
    private void initializeGameBoard() {
        boardPanel.removeAll();
        cardButtons.clear();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                CardButton button = new CardButton(i, j);
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
