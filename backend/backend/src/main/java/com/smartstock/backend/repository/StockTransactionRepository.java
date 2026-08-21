package com.smartstock.backend.repository;

import com.smartstock.backend.model.StockTransaction;
import com.smartstock.backend.model.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransactionRepository
        extends JpaRepository<StockTransaction, Long> {

    List<StockTransaction>
            findByProductIdOrderByCreatedAtDesc(
                    Long productId
            );

    List<StockTransaction>
            findAllByOrderByCreatedAtDesc();

    List<StockTransaction>
            findByProductIdAndTransactionTypeAndCreatedAtAfterOrderByCreatedAtAsc(
                    Long productId,
                    TransactionType transactionType,
                    LocalDateTime createdAt
            );

    List<StockTransaction>
            findAllByProductCompanyIdOrderByCreatedAtDesc(
                    Long companyId
            );

    List<StockTransaction>
            findByProductIdAndProductCompanyIdOrderByCreatedAtDesc(
                    Long productId,
                    Long companyId
            );

    List<StockTransaction>
            findByProductIdAndProductCompanyIdAndTransactionTypeAndCreatedAtAfterOrderByCreatedAtAsc(
                    Long productId,
                    Long companyId,
                    TransactionType transactionType,
                    LocalDateTime createdAt
            );
}