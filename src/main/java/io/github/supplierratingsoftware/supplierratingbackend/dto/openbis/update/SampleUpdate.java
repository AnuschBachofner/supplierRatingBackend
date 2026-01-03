package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id.SamplePermId;

import java.util.Map;

/**
 * Represents an update request for a sample in OpenBIS (openBIS V3 API).
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "@type": "as.dto.sample.update.SampleUpdate",
 *         "sampleId": { ... },
 *         "properties": { ... }
 *     }
 * </pre>
 *
 * @param sampleId The sample identifier (openBIS Perm ID) to update.
 * @param properties The properties to update for the sample.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName(OpenBisJsonConstants.SAMPLE_UPDATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = OpenBisJsonConstants.TYPE_JSON_PROPERTY_KEY)
public record SampleUpdate(
        SamplePermId sampleId,
        Map<String, String> properties
) {
}
