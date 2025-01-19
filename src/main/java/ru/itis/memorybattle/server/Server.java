package ru.itis.memorybattle.server;

import java.io.*;
import java.net.*;
import java.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static final int ROWS = 4;
    private static final int COLS = 4;

    private final List<ClientHandler> players = new ArrayList<>();
    private final String[][] board = new String[ROWS][COLS];
    private final boolean[][] matched = new boolean[ROWS][COLS];
    private int currentPlayerIndex = 0;
    private final Map<String, Integer> scores = new HashMap<>();


    public void start() throws IOException {
        System.out.println("Сервер запущен. Ожидание игроков...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (players.size() < 2) {
                Socket socket = serverSocket.accept();
                ClientHandler player = new ClientHandler(socket);
                players.add(player);
                scores.put(player.getName(), 0);
                new Thread(player).start();
                System.out.println("Игрок подключён: " + player.getName());
            }

            System.out.println("Оба игрока подключены. Игра начинается!");
            initializeBoard();
            sendToAll("START_GAME " + ROWS + "x" + COLS);
            sendTurn();
        }
    }

    private void initializeBoard() {
        List<String> cards = new ArrayList<>();
        for (int i = 1; i <= (ROWS * COLS) / 2; i++) {
            cards.add(String.valueOf(i));
            cards.add(String.valueOf(i));
        }
        Collections.shuffle(cards);

        int index = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = cards.get(index++);
                matched[i][j] = false;
            }
        }
    }

    private void sendTurn() {
        String currentPlayer = players.get(currentPlayerIndex).getName();
        sendToAll("TURN " + currentPlayer);
    }

    private void sendToAll(String message) {
        for (ClientHandler player : players) {
            player.sendMessage(message);
        }
    }

    private synchronized void handleMove(ClientHandler player, int x1, int y1, int x2, int y2) {
        if (currentPlayerIndex != players.indexOf(player)) {
            player.sendMessage("NOT_YOUR_TURN");
            return;
        }

        if (matched[x1][y1] || matched[x2][y2]) {
            player.sendMessage("INVALID_MOVE");
            return;
        }

        if (board[x1][y1].equals(board[x2][y2])) {
            matched[x1][y1] = true;
            matched[x2][y2] = true;

            int score = scores.get(player.getName()) + 1;
            scores.put(player.getName(), score);

            sendToAll("MATCH " + x1 + " " + y1 + " " + x2 + " " + y2);

            if (isGameOver()) {
                endGame();
            } else {
                // Оставляем ход текущему игроку
                sendTurn();
            }
        } else {
            sendToAll("NO_MATCH " + x1 + " " + y1 + " " + x2 + " " + y2);
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            sendTurn();
        }
    }

    private boolean isGameOver() {
        for (boolean[] row : matched) {
            for (boolean matchedCell : row) {
                if (!matchedCell) return false;
            }
        }
        return true;
    }

    private void endGame() {
        StringBuilder result = new StringBuilder("END_GAME");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            result.append(" ").append(entry.getKey()).append(":").append(entry.getValue());
        }
        sendToAll(result.toString());
    }


    private class ClientHandler implements Runnable {
        private final Socket socket;
        private final PrintWriter out;
        private final BufferedReader in;
        private final String name;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("ENTER_NAME");
            this.name = in.readLine();
        }

        public String getName() {
            return name;
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        @Override
        public void run() {
            try {
                String input;
                while ((input = in.readLine()) != null) {
                    if (input.startsWith("PLAYER_MOVE")) {
                        String[] parts = input.split(" ");
                        int x1 = Integer.parseInt(parts[1]);
                        int y1 = Integer.parseInt(parts[2]);
                        int x2 = Integer.parseInt(parts[3]);
                        int y2 = Integer.parseInt(parts[4]);
                        handleMove(this, x1, y1, x2, y2);
                    }
                }
            } catch (IOException e) {
                System.out.println("Игрок отключился: " + name);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}