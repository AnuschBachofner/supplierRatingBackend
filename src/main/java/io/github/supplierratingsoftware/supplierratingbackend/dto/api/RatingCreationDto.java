package io.github.supplierratingsoftware.supplierratingbackend.dto.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a request to create a new Rating for an Order.
 * Matches API v1.3.0 specifications.
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *     <li>Scores (quality, cost, reliability) are mandatory and must be between 1-5.</li>
 *     <li>Reasons for mandatory scores are required.</li>
 *     <li>Availability is optional (validation context dependent).</li>
 * </ul>
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "orderId": "ORDER_ID",
 *         "quality": 5,
 *         "qualityReason": "Excellent quality",
 *         "cost": 3,
 *         "costReason": "Reasonable pricing",
 *         "reliability": 4,
 *         "reliabilityReason": "Highly reliable",
 *         "availability": 5,
 *         "availabilityReason": "Always available",
 *         "ratingComment": "Overall good supplier"
 *     }
 * </pre>
 *
 * @param orderId            The PermID of the order being rated (Mandatory).
 * @param quality            Quality score (1-5) (Mandatory).
 * @param qualityReason      Reason for quality score (Mandatory).
 * @param cost               Cost score (1-5) (Mandatory).
 * @param costReason         Reason for cost score (Mandatory).
 * @param reliability        Reliability score (1-5) (Mandatory).
 * @param reliabilityReason  Reason for reliability score (Mandatory).
 * @param availability       Availability score (1-5) (Optional).
 * @param availabilityReason Reason for availability score (Optional).
 * @param ratingComment      General comment (Optional).
 */
public record RatingCreationDto(
        @NotBlank(message = "Order ID is required")
        String orderId,

        @NotNull(message = "Quality score is required")
        @Min(value = 1, message = "Score must be at least 1")
        @Max(value = 5, message = "Score must be at most 5")
        Integer quality,

        @NotBlank(message = "Reason for quality rating is required")
        String qualityReason,

        @NotNull(message = "Cost score is required")
        @Min(value = 1, message = "Score must be at least 1")
        @Max(value = 5, message = "Score must be at most 5")
        Integer cost,

        @NotBlank(message = "Reason for cost rating is required")
        String costReason,

        @NotNull(message = "Reliability score is required")
        @Min(value = 1, message = "Score must be at least 1")
        @Max(value = 5, message = "Score must be at most 5")
        Integer reliability,

        @NotBlank(message = "Reason for reliability rating is required")
        String reliabilityReason,

        @Min(value = 1, message = "Score must be at least 1")
        @Max(value = 5, message = "Score must be at most 5")
        Integer availability,

        String availabilityReason,

        String ratingComment
) {
}