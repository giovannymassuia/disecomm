package io.giovannymassuia.disecomm.inventorymanagement.health;

import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.availability.ReadinessStateHealthIndicator;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.stereotype.Component;

@Component("custom-readiness")
public class CustomReadinessIndicator extends ReadinessStateHealthIndicator {
    private final Logger log = LoggerFactory.getLogger(CustomReadinessIndicator.class);
    private final AtomicBoolean ready = new AtomicBoolean(true);

    public CustomReadinessIndicator(ApplicationAvailability availability) {
        super(availability);
    }

    @Override
    protected AvailabilityState getState(ApplicationAvailability applicationAvailability) {
        if (ready.get()) {
            return ReadinessState.ACCEPTING_TRAFFIC;
        }
        return ReadinessState.REFUSING_TRAFFIC;
    }

    public void setReady(boolean ready) {
        this.ready.set(ready);
    }

}
