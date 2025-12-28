package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
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
 *          "permId": "LIEFERANT"
 *      }
 * </pre>
 *
 * Fields with a {@code null} value are ignored during serialization.
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("as.dto.sample.search.SampleTypeSearchCriteria")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleTypeSearchCriteria implements SearchCriteria {

    private CodeSearchCriteria code;

    /**
     * Helper to create a new SampleTypeSearchCriteria with a specific code (fluent interface).
     *
     * @param codeValue The sample type code (e.g. "LIEFERANT").
     * @return A new SampleTypeSearchCriteria instance.
     */
    public static SampleTypeSearchCriteria withCode(String codeValue) {
        return new SampleTypeSearchCriteria(CodeSearchCriteria.withCode(codeValue));
    }
}