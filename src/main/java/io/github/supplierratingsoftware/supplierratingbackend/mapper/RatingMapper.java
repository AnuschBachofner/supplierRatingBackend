package io.github.supplierratingsoftware.supplierratingbackend.mapper;

import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisSchemaConstants;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.RatingDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Component responsible for mapping technical OpenBIS samples to the domain-specific {@link RatingDto}.
 * <p>
 * It handles the extraction of property values from the generic string-map (e.g. "QUALITAET" -> "qualityRating")
 * and ensures null-safety during the transformation.
 * </p>
 */
@Component
public class RatingMapper {

    private static final Logger log = LoggerFactory.getLogger(RatingMapper.class);

    /**
     * Converts a generic {@link OpenBisSample} into a {@link RatingDto}.
     *
     * @param sample The raw sample object from OpenBIS. Can be null.
     * @return The domain-specific rating DTO, or null if the input sample is null.
     */
    public RatingDto toDto(OpenBisSample sample) {
        if (sample == null) {
            return null;
        }

        Map<String, String> properties = sample.properties();
        if (properties == null) {
            properties = Map.of();
        }

        // Extract Parent Order ID (Foreign Key)
        String orderId = null;
        List<OpenBisSample> parents = sample.parents();
        if (parents != null && !parents.isEmpty()) {
            OpenBisSample parentOrder = parents.get(0);
            if (parentOrder != null && parentOrder.permId() != null) {
                orderId = parentOrder.permId().permId();
            }
        }

        return new RatingDto(
                getDoubleProperty(properties, OpenBisSchemaConstants.QUALITY_RATING_PROPERTY),
                properties.get(OpenBisSchemaConstants.QUALITY_REASON_RATING_PROPERTY),

                getDoubleProperty(properties, OpenBisSchemaConstants.COST_RATING_PROPERTY),
                properties.get(OpenBisSchemaConstants.COST_REASON_RATING_PROPERTY),

                getDoubleProperty(properties, OpenBisSchemaConstants.RELIABILITY_RATING_PROPERTY),
                properties.get(OpenBisSchemaConstants.RELIABILITY_REASON_RATING_PROPERTY),

                getDoubleProperty(properties, OpenBisSchemaConstants.AVAILABILITY_RATING_PROPERTY),
                properties.get(OpenBisSchemaConstants.AVAILABILITY_REASON_RATING_PROPERTY),

                getDoubleProperty(properties, OpenBisSchemaConstants.TOTAL_SCORE_RATING_PROPERTY),

                properties.get(OpenBisSchemaConstants.RATING_COMMENT_RATING_PROPERTY),

                sample.permId() != null ? sample.permId().permId() : null,
                sample.code(),
                orderId,
                null, //TODO: populate supplierId, once supplier reference is available
                null //TODO: populate supplierName, once supplier reference is available
        );
    }

    /**
     * Helper method to safely extract and parse a Double property.
     * Logs a warning if the value exists but is not a valid number.
     *
     * @param properties The property map.
     * @param key        The key of the property to extract.
     * @return The parsed Double, or null if missing or invalid.
     */
    private Double getDoubleProperty(Map<String, String> properties, String key) {
        if (properties == null || key == null) {
            return null;
        }

        String value = properties.get(key);
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            log.warn("Data Quality Issue: Failed to parse property '{}' with value '{}' to Double. Returning null.", key, value);
            return null;
        }
    }
}