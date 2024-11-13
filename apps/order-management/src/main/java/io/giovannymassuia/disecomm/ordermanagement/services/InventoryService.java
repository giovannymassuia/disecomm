package io.giovannymassuia.disecomm.ordermanagement.services;

import io.giovannymassuia.disecomm.ordermanagement.database.Inventory;
import io.giovannymassuia.disecomm.ordermanagement.database.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void updateOnHandQuantity(String sku, Integer newQuantity) {
        inventoryRepository.findById(sku)
            .ifPresentOrElse(inventory -> {
                    inventory.setOnHand(newQuantity);
                    inventoryRepository.save(inventory);
                },
                () -> inventoryRepository.save(new Inventory(sku, newQuantity))
            );
    }
}
