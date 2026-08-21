package com.smartstock.backend.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartstock.backend.dto.UserCreateRequest;
import com.smartstock.backend.dto.UserResponse;
import com.smartstock.backend.model.Role;
import com.smartstock.backend.model.User;
import com.smartstock.backend.repository.UserRepository;

@Service
public class UserManagementService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public UserManagementService(
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
        }

        public UserResponse createUser(
                        UserCreateRequest request,
                        String currentUserEmail) {

                if (userRepository.findByEmail(
                                request.getEmail()).isPresent()) {
                        throw new RuntimeException(
                                        "Email already exists");
                }

                if (request.getRole() == null) {
                        throw new IllegalArgumentException(
                                        "Role is required");
                }

                if (request.getRole() == Role.OWNER) {
                        throw new IllegalArgumentException(
                                        "Owners cannot create another OWNER account here");
                }

                User currentOwner = userRepository.findByEmail(currentUserEmail)
                                .orElseThrow(() -> new RuntimeException(
                                                "Current user not found"));

                if (currentOwner.getCompany() == null) {
                        throw new IllegalStateException(
                                        "Current owner is not assigned to a company");
                }

                User user = new User();

                user.setName(request.getName());
                user.setEmail(request.getEmail());

                user.setPassword(
                                passwordEncoder.encode(
                                                request.getPassword()));

                user.setRole(request.getRole());
                user.setActive(true);

                // Important: inherit company from logged-in owner
                user.setCompany(
                                currentOwner.getCompany());

                User savedUser = userRepository.save(user);

                return mapToResponse(savedUser);
        }

        public List<UserResponse> getAllUsers(
                        String currentUserEmail) {

                User currentUser = userRepository.findByEmail(currentUserEmail)
                                .orElseThrow(() -> new RuntimeException(
                                                "Current user not found"));

                if (currentUser.getCompany() == null) {
                        throw new IllegalStateException(
                                        "Current user is not assigned to a company");
                }

                return userRepository
                                .findAllByCompanyId(
                                                currentUser.getCompany().getId())
                                .stream()
                                .map(this::mapToResponse)
                                .toList();
        }

        private UserResponse mapToResponse(User user) {
                return new UserResponse(
                                user.getId(),
                                user.getName(),
                                user.getEmail(),
                                user.getRole() != null
                                                ? user.getRole().name()
                                                : "UNASSIGNED",
                                user.isActive(),
                                user.getLastLogin());
        }

        public UserResponse updateUserRole(
                        Long userId,
                        Role newRole,
                        String currentUserEmail) {

                User targetUser = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                if (targetUser.getEmail().equalsIgnoreCase(currentUserEmail)) {
                        throw new IllegalArgumentException(
                                        "You cannot change your own role");
                }

                if (newRole == null) {
                        throw new IllegalArgumentException(
                                        "Role is required");
                }

                if (newRole == Role.OWNER) {
                        throw new IllegalArgumentException(
                                        "OWNER role cannot be assigned");
                }

                if (targetUser.getRole() == Role.OWNER) {
                        throw new IllegalArgumentException(
                                        "Another OWNER account cannot be modified");
                }

                if (targetUser.getRole() == newRole) {
                        throw new IllegalArgumentException(
                                        "User already has this role");
                }

                targetUser.setRole(newRole);

                User savedUser = userRepository.save(targetUser);

                return mapToResponse(savedUser);
        }

        public UserResponse updateUserStatus(
                        Long userId,
                        boolean active,
                        String currentUserEmail) {

                User targetUser = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                if (targetUser.getEmail()
                                .equalsIgnoreCase(currentUserEmail)) {

                        throw new IllegalArgumentException(
                                        "You cannot disable your own account");
                }

                if (targetUser.getRole() == Role.OWNER) {
                        throw new IllegalArgumentException(
                                        "Another OWNER account cannot be disabled");
                }

                if (targetUser.isActive() == active) {
                        throw new IllegalArgumentException(
                                        active
                                                        ? "User is already active"
                                                        : "User is already disabled");
                }

                targetUser.setActive(active);

                User savedUser = userRepository.save(targetUser);

                return mapToResponse(savedUser);
        }

        public void deleteUser(
                        Long userId,
                        String currentUserEmail) {

                User targetUser = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                if (targetUser.getEmail()
                                .equalsIgnoreCase(currentUserEmail)) {

                        throw new IllegalArgumentException(
                                        "You cannot delete your own account");
                }

                if (targetUser.getRole() == Role.OWNER) {
                        throw new IllegalArgumentException(
                                        "OWNER account cannot be deleted");
                }

                userRepository.delete(targetUser);
        }

}