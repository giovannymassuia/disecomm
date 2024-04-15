package io.giovannymassuia.disecomm.ordermanagement.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryConsumer {

    private final Logger logger = LoggerFactory.getLogger(InventoryConsumer.class);

    private final ObjectMapper objectMapper;

    public InventoryConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "inventory-adjustments")
    public void consume(ConsumerRecord<String, String> record) {
        var request = parse(record.value());
        logger.info("Consumed inventory adjustment key: {}, value: {}", record.key(), request);
    }

    private InventoryAdjustmentRequest parse(String value) {
        try {
            return objectMapper.readValue(value, InventoryAdjustmentRequest.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public record InventoryAdjustmentRequest(String sku, int newQuantity) {

    }

}
