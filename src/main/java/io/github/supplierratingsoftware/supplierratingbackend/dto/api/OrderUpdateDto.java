package io.github.supplierratingsoftware.supplierratingbackend.dto.api;

import io.github.supplierratingsoftware.supplierratingbackend.constant.api.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Represents an update request for an Order.
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
 *         "orderComment": "Please ship to warehouse A"
 *     }
 * </pre>
 *
 * <p><strong>No supplier update:</strong>
 * The supplier relation cannot be changed, so {@code supplierId} is excluded from this DTO.</p>
 *
 * @param name          The name/short description of the order (Mandatory).
 * @param mainCategory  The main category (e.g. "Beschaffung") (Mandatory).
 * @param subCategory   The subcategory (e.g. "Messmittel") (Mandatory)
 * @param details       Detailed description.
 * @param frequency     The frequency (e.g. "One-time").
 * @param contactPerson The contact person.
 * @param contactEmail  The contact email.
 * @param contactPhone  The contact phone.
 * @param reason        The reason for the order (Mandatory).
 * @param orderMethod   The method of ordering.
 * @param orderedBy     The name of the purchaser (Mandatory).
 * @param orderDate     The date of the order (YYYY-MM-DD) (Mandatory).
 * @param deliveryDate  The delivery date (YYYY-MM-DD).
 * @param orderComment  General comment.
 */
public record OrderUpdateDto(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Main category is required")
        @Pattern(regexp = ValidationConstants.MAIN_CATEGORY_REGEX, message = ValidationConstants.MAIN_CATEGORY_MESSAGE)
        String mainCategory,

        @NotBlank(message = "Subcategory is required")
        @Pattern(regexp = ValidationConstants.SUB_CATEGORY_REGEX, message = ValidationConstants.SUB_CATEGORY_MESSAGE)
        String subCategory,

        String details, // optional

        String frequency, // optional

        String contactPerson, // optional

        @Email(message = "Contact email must be a valid email address")
        String contactEmail, // optional

        String contactPhone, // optional

        @NotBlank(message = "Reason is required")
        String reason,

        String orderMethod, // optional

        @NotBlank(message = "Ordered by is required")
        String orderedBy,

        @NotBlank(message = "Order date is required")
        @Pattern(regexp = ValidationConstants.DATE_REGEX, message = ValidationConstants.DATE_MESSAGE)
        String orderDate,

        @Pattern(regexp = ValidationConstants.DATE_REGEX, message = ValidationConstants.DATE_MESSAGE)
        String deliveryDate, // optional

        String orderComment // optional
) {
}
