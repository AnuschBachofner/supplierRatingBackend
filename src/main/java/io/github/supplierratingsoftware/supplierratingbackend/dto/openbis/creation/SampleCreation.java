package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.creation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id.*;

import java.util.Map;

/**
 * Represents a sample creation request from the openBIS V3 API.
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "@type": "as.dto.sample.creation.SampleCreation",
 *         "spaceId": { ... },
 *         "projectId": { ... },
 *         "experimentId": { ... },
 *         "typeId": { ... },
 *         "code": "SAMPLE_CODE",
 *         "properties": { ... }
 *     }
 * </pre>
 *
 * @param spaceId The space identifier where the sample is created.
 * @param projectId The project identifier where the sample is created.
 * @param experimentId The experiment identifier where the sample is created.
 * @param typeId The type of the sample.
 * @param code The code of the sample.
 * @param properties Additional properties for the sample.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName(OpenBisJsonConstants.SAMPLE_CREATION)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = OpenBisJsonConstants.TYPE_JSON_PROPERTY_KEY)
public record SampleCreation(
        SpacePermId spaceId,
        ProjectIdentifier projectId,
        ExperimentIdentifier experimentId,
        EntityTypePermId typeId,
        String code,
        Map<String, String> properties
) {
}