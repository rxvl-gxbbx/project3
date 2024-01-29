package com.rxvlvxr.repositories;

import com.rxvlvxr.models.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// репозиторий для сущности measurement
@Repository
public interface MeasurementsRepository extends JpaRepository<Measurement, Integer> {
    // считает количество строк где значение raining == true
    int countMeasurementByRainingIsTrue();
}
