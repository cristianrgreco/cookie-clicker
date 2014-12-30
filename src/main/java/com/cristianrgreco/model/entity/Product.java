package com.cristianrgreco.model.entity;

public class Product {
    private String name;
    private double price;
    private double cookiesPerSecond;
    private double efficiency;

    @Override
    public String toString() {
        return "Product [name=" + this.name + ", price=" + this.price + ", cookiesPerSecond="
                + this.cookiesPerSecond + "]";
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCookiesPerSecond() {
        return this.cookiesPerSecond;
    }

    public void setCookiesPerSecond(double cookiesPerSecond) {
        this.cookiesPerSecond = cookiesPerSecond;
    }

    public double getEfficiency() {
        return this.efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }
}
