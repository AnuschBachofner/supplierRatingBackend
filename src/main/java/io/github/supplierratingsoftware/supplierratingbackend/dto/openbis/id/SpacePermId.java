package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;

/**
 * Represents a space permId from the openBIS V3 API.
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "@type": "as.dto.space.id.SpacePermId",
 *         "permId": "SPACE"
 *     }
 * </pre>
 *
 * Fields with a {@code null} value are ignored during serialization.
 *
 * @param permId The perm id of the space.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName(OpenBisJsonConstants.SPACE_PERM_ID)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = OpenBisJsonConstants.TYPE_JSON_PROPERTY_KEY)
public record SpacePermId(String permId) {
}
