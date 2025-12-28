package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Represents the code of an openBIS entity type from the openBIS V3 API.
 * (openBIS response)
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "code": "ENTITY_TYPE_CODE"
 *     }
 * </pre>
 *
 * Fields with a {@code null} value are ignored during serialization.
 * Unknown fields (such as {@code @type} or {@code @id} are ignored during deserialization.
 *
 * @param code The code of the entity type.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public record OpenBisEntityType(String code) {
}
