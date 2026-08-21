package com.smartstock.backend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartstock.backend.dto.ProductPageResponse;
import com.smartstock.backend.dto.ProductRequest;
import com.smartstock.backend.dto.ProductResponse;

import com.smartstock.backend.model.Category;
import com.smartstock.backend.model.Company;
import com.smartstock.backend.model.Inventory;
import com.smartstock.backend.model.Product;
import com.smartstock.backend.model.Supplier;
import com.smartstock.backend.model.User;

import com.smartstock.backend.repository.CategoryRepository;
import com.smartstock.backend.repository.InventoryRepository;
import com.smartstock.backend.repository.ProductRepository;
import com.smartstock.backend.repository.SupplierRepository;
import com.smartstock.backend.repository.UserRepository;

@Service
public class ProductService {

        private final ProductRepository productRepository;

        private final CategoryRepository categoryRepository;

        private final SupplierRepository supplierRepository;

        private final InventoryRepository inventoryRepository;

        private final UserRepository userRepository;

        public ProductService(
                        ProductRepository productRepository,
                        CategoryRepository categoryRepository,
                        SupplierRepository supplierRepository,
                        InventoryRepository inventoryRepository,
                        UserRepository userRepository) {

                this.productRepository = productRepository;

                this.categoryRepository = categoryRepository;

                this.supplierRepository = supplierRepository;

                this.inventoryRepository = inventoryRepository;

                this.userRepository = userRepository;
        }

        private Company getCurrentCompany(
                        String currentUserEmail) {

                User user = userRepository
                                .findByEmail(
                                                currentUserEmail)
                                .orElseThrow(() -> new RuntimeException(
                                                "Current user not found"));

                if (user.getCompany() == null) {

                        throw new IllegalStateException(
                                        "Current user is not assigned to a company");
                }

                return user.getCompany();
        }

