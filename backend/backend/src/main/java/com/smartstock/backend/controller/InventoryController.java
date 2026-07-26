package com.smartstock.backend.controller;


import com.smartstock.backend.model.Inventory;
import com.smartstock.backend.model.Product;

import com.smartstock.backend.repository.InventoryRepository;
import com.smartstock.backend.repository.ProductRepository;

import com.smartstock.backend.exception.ResourceNotFoundException;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/inventory")
@CrossOrigin("*")
public class InventoryController {



    private final InventoryRepository inventoryRepository;

    private final ProductRepository productRepository;



    public InventoryController(
            InventoryRepository inventoryRepository,
            ProductRepository productRepository
    ) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }




    // GET ALL INVENTORY

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {

        return ResponseEntity.ok(
                inventoryRepository.findAll()
        );
    }




    // GET INVENTORY BY ID

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(
            @PathVariable Long id
    ) {


        Inventory inventory =
                inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory not found with id: " + id
                        )
                );


        return ResponseEntity.ok(inventory);

    }





    // CREATE INVENTORY

    @PostMapping
    public ResponseEntity<Inventory> createInventory(
            @RequestBody Inventory inventory
    ) {


        if(inventory.getProduct() != null) {


            Long productId =
                    inventory.getProduct().getId();


            Product product =
                    productRepository.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found with id: "
                                    + productId
                            )
                    );


            inventory.setProduct(product);

        }



        Inventory savedInventory =
                inventoryRepository.save(inventory);



        return new ResponseEntity<>(
                savedInventory,
                HttpStatus.CREATED
        );

    }





    // UPDATE INVENTORY

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(
            @PathVariable Long id,
            @RequestBody Inventory inventoryDetails
    ) {



        Inventory inventory =
                inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory not found with id: "
                                + id
                        )
                );



        inventory.setStockLevel(
                inventoryDetails.getStockLevel()
        );


        inventory.setMinimumStock(
                inventoryDetails.getMinimumStock()
        );



        return ResponseEntity.ok(
                inventoryRepository.save(inventory)
        );

    }





    // DELETE INVENTORY

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInventory(
            @PathVariable Long id
    ) {


        Inventory inventory =
                inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory not found with id: "
                                + id
                        )
                );


        inventoryRepository.delete(inventory);



        return ResponseEntity.ok(
                "Inventory deleted successfully"
        );

    }





    // LOW STOCK ALERTS

    @GetMapping("/low-stock")
    public ResponseEntity<List<Inventory>> getLowStockItems() {


        List<Inventory> lowStock =
                inventoryRepository.findAll()
                .stream()
                .filter(item ->
                        item.getStockLevel()
                        <= item.getMinimumStock()
                )
                .toList();



        return ResponseEntity.ok(lowStock);

    }


}