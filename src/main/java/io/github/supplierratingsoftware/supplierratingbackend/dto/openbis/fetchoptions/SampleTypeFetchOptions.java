package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Representation of fetch options for entity types in OpenBIS V3 API.
 * Empty Record (Marker)
 *
 * <p><strong>Purpose:</strong><br>
 * Tells openBIS to fetch the entity's type information.
 * </p>
 *
 * <p><strong>JSON Example</strong></p>
 * <pre>
 *     {
 *         "@type": "as.dto.sample.fetchoptions.SampleTypeFetchOptions"
 *     }
 * </pre>
 *
 * @see SampleFetchOptions
 */
@JsonTypeName("as.dto.sample.fetchoptions.SampleTypeFetchOptions")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public record SampleTypeFetchOptions() {
}
