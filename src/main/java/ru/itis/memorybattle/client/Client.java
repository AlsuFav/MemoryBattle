package ru.itis.memorybattle.client;

import ru.itis.memorybattle.exceptions.ClientException;
import ru.itis.memorybattle.exceptions.MessageReadException;
import ru.itis.memorybattle.exceptions.MessageWriteException;
import ru.itis.memorybattle.exceptions.ServerException;
import ru.itis.memorybattle.gui.MainUI;
import ru.itis.memorybattle.model.Message;
import ru.itis.memorybattle.protocol.Protocol;
import ru.itis.memorybattle.utils.GameMessageProvider;
import ru.itis.memorybattle.utils.LogMessages;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import static ru.itis.memorybattle.utils.MessageType.*;


public class Client extends Component {
    private final String serverAddress;
    private final int port;
    private final String name;
    private Socket socket;
    private MainUI mainUI;
    private ClientThread clientThread;


    public Client(String serverAddress, int port, MainUI mainUI, String name) {
        this.serverAddress = serverAddress;
        this.port = port;

        this.mainUI = mainUI;
        mainUI.setClient(this);

        this.name = name;
    }

    public void connect() {
        try {
            socket = new Socket(serverAddress, port);

            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            this.clientThread = new ClientThread(socket, input, output);
            new Thread(clientThread).start();

            sendPlayerName(name);

            System.out.println("Подключен к серверу: " + serverAddress + ":" + port);
        } catch (IOException e) {
            System.out.println("Не удалось подключиться к серверу: " + e.getMessage());
        }
    }

    public synchronized void sendMessage(Message message) {
        try {
            Protocol.writeMessage(clientThread.getOutput(), message);
        } catch (MessageWriteException e) {
            clientThread.stop();
            throw new ClientException(LogMessages.WRITE_CLIENT_EXCEPTION, e);
        }
    }

    public synchronized void sendPlayerName(String name) {
        Message message = GameMessageProvider.createMessage(INITIALIZE_PLAYER, name.getBytes());
        sendMessage(message);
    }

    public synchronized void sendMove(int x1, int y1, int x2, int y2) {
        Message message = GameMessageProvider.createMessage(PLAYER_MOVE, (x1 + " " + y1 + " " + x2 + " " + y2).getBytes());
        sendMessage(message);
    }

    public synchronized void sendCardOpenRequest(int x, int y) {
        Message message = GameMessageProvider.createMessage(OPEN_CARD_REQUEST, (x + " " + y).getBytes());
        sendMessage(message);
    }

    public String getName() {
        return name;
    }

    private class ClientThread implements Runnable {
        private final Socket socket;
        private final InputStream input;
        private final OutputStream output;
        private boolean alive = false;


        public ClientThread(Socket socket, InputStream input, OutputStream output) throws IOException {
            this.socket = socket;
            this.input = input;
            this.output = output;
            this.alive = true;
        }

        @Override
        public void run() {
            try {
                while (alive) {
                    Message message = Protocol.readMessage(input);

                    if (message != null) {
                        int type = message.getType();

                        if (type == START_GAME) {
                            String[] parts = new String(message.getData(), StandardCharsets.UTF_8).split(" ");
                            int rows = Integer.parseInt(parts[0]);
                            int cols = Integer.parseInt(parts[1]);

                            mainUI.initializeGameBoard(rows, cols);
                        }

                        else if (type == SCORES_UPDATE) {
                            String[] parts = new String(message.getData(), StandardCharsets.UTF_8).split(" ");
                            String player1 = parts[0];
                            int scores1 = Integer.parseInt(parts[1]);
                            String player2 = parts[2];
                            int scores2 = Integer.parseInt(parts[3]);

                            mainUI.updateScores(player1, scores1, player2, scores2);
                        }

                        else if (type == WAIT_ANOTHER_PLAYER) {
                            mainUI.showWaiting();
                        }

                        else if (type == TURN) {
                            mainUI.setMyTurn(true);
                            mainUI.showTurn();
                        }

                        else if (type == NOT_YOUR_TURN) {
                            mainUI.setMyTurn(false);
                            mainUI.showNoTurn();
                        }

                        else if (type == EXTRA_TURN) {
                            mainUI.setMyTurn(true);
                            mainUI.showExtraTurn();
                        }

                        else if (type == NOT_YOUR_EXTRA_TURN) {
                            mainUI.setMyTurn(false);
                            mainUI.showNoExtraTurn();
                        }

                        else if (type == MATCH) {
                            String[] parts = new String(message.getData(), StandardCharsets.UTF_8).split(" ");
                            int x1 = Integer.parseInt(parts[0]);
                            int y1 = Integer.parseInt(parts[1]);
                            int x2 = Integer.parseInt(parts[2]);
                            int y2 = Integer.parseInt(parts[3]);

                            mainUI.handleMatch(x1, y1, x2, y2);
                        }

                        else if (type == NO_MATCH) {
                            String[] parts = new String(message.getData(), StandardCharsets.UTF_8).split(" ");
                            int x1 = Integer.parseInt(parts[0]);
                            int y1 = Integer.parseInt(parts[1]);
                            int x2 = Integer.parseInt(parts[2]);
                            int y2 = Integer.parseInt(parts[3]);

                            mainUI.handleNoMatch(x1, y1, x2, y2);
                        }

                        else if (type == END_GAME) {
                            String winner = new String(message.getData(), StandardCharsets.UTF_8);
                            mainUI.handleEndGame(winner);
                        }

                        else if (type == OPEN_CARD_RESPONSE) {
                            String[] parts = new String(message.getData(), StandardCharsets.UTF_8).split(" ");
                            int x = Integer.parseInt(parts[0]);
                            int y = Integer.parseInt(parts[1]);
                            String source = parts[2];

                            mainUI.handleCardOpen(x, y, source);
                        }

                        else if (type == OPEN_SPECIAL_CARD) {
                            String[] parts = new String(message.getData(), StandardCharsets.UTF_8).split(" ");
                            int x = Integer.parseInt(parts[0]);
                            int y = Integer.parseInt(parts[1]);
                            String source = parts[2];

                            mainUI.handleSpecialCardOpen(x, y, source);
                        }

                        else if (type == MOVE_AFTER_SPECIAL_CARD_OPEN) {
                            mainUI.handleSpecialCardOpen();
                        }

                        else if (type == SPECIAL_CARD_EXTRA_TURN) {
                            mainUI.showSpecialCardExtraTurnOpen();
                        }

                        else if (type == SPECIAL_CARD_SHUFFLE) {
                            mainUI.showSpecialCardShuffleOpen();
                        }
                    }
                }
            } catch (MessageReadException e) {
                throw new ServerException(LogMessages.READ_CLIENT_EXCEPTION, e);
            }
        }

        public void stop() {
            try {
                input.close();
                output.close();
                socket.close();
            } catch (IOException e) {
                throw new ServerException(LogMessages.LOST_CONNECTION_CLIENT_EXCEPTION, e);
            }
        }

        public OutputStream getOutput() {
            return output;
        }
    }
}