package com.example.demo.domain.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BooleanConstraintValidator implements ConstraintValidator<BooleanConstraint, String> {
    @Override
    public boolean isValid(String field, ConstraintValidatorContext context) {
        return field == null || field.toLowerCase().equals("true") || field.toLowerCase().equals("false");
    }
}
