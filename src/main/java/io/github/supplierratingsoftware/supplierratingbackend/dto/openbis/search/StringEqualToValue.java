package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an exact string match value wrapper.
 * Used inside criteria like {@link CodeSearchCriteria}.
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "@type": "as.dto.common.search.StringEqualToValue",
 *         "value": "LIEFERANT"
 *     }
 * </pre>
 * <p>
 * Fields with a {@code null} value are ignored during serialization.
 *
 * <p><strong>Type Info:</strong><br>
 * The {@code @JsonTypeName} annotation specifies the JSON type name for serialization and deserialization.
 * Type info is needed because openBIS allows different matchers (StartsWith, EndsWith, etc.)
 * Jackson handles the @type automatically via the @JsonTypeName.
 * </p>
 *
 * @see CodeSearchCriteria
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("as.dto.common.search.StringEqualToValue")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StringEqualToValue {

    private String value;
}