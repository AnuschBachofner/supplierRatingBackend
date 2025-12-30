package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
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
@JsonTypeName(OpenBisJsonConstants.PERM_ID_SEARCH_CRITERIA)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermIdSearchCriteria implements SearchCriteria {

    private StringEqualToValue fieldValue;

    /**
     * Constructor.
     * Creates a PermIdSearchCriteria with the given PermID.
     *
     * @param value The PermID to search for.
     */
    public PermIdSearchCriteria(String value) {
        this.fieldValue = new StringEqualToValue(value);
    }

    /**
     * Factory method to create a PermIdSearchCriteria with the given PermID.
     *
     * @param id The PermID to search for.
     * @return A PermIdSearchCriteria instance.
     */
    public static PermIdSearchCriteria withId(String id) {
        return new PermIdSearchCriteria(id);
    }
}
