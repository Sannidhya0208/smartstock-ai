package com.smartstock.backend.dto;

public class ReorderRecommendationResponse {

    private Long productId;
    private String productName;
    private Integer currentStock;
    private Integer minimumStock;
    private Integer recommendedOrderQuantity;
    private String priority;
    private String reason;

    public ReorderRecommendationResponse() {
    }

    public ReorderRecommendationResponse(
            Long productId,
            String productName,
            Integer currentStock,
            Integer minimumStock,
            Integer recommendedOrderQuantity,
            String priority,
            String reason) {

        this.productId = productId;
        this.productName = productName;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
        this.recommendedOrderQuantity = recommendedOrderQuantity;
        this.priority = priority;
        this.reason = reason;
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

    public Integer getRecommendedOrderQuantity() {
        return recommendedOrderQuantity;
    }

    public void setRecommendedOrderQuantity(Integer recommendedOrderQuantity) {
        this.recommendedOrderQuantity = recommendedOrderQuantity;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}