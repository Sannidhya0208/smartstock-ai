package com.smartstock.backend.controller;

import com.smartstock.backend.dto.CategoryRequest;
import com.smartstock.backend.dto.CategoryResponse;
import com.smartstock.backend.service.CategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(
            CategoryService categoryService
    ) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestBody CategoryRequest request
    ) {
        return new ResponseEntity<>(
                categoryService.createCategory(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok(
                categoryService.getAllCategories()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                categoryService.getCategoryById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequest request
    ) {
        return ResponseEntity.ok(
                categoryService.updateCategory(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id
    ) {
        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }
}