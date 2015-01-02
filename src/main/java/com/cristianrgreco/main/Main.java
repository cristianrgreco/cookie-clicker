package com.cristianrgreco.main;

import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.cristianrgreco.factory.GameFactory;
import com.cristianrgreco.game.Game;

public class Main {
    private static final String TARGET_URL = "http://orteil.dashnet.org/cookieclicker/";
    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        LOGGER.info("Starting cookie clicker");
        setLookAndFeelToMatchSystem();
        initialiseAndStartGame();
    }

    private static void setLookAndFeelToMatchSystem() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }

    private static void initialiseAndStartGame() {
        Game game = GameFactory.chromeGame(TARGET_URL);
        game.startGame();
    }
}
