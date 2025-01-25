package ru.itis.memorybattle.core;

import ru.itis.memorybattle.exceptions.DbException;
import ru.itis.memorybattle.service.CardService;

import java.sql.SQLException;
import java.util.*;

public class GameLogic {
    private final int rows;
    private final int cols;
    private final Card[][] board;
    private final List<Player> players;
    private Player currentPlayer;
    private boolean isGameOver;
    private CardService cardService;

    public GameLogic(int rows, int cols, CardService cardService) {
        this.rows = rows;
        this.cols = cols;
        this.board = new Card[rows][cols];
        players = new ArrayList<>();
        this.isGameOver = false;
        this.cardService = cardService;

        initializeBoard();
    }

    private void initializeBoard() {
        List<Card> cards;
        try {
            cards = cardService.getAllCards();
        } catch (SQLException e) {
            throw new DbException();
        }
        // Перемешиваем карты
//        Collections.shuffle(cards);

        Iterator<Card> iterator = cards.iterator();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = iterator.next();
            }
        }
    }

    public void addPlayer(Player player) {
        players.add(player);

        if (currentPlayer == null) {
            currentPlayer = player;
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
            currentPlayer.setScores(currentPlayer.getScores() + 1);
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


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        currentPlayer = getNotCurrentPlayer();
    }

    public Player getNotCurrentPlayer() {
        if (currentPlayer.equals(players.getFirst())) {
            return  players.getLast();
        } else return players.getFirst();
    }

    public Card getCard(int row, int col) {
        return board[row][col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
