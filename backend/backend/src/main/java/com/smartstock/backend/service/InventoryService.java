package com.smartstock.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartstock.backend.dto.InventoryRequest;
import com.smartstock.backend.dto.InventoryResponse;
import com.smartstock.backend.dto.StockRequest;
import com.smartstock.backend.dto.StockResponse;

import com.smartstock.backend.exception.BadRequestException;
import com.smartstock.backend.exception.ResourceNotFoundException;

import com.smartstock.backend.model.Company;
import com.smartstock.backend.model.Inventory;
import com.smartstock.backend.model.Product;
import com.smartstock.backend.model.StockTransaction;
import com.smartstock.backend.model.TransactionType;
import com.smartstock.backend.model.User;

import com.smartstock.backend.repository.InventoryRepository;
import com.smartstock.backend.repository.ProductRepository;
import com.smartstock.backend.repository.StockTransactionRepository;
import com.smartstock.backend.repository.UserRepository;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final StockTransactionRepository stockTransactionRepository;
    private final UserRepository userRepository;

    public InventoryService(
            InventoryRepository inventoryRepository,
            ProductRepository productRepository,
            StockTransactionRepository stockTransactionRepository,
            UserRepository userRepository
    ) {
        this.inventoryRepository =
                inventoryRepository;

        this.productRepository =
                productRepository;

        this.stockTransactionRepository =
                stockTransactionRepository;

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
    public InventoryResponse createInventory(
            InventoryRequest request,
            String currentUserEmail
    ) {

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        Product product = productRepository
                .findByIdAndCompanyId(
                        request.getProductId(),
                        company.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"
                        )
                );

        if (inventoryRepository
                .existsByProductId(
                        product.getId()
                )) {

            throw new BadRequestException(
                    "Inventory already exists for this product"
            );
        }

        if (request.getStockLevel() != null
                && request.getStockLevel() < 0) {

            throw new BadRequestException(
                    "Stock level cannot be negative"
            );
        }

        if (request.getMinimumStock() != null
                && request.getMinimumStock() < 0) {

            throw new BadRequestException(
                    "Minimum stock cannot be negative"
            );
        }

        Inventory inventory =
                new Inventory();

        inventory.setProduct(
                product
        );

        inventory.setStockLevel(
                request.getStockLevel()
        );

        inventory.setMinimumStock(
                request.getMinimumStock()
        );

        Inventory saved =
                inventoryRepository.save(
                        inventory
                );

        return mapToResponse(
                saved
        );
    }

    public List<InventoryResponse> getAllInventory(
            String currentUserEmail
    ) {

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        return inventoryRepository
                .findAllByProductCompanyId(
                        company.getId()
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public InventoryResponse getInventoryById(
            Long id,
            String currentUserEmail
    ) {

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        Inventory inventory =
                inventoryRepository
                        .findByIdAndProductCompanyId(
                                id,
                                company.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Inventory not found"
                                )
                        );

        return mapToResponse(
                inventory
        );
    }

    @Transactional
    public void deleteInventory(
            Long id,
            String currentUserEmail
    ) {

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        Inventory inventory =
                inventoryRepository
                        .findByIdAndProductCompanyId(
                                id,
                                company.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Inventory not found"
                                )
                        );

        inventoryRepository.delete(
                inventory
        );
    }

    @Transactional
    public StockResponse stockIn(
            Long inventoryId,
            StockRequest request,
            String currentUserEmail
    ) {

        validateStockQuantity(
                request.getQuantity()
        );

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        Inventory inventory =
                inventoryRepository
                        .findByIdAndProductCompanyId(
                                inventoryId,
                                company.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Inventory not found"
                                )
                        );

        int stockBefore =
                inventory.getStockLevel();

        int stockAfter =
                stockBefore
                        + request.getQuantity();

        inventory.setStockLevel(
                stockAfter
        );

        Inventory savedInventory =
                inventoryRepository.save(
                        inventory
                );

        saveTransaction(
                savedInventory,
                TransactionType.STOCK_IN,
                request.getQuantity(),
                stockBefore,
                stockAfter
        );

        return mapToStockResponse(
                savedInventory,
                request.getQuantity()
                        + " units added successfully"
        );
    }

    @Transactional
    public StockResponse stockOut(
            Long inventoryId,
            StockRequest request,
            String currentUserEmail
    ) {

        validateStockQuantity(
                request.getQuantity()
        );

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        Inventory inventory =
                inventoryRepository
                        .findByIdAndProductCompanyId(
                                inventoryId,
                                company.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Inventory not found"
                                )
                        );

        int stockBefore =
                inventory.getStockLevel();

        if (request.getQuantity()
                > stockBefore) {

            throw new BadRequestException(
                    "Insufficient stock. Available stock: "
                            + stockBefore
            );
        }

        int stockAfter =
                stockBefore
                        - request.getQuantity();

        inventory.setStockLevel(
                stockAfter
        );

        Inventory savedInventory =
                inventoryRepository.save(
                        inventory
                );

        saveTransaction(
                savedInventory,
                TransactionType.STOCK_OUT,
                request.getQuantity(),
                stockBefore,
                stockAfter
        );

        return mapToStockResponse(
                savedInventory,
                request.getQuantity()
                        + " units removed successfully"
        );
    }

    private void validateStockQuantity(
            Integer quantity
    ) {

        if (quantity == null
                || quantity <= 0) {

            throw new BadRequestException(
                    "Quantity must be greater than zero"
            );
        }
    }

    private InventoryResponse mapToResponse(
            Inventory inventory
    ) {

        InventoryResponse response =
                new InventoryResponse();

        response.setId(
                inventory.getId()
        );

        response.setProductId(
                inventory
                        .getProduct()
                        .getId()
        );

        response.setProductName(
                inventory
                        .getProduct()
                        .getName()
        );

        response.setStockLevel(
                inventory.getStockLevel()
        );

        response.setMinimumStock(
                inventory.getMinimumStock()
        );

        return response;
    }

    private StockResponse mapToStockResponse(
            Inventory inventory,
            String message
    ) {

        StockResponse response =
                new StockResponse();

        response.setInventoryId(
                inventory.getId()
        );

        response.setProductName(
                inventory
                        .getProduct()
                        .getName()
        );

        response.setStockLevel(
                inventory.getStockLevel()
        );

        response.setMessage(
                message
        );

        return response;
    }

    private void saveTransaction(
            Inventory inventory,
            TransactionType transactionType,
            int quantity,
            int stockBefore,
            int stockAfter
    ) {

        StockTransaction transaction =
                new StockTransaction();

        transaction.setProduct(
                inventory.getProduct()
        );

        transaction.setTransactionType(
                transactionType
        );

        transaction.setQuantity(
                quantity
        );

        transaction.setStockBefore(
                stockBefore
        );

        transaction.setStockAfter(
                stockAfter
        );

        stockTransactionRepository.save(
                transaction
        );
    }
}