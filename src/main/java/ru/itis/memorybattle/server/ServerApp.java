package ru.itis.memorybattle.server;

import java.io.IOException;
import java.sql.SQLException;

public class ServerApp {
    public static void main(String[] args) throws IOException, SQLException {
        Server server = new Server(); // Укажите нужный порт
        server.start();
    }
}
