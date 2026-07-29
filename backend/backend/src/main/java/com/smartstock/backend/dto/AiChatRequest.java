package com.smartstock.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AiChatRequest {

    @NotBlank(message = "Question must not be empty")
    @Size(
            max = 500,
            message = "Question must not exceed 500 characters"
    )
    private String question;

    public AiChatRequest() {
    }

    public AiChatRequest(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}