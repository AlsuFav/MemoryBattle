package ru.itis.memorybattle.client;

import ru.itis.memorybattle.gui.MainUI;
import ru.itis.memorybattle.service.CardService;

public class ClientApp {
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 12345);
        client.connect();
        CardService cardService = new CardService();
        if (client.isConnected()) {
            new MainUI(client, 4, 4, cardService);
        } else {
            System.out.println("Unable to start the game. Server is unavailable.");
        }
    }
}
