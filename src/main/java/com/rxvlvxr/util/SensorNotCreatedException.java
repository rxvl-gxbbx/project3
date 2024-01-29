package com.rxvlvxr.util;

// исключение, которое будет вызываться при ошибке валидации
public class SensorNotCreatedException extends RuntimeException {
    public SensorNotCreatedException(String message) {
        super(message);
    }
}
