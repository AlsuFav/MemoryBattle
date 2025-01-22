package ru.itis.memorybattle.gui;

import ru.itis.memorybattle.client.Client;
import ru.itis.memorybattle.gui.components.CardButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class MainUI extends JFrame {
    private final JPanel boardPanel;
    private CardButton firstSelected = null;
    private final Map<String, CardButton> cardButtons = new HashMap<>();
    private Client client;
    private boolean isMyTurn = false;

    public MainUI() {

        setTitle("Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        boardPanel = new JPanel();

        pack();
        setVisible(true);
    }

    public void showTurn() {
        JOptionPane.showMessageDialog(this, "Ваш ход!");
    }

    public void showNoTurn() {
        JOptionPane.showMessageDialog(this, "Ceйчас ход другого игрока.");
    }

    // Инициализация доски
    public void initializeGameBoard(int rows, int cols) {
        boardPanel.setLayout(new GridLayout(rows, cols));
        add(boardPanel, BorderLayout.CENTER);

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
        if (!isMyTurn) {
            showNoTurn();
            return;
        }

        CardButton clickedButton = (CardButton) e.getSource();

        if (firstSelected == null) {
            firstSelected = clickedButton;

            client.sendCardOpenRequest(firstSelected.getRow(), firstSelected.getCol());

        } else {
            if (firstSelected.equals(clickedButton)) return; // Если это та же кнопка

            client.sendCardOpenRequest(clickedButton.getRow(), clickedButton.getCol());

            client.sendMove(firstSelected.getRow(), firstSelected.getCol(), clickedButton.getRow(), clickedButton.getCol());
            firstSelected = null; // Сбросить после хода
        }
    }

    public void handleCardOpen(int x, int y, String source) {
        CardButton button = cardButtons.get(x + "-" + y);
        button.open(source);
    }

    public void handleCardClose(int x, int y) {
        CardButton button = cardButtons.get(x + "-" + y);
        button.close();
    }

    // Обработка совпадения карт
    public void handleMatch(int x1, int y1, int x2, int y2) {

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
    public void handleNoMatch(int x1, int y1, int x2, int y2) {

        SwingUtilities.invokeLater(() -> {
            handleCardClose(x1, y1);
            handleCardClose(x2, y2);
        });
    }

    // Обработка окончания игры
    public void handleEndGame(String results) {
        String result = "Игра окончена! Результаты: " + results;

        JOptionPane.showMessageDialog(this, result);
        System.exit(0);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setMyTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }
}
