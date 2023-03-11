package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {

    private int id;
    @NotBlank
    @NonNull
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    private Duration duration;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = Duration.ofMinutes(duration);
    }

    public int getDuration() {
        return (int) this.duration.toMinutes();
    }
}