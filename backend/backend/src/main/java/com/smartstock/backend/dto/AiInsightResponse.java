package com.smartstock.backend.dto;

import java.time.LocalDateTime;

public class AiInsightResponse {

    private LocalDateTime generatedAt;
    private String model;
    private String insight;

    public AiInsightResponse() {
    }

    public AiInsightResponse(
            LocalDateTime generatedAt,
            String model,
            String insight) {

        this.generatedAt = generatedAt;
        this.model = model;
        this.insight = insight;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getInsight() {
        return insight;
    }

    public void setInsight(String insight) {
        this.insight = insight;
    }
}