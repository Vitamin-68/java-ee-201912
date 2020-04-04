package com.yuriy_shaynuk.spring_security.repository;

import com.yuriy_shaynuk.spring_security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
