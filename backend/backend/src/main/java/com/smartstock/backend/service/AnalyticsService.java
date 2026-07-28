package com.smartstock.backend.service;

import com.smartstock.backend.dto.AnalyticsResponse;
import com.smartstock.backend.model.Inventory;
import com.smartstock.backend.model.Product;
import com.smartstock.backend.repository.InventoryRepository;
import com.smartstock.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class AnalyticsService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public AnalyticsService(
            ProductRepository productRepository,
            InventoryRepository inventoryRepository) {

        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public AnalyticsResponse getAnalytics() {

        List<Product> products = productRepository.findAll();
        List<Inventory> inventoryItems = inventoryRepository.findAll();

        AnalyticsResponse response = new AnalyticsResponse();

        response.setTotalProducts(products.size());

        BigDecimal totalInventoryValue = inventoryItems.stream()
                .filter(item ->
                        item.getProduct() != null
                                && item.getProduct().getPrice() != null
                                && item.getStockLevel() != null)
                .map(item ->
                        BigDecimal.valueOf(item.getProduct().getPrice())
                                .multiply(BigDecimal.valueOf(item.getStockLevel())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long lowStockProducts = inventoryItems.stream()
                .filter(item ->
                        item.getStockLevel() != null
                                && item.getMinimumStock() != null
                                && item.getStockLevel() > 0
                                && item.getStockLevel() <= item.getMinimumStock())
                .count();

        long outOfStockProducts = inventoryItems.stream()
                .filter(item ->
                        item.getStockLevel() != null
                                && item.getStockLevel() == 0)
                .count();

        BigDecimal averageProductPrice = products.stream()
                .filter(product -> product.getPrice() != null)
                .map(product -> BigDecimal.valueOf(product.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long productsWithPrice = products.stream()
                .filter(product -> product.getPrice() != null)
                .count();

        if (productsWithPrice > 0) {
            averageProductPrice = averageProductPrice.divide(
                    BigDecimal.valueOf(productsWithPrice),
                    2,
                    RoundingMode.HALF_UP
            );
        }

        double averageStockLevel = inventoryItems.stream()
                .filter(item -> item.getStockLevel() != null)
                .mapToInt(Inventory::getStockLevel)
                .average()
                .orElse(0.0);

        response.setTotalInventoryValue(totalInventoryValue);
        response.setLowStockProducts(lowStockProducts);
        response.setOutOfStockProducts(outOfStockProducts);
        response.setAverageProductPrice(averageProductPrice);
        response.setAverageStockLevel(
                Math.round(averageStockLevel * 100.0) / 100.0
        );

        return response;
    }
}