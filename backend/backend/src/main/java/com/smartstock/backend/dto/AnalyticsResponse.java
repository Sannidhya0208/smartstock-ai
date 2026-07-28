package com.smartstock.backend.dto;

import java.math.BigDecimal;

public class AnalyticsResponse {

    private long totalProducts;
    private BigDecimal totalInventoryValue;
    private long lowStockProducts;
    private long outOfStockProducts;
    private BigDecimal averageProductPrice;
    private double averageStockLevel;

    public AnalyticsResponse() {}

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public BigDecimal getTotalInventoryValue() {
        return totalInventoryValue;
    }

    public void setTotalInventoryValue(BigDecimal totalInventoryValue) {
        this.totalInventoryValue = totalInventoryValue;
    }

    public long getLowStockProducts() {
        return lowStockProducts;
    }

    public void setLowStockProducts(long lowStockProducts) {
        this.lowStockProducts = lowStockProducts;
    }

    public long getOutOfStockProducts() {
        return outOfStockProducts;
    }

    public void setOutOfStockProducts(long outOfStockProducts) {
        this.outOfStockProducts = outOfStockProducts;
    }

    public BigDecimal getAverageProductPrice() {
        return averageProductPrice;
    }

    public void setAverageProductPrice(BigDecimal averageProductPrice) {
        this.averageProductPrice = averageProductPrice;
    }

    public double getAverageStockLevel() {
        return averageStockLevel;
    }

    public void setAverageStockLevel(double averageStockLevel) {
        this.averageStockLevel = averageStockLevel;
    }
}