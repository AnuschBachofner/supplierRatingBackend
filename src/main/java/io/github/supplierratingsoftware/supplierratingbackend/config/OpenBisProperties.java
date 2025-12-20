package io.github.supplierratingsoftware.supplierratingbackend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OpenBIS connection configuration properties.
 *
 * <p><strong>YAML Configuration Example:</strong></p>
 * <pre>
 *     openbis:
 *          api-url: "https://openbis.example.com/openbis/openbis/rmi-application-server-v3.json"
 *          user: "admin"
 *          password: "secret-password"
 * </pre>
 *
 * @param apiUrl   The full URL to the OpenBIS V3 JSON-RPC endpoint.
 * @param user     The OpenBIS username.
 * @param password The OpenBIS password.
 */
@ConfigurationProperties(prefix = "openbis")
public record OpenBisProperties(String apiUrl, String user, String password) {
}
