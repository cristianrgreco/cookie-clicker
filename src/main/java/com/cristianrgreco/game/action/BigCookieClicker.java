package com.cristianrgreco.game.action;

import java.util.concurrent.locks.Lock;

import org.apache.log4j.Logger;

import com.cristianrgreco.controller.InformationFrameController;
import com.cristianrgreco.controller.WebDriverController;

public class BigCookieClicker implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(BigCookieClicker.class);

    private Lock lock;
    private WebDriverController webDriverController;
    private InformationFrameController informationFrameController;

    public BigCookieClicker(Lock lock, WebDriverController webDriverController,
            InformationFrameController informationFrameController) {
        this.lock = lock;
        this.webDriverController = webDriverController;
        this.informationFrameController = informationFrameController;
    }

    @Override
    public void run() {
        while (true) {
            this.lock.lock();
            try {
                this.webDriverController.clickOnBigCookie();
            } finally {
                this.lock.unlock();
            }
        }
    }
}
