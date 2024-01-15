package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder
public class Director {
    private Integer id;
    @NotBlank
    private String name;

    public Director(String name) {
        this.name = name;
    }
}
