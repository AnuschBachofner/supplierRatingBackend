package io.github.supplierratingsoftware.supplierratingbackend.mapper;

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
                props.get("NAME"),
                props.get("KUNDENNUMMER"),
                props.get("LIEFERANTEN_ZUSATZ"),
                props.get("LIEFERANTEN_STRASSE"),
                props.get("LIEFERANTEN_POSTFACH"),
                props.get("LIEFERANTEN_LAND"),
                props.get("LIEFERANTEN_POSTLEITZAHL"),
                props.get("LIEFERANTEN_ORT"),
                props.get("LIEFERANTEN_WEBLINK"),
                props.get("LIEFERANTEN_EMAIL"),
                props.get("LIEFERANTEN_TELEFON"),
                props.get("MWST"),
                props.get("KONDITIONEN"),
                props.get("KUNDENINFORMATION"),
                sample.permId() != null ? sample.permId().permId() : null,
                sample.code(),
                null // TODO: implement SupplierStats, once it's available
        );
    }
}
