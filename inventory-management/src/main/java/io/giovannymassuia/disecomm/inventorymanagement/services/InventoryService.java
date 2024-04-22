package io.giovannymassuia.disecomm.inventorymanagement.services;

import io.giovannymassuia.disecomm.inventorymanagement.database.Inventory;
import io.giovannymassuia.disecomm.inventorymanagement.database.InventoryRepository;
import java.util.Map;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryService(InventoryRepository inventoryRepository,
        KafkaTemplate<String, Object> kafkaTemplate) {
        this.inventoryRepository = inventoryRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void updateOnHandQuantity(String sku, Integer newQuantity) {
        inventoryRepository.findById(sku)
            .ifPresentOrElse(inventory -> {
                    inventory.setOnHand(newQuantity);
                    inventoryRepository.save(inventory);
                },
                () -> inventoryRepository.save(new Inventory(sku, newQuantity))
            );

        kafkaTemplate.send("inventory-adjustments",
            sku, Map.of(
                "event", "onHandUpdated",
                "payload", Map.of(
                    "sku", sku,
                    "quantities", Map.of(
                        "onHand", newQuantity))));
    }
}
