package io.github.supplierratingsoftware.supplierratingbackend.dto.api;

import io.github.supplierratingsoftware.supplierratingbackend.constant.api.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

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
 * @param name              The supplier's name (Mandatory).
 * @param customerNumber    The customer number at that supplier (Mandatory).
 * @param addition          The supplier's additional address information.
 * @param street            The supplier's street (Mandatory).
 * @param poBox             The supplier's PO Box.
 * @param country           The supplier's country (Mandatory).
 * @param zipCode           The supplier's zip code (Mandatory).
 * @param city              The supplier's city (Mandatory).
 * @param website           The supplier's website.
 * @param email             The supplier's email.
 * @param phoneNumber       The supplier's phone number.
 * @param vatId             The supplier's VAT ID (Mandatory).
 * @param conditions        The supplier's conditions (Mandatory).
 * @param customerInfo      Additional customer information.
 */
public record SupplierCreationDto(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Customer number is required")
        String customerNumber,

        String addition, // Optional

        @NotBlank(message = "Street is required")
        String street, // Optional

        String poBox, // Optional

        @NotBlank(message = "Country is required")
        @Pattern(regexp = ValidationConstants.COUNTRY_REGEX, message = ValidationConstants.COUNTRY_MESSAGE)
        String country,

        @NotBlank(message = "Zip code is required")
        String zipCode,

        @NotBlank(message = "City is required")
        String city,

        @Pattern(regexp = ValidationConstants.URL_REGEX, message = ValidationConstants.URL_MESSAGE)
        String website, // Optional

        @Email(message = "Email must be a valid email address")
        String email, // Optional

        String phoneNumber, // Optional

        @NotBlank(message = "VAT ID is required")
        String vatId, // Optional

        @NotBlank(message = "Conditions are required")
        String conditions, // Optional

        String customerInfo // Optional
) {
}