package com.smartstock.backend.dto;

import com.smartstock.backend.model.Role;

public class RoleUpdateRequest {

    private Role role;

    public RoleUpdateRequest() {
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}