package com.smartstock.backend.dto;

public class LowStockResponse {

    private Long productId;
    private String productName;
    private Integer currentStock;
    private Integer minimumStock;
    private String status;

    public LowStockResponse() {
    }

    public LowStockResponse(
            Long productId,
            String productName,
            Integer currentStock,
            Integer minimumStock,
            String status) {
        this.productId = productId;
        this.productName = productName;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
        this.status = status;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(Integer minimumStock) {
        this.minimumStock = minimumStock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}