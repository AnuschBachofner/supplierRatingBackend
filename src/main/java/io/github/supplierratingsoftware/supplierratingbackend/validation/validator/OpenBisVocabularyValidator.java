package io.github.supplierratingsoftware.supplierratingbackend.validation.validator;

import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisSchemaConstants;
import io.github.supplierratingsoftware.supplierratingbackend.validation.annotation.OpenBisVocabulary;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

/**
 * Validator for {@link OpenBisVocabulary}.
 *
 * Validates that the provided string value matches one of the allowed values from the specified OpenBIS vocabulary.
 */
public class OpenBisVocabularyValidator implements ConstraintValidator<OpenBisVocabulary, String> {
    private String type;

    /**
     * Initializes the validator with the specified constraint annotation.
     *
     * @param constraintAnnotation The constraint annotation to initialize with.
     */
    @Override
    public void initialize(OpenBisVocabulary constraintAnnotation) {
        this.type = constraintAnnotation.type();
    }

    /**
     * Validates the provided string value against the allowed values for the specified vocabulary type.
     * @param value The value to validate.
     * @param context The validation context.
     * @return True if the value is valid, false otherwise.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Set<String> allowedValues = switch (type) {
            case OpenBisSchemaConstants.MAIN_CATEGORY_ORDER_PROPERTY ->
                // Vocabularies for main categories
                OpenBisSchemaConstants.ORDER_MAIN_CATEGORY_MAPPING_LABEL_TO_CODE.keySet();

            case OpenBisSchemaConstants.SUB_CATEGORY_ORDER_PROPERTY ->
                // Vocabularies for sub categories
                OpenBisSchemaConstants.ORDER_SUB_CATEGORY_MAPPING_LABEL_TO_CODE.keySet();

            case OpenBisSchemaConstants.COUNTRY_SUPPLIER_PROPERTY ->
                // Vocabularies for supplier countries
                OpenBisSchemaConstants.SUPPLIER_COUNTRY_MAPPING_LABEL_TO_CODE.keySet();

            default ->
                // Default case (-> invalid type)
                null;
        };
        if (allowedValues == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid OpenBisVocabulary type configuration:" + type).addConstraintViolation();
            return false;
        }

        boolean isValid = allowedValues.contains(value);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    // List allowed values
                    "Must be one of: " + String.join(", ", allowedValues)
            ).addConstraintViolation();
        }

        return isValid;
    }
}
