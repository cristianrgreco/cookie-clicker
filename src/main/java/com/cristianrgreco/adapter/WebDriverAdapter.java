package com.cristianrgreco.adapter;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    public WebDriverAdapter(WebDriver webDriver, String targetUrl) {
        this.webDriver = webDriver;
        this.actions = new Actions(this.webDriver);
        this.webDriverWait = new WebDriverWait(this.webDriver, WEB_DRIVER_WAIT_SECONDS);
        this.targetUrl = targetUrl;
    }

    public void connectToTargetUrl() {
        this.webDriver.get(this.targetUrl);
        LOGGER.debug("Waiting for page elements to load");
        this.webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("product0")));
    }

    public void clickOnBigCookie() {
        WebElement bigCookie = this.webDriver.findElement(By.id("bigCookie"));
        bigCookie.click();
    }

    public List<WebElement> getListOfUnlockedProducts() {
        List<WebElement> unlockedProducts = this.webDriver.findElements(By.cssSelector(".product.unlocked"));
        return unlockedProducts;
    }

    public String getNameOfUnlockedProduct(int productId, WebElement product) {
        String productName = product.findElement(By.id("productName" + productId)).getText();
        return productName;
    }

    public String getPriceOfUnlockedProduct(int productId, WebElement product) {
        String priceOfProduct = product.findElement(By.id("productPrice" + productId)).getText();
        return priceOfProduct;
    }

    public String getCookiesPerSecondOfUnlockedProduct(int productId, WebElement product) {
        this.actions.moveToElement(product).perform();
        String cookiesPerSecondOfProduct = this.webDriver.findElement(By.id("tooltip"))
                .findElement(By.tagName("b")).getText();
        return cookiesPerSecondOfProduct;
    }

    public void purchaseUnlockedProduct(int productId) {
        this.webDriver.findElement(By.id("product" + productId)).click();
    }

    public String getPerformanceString() {
        String performanceString = this.webDriver.findElement(By.id("cookies")).getText();
        return performanceString;
    }
}