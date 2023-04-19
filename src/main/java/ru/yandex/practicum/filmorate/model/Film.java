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

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, int genre, int mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = Duration.ofMinutes(duration);
        if(genre > 0) {
            this.genres.add(new Genre(genre));
        }
        if (mpa > 0) {
            this.mpa = new Mpa(mpa);
        }
    }



    public Film(int id, String name, String description, LocalDate releaseDate, int duration, int genre, int categoryId,
                String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = Duration.ofMinutes(duration);
        this.genres.add(new Genre(genre));
        this.mpa = new Mpa(categoryId, categoryName);
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
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration.toMinutes());
        if (genres.size() > 0) {
            values.put("genre_id", genres.get(0).getId());
        }
        if (mpa.getId() > 0) {
            values.put("category_id", mpa.getId());
        }
        return values;
    }
}