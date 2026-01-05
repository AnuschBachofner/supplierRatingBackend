package io.github.supplierratingsoftware.supplierratingbackend.validation.annotation;

import io.github.supplierratingsoftware.supplierratingbackend.validation.validator.OpenBisVocabularyValidator;
import io.github.supplierratingsoftware.supplierratingbackend.constant.openbis.OpenBisSchemaConstants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validating OpenBIS vocabulary terms.
 * This Annotation is used to validate that a field contains a valid OpenBIS vocabulary term.
 * <p>
 * The {@code type} element must be set to one of the supported constant values defined in
 * {@link OpenBisSchemaConstants}. Supplying any other value may cause validation to fail.
 * </p>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OpenBisVocabularyValidator.class)
public @interface OpenBisVocabulary {

    /**
     * The OpenBIS vocabulary term type to validate against.
     * <p>
     * Must be one of the supported constant values defined in {@link OpenBisSchemaConstants}.
     * </p>
     */
    String type();

    String message() default "Value is not a valid OpenBIS vocabulary term";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
