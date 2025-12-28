package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * NOTE: file is not in use yet. But will be used in the future, when we build other Endpoints, such as {@code GET /suppliers/{id}}
 * TODO: implement PermIdSearchCriteria or remove it.
 * Criterion to search for an entity by its PermId.
 * Used inside SampleTypeSearchCriteria instead of CodeSearchCriteria.
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *      {
 *          "@type": "as.dto.common.search.PermIdSearchCriteria",
 *          "fieldValue": {
 *              "@type": "as.dto.common.search.StringEqualToValue",
 *              "value": "LIEFERANT"
 *              }
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
@JsonTypeName("as.dto.common.search.PermIdSearchCriteria")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermIdSearchCriteria implements SearchCriteria {

    private StringEqualToValue fieldValue;

    public PermIdSearchCriteria(String value) {
        this.fieldValue = new StringEqualToValue(value);
    }

    public static PermIdSearchCriteria withId(String id) {
        return new PermIdSearchCriteria(id);
    }
}
