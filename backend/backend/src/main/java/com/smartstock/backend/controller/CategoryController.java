package com.smartstock.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import com.smartstock.backend.dto.CategoryRequest;
import com.smartstock.backend.dto.CategoryResponse;

import com.smartstock.backend.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(
            CategoryService categoryService
    ) {
        this.categoryService =
                categoryService;
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER')"
    )
    @PostMapping
    public ResponseEntity<CategoryResponse>
            createCategory(
                    @RequestBody
                    CategoryRequest request,

                    Authentication authentication
            ) {

        return new ResponseEntity<>(
                categoryService
                        .createCategory(
                                request,
                                authentication
                                        .getName()
                        ),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER','STAFF')"
    )
    @GetMapping
    public ResponseEntity<List<CategoryResponse>>
            getCategories(
                    Authentication authentication
            ) {

        return ResponseEntity.ok(
                categoryService
                        .getAllCategories(
                                authentication
                                        .getName()
                        )
        );
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER','STAFF')"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse>
            getCategoryById(
                    @PathVariable Long id,
                    Authentication authentication
            ) {

        return ResponseEntity.ok(
                categoryService
                        .getCategoryById(
                                id,
                                authentication
                                        .getName()
                        )
        );
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER')"
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse>
            updateCategory(
                    @PathVariable Long id,
                    @RequestBody
                    CategoryRequest request,
                    Authentication authentication
            ) {

        return ResponseEntity.ok(
                categoryService
                        .updateCategory(
                                id,
                                request,
                                authentication
                                        .getName()
                        )
        );
    }

    @PreAuthorize(
            "hasRole('OWNER')"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
            deleteCategory(
                    @PathVariable Long id,
                    Authentication authentication
            ) {

        categoryService.deleteCategory(
                id,
                authentication.getName()
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}