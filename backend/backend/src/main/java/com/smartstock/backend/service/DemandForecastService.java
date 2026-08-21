package com.smartstock.backend.service;

import com.smartstock.backend.dto.DemandForecastResponse;
import com.smartstock.backend.exception.ResourceNotFoundException;

import com.smartstock.backend.model.Company;
import com.smartstock.backend.model.Inventory;
import com.smartstock.backend.model.StockTransaction;
import com.smartstock.backend.model.TransactionType;
import com.smartstock.backend.model.User;

import com.smartstock.backend.repository.InventoryRepository;
import com.smartstock.backend.repository.StockTransactionRepository;
import com.smartstock.backend.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DemandForecastService {

    private static final int HISTORY_DAYS = 30;
    private static final int DEFAULT_FORECAST_DAYS = 7;

    private final InventoryRepository inventoryRepository;
    private final StockTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public DemandForecastService(
            InventoryRepository inventoryRepository,
            StockTransactionRepository transactionRepository,
            UserRepository userRepository
    ) {
        this.inventoryRepository = inventoryRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
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

    public DemandForecastResponse forecastDemand(
            Long inventoryId,
            Integer forecastDays,
            String currentUserEmail
    ) {

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
                                        "Inventory not found with ID: "
                                                + inventoryId
                                )
                        );

        int selectedForecastDays =
                forecastDays == null
                        || forecastDays <= 0
                        ? DEFAULT_FORECAST_DAYS
                        : forecastDays;

        LocalDateTime historyStart =
                LocalDateTime.now()
                        .minusDays(
                                HISTORY_DAYS
                        );

        Long productId =
                inventory
                        .getProduct()
                        .getId();

        List<StockTransaction> stockOutTransactions =
                transactionRepository
                        .findByProductIdAndProductCompanyIdAndTransactionTypeAndCreatedAtAfterOrderByCreatedAtAsc(
                                productId,
                                company.getId(),
                                TransactionType.STOCK_OUT,
                                historyStart
                        );

        int totalStockOut =
                stockOutTransactions
                        .stream()
                        .mapToInt(
                                StockTransaction::getQuantity
                        )
                        .sum();

        double averageDailyDemand =
                (double) totalStockOut
                        / HISTORY_DAYS;

        int predictedDemand =
                (int) Math.ceil(
                        averageDailyDemand
                                * selectedForecastDays
                );

        int currentStock =
                inventory.getStockLevel();

        int recommendedReorderQuantity =
                Math.max(
                        predictedDemand
                                - currentStock,
                        0
                );

        String stockRisk =
                determineStockRisk(
                        currentStock,
                        predictedDemand
                );

        return new DemandForecastResponse(
                productId,
                inventory
                        .getProduct()
                        .getName(),
                currentStock,
                roundToTwoDecimals(
                        averageDailyDemand
                ),
                selectedForecastDays,
                predictedDemand,
                recommendedReorderQuantity,
                stockRisk
        );
    }

    private String determineStockRisk(
            int currentStock,
            int predictedDemand
    ) {

        if (predictedDemand == 0) {
            return "NO_DEMAND_DATA";
        }

        if (currentStock == 0) {
            return "CRITICAL";
        }

        if (currentStock < predictedDemand) {
            return "HIGH";
        }

        if (currentStock == predictedDemand) {
            return "MEDIUM";
        }

        return "LOW";
    }

    private double roundToTwoDecimals(
            double value
    ) {
        return Math.round(
                value * 100.0
        ) / 100.0;
    }
}