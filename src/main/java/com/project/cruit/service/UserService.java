package com.project.cruit.service;

import com.project.cruit.entity.User;
import com.project.cruit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Long join(User user) {
        if (checkDuplicateEmail(user.getEmail())) {
            throw new IllegalStateException("The email already exists");
        }
        if (checkDuplicateName(user.getName())) {
            throw new IllegalStateException("The name already exists");
        }

        return userRepository.save(user).getId();
    }

    public Boolean checkDuplicateName(String name) {
        return userRepository.existsByName(name);
    }
    public Boolean checkDuplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }


}
