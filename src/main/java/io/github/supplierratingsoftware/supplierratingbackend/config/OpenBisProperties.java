package io.github.supplierratingsoftware.supplierratingbackend.config;

import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.ConfigurationConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for OpenBIS connection and search defaults.
 * Maps to prefix "openbis" in application.yaml.
 *
 * @param apiUrl   The URL of the OpenBIS JSON-RPC API (v3).
 * @param user     The OpenBIS username.
 * @param password The OpenBIS password.
 * @param search   Configuration for search scopes (Space/Projects).
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

        @Valid
        SearchConfig search
) {

    /**
     * Nested configuration for search scopes.
     *
     * @param defaultSpace    The default Space to search in (e.g. "LIEFERANTENBEWERTUNG").
     * @param supplierProject The Project code for suppliers (e.g. "LIEFERANTEN").
     * @param supplierType    The Sample Type code for suppliers (e.g. "LIEFERANT").
     * @param orderProject    The Project code for orders (e.g. "BESTELLUNGEN").
     * @param orderType       The Sample Type code for orders (e.g. "BESTELLUNG").
     */
    public record SearchConfig(
            @NotBlank(message = "The default Space must not be blank.")
            String defaultSpace,

            @NotBlank(message = "The supplier Project must not be blank.")
            String supplierProject,

            @NotBlank(message = "The supplier Sample Type must not be blank.")
            String supplierType,

            @NotBlank(message = "The order Project must not be blank.")
            String orderProject,

            @NotBlank(message = "The order Sample Type must not be blank.")
            String orderType
    ) {
    }
}
