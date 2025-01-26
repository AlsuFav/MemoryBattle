package ru.itis.memorybattle.server;

import ru.itis.memorybattle.core.GameLogic;
import ru.itis.memorybattle.exceptions.DbConfigException;
import ru.itis.memorybattle.repository.CardDao;
import ru.itis.memorybattle.repository.impl.CardDaoImpl;
import ru.itis.memorybattle.service.CardService;
import ru.itis.memorybattle.service.impl.CardServiceImpl;

import java.io.IOException;

import static ru.itis.memorybattle.utils.GameSettings.*;

public class ServerApp {
    public static void main(String[] args) throws IOException, DbConfigException {

        CardDao cardDao = new CardDaoImpl();
        CardService cardService = new CardServiceImpl(cardDao);

        GameLogic gameLogic = new GameLogic(ROWS, COLS, cardService);

        Server server = new Server(PORT, gameLogic);
        server.start();
    }
}