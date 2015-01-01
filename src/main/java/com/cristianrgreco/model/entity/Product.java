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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.cookiesPerSecond);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        temp = Double.doubleToLongBits(this.price);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        if (Double.doubleToLongBits(this.cookiesPerSecond) != Double.doubleToLongBits(other.cookiesPerSecond))
            return false;
        if (this.name == null) {
            if (other.name != null)
                return false;
        } else if (!this.name.equals(other.name))
            return false;
        if (Double.doubleToLongBits(this.price) != Double.doubleToLongBits(other.price))
            return false;
        return true;
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
