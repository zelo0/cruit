package com.project.cruit.service;

import com.project.cruit.domain.User;
import com.project.cruit.domain.UserPart;
import com.project.cruit.domain.part.Part;
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

    public Boolean hasPartLeader(Part part) {
        return userPartRepository.countByPartAndIsLeader(part, true) > 0;
    }
}
