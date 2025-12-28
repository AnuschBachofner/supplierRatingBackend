package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;

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
@JsonTypeName(OpenBisJsonConstants.SAMPLE_TYPE_FETCH_OPTIONS)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = OpenBisJsonConstants.TYPE_JSON_PROPERTY_KEY)
public record SampleTypeFetchOptions() {
}
