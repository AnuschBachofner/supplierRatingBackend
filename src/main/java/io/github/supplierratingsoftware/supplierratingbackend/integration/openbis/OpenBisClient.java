package io.github.supplierratingsoftware.supplierratingbackend.integration.openbis;

import io.github.supplierratingsoftware.supplierratingbackend.config.OpenBisProperties;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.creation.SampleCreation;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.generic.JsonRpcRequest;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.generic.JsonRpcResponse;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id.SamplePermId;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSample;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSearchResult;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.update.SampleUpdate;
import io.github.supplierratingsoftware.supplierratingbackend.exception.OpenBisIntegrationException;
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

    /**
     * Constructs an instance of OpenBisClient with the specified RestClient builder and OpenBisProperties.
     *
     * @param properties an OpenBisProperties instance containing the necessary OpenBIS configuration properties
     *                   such as API URL, username, and password.
     * @param restClientBuilder a RestClient builder used to configure the RestClient used by the OpenBisClient.
     */
    public OpenBisClient(OpenBisProperties properties, RestClient.Builder restClientBuilder) {
        this.properties = properties;

        // OpenBIS sends "application/json-rpc" responses instead of "application/json"
        // This custom Jackson-Converter handles this case by adding "application/json-rpc" to the list of supported media types
        JacksonJsonHttpMessageConverter converter = new JacksonJsonHttpMessageConverter();
        converter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON, MediaType.parseMediaType(OpenBisJsonConstants.SUPPORTED_MEDIA_TYPE)));

        // Build the RestClient with the custom Jackson-Converter
        this.restClient = restClientBuilder
                .baseUrl(properties.apiUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .configureMessageConverters(converters -> converters.addCustomConverter(converter))
                .build();
    }

    /**
     * NOTE: The Login process is currently a temporary solution until the final login/token process is implemented.
     * TODO: Implement a final login/token process and replace the temporary solution.
     * TODO: This would involve a more secure and robust authentication mechanism from the REST-API of this application to OpenBIS.
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
        ParameterizedTypeReference<JsonRpcResponse<String>> responseType = new ParameterizedTypeReference<>() {};

        // Send the login request
        JsonRpcResponse<String> response;
        try {
            // Send the login request and get the session token as a response (String)
            response = restClient.post().body(request).retrieve().body(responseType);
        } catch (Exception e) {
            log.error("Login request failed: {}", e.getMessage());
            throw new OpenBisIntegrationException("Login request failed: " + e.getMessage(), e);
        }

        // Validate response
        validateResponse(response, "Failed to log in to OpenBIS");

        // Check for null session token
        if (response.result() == null) {
            log.error("Login request returned `null` session token");
            throw new OpenBisIntegrationException("Login request returned `null` session token");
        }

        // Success, return session token
        String sessionToken = response.result();
        log.info("Successfully logged in to OpenBIS");
        log.debug("Session token: {}", sessionToken);
        return sessionToken;
    }

    /**
     * Meldet einen Benutzer mit individuellen Zugangsdaten bei openBIS an.
     * @param username Der vom Frontend gesendete Benutzername.
     * @param password Das vom Frontend gesendete Passwort.
     * @return Ein Session-Token (Eintrittskarte), wenn der Login erfolgreich war.
     */
    public String authenticate(String username, String password) {
        log.info("Versuche Benutzer '{}' bei openBIS anzumelden...", username);

        // 1. Wir erstellen das Paket für openBIS (die JSON-RPC Anfrage).
        // openBIS erwartet beim Login eine Liste mit zwei Einträgen: [Username, Passwort].
        JsonRpcRequest request = new JsonRpcRequest(
                OpenBisJsonConstants.LOGIN_METHOD_NAME,
                List.of(username, password)
        );

        // 2. Wir definieren, dass wir als Antwort einen String (den Token) erwarten.
        ParameterizedTypeReference<JsonRpcResponse<String>> responseType = new ParameterizedTypeReference<>() {};

        try {
            // 3. Wir schicken die Anfrage ab.
            JsonRpcResponse<String> response = restClient.post()
                    .body(request)
                    .retrieve()
                    .body(responseType);

            // 4. Wir prüfen, ob die Antwort gültig ist.
            validateResponse(response, "Login fehlgeschlagen");

            if (response.result() == null) {
                throw new OpenBisIntegrationException("Kein Session-Token von openBIS erhalten.");
            }

            log.info("Benutzer '{}' erfolgreich angemeldet.", username);
            return response.result(); // Das ist unser Token!

        } catch (Exception e) {
            log.error("Fehler beim openBIS-Login für '{}': {}", username, e.getMessage());
            throw new OpenBisIntegrationException("Authentifizierung fehlgeschlagen: " + e.getMessage());
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
        String sessionToken = login();

        // Create the search request payload
        // Order of parameters in V3 API: [sessionToken, criteria, fetchOptions]
        JsonRpcRequest request = new JsonRpcRequest(OpenBisJsonConstants.SEARCH_SAMPLES_METHOD_NAME, List.of(sessionToken, criteria, fetchOptions));

        // Define the return type of the search request
        // We expect a JsonRpcResponse<OpenBisSearchResult<OpenBisSample>>
        // where the OpenBisSearchResult contains a list of OpenBisSample objects
        ParameterizedTypeReference<JsonRpcResponse<OpenBisSearchResult<OpenBisSample>>> responseType = new ParameterizedTypeReference<>() {};

        // Send the search request
        JsonRpcResponse<OpenBisSearchResult<OpenBisSample>> response;
        try {
            // Send the search request and get the search result as a response (OpenBisSearchResult<OpenBisSample>)
            response = restClient.post()
                    .body(request)
                    .retrieve()
                    .body(responseType);
        } catch (Exception e) {
            log.error("Search request failed: {}", e.getMessage());
            throw new OpenBisIntegrationException("Search request failed: " + e.getMessage(), e);
        }

        // Validate response
        validateResponse(response, "Failed to search for samples");

        // Ensure response result is not null
        if (response.result() == null) {
            log.error("Search returned a `null` result");
            throw new OpenBisIntegrationException("Search returned a `null` result");
        }

        // Success, return the list of samples
        return response.result().objects();
    }

    /**
     * Creates new samples in openBIS.
     *
     * @param samples The list of samples to create.
     * @return A list of permIds of the created samples.
     * @throws RuntimeException if the creation fails.
     */
    public List<SamplePermId> createSamples(List<SampleCreation> samples) {

        // Safety check: Ensure we have samples to create
        if (samples == null || samples.isEmpty()) {
            return List.of();
        }

        // Safety check: Ensure we are logged in
        String sessionToken = login();

        // Prepare Request
        List<Object> params = List.of(sessionToken, samples);
        JsonRpcRequest request = new JsonRpcRequest(OpenBisJsonConstants.CREATE_SAMPLE_METHOD_NAME, params);

        // Define Response Type
        ParameterizedTypeReference<JsonRpcResponse<List<SamplePermId>>> responseType = new ParameterizedTypeReference<>() {};

        // Execute Request
        JsonRpcResponse<List<SamplePermId>> response;
        try {
            response = restClient.post()
                    .body(request)
                    .retrieve()
                    .body(responseType);
        } catch (Exception e) {
            log.error("Creation request failed: {}", e.getMessage());
            throw new OpenBisIntegrationException("Creation request failed: " + e.getMessage(), e);
        }

        // Validate response
        validateResponse(response, "Failed to create samples");

        // Ensure response result is not null
        if (response.result() == null) {
            log.error("Creation returned a `null` result");
            throw new OpenBisIntegrationException("Creation returned a `null` result");
        }

        // Success, return the list of sample perm IDs of the created sample
        return response.result();
    }

    /**
     * Updates existing samples in OpenBIS.
     *
     * @param updates The list of update objects.
     */
    public void updateSamples(List<SampleUpdate> updates) {
        if (updates == null || updates.isEmpty()) return;
        String sessionToken = login();

        // Create the update request payload
        List<Object> params = List.of(sessionToken, updates);
        JsonRpcRequest request = new JsonRpcRequest(OpenBisJsonConstants.UPDATE_SAMPLE_METHOD_NAME, params);

        // Define Response Type (OpenBIS returns "result": null for update requests)
        ParameterizedTypeReference<JsonRpcResponse<Void>> responseType = new ParameterizedTypeReference<>() {};

        // Execute Request
        JsonRpcResponse<Void> response;
        try {
            response = restClient.post()
                    .body(request)
                    .retrieve()
                    .body(responseType);
        } catch (Exception e) {
            log.error("Update request failed: {}", e.getMessage());
            throw new OpenBisIntegrationException("Update request failed: " + e.getMessage(), e);
        }

        // Validate response
        validateResponse(response, "Failed to update samples");
    }

    /**
     * Validates that the response is not null and does not contain an OpenBIS error.
     *
     * @param response      The response to validate.
     * @param errorMessage  The error message to use if validation fails.
     * @throws OpenBisIntegrationException if the validation fails.
     */
    private void validateResponse(JsonRpcResponse<?> response, String errorMessage) {
        if (response == null) {
            log.error("{}: Received null response from OpenBIS", errorMessage);
            throw new OpenBisIntegrationException(errorMessage + ": Received null response from OpenBIS");
        }

        if (response.hasError()) {
            if (response.error() != null) {
                log.error("Response: {}", response.error());
            }
            String errorDetail = (response.error() != null) ? response.error().toString() : "Unknown Error";
            log.error(errorMessage);
            log.error("Error in OpenBIS Response. Details: {}", errorDetail);
            throw new OpenBisIntegrationException(errorMessage + ": " + errorDetail);
        }
    }
}
