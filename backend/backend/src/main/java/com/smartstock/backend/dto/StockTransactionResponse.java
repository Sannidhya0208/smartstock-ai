package com.smartstock.backend.dto;

import java.time.LocalDateTime;

public class StockTransactionResponse {

    private Long id;
    private Long productId;
    private String productName;
    private String transactionType;
    private Integer quantity;
    private Integer stockBefore;
    private Integer stockAfter;
    private LocalDateTime createdAt;

    public StockTransactionResponse() {
    }

    public StockTransactionResponse(
            Long id,
            Long productId,
            String productName,
            String transactionType,
            Integer quantity,
            Integer stockBefore,
            Integer stockAfter,
            LocalDateTime createdAt) {

        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.stockBefore = stockBefore;
        this.stockAfter = stockAfter;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getStockBefore() {
        return stockBefore;
    }

    public Integer getStockAfter() {
        return stockAfter;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}