package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

/**
 * Criterion to search for samples within a specific Space (e.g. "LIEFERANTENBEWERTUNG").
 * <p>
 * According to the OpenBIS 6.x API logs, this is a Composite Search Criteria.
 * It contains a list of sub-criteria (e.g. CodeSearchCriteria) to identify the space.
 * </p>
 */
@Getter
@Setter
@JsonTypeName("as.dto.space.search.SpaceSearchCriteria")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpaceSearchCriteria extends AbstractCompositeSearchCriteria {

    /**
     * Static helper to create a Space criteria that filters by Code.
     * Validated against OpenBIS 6.x web UI payloads.
     *
     * Structure: SpaceSearchCriteria -> criteria list -> CodeSearchCriteria -> "VALUE"
     *
     * @param spaceCode The code of the space (e.g. "LIEFERANTENBEWERTUNG")
     * @return The configured criteria.
     */
    public static SpaceSearchCriteria withCode(String spaceCode) {
        SpaceSearchCriteria spaceCriteria = new SpaceSearchCriteria();
        spaceCriteria.with(CodeSearchCriteria.withCode(spaceCode));
        return spaceCriteria;
    }
}