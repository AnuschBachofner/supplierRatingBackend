package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;

/**
 * Represents an experiment identifier from the openBIS V3 API.
 * Experiments in openBIS are also called collections.
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "@type": "as.dto.experiment.id.ExperimentIdentifier",
 *         "identifier": "/SPACE/PROJECT/COLLECTION"
 *     }
 * </pre>
 *
 * Fields with a {@code null} value are ignored during serialization.
 *
 * @param identifier The experiment identifier.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName(OpenBisJsonConstants.EXPERIMENT_IDENTIFIER)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = OpenBisJsonConstants.TYPE_JSON_PROPERTY_KEY)
public record ExperimentIdentifier(String identifier) {
}