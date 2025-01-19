package ru.itis.memorybattle.core;

import java.util.*;

public class GameLogic {
    private final int rows;
    private final int cols;
    private final Card[][] board;
    private boolean isGameOver;

    public GameLogic(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.board = new Card[rows][cols];
        this.isGameOver = false;
        initializeBoard();
    }

    // Инициализация игрового поля с парами карточек
    private void initializeBoard() {
        List<Integer> ids = new ArrayList<>();
        int totalPairs = (rows * cols) / 2;

        for (int i = 1; i <= totalPairs; i++) {
            ids.add(i);
            ids.add(i); // Добавляем пару
        }
        Collections.shuffle(ids);

        Iterator<Integer> iterator = ids.iterator();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = new Card(iterator.next());
            }
        }
    }

    public Card getCard(int row, int col) {
        return board[row][col];
    }

    public boolean makeMove(int row1, int col1, int row2, int col2) {
        if (isGameOver) return false;

        Card card1 = board[row1][col1];
        Card card2 = board[row2][col2];

        if (card1.isMatched() || card2.isMatched() || card1 == card2) {
            return false; // Нельзя выбрать уже найденные карточки или одну и ту же
        }

        card1.setRevealed(true);
        card2.setRevealed(true);

        if (card1.getId() == card2.getId()) {
            card1.setMatched(true);
            card2.setMatched(true);
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

    public void printBoard() {
        for (Card[] row : board) {
            for (Card card : row) {
                System.out.print(card + " ");
            }
            System.out.println();
        }
    }
}