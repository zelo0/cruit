package com.project.cruit.repository;

import com.project.cruit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByName(String name);
    Boolean existsByEmail(String email);
}
