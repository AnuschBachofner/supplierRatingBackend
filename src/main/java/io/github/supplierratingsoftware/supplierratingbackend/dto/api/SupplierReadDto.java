package io.github.supplierratingsoftware.supplierratingbackend.dto.api;

/**
 * Represents a supplier.
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *   {
 *     "name": "Acme Corp",
 *     "customerNumber": "KD-1234",
 *     "addition": "Building B",
 *     "street": "Industrial Ave 5",
 *     "poBox": "string",
 *     "country": "CH",
 *     "zipCode": "8005",
 *     "city": "Zurich",
 *     "website": "https://acme.com",
 *     "email": "string",
 *     "phoneNumber": "string",
 *     "vatId": "string",
 *     "conditions": "string",
 *     "customerInfo": "string",
 *     "id": "string",
 *     "code": "string",
 *     "stats": {
 *       "avgQuality": 4.5,
 *       "avgCost": 3.8,
 *       "avgReliability": 5,
 *       "avgAvailability": 4.2,
 *       "avgTotal": 4.4,
 *       "totalRatingCount": 12
 *     }
 *   }
 * </pre>
 *
 * @param name           The supplier's name.
 * @param customerNumber The customer number of the supplier.
 * @param addition       The supplier's additional address information.
 * @param street         The supplier's street.
 * @param poBox          The supplier's PO Box.
 * @param country        The supplier's country.
 * @param zipCode        The supplier's ZIP code.
 * @param city           The supplier's city.
 * @param website        The supplier's website.
 * @param email          The supplier's email address.
 * @param phoneNumber    The supplier's phone number.
 * @param vatId          The supplier's VAT ID.
 * @param conditions     The conditions at this supplier.
 * @param customerInfo   Additional information about the supplier.
 * @param id             The openBIS permID of the supplier.
 * @param code           The openBIS code of the supplier.
 * @param stats          The supplier's statistics (Rating).
 * @see RatingStatsDto
 */
public record SupplierReadDto(
        String name,
        String customerNumber,
        String addition,
        String street,
        String poBox,
        String country,
        String zipCode,
        String city,
        String website,
        String email,
        String phoneNumber,
        String vatId,
        String conditions,
        String customerInfo,
        String id,
        String code,
        RatingStatsDto stats
) {
}
