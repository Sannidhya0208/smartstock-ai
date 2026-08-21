package com.smartstock.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartstock.backend.model.Inventory;

public interface InventoryRepository
        extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(
            Long productId
    );

    boolean existsByProductId(
            Long productId
    );

    List<Inventory> findAllByProductCompanyId(
            Long companyId
    );

    Optional<Inventory> findByIdAndProductCompanyId(
            Long id,
            Long companyId
    );

    Optional<Inventory> findByProductIdAndProductCompanyId(
            Long productId,
            Long companyId
    );
}