package com.proyecto.interno.api_franquicias.domain.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoNumbersValidator implements ConstraintValidator<NoNumbers, String> {
    private static final String NO_NUMBERS_REGEX = "^[^0-9]*$";

    @Override
    public void initialize(NoNumbers constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(NO_NUMBERS_REGEX);
    }
}