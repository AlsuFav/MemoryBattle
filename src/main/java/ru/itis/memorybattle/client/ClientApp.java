package ru.itis.memorybattle.client;

import ru.itis.memorybattle.gui.MainUI;
import ru.itis.memorybattle.gui.ConfigUI;


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

        MainUI mainUI = new MainUI();

        Client client = new Client(serverIP, serverPort, mainUI, name);
        client.connect();

    }
}