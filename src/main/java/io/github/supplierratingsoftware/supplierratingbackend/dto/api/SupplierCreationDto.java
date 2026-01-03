package io.github.supplierratingsoftware.supplierratingbackend.dto.api;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents a supplier creation request.
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "name": "",
 *         "customerNumber": "",
 *         "addition": "",
 *         "street": "",
 *         "poBox": "",
 *         "country": "",
 *         "zipCode": "",
 *         "city": "",
 *         "website": "",
 *         "email": "",
 *         "phoneNumber": "",
 *         "vatId": "",
 *         "conditions": "",
 *         "customerInfo": ""
 *     }
 * </pre>
 *
 * @param name              The supplier's name.
 * @param customerNumber    The customer number at that supplier.
 * @param addition          The supplier's additional address information.
 * @param street            The supplier's street.
 * @param poBox             The supplier's PO Box.
 * @param country           The supplier's country.
 * @param zipCode           The supplier's zip code.
 * @param city              The supplier's city.
 * @param website           The supplier's website.
 * @param email             The supplier's email.
 * @param phoneNumber       The supplier's phone number.
 * @param vatId             The supplier's VAT ID.
 * @param conditions        The supplier's conditions.
 * @param customerInfo      Additional customer information.
 */
public record SupplierCreationDto(
        @NotBlank(message = "Name is required")
        String name,

        String customerNumber, // Optional

        String addition, // Optional

        String street, // Optional

        String poBox, // Optional

        @NotBlank(message = "Country is required")
        String country,

        @NotBlank(message = "Zip code is required")
        String zipCode,

        @NotBlank(message = "City is required")
        String city,

        String website, // Optional

        String email, // Optional

        String phoneNumber, // Optional

        String vatId, // Optional

        String conditions, // Optional

        String customerInfo // Optional
) {
}