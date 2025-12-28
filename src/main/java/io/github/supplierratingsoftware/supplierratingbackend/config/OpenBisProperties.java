package io.github.supplierratingsoftware.supplierratingbackend.config;

import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.ConfigurationConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for OpenBIS connection and search defaults.
 * Maps to prefix "openbis" in application.yaml.
 *
 * @param apiUrl   The URL of the OpenBIS JSON-RPC API (v3).
 * @param user     The OpenBIS username.
 * @param password The OpenBIS password.
 * @param search   Configuration for search scopes (Space/Projects).
 */
@ConfigurationProperties(prefix = ConfigurationConstants.OPENBIS_YAML_CONFIG_PREFIX)
public record OpenBisProperties(String apiUrl, String user, String password, SearchConfig search) {
    /**
     * Nested configuration for search scopes.
     *
     * @param defaultSpace    The default Space to search in (e.g. "LIEFERANTENBEWERTUNG").
     * @param supplierProject The Project code for suppliers (e.g. "LIEFERANTEN").
     */
    public record SearchConfig(String defaultSpace, String supplierProject) {
    }
}
