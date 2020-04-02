package com.yuriy_shaynuk.spring_security.service;

import com.yuriy_shaynuk.spring_security.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
