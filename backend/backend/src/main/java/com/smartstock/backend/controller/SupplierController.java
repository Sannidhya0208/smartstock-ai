package com.smartstock.backend.controller;

import com.smartstock.backend.dto.SupplierRequest;
import com.smartstock.backend.dto.SupplierResponse;
import com.smartstock.backend.service.SupplierService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(
            @RequestBody SupplierRequest request
    ) {
        return new ResponseEntity<>(
                supplierService.createSupplier(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getSuppliers() {
        return ResponseEntity.ok(
                supplierService.getAllSuppliers()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getSupplierById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                supplierService.getSupplierById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> updateSupplier(
            @PathVariable Long id,
            @RequestBody SupplierRequest request
    ) {
        return ResponseEntity.ok(
                supplierService.updateSupplier(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(
            @PathVariable Long id
    ) {
        supplierService.deleteSupplier(id);

        return ResponseEntity.noContent().build();
    }
}