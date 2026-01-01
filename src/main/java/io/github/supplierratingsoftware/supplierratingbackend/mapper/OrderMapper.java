package io.github.supplierratingsoftware.supplierratingbackend.mapper;

import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisSchemaConstants;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSample;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Component responsible for mapping technical OpenBIS samples to the domain-specific {@link OrderDto} and vice versa.
 * <p>
 * Handles both directions:
 * <ul>
 *     <li>READ: {@link OpenBisSample} -> {@link OrderDto}</li>
 *     <li>WRITE: TODO: implement WRITE later</li>
 * </ul>
 * </p>
 */
@Component
public class OrderMapper {

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
}
