package com.smartstock.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartstock.backend.dto.CategoryRequest;
import com.smartstock.backend.dto.CategoryResponse;

import com.smartstock.backend.model.Category;
import com.smartstock.backend.model.Company;
import com.smartstock.backend.model.User;

import com.smartstock.backend.repository.CategoryRepository;
import com.smartstock.backend.repository.UserRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(
            CategoryRepository categoryRepository,
            UserRepository userRepository
    ) {
        this.categoryRepository =
                categoryRepository;

        this.userRepository =
                userRepository;
    }

    private Company getCurrentCompany(
            String currentUserEmail
    ) {

        User user = userRepository
                .findByEmail(currentUserEmail)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Current user not found"
                        )
                );

        if (user.getCompany() == null) {
            throw new IllegalStateException(
                    "Current user is not assigned to a company"
            );
        }

        return user.getCompany();
    }

    @Transactional
    public CategoryResponse createCategory(
            CategoryRequest request,
            String currentUserEmail
    ) {

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        Category category =
                new Category();

        category.setName(
                request.getName()
        );

        category.setCompany(
                company
        );

        Category saved =
                categoryRepository.save(
                        category
                );

        return mapToResponse(
                saved
        );
    }

    public List<CategoryResponse> getAllCategories(
            String currentUserEmail
    ) {

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        return categoryRepository
                .findAllByCompanyId(
                        company.getId()
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(
            Long id,
            String currentUserEmail
    ) {

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        Category category =
                categoryRepository
                        .findByIdAndCompanyId(
                                id,
                                company.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"
                                )
                        );

        return mapToResponse(
                category
        );
    }

    @Transactional
    public CategoryResponse updateCategory(
            Long id,
            CategoryRequest request,
            String currentUserEmail
    ) {

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        Category category =
                categoryRepository
                        .findByIdAndCompanyId(
                                id,
                                company.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"
                                )
                        );

        category.setName(
                request.getName()
        );

        Category updated =
                categoryRepository.save(
                        category
                );

        return mapToResponse(
                updated
        );
    }

    @Transactional
    public void deleteCategory(
            Long id,
            String currentUserEmail
    ) {

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        Category category =
                categoryRepository
                        .findByIdAndCompanyId(
                                id,
                                company.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"
                                )
                        );

        categoryRepository.delete(
                category
        );
    }

    private CategoryResponse mapToResponse(
            Category category
    ) {

        CategoryResponse response =
                new CategoryResponse();

        response.setId(
                category.getId()
        );

        response.setName(
                category.getName()
        );

        return response;
    }
}