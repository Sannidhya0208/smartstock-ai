package com.smartstock.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartstock.backend.dto.SupplierRequest;
import com.smartstock.backend.dto.SupplierResponse;

import com.smartstock.backend.model.Company;
import com.smartstock.backend.model.Supplier;
import com.smartstock.backend.model.User;

import com.smartstock.backend.repository.SupplierRepository;
import com.smartstock.backend.repository.UserRepository;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    public SupplierService(
            SupplierRepository supplierRepository,
            UserRepository userRepository
    ) {
        this.supplierRepository =
                supplierRepository;

        this.userRepository =
                userRepository;
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

    @Transactional
    public SupplierResponse createSupplier(
            SupplierRequest request,
            String currentUserEmail
    ) {

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        Supplier supplier =
                new Supplier();

        supplier.setName(
                request.getName()
        );

        supplier.setEmail(
                request.getEmail()
        );

        supplier.setPhone(
                request.getPhone()
        );

        supplier.setCompany(
                company
        );

        Supplier saved =
                supplierRepository.save(
                        supplier
                );

        return mapToResponse(
                saved
        );
    }

    public List<SupplierResponse> getAllSuppliers(
            String currentUserEmail
    ) {

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        return supplierRepository
                .findAllByCompanyId(
                        company.getId()
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public SupplierResponse getSupplierById(
            Long id,
            String currentUserEmail
    ) {

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        Supplier supplier =
                supplierRepository
                        .findByIdAndCompanyId(
                                id,
                                company.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Supplier not found"
                                )
                        );

        return mapToResponse(
                supplier
        );
    }

    @Transactional
    public SupplierResponse updateSupplier(
            Long id,
            SupplierRequest request,
            String currentUserEmail
    ) {

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        Supplier supplier =
                supplierRepository
                        .findByIdAndCompanyId(
                                id,
                                company.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Supplier not found"
                                )
                        );

        supplier.setName(
                request.getName()
        );

        supplier.setEmail(
                request.getEmail()
        );

        supplier.setPhone(
                request.getPhone()
        );

        Supplier updated =
                supplierRepository.save(
                        supplier
                );

        return mapToResponse(
                updated
        );
    }

    @Transactional
    public void deleteSupplier(
            Long id,
            String currentUserEmail
    ) {

        Company company =
                getCurrentCompany(
                        currentUserEmail
                );

        Supplier supplier =
                supplierRepository
                        .findByIdAndCompanyId(
                                id,
                                company.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Supplier not found"
                                )
                        );

        supplierRepository.delete(
                supplier
        );
    }

    private SupplierResponse mapToResponse(
            Supplier supplier
    ) {

        SupplierResponse response =
                new SupplierResponse();

        response.setId(
                supplier.getId()
        );

        response.setName(
                supplier.getName()
        );

        response.setEmail(
                supplier.getEmail()
        );

        response.setPhone(
                supplier.getPhone()
        );

        return response;
    }
}