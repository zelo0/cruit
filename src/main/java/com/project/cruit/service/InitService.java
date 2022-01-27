package com.project.cruit.service;

import com.project.cruit.entity.stack.FrontendStack;
import com.project.cruit.entity.stack.Stack;
import com.project.cruit.repository.StackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class InitService {

    private final StackRepository stackRepository;

    public void initStack() {
        Stack s1 = new FrontendStack("리액트", "www/react.blabla");
        stackRepository.save(s1);
    }
}
