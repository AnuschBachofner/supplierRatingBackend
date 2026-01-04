package io.github.supplierratingsoftware.supplierratingbackend.mapper;

import io.github.supplierratingsoftware.supplierratingbackend.config.OpenBisProperties;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisSchemaConstants;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderCreationDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.creation.SampleCreation;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id.*;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSample;
import io.github.supplierratingsoftware.supplierratingbackend.exception.OpenBisIntegrationException;
import io.github.supplierratingsoftware.supplierratingbackend.util.OpenBisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Component responsible for mapping technical OpenBIS samples to the domain-specific {@link OrderDto} and vice versa.
 * <p>
 * Handles both directions:
 * <ul>
 *     <li>READ: {@link OpenBisSample} -> {@link OrderDto}</li>
 *     <li>WRITE: {@link OrderCreationDto} -> {@link SampleCreation}</li>
 * </ul>
 * </p>
 */
@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OpenBisProperties properties;

    /**
     * Converts a generic {@link OpenBisSample} into a {@link OrderDto}.
     * (READ direction)
     *
     * @param sample The raw sample object from OpenBIS. Can be null.
     * @return The DTO representation of the order, or null if the sample is null.
     */
    public OrderDto toApiDto(OpenBisSample sample) {
        if (sample == null) return null;

        Map<String, String> props = sample.properties();

        if (props == null) props = Map.of();

        String supplierId = null;
        if (sample.parents() != null && !sample.parents().isEmpty()) {
            OpenBisSample parent = sample.parents().get(0);
            if (parent != null && parent.permId() != null) {
                supplierId = parent.permId().permId();
            }
        }

        return new OrderDto(
                props.get(OpenBisSchemaConstants.NAME_ORDER_PROPERTY),
                props.get(OpenBisSchemaConstants.MAIN_CATEGORY_ORDER_PROPERTY),
                props.get(OpenBisSchemaConstants.SUB_CATEGORY_ORDER_PROPERTY),
                props.get(OpenBisSchemaConstants.DESCRIPTION_ORDER_PROPERTY),
                props.get(OpenBisSchemaConstants.FREQUENCY_ORDER_PROPERTY),
                props.get(OpenBisSchemaConstants.CONTACT_NAME_ORDER_PROPERTY),
                props.get(OpenBisSchemaConstants.CONTACT_EMAIL_ORDER_PROPERTY),
                props.get(OpenBisSchemaConstants.CONTACT_PHONE_ORDER_PROPERTY),
                props.get(OpenBisSchemaConstants.ORDER_REASON_ORDER_PROPERTY),
                props.get(OpenBisSchemaConstants.ORDER_METHOD_ORDER_PROPERTY),
                props.get(OpenBisSchemaConstants.PURCHASER_ORDER_PROPERTY),
                props.get(OpenBisSchemaConstants.ORDER_DATE_ORDER_PROPERTY),
                props.get(OpenBisSchemaConstants.DELIVERY_DATE_ORDER_PROPERTY),
                props.get(OpenBisSchemaConstants.ORDER_COMMENT_ORDER_PROPERTY),
                sample.permId() != null ? sample.permId().permId() : null,
                sample.code(),
                null, //TODO: to be implemented by business logic (if there is any rating, then ...)
                supplierId,
                null, //TODO: to be implemented, when search for single supplier is implemented
                null //TODO: to be implemented, when rating logic is implemented
        );
    }

    /**
     * Converts an {@link OrderCreationDto} into an openBIS {@link SampleCreation} object.
     * (WRITE Direction)
     * <p>
     * Generates a unique code: BESTELLUNG-<UUID>
     * Assigns technical identifiers (Space, Project, Experiment).
     * <strong>Crucial:</strong> Links the order to its parent supplier via {@code parentIds}.
     * </p>
     *
     * @param dto The creation request from the API.
     * @return The openBIS creation payload.
     */
    public SampleCreation toOpenBisCreation(OrderCreationDto dto) {
        // Generate unique code: BESTELLUNG-<UUID>
        String code = properties.order().typeCode() + "-" + UUID.randomUUID();

        // Prepare Identifiers
        EntityTypePermId typeId = new EntityTypePermId(properties.order().typeCode());
        SpacePermId spaceId = new SpacePermId(properties.defaultSpace());

        ProjectIdentifier projectId = new ProjectIdentifier(
                OpenBisUtils.buildIdentifier(properties.defaultSpace(), properties.order().projectCode())
        );

        ExperimentIdentifier experimentId = new ExperimentIdentifier(
                OpenBisUtils.buildIdentifier(
                        properties.defaultSpace(),
                        properties.order().projectCode(),
                        properties.order().collectionCode()
                )
        );

        // Map Properties
        Map<String, String> props = new HashMap<>();

        // Mandatory
        props.put(OpenBisSchemaConstants.NAME_ORDER_PROPERTY, dto.name());
        props.put(OpenBisSchemaConstants.MAIN_CATEGORY_ORDER_PROPERTY, dto.mainCategory());
        props.put(OpenBisSchemaConstants.SUB_CATEGORY_ORDER_PROPERTY, dto.subCategory());
        props.put(OpenBisSchemaConstants.ORDER_REASON_ORDER_PROPERTY, dto.reason());
        props.put(OpenBisSchemaConstants.PURCHASER_ORDER_PROPERTY, dto.orderedBy());
        props.put(OpenBisSchemaConstants.ORDER_DATE_ORDER_PROPERTY, dto.orderDate());

        // Optional
        OpenBisUtils.putIfNotNull(props, OpenBisSchemaConstants.DESCRIPTION_ORDER_PROPERTY, dto.details());
        OpenBisUtils.putIfNotNull(props, OpenBisSchemaConstants.FREQUENCY_ORDER_PROPERTY, dto.frequency());
        OpenBisUtils.putIfNotNull(props, OpenBisSchemaConstants.CONTACT_NAME_ORDER_PROPERTY, dto.contactPerson());
        OpenBisUtils.putIfNotNull(props, OpenBisSchemaConstants.CONTACT_EMAIL_ORDER_PROPERTY, dto.contactEmail());
        OpenBisUtils.putIfNotNull(props, OpenBisSchemaConstants.CONTACT_PHONE_ORDER_PROPERTY, dto.contactPhone());
        OpenBisUtils.putIfNotNull(props, OpenBisSchemaConstants.ORDER_METHOD_ORDER_PROPERTY, dto.orderMethod());
        OpenBisUtils.putIfNotNull(props, OpenBisSchemaConstants.DELIVERY_DATE_ORDER_PROPERTY, dto.deliveryDate());
        OpenBisUtils.putIfNotNull(props, OpenBisSchemaConstants.ORDER_COMMENT_ORDER_PROPERTY, dto.orderComment());

        // Link Parent (Supplier)
        List<SamplePermId> parentIds = null;
        parentIds = List.of(new SamplePermId(dto.supplierId()));

        return new SampleCreation(
                spaceId,
                projectId,
                experimentId,
                typeId,
                code,
                props,
                parentIds // <-- Linked Supplier
        );
    }
}
