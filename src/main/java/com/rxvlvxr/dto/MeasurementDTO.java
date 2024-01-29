package com.rxvlvxr.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// класс для Jackson (JSON) входных/выходных данных
public class MeasurementDTO {
    // аннотация для валидации значений
    @Min(value = (-100), message = "Минимальная температура - (-100) градусов")
    @Max(value = 100, message = "Максимальная температура - 100 градусов")
    @NotNull(message = "Значение не должно быть пустым")
    private Double value;
    @NotNull(message = "Значение не должно быть пустым")
    private Boolean raining;
    @NotNull(message = "Значение не должно быть пустым")
    private SensorDTO sensor;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
}
