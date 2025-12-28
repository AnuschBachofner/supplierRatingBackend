package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract base class for search criteria that act as containers for other criteria.
 * <p>
 * This implements the <b>Composite Pattern</b> used by OpenBIS (a design pattern
 * where a criteria object acts as a container holding a collection of other criteria objects,
 * allowing for complex, nested logical queries like "Match A AND Match B"). Instead of a single value,
 * these objects contain a list of sub-criteria and an operator (AND, OR).
 * </p>
 *
 * <p><strong>Example Logic:</strong><br>
 * If you want to find a Supplier (Type=LIEFERANT) <b>AND</b> with Name="Acme",
 * this class holds the list containing both the Type-Criterion and the Property-Criterion.
 * </p>
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "@type": "as.dto.sample.search.SampleSearchCriteria",
 *         "operator": "AND",
 *         "criteria": []
 *     }
 * </pre>
 *
 * Fields with a {@code null} value are ignored during serialization.
 *
 * @see SampleTypeSearchCriteria
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractCompositeSearchCriteria implements SearchCriteria {

    private String operator = "AND";
    private List<SearchCriteria> criteria = new ArrayList<>();

    /**
     * Fluent API helper to add sub-criteria.
     *
     * @param criteria One or more criteria to add (e.g. {@link SampleTypeSearchCriteria}).
     * @return This instance (as AbstractCompositeSearchCriteria) for chaining.
     */
    public AbstractCompositeSearchCriteria with(SearchCriteria... criteria) {
        if (criteria != null) {
            this.criteria.addAll(Arrays.asList(criteria));
        }
        return this;
    }
}