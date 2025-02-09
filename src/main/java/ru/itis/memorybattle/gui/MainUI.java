package ru.itis.memorybattle.gui;

import ru.itis.memorybattle.client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class MainUI extends JFrame {
    private final JPanel boardPanel;
    private final JPanel scorePanel;
    private final JPanel player1Panel;
    private final JPanel player2Panel;
    private final JLabel player1ScoreLabel;
    private final JLabel player2ScoreLabel;
    private CardButton firstSelected;
    private CardButton secondSelected;
    private final Map<String, CardButton> cardButtons;
    private Client client;
    private boolean isMyTurn;
    private final Map<String, Integer> scores;

    public MainUI() {
        firstSelected = null;
        secondSelected = null;
        cardButtons = new HashMap<>();
        isMyTurn = false;
        scores = new HashMap<>();

        setTitle("Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Панель для отображения счета
        scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(1, 2));
        scorePanel.setBackground(new Color(30, 30, 30));

        // Панель и метка для игрока 1
        player1Panel = createPlayerPanel("Игрок 1", new Color(100, 149, 237));
        player1ScoreLabel = new JLabel("0", JLabel.CENTER);
        styleScoreLabel(player1ScoreLabel);
        player1Panel.add(player1ScoreLabel, BorderLayout.CENTER);

        // Панель и метка для игрока 2
        player2Panel = createPlayerPanel("Игрок 2", new Color(255, 165, 0));
        player2ScoreLabel = new JLabel("0", JLabel.CENTER);
        styleScoreLabel(player2ScoreLabel);
        player2Panel.add(player2ScoreLabel, BorderLayout.CENTER);

        // Добавляем панели игроков в общую панель счета
        scorePanel.add(player1Panel);
        scorePanel.add(player2Panel);

        // Добавляем панель счета в верхнюю часть окна
        add(scorePanel, BorderLayout.NORTH);

        boardPanel = new JPanel();

        pack();
        setVisible(true);
    }

    private JPanel createPlayerPanel(String playerName, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(50, 50, 50)); // Темный фон для карточки игрока
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2, true), // Рамка с цветом игрока
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Отступы внутри
        ));

        JLabel nameLabel = new JLabel(playerName, JLabel.CENTER);
        nameLabel.setFont(new Font("Sans-serif", Font.BOLD, 16));
        nameLabel.setForeground(color);
        panel.add(nameLabel, BorderLayout.NORTH);

        return panel;
    }

    private void styleScoreLabel(JLabel label) {
        label.setFont(new Font("Sans-serif", Font.BOLD, 32));
        label.setForeground(Color.WHITE);
    }

    public void showWaiting() {
        JOptionPane.showMessageDialog(this, "Ожидаем второго игрока...");
    }

    public void showTurn() {
        JOptionPane.showMessageDialog(this, "Ваш ход!");
    }

    public void showNoTurn() {
        JOptionPane.showMessageDialog(this, "Сейчас ход другого игрока.");
    }

    public void showExtraTurn() {
        JOptionPane.showMessageDialog(this, "Ваш дополнительный ход!");
    }

    public void showNoExtraTurn() {
        JOptionPane.showMessageDialog(this, "Сейчас дополнительный ход другого игрока.");
    }

    public void showSpecialCardExtraTurnOpen() {
        JOptionPane.showMessageDialog(this, "У вас теперь есть дополнительный ход!");
    }

    public void showSpecialCardShuffleOpen() {
        shuffleAnimation();
        JOptionPane.showMessageDialog(this, "Все закрытые карты перемешаны!");
    }


    public void initializeGameBoard(int rows, int cols) {
        boardPanel.setLayout(new GridLayout(rows, cols, 10, 10));
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


    private void handleCardClick(ActionEvent e) {
        if (!isMyTurn) {
            showNoTurn();
            return;
        }

        CardButton clickedButton = (CardButton) e.getSource();

        if (firstSelected == null) {
            firstSelected = clickedButton;

            client.sendCardOpenRequest(firstSelected.getRow(), firstSelected.getCol());

        } else if (secondSelected == null) {
            secondSelected = clickedButton;

            if (firstSelected.equals(secondSelected)) return; // Если это та же кнопка

            client.sendCardOpenRequest(secondSelected.getRow(), secondSelected.getCol());

            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            if (secondSelected == null) return;

            client.sendMove(firstSelected.getRow(), firstSelected.getCol(), secondSelected.getRow(), secondSelected.getCol());

            firstSelected = null;
            secondSelected = null;
        }
    }


    public void handleCardOpen(int x, int y, String source) {
        CardButton button = cardButtons.get(x + "-" + y);
        button.open(source);

        if (secondSelected != null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    public void handleSpecialCardOpen(int x, int y, String source) {
        CardButton card = cardButtons.get(x + "-" + y);
        card.open(source);

        card.setMatched(true);
        card.setEnabled(false);
    }


    public void handleSpecialCardOpen() {
        if (secondSelected != null) {
            secondSelected = null;
        } else firstSelected = null;
    }

    public void shuffleAnimation() {
        // Создаём массив всех кнопок
        CardButton[] buttons = cardButtons.values().toArray(new CardButton[0]);

        // Сохраняем исходные положения для всех кнопок
        Map<CardButton, Point> originalLocations = new HashMap<>();
        for (CardButton button : buttons) {
            originalLocations.put(button, button.getLocation());
        }

        // Таймер для анимации
        Timer timer = new Timer(100, null); // 100 мс между кадрами
        final int[] step = {0}; // Счётчик шагов анимации

        timer.addActionListener(e -> {
            if (step[0] < 10) { // 10 шагов анимации
                for (CardButton button : buttons) {
                    // Случайное смещение кнопки для "тряски"
                    int dx = (int) (Math.random() * 10 - 5); // Смещение по X (-5 до +5)
                    int dy = (int) (Math.random() * 10 - 5); // Смещение по Y (-5 до +5)

                    // Устанавливаем новое положение
                    Point originalLocation = originalLocations.get(button);
                    button.setLocation(originalLocation.x + dx, originalLocation.y + dy);
                }

                step[0]++;
            } else {
                // Когда анимация завершена, возвращаем кнопки в исходное положение
                for (CardButton button : buttons) {
                    Point originalLocation = originalLocations.get(button);
                    button.setLocation(originalLocation);
                }

                // Останавливаем таймер, когда анимация завершена
                timer.stop();
            }
        });

        // Запуск таймера
        timer.start();
    }


    public void handleCardClose(int x, int y) {
        CardButton button = cardButtons.get(x + "-" + y);
        button.close();
    }


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


    public void handleNoMatch(int x1, int y1, int x2, int y2) {

        SwingUtilities.invokeLater(() -> {
            handleCardClose(x1, y1);
            handleCardClose(x2, y2);
        });
    }


    public void updateScores (String player1, int scores1, String player2, int scores2) {
        scores.put(player1, scores1);
        scores.put(player2, scores2);

        player1ScoreLabel.setText(player1 + ": " + scores1);
        player2ScoreLabel.setText(player2 + ": " + scores2);
    }


    public void handleEndGame(String winner) {
        String result = "Игра окончена! Победитель: " + winner;

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