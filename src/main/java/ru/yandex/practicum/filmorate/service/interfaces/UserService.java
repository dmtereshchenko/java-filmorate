package ru.yandex.practicum.filmorate.service.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    public void addFriend(int userId, int friendId);
    public void deleteFriend(int userId, int friendId);
    public List<User> returnFriendList(int id);
    public List<User> findMutualFriends(int userId, int friendId);
    public User getUserFromStorage(int id);
    public boolean checkUser(int id);
    public List<User> getAllUsers();
    public int addUser(User user);
    public void updateUser(User user);
}

