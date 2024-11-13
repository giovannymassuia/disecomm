package io.giovannymassuia.disecomm.inventorymanagement.controllers;

import io.giovannymassuia.disecomm.inventorymanagement.services.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/on-hand")
    public ResponseEntity<?> updateOnHand(@RequestBody UpdateOnHandRequest request) {
        logger.info("Updating inventory for sku: {} to new quantity: {}", request.sku(),
            request.newQuantity());
        inventoryService.updateOnHandQuantity(request.sku(), request.newQuantity());
        return ResponseEntity.ok().build();
    }

    public record UpdateOnHandRequest(String sku, int newQuantity) {

    }

}
