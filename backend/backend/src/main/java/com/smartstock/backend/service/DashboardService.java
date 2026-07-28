package com.smartstock.backend.service;

import com.smartstock.backend.dto.DashboardResponse;
import com.smartstock.backend.model.Inventory;
import com.smartstock.backend.repository.CategoryRepository;
import com.smartstock.backend.repository.InventoryRepository;
import com.smartstock.backend.repository.ProductRepository;
import com.smartstock.backend.repository.SupplierRepository;

import com.smartstock.backend.dto.LowStockResponse;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryRepository inventoryRepository;

    public DashboardService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            SupplierRepository supplierRepository,
            InventoryRepository inventoryRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public DashboardResponse getDashboardSummary() {

        DashboardResponse response = new DashboardResponse();

        response.setTotalProducts(productRepository.count());
        response.setTotalCategories(categoryRepository.count());
        response.setTotalSuppliers(supplierRepository.count());
        response.setTotalInventoryItems(inventoryRepository.count());

        List<Inventory> inventoryItems = inventoryRepository.findAll();

        long lowStockCount = inventoryItems.stream()
                .filter(item ->
                        item.getStockLevel() != null
                                && item.getMinimumStock() != null
                                && item.getStockLevel() <= item.getMinimumStock())
                .count();

        BigDecimal totalInventoryValue = inventoryItems.stream()
                .filter(item ->
                        item.getProduct() != null
                                && item.getProduct().getPrice() != null
                                && item.getStockLevel() != null)
                .map(item ->
                        BigDecimal.valueOf(item.getProduct().getPrice())
                                .multiply(BigDecimal.valueOf(item.getStockLevel())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        response.setLowStockProducts(lowStockCount);
        response.setTotalInventoryValue(totalInventoryValue);

        return response;
    }
    public List<LowStockResponse> getLowStockProducts() {

    return inventoryRepository.findAll()
            .stream()
            .filter(item ->
                    item.getStockLevel() != null
                            && item.getMinimumStock() != null
                            && item.getStockLevel() <= item.getMinimumStock())
            .map(item -> new LowStockResponse(
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getStockLevel(),
                    item.getMinimumStock(),
                    item.getStockLevel() == 0
                            ? "OUT_OF_STOCK"
                            : "LOW_STOCK"
            ))
            .collect(Collectors.toList());
    }
}