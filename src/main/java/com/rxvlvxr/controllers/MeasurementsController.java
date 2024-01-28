package com.rxvlvxr.controllers;

import com.rxvlvxr.dto.MeasurementDTO;
import com.rxvlvxr.dto.MeasurementsResponse;
import com.rxvlvxr.models.Measurement;
import com.rxvlvxr.services.MeasurementsService;
import com.rxvlvxr.util.ErrorResponse;
import com.rxvlvxr.util.ErrorUtil;
import com.rxvlvxr.util.MeasurementNotCreatedException;
import com.rxvlvxr.util.MeasurementValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {

    private final MeasurementsService measurementsService;
    private final MeasurementValidator measurementValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementsController(MeasurementsService measurementsService, MeasurementValidator measurementValidator, ModelMapper modelMapper) {
        this.measurementsService = measurementsService;
        this.measurementValidator = measurementValidator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<String> measure(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult) {
        measurementValidator.validate(convertToMeasurement(measurementDTO), bindingResult);

        if (bindingResult.hasErrors()) throw new MeasurementNotCreatedException(ErrorUtil.getErrorMsg(bindingResult));

        measurementsService.save(convertToMeasurement(measurementDTO));

        return ResponseEntity.status(HttpStatus.CREATED).body("Measurement is created");
    }

    @GetMapping
    public MeasurementsResponse index() {
        return new MeasurementsResponse(measurementsService.findAll().stream().map(this::convertToMeasurementDTO).collect(Collectors.toList()));
    }

    @GetMapping("/rainyDaysCount")
    public int rainyDaysIndex() {
        return measurementsService.countMeasurementByRainingIsTrue();
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MeasurementNotCreatedException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), LocalDateTime.now(ZoneId.systemDefault())), HttpStatus.BAD_REQUEST);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }
}
