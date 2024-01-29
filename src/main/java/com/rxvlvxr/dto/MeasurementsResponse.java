package com.rxvlvxr.dto;

import java.util.List;

// следую best practice и оборачиваю список объектов в отдельный класс
// этот класс нужен для ответа в JSON формате
public class MeasurementsResponse {
    // список объектов
    private List<MeasurementDTO> measurements;

    public MeasurementsResponse(List<MeasurementDTO> measurements) {
        this.measurements = measurements;
    }

    public List<MeasurementDTO> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<MeasurementDTO> measurements) {
        this.measurements = measurements;
    }
}
