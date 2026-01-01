package io.github.supplierratingsoftware.supplierratingbackend.service;

import io.github.supplierratingsoftware.supplierratingbackend.config.OpenBisProperties;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.RatingDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.PropertyFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleTypeFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSample;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.PermIdSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleTypeSearchCriteria;
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
     * @return An Optional containing the mapped {@link RatingDto} if found, or empty otherwise.
     */
    public Optional<RatingDto> getRatingById(String permId) {
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
}