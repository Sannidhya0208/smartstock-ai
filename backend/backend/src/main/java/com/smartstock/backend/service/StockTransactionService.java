package com.smartstock.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smartstock.backend.dto.StockTransactionResponse;

import com.smartstock.backend.model.Company;
import com.smartstock.backend.model.StockTransaction;
import com.smartstock.backend.model.User;

import com.smartstock.backend.repository.ProductRepository;
import com.smartstock.backend.repository.StockTransactionRepository;
import com.smartstock.backend.repository.UserRepository;

@Service
public class StockTransactionService {

    private final StockTransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public StockTransactionService(
            StockTransactionRepository transactionRepository,
            UserRepository userRepository,
            ProductRepository productRepository
    ) {
        this.transactionRepository =
                transactionRepository;

        this.userRepository =
                userRepository;

        this.productRepository =
                productRepository;
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

    public List<StockTransactionResponse> getAllTransactions(
            String currentUserEmail
    ) {

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        return transactionRepository
                .findAllByProductCompanyIdOrderByCreatedAtDesc(
                        company.getId()
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<StockTransactionResponse> getProductTransactions(
            Long productId,
            String currentUserEmail
    ) {

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        productRepository
                .findByIdAndCompanyId(
                        productId,
                        company.getId()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found"
                        )
                );

        return transactionRepository
                .findByProductIdAndProductCompanyIdOrderByCreatedAtDesc(
                        productId,
                        company.getId()
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private StockTransactionResponse mapToResponse(
            StockTransaction transaction
    ) {

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