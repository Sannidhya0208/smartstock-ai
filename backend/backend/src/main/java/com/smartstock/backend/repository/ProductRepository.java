package com.smartstock.backend.repository;

import com.smartstock.backend.model.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    List<Product> findAllByCompanyId(
            Long companyId
    );

    Optional<Product> findByIdAndCompanyId(
            Long id,
            Long companyId
    );

    Page<Product> findAllByCompanyId(
            Long companyId,
            Pageable pageable
    );

    Page<Product> findByCompanyIdAndCategoryId(
            Long companyId,
            Long categoryId,
            Pageable pageable
    );

    Page<Product> findByCompanyIdAndSupplierId(
            Long companyId,
            Long supplierId,
            Pageable pageable
    );

    @Query("""
        SELECT p
        FROM Product p
        WHERE p.company.id = :companyId
          AND (
                LOWER(p.name)
                    LIKE LOWER(
                        CONCAT('%', :search, '%')
                    )
                OR
                LOWER(p.sku)
                    LIKE LOWER(
                        CONCAT('%', :search, '%')
                    )
          )
        """)
    Page<Product> searchByCompany(
            @Param("companyId")
            Long companyId,

            @Param("search")
            String search,

            Pageable pageable
    );
}