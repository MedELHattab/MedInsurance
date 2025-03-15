package org.example.medinsurance.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class MinAgeValidator implements ConstraintValidator<MinAge, LocalDate> {

    private int minAge;

    @Override
    public void initialize(MinAge constraintAnnotation) {
        this.minAge = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate birthday, ConstraintValidatorContext context) {
        // Null values are handled by @NotNull annotation
        if (birthday == null) {
            return true;
        }

        LocalDate today = LocalDate.now();
        Period period = Period.between(birthday, today);

        return period.getYears() >= minAge;
    }
}