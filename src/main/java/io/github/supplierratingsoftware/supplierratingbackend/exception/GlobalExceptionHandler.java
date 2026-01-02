package io.github.supplierratingsoftware.supplierratingbackend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the Supplier Rating Backend application.
 *
 * This class centralizes exception handling across the application, providing
 * standardized responses for various types of exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles validation errors by extracting field-specific error messages.
     *
     * @param ex The MethodArgumentNotValidException containing validation errors.
     * @return A ResponseEntity with HTTP status 400 (Bad Request) and a map of field errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Validation error: {}", ex.getBindingResult());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles resource not found exceptions by returning a 404 status with the exception message.
     *
     * @param ex The RuntimeException indicating the resource was not found.
     * @return A ResponseEntity with HTTP status 404 (Not Found) and the exception message.
     */
    @ExceptionHandler({OpenBisResourceNotFoundException.class})
    public ResponseEntity<String> handleResourceNotFound(RuntimeException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles illegal argument exceptions by returning a 400 status with the exception message.
     *
     * @param ex The IllegalArgumentException indicating an invalid argument.
     * @return A ResponseEntity with HTTP status 400 (Bad Request) and the exception message.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Bad request (Business Rule Violation): {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    /**
     * Handles OpenBIS integration exceptions by returning a 500 status with the exception message.
     *
     * @param ex The OpenBisIntegrationException indicating an OpenBIS integration error.
     * @return A ResponseEntity with HTTP status 500 (Internal Server Error) and the exception message.
     */
    @ExceptionHandler(OpenBisIntegrationException.class)
    public ResponseEntity<String> handleOpenBisIntegration(OpenBisIntegrationException ex) {
        log.error("OpenBIS integration error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}