        @Transactional
        public ProductResponse createProduct(
                        ProductRequest request,
                        String currentUserEmail) {

                Company company = getCurrentCompany(
                                currentUserEmail);

                Category category = categoryRepository
                                .findByIdAndCompanyId(
                                                request.getCategoryId(),
                                                company.getId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Category not found"));

                Supplier supplier = supplierRepository
                                .findByIdAndCompanyId(
                                                request.getSupplierId(),
                                                company.getId())

                                .orElseThrow(() -> new RuntimeException(
                                                "Supplier not found"));

                Product product = new Product();

                product.setName(
                                request.getName());

                product.setSku(
                                request.getSku());

                product.setPrice(
                                request.getPrice());

                product.setQuantity(
                                request.getQuantity());

                product.setCategory(
                                category);

                product.setSupplier(
                                supplier);

                product.setCompany(
                                company);

                Product savedProduct = productRepository.save(
                                product);

                if (Boolean.TRUE.equals(
                                request.getAddToInventory())) {

                        int stockLevel = request.getQuantity() != null
                                        ? request.getQuantity()
                                        : 0;

                        int minimumStock = request.getMinimumStock() != null
                                        ? request.getMinimumStock()
                                        : 10;

                        if (stockLevel < 0) {
                                throw new IllegalArgumentException(
                                                "Quantity cannot be negative");
                        }

                        if (minimumStock < 0) {
                                throw new IllegalArgumentException(
                                                "Minimum stock cannot be negative");
                        }

                        Inventory inventory = new Inventory();

                        inventory.setProduct(
                                        savedProduct);

                        inventory.setStockLevel(
                                        stockLevel);

                        inventory.setMinimumStock(
                                        minimumStock);

                        inventoryRepository.save(
                                        inventory);
                }

                return mapToResponse(
                                savedProduct);
        }

        public List<ProductResponse> getAllProducts(
                        String currentUserEmail) {

                Company company = getCurrentCompany(
                                currentUserEmail);

                return productRepository
                                .findAllByCompanyId(
                                                company.getId())
                                .stream()
                                .map(
                                                this::mapToResponse)
                                .toList();
        }

        public ProductResponse getProductById(
                        Long id,
                        String currentUserEmail) {

                Company company = getCurrentCompany(
                                currentUserEmail);

                Product product = productRepository
                                .findByIdAndCompanyId(
                                                id,
                                                company.getId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Product not found"));

                return mapToResponse(
                                product);
        }

        @Transactional
        public ProductResponse updateProduct(
                        Long id,
                        ProductRequest request,
                        String currentUserEmail) {

                Company company = getCurrentCompany(
                                currentUserEmail);

                Product product = productRepository
                                .findByIdAndCompanyId(
                                                id,
                                                company.getId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Product not found"));

                product.setName(
                                request.getName());

                product.setSku(
                                request.getSku());

                product.setPrice(
                                request.getPrice());

                product.setQuantity(
                                request.getQuantity());

                if (request.getCategoryId() != null) {

                        Category category = categoryRepository
                                        .findByIdAndCompanyId(
                                                        request.getCategoryId(),
                                                        company.getId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Category not found"));

                        product.setCategory(category);
                }

                if (request.getSupplierId() != null) {

                        Supplier supplier = supplierRepository
                                        .findByIdAndCompanyId(
                                                        request.getSupplierId(),
                                                        company.getId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Supplier not found"));

                        product.setSupplier(supplier);
                }

                Product savedProduct = productRepository.save(
                                product);

                if (Boolean.TRUE.equals(
                                request.getAddToInventory())) {

                        int stockLevel = request.getQuantity() != null
                                        ? request.getQuantity()
                                        : 0;

                        int minimumStock = request.getMinimumStock() != null
                                        ? request.getMinimumStock()
                                        : 10;

                        if (stockLevel < 0
                                        || minimumStock < 0) {

                                throw new IllegalArgumentException(
                                                "Stock values cannot be negative");
                        }

                        Inventory inventory = inventoryRepository
                                        .findByProductId(
                                                        savedProduct
                                                                        .getId())
                                        .orElseGet(() -> {

                                                Inventory newInventory = new Inventory();

                                                newInventory.setProduct(
                                                                savedProduct);

                                                return newInventory;
                                        });

                        inventory.setStockLevel(
                                        stockLevel);

                        inventory.setMinimumStock(
                                        minimumStock);

                        inventoryRepository.save(
                                        inventory);
                }

                return mapToResponse(
                                savedProduct);
        }

        @Transactional
        public void deleteProduct(
                        Long id,
                        String currentUserEmail) {

                Company company = getCurrentCompany(
                                currentUserEmail);

                Product product = productRepository
                                .findByIdAndCompanyId(
                                                id,
                                                company.getId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Product not found"));

                productRepository.delete(
                                product);
        }

        public ProductPageResponse getProducts(
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection,
                        String search,
                        Long categoryId,
                        Long supplierId,
                        String currentUserEmail) {

                Company company = getCurrentCompany(
                                currentUserEmail);

                Sort sort = sortDirection
                                .equalsIgnoreCase(
                                                "desc")
                                                                ? Sort.by(
                                                                                sortBy).descending()
                                                                : Sort.by(
                                                                                sortBy).ascending();

                Pageable pageable = PageRequest.of(
                                page,
                                size,
                                sort);

                Page<Product> productPage;

                if (search != null
                                && !search.isBlank()) {

                        productPage = productRepository
                                        .searchByCompany(
                                                        company.getId(),
                                                        search,
                                                        pageable);

                } else if (categoryId != null) {

                        productPage = productRepository
                                        .findByCompanyIdAndCategoryId(
                                                        company.getId(),
                                                        categoryId,
                                                        pageable);

                } else if (supplierId != null) {

                        productPage = productRepository
                                        .findByCompanyIdAndSupplierId(
                                                        company.getId(),
                                                        supplierId,
                                                        pageable);

                } else {

                        productPage = productRepository
                                        .findAllByCompanyId(
                                                        company.getId(),
                                                        pageable);
                }

                List<ProductResponse> products = productPage
                                .getContent()
                                .stream()
                                .map(
                                                this::mapToResponse)
                                .toList();

                return new ProductPageResponse(
                                products,
                                productPage.getNumber(),
                                productPage.getSize(),
                                productPage.getTotalElements(),
                                productPage.getTotalPages(),
                                productPage.isLast());
        }

        private ProductResponse mapToResponse(
                        Product product) {

                ProductResponse response = new ProductResponse();

                response.setId(
                                product.getId());

                response.setName(
                                product.getName());

                response.setSku(
                                product.getSku());

                response.setPrice(
                                product.getPrice());

                response.setQuantity(
                                product.getQuantity());

                if (product.getCategory() != null) {

                        response.setCategoryId(
                                        product
                                                        .getCategory()
                                                        .getId());

                        response.setCategory(
                                        product
                                                        .getCategory()
                                                        .getName());
                }

                if (product.getSupplier() != null) {

                        response.setSupplierId(
                                        product
                                                        .getSupplier()
                                                        .getId());

                        response.setSupplier(
                                        product
                                                        .getSupplier()
                                                        .getName());
                }

                return response;
        }
}