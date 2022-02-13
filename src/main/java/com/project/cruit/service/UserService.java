package com.project.cruit.service;

import com.project.cruit.domain.User;
import com.project.cruit.exception.EmailExistsException;
import com.project.cruit.exception.NameExistsException;
import com.project.cruit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}
