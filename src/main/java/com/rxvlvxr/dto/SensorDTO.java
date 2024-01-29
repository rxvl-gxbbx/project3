package com.rxvlvxr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// логика аналогична остальным DTO
public class SensorDTO {
    // валидация
    @Size(min = 3, max = 30, message = "Поле должно содержать от 3 до 30 символов")
    @NotBlank(message = "Название не должно быть пустым")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
