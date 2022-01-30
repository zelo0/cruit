package com.project.cruit.service;

import com.project.cruit.entity.User;
import com.project.cruit.entity.UserPart;
import com.project.cruit.entity.part.Part;
import com.project.cruit.repository.UserPartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPartService {
    private final UserPartRepository userPartRepository;

    @Transactional
    public void saveUserPart(User user, Part part) {
        userPartRepository.save(new UserPart(user, part));
    }
}
