package com.project.cruit.service;

import com.project.cruit.domain.Position;
import com.project.cruit.domain.User;
import com.project.cruit.domain.UserStack;
import com.project.cruit.domain.stack.Stack;
import com.project.cruit.dto.JoinRequestDto;
import com.project.cruit.exception.EmailExistsException;
import com.project.cruit.exception.InvalidPageOffsetException;
import com.project.cruit.exception.NameExistsException;
import com.project.cruit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User join(JoinRequestDto joinRequest) {
        if (checkDuplicateEmail(joinRequest.getEmail())) {
            throw new EmailExistsException();
        }
        if (checkDuplicateName(joinRequest.getName())) {
            throw new NameExistsException();
        }

        Position position = null;
        switch (joinRequest.getPosition()) {
            case "FRONTEND":
                position = Position.FRONTEND;
                break;
            case "BACKEND":
                position = Position.BACKEND;
                break;
            case "DESIGN":
                position = Position.DESIGN;
                break;
        }

        User user = new User(joinRequest.getEmail(), passwordEncoder.encode(joinRequest.getPassword()), joinRequest.getName(), position.name());
        return userRepository.save(user);
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
    public void setProfile(Long userId, String filePath) {
        User user = userRepository.findById(userId).get();
        user.setProfile(filePath);
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


    public Page<User> findPageByStackAndLeader(PageRequest pageRequest, String stackFilter, String leaderFilter, int page) {
        Page<User> users;
        if (stackFilter.isBlank()) {
            // 굳이 조인할 필요 없으니 if문으로 분기
            users =  findByNoStackFilter(pageRequest, leaderFilter);
        } else {
            List<String> stackFilterList = List.of(stackFilter.split(";"));
            users = findByStackFilter(stackFilterList, leaderFilter, pageRequest);
        }

        // page offset이 너무 크면 에러
        if ((users.getTotalPages() != 0 && users.getTotalPages() <= page) || (users.getTotalPages() == 0 && page > 0)) {
            throw new InvalidPageOffsetException();
        }

        return users;
    }
}
