package io.github.supplierratingsoftware.supplierratingbackend.integration.openbis;

import io.github.supplierratingsoftware.supplierratingbackend.config.OpenBisProperties;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.generic.JsonRpcRequest;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.generic.JsonRpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class OpenBisClient {
    private static final Logger log = LoggerFactory.getLogger(OpenBisClient.class);
    private final RestClient restClient;
    private final OpenBisProperties properties;

    // NOTE: This is a temporary solution until the final login/token process is implemented.
    private String sessionToken;

    /**
     * Constructs an instance of OpenBisClient with the specified RestClient builder and OpenBisProperties.
     *
     * @param properties an OpenBisProperties instance containing the necessary OpenBIS configuration properties
     *                   such as API URL, username, and password.
     */
    public OpenBisClient(OpenBisProperties properties) {
        this.properties = properties;

        // OpenBIS sends "application/json-rpc" responses instead of "application/json"
        // This custom Jackson-Converter handles this case by adding "application/json-rpc" to the list of supported media types
        JacksonJsonHttpMessageConverter converter = new JacksonJsonHttpMessageConverter();
        converter.setSupportedMediaTypes(List.of(
                MediaType.APPLICATION_JSON,
                MediaType.parseMediaType("application/json-rpc")
        ));

        // Build the RestClient with the custom Jackson-Converter
        this.restClient = RestClient.builder()
                .baseUrl(properties.apiUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .configureMessageConverters(converters -> converters.addCustomConverter(converter))
                .build();
    }

    /**
     * Logs in to the OpenBIS system using the configured API URL, username, and password.
     * Sends an HTTP request to the OpenBIS API and retrieves a session token upon successful authentication.
     *
     * @return the session token for the logged-in user if the login is successful.
     * @throws RuntimeException if the login attempt fails or the response contains an error.
     */
    public String login() {
        log.info("Logging in to OpenBIS: {}", properties.apiUrl());

        // NOTE: The logic of getting the username and password will be changed once the final login/token process is implemented.
        // Create the login request with the configured username and password
        JsonRpcRequest request = new JsonRpcRequest("login", List.of(properties.user(), properties.password()));

        // Create a ParameterizedTypeReference for the response type
        // This is needed because the response type is generic
        // We expect a JsonRpcResponse<String> where the String is the session token
        ParameterizedTypeReference<JsonRpcResponse<String>> responseType = new ParameterizedTypeReference<>() {
        };

        // Send the login request and get the session token as a response (String)
        JsonRpcResponse<String> response = restClient.post().body(request).retrieve().body(responseType);

        if (response != null && !response.hasError() && response.result() != null) {
            sessionToken = response.result();
            log.info("Successfully logged in to OpenBIS");
            log.info("Session token: {}", sessionToken);
            return sessionToken;
        } else {
            log.error("Failed to log in to OpenBIS");
            log.error("Response: {}", response);
            throw new RuntimeException("Failed to log in to OpenBIS");
        }
    }

    /**
     * Returns the session token for the logged-in user.
     *
     * @return the session token for the logged-in user.
     */
    public String getSessionToken() {
        return sessionToken;
    }
}
