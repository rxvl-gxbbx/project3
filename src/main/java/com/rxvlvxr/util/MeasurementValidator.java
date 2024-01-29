package com.rxvlvxr.util;

import com.rxvlvxr.models.Measurement;
import com.rxvlvxr.models.Sensor;
import com.rxvlvxr.repositories.SensorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

// валидация Measurement
@Component
public class MeasurementValidator implements Validator {

    private final SensorsRepository sensorsRepository;

    @Autowired
    public MeasurementValidator(SensorsRepository sensorsRepository) {
        this.sensorsRepository = sensorsRepository;
    }

    // говорим что объект должен быть класса Measurement
    @Override
    public boolean supports(Class<?> clazz) {
        return Measurement.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Measurement measurement = (Measurement) target;
        Optional<Sensor> sensorFromMeasurement = Optional.ofNullable(measurement.getSensor());

        // валидация сенсора, сенсор должен быть найден, в ином случае валидация не пройдет
        if (sensorFromMeasurement.isPresent() && sensorsRepository.findByName(sensorFromMeasurement.get().getName()).isEmpty())
            errors.rejectValue("sensor", "", "Сенсор с таким названием не найден");
    }
}
