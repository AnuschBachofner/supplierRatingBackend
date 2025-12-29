package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;

/**
 * Represents fetch options for samples in OpenBIS V3 API.
 *
 * <p><strong>Purpose:</strong><br>
 * Tells openBIS to fetch a sample along with its properties and type information.
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "@type": "as.dto.sample.fetchoptions.SampleFetchOptions",
 *         "properties": { ... },
 *         "type": { ... }
 *     }
 *
 * Fields with a {@code null} value are ignored during serialization.
 * </pre>
 *
 * @param properties The fetch options for sample properties
 * @param type The fetch options for sample type
 * @see PropertyFetchOptions
 * @see SampleTypeFetchOptions
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName(OpenBisJsonConstants.SAMPLE_FETCH_OPTIONS)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = OpenBisJsonConstants.TYPE_JSON_PROPERTY_KEY)
public record SampleFetchOptions(PropertyFetchOptions properties, SampleTypeFetchOptions type, SampleFetchOptions parents) {
}
