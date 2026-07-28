package com.smartstock.backend.service;

import com.smartstock.backend.dto.ReorderRecommendationResponse;
import com.smartstock.backend.model.Inventory;
import com.smartstock.backend.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RecommendationService {

    private final InventoryRepository inventoryRepository;

    public RecommendationService(
            InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<ReorderRecommendationResponse>
            getReorderRecommendations() {

        return inventoryRepository.findAll()
                .stream()
                .filter(this::requiresReorder)
                .map(this::createRecommendation)
                .sorted(Comparator.comparingInt(
                        this::getPriorityRank))
                .toList();
    }

    private boolean requiresReorder(Inventory inventory) {
        return inventory.getProduct() != null
                && inventory.getStockLevel() != null
                && inventory.getMinimumStock() != null
                && inventory.getStockLevel()
                    <= inventory.getMinimumStock();
    }

    private ReorderRecommendationResponse createRecommendation(
            Inventory inventory) {

        int currentStock = inventory.getStockLevel();
        int minimumStock = inventory.getMinimumStock();

        /*
         * Replenish stock to twice the minimum-stock level.
         * Example: minimum stock = 10 and current stock = 4
         * Recommended order = 20 - 4 = 16
         */
        int targetStock = minimumStock * 2;

        int recommendedQuantity = Math.max(
                targetStock - currentStock,
                1
        );

        String priority;
        String reason;

        if (currentStock == 0) {
            priority = "CRITICAL";
            reason = "Product is out of stock";
        } else if (currentStock
                <= Math.max(1, minimumStock / 2)) {
            priority = "HIGH";
            reason = "Stock is at or below 50% of minimum level";
        } else {
            priority = "MEDIUM";
            reason = "Stock has reached the minimum level";
        }

        return new ReorderRecommendationResponse(
                inventory.getProduct().getId(),
                inventory.getProduct().getName(),
                currentStock,
                minimumStock,
                recommendedQuantity,
                priority,
                reason
        );
    }

    private int getPriorityRank(
            ReorderRecommendationResponse recommendation) {

        return switch (recommendation.getPriority()) {
            case "CRITICAL" -> 1;
            case "HIGH" -> 2;
            case "MEDIUM" -> 3;
            default -> 4;
        };
    }
}