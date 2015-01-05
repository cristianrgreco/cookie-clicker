package com.cristianrgreco.game.action;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.cristianrgreco.controller.InformationFrameController;
import com.cristianrgreco.controller.WebDriverController;
import com.cristianrgreco.model.entity.PerformanceData;
import com.cristianrgreco.model.entity.Product;

public class ProductPurchaser implements Runnable {
    private static final double BASE_PRODUCT_EFFICIENCY = Double.MAX_VALUE * -1;
    private static final Logger LOGGER = Logger.getLogger(ProductPurchaser.class);

    private WebDriverController webDriverController;
    private InformationFrameController informationFrameController;
    private Map<Integer, Product> products;
    private Set<Integer> purchaseReceipts;

    public ProductPurchaser(WebDriverController webDriverController,
            InformationFrameController informationFrameController) {
        this.webDriverController = webDriverController;
        this.informationFrameController = informationFrameController;
        this.products = new HashMap<>();
        this.purchaseReceipts = new HashSet<>();
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
                this.addNewProductToCache(productId, newProduct);
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
                    this.purchaseProductAndUpdateSpecification(productId, productToPurchase);
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
        if (this.purchaseReceipts.contains(productId)) {
            newProduct = this.webDriverController.createProductObjectFromWebPage(productId, product);
        } else {
            newProduct = this.webDriverController.createProductObjectFromEstimate(productId, product);
        }
        this.products.put(productId, newProduct);
    }

    private double calculateEfficiencyOfProduct(Product product, double cookiesPerSecond) {
        double productPrice = product.getPrice();
        double productCookiesPerSecond = product.getCookiesPerSecond();
        double productEfficiency = productCookiesPerSecond / productPrice;
        return productEfficiency;
    }

    private void purchaseProductAndUpdateSpecification(int productId, WebElement product) {
        this.webDriverController.purchaseUnlockedProduct(productId);
        Product updatedProduct;
        updatedProduct = this.webDriverController.createProductObjectFromWebPage(productId, product);
        this.products.put(productId, updatedProduct);
        this.purchaseReceipts.add(productId);
    }
}
