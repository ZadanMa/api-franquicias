package com.proyecto.interno.api_franquicias.domain.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NoNumbersValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoNumbers {
    String message() default "El campo no debe contener números";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}