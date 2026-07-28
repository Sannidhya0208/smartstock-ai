package com.smartstock.backend.repository;

import com.smartstock.backend.model.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smartstock.backend.model.TransactionType;
import java.time.LocalDateTime;

import java.util.List;

public interface StockTransactionRepository
        extends JpaRepository<StockTransaction, Long> {

    List<StockTransaction>
            findByProductIdOrderByCreatedAtDesc(Long productId);

    List<StockTransaction>
            findAllByOrderByCreatedAtDesc();

    List<StockTransaction>
        findByProductIdAndTransactionTypeAndCreatedAtAfterOrderByCreatedAtAsc(
            Long productId,
            TransactionType transactionType,
            LocalDateTime createdAt
        );
}