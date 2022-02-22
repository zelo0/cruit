package com.project.cruit.service;

import com.project.cruit.domain.Position;
import com.project.cruit.domain.User;
import com.project.cruit.domain.UserStack;
import com.project.cruit.domain.stack.Stack;
import com.project.cruit.exception.EmailExistsException;
import com.project.cruit.exception.NameExistsException;
import com.project.cruit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(User user) {
        if (checkDuplicateEmail(user.getEmail())) {
            throw new EmailExistsException();
        }
        if (checkDuplicateName(user.getName())) {
            throw new NameExistsException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user).getId();
    }

    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    public Boolean checkDuplicateName(String name) {
        return userRepository.existsByName(name);
    }
    public Boolean checkDuplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public String setName(Long userId, String name) {
        User user = userRepository.findById(userId).get();
        user.setName(name);
        return user.getName();
    }

    @Transactional
    public Position setPosition(Long userId, String position) {
        User user = userRepository.findById(userId).get();

        switch (position) {
            case "FRONTEND":
                user.setPosition(Position.FRONTEND);
                break;
            case "BACKEND":
                user.setPosition(Position.BACKEND);
                break;
            case "DESIGN":
                user.setPosition(Position.DESIGN);
                break;
        }
        System.out.println("user = " + user.getPosition().toString());
        return user.getPosition();
    }


    @Transactional
    public Boolean setCanBeLeader(Long userId, Boolean canBeLeader) {
        User user = userRepository.findById(userId).get();
        user.setCanBeLeader(canBeLeader);
        return user.getCanBeLeader();
    }

    @Transactional
    public List<Stack> setMyUserStacks(Long userId, List<Stack> stacks) {
        User user = userRepository.findById(userId).get();
        user.getUserStacks().clear();
        for (Stack stack : stacks) {
            user.getUserStacks().add(new UserStack(user, stack));
        }
        return user.getUserStacks().stream().map(userStack -> userStack.getStack() ).collect(Collectors.toList());
    }
}
