package ru.itis.memorybattle.core;

import java.util.*;

public class GameLogic {
    private final int rows;
    private final int cols;
    private final Card[][] board;
    private final Map<String, Integer> scores;
    private int currentPlayerIndex;
    private boolean isGameOver;

    public GameLogic(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.board = new Card[rows][cols];
        this.scores = new HashMap<>();
        this.currentPlayerIndex = 0;
        this.isGameOver = false;
        initializeBoard();
    }

    private void initializeBoard() {
        int totalСards = (rows * cols);
        ArrayList<Integer> ids = new ArrayList<>();

        for (int i = 0; i < totalСards; i++) {
            ids.add(i);
        }

        Iterator<Integer> iterator = ids.iterator();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int id = iterator.next();
                board[i][j] = new Card(id, id / 2);
            }
        }
    }

    public boolean makeMove(int row1, int col1, int row2, int col2) {
        if (isGameOver) return false;

        Card card1 = board[row1][col1];
        Card card2 = board[row2][col2];

        if (card1.isMatched() || card2.isMatched() || card1.equals(card2)) {
            return false; // Нельзя выбрать уже найденные карточки или одну и ту же
        }

        card1.setRevealed(true);
        card2.setRevealed(true);

        if (card1.isSimilar(card2)) {
            card1.setMatched(true);
            card2.setMatched(true);
            scores.put("Player" + currentPlayerIndex, scores.getOrDefault("Player" + currentPlayerIndex, 0) + 1);
            checkGameOver();
            return true; // Пара найдена
        } else {
            card1.setRevealed(false);
            card2.setRevealed(false);
            return false; // Пара не найдена
        }
    }

    private void checkGameOver() {
        for (Card[] row : board) {
            for (Card card : row) {
                if (!card.isMatched()) return;
            }
        }
        isGameOver = true;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 2;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public Card getCard(int row, int col) {
        return board[row][col];
    }
}
