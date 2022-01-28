package com.project.cruit.service;

import com.project.cruit.entity.UserPart;
import com.project.cruit.repository.UserPartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPartService {
    private final UserPartRepository userPartRepository;

    public void saveUserPart(UserPart userPart) {
        userPartRepository.save(userPart);
    }
}
