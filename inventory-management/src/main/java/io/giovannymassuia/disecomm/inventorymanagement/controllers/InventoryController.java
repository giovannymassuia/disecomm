package io.giovannymassuia.disecomm.inventorymanagement.controllers;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<?> adjustInventory(@RequestBody InventoryAdjustmentRequest request) {

        logger.info("Adjusting inventory for sku: {} to new quantity: {}", request.sku(),
            request.newQuantity());

        // random fibonacci calculation between 1 and 40
        int n = (int) (Math.random() * 50) + 1;
        long result = fibonacci(n);

        // add random delay between 100 and 2000 ms
        try {
            Thread.sleep((long) (Math.random() * 1900) + 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // post event with new quantity as json
        kafkaTemplate.send("inventory-adjustments", request.sku(), request);

        return ResponseEntity.ok(Map.of(
            "sku", request.sku,
            "newQuantity", request.newQuantity
        ));
    }

    public record InventoryAdjustmentRequest(String sku, int newQuantity) {

    }

    private long fibonacci(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}
