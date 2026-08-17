package com.smartstock.backend.controller;

import com.smartstock.backend.dto.StockTransactionResponse;
import com.smartstock.backend.service.StockTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class StockTransactionController {

    private final StockTransactionService transactionService;

    public StockTransactionController(
            StockTransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @PreAuthorize("hasAnyRole('OWNER','MANAGER','STAFF')")
    @GetMapping
    public ResponseEntity<List<StockTransactionResponse>>
            getAllTransactions() {

        return ResponseEntity.ok(
                transactionService.getAllTransactions()
        );
    }
    @PreAuthorize("hasAnyRole('OWNER','MANAGER','STAFF')")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockTransactionResponse>>
            getProductTransactions(
                    @PathVariable Long productId) {

        return ResponseEntity.ok(
                transactionService
                        .getProductTransactions(productId)
        );
    }
}