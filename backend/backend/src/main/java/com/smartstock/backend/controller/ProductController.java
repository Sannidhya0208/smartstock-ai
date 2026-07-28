package com.smartstock.backend.controller;


import com.smartstock.backend.dto.ProductRequest;
import com.smartstock.backend.dto.ProductResponse;
import com.smartstock.backend.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.smartstock.backend.dto.ProductPageResponse;

import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/products")
public class ProductController {



    private final ProductService productService;



    public ProductController(ProductService productService){

        this.productService = productService;

    }




    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest request
    ){

        return new ResponseEntity<>(
                productService.createProduct(request),
                HttpStatus.CREATED
        );

    }





    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(){

        return ResponseEntity.ok(
                productService.getAllProducts()
        );

    }





    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long id
    ){

        return ResponseEntity.ok(
                productService.getProductById(id)
        );

    }





    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request
    ){

        return ResponseEntity.ok(
                productService.updateProduct(id, request)
        );

    }





    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long id
    ){

        productService.deleteProduct(id);

        return ResponseEntity.ok(
                "Product deleted successfully"
        );

    }


    @GetMapping("/page")
        public ResponseEntity<ProductPageResponse> getProducts(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size,
                @RequestParam(defaultValue = "id") String sortBy,
                @RequestParam(defaultValue = "asc") String sortDirection,
                @RequestParam(required = false) String search,
                @RequestParam(required = false) Long categoryId,
                @RequestParam(required = false) Long supplierId) {

        return ResponseEntity.ok(
                    productService.getProducts(
                        page,
                        size,
                        sortBy,
                        sortDirection,
                        search,
                        categoryId,
                        supplierId
                ));
        }


}