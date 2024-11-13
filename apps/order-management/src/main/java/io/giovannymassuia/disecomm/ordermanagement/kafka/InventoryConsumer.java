package io.giovannymassuia.disecomm.ordermanagement.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.giovannymassuia.disecomm.ordermanagement.services.InventoryService;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryConsumer {

    private final Logger logger = LoggerFactory.getLogger(InventoryConsumer.class);

    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper;

    public InventoryConsumer(InventoryService inventoryService, ObjectMapper objectMapper) {
        this.inventoryService = inventoryService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "inventory-adjustments")
    public void consume(ConsumerRecord<String, String> record) {
        var value = parse(record.value());

        if (value.event().equals("onHandUpdated")) {
            var payload = OnHandUpdatePayload.parse(value.payload());
            logger.info("Consumed inventory adjustment key: {}, value: {}", record.key(), payload);

            int newOnHand = payload.quantities().getOrDefault("onHand", 0);
            inventoryService.updateOnHandQuantity(payload.sku(), newOnHand);
        }
    }

    private InventoryAdjustmentMessage parse(String value) {
        try {
            return objectMapper.readValue(value, InventoryAdjustmentMessage.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public record InventoryAdjustmentMessage(String event, Map<String, Object> payload) {

    }

    public record OnHandUpdatePayload(String sku, Map<String, Integer> quantities) {

        @SuppressWarnings("unchecked")
        public static OnHandUpdatePayload parse(Map<String, Object> payload) {
            return new OnHandUpdatePayload(
                (String) payload.get("sku"),
                (Map<String, Integer>) payload.get("quantities"));
        }
    }

}
