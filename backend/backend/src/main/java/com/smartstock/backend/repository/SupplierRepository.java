package com.smartstock.backend.repository;

import java.util.List;
import java.util.Optional;

import com.smartstock.backend.model.Supplier;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository
        extends JpaRepository<Supplier, Long> {

    List<Supplier> findAllByCompanyId(
            Long companyId
    );

    Optional<Supplier> findByIdAndCompanyId(
            Long id,
            Long companyId
    );
}