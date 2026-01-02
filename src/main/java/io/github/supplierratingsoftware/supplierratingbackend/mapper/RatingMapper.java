package io.github.supplierratingsoftware.supplierratingbackend.mapper;

import io.github.supplierratingsoftware.supplierratingbackend.config.OpenBisProperties;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisSchemaConstants;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.RatingCreationDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.RatingDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.creation.SampleCreation;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id.EntityTypePermId;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id.ExperimentIdentifier;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id.ProjectIdentifier;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id.SamplePermId;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id.SpacePermId;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSample;
import io.github.supplierratingsoftware.supplierratingbackend.util.OpenBisUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

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
@RequiredArgsConstructor
public class RatingMapper {

    private static final Logger log = LoggerFactory.getLogger(RatingMapper.class);
    private final OpenBisProperties properties;

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

    /**
     * Converts a {@link RatingCreationDto} into an openBIS {@link SampleCreation} object.
     * (WRITE direction)
     * <p>
     * Generate unique code: <TYPE_CODE>-<UUID> (e.g. BESTELLBEWERTUNG-<UUID>).
     * and assigns technical identifiers (Space, Project, Experiment).
     * </p>
     *
     * @param dto The rating creation DTO to convert.
     * @return The openBIS creation payload.
     */
    public SampleCreation toOpenBisCreation(RatingCreationDto dto) {
        // Generate unique code: BESTELLBEWERTUNG-<UUID>
        String code = properties.rating().typeCode() + "-" + UUID.randomUUID();

        // Prepare Identifiers
        EntityTypePermId typeId = new EntityTypePermId(properties.rating().typeCode());
        SpacePermId spaceId = new SpacePermId(properties.defaultSpace());
        ProjectIdentifier projectId = new ProjectIdentifier(
                OpenBisUtils.buildIdentifier(properties.defaultSpace(), properties.rating().projectCode())
        );
        ExperimentIdentifier experimentId = new ExperimentIdentifier(
                OpenBisUtils.buildIdentifier(
                        properties.defaultSpace(),
                        properties.rating().projectCode(),
                        properties.rating().collectionCode()
                )
        );

        // Map Properties
        Map<String, String> props = new HashMap<>();

        // Mandatory fields
        props.put(OpenBisSchemaConstants.QUALITY_RATING_PROPERTY, String.valueOf(dto.quality()));
        props.put(OpenBisSchemaConstants.QUALITY_REASON_RATING_PROPERTY, dto.qualityReason());
        props.put(OpenBisSchemaConstants.COST_RATING_PROPERTY, String.valueOf(dto.cost()));
        props.put(OpenBisSchemaConstants.COST_REASON_RATING_PROPERTY, dto.costReason());
        props.put(OpenBisSchemaConstants.RELIABILITY_RATING_PROPERTY, String.valueOf(dto.reliability()));
        props.put(OpenBisSchemaConstants.RELIABILITY_REASON_RATING_PROPERTY, dto.reliabilityReason());

        // Optional fields
        if (dto.availability() != null) { // check manually since dto.availability() is an Integer and not the String we would pass to putIfNotNull.
            props.put(OpenBisSchemaConstants.AVAILABILITY_RATING_PROPERTY, String.valueOf(dto.availability()));
        }
        OpenBisUtils.putIfNotNull(props, OpenBisSchemaConstants.AVAILABILITY_REASON_RATING_PROPERTY, dto.availabilityReason());
        OpenBisUtils.putIfNotNull(props, OpenBisSchemaConstants.RATING_COMMENT_RATING_PROPERTY, dto.ratingComment());

        // Calculate total score
        double totalScore = calculateTotalScore(dto);
        props.put(OpenBisSchemaConstants.TOTAL_SCORE_RATING_PROPERTY, String.valueOf(totalScore));

        // Link Parent (Order)
        List<SamplePermId> parentIds = List.of(new SamplePermId(dto.orderId()));

        return new SampleCreation(
                spaceId,
                projectId,
                experimentId,
                typeId,
                code,
                props,
                parentIds
        );
    }

    /**
     * Helper method to calculate the total score from the given rating DTO.
     * Formula: (Quality + Cost + Reliability + [Availability]) / Count
     *
     * @param dto The rating DTO to calculate the total score for.
     * @return The calculated total score.
     */
    private double calculateTotalScore(RatingCreationDto dto) {
        Stream.Builder<Integer> streamBuilder = Stream.builder();
        streamBuilder.add(dto.quality());
        streamBuilder.add(dto.cost());
        streamBuilder.add(dto.reliability());

        if (dto.availability() != null) {
            streamBuilder.add(dto.availability());
        }

        double totalScore = streamBuilder.build()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        // Round to 2 decimal places
        totalScore = Math.round(totalScore * 100.0) / 100.0;

        log.debug("Calculated total score: {} for order {}", totalScore, dto.orderId());
        return totalScore;
    }
}