package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisJsonConstants;

/**
 * The root interface for all search criteria used in the openBIS V3 API.
 *
 * <p><strong>Purpose:</strong><br>
 * This interface enables <b>polymorphic serialization/deserialization</b> via Jackson.
 * OpenBIS search requests often require nested criteria, where a list
 * can contain various types of criteria (e.g., searching by Code, by Type, by Property, or by Parent).
 * </p>
 *
 * <p><strong>Mechanism:</strong><br>
 * It uses the JSON property {@code "@type"} as a discriminator
 * (a special field acting as a label to tell the parser which specific Java class to instantiate).
 * This corresponds strictly to the OpenBIS V3 JSON-RPC specification.
 * </p>
 *
 * <p><strong>Implementation:</strong><br>
 * Every class implementing this interface <b>must</b> be annotated with
 * {@code @JsonTypeName("as.dto.xy.search.SomeSearchCriteria")} to define the specific
 * value for the {@code @type} field. Jackson uses this name to map the JSON to the
 * correct Java class.
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
 *                  "operator": "AND",
 *                  "criteria": [
 *                      {
 *                          "@type": "as.dto.common.search.CodeSearchCriteria",
 *                          "fieldValue": { "value": "LIEFERANT", ... }
 *                      }
 *                  ]
 *              }
 *          ]
 *      }
 * </pre>
 *
 * <p><strong>Allowed JSON Sub Types known by Jackson:</strong><br>
 * <ul>
 *     <li>{@link SampleSearchCriteria}</li>
 *     <li>{@link SpaceSearchCriteria}</li>
 *     <li>{@link ProjectSearchCriteria}</li>
 *     <li>{@link CodeSearchCriteria}</li>
 *     <li>{@link SampleTypeSearchCriteria}</li>
 *     <li>{@link SampleParentsSearchCriteria}</li>
 * </ul>
 * </p>
 *
 * @see AbstractCompositeSearchCriteria
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = OpenBisJsonConstants.TYPE_JSON_PROPERTY_KEY
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SampleSearchCriteria.class, name = OpenBisJsonConstants.SAMPLE_SEARCH_CRITERIA),
        @JsonSubTypes.Type(value = SpaceSearchCriteria.class, name = OpenBisJsonConstants.SPACE_SEARCH_CRITERIA),
        @JsonSubTypes.Type(value = ProjectSearchCriteria.class, name = OpenBisJsonConstants.PROJECT_SEARCH_CRITERIA),
        @JsonSubTypes.Type(value = CodeSearchCriteria.class, name = OpenBisJsonConstants.CODE_SEARCH_CRITERIA),
        @JsonSubTypes.Type(value = SampleTypeSearchCriteria.class, name = OpenBisJsonConstants.SAMPLE_TYPE_SEARCH_CRITERIA),
        @JsonSubTypes.Type(value = SampleParentsSearchCriteria.class, name = OpenBisJsonConstants.SAMPLE_PARENTS_SEARCH_CRITERIA)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public interface SearchCriteria {
    // Marker Interface
}