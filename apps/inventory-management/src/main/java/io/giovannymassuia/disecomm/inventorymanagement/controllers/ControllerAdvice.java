package io.giovannymassuia.disecomm.inventorymanagement.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errors = e.getConstraintViolations().stream()
                                         .collect(Collectors.toMap(
                                             violation -> violation.getPropertyPath().toString(),
                                             ConstraintViolation::getMessage
                                         ));

        return ResponseEntity.badRequest().body(Map.of("errors", errors));
    }

}
