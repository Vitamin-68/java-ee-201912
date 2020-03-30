package com.devcolibri.secure.service;

import com.devcolibri.secure.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final String SALT = "qwerty123";

    @Override
    public User getUser(String login) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(this.passwordEncoder.encode(login));
        return user;
    }
}
