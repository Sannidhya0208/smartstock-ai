package com.smartstock.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.smartstock.backend.dto.RoleUpdateRequest;
import com.smartstock.backend.dto.UserCreateRequest;
import com.smartstock.backend.dto.UserResponse;
import com.smartstock.backend.dto.UserStatusUpdateRequest;
import com.smartstock.backend.service.UserManagementService;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/users")
public class UserManagementController {

        private final UserManagementService userManagementService;

        public UserManagementController(
                        UserManagementService userManagementService) {
                this.userManagementService = userManagementService;
        }

        @PreAuthorize("hasRole('OWNER')")
        @PostMapping
        public ResponseEntity<UserResponse> createUser(
                        @RequestBody UserCreateRequest request,
                        Authentication authentication) {

                return new ResponseEntity<>(
                                userManagementService.createUser(
                                                request,
                                                authentication.getName()),
                                HttpStatus.CREATED);
        }

        @PreAuthorize("hasRole('OWNER')")
        @GetMapping
        public ResponseEntity<List<UserResponse>> getAllUsers(
                        Authentication authentication) {

                return ResponseEntity.ok(
                                userManagementService.getAllUsers(
                                                authentication.getName()));
        }

        @PreAuthorize("hasRole('OWNER')")
        @PatchMapping("/{id}/role")
        public ResponseEntity<UserResponse> updateUserRole(
                        @PathVariable Long id,
                        @RequestBody RoleUpdateRequest request,
                        Authentication authentication) {

                UserResponse response = userManagementService.updateUserRole(
                                id,
                                request.getRole(),
                                authentication.getName());

                return ResponseEntity.ok(response);
        }

        @PreAuthorize("hasRole('OWNER')")
        @PatchMapping("/{id}/status")
        public ResponseEntity<UserResponse> updateUserStatus(
                        @PathVariable Long id,
                        @RequestBody UserStatusUpdateRequest request,
                        Authentication authentication) {

                UserResponse response = userManagementService.updateUserStatus(
                                id,
                                request.isActive(),
                                authentication.getName());

                return ResponseEntity.ok(response);
        }

        @PreAuthorize("hasRole('OWNER')")
        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteUser(
                        @PathVariable Long id,
                        Authentication authentication) {

                userManagementService.deleteUser(
                                id,
                                authentication.getName());

                return ResponseEntity.ok(
                                "User deleted successfully");
        }
}