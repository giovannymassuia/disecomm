package io.giovannymassuia.disecomm.inventorymanagement.controllers;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pod")
public class PodNameController {

    @Value("${HOSTNAME:unknown}")
    private String podName;

    @GetMapping()
    public ResponseEntity<?> getPodName() {
        return ResponseEntity.ok(Map.of(
            "podName", podName
        ));
    }
}
