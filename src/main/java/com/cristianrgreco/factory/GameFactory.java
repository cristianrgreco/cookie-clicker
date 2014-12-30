package com.cristianrgreco.factory;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.cristianrgreco.controller.InformationFrameController;
import com.cristianrgreco.controller.WebDriverController;
import com.cristianrgreco.game.Game;
import com.cristianrgreco.ui.InformationFrame;

public class GameFactory {
    private static InformationFrame informationFrame;
    private static final Logger LOGGER = Logger.getLogger(GameFactory.class);

    static {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                informationFrame = new InformationFrame();
            }
        });
    }

    private GameFactory() {
    }

    public static Game chromeGame(String targetUrl) {
        LOGGER.info("Creating new Chrome game");
        WebDriverController webDriverController = WebDriverFactory.chromeWebDriver(targetUrl);
        Game chromeGame = createGameFromController(webDriverController);
        return chromeGame;
    }

    public static Game firefoxGame(String targetUrl) {
        LOGGER.info("Creating new Firefox game");
        WebDriverController webDriverController = WebDriverFactory.firefoxWebDriver(targetUrl);
        Game firefoxGame = createGameFromController(webDriverController);
        return firefoxGame;
    }

    private static Game createGameFromController(WebDriverController webDriverController) {
        InformationFrameController informationFrameController = new InformationFrameController(informationFrame);
        Game game = new Game(webDriverController, informationFrameController);
        return game;
    }
}
