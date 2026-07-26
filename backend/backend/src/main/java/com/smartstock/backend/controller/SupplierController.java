package com.smartstock.backend.controller;


import com.smartstock.backend.model.Supplier;
import com.smartstock.backend.repository.SupplierRepository;
import com.smartstock.backend.exception.ResourceNotFoundException;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin("*")
public class SupplierController {


    private final SupplierRepository supplierRepository;


    public SupplierController(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }



    // GET ALL SUPPLIERS
    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSuppliers() {

        return ResponseEntity.ok(
                supplierRepository.findAll()
        );
    }



    // GET SUPPLIER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById(
            @PathVariable Long id
    ) {

        Supplier supplier =
                supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found with id: " + id
                        )
                );


        return ResponseEntity.ok(supplier);
    }



    // CREATE SUPPLIER
    @PostMapping
    public ResponseEntity<Supplier> createSupplier(
            @Valid @RequestBody Supplier supplier
    ) {


        Supplier savedSupplier =
                supplierRepository.save(supplier);


        return new ResponseEntity<>(
                savedSupplier,
                HttpStatus.CREATED
        );
    }



    // UPDATE SUPPLIER
    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(
            @PathVariable Long id,
            @RequestBody Supplier supplierDetails
    ) {


        Supplier supplier =
                supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found with id: " + id
                        )
                );


        supplier.setName(
                supplierDetails.getName()
        );


        supplier.setEmail(
                supplierDetails.getEmail()
        );


        supplier.setPhone(
                supplierDetails.getPhone()
        );


        return ResponseEntity.ok(
                supplierRepository.save(supplier)
        );
    }



    // DELETE SUPPLIER
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(
            @PathVariable Long id
    ) {


        Supplier supplier =
                supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found with id: " + id
                        )
                );


        supplierRepository.delete(supplier);


        return ResponseEntity.ok(
                "Supplier deleted successfully"
        );
    }

}