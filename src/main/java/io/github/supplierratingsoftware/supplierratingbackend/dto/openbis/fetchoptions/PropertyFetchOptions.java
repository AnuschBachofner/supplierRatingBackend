package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;

/**
 * Representation of fetch options for properties in OpenBIS V3 API.
 * Empty Record (Marker)
 *
 * <p><strong>Purpose:</strong><br>
 * Tells openBIS to fetch the properties of a sample.
 * </p>
 *
 * <p><strong>JSON Example</strong></p>
 * <pre>
 *     {
 *         "@type": "as.dto.property.fetchoptions.PropertyFetchOptions"
 *     }
 * </pre>
 *
 * @see SampleFetchOptions
 */
@JsonTypeName(OpenBisJsonConstants.PROPERTY_FETCH_OPTIONS)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = OpenBisJsonConstants.TYPE_JSON_PROPERTY_KEY)
public record PropertyFetchOptions() {
}
