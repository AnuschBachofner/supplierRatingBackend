package io.github.supplierratingsoftware.supplierratingbackend.integration.openbis;

import io.github.supplierratingsoftware.supplierratingbackend.config.OpenBisProperties;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.generic.JsonRpcRequest;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.generic.JsonRpcResponse;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSample;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSearchResult;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleSearchCriteria;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Low-level client for communicating with the OpenBIS V3 JSON-RPC API.
 * <p>
 * This component handles the HTTP transport layer, session management (login),
 * and the serialization/deserialization of JSON-RPC messages. It is designed to be
 * unaware of specific business logic, focusing solely on the technical exchange of data.
 * </p>
 *
 * <p><strong>Configuration:</strong><br>
 * Uses {@link OpenBisProperties} for API URL and credentials.
 * Configures a specific {@link JacksonJsonHttpMessageConverter} to handle the "application/json-rpc" MIME type.
 * </p>
 */
@Service
public class OpenBisClient {
    private static final Logger log = LoggerFactory.getLogger(OpenBisClient.class);
    private final RestClient restClient;
    private final OpenBisProperties properties;

    // NOTE: This is a temporary solution until the final login/token process is implemented.
    @Getter
    private String sessionToken; // NOTE: since this is a temporary solution, we don't yet expect multi-threaded access.
                                 // otherwise we would need to synchronize access to this field.

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
        converter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON, MediaType.parseMediaType(OpenBisJsonConstants.SUPPORTED_MEDIA_TYPE)));

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
        JsonRpcRequest request = new JsonRpcRequest(OpenBisJsonConstants.LOGIN_METHOD_NAME, List.of(properties.user(), properties.password()));

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
            logResponseErrors("Failed to log in to OpenBIS", response);
            throw new RuntimeException("Failed to log in to OpenBIS");
        }
    }

    /**
     * Searches in openBIS for samples based on the specified criteria and fetch options.
     *
     * @param criteria     The search criteria to use for the search.
     * @param fetchOptions The fetch options to use for the search.
     * @return A list of {@link OpenBisSample} objects matching the specified criteria.
     */
    public List<OpenBisSample> searchSamples(SampleSearchCriteria criteria, SampleFetchOptions fetchOptions) {
        // Get the session token from the client
        String sessionToken = (this.sessionToken == null) ? login() : this.sessionToken;

        // Create the search request payload
        // Order of parameters in V3 API: [sessionToken, criteria, fetchOptions]
        JsonRpcRequest request = new JsonRpcRequest(OpenBisJsonConstants.SEARCH_SAMPLES_METHOD_NAME, List.of(sessionToken, criteria, fetchOptions));

        // Define the return type of the search request
        // We expect a JsonRpcResponse<OpenBisSearchResult<OpenBisSample>>
        // where the OpenBisSearchResult contains a list of OpenBisSample objects
        ParameterizedTypeReference<JsonRpcResponse<OpenBisSearchResult<OpenBisSample>>> responseType = new ParameterizedTypeReference<>() {
        };

        // Send the search request and get the search result as a response (OpenBisSearchResult<OpenBisSample>)
        JsonRpcResponse<OpenBisSearchResult<OpenBisSample>> response = restClient.post()
                .body(request)
                .retrieve()
                .body(responseType);

        // Unpack the search result and return the list of samples and handle any errors
        if (response != null && !response.hasError() && response.result() != null) {
            return response.result().objects();
        } else {
            logResponseErrors("Failed to search for samples", response);
            throw new RuntimeException("Failed to search for samples");
        }
    }

    /**
     * Logs an error message if the specified response contains an error.
     * (Helper method for error logging)
     *
     * @param message  The error message to log.
     * @param response The response to check for errors.
     */
    private void logResponseErrors(String message, JsonRpcResponse<?> response) {
        if (response != null && response.hasError()) {
            log.error(message);
            log.error("Response: {}", response.error());
        }
    }
}
