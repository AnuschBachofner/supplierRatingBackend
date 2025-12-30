package io.github.supplierratingsoftware.supplierratingbackend.dto.api;

/**
 * Represents a rating.
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "quality": 4.5,
 *         "qualityReason": "Quality is good",
 *         "cost": 3.8,
 *         "costReason": "Cost is good",
 *         "reliability": 5,
 *         "reliabilityReason": "Reliability is good",
 *         "availability": 4.2,
 *         "availabilityReason": "Availability is good",
 *         "totalScore": 4.2,
 *         "ratingComment": "This is a good rating!",
 *         "id": "12345",
 *         "code": "string",
 *         "orderId": "12345",
 *         "supplierId": "string",
 *         "supplierName": "Acme Corp"
 *     }
 * </pre>
 *
 * @param quality The quality rating.
 * @param qualityReason The reason for the quality rating.
 * @param cost The cost rating.
 * @param costReason The reason for the cost rating.
 * @param reliability The reliability rating.
 * @param reliabilityReason The reason for the reliability rating.
 * @param availability The availability rating.
 * @param availabilityReason The reason for the availability rating.
 * @param totalScore The total rating score.
 * @param ratingComment A general comment about the rating.
 * @param id The openBIS PermID of the rating.
 * @param code The openBIS Code of the rating.
 * @param orderId The openBIS permID of the order this rating belongs to.
 * @param supplierId The openBIS permID of the supplier (Parent of the order).
 * @param supplierName The supplier name (Parent of the order).
 */
public record RatingDto(
        Double quality,
        String qualityReason,

        Double cost,
        String costReason,

        Double reliability,
        String reliabilityReason,

        Double availability,
        String availabilityReason,

        Double totalScore,
        String ratingComment,

        String id,              // OpenBIS PermID
        String code,            // OpenBIS Code

        String orderId,         // PermID of the parent Order

        String supplierId,      // PermID of the parents parent Supplier
        String supplierName     // Name of the parents parent Supplier
) {
}
