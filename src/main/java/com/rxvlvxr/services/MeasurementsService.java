package com.rxvlvxr.services;

import com.rxvlvxr.models.Measurement;
import com.rxvlvxr.repositories.MeasurementsRepository;
import com.rxvlvxr.repositories.SensorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
// по умолчанию все транзакции будут только для чтения
@Transactional(readOnly = true)
public class MeasurementsService {
    private final MeasurementsRepository measurementsRepository;
    private final SensorsRepository sensorsRepository;

    // внедряем зависимости
    @Autowired
    public MeasurementsService(MeasurementsRepository measurementsRepository, SensorsRepository sensorsRepository) {
        this.measurementsRepository = measurementsRepository;
        this.sensorsRepository = sensorsRepository;
    }

    // явно указываем что транзакция вносит изменения в таблицу
    @Transactional
    public void save(Measurement measurement) {
        sensorsRepository.findByName(measurement.getSensor().getName()).ifPresent(sensor -> {
            measurement.setSensor(sensor);
            sensor.getMeasurements().add(measurement);
        });

        // устанавливаем текущее время
        measurement.setMeasuredAt(LocalDateTime.now(ZoneId.systemDefault()));

        measurementsRepository.save(measurement);
    }

    // находит все записи из таблицы measurement и возвращает их
    public List<Measurement> findAll() {
        return measurementsRepository.findAll();
    }

    // считает сколько дождливых дней было
    public int countMeasurementByRainingIsTrue() {
        return measurementsRepository.countMeasurementByRainingIsTrue();
    }
}
