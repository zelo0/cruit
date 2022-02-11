package com.project.cruit.repository;

import com.project.cruit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByName(String name);
    Boolean existsByEmail(String email);
}
