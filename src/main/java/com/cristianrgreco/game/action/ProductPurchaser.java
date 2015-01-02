package com.cristianrgreco.game.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.cristianrgreco.controller.InformationFrameController;
import com.cristianrgreco.controller.WebDriverController;
import com.cristianrgreco.model.entity.PerformanceData;
import com.cristianrgreco.model.entity.Product;

public class ProductPurchaser implements Runnable {
    private static final double BASE_PRODUCT_EFFICIENCY = Double.MAX_VALUE * -1;
    private static final Logger LOGGER = Logger.getLogger(ProductPurchaser.class);

    private Lock lock;
    private WebDriverController webDriverController;
    private InformationFrameController informationFrameController;
    private Map<Integer, Product> products;

    public ProductPurchaser(Lock lock, WebDriverController webDriverController,
            InformationFrameController informationFrameController) {
        this.lock = lock;
        this.webDriverController = webDriverController;
        this.informationFrameController = informationFrameController;
        this.products = new HashMap<>();
    }

    @Override
    public void run() {
        int currentNumberOfUnlockedProducts = 0;
        int idOfMostCostEffectiveProduct = -1;

        while (true) {
            List<WebElement> unlockedProducts = this.webDriverController.getListOfUnlockedProducts();
            int numberOfUnlockedProducts = unlockedProducts.size();
            if (numberOfUnlockedProducts > currentNumberOfUnlockedProducts) {
                currentNumberOfUnlockedProducts = numberOfUnlockedProducts;
                int productId = numberOfUnlockedProducts - 1;
                WebElement newProduct = unlockedProducts.get(productId);
                this.lock.lock();
                try {
                    this.addNewProductToCache(productId, newProduct);
                } finally {
                    this.lock.unlock();
                }
                idOfMostCostEffectiveProduct = -1;
            }

            PerformanceData performanceData = this.webDriverController.getPerformanceData();
            double numberOfCookies = performanceData.getNumberOfCookies();
            double cookiesPerSecond = performanceData.getCookiesPerSecond();

            if (idOfMostCostEffectiveProduct == -1) {
                double bestProductEfficiency = BASE_PRODUCT_EFFICIENCY;
                for (Map.Entry<Integer, Product> entrySet : this.products.entrySet()) {
                    Integer productId = entrySet.getKey();
                    Product product = entrySet.getValue();
                    String productName = product.getName();
                    double productEfficiency = this.calculateEfficiencyOfProduct(product, cookiesPerSecond);
                    product.setEfficiency(productEfficiency);
                    this.informationFrameController.updateProductTable(productId, product);
                    LOGGER.debug("Efficiency of " + productName + ": " + productEfficiency);
                    if (productEfficiency > bestProductEfficiency) {
                        bestProductEfficiency = productEfficiency;
                        idOfMostCostEffectiveProduct = productId;
                    }
                }
            }

            if (idOfMostCostEffectiveProduct != -1) {
                int productId = idOfMostCostEffectiveProduct;
                Product productToBuy = this.products.get(productId);
                String productName = productToBuy.getName();
                double productPrice = productToBuy.getPrice();

                int percentageComplete = this.calculatePercentageComplete(numberOfCookies, productPrice);
                this.informationFrameController.setProductLabel(productName);
                this.informationFrameController.setProductProgressBar(percentageComplete);

                if (productPrice <= numberOfCookies) {
                    LOGGER.debug("Purchasing product of ID " + productId + ": " + productName);
                    WebElement productToPurchase = unlockedProducts.get(productId);
                    this.lock.lock();
                    try {
                        this.purchaseProductAndUpdateSpecification(productId, productToPurchase);
                    } finally {
                        this.lock.unlock();
                    }
                    idOfMostCostEffectiveProduct = -1;
                }
            }
        }
    }

    private int calculatePercentageComplete(double current, double total) {
        double percentageComplete = (current / total) * 100;
        int percentageCompleteInteger = (int) percentageComplete;
        return percentageCompleteInteger;
    }

    private void addNewProductToCache(int productId, WebElement product) {
        Product newProduct;
        newProduct = this.webDriverController.convertProductWebElementToProductObject(productId, product);
        this.products.put(productId, newProduct);
    }

    private double calculateEfficiencyOfProduct(Product product, double cookiesPerSecond) {
        double productPrice = product.getPrice();
        double productCookiesPerSecond = product.getCookiesPerSecond();
        double productEfficiency = productCookiesPerSecond / productPrice;
        double timeRequiredForProduct = productPrice / cookiesPerSecond;
        productEfficiency = 0 - (timeRequiredForProduct / productEfficiency);
        return productEfficiency;
    }

    private void purchaseProductAndUpdateSpecification(int productId, WebElement product) {
        this.webDriverController.purchaseUnlockedProduct(productId);
        Product updatedProduct;
        updatedProduct = this.webDriverController.convertProductWebElementToProductObject(productId, product);
        this.products.put(productId, updatedProduct);
    }
}