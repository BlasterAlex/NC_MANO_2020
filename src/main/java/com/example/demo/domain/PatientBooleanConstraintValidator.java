package com.example.demo.domain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PatientBooleanConstraintValidator implements ConstraintValidator<PatientBooleanConstraint, String> {
    @Override
    public boolean isValid(String field, ConstraintValidatorContext context) {
        return field == null || field.toLowerCase().equals("true") || field.toLowerCase().equals("false");
    }
}
