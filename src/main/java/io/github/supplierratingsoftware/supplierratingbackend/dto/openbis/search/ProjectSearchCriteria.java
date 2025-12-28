package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;
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
@JsonTypeName(OpenBisJsonConstants.PROJECT_SEARCH_CRITERIA)
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