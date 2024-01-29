package com.rxvlvxr.util;

import com.rxvlvxr.models.Sensor;
import com.rxvlvxr.repositories.SensorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SensorNameValidator implements Validator {
    private final SensorsRepository sensorsRepository;

    @Autowired
    public SensorNameValidator(SensorsRepository sensorsRepository) {
        this.sensorsRepository = sensorsRepository;
    }

    // класс должен соответствовать классу Sensor
    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Sensor sensor = (Sensor) target;

        // название сенсора должно быть уникальным
        sensorsRepository.findByName(sensor.getName()).ifPresent(sens -> errors.rejectValue
                ("name", "", "Сенсор с таким названием уже существует")
        );
    }
}
