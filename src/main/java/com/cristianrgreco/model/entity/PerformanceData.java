package com.cristianrgreco.model.entity;

public class PerformanceData {
    private double numberOfCookies;
    private double cookiesPerSecond;

    public PerformanceData() {
        this(-1, -1);
    }

    public PerformanceData(double numberOfCookies, double cookiesPerSecond) {
        this.numberOfCookies = numberOfCookies;
        this.cookiesPerSecond = cookiesPerSecond;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.cookiesPerSecond);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.numberOfCookies);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        PerformanceData otherPerformanceData = (PerformanceData) obj;
        return this.getCookiesPerSecond() == otherPerformanceData.getCookiesPerSecond()
                && this.getNumberOfCookies() == otherPerformanceData.getNumberOfCookies();
    }

    @Override
    public String toString() {
        return "PerformanceData [numberOfCookies=" + this.numberOfCookies + ", cookiesPerSecond="
                + this.cookiesPerSecond + "]";
    }

    public double getNumberOfCookies() {
        return this.numberOfCookies;
    }

    public void setNumberOfCookies(double numberOfCookies) {
        this.numberOfCookies = numberOfCookies;
    }

    public double getCookiesPerSecond() {
        return this.cookiesPerSecond;
    }

    public void setCookiesPerSecond(double cookiesPerSecond) {
        this.cookiesPerSecond = cookiesPerSecond;
    }
}
