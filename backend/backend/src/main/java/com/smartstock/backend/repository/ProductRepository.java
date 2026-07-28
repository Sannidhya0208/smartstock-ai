package com.smartstock.backend.repository;

import com.smartstock.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(
            String name,
            String sku,
            Pageable pageable
    );

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findBySupplierId(Long supplierId, Pageable pageable);
}