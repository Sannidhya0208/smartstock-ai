package com.smartstock.backend.service;

import com.smartstock.backend.model.Supplier;
import com.smartstock.backend.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier save(Supplier supplier) {
        return supplierRepository.save(supplier);
    }
}