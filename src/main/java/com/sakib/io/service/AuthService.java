package com.sakib.io.service;

import com.sakib.io.litespring.annotation.Autowired;
import com.sakib.io.litespring.annotation.Component;
import com.sakib.io.models.User;
import com.sakib.io.repository.UserRepository;

@Component
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    public boolean register(User user) {
        return userRepository.register(user);
    }

    public User login(String username, String password) {
        if(userRepository.passwordMatch(username, password))
            return userRepository.getUser(username);
        return null;
    }

    public User getUser(String username) {
        return userRepository.getUser(username);
    }

    public boolean deleteUser(String username) {
        return userRepository.deleteUser(username);
    }

}
