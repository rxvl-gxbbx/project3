package com.rxvlvxr.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

// данный класс нужен для создания сообщения об ошибке
public class ErrorUtil {
    public static String getErrorMsg(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();

        for (FieldError error : errors)
            errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");

        return errorMessage.toString();
    }
}
