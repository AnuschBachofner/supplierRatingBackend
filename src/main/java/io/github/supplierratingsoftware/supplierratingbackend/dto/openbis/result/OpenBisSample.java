package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id.OpenBisEntityType;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id.OpenBisPermId;

import java.util.List;
import java.util.Map;

/**
 * Represents a sample from the openBIS V3 API.
 * (openBIS response)
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "permId": { ... },
 *         "type": { ... },
 *         "code": "SAMPLE_CODE",
 *         "properties": { ... }
 *         "parents": [ ... ]
 *     }
 * </pre>
 * <p>
 * Fields with a {@code null} value are ignored during serialization.
 * Unknown fields (such as {@code registrator} or {@code modificationDate} are ignored during deserialization.)
 *
 * @param permId     The perm id of the sample.
 * @param type       The type of the sample.
 * @param code       The code of the sample.
 * @param properties The properties (payload) of the sample.
 * @param parents    The parent samples of the current sample (used for hierarchical data retrieval).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = OpenBisJsonConstants.ID_JSON_PROPERTY_KEY)
public record OpenBisSample(
        OpenBisPermId permId,
        OpenBisEntityType type,
        String code,
        Map<String, String> properties,
        List<OpenBisSample> parents
) {

    /**
     * Safely retrieves a property from the sample's properties map.
     *
     * @param key The key of the property to retrieve (e.g. "NAME").
     * @return The value of the property, or {@code null} if the properties map is {@code null} or the property does not exist.
     */
    public String getProperty(String key) {
        if (properties == null) return null;
        return properties.get(key);
    }
}
