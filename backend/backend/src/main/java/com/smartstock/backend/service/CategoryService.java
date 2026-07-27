package com.smartstock.backend.service;


import com.smartstock.backend.dto.CategoryRequest;
import com.smartstock.backend.dto.CategoryResponse;
import com.smartstock.backend.model.Category;
import com.smartstock.backend.repository.CategoryRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;



@Service
public class CategoryService {


    private final CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository){

        this.categoryRepository = categoryRepository;

    }



    public CategoryResponse createCategory(CategoryRequest request){


        Category category = new Category();

        category.setName(request.getName());


        Category saved =
                categoryRepository.save(category);


        return mapToResponse(saved);

    }




    public List<CategoryResponse> getAllCategories(){


        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }




    private CategoryResponse mapToResponse(Category category){


        CategoryResponse response =
                new CategoryResponse();


        response.setId(category.getId());

        response.setName(category.getName());


        return response;

    }

}