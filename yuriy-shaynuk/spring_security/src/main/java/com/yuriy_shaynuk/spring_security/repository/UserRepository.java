package com.yuriy_shaynuk.spring_security.repository;

import com.yuriy_shaynuk.spring_security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
