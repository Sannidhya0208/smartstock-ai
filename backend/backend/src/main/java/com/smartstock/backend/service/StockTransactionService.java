package com.smartstock.backend.service;

import com.smartstock.backend.dto.StockTransactionResponse;
import com.smartstock.backend.model.StockTransaction;
import com.smartstock.backend.repository.StockTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockTransactionService {

    private final StockTransactionRepository transactionRepository;

    public StockTransactionService(
            StockTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<StockTransactionResponse> getAllTransactions() {
        return transactionRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<StockTransactionResponse> getProductTransactions(
            Long productId) {

        return transactionRepository
                .findByProductIdOrderByCreatedAtDesc(productId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private StockTransactionResponse mapToResponse(
            StockTransaction transaction) {

        return new StockTransactionResponse(
                transaction.getId(),
                transaction.getProduct().getId(),
                transaction.getProduct().getName(),
                transaction.getTransactionType().name(),
                transaction.getQuantity(),
                transaction.getStockBefore(),
                transaction.getStockAfter(),
                transaction.getCreatedAt()
        );
    }
}