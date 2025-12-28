package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

/**
 * Criterion to search for samples within a specific Project (e.g. "LIEFERANTEN").
 * <p>
 * Validated against OpenBIS 6.x web UI payloads.
 * Structure: ProjectSearchCriteria -> criteria list -> CodeSearchCriteria -> "VALUE"
 * </p>
 */
@Getter
@Setter
@JsonTypeName("as.dto.project.search.ProjectSearchCriteria")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectSearchCriteria extends AbstractCompositeSearchCriteria {

    /**
     * Static helper to create a Project criteria that filters by Code.
     *
     * @param projectCode The code of the project (e.g. "LIEFERANTEN")
     * @return The configured criteria.
     */
    public static ProjectSearchCriteria withCode(String projectCode) {
        ProjectSearchCriteria criteria = new ProjectSearchCriteria();
        criteria.with(CodeSearchCriteria.withCode(projectCode));
        return criteria;
    }
}