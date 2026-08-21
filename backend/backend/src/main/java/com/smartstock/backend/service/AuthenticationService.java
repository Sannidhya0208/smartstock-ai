package com.smartstock.backend.service;

import com.smartstock.backend.dto.auth.AuthResponse;
import com.smartstock.backend.dto.auth.LoginRequest;
import com.smartstock.backend.dto.auth.RegisterRequest;
import com.smartstock.backend.model.User;
import com.smartstock.backend.model.Role;
import com.smartstock.backend.repository.UserRepository;
import com.smartstock.backend.security.JwtService;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartstock.backend.model.Company;
import com.smartstock.backend.repository.CompanyRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;
        private final CompanyRepository companyRepository;

        public AuthenticationService(
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService,
                        AuthenticationManager authenticationManager,
                        CompanyRepository companyRepository) {

                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
                this.jwtService = jwtService;
                this.authenticationManager = authenticationManager;
                this.companyRepository = companyRepository;

        }

        @Transactional
        public AuthResponse register(RegisterRequest request) {

                if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                        throw new RuntimeException("Email already exists");
                }

                if (request.getCompanyName() == null
                                || request.getCompanyName().isBlank()) {

                        throw new IllegalArgumentException(
                                        "Company name is required");
                }

                Company company = new Company();

                company.setName(
                                request.getCompanyName().trim());

                company.setBusinessEmail(
                                request.getBusinessEmail() != null
                                                && !request.getBusinessEmail().isBlank()
                                                                ? request.getBusinessEmail().trim()
                                                                : request.getEmail().trim());

                company.setPhone(
                                request.getPhone());

                company.setSubscriptionPlan("FREE");
                company.setActive(true);

                Company savedCompany = companyRepository.save(company);

                User user = new User();

                user.setName(request.getName());
                user.setEmail(request.getEmail());

                user.setPassword(
                                passwordEncoder.encode(
                                                request.getPassword()));

                user.setRole(Role.OWNER);
                user.setActive(true);

                user.setCompany(savedCompany);

                userRepository.save(user);

                String token = jwtService.generateToken(
                                user.getEmail());

                return new AuthResponse(
                                token,
                                user.getEmail(),
                                user.getRole().name(),
                                user.getCompany().getId(),
                                user.getCompany().getName());
        }

        public AuthResponse login(LoginRequest request) {

                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));

                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow();

                user.setLastLogin(LocalDateTime.now());

                userRepository.save(user);

                String token = jwtService.generateToken(
                                user.getEmail());

                return new AuthResponse(
                                token,
                                user.getEmail(),
                                user.getRole().name(),
                                user.getCompany().getId(),
                                user.getCompany().getName());
        }

}