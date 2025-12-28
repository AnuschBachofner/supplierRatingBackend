package io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * A generic wrapper for search results from the openBIS V3 API.
 * OpenBIS wraps lists in an object containing the list {@code objects} and the total count {@code totalCount}.
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "objects": [ ... ],
 *         "totalCount": 123
 *     }
 * </pre>
 *
 * Fields with a {@code null} value are ignored during serialization.
 * Unknown fields (such as {@code @type} or {@code @id} are ignored during deserialization.)
 *
 * @param objects    The list of objects matching the search criteria.
 * @param totalCount The total number of objects matching the search criteria.
 * @param <T>        The type of objects in the search result (e.g., {@link OpenBisSample})
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenBisSearchResult<T>(List<T> objects, long totalCount) {
}
