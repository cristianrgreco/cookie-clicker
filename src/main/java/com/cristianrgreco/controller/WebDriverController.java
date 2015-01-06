package com.cristianrgreco.controller;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import com.cristianrgreco.adapter.WebDriverAdapter;
import com.cristianrgreco.model.builder.ProductBuilder;
import com.cristianrgreco.model.entity.PerformanceData;
import com.cristianrgreco.model.entity.Product;
import com.cristianrgreco.util.NumberParser;
import com.cristianrgreco.util.PerformanceParser;

public class WebDriverController {
    private static final Lock LOCK = new ReentrantLock(true);
    private static final Logger LOGGER = Logger.getLogger(WebDriverController.class);

    private WebDriverAdapter webDriverAdapter;
    private NumberParser numberParser;
    private PerformanceParser performanceParser;

    public WebDriverController(WebDriverAdapter webDriverAdapter, NumberParser numberParser,
            PerformanceParser performanceParser) {
        this.webDriverAdapter = webDriverAdapter;
        this.numberParser = numberParser;
        this.performanceParser = performanceParser;
    }

    public void connectToTargetUrl() {
        LOCK.lock();
        try {
            LOGGER.info("Connecting to target URL");
            this.webDriverAdapter.connectToTargetUrl();
            LOGGER.info("Connected to target URL");
        } finally {
            LOCK.unlock();
        }
    }

    public void clickOnBigCookie() {
        LOCK.lock();
        try {
            LOGGER.debug("Clicking on big cookie");
            this.webDriverAdapter.clickOnBigCookie();
        } finally {
            LOCK.unlock();
        }
    }

    public List<WebElement> getListOfUnlockedProducts() {
        List<WebElement> unlockedProducts;
        LOCK.lock();
        try {
            LOGGER.debug("Getting list of unlocked products");
            unlockedProducts = this.webDriverAdapter.getListOfUnlockedProducts();
        } finally {
            LOCK.unlock();
        }
        int numberOfUnlockedProducts = unlockedProducts.size();
        LOGGER.debug("Found " + numberOfUnlockedProducts + " unlocked products");
        return unlockedProducts;
    }

    public Product createProductObjectFromEstimate(int productId, WebElement product) {
        LOGGER.debug("Converting product web element of ID " + productId + " to object from estimate");
        Product productObject;
        String productName = null;
        double estimatedCookiesPerSecond;
        double productPrice = -1;
        try {
            LOCK.lock();
            try {
                productName = this.webDriverAdapter.getNameOfUnlockedProduct(productId, product);
                String productPriceString = this.webDriverAdapter.getPriceOfUnlockedProduct(productId, product);
                productPrice = this.numberParser.parseDoubleWithAppendedText(productPriceString);
                estimatedCookiesPerSecond = this.estimateProductCookiesPerSecondFromPrice(productPrice);
            } finally {
                LOCK.unlock();
            }
            productObject = new ProductBuilder().setName(productName).setPrice(productPrice)
                    .setCookiesPerSecond(estimatedCookiesPerSecond).build();
        } catch (StaleElementReferenceException e) {
            LOGGER.debug("Encountered a StaleElementException, retrying");
            productObject = this.createProductObjectFromEstimate(productId, product);
            return productObject;
        } catch (NumberFormatException | ParseException e) {
            LOGGER.error("Error occurred while getting information for a product", e);
            throw new IllegalStateException(e);
        }
        LOGGER.info("Created the following product for ID of " + productId + ": " + productObject);
        return productObject;
    }

    private double estimateProductCookiesPerSecondFromPrice(double productPrice) {
        double estimatedCookiesPerSecond = productPrice / 100;
        estimatedCookiesPerSecond -= ((estimatedCookiesPerSecond / 10) * 5);
        return estimatedCookiesPerSecond;
    }

    public Product createProductObjectFromWebPage(int productId, WebElement product) {
        LOGGER.debug("Converting product web element of ID " + productId + " to object from web page");
        Product productObject;
        String productName = null;
        String productCookiesPerSecondString;
        double productPrice = -1;
        try {
            LOCK.lock();
            try {
                productName = this.webDriverAdapter.getNameOfUnlockedProduct(productId, product);
                String productPriceString = this.webDriverAdapter.getPriceOfUnlockedProduct(productId, product);
                productPrice = this.numberParser.parseDoubleWithAppendedText(productPriceString);
                productCookiesPerSecondString = this.webDriverAdapter.getCookiesPerSecondOfUnlockedProduct(
                        productId, product);
            } finally {
                LOCK.unlock();
            }
            double productCookiesPerSecond = this.numberParser
                    .parseDoubleWithAppendedText(productCookiesPerSecondString);
            productObject = new ProductBuilder().setName(productName).setPrice(productPrice)
                    .setCookiesPerSecond(productCookiesPerSecond).build();
        } catch (NoSuchElementException e) {
            LOGGER.debug("Product does not expose CPS, creating estimate");
            double estimatedCookiesPerSecond = this.estimateProductCookiesPerSecondFromPrice(productPrice);
            productObject = new ProductBuilder().setName(productName).setPrice(productPrice)
                    .setCookiesPerSecond(estimatedCookiesPerSecond).build();
        } catch (StaleElementReferenceException e) {
            LOGGER.debug("Encountered a StaleElementException, retrying");
            productObject = this.createProductObjectFromWebPage(productId, product);
            return productObject;
        } catch (NumberFormatException | ParseException e) {
            LOGGER.error("Error occurred while getting information for a product", e);
            throw new IllegalStateException(e);
        }
        LOGGER.info("Created the following product for ID of " + productId + ": " + productObject);
        return productObject;
    }

    public void purchaseUnlockedProduct(int productId) {
        LOCK.lock();
        try {
            LOGGER.info("Purchasing product of ID " + productId);
            this.webDriverAdapter.purchaseUnlockedProduct(productId);
        } finally {
            LOCK.unlock();
        }
    }

    public PerformanceData getPerformanceData() {
        String performanceString;
        LOCK.lock();
        try {
            LOGGER.debug("Getting performance data");
            performanceString = this.webDriverAdapter.getPerformanceString();
        } finally {
            LOCK.unlock();
        }
        PerformanceData performanceData = this.performanceParser.parsePerformanceData(performanceString);
        return performanceData;
    }
}
