package com.application.demo.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EducationDateValidatorImpl.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EducationDateValidator {
    String message() default "Start date must be less than end date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

