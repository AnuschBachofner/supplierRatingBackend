package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Represents the Perm Id of an openBIS entity from the openBIS V3 API.
 * (openBIS response)
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "permId": "PERM_ID"
 *     }
 * </pre>
 *
 * Fields with a {@code null} value are ignored during serialization.
 * Unknown fields (such as {@code @type} or {@code @id} are ignored during deserialization.
 *
 * @param permId The perm id of the entity.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenBisPermId(String permId) {
}
