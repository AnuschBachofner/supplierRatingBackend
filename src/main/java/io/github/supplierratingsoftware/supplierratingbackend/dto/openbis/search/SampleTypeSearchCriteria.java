package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Criterion to search for samples of a specific type (e.g. "LIEFERANT").
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *      {
 *          "@type": "as.dto.sample.search.SampleTypeSearchCriteria",
 *          "operator": "AND",
 *          "criteria": [
 *              {
 *                  "@type": "as.dto.common.search.CodeSearchCriteria",
 *                  "fieldValue": {
 *                      "@type": "as.dto.common.search.StringEqualToValue",
 *                      "value": "LIEFERANT"
 *                  }
 *              }
 *          ]
 *      }
 * </pre>
 *
 * <p><strong>Structure:</strong><br>
 * This acts as a composite criteria. To filter by the type's code,
 * we add a {@link CodeSearchCriteria} to the criteria list of this object.
 * </p>
 *
 * Fields with a {@code null} value are ignored during serialization.
 *
 */
@Getter
@Setter
@NoArgsConstructor
@JsonTypeName(OpenBisJsonConstants.SAMPLE_TYPE_SEARCH_CRITERIA)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleTypeSearchCriteria extends AbstractCompositeSearchCriteria {

    /**
     * Helper to create a new SampleTypeSearchCriteria with a specific code (fluent interface).
     *
     * @param codeValue The sample type code (e.g. "LIEFERANT").
     * @return A new SampleTypeSearchCriteria instance configured to match the code.
     */
    public static SampleTypeSearchCriteria withCode(String codeValue) {
        SampleTypeSearchCriteria criteria = new SampleTypeSearchCriteria();
        criteria.with(CodeSearchCriteria.withCode(codeValue));
        return criteria;
    }
}