package io.github.supplierratingsoftware.supplierratingbackend.mapper;

import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisSchemaConstants;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.RatingDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSample;
import io.github.supplierratingsoftware.supplierratingbackend.util.OpenBisUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Component responsible for mapping technical OpenBIS samples to the domain-specific {@link RatingDto} and vice versa.
 * <p>
 * Handles both directions:
 * <ul>
 *     <li>READ: {@link OpenBisSample} -> {@link RatingDto}</li>
 *     <li>WRITE: TODO: implement WRITE later</li>
 * </ul>
 * </p>
 */
@Component
public class RatingMapper {

    /**
     * Converts a generic {@link OpenBisSample} into a {@link RatingDto}.
     * (READ direction)
     *
     * @param sample The raw sample object from OpenBIS. Can be null.
     * @return The domain-specific rating DTO, or null if the input sample is null.
     */
    public RatingDto toApiDto(OpenBisSample sample) {
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
                OpenBisUtils.parseDoubleOrNull(
                        properties.get(OpenBisSchemaConstants.QUALITY_RATING_PROPERTY),
                        OpenBisSchemaConstants.QUALITY_RATING_PROPERTY,
                        sample.code()
                ),
                properties.get(OpenBisSchemaConstants.QUALITY_REASON_RATING_PROPERTY),

                OpenBisUtils.parseDoubleOrNull(
                        properties.get(OpenBisSchemaConstants.COST_RATING_PROPERTY),
                        OpenBisSchemaConstants.COST_RATING_PROPERTY,
                        sample.code()
                ),
                properties.get(OpenBisSchemaConstants.COST_REASON_RATING_PROPERTY),

                OpenBisUtils.parseDoubleOrNull(
                        properties.get(OpenBisSchemaConstants.RELIABILITY_RATING_PROPERTY),
                        OpenBisSchemaConstants.RELIABILITY_RATING_PROPERTY,
                        sample.code()
                ),
                properties.get(OpenBisSchemaConstants.RELIABILITY_REASON_RATING_PROPERTY),

                OpenBisUtils.parseDoubleOrNull(
                        properties.get(OpenBisSchemaConstants.AVAILABILITY_RATING_PROPERTY),
                        OpenBisSchemaConstants.AVAILABILITY_RATING_PROPERTY,
                        sample.code()
                ),
                properties.get(OpenBisSchemaConstants.AVAILABILITY_REASON_RATING_PROPERTY),

                OpenBisUtils.parseDoubleOrNull(
                        properties.get(OpenBisSchemaConstants.TOTAL_SCORE_RATING_PROPERTY),
                        OpenBisSchemaConstants.TOTAL_SCORE_RATING_PROPERTY,
                        sample.code()
                ),

                properties.get(OpenBisSchemaConstants.RATING_COMMENT_RATING_PROPERTY),

                sample.permId() != null ? sample.permId().permId() : null,
                sample.code(),
                orderId,
                null, //TODO: populate supplierId, once supplier reference is available
                null //TODO: populate supplierName, once supplier reference is available
        );
    }
}