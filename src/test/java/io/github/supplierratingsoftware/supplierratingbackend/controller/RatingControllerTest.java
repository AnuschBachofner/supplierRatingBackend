package io.github.supplierratingsoftware.supplierratingbackend.controller;

import io.github.supplierratingsoftware.supplierratingbackend.constant.api.ValidationConstants;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.RatingCreationDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.RatingReadDto;
import io.github.supplierratingsoftware.supplierratingbackend.service.RatingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link RatingController}.
 * <p>
 * Focuses on testing the web layer, specifically input validation and HTTP status codes.
 * The service layer is mocked to isolate the controller logic.
 * </p>
 */
@WebMvcTest(RatingController.class)
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RatingService ratingService;

    // --- API Constants ---
    private static final String RATING_BASE_URL = "/api/v1/ratings";
    private static final String RATING_ID_URL_TEMPLATE = "/api/v1/ratings/{id}";

    // --- Dummy Data Constants (IDs & Codes) ---
    private static final String DUMMY_RATING_ID = "Test Rating Perm ID";
    private static final String DUMMY_RATING_CODE = "TEST-RATING-CODE";
    private static final String DUMMY_ORDER_ID = "Test Order Perm ID";
    private static final String DUMMY_SUPPLIER_ID = "Test Supplier Perm ID";
    private static final String DUMMY_SUPPLIER_NAME = "Test Supplier Name";

    // --- Dummy Data Constants (Content) ---
    private static final String DUMMY_QUALITY_REASON = "Test Quality Reason";
    private static final String DUMMY_COST_REASON = "Test Cost Reason";
    private static final String DUMMY_RELIABILITY_REASON = "Test Reliability Reason";
    private static final String DUMMY_AVAILABILITY_REASON = "Test Availability Reason";
    private static final String DUMMY_COMMENT = "Test General Comment";

    // --- Valid Score Constants ---
    // We use the MAX value as a representative valid score.
    private static final Integer VALID_SCORE = ValidationConstants.RATING_SCORE_MAX;
    private static final Double VALID_TOTAL_SCORE = (double) VALID_SCORE;

    // --- Boundary/Edge Case Constants (Calculated) ---
    // Dynamically calculated based on the ValidationConstants to ensure robust boundary testing.
    private static final Integer INVALID_SCORE_TOO_LOW = ValidationConstants.RATING_SCORE_MIN - 1;
    private static final Integer INVALID_SCORE_TOO_HIGH = ValidationConstants.RATING_SCORE_MAX + 1;

    // --- Validation Strings ---
    private static final String BLANK_STRING = " ";
    private static final String EMPTY_STRING = "";

    // --- JSON Constants ---
    private static final String JSON_ID_PATH = "$.id";

    // --- Helper Methods - RatingReadDto ---

    /**
     * Creates a valid {@link RatingReadDto} for mocking service response.
     */
    private static RatingReadDto createValidReadDto() {
        return new RatingReadDto(
                VALID_SCORE, DUMMY_QUALITY_REASON,
                VALID_SCORE, DUMMY_COST_REASON,
                VALID_SCORE, DUMMY_RELIABILITY_REASON,
                VALID_SCORE, DUMMY_AVAILABILITY_REASON,
                VALID_TOTAL_SCORE, DUMMY_COMMENT,
                DUMMY_RATING_ID, DUMMY_RATING_CODE,
                DUMMY_ORDER_ID, DUMMY_SUPPLIER_ID, DUMMY_SUPPLIER_NAME
        );
    }

    // --- Tests for GET ---

    /**
     * Verifies that GET /api/v1/ratings/{id} returns 200 OK and the rating details when found.
     */
    @Test
    void getRatingById_shouldReturnOkAndRating() throws Exception {
        RatingReadDto readDto = createValidReadDto();
        when(ratingService.getRatingById(DUMMY_RATING_ID)).thenReturn(Optional.of(readDto));

        mockMvc.perform(get(RATING_ID_URL_TEMPLATE, DUMMY_RATING_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ID_PATH).value(DUMMY_RATING_ID));
    }

    /**
     * Verifies that GET /api/v1/ratings/{id} returns 404 Not Found when the rating does not exist.
     */
    @Test
    void getRatingById_shouldReturnNotFound_whenRatingDoesNotExist() throws Exception {
        when(ratingService.getRatingById(DUMMY_RATING_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(RATING_ID_URL_TEMPLATE, DUMMY_RATING_ID))
                .andExpect(status().isNotFound());
    }

    // --- Tests for CREATE ---

    /**
     * Verifies that creating a rating with a valid DTO returns 201 Created.
     */
    @Test
    void createRating_shouldReturnCreated_whenDtoIsValid() throws Exception {
        RatingCreationDto validDto = new CreationBuilder().build();
        RatingReadDto expectedReadDto = createValidReadDto();

        when(ratingService.createRating(any(RatingCreationDto.class))).thenReturn(expectedReadDto);

        mockMvc.perform(post(RATING_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(JSON_ID_PATH).value(DUMMY_RATING_ID));
    }

    /**
     * Parameterized test that verifies creating a rating with invalid fields returns 400 Bad Request.
     * Tests all validation scenarios including @NotBlank, @NotNull, @Min, and @Max validators.
     *
     * @param invalidDto      The invalid RatingCreationDto to test
     * @param testDescription Description of what makes the DTO invalid (for test reporting)
     */
    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("invalidCreationDtoProvider")
    void createRating_shouldReturnBadRequest_whenValidationFails(
            RatingCreationDto invalidDto,
            String testDescription) throws Exception {

        mockMvc.perform(post(RATING_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    // --- Data Provider for Parameterized Tests ---

    /**
     * Provides test cases for create rating validation failures.
     * Uses the CreationBuilder to construct invalid DTOs cleanly.
     */
    private static Stream<Arguments> invalidCreationDtoProvider() {
        return Stream.of(
                // Order ID Validation
                Arguments.of(new CreationBuilder() {{ orderId = null; }}.build(), "Order ID missing (null)"),
                Arguments.of(new CreationBuilder() {{ orderId = BLANK_STRING; }}.build(), "Order ID blank"),
                Arguments.of(new CreationBuilder() {{ orderId = EMPTY_STRING; }}.build(), "Order ID empty"),

                // Quality Score Validation
                Arguments.of(new CreationBuilder() {{ quality = null; }}.build(), "Quality Score missing"),
                Arguments.of(new CreationBuilder() {{ quality = INVALID_SCORE_TOO_LOW; }}.build(), "Quality Score too low (<1)"),
                Arguments.of(new CreationBuilder() {{ quality = INVALID_SCORE_TOO_HIGH; }}.build(), "Quality Score too high (>5)"),

                // Quality Reason Validation
                Arguments.of(new CreationBuilder() {{ qualityReason = null; }}.build(), "Quality Reason missing"),
                Arguments.of(new CreationBuilder() {{ qualityReason = BLANK_STRING; }}.build(), "Quality Reason blank"),
                Arguments.of(new CreationBuilder() {{ qualityReason = EMPTY_STRING; }}.build(), "Quality Reason empty"),

                // Cost Score Validation
                Arguments.of(new CreationBuilder() {{ cost = null; }}.build(), "Cost Score missing"),
                Arguments.of(new CreationBuilder() {{ cost = INVALID_SCORE_TOO_LOW; }}.build(), "Cost Score too low (<1)"),
                Arguments.of(new CreationBuilder() {{ cost = INVALID_SCORE_TOO_HIGH; }}.build(), "Cost Score too high (>5)"),

                // Cost Reason Validation
                Arguments.of(new CreationBuilder() {{ costReason = null; }}.build(), "Cost Reason missing"),
                Arguments.of(new CreationBuilder() {{ costReason = BLANK_STRING; }}.build(), "Cost Reason blank"),
                Arguments.of(new CreationBuilder() {{ costReason = EMPTY_STRING; }}.build(), "Cost Reason empty"),

                // Reliability Score Validation
                Arguments.of(new CreationBuilder() {{ reliability = null; }}.build(), "Reliability Score missing"),
                Arguments.of(new CreationBuilder() {{ reliability = INVALID_SCORE_TOO_LOW; }}.build(), "Reliability Score too low (<1)"),
                Arguments.of(new CreationBuilder() {{ reliability = INVALID_SCORE_TOO_HIGH; }}.build(), "Reliability Score too high (>5)"),

                // Reliability Reason Validation
                Arguments.of(new CreationBuilder() {{ reliabilityReason = null; }}.build(), "Reliability Reason missing"),
                Arguments.of(new CreationBuilder() {{ reliabilityReason = BLANK_STRING; }}.build(), "Reliability Reason blank"),
                Arguments.of(new CreationBuilder() {{ reliabilityReason = EMPTY_STRING; }}.build(), "Reliability Reason empty"),

                // Availability Score Validation (Optional but must be valid if present)
                Arguments.of(new CreationBuilder() {{ availability = INVALID_SCORE_TOO_LOW; }}.build(), "Availability Score too low (<1)"),
                Arguments.of(new CreationBuilder() {{ availability = INVALID_SCORE_TOO_HIGH; }}.build(), "Availability Score too high (>5)")
        );
    }

    // --- Builders for DTOs ---

    /**
     * Builder for creating RatingCreationDto instances with predefined valid values.
     */
    static class CreationBuilder {
        String orderId = DUMMY_ORDER_ID;
        Integer quality = VALID_SCORE;
        String qualityReason = DUMMY_QUALITY_REASON;
        Integer cost = VALID_SCORE;
        String costReason = DUMMY_COST_REASON;
        Integer reliability = VALID_SCORE;
        String reliabilityReason = DUMMY_RELIABILITY_REASON;
        Integer availability = VALID_SCORE;
        String availabilityReason = DUMMY_AVAILABILITY_REASON;
        String ratingComment = DUMMY_COMMENT;

        /**
         * Builds and returns a new RatingCreationDto with the current configuration.
         * @return RatingCreationDto instance
         */
        RatingCreationDto build() {
            return new RatingCreationDto(
                    orderId,
                    quality, qualityReason,
                    cost, costReason,
                    reliability, reliabilityReason,
                    availability, availabilityReason,
                    ratingComment
            );
        }
    }
}