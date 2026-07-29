package com.smartstock.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AiDashboardResponse {

    private long totalProducts;
    private long totalInventoryItems;
    private long lowStockItems;
    private long outOfStockItems;
    private BigDecimal totalInventoryValue;
    private String aiSummary;
    private String model;
    private LocalDateTime generatedAt;

    public AiDashboardResponse() {
    }

    public AiDashboardResponse(
            long totalProducts,
            long totalInventoryItems,
            long lowStockItems,
            long outOfStockItems,
            BigDecimal totalInventoryValue,
            String aiSummary,
            String model,
            LocalDateTime generatedAt) {

        this.totalProducts = totalProducts;
        this.totalInventoryItems = totalInventoryItems;
        this.lowStockItems = lowStockItems;
        this.outOfStockItems = outOfStockItems;
        this.totalInventoryValue = totalInventoryValue;
        this.aiSummary = aiSummary;
        this.model = model;
        this.generatedAt = generatedAt;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalInventoryItems() {
        return totalInventoryItems;
    }

    public void setTotalInventoryItems(long totalInventoryItems) {
        this.totalInventoryItems = totalInventoryItems;
    }

    public long getLowStockItems() {
        return lowStockItems;
    }

    public void setLowStockItems(long lowStockItems) {
        this.lowStockItems = lowStockItems;
    }

    public long getOutOfStockItems() {
        return outOfStockItems;
    }

    public void setOutOfStockItems(long outOfStockItems) {
        this.outOfStockItems = outOfStockItems;
    }

    public BigDecimal getTotalInventoryValue() {
        return totalInventoryValue;
    }

    public void setTotalInventoryValue(
            BigDecimal totalInventoryValue) {
        this.totalInventoryValue = totalInventoryValue;
    }

    public String getAiSummary() {
        return aiSummary;
    }

    public void setAiSummary(String aiSummary) {
        this.aiSummary = aiSummary;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}