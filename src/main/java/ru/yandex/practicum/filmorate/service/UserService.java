package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(int userId, int friendId) {
        User user1 = storage.getUserById(userId);
        User user2 = storage.getUserById(friendId);
        user1.addFriend(friendId);
        user2.addFriend(userId);
        storage.updateUser(user1);
        storage.updateUser(user2);
    }

    public void deleteFriend(int userId, int friendId) {
        User user1 = storage.getUserById(userId);
        User user2 = storage.getUserById(friendId);
        user1.removeFriend(friendId);
        user2.removeFriend(userId);
        storage.updateUser(user1);
        storage.updateUser(user2);
    }

    public List<User> returnFriendList(int id) {
        Set<Integer> friendSet = storage.getUserById(id).getFriendList();
        ArrayList<User> friendList = new ArrayList<>();
        for (int i : friendSet) {
            friendList.add(storage.getUserById(i));
        }
        return friendList;
    }

    public List<User> findMutualFriends(int userId, int friendId) {
        List<User> mutualFriends = new ArrayList<>();
        User user1 = storage.getUserById(userId);
        User user2 = storage.getUserById(friendId);
        Set<Integer> user1FriendList = user1.getFriendList();
        Set<Integer> user2FriendList = user2.getFriendList();
        for (int i : user1FriendList) {
            if (user2FriendList.contains(i)) {
                mutualFriends.add(storage.getUserById(i));
            }
        }
        return mutualFriends;
    }
}
