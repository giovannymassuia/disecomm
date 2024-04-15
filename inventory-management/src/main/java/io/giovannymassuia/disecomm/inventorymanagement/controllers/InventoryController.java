package io.giovannymassuia.disecomm.inventorymanagement.controllers;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<?> adjustInventory(@RequestBody InventoryAdjustmentRequest request) {

        // post event with new quantity as json
        kafkaTemplate.send("inventory-adjustments", request.sku(), request);

        return ResponseEntity.ok(Map.of(
            "sku", request.sku,
            "newQuantity", request.newQuantity
        ));
    }

    public record InventoryAdjustmentRequest(String sku, int newQuantity) {

    }
}
