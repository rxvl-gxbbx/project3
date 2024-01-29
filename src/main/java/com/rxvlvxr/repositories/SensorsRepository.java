package com.rxvlvxr.repositories;

import com.rxvlvxr.models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// репозиторий сущности sensor
@Repository
public interface SensorsRepository extends JpaRepository<Sensor, Integer> {
    // ищет по имени
    Optional<Sensor> findByName(String name);
}
