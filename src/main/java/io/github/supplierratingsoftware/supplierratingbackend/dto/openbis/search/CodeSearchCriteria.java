package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Criterion to search for an entity by its Code.
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *      {
 *          "@type": "as.dto.common.search.CodeSearchCriteria",
 *          "fieldValue": {
 *              "@type": "as.dto.common.search.StringEqualToValue",
 *              "value": "MY_CODE"
 *          }
 *      }
 * </pre>
 *
 * Fields with a {@code null} value are ignored during serialization.
 *
 * @see StringEqualToValue
 */
@Getter
@Setter
@NoArgsConstructor
@JsonTypeName("as.dto.common.search.CodeSearchCriteria")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CodeSearchCriteria implements SearchCriteria {
    private StringEqualToValue fieldValue;

    /**
     * Constructor.
     * @param value The code to search for (exact match).
     */
    public CodeSearchCriteria(String value) {
        this.fieldValue = new StringEqualToValue(value);
    }

    /**
     * Static helper to create a code criteria.
     * @param code The code to search for (exact match).
     * @return The criteria object.
     */
    public static CodeSearchCriteria withCode(String code) {
        return new CodeSearchCriteria(code);
    }
}
