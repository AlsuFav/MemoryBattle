package ru.itis.memorybattle.server;

import ru.itis.memorybattle.core.GameLogic;
import ru.itis.memorybattle.repository.CardDaoImpl;
import ru.itis.memorybattle.service.CardService;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static final int ROWS = 4;
    private static final int COLS = 4;

    private final List<ClientHandler> players = new ArrayList<>();
    CardService cardService = new CardService();
    private final GameLogic gameLogic = new GameLogic(ROWS, COLS, cardService); // Логика игры
    private final Map<String, Integer> scores = new HashMap<>();

    public Server() throws SQLException {
    }

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
            sendToAll("START_GAME " + ROWS + "x" + COLS);
            sendTurn();
        }
    }

    private void sendTurn() {
        String currentPlayer = players.get(gameLogic.getCurrentPlayerIndex()).getName();
        sendToAll("TURN " + currentPlayer);
    }

    private void sendToAll(String message) {
        for (ClientHandler player : players) {
            player.sendMessage(message);
        }
    }

    private synchronized void handleMove(ClientHandler player, int x1, int y1, int x2, int y2) {
        if (gameLogic.getCurrentPlayerIndex() != players.indexOf(player)) {
            player.sendMessage("NOT_YOUR_TURN");
            return;
        }

        System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);

        boolean match = gameLogic.makeMove(x1, y1, x2, y2);

        if (match) {
            sendToAll("MATCH " + x1 + " " + y1 + " " + x2 + " " + y2);
            if (gameLogic.isGameOver()) {
                endGame();
            } else {
                // Игрок продолжает, не переключаем ход
                sendTurn();
            }
        } else {
            sendToAll("NO_MATCH " + x1 + " " + y1 + " " + x2 + " " + y2);
            gameLogic.switchPlayer(); // Передаем ход другому игроку
            sendTurn();
        }
    }

    private void endGame() {
        StringBuilder result = new StringBuilder("END_GAME");
        for (Map.Entry<String, Integer> entry : gameLogic.getScores().entrySet()) {
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
