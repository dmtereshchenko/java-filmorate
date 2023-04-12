package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film implements Comparable<Film> {

    private int id;
    @NotBlank
    @NonNull
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private Set<Integer> userLikes = new HashSet<>();
    private FilmCategory filmCategory;
    private String genre;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = Duration.ofMinutes(duration);
    }

    public int getDuration() {
        return (int) this.duration.toMinutes();
    }

    public void addLike(int userId) {
        userLikes.add(userId);
    }

    public void removeLike(int userId) {
        userLikes.remove(userId);
    }

    @Override
    public int compareTo(Film o) {
        if (this.userLikes.size() < o.getUserLikes().size()) {
            return -1;
        } else if (this.userLikes.size() > o.getUserLikes().size()) {
            return 1;
        } else {
            return 0;
        }
    }
}