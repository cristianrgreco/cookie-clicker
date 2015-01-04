package com.cristianrgreco.game.action;

import org.apache.log4j.Logger;

import com.cristianrgreco.controller.InformationFrameController;
import com.cristianrgreco.controller.WebDriverController;

public class BigCookieClicker implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(BigCookieClicker.class);

    private WebDriverController webDriverController;
    private InformationFrameController informationFrameController;

    public BigCookieClicker(WebDriverController webDriverController,
            InformationFrameController informationFrameController) {
        this.webDriverController = webDriverController;
        this.informationFrameController = informationFrameController;
    }

    @Override
    public void run() {
        while (true) {
            this.webDriverController.clickOnBigCookie();
        }
    }
}
