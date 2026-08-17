package com.smartstock.backend.controller;

import com.smartstock.backend.dto.InventoryRequest;
import com.smartstock.backend.dto.InventoryResponse;
import com.smartstock.backend.service.InventoryService;

import com.smartstock.backend.dto.StockRequest;
import com.smartstock.backend.dto.StockResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }
    @PreAuthorize("hasAnyRole('OWNER')")
    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(
            @RequestBody InventoryRequest request) {

        return new ResponseEntity<>(
                inventoryService.createInventory(request),
                HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyRole('OWNER','MANAGER','STAFF')")
    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        return ResponseEntity.ok(
                inventoryService.getAllInventory());
    }
    @PreAuthorize("hasAnyRole('OWNER','MANAGER','STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getInventoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                inventoryService.getInventoryById(id));
    }
    @PreAuthorize("hasAnyRole('OWNER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInventory(
            @PathVariable Long id) {

        inventoryService.deleteInventory(id);

        return ResponseEntity.ok("Inventory deleted successfully");
    }
    @PreAuthorize("hasAnyRole('OWNER','MANAGER','STAFF')")
    @PostMapping("/{id}/stock-in")
        public ResponseEntity<StockResponse> stockIn(
                @PathVariable Long id,
                @RequestBody StockRequest request) {

                return ResponseEntity.ok(
                inventoryService.stockIn(id, request));
        }
    @PreAuthorize("hasAnyRole('OWNER','MANAGER','STAFF')")
    @PostMapping("/{id}/stock-out")
        public ResponseEntity<StockResponse> stockOut(
                @PathVariable Long id,
                @RequestBody StockRequest request) {

                return ResponseEntity.ok(
                inventoryService.stockOut(id, request));
    }
}