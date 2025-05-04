package com.nlu.cdweb.BookStore.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.EmailValidator;

public class UsernameOrEmailValidator implements ConstraintValidator<ValidUsernameOrEmail, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        // Nếu có @ thì phải đúng dạng email
        if (value.contains("@")) {
            return EmailValidator.getInstance().isValid(value);
        }
        // Nếu không có @ thì chỉ cần không rỗng là được
        return true;
    }
}

