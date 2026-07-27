package com.smartstock.backend.controller;

import com.smartstock.backend.dto.InventoryRequest;
import com.smartstock.backend.dto.InventoryResponse;
import com.smartstock.backend.service.InventoryService;

import com.smartstock.backend.dto.StockRequest;
import com.smartstock.backend.dto.StockResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(
            @RequestBody InventoryRequest request) {

        return new ResponseEntity<>(
                inventoryService.createInventory(request),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        return ResponseEntity.ok(
                inventoryService.getAllInventory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getInventoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                inventoryService.getInventoryById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInventory(
            @PathVariable Long id) {

        inventoryService.deleteInventory(id);

        return ResponseEntity.ok("Inventory deleted successfully");
    }
    @PostMapping("/{id}/stock-in")
        public ResponseEntity<StockResponse> stockIn(
                @PathVariable Long id,
                @RequestBody StockRequest request) {

                return ResponseEntity.ok(
                inventoryService.stockIn(id, request));
        }

    @PostMapping("/{id}/stock-out")
        public ResponseEntity<StockResponse> stockOut(
                @PathVariable Long id,
                @RequestBody StockRequest request) {

                return ResponseEntity.ok(
                inventoryService.stockOut(id, request));
    }
}