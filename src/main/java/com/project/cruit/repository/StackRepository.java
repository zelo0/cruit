package com.project.cruit.repository;

import com.project.cruit.entity.stack.Stack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StackRepository extends JpaRepository<Stack, Long> {
}
