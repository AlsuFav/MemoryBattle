package ru.itis.memorybattle.server;

import ru.itis.memorybattle.core.Card;
import ru.itis.memorybattle.core.GameLogic;
import ru.itis.memorybattle.exceptions.MessageReadException;
import ru.itis.memorybattle.exceptions.MessageWriteException;
import ru.itis.memorybattle.exceptions.ServerException;
import ru.itis.memorybattle.model.Message;
import ru.itis.memorybattle.protocol.Protocol;
import ru.itis.memorybattle.utils.GameMessageProvider;
import ru.itis.memorybattle.utils.LogMessages;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ru.itis.memorybattle.utils.GameSettings.COLS;
import static ru.itis.memorybattle.utils.GameSettings.ROWS;
import static ru.itis.memorybattle.utils.MessageType.*;

public class Server {
    private static final int PORT = 12345;

    private final List<ClientHandler> clients = new ArrayList<>();
    private final GameLogic gameLogic = new GameLogic(ROWS, COLS); // Логика игры
    private final Map<Integer, Integer> scores = new HashMap<>();


    public void start() throws IOException {
        System.out.println("Сервер запущен. Ожидание игроков...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (clients.size() < 2) {
                Socket socket = serverSocket.accept();
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();

                ClientHandler player = new ClientHandler(socket, input, output);

                clients.add(player);

                scores.put(player.getId(), 0);

                new Thread(player).start();

                System.out.println("Игрок подключён: " + player.getName());
            }

            System.out.println("Оба игрока подключены. Игра начинается!");

            sendStartGame();

            sendTurn();
            sendNoTurn();
        }
    }

    private void sendStartGame() {
        Message message = GameMessageProvider.createMessage(START_GAME, STR."\{ROWS} \{COLS}".getBytes());
        sendToAll(message);
    }

    private void sendTurn() {
        ClientHandler currentPlayer = clients.get(gameLogic.getCurrentPlayerIndex());
        sendMessage(currentPlayer.getId(), GameMessageProvider.createMessage(TURN, "".getBytes()));
    }

    private void sendNoTurn() {
        ClientHandler notCurrentPlayer = clients.get((gameLogic.getCurrentPlayerIndex() + 1) % 2);
        sendMessage(notCurrentPlayer.getId(), GameMessageProvider.createMessage(NOT_YOUR_TURN, "".getBytes()));
    }

    public void sendMessage(int connectionId, Message message) {
        ClientHandler client = clients.get(connectionId);
        try {
            Protocol.writeMessage(client.getOutput(), message);
        } catch (MessageWriteException e) {
            client.stop();
        }
    }

    private void sendToAll(Message message) {
        for (ClientHandler client : clients) {
            sendMessage(client.getId(), message);
        }
    }

    private synchronized void handleCardOpenRequest (int x, int y) {
        Card card = gameLogic.getCard(x, y);

        Message message = GameMessageProvider.createMessage(OPEN_CARDS_RESPONSE, (x + " " + y + " " + card.getUniqueCardId()).getBytes());
        sendToAll(message);
    }

    private synchronized void handleMove(int x1, int y1, int x2, int y2) {

        boolean match = gameLogic.makeMove(x1, y1, x2, y2);

        if (match) {
            scores.put(gameLogic.getCurrentPlayerIndex(), scores.get(gameLogic.getCurrentPlayerIndex()) + 1);

            Message message = GameMessageProvider.createMessage(MATCH, (STR."\{x1} \{y1} \{x2} \{y2}").getBytes());
            sendToAll(message);

            if (gameLogic.isGameOver()) {
                endGame();
            } else {
                sendTurn();
                sendNoTurn();
            }

        } else {
            Message message = GameMessageProvider.createMessage(NO_MATCH, (STR."\{x1} \{y1} \{x2} \{y2}").getBytes());
            sendToAll(message);

            gameLogic.switchPlayer(); // Передаем ход другому игроку
            sendTurn();
            sendNoTurn();
        }
    }

    private void endGame() {

        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Integer> entry : gameLogic.getScores().entrySet()) {
            result.append(" ").append(entry.getKey()).append(":").append(entry.getValue());
        }

        Message message = GameMessageProvider.createMessage(END_GAME, result.toString().getBytes());
        sendToAll(message);
    }


    private class ClientHandler implements Runnable {
        private final Socket socket;
        private final InputStream input;
        private final OutputStream output;
        private final int id;

        private boolean alive = false;
        private String name;

        public ClientHandler(Socket socket, InputStream input, OutputStream output) throws IOException {
            this.socket = socket;
            this.input = input;
            this.output = output;
            id = clients.size();
            alive = true;
        }

        @Override
        public void run() {
            try {
                while (alive) {
                    Message message = Protocol.readMessage(input);;
                    if (message != null) {
                        int type = message.getType();
                        if (type == PLAYER_MOVE) {
                            String[] parts = new String(message.getData(), StandardCharsets.UTF_8).split(" ");
                            int x1 = Integer.parseInt(parts[0]);
                            int y1 = Integer.parseInt(parts[1]);
                            int x2 = Integer.parseInt(parts[2]);
                            int y2 = Integer.parseInt(parts[3]);
                            handleMove(x1, y1, x2, y2);
                        } else if (type == SEND_NAME) {
                            this.name = new String(message.getData(), StandardCharsets.UTF_8);
                        } else if (type == OPEN_CARD_REQUEST) {
                            String[] parts = new String(message.getData(), StandardCharsets.UTF_8).split(" ");
                            int x = Integer.parseInt(parts[0]);
                            int y = Integer.parseInt(parts[1]);
                            handleCardOpenRequest(x, y);
                        }
                    }
                }
            } catch (MessageReadException e) {
                throw new ServerException(LogMessages.READ_SERVER_EXCEPTION, e);
            }
        }

        public void stop() {
            try {
                input.close();
                output.close();
                socket.close();
                clients.remove(this);
            } catch (IOException e) {
                throw new ServerException(LogMessages.LOST_CONNECTION_SERVER_EXCEPTIONS, e);
            }
        }

        public OutputStream getOutput() {
            return output;
        }

        public InputStream getInput() {
            return input;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }
}