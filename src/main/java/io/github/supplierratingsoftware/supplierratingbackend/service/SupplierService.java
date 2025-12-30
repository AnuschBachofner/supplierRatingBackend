package io.github.supplierratingsoftware.supplierratingbackend.service;

import io.github.supplierratingsoftware.supplierratingbackend.config.OpenBisProperties;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisSchemaConstants;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.RatingStatsDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.SupplierDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.PropertyFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleTypeFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSample;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.ProjectSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleTypeSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SpaceSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.integration.openbis.OpenBisClient;
import io.github.supplierratingsoftware.supplierratingbackend.mapper.SupplierMapper;
import io.github.supplierratingsoftware.supplierratingbackend.util.OpenBisParseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Service responsible for managing supplier-related business logic.
 * <p>
 * This service communicates with the {@link OpenBisClient} to retrieve raw data
 * and uses the {@link SupplierMapper} to transform it into API-compliant DTOs.
 * It also aggregates statistics (ratings) for suppliers.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class SupplierService {

    private final OpenBisClient openBisClient;
    private final SupplierMapper supplierMapper;
    private final OpenBisProperties properties;

    /**
     * Retrieves all suppliers from OpenBIS.
     * <p>
     * Executes a server-side filtered search for samples located in the configured
     * space, project, and matching the configured sample type.
     * It fetches only the necessary properties and types for the suppliers,
     * including their child orders and ratings to calculate aggregates.
     * </p>
     *
     * @return A list of {@link SupplierDto} objects representing all available suppliers.
     */
    public List<SupplierDto> getAllSuppliers() {
        // 1. Define Hierarchy Fetch Options (Recursive Fetching)

        // Level 2: Ratings (Children of Orders)
        SampleFetchOptions ratingFetchOptions = new SampleFetchOptions(
                new PropertyFetchOptions(),
                new SampleTypeFetchOptions(),
                null,
                null
        );

        // Level 1: Orders (Children of Suppliers) -> fetch their children (Ratings)
        SampleFetchOptions orderFetchOptions = new SampleFetchOptions(
                new PropertyFetchOptions(),
                new SampleTypeFetchOptions(),
                null,
                ratingFetchOptions
        );

        // Level 0: Suppliers -> fetch their children (Orders)
        SampleFetchOptions supplierFetchOptions = new SampleFetchOptions(
                new PropertyFetchOptions(),
                new SampleTypeFetchOptions(),
                null,
                orderFetchOptions
        );

        // 2. Define Search Criteria
        SampleSearchCriteria criteria = SampleSearchCriteria.create()
                .with(SpaceSearchCriteria.withCode(properties.search().defaultSpace()))
                .with(ProjectSearchCriteria.withCode(properties.search().supplierProject()))
                .with(SampleTypeSearchCriteria.withCode(properties.search().supplierType()));

        // 3. Execute Search
        List<OpenBisSample> rawSamples = openBisClient.searchSamples(criteria, supplierFetchOptions);

        // 4. Map & Aggregate
        return rawSamples.stream()
                .map(supplier -> {
                    RatingStatsDto stats = calculateStats(supplier);
                    return supplierMapper.toDto(supplier, stats);
                })
                .toList();
    }

    /**
     * Calculates the average ratings for a given supplier based on its child orders and their ratings.
     * <p>
     * Logic:
     * - Null or blank values in openBIS are ignored (they do not pull down the average).
     * - Averages are calculated individually per category (Quality, Cost, etc.).
     * </p>
     *
     * @param supplier The supplier sample.
     * @return The aggregated rating statistics.
     */
    private RatingStatsDto calculateStats(OpenBisSample supplier) {
        List<OpenBisSample> orders = supplier.children();
        if (orders == null || orders.isEmpty()) {
            return emptyStats();
        }

        // Flatten: Supplier -> Orders -> Ratings
        List<OpenBisSample> ratings = orders.stream()
                .map(OpenBisSample::children)           // Get children of Orders
                .filter(Objects::nonNull)               // Ignore Orders without children
                .flatMap(List::stream)                  // Convert Stream<List<Sample>> to Stream<Sample>
                .filter(child -> child.type() != null  // Handle null type gracefully
                        && properties // If the child type is defined
                        .search()
                        .ratingType()
                        .equals(child.type().code()))   // Ensure strict type safety
                .toList();

        if (ratings.isEmpty()) {
            return emptyStats();
        }

        // Initialize accumulators for each category
        RatingAccumulator qualityAcc = new RatingAccumulator();
        RatingAccumulator costAcc = new RatingAccumulator();
        RatingAccumulator reliabilityAcc = new RatingAccumulator();
        RatingAccumulator availabilityAcc = new RatingAccumulator();
        RatingAccumulator totalAcc = new RatingAccumulator();

        // Iterate and accumulate
        for (OpenBisSample rating : ratings) {
            String code = rating.code();

            qualityAcc.add(OpenBisParseUtils.parseDoubleOrNull(
                    rating.getProperty(OpenBisSchemaConstants.QUALITY_RATING_PROPERTY),
                    OpenBisSchemaConstants.QUALITY_RATING_PROPERTY, code));

            costAcc.add(OpenBisParseUtils.parseDoubleOrNull(
                    rating.getProperty(OpenBisSchemaConstants.COST_RATING_PROPERTY),
                    OpenBisSchemaConstants.COST_RATING_PROPERTY, code));

            reliabilityAcc.add(OpenBisParseUtils.parseDoubleOrNull(
                    rating.getProperty(OpenBisSchemaConstants.RELIABILITY_RATING_PROPERTY),
                    OpenBisSchemaConstants.RELIABILITY_RATING_PROPERTY, code));

            availabilityAcc.add(OpenBisParseUtils.parseDoubleOrNull(
                    rating.getProperty(OpenBisSchemaConstants.AVAILABILITY_RATING_PROPERTY),
                    OpenBisSchemaConstants.AVAILABILITY_RATING_PROPERTY, code));

            totalAcc.add(OpenBisParseUtils.parseDoubleOrNull(
                    rating.getProperty(OpenBisSchemaConstants.TOTAL_SCORE_RATING_PROPERTY),
                    OpenBisSchemaConstants.TOTAL_SCORE_RATING_PROPERTY, code));
        }

        return new RatingStatsDto(
                qualityAcc.getAverage(),
                costAcc.getAverage(),
                reliabilityAcc.getAverage(),
                availabilityAcc.getAverage(),
                totalAcc.getAverage(),
                ratings.size() // Total count of rating objects
        );
    }

    /**
     * Generates an empty stats object.
     * We use nulls for averages to indicate "no ratings yet" to the frontend.
     */
    private RatingStatsDto emptyStats() {
        return new RatingStatsDto(null, null, null, null, null, 0);
    }

    /**
     * Internal helper class to handle summation and averaging of rating values.
     * Encapsulates the logic for ignoring null values and calculating rounded averages.
     */
    private static class RatingAccumulator {
        private double sum = 0.0;
        private int count = 0;

        /**
         * Adds a value to the accumulator if it is not null.
         *
         * @param value The value to add (can be null).
         */
        void add(Double value) {
            if (value != null) {
                sum += value;
                count++;
            }
        }

        /**
         * Calculates the average of the accumulated values.
         *
         * @return The average rounded to 2 decimal places, or null if the count is 0.
         */
        Double getAverage() {
            if (count == 0) {
                return null;
            }
            return Math.round((sum / count) * 100.0) / 100.0;
        }
    }
}