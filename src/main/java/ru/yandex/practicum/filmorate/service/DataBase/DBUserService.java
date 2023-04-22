package ru.yandex.practicum.filmorate.service.DataBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.interfaces.UserService;
import ru.yandex.practicum.filmorate.storage.database.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.database.UserDao;

import java.util.*;

@Primary
@Service
public class DBUserService implements UserService {

    private final UserDao dao;
    private final FriendshipDao friendshipDao;

    @Autowired
    public DBUserService(UserDao dao, FriendshipDao friendshipDao) {
        this.dao = dao; this.friendshipDao = friendshipDao;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if (dao.getById(userId).isPresent() && dao.getById(friendId).isPresent()) {
            friendshipDao.add(userId, friendId);
        }
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        friendshipDao.remove(userId, friendId);
    }

    @Override
    public List<User> returnFriendList(int id) {
        List<Integer> friendIds = friendshipDao.getFriendList(id);
        List<User> friendList = new ArrayList<>();
        for (int i : friendIds) {
            if (dao.getById(i).isPresent()) {
                friendList.add(dao.getById(i).get());
            }
        }
        return friendList;
    }

    @Override
    public List<User> findMutualFriends(int userId, int friendId) {
        List<Integer> user1Friends = friendshipDao.getFriendList(userId);
        Set<Integer> user1Set = new HashSet<>(user1Friends);
        List<Integer> user2Friends = friendshipDao.getFriendList(friendId);
        Set<Integer> user2Set = new HashSet<>(user2Friends);
        List<User> mutualFriends = new ArrayList<>();
        for (int i : user1Set) {
            if (user2Set.contains(i)) {
                mutualFriends.add(dao.getById(i).get());
            }
        }
        return mutualFriends;

    }

    @Override
    public User getUserFromStorage(int id) {
        if (dao.getById(id).isPresent()) {
            return dao.getById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public boolean checkUser(int id) {
        return dao.getById(id).isPresent();
    }

    @Override
    public List<User> getAllUsers() {
        return dao.getAll();
    }

    public int addUser(User user) {
        return dao.add(user);
    }

    @Override
    public void updateUser(User user) {
        dao.update(user);
    }
}