package io.github.supplierratingsoftware.supplierratingbackend.dto.api;

/**
 * Represents supplier statistics.
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *       "avgQuality": 4.5,
 *       "avgCost": 3.8,
 *       "avgReliability": 5,
 *       "avgAvailability": 4.2,
 *       "avgTotal": 4.4,
 *       "totalRatingCount": 12
 *     }
 * </pre>
 *
 * @param avgQuality       The average quality rating.
 * @param avgCost          The average cost rating.
 * @param avgReliability   The average reliability rating.
 * @param avgAvailability  The average availability rating.
 * @param avgTotal         The average total rating.
 * @param totalRatingCount The total number of ratings.
 * @see SupplierReadDto
 */
public record RatingStatsDto(
        Double avgQuality,
        Double avgCost,
        Double avgReliability,
        Double avgAvailability,
        Double avgTotal,
        Integer totalRatingCount
) {
}
