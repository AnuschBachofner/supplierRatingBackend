package io.github.supplierratingsoftware.supplierratingbackend.dto.api;

import io.github.supplierratingsoftware.supplierratingbackend.constant.api.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Represents an update request for a supplier.
 *
 * <p><strong>JSON Example:</strong></p>
 * <pre>
 *     {
 *         "name": "Acme Corp",
 *         "customerNumber": "KD-1234",
 *         "addition": "Suite 123",
 *         "street": "Industrial Ave 5",
 *         "poBox": "string",
 *         "country": "CH",
 *         "zipCode": "8005",
 *         "city": "Zurich",
 *         "website": "https://acme.com",
 *         "email": "string",
 *         "phoneNumber": "string",
 *         "vatId": "string",
 *         "conditions": "string",
 *         "customerInfo": "string"
 *     }
 * </pre>
 *
 * @param name            The supplier's name.
 * @param customerNumber  The customer number at that supplier.
 * @param addition        The supplier's additional address information.
 * @param street          The supplier's street.
 * @param poBox           The supplier's postal box.
 * @param country         The supplier's country.
 * @param zipCode         The supplier's postal code.
 * @param city            The supplier's city.
 * @param website         The supplier's website URL.
 * @param email           The supplier's email address.
 * @param phoneNumber     The supplier's phone number.
 * @param vatId           The supplier's VAT identification number.
 * @param conditions      The supplier's conditions or terms.
 * @param customerInfo    Additional customer information.
 */
public record SupplierUpdateDto(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Customer number is required")
        String customerNumber,

        String addition,

        @NotBlank(message = "Street is required")
        String street,

        String poBox,

        @NotBlank(message = "Country is required")
        @Pattern(regexp = ValidationConstants.COUNTRY_REGEX, message = ValidationConstants.COUNTRY_MESSAGE)
        String country,

        @NotBlank(message = "Postal code is required")
        String zipCode,

        @NotBlank(message = "City is required")
        String city,

        @Pattern(regexp = ValidationConstants.URL_REGEX, message = ValidationConstants.URL_MESSAGE)
        String website,

        @Email(message = "Email must be a valid email address")
        String email,

        String phoneNumber,

        @NotBlank(message = "VAT ID is required")
        String vatId,

        @NotBlank(message = "Conditions are required")
        String conditions,

        String customerInfo
) {
}