package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the permanent identifier of an entity type in OpenBIS.
 * Used to reference types like "LIEFERANT" or "ORDER".
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *      {
 *          "@type": "as.dto.entitytype.id.EntityTypePermId",
 *          "permId": "LIEFERANT"
 *      }
 * </pre>
 *
 * <p><strong>Type Info:</strong><br>
 * Jackson handles the @type automatically via the @JsonTypeName annotation.
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("as.dto.entitytype.id.EntityTypePermId")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityTypePermId {
    private String permId;
}
