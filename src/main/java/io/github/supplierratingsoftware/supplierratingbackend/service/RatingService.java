package io.github.supplierratingsoftware.supplierratingbackend.service;

import io.github.supplierratingsoftware.supplierratingbackend.config.OpenBisProperties;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.RatingCreationDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.RatingReadDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.creation.SampleCreation;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.PropertyFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleTypeFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id.SamplePermId;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSample;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.PermIdSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.ProjectSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleParentsSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleTypeSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SpaceSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.exception.OpenBisIntegrationException;
import io.github.supplierratingsoftware.supplierratingbackend.exception.OpenBisResourceNotFoundException;
import io.github.supplierratingsoftware.supplierratingbackend.integration.openbis.OpenBisClient;
import io.github.supplierratingsoftware.supplierratingbackend.mapper.RatingMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service responsible for managing supplier ratings.
 * <p>
 * This service communicates with the {@link OpenBisClient} to retrieve raw data
 * and uses the {@link RatingMapper} to transform it into API-compliant DTOs.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class RatingService {

    private static final Logger log = LoggerFactory.getLogger(RatingService.class);

    private final OpenBisClient openBisClient;
    private final OpenBisProperties properties;
    private final RatingMapper ratingMapper;

    /**
     * Retrieves a single rating by its OpenBIS PermID.
     * <p>
     * Ensures strict type safety by filtering for the configured rating sample type.
     * </p>
     *
     * @param permId The unique PermID of the rating.
     * @return An Optional containing the mapped {@link RatingReadDto} if found, or empty otherwise.
     */
    public Optional<RatingReadDto> getRatingById(String permId) {
        log.debug("Fetching rating with ID: {}", permId);

        // 1. Criteria: Must match PermID AND the configured Rating Type
        // This prevents returning objects of other types (e.g. Orders) even if the ID exists.
        SampleSearchCriteria criteria = SampleSearchCriteria.create()
                .with(PermIdSearchCriteria.withId(permId))
                .with(SampleTypeSearchCriteria.withCode(properties.rating().typeCode()));

        // 2. Fetch Options: Load Properties, Type, and Parent (Order)
        SampleFetchOptions fetchOptions = new SampleFetchOptions(
                new PropertyFetchOptions(),     // Load Scores
                new SampleTypeFetchOptions(),   // Load Type Info
                new SampleFetchOptions(         // PARENTS FETCHING (The Order)
                        null,                   // No properties needed for Order here
                        null,
                        null,                    // Stop recursion
                        null                    // No children needed for Orders
                ),
                null                    // No children needed for Ratings
        );

        // 3. Execute Search
        List<OpenBisSample> samples = openBisClient.searchSamples(criteria, fetchOptions);

        if (samples.isEmpty()) {
            return Optional.empty();
        }

        // 4. Map & Return
        return Optional.ofNullable(ratingMapper.toApiDto(samples.get(0)));
    }

    /**
     * Creates a new rating in OpenBIS based on the provided data.
     *
     * @param creationDto The data for the new rating.
     * @return The created rating DTO (fetched fresh from openBIS to ensure consistency).
     */
    public RatingReadDto createRating(RatingCreationDto creationDto) {
        log.info("Creating rating for order: {}", creationDto.orderId());

        // Check if Order Exists
        validateOrderExists(creationDto.orderId());

        // Check 1:1 Rule (Does a rating already exist for this order?)
        validateNoExistingRating(creationDto.orderId());

        // Create Rating in OpenBIS
        SampleCreation creation = ratingMapper.toOpenBisCreation(creationDto);
        List<SamplePermId> createdIds = openBisClient.createSamples(List.of(creation));

        if (createdIds.isEmpty()) throw new OpenBisIntegrationException("Rating creation failed: No PermID returned from openBIS");

        SamplePermId permId = createdIds.getFirst();
        log.info("Successfully created rating with PermID: {}", permId.permId());

        // Fetch the fresh rating from OpenBIS to ensure consistency and return
        return fetchFreshRating(permId.permId());
    }

    /**
     * Helper method to validate that the given order ID exists in openBIS and that it actually is an Order.
     *
     * @param orderId The order ID to validate.
     * @throws OpenBisResourceNotFoundException if the order does not exist.
     */
    private void validateOrderExists(String orderId) {
        // Validate ID AND Type (must be an Order)
        SampleSearchCriteria criteria = SampleSearchCriteria.create()
                .with(PermIdSearchCriteria.withId(orderId))
                .with(SampleTypeSearchCriteria.withCode(properties.order().typeCode()));
        SampleFetchOptions fetchOptions = new SampleFetchOptions(
                new PropertyFetchOptions(),
                new SampleTypeFetchOptions(),
                null,
                null);
        List<OpenBisSample> results = openBisClient.searchSamples(criteria, fetchOptions);
        if (results.isEmpty()) throw new OpenBisResourceNotFoundException("Order with PermID " + orderId + " not found.");
    }

    /**
     * Helper method to validate that no rating already exists for the given order ID.
     * Ensures 1:1 rule for rating to order relationship.
     *
     * @param orderId The order ID to validate if a rating already exists for.
     * @throws IllegalArgumentException if a rating already exists for the given order ID.
     */
    private void validateNoExistingRating(String orderId) {
        SampleSearchCriteria criteria = SampleSearchCriteria.create()
                .with(SpaceSearchCriteria.withCode(properties.defaultSpace()))
                .with(ProjectSearchCriteria.withCode(properties.rating().projectCode()))
                .with(SampleTypeSearchCriteria.withCode(properties.rating().typeCode()))
                .with(SampleParentsSearchCriteria.withParentId(orderId));
        SampleFetchOptions fetchOptions = new SampleFetchOptions(
                new PropertyFetchOptions(),
                new SampleTypeFetchOptions(),
                null,
                null
        );
        List<OpenBisSample> results = openBisClient.searchSamples(criteria, fetchOptions);
        if (!results.isEmpty()) throw new IllegalArgumentException("Order " + orderId + " is already rated. Cannot create duplicate rating.");
    }

    /**
     * Optimized fetch of fresh rating from OpenBIS using the provided PermID.
     *
     * @param permId The PermID of the rating to fetch.
     * @return The fetched rating DTO.
     * @throws OpenBisResourceNotFoundException if the rating with the given PermID is not found.
     */
    private RatingReadDto fetchFreshRating(String permId) {
        SampleSearchCriteria criteria = SampleSearchCriteria.create().with(PermIdSearchCriteria.withId(permId));
        SampleFetchOptions parentOptions = new SampleFetchOptions(null, null, null, null);
        SampleFetchOptions fetchOptions = new SampleFetchOptions(
                new PropertyFetchOptions(),
                new SampleTypeFetchOptions(),
                parentOptions,
                null);
        List<OpenBisSample> results = openBisClient.searchSamples(criteria, fetchOptions);
        if (results.isEmpty()) throw new OpenBisResourceNotFoundException("Critical Error: Rating created but could not be retrieved immediately. PermID: " + permId);
        return ratingMapper.toApiDto(results.getFirst());
    }
}