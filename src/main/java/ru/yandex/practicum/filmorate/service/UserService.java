package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    List<User> returnFriendList(int id);

    List<User> findMutualFriends(int userId, int friendId);

    User getUserFromStorage(int id);

    boolean checkUser(int id);

    List<User> getAllUsers();

    int addUser(User user);

    void updateUser(User user);
}

