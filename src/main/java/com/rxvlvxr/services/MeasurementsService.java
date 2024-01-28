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
@Transactional(readOnly = true)
public class MeasurementsService {
    private final MeasurementsRepository measurementsRepository;
    private final SensorsRepository sensorsRepository;

    @Autowired
    public MeasurementsService(MeasurementsRepository measurementsRepository, SensorsRepository sensorsRepository) {
        this.measurementsRepository = measurementsRepository;
        this.sensorsRepository = sensorsRepository;
    }

    @Transactional
    public void save(Measurement measurement) {
        sensorsRepository.findByName(measurement.getSensor().getName()).ifPresent(sensor -> {
            measurement.setSensor(sensor);
            sensor.getMeasurements().add(measurement);
        });

        measurement.setMeasuredAt(LocalDateTime.now(ZoneId.systemDefault()));

        measurementsRepository.save(measurement);
    }

    public List<Measurement> findAll() {
        return measurementsRepository.findAll();
    }

    public int countMeasurementByRainingIsTrue() {
        return measurementsRepository.countMeasurementByRainingIsTrue();
    }
}
