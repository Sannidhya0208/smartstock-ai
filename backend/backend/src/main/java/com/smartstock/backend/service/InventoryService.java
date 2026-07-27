package com.smartstock.backend.service;

import com.smartstock.backend.dto.InventoryRequest;
import com.smartstock.backend.dto.InventoryResponse;
import com.smartstock.backend.exception.ResourceNotFoundException;
import com.smartstock.backend.model.Inventory;
import com.smartstock.backend.model.Product;
import com.smartstock.backend.repository.InventoryRepository;
import com.smartstock.backend.repository.ProductRepository;

import com.smartstock.backend.exception.BadRequestException;

import com.smartstock.backend.dto.StockRequest;
import com.smartstock.backend.dto.StockResponse;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public InventoryService(
            InventoryRepository inventoryRepository,
            ProductRepository productRepository) {

        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    public InventoryResponse createInventory(InventoryRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        Inventory inventory = new Inventory();

        inventory.setProduct(product);
        inventory.setStockLevel(request.getStockLevel());
        inventory.setMinimumStock(request.getMinimumStock());

        Inventory saved = inventoryRepository.save(inventory);

        return mapToResponse(saved);
    }

    public List<InventoryResponse> getAllInventory() {

        return inventoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public InventoryResponse getInventoryById(Long id) {

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found"));

        return mapToResponse(inventory);
    }

    public void deleteInventory(Long id) {

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found"));

        inventoryRepository.delete(inventory);
    }

    private InventoryResponse mapToResponse(Inventory inventory) {

        InventoryResponse response = new InventoryResponse();

        response.setId(inventory.getId());
        response.setProductId(inventory.getProduct().getId());
        response.setProductName(inventory.getProduct().getName());
        response.setStockLevel(inventory.getStockLevel());
        response.setMinimumStock(inventory.getMinimumStock());

        return response;
    }

    public StockResponse stockIn(Long inventoryId, StockRequest request) {

        validateStockQuantity(request.getQuantity());

        Inventory inventory = inventoryRepository.findById(inventoryId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Inventory not found"));

        int updatedStock = inventory.getStockLevel() + request.getQuantity();

        inventory.setStockLevel(updatedStock);

        Inventory savedInventory = inventoryRepository.save(inventory);

        return mapToStockResponse(
            savedInventory,
            request.getQuantity() + " units added successfully");
    }
    public StockResponse stockOut(Long inventoryId, StockRequest request) {

        validateStockQuantity(request.getQuantity());

        Inventory inventory = inventoryRepository.findById(inventoryId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Inventory not found"));

        if (inventory.getStockLevel() < request.getQuantity()) {
            throw new BadRequestException(
                "Insufficient stock. Available stock: "
                    + inventory.getStockLevel());
    }

        int updatedStock = inventory.getStockLevel() - request.getQuantity();

        inventory.setStockLevel(updatedStock);

        Inventory savedInventory = inventoryRepository.save(inventory);

        return mapToStockResponse(
            savedInventory,
            request.getQuantity() + " units removed successfully");
    }
    private void validateStockQuantity(Integer quantity) {

    if (quantity == null || quantity <= 0) {
        throw new BadRequestException(
        "Quantity must be greater than zero");
        }
    }
    private StockResponse mapToStockResponse(
        Inventory inventory,
        String message) {

        StockResponse response = new StockResponse();

        response.setInventoryId(inventory.getId());
        response.setProductName(inventory.getProduct().getName());
        response.setStockLevel(inventory.getStockLevel());
        response.setMessage(message);

        return response;
    }
}