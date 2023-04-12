package ru.yandex.practicum.filmorate.service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAL.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public class DBUserService implements UserService {

    private final UserDao dao;

    @Autowired
    public DBUserService(UserDao dao) {this.dao = dao;}

    @Override
    public void addFriend(int userId, int friendId) {

    }

    @Override
    public void deleteFriend(int userId, int friendId) {

    }

    @Override
    public List<User> returnFriendList(int id) {
        return null;
    }

    @Override
    public List<User> findMutualFriends(int userId, int friendId) {
        return null;
    }

    @Override
    public User getUserFromStorage(int id) {
        if (dao.findUserById(id).isPresent()) {
            return dao.findUserById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public boolean checkUser(int id) {
        return dao.findUserById(id).isPresent();
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    public void addUser(User user) {
        int id = dao.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        dao.updateUser(user);
    }
}
