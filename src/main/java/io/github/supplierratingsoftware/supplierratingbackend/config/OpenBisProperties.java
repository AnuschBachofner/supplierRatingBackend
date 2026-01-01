package io.github.supplierratingsoftware.supplierratingbackend.config;

import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.ConfigurationConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for OpenBIS connection and entity defaults.
 * Maps to the prefix "openbis" in application.yaml.
 *
 * @param apiUrl   The URL of the OpenBIS JSON-RPC API (v3).
 * @param user     The OpenBIS username.
 * @param password The OpenBIS password.
 * @param supplier Configuration for supplier entities.
 * @param order    Configuration for order entities.
 * @param rating   Configuration for rating entities.
 */
@Validated
@ConfigurationProperties(prefix = ConfigurationConstants.OPENBIS_YAML_CONFIG_PREFIX)
public record OpenBisProperties(
        @NotBlank(message = "The OpenBIS API URL must not be blank.")
        String apiUrl,

        @NotBlank(message = "The OpenBIS username must not be blank.")
        String user,

        @NotBlank(message = "The OpenBIS password must not be blank.")
        String password,

        @NotBlank(message = "The default Space must not be blank.")
        String defaultSpace,

        @Valid
        OpenBisProperties.EntityConfig supplier,

        @Valid
        OpenBisProperties.EntityConfig order,

        @Valid
        OpenBisProperties.EntityConfig rating
        ) {

    /**
     * Nested configuration for entity-related settings.
     * This configuration includes details like the project, sample type, and collection codes.
     *
     * @param projectCode The project code in openBIS.
     * @param typeCode The sample type code in openBIS.
     * @param collectionCode The collection code in openBIS.
     */
    public record EntityConfig(
            @NotBlank(message = "The project code must not be blank.")
            String projectCode,

            @NotBlank(message = "The sample type code must not be blank.")
            String typeCode,

            //TODO: set `@NotBlank()` annotation as soon as the write functionality is implemented for all entities.
            //@NotBlank(message = "The collection code must not be blank.")
            String collectionCode
    ) {
    }
}
