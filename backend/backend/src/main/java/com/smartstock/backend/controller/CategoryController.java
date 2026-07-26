package com.smartstock.backend.controller;


import com.smartstock.backend.model.Category;
import com.smartstock.backend.repository.CategoryRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
public class CategoryController {


    private final CategoryRepository categoryRepository;


    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }



    // GET ALL CATEGORIES
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {

        return ResponseEntity.ok(
                categoryRepository.findAll()
        );
    }



    // GET CATEGORY BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(
            @PathVariable Long id
    ) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found")
                );


        return ResponseEntity.ok(category);
    }



    // CREATE CATEGORY
    @PostMapping
    public ResponseEntity<Category> createCategory(
            @Valid @RequestBody Category category
    ) {

        Category saved =
                categoryRepository.save(category);


        return new ResponseEntity<>(
                saved,
                HttpStatus.CREATED
        );
    }



    // UPDATE CATEGORY
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestBody Category updatedCategory
    ) {


        Category category =
                categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found")
                );


        category.setName(
                updatedCategory.getName()
        );


        return ResponseEntity.ok(
                categoryRepository.save(category)
        );
    }



    // DELETE CATEGORY
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long id
    ) {

        categoryRepository.deleteById(id);


        return ResponseEntity.ok(
                "Category deleted successfully"
        );
    }

}