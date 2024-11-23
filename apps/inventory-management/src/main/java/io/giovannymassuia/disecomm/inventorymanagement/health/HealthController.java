package io.giovannymassuia.disecomm.inventorymanagement.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
    private final Logger log = LoggerFactory.getLogger(HealthController.class);

    private final CustomReadinessIndicator readinessIndicator;
    private final CustomLivenessIndicator livenessIndicator;

    public HealthController(CustomReadinessIndicator readinessIndicator, CustomLivenessIndicator livenessIndicator) {
        this.readinessIndicator = readinessIndicator;
        this.livenessIndicator = livenessIndicator;
    }

    @PostMapping("/liveness/alive")
    public void alive() {
        livenessIndicator.setReady(true);
    }

    @PostMapping("/liveness/dead")
    public void dead() {
        livenessIndicator.setReady(false);
    }

    @PostMapping("/readiness/ready")
    public void ready() {
        readinessIndicator.setReady(true);
    }

    @PostMapping("/readiness/not-ready")
    public void notReady() {
        readinessIndicator.setReady(false);

        setReadyTimeout();
    }

    @Async
    public void setReadyTimeout() {
        try {
            log.info("Setting readiness to false for 45 seconds");
            Thread.sleep(45000);
            log.info("Setting readiness to true");
            readinessIndicator.setReady(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
