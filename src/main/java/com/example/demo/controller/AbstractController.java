package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractController<EntryBean> {

    final Class<EntryBean> clazz;

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    protected AbstractController(Class<EntryBean> clazz) {
        this.clazz = clazz;
    }

    protected boolean notValidField(String fieldName, Object fieldValue, Errors errors) {
        Iterator<ConstraintViolation<EntryBean>> cvIter;
        if (fieldValue != null && (cvIter = validator.validateValue(
                clazz, fieldName, fieldValue).iterator()).hasNext()) {
            errors.rejectValue(fieldName, "invalid field", cvIter.next().getMessage());
            return true;
        }
        return false;
    }

    protected Object compareValue(Object oldVal, Object newVal) {
        if (newVal != null && !newVal.equals(oldVal)) {
            return newVal;
        }
        return oldVal;
    }

    protected ResponseEntity<String> printValidError(Errors errors) {
        FieldError field = errors.getFieldErrors().get(0);
        return new ResponseEntity<>(field.getField() + ": " +
                field.getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    public abstract Errors validate(EntryBean entry);

    public abstract ResponseEntity<List<EntryBean>> findAll();

    public abstract ResponseEntity<EntryBean> find(@PathVariable Long id);

    public abstract ResponseEntity<String> add(@RequestBody @Valid EntryBean entry, Errors error);

    public abstract ResponseEntity<String> delete(@PathVariable Long id);

    public abstract ResponseEntity<String> update(@PathVariable Long id, @RequestBody @Valid EntryBean entry, Errors errors);

    public abstract ResponseEntity<String> edit(@PathVariable Long id, @RequestBody EntryBean entry);
}
