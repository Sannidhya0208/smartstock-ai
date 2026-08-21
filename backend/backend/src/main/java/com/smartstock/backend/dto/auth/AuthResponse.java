package com.smartstock.backend.dto.auth;

public class AuthResponse {

    private String token;
    private String email;
    private String role;
    private Long companyId;
    private String companyName;

    public AuthResponse() {
    }

    public AuthResponse(
            String token,
            String email,
            String role,
            Long companyId,
            String companyName
    ) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.companyId = companyId;
        this.companyName = companyName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}