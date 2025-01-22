package ru.itis.memorybattle.client;

import ru.itis.memorybattle.gui.MainUI;
import ru.itis.memorybattle.gui.ConfigUI;

import static ru.itis.memorybattle.utils.GameSettings.COLS;
import static ru.itis.memorybattle.utils.GameSettings.ROWS;

public class ClientApp {
    public static void main(String[] args) {
        ConfigUI configUI = new ConfigUI();

        // Ожидаем, пока пользователь введет данные
        while (configUI.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String serverIP = configUI.getIp();
        int serverPort = configUI.getPort();
        String name = configUI.getName();

        // Создаем клиента с заданными параметрами
        Client client = new Client(serverIP, serverPort, name);
        client.connect();

        if (client.isConnected()) {
            new MainUI(client, ROWS, COLS);
        } else {
            System.out.println("Unable to start the game. Server is unavailable.");
        }
    }
}
