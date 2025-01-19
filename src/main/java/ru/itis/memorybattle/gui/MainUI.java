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
        this.boardPanel = new JPanel(new GridLayout(rows, cols));

        initUI();
        initServerListener();
    }

    private void initUI() {
        setTitle("Memory Battle Online");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                CardButton button = new CardButton(i, j, this::onCardClick);
                button.setEnabled(false); // Кнопки изначально отключены
                boardPanel.add(button);
                cardButtons.put(i + "-" + j, button);
            }
        }

        add(boardPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void initServerListener() {
        client.listen(response -> SwingUtilities.invokeLater(() -> handleServerResponse(response)));
    }

    private void handleServerResponse(String response) {
        if (response.startsWith("START_GAME")) {
            enableBoard(true);
        } else if (response.startsWith("MATCH")) {
            handleMatch(response);
        } else if (response.startsWith("NO_MATCH")) {
            handleNoMatch(response);
        } else if (response.startsWith("END_GAME")) {
            handleEndGame(response);
        } else if (response.startsWith("TURN")) {
            handleTurn(response);
        } else if (response.startsWith("NOT_YOUR_TURN")) {
            JOptionPane.showMessageDialog(this, "Сейчас не ваш ход!");
        } else if (response.startsWith("INVALID_MOVE")) {
            JOptionPane.showMessageDialog(this, "Неверный ход, попробуйте снова.");
        }
    }

    private void handleTurn(String response) {
        String[] parts = response.split(" ");
        String currentPlayer = parts[1];
        isPlayerTurn = client.getName().equals(currentPlayer); // Проверяем, текущий ли это игрок
        JOptionPane.showMessageDialog(this, "Сейчас ход игрока: " + currentPlayer);

        // Включаем или отключаем кнопки в зависимости от очередности хода
        enableBoard(isPlayerTurn);
    }

    private void handleMatch(String response) {
        String[] parts = response.split(" ");
        int x1 = Integer.parseInt(parts[1]);
        int y1 = Integer.parseInt(parts[2]);
        int x2 = Integer.parseInt(parts[3]);
        int y2 = Integer.parseInt(parts[4]);

        cardButtons.get(x1 + "-" + y1).setText("✓");
        cardButtons.get(x2 + "-" + y2).setText("✓");
        cardButtons.get(x1 + "-" + y1).setEnabled(false);
        cardButtons.get(x2 + "-" + y2).setEnabled(false);
    }

    private void handleNoMatch(String response) {
        String[] parts = response.split(" ");
        int x1 = Integer.parseInt(parts[1]);
        int y1 = Integer.parseInt(parts[2]);
        int x2 = Integer.parseInt(parts[3]);
        int y2 = Integer.parseInt(parts[4]);

        // Закрываем карточки
        cardButtons.get(x1 + "-" + y1).setText("");
        cardButtons.get(x2 + "-" + y2).setText("");
        cardButtons.get(x1 + "-" + y1).setEnabled(true);
        cardButtons.get(x2 + "-" + y2).setEnabled(true);
    }

    private void handleEndGame(String response) {
        JOptionPane.showMessageDialog(this, "Игра окончена! Результаты: " + response);
        enableBoard(false);
    }

    private void enableBoard(boolean enable) {
        for (CardButton button : cardButtons.values()) {
            button.setEnabled(enable);
        }
    }

    private void onCardClick(ActionEvent e) {
        if (!isPlayerTurn) {
            JOptionPane.showMessageDialog(this, "Это не ваш ход!");
            return;
        }

        CardButton button = (CardButton) e.getSource();
        button.setText("?");

        if (firstSelected == null) {
            firstSelected = button;
            button.setEnabled(false); // Блокируем первую выбранную карту
        } else {
            button.setEnabled(false);
            client.sendMove(firstSelected.getRow(), firstSelected.getCol(), button.getRow(), button.getCol());
            firstSelected = null; // Сбрасываем выбор после второго клика
        }
    }
}