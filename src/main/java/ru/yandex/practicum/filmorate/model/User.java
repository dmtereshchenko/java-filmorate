package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class User {

    private int id;
    @Email
    private String email;
    private String name;
    @NotBlank
    @NonNull
    private String login;
    @Past
    private LocalDate birthday;
    private Set<Integer> friendList = new HashSet<>();

    @JsonCreator
    public User(String login, String name, String email, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(int id, String login, String email, String name, LocalDate birthday) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriend(int id) {
        friendList.add(id);
    }

    public void removeFriend(int id) {
        friendList.remove(id);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("login", login);
        values.put("e_mail", email);
        values.put("name", name);
        values.put("birthday", birthday);
        return values;
    }
}