package com.smartstock.backend.repository;

import java.util.List;
import java.util.Optional;

import com.smartstock.backend.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {

    List<Category> findAllByCompanyId(
            Long companyId
    );

    Optional<Category> findByIdAndCompanyId(
            Long id,
            Long companyId
    );
}