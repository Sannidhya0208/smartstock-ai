package com.smartstock.backend.repository;

import com.smartstock.backend.model.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockTransactionRepository
        extends JpaRepository<StockTransaction, Long> {

    List<StockTransaction>
            findByProductIdOrderByCreatedAtDesc(Long productId);

    List<StockTransaction>
            findAllByOrderByCreatedAtDesc();
}