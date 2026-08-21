package com.smartstock.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import com.smartstock.backend.dto.InventoryRequest;
import com.smartstock.backend.dto.InventoryResponse;
import com.smartstock.backend.dto.StockRequest;
import com.smartstock.backend.dto.StockResponse;

import com.smartstock.backend.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(
            InventoryService inventoryService
    ) {
        this.inventoryService =
                inventoryService;
    }

    @PreAuthorize(
            "hasRole('OWNER')"
    )
    @PostMapping
    public ResponseEntity<InventoryResponse>
            createInventory(
                    @RequestBody
                    InventoryRequest request,

                    Authentication authentication
            ) {

        return new ResponseEntity<>(
                inventoryService.createInventory(
                        request,
                        authentication.getName()
                ),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER','STAFF')"
    )
    @GetMapping
    public ResponseEntity<List<InventoryResponse>>
            getAllInventory(
                    Authentication authentication
            ) {

        return ResponseEntity.ok(
                inventoryService.getAllInventory(
                        authentication.getName()
                )
        );
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER','STAFF')"
    )
    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse>
            getInventoryById(
                    @PathVariable
                    Long id,

                    Authentication authentication
            ) {

        return ResponseEntity.ok(
                inventoryService.getInventoryById(
                        id,
                        authentication.getName()
                )
        );
    }

    @PreAuthorize(
            "hasRole('OWNER')"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String>
            deleteInventory(
                    @PathVariable
                    Long id,

                    Authentication authentication
            ) {

        inventoryService.deleteInventory(
                id,
                authentication.getName()
        );

        return ResponseEntity.ok(
                "Inventory deleted successfully"
        );
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER','STAFF')"
    )
    @PostMapping("/{id}/stock-in")
    public ResponseEntity<StockResponse>
            stockIn(
                    @PathVariable
                    Long id,

                    @RequestBody
                    StockRequest request,

                    Authentication authentication
            ) {

        return ResponseEntity.ok(
                inventoryService.stockIn(
                        id,
                        request,
                        authentication.getName()
                )
        );
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER','STAFF')"
    )
    @PostMapping("/{id}/stock-out")
    public ResponseEntity<StockResponse>
            stockOut(
                    @PathVariable
                    Long id,

                    @RequestBody
                    StockRequest request,

                    Authentication authentication
            ) {

        return ResponseEntity.ok(
                inventoryService.stockOut(
                        id,
                        request,
                        authentication.getName()
                )
        );
    }
}