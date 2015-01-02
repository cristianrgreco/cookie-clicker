package com.cristianrgreco.controller;

import java.text.ParseException;
import java.util.List;

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
        LOGGER.info("Connecting to target URL");
        this.webDriverAdapter.connectToTargetUrl();
        LOGGER.info("Connected to target URL");
    }

    public void clickOnBigCookie() {
        LOGGER.debug("Clicking on big cookie");
        this.webDriverAdapter.clickOnBigCookie();
    }

    public List<WebElement> getListOfUnlockedProducts() {
        LOGGER.debug("Getting list of unlocked products");
        List<WebElement> unlockedProducts = this.webDriverAdapter.getListOfUnlockedProducts();
        int numberOfUnlockedProducts = unlockedProducts.size();
        LOGGER.debug("Found " + numberOfUnlockedProducts + " unlocked products");
        return unlockedProducts;
    }

    public Product convertProductWebElementToProductObject(int productId, WebElement product) {
        LOGGER.debug("Converting product web element of ID " + productId + " to product object");
        Product productObject = null;
        String productName = null;
        double productPrice = -1;
        try {
            productName = this.webDriverAdapter.getNameOfUnlockedProduct(productId, product);
            String productPriceString = this.webDriverAdapter.getPriceOfUnlockedProduct(productId, product);
            productPrice = this.numberParser.parseDoubleWithAppendedText(productPriceString);
            String productCookiesPerSecondString = this.webDriverAdapter.getCookiesPerSecondOfUnlockedProduct(
                    productId, product);
            double productCookiesPerSecond = this.numberParser
                    .parseDoubleWithAppendedText(productCookiesPerSecondString);
            productObject = new ProductBuilder().setName(productName).setPrice(productPrice)
                    .setCookiesPerSecond(productCookiesPerSecond).build();
        } catch (NoSuchElementException e) {
            LOGGER.debug("New product found, estimating product specification");
            double estimatedCookiesPerSecond = this.estimateProductCookiesPerSecondFromPrice(productPrice);
            productObject = new ProductBuilder().setName(productName).setPrice(productPrice)
                    .setCookiesPerSecond(estimatedCookiesPerSecond).build();
        } catch (StaleElementReferenceException e) {
            LOGGER.debug("Encountered a StaleElementException, retrying");
            productObject = this.convertProductWebElementToProductObject(productId, product);
            return productObject;
        } catch (NumberFormatException | ParseException e) {
            LOGGER.error("Error occurred while getting information for a product", e);
            throw new IllegalStateException(e);
        }
        LOGGER.info("Created the following product object for ID of " + productId + ": " + productObject);
        return productObject;
    }

    private double estimateProductCookiesPerSecondFromPrice(double productPrice) {
        double estimatedCookiesPerSecond = productPrice / 100;
        estimatedCookiesPerSecond -= ((estimatedCookiesPerSecond / 10) * 5);
        return estimatedCookiesPerSecond;
    }

    public void purchaseUnlockedProduct(int productId) {
        LOGGER.info("Purchasing product of ID " + productId);
        this.webDriverAdapter.purchaseUnlockedProduct(productId);
    }

    public PerformanceData getPerformanceData() {
        LOGGER.debug("Getting performance data");
        String performanceString = this.webDriverAdapter.getPerformanceString();
        PerformanceData performanceData = this.performanceParser.parsePerformanceData(performanceString);
        return performanceData;
    }
}
