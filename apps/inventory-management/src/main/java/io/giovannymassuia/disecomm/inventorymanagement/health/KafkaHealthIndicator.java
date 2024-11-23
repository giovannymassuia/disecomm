package io.giovannymassuia.disecomm.inventorymanagement.health;

import io.giovannymassuia.disecomm.inventorymanagement.controllers.InventoryController;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.common.errors.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.availability.ReadinessStateHealthIndicator;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component("kafka-health")
public class KafkaHealthIndicator extends ReadinessStateHealthIndicator {

    private final Logger log = LoggerFactory.getLogger(InventoryController.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaHealthIndicator(ApplicationAvailability availability,
        KafkaTemplate<String, Object> kafkaTemplate) {
        super(availability);
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    protected AvailabilityState getState(ApplicationAvailability applicationAvailability) {
        try {
            kafkaTemplate.send("kafka-health-indicator", "♥").get(1000, TimeUnit.MILLISECONDS);
            log.debug("Kafka health check succeeded");
            return ReadinessState.ACCEPTING_TRAFFIC;
        } catch (InterruptedException | ExecutionException | TimeoutException |
                 NullPointerException e) {
            log.error("Error while checking Kafka health", e);
        } catch (java.util.concurrent.TimeoutException e) {
            log.error("Kafka health check timed out", e);
        }

        return ReadinessState.REFUSING_TRAFFIC;
    }

}
