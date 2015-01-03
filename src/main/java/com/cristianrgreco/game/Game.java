package com.cristianrgreco.game;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.cristianrgreco.controller.InformationFrameController;
import com.cristianrgreco.controller.WebDriverController;
import com.cristianrgreco.game.action.BigCookieClicker;
import com.cristianrgreco.game.action.ProductPurchaser;

public class Game {
    private static final Lock LOCK = new ReentrantLock(true);
    private static final int NUMBER_OF_CPUS = Runtime.getRuntime().availableProcessors();
    private static final Logger LOGGER = Logger.getLogger(Game.class);

    private WebDriverController webDriverController;
    private InformationFrameController informationFrameController;

    public Game(WebDriverController controller, InformationFrameController informationFrameController) {
        this.webDriverController = controller;
        this.informationFrameController = informationFrameController;
    }

    public void startGame() {
        LOGGER.debug("Starting new game with up to " + NUMBER_OF_CPUS + " threads");
        this.webDriverController.connectToTargetUrl();
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_CPUS);
        executor.execute(new BigCookieClicker(LOCK, this.webDriverController, this.informationFrameController));
        executor.execute(new ProductPurchaser(LOCK, this.webDriverController, this.informationFrameController));
    }
}
