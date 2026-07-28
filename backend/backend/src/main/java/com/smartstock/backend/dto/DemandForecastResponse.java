package com.smartstock.backend.dto;

public class DemandForecastResponse {

    private Long productId;
    private String productName;
    private Integer currentStock;
    private double averageDailyDemand;
    private Integer forecastDays;
    private Integer predictedDemand;
    private Integer recommendedReorderQuantity;
    private String stockRisk;

    public DemandForecastResponse() {
    }

    public DemandForecastResponse(
            Long productId,
            String productName,
            Integer currentStock,
            double averageDailyDemand,
            Integer forecastDays,
            Integer predictedDemand,
            Integer recommendedReorderQuantity,
            String stockRisk) {

        this.productId = productId;
        this.productName = productName;
        this.currentStock = currentStock;
        this.averageDailyDemand = averageDailyDemand;
        this.forecastDays = forecastDays;
        this.predictedDemand = predictedDemand;
        this.recommendedReorderQuantity = recommendedReorderQuantity;
        this.stockRisk = stockRisk;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public double getAverageDailyDemand() {
        return averageDailyDemand;
    }

    public Integer getForecastDays() {
        return forecastDays;
    }

    public Integer getPredictedDemand() {
        return predictedDemand;
    }

    public Integer getRecommendedReorderQuantity() {
        return recommendedReorderQuantity;
    }

    public String getStockRisk() {
        return stockRisk;
    }
}