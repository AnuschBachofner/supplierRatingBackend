package io.github.supplierratingsoftware.supplierratingbackend.mapper;

import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisSchemaConstants;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.SupplierDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSample;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Component responsible for mapping technical OpenBIS samples to the domain-specific {@link SupplierDto}.
 * <p>
 * It handles the extraction of property values from the generic string-map (e.g. "LIEFERANTEN_ORT" -> "city")
 * and ensures null-safety during the transformation.
 * </p>
 */
@Component
public class SupplierMapper {

    /**
     * Converts a generic {@link OpenBisSample} into a {@link SupplierDto}.
     *
     * @param sample The raw sample object from OpenBIS. Can be null.
     * @return The mapped {@link SupplierDto}, or {@code null} if the input sample was null.
     */
    public SupplierDto toDto(OpenBisSample sample) {
        if (sample == null) return null;

        Map<String, String> props = sample.properties(); // Access the Map at the record.

        if (props == null) props = Map.of();

        return new SupplierDto(
                props.get(OpenBisSchemaConstants.NAME_SUPPLIER_PROPERTY),
                props.get(OpenBisSchemaConstants.CUSTOMER_NUMBER_SUPPLIER_PROPERTY),
                props.get(OpenBisSchemaConstants.ADDITION_SUPPLIER_PROPERTY),
                props.get(OpenBisSchemaConstants.STREET_SUPPLIER_PROPERTY),
                props.get(OpenBisSchemaConstants.PO_BOX_SUPPLIER_PROPERTY),
                props.get(OpenBisSchemaConstants.COUNTRY_SUPPLIER_PROPERTY),
                props.get(OpenBisSchemaConstants.ZIP_CODE_SUPPLIER_PROPERTY),
                props.get(OpenBisSchemaConstants.CITY_SUPPLIER_PROPERTY),
                props.get(OpenBisSchemaConstants.WEBSITE_SUPPLIER_PROPERTY),
                props.get(OpenBisSchemaConstants.EMAIL_SUPPLIER_PROPERTY),
                props.get(OpenBisSchemaConstants.PHONE_NUMBER_SUPPLIER_PROPERTY),
                props.get(OpenBisSchemaConstants.VAT_ID_SUPPLIER_PROPERTY),
                props.get(OpenBisSchemaConstants.CONDITIONS_SUPPLIER_PROPERTY),
                props.get(OpenBisSchemaConstants.CUSTOMER_INFO_SUPPLIER_PROPERTY),
                sample.permId() != null ? sample.permId().permId() : null,
                sample.code(),
                null // TODO: implement SupplierStats, once it's available
        );
    }
}
