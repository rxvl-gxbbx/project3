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

// создание REST контроллера
@RestController
@RequestMapping("/measurements")
public class MeasurementsController {

    private final MeasurementsService measurementsService;
    private final MeasurementValidator measurementValidator;
    private final ModelMapper modelMapper;

    // внедряем необходимые зависимости
    @Autowired
    public MeasurementsController(MeasurementsService measurementsService, MeasurementValidator measurementValidator, ModelMapper modelMapper) {
        this.measurementsService = measurementsService;
        this.measurementValidator = measurementValidator;
        this.modelMapper = modelMapper;
    }

    /*
    обрабатывает POST запрос по адресу /measurements/add
    метод для добавления измерений сенсора, который при успешной работе (корректном запросе):
    1) сохраняет запись в базу данных
    2) возвращает HTTP Status 201
    3) возвращает строку в теле ответа "Measurement is created"
    */
    @PostMapping("/add")
    public ResponseEntity<String> measure(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult) {
        // валидируем входные данные
        measurementValidator.validate(convertToMeasurement(measurementDTO), bindingResult);

        // проверяем есть ли ошибки в запросе, если есть, то выбрасываем исключение
        if (bindingResult.hasErrors()) throw new MeasurementNotCreatedException(ErrorUtil.getErrorMsg(bindingResult));

        // сохраняем измерение в базу данных
        measurementsService.save(convertToMeasurement(measurementDTO));

        return ResponseEntity.status(HttpStatus.CREATED).body("Measurement is created");
    }

    /*
    метод, который обрабатывает GET запрос и возвращает объект типа MeasurementsResponse
    ответ включает в себя список всех измерений, содержащийся в базе данных
    */
    @GetMapping
    public MeasurementsResponse index() {
        return new MeasurementsResponse(measurementsService.findAll().stream()
                // конвертируем каждый объект в объект MeasurementDTO
                .map(this::convertToMeasurementDTO)
                // собираем в объект типа List
                .collect(Collectors.toList()));
    }

    /*
    метод обрабатывает GET запрос и возвращает количество дней когда был дождь (где значение raining == true)
    */
    @GetMapping("/rainyDaysCount")
    public int rainyDaysIndex() {
        return measurementsService.countMeasurementByRainingIsTrue();
    }

    /*
    метод обрабатывает исключения, возвращает ответ в виде объекта типа ErrorResponse и HTTP Status 400
     */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MeasurementNotCreatedException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), LocalDateTime.now(ZoneId.systemDefault())), HttpStatus.BAD_REQUEST);
    }

    // конвертация объекта типа MeasurementDTO в Measurement
    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    // конвертация объекта типа Measurement в MeasurementDTO
    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }
}
