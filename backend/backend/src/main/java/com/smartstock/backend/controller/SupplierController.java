package com.smartstock.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import com.smartstock.backend.dto.SupplierRequest;
import com.smartstock.backend.dto.SupplierResponse;

import com.smartstock.backend.service.SupplierService;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(
            SupplierService supplierService
    ) {
        this.supplierService =
                supplierService;
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER')"
    )
    @PostMapping
    public ResponseEntity<SupplierResponse>
            createSupplier(
                    @RequestBody
                    SupplierRequest request,

                    Authentication authentication
            ) {

        return new ResponseEntity<>(
                supplierService
                        .createSupplier(
                                request,
                                authentication
                                        .getName()
                        ),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER','STAFF')"
    )
    @GetMapping
    public ResponseEntity<List<SupplierResponse>>
            getSuppliers(
                    Authentication authentication
            ) {

        return ResponseEntity.ok(
                supplierService
                        .getAllSuppliers(
                                authentication
                                        .getName()
                        )
        );
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER','STAFF')"
    )
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse>
            getSupplierById(
                    @PathVariable Long id,
                    Authentication authentication
            ) {

        return ResponseEntity.ok(
                supplierService
                        .getSupplierById(
                                id,
                                authentication
                                        .getName()
                        )
        );
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER')"
    )
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse>
            updateSupplier(
                    @PathVariable Long id,
                    @RequestBody
                    SupplierRequest request,
                    Authentication authentication
            ) {

        return ResponseEntity.ok(
                supplierService
                        .updateSupplier(
                                id,
                                request,
                                authentication
                                        .getName()
                        )
        );
    }

    @PreAuthorize(
            "hasRole('OWNER')"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
            deleteSupplier(
                    @PathVariable Long id,
                    Authentication authentication
            ) {

        supplierService.deleteSupplier(
                id,
                authentication.getName()
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}