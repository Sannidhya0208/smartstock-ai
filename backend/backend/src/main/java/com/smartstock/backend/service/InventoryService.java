package com.smartstock.backend.service;

import com.smartstock.backend.model.Inventory;
import com.smartstock.backend.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> getInventory() {
        return inventoryRepository.findAll();
    }

    public Inventory save(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }
}