package io.github.supplierratingsoftware.supplierratingbackend.controller;

import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderCreationDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderReadDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderUpdateDto;
import io.github.supplierratingsoftware.supplierratingbackend.service.OrderService;
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
import java.util.UUID;
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
 * Integration tests for the {@link OrderController}.
 * <p>
 * Focuses on testing the web layer, specifically input validation and HTTP status codes.
 * The service layer is mocked to isolate the controller logic.
 * </p>
 */
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    // --- API Constants ---
    private static final String ORDER_BASE_URL = "/api/v1/orders";
    private static final String ORDER_ID_URL_TEMPLATE = "/api/v1/orders/{id}";

    // --- Dummy Data Constants ---
    private static final String DUMMY_ID = "Test Perm ID";
    private static final String DUMMY_CODE = "BESTELLUNG" + "-" + UUID.randomUUID();
    private static final String DUMMY_NAME = "Test Order Name";
    private static final String VALID_MAIN_CATEGORY_LABEL = "Beschaffung";
    private static final String INVALID_MAIN_CATEGORY_LABEL = "InvalidCategory"; // Not in vocabulary
    private static final String VALID_SUB_CATEGORY_LABEL = "Messmittel";
    private static final String INVALID_SUB_CATEGORY_LABEL = "InvalidSubCategory";
    private static final String DUMMY_DETAILS = "Test Details";
    private static final String DUMMY_FREQUENCY = "One-time";
    private static final String DUMMY_CONTACT_PERSON = "John Doe";
    private static final String VALID_CONTACT_EMAIL = "john.doe@example.com";
    private static final String INVALID_CONTACT_EMAIL = "john.doe";
    private static final String DUMMY_CONTACT_PHONE = "+41 79 123 45 67";
    private static final String DUMMY_REASON = "Test Reason";
    private static final String DUMMY_ORDER_METHOD = "Test Order Method";
    private static final String DUMMY_ORDERED_BY = "Jane Smith";
    private static final String VALID_ORDER_DATE = "2025-01-01"; // YYYY-MM-DD
    private static final String INVALID_ORDER_DATE = "01.01.2025"; // Wrong format
    private static final String VALID_DELIVERY_DATE = "2025-01-15"; // YYYY-MM-DD
    private static final String INVALID_DELIVERY_DATE = "15.01.2025"; // Wrong format
    private static final String DUMMY_COMMENT = "Test Comment";
    private static final String DUMMY_SUPPLIER_ID = "SUP-123";
    private static final String DUMMY_SUPPLIER_NAME = "Acme Corp";
    private static final String DUMMY_RATING_STATUS = "Pending";

    // --- Validation specific constants ---
    private static final String BLANK_STRING = " ";
    private static final String EMPTY_STRING = "";

    // --- JSON Constants ---
    private static final String JSON_EMPTY_ARRAY = "[]";
    private static final String JSON_NAME_PATH = "$.name";
    private static final String JSON_ID_PATH = "$.id";

    // --- Helper Methods - OrderReadDto ---

    /**
     * Creates a valid {@link OrderReadDto} as an expected response from the service.
     */
    private OrderReadDto createValidReadDto() {
        return new OrderReadDto(
                DUMMY_NAME,
                VALID_MAIN_CATEGORY_LABEL,
                VALID_SUB_CATEGORY_LABEL,
                DUMMY_DETAILS,
                DUMMY_FREQUENCY,
                DUMMY_CONTACT_PERSON,
                VALID_CONTACT_EMAIL,
                DUMMY_CONTACT_PHONE,
                DUMMY_REASON,
                DUMMY_ORDER_METHOD,
                DUMMY_ORDERED_BY,
                VALID_ORDER_DATE,
                VALID_DELIVERY_DATE,
                DUMMY_COMMENT,
                DUMMY_ID,
                DUMMY_CODE,
                DUMMY_RATING_STATUS,
                DUMMY_SUPPLIER_ID,
                DUMMY_SUPPLIER_NAME,
                null
        );
    }

    // --- Tests for GET ---

    /**
     * Verifies that GET /api/v1/orders returns 200 OK and an empty list when no orders exist.
     */
    @Test
    void getAllOrders_shouldReturnOkAndList() throws Exception {
        when(orderService.getAllOrders(null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(ORDER_BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(JSON_EMPTY_ARRAY));
    }

    /**
     * Verifies that GET /api/v1/orders?supplierId=... passes the filter to the service.
     */
    @Test
    void getAllOrders_shouldPassSupplierFilterToService() throws Exception {
        when(orderService.getAllOrders(eq(DUMMY_SUPPLIER_ID))).thenReturn(Collections.emptyList());

        mockMvc.perform(get(ORDER_BASE_URL)
                        .param("supplierId", DUMMY_SUPPLIER_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(JSON_EMPTY_ARRAY));
    }

    /**
     * Verifies that GET /api/v1/orders/{id} returns 200 OK and the order details.
     */
    @Test
    void getOrderById_shouldReturnOkAndOrder() throws Exception {
        OrderReadDto readDto = createValidReadDto();
        when(orderService.getOrderById(DUMMY_ID)).thenReturn(readDto);

        mockMvc.perform(get(ORDER_ID_URL_TEMPLATE, DUMMY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_NAME_PATH).value(DUMMY_NAME))
                .andExpect(jsonPath(JSON_ID_PATH).value(DUMMY_ID));
    }

    // --- Tests for CREATE ---

    /**
     * Verifies that creating an order with a valid DTO returns 201 Created.
     */
    @Test
    void createOrder_shouldReturnCreated_whenDtoIsValid() throws Exception {
        OrderCreationDto validDto = new CreationBuilder().build();
        OrderReadDto expectedReadDto = createValidReadDto();

        when(orderService.createOrder(any(OrderCreationDto.class))).thenReturn(expectedReadDto);

        mockMvc.perform(post(ORDER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(JSON_NAME_PATH).value(DUMMY_NAME))
                .andExpect(jsonPath(JSON_ID_PATH).value(DUMMY_ID));
    }

    /**
     * Parameterized test that verifies creating an order with invalid fields returns 400 Bad Request.
     */
    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("invalidCreationDtoProvider")
    void createOrder_shouldReturnBadRequest_whenValidationFails(
            OrderCreationDto invalidDto,
            String testDescription) throws Exception {

        mockMvc.perform(post(ORDER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    // --- Tests for UPDATE ---

    /**
     * Verifies that updating an order with a valid DTO returns 200 OK.
     */
    @Test
    void updateOrder_shouldReturnOk_whenDtoIsValid() throws Exception {
        OrderUpdateDto validDto = new UpdateBuilder().build();
        OrderReadDto expectedReadDto = createValidReadDto();

        when(orderService.updateOrder(eq(DUMMY_ID), any(OrderUpdateDto.class))).thenReturn(expectedReadDto);

        mockMvc.perform(put(ORDER_ID_URL_TEMPLATE, DUMMY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_NAME_PATH).value(DUMMY_NAME));
    }

    /**
     * Parameterized test that verifies updating an order with invalid fields returns 400 Bad Request.
     */
    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("invalidUpdateDtoProvider")
    void updateOrder_shouldReturnBadRequest_whenValidationFails(
            OrderUpdateDto invalidDto,
            String testDescription) throws Exception {

        mockMvc.perform(put(ORDER_ID_URL_TEMPLATE, DUMMY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    // --- Data Providers for Parameterized Tests ---

    /**
     * Provides test cases for create order validation failures.
     * Each argument contains: (invalidDto, testDescription)
     *
     * @return Stream of Arguments for parameterized tests
     */
    private static Stream<Arguments> invalidCreationDtoProvider() {
        return Stream.of(
                // Format & Vocabulary
                Arguments.of(new CreationBuilder() {{ mainCategory = INVALID_MAIN_CATEGORY_LABEL; }}.build(), "mainCategory not in vocabulary"),
                Arguments.of(new CreationBuilder() {{ subCategory = INVALID_SUB_CATEGORY_LABEL; }}.build(), "subCategory not in vocabulary"),
                Arguments.of(new CreationBuilder() {{ orderDate = INVALID_ORDER_DATE; }}.build(), "orderDate has invalid format"),
                Arguments.of(new CreationBuilder() {{ deliveryDate = INVALID_DELIVERY_DATE; }}.build(), "deliveryDate has invalid format"),
                Arguments.of(new CreationBuilder() {{ contactEmail = INVALID_CONTACT_EMAIL; }}.build(), "contactEmail has invalid format"),

                // Mandatory: Name
                Arguments.of(new CreationBuilder() {{ name = null; }}.build(), "name is missing (null)"),
                Arguments.of(new CreationBuilder() {{ name = BLANK_STRING; }}.build(), "name is blank"),
                Arguments.of(new CreationBuilder() {{ name = EMPTY_STRING; }}.build(), "name is empty"),

                // Mandatory: MainCategory
                Arguments.of(new CreationBuilder() {{ mainCategory = null; }}.build(), "mainCategory is missing (null)"),
                Arguments.of(new CreationBuilder() {{ mainCategory = BLANK_STRING; }}.build(), "mainCategory is blank"),
                Arguments.of(new CreationBuilder() {{ mainCategory = EMPTY_STRING; }}.build(), "mainCategory is empty"),

                // Mandatory: SubCategory
                Arguments.of(new CreationBuilder() {{ subCategory = null; }}.build(), "subCategory is missing (null)"),
                Arguments.of(new CreationBuilder() {{ subCategory = BLANK_STRING; }}.build(), "subCategory is blank"),
                Arguments.of(new CreationBuilder() {{ subCategory = EMPTY_STRING; }}.build(), "subCategory is empty"),

                // Mandatory: Reason
                Arguments.of(new CreationBuilder() {{ reason = null; }}.build(), "reason is missing (null)"),
                Arguments.of(new CreationBuilder() {{ reason = BLANK_STRING; }}.build(), "reason is blank"),
                Arguments.of(new CreationBuilder() {{ reason = EMPTY_STRING; }}.build(), "reason is empty"),

                // Mandatory: OrderedBy
                Arguments.of(new CreationBuilder() {{ orderedBy = null; }}.build(), "orderedBy is missing (null)"),
                Arguments.of(new CreationBuilder() {{ orderedBy = BLANK_STRING; }}.build(), "orderedBy is blank"),
                Arguments.of(new CreationBuilder() {{ orderedBy = EMPTY_STRING; }}.build(), "orderedBy is empty"),

                // Mandatory: OrderDate
                Arguments.of(new CreationBuilder() {{ orderDate = null; }}.build(), "orderDate is missing (null)"),
                Arguments.of(new CreationBuilder() {{ orderDate = BLANK_STRING; }}.build(), "orderDate is blank"),
                Arguments.of(new CreationBuilder() {{ orderDate = EMPTY_STRING; }}.build(), "orderDate is empty"),

                // Mandatory: SupplierId
                Arguments.of(new CreationBuilder() {{ supplierId = null; }}.build(), "supplierId is missing (null)"),
                Arguments.of(new CreationBuilder() {{ supplierId = BLANK_STRING; }}.build(), "supplierId is blank"),
                Arguments.of(new CreationBuilder() {{ supplierId = EMPTY_STRING; }}.build(), "supplierId is empty")
        );
    }

    /**
     * Provides test cases for update order validation failures.
     * Each argument contains: (invalidDto, testDescription)
     *
     * @return Stream of Arguments for parameterized tests
     */
    private static Stream<Arguments> invalidUpdateDtoProvider() {
        return Stream.of(
                // Format & Vocabulary
                Arguments.of(new UpdateBuilder() {{ mainCategory = INVALID_MAIN_CATEGORY_LABEL; }}.build(), "mainCategory not in vocabulary"),
                Arguments.of(new UpdateBuilder() {{ subCategory = INVALID_SUB_CATEGORY_LABEL; }}.build(), "subCategory not in vocabulary"),
                Arguments.of(new UpdateBuilder() {{ orderDate = INVALID_ORDER_DATE; }}.build(), "orderDate has invalid format"),
                Arguments.of(new UpdateBuilder() {{ deliveryDate = INVALID_DELIVERY_DATE; }}.build(), "deliveryDate has invalid format"),
                Arguments.of(new UpdateBuilder() {{ contactEmail = INVALID_CONTACT_EMAIL; }}.build(), "contactEmail has invalid format"),

                // Mandatory: Name
                Arguments.of(new UpdateBuilder() {{ name = null; }}.build(), "name is missing (null)"),
                Arguments.of(new UpdateBuilder() {{ name = BLANK_STRING; }}.build(), "name is blank"),
                Arguments.of(new UpdateBuilder() {{ name = EMPTY_STRING; }}.build(), "name is empty"),

                // Mandatory: MainCategory
                Arguments.of(new UpdateBuilder() {{ mainCategory = null; }}.build(), "mainCategory is missing (null)"),
                Arguments.of(new UpdateBuilder() {{ mainCategory = BLANK_STRING; }}.build(), "mainCategory is blank"),
                Arguments.of(new UpdateBuilder() {{ mainCategory = EMPTY_STRING; }}.build(), "mainCategory is empty"),

                // Mandatory: SubCategory
                Arguments.of(new UpdateBuilder() {{ subCategory = null; }}.build(), "subCategory is missing (null)"),
                Arguments.of(new UpdateBuilder() {{ subCategory = BLANK_STRING; }}.build(), "subCategory is blank"),
                Arguments.of(new UpdateBuilder() {{ subCategory = EMPTY_STRING; }}.build(), "subCategory is empty"),

                // Mandatory: Reason
                Arguments.of(new UpdateBuilder() {{ reason = null; }}.build(), "reason is missing (null)"),
                Arguments.of(new UpdateBuilder() {{ reason = BLANK_STRING; }}.build(), "reason is blank"),
                Arguments.of(new UpdateBuilder() {{ reason = EMPTY_STRING; }}.build(), "reason is empty"),

                // Mandatory: OrderedBy
                Arguments.of(new UpdateBuilder() {{ orderedBy = null; }}.build(), "orderedBy is missing (null)"),
                Arguments.of(new UpdateBuilder() {{ orderedBy = BLANK_STRING; }}.build(), "orderedBy is blank"),
                Arguments.of(new UpdateBuilder() {{ orderedBy = EMPTY_STRING; }}.build(), "orderedBy is empty"),

                // Mandatory: OrderDate
                Arguments.of(new UpdateBuilder() {{ orderDate = null; }}.build(), "orderDate is missing (null)"),
                Arguments.of(new UpdateBuilder() {{ orderDate = BLANK_STRING; }}.build(), "orderDate is blank"),
                Arguments.of(new UpdateBuilder() {{ orderDate = EMPTY_STRING; }}.build(), "orderDate is empty")
        );
    }

    // --- Builders for DTOs ---

    /**
     * Builder for creating OrderCreationDto instances with predefined values.
     */
    static class CreationBuilder {
        String name = DUMMY_NAME;
        String mainCategory = VALID_MAIN_CATEGORY_LABEL;
        String subCategory = VALID_SUB_CATEGORY_LABEL;
        String details = DUMMY_DETAILS;
        String frequency = DUMMY_FREQUENCY;
        String contactPerson = DUMMY_CONTACT_PERSON;
        String contactEmail = VALID_CONTACT_EMAIL;
        String contactPhone = DUMMY_CONTACT_PHONE;
        String reason = DUMMY_REASON;
        String orderMethod = DUMMY_ORDER_METHOD;
        String orderedBy = DUMMY_ORDERED_BY;
        String orderDate = VALID_ORDER_DATE;
        String deliveryDate = VALID_DELIVERY_DATE;
        String orderComment = DUMMY_COMMENT;
        String supplierId = DUMMY_SUPPLIER_ID;

        /**
         * Builds and returns a new OrderCreationDto with the current configuration.
         *
         * @return OrderCreationDto instance
         */
        OrderCreationDto build() {
            return new OrderCreationDto(name, mainCategory, subCategory, details, frequency, contactPerson, contactEmail,
                    contactPhone, reason, orderMethod, orderedBy, orderDate, deliveryDate, orderComment, supplierId);
        }
    }

    /**
     * Builder for creating OrderUpdateDto instances with predefined values.
     */
    static class UpdateBuilder {
        String name = DUMMY_NAME;
        String mainCategory = VALID_MAIN_CATEGORY_LABEL;
        String subCategory = VALID_SUB_CATEGORY_LABEL;
        String details = DUMMY_DETAILS;
        String frequency = DUMMY_FREQUENCY;
        String contactPerson = DUMMY_CONTACT_PERSON;
        String contactEmail = VALID_CONTACT_EMAIL;
        String contactPhone = DUMMY_CONTACT_PHONE;
        String reason = DUMMY_REASON;
        String orderMethod = DUMMY_ORDER_METHOD;
        String orderedBy = DUMMY_ORDERED_BY;
        String orderDate = VALID_ORDER_DATE;
        String deliveryDate = VALID_DELIVERY_DATE;
        String orderComment = DUMMY_COMMENT;

        /**
         * Builds and returns a new OrderUpdateDto with the current configuration.
         *
         * @return OrderUpdateDto instance
         */
        OrderUpdateDto build() {
            return new OrderUpdateDto(name, mainCategory, subCategory, details, frequency, contactPerson, contactEmail,
                    contactPhone, reason, orderMethod, orderedBy, orderDate, deliveryDate, orderComment);
        }
    }
}
