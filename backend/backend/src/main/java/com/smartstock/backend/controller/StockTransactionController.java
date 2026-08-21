package com.smartstock.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import com.smartstock.backend.dto.StockTransactionResponse;

import com.smartstock.backend.service.StockTransactionService;

@RestController
@RequestMapping("/api/transactions")
public class StockTransactionController {

    private final StockTransactionService transactionService;

    public StockTransactionController(
            StockTransactionService transactionService
    ) {
        this.transactionService =
                transactionService;
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER','STAFF')"
    )
    @GetMapping
    public ResponseEntity<List<StockTransactionResponse>>
            getAllTransactions(
                    Authentication authentication
            ) {

        return ResponseEntity.ok(
                transactionService
                        .getAllTransactions(
                                authentication.getName()
                        )
        );
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER','STAFF')"
    )
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockTransactionResponse>>
            getProductTransactions(
                    @PathVariable
                    Long productId,

                    Authentication authentication
            ) {

        return ResponseEntity.ok(
                transactionService
                        .getProductTransactions(
                                productId,
                                authentication.getName()
                        )
        );
    }
}