package org.example.medinsurance.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinAgeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinAge {
    String message() default "Must be at least {value} years old";

    int value() default 18;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}