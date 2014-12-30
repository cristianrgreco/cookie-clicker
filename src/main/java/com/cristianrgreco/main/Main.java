package com.cristianrgreco.main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import com.cristianrgreco.factory.GameFactory;
import com.cristianrgreco.game.Game;

public class Main {
    private static final String TARGET_URL = "http://orteil.dashnet.org/cookieclicker/";
    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        setLookAndFeelToMatchSystem();
        initialiseAndStartGame();
    }

    private static void setLookAndFeelToMatchSystem() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            LOGGER.error(null, e);
        }
    }

    private static void initialiseAndStartGame() {
        Game game = GameFactory.chromeGame(TARGET_URL);
        new Thread(game).start();
    }
}
