package com.project.cruit.repository;

import com.project.cruit.domain.UserPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPartRepository extends JpaRepository<UserPart, Long> {
}
