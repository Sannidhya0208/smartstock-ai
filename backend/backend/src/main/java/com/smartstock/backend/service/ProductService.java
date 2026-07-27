package com.smartstock.backend.service;


import com.smartstock.backend.dto.ProductRequest;
import com.smartstock.backend.dto.ProductResponse;
import com.smartstock.backend.model.Category;
import com.smartstock.backend.model.Product;
import com.smartstock.backend.model.Supplier;
import com.smartstock.backend.repository.CategoryRepository;
import com.smartstock.backend.repository.ProductRepository;
import com.smartstock.backend.repository.SupplierRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;



@Service
public class ProductService {


    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final SupplierRepository supplierRepository;



    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            SupplierRepository supplierRepository
    ){

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;

    }



    public ProductResponse createProduct(ProductRequest request){


        Product product = new Product();


        product.setName(request.getName());

        product.setSku(request.getSku());

        product.setPrice(request.getPrice());

        product.setQuantity(request.getQuantity());



        Category category =
                categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> 
                    new RuntimeException("Category not found")
                );


        Supplier supplier =
                supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() ->
                    new RuntimeException("Supplier not found")
                );


        product.setCategory(category);

        product.setSupplier(supplier);



        Product savedProduct =
                productRepository.save(product);



        return mapToResponse(savedProduct);

    }




    public List<ProductResponse> getAllProducts(){


        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }





    public ProductResponse getProductById(Long id){


        Product product =
                productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );


        return mapToResponse(product);

    }





    public ProductResponse updateProduct(
            Long id,
            ProductRequest request
    ){


        Product product =
                productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );


        product.setName(request.getName());

        product.setSku(request.getSku());

        product.setPrice(request.getPrice());

        product.setQuantity(request.getQuantity());



        if(request.getCategoryId()!=null){

            Category category =
                    categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() ->
                            new RuntimeException("Category not found")
                    );

            product.setCategory(category);

        }



        if(request.getSupplierId()!=null){

            Supplier supplier =
                    supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() ->
                            new RuntimeException("Supplier not found")
                    );

            product.setSupplier(supplier);

        }



        Product updatedProduct =
                productRepository.save(product);



        return mapToResponse(updatedProduct);

    }





    public void deleteProduct(Long id){

        productRepository.deleteById(id);

    }





    private ProductResponse mapToResponse(Product product){


        ProductResponse response =
                new ProductResponse();


        response.setId(product.getId());

        response.setName(product.getName());

        response.setSku(product.getSku());

        response.setPrice(product.getPrice());

        response.setQuantity(product.getQuantity());


        if(product.getCategory()!=null){
            response.setCategory(
                    product.getCategory().getName()
            );
        }


        if(product.getSupplier()!=null){
            response.setSupplier(
                    product.getSupplier().getName()
            );
        }


        return response;

    }

}