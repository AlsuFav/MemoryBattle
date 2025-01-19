package ru.itis.memorybattle.client;

import ru.itis.memorybattle.gui.MainUI;

public class ClientApp {
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 12345);
        client.connect();

        if (client.isConnected()) {
            new MainUI(client, 4, 4);
        } else {
            System.out.println("Unable to start the game. Server is unavailable.");
        }
    }
}
