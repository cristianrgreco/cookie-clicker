package com.cristianrgreco.factory;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.cristianrgreco.adapter.WebDriverAdapter;
import com.cristianrgreco.controller.WebDriverController;
import com.cristianrgreco.util.NumberParser;
import com.cristianrgreco.util.PerformanceParser;

public class WebDriverFactory {
    private static final String CHROME_DRIVER_PROPERTY_KEY = "webdriver.chrome.driver";
    private static final Logger LOGGER = Logger.getLogger(WebDriverFactory.class);

    private WebDriverFactory() {
    }

    public static WebDriverController firefoxWebDriver(String targetUrl) {
        LOGGER.info("Creating new Firefox web driver for URL " + targetUrl);
        WebDriver webDriver = new FirefoxDriver();
        WebDriverController webDriverController = createControllerFromDriver(webDriver, targetUrl);
        return webDriverController;
    }

    public static WebDriverController chromeWebDriver(String targetUrl) {
        LOGGER.info("Creating new Chrome web driver for URL: " + targetUrl);
        String chromeDriverUri = System.getProperty(CHROME_DRIVER_PROPERTY_KEY);
        LOGGER.info("Using driver from system property " + CHROME_DRIVER_PROPERTY_KEY + ": " + chromeDriverUri);
        WebDriver webDriver = new ChromeDriver();
        WebDriverController webDriverController = createControllerFromDriver(webDriver, targetUrl);
        return webDriverController;
    }

    private static WebDriverController createControllerFromDriver(WebDriver webDriver, String targetUrl) {
        WebDriverAdapter webDriverAdapter = new WebDriverAdapter(webDriver, targetUrl);
        NumberParser numberParser = new NumberParser();
        PerformanceParser performanceParser = new PerformanceParser(numberParser);
        WebDriverController webDriverController = new WebDriverController(webDriverAdapter, numberParser,
                performanceParser);
        return webDriverController;
    }
}
