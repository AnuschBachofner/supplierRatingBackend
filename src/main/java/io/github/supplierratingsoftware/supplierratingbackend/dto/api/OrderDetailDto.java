package io.github.supplierratingsoftware.supplierratingbackend.dto.api;

/**
 * Represents an Order Detail DTO.
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
 *         "contactPhone": "+41 12 345 67 89",
 *         "reason": "Quality assurance",
 *         "orderMethod": "Email",
 *         "orderedBy": "Jane Doe",
 *         "orderDate": "2021-01-01",
 *         "deliveryDate": "2021-01-02",
 *         "orderComment": "None",
 *         "id": "12345",
 *         "code": "string",
 *         "ratingStatus": "Pending",
 *         "supplierId": "string",
 *         "supplierName": "Acme Corp",
 *         "ratingId": "string"
 *     }
 * </pre>
 *
 * @param name          The name of the order.
 * @param mainCategory  The main category of the order ("Beschaffung" or "Dienstleistung").
 * @param subCategory   The subcategory of the order.
 * @param details       The exact description of the order.
 * @param frequency     The frequency of the order.
 * @param contactPerson The contact person for this order.
 * @param contactEmail  The contact email address for this order.
 * @param contactPhone  The contact phone number for this order.
 * @param reason        The reason for this order.
 * @param orderMethod   The method of order (e.g., "Email", "Phone", "In-Person").
 * @param orderedBy     The name of the purchaser.
 * @param orderDate     The date of the order.
 * @param deliveryDate  The date of delivery.
 * @param orderComment  General comment about the order.
 * @param id            The openBIS permID of the order.
 * @param code          The openBIS code of the order.
 * @param ratingStatus  The status of the order's rating.
 * @param supplierId    The openBIS permID of the supplier (Parent of the order).
 * @param supplierName  The name of the supplier (Parent of the order).
 * @param ratingId      The openBIS permID of the rating (Children of the order).
 */
public record OrderDetailDto(
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
        String orderDate,
        String deliveryDate,
        String orderComment,
        String id,
        String code,
        String ratingStatus,
        String supplierId,
        String supplierName,
        String ratingId
) {
}
