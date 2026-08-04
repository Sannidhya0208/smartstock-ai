package com.smartstock.backend.service;

import com.smartstock.backend.dto.SupplierRequest;
import com.smartstock.backend.dto.SupplierResponse;
import com.smartstock.backend.model.Supplier;
import com.smartstock.backend.repository.SupplierRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierService {

        private final SupplierRepository supplierRepository;

        public SupplierService(
                        SupplierRepository supplierRepository) {

                this.supplierRepository = supplierRepository;

        }

        public SupplierResponse createSupplier(
                        SupplierRequest request) {

                Supplier supplier = new Supplier();

                supplier.setName(request.getName());

                supplier.setEmail(request.getEmail());

                supplier.setPhone(request.getPhone());

                Supplier saved = supplierRepository.save(supplier);

                return mapToResponse(saved);

        }

        public List<SupplierResponse> getAllSuppliers() {

                return supplierRepository.findAll()
                                .stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());

        }

        private SupplierResponse mapToResponse(
                        Supplier supplier) {

                SupplierResponse response = new SupplierResponse();

                response.setId(supplier.getId());

                response.setName(supplier.getName());

                response.setEmail(supplier.getEmail());

                response.setPhone(supplier.getPhone());

                return response;

        }

        public SupplierResponse getSupplierById(Long id) {

                Supplier supplier = supplierRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Supplier not found"));

                return mapToResponse(supplier);
        }

        public SupplierResponse updateSupplier(
                        Long id,
                        SupplierRequest request) {

                Supplier supplier = supplierRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Supplier not found"));

                supplier.setName(request.getName());
                supplier.setEmail(request.getEmail());
                supplier.setPhone(request.getPhone());

                Supplier updated = supplierRepository.save(supplier);

                return mapToResponse(updated);
        }

        public void deleteSupplier(Long id) {

                Supplier supplier = supplierRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Supplier not found"));

                supplierRepository.delete(supplier);
        }

}