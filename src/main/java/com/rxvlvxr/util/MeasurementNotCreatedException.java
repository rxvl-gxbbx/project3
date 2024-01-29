package com.rxvlvxr.util;

// исключение при неудачной валидации
public class MeasurementNotCreatedException extends RuntimeException {
    public MeasurementNotCreatedException(String message) {
        super(message);
    }
}
