package com.cristianrgreco.controller;

import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import com.cristianrgreco.adapter.WebDriverAdapter;
import com.cristianrgreco.model.builder.ProductBuilder;
import com.cristianrgreco.model.entity.PerformanceData;
import com.cristianrgreco.model.entity.Product;
import com.cristianrgreco.util.NumberFormatter;

public class WebDriverController {
    private static final Pattern PERFORMANCE_STRING = Pattern.compile("(.*?)cookie.*?: (.*)", Pattern.DOTALL);
    private static final Logger LOGGER = Logger.getLogger(WebDriverController.class);

    private WebDriverAdapter webDriverAdapter;
    private NumberFormatter numberFormatter;

    public WebDriverController(WebDriverAdapter webDriverAdapter, NumberFormatter numberFormatter) {
        this.webDriverAdapter = webDriverAdapter;
        this.numberFormatter = numberFormatter;
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
            productPrice = this.numberFormatter.parseDoubleWithAppendedText(productPriceString);
            String productCookiesPerSecondString = this.webDriverAdapter.getCookiesPerSecondOfUnlockedProduct(
                    productId, product);
            double productCookiesPerSecond = this.numberFormatter
                    .parseDoubleWithAppendedText(productCookiesPerSecondString);
            productObject = new ProductBuilder().setName(productName).setPrice(productPrice)
                    .setCookiesPerSecond(productCookiesPerSecond).build();
        } catch (NoSuchElementException | NumberFormatException | ParseException e) {
            LOGGER.debug("No product cookies per second found, estimating product specification");
            double estimatedCookiesPerSecond = this.estimateProductCookiesPerSecondFromPrice(productPrice);
            productObject = new ProductBuilder().setName(productName).setPrice(productPrice)
                    .setCookiesPerSecond(estimatedCookiesPerSecond).build();
        } catch (StaleElementReferenceException e) {
            LOGGER.debug("Encountered a StaleElementException, retrying");
            productObject = this.convertProductWebElementToProductObject(productId, product);
            return productObject;
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
        String performanceString = this.webDriverAdapter.getPerformanceString();
        Matcher matcher = PERFORMANCE_STRING.matcher(performanceString);
        matcher.find();
        String numberOfCookiesString = matcher.group(1);
        double numberOfCookies = this.parseNumberOfCookies(numberOfCookiesString);
        String cookiesPerSecondString = matcher.group(2);
        double cookiesPerSecond = this.parseCookiesPerSecond(cookiesPerSecondString);
        PerformanceData performanceData = new PerformanceData(numberOfCookies, cookiesPerSecond);
        LOGGER.debug("Got the following performance data: " + performanceData);
        return performanceData;
    }

    private double parseNumberOfCookies(String numberOfCookiesString) {
        double numberOfCookies = -1;
        try {
            numberOfCookies = this.numberFormatter.parseDoubleWithAppendedText(numberOfCookiesString);
        } catch (ParseException e) {
            LOGGER.error(null, e);
        }
        return numberOfCookies;
    }

    private double parseCookiesPerSecond(String cookiesPerSecondString) {
        double cookiesPerSecond = -1;
        try {
            cookiesPerSecond = this.numberFormatter.parseDoubleWithAppendedText(cookiesPerSecondString);
            if (cookiesPerSecond == 0.0) {
                cookiesPerSecond = 0.1;
            }
        } catch (ParseException e) {
            LOGGER.error(null, e);
        }
        return cookiesPerSecond;
    }
}
