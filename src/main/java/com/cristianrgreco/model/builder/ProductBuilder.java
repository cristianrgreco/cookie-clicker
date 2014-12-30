package com.cristianrgreco.model.builder;

import com.cristianrgreco.model.entity.Product;

public class ProductBuilder {
    private String name;
    private double price;
    private double cookiesPerSecond;

    public ProductBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder setPrice(double price) {
        this.price = price;
        return this;
    }

    public ProductBuilder setCookiesPerSecond(double cookiesPerSecond) {
        this.cookiesPerSecond = cookiesPerSecond;
        return this;
    }

    public Product build() {
        Product product = new Product();
        product.setName(this.name);
        product.setPrice(this.price);
        product.setCookiesPerSecond(this.cookiesPerSecond);
        return product;
    }
}
