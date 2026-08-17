package com.smartstock.backend.dto;

import java.time.LocalDateTime;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String role;
    private boolean active;
    private LocalDateTime lastLogin;

    public UserResponse() {
    }

    public UserResponse(
            Long id,
            String name,
            String email,
            String role,
            boolean active,
            LocalDateTime lastLogin) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.active = active;
        this.lastLogin = lastLogin;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

}