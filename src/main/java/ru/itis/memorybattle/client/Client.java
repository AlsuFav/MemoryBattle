package ru.itis.memorybattle.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private final String serverAddress;
    private final int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean connected;
    private String name;

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

    public void listen(ServerResponseHandler handler) {
        new Thread(() -> {
            try {
                String response;
                while ((response = in.readLine()) != null) {
                    handler.handle(response);
                }
            } catch (IOException e) {
                System.out.println("Соединение потеряно: " + e.getMessage());
            }
        }).start();
    }

    public boolean isConnected() {
        return connected;
    }

    public String getName() {
        return name;
    }
}