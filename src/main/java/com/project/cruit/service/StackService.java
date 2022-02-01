package com.project.cruit.service;

import com.project.cruit.entity.stack.Stack;
import com.project.cruit.repository.StackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StackService {
    private final StackRepository stackRepository;

    @Transactional
    public void saveStacks(List<Stack> stackList) {
        stackRepository.saveAll(stackList);
    }

    public Stack findByName(String stackName) {
        return stackRepository.findByName(stackName);
    }
}
