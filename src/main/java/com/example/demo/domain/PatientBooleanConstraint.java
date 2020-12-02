package com.example.demo.domain;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PatientBooleanConstraintValidator.class)

public @interface PatientBooleanConstraint {
    String message() default "должнен быть типом boolean";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
