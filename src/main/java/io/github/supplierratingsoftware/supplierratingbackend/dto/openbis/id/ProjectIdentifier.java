package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;

/**
 * Represents a project identifier from the openBIS V3 API.
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "@type": "as.dto.project.id.ProjectIdentifier",
 *         "identifier": "/SPACE/PROJECT"
 *     }
 * </pre>
 *
 * Fields with a {@code null} value are ignored during serialization.
 *
 * @param identifier The project identifier.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName(OpenBisJsonConstants.PROJECT_IDENTIFIER)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = OpenBisJsonConstants.TYPE_JSON_PROPERTY_KEY)
public record ProjectIdentifier(String identifier) {
}