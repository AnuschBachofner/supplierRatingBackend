package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;

/**
 * Criteria for searching samples based on their parent sample in openBIS.
 * <p>
 * This is a composite criteria container. You usually start a search by calling
 * {@code SampleParentsSearchCriteria.withParentId(String)} to create a criteria for filtering by parent PermId.
 * </p>
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "@type": "as.dto.sample.search.SampleParentsSearchCriteria",
 *         "operator": "AND",
 *         "criteria": [
 *             {
 *                 "@type": "as.dto.common.search.PermIdSearchCriteria",
 *                 "fieldValue": {
 *                     "@type": "as.dto.common.search.StringEqualToValue",
 *                     "value": "TARGET_SUPPLIER_PERMID"
 *                 }
 *             }
 *         ]
 *     }
 * </pre>
 *
 * Fields with a {@code null} value are ignored during serialization.
 *
 * @see AbstractCompositeSearchCriteria
 * @see PermIdSearchCriteria
 *
 */
@JsonTypeName(OpenBisJsonConstants.SAMPLE_PARENTS_SEARCH_CRITERIA)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleParentsSearchCriteria extends AbstractCompositeSearchCriteria {

    public SampleParentsSearchCriteria() {
        super();
    }

    /**
     * Static helper to create a criteria for filtering by parent PermId
     * @param parentPermId The PermId of the parent sample
     * @return The configured search criteria
     */
    public static SampleParentsSearchCriteria withParentId(String parentPermId) {
        SampleParentsSearchCriteria criteria = new SampleParentsSearchCriteria();
        criteria.with(PermIdSearchCriteria.withId(parentPermId));
        return criteria;
    }
}
