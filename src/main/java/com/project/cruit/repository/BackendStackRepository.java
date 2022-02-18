package com.project.cruit.repository;

import com.project.cruit.domain.stack.BackendStack;
import com.project.cruit.domain.stack.Stack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackendStackRepository extends JpaRepository<BackendStack, Long> {
}
