package com.smartstock.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartstock.backend.model.Company;

public interface CompanyRepository
        extends JpaRepository<Company, Long> {

    Optional<Company> findByBusinessEmail(
            String businessEmail
    );
}