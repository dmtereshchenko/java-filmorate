package ru.yandex.practicum.filmorate.service.InMemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.inmemory.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class InMemoryUserService implements UserService {

    private final InMemoryUserStorage storage;

    @Autowired
    public InMemoryUserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(int userId, int friendId) {
        User user1 = storage.getById(userId);
        User user2 = storage.getById(friendId);
        user1.addFriend(friendId);
        user2.addFriend(userId);
        storage.update(user1);
        storage.update(user2);
    }

    public void deleteFriend(int userId, int friendId) {
        User user1 = storage.getById(userId);
        User user2 = storage.getById(friendId);
        user1.removeFriend(friendId);
        user2.removeFriend(userId);
        storage.update(user1);
        storage.update(user2);
    }

    public List<User> returnFriendList(int id) {
        User user = storage.getById(id);
        Set<Integer> friendSet = user.getFriendList();
        ArrayList<User> friendList = new ArrayList<>();
        for (int i : friendSet) {
            friendList.add(storage.getById(i));
        }
        return friendList;
    }

    public List<User> findMutualFriends(int userId, int friendId) {
        List<User> mutualFriends = new ArrayList<>();
        User user1 = storage.getById(userId);
        User user2 = storage.getById(friendId);
        Set<Integer> user1FriendList = user1.getFriendList();
        Set<Integer> user2FriendList = user2.getFriendList();
        for (int i : user1FriendList) {
            if (user2FriendList.contains(i)) {
                mutualFriends.add(storage.getById(i));
            }
        }
        return mutualFriends;
    }

    public User getUserFromStorage(int id) {
        return storage.getById(id);
    }

    public boolean checkUser(int id) {
        return storage.exist(id);
    }

    public List<User> getAllUsers() {
        return storage.getAll();
    }

    public int addUser(User user) {
        storage.add(user);
        return storage.generateId();
    }

    public void updateUser(User user) {
        storage.update(user);
    }
}
