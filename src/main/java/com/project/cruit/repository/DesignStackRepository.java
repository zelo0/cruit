package com.project.cruit.repository;

import com.project.cruit.domain.stack.DesignStack;
import com.project.cruit.domain.stack.Stack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignStackRepository extends JpaRepository<DesignStack, Long> {
}
