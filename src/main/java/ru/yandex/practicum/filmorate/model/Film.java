package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

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
    @JsonProperty("mpa")
    private Mpa mpa = new Mpa();

    @JsonProperty("genres")
    private List<Genre> genres = new ArrayList<>();

    @JsonCreator
    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = Duration.ofMinutes(duration);
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = Duration.ofMinutes(duration);
    }

    public Film(String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = Duration.ofMinutes(duration);
        this.mpa = mpa;
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = Duration.ofMinutes(duration);
        this.mpa = mpa;
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

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        if (getId() != 0) {
            values.put("film_id", getId());
        }
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration.toMinutes());
        if (mpa.getId() > 0) {
            values.put("category_id", mpa.getId());
        }
        return values;
    }

    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }
}