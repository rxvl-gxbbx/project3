package com.rxvlvxr.controllers;

import com.rxvlvxr.dto.SensorDTO;
import com.rxvlvxr.models.Sensor;
import com.rxvlvxr.services.SensorsService;
import com.rxvlvxr.util.ErrorResponse;
import com.rxvlvxr.util.ErrorUtil;
import com.rxvlvxr.util.SensorNameValidator;
import com.rxvlvxr.util.SensorNotCreatedException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/sensors")
public class SensorsController {
    private final SensorsService sensorsService;
    private final SensorNameValidator sensorNameValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public SensorsController(SensorsService sensorsService, SensorNameValidator sensorNameValidator, ModelMapper modelMapper) {
        this.sensorsService = sensorsService;
        this.sensorNameValidator = sensorNameValidator;
        this.modelMapper = modelMapper;
    }

    // метод для регистрации сенсора, аналогичный по логике с методом measure из класса MeasurementsController
    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody @Valid SensorDTO sensorDTO, BindingResult bindingResult) {
        sensorNameValidator.validate(convertToSensor(sensorDTO), bindingResult);

        if (bindingResult.hasErrors()) throw new SensorNotCreatedException(ErrorUtil.getErrorMsg(bindingResult));

        sensorsService.save(convertToSensor(sensorDTO));

        return ResponseEntity.status(HttpStatus.CREATED).body("Sensor is created");
    }

    // ловим исключения
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(SensorNotCreatedException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), LocalDateTime.now(ZoneId.systemDefault())), HttpStatus.BAD_REQUEST);
    }

    // конвертация из SensorDTO в Sensor
    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }
}
