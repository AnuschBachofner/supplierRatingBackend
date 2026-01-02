package io.github.supplierratingsoftware.supplierratingbackend.dto.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Represents an order creation request.
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "name": "Lab material Q1",
 *         "mainCategory": "BESCHAFFUNG",
 *         "subCategory": "MESSMITTEL",
 *         "details": "50x Precision scales",
 *         "frequency": "One-time",
 *         "contactPerson": "John Doe",
 *         "contactEmail": "john.doe@example.com",
 *         "contactPhone": "+1234567890",
 *         "reason": "New project requirements",
 *         "orderMethod": "Online",
 *         "orderedBy": "Jane Smith",
 *         "orderDate": "2023-01-01",
 *         "deliveryDate": "2023-01-15",
 *         "orderComment": "Please ship to warehouse A",
 *         "supplierId": "SUP-12345"
 *     }
 * </pre>
 *
 * @param name          The name/short description of the order (Mandatory).
 * @param mainCategory  The main category (e.g. "BESCHAFFUNG").
 * @param subCategory   The subcategory.
 * @param details       Detailed description.
 * @param frequency     The frequency (e.g. "One-time").
 * @param contactPerson The contact person.
 * @param contactEmail  The contact email.
 * @param contactPhone  The contact phone.
 * @param reason        The reason for the order.
 * @param orderMethod   The method of ordering.
 * @param orderedBy     The name of the purchaser.
 * @param orderDate     The date of the order (YYYY-MM-DD).
 * @param deliveryDate  The delivery date (YYYY-MM-DD).
 * @param orderComment  General comment.
 * @param supplierId    The PermID of the supplier this order belongs to (Mandatory).
 */
public record OrderCreationDto(
        @NotBlank(message = "Name is required")
        String name,

        String mainCategory,
        String subCategory,
        String details,
        String frequency,
        String contactPerson,
        String contactEmail,
        String contactPhone,
        String reason,
        String orderMethod,
        String orderedBy,

        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Order date must be in format YYYY-MM-DD")
        String orderDate,

        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Delivery date must be in format YYYY-MM-DD")
        String deliveryDate,

        String orderComment,

        @NotBlank(message = "Supplier ID is required")
        String supplierId
) {
}