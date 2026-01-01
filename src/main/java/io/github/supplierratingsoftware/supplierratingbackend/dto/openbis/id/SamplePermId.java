package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;

/**
 * Represents a sample permId from the openBIS V3 API.
 * (openBIS response)
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "@type": "as.dto.sample.id.SamplePermId",
 *         "permId": "20251231103114886-315"
 *     }
 * </pre>
 *
 * Unknown fields (such as {@code @type} or {@code @id} are ignored during deserialization.)
 *
 * @param permId The perm id of the sample.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName(OpenBisJsonConstants.SAMPLE_PERM_ID)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = OpenBisJsonConstants.TYPE_JSON_PROPERTY_KEY)
public record SamplePermId(String permId) {
}