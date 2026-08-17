package com.smartstock.backend.controller;

import com.smartstock.backend.dto.SupplierRequest;
import com.smartstock.backend.dto.SupplierResponse;
import com.smartstock.backend.service.SupplierService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(
            SupplierService supplierService
    ) {
        this.supplierService = supplierService;
    }
    @PreAuthorize("hasAnyRole('OWNER','MANAGER')")
    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(
            @RequestBody SupplierRequest request
    ) {
        return new ResponseEntity<>(
                supplierService.createSupplier(request),
                HttpStatus.CREATED
        );
    }
    @PreAuthorize("hasAnyRole('OWNER','MANAGER','STAFF')")
    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getSuppliers() {
        return ResponseEntity.ok(
                supplierService.getAllSuppliers()
        );
    }
    @PreAuthorize("hasAnyRole('OWNER','MANAGER','STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getSupplierById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                supplierService.getSupplierById(id)
        );
    }
    @PreAuthorize("hasAnyRole('OWNER','MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> updateSupplier(
            @PathVariable Long id,
            @RequestBody SupplierRequest request
    ) {
        return ResponseEntity.ok(
                supplierService.updateSupplier(id, request)
        );
    }
    @PreAuthorize("hasAnyRole('OWNER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(
            @PathVariable Long id
    ) {
        supplierService.deleteSupplier(id);

        return ResponseEntity.noContent().build();
    }
}