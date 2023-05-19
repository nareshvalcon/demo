package com.application.demo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.application.demo.entity.Education;

public class EducationDateValidatorImpl implements ConstraintValidator<EducationDateValidator, Education> {

    @Override
    public void initialize(EducationDateValidator constraintAnnotation) {
    }

    @Override
    public boolean isValid(Education education, ConstraintValidatorContext context) {
        // Consider empty end date as valid. Add your own business rules as required.
        if (Integer.toString(education.getEndYear()) == null) {
            return true;
        }

        // Check that start date is before end date
        return Integer.toString(education.getStartYear()).compareTo(Integer.toString(education.getEndYear())) < 0;
    }
}

