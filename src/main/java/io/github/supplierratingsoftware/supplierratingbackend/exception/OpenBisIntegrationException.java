package io.github.supplierratingsoftware.supplierratingbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an interaction with the OpenBIS system fails.
 * Examples: Creation failed, Search failed, Login failed.
 * <p>
 * This Exception gets mapped to HTTP status 500.
 * </p>
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class OpenBisIntegrationException extends RuntimeException {

    /**
     * Constructor for OpenBisIntegrationException.
     * @param message Detailed error message describing the failure.
     */
    public OpenBisIntegrationException(String message) {
        super(message);
    }

    /**
     * Constructor for OpenBisIntegrationException with cause.
     * @param message Detailed error message describing the failure.
     * @param cause The underlying cause of the exception.
     */
    public OpenBisIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}