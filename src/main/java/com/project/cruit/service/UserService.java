package com.project.cruit.service;

import com.project.cruit.domain.Position;
import com.project.cruit.domain.User;
import com.project.cruit.domain.UserStack;
import com.project.cruit.domain.stack.Stack;
import com.project.cruit.exception.EmailExistsException;
import com.project.cruit.exception.NameExistsException;
import com.project.cruit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        // 다른 포지션으로 바꾸는 경우 사용할 수 있는 스택(userStack) 리스트 비우기
        if (!user.getPosition().name().equals(position)) {
            user.getUserStacks().clear();
        }
        
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
        
        
        System.out.println("user = " + user.getPosition().name());
        return user.getPosition();
    }


    @Transactional
    public Boolean setCanBeLeader(Long userId, Boolean canBeLeader) {
        User user = userRepository.findById(userId).get();
        user.setCanBeLeader(canBeLeader);
        return user.getCanBeLeader();
    }

    @Transactional
    public List<Stack> setUserStacks(Long userId, List<Stack> stacks) {
        User user = userRepository.findById(userId).get();
        user.getUserStacks().clear();
        for (Stack stack : stacks) {
            user.getUserStacks().add(new UserStack(user, stack));
        }
        return user.getUserStacks().stream().map(userStack -> userStack.getStack() ).collect(Collectors.toList());
    }

    @Transactional
    public String setGithub(Long userId, String github) {
        User user = userRepository.findById(userId).get();
        user.setGithub(github);
        return user.getGithub();
    }

    @Transactional
    public String setIntroduction(Long userId, String introduction) {
        User user = userRepository.findById(userId).get();
        user.setIntroduction(introduction);
        return user.getIntroduction();
    }

    public Page<User> findByNoStackFilter(Pageable pageable, String leaderFilter) {
        if (leaderFilter.equals("all")) {
            return userRepository.findAll(pageable);
        }
        else {
            return userRepository.findByCanBeLeader(Boolean.parseBoolean(leaderFilter), pageable);
        }
    }

    public Page<User> findByStackFilter(List<String> stackFilterList, String leaderFilter, Pageable pageable) {
        if (leaderFilter.equals("all")) {
            return userRepository.findByStackFilter(stackFilterList, pageable);
        }
        else {
            return userRepository.findByStackFilterAndCanBeLeader(stackFilterList, Boolean.parseBoolean(leaderFilter), pageable);
        }
    }

    public User findByName(String name) {
        return userRepository.findByName(name);
    }
}
