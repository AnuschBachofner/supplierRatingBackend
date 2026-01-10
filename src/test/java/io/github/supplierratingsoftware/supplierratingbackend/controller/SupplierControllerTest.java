package io.github.supplierratingsoftware.supplierratingbackend.controller;

import io.github.supplierratingsoftware.supplierratingbackend.dto.api.SupplierCreationDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.SupplierReadDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.SupplierUpdateDto;
import io.github.supplierratingsoftware.supplierratingbackend.service.SupplierService;
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

import java.util.Collections;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link SupplierController}.
 * <p>
 * Focuses on testing the web layer, specifically input validation and HTTP status codes.
 * The service layer is mocked to isolate the controller logic.
 * </p>
 */
@WebMvcTest(SupplierController.class)
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SupplierService supplierService;

    // --- API Constants ---
    private static final String SUPPLIER_BASE_URL = "/api/v1/suppliers";
    private static final String SUPPLIER_ID_URL_TEMPLATE = "/api/v1/suppliers/{id}";

    // --- Dummy Data Constants ---
    private static final String DUMMY_ID = "Test Perm Id";
    private static final String DUMMY_CODE = "Test Code";
    private static final String DUMMY_NAME = "Test Supplier Name";
    private static final String DUMMY_CUSTOMER_NUMBER = "Test Customer Number";
    private static final String DUMMY_ADDITION = "Test Addition";
    private static final String DUMMY_STREET = "Test Street";
    private static final String DUMMY_PO_BOX = "Test PO Box";
    private static final String VALID_COUNTRY_LABEL = "CH"; // Must allow CH, D, F, FL, NL
    private static final String INVALID_COUNTRY_LABEL = "USA"; // Not in vocabulary
    private static final String DUMMY_ZIP = "Test Zip Code";
    private static final String DUMMY_CITY = "Test City";
    private static final String VALID_WEBSITE = "https://example.com";
    private static final String INVALID_WEBSITE = "ht tp://broken-url";
    private static final String VALID_EMAIL = "contact@example.com";
    private static final String INVALID_EMAIL = "not-an.email";
    private static final String DUMMY_PHONE = "+41 44 123 45 67";
    private static final String DUMMY_VAT = "Test VAT ID";
    private static final String DUMMY_CONDITIONS = "Test Conditions";
    private static final String DUMMY_INFO = "Test Info";

    // --- Validation specific constants ---
    private static final String BLANK_STRING = " ";
    private static final String EMPTY_STRING = "";

    // --- JSON Constants ---
    private static final String EMPTY_JSON_LIST = "[]";
    private static final String JSON_NAME_PATH = "$.name";
    private static final String JSON_ID_PATH = "$.id";

    // --- Helper Methods - SupplierReadDto ---

    /**
     * Creates a valid {@link SupplierReadDto} for testing purposes.
     */
    private static SupplierReadDto createValidReadDto() {
        return new SupplierReadDto(
                DUMMY_NAME,
                DUMMY_CUSTOMER_NUMBER,
                DUMMY_ADDITION,
                DUMMY_STREET,
                DUMMY_PO_BOX,
                VALID_COUNTRY_LABEL,
                DUMMY_ZIP,
                DUMMY_CITY,
                VALID_WEBSITE,
                VALID_EMAIL,
                DUMMY_PHONE,
                DUMMY_VAT,
                DUMMY_CONDITIONS,
                DUMMY_INFO,
                DUMMY_ID,
                DUMMY_CODE,
                null,
                null
        );
    }

    // --- Tests for GET ---

    /**
     * Verifies that GET /api/v1/suppliers returns 200 OK and an empty list when no suppliers exist.
     */
    @Test
    void getAllSuppliers_shouldReturnOkAndList() throws Exception {
        // Arrange
        when(supplierService.getAllSuppliers()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get(SUPPLIER_BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(EMPTY_JSON_LIST));
    }

    /**
     * Verifies that GET /api/v1/suppliers/{id} returns 200 OK and the supplier details.
     */
    @Test
    void getSupplierById_shouldReturnOkAndSupplier() throws Exception {
        // Arrange
        SupplierReadDto readDto = createValidReadDto();
        when(supplierService.getSupplierById(DUMMY_ID)).thenReturn(readDto);

        // Act & Assert
        mockMvc.perform(get(SUPPLIER_ID_URL_TEMPLATE, DUMMY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_NAME_PATH).value(DUMMY_NAME))
                .andExpect(jsonPath(JSON_ID_PATH).value(DUMMY_ID));
    }

    // --- Tests for CREATE ---

    /**
     * Verifies that creating a supplier with a valid DTO returns 201 Created.
     */
    @Test
    void createSupplier_shouldReturnCreated_whenDtoIsValid() throws Exception {
        // Arrange
        SupplierCreationDto validDto = new CreationBuilder().build();
        SupplierReadDto expectedReadDto = createValidReadDto();

        when(supplierService.createSupplier(any(SupplierCreationDto.class))).thenReturn(expectedReadDto);

        // Act & Assert
        mockMvc.perform(post(SUPPLIER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(JSON_NAME_PATH).value(DUMMY_NAME))
                .andExpect(jsonPath(JSON_ID_PATH).value(DUMMY_ID));
    }

    /**
     * Parameterized test that verifies creating a supplier with invalid fields returns 400 Bad Request.
     * Tests all validation scenarios including @NotBlank, @OpenBisVocabulary, @Pattern, and @Email validators.
     *
     * @param invalidDto      The invalid SupplierCreationDto to test
     * @param testDescription Description of what makes the DTO invalid (for test reporting)
     */
    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("invalidCreationDtoProvider")
    void createSupplier_shouldReturnBadRequest_whenValidationFails(
            SupplierCreationDto invalidDto,
            String testDescription) throws Exception {

        // Act & Assert
        mockMvc.perform(post(SUPPLIER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    // --- Tests for UPDATE ---

    /**
     * Verifies that updating a supplier with a valid DTO returns 200 OK.
     */
    @Test
    void updateSupplier_shouldReturnOk_whenDtoIsValid() throws Exception {
        // Arrange
        SupplierUpdateDto validDto = new UpdateBuilder().build();
        SupplierReadDto expectedReadDto = createValidReadDto();

        when(supplierService.updateSupplier(eq(DUMMY_ID), any(SupplierUpdateDto.class))).thenReturn(expectedReadDto);

        // Act & Assert
        mockMvc.perform(put(SUPPLIER_ID_URL_TEMPLATE, DUMMY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_NAME_PATH).value(DUMMY_NAME));
    }

    /**
     * Parameterized test that verifies updating a supplier with invalid fields returns 400 Bad Request.
     * Tests all validation scenarios including @NotBlank, @OpenBisVocabulary, @Pattern, and @Email validators.
     *
     * @param invalidDto      The invalid SupplierUpdateDto to test
     * @param testDescription Description of what makes the DTO invalid (for test reporting)
     */
    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("invalidUpdateDtoProvider")
    void updateSupplier_shouldReturnBadRequest_whenValidationFails(
            SupplierUpdateDto invalidDto,
            String testDescription) throws Exception {

        // Act & Assert
        mockMvc.perform(put(SUPPLIER_ID_URL_TEMPLATE, DUMMY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    // --- Data Providers for Parameterized Tests ---

    /**
     * Provides test cases for creation supplier validation failures.
     *
     * @return Stream of Arguments for parameterized tests
     */
    private static Stream<Arguments> invalidCreationDtoProvider() {
        return Stream.of(
                // Format & Vocabulary Validators
                Arguments.of(new CreationBuilder() {{ email = INVALID_EMAIL; }}.build(), "Invalid Email Format"),
                Arguments.of(new CreationBuilder() {{ website = INVALID_WEBSITE; }}.build(), "Invalid Website URL"),
                Arguments.of(new CreationBuilder() {{ country = INVALID_COUNTRY_LABEL; }}.build(), "Invalid Country Vocabulary"),

                // Mandatory Fields: Name
                Arguments.of(new CreationBuilder() {{ name = null; }}.build(), "Name missing (null)"),
                Arguments.of(new CreationBuilder() {{ name = BLANK_STRING; }}.build(), "Name blank"),
                Arguments.of(new CreationBuilder() {{ name = EMPTY_STRING; }}.build(), "Name empty"),

                // Mandatory Fields: Customer Number
                Arguments.of(new CreationBuilder() {{ customerNumber = null; }}.build(), "Customer Number missing"),
                Arguments.of(new CreationBuilder() {{ customerNumber = BLANK_STRING; }}.build(), "Customer Number blank"),
                Arguments.of(new CreationBuilder() {{ customerNumber = EMPTY_STRING; }}.build(), "Customer Number empty"),

                // Mandatory Fields: Street
                Arguments.of(new CreationBuilder() {{ street = null; }}.build(), "Street missing"),
                Arguments.of(new CreationBuilder() {{ street = BLANK_STRING; }}.build(), "Street blank"),
                Arguments.of(new CreationBuilder() {{ street = EMPTY_STRING; }}.build(), "Street empty"),

                // Mandatory Fields: Country
                Arguments.of(new CreationBuilder() {{ country = null; }}.build(), "Country missing"),
                Arguments.of(new CreationBuilder() {{ country = BLANK_STRING; }}.build(), "Country blank"),
                Arguments.of(new CreationBuilder() {{ country = EMPTY_STRING; }}.build(), "Country empty"),

                // Mandatory Fields: Zip
                Arguments.of(new CreationBuilder() {{ zipCode = null; }}.build(), "Zip missing"),
                Arguments.of(new CreationBuilder() {{ zipCode = BLANK_STRING; }}.build(), "Zip blank"),
                Arguments.of(new CreationBuilder() {{ zipCode = EMPTY_STRING; }}.build(), "Zip empty"),

                // Mandatory Fields: City
                Arguments.of(new CreationBuilder() {{ city = null; }}.build(), "City missing"),
                Arguments.of(new CreationBuilder() {{ city = BLANK_STRING; }}.build(), "City blank"),
                Arguments.of(new CreationBuilder() {{ city = EMPTY_STRING; }}.build(), "City empty"),

                // Mandatory Fields: VatId
                Arguments.of(new CreationBuilder() {{ vatId = null; }}.build(), "VatID missing"),
                Arguments.of(new CreationBuilder() {{ vatId = BLANK_STRING; }}.build(), "VatID blank"),
                Arguments.of(new CreationBuilder() {{ vatId = EMPTY_STRING; }}.build(), "VatID empty"),

                // Mandatory Fields: Conditions
                Arguments.of(new CreationBuilder() {{ conditions = null; }}.build(), "Conditions missing"),
                Arguments.of(new CreationBuilder() {{ conditions = BLANK_STRING; }}.build(), "Conditions blank"),
                Arguments.of(new CreationBuilder() {{ conditions = EMPTY_STRING; }}.build(), "Conditions empty")
        );
    }

    /**
     * Provides test cases for update supplier validation failures.
     *
     * @return Stream of Arguments for parameterized tests
     */
    private static Stream<Arguments> invalidUpdateDtoProvider() {
        return Stream.of(
                // Format & Vocabulary Validators
                Arguments.of(new UpdateBuilder() {{ email = INVALID_EMAIL; }}.build(), "Invalid Email Format"),
                Arguments.of(new UpdateBuilder() {{ website = INVALID_WEBSITE; }}.build(), "Invalid Website URL"),
                Arguments.of(new UpdateBuilder() {{ country = INVALID_COUNTRY_LABEL; }}.build(), "Invalid Country Vocabulary"),

                // Mandatory Fields: Name
                Arguments.of(new UpdateBuilder() {{ name = null; }}.build(), "Name missing (null)"),
                Arguments.of(new UpdateBuilder() {{ name = BLANK_STRING; }}.build(), "Name blank"),
                Arguments.of(new UpdateBuilder() {{ name = EMPTY_STRING; }}.build(), "Name empty"),

                // Mandatory Fields: Customer Number
                Arguments.of(new UpdateBuilder() {{ customerNumber = null; }}.build(), "Customer Number missing"),
                Arguments.of(new UpdateBuilder() {{ customerNumber = BLANK_STRING; }}.build(), "Customer Number blank"),
                Arguments.of(new UpdateBuilder() {{ customerNumber = EMPTY_STRING; }}.build(), "Customer Number empty"),

                // Mandatory Fields: Street
                Arguments.of(new UpdateBuilder() {{ street = null; }}.build(), "Street missing"),
                Arguments.of(new UpdateBuilder() {{ street = BLANK_STRING; }}.build(), "Street blank"),
                Arguments.of(new UpdateBuilder() {{ street = EMPTY_STRING; }}.build(), "Street empty"),

                // Mandatory Fields: Country
                Arguments.of(new UpdateBuilder() {{ country = null; }}.build(), "Country missing"),
                Arguments.of(new UpdateBuilder() {{ country = BLANK_STRING; }}.build(), "Country blank"),
                Arguments.of(new UpdateBuilder() {{ country = EMPTY_STRING; }}.build(), "Country empty"),

                // Mandatory Fields: Zip
                Arguments.of(new UpdateBuilder() {{ zipCode = null; }}.build(), "Zip missing"),
                Arguments.of(new UpdateBuilder() {{ zipCode = BLANK_STRING; }}.build(), "Zip blank"),
                Arguments.of(new UpdateBuilder() {{ zipCode = EMPTY_STRING; }}.build(), "Zip empty"),

                // Mandatory Fields: City
                Arguments.of(new UpdateBuilder() {{ city = null; }}.build(), "City missing"),
                Arguments.of(new UpdateBuilder() {{ city = BLANK_STRING; }}.build(), "City blank"),
                Arguments.of(new UpdateBuilder() {{ city = EMPTY_STRING; }}.build(), "City empty"),

                // Mandatory Fields: VatId
                Arguments.of(new UpdateBuilder() {{ vatId = null; }}.build(), "VatID missing"),
                Arguments.of(new UpdateBuilder() {{ vatId = BLANK_STRING; }}.build(), "VatID blank"),
                Arguments.of(new UpdateBuilder() {{ vatId = EMPTY_STRING; }}.build(), "VatID empty"),

                // Mandatory Fields: Conditions
                Arguments.of(new UpdateBuilder() {{ conditions = null; }}.build(), "Conditions missing"),
                Arguments.of(new UpdateBuilder() {{ conditions = BLANK_STRING; }}.build(), "Conditions blank"),
                Arguments.of(new UpdateBuilder() {{ conditions = EMPTY_STRING; }}.build(), "Conditions empty")
        );
    }

    // --- Builders for DTOs  ---

    /**
     * Builder for creating SupplierCreationDto instances with predefined values.
     */
    static class CreationBuilder {
        String name = DUMMY_NAME;
        String customerNumber = DUMMY_CUSTOMER_NUMBER;
        String addition = DUMMY_ADDITION;
        String street = DUMMY_STREET;
        String poBox = DUMMY_PO_BOX;
        String country = VALID_COUNTRY_LABEL;
        String zipCode = DUMMY_ZIP;
        String city = DUMMY_CITY;
        String website = VALID_WEBSITE;
        String email = VALID_EMAIL;
        String phoneNumber = DUMMY_PHONE;
        String vatId = DUMMY_VAT;
        String conditions = DUMMY_CONDITIONS;
        String customerInfo = DUMMY_INFO;

        /**
         * Builds a SupplierCreationDto with the current configuration.
         * @return A new SupplierCreationDto instance.
         */
        SupplierCreationDto build() {
            return new SupplierCreationDto(name, customerNumber, addition, street, poBox, country, zipCode, city, website,
                    email, phoneNumber, vatId, conditions, customerInfo);
        }
    }

    /**
     * Builder for creating SupplierUpdateDto instances with predefined values.
     */
    static class UpdateBuilder {
        String name = DUMMY_NAME;
        String customerNumber = DUMMY_CUSTOMER_NUMBER;
        String addition = DUMMY_ADDITION;
        String street = DUMMY_STREET;
        String poBox = DUMMY_PO_BOX;
        String country = VALID_COUNTRY_LABEL;
        String zipCode = DUMMY_ZIP;
        String city = DUMMY_CITY;
        String website = VALID_WEBSITE;
        String email = VALID_EMAIL;
        String phoneNumber = DUMMY_PHONE;
        String vatId = DUMMY_VAT;
        String conditions = DUMMY_CONDITIONS;
        String customerInfo = DUMMY_INFO;

        /**
         * Builds a SupplierUpdateDto with the current configuration.
         * @return A new SupplierUpdateDto instance.
         */
        SupplierUpdateDto build() {
            return new SupplierUpdateDto(name, customerNumber, addition, street, poBox, country, zipCode, city, website,
                    email, phoneNumber, vatId, conditions, customerInfo);
        }
    }
}