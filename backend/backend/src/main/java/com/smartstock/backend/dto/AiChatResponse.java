package com.smartstock.backend.dto;

import java.time.LocalDateTime;

public class AiChatResponse {

    private String question;
    private String answer;
    private String model;
    private LocalDateTime generatedAt;

    public AiChatResponse() {
    }

    public AiChatResponse(
            String question,
            String answer,
            String model,
            LocalDateTime generatedAt) {

        this.question = question;
        this.answer = answer;
        this.model = model;
        this.generatedAt = generatedAt;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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