package ru.itis.memorybattle.client;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Client extends Component {
    private final String serverAddress;
    private final int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean connected;
    private String name;
    private GameListener gameListener;


    public Client(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.connected = false;
    }

    public void connect() {
        try {
            socket = new Socket(serverAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Получение имени клиента
            System.out.print("Введите имя игрока: ");
            name = new BufferedReader(new InputStreamReader(System.in)).readLine();
            out.println(name);

            connected = true;
            System.out.println("Подключен к серверу: " + serverAddress + ":" + port);
        } catch (IOException e) {
            System.out.println("Не удалось подключиться к серверу: " + e.getMessage());
        }
    }

    public void sendMove(int x1, int y1, int x2, int y2) {
        if (connected) {
            out.println("PLAYER_MOVE " + x1 + " " + y1 + " " + x2 + " " + y2);
        }
    }


    public boolean isConnected() {
        return connected;
    }

    public String getName() {
        return name;
    }

    public void addGameListener(GameListener listener) {
        this.gameListener = listener;
        new Thread(this::listenForServerMessages).start();
    }

    private void listenForServerMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (gameListener != null) {
                    gameListener.onGameMessage(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEndGame(String message) {
        String[] parts = message.split(" ");
        StringBuilder result = new StringBuilder("Игра окончена! Результаты:\n");

        for (int i = 1; i < parts.length; i++) {
            result.append(parts[i]).append("\n");
        }

        JOptionPane.showMessageDialog(this, result.toString());
        System.exit(0);
    }
}
