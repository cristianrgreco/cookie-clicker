package com.cristianrgreco.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverAdapter {
    private static final long WEB_DRIVER_WAIT_SECONDS = 15L;
    private static final Logger LOGGER = Logger.getLogger(WebDriverAdapter.class);

    private WebDriver webDriver;
    private Actions actions;
    private WebDriverWait webDriverWait;
    private String targetUrl;

    private WebElement bigCookie;
    private Action bigCookieAction;
    private WebElement cookies;
    private WebElement tooltip;
    private Map<Integer, WebElement> unlockedProducts;
    private Map<Integer, String> unlockedProductNames;
    private Map<Integer, String> unlockedProductCookiesPerSecond;

    public WebDriverAdapter(WebDriver webDriver, String targetUrl) {
        this.webDriver = webDriver;
        this.actions = new Actions(this.webDriver);
        this.webDriverWait = new WebDriverWait(this.webDriver, WEB_DRIVER_WAIT_SECONDS);
        this.targetUrl = targetUrl;
        this.unlockedProducts = new HashMap<>();
        this.unlockedProductNames = new HashMap<>();
        this.unlockedProductCookiesPerSecond = new HashMap<>();
    }

    public void connectToTargetUrl() {
        this.webDriver.get(this.targetUrl);
        LOGGER.debug("Waiting for page elements to load");
        this.webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("product0")));
    }

    public void clickOnBigCookie() {
        if (this.bigCookie == null) {
            this.bigCookie = this.webDriver.findElement(By.id("bigCookie"));
            Actions bigCookieActions = new Actions(this.webDriver).moveToElement(this.bigCookie);
            for (int i = 0; i < 50; i++) {
                bigCookieActions = bigCookieActions.click();
            }
            this.bigCookieAction = bigCookieActions.build();
        }
        this.bigCookieAction.perform();
    }

    public List<WebElement> getListOfUnlockedProducts() {
        List<WebElement> unlockedProducts = this.webDriver.findElements(By.cssSelector(".product.unlocked"));
        return unlockedProducts;
    }

    public String getNameOfUnlockedProduct(int productId, WebElement product) {
        String productName = this.unlockedProductNames.get(productId);
        if (productName == null) {
            productName = product.findElement(By.id("productName" + productId)).getText();
            this.unlockedProductNames.put(productId, productName);
        }
        return productName;
    }

    public String getPriceOfUnlockedProduct(int productId, WebElement product) {
        String priceOfProduct = product.findElement(By.id("productPrice" + productId)).getText();
        return priceOfProduct;
    }

    public String getCookiesPerSecondOfUnlockedProduct(int productId, WebElement product) {
        String cookiesPerSecond = this.unlockedProductCookiesPerSecond.get(productId);
        if (cookiesPerSecond == null) {
            if (this.tooltip == null) {
                this.tooltip = this.webDriver.findElement(By.id("tooltip"));
            }
            this.actions.moveToElement(product).perform();
            boolean isTooltipVisible = false;
            while (!isTooltipVisible) {
                isTooltipVisible = this.tooltip.getText().length() > 0;
            }
            cookiesPerSecond = this.tooltip.findElement(By.tagName("b")).getText();
            this.unlockedProductCookiesPerSecond.put(productId, cookiesPerSecond);
        }
        return cookiesPerSecond;
    }

    public void purchaseUnlockedProduct(int productId) {
        WebElement unlockedProduct = this.unlockedProducts.get(productId);
        if (unlockedProduct == null) {
            unlockedProduct = this.webDriver.findElement(By.id("product" + productId));
            this.unlockedProducts.put(productId, unlockedProduct);
        }
        unlockedProduct.click();
    }

    public String getPerformanceString() {
        if (this.cookies == null) {
            this.cookies = this.webDriver.findElement(By.id("cookies"));
        }
        String performanceString = this.cookies.getText();
        return performanceString;
    }
}