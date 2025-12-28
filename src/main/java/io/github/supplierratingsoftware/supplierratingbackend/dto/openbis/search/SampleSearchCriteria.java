package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;
import lombok.Getter;
import lombok.Setter;

/**
 * Defines the criteria for searching Samples in OpenBIS.
 * <p>
 * This is a composite criteria container. You usually start a search by calling
 * {@code SampleSearchCriteria.create().with(...)}.
 * </p>
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *      {
 *          "@type": "as.dto.sample.search.SampleSearchCriteria",
 *          "operator": "AND",
 *          "criteria": [
 *              {
 *                  "@type": "as.dto.sample.search.SampleTypeSearchCriteria",
 *                  "permId": "LIEFERANT"
 *              }
 *          ]
 *      }
 * </pre>
 */
@Getter
@Setter
@JsonTypeName(OpenBisJsonConstants.SAMPLE_SEARCH_CRITERIA)
public class SampleSearchCriteria extends AbstractCompositeSearchCriteria {

    /**
     * Helper to create a new {@link SampleSearchCriteria} instance (fluent interface).
     *
     * @return A new instance of {@link SampleSearchCriteria}.
     */
    public static SampleSearchCriteria create() {
        return new SampleSearchCriteria();
    }

    /**
     * Overridden to return {@link SampleSearchCriteria} instead of the abstract parent,
     * enabling smoother method chaining.
     *
     * @param criteria One or more criteria to add (e.g. {@link SampleTypeSearchCriteria}).
     * @return This instance (as SampleSearchCriteria) for chaining.
     */
    @Override
    public SampleSearchCriteria with(SearchCriteria... criteria) {
        super.with(criteria);
        return this;
    }
}