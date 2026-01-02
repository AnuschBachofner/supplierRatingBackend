package io.github.supplierratingsoftware.supplierratingbackend.constant.openbis;

/**
 * Contains technical constants related to the OpenBIS V3 JSON-RPC protocol.
 * Includes JSON type identifiers, fixed field names, operators, etc.
 */
public final class OpenBisJsonConstants {

    // JSON RPC
    public static final String SUPPORTED_MEDIA_TYPE = "application/json-rpc";
    public static final String JSON_RPC_VERSION = "2.0";

    // JSON Type Names
    // - Fetch Options
    public static final String PROPERTY_FETCH_OPTIONS = "as.dto.property.fetchoptions.PropertyFetchOptions";
    public static final String SAMPLE_FETCH_OPTIONS = "as.dto.sample.fetchoptions.SampleFetchOptions";
    public static final String SAMPLE_TYPE_FETCH_OPTIONS = "as.dto.sample.fetchoptions.SampleTypeFetchOptions";

    // - IDs
    public static final String ENTITY_TYPE_PERM_ID = "as.dto.entitytype.id.EntityTypePermId";

    // - Search Criteria
    public static final String CODE_SEARCH_CRITERIA = "as.dto.common.search.CodeSearchCriteria";
    public static final String PERM_ID_SEARCH_CRITERIA = "as.dto.common.search.PermIdSearchCriteria";
    public static final String PROJECT_SEARCH_CRITERIA = "as.dto.project.search.ProjectSearchCriteria";
    public static final String SAMPLE_SEARCH_CRITERIA = "as.dto.sample.search.SampleSearchCriteria";
    public static final String SAMPLE_TYPE_SEARCH_CRITERIA = "as.dto.sample.search.SampleTypeSearchCriteria";
    public static final String SPACE_SEARCH_CRITERIA = "as.dto.space.search.SpaceSearchCriteria";
    public static final String STRING_EQUAL_TO_VALUE = "as.dto.common.search.StringEqualToValue";
    public static final String SAMPLE_PARENTS_SEARCH_CRITERIA = "as.dto.sample.search.SampleParentsSearchCriteria";

    // - Creation Types
    public static final String SAMPLE_CREATION = "as.dto.sample.create.SampleCreation";
    public static final String SPACE_PERM_ID = "as.dto.space.id.SpacePermId";
    public static final String PROJECT_IDENTIFIER = "as.dto.project.id.ProjectIdentifier";
    public static final String EXPERIMENT_IDENTIFIER = "as.dto.experiment.id.ExperimentIdentifier";
    public static final String SAMPLE_PERM_ID = "as.dto.sample.id.SamplePermId"; // For responses

    // JSON Property Keys
    public static final String TYPE_JSON_PROPERTY_KEY = "@type";
    public static final String ID_JSON_PROPERTY_KEY = "@id";

    // Operators
    public static final String AND_OPERATOR = "AND";

    // Method Names
    public static final String LOGIN_METHOD_NAME = "login";
    public static final String SEARCH_SAMPLES_METHOD_NAME = "searchSamples";
    public static final String CREATE_SAMPLE_METHOD_NAME = "createSamples";

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private OpenBisJsonConstants() {
    }
}
