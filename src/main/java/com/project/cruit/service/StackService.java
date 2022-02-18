package com.project.cruit.service;

import com.project.cruit.domain.Position;
import com.project.cruit.domain.stack.Stack;
import com.project.cruit.repository.BackendStackRepository;
import com.project.cruit.repository.DesignStackRepository;
import com.project.cruit.repository.FrontendStackRepository;
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
    private final FrontendStackRepository frontendStackRepository;
    private final BackendStackRepository backendStackRepository;
    private final DesignStackRepository designStackRepository;

    @Transactional
    public void saveStacks(List<Stack> stackList) {
        stackRepository.saveAll(stackList);
    }

    public Stack findByName(String stackName) {
        return stackRepository.findByName(stackName);
    }

    public List<? extends Stack> findAllByPosition(Position position) {
        List<? extends Stack> result = null;
        switch (position) {
            case FRONTEND:
                result =  frontendStackRepository.findAll();
                break;
            case BACKEND:
                result =   backendStackRepository.findAll();
                break;
            case DESIGN:
                result =   designStackRepository.findAll();
                break;
        }

        return result;

    }
}
