package com.smartstock.backend.dto;

import java.util.List;

public class HostedChatRequest {

    private String model;
    private List<Message> messages;
    private Boolean stream;
    private Double temperature;

    public HostedChatRequest() {
    }

    public HostedChatRequest(
            String model,
            List<Message> messages,
            Boolean stream,
            Double temperature
    ) {
        this.model = model;
        this.messages = messages;
        this.stream = stream;
        this.temperature = temperature;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public static class Message {

        private String role;
        private String content;

        public Message() {
        }

        public Message(
                String role,
                String content
        ) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